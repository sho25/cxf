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
name|cdi
package|;
end_package

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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|ServiceLoader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|event
operator|.
name|Observes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|AfterBeanDiscovery
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|AfterDeploymentValidation
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|AnnotatedType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|BeanManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|Extension
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|InjectionTarget
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|ProcessBean
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
name|ApplicationPath
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
name|core
operator|.
name|Application
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
name|MessageBodyReader
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
name|MessageBodyWriter
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
name|utils
operator|.
name|ResourceUtils
import|;
end_import

begin_comment
comment|/**  * Apache CXF portable CDI extension to support initialization of JAX-RS / JAX-WS resources.    */
end_comment

begin_class
specifier|public
class|class
name|JAXRSCdiResourceExtension
implements|implements
name|Extension
block|{
specifier|private
specifier|final
name|List
argument_list|<
name|Bean
argument_list|<
name|?
argument_list|>
argument_list|>
name|applicationBeans
init|=
operator|new
name|ArrayList
argument_list|<
name|Bean
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|Bean
argument_list|<
name|?
argument_list|>
argument_list|>
name|serviceBeans
init|=
operator|new
name|ArrayList
argument_list|<
name|Bean
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|Bean
argument_list|<
name|?
argument_list|>
argument_list|>
name|providerBeans
init|=
operator|new
name|ArrayList
argument_list|<
name|Bean
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Bean
argument_list|<
name|?
argument_list|>
argument_list|,
name|Bean
argument_list|<
name|JAXRSServerFactoryBean
argument_list|>
argument_list|>
name|factoryBeans
init|=
operator|new
name|HashMap
argument_list|<
name|Bean
argument_list|<
name|?
argument_list|>
argument_list|,
name|Bean
argument_list|<
name|JAXRSServerFactoryBean
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|JAXRSServerFactoryBean
argument_list|>
name|factories
init|=
operator|new
name|ArrayList
argument_list|<
name|JAXRSServerFactoryBean
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|services
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
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
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|collect
parameter_list|(
annotation|@
name|Observes
specifier|final
name|ProcessBean
argument_list|<
name|T
argument_list|>
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|getAnnotated
argument_list|()
operator|.
name|isAnnotationPresent
argument_list|(
name|ApplicationPath
operator|.
name|class
argument_list|)
condition|)
block|{
name|applicationBeans
operator|.
name|add
argument_list|(
name|event
operator|.
name|getBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|event
operator|.
name|getAnnotated
argument_list|()
operator|.
name|isAnnotationPresent
argument_list|(
name|Path
operator|.
name|class
argument_list|)
condition|)
block|{
name|serviceBeans
operator|.
name|add
argument_list|(
name|event
operator|.
name|getBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|event
operator|.
name|getAnnotated
argument_list|()
operator|.
name|isAnnotationPresent
argument_list|(
name|Provider
operator|.
name|class
argument_list|)
condition|)
block|{
name|providerBeans
operator|.
name|add
argument_list|(
name|event
operator|.
name|getBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|load
parameter_list|(
annotation|@
name|Observes
specifier|final
name|AfterDeploymentValidation
name|event
parameter_list|,
specifier|final
name|BeanManager
name|beanManager
parameter_list|)
block|{
for|for
control|(
specifier|final
name|Bean
argument_list|<
name|?
argument_list|>
name|bean
range|:
name|serviceBeans
control|)
block|{
name|services
operator|.
name|add
argument_list|(
name|beanManager
operator|.
name|getReference
argument_list|(
name|bean
argument_list|,
name|bean
operator|.
name|getBeanClass
argument_list|()
argument_list|,
name|beanManager
operator|.
name|createCreationalContext
argument_list|(
name|bean
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
specifier|final
name|Bean
argument_list|<
name|?
argument_list|>
name|bean
range|:
name|providerBeans
control|)
block|{
name|providers
operator|.
name|add
argument_list|(
name|beanManager
operator|.
name|getReference
argument_list|(
name|bean
argument_list|,
name|bean
operator|.
name|getBeanClass
argument_list|()
argument_list|,
name|beanManager
operator|.
name|createCreationalContext
argument_list|(
name|bean
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|Bean
argument_list|<
name|?
argument_list|>
argument_list|,
name|Bean
argument_list|<
name|JAXRSServerFactoryBean
argument_list|>
argument_list|>
name|entry
range|:
name|factoryBeans
operator|.
name|entrySet
argument_list|()
control|)
block|{
specifier|final
name|Application
name|instance
init|=
operator|(
name|Application
operator|)
name|beanManager
operator|.
name|getReference
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|getBeanClass
argument_list|()
argument_list|,
name|beanManager
operator|.
name|createCreationalContext
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
comment|// Create the JAXRSServerFactoryBean for each application we have discovered
name|factories
operator|.
name|add
argument_list|(
name|createFactoryInstance
argument_list|(
name|instance
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|beanManager
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|injectFactories
parameter_list|(
annotation|@
name|Observes
specifier|final
name|AfterBeanDiscovery
name|event
parameter_list|,
specifier|final
name|BeanManager
name|beanManager
parameter_list|)
block|{
specifier|final
name|AnnotatedType
argument_list|<
name|JAXRSServerFactoryBean
argument_list|>
name|factoryAnnotatedType
init|=
name|beanManager
operator|.
name|createAnnotatedType
argument_list|(
name|JAXRSServerFactoryBean
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|InjectionTarget
argument_list|<
name|JAXRSServerFactoryBean
argument_list|>
name|injectionTarget
init|=
name|beanManager
operator|.
name|createInjectionTarget
argument_list|(
name|factoryAnnotatedType
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|Bean
argument_list|<
name|?
argument_list|>
name|applicationBean
range|:
name|applicationBeans
control|)
block|{
specifier|final
name|Bean
argument_list|<
name|JAXRSServerFactoryBean
argument_list|>
name|factoryBean
init|=
operator|new
name|JAXRSCdiServerFactoryBean
argument_list|(
name|applicationBean
argument_list|,
name|injectionTarget
argument_list|)
decl_stmt|;
name|event
operator|.
name|addBean
argument_list|(
name|factoryBean
argument_list|)
expr_stmt|;
name|factoryBeans
operator|.
name|put
argument_list|(
name|applicationBean
argument_list|,
name|factoryBean
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|JAXRSServerFactoryBean
argument_list|>
name|getFactories
parameter_list|()
block|{
return|return
name|Collections
operator|.
expr|<
name|JAXRSServerFactoryBean
operator|>
name|unmodifiableList
argument_list|(
name|factories
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|private
name|JAXRSServerFactoryBean
name|createFactoryInstance
parameter_list|(
specifier|final
name|Application
name|application
parameter_list|,
specifier|final
name|Bean
argument_list|<
name|?
argument_list|>
name|factoryBean
parameter_list|,
specifier|final
name|BeanManager
name|beanManager
parameter_list|)
block|{
specifier|final
name|JAXRSServerFactoryBean
name|instance
init|=
operator|(
name|JAXRSServerFactoryBean
operator|)
name|beanManager
operator|.
name|getReference
argument_list|(
name|factoryBean
argument_list|,
name|factoryBean
operator|.
name|getBeanClass
argument_list|()
argument_list|,
name|beanManager
operator|.
name|createCreationalContext
argument_list|(
name|factoryBean
argument_list|)
argument_list|)
decl_stmt|;
name|ResourceUtils
operator|.
name|initializeApplication
argument_list|(
name|instance
argument_list|,
name|application
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|instance
operator|.
name|setServiceBeans
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|(
name|services
argument_list|)
argument_list|)
expr_stmt|;
name|instance
operator|.
name|setProviders
argument_list|(
name|providers
argument_list|)
expr_stmt|;
specifier|final
name|ServiceLoader
argument_list|<
name|MessageBodyWriter
argument_list|>
name|writers
init|=
name|ServiceLoader
operator|.
name|load
argument_list|(
name|MessageBodyWriter
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|MessageBodyWriter
argument_list|<
name|?
argument_list|>
name|writer
range|:
name|writers
control|)
block|{
name|instance
operator|.
name|setProvider
argument_list|(
name|writer
argument_list|)
expr_stmt|;
block|}
specifier|final
name|ServiceLoader
argument_list|<
name|MessageBodyReader
argument_list|>
name|readers
init|=
name|ServiceLoader
operator|.
name|load
argument_list|(
name|MessageBodyReader
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|MessageBodyReader
argument_list|<
name|?
argument_list|>
name|reader
range|:
name|readers
control|)
block|{
name|instance
operator|.
name|setProvider
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
block|}
end_class

end_unit

