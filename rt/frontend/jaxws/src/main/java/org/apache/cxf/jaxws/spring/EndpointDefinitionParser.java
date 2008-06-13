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
name|jaxws
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
name|Attr
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
name|NamedNodeMap
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
name|BusFactory
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|jaxws
operator|.
name|EndpointImpl
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
name|FatalBeanException
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
name|EndpointDefinitionParser
extends|extends
name|AbstractBeanDefinitionParser
block|{
specifier|private
specifier|static
specifier|final
name|String
name|IMPLEMENTOR
init|=
literal|"implementor"
decl_stmt|;
specifier|public
name|EndpointDefinitionParser
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|setBeanClass
argument_list|(
name|SpringEndpointImpl
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getSuffix
parameter_list|()
block|{
return|return
literal|".jaxws-endpoint"
return|;
block|}
annotation|@
name|Override
specifier|protected
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
name|boolean
name|isAbstract
init|=
literal|false
decl_stmt|;
name|NamedNodeMap
name|atts
init|=
name|element
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|atts
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Attr
name|node
init|=
operator|(
name|Attr
operator|)
name|atts
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|val
init|=
name|node
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|String
name|pre
init|=
name|node
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|node
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"createdFromAPI"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|bean
operator|.
name|setAbstract
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|isAbstract
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isAttribute
argument_list|(
name|pre
argument_list|,
name|name
argument_list|)
operator|&&
operator|!
literal|"publish"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|&&
operator|!
literal|"bus"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"endpointName"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"serviceName"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|QName
name|q
init|=
name|parseQName
argument_list|(
name|element
argument_list|,
name|val
argument_list|)
decl_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
name|name
argument_list|,
name|q
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"depends-on"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|bean
operator|.
name|addDependsOn
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|IMPLEMENTOR
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|loadImplementor
argument_list|(
name|bean
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
literal|"name"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|mapToProperty
argument_list|(
name|bean
argument_list|,
name|name
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"abstract"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|bean
operator|.
name|setAbstract
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|isAbstract
operator|=
literal|true
expr_stmt|;
block|}
block|}
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
literal|"properties"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|Map
name|map
init|=
name|ctx
operator|.
name|getDelegate
argument_list|()
operator|.
name|parseMapElement
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
literal|"properties"
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"binding"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|setFirstChildAsProperty
argument_list|(
operator|(
name|Element
operator|)
name|n
argument_list|,
name|ctx
argument_list|,
name|bean
argument_list|,
literal|"bindingConfig"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"inInterceptors"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"inFaultInterceptors"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"outInterceptors"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"outFaultInterceptors"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"features"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"schemaLocations"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"handlers"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
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
name|name
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|IMPLEMENTOR
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|ctx
operator|.
name|getDelegate
argument_list|()
operator|.
name|parseConstructorArgElement
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
expr_stmt|;
block|}
else|else
block|{
name|setFirstChildAsProperty
argument_list|(
operator|(
name|Element
operator|)
name|n
argument_list|,
name|ctx
argument_list|,
name|bean
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|isAbstract
condition|)
block|{
name|bean
operator|.
name|setInitMethodName
argument_list|(
literal|"publish"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setDestroyMethodName
argument_list|(
literal|"stop"
argument_list|)
expr_stmt|;
block|}
comment|// We don't want to delay the registration of our Server
name|bean
operator|.
name|setLazyInit
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|loadImplementor
parameter_list|(
name|BeanDefinitionBuilder
name|bean
parameter_list|,
name|String
name|val
parameter_list|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|val
argument_list|)
condition|)
block|{
if|if
condition|(
name|val
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
name|bean
operator|.
name|addConstructorArgReference
argument_list|(
name|val
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|Object
name|obj
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|val
argument_list|,
name|getClass
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|bean
operator|.
name|addConstructorArg
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|FatalBeanException
argument_list|(
literal|"Could not load class: "
operator|+
name|val
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
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
name|id
operator|=
name|EndpointImpl
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"--"
operator|+
name|definition
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
return|return
name|id
return|;
block|}
specifier|public
specifier|static
class|class
name|SpringEndpointImpl
extends|extends
name|EndpointImpl
implements|implements
name|ApplicationContextAware
block|{
specifier|public
name|SpringEndpointImpl
parameter_list|(
name|Object
name|implementor
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|Bus
operator|)
literal|null
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SpringEndpointImpl
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|Object
name|implementor
parameter_list|)
block|{
name|super
argument_list|(
name|bus
argument_list|,
name|implementor
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
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
decl_stmt|;
name|BusWiringBeanFactoryPostProcessor
operator|.
name|updateBusReferencesInContext
argument_list|(
name|bus
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

