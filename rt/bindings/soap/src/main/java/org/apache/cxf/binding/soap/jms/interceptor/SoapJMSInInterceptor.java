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
name|binding
operator|.
name|soap
operator|.
name|jms
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
name|Map
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
name|binding
operator|.
name|Binding
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
name|binding
operator|.
name|soap
operator|.
name|SoapMessage
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|AbstractSoapInterceptor
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
name|AttachmentInInterceptor
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
name|phase
operator|.
name|Phase
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|SoapJMSInInterceptor
extends|extends
name|AbstractSoapInterceptor
block|{
specifier|public
name|SoapJMSInInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|RECEIVE
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|AttachmentInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
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
if|if
condition|(
name|headers
operator|!=
literal|null
condition|)
block|{
name|checkContentType
argument_list|(
name|message
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|checkContentEncoding
argument_list|(
name|message
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|checkRequestURI
argument_list|(
name|message
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|checkSoapAction
argument_list|(
name|message
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|checkBindingVersion
argument_list|(
name|message
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|checkJMSMessageFormat
argument_list|(
name|message
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @param message      * @param headers      */
specifier|private
name|void
name|checkContentEncoding
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|contentEncodingList
init|=
name|headers
operator|.
name|get
argument_list|(
name|SoapJMSConstants
operator|.
name|CONTENTENCODING_FIELD
argument_list|)
decl_stmt|;
name|JMSFault
name|jmsFault
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|contentEncodingList
operator|!=
literal|null
operator|&&
name|contentEncodingList
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|String
name|contentEncoding
init|=
name|contentEncodingList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|"gzip"
operator|.
name|equals
argument_list|(
name|contentEncoding
argument_list|)
condition|)
block|{
name|jmsFault
operator|=
name|JMSFaultFactory
operator|.
name|createContentEncodingNotSupported
argument_list|(
name|contentEncoding
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|jmsFault
operator|!=
literal|null
condition|)
block|{
name|Fault
name|f
init|=
name|createFault
argument_list|(
name|message
argument_list|,
name|jmsFault
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
throw|throw
name|f
throw|;
block|}
block|}
block|}
comment|/**      * @param message      * @param headers      */
specifier|private
name|void
name|checkJMSMessageFormat
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|mt
init|=
name|headers
operator|.
name|get
argument_list|(
name|SoapJMSConstants
operator|.
name|JMS_MESSAGE_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|mt
operator|!=
literal|null
operator|&&
name|mt
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|String
name|messageType
init|=
name|mt
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|"text"
operator|.
name|equals
argument_list|(
name|messageType
argument_list|)
operator|&&
operator|!
literal|"byte"
operator|.
name|equals
argument_list|(
name|messageType
argument_list|)
condition|)
block|{
name|JMSFault
name|jmsFault
init|=
name|JMSFaultFactory
operator|.
name|createUnsupportedJMSMessageFormatFault
argument_list|(
name|messageType
argument_list|)
decl_stmt|;
name|Fault
name|f
init|=
name|createFault
argument_list|(
name|message
argument_list|,
name|jmsFault
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
throw|throw
name|f
throw|;
block|}
block|}
block|}
block|}
comment|/**      * @param message      * @param headers      */
specifier|private
name|void
name|checkSoapAction
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|)
block|{
name|JMSFault
name|jmsFault
init|=
literal|null
decl_stmt|;
name|String
name|soapAction
init|=
literal|null
decl_stmt|;
name|String
name|contentType
init|=
literal|null
decl_stmt|;
name|String
name|contentTypeAction
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|ct
init|=
name|headers
operator|.
name|get
argument_list|(
name|SoapJMSConstants
operator|.
name|CONTENTTYPE_FIELD
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|sa
init|=
name|headers
operator|.
name|get
argument_list|(
name|SoapJMSConstants
operator|.
name|SOAPACTION_FIELD
argument_list|)
decl_stmt|;
if|if
condition|(
name|sa
operator|!=
literal|null
operator|&&
name|sa
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|soapAction
operator|=
name|sa
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|soapAction
operator|!=
literal|null
operator|&&
name|soapAction
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|soapAction
operator|=
name|soapAction
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|soapAction
operator|.
name|lastIndexOf
argument_list|(
literal|"\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ct
operator|!=
literal|null
operator|&&
name|ct
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|contentType
operator|=
name|ct
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|contentType
operator|!=
literal|null
operator|&&
name|contentType
operator|.
name|indexOf
argument_list|(
literal|"action="
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|contentTypeAction
operator|=
name|contentType
operator|.
name|substring
argument_list|(
name|contentType
operator|.
name|indexOf
argument_list|(
literal|"action="
argument_list|)
operator|+
literal|7
argument_list|)
expr_stmt|;
if|if
condition|(
name|contentTypeAction
operator|.
name|indexOf
argument_list|(
literal|";"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|contentTypeAction
operator|=
name|contentTypeAction
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|contentTypeAction
operator|.
name|indexOf
argument_list|(
literal|";"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|contentTypeAction
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|contentTypeAction
operator|=
name|contentTypeAction
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|contentTypeAction
operator|.
name|lastIndexOf
argument_list|(
literal|"\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|contentTypeAction
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|sa
operator|==
literal|null
condition|)
block|{
comment|//miss SOAPJMS_soapAction header, throw fault per the spec
name|jmsFault
operator|=
name|JMSFaultFactory
operator|.
name|createMissingSoapActionFault
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|soapAction
operator|!=
literal|null
operator|&&
operator|!
name|soapAction
operator|.
name|equals
argument_list|(
name|contentTypeAction
argument_list|)
condition|)
block|{
name|jmsFault
operator|=
name|JMSFaultFactory
operator|.
name|createMismatchedSoapActionFault
argument_list|(
name|contentTypeAction
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|jmsFault
operator|!=
literal|null
condition|)
block|{
name|Fault
name|f
init|=
name|createFault
argument_list|(
name|message
argument_list|,
name|jmsFault
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
throw|throw
name|f
throw|;
block|}
block|}
block|}
comment|/**      * @param message      * @param headers      */
specifier|private
name|void
name|checkRequestURI
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ru
init|=
name|headers
operator|.
name|get
argument_list|(
name|SoapJMSConstants
operator|.
name|REQUESTURI_FIELD
argument_list|)
decl_stmt|;
name|JMSFault
name|jmsFault
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ru
operator|!=
literal|null
operator|&&
name|ru
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|String
name|requestURI
init|=
name|ru
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|mr
init|=
name|headers
operator|.
name|get
argument_list|(
name|SoapJMSConstants
operator|.
name|MALFORMED_REQUESTURI
argument_list|)
decl_stmt|;
if|if
condition|(
name|mr
operator|!=
literal|null
operator|&&
name|mr
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|&&
name|mr
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|equals
argument_list|(
literal|"true"
argument_list|)
condition|)
block|{
name|jmsFault
operator|=
name|JMSFaultFactory
operator|.
name|createMalformedRequestURIFault
argument_list|(
name|requestURI
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|trn
init|=
name|headers
operator|.
name|get
argument_list|(
name|SoapJMSConstants
operator|.
name|TARGET_SERVICE_IN_REQUESTURI
argument_list|)
decl_stmt|;
if|if
condition|(
name|trn
operator|!=
literal|null
operator|&&
name|trn
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|&&
name|trn
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|equals
argument_list|(
literal|"true"
argument_list|)
condition|)
block|{
name|jmsFault
operator|=
name|JMSFaultFactory
operator|.
name|createTargetServiceNotAllowedInRequestURIFault
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|jmsFault
operator|=
name|JMSFaultFactory
operator|.
name|createMissingRequestURIFault
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|jmsFault
operator|!=
literal|null
condition|)
block|{
name|Fault
name|f
init|=
name|createFault
argument_list|(
name|message
argument_list|,
name|jmsFault
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
throw|throw
name|f
throw|;
block|}
block|}
block|}
comment|/**      * @param message      * @param headers      */
specifier|private
name|void
name|checkContentType
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ct
init|=
name|headers
operator|.
name|get
argument_list|(
name|SoapJMSConstants
operator|.
name|CONTENTTYPE_FIELD
argument_list|)
decl_stmt|;
name|JMSFault
name|jmsFault
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ct
operator|!=
literal|null
operator|&&
name|ct
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|String
name|contentType
init|=
name|ct
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|contentType
operator|.
name|startsWith
argument_list|(
literal|"text/xml"
argument_list|)
operator|&&
operator|!
name|contentType
operator|.
name|startsWith
argument_list|(
literal|"application/soap+xml"
argument_list|)
operator|&&
operator|!
name|contentType
operator|.
name|startsWith
argument_list|(
literal|"multipart/related"
argument_list|)
condition|)
block|{
name|jmsFault
operator|=
name|JMSFaultFactory
operator|.
name|createContentTypeMismatchFault
argument_list|(
name|contentType
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|jmsFault
operator|=
name|JMSFaultFactory
operator|.
name|createMissingContentTypeFault
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|jmsFault
operator|!=
literal|null
condition|)
block|{
name|Fault
name|f
init|=
name|createFault
argument_list|(
name|message
argument_list|,
name|jmsFault
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
throw|throw
name|f
throw|;
block|}
block|}
block|}
comment|/**      * @param message      * @param headers      */
specifier|private
name|void
name|checkBindingVersion
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|bv
init|=
name|headers
operator|.
name|get
argument_list|(
name|SoapJMSConstants
operator|.
name|BINDINGVERSION_FIELD
argument_list|)
decl_stmt|;
if|if
condition|(
name|bv
operator|!=
literal|null
operator|&&
name|bv
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|String
name|bindingVersion
init|=
name|bv
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|"1.0"
operator|.
name|equals
argument_list|(
name|bindingVersion
argument_list|)
condition|)
block|{
name|JMSFault
name|jmsFault
init|=
name|JMSFaultFactory
operator|.
name|createUnrecognizedBindingVerionFault
argument_list|(
name|bindingVersion
argument_list|)
decl_stmt|;
name|Fault
name|f
init|=
name|createFault
argument_list|(
name|message
argument_list|,
name|jmsFault
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
throw|throw
name|f
throw|;
block|}
block|}
block|}
block|}
specifier|private
name|Fault
name|createFault
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|JMSFault
name|jmsFault
parameter_list|)
block|{
name|Fault
name|f
init|=
literal|null
decl_stmt|;
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
name|Binding
name|b
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|e
condition|)
block|{
name|b
operator|=
name|e
operator|.
name|getBinding
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|b
condition|)
block|{
name|SoapFaultFactory
name|sff
init|=
operator|new
name|SoapFaultFactory
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|f
operator|=
name|sff
operator|.
name|createFault
argument_list|(
name|jmsFault
argument_list|)
expr_stmt|;
block|}
return|return
name|f
return|;
block|}
block|}
end_class

end_unit

