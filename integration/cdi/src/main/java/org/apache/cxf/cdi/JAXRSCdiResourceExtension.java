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
name|lang
operator|.
name|reflect
operator|.
name|Type
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|ServiceLoader
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
name|enterprise
operator|.
name|context
operator|.
name|spi
operator|.
name|CreationalContext
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
name|BeforeShutdown
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
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|ProcessProducerField
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
name|ProcessProducerMethod
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
name|extension
operator|.
name|ExtensionManagerBus
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
name|cdi
operator|.
name|extension
operator|.
name|JAXRSServerFactoryCustomizationExtension
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
name|feature
operator|.
name|Feature
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
comment|/**  * Apache CXF portable CDI extension to support initialization of JAX-RS resources.  */
end_comment

begin_class
specifier|public
class|class
name|JAXRSCdiResourceExtension
implements|implements
name|Extension
block|{
specifier|private
name|boolean
name|hasBus
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|Bean
argument_list|<
name|?
argument_list|>
argument_list|>
name|applicationBeans
init|=
operator|new
name|LinkedHashSet
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
name|List
argument_list|<
name|Bean
argument_list|<
name|?
extends|extends
name|Feature
argument_list|>
argument_list|>
name|featureBeans
init|=
operator|new
name|ArrayList
argument_list|<
name|Bean
argument_list|<
name|?
extends|extends
name|Feature
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|CreationalContext
argument_list|<
name|?
argument_list|>
argument_list|>
name|disposableCreationalContexts
init|=
operator|new
name|ArrayList
argument_list|<
name|CreationalContext
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Holder of the classified resource classes, converted to appropriate instance      * representations.      */
specifier|private
specifier|static
class|class
name|ClassifiedClasses
block|{
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Feature
argument_list|>
name|features
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|CdiResourceProvider
argument_list|>
name|resourceProviders
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|addProviders
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|Object
argument_list|>
name|others
parameter_list|)
block|{
name|this
operator|.
name|providers
operator|.
name|addAll
argument_list|(
name|others
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addFeatures
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|Feature
argument_list|>
name|others
parameter_list|)
block|{
name|this
operator|.
name|features
operator|.
name|addAll
argument_list|(
name|others
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addResourceProvider
parameter_list|(
specifier|final
name|CdiResourceProvider
name|other
parameter_list|)
block|{
name|this
operator|.
name|resourceProviders
operator|.
name|add
argument_list|(
name|other
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getProviders
parameter_list|()
block|{
return|return
name|providers
return|;
block|}
specifier|public
name|List
argument_list|<
name|Feature
argument_list|>
name|getFeatures
parameter_list|()
block|{
return|return
name|features
return|;
block|}
specifier|public
name|List
argument_list|<
name|CdiResourceProvider
argument_list|>
name|getResourceProviders
parameter_list|()
block|{
return|return
name|resourceProviders
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
elseif|else
if|if
condition|(
name|event
operator|.
name|getBean
argument_list|()
operator|.
name|getTypes
argument_list|()
operator|.
name|contains
argument_list|(
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Feature
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
elseif|else
if|if
condition|(
name|event
operator|.
name|getBean
argument_list|()
operator|.
name|getTypes
argument_list|()
operator|.
name|contains
argument_list|(
name|Feature
operator|.
name|class
argument_list|)
condition|)
block|{
name|featureBeans
operator|.
name|add
argument_list|(
operator|(
name|Bean
argument_list|<
name|?
extends|extends
name|Feature
argument_list|>
operator|)
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
name|CdiBusBean
operator|.
name|CXF
operator|.
name|equals
argument_list|(
name|event
operator|.
name|getBean
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|Bus
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|event
operator|.
name|getBean
argument_list|()
operator|.
name|getBeanClass
argument_list|()
argument_list|)
condition|)
block|{
name|hasBus
operator|=
literal|true
expr_stmt|;
block|}
block|}
specifier|public
parameter_list|<
name|T
parameter_list|,
name|X
parameter_list|>
name|void
name|collect
parameter_list|(
annotation|@
name|Observes
specifier|final
name|ProcessProducerField
argument_list|<
name|T
argument_list|,
name|X
argument_list|>
name|event
parameter_list|)
block|{
specifier|final
name|Type
name|baseType
init|=
name|event
operator|.
name|getAnnotatedProducerField
argument_list|()
operator|.
name|getBaseType
argument_list|()
decl_stmt|;
name|processProducer
argument_list|(
name|event
argument_list|,
name|baseType
argument_list|)
expr_stmt|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|,
name|X
parameter_list|>
name|void
name|collect
parameter_list|(
annotation|@
name|Observes
specifier|final
name|ProcessProducerMethod
argument_list|<
name|T
argument_list|,
name|X
argument_list|>
name|event
parameter_list|)
block|{
specifier|final
name|Type
name|baseType
init|=
name|event
operator|.
name|getAnnotatedProducerMethod
argument_list|()
operator|.
name|getBaseType
argument_list|()
decl_stmt|;
name|processProducer
argument_list|(
name|event
argument_list|,
name|baseType
argument_list|)
expr_stmt|;
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
comment|// no need of creational context, it only works for app scoped instances anyway
specifier|final
name|Bean
argument_list|<
name|?
argument_list|>
name|busBean
init|=
name|beanManager
operator|.
name|resolve
argument_list|(
name|beanManager
operator|.
name|getBeans
argument_list|(
name|CdiBusBean
operator|.
name|CXF
argument_list|)
argument_list|)
decl_stmt|;
name|bus
operator|=
operator|(
name|Bus
operator|)
name|beanManager
operator|.
name|getReference
argument_list|(
name|busBean
argument_list|,
name|Bus
operator|.
name|class
argument_list|,
name|beanManager
operator|.
name|createCreationalContext
argument_list|(
name|busBean
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|Bean
argument_list|<
name|?
argument_list|>
name|application
range|:
name|applicationBeans
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
name|application
argument_list|,
name|application
operator|.
name|getBeanClass
argument_list|()
argument_list|,
name|createCreationalContext
argument_list|(
name|beanManager
argument_list|,
name|application
argument_list|)
argument_list|)
decl_stmt|;
comment|// If there is an application without any singletons and classes defined, we will
comment|// create a server factory bean with all services and providers discovered.
if|if
condition|(
name|instance
operator|.
name|getSingletons
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|instance
operator|.
name|getClasses
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|JAXRSServerFactoryBean
name|factory
init|=
name|createFactoryInstance
argument_list|(
name|instance
argument_list|,
name|loadServices
argument_list|(
name|beanManager
argument_list|,
name|Collections
operator|.
expr|<
name|Class
argument_list|<
name|?
argument_list|>
operator|>
name|emptySet
argument_list|()
argument_list|)
argument_list|,
name|loadProviders
argument_list|(
name|beanManager
argument_list|,
name|Collections
operator|.
expr|<
name|Class
argument_list|<
name|?
argument_list|>
operator|>
name|emptySet
argument_list|()
argument_list|)
argument_list|,
name|loadFeatures
argument_list|(
name|beanManager
argument_list|,
name|Collections
operator|.
expr|<
name|Class
argument_list|<
name|?
argument_list|>
operator|>
name|emptySet
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|customize
argument_list|(
name|beanManager
argument_list|,
name|factory
argument_list|)
expr_stmt|;
name|factory
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// If there is an application with any singletons or classes defined, we will
comment|// create a server factory bean with only application singletons and classes.
specifier|final
name|JAXRSServerFactoryBean
name|factory
init|=
name|createFactoryInstance
argument_list|(
name|instance
argument_list|,
name|beanManager
argument_list|)
decl_stmt|;
name|customize
argument_list|(
name|beanManager
argument_list|,
name|factory
argument_list|)
expr_stmt|;
name|factory
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|injectBus
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
if|if
condition|(
operator|!
name|hasBus
condition|)
block|{
specifier|final
name|AnnotatedType
argument_list|<
name|ExtensionManagerBus
argument_list|>
name|busAnnotatedType
init|=
name|beanManager
operator|.
name|createAnnotatedType
argument_list|(
name|ExtensionManagerBus
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|InjectionTarget
argument_list|<
name|ExtensionManagerBus
argument_list|>
name|busInjectionTarget
init|=
name|beanManager
operator|.
name|createInjectionTarget
argument_list|(
name|busAnnotatedType
argument_list|)
decl_stmt|;
name|event
operator|.
name|addBean
argument_list|(
operator|new
name|CdiBusBean
argument_list|(
name|busInjectionTarget
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|applicationBeans
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|serviceBeans
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|DefaultApplicationBean
name|applicationBean
init|=
operator|new
name|DefaultApplicationBean
argument_list|()
decl_stmt|;
name|applicationBeans
operator|.
name|add
argument_list|(
name|applicationBean
argument_list|)
expr_stmt|;
name|event
operator|.
name|addBean
argument_list|(
name|applicationBean
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Releases created CreationalContext instances      */
specifier|public
name|void
name|release
parameter_list|(
annotation|@
name|Observes
specifier|final
name|BeforeShutdown
name|event
parameter_list|)
block|{
for|for
control|(
specifier|final
name|CreationalContext
argument_list|<
name|?
argument_list|>
name|disposableCreationalContext
range|:
name|disposableCreationalContexts
control|)
block|{
name|disposableCreationalContext
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Create the JAXRSServerFactoryBean from the application and all discovered service and provider instances.      * @param application application instance      * @param services all discovered services      * @param providers all discovered providers      * @return JAXRSServerFactoryBean instance      */
specifier|private
name|JAXRSServerFactoryBean
name|createFactoryInstance
parameter_list|(
specifier|final
name|Application
name|application
parameter_list|,
specifier|final
name|List
argument_list|<
name|?
argument_list|>
name|services
parameter_list|,
specifier|final
name|List
argument_list|<
name|?
argument_list|>
name|providers
parameter_list|,
specifier|final
name|List
argument_list|<
name|?
extends|extends
name|Feature
argument_list|>
name|features
parameter_list|)
block|{
specifier|final
name|JAXRSServerFactoryBean
name|instance
init|=
name|ResourceUtils
operator|.
name|createApplication
argument_list|(
name|application
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|instance
operator|.
name|setServiceBeans
argument_list|(
operator|new
name|ArrayList
argument_list|<>
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
name|instance
operator|.
name|setProviders
argument_list|(
name|loadExternalProviders
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|.
name|setFeatures
argument_list|(
name|features
argument_list|)
expr_stmt|;
return|return
name|instance
return|;
block|}
comment|/**      * Create the JAXRSServerFactoryBean from the objects declared by application itself.      * @param application application instance      * @return JAXRSServerFactoryBean instance      */
specifier|private
name|JAXRSServerFactoryBean
name|createFactoryInstance
parameter_list|(
specifier|final
name|Application
name|application
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
name|ResourceUtils
operator|.
name|createApplication
argument_list|(
name|application
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
name|bus
argument_list|)
decl_stmt|;
specifier|final
name|ClassifiedClasses
name|classified
init|=
name|classes2singletons
argument_list|(
name|application
argument_list|,
name|beanManager
argument_list|)
decl_stmt|;
name|instance
operator|.
name|setProviders
argument_list|(
name|classified
operator|.
name|getProviders
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|.
name|getFeatures
argument_list|()
operator|.
name|addAll
argument_list|(
name|classified
operator|.
name|getFeatures
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|CdiResourceProvider
name|resourceProvider
range|:
name|classified
operator|.
name|getResourceProviders
argument_list|()
control|)
block|{
name|instance
operator|.
name|setResourceProvider
argument_list|(
name|resourceProvider
operator|.
name|getResourceClass
argument_list|()
argument_list|,
name|resourceProvider
argument_list|)
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
comment|/**      * JAX-RS application has defined singletons as being classes of any providers, resources and features.      * In the JAXRSServerFactoryBean, those should be split around several method calls depending on instance      * type. At the moment, only the Feature is CXF-specific and should be replaced by JAX-RS Feature implementation.      * @param application the application instance      * @return classified instances of classes by instance types      */
specifier|private
name|ClassifiedClasses
name|classes2singletons
parameter_list|(
specifier|final
name|Application
name|application
parameter_list|,
specifier|final
name|BeanManager
name|beanManager
parameter_list|)
block|{
specifier|final
name|ClassifiedClasses
name|classified
init|=
operator|new
name|ClassifiedClasses
argument_list|()
decl_stmt|;
comment|// now loop through the classes
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
name|application
operator|.
name|getClasses
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|classes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|classified
operator|.
name|addProviders
argument_list|(
name|loadProviders
argument_list|(
name|beanManager
argument_list|,
name|classes
argument_list|)
argument_list|)
expr_stmt|;
name|classified
operator|.
name|addFeatures
argument_list|(
name|loadFeatures
argument_list|(
name|beanManager
argument_list|,
name|classes
argument_list|)
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|classes
operator|.
name|contains
argument_list|(
name|bean
operator|.
name|getBeanClass
argument_list|()
argument_list|)
condition|)
block|{
name|classified
operator|.
name|addResourceProvider
argument_list|(
operator|new
name|CdiResourceProvider
argument_list|(
name|beanManager
argument_list|,
name|bean
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|classified
return|;
block|}
comment|/**      * Load external providers from service loader      * @return loaded external providers      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|loadExternalProviders
parameter_list|()
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
name|providers
operator|.
name|add
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
name|providers
operator|.
name|add
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
return|return
name|providers
return|;
block|}
comment|/**      * Gets the references for all discovered JAX-RS resources      * @param beanManager bean manager instance      * @param limitedClasses not null, if empty ignored.  the set of classes to consider as providers      * @return the references for all discovered JAX-RS resources      */
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|loadProviders
parameter_list|(
specifier|final
name|BeanManager
name|beanManager
parameter_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|limitedClasses
parameter_list|)
block|{
return|return
name|loadBeans
argument_list|(
name|beanManager
argument_list|,
name|limitedClasses
argument_list|,
name|providerBeans
argument_list|)
return|;
block|}
comment|/**      * Gets the references for all discovered JAX-RS providers      * @param beanManager bean manager instance      * @param limitedClasses not null, if empty ignored.  the set of classes to consider as providers      * @return the references for all discovered JAX-RS providers      */
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|loadServices
parameter_list|(
specifier|final
name|BeanManager
name|beanManager
parameter_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|limitedClasses
parameter_list|)
block|{
return|return
name|loadBeans
argument_list|(
name|beanManager
argument_list|,
name|limitedClasses
argument_list|,
name|serviceBeans
argument_list|)
return|;
block|}
comment|/**      * Gets references for all beans of a given type      * @param beanManager bean manager instance      * @param limitedClasses not null, if empty ignored.  the set of classes to consider as providers      * @param beans the collection of beans to go through      * @return the references for all discovered JAX-RS providers      */
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|loadBeans
parameter_list|(
specifier|final
name|BeanManager
name|beanManager
parameter_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|limitedClasses
parameter_list|,
name|Collection
argument_list|<
name|Bean
argument_list|<
name|?
argument_list|>
argument_list|>
name|beans
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|instances
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Bean
argument_list|<
name|?
argument_list|>
name|bean
range|:
name|beans
control|)
block|{
if|if
condition|(
name|limitedClasses
operator|.
name|isEmpty
argument_list|()
operator|||
name|limitedClasses
operator|.
name|contains
argument_list|(
name|bean
operator|.
name|getBeanClass
argument_list|()
argument_list|)
condition|)
block|{
name|instances
operator|.
name|add
argument_list|(
name|beanManager
operator|.
name|getReference
argument_list|(
name|bean
argument_list|,
name|Object
operator|.
name|class
argument_list|,
name|createCreationalContext
argument_list|(
name|beanManager
argument_list|,
name|bean
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|instances
return|;
block|}
comment|/**      * Gets the references for all discovered CXF-specific features      * @param beanManager bean manager instance      * @return the references for all discovered CXF-specific features      */
specifier|private
name|List
argument_list|<
name|Feature
argument_list|>
name|loadFeatures
parameter_list|(
specifier|final
name|BeanManager
name|beanManager
parameter_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|limitedClasses
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|Feature
argument_list|>
name|features
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Bean
argument_list|<
name|?
extends|extends
name|Feature
argument_list|>
name|bean
range|:
name|featureBeans
control|)
block|{
if|if
condition|(
name|limitedClasses
operator|.
name|isEmpty
argument_list|()
operator|||
name|limitedClasses
operator|.
name|contains
argument_list|(
name|bean
operator|.
name|getBeanClass
argument_list|()
argument_list|)
condition|)
block|{
name|features
operator|.
name|add
argument_list|(
operator|(
name|Feature
operator|)
name|beanManager
operator|.
name|getReference
argument_list|(
name|bean
argument_list|,
name|Feature
operator|.
name|class
argument_list|,
name|createCreationalContext
argument_list|(
name|beanManager
argument_list|,
name|bean
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|features
return|;
block|}
comment|/**      * Look and apply the available JAXRSServerFactoryBean extensions to customize its      * creation (f.e. add features, providers, assign transport, ...)      * @param beanManager bean manager      * @param bean JAX-RS server factory bean about to be created      */
specifier|private
name|void
name|customize
parameter_list|(
specifier|final
name|BeanManager
name|beanManager
parameter_list|,
specifier|final
name|JAXRSServerFactoryBean
name|bean
parameter_list|)
block|{
specifier|final
name|Collection
argument_list|<
name|Bean
argument_list|<
name|?
argument_list|>
argument_list|>
name|extensionBeans
init|=
name|beanManager
operator|.
name|getBeans
argument_list|(
name|JAXRSServerFactoryCustomizationExtension
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|Bean
argument_list|<
name|?
argument_list|>
name|extensionBean
range|:
name|extensionBeans
control|)
block|{
specifier|final
name|JAXRSServerFactoryCustomizationExtension
name|extension
init|=
operator|(
name|JAXRSServerFactoryCustomizationExtension
operator|)
name|beanManager
operator|.
name|getReference
argument_list|(
name|extensionBean
argument_list|,
name|extensionBean
operator|.
name|getBeanClass
argument_list|()
argument_list|,
name|beanManager
operator|.
name|createCreationalContext
argument_list|(
name|extensionBean
argument_list|)
argument_list|)
decl_stmt|;
name|extension
operator|.
name|customize
argument_list|(
name|bean
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates and collects the CreationalContext instances for future releasing.      * @param beanManager bean manager instance      * @param bean bean instance to create CreationalContext for      * @return CreationalContext instance      */
specifier|private
parameter_list|<
name|T
parameter_list|>
name|CreationalContext
argument_list|<
name|T
argument_list|>
name|createCreationalContext
parameter_list|(
specifier|final
name|BeanManager
name|beanManager
parameter_list|,
name|Bean
argument_list|<
name|T
argument_list|>
name|bean
parameter_list|)
block|{
specifier|final
name|CreationalContext
argument_list|<
name|T
argument_list|>
name|creationalContext
init|=
name|beanManager
operator|.
name|createCreationalContext
argument_list|(
name|bean
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|bean
operator|instanceof
name|DefaultApplicationBean
operator|)
condition|)
block|{
name|disposableCreationalContexts
operator|.
name|add
argument_list|(
name|creationalContext
argument_list|)
expr_stmt|;
block|}
return|return
name|creationalContext
return|;
block|}
comment|/**      * Extracts relevant beans from producers.      * @param event process bean event      * @param baseType base type of the producer      */
specifier|private
parameter_list|<
name|T
parameter_list|>
name|void
name|processProducer
parameter_list|(
specifier|final
name|ProcessBean
argument_list|<
name|T
argument_list|>
name|event
parameter_list|,
specifier|final
name|Type
name|baseType
parameter_list|)
block|{
if|if
condition|(
name|baseType
operator|instanceof
name|Class
argument_list|<
name|?
argument_list|>
condition|)
block|{
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|baseType
decl_stmt|;
if|if
condition|(
name|clazz
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
name|clazz
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
elseif|else
if|if
condition|(
name|clazz
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
block|}
block|}
end_class

unit|}
end_unit

