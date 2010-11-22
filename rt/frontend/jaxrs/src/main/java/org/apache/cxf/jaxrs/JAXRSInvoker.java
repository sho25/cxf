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
name|Collections
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
name|ResourceBundle
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
name|WebApplicationException
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
name|MediaType
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
name|MultivaluedMap
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
name|Response
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
name|Fault
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
name|InterceptorChain
operator|.
name|State
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
name|MetadataMap
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
name|MethodInvocationInfo
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
name|OperationResourceInfoStack
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
name|Parameter
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
name|ParameterType
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
name|provider
operator|.
name|ProviderFactory
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
name|jaxrs
operator|.
name|utils
operator|.
name|JAXRSUtils
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
name|Exchange
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
name|MessageContentsList
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
name|AbstractInvoker
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSInvoker
extends|extends
name|AbstractInvoker
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
name|JAXRSInvoker
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SERVICE_LOADER_AS_CONTEXT
init|=
literal|"org.apache.cxf.serviceloader-context"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SERVICE_OBJECT_SCOPE
init|=
literal|"org.apache.cxf.service.scope"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST_SCOPE
init|=
literal|"request"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|LAST_SERVICE_OBJECT
init|=
literal|"org.apache.cxf.service.object.last"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST_WAS_SUSPENDED
init|=
literal|"org.apache.cxf.service.request.suspended"
decl_stmt|;
specifier|public
name|JAXRSInvoker
parameter_list|()
block|{     }
specifier|public
name|Object
name|invoke
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|request
parameter_list|)
block|{
name|Response
name|response
init|=
name|exchange
operator|.
name|get
argument_list|(
name|Response
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|response
operator|!=
literal|null
condition|)
block|{
comment|// this means a blocking request filter provided a Response
comment|// or earlier exception has been converted to Response
comment|//TODO: should we remove response from exchange ?
comment|//      or should we rather ignore content list and have
comment|//      Response set here for all cases and extract it
comment|//      in the out interceptor instead of dealing with the contents list ?
return|return
operator|new
name|MessageContentsList
argument_list|(
name|response
argument_list|)
return|;
block|}
name|ResourceProvider
name|provider
init|=
name|getResourceProvider
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Object
name|rootInstance
init|=
name|getServiceObject
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Object
name|serviceObject
init|=
name|getActualServiceObject
argument_list|(
name|exchange
argument_list|,
name|rootInstance
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|invoke
argument_list|(
name|exchange
argument_list|,
name|request
argument_list|,
name|serviceObject
argument_list|)
return|;
block|}
finally|finally
block|{
name|boolean
name|suspended
init|=
name|exchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|getState
argument_list|()
operator|==
name|State
operator|.
name|SUSPENDED
decl_stmt|;
if|if
condition|(
operator|!
name|suspended
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
name|ProviderFactory
operator|.
name|getInstance
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
operator|.
name|clearThreadLocalProxies
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|isServiceObjectRequestScope
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
condition|)
block|{
name|provider
operator|.
name|releaseInstance
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|,
name|rootInstance
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|persistRoots
argument_list|(
name|exchange
argument_list|,
name|rootInstance
argument_list|,
name|provider
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|persistRoots
argument_list|(
name|exchange
argument_list|,
name|rootInstance
argument_list|,
name|provider
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|REQUEST_WAS_SUSPENDED
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|persistRoots
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|rootInstance
parameter_list|,
name|Object
name|provider
parameter_list|)
block|{
name|exchange
operator|.
name|put
argument_list|(
name|JAXRSUtils
operator|.
name|ROOT_INSTANCE
argument_list|,
name|rootInstance
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|JAXRSUtils
operator|.
name|ROOT_PROVIDER
argument_list|,
name|provider
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|Object
name|invoke
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|request
parameter_list|,
name|Object
name|resourceObject
parameter_list|)
block|{
name|OperationResourceInfo
name|ori
init|=
name|exchange
operator|.
name|get
argument_list|(
name|OperationResourceInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|ClassResourceInfo
name|cri
init|=
name|ori
operator|.
name|getClassResourceInfo
argument_list|()
decl_stmt|;
name|boolean
name|wasSuspended
init|=
name|exchange
operator|.
name|remove
argument_list|(
name|REQUEST_WAS_SUSPENDED
argument_list|)
operator|!=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|wasSuspended
condition|)
block|{
name|pushOntoStack
argument_list|(
name|ori
argument_list|,
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|resourceObject
argument_list|)
argument_list|,
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cri
operator|.
name|isRoot
argument_list|()
condition|)
block|{
name|JAXRSUtils
operator|.
name|handleSetters
argument_list|(
name|ori
argument_list|,
name|resourceObject
argument_list|,
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
expr_stmt|;
name|InjectionUtils
operator|.
name|injectContextFields
argument_list|(
name|resourceObject
argument_list|,
name|ori
operator|.
name|getClassResourceInfo
argument_list|()
argument_list|,
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
expr_stmt|;
name|InjectionUtils
operator|.
name|injectResourceFields
argument_list|(
name|resourceObject
argument_list|,
name|ori
operator|.
name|getClassResourceInfo
argument_list|()
argument_list|,
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Method
name|methodToInvoke
init|=
name|InjectionUtils
operator|.
name|checkProxy
argument_list|(
name|cri
operator|.
name|getMethodDispatcher
argument_list|()
operator|.
name|getMethod
argument_list|(
name|ori
argument_list|)
argument_list|,
name|resourceObject
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|params
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|request
operator|instanceof
name|List
condition|)
block|{
name|params
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|request
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|request
operator|!=
literal|null
condition|)
block|{
name|params
operator|=
operator|new
name|MessageContentsList
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
name|Object
name|result
init|=
literal|null
decl_stmt|;
name|ClassLoader
name|contextLoader
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|setServiceLoaderAsContextLoader
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
condition|)
block|{
name|contextLoader
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|resourceObject
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|result
operator|=
name|invoke
argument_list|(
name|exchange
argument_list|,
name|resourceObject
argument_list|,
name|methodToInvoke
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Fault
name|ex
parameter_list|)
block|{
name|Response
name|excResponse
init|=
name|JAXRSUtils
operator|.
name|convertFaultToResponse
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
argument_list|,
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|excResponse
operator|==
literal|null
condition|)
block|{
name|ProviderFactory
operator|.
name|getInstance
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
operator|.
name|clearThreadLocalProxies
argument_list|()
expr_stmt|;
name|ClassResourceInfo
name|criRoot
init|=
operator|(
name|ClassResourceInfo
operator|)
name|exchange
operator|.
name|get
argument_list|(
name|JAXRSUtils
operator|.
name|ROOT_RESOURCE_CLASS
argument_list|)
decl_stmt|;
if|if
condition|(
name|criRoot
operator|!=
literal|null
condition|)
block|{
name|criRoot
operator|.
name|clearThreadLocalProxies
argument_list|()
expr_stmt|;
block|}
name|exchange
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROPOGATE_EXCEPTION
argument_list|,
name|JAXRSUtils
operator|.
name|propogateException
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
return|return
operator|new
name|MessageContentsList
argument_list|(
name|excResponse
argument_list|)
return|;
block|}
finally|finally
block|{
name|exchange
operator|.
name|put
argument_list|(
name|LAST_SERVICE_OBJECT
argument_list|,
name|resourceObject
argument_list|)
expr_stmt|;
if|if
condition|(
name|contextLoader
operator|!=
literal|null
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|contextLoader
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ori
operator|.
name|isSubResourceLocator
argument_list|()
condition|)
block|{
try|try
block|{
name|Message
name|msg
init|=
name|exchange
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
name|getTemplateValues
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|String
name|subResourcePath
init|=
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
decl_stmt|;
name|String
name|httpMethod
init|=
operator|(
name|String
operator|)
name|msg
operator|.
name|get
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
decl_stmt|;
name|String
name|contentType
init|=
operator|(
name|String
operator|)
name|msg
operator|.
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentType
operator|==
literal|null
condition|)
block|{
name|contentType
operator|=
literal|"*/*"
expr_stmt|;
block|}
name|List
argument_list|<
name|MediaType
argument_list|>
name|acceptContentType
init|=
operator|(
name|List
argument_list|<
name|MediaType
argument_list|>
operator|)
name|msg
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|)
decl_stmt|;
name|result
operator|=
name|checkResultObject
argument_list|(
name|result
argument_list|,
name|subResourcePath
argument_list|)
expr_stmt|;
name|ClassResourceInfo
name|subCri
init|=
name|cri
operator|.
name|getSubResource
argument_list|(
name|methodToInvoke
operator|.
name|getReturnType
argument_list|()
argument_list|,
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|result
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|subCri
operator|==
literal|null
condition|)
block|{
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
name|errorM
init|=
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
literal|"NO_SUBRESOURCE_FOUND"
argument_list|,
name|BUNDLE
argument_list|,
name|subResourcePath
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|severe
argument_list|(
name|errorM
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|404
argument_list|)
throw|;
block|}
name|OperationResourceInfo
name|subOri
init|=
name|JAXRSUtils
operator|.
name|findTargetMethod
argument_list|(
name|subCri
argument_list|,
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|,
name|httpMethod
argument_list|,
name|values
argument_list|,
name|contentType
argument_list|,
name|acceptContentType
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|OperationResourceInfo
operator|.
name|class
argument_list|,
name|subOri
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|URITemplate
operator|.
name|TEMPLATE_PARAMETERS
argument_list|,
name|values
argument_list|)
expr_stmt|;
comment|// work out request parameters for the sub-resouce class. Here we
comment|// presume Inputstream has not been consumed yet by the root resource class.
comment|//I.e., only one place either in the root resource or sub-resouce class can
comment|//have a parameter that read from entitybody.
name|List
argument_list|<
name|Object
argument_list|>
name|newParams
init|=
name|JAXRSUtils
operator|.
name|processParameters
argument_list|(
name|subOri
argument_list|,
name|values
argument_list|,
name|msg
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setContent
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|newParams
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|invoke
argument_list|(
name|exchange
argument_list|,
name|newParams
argument_list|,
name|result
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
name|Response
name|excResponse
init|=
name|JAXRSUtils
operator|.
name|convertFaultToResponse
argument_list|(
name|ex
argument_list|,
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|MessageContentsList
argument_list|(
name|excResponse
argument_list|)
return|;
block|}
block|}
return|return
name|result
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
name|MultivaluedMap
name|getTemplateValues
parameter_list|(
name|Message
name|msg
parameter_list|)
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|oldValues
init|=
operator|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|msg
operator|.
name|get
argument_list|(
name|URITemplate
operator|.
name|TEMPLATE_PARAMETERS
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldValues
operator|!=
literal|null
condition|)
block|{
name|values
operator|.
name|putAll
argument_list|(
name|oldValues
argument_list|)
expr_stmt|;
block|}
return|return
name|values
return|;
block|}
specifier|private
name|boolean
name|setServiceLoaderAsContextLoader
parameter_list|(
name|Message
name|inMessage
parameter_list|)
block|{
name|Object
name|en
init|=
name|inMessage
operator|.
name|getContextualProperty
argument_list|(
name|SERVICE_LOADER_AS_CONTEXT
argument_list|)
decl_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|en
argument_list|)
operator|||
literal|"true"
operator|.
name|equals
argument_list|(
name|en
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isServiceObjectRequestScope
parameter_list|(
name|Message
name|inMessage
parameter_list|)
block|{
name|Object
name|scope
init|=
name|inMessage
operator|.
name|getContextualProperty
argument_list|(
name|SERVICE_OBJECT_SCOPE
argument_list|)
decl_stmt|;
return|return
name|REQUEST_SCOPE
operator|.
name|equals
argument_list|(
name|scope
argument_list|)
return|;
block|}
specifier|private
name|ResourceProvider
name|getResourceProvider
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|provider
init|=
name|exchange
operator|.
name|remove
argument_list|(
name|JAXRSUtils
operator|.
name|ROOT_PROVIDER
argument_list|)
decl_stmt|;
if|if
condition|(
name|provider
operator|==
literal|null
condition|)
block|{
name|OperationResourceInfo
name|ori
init|=
name|exchange
operator|.
name|get
argument_list|(
name|OperationResourceInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|ClassResourceInfo
name|cri
init|=
name|ori
operator|.
name|getClassResourceInfo
argument_list|()
decl_stmt|;
return|return
name|cri
operator|.
name|getResourceProvider
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|(
name|ResourceProvider
operator|)
name|provider
return|;
block|}
block|}
specifier|public
name|Object
name|getServiceObject
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|root
init|=
name|exchange
operator|.
name|remove
argument_list|(
name|JAXRSUtils
operator|.
name|ROOT_INSTANCE
argument_list|)
decl_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
return|return
name|root
return|;
block|}
name|OperationResourceInfo
name|ori
init|=
name|exchange
operator|.
name|get
argument_list|(
name|OperationResourceInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|ClassResourceInfo
name|cri
init|=
name|ori
operator|.
name|getClassResourceInfo
argument_list|()
decl_stmt|;
return|return
name|cri
operator|.
name|getResourceProvider
argument_list|()
operator|.
name|getInstance
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Object
name|getActualServiceObject
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|rootInstance
parameter_list|)
block|{
name|Object
name|last
init|=
name|exchange
operator|.
name|get
argument_list|(
name|LAST_SERVICE_OBJECT
argument_list|)
decl_stmt|;
return|return
name|last
operator|!=
literal|null
condition|?
name|last
else|:
name|rootInstance
return|;
block|}
specifier|private
specifier|static
name|Object
name|checkResultObject
parameter_list|(
name|Object
name|result
parameter_list|,
name|String
name|subResourcePath
parameter_list|)
block|{
comment|//the result becomes the object that will handle the request
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|result
operator|instanceof
name|MessageContentsList
condition|)
block|{
name|result
operator|=
operator|(
operator|(
name|MessageContentsList
operator|)
name|result
operator|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|result
operator|instanceof
name|List
condition|)
block|{
name|result
operator|=
operator|(
operator|(
name|List
operator|)
name|result
operator|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|result
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|result
operator|=
operator|(
operator|(
name|Object
index|[]
operator|)
name|result
operator|)
index|[
literal|0
index|]
expr_stmt|;
block|}
block|}
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
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
name|errorM
init|=
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
literal|"NULL_SUBRESOURCE"
argument_list|,
name|BUNDLE
argument_list|,
name|subResourcePath
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
name|errorM
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|404
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|void
name|pushOntoStack
parameter_list|(
name|OperationResourceInfo
name|ori
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|realClass
parameter_list|,
name|Message
name|msg
parameter_list|)
block|{
name|OperationResourceInfoStack
name|stack
init|=
name|msg
operator|.
name|get
argument_list|(
name|OperationResourceInfoStack
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|stack
operator|==
literal|null
condition|)
block|{
name|stack
operator|=
operator|new
name|OperationResourceInfoStack
argument_list|()
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|OperationResourceInfoStack
operator|.
name|class
argument_list|,
name|stack
argument_list|)
expr_stmt|;
block|}
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
operator|(
name|MultivaluedMap
operator|)
name|msg
operator|.
name|get
argument_list|(
name|URITemplate
operator|.
name|TEMPLATE_PARAMETERS
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|params
operator|==
literal|null
operator|||
name|params
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|values
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|values
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|params
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
for|for
control|(
name|Parameter
name|pm
range|:
name|ori
operator|.
name|getParameters
argument_list|()
control|)
block|{
if|if
condition|(
name|pm
operator|.
name|getType
argument_list|()
operator|==
name|ParameterType
operator|.
name|PATH
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|paramValues
init|=
name|params
operator|.
name|get
argument_list|(
name|pm
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|paramValues
operator|!=
literal|null
condition|)
block|{
name|values
operator|.
name|addAll
argument_list|(
name|paramValues
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|stack
operator|.
name|push
argument_list|(
operator|new
name|MethodInvocationInfo
argument_list|(
name|ori
argument_list|,
name|realClass
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

