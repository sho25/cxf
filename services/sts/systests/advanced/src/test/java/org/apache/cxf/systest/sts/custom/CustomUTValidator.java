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
name|systest
operator|.
name|sts
operator|.
name|custom
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|saaj
operator|.
name|SAAJInInterceptor
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
name|saaj
operator|.
name|SAAJUtils
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
name|common
operator|.
name|util
operator|.
name|XMLUtils
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
name|dom
operator|.
name|handler
operator|.
name|RequestData
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
name|dom
operator|.
name|validate
operator|.
name|Credential
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
name|dom
operator|.
name|validate
operator|.
name|UsernameTokenValidator
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
name|dom
operator|.
name|validate
operator|.
name|Validator
import|;
end_import

begin_comment
comment|/**  * A Validator that checks for a custom "realm" parameter in the RST request and only allows  * authentication if the value is equal to "custom-realm".  */
end_comment

begin_class
specifier|public
class|class
name|CustomUTValidator
implements|implements
name|Validator
block|{
specifier|public
name|Credential
name|validate
parameter_list|(
name|Credential
name|credential
parameter_list|,
name|RequestData
name|data
parameter_list|)
throws|throws
name|WSSecurityException
block|{
if|if
condition|(
name|credential
operator|==
literal|null
operator|||
name|credential
operator|.
name|getUsernametoken
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
literal|"noCredential"
argument_list|)
throw|;
block|}
comment|// Need to use SAAJ to get the SOAP Body as we are just using the UsernameTokenInterceptor
name|SOAPMessage
name|soapMessage
init|=
name|getSOAPMessage
argument_list|(
operator|(
name|SoapMessage
operator|)
name|data
operator|.
name|getMsgContext
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|Element
name|soapBody
init|=
name|SAAJUtils
operator|.
name|getBody
argument_list|(
name|soapMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|soapBody
operator|!=
literal|null
condition|)
block|{
comment|// Find custom Element in the SOAP Body
name|Element
name|realm
init|=
name|XMLUtils
operator|.
name|findElement
argument_list|(
name|soapBody
argument_list|,
literal|"realm"
argument_list|,
literal|"http://cxf.apache.org/custom"
argument_list|)
decl_stmt|;
if|if
condition|(
name|realm
operator|!=
literal|null
condition|)
block|{
name|String
name|realmStr
init|=
name|realm
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"custom-realm"
operator|.
name|equals
argument_list|(
name|realmStr
argument_list|)
condition|)
block|{
name|UsernameTokenValidator
name|validator
init|=
operator|new
name|UsernameTokenValidator
argument_list|()
decl_stmt|;
return|return
name|validator
operator|.
name|validate
argument_list|(
name|credential
argument_list|,
name|data
argument_list|)
return|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|SOAPException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
literal|"noCredential"
argument_list|)
throw|;
block|}
specifier|private
name|SOAPMessage
name|getSOAPMessage
parameter_list|(
name|SoapMessage
name|msg
parameter_list|)
block|{
name|SAAJInInterceptor
operator|.
name|INSTANCE
operator|.
name|handleMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
return|return
name|msg
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

