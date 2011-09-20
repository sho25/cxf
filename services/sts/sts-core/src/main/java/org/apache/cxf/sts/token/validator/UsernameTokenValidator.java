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
name|sts
operator|.
name|token
operator|.
name|validator
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
name|security
operator|.
name|Principal
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
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|Callback
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|UnsupportedCallbackException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
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
name|sts
operator|.
name|QNameConstants
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
name|sts
operator|.
name|STSPropertiesMBean
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
name|sts
operator|.
name|request
operator|.
name|ReceivedToken
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
name|sts
operator|.
name|request
operator|.
name|TokenRequirements
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
name|ws
operator|.
name|security
operator|.
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|secext
operator|.
name|EncodedString
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
name|ws
operator|.
name|security
operator|.
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|secext
operator|.
name|PasswordString
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
name|ws
operator|.
name|security
operator|.
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|secext
operator|.
name|UsernameTokenType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|WSConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|WSPasswordCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|WSSConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
operator|.
name|WSUsernameTokenPrincipal
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|components
operator|.
name|crypto
operator|.
name|Crypto
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
operator|.
name|message
operator|.
name|token
operator|.
name|BinarySecurity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|message
operator|.
name|token
operator|.
name|UsernameToken
import|;
end_import

begin_comment
comment|/**  * This class validates a wsse UsernameToken.  */
end_comment

begin_class
specifier|public
class|class
name|UsernameTokenValidator
implements|implements
name|TokenValidator
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
name|UsernameTokenValidator
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Return true if this TokenValidator implementation is capable of validating the      * ReceivedToken argument.      */
specifier|public
name|boolean
name|canHandleToken
parameter_list|(
name|ReceivedToken
name|validateTarget
parameter_list|)
block|{
if|if
condition|(
name|validateTarget
operator|.
name|getToken
argument_list|()
operator|instanceof
name|UsernameTokenType
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Validate a Token using the given TokenValidatorParameters.      */
specifier|public
name|TokenValidatorResponse
name|validateToken
parameter_list|(
name|TokenValidatorParameters
name|tokenParameters
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Validating UsernameToken"
argument_list|)
expr_stmt|;
name|TokenRequirements
name|tokenRequirements
init|=
name|tokenParameters
operator|.
name|getTokenRequirements
argument_list|()
decl_stmt|;
name|ReceivedToken
name|validateTarget
init|=
name|tokenRequirements
operator|.
name|getValidateTarget
argument_list|()
decl_stmt|;
name|STSPropertiesMBean
name|stsProperties
init|=
name|tokenParameters
operator|.
name|getStsProperties
argument_list|()
decl_stmt|;
name|Crypto
name|sigCrypto
init|=
name|stsProperties
operator|.
name|getSignatureCrypto
argument_list|()
decl_stmt|;
name|CallbackHandler
name|callbackHandler
init|=
name|stsProperties
operator|.
name|getCallbackHandler
argument_list|()
decl_stmt|;
name|RequestData
name|requestData
init|=
operator|new
name|RequestData
argument_list|()
decl_stmt|;
name|requestData
operator|.
name|setSigCrypto
argument_list|(
name|sigCrypto
argument_list|)
expr_stmt|;
name|WSSConfig
name|wssConfig
init|=
name|WSSConfig
operator|.
name|getNewInstance
argument_list|()
decl_stmt|;
name|requestData
operator|.
name|setWssConfig
argument_list|(
name|wssConfig
argument_list|)
expr_stmt|;
name|requestData
operator|.
name|setCallbackHandler
argument_list|(
name|callbackHandler
argument_list|)
expr_stmt|;
name|TokenValidatorResponse
name|response
init|=
operator|new
name|TokenValidatorResponse
argument_list|()
decl_stmt|;
name|response
operator|.
name|setValid
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|validateTarget
operator|!=
literal|null
operator|&&
name|validateTarget
operator|.
name|isUsernameToken
argument_list|()
condition|)
block|{
comment|//
comment|// Parse the JAXB object
comment|//
name|String
name|passwordType
init|=
literal|null
decl_stmt|;
name|String
name|passwordValue
init|=
literal|null
decl_stmt|;
name|String
name|nonce
init|=
literal|null
decl_stmt|;
name|String
name|created
init|=
literal|null
decl_stmt|;
name|UsernameTokenType
name|usernameTokenType
init|=
operator|(
name|UsernameTokenType
operator|)
name|validateTarget
operator|.
name|getToken
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|any
range|:
name|usernameTokenType
operator|.
name|getAny
argument_list|()
control|)
block|{
if|if
condition|(
name|any
operator|instanceof
name|JAXBElement
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|anyElement
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|any
decl_stmt|;
if|if
condition|(
name|QNameConstants
operator|.
name|PASSWORD
operator|.
name|equals
argument_list|(
name|anyElement
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|PasswordString
name|passwordString
init|=
operator|(
name|PasswordString
operator|)
name|anyElement
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|passwordType
operator|=
name|passwordString
operator|.
name|getType
argument_list|()
expr_stmt|;
name|passwordValue
operator|=
name|passwordString
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|QNameConstants
operator|.
name|NONCE
operator|.
name|equals
argument_list|(
name|anyElement
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|EncodedString
name|nonceES
init|=
operator|(
name|EncodedString
operator|)
name|anyElement
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|nonce
operator|=
name|nonceES
operator|.
name|getValue
argument_list|()
expr_stmt|;
comment|// Encoding Type must be equal to Base64Binary
if|if
condition|(
operator|!
name|BinarySecurity
operator|.
name|BASE64_ENCODING
operator|.
name|equals
argument_list|(
name|nonceES
operator|.
name|getEncodingType
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|errorMsg
init|=
literal|"The UsernameToken nonce element has a bad encoding type"
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
name|errorMsg
operator|+
literal|" : "
operator|+
name|nonceES
operator|.
name|getEncodingType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|any
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|element
init|=
operator|(
name|Element
operator|)
name|any
decl_stmt|;
if|if
condition|(
name|WSConstants
operator|.
name|WSU_NS
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|&&
name|WSConstants
operator|.
name|CREATED_LN
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|created
operator|=
name|element
operator|.
name|getTextContent
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|//
comment|// Validate the token
comment|//
try|try
block|{
name|boolean
name|valid
init|=
name|verifyPassword
argument_list|(
name|usernameTokenType
operator|.
name|getUsername
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|,
name|passwordValue
argument_list|,
name|passwordType
argument_list|,
name|nonce
argument_list|,
name|created
argument_list|,
name|requestData
argument_list|)
decl_stmt|;
name|response
operator|.
name|setValid
argument_list|(
name|valid
argument_list|)
expr_stmt|;
if|if
condition|(
name|valid
condition|)
block|{
name|Principal
name|principal
init|=
name|createPrincipal
argument_list|(
name|usernameTokenType
operator|.
name|getUsername
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|,
name|passwordValue
argument_list|,
name|passwordType
argument_list|,
name|nonce
argument_list|,
name|created
argument_list|)
decl_stmt|;
name|response
operator|.
name|setPrincipal
argument_list|(
name|principal
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|""
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|response
return|;
block|}
end_class

begin_function
specifier|private
name|boolean
name|verifyPassword
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|passwordValue
parameter_list|,
name|String
name|passwordType
parameter_list|,
name|String
name|nonce
parameter_list|,
name|String
name|createdTime
parameter_list|,
name|RequestData
name|requestData
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|WSPasswordCallback
name|pwCb
init|=
operator|new
name|WSPasswordCallback
argument_list|(
name|username
argument_list|,
literal|null
argument_list|,
name|passwordType
argument_list|,
name|WSPasswordCallback
operator|.
name|USERNAME_TOKEN
argument_list|,
name|requestData
argument_list|)
decl_stmt|;
try|try
block|{
name|requestData
operator|.
name|getCallbackHandler
argument_list|()
operator|.
name|handle
argument_list|(
operator|new
name|Callback
index|[]
block|{
name|pwCb
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|""
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|FAILED_AUTHENTICATION
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|UnsupportedCallbackException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|""
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|FAILED_AUTHENTICATION
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|String
name|origPassword
init|=
name|pwCb
operator|.
name|getPassword
argument_list|()
decl_stmt|;
if|if
condition|(
name|origPassword
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Callback supplied no password for: "
operator|+
name|username
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|WSConstants
operator|.
name|PASSWORD_DIGEST
operator|.
name|equals
argument_list|(
name|passwordType
argument_list|)
condition|)
block|{
name|String
name|passDigest
init|=
name|UsernameToken
operator|.
name|doPasswordDigest
argument_list|(
name|nonce
argument_list|,
name|createdTime
argument_list|,
name|origPassword
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|passDigest
operator|.
name|equals
argument_list|(
name|passwordValue
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|!
name|origPassword
operator|.
name|equals
argument_list|(
name|passwordValue
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
end_function

begin_comment
comment|/**      * Create a principal based on the authenticated UsernameToken.      */
end_comment

begin_function
specifier|private
name|Principal
name|createPrincipal
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|passwordValue
parameter_list|,
name|String
name|passwordType
parameter_list|,
name|String
name|nonce
parameter_list|,
name|String
name|createdTime
parameter_list|)
block|{
name|boolean
name|hashed
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|WSConstants
operator|.
name|PASSWORD_DIGEST
operator|.
name|equals
argument_list|(
name|passwordType
argument_list|)
condition|)
block|{
name|hashed
operator|=
literal|true
expr_stmt|;
block|}
name|WSUsernameTokenPrincipal
name|principal
init|=
operator|new
name|WSUsernameTokenPrincipal
argument_list|(
name|username
argument_list|,
name|hashed
argument_list|)
decl_stmt|;
name|principal
operator|.
name|setNonce
argument_list|(
name|nonce
argument_list|)
expr_stmt|;
name|principal
operator|.
name|setPassword
argument_list|(
name|passwordValue
argument_list|)
expr_stmt|;
name|principal
operator|.
name|setCreatedTime
argument_list|(
name|createdTime
argument_list|)
expr_stmt|;
name|principal
operator|.
name|setPasswordType
argument_list|(
name|passwordType
argument_list|)
expr_stmt|;
return|return
name|principal
return|;
block|}
end_function

unit|}
end_unit

