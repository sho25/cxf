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
name|http_jetty
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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|NodeList
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
name|jsse
operator|.
name|spring
operator|.
name|TLSServerParametersConfig
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
name|security
operator|.
name|TLSServerParametersType
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
name|transport
operator|.
name|http_jetty
operator|.
name|JettyHTTPServerEngine
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
name|http_jetty
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
name|http_jetty
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
name|http_jetty
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
name|apache
operator|.
name|cxf
operator|.
name|transports
operator|.
name|http_jetty
operator|.
name|configuration
operator|.
name|ThreadingParametersType
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
name|MutablePropertyValues
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
name|PropertyValue
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
name|xml
operator|.
name|ParserContext
import|;
end_import

begin_class
specifier|public
class|class
name|JettyHTTPServerEngineBeanDefinitionParser
extends|extends
name|AbstractBeanDefinitionParser
block|{
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
name|String
name|portStr
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"port"
argument_list|)
decl_stmt|;
name|int
name|port
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|portStr
argument_list|)
decl_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"port"
argument_list|,
name|port
argument_list|)
expr_stmt|;
name|MutablePropertyValues
name|engineFactoryProperties
init|=
name|ctx
operator|.
name|getContainingBeanDefinition
argument_list|()
operator|.
name|getPropertyValues
argument_list|()
decl_stmt|;
name|PropertyValue
name|busValue
init|=
name|engineFactoryProperties
operator|.
name|getPropertyValue
argument_list|(
literal|"bus"
argument_list|)
decl_stmt|;
comment|// get the property value from paranets
try|try
block|{
name|NodeList
name|children
init|=
name|element
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|children
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|n
init|=
name|children
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|.
name|getNodeType
argument_list|()
operator|==
name|Node
operator|.
name|ELEMENT_NODE
condition|)
block|{
name|String
name|name
init|=
name|n
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"tlsServerParameters"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|TLSServerParametersType
name|parametersType
init|=
name|JAXBHelper
operator|.
name|parseElement
argument_list|(
operator|(
name|Element
operator|)
name|n
argument_list|,
name|bean
argument_list|,
name|TLSServerParametersType
operator|.
name|class
argument_list|)
decl_stmt|;
name|TLSServerParametersConfig
name|param
init|=
operator|new
name|TLSServerParametersConfig
argument_list|(
name|parametersType
argument_list|)
decl_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"tlsServerParameters"
argument_list|,
name|param
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"tlsServerParametersRef"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|TLSServerParametersIdentifiedType
name|parameterTypeRef
init|=
name|JAXBHelper
operator|.
name|parseElement
argument_list|(
operator|(
name|Element
operator|)
name|n
argument_list|,
name|bean
argument_list|,
name|TLSServerParametersIdentifiedType
operator|.
name|class
argument_list|)
decl_stmt|;
name|TLSServerParameters
name|param
init|=
name|getTlsServerParameters
argument_list|(
name|engineFactoryProperties
argument_list|,
name|parameterTypeRef
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"tlsServerParameters"
argument_list|,
name|param
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"threadingParameters"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|ThreadingParametersType
name|parametersType
init|=
name|JAXBHelper
operator|.
name|parseElement
argument_list|(
operator|(
name|Element
operator|)
name|n
argument_list|,
name|bean
argument_list|,
name|ThreadingParametersType
operator|.
name|class
argument_list|)
decl_stmt|;
name|ThreadingParameters
name|param
init|=
name|toThreadingParameters
argument_list|(
name|parametersType
argument_list|)
decl_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"threadingParameters"
argument_list|,
name|param
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"threadingParametersRef"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|ThreadingParametersIdentifiedType
name|parametersTypeRef
init|=
name|JAXBHelper
operator|.
name|parseElement
argument_list|(
operator|(
name|Element
operator|)
name|n
argument_list|,
name|bean
argument_list|,
name|ThreadingParametersIdentifiedType
operator|.
name|class
argument_list|)
decl_stmt|;
name|ThreadingParameters
name|param
init|=
name|getThreadingParameters
argument_list|(
name|engineFactoryProperties
argument_list|,
name|parametersTypeRef
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"threadingParameters"
argument_list|,
name|param
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"connector"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
comment|// only deal with the one connector here
name|List
name|list
init|=
name|ctx
operator|.
name|getDelegate
argument_list|()
operator|.
name|parseListElement
argument_list|(
operator|(
name|Element
operator|)
name|n
argument_list|,
name|bean
operator|.
name|getBeanDefinition
argument_list|()
argument_list|)
decl_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"connector"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"handlers"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|List
name|handlers
init|=
name|ctx
operator|.
name|getDelegate
argument_list|()
operator|.
name|parseListElement
argument_list|(
operator|(
name|Element
operator|)
name|n
argument_list|,
name|bean
operator|.
name|getBeanDefinition
argument_list|()
argument_list|)
decl_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"handlers"
argument_list|,
name|handlers
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"sessionSupport"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"reuseAddress"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|String
name|text
init|=
name|n
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
name|name
argument_list|,
name|Boolean
operator|.
name|valueOf
argument_list|(
name|text
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
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
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"bus"
argument_list|,
name|busValue
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setLazyInit
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|private
name|TLSServerParameters
name|getTlsServerParameters
parameter_list|(
name|MutablePropertyValues
name|engineFactoryProperties
parameter_list|,
name|String
name|reference
parameter_list|)
block|{
name|TLSServerParameters
name|result
init|=
literal|null
decl_stmt|;
name|PropertyValue
name|tlsParameterMapValue
init|=
name|engineFactoryProperties
operator|.
name|getPropertyValue
argument_list|(
literal|"tlsServerParametersMap"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|tlsParameterMapValue
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not find the tlsServerParametersMap "
operator|+
literal|"from the JettyHTTPServerEngineFactory!"
argument_list|)
throw|;
block|}
else|else
block|{
name|Map
name|tlsServerParametersMap
init|=
operator|(
name|Map
operator|)
name|tlsParameterMapValue
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|result
operator|=
operator|(
name|TLSServerParameters
operator|)
name|tlsServerParametersMap
operator|.
name|get
argument_list|(
name|reference
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not find the tlsServerParametersMap reference ["
operator|+
name|reference
operator|+
literal|"]'s mapping tlsParameter"
argument_list|)
throw|;
block|}
block|}
return|return
name|result
return|;
block|}
specifier|private
name|ThreadingParameters
name|getThreadingParameters
parameter_list|(
name|MutablePropertyValues
name|engineFactoryProperties
parameter_list|,
name|String
name|reference
parameter_list|)
block|{
name|ThreadingParameters
name|result
init|=
literal|null
decl_stmt|;
name|PropertyValue
name|threadingParametersMapValue
init|=
name|engineFactoryProperties
operator|.
name|getPropertyValue
argument_list|(
literal|"threadingParametersMap"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|threadingParametersMapValue
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not find the threadingParametersMap "
operator|+
literal|"from the JettyHTTPServerEngineFactory!"
argument_list|)
throw|;
block|}
else|else
block|{
name|Map
name|threadingParametersMap
init|=
operator|(
name|Map
operator|)
name|threadingParametersMapValue
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|result
operator|=
operator|(
name|ThreadingParameters
operator|)
name|threadingParametersMap
operator|.
name|get
argument_list|(
name|reference
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not find the threadingParametersMap reference ["
operator|+
name|reference
operator|+
literal|"]'s mapping threadingParameters"
argument_list|)
throw|;
block|}
block|}
return|return
name|result
return|;
block|}
specifier|private
name|ThreadingParameters
name|toThreadingParameters
parameter_list|(
name|ThreadingParametersType
name|paramtype
parameter_list|)
block|{
name|ThreadingParameters
name|params
init|=
operator|new
name|ThreadingParameters
argument_list|()
decl_stmt|;
name|params
operator|.
name|setMaxThreads
argument_list|(
name|paramtype
operator|.
name|getMaxThreads
argument_list|()
argument_list|)
expr_stmt|;
name|params
operator|.
name|setMinThreads
argument_list|(
name|paramtype
operator|.
name|getMinThreads
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|params
return|;
block|}
comment|/*      * We do not require an id from the configuration.      *       * (non-Javadoc)      * @see org.springframework.beans.factory.xml.AbstractBeanDefinitionParser#shouldGenerateId()      */
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
name|getBeanClass
parameter_list|(
name|Element
name|arg0
parameter_list|)
block|{
return|return
name|JettyHTTPServerEngine
operator|.
name|class
return|;
block|}
block|}
end_class

end_unit

