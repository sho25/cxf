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
name|security
operator|.
name|cert
operator|.
name|X509Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Set
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
name|cache
operator|.
name|CacheUtils
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
name|CertConstraintsParser
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
name|SAMLRealmCodec
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
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|tokenstore
operator|.
name|TokenStore
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
name|principal
operator|.
name|SAMLTokenPrincipalImpl
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
name|saml
operator|.
name|SAMLKeyInfo
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
name|saml
operator|.
name|SAMLUtil
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
name|saml
operator|.
name|SamlAssertionWrapper
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
name|WSConstants
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
name|WSDocInfo
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
name|engine
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
name|saml
operator|.
name|WSSSAMLKeyInfoProcessor
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
name|SignatureTrustValidator
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

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|DateTime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|common
operator|.
name|SAMLVersion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xmlsec
operator|.
name|signature
operator|.
name|KeyInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xmlsec
operator|.
name|signature
operator|.
name|Signature
import|;
end_import

begin_comment
comment|/**  * Validate a SAML Assertion. It is valid if it was issued and signed by this STS.  */
end_comment

begin_class
specifier|public
class|class
name|SAMLTokenValidator
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
name|SAMLTokenValidator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Validator
name|validator
init|=
operator|new
name|SignatureTrustValidator
argument_list|()
decl_stmt|;
specifier|private
name|CertConstraintsParser
name|certConstraints
init|=
operator|new
name|CertConstraintsParser
argument_list|()
decl_stmt|;
specifier|private
name|SAMLRealmCodec
name|samlRealmCodec
decl_stmt|;
specifier|private
name|SAMLRoleParser
name|samlRoleParser
init|=
operator|new
name|DefaultSAMLRoleParser
argument_list|()
decl_stmt|;
comment|/**      * Whether to validate the signature of the Assertion (if it exists) against the       * relevant profile. Default is true.      */
specifier|private
name|boolean
name|validateSignatureAgainstProfile
init|=
literal|true
decl_stmt|;
comment|/**      * Set a list of Strings corresponding to regular expression constraints on the subject DN      * of a certificate that was used to sign a received Assertion      */
specifier|public
name|void
name|setSubjectConstraints
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|subjectConstraints
parameter_list|)
block|{
name|certConstraints
operator|.
name|setSubjectConstraints
argument_list|(
name|subjectConstraints
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * Set the SAMLRealmCodec instance to use to return a realm from a validated token      * @param samlRealmCodec the SAMLRealmCodec instance to use to return a realm from a validated token      */
specifier|public
name|void
name|setSamlRealmCodec
parameter_list|(
name|SAMLRealmCodec
name|samlRealmCodec
parameter_list|)
block|{
name|this
operator|.
name|samlRealmCodec
operator|=
name|samlRealmCodec
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
comment|/**      * Return true if this TokenValidator implementation is capable of validating the      * ReceivedToken argument. The realm is ignored in this Validator.      */
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
name|Object
name|token
init|=
name|validateTarget
operator|.
name|getToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|token
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|tokenElement
init|=
operator|(
name|Element
operator|)
name|token
decl_stmt|;
name|String
name|namespace
init|=
name|tokenElement
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|String
name|localname
init|=
name|tokenElement
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|WSConstants
operator|.
name|SAML_NS
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
operator|||
name|WSConstants
operator|.
name|SAML2_NS
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
operator|)
operator|&&
literal|"Assertion"
operator|.
name|equals
argument_list|(
name|localname
argument_list|)
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
literal|"Validating SAML Token"
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
name|isDOMElement
argument_list|()
condition|)
block|{
return|return
name|response
return|;
block|}
try|try
block|{
name|Element
name|validateTargetElement
init|=
operator|(
name|Element
operator|)
name|validateTarget
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|SamlAssertionWrapper
name|assertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|validateTargetElement
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|assertion
operator|.
name|isSigned
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"The received assertion is not signed, and therefore not trusted"
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
name|RequestData
name|requestData
init|=
operator|new
name|RequestData
argument_list|()
decl_stmt|;
name|requestData
operator|.
name|setSigVerCrypto
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
name|requestData
operator|.
name|setMsgContext
argument_list|(
name|tokenParameters
operator|.
name|getMessageContext
argument_list|()
argument_list|)
expr_stmt|;
name|requestData
operator|.
name|setSubjectCertConstraints
argument_list|(
name|certConstraints
operator|.
name|getCompiledSubjectContraints
argument_list|()
argument_list|)
expr_stmt|;
name|requestData
operator|.
name|setWsDocInfo
argument_list|(
operator|new
name|WSDocInfo
argument_list|(
name|validateTargetElement
operator|.
name|getOwnerDocument
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// Verify the signature
name|Signature
name|sig
init|=
name|assertion
operator|.
name|getSignature
argument_list|()
decl_stmt|;
name|KeyInfo
name|keyInfo
init|=
name|sig
operator|.
name|getKeyInfo
argument_list|()
decl_stmt|;
name|SAMLKeyInfo
name|samlKeyInfo
init|=
name|SAMLUtil
operator|.
name|getCredentialFromKeyInfo
argument_list|(
name|keyInfo
operator|.
name|getDOM
argument_list|()
argument_list|,
operator|new
name|WSSSAMLKeyInfoProcessor
argument_list|(
name|requestData
argument_list|)
argument_list|,
name|sigCrypto
argument_list|)
decl_stmt|;
name|assertion
operator|.
name|verifySignature
argument_list|(
name|samlKeyInfo
argument_list|)
expr_stmt|;
name|SecurityToken
name|secToken
init|=
literal|null
decl_stmt|;
name|byte
index|[]
name|signatureValue
init|=
name|assertion
operator|.
name|getSignatureValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|tokenParameters
operator|.
name|getTokenStore
argument_list|()
operator|!=
literal|null
operator|&&
name|signatureValue
operator|!=
literal|null
operator|&&
name|signatureValue
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|int
name|hash
init|=
name|Arrays
operator|.
name|hashCode
argument_list|(
name|signatureValue
argument_list|)
decl_stmt|;
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
operator|!=
literal|null
operator|&&
name|secToken
operator|.
name|isExpired
argument_list|()
condition|)
block|{
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
literal|"Token: "
operator|+
name|secToken
operator|.
name|getId
argument_list|()
operator|+
literal|" is in the cache but expired - revalidating"
argument_list|)
expr_stmt|;
block|}
name|secToken
operator|=
literal|null
expr_stmt|;
block|}
name|Principal
name|principal
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|secToken
operator|==
literal|null
condition|)
block|{
comment|// Validate the assertion against schemas/profiles
name|validateAssertion
argument_list|(
name|assertion
argument_list|)
expr_stmt|;
comment|// Now verify trust on the signature
name|Credential
name|trustCredential
init|=
operator|new
name|Credential
argument_list|()
decl_stmt|;
name|trustCredential
operator|.
name|setPublicKey
argument_list|(
name|samlKeyInfo
operator|.
name|getPublicKey
argument_list|()
argument_list|)
expr_stmt|;
name|trustCredential
operator|.
name|setCertificates
argument_list|(
name|samlKeyInfo
operator|.
name|getCerts
argument_list|()
argument_list|)
expr_stmt|;
name|trustCredential
operator|=
name|validator
operator|.
name|validate
argument_list|(
name|trustCredential
argument_list|,
name|requestData
argument_list|)
expr_stmt|;
name|principal
operator|=
name|trustCredential
operator|.
name|getPrincipal
argument_list|()
expr_stmt|;
comment|// Finally check that subject DN of the signing certificate matches a known constraint
name|X509Certificate
name|cert
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|trustCredential
operator|.
name|getCertificates
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cert
operator|=
name|trustCredential
operator|.
name|getCertificates
argument_list|()
index|[
literal|0
index|]
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|certConstraints
operator|.
name|matches
argument_list|(
name|cert
argument_list|)
condition|)
block|{
return|return
name|response
return|;
block|}
block|}
if|if
condition|(
name|principal
operator|==
literal|null
condition|)
block|{
name|principal
operator|=
operator|new
name|SAMLTokenPrincipalImpl
argument_list|(
name|assertion
argument_list|)
expr_stmt|;
block|}
comment|// Parse roles from the validated token
if|if
condition|(
name|samlRoleParser
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|Principal
argument_list|>
name|roles
init|=
name|samlRoleParser
operator|.
name|parseRolesFromAssertion
argument_list|(
name|principal
argument_list|,
literal|null
argument_list|,
name|assertion
argument_list|)
decl_stmt|;
name|response
operator|.
name|setRoles
argument_list|(
name|roles
argument_list|)
expr_stmt|;
block|}
comment|// Get the realm of the SAML token
name|String
name|tokenRealm
init|=
literal|null
decl_stmt|;
name|SAMLRealmCodec
name|codec
init|=
name|samlRealmCodec
decl_stmt|;
if|if
condition|(
name|codec
operator|==
literal|null
condition|)
block|{
name|codec
operator|=
name|stsProperties
operator|.
name|getSamlRealmCodec
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|codec
operator|!=
literal|null
condition|)
block|{
name|tokenRealm
operator|=
name|codec
operator|.
name|getRealmFromToken
argument_list|(
name|assertion
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
operator|(
name|String
operator|)
name|props
operator|.
name|get
argument_list|(
name|STSConstants
operator|.
name|TOKEN_REALM
argument_list|)
decl_stmt|;
if|if
condition|(
name|cachedRealm
operator|!=
literal|null
operator|&&
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
name|setTokenRealm
argument_list|(
name|tokenRealm
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|validateConditions
argument_list|(
name|assertion
argument_list|,
name|validateTarget
argument_list|)
condition|)
block|{
return|return
name|response
return|;
block|}
comment|// Store the successfully validated token in the cache
if|if
condition|(
name|secToken
operator|==
literal|null
condition|)
block|{
name|storeTokenInCache
argument_list|(
name|tokenParameters
operator|.
name|getTokenStore
argument_list|()
argument_list|,
name|assertion
argument_list|,
name|tokenParameters
operator|.
name|getPrincipal
argument_list|()
argument_list|,
name|tokenRealm
argument_list|)
expr_stmt|;
block|}
comment|// Add the SamlAssertionWrapper to the properties, as the claims are required to be transformed
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|addProps
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|addProps
operator|.
name|put
argument_list|(
name|SamlAssertionWrapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|assertion
argument_list|)
expr_stmt|;
name|response
operator|.
name|setAdditionalProperties
argument_list|(
name|addProps
argument_list|)
expr_stmt|;
name|response
operator|.
name|setPrincipal
argument_list|(
name|principal
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
name|LOG
operator|.
name|fine
argument_list|(
literal|"SAML Token successfully validated"
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
comment|/**      * Validate the assertion against schemas/profiles      */
specifier|protected
name|void
name|validateAssertion
parameter_list|(
name|SamlAssertionWrapper
name|assertion
parameter_list|)
throws|throws
name|WSSecurityException
block|{
if|if
condition|(
name|validateSignatureAgainstProfile
condition|)
block|{
name|assertion
operator|.
name|validateSignatureAgainstProfile
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|boolean
name|validateConditions
parameter_list|(
name|SamlAssertionWrapper
name|assertion
parameter_list|,
name|ReceivedToken
name|validateTarget
parameter_list|)
block|{
name|DateTime
name|validFrom
init|=
literal|null
decl_stmt|;
name|DateTime
name|validTill
init|=
literal|null
decl_stmt|;
name|DateTime
name|issueInstant
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|assertion
operator|.
name|getSamlVersion
argument_list|()
operator|.
name|equals
argument_list|(
name|SAMLVersion
operator|.
name|VERSION_20
argument_list|)
condition|)
block|{
name|validFrom
operator|=
name|assertion
operator|.
name|getSaml2
argument_list|()
operator|.
name|getConditions
argument_list|()
operator|.
name|getNotBefore
argument_list|()
expr_stmt|;
name|validTill
operator|=
name|assertion
operator|.
name|getSaml2
argument_list|()
operator|.
name|getConditions
argument_list|()
operator|.
name|getNotOnOrAfter
argument_list|()
expr_stmt|;
name|issueInstant
operator|=
name|assertion
operator|.
name|getSaml2
argument_list|()
operator|.
name|getIssueInstant
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|validFrom
operator|=
name|assertion
operator|.
name|getSaml1
argument_list|()
operator|.
name|getConditions
argument_list|()
operator|.
name|getNotBefore
argument_list|()
expr_stmt|;
name|validTill
operator|=
name|assertion
operator|.
name|getSaml1
argument_list|()
operator|.
name|getConditions
argument_list|()
operator|.
name|getNotOnOrAfter
argument_list|()
expr_stmt|;
name|issueInstant
operator|=
name|assertion
operator|.
name|getSaml1
argument_list|()
operator|.
name|getIssueInstant
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|validFrom
operator|!=
literal|null
operator|&&
name|validFrom
operator|.
name|isAfterNow
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"SAML Token condition not met"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|validTill
operator|!=
literal|null
operator|&&
name|validTill
operator|.
name|isBeforeNow
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"SAML Token condition not met"
argument_list|)
expr_stmt|;
name|validateTarget
operator|.
name|setState
argument_list|(
name|STATE
operator|.
name|EXPIRED
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|issueInstant
operator|!=
literal|null
operator|&&
name|issueInstant
operator|.
name|isAfterNow
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"SAML Token IssueInstant not met"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|storeTokenInCache
parameter_list|(
name|TokenStore
name|tokenStore
parameter_list|,
name|SamlAssertionWrapper
name|assertion
parameter_list|,
name|Principal
name|principal
parameter_list|,
name|String
name|tokenRealm
parameter_list|)
throws|throws
name|WSSecurityException
block|{
comment|// Store the successfully validated token in the cache
name|byte
index|[]
name|signatureValue
init|=
name|assertion
operator|.
name|getSignatureValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|tokenStore
operator|!=
literal|null
operator|&&
name|signatureValue
operator|!=
literal|null
operator|&&
name|signatureValue
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|DateTime
name|validTill
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|assertion
operator|.
name|getSamlVersion
argument_list|()
operator|.
name|equals
argument_list|(
name|SAMLVersion
operator|.
name|VERSION_20
argument_list|)
condition|)
block|{
name|validTill
operator|=
name|assertion
operator|.
name|getSaml2
argument_list|()
operator|.
name|getConditions
argument_list|()
operator|.
name|getNotOnOrAfter
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|validTill
operator|=
name|assertion
operator|.
name|getSaml1
argument_list|()
operator|.
name|getConditions
argument_list|()
operator|.
name|getNotOnOrAfter
argument_list|()
expr_stmt|;
block|}
name|SecurityToken
name|securityToken
init|=
name|CacheUtils
operator|.
name|createSecurityTokenForStorage
argument_list|(
name|assertion
operator|.
name|getElement
argument_list|()
argument_list|,
name|assertion
operator|.
name|getId
argument_list|()
argument_list|,
name|validTill
operator|.
name|toDate
argument_list|()
argument_list|,
name|principal
argument_list|,
name|tokenRealm
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|CacheUtils
operator|.
name|storeTokenInCache
argument_list|(
name|securityToken
argument_list|,
name|tokenStore
argument_list|,
name|signatureValue
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|SAMLRoleParser
name|getSamlRoleParser
parameter_list|()
block|{
return|return
name|samlRoleParser
return|;
block|}
specifier|public
name|void
name|setSamlRoleParser
parameter_list|(
name|SAMLRoleParser
name|samlRoleParser
parameter_list|)
block|{
name|this
operator|.
name|samlRoleParser
operator|=
name|samlRoleParser
expr_stmt|;
block|}
comment|/**      * Whether to validate the signature of the Assertion (if it exists) against the       * relevant profile. Default is true.      */
specifier|public
name|boolean
name|isValidateSignatureAgainstProfile
parameter_list|()
block|{
return|return
name|validateSignatureAgainstProfile
return|;
block|}
comment|/**      * Whether to validate the signature of the Assertion (if it exists) against the       * relevant profile. Default is true.      */
specifier|public
name|void
name|setValidateSignatureAgainstProfile
parameter_list|(
name|boolean
name|validateSignatureAgainstProfile
parameter_list|)
block|{
name|this
operator|.
name|validateSignatureAgainstProfile
operator|=
name|validateSignatureAgainstProfile
expr_stmt|;
block|}
block|}
end_class

end_unit

