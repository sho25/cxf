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
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|JMSFaultFactory
block|{
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JMSFaultFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|JMSFaultFactory
parameter_list|()
block|{     }
specifier|public
specifier|static
name|JMSFault
name|createContentTypeMismatchFault
parameter_list|(
name|String
name|contentType
parameter_list|)
block|{
name|String
name|m
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
literal|"CONTENTTYPE_MISMATCH"
argument_list|,
name|LOG
argument_list|,
operator|new
name|Object
index|[]
block|{
name|contentType
block|}
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|createFault
argument_list|(
name|SoapJMSConstants
operator|.
name|getContentTypeMismatchQName
argument_list|()
argument_list|,
name|m
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JMSFault
name|createContentEncodingNotSupported
parameter_list|(
name|String
name|contentEncoding
parameter_list|)
block|{
name|String
name|m
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
literal|"CONTENT_ENCODING_NOT_SUPPORTED"
argument_list|,
name|LOG
argument_list|,
operator|new
name|Object
index|[]
block|{
name|contentEncoding
block|}
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|createFault
argument_list|(
name|SoapJMSConstants
operator|.
name|getContentTypeMismatchQName
argument_list|()
argument_list|,
name|m
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JMSFault
name|createMalformedRequestURIFault
parameter_list|(
name|String
name|requestURI
parameter_list|)
block|{
name|String
name|m
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
literal|"MALFORMED_REQUESTURI"
argument_list|,
name|LOG
argument_list|,
operator|new
name|Object
index|[]
block|{
name|requestURI
block|}
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|createFault
argument_list|(
name|SoapJMSConstants
operator|.
name|getMalformedRequestURIQName
argument_list|()
argument_list|,
name|m
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JMSFault
name|createMismatchedSoapActionFault
parameter_list|(
name|String
name|soapAction
parameter_list|)
block|{
name|String
name|m
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
literal|"MISMATCHED_SOAPACTION"
argument_list|,
name|LOG
argument_list|,
operator|new
name|Object
index|[]
block|{
name|soapAction
block|}
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|createFault
argument_list|(
name|SoapJMSConstants
operator|.
name|getMismatchedSoapActionQName
argument_list|()
argument_list|,
name|m
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JMSFault
name|createMissingContentTypeFault
parameter_list|()
block|{
name|String
name|m
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
literal|"MISSING_CONTENTTYPE"
argument_list|,
name|LOG
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|createFault
argument_list|(
name|SoapJMSConstants
operator|.
name|getMissingContentTypeQName
argument_list|()
argument_list|,
name|m
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JMSFault
name|createMissingRequestURIFault
parameter_list|()
block|{
name|String
name|m
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
literal|"MISSING_REQUESTURI"
argument_list|,
name|LOG
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|createFault
argument_list|(
name|SoapJMSConstants
operator|.
name|getMissingRequestURIQName
argument_list|()
argument_list|,
name|m
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JMSFault
name|createTargetServiceNotAllowedInRequestURIFault
parameter_list|()
block|{
name|String
name|m
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
literal|"TARGET_SERVICE_NOT_ALLOWED_IN_REQUESTURI"
argument_list|,
name|LOG
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|createFault
argument_list|(
name|SoapJMSConstants
operator|.
name|getTargetServiceNotAllowedInRequestURIQName
argument_list|()
argument_list|,
name|m
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JMSFault
name|createUnrecognizedBindingVerionFault
parameter_list|(
name|String
name|bindingVersion
parameter_list|)
block|{
name|String
name|m
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
literal|"UNRECOGNIZED_BINDINGVERSION"
argument_list|,
name|LOG
argument_list|,
operator|new
name|Object
index|[]
block|{
name|bindingVersion
block|}
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|createFault
argument_list|(
name|SoapJMSConstants
operator|.
name|getUnrecognizedBindingVersionQName
argument_list|()
argument_list|,
name|m
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JMSFault
name|createUnsupportedJMSMessageFormatFault
parameter_list|(
name|String
name|messageFormat
parameter_list|)
block|{
name|String
name|m
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
literal|"UNSUPPORTED_JMSMESSAGEFORMAT"
argument_list|,
name|LOG
argument_list|,
operator|new
name|Object
index|[]
block|{
name|messageFormat
block|}
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|createFault
argument_list|(
name|SoapJMSConstants
operator|.
name|getUnsupportedJMSMessageFormatQName
argument_list|()
argument_list|,
name|m
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|JMSFault
name|createFault
parameter_list|(
name|QName
name|faultCode
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|JMSFaultType
name|jmsFaultType
init|=
operator|new
name|JMSFaultType
argument_list|()
decl_stmt|;
name|jmsFaultType
operator|.
name|setFaultCode
argument_list|(
name|faultCode
argument_list|)
expr_stmt|;
name|JMSFault
name|jmsFault
init|=
operator|new
name|JMSFault
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|jmsFault
operator|.
name|setJmsFaultType
argument_list|(
name|jmsFaultType
argument_list|)
expr_stmt|;
name|jmsFault
operator|.
name|setSender
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|jmsFault
return|;
block|}
block|}
end_class

end_unit

