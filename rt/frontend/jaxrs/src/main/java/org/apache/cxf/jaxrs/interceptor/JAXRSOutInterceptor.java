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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|events
operator|.
name|XMLEvent
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
name|SystemUtils
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
name|io
operator|.
name|CachedOutputStream
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|staxutils
operator|.
name|CachingXmlEventWriter
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
name|staxutils
operator|.
name|StaxUtils
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
name|HttpUtils
operator|.
name|getOriginalAddress
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
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
name|ori
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
name|ori
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
name|serializeMessage
argument_list|(
name|message
argument_list|,
name|response
argument_list|,
name|ori
argument_list|,
name|baseAddress
argument_list|,
literal|true
argument_list|)
expr_stmt|;
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|void
name|serializeMessage
parameter_list|(
name|Message
name|message
parameter_list|,
name|Response
name|response
parameter_list|,
name|OperationResourceInfo
name|ori
parameter_list|,
name|String
name|baseAddress
parameter_list|,
name|boolean
name|firstTry
parameter_list|)
block|{
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
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|theHeaders
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
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
decl_stmt|;
if|if
condition|(
name|firstTry
operator|&&
name|theHeaders
operator|!=
literal|null
condition|)
block|{
comment|// some headers might've been setup by custom cxf interceptors
name|theHeaders
operator|.
name|putAll
argument_list|(
operator|(
name|Map
operator|)
name|response
operator|.
name|getMetadata
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
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
block|}
name|Object
name|responseObj
init|=
name|response
operator|.
name|getEntity
argument_list|()
decl_stmt|;
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
literal|null
decl_stmt|;
if|if
condition|(
name|firstTry
condition|)
block|{
name|invoked
operator|=
name|ori
operator|==
literal|null
condition|?
literal|null
else|:
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
expr_stmt|;
block|}
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
name|message
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
name|outOriginal
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
literal|500
argument_list|)
expr_stmt|;
name|writeResponseErrorMessage
argument_list|(
name|outOriginal
argument_list|,
literal|"NO_MSG_WRITER"
argument_list|,
name|targetType
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|boolean
name|enabled
init|=
name|checkBufferingMode
argument_list|(
name|message
argument_list|,
name|writer
argument_list|,
name|firstTry
argument_list|)
decl_stmt|;
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
try|try
block|{
name|writer
operator|.
name|writeTo
argument_list|(
name|responseObj
argument_list|,
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
name|responseType
argument_list|,
name|response
operator|.
name|getMetadata
argument_list|()
argument_list|,
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|checkCachedStream
argument_list|(
name|message
argument_list|,
name|outOriginal
argument_list|,
name|enabled
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|enabled
condition|)
block|{
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|outOriginal
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|XMLStreamWriter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|handleWriteException
argument_list|(
name|message
argument_list|,
name|response
argument_list|,
name|ori
argument_list|,
name|baseAddress
argument_list|,
name|ex
argument_list|,
name|responseObj
argument_list|,
name|firstTry
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|handleWriteException
argument_list|(
name|message
argument_list|,
name|response
argument_list|,
name|ori
argument_list|,
name|baseAddress
argument_list|,
name|ex
argument_list|,
name|responseObj
argument_list|,
name|firstTry
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|checkBufferingMode
parameter_list|(
name|Message
name|m
parameter_list|,
name|MessageBodyWriter
name|w
parameter_list|,
name|boolean
name|firstTry
parameter_list|)
block|{
if|if
condition|(
operator|!
name|firstTry
condition|)
block|{
return|return
literal|false
return|;
block|}
name|boolean
name|enabled
init|=
name|SystemUtils
operator|.
name|isBufferingEnabled
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|enabled
operator|&&
operator|!
name|SystemUtils
operator|.
name|isBufferingSet
argument_list|()
condition|)
block|{
name|enabled
operator|=
name|InjectionUtils
operator|.
name|invokeBooleanGetter
argument_list|(
name|w
argument_list|,
literal|"getEnableBuffering"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|enabled
condition|)
block|{
name|boolean
name|streamingOn
init|=
literal|"org.apache.cxf.jaxrs.provider.JAXBElementProvider"
operator|.
name|equals
argument_list|(
name|w
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|InjectionUtils
operator|.
name|invokeBooleanGetter
argument_list|(
name|w
argument_list|,
literal|"getEnableStreaming"
argument_list|)
decl_stmt|;
if|if
condition|(
name|streamingOn
condition|)
block|{
name|m
operator|.
name|put
argument_list|(
name|XMLStreamWriter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|CachingXmlEventWriter
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|m
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
operator|new
name|CachedOutputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|enabled
return|;
block|}
specifier|private
name|void
name|checkCachedStream
parameter_list|(
name|Message
name|m
parameter_list|,
name|OutputStream
name|osOriginal
parameter_list|,
name|boolean
name|enabled
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|enabled
condition|)
block|{
return|return;
block|}
name|XMLStreamWriter
name|writer
init|=
operator|(
name|XMLStreamWriter
operator|)
name|m
operator|.
name|get
argument_list|(
name|XMLStreamWriter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|writer
operator|instanceof
name|CachingXmlEventWriter
condition|)
block|{
name|CachingXmlEventWriter
name|cache
init|=
operator|(
name|CachingXmlEventWriter
operator|)
name|writer
decl_stmt|;
if|if
condition|(
name|cache
operator|.
name|getEvents
argument_list|()
operator|.
name|size
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|XMLStreamWriter
name|origWriter
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|osOriginal
argument_list|)
decl_stmt|;
for|for
control|(
name|XMLEvent
name|event
range|:
name|cache
operator|.
name|getEvents
argument_list|()
control|)
block|{
name|StaxUtils
operator|.
name|writeEvent
argument_list|(
name|event
argument_list|,
name|origWriter
argument_list|)
expr_stmt|;
block|}
block|}
name|m
operator|.
name|put
argument_list|(
name|XMLStreamWriter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return;
block|}
name|OutputStream
name|os
init|=
name|m
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
name|os
operator|!=
name|osOriginal
operator|&&
name|os
operator|instanceof
name|CachedOutputStream
condition|)
block|{
name|CachedOutputStream
name|cos
init|=
operator|(
name|CachedOutputStream
operator|)
name|os
decl_stmt|;
if|if
condition|(
name|cos
operator|.
name|size
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|cos
operator|.
name|writeCacheTo
argument_list|(
name|osOriginal
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|handleWriteException
parameter_list|(
name|Message
name|message
parameter_list|,
name|Response
name|response
parameter_list|,
name|OperationResourceInfo
name|ori
parameter_list|,
name|String
name|baseAddress
parameter_list|,
name|Throwable
name|ex
parameter_list|,
name|Object
name|responseObj
parameter_list|,
name|boolean
name|firstTry
parameter_list|)
block|{
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
name|firstTry
condition|)
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
name|baseAddress
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|excResponse
operator|!=
literal|null
condition|)
block|{
name|serializeMessage
argument_list|(
name|message
argument_list|,
name|excResponse
argument_list|,
name|ori
argument_list|,
name|baseAddress
argument_list|,
literal|false
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
name|message
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
name|errorString
argument_list|,
name|BUNDLE
argument_list|,
name|parameter
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|message
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|message
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
return|return
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
return|;
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

