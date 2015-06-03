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
name|claims
operator|.
name|ClaimsAttributeStatementProvider
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
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|SAMLCallback
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
name|common
operator|.
name|saml
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
name|wss4j
operator|.
name|common
operator|.
name|saml
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
name|wss4j
operator|.
name|common
operator|.
name|saml
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
name|wss4j
operator|.
name|common
operator|.
name|saml
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
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|bean
operator|.
name|SubjectBean
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

begin_comment
comment|/**  * A TokenProvider implementation that provides a SAML Token.  */
end_comment

begin_class
specifier|public
class|class
name|SAMLTokenProvider
extends|extends
name|AbstractSAMLTokenProvider
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
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|SamlCustomHandler
name|samlCustomHandler
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
return|return
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
name|SamlAssertionWrapper
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
comment|// set the token in cache (only if the token is signed)
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
name|token
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
name|tokenParameters
operator|.
name|getPrincipal
argument_list|()
argument_list|,
name|tokenParameters
operator|.
name|getRealm
argument_list|()
argument_list|,
name|tokenParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getRenewing
argument_list|()
argument_list|)
decl_stmt|;
name|CacheUtils
operator|.
name|storeTokenInCache
argument_list|(
name|securityToken
argument_list|,
name|tokenParameters
operator|.
name|getTokenStore
argument_list|()
argument_list|,
name|signatureValue
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
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
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
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"AssertionID"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
block|}
name|response
operator|.
name|setCreated
argument_list|(
name|validFrom
operator|.
name|toDate
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|setExpires
argument_list|(
name|validTill
operator|.
name|toDate
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
name|LOG
operator|.
name|fine
argument_list|(
literal|"SAML Token successfully created"
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
name|authDecisionStatementProviders
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
name|authDecisionStatementProviders
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
specifier|public
name|void
name|setSamlCustomHandler
parameter_list|(
name|SamlCustomHandler
name|samlCustomHandler
parameter_list|)
block|{
name|this
operator|.
name|samlCustomHandler
operator|=
name|samlCustomHandler
expr_stmt|;
block|}
specifier|private
name|SamlAssertionWrapper
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
name|SAMLCallback
name|samlCallback
init|=
operator|new
name|SAMLCallback
argument_list|()
decl_stmt|;
name|SAMLUtil
operator|.
name|doSAMLCallback
argument_list|(
name|handler
argument_list|,
name|samlCallback
argument_list|)
expr_stmt|;
name|SamlAssertionWrapper
name|assertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|samlCallback
argument_list|)
decl_stmt|;
if|if
condition|(
name|samlCustomHandler
operator|!=
literal|null
condition|)
block|{
name|samlCustomHandler
operator|.
name|handle
argument_list|(
name|assertion
argument_list|,
name|tokenParameters
argument_list|)
expr_stmt|;
block|}
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
name|signToken
argument_list|(
name|assertion
argument_list|,
name|samlRealm
argument_list|,
name|stsProperties
argument_list|,
name|tokenParameters
operator|.
name|getKeyRequirements
argument_list|()
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
name|boolean
name|statementAdded
init|=
literal|false
decl_stmt|;
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
argument_list|<>
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
name|statementAdded
operator|=
literal|true
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
argument_list|<>
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
name|statementAdded
operator|=
literal|true
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
argument_list|<>
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
name|statementAdded
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
comment|// If no providers have been configured, then default to the ClaimsAttributeStatementProvider
comment|// If no Claims are available then use the DefaultAttributeStatementProvider
if|if
condition|(
operator|!
name|statementAdded
condition|)
block|{
name|attrBeanList
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|AttributeStatementProvider
name|attributeProvider
init|=
operator|new
name|ClaimsAttributeStatementProvider
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
if|if
condition|(
name|attributeBean
operator|!=
literal|null
condition|)
block|{
name|attrBeanList
operator|.
name|add
argument_list|(
name|attributeBean
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|attributeProvider
operator|=
operator|new
name|DefaultAttributeStatementProvider
argument_list|()
expr_stmt|;
name|attributeBean
operator|=
name|attributeProvider
operator|.
name|getStatement
argument_list|(
name|tokenParameters
argument_list|)
expr_stmt|;
name|attrBeanList
operator|.
name|add
argument_list|(
name|attributeBean
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Get the Subject and Conditions
name|SubjectProviderParameters
name|subjectProviderParameters
init|=
operator|new
name|SubjectProviderParameters
argument_list|()
decl_stmt|;
name|subjectProviderParameters
operator|.
name|setProviderParameters
argument_list|(
name|tokenParameters
argument_list|)
expr_stmt|;
name|subjectProviderParameters
operator|.
name|setDoc
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|subjectProviderParameters
operator|.
name|setSecret
argument_list|(
name|secret
argument_list|)
expr_stmt|;
name|subjectProviderParameters
operator|.
name|setAttrBeanList
argument_list|(
name|attrBeanList
argument_list|)
expr_stmt|;
name|subjectProviderParameters
operator|.
name|setAuthBeanList
argument_list|(
name|authBeanList
argument_list|)
expr_stmt|;
name|subjectProviderParameters
operator|.
name|setAuthDecisionBeanList
argument_list|(
name|authDecisionBeanList
argument_list|)
expr_stmt|;
name|SubjectBean
name|subjectBean
init|=
name|subjectProvider
operator|.
name|getSubject
argument_list|(
name|subjectProviderParameters
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
name|getReceivedKey
argument_list|()
operator|==
literal|null
operator|||
operator|(
name|keyRequirements
operator|.
name|getReceivedKey
argument_list|()
operator|.
name|getX509Cert
argument_list|()
operator|==
literal|null
operator|&&
name|keyRequirements
operator|.
name|getReceivedKey
argument_list|()
operator|.
name|getPublicKey
argument_list|()
operator|==
literal|null
operator|)
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

