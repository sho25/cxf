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
name|provider
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|KeyRequirements
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
name|sts
operator|.
name|token
operator|.
name|realm
operator|.
name|SAMLRealm
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
name|STSException
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
name|saml
operator|.
name|ext
operator|.
name|AssertionWrapper
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
name|saml
operator|.
name|ext
operator|.
name|SAMLParms
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
name|saml
operator|.
name|ext
operator|.
name|bean
operator|.
name|AttributeStatementBean
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
name|saml
operator|.
name|ext
operator|.
name|bean
operator|.
name|AuthDecisionStatementBean
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
name|saml
operator|.
name|ext
operator|.
name|bean
operator|.
name|AuthenticationStatementBean
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
name|saml
operator|.
name|ext
operator|.
name|bean
operator|.
name|ConditionsBean
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
name|saml
operator|.
name|ext
operator|.
name|bean
operator|.
name|SubjectBean
import|;
end_import

begin_comment
comment|/**  * A TokenProvider implementation that provides a SAML Token.  */
end_comment

begin_class
specifier|public
class|class
name|SAMLTokenProvider
implements|implements
name|TokenProvider
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
name|SAMLTokenProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|List
argument_list|<
name|AttributeStatementProvider
argument_list|>
name|attributeStatementProviders
decl_stmt|;
specifier|private
name|List
argument_list|<
name|AuthenticationStatementProvider
argument_list|>
name|authenticationStatementProviders
decl_stmt|;
specifier|private
name|List
argument_list|<
name|AuthDecisionStatementProvider
argument_list|>
name|authDecisionStatementProviders
decl_stmt|;
specifier|private
name|SubjectProvider
name|subjectProvider
init|=
operator|new
name|DefaultSubjectProvider
argument_list|()
decl_stmt|;
specifier|private
name|ConditionsProvider
name|conditionsProvider
init|=
operator|new
name|DefaultConditionsProvider
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|signToken
init|=
literal|true
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|SAMLRealm
argument_list|>
name|realmMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|SAMLRealm
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Return true if this TokenProvider implementation is capable of providing a token      * that corresponds to the given TokenType.      */
specifier|public
name|boolean
name|canHandleToken
parameter_list|(
name|String
name|tokenType
parameter_list|)
block|{
return|return
name|canHandleToken
argument_list|(
name|tokenType
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Return true if this TokenProvider implementation is capable of providing a token      * that corresponds to the given TokenType in a given realm.      */
specifier|public
name|boolean
name|canHandleToken
parameter_list|(
name|String
name|tokenType
parameter_list|,
name|String
name|realm
parameter_list|)
block|{
if|if
condition|(
name|realm
operator|!=
literal|null
operator|&&
operator|!
name|realmMap
operator|.
name|containsKey
argument_list|(
name|realm
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
operator|||
name|WSConstants
operator|.
name|SAML2_NS
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
operator|||
name|WSConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
operator|||
name|WSConstants
operator|.
name|SAML_NS
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
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
comment|/**      * Create a token given a TokenProviderParameters      */
specifier|public
name|TokenProviderResponse
name|createToken
parameter_list|(
name|TokenProviderParameters
name|tokenParameters
parameter_list|)
block|{
name|testKeyType
argument_list|(
name|tokenParameters
argument_list|)
expr_stmt|;
name|byte
index|[]
name|secret
init|=
literal|null
decl_stmt|;
name|byte
index|[]
name|entropyBytes
init|=
literal|null
decl_stmt|;
name|long
name|keySize
init|=
literal|0
decl_stmt|;
name|boolean
name|computedKey
init|=
literal|false
decl_stmt|;
name|KeyRequirements
name|keyRequirements
init|=
name|tokenParameters
operator|.
name|getKeyRequirements
argument_list|()
decl_stmt|;
name|TokenRequirements
name|tokenRequirements
init|=
name|tokenParameters
operator|.
name|getTokenRequirements
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Handling token of type: "
operator|+
name|tokenRequirements
operator|.
name|getTokenType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|STSConstants
operator|.
name|SYMMETRIC_KEY_KEYTYPE
operator|.
name|equals
argument_list|(
name|keyRequirements
operator|.
name|getKeyType
argument_list|()
argument_list|)
condition|)
block|{
name|SymmetricKeyHandler
name|keyHandler
init|=
operator|new
name|SymmetricKeyHandler
argument_list|(
name|tokenParameters
argument_list|)
decl_stmt|;
name|keyHandler
operator|.
name|createSymmetricKey
argument_list|()
expr_stmt|;
name|secret
operator|=
name|keyHandler
operator|.
name|getSecret
argument_list|()
expr_stmt|;
name|entropyBytes
operator|=
name|keyHandler
operator|.
name|getEntropyBytes
argument_list|()
expr_stmt|;
name|keySize
operator|=
name|keyHandler
operator|.
name|getKeySize
argument_list|()
expr_stmt|;
name|computedKey
operator|=
name|keyHandler
operator|.
name|isComputedKey
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|AssertionWrapper
name|assertion
init|=
name|createSamlToken
argument_list|(
name|tokenParameters
argument_list|,
name|secret
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|Element
name|token
init|=
name|assertion
operator|.
name|toDOM
argument_list|(
name|doc
argument_list|)
decl_stmt|;
comment|// set the token in cache
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
name|SecurityToken
name|securityToken
init|=
operator|new
name|SecurityToken
argument_list|(
name|assertion
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|securityToken
operator|.
name|setToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|securityToken
operator|.
name|setPrincipal
argument_list|(
name|tokenParameters
operator|.
name|getPrincipal
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|hash
init|=
literal|0
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
name|hash
operator|=
name|Arrays
operator|.
name|hashCode
argument_list|(
name|signatureValue
argument_list|)
expr_stmt|;
name|securityToken
operator|.
name|setAssociatedHash
argument_list|(
name|hash
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|tokenParameters
operator|.
name|getRealm
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Properties
name|props
init|=
name|securityToken
operator|.
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|props
operator|==
literal|null
condition|)
block|{
name|props
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
block|}
name|props
operator|.
name|setProperty
argument_list|(
name|STSConstants
operator|.
name|TOKEN_REALM
argument_list|,
name|tokenParameters
operator|.
name|getRealm
argument_list|()
argument_list|)
expr_stmt|;
name|securityToken
operator|.
name|setProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
name|Integer
name|timeToLive
init|=
call|(
name|int
call|)
argument_list|(
name|conditionsProvider
operator|.
name|getLifetime
argument_list|()
operator|*
literal|1000
argument_list|)
decl_stmt|;
name|tokenParameters
operator|.
name|getTokenStore
argument_list|()
operator|.
name|add
argument_list|(
name|securityToken
argument_list|,
name|timeToLive
argument_list|)
expr_stmt|;
block|}
name|TokenProviderResponse
name|response
init|=
operator|new
name|TokenProviderResponse
argument_list|()
decl_stmt|;
name|response
operator|.
name|setToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|String
name|tokenType
init|=
name|tokenRequirements
operator|.
name|getTokenType
argument_list|()
decl_stmt|;
if|if
condition|(
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
operator|||
name|WSConstants
operator|.
name|SAML2_NS
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
condition|)
block|{
name|response
operator|.
name|setTokenId
argument_list|(
name|token
operator|.
name|getAttribute
argument_list|(
literal|"ID"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|response
operator|.
name|setTokenId
argument_list|(
name|token
operator|.
name|getAttribute
argument_list|(
literal|"AssertionID"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|response
operator|.
name|setLifetime
argument_list|(
name|conditionsProvider
operator|.
name|getLifetime
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|setEntropy
argument_list|(
name|entropyBytes
argument_list|)
expr_stmt|;
if|if
condition|(
name|keySize
operator|>
literal|0
condition|)
block|{
name|response
operator|.
name|setKeySize
argument_list|(
name|keySize
argument_list|)
expr_stmt|;
block|}
name|response
operator|.
name|setComputedKey
argument_list|(
name|computedKey
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
catch|catch
parameter_list|(
name|Exception
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
name|STSException
argument_list|(
literal|"Can't serialize SAML assertion"
argument_list|,
name|e
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
block|}
comment|/**      * Set the List of AttributeStatementProviders.      */
specifier|public
name|void
name|setAttributeStatementProviders
parameter_list|(
name|List
argument_list|<
name|AttributeStatementProvider
argument_list|>
name|attributeStatementProviders
parameter_list|)
block|{
name|this
operator|.
name|attributeStatementProviders
operator|=
name|attributeStatementProviders
expr_stmt|;
block|}
comment|/**      * Get the List of AttributeStatementProviders.      */
specifier|public
name|List
argument_list|<
name|AttributeStatementProvider
argument_list|>
name|getAttributeStatementProviders
parameter_list|()
block|{
return|return
name|attributeStatementProviders
return|;
block|}
comment|/**      * Set the List of AuthenticationStatementProviders.      */
specifier|public
name|void
name|setAuthenticationStatementProviders
parameter_list|(
name|List
argument_list|<
name|AuthenticationStatementProvider
argument_list|>
name|authnStatementProviders
parameter_list|)
block|{
name|this
operator|.
name|authenticationStatementProviders
operator|=
name|authnStatementProviders
expr_stmt|;
block|}
comment|/**      * Get the List of AuthenticationStatementProviders.      */
specifier|public
name|List
argument_list|<
name|AuthenticationStatementProvider
argument_list|>
name|getAuthenticationStatementProviders
parameter_list|()
block|{
return|return
name|authenticationStatementProviders
return|;
block|}
comment|/**      * Set the List of AuthDecisionStatementProviders.      */
specifier|public
name|void
name|setAuthDecisionStatementProviders
parameter_list|(
name|List
argument_list|<
name|AuthDecisionStatementProvider
argument_list|>
name|authDecisionStatementProviders
parameter_list|)
block|{
name|this
operator|.
name|authenticationDecisionStatementProviders
operator|=
name|authDecisionStatementProviders
expr_stmt|;
block|}
comment|/**      * Get the List of AuthDecisionStatementProviders.      */
specifier|public
name|List
argument_list|<
name|AuthDecisionStatementProvider
argument_list|>
name|getAuthDecisionStatementProviders
parameter_list|()
block|{
return|return
name|authenticationDecisionStatementProviders
return|;
block|}
comment|/**      * Set the SubjectProvider.      */
specifier|public
name|void
name|setSubjectProvider
parameter_list|(
name|SubjectProvider
name|subjectProvider
parameter_list|)
block|{
name|this
operator|.
name|subjectProvider
operator|=
name|subjectProvider
expr_stmt|;
block|}
comment|/**      * Get the SubjectProvider.      */
specifier|public
name|SubjectProvider
name|getSubjectProvider
parameter_list|()
block|{
return|return
name|subjectProvider
return|;
block|}
comment|/**      * Set the ConditionsProvider      */
specifier|public
name|void
name|setConditionsProvider
parameter_list|(
name|ConditionsProvider
name|conditionsProvider
parameter_list|)
block|{
name|this
operator|.
name|conditionsProvider
operator|=
name|conditionsProvider
expr_stmt|;
block|}
comment|/**      * Get the ConditionsProvider      */
specifier|public
name|ConditionsProvider
name|getConditionsProvider
parameter_list|()
block|{
return|return
name|conditionsProvider
return|;
block|}
comment|/**      * Return whether the provided token will be signed or not. Default is true.      */
specifier|public
name|boolean
name|isSignToken
parameter_list|()
block|{
return|return
name|signToken
return|;
block|}
comment|/**      * Set whether the provided token will be signed or not. Default is true.      */
specifier|public
name|void
name|setSignToken
parameter_list|(
name|boolean
name|signToken
parameter_list|)
block|{
name|this
operator|.
name|signToken
operator|=
name|signToken
expr_stmt|;
block|}
comment|/**      * Set the map of realm->SAMLRealm for this token provider      * @param realms the map of realm->SAMLRealm for this token provider      */
specifier|public
name|void
name|setRealmMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|SAMLRealm
argument_list|>
name|realms
parameter_list|)
block|{
name|this
operator|.
name|realmMap
operator|=
name|realms
expr_stmt|;
block|}
comment|/**      * Get the map of realm->SAMLRealm for this token provider      * @return the map of realm->SAMLRealm for this token provider      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|SAMLRealm
argument_list|>
name|getRealmMap
parameter_list|()
block|{
return|return
name|realmMap
return|;
block|}
specifier|private
name|AssertionWrapper
name|createSamlToken
parameter_list|(
name|TokenProviderParameters
name|tokenParameters
parameter_list|,
name|byte
index|[]
name|secret
parameter_list|,
name|Document
name|doc
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|realm
init|=
name|tokenParameters
operator|.
name|getRealm
argument_list|()
decl_stmt|;
name|SAMLRealm
name|samlRealm
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|realm
operator|!=
literal|null
operator|&&
name|realmMap
operator|.
name|containsKey
argument_list|(
name|realm
argument_list|)
condition|)
block|{
name|samlRealm
operator|=
name|realmMap
operator|.
name|get
argument_list|(
name|realm
argument_list|)
expr_stmt|;
block|}
name|SamlCallbackHandler
name|handler
init|=
name|createCallbackHandler
argument_list|(
name|tokenParameters
argument_list|,
name|secret
argument_list|,
name|samlRealm
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|SAMLParms
name|samlParms
init|=
operator|new
name|SAMLParms
argument_list|()
decl_stmt|;
name|samlParms
operator|.
name|setCallbackHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|AssertionWrapper
name|assertion
init|=
operator|new
name|AssertionWrapper
argument_list|(
name|samlParms
argument_list|)
decl_stmt|;
if|if
condition|(
name|signToken
condition|)
block|{
name|STSPropertiesMBean
name|stsProperties
init|=
name|tokenParameters
operator|.
name|getStsProperties
argument_list|()
decl_stmt|;
name|String
name|alias
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|samlRealm
operator|!=
literal|null
condition|)
block|{
name|alias
operator|=
name|samlRealm
operator|.
name|getSignatureAlias
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|alias
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|alias
argument_list|)
condition|)
block|{
name|alias
operator|=
name|stsProperties
operator|.
name|getSignatureUsername
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|alias
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|alias
argument_list|)
condition|)
block|{
name|Crypto
name|signatureCrypto
init|=
name|stsProperties
operator|.
name|getSignatureCrypto
argument_list|()
decl_stmt|;
if|if
condition|(
name|signatureCrypto
operator|!=
literal|null
condition|)
block|{
name|alias
operator|=
name|signatureCrypto
operator|.
name|getDefaultX509Identifier
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Signature alias is null so using default alias: "
operator|+
name|alias
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Get the password
name|WSPasswordCallback
index|[]
name|cb
init|=
block|{
operator|new
name|WSPasswordCallback
argument_list|(
name|alias
argument_list|,
name|WSPasswordCallback
operator|.
name|SIGNATURE
argument_list|)
block|}
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Creating SAML Token"
argument_list|)
expr_stmt|;
name|stsProperties
operator|.
name|getCallbackHandler
argument_list|()
operator|.
name|handle
argument_list|(
name|cb
argument_list|)
expr_stmt|;
name|String
name|password
init|=
name|cb
index|[
literal|0
index|]
operator|.
name|getPassword
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Signing SAML Token"
argument_list|)
expr_stmt|;
name|boolean
name|useKeyValue
init|=
name|stsProperties
operator|.
name|getSignatureProperties
argument_list|()
operator|.
name|isUseKeyValue
argument_list|()
decl_stmt|;
name|assertion
operator|.
name|signAssertion
argument_list|(
name|alias
argument_list|,
name|password
argument_list|,
name|stsProperties
operator|.
name|getSignatureCrypto
argument_list|()
argument_list|,
name|useKeyValue
argument_list|)
expr_stmt|;
block|}
return|return
name|assertion
return|;
block|}
specifier|public
name|SamlCallbackHandler
name|createCallbackHandler
parameter_list|(
name|TokenProviderParameters
name|tokenParameters
parameter_list|,
name|byte
index|[]
name|secret
parameter_list|,
name|SAMLRealm
name|samlRealm
parameter_list|,
name|Document
name|doc
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Parse the AttributeStatements
name|List
argument_list|<
name|AttributeStatementBean
argument_list|>
name|attrBeanList
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|attributeStatementProviders
operator|!=
literal|null
operator|&&
name|attributeStatementProviders
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|attrBeanList
operator|=
operator|new
name|ArrayList
argument_list|<
name|AttributeStatementBean
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|AttributeStatementProvider
name|statementProvider
range|:
name|attributeStatementProviders
control|)
block|{
name|AttributeStatementBean
name|statementBean
init|=
name|statementProvider
operator|.
name|getStatement
argument_list|(
name|tokenParameters
argument_list|)
decl_stmt|;
if|if
condition|(
name|statementBean
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"AttributeStatements"
operator|+
name|statementBean
operator|.
name|toString
argument_list|()
operator|+
literal|"returned by AttributeStatementProvider "
operator|+
name|statementProvider
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|attrBeanList
operator|.
name|add
argument_list|(
name|statementBean
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Parse the AuthenticationStatements
name|List
argument_list|<
name|AuthenticationStatementBean
argument_list|>
name|authBeanList
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|authenticationStatementProviders
operator|!=
literal|null
operator|&&
name|authenticationStatementProviders
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|authBeanList
operator|=
operator|new
name|ArrayList
argument_list|<
name|AuthenticationStatementBean
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|AuthenticationStatementProvider
name|statementProvider
range|:
name|authenticationStatementProviders
control|)
block|{
name|AuthenticationStatementBean
name|statementBean
init|=
name|statementProvider
operator|.
name|getStatement
argument_list|(
name|tokenParameters
argument_list|)
decl_stmt|;
if|if
condition|(
name|statementBean
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"AuthenticationStatement"
operator|+
name|statementBean
operator|.
name|toString
argument_list|()
operator|+
literal|"returned by AuthenticationStatementProvider "
operator|+
name|statementProvider
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|authBeanList
operator|.
name|add
argument_list|(
name|statementBean
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Parse the AuthDecisionStatements
name|List
argument_list|<
name|AuthDecisionStatementBean
argument_list|>
name|authDecisionBeanList
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|authDecisionStatementProviders
operator|!=
literal|null
operator|&&
name|authDecisionStatementProviders
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|authDecisionBeanList
operator|=
operator|new
name|ArrayList
argument_list|<
name|AuthDecisionStatementBean
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|AuthDecisionStatementProvider
name|statementProvider
range|:
name|authDecisionStatementProviders
control|)
block|{
name|AuthDecisionStatementBean
name|statementBean
init|=
name|statementProvider
operator|.
name|getStatement
argument_list|(
name|tokenParameters
argument_list|)
decl_stmt|;
if|if
condition|(
name|statementBean
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"AuthDecisionStatement"
operator|+
name|statementBean
operator|.
name|toString
argument_list|()
operator|+
literal|"returned by AuthDecisionStatementProvider "
operator|+
name|statementProvider
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|authDecisionBeanList
operator|.
name|add
argument_list|(
name|statementBean
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// If no statements, then default to the DefaultAttributeStatementProvider
if|if
condition|(
operator|(
name|attrBeanList
operator|==
literal|null
operator|||
name|attrBeanList
operator|.
name|isEmpty
argument_list|()
operator|)
operator|&&
operator|(
name|authBeanList
operator|==
literal|null
operator|||
name|authBeanList
operator|.
name|isEmpty
argument_list|()
operator|)
operator|&&
operator|(
name|authDecisionBeanList
operator|==
literal|null
operator|||
name|authDecisionBeanList
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
name|attrBeanList
operator|=
operator|new
name|ArrayList
argument_list|<
name|AttributeStatementBean
argument_list|>
argument_list|()
expr_stmt|;
name|AttributeStatementProvider
name|attributeProvider
init|=
operator|new
name|DefaultAttributeStatementProvider
argument_list|()
decl_stmt|;
name|AttributeStatementBean
name|attributeBean
init|=
name|attributeProvider
operator|.
name|getStatement
argument_list|(
name|tokenParameters
argument_list|)
decl_stmt|;
name|attrBeanList
operator|.
name|add
argument_list|(
name|attributeBean
argument_list|)
expr_stmt|;
block|}
comment|// Get the Subject and Conditions
name|SubjectBean
name|subjectBean
init|=
name|subjectProvider
operator|.
name|getSubject
argument_list|(
name|tokenParameters
argument_list|,
name|doc
argument_list|,
name|secret
argument_list|)
decl_stmt|;
name|ConditionsBean
name|conditionsBean
init|=
name|conditionsProvider
operator|.
name|getConditions
argument_list|(
name|tokenParameters
argument_list|)
decl_stmt|;
comment|// Set all of the beans on the SamlCallbackHandler
name|SamlCallbackHandler
name|handler
init|=
operator|new
name|SamlCallbackHandler
argument_list|()
decl_stmt|;
name|handler
operator|.
name|setTokenProviderParameters
argument_list|(
name|tokenParameters
argument_list|)
expr_stmt|;
name|handler
operator|.
name|setSubjectBean
argument_list|(
name|subjectBean
argument_list|)
expr_stmt|;
name|handler
operator|.
name|setConditionsBean
argument_list|(
name|conditionsBean
argument_list|)
expr_stmt|;
name|handler
operator|.
name|setAttributeBeans
argument_list|(
name|attrBeanList
argument_list|)
expr_stmt|;
name|handler
operator|.
name|setAuthenticationBeans
argument_list|(
name|authBeanList
argument_list|)
expr_stmt|;
name|handler
operator|.
name|setAuthDecisionStatementBeans
argument_list|(
name|authDecisionBeanList
argument_list|)
expr_stmt|;
if|if
condition|(
name|samlRealm
operator|!=
literal|null
condition|)
block|{
name|handler
operator|.
name|setIssuer
argument_list|(
name|samlRealm
operator|.
name|getIssuer
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|handler
return|;
block|}
comment|/**      * Do some tests on the KeyType parameter.      */
specifier|private
name|void
name|testKeyType
parameter_list|(
name|TokenProviderParameters
name|tokenParameters
parameter_list|)
block|{
name|KeyRequirements
name|keyRequirements
init|=
name|tokenParameters
operator|.
name|getKeyRequirements
argument_list|()
decl_stmt|;
name|String
name|keyType
init|=
name|keyRequirements
operator|.
name|getKeyType
argument_list|()
decl_stmt|;
if|if
condition|(
name|STSConstants
operator|.
name|PUBLIC_KEY_KEYTYPE
operator|.
name|equals
argument_list|(
name|keyType
argument_list|)
condition|)
block|{
if|if
condition|(
name|keyRequirements
operator|.
name|getCertificate
argument_list|()
operator|==
literal|null
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
literal|"A PublicKey Keytype is requested, but no certificate is provided"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"No client certificate for PublicKey KeyType"
argument_list|,
name|STSException
operator|.
name|INVALID_REQUEST
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|STSConstants
operator|.
name|SYMMETRIC_KEY_KEYTYPE
operator|.
name|equals
argument_list|(
name|keyType
argument_list|)
operator|&&
operator|!
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
operator|.
name|equals
argument_list|(
name|keyType
argument_list|)
operator|&&
name|keyType
operator|!=
literal|null
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
literal|"An unknown KeyType was requested: "
operator|+
name|keyType
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Unknown KeyType"
argument_list|,
name|STSException
operator|.
name|INVALID_REQUEST
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

