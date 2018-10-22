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
name|lang
operator|.
name|reflect
operator|.
name|Proxy
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
name|LinkedList
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|annotations
operator|.
name|Provider
operator|.
name|Scope
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
name|logging
operator|.
name|LogUtils
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
name|ClassHelper
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
name|PackageUtils
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
name|endpoint
operator|.
name|Server
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
name|helpers
operator|.
name|CastUtils
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
name|interceptor
operator|.
name|Interceptor
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
name|utils
operator|.
name|ResourceUtils
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
name|service
operator|.
name|factory
operator|.
name|ServiceConstructionException
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
name|annotation
operator|.
name|Value
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
name|annotation
operator|.
name|ComponentScan
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
name|annotation
operator|.
name|FilterType
import|;
end_import

begin_class
annotation|@
name|ComponentScan
argument_list|(
name|includeFilters
operator|=
annotation|@
name|ComponentScan
operator|.
name|Filter
argument_list|(
name|type
operator|=
name|FilterType
operator|.
name|ANNOTATION
argument_list|,
name|value
operator|=
block|{
name|ApplicationPath
operator|.
name|class
block|,
name|Path
operator|.
name|class
block|,
name|Provider
operator|.
name|class
block|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|Provider
operator|.
name|class
block|}
argument_list|)
argument_list|)
specifier|public
specifier|abstract
class|class
name|AbstractSpringComponentScanServer
extends|extends
name|AbstractSpringConfigurationFactory
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AbstractSpringComponentScanServer
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Value
argument_list|(
literal|"${cxf.jaxrs.classes-scan-packages:}"
argument_list|)
specifier|private
name|String
name|classesScanPackages
decl_stmt|;
annotation|@
name|Value
argument_list|(
literal|"${cxf.jaxrs.component-scan-packages:}"
argument_list|)
specifier|private
name|String
name|componentScanPackages
decl_stmt|;
annotation|@
name|Value
argument_list|(
literal|"${cxf.jaxrs.component-scan-beans:}"
argument_list|)
specifier|private
name|String
name|componentScanBeans
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ResourceProvider
argument_list|>
name|resourceProviders
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|jaxrsProviders
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Feature
argument_list|>
name|cxfFeatures
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|serviceAnnotation
decl_stmt|;
specifier|protected
name|AbstractSpringComponentScanServer
parameter_list|()
block|{      }
specifier|protected
name|AbstractSpringComponentScanServer
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|serviceAnnotation
parameter_list|)
block|{
name|this
operator|.
name|serviceAnnotation
operator|=
name|serviceAnnotation
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Server
name|createJaxRsServer
parameter_list|()
block|{
name|JAXRSServerFactoryBean
name|factoryBean
init|=
literal|null
decl_stmt|;
name|String
index|[]
name|beanNames
init|=
name|applicationContext
operator|.
name|getBeanNamesForAnnotation
argument_list|(
name|ApplicationPath
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|beanNames
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|componentScanPackagesSet
init|=
name|parseSetProperty
argument_list|(
name|componentScanPackages
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|componentScanBeansSet
init|=
name|parseSetProperty
argument_list|(
name|componentScanBeans
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|beanName
range|:
name|beanNames
control|)
block|{
if|if
condition|(
name|isComponentMatched
argument_list|(
name|beanName
argument_list|,
name|componentScanPackagesSet
argument_list|,
name|componentScanBeansSet
argument_list|)
condition|)
block|{
name|Application
name|app
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|beanName
argument_list|,
name|Application
operator|.
name|class
argument_list|)
decl_stmt|;
name|factoryBean
operator|=
name|createFactoryBeanFromApplication
argument_list|(
name|app
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|cxfBeanName
range|:
name|applicationContext
operator|.
name|getBeanNamesForAnnotation
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|Provider
operator|.
name|class
argument_list|)
control|)
block|{
if|if
condition|(
name|isComponentMatched
argument_list|(
name|cxfBeanName
argument_list|,
name|componentScanPackagesSet
argument_list|,
name|componentScanBeansSet
argument_list|)
condition|)
block|{
name|addCxfProvider
argument_list|(
name|getProviderBean
argument_list|(
name|cxfBeanName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
break|break;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|classesScanPackages
argument_list|)
condition|)
block|{
try|try
block|{
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
name|appClasses
init|=
name|ClasspathScanner
operator|.
name|findClasses
argument_list|(
name|classesScanPackages
argument_list|,
name|ApplicationPath
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Application
argument_list|>
name|apps
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|JAXRSServerFactoryBeanDefinitionParser
operator|.
name|createBeansFromDiscoveredClasses
argument_list|(
name|super
operator|.
name|applicationContext
argument_list|,
name|appClasses
operator|.
name|get
argument_list|(
name|ApplicationPath
operator|.
name|class
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|apps
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|factoryBean
operator|=
name|createFactoryBeanFromApplication
argument_list|(
name|apps
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
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
name|cxfClasses
init|=
name|ClasspathScanner
operator|.
name|findClasses
argument_list|(
name|classesScanPackages
argument_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|Provider
operator|.
name|class
argument_list|)
decl_stmt|;
name|addCxfProvidersFromClasses
argument_list|(
name|cxfClasses
operator|.
name|get
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|Provider
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|factoryBean
operator|!=
literal|null
condition|)
block|{
name|setFactoryCxfProviders
argument_list|(
name|factoryBean
argument_list|)
expr_stmt|;
return|return
name|factoryBean
operator|.
name|create
argument_list|()
return|;
block|}
return|return
name|super
operator|.
name|createJaxRsServer
argument_list|()
return|;
block|}
specifier|protected
name|void
name|setJaxrsResources
parameter_list|(
name|JAXRSServerFactoryBean
name|factory
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|componentScanPackagesSet
init|=
name|parseSetProperty
argument_list|(
name|componentScanPackages
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|componentScanBeansSet
init|=
name|parseSetProperty
argument_list|(
name|componentScanBeans
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|beanName
range|:
name|applicationContext
operator|.
name|getBeanDefinitionNames
argument_list|()
control|)
block|{
if|if
condition|(
name|isValidComponent
argument_list|(
name|beanName
argument_list|,
name|Path
operator|.
name|class
argument_list|,
name|componentScanPackagesSet
argument_list|,
name|componentScanBeansSet
argument_list|)
condition|)
block|{
name|SpringResourceFactory
name|resourceFactory
init|=
operator|new
name|SpringResourceFactory
argument_list|(
name|beanName
argument_list|)
decl_stmt|;
name|resourceFactory
operator|.
name|setApplicationContext
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
name|resourceProviders
operator|.
name|add
argument_list|(
name|resourceFactory
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isValidComponent
argument_list|(
name|beanName
argument_list|,
name|Provider
operator|.
name|class
argument_list|,
name|componentScanPackagesSet
argument_list|,
name|componentScanBeansSet
argument_list|)
condition|)
block|{
name|jaxrsProviders
operator|.
name|add
argument_list|(
name|getProviderBean
argument_list|(
name|beanName
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isValidComponent
argument_list|(
name|beanName
argument_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|Provider
operator|.
name|class
argument_list|,
name|componentScanPackagesSet
argument_list|,
name|componentScanBeansSet
argument_list|)
condition|)
block|{
name|addCxfProvider
argument_list|(
name|getProviderBean
argument_list|(
name|beanName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|classesScanPackages
argument_list|)
condition|)
block|{
try|try
block|{
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
name|classesScanPackages
argument_list|,
name|Provider
operator|.
name|class
argument_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|Provider
operator|.
name|class
argument_list|)
decl_stmt|;
name|jaxrsProviders
operator|.
name|addAll
argument_list|(
name|JAXRSServerFactoryBeanDefinitionParser
operator|.
name|createBeansFromDiscoveredClasses
argument_list|(
name|applicationContext
argument_list|,
name|classes
operator|.
name|get
argument_list|(
name|Provider
operator|.
name|class
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|warnIfDuplicatesAvailable
argument_list|(
name|jaxrsProviders
argument_list|)
expr_stmt|;
name|addCxfProvidersFromClasses
argument_list|(
name|classes
operator|.
name|get
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|Provider
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
name|factory
operator|.
name|setResourceProviders
argument_list|(
name|getResourceProviders
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setProviders
argument_list|(
name|getJaxrsProviders
argument_list|()
argument_list|)
expr_stmt|;
name|setFactoryCxfProviders
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setFactoryCxfProviders
parameter_list|(
name|JAXRSServerFactoryBean
name|factory
parameter_list|)
block|{
name|factory
operator|.
name|setFeatures
argument_list|(
name|getFeatures
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setInInterceptors
argument_list|(
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setOutInterceptors
argument_list|(
name|getOutInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setOutFaultInterceptors
argument_list|(
name|getOutFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|addCxfProvidersFromClasses
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
name|List
argument_list|<
name|Object
argument_list|>
name|cxfProviders
init|=
name|JAXRSServerFactoryBeanDefinitionParser
operator|.
name|createBeansFromDiscoveredClasses
argument_list|(
name|applicationContext
argument_list|,
name|classes
argument_list|,
literal|null
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|cxfProvider
range|:
name|cxfProviders
control|)
block|{
name|addCxfProvider
argument_list|(
name|cxfProvider
argument_list|)
expr_stmt|;
block|}
name|warnIfDuplicatesAvailable
argument_list|(
name|cxfFeatures
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|boolean
name|isValidComponent
parameter_list|(
name|String
name|beanName
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|ann
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|componentScanPackagesSet
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|componentScanBeansSet
parameter_list|)
block|{
return|return
name|isAnnotationAvailable
argument_list|(
name|beanName
argument_list|,
name|ann
argument_list|)
operator|&&
name|nonProxyClass
argument_list|(
name|beanName
argument_list|)
operator|&&
name|isComponentMatched
argument_list|(
name|beanName
argument_list|,
name|componentScanPackagesSet
argument_list|,
name|componentScanBeansSet
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|isComponentMatched
parameter_list|(
name|String
name|beanName
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|componentScanPackagesSet
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|componentScanBeansSet
parameter_list|)
block|{
return|return
name|matchesServiceAnnotation
argument_list|(
name|beanName
argument_list|)
operator|&&
name|matchesComponentPackage
argument_list|(
name|beanName
argument_list|,
name|componentScanPackagesSet
argument_list|)
operator|&&
name|matchesComponentName
argument_list|(
name|beanName
argument_list|,
name|componentScanBeansSet
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|nonProxyClass
parameter_list|(
name|String
name|beanName
parameter_list|)
block|{
comment|// JAX-RS runtime needs to be able to access the real component class to introspect it for
comment|// JAX-RS annotations; the following check ensures that the valid proxified components
comment|// are accepted while the client proxies are ignored.
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|ClassHelper
operator|.
name|getRealClassFromClass
argument_list|(
name|applicationContext
operator|.
name|getType
argument_list|(
name|beanName
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|Proxy
operator|.
name|isProxyClass
argument_list|(
name|type
argument_list|)
operator|&&
name|applicationContext
operator|.
name|isSingleton
argument_list|(
name|beanName
argument_list|)
condition|)
block|{
name|type
operator|=
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|applicationContext
operator|.
name|getBean
argument_list|(
name|beanName
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Proxy
operator|.
name|isProxyClass
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Can not determine the real class of the component '"
operator|+
name|beanName
operator|+
literal|"'"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|protected
name|boolean
name|matchesComponentName
parameter_list|(
name|String
name|beanName
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|componentScanBeansSet
parameter_list|)
block|{
return|return
name|componentScanBeansSet
operator|==
literal|null
operator|||
name|componentScanBeansSet
operator|.
name|contains
argument_list|(
name|beanName
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|matchesComponentPackage
parameter_list|(
name|String
name|beanName
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|componentScanPackagesSet
parameter_list|)
block|{
return|return
name|componentScanPackagesSet
operator|==
literal|null
operator|||
operator|!
name|applicationContext
operator|.
name|isSingleton
argument_list|(
name|beanName
argument_list|)
operator|||
name|componentScanPackagesSet
operator|.
name|contains
argument_list|(
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|applicationContext
operator|.
name|getBean
argument_list|(
name|beanName
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|void
name|warnIfDuplicatesAvailable
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|providers
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|classNames
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|providers
control|)
block|{
if|if
condition|(
operator|!
name|classNames
operator|.
name|add
argument_list|(
name|o
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Duplicate Provider "
operator|+
name|o
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" has been detected"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|Object
name|getProviderBean
parameter_list|(
name|String
name|beanName
parameter_list|)
block|{
return|return
name|applicationContext
operator|.
name|getBean
argument_list|(
name|beanName
argument_list|)
return|;
block|}
specifier|protected
name|void
name|addCxfProvider
parameter_list|(
name|Object
name|bean
parameter_list|)
block|{
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|Provider
name|ann
init|=
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|Provider
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ann
operator|.
name|scope
argument_list|()
operator|==
name|Scope
operator|.
name|Client
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|ann
operator|.
name|value
argument_list|()
operator|==
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|Provider
operator|.
name|Type
operator|.
name|Feature
condition|)
block|{
name|cxfFeatures
operator|.
name|add
argument_list|(
operator|(
name|Feature
operator|)
name|bean
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ann
operator|.
name|value
argument_list|()
operator|==
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|Provider
operator|.
name|Type
operator|.
name|InInterceptor
condition|)
block|{
name|super
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|(
name|Interceptor
argument_list|<
name|?
argument_list|>
operator|)
name|bean
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ann
operator|.
name|value
argument_list|()
operator|==
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|Provider
operator|.
name|Type
operator|.
name|OutInterceptor
condition|)
block|{
name|super
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|(
name|Interceptor
argument_list|<
name|?
argument_list|>
operator|)
name|bean
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ann
operator|.
name|value
argument_list|()
operator|==
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|Provider
operator|.
name|Type
operator|.
name|OutFaultInterceptor
condition|)
block|{
name|super
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|(
name|Interceptor
argument_list|<
name|?
argument_list|>
operator|)
name|bean
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|boolean
name|matchesServiceAnnotation
parameter_list|(
name|String
name|beanName
parameter_list|)
block|{
return|return
name|serviceAnnotation
operator|==
literal|null
operator|||
name|isAnnotationAvailable
argument_list|(
name|beanName
argument_list|,
name|serviceAnnotation
argument_list|)
return|;
block|}
specifier|protected
parameter_list|<
name|A
extends|extends
name|Annotation
parameter_list|>
name|boolean
name|isAnnotationAvailable
parameter_list|(
name|String
name|beanName
parameter_list|,
name|Class
argument_list|<
name|A
argument_list|>
name|annClass
parameter_list|)
block|{
return|return
name|applicationContext
operator|.
name|findAnnotationOnBean
argument_list|(
name|beanName
argument_list|,
name|annClass
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|protected
name|List
argument_list|<
name|ResourceProvider
argument_list|>
name|getResourceProviders
parameter_list|()
block|{
return|return
name|resourceProviders
return|;
block|}
specifier|protected
name|List
argument_list|<
name|Object
argument_list|>
name|getJaxrsProviders
parameter_list|()
block|{
return|return
name|jaxrsProviders
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Feature
argument_list|>
name|getFeatures
parameter_list|()
block|{
return|return
name|cxfFeatures
return|;
block|}
specifier|protected
name|JAXRSServerFactoryBean
name|createFactoryBeanFromApplication
parameter_list|(
name|Application
name|app
parameter_list|)
block|{
return|return
name|ResourceUtils
operator|.
name|createApplication
argument_list|(
name|app
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
name|getBus
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|parseSetProperty
parameter_list|(
name|String
name|componentScanProp
parameter_list|)
block|{
return|return
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|componentScanProp
argument_list|)
condition|?
name|ClasspathScanner
operator|.
name|parsePackages
argument_list|(
name|componentScanProp
argument_list|)
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

