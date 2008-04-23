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
name|ProduceMime
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
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
if|if
condition|(
name|operation
operator|==
literal|null
condition|)
block|{
return|return;
block|}
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
name|Response
name|response
init|=
operator|(
name|Response
operator|)
name|responseObj
decl_stmt|;
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
argument_list|)
decl_stmt|;
name|MessageBodyWriter
name|writer
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
argument_list|()
operator|.
name|createMessageBodyWriter
argument_list|(
name|targetType
argument_list|,
name|type
argument_list|)
expr_stmt|;
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
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
name|responseObj
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
try|try
block|{
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
name|MediaType
name|mt
init|=
name|computeFinalContentTypes
argument_list|(
name|availableContentTypes
argument_list|,
name|writer
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Response content type is: "
operator|+
name|mt
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
name|mt
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeTo
argument_list|(
name|responseObj
argument_list|,
name|mt
argument_list|,
literal|null
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
specifier|private
name|List
argument_list|<
name|MediaType
argument_list|>
name|computeAvailableContentTypes
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
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
name|methodMimeTypes
init|=
name|exchange
operator|.
name|get
argument_list|(
name|OperationResourceInfo
operator|.
name|class
argument_list|)
operator|.
name|getProduceTypes
argument_list|()
decl_stmt|;
name|String
name|acceptContentTypes
init|=
operator|(
name|String
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
name|List
argument_list|<
name|MediaType
argument_list|>
name|acceptValues
init|=
name|JAXRSUtils
operator|.
name|parseMediaTypes
argument_list|(
name|acceptContentTypes
argument_list|)
decl_stmt|;
return|return
name|JAXRSUtils
operator|.
name|intersectMimeTypes
argument_list|(
name|methodMimeTypes
argument_list|,
name|acceptValues
argument_list|)
return|;
block|}
specifier|private
name|MediaType
name|computeFinalContentTypes
parameter_list|(
name|List
argument_list|<
name|MediaType
argument_list|>
name|produceContentTypes
parameter_list|,
name|MessageBodyWriter
name|provider
parameter_list|)
block|{
name|List
argument_list|<
name|MediaType
argument_list|>
name|providerMimeTypes
init|=
name|JAXRSUtils
operator|.
name|getProduceTypes
argument_list|(
name|provider
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|ProduceMime
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|MediaType
argument_list|>
name|list
init|=
name|JAXRSUtils
operator|.
name|intersectMimeTypes
argument_list|(
name|produceContentTypes
argument_list|,
name|providerMimeTypes
argument_list|)
decl_stmt|;
return|return
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
end_class

end_unit

