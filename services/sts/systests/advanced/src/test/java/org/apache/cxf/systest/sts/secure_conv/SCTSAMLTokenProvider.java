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
name|secure_conv
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
name|provider
operator|.
name|AttributeStatementProvider
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
name|provider
operator|.
name|ConditionsProvider
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
name|provider
operator|.
name|DefaultAttributeStatementProvider
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
name|provider
operator|.
name|DefaultConditionsProvider
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
name|provider
operator|.
name|DefaultSubjectProvider
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
name|provider
operator|.
name|SamlCallbackHandler
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
name|provider
operator|.
name|SubjectProvider
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
name|provider
operator|.
name|TokenProvider
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
name|provider
operator|.
name|TokenProviderParameters
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
name|provider
operator|.
name|TokenProviderResponse
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
name|validator
operator|.
name|SCTValidator
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
comment|/**  * A TokenProvider implementation that provides a SAML Token that contains a Symmetric Key that is obtained  * from the TokenProviderParameter properties.  */
end_comment

begin_class
specifier|public
class|class
name|SCTSAMLTokenProvider
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
name|SCTSAMLTokenProvider
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
comment|/**      * Return true if this TokenProvider implementation is capable of providing a token      * that corresponds to the given TokenType.      */
specifier|public
name|boolean
name|canHandleToken
parameter_list|(
name|String
name|tokenType
parameter_list|)
block|{
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
return|return
name|canHandleToken
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
name|keyRequirements
operator|.
name|setKeyType
argument_list|(
name|STSConstants
operator|.
name|SYMMETRIC_KEY_KEYTYPE
argument_list|)
expr_stmt|;
name|secret
operator|=
operator|(
name|byte
index|[]
operator|)
name|tokenParameters
operator|.
name|getAdditionalProperties
argument_list|()
operator|.
name|get
argument_list|(
name|SCTValidator
operator|.
name|SCT_VALIDATOR_SECRET
argument_list|)
expr_stmt|;
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
name|SamlCallbackHandler
name|handler
init|=
name|createCallbackHandler
argument_list|(
name|tokenParameters
argument_list|,
name|secret
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
comment|// Get the password
name|String
name|alias
init|=
name|stsProperties
operator|.
name|getSignatureUsername
argument_list|()
decl_stmt|;
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
comment|// If no statements, then default to the DefaultAttributeStatementProvider
if|if
condition|(
name|attrBeanList
operator|==
literal|null
operator|||
name|attrBeanList
operator|.
name|isEmpty
argument_list|()
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
name|keyRequirements
operator|.
name|getReceivedKey
argument_list|()
operator|.
name|getX509Cert
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

