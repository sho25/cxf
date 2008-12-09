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
name|Method
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
name|Arrays
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
name|ResourceBundle
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executor
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
name|Path
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
name|i18n
operator|.
name|BundleUtils
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
name|jaxrs
operator|.
name|lifecycle
operator|.
name|PerRequestResourceProvider
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
name|lifecycle
operator|.
name|SingletonResourceProvider
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
name|MethodDispatcher
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
name|URITemplate
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
name|service
operator|.
name|Service
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
name|AbstractServiceFactoryBean
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
name|invoker
operator|.
name|Invoker
import|;
end_import

begin_comment
comment|/**  * Build a JAX-RS service model from resource classes.  */
end_comment

begin_class
specifier|public
class|class
name|JAXRSServiceFactoryBean
extends|extends
name|AbstractServiceFactoryBean
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
name|JAXRSServiceFactoryBean
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|JAXRSServiceFactoryBean
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|classResourceInfos
init|=
operator|new
name|ArrayList
argument_list|<
name|ClassResourceInfo
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|Class
argument_list|,
name|ResourceProvider
argument_list|>
name|resourceProviders
init|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|,
name|ResourceProvider
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Invoker
name|invoker
decl_stmt|;
specifier|private
name|Executor
name|executor
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
decl_stmt|;
specifier|public
name|JAXRSServiceFactoryBean
parameter_list|()
block|{     }
annotation|@
name|Override
specifier|public
name|Service
name|create
parameter_list|()
block|{
name|initializeServiceModel
argument_list|()
expr_stmt|;
name|initializeDefaultInterceptors
argument_list|()
expr_stmt|;
if|if
condition|(
name|invoker
operator|!=
literal|null
condition|)
block|{
name|getService
argument_list|()
operator|.
name|setInvoker
argument_list|(
name|getInvoker
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getService
argument_list|()
operator|.
name|setInvoker
argument_list|(
name|createInvoker
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getExecutor
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getService
argument_list|()
operator|.
name|setExecutor
argument_list|(
name|getExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getDataBinding
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getService
argument_list|()
operator|.
name|setDataBinding
argument_list|(
name|getDataBinding
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|getService
argument_list|()
return|;
block|}
specifier|public
name|Executor
name|getExecutor
parameter_list|()
block|{
return|return
name|executor
return|;
block|}
specifier|public
name|void
name|setExecutor
parameter_list|(
name|Executor
name|executor
parameter_list|)
block|{
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
block|}
specifier|public
name|Invoker
name|getInvoker
parameter_list|()
block|{
return|return
name|invoker
return|;
block|}
specifier|public
name|void
name|setInvoker
parameter_list|(
name|Invoker
name|invoker
parameter_list|)
block|{
name|this
operator|.
name|invoker
operator|=
name|invoker
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Class
argument_list|>
name|getResourceClasses
parameter_list|()
block|{
name|List
argument_list|<
name|Class
argument_list|>
name|resourceClasses
init|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ClassResourceInfo
name|cri
range|:
name|classResourceInfos
control|)
block|{
name|resourceClasses
operator|.
name|add
argument_list|(
name|cri
operator|.
name|getResourceClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|resourceClasses
return|;
block|}
specifier|public
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|getClassResourceInfo
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|classResourceInfos
argument_list|)
return|;
block|}
specifier|public
name|void
name|setResourceClasses
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|>
name|classes
parameter_list|)
block|{
for|for
control|(
name|Class
name|resourceClass
range|:
name|classes
control|)
block|{
name|ClassResourceInfo
name|classResourceInfo
init|=
name|createClassResourceInfo
argument_list|(
name|resourceClass
argument_list|,
name|resourceClass
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|classResourceInfo
operator|!=
literal|null
condition|)
block|{
name|classResourceInfos
operator|.
name|add
argument_list|(
name|classResourceInfo
argument_list|)
expr_stmt|;
block|}
block|}
name|injectContexts
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setResourceClasses
parameter_list|(
name|Class
modifier|...
name|classes
parameter_list|)
block|{
name|setResourceClasses
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|classes
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setResourceClassesFromBeans
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|beans
parameter_list|)
block|{
for|for
control|(
name|Object
name|bean
range|:
name|beans
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|realClass
init|=
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|bean
argument_list|)
decl_stmt|;
name|ClassResourceInfo
name|classResourceInfo
init|=
name|createClassResourceInfo
argument_list|(
name|bean
operator|.
name|getClass
argument_list|()
argument_list|,
name|realClass
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|classResourceInfo
operator|!=
literal|null
condition|)
block|{
name|classResourceInfos
operator|.
name|add
argument_list|(
name|classResourceInfo
argument_list|)
expr_stmt|;
name|classResourceInfo
operator|.
name|setResourceProvider
argument_list|(
operator|new
name|SingletonResourceProvider
argument_list|(
name|bean
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|injectContexts
parameter_list|()
block|{
for|for
control|(
name|ClassResourceInfo
name|cri
range|:
name|classResourceInfos
control|)
block|{
if|if
condition|(
name|cri
operator|.
name|isSingleton
argument_list|()
condition|)
block|{
name|InjectionUtils
operator|.
name|injectContextProxies
argument_list|(
name|cri
argument_list|,
name|cri
operator|.
name|getResourceProvider
argument_list|()
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|setResourceProvider
parameter_list|(
name|Class
name|c
parameter_list|,
name|ResourceProvider
name|rp
parameter_list|)
block|{
name|resourceProviders
operator|.
name|put
argument_list|(
name|c
argument_list|,
name|rp
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|initializeServiceModel
parameter_list|()
block|{
name|updateClassResourceProviders
argument_list|()
expr_stmt|;
name|JAXRSServiceImpl
name|service
init|=
operator|new
name|JAXRSServiceImpl
argument_list|(
name|classResourceInfos
argument_list|)
decl_stmt|;
name|setService
argument_list|(
name|service
argument_list|)
expr_stmt|;
if|if
condition|(
name|properties
operator|!=
literal|null
condition|)
block|{
name|service
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|updateClassResourceProviders
parameter_list|()
block|{
for|for
control|(
name|ClassResourceInfo
name|cri
range|:
name|classResourceInfos
control|)
block|{
if|if
condition|(
name|cri
operator|.
name|getResourceProvider
argument_list|()
operator|!=
literal|null
condition|)
block|{
continue|continue;
block|}
name|ResourceProvider
name|rp
init|=
name|resourceProviders
operator|.
name|get
argument_list|(
name|cri
operator|.
name|getResourceClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|rp
operator|!=
literal|null
condition|)
block|{
name|cri
operator|.
name|setResourceProvider
argument_list|(
name|rp
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//default lifecycle is per-request
name|rp
operator|=
operator|new
name|PerRequestResourceProvider
argument_list|(
name|cri
operator|.
name|getResourceClass
argument_list|()
argument_list|)
expr_stmt|;
name|cri
operator|.
name|setResourceProvider
argument_list|(
name|rp
argument_list|)
expr_stmt|;
block|}
block|}
name|injectContexts
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|ClassResourceInfo
name|createClassResourceInfo
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|rClass
parameter_list|,
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|sClass
parameter_list|,
name|boolean
name|root
parameter_list|)
block|{
name|ClassResourceInfo
name|cri
init|=
operator|new
name|ClassResourceInfo
argument_list|(
name|rClass
argument_list|,
name|sClass
argument_list|,
name|root
argument_list|)
decl_stmt|;
if|if
condition|(
name|root
condition|)
block|{
name|URITemplate
name|t
init|=
name|URITemplate
operator|.
name|createTemplate
argument_list|(
name|cri
argument_list|,
name|cri
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
name|cri
operator|.
name|setURITemplate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
name|MethodDispatcher
name|md
init|=
name|createOperation
argument_list|(
name|cri
argument_list|)
decl_stmt|;
name|cri
operator|.
name|setMethodDispatcher
argument_list|(
name|md
argument_list|)
expr_stmt|;
return|return
name|checkMethodDispatcher
argument_list|(
name|cri
argument_list|)
condition|?
name|cri
else|:
literal|null
return|;
block|}
specifier|protected
name|MethodDispatcher
name|createOperation
parameter_list|(
name|ClassResourceInfo
name|cri
parameter_list|)
block|{
name|MethodDispatcher
name|md
init|=
operator|new
name|MethodDispatcher
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|m
range|:
name|cri
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getMethods
argument_list|()
control|)
block|{
name|Method
name|annotatedMethod
init|=
name|AnnotationUtils
operator|.
name|getAnnotatedMethod
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|String
name|httpMethod
init|=
name|AnnotationUtils
operator|.
name|getHttpMethodValue
argument_list|(
name|annotatedMethod
argument_list|)
decl_stmt|;
name|Path
name|path
init|=
operator|(
name|Path
operator|)
name|AnnotationUtils
operator|.
name|getMethodAnnotation
argument_list|(
name|annotatedMethod
argument_list|,
name|Path
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|httpMethod
operator|!=
literal|null
operator|||
name|path
operator|!=
literal|null
condition|)
block|{
name|md
operator|.
name|bind
argument_list|(
name|createOperationInfo
argument_list|(
name|m
argument_list|,
name|annotatedMethod
argument_list|,
name|cri
argument_list|,
name|path
argument_list|,
name|httpMethod
argument_list|)
argument_list|,
name|m
argument_list|)
expr_stmt|;
if|if
condition|(
name|httpMethod
operator|==
literal|null
condition|)
block|{
comment|// subresource locator
name|Class
name|subResourceClass
init|=
name|m
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
name|ClassResourceInfo
name|subCri
init|=
name|createClassResourceInfo
argument_list|(
name|subResourceClass
argument_list|,
name|subResourceClass
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|checkMethodDispatcher
argument_list|(
name|subCri
argument_list|)
condition|)
block|{
name|cri
operator|.
name|addSubClassResourceInfo
argument_list|(
name|subCri
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|md
return|;
block|}
specifier|private
name|OperationResourceInfo
name|createOperationInfo
parameter_list|(
name|Method
name|m
parameter_list|,
name|Method
name|annotatedMethod
parameter_list|,
name|ClassResourceInfo
name|cri
parameter_list|,
name|Path
name|path
parameter_list|,
name|String
name|httpMethod
parameter_list|)
block|{
name|OperationResourceInfo
name|ori
init|=
operator|new
name|OperationResourceInfo
argument_list|(
name|m
argument_list|,
name|cri
argument_list|)
decl_stmt|;
name|URITemplate
name|t
init|=
name|URITemplate
operator|.
name|createTemplate
argument_list|(
name|cri
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|ori
operator|.
name|setURITemplate
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|ori
operator|.
name|setHttpMethod
argument_list|(
name|httpMethod
argument_list|)
expr_stmt|;
name|ori
operator|.
name|setAnnotatedMethod
argument_list|(
name|annotatedMethod
argument_list|)
expr_stmt|;
return|return
name|ori
return|;
block|}
specifier|private
name|boolean
name|checkMethodDispatcher
parameter_list|(
name|ClassResourceInfo
name|cr
parameter_list|)
block|{
if|if
condition|(
name|cr
operator|.
name|getMethodDispatcher
argument_list|()
operator|.
name|getOperationResourceInfos
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"NO_RESOURCE_OP_EXC"
argument_list|,
name|BUNDLE
argument_list|,
name|cr
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
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
name|Invoker
name|createInvoker
parameter_list|()
block|{
return|return
operator|new
name|JAXRSInvoker
argument_list|()
return|;
block|}
block|}
end_class

end_unit

