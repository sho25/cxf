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
name|io
operator|.
name|OutputStream
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
name|core
operator|.
name|HttpHeaders
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
name|Response
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
name|interceptor
operator|.
name|AbstractOutDatabindingInterceptor
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
name|ResponseHandler
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
name|phase
operator|.
name|Phase
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSOutInterceptor
extends|extends
name|AbstractOutDatabindingInterceptor
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
name|JAXRSOutInterceptor
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
name|JAXRSOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|JAXRSOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|MARSHAL
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
name|String
name|baseAddress
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|Message
operator|.
name|BASE_PATH
argument_list|)
decl_stmt|;
try|try
block|{
name|processResponse
argument_list|(
name|message
argument_list|,
name|baseAddress
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|ProviderFactory
operator|.
name|getInstance
argument_list|(
name|baseAddress
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
name|JAXRSInInterceptor
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|void
name|processResponse
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|baseAddress
parameter_list|)
block|{
name|MessageContentsList
name|objs
init|=
name|MessageContentsList
operator|.
name|getContentsList
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|objs
operator|==
literal|null
operator|||
name|objs
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|objs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|Object
name|responseObj
init|=
name|objs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|objs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|Response
condition|)
block|{
name|response
operator|=
operator|(
name|Response
operator|)
name|responseObj
expr_stmt|;
block|}
else|else
block|{
name|response
operator|=
name|Response
operator|.
name|ok
argument_list|(
name|responseObj
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|OperationResourceInfo
name|operation
init|=
operator|(
name|OperationResourceInfo
operator|)
name|exchange
operator|.
name|get
argument_list|(
name|OperationResourceInfo
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ProviderInfo
argument_list|<
name|ResponseHandler
argument_list|>
argument_list|>
name|handlers
init|=
name|ProviderFactory
operator|.
name|getInstance
argument_list|(
name|baseAddress
argument_list|)
operator|.
name|getResponseHandlers
argument_list|()
decl_stmt|;
for|for
control|(
name|ProviderInfo
argument_list|<
name|ResponseHandler
argument_list|>
name|rh
range|:
name|handlers
control|)
block|{
name|Response
name|r
init|=
name|rh
operator|.
name|getProvider
argument_list|()
operator|.
name|handleResponse
argument_list|(
name|message
argument_list|,
name|operation
argument_list|,
name|response
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
name|response
operator|=
name|r
expr_stmt|;
block|}
block|}
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|RESPONSE_CODE
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|response
operator|.
name|getMetadata
argument_list|()
argument_list|)
expr_stmt|;
name|responseObj
operator|=
name|response
operator|.
name|getEntity
argument_list|()
expr_stmt|;
if|if
condition|(
name|responseObj
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Class
name|targetType
init|=
name|responseObj
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MediaType
argument_list|>
name|availableContentTypes
init|=
name|computeAvailableContentTypes
argument_list|(
name|message
argument_list|,
name|response
argument_list|)
decl_stmt|;
name|Method
name|invoked
init|=
name|operation
operator|==
literal|null
condition|?
literal|null
else|:
name|operation
operator|.
name|getMethodToInvoke
argument_list|()
decl_stmt|;
name|MessageBodyWriter
name|writer
init|=
literal|null
decl_stmt|;
name|MediaType
name|responseType
init|=
literal|null
decl_stmt|;
for|for
control|(
name|MediaType
name|type
range|:
name|availableContentTypes
control|)
block|{
name|writer
operator|=
name|ProviderFactory
operator|.
name|getInstance
argument_list|(
name|baseAddress
argument_list|)
operator|.
name|createMessageBodyWriter
argument_list|(
name|targetType
argument_list|,
name|invoked
operator|!=
literal|null
condition|?
name|invoked
operator|.
name|getGenericReturnType
argument_list|()
else|:
literal|null
argument_list|,
name|invoked
operator|!=
literal|null
condition|?
name|invoked
operator|.
name|getAnnotations
argument_list|()
else|:
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|type
argument_list|,
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|responseType
operator|=
name|type
expr_stmt|;
break|break;
block|}
block|}
name|OutputStream
name|out
init|=
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|writer
operator|==
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|RESPONSE_CODE
argument_list|,
literal|406
argument_list|)
expr_stmt|;
name|writeResponseErrorMessage
argument_list|(
name|out
argument_list|,
literal|"NO_MSG_WRITER"
argument_list|,
name|invoked
operator|!=
literal|null
condition|?
name|invoked
operator|.
name|getReturnType
argument_list|()
operator|.
name|getSimpleName
argument_list|()
else|:
literal|""
argument_list|)
expr_stmt|;
return|return;
block|}
try|try
block|{
name|responseType
operator|=
name|checkFinalContentType
argument_list|(
name|responseType
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Response content type is: "
operator|+
name|responseType
operator|.
name|toString
argument_list|()
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
name|responseType
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Response EntityProvider is: "
operator|+
name|writer
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeTo
argument_list|(
name|responseObj
argument_list|,
name|targetType
argument_list|,
name|invoked
operator|.
name|getGenericReturnType
argument_list|()
argument_list|,
name|invoked
operator|!=
literal|null
condition|?
name|invoked
operator|.
name|getAnnotations
argument_list|()
else|:
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|responseType
argument_list|,
name|response
operator|.
name|getMetadata
argument_list|()
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|RESPONSE_CODE
argument_list|,
literal|500
argument_list|)
expr_stmt|;
name|writeResponseErrorMessage
argument_list|(
name|out
argument_list|,
literal|"SERIALIZE_ERROR"
argument_list|,
name|responseObj
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|RESPONSE_CODE
argument_list|,
literal|204
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|writeResponseErrorMessage
parameter_list|(
name|OutputStream
name|out
parameter_list|,
name|String
name|errorString
parameter_list|,
name|String
name|parameter
parameter_list|)
block|{
try|try
block|{
comment|// TODO : make sure this message is picked up from a resource bundle
name|out
operator|.
name|write
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
name|errorString
argument_list|,
name|BUNDLE
argument_list|,
name|parameter
argument_list|)
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|another
parameter_list|)
block|{
comment|// ignore
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|List
argument_list|<
name|MediaType
argument_list|>
name|computeAvailableContentTypes
parameter_list|(
name|Message
name|message
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
name|Object
name|contentType
init|=
name|response
operator|.
name|getMetadata
argument_list|()
operator|.
name|getFirst
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MediaType
argument_list|>
name|produceTypes
init|=
literal|null
decl_stmt|;
name|OperationResourceInfo
name|operation
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
if|if
condition|(
name|contentType
operator|!=
literal|null
condition|)
block|{
name|produceTypes
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|MediaType
operator|.
name|valueOf
argument_list|(
name|contentType
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operation
operator|!=
literal|null
condition|)
block|{
name|produceTypes
operator|=
name|operation
operator|.
name|getProduceTypes
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|produceTypes
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|MediaType
operator|.
name|APPLICATION_OCTET_STREAM_TYPE
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|MediaType
argument_list|>
name|acceptContentTypes
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
if|if
condition|(
name|acceptContentTypes
operator|==
literal|null
condition|)
block|{
name|acceptContentTypes
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|MediaType
operator|.
name|WILDCARD_TYPE
argument_list|)
expr_stmt|;
block|}
return|return
name|JAXRSUtils
operator|.
name|intersectMimeTypes
argument_list|(
name|acceptContentTypes
argument_list|,
name|produceTypes
argument_list|)
return|;
block|}
specifier|private
name|MediaType
name|checkFinalContentType
parameter_list|(
name|MediaType
name|mt
parameter_list|)
block|{
if|if
condition|(
name|mt
operator|.
name|isWildcardType
argument_list|()
operator|&&
name|mt
operator|.
name|isWildcardSubtype
argument_list|()
condition|)
block|{
return|return
name|MediaType
operator|.
name|APPLICATION_OCTET_STREAM_TYPE
return|;
block|}
else|else
block|{
return|return
name|mt
return|;
block|}
block|}
block|}
end_class

end_unit

