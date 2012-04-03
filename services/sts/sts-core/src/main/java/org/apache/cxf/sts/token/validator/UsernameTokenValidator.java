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
name|Properties
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
name|CallbackHandler
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
name|JAXBContext
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
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
name|Marshaller
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
name|Document
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
name|helpers
operator|.
name|DOMUtils
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
name|STSConstants
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
name|ReceivedToken
operator|.
name|STATE
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
name|token
operator|.
name|realm
operator|.
name|UsernameTokenRealmCodec
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
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|tokenstore
operator|.
name|SecurityToken
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
name|CustomTokenPrincipal
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
name|UsernameToken
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
name|ws
operator|.
name|security
operator|.
name|validate
operator|.
name|Validator
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
specifier|private
name|Validator
name|validator
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|validate
operator|.
name|UsernameTokenValidator
argument_list|()
decl_stmt|;
specifier|private
name|UsernameTokenRealmCodec
name|usernameTokenRealmCodec
decl_stmt|;
comment|/**      * Set the WSS4J Validator instance to use to validate the token.      * @param validator the WSS4J Validator instance to use to validate the token      */
specifier|public
name|void
name|setValidator
parameter_list|(
name|Validator
name|validator
parameter_list|)
block|{
name|this
operator|.
name|validator
operator|=
name|validator
expr_stmt|;
block|}
comment|/**      * Set the UsernameTokenRealmCodec instance to use to return a realm from a validated token      * @param usernameTokenRealmCodec the UsernameTokenRealmCodec instance to use to return a       *                                realm from a validated token      */
specifier|public
name|void
name|setUsernameTokenRealmCodec
parameter_list|(
name|UsernameTokenRealmCodec
name|usernameTokenRealmCodec
parameter_list|)
block|{
name|this
operator|.
name|usernameTokenRealmCodec
operator|=
name|usernameTokenRealmCodec
expr_stmt|;
block|}
comment|/**      * Return true if this TokenValidator implementation is capable of validating the      * ReceivedToken argument.      */
specifier|public
name|boolean
name|canHandleToken
parameter_list|(
name|ReceivedToken
name|validateTarget
parameter_list|)
block|{
return|return
name|canHandleToken
argument_list|(
name|validateTarget
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Return true if this TokenValidator implementation is capable of validating the      * ReceivedToken argument. The realm is ignored in this token Validator.      */
specifier|public
name|boolean
name|canHandleToken
parameter_list|(
name|ReceivedToken
name|validateTarget
parameter_list|,
name|String
name|realm
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
name|ReceivedToken
name|validateTarget
init|=
name|tokenParameters
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|validateTarget
operator|.
name|setState
argument_list|(
name|STATE
operator|.
name|INVALID
argument_list|)
expr_stmt|;
name|response
operator|.
name|setToken
argument_list|(
name|validateTarget
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|validateTarget
operator|.
name|isUsernameToken
argument_list|()
condition|)
block|{
return|return
name|response
return|;
block|}
comment|//
comment|// Turn the JAXB UsernameTokenType into a DOM Element for validation
comment|//
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
comment|// Marshall the received JAXB object into a DOM Element
name|Element
name|usernameTokenElement
init|=
literal|null
decl_stmt|;
try|try
block|{
name|JAXBContext
name|jaxbContext
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
literal|"org.apache.cxf.ws.security.sts.provider.model"
argument_list|)
decl_stmt|;
name|Marshaller
name|marshaller
init|=
name|jaxbContext
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|Element
name|rootElement
init|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"root-element"
argument_list|)
decl_stmt|;
name|JAXBElement
argument_list|<
name|UsernameTokenType
argument_list|>
name|tokenType
init|=
operator|new
name|JAXBElement
argument_list|<
name|UsernameTokenType
argument_list|>
argument_list|(
name|QNameConstants
operator|.
name|USERNAME_TOKEN
argument_list|,
name|UsernameTokenType
operator|.
name|class
argument_list|,
name|usernameTokenType
argument_list|)
decl_stmt|;
name|marshaller
operator|.
name|marshal
argument_list|(
name|tokenType
argument_list|,
name|rootElement
argument_list|)
expr_stmt|;
name|usernameTokenElement
operator|=
operator|(
name|Element
operator|)
name|rootElement
operator|.
name|getFirstChild
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
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
return|return
name|response
return|;
block|}
comment|//
comment|// Validate the token
comment|//
try|try
block|{
name|boolean
name|allowNamespaceQualifiedPasswordTypes
init|=
name|wssConfig
operator|.
name|getAllowNamespaceQualifiedPasswordTypes
argument_list|()
decl_stmt|;
name|boolean
name|bspCompliant
init|=
name|wssConfig
operator|.
name|isWsiBSPCompliant
argument_list|()
decl_stmt|;
name|UsernameToken
name|ut
init|=
operator|new
name|UsernameToken
argument_list|(
name|usernameTokenElement
argument_list|,
name|allowNamespaceQualifiedPasswordTypes
argument_list|,
name|bspCompliant
argument_list|)
decl_stmt|;
comment|// The parsed principal is set independent whether validation is successful or not
name|response
operator|.
name|setPrincipal
argument_list|(
operator|new
name|CustomTokenPrincipal
argument_list|(
name|ut
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|ut
operator|.
name|getPassword
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|response
return|;
block|}
comment|// See if the UsernameToken is stored in the cache
name|int
name|hash
init|=
name|ut
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|SecurityToken
name|secToken
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|tokenParameters
operator|.
name|getTokenStore
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|secToken
operator|=
name|tokenParameters
operator|.
name|getTokenStore
argument_list|()
operator|.
name|getToken
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|hash
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|secToken
operator|!=
literal|null
operator|&&
name|secToken
operator|.
name|getTokenHash
argument_list|()
operator|!=
name|hash
condition|)
block|{
name|secToken
operator|=
literal|null
expr_stmt|;
block|}
block|}
if|if
condition|(
name|secToken
operator|==
literal|null
condition|)
block|{
name|Credential
name|credential
init|=
operator|new
name|Credential
argument_list|()
decl_stmt|;
name|credential
operator|.
name|setUsernametoken
argument_list|(
name|ut
argument_list|)
expr_stmt|;
name|validator
operator|.
name|validate
argument_list|(
name|credential
argument_list|,
name|requestData
argument_list|)
expr_stmt|;
block|}
name|Principal
name|principal
init|=
name|createPrincipal
argument_list|(
name|ut
operator|.
name|getName
argument_list|()
argument_list|,
name|ut
operator|.
name|getPassword
argument_list|()
argument_list|,
name|ut
operator|.
name|getPasswordType
argument_list|()
argument_list|,
name|ut
operator|.
name|getNonce
argument_list|()
argument_list|,
name|ut
operator|.
name|getCreated
argument_list|()
argument_list|)
decl_stmt|;
comment|// Get the realm of the UsernameToken
name|String
name|tokenRealm
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|usernameTokenRealmCodec
operator|!=
literal|null
condition|)
block|{
name|tokenRealm
operator|=
name|usernameTokenRealmCodec
operator|.
name|getRealmFromToken
argument_list|(
name|ut
argument_list|)
expr_stmt|;
comment|// verify the realm against the cached token
if|if
condition|(
name|secToken
operator|!=
literal|null
condition|)
block|{
name|Properties
name|props
init|=
name|secToken
operator|.
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|props
operator|!=
literal|null
condition|)
block|{
name|String
name|cachedRealm
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|STSConstants
operator|.
name|TOKEN_REALM
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|tokenRealm
operator|.
name|equals
argument_list|(
name|cachedRealm
argument_list|)
condition|)
block|{
return|return
name|response
return|;
block|}
block|}
block|}
block|}
name|response
operator|.
name|setPrincipal
argument_list|(
name|principal
argument_list|)
expr_stmt|;
name|response
operator|.
name|setTokenRealm
argument_list|(
name|tokenRealm
argument_list|)
expr_stmt|;
name|validateTarget
operator|.
name|setState
argument_list|(
name|STATE
operator|.
name|VALID
argument_list|)
expr_stmt|;
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
return|return
name|response
return|;
block|}
comment|/**      * Create a principal based on the authenticated UsernameToken.      */
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
block|}
end_class

end_unit

