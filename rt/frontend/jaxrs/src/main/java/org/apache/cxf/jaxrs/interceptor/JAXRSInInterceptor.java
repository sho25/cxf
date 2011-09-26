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
name|interceptor
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
name|Level
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
name|jaxrs
operator|.
name|JAXRSServiceImpl
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
name|ext
operator|.
name|RequestHandler
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
name|UriInfoImpl
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
name|HttpUtils
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
name|MessageUtils
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
name|phase
operator|.
name|AbstractPhaseInterceptor
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
name|phase
operator|.
name|Phase
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

begin_class
specifier|public
class|class
name|JAXRSInInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
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
name|JAXRSInInterceptor
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
name|JAXRSInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|JAXRSInInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|UNMARSHAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleFault
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|super
operator|.
name|handleFault
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Cleanup thread local variables"
argument_list|)
expr_stmt|;
name|Object
name|rootInstance
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|remove
argument_list|(
name|JAXRSUtils
operator|.
name|ROOT_INSTANCE
argument_list|)
decl_stmt|;
name|Object
name|rootProvider
init|=
name|message
operator|.
name|getExchange
argument_list|()
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
name|rootInstance
operator|!=
literal|null
operator|&&
name|rootProvider
operator|!=
literal|null
condition|)
block|{
try|try
block|{
operator|(
operator|(
name|ResourceProvider
operator|)
name|rootProvider
operator|)
operator|.
name|releaseInstance
argument_list|(
name|message
argument_list|,
name|rootInstance
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|tex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Exception occurred during releasing the service instance, "
operator|+
name|tex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|ProviderFactory
operator|.
name|getInstance
argument_list|(
name|message
argument_list|)
operator|.
name|clearThreadLocalProxies
argument_list|()
expr_stmt|;
name|ClassResourceInfo
name|cri
init|=
operator|(
name|ClassResourceInfo
operator|)
name|message
operator|.
name|getExchange
argument_list|()
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
name|cri
operator|!=
literal|null
condition|)
block|{
name|cri
operator|.
name|clearThreadLocalProxies
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
name|processRequest
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
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
name|message
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
name|message
argument_list|)
operator|.
name|clearThreadLocalProxies
argument_list|()
expr_stmt|;
name|message
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
name|JAXRSUtils
operator|.
name|propogateException
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|Response
operator|.
name|class
argument_list|,
name|excResponse
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|processRequest
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|OperationResourceInfo
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
comment|// it's a suspended invocation;
return|return;
block|}
name|RequestPreprocessor
name|rp
init|=
name|ProviderFactory
operator|.
name|getInstance
argument_list|(
name|message
argument_list|)
operator|.
name|getRequestPreprocessor
argument_list|()
decl_stmt|;
if|if
condition|(
name|rp
operator|!=
literal|null
condition|)
block|{
name|rp
operator|.
name|preprocess
argument_list|(
name|message
argument_list|,
operator|new
name|UriInfoImpl
argument_list|(
name|message
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Response
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
block|}
name|String
name|requestContentType
init|=
operator|(
name|String
operator|)
name|message
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
name|requestContentType
operator|==
literal|null
condition|)
block|{
name|requestContentType
operator|=
literal|"*/*"
expr_stmt|;
block|}
name|String
name|rawPath
init|=
name|HttpUtils
operator|.
name|getPathToMatch
argument_list|(
name|message
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|//1. Matching target resource class
name|Service
name|service
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Service
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|resources
init|=
operator|(
operator|(
name|JAXRSServiceImpl
operator|)
name|service
operator|)
operator|.
name|getClassResourceInfos
argument_list|()
decl_stmt|;
name|String
name|acceptTypes
init|=
name|HttpUtils
operator|.
name|getProtocolHeader
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|acceptTypes
operator|==
literal|null
condition|)
block|{
name|acceptTypes
operator|=
literal|"*/*"
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|,
name|acceptTypes
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|MediaType
argument_list|>
name|acceptContentTypes
init|=
literal|null
decl_stmt|;
try|try
block|{
name|JAXRSUtils
operator|.
name|sortMediaTypes
argument_list|(
name|acceptTypes
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|406
argument_list|)
throw|;
block|}
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|,
name|acceptContentTypes
argument_list|)
expr_stmt|;
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
name|ClassResourceInfo
name|resource
init|=
name|JAXRSUtils
operator|.
name|selectResourceClass
argument_list|(
name|resources
argument_list|,
name|rawPath
argument_list|,
name|values
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|resource
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
name|errorMsg
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
literal|"NO_ROOT_EXC"
argument_list|,
name|BUNDLE
argument_list|,
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUEST_URI
argument_list|)
argument_list|,
name|rawPath
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|errorMsg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Response
name|resp
init|=
name|JAXRSUtils
operator|.
name|createResponse
argument_list|(
name|resource
argument_list|,
name|message
argument_list|,
name|errorMsg
operator|.
name|toString
argument_list|()
argument_list|,
name|Response
operator|.
name|Status
operator|.
name|NOT_FOUND
operator|.
name|getStatusCode
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|resp
argument_list|)
throw|;
block|}
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|JAXRSUtils
operator|.
name|ROOT_RESOURCE_CLASS
argument_list|,
name|resource
argument_list|)
expr_stmt|;
name|String
name|httpMethod
init|=
name|HttpUtils
operator|.
name|getProtocolHeader
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|,
literal|"POST"
argument_list|)
decl_stmt|;
name|OperationResourceInfo
name|ori
init|=
literal|null
decl_stmt|;
name|boolean
name|operChecked
init|=
literal|false
decl_stmt|;
name|List
argument_list|<
name|ProviderInfo
argument_list|<
name|RequestHandler
argument_list|>
argument_list|>
name|shs
init|=
name|ProviderFactory
operator|.
name|getInstance
argument_list|(
name|message
argument_list|)
operator|.
name|getRequestHandlers
argument_list|()
decl_stmt|;
for|for
control|(
name|ProviderInfo
argument_list|<
name|RequestHandler
argument_list|>
name|sh
range|:
name|shs
control|)
block|{
name|String
name|newAcceptTypes
init|=
name|HttpUtils
operator|.
name|getProtocolHeader
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|,
literal|"*/*"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|acceptTypes
operator|.
name|equals
argument_list|(
name|newAcceptTypes
argument_list|)
operator|||
operator|(
name|ori
operator|==
literal|null
operator|&&
operator|!
name|operChecked
operator|)
condition|)
block|{
name|acceptTypes
operator|=
name|newAcceptTypes
expr_stmt|;
name|acceptContentTypes
operator|=
name|JAXRSUtils
operator|.
name|sortMediaTypes
argument_list|(
name|newAcceptTypes
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|,
name|acceptContentTypes
argument_list|)
expr_stmt|;
if|if
condition|(
name|ori
operator|!=
literal|null
condition|)
block|{
name|values
operator|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|resource
operator|=
name|JAXRSUtils
operator|.
name|selectResourceClass
argument_list|(
name|resources
argument_list|,
name|rawPath
argument_list|,
name|values
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|ori
operator|=
name|JAXRSUtils
operator|.
name|findTargetMethod
argument_list|(
name|resource
argument_list|,
name|message
argument_list|,
name|httpMethod
argument_list|,
name|values
argument_list|,
name|requestContentType
argument_list|,
name|acceptContentTypes
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|setExchangeProperties
argument_list|(
name|message
argument_list|,
name|ori
argument_list|,
name|values
argument_list|,
name|resources
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
name|operChecked
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|InjectionUtils
operator|.
name|injectContextFields
argument_list|(
name|sh
operator|.
name|getProvider
argument_list|()
argument_list|,
name|sh
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|InjectionUtils
operator|.
name|injectContextMethods
argument_list|(
name|sh
operator|.
name|getProvider
argument_list|()
argument_list|,
name|sh
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|sh
operator|.
name|getProvider
argument_list|()
operator|.
name|handleRequest
argument_list|(
name|message
argument_list|,
name|resource
argument_list|)
decl_stmt|;
if|if
condition|(
name|response
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|Response
operator|.
name|class
argument_list|,
name|response
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|String
name|newAcceptTypes
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|acceptTypes
operator|.
name|equals
argument_list|(
name|newAcceptTypes
argument_list|)
operator|||
name|ori
operator|==
literal|null
condition|)
block|{
name|acceptTypes
operator|=
name|newAcceptTypes
expr_stmt|;
name|acceptContentTypes
operator|=
name|JAXRSUtils
operator|.
name|sortMediaTypes
argument_list|(
name|acceptTypes
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|,
name|acceptContentTypes
argument_list|)
expr_stmt|;
if|if
condition|(
name|ori
operator|!=
literal|null
condition|)
block|{
name|values
operator|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|resource
operator|=
name|JAXRSUtils
operator|.
name|selectResourceClass
argument_list|(
name|resources
argument_list|,
name|rawPath
argument_list|,
name|values
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|ori
operator|=
name|JAXRSUtils
operator|.
name|findTargetMethod
argument_list|(
name|resource
argument_list|,
name|message
argument_list|,
name|httpMethod
argument_list|,
name|values
argument_list|,
name|requestContentType
argument_list|,
name|acceptContentTypes
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|setExchangeProperties
argument_list|(
name|message
argument_list|,
name|ori
argument_list|,
name|values
argument_list|,
name|resources
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
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
name|httpMethod
argument_list|)
condition|)
block|{
name|Response
name|response
init|=
name|JAXRSUtils
operator|.
name|createResponse
argument_list|(
name|resource
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|200
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|Response
operator|.
name|class
argument_list|,
name|response
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
throw|throw
name|ex
throw|;
block|}
block|}
block|}
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Request path is: "
operator|+
name|rawPath
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Request HTTP method is: "
operator|+
name|httpMethod
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Request contentType is: "
operator|+
name|requestContentType
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Accept contentType is: "
operator|+
name|acceptTypes
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Found operation: "
operator|+
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|setExchangeProperties
argument_list|(
name|message
argument_list|,
name|ori
argument_list|,
name|values
argument_list|,
name|resources
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|//Process parameters
name|List
argument_list|<
name|Object
argument_list|>
name|params
init|=
name|JAXRSUtils
operator|.
name|processParameters
argument_list|(
name|ori
argument_list|,
name|values
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setExchangeProperties
parameter_list|(
name|Message
name|message
parameter_list|,
name|OperationResourceInfo
name|ori
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
parameter_list|,
name|int
name|numberOfResources
parameter_list|)
block|{
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|OperationResourceInfo
operator|.
name|class
argument_list|,
name|ori
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"org.apache.cxf.resource.method"
argument_list|,
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
argument_list|)
expr_stmt|;
name|message
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
name|String
name|plainOperationName
init|=
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|numberOfResources
operator|>
literal|1
condition|)
block|{
name|plainOperationName
operator|=
name|ori
operator|.
name|getClassResourceInfo
argument_list|()
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"#"
operator|+
name|plainOperationName
expr_stmt|;
block|}
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
literal|"org.apache.cxf.resource.operation.name"
argument_list|,
name|plainOperationName
argument_list|)
expr_stmt|;
name|boolean
name|oneway
init|=
name|ori
operator|.
name|isOneway
argument_list|()
operator|||
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|HttpUtils
operator|.
name|getProtocolHeader
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|ONE_WAY_REQUEST
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setOneWay
argument_list|(
name|oneway
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

