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
name|jaxrs
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|Provider
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
name|util
operator|.
name|ClasspathScanner
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
name|jaxrs
operator|.
name|JAXRSServerFactoryBean
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
name|jaxrs
operator|.
name|JAXRSServiceFactoryBean
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
name|jaxrs
operator|.
name|lifecycle
operator|.
name|ResourceProvider
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
name|jaxrs
operator|.
name|model
operator|.
name|UserResource
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
name|jaxrs
operator|.
name|utils
operator|.
name|ResourceUtils
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
name|BeanCreationException
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
name|JAXRSServerFactoryBeanDefinitionParser
extends|extends
name|AbstractBeanDefinitionParser
block|{
specifier|public
name|JAXRSServerFactoryBeanDefinitionParser
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|setBeanClass
argument_list|(
name|SpringJAXRSServerFactoryBean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|mapAttribute
parameter_list|(
name|BeanDefinitionBuilder
name|bean
parameter_list|,
name|Element
name|e
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|val
parameter_list|)
block|{
if|if
condition|(
literal|"beanNames"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|String
index|[]
name|values
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|val
argument_list|,
literal|" "
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|SpringResourceFactory
argument_list|>
name|tempFactories
init|=
operator|new
name|ArrayList
argument_list|<
name|SpringResourceFactory
argument_list|>
argument_list|(
name|values
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|v
range|:
name|values
control|)
block|{
name|String
name|theValue
init|=
name|v
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|theValue
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|tempFactories
operator|.
name|add
argument_list|(
operator|new
name|SpringResourceFactory
argument_list|(
name|theValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"tempResourceProviders"
argument_list|,
name|tempFactories
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
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
name|e
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
literal|"base-packages"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
specifier|final
name|String
index|[]
name|values
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|val
argument_list|,
literal|","
argument_list|)
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|basePackages
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|values
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|String
name|value
range|:
name|values
control|)
block|{
specifier|final
name|String
name|trimmed
init|=
name|value
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|trimmed
operator|.
name|equals
argument_list|(
name|ClasspathScanner
operator|.
name|ALL_PACKAGES
argument_list|)
condition|)
block|{
name|basePackages
operator|.
name|clear
argument_list|()
expr_stmt|;
name|basePackages
operator|.
name|add
argument_list|(
name|trimmed
argument_list|)
expr_stmt|;
break|break;
block|}
elseif|else
if|if
condition|(
name|trimmed
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|basePackages
operator|.
name|add
argument_list|(
name|trimmed
argument_list|)
expr_stmt|;
block|}
block|}
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"basePackages"
argument_list|,
name|basePackages
argument_list|)
expr_stmt|;
block|}
else|else
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
annotation|@
name|Override
specifier|protected
name|void
name|mapElement
parameter_list|(
name|ParserContext
name|ctx
parameter_list|,
name|BeanDefinitionBuilder
name|bean
parameter_list|,
name|Element
name|el
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
literal|"properties"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"extensionMappings"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"languageMappings"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
init|=
name|ctx
operator|.
name|getDelegate
argument_list|()
operator|.
name|parseMapElement
argument_list|(
name|el
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
name|map
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"executor"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|setFirstChildAsProperty
argument_list|(
name|el
argument_list|,
name|ctx
argument_list|,
name|bean
argument_list|,
literal|"serviceFactory.executor"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"invoker"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|setFirstChildAsProperty
argument_list|(
name|el
argument_list|,
name|ctx
argument_list|,
name|bean
argument_list|,
literal|"serviceFactory.invoker"
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
name|el
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
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|ctx
operator|.
name|getDelegate
argument_list|()
operator|.
name|parseListElement
argument_list|(
name|el
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
literal|"providers"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"serviceBeans"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"modelBeans"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|ctx
operator|.
name|getDelegate
argument_list|()
operator|.
name|parseListElement
argument_list|(
name|el
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
literal|"serviceFactories"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|ctx
operator|.
name|getDelegate
argument_list|()
operator|.
name|parseListElement
argument_list|(
name|el
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
literal|"resourceProviders"
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"model"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|UserResource
argument_list|>
name|resources
init|=
name|ResourceUtils
operator|.
name|getResourcesFromElement
argument_list|(
name|el
argument_list|)
decl_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"modelBeans"
argument_list|,
name|resources
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setFirstChildAsProperty
argument_list|(
name|el
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
name|super
operator|.
name|doParse
argument_list|(
name|element
argument_list|,
name|ctx
argument_list|,
name|bean
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setInitMethodName
argument_list|(
literal|"create"
argument_list|)
expr_stmt|;
comment|// We don't really want to delay the registration of our Server
name|bean
operator|.
name|setLazyInit
argument_list|(
literal|false
argument_list|)
expr_stmt|;
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
name|getBeanClass
argument_list|()
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
annotation|@
name|Override
specifier|protected
name|boolean
name|hasBusProperty
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
specifier|static
class|class
name|SpringJAXRSServerFactoryBean
extends|extends
name|JAXRSServerFactoryBean
implements|implements
name|ApplicationContextAware
block|{
specifier|private
name|List
argument_list|<
name|SpringResourceFactory
argument_list|>
name|tempFactories
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|basePackages
decl_stmt|;
specifier|private
name|ApplicationContext
name|context
decl_stmt|;
specifier|public
name|SpringJAXRSServerFactoryBean
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|SpringJAXRSServerFactoryBean
parameter_list|(
name|JAXRSServiceFactoryBean
name|sf
parameter_list|)
block|{
name|super
argument_list|(
name|sf
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setBasePackages
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|basePackages
parameter_list|)
block|{
name|this
operator|.
name|basePackages
operator|=
name|basePackages
expr_stmt|;
block|}
specifier|public
name|void
name|setTempResourceProviders
parameter_list|(
name|List
argument_list|<
name|SpringResourceFactory
argument_list|>
name|providers
parameter_list|)
block|{
name|tempFactories
operator|=
name|providers
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
name|this
operator|.
name|context
operator|=
name|ctx
expr_stmt|;
if|if
condition|(
name|tempFactories
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|ResourceProvider
argument_list|>
name|factories
init|=
operator|new
name|ArrayList
argument_list|<
name|ResourceProvider
argument_list|>
argument_list|(
name|tempFactories
operator|.
name|size
argument_list|()
argument_list|)
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
name|tempFactories
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|SpringResourceFactory
name|factory
init|=
name|tempFactories
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|factory
operator|.
name|setApplicationContext
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|factories
operator|.
name|add
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
name|tempFactories
operator|.
name|clear
argument_list|()
expr_stmt|;
name|super
operator|.
name|setResourceProviders
argument_list|(
name|factories
argument_list|)
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
name|basePackages
operator|!=
literal|null
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|classes
init|=
name|ClasspathScanner
operator|.
name|findClasses
argument_list|(
name|basePackages
argument_list|,
name|Provider
operator|.
name|class
argument_list|,
name|Path
operator|.
name|class
argument_list|)
decl_stmt|;
name|this
operator|.
name|setProviders
argument_list|(
name|createBeans
argument_list|(
name|classes
operator|.
name|get
argument_list|(
name|Provider
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|setServiceBeans
argument_list|(
name|createBeans
argument_list|(
name|classes
operator|.
name|get
argument_list|(
name|Path
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|BeanDefinitionStoreException
argument_list|(
literal|"I/O failure during classpath scanning"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|BeanCreationException
argument_list|(
literal|"Failed to create bean from classfile"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
if|if
condition|(
name|bus
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
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|createBeans
parameter_list|(
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
range|:
name|classes
control|)
block|{
name|providers
operator|.
name|add
argument_list|(
name|context
operator|.
name|getAutowireCapableBeanFactory
argument_list|()
operator|.
name|createBean
argument_list|(
name|clazz
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|providers
return|;
block|}
block|}
block|}
end_class

end_unit

