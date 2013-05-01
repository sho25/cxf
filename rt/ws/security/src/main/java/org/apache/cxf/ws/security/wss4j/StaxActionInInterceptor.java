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
name|ws
operator|.
name|security
operator|.
name|wss4j
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
name|logging
operator|.
name|Logger
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
name|SoapFault
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
name|SoapVersion
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
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|WSSecurityException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|stax
operator|.
name|ext
operator|.
name|WSSConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|stax
operator|.
name|securityEvent
operator|.
name|WSSecurityEventConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|stax
operator|.
name|securityEvent
operator|.
name|SecurityEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|stax
operator|.
name|securityEvent
operator|.
name|SecurityEventConstants
operator|.
name|Event
import|;
end_import

begin_comment
comment|/**  * This interceptor handles parsing the StaX WS-Security results (events) + checks to see  * whether the required Actions were fulfilled. If no Actions were defined in the configuration,  * then no checking is done on the received security events.  */
end_comment

begin_class
specifier|public
class|class
name|StaxActionInInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|SoapMessage
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
name|StaxActionInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|inActions
decl_stmt|;
specifier|public
name|StaxActionInInterceptor
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|inActions
parameter_list|)
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_PROTOCOL
argument_list|)
expr_stmt|;
name|this
operator|.
name|inActions
operator|=
name|inActions
expr_stmt|;
name|this
operator|.
name|getBefore
argument_list|()
operator|.
name|add
argument_list|(
name|StaxSecurityContextInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|soapMessage
parameter_list|)
throws|throws
name|Fault
block|{
if|if
condition|(
name|inActions
operator|==
literal|null
operator|||
name|inActions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|incomingSecurityEventList
init|=
operator|(
name|List
argument_list|<
name|SecurityEvent
argument_list|>
operator|)
name|soapMessage
operator|.
name|get
argument_list|(
name|SecurityEvent
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".in"
argument_list|)
decl_stmt|;
if|if
condition|(
name|incomingSecurityEventList
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Security processing failed (actions mismatch)"
argument_list|)
expr_stmt|;
name|WSSecurityException
name|ex
init|=
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|INVALID_SECURITY
argument_list|)
decl_stmt|;
throw|throw
name|createSoapFault
argument_list|(
name|soapMessage
operator|.
name|getVersion
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
for|for
control|(
name|String
name|action
range|:
name|inActions
control|)
block|{
name|Event
name|requiredEvent
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|WSSConstants
operator|.
name|TIMESTAMP
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|requiredEvent
operator|=
name|WSSecurityEventConstants
operator|.
name|Timestamp
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|WSSConstants
operator|.
name|USERNAMETOKEN
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|requiredEvent
operator|=
name|WSSecurityEventConstants
operator|.
name|UsernameToken
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|WSSConstants
operator|.
name|SIGNATURE
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|requiredEvent
operator|=
name|WSSecurityEventConstants
operator|.
name|SignatureValue
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|WSSConstants
operator|.
name|SAML_TOKEN_SIGNED
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
operator|||
name|WSSConstants
operator|.
name|SAML_TOKEN_UNSIGNED
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|requiredEvent
operator|=
name|WSSecurityEventConstants
operator|.
name|SamlToken
expr_stmt|;
block|}
if|if
condition|(
name|requiredEvent
operator|!=
literal|null
operator|&&
operator|!
name|isEventInResults
argument_list|(
name|requiredEvent
argument_list|,
name|incomingSecurityEventList
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Security processing failed (actions mismatch)"
argument_list|)
expr_stmt|;
name|WSSecurityException
name|ex
init|=
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|INVALID_SECURITY
argument_list|)
decl_stmt|;
throw|throw
name|createSoapFault
argument_list|(
name|soapMessage
operator|.
name|getVersion
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
if|if
condition|(
name|WSSConstants
operator|.
name|ENCRYPT
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|boolean
name|foundEncryptionPart
init|=
name|isEventInResults
argument_list|(
name|WSSecurityEventConstants
operator|.
name|EncryptedPart
argument_list|,
name|incomingSecurityEventList
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|foundEncryptionPart
condition|)
block|{
name|foundEncryptionPart
operator|=
name|isEventInResults
argument_list|(
name|WSSecurityEventConstants
operator|.
name|EncryptedElement
argument_list|,
name|incomingSecurityEventList
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|foundEncryptionPart
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Security processing failed (actions mismatch)"
argument_list|)
expr_stmt|;
name|WSSecurityException
name|ex
init|=
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|INVALID_SECURITY
argument_list|)
decl_stmt|;
throw|throw
name|createSoapFault
argument_list|(
name|soapMessage
operator|.
name|getVersion
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
block|}
specifier|private
name|boolean
name|isEventInResults
parameter_list|(
name|Event
name|event
parameter_list|,
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|incomingSecurityEventList
parameter_list|)
block|{
for|for
control|(
name|SecurityEvent
name|incomingEvent
range|:
name|incomingSecurityEventList
control|)
block|{
if|if
condition|(
name|event
operator|==
name|incomingEvent
operator|.
name|getSecurityEventType
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Create a SoapFault from a WSSecurityException, following the SOAP Message Security      * 1.1 specification, chapter 12 "Error Handling".      *       * When the Soap version is 1.1 then set the Fault/Code/Value from the fault code      * specified in the WSSecurityException (if it exists).      *       * Otherwise set the Fault/Code/Value to env:Sender and the Fault/Code/Subcode/Value      * as the fault code from the WSSecurityException.      */
specifier|private
name|SoapFault
name|createSoapFault
parameter_list|(
name|SoapVersion
name|version
parameter_list|,
name|WSSecurityException
name|e
parameter_list|)
block|{
name|SoapFault
name|fault
decl_stmt|;
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
name|faultCode
init|=
name|e
operator|.
name|getFaultCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|version
operator|.
name|getVersion
argument_list|()
operator|==
literal|1.1
operator|&&
name|faultCode
operator|!=
literal|null
condition|)
block|{
name|fault
operator|=
operator|new
name|SoapFault
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|,
name|faultCode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fault
operator|=
operator|new
name|SoapFault
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|,
name|version
operator|.
name|getSender
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|version
operator|.
name|getVersion
argument_list|()
operator|!=
literal|1.1
operator|&&
name|faultCode
operator|!=
literal|null
condition|)
block|{
name|fault
operator|.
name|setSubCode
argument_list|(
name|faultCode
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|fault
return|;
block|}
block|}
end_class

end_unit

