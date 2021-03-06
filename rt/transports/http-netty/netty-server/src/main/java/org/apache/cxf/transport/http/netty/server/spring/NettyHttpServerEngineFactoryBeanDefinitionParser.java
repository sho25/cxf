begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|http
operator|.
name|netty
operator|.
name|server
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|bus
operator|.
name|spring
operator|.
name|BusWiringBeanFactoryPostProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|injection
operator|.
name|NoJSR250Annotations
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|configuration
operator|.
name|jsse
operator|.
name|TLSServerParameters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|configuration
operator|.
name|spring
operator|.
name|AbstractBeanDefinitionParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|configuration
operator|.
name|spring
operator|.
name|BusWiringType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|DOMUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|http
operator|.
name|netty
operator|.
name|server
operator|.
name|NettyHttpServerEngineFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|http
operator|.
name|netty
operator|.
name|server
operator|.
name|ThreadingParameters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transports
operator|.
name|http_netty_server
operator|.
name|configuration
operator|.
name|TLSServerParametersIdentifiedType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transports
operator|.
name|http_netty_server
operator|.
name|configuration
operator|.
name|ThreadingParametersIdentifiedType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|BeansException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|BeanDefinitionStoreException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|support
operator|.
name|AbstractBeanDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|support
operator|.
name|BeanDefinitionBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|support
operator|.
name|ManagedList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|ParserContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContextAware
import|;
end_import

begin_class
specifier|public
class|class
name|NettyHttpServerEngineFactoryBeanDefinitionParser
extends|extends
name|AbstractBeanDefinitionParser
block|{
specifier|static
specifier|final
name|String
name|HTTP_NETTY_SERVER_NS
init|=
literal|"http://cxf.apache.org/transports/http-netty-server/configuration"
decl_stmt|;
specifier|protected
name|String
name|resolveId
parameter_list|(
name|Element
name|elem
parameter_list|,
name|AbstractBeanDefinition
name|definition
parameter_list|,
name|ParserContext
name|ctx
parameter_list|)
throws|throws
name|BeanDefinitionStoreException
block|{
name|String
name|id
init|=
name|this
operator|.
name|getIdOrName
argument_list|(
name|elem
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|id
argument_list|)
condition|)
block|{
return|return
name|NettyHttpServerEngineFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
name|id
operator|=
name|super
operator|.
name|resolveId
argument_list|(
name|elem
argument_list|,
name|definition
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|ctx
operator|.
name|getRegistry
argument_list|()
operator|.
name|containsBeanDefinition
argument_list|(
name|NettyHttpServerEngineFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|ctx
operator|.
name|getRegistry
argument_list|()
operator|.
name|registerAlias
argument_list|(
name|id
argument_list|,
name|NettyHttpServerEngineFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|id
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|doParse
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|ctx
parameter_list|,
name|BeanDefinitionBuilder
name|bean
parameter_list|)
block|{
comment|//bean.setAbstract(true);
name|String
name|bus
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"bus"
argument_list|)
decl_stmt|;
name|BeanDefinitionBuilder
name|factbean
init|=
name|BeanDefinitionBuilder
operator|.
name|rootBeanDefinition
argument_list|(
name|NettySpringTypesFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|ctx
operator|.
name|getRegistry
argument_list|()
operator|.
name|registerBeanDefinition
argument_list|(
name|NettySpringTypesFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|factbean
operator|.
name|getBeanDefinition
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|bus
argument_list|)
condition|)
block|{
name|addBusWiringAttribute
argument_list|(
name|bean
argument_list|,
name|BusWiringType
operator|.
name|CONSTRUCTOR
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bean
operator|.
name|addConstructorArgReference
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
name|bean
operator|.
name|addConstructorArgValue
argument_list|(
name|mapElementToJaxbBean
argument_list|(
name|element
argument_list|,
name|TLSServerParametersIdentifiedType
operator|.
name|class
argument_list|,
name|NettySpringTypesFactory
operator|.
name|class
argument_list|,
literal|"createTLSServerParametersMap"
argument_list|)
argument_list|)
expr_stmt|;
name|bean
operator|.
name|addConstructorArgValue
argument_list|(
name|mapElementToJaxbBean
argument_list|(
name|element
argument_list|,
name|ThreadingParametersIdentifiedType
operator|.
name|class
argument_list|,
name|NettySpringTypesFactory
operator|.
name|class
argument_list|,
literal|"createThreadingParametersMap"
argument_list|)
argument_list|)
expr_stmt|;
comment|// parser the engine list
name|List
argument_list|<
name|Object
argument_list|>
name|list
init|=
name|getRequiredElementsList
argument_list|(
name|element
argument_list|,
name|ctx
argument_list|,
operator|new
name|QName
argument_list|(
name|HTTP_NETTY_SERVER_NS
argument_list|,
literal|"engine"
argument_list|)
argument_list|,
name|bean
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"enginesList"
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not process configuration."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|getRequiredElementsList
parameter_list|(
name|Element
name|parent
parameter_list|,
name|ParserContext
name|ctx
parameter_list|,
name|QName
name|name
parameter_list|,
name|BeanDefinitionBuilder
name|bean
parameter_list|)
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|elemList
init|=
name|DOMUtils
operator|.
name|findAllElementsByTagNameNS
argument_list|(
name|parent
argument_list|,
name|name
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|name
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
name|ManagedList
argument_list|<
name|Object
argument_list|>
name|list
init|=
operator|new
name|ManagedList
argument_list|<>
argument_list|(
name|elemList
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|list
operator|.
name|setSource
argument_list|(
name|ctx
operator|.
name|extractSource
argument_list|(
name|parent
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Element
name|elem
range|:
name|elemList
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|ctx
operator|.
name|getDelegate
argument_list|()
operator|.
name|parsePropertySubElement
argument_list|(
name|elem
argument_list|,
name|bean
operator|.
name|getBeanDefinition
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
comment|/*      * We do not require an id from the configuration.      *      * (non-Javadoc)      * @see org.springframework.beans.factory.xml.AbstractBeanDefinitionParser#shouldGenerateId()      */
annotation|@
name|Override
specifier|protected
name|boolean
name|shouldGenerateId
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|getBeanClass
parameter_list|(
name|Element
name|arg0
parameter_list|)
block|{
return|return
name|SpringNettyHttpServerEngineFactory
operator|.
name|class
return|;
block|}
annotation|@
name|NoJSR250Annotations
argument_list|(
name|unlessNull
operator|=
literal|"bus"
argument_list|)
specifier|public
specifier|static
class|class
name|SpringNettyHttpServerEngineFactory
extends|extends
name|NettyHttpServerEngineFactory
implements|implements
name|ApplicationContextAware
block|{
specifier|public
name|SpringNettyHttpServerEngineFactory
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|SpringNettyHttpServerEngineFactory
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|TLSServerParameters
argument_list|>
name|tls
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|ThreadingParameters
argument_list|>
name|threads
parameter_list|)
block|{
name|super
argument_list|(
name|bus
argument_list|,
name|tls
argument_list|,
name|threads
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|ctx
parameter_list|)
throws|throws
name|BeansException
block|{
if|if
condition|(
name|getBus
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setBus
argument_list|(
name|BusWiringBeanFactoryPostProcessor
operator|.
name|addDefaultBus
argument_list|(
name|ctx
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

