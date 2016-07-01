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
name|reflect
operator|.
name|Method
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
name|container
operator|.
name|AsyncResponse
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
name|classloader
operator|.
name|ClassLoaderUtils
operator|.
name|ClassLoaderHolder
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
name|AsyncResponseImpl
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
name|ServerProviderFactory
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
name|ExceptionUtils
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
name|JAXRSInvoker
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
name|PROXY_INVOCATION_ERROR_FRAGMENT
init|=
literal|"object is not an instance of declaring class"
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
name|MessageContentsList
name|responseList
init|=
name|checkExchangeForResponse
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|responseList
operator|!=
literal|null
condition|)
block|{
return|return
name|responseList
return|;
block|}
name|AsyncResponse
name|asyncResp
init|=
name|exchange
operator|.
name|get
argument_list|(
name|AsyncResponse
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|asyncResp
operator|!=
literal|null
condition|)
block|{
name|AsyncResponseImpl
name|asyncImpl
init|=
operator|(
name|AsyncResponseImpl
operator|)
name|asyncResp
decl_stmt|;
name|asyncImpl
operator|.
name|prepareContinuation
argument_list|()
expr_stmt|;
try|try
block|{
name|asyncImpl
operator|.
name|handleTimeout
argument_list|()
expr_stmt|;
return|return
name|handleAsyncResponse
argument_list|(
name|exchange
argument_list|,
name|asyncImpl
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
return|return
name|handleAsyncFault
argument_list|(
name|exchange
argument_list|,
name|asyncImpl
argument_list|,
name|t
argument_list|)
return|;
block|}
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
literal|null
decl_stmt|;
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
try|try
block|{
name|rootInstance
operator|=
name|getServiceObject
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
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
catch|catch
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
name|responseList
operator|=
name|checkExchangeForResponse
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|responseList
operator|!=
literal|null
condition|)
block|{
return|return
name|responseList
return|;
block|}
return|return
name|handleFault
argument_list|(
name|ex
argument_list|,
name|inMessage
argument_list|)
return|;
block|}
finally|finally
block|{
name|boolean
name|suspended
init|=
name|isSuspended
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|suspended
operator|||
name|exchange
operator|.
name|isOneWay
argument_list|()
operator|||
name|inMessage
operator|.
name|get
argument_list|(
name|Message
operator|.
name|THREAD_CONTEXT_SWITCHED
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|ServerProviderFactory
operator|.
name|clearThreadLocalProxies
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|suspended
operator|||
name|isServiceObjectRequestScope
argument_list|(
name|inMessage
argument_list|)
condition|)
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
else|else
block|{
name|provider
operator|.
name|releaseInstance
argument_list|(
name|inMessage
argument_list|,
name|rootInstance
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|boolean
name|isSuspended
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
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
return|;
block|}
specifier|private
name|Object
name|handleAsyncResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncResponseImpl
name|ar
parameter_list|)
block|{
name|Object
name|asyncObj
init|=
name|ar
operator|.
name|getResponseObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|asyncObj
operator|instanceof
name|Throwable
condition|)
block|{
return|return
name|handleAsyncFault
argument_list|(
name|exchange
argument_list|,
name|ar
argument_list|,
operator|(
name|Throwable
operator|)
name|asyncObj
argument_list|)
return|;
block|}
else|else
block|{
name|setResponseContentTypeIfNeeded
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|,
name|asyncObj
argument_list|)
expr_stmt|;
return|return
operator|new
name|MessageContentsList
argument_list|(
name|asyncObj
argument_list|)
return|;
block|}
block|}
specifier|private
name|Object
name|handleAsyncFault
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncResponseImpl
name|ar
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
try|try
block|{
return|return
name|handleFault
argument_list|(
operator|new
name|Fault
argument_list|(
name|t
argument_list|)
argument_list|,
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Fault
name|ex
parameter_list|)
block|{
name|ar
operator|.
name|setUnmappedThrowable
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
operator|==
literal|null
condition|?
name|ex
else|:
name|ex
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isSuspended
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|ar
operator|.
name|reset
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|unpause
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|MessageContentsList
argument_list|(
name|Response
operator|.
name|serverError
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
return|;
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
specifier|final
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
specifier|final
name|ClassResourceInfo
name|cri
init|=
name|ori
operator|.
name|getClassResourceInfo
argument_list|()
decl_stmt|;
specifier|final
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
specifier|final
name|ServerProviderFactory
name|providerFactory
init|=
name|ServerProviderFactory
operator|.
name|getInstance
argument_list|(
name|inMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|cri
operator|.
name|isRoot
argument_list|()
condition|)
block|{
name|cri
operator|.
name|injectContexts
argument_list|(
name|resourceObject
argument_list|,
name|ori
argument_list|,
name|inMessage
argument_list|)
expr_stmt|;
name|ProviderInfo
argument_list|<
name|Application
argument_list|>
name|appProvider
init|=
name|providerFactory
operator|.
name|getApplicationProvider
argument_list|()
decl_stmt|;
if|if
condition|(
name|appProvider
operator|!=
literal|null
condition|)
block|{
name|InjectionUtils
operator|.
name|injectContexts
argument_list|(
name|appProvider
operator|.
name|getProvider
argument_list|()
argument_list|,
name|appProvider
argument_list|,
name|inMessage
argument_list|)
expr_stmt|;
block|}
block|}
name|Method
name|methodToInvoke
init|=
name|getMethodToInvoke
argument_list|(
name|cri
argument_list|,
name|ori
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
name|ClassLoaderHolder
name|contextLoader
init|=
literal|null
decl_stmt|;
name|AsyncResponseImpl
name|asyncResponse
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|setServiceLoaderAsContextLoader
argument_list|(
name|inMessage
argument_list|)
condition|)
block|{
name|contextLoader
operator|=
name|ClassLoaderUtils
operator|.
name|setThreadContextClassloader
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
if|if
condition|(
operator|!
name|ori
operator|.
name|isSubResourceLocator
argument_list|()
condition|)
block|{
name|asyncResponse
operator|=
operator|(
name|AsyncResponseImpl
operator|)
name|inMessage
operator|.
name|get
argument_list|(
name|AsyncResponse
operator|.
name|class
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
if|if
condition|(
name|asyncResponse
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|asyncResponse
operator|.
name|suspendContinuationIfNeeded
argument_list|()
condition|)
block|{
name|result
operator|=
name|handleAsyncResponse
argument_list|(
name|exchange
argument_list|,
name|asyncResponse
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|providerFactory
operator|.
name|clearThreadLocalProxies
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Fault
name|ex
parameter_list|)
block|{
name|Object
name|faultResponse
decl_stmt|;
if|if
condition|(
name|asyncResponse
operator|!=
literal|null
condition|)
block|{
name|faultResponse
operator|=
name|handleAsyncFault
argument_list|(
name|exchange
argument_list|,
name|asyncResponse
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|==
literal|null
condition|?
name|ex
else|:
name|ex
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|faultResponse
operator|=
name|handleFault
argument_list|(
name|ex
argument_list|,
name|inMessage
argument_list|,
name|cri
argument_list|,
name|methodToInvoke
argument_list|)
expr_stmt|;
block|}
return|return
name|faultResponse
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
name|contextLoader
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
name|ClassResourceInfo
name|subCri
init|=
literal|null
decl_stmt|;
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
name|inMessage
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
name|inMessage
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
name|inMessage
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
name|exchange
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
name|subCri
operator|=
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
name|exchange
operator|.
name|getBus
argument_list|()
argument_list|,
name|result
argument_list|)
argument_list|,
name|result
argument_list|)
expr_stmt|;
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
name|ExceptionUtils
operator|.
name|toNotFoundException
argument_list|(
literal|null
argument_list|,
literal|null
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
name|Collections
operator|.
name|singletonMap
argument_list|(
name|subCri
argument_list|,
name|values
argument_list|)
argument_list|,
name|inMessage
argument_list|,
name|httpMethod
argument_list|,
name|values
argument_list|,
name|contentType
argument_list|,
name|acceptContentType
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
name|inMessage
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
if|if
condition|(
operator|!
name|subOri
operator|.
name|isSubResourceLocator
argument_list|()
operator|&&
name|JAXRSUtils
operator|.
name|runContainerRequestFilters
argument_list|(
name|providerFactory
argument_list|,
name|inMessage
argument_list|,
literal|false
argument_list|,
name|subOri
operator|.
name|getNameBindings
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|MessageContentsList
argument_list|(
name|exchange
operator|.
name|get
argument_list|(
name|Response
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
comment|// work out request parameters for the sub-resource class. Here we
comment|// presume InputStream has not been consumed yet by the root resource class.
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
name|inMessage
argument_list|)
decl_stmt|;
name|inMessage
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
name|IOException
name|ex
parameter_list|)
block|{
name|Response
name|resp
init|=
name|JAXRSUtils
operator|.
name|convertFaultToResponse
argument_list|(
name|ex
argument_list|,
name|inMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|resp
operator|==
literal|null
condition|)
block|{
name|resp
operator|=
name|JAXRSUtils
operator|.
name|convertFaultToResponse
argument_list|(
name|ex
argument_list|,
name|inMessage
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|MessageContentsList
argument_list|(
name|resp
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
decl_stmt|;
if|if
condition|(
name|JAXRSUtils
operator|.
name|noResourceMethodForOptions
argument_list|(
name|ex
operator|.
name|getResponse
argument_list|()
argument_list|,
operator|(
name|String
operator|)
name|inMessage
operator|.
name|get
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
argument_list|)
condition|)
block|{
name|excResponse
operator|=
name|JAXRSUtils
operator|.
name|createResponse
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|subCri
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|200
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|excResponse
operator|=
name|JAXRSUtils
operator|.
name|convertFaultToResponse
argument_list|(
name|ex
argument_list|,
name|inMessage
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|MessageContentsList
argument_list|(
name|excResponse
argument_list|)
return|;
block|}
block|}
name|setResponseContentTypeIfNeeded
argument_list|(
name|inMessage
argument_list|,
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|protected
name|Method
name|getMethodToInvoke
parameter_list|(
name|ClassResourceInfo
name|cri
parameter_list|,
name|OperationResourceInfo
name|ori
parameter_list|,
name|Object
name|resourceObject
parameter_list|)
block|{
name|Method
name|resourceMethod
init|=
name|cri
operator|.
name|getMethodDispatcher
argument_list|()
operator|.
name|getMethod
argument_list|(
name|ori
argument_list|)
decl_stmt|;
name|Method
name|methodToInvoke
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|Proxy
operator|.
name|class
operator|.
name|isInstance
argument_list|(
name|resourceObject
argument_list|)
condition|)
block|{
name|methodToInvoke
operator|=
name|cri
operator|.
name|getMethodDispatcher
argument_list|()
operator|.
name|getProxyMethod
argument_list|(
name|resourceMethod
argument_list|)
expr_stmt|;
if|if
condition|(
name|methodToInvoke
operator|==
literal|null
condition|)
block|{
name|methodToInvoke
operator|=
name|InjectionUtils
operator|.
name|checkProxy
argument_list|(
name|resourceMethod
argument_list|,
name|resourceObject
argument_list|)
expr_stmt|;
name|cri
operator|.
name|getMethodDispatcher
argument_list|()
operator|.
name|addProxyMethod
argument_list|(
name|resourceMethod
argument_list|,
name|methodToInvoke
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|methodToInvoke
operator|=
name|resourceMethod
expr_stmt|;
block|}
return|return
name|methodToInvoke
return|;
block|}
specifier|private
name|MessageContentsList
name|checkExchangeForResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Response
name|r
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
name|r
operator|!=
literal|null
condition|)
block|{
name|JAXRSUtils
operator|.
name|setMessageContentType
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|,
name|r
argument_list|)
expr_stmt|;
return|return
operator|new
name|MessageContentsList
argument_list|(
name|r
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
name|void
name|setResponseContentTypeIfNeeded
parameter_list|(
name|Message
name|inMessage
parameter_list|,
name|Object
name|response
parameter_list|)
block|{
if|if
condition|(
name|response
operator|instanceof
name|Response
condition|)
block|{
name|JAXRSUtils
operator|.
name|setMessageContentType
argument_list|(
name|inMessage
argument_list|,
operator|(
name|Response
operator|)
name|response
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Object
name|handleFault
parameter_list|(
name|Throwable
name|ex
parameter_list|,
name|Message
name|inMessage
parameter_list|)
block|{
return|return
name|handleFault
argument_list|(
operator|new
name|Fault
argument_list|(
name|ex
argument_list|)
argument_list|,
name|inMessage
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|private
name|Object
name|handleFault
parameter_list|(
name|Fault
name|ex
parameter_list|,
name|Message
name|inMessage
parameter_list|,
name|ClassResourceInfo
name|cri
parameter_list|,
name|Method
name|methodToInvoke
parameter_list|)
block|{
name|String
name|errorMessage
init|=
name|ex
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|errorMessage
operator|!=
literal|null
operator|&&
name|cri
operator|!=
literal|null
operator|&&
name|errorMessage
operator|.
name|contains
argument_list|(
name|PROXY_INVOCATION_ERROR_FRAGMENT
argument_list|)
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
literal|"PROXY_INVOCATION_FAILURE"
argument_list|,
name|BUNDLE
argument_list|,
name|methodToInvoke
argument_list|,
name|cri
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getName
argument_list|()
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
block|}
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
operator|==
literal|null
condition|?
name|ex
else|:
name|ex
operator|.
name|getCause
argument_list|()
argument_list|,
name|inMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|excResponse
operator|==
literal|null
condition|)
block|{
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROPOGATE_EXCEPTION
argument_list|,
name|ExceptionUtils
operator|.
name|propogateException
argument_list|(
name|inMessage
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
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
specifier|protected
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
argument_list|<
name|?
argument_list|>
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
name|ExceptionUtils
operator|.
name|toNotFoundException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

