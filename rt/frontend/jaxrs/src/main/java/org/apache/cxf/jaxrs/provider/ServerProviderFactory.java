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
name|provider
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
name|Arrays
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
name|HashMap
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
name|LinkedHashSet
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|RuntimeType
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
name|container
operator|.
name|ContainerRequestFilter
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
name|container
operator|.
name|ContainerResponseFilter
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
name|container
operator|.
name|DynamicFeature
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
name|container
operator|.
name|PreMatching
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
name|core
operator|.
name|Configurable
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
name|Configuration
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
name|FeatureContext
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
name|ExceptionMapper
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
name|ReaderInterceptor
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
name|WriterInterceptor
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
name|endpoint
operator|.
name|Endpoint
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
name|impl
operator|.
name|ConfigurableImpl
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
name|impl
operator|.
name|RequestPreprocessor
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
name|impl
operator|.
name|ResourceInfoImpl
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
name|impl
operator|.
name|WebApplicationExceptionMapper
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
name|BeanParamInfo
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
name|ClassResourceInfo
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
name|FilterProviderInfo
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
name|OperationResourceInfo
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
name|ProviderInfo
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
name|AnnotationUtils
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
name|InjectionUtils
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
name|message
operator|.
name|Message
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|ServerProviderFactory
extends|extends
name|ProviderFactory
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SHARED_SERVER_FACTORY
init|=
literal|"jaxrs.shared.server.factory"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|SERVER_FILTER_INTERCEPTOR_CLASSES
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|(
name|Arrays
operator|.
expr|<
name|Class
argument_list|<
name|?
argument_list|>
operator|>
name|asList
argument_list|(
name|ContainerRequestFilter
operator|.
name|class
argument_list|,
name|ContainerResponseFilter
operator|.
name|class
argument_list|,
name|ReaderInterceptor
operator|.
name|class
argument_list|,
name|WriterInterceptor
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WADL_PROVIDER_NAME
init|=
literal|"org.apache.cxf.jaxrs.model.wadl.WadlGenerator"
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ProviderInfo
argument_list|<
name|ExceptionMapper
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|exceptionMappers
init|=
operator|new
name|ArrayList
argument_list|<
name|ProviderInfo
argument_list|<
name|ExceptionMapper
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ProviderInfo
argument_list|<
name|ContainerRequestFilter
argument_list|>
argument_list|>
name|preMatchContainerRequestFilters
init|=
operator|new
name|ArrayList
argument_list|<
name|ProviderInfo
argument_list|<
name|ContainerRequestFilter
argument_list|>
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|NameKey
argument_list|,
name|ProviderInfo
argument_list|<
name|ContainerRequestFilter
argument_list|>
argument_list|>
name|postMatchContainerRequestFilters
init|=
operator|new
name|NameKeyMap
argument_list|<
name|ProviderInfo
argument_list|<
name|ContainerRequestFilter
argument_list|>
argument_list|>
argument_list|(
literal|true
argument_list|)
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|NameKey
argument_list|,
name|ProviderInfo
argument_list|<
name|ContainerResponseFilter
argument_list|>
argument_list|>
name|postMatchContainerResponseFilters
init|=
operator|new
name|NameKeyMap
argument_list|<
name|ProviderInfo
argument_list|<
name|ContainerResponseFilter
argument_list|>
argument_list|>
argument_list|(
literal|false
argument_list|)
decl_stmt|;
specifier|private
name|RequestPreprocessor
name|requestPreprocessor
decl_stmt|;
specifier|private
name|ProviderInfo
argument_list|<
name|Application
argument_list|>
name|application
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|DynamicFeature
argument_list|>
name|dynamicFeatures
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|DynamicFeature
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|BeanParamInfo
argument_list|>
name|beanParams
init|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|BeanParamInfo
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|ProviderInfo
argument_list|<
name|ContainerRequestFilter
argument_list|>
name|wadlGenerator
decl_stmt|;
specifier|private
name|ServerProviderFactory
parameter_list|(
name|ProviderFactory
name|baseFactory
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|super
argument_list|(
name|baseFactory
argument_list|,
name|bus
argument_list|)
expr_stmt|;
if|if
condition|(
name|baseFactory
operator|==
literal|null
condition|)
block|{
name|wadlGenerator
operator|=
name|createWadlGenerator
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|ProviderInfo
argument_list|<
name|ContainerRequestFilter
argument_list|>
name|createWadlGenerator
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|Object
name|provider
init|=
name|createProvider
argument_list|(
name|WADL_PROVIDER_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|provider
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
operator|new
name|ProviderInfo
argument_list|<
name|ContainerRequestFilter
argument_list|>
argument_list|(
operator|(
name|ContainerRequestFilter
operator|)
name|provider
argument_list|,
name|bus
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|ServerProviderFactory
name|getInstance
parameter_list|()
block|{
return|return
name|createInstance
argument_list|(
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|ServerProviderFactory
name|createInstance
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
name|bus
operator|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
expr_stmt|;
block|}
name|ServerProviderFactory
name|baseFactory
init|=
name|initBaseFactory
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|ServerProviderFactory
name|factory
init|=
operator|new
name|ServerProviderFactory
argument_list|(
name|baseFactory
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|factory
operator|.
name|setBusProviders
argument_list|()
expr_stmt|;
return|return
name|factory
return|;
block|}
specifier|public
specifier|static
name|ServerProviderFactory
name|getInstance
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|Endpoint
name|e
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|(
name|ServerProviderFactory
operator|)
name|e
operator|.
name|get
argument_list|(
name|SERVER_FACTORY_NAME
argument_list|)
return|;
block|}
specifier|private
specifier|static
specifier|synchronized
name|ServerProviderFactory
name|initBaseFactory
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|ServerProviderFactory
name|factory
init|=
operator|(
name|ServerProviderFactory
operator|)
name|bus
operator|.
name|getProperty
argument_list|(
name|SHARED_SERVER_FACTORY
argument_list|)
decl_stmt|;
if|if
condition|(
name|factory
operator|!=
literal|null
condition|)
block|{
return|return
name|factory
return|;
block|}
name|factory
operator|=
operator|new
name|ServerProviderFactory
argument_list|(
literal|null
argument_list|,
name|bus
argument_list|)
expr_stmt|;
name|ProviderFactory
operator|.
name|initBaseFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setProviders
argument_list|(
operator|new
name|WebApplicationExceptionMapper
argument_list|()
argument_list|)
expr_stmt|;
name|bus
operator|.
name|setProperty
argument_list|(
name|SHARED_SERVER_FACTORY
argument_list|,
name|factory
argument_list|)
expr_stmt|;
return|return
name|factory
return|;
block|}
specifier|public
name|List
argument_list|<
name|ProviderInfo
argument_list|<
name|ContainerRequestFilter
argument_list|>
argument_list|>
name|getPreMatchContainerRequestFilters
parameter_list|()
block|{
return|return
name|getContainerRequestFilters
argument_list|(
name|preMatchContainerRequestFilters
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|ProviderInfo
argument_list|<
name|ContainerRequestFilter
argument_list|>
argument_list|>
name|getPostMatchContainerRequestFilters
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|names
parameter_list|)
block|{
return|return
name|getPostMatchContainerFilters
argument_list|(
name|postMatchContainerRequestFilters
argument_list|,
name|names
argument_list|)
return|;
block|}
specifier|private
name|List
argument_list|<
name|ProviderInfo
argument_list|<
name|ContainerRequestFilter
argument_list|>
argument_list|>
name|getContainerRequestFilters
parameter_list|(
name|List
argument_list|<
name|ProviderInfo
argument_list|<
name|ContainerRequestFilter
argument_list|>
argument_list|>
name|filters
parameter_list|,
name|boolean
name|syncNeeded
parameter_list|)
block|{
name|ProviderInfo
argument_list|<
name|ContainerRequestFilter
argument_list|>
name|generator
init|=
name|wadlGenerator
operator|!=
literal|null
condition|?
name|wadlGenerator
else|:
operator|(
operator|(
name|ServerProviderFactory
operator|)
name|getBaseFactory
argument_list|()
operator|)
operator|.
name|wadlGenerator
decl_stmt|;
if|if
condition|(
name|generator
operator|==
literal|null
condition|)
block|{
return|return
name|filters
return|;
block|}
if|if
condition|(
name|filters
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|generator
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|syncNeeded
condition|)
block|{
name|filters
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|generator
argument_list|)
expr_stmt|;
return|return
name|filters
return|;
block|}
else|else
block|{
synchronized|synchronized
init|(
name|filters
init|)
block|{
if|if
condition|(
name|filters
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|!=
name|generator
condition|)
block|{
name|filters
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|generator
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|filters
return|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|ProviderInfo
argument_list|<
name|ContainerResponseFilter
argument_list|>
argument_list|>
name|getContainerResponseFilters
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|names
parameter_list|)
block|{
return|return
name|getPostMatchContainerFilters
argument_list|(
name|postMatchContainerResponseFilters
argument_list|,
name|names
argument_list|)
return|;
block|}
specifier|public
name|void
name|addBeanParamInfo
parameter_list|(
name|BeanParamInfo
name|bpi
parameter_list|)
block|{
name|beanParams
operator|.
name|put
argument_list|(
name|bpi
operator|.
name|getResourceClass
argument_list|()
argument_list|,
name|bpi
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BeanParamInfo
name|getBeanParamInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|beanClass
parameter_list|)
block|{
return|return
name|beanParams
operator|.
name|get
argument_list|(
name|beanClass
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
extends|extends
name|Throwable
parameter_list|>
name|ExceptionMapper
argument_list|<
name|T
argument_list|>
name|createExceptionMapper
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|exceptionType
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|ExceptionMapper
argument_list|<
name|T
argument_list|>
name|mapper
init|=
name|doCreateExceptionMapper
argument_list|(
name|exceptionType
argument_list|,
name|m
argument_list|)
decl_stmt|;
if|if
condition|(
name|mapper
operator|!=
literal|null
operator|||
name|isBaseFactory
argument_list|()
condition|)
block|{
return|return
name|mapper
return|;
block|}
return|return
operator|(
operator|(
name|ServerProviderFactory
operator|)
name|getBaseFactory
argument_list|()
operator|)
operator|.
name|createExceptionMapper
argument_list|(
name|exceptionType
argument_list|,
name|m
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
parameter_list|<
name|T
extends|extends
name|Throwable
parameter_list|>
name|ExceptionMapper
argument_list|<
name|T
argument_list|>
name|doCreateExceptionMapper
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|exceptionType
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|List
argument_list|<
name|ExceptionMapper
argument_list|<
name|?
argument_list|>
argument_list|>
name|candidates
init|=
operator|new
name|LinkedList
argument_list|<
name|ExceptionMapper
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ProviderInfo
argument_list|<
name|ExceptionMapper
argument_list|<
name|?
argument_list|>
argument_list|>
name|em
range|:
name|exceptionMappers
control|)
block|{
name|handleMapper
argument_list|(
name|candidates
argument_list|,
name|em
argument_list|,
name|exceptionType
argument_list|,
name|m
argument_list|,
name|ExceptionMapper
operator|.
name|class
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|candidates
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|candidates
argument_list|,
operator|new
name|ClassComparator
argument_list|(
name|exceptionType
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|(
name|ExceptionMapper
argument_list|<
name|T
argument_list|>
operator|)
name|candidates
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|protected
name|void
name|setProviders
parameter_list|(
name|Object
modifier|...
name|providers
parameter_list|)
block|{
name|List
argument_list|<
name|ProviderInfo
argument_list|<
name|ContainerRequestFilter
argument_list|>
argument_list|>
name|postMatchRequestFilters
init|=
operator|new
name|LinkedList
argument_list|<
name|ProviderInfo
argument_list|<
name|ContainerRequestFilter
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ProviderInfo
argument_list|<
name|ContainerResponseFilter
argument_list|>
argument_list|>
name|postMatchResponseFilters
init|=
operator|new
name|LinkedList
argument_list|<
name|ProviderInfo
argument_list|<
name|ContainerResponseFilter
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ProviderInfo
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
argument_list|>
name|theProviders
init|=
name|prepareProviders
argument_list|(
operator|(
name|Object
index|[]
operator|)
name|providers
argument_list|,
name|application
argument_list|)
decl_stmt|;
name|super
operator|.
name|setCommonProviders
argument_list|(
name|theProviders
argument_list|)
expr_stmt|;
for|for
control|(
name|ProviderInfo
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|provider
range|:
name|theProviders
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|providerCls
init|=
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|provider
operator|.
name|getProvider
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|filterContractSupported
argument_list|(
name|provider
argument_list|,
name|providerCls
argument_list|,
name|ContainerRequestFilter
operator|.
name|class
argument_list|)
condition|)
block|{
name|addContainerRequestFilter
argument_list|(
name|postMatchRequestFilters
argument_list|,
operator|(
name|ProviderInfo
argument_list|<
name|ContainerRequestFilter
argument_list|>
operator|)
name|provider
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|filterContractSupported
argument_list|(
name|provider
argument_list|,
name|providerCls
argument_list|,
name|ContainerResponseFilter
operator|.
name|class
argument_list|)
condition|)
block|{
name|postMatchResponseFilters
operator|.
name|add
argument_list|(
operator|(
name|ProviderInfo
argument_list|<
name|ContainerResponseFilter
argument_list|>
operator|)
name|provider
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|DynamicFeature
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|providerCls
argument_list|)
condition|)
block|{
comment|//TODO: review the possibility of DynamicFeatures needing to have Contexts injected
name|Object
name|feature
init|=
name|provider
operator|.
name|getProvider
argument_list|()
decl_stmt|;
name|dynamicFeatures
operator|.
name|add
argument_list|(
operator|(
name|DynamicFeature
operator|)
name|feature
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ExceptionMapper
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|providerCls
argument_list|)
condition|)
block|{
name|addProviderToList
argument_list|(
name|exceptionMappers
argument_list|,
name|provider
argument_list|)
expr_stmt|;
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|preMatchContainerRequestFilters
argument_list|,
operator|new
name|BindingPriorityComparator
argument_list|(
name|ContainerRequestFilter
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|mapInterceptorFilters
argument_list|(
name|postMatchContainerRequestFilters
argument_list|,
name|postMatchRequestFilters
argument_list|,
name|ContainerRequestFilter
operator|.
name|class
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|mapInterceptorFilters
argument_list|(
name|postMatchContainerResponseFilters
argument_list|,
name|postMatchResponseFilters
argument_list|,
name|ContainerResponseFilter
operator|.
name|class
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|injectContextProxies
argument_list|(
name|exceptionMappers
argument_list|,
name|postMatchContainerRequestFilters
operator|.
name|values
argument_list|()
argument_list|,
name|preMatchContainerRequestFilters
argument_list|,
name|postMatchContainerResponseFilters
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|injectContextProxiesIntoProvider
parameter_list|(
name|ProviderInfo
argument_list|<
name|?
argument_list|>
name|pi
parameter_list|)
block|{
name|injectContextProxiesIntoProvider
argument_list|(
name|pi
argument_list|,
name|application
operator|==
literal|null
condition|?
literal|null
else|:
name|application
operator|.
name|getProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|injectContextValues
parameter_list|(
name|ProviderInfo
argument_list|<
name|?
argument_list|>
name|pi
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
name|InjectionUtils
operator|.
name|injectContexts
argument_list|(
name|pi
operator|.
name|getProvider
argument_list|()
argument_list|,
name|pi
argument_list|,
name|m
argument_list|)
expr_stmt|;
if|if
condition|(
name|application
operator|!=
literal|null
operator|&&
name|application
operator|.
name|contextsAvailable
argument_list|()
condition|)
block|{
name|InjectionUtils
operator|.
name|injectContexts
argument_list|(
name|application
operator|.
name|getProvider
argument_list|()
argument_list|,
name|application
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|addContainerRequestFilter
parameter_list|(
name|List
argument_list|<
name|ProviderInfo
argument_list|<
name|ContainerRequestFilter
argument_list|>
argument_list|>
name|postMatchFilters
parameter_list|,
name|ProviderInfo
argument_list|<
name|ContainerRequestFilter
argument_list|>
name|p
parameter_list|)
block|{
name|ContainerRequestFilter
name|filter
init|=
name|p
operator|.
name|getProvider
argument_list|()
decl_stmt|;
if|if
condition|(
name|isWadlGenerator
argument_list|(
name|filter
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|wadlGenerator
operator|=
name|p
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|isPrematching
argument_list|(
name|filter
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|addProviderToList
argument_list|(
name|preMatchContainerRequestFilters
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|postMatchFilters
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
name|boolean
name|isWadlGenerator
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|filterCls
parameter_list|)
block|{
if|if
condition|(
name|filterCls
operator|==
literal|null
operator|||
name|filterCls
operator|==
name|Object
operator|.
name|class
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|WADL_PROVIDER_NAME
operator|.
name|equals
argument_list|(
name|filterCls
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
name|isWadlGenerator
argument_list|(
name|filterCls
operator|.
name|getSuperclass
argument_list|()
argument_list|)
return|;
block|}
block|}
specifier|public
name|RequestPreprocessor
name|getRequestPreprocessor
parameter_list|()
block|{
return|return
name|requestPreprocessor
return|;
block|}
specifier|public
name|void
name|setApplicationProvider
parameter_list|(
name|ProviderInfo
argument_list|<
name|Application
argument_list|>
name|app
parameter_list|)
block|{
name|application
operator|=
name|app
expr_stmt|;
block|}
specifier|public
name|ProviderInfo
argument_list|<
name|Application
argument_list|>
name|getApplicationProvider
parameter_list|()
block|{
return|return
name|application
return|;
block|}
specifier|public
name|void
name|setRequestPreprocessor
parameter_list|(
name|RequestPreprocessor
name|rp
parameter_list|)
block|{
name|this
operator|.
name|requestPreprocessor
operator|=
name|rp
expr_stmt|;
block|}
specifier|public
name|void
name|clearExceptionMapperProxies
parameter_list|()
block|{
name|clearProxies
argument_list|(
name|exceptionMappers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|clearProviders
parameter_list|()
block|{
name|super
operator|.
name|clearProviders
argument_list|()
expr_stmt|;
name|exceptionMappers
operator|.
name|clear
argument_list|()
expr_stmt|;
name|postMatchContainerRequestFilters
operator|.
name|clear
argument_list|()
expr_stmt|;
name|postMatchContainerResponseFilters
operator|.
name|clear
argument_list|()
expr_stmt|;
name|preMatchContainerRequestFilters
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|clearThreadLocalProxies
parameter_list|()
block|{
if|if
condition|(
name|application
operator|!=
literal|null
condition|)
block|{
name|application
operator|.
name|clearThreadLocalProxies
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|clearThreadLocalProxies
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|applyDynamicFeatures
parameter_list|(
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|list
parameter_list|)
block|{
if|if
condition|(
name|dynamicFeatures
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|ClassResourceInfo
name|cri
range|:
name|list
control|)
block|{
name|doApplyDynamicFeatures
argument_list|(
name|cri
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|doApplyDynamicFeatures
parameter_list|(
name|ClassResourceInfo
name|cri
parameter_list|)
block|{
name|Set
argument_list|<
name|OperationResourceInfo
argument_list|>
name|oris
init|=
name|cri
operator|.
name|getMethodDispatcher
argument_list|()
operator|.
name|getOperationResourceInfos
argument_list|()
decl_stmt|;
for|for
control|(
name|OperationResourceInfo
name|ori
range|:
name|oris
control|)
block|{
for|for
control|(
name|DynamicFeature
name|feature
range|:
name|dynamicFeatures
control|)
block|{
name|FeatureContext
name|methodConfigurable
init|=
operator|new
name|MethodFeatureContextImpl
argument_list|(
name|ori
argument_list|)
decl_stmt|;
name|feature
operator|.
name|configure
argument_list|(
operator|new
name|ResourceInfoImpl
argument_list|(
name|ori
argument_list|)
argument_list|,
name|methodConfigurable
argument_list|)
expr_stmt|;
block|}
block|}
name|Collection
argument_list|<
name|ClassResourceInfo
argument_list|>
name|subs
init|=
name|cri
operator|.
name|getSubResources
argument_list|()
decl_stmt|;
for|for
control|(
name|ClassResourceInfo
name|sub
range|:
name|subs
control|)
block|{
if|if
condition|(
name|sub
operator|!=
name|cri
condition|)
block|{
name|doApplyDynamicFeatures
argument_list|(
name|sub
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
specifier|static
name|boolean
name|isPrematching
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|filterCls
parameter_list|)
block|{
return|return
name|AnnotationUtils
operator|.
name|getClassAnnotation
argument_list|(
name|filterCls
argument_list|,
name|PreMatching
operator|.
name|class
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|private
class|class
name|MethodFeatureContextImpl
implements|implements
name|FeatureContext
block|{
specifier|private
name|Configurable
argument_list|<
name|FeatureContext
argument_list|>
name|configImpl
decl_stmt|;
specifier|private
name|OperationResourceInfo
name|ori
decl_stmt|;
specifier|private
name|String
name|nameBinding
decl_stmt|;
specifier|public
name|MethodFeatureContextImpl
parameter_list|(
name|OperationResourceInfo
name|ori
parameter_list|)
block|{
name|this
operator|.
name|ori
operator|=
name|ori
expr_stmt|;
name|configImpl
operator|=
operator|new
name|MethodFeatureContextConfigurable
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|nameBinding
operator|=
name|DEFAULT_FILTER_NAME_BINDING
operator|+
name|ori
operator|.
name|getClassResourceInfo
argument_list|()
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configImpl
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|FeatureContext
name|property
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|configImpl
operator|.
name|property
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|FeatureContext
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|configImpl
operator|.
name|register
argument_list|(
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|FeatureContext
name|register
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|configImpl
operator|.
name|register
argument_list|(
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|FeatureContext
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|int
name|index
parameter_list|)
block|{
return|return
name|configImpl
operator|.
name|register
argument_list|(
name|cls
argument_list|,
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|FeatureContext
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|contracts
parameter_list|)
block|{
return|return
name|configImpl
operator|.
name|register
argument_list|(
name|cls
argument_list|,
name|contracts
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|FeatureContext
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|map
parameter_list|)
block|{
return|return
name|configImpl
operator|.
name|register
argument_list|(
name|cls
argument_list|,
name|map
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|FeatureContext
name|register
parameter_list|(
name|Object
name|object
parameter_list|,
name|int
name|index
parameter_list|)
block|{
return|return
name|configImpl
operator|.
name|register
argument_list|(
name|object
argument_list|,
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|FeatureContext
name|register
parameter_list|(
name|Object
name|object
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|contracts
parameter_list|)
block|{
return|return
name|configImpl
operator|.
name|register
argument_list|(
name|object
argument_list|,
name|contracts
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|FeatureContext
name|register
parameter_list|(
name|Object
name|object
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|map
parameter_list|)
block|{
return|return
name|configImpl
operator|.
name|register
argument_list|(
name|object
argument_list|,
name|map
argument_list|)
return|;
block|}
name|FeatureContext
name|doRegister
parameter_list|(
name|Object
name|provider
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|contracts
parameter_list|)
block|{
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|actualContracts
init|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|contract
range|:
name|contracts
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|SERVER_FILTER_INTERCEPTOR_CLASSES
operator|.
name|contains
argument_list|(
name|contract
argument_list|)
operator|&&
name|contract
operator|.
name|isAssignableFrom
argument_list|(
name|provider
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|actualContracts
operator|.
name|put
argument_list|(
name|contract
argument_list|,
name|contracts
operator|.
name|get
argument_list|(
name|contract
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|actualContracts
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|registerUserProvider
argument_list|(
operator|new
name|FilterProviderInfo
argument_list|<
name|Object
argument_list|>
argument_list|(
name|provider
argument_list|,
name|getBus
argument_list|()
argument_list|,
name|nameBinding
argument_list|,
name|actualContracts
argument_list|)
argument_list|)
expr_stmt|;
name|ori
operator|.
name|addNameBindings
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|nameBinding
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
block|}
specifier|private
specifier|static
class|class
name|MethodFeatureContextConfigurable
extends|extends
name|ConfigurableImpl
argument_list|<
name|FeatureContext
argument_list|>
block|{
specifier|protected
name|MethodFeatureContextConfigurable
parameter_list|(
name|MethodFeatureContextImpl
name|mc
parameter_list|)
block|{
name|super
argument_list|(
name|mc
argument_list|,
name|RuntimeType
operator|.
name|SERVER
argument_list|,
name|SERVER_FILTER_INTERCEPTOR_CLASSES
operator|.
name|toArray
argument_list|(
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{}
block|)
block|)
class|;
block|}
end_class

begin_function
annotation|@
name|Override
specifier|public
name|FeatureContext
name|register
parameter_list|(
name|Object
name|provider
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|contracts
parameter_list|)
block|{
name|super
operator|.
name|register
argument_list|(
name|provider
argument_list|,
name|contracts
argument_list|)
expr_stmt|;
return|return
operator|(
operator|(
name|MethodFeatureContextImpl
operator|)
name|super
operator|.
name|getConfigurable
argument_list|()
operator|)
operator|.
name|doRegister
argument_list|(
name|provider
argument_list|,
name|contracts
argument_list|)
return|;
block|}
end_function

unit|}      }
end_unit

