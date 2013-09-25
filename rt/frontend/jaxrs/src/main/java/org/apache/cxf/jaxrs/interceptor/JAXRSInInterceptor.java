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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|HttpMethod
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
name|InternalServerErrorException
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
name|NotAcceptableException
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
name|NotFoundException
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
name|interceptor
operator|.
name|OutgoingChainInterceptor
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
name|logging
operator|.
name|FaultListener
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
name|logging
operator|.
name|NoOpFaultListener
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
name|message
operator|.
name|MessageImpl
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
specifier|private
specifier|static
specifier|final
name|String
name|RESOURCE_METHOD
init|=
literal|"org.apache.cxf.resource.method"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RESOURCE_OPERATION_NAME
init|=
literal|"org.apache.cxf.resource.operation.name"
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
specifier|public
name|void
name|handleMessage
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
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|REST_MESSAGE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
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
name|Fault
name|ex
parameter_list|)
block|{
name|convertExceptionToResponseIfPossible
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
argument_list|,
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
name|convertExceptionToResponseIfPossible
argument_list|(
name|ex
argument_list|,
name|message
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
name|ServerProviderFactory
name|providerFactory
init|=
name|ServerProviderFactory
operator|.
name|getInstance
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|RequestPreprocessor
name|rp
init|=
name|providerFactory
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
block|}
comment|// Global pre-match request filters
if|if
condition|(
name|JAXRSUtils
operator|.
name|runContainerRequestFilters
argument_list|(
name|providerFactory
argument_list|,
name|message
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// HTTP method
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
name|HttpMethod
operator|.
name|POST
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// Path to match
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
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|protocolHeaders
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
comment|// Content-Type
name|String
name|requestContentType
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|ctHeaderValues
init|=
name|protocolHeaders
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
name|ctHeaderValues
operator|!=
literal|null
condition|)
block|{
name|requestContentType
operator|=
name|ctHeaderValues
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|requestContentType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|requestContentType
operator|==
literal|null
condition|)
block|{
name|requestContentType
operator|=
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
expr_stmt|;
if|if
condition|(
name|requestContentType
operator|==
literal|null
condition|)
block|{
name|requestContentType
operator|=
name|MediaType
operator|.
name|WILDCARD
expr_stmt|;
block|}
block|}
comment|// Accept
name|String
name|acceptTypes
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|acceptHeaderValues
init|=
name|protocolHeaders
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
name|acceptHeaderValues
operator|!=
literal|null
condition|)
block|{
name|acceptTypes
operator|=
name|acceptHeaderValues
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
if|if
condition|(
name|acceptTypes
operator|==
literal|null
condition|)
block|{
name|acceptTypes
operator|=
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
expr_stmt|;
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
name|acceptContentTypes
operator|=
name|JAXRSUtils
operator|.
name|sortMediaTypes
argument_list|(
name|acceptTypes
argument_list|,
name|JAXRSUtils
operator|.
name|MEDIA_TYPE_Q_PARAM
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
name|NotAcceptableException
argument_list|()
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
comment|//1. Matching target resource class
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|resources
init|=
name|JAXRSUtils
operator|.
name|getRootResources
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|ClassResourceInfo
argument_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|matchedResources
init|=
name|JAXRSUtils
operator|.
name|selectResourceClass
argument_list|(
name|resources
argument_list|,
name|rawPath
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|matchedResources
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
name|resources
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
name|NotFoundException
argument_list|(
name|resp
argument_list|)
throw|;
block|}
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|matchedValues
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
name|OperationResourceInfo
name|ori
init|=
literal|null
decl_stmt|;
try|try
block|{
name|ori
operator|=
name|JAXRSUtils
operator|.
name|findTargetMethod
argument_list|(
name|matchedResources
argument_list|,
name|message
argument_list|,
name|httpMethod
argument_list|,
name|matchedValues
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
name|matchedValues
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
name|resources
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
name|matchedValues
argument_list|,
name|resources
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Global and name-bound post-match request filters
if|if
condition|(
name|JAXRSUtils
operator|.
name|runContainerRequestFilters
argument_list|(
name|providerFactory
argument_list|,
name|message
argument_list|,
literal|false
argument_list|,
name|ori
operator|.
name|getNameBindings
argument_list|()
argument_list|,
literal|false
argument_list|)
condition|)
block|{
return|return;
block|}
comment|//Process parameters
try|try
block|{
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
name|matchedValues
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
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|convertExceptionToResponseIfPossible
argument_list|(
name|ex
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|convertExceptionToResponseIfPossible
parameter_list|(
name|Throwable
name|ex
parameter_list|,
name|Message
name|message
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
name|ServerProviderFactory
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
operator|instanceof
name|RuntimeException
condition|?
operator|(
name|RuntimeException
operator|)
name|ex
else|:
operator|new
name|InternalServerErrorException
argument_list|(
name|ex
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
name|Response
operator|.
name|class
argument_list|,
name|excResponse
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
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|JAXRSUtils
operator|.
name|ROOT_RESOURCE_CLASS
argument_list|,
name|ori
operator|.
name|getClassResourceInfo
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|RESOURCE_METHOD
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
name|RESOURCE_OPERATION_NAME
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
name|Response
name|r
init|=
name|JAXRSUtils
operator|.
name|convertFaultToResponse
argument_list|(
name|message
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|removeContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
expr_stmt|;
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|setFaultObserver
argument_list|(
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|FaultListener
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|FaultListener
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|NoOpFaultListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Endpoint
name|e
init|=
name|message
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
name|Message
name|mout
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|mout
operator|.
name|setContent
argument_list|(
name|List
operator|.
name|class
argument_list|,
operator|new
name|MessageContentsList
argument_list|(
name|r
argument_list|)
argument_list|)
expr_stmt|;
name|mout
operator|.
name|setExchange
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|mout
operator|=
name|e
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|(
name|mout
argument_list|)
expr_stmt|;
name|mout
operator|.
name|setInterceptorChain
argument_list|(
name|OutgoingChainInterceptor
operator|.
name|getOutInterceptorChain
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setOutMessage
argument_list|(
name|mout
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|iterator
init|=
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|inInterceptor
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|inInterceptor
operator|.
name|getClass
argument_list|()
operator|==
name|OutgoingChainInterceptor
operator|.
name|class
condition|)
block|{
operator|(
operator|(
name|OutgoingChainInterceptor
operator|)
name|inInterceptor
operator|)
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
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
name|ServerProviderFactory
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
block|}
end_class

end_unit

