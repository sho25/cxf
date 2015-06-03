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
name|PublicKey
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|x500
operator|.
name|X500Principal
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
name|ReceivedKey
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
name|service
operator|.
name|EncryptionProperties
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
name|crypto
operator|.
name|CryptoType
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
name|saml
operator|.
name|bean
operator|.
name|KeyInfoBean
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
name|KeyInfoBean
operator|.
name|CERT_IDENTIFIER
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
name|common
operator|.
name|saml
operator|.
name|builder
operator|.
name|SAML1Constants
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
name|builder
operator|.
name|SAML2Constants
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
name|message
operator|.
name|WSSecEncryptedKey
import|;
end_import

begin_comment
comment|/**  * A default implementation of SubjectProvider to create a SAML Assertion. The Subject name is the name  * of the current principal, the subject name qualifier is a default URL that can be configured, and the  * subject confirmation method is created according to the token type and key type. If the Subject  * Confirmation Method is SymmetricKey or PublicKey, the appropriate KeyInfoBean object is created and  * attached to the Subject.  */
end_comment

begin_class
specifier|public
class|class
name|DefaultSubjectProvider
implements|implements
name|SubjectProvider
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
name|DefaultSubjectProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|subjectNameQualifier
init|=
literal|"http://cxf.apache.org/sts"
decl_stmt|;
specifier|private
name|String
name|subjectNameIDFormat
init|=
name|SAML2Constants
operator|.
name|NAMEID_FORMAT_UNSPECIFIED
decl_stmt|;
comment|/**      * Set the SubjectNameQualifier.      */
specifier|public
name|void
name|setSubjectNameQualifier
parameter_list|(
name|String
name|subjectNameQualifier
parameter_list|)
block|{
name|this
operator|.
name|subjectNameQualifier
operator|=
name|subjectNameQualifier
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Setting Subject Name Qualifier: "
operator|+
name|subjectNameQualifier
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the SubjectNameIDFormat.      */
specifier|public
name|void
name|setSubjectNameIDFormat
parameter_list|(
name|String
name|subjectNameIDFormat
parameter_list|)
block|{
name|this
operator|.
name|subjectNameIDFormat
operator|=
name|subjectNameIDFormat
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Setting Subject Name format: "
operator|+
name|subjectNameIDFormat
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get a SubjectBean object.      */
specifier|public
name|SubjectBean
name|getSubject
parameter_list|(
name|TokenProviderParameters
name|providerParameters
parameter_list|,
name|Document
name|doc
parameter_list|,
name|byte
index|[]
name|secret
parameter_list|)
block|{
comment|// 1. Get the principal
name|Principal
name|principal
init|=
name|getPrincipal
argument_list|(
name|providerParameters
argument_list|)
decl_stmt|;
if|if
condition|(
name|principal
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Error in getting principal"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Error in getting principal"
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
comment|// 2. Create the SubjectBean using the principal
name|SubjectBean
name|subjectBean
init|=
name|createSubjectBean
argument_list|(
name|principal
argument_list|,
name|providerParameters
argument_list|)
decl_stmt|;
comment|// 3. Create the KeyInfoBean and set it on the SubjectBean
name|KeyInfoBean
name|keyInfo
init|=
name|createKeyInfo
argument_list|(
name|providerParameters
argument_list|,
name|doc
argument_list|,
name|secret
argument_list|)
decl_stmt|;
name|subjectBean
operator|.
name|setKeyInfo
argument_list|(
name|keyInfo
argument_list|)
expr_stmt|;
return|return
name|subjectBean
return|;
block|}
comment|/**      * Get the Principal (which is used as the Subject). By default, we check the following (in order):      *  - A valid OnBehalfOf principal      *  - A valid ActAs principal      *  - A valid principal associated with a token received as ValidateTarget      *  - The principal associated with the request. We don't need to check to see if it is "valid" here, as it      *    is not parsed by the STS (but rather the WS-Security layer).      */
specifier|protected
name|Principal
name|getPrincipal
parameter_list|(
name|TokenProviderParameters
name|providerParameters
parameter_list|)
block|{
name|Principal
name|principal
init|=
literal|null
decl_stmt|;
comment|//TokenValidator in IssueOperation has validated the ReceivedToken
comment|//if validation was successful, the principal was set in ReceivedToken
if|if
condition|(
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getOnBehalfOf
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ReceivedToken
name|receivedToken
init|=
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getOnBehalfOf
argument_list|()
decl_stmt|;
if|if
condition|(
name|receivedToken
operator|.
name|getState
argument_list|()
operator|.
name|equals
argument_list|(
name|STATE
operator|.
name|VALID
argument_list|)
condition|)
block|{
name|principal
operator|=
name|receivedToken
operator|.
name|getPrincipal
argument_list|()
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getActAs
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ReceivedToken
name|receivedToken
init|=
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getActAs
argument_list|()
decl_stmt|;
if|if
condition|(
name|receivedToken
operator|.
name|getState
argument_list|()
operator|.
name|equals
argument_list|(
name|STATE
operator|.
name|VALID
argument_list|)
condition|)
block|{
name|principal
operator|=
name|receivedToken
operator|.
name|getPrincipal
argument_list|()
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getValidateTarget
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ReceivedToken
name|receivedToken
init|=
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getValidateTarget
argument_list|()
decl_stmt|;
if|if
condition|(
name|receivedToken
operator|.
name|getState
argument_list|()
operator|.
name|equals
argument_list|(
name|STATE
operator|.
name|VALID
argument_list|)
condition|)
block|{
name|principal
operator|=
name|receivedToken
operator|.
name|getPrincipal
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|principal
operator|=
name|providerParameters
operator|.
name|getPrincipal
argument_list|()
expr_stmt|;
block|}
return|return
name|principal
return|;
block|}
comment|/**      * Create the SubjectBean using the specified principal.      */
specifier|protected
name|SubjectBean
name|createSubjectBean
parameter_list|(
name|Principal
name|principal
parameter_list|,
name|TokenProviderParameters
name|providerParameters
parameter_list|)
block|{
name|TokenRequirements
name|tokenRequirements
init|=
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
decl_stmt|;
name|KeyRequirements
name|keyRequirements
init|=
name|providerParameters
operator|.
name|getKeyRequirements
argument_list|()
decl_stmt|;
name|String
name|tokenType
init|=
name|tokenRequirements
operator|.
name|getTokenType
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
name|String
name|confirmationMethod
init|=
name|getSubjectConfirmationMethod
argument_list|(
name|tokenType
argument_list|,
name|keyType
argument_list|)
decl_stmt|;
name|String
name|subjectName
init|=
name|principal
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|SAML2Constants
operator|.
name|NAMEID_FORMAT_UNSPECIFIED
operator|.
name|equals
argument_list|(
name|subjectNameIDFormat
argument_list|)
operator|&&
name|principal
operator|instanceof
name|X500Principal
condition|)
block|{
comment|// Just use the "cn" instead of the entire DN
try|try
block|{
name|String
name|principalName
init|=
name|principal
operator|.
name|getName
argument_list|()
decl_stmt|;
name|int
name|index
init|=
name|principalName
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
decl_stmt|;
name|principalName
operator|=
name|principalName
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|,
name|principalName
operator|.
name|indexOf
argument_list|(
literal|','
argument_list|,
name|index
argument_list|)
argument_list|)
expr_stmt|;
name|subjectName
operator|=
name|principalName
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|subjectName
operator|=
name|principal
operator|.
name|getName
argument_list|()
expr_stmt|;
comment|//Ignore, not X500 compliant thus use the whole string as the value
block|}
block|}
name|SubjectBean
name|subjectBean
init|=
operator|new
name|SubjectBean
argument_list|(
name|subjectName
argument_list|,
name|subjectNameQualifier
argument_list|,
name|confirmationMethod
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Creating new subject with principal name: "
operator|+
name|principal
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|subjectNameIDFormat
operator|!=
literal|null
operator|&&
name|subjectNameIDFormat
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|subjectBean
operator|.
name|setSubjectNameIDFormat
argument_list|(
name|subjectNameIDFormat
argument_list|)
expr_stmt|;
block|}
return|return
name|subjectBean
return|;
block|}
comment|/**      * Get the SubjectConfirmation method given a tokenType and keyType      */
specifier|protected
name|String
name|getSubjectConfirmationMethod
parameter_list|(
name|String
name|tokenType
parameter_list|,
name|String
name|keyType
parameter_list|)
block|{
if|if
condition|(
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
if|if
condition|(
name|STSConstants
operator|.
name|SYMMETRIC_KEY_KEYTYPE
operator|.
name|equals
argument_list|(
name|keyType
argument_list|)
operator|||
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
return|return
name|SAML1Constants
operator|.
name|CONF_HOLDER_KEY
return|;
block|}
else|else
block|{
return|return
name|SAML1Constants
operator|.
name|CONF_BEARER
return|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|STSConstants
operator|.
name|SYMMETRIC_KEY_KEYTYPE
operator|.
name|equals
argument_list|(
name|keyType
argument_list|)
operator|||
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
return|return
name|SAML2Constants
operator|.
name|CONF_HOLDER_KEY
return|;
block|}
else|else
block|{
return|return
name|SAML2Constants
operator|.
name|CONF_BEARER
return|;
block|}
block|}
block|}
comment|/**      * Create and return the KeyInfoBean to be inserted into the SubjectBean      */
specifier|protected
name|KeyInfoBean
name|createKeyInfo
parameter_list|(
name|TokenProviderParameters
name|providerParameters
parameter_list|,
name|Document
name|doc
parameter_list|,
name|byte
index|[]
name|secret
parameter_list|)
block|{
name|KeyRequirements
name|keyRequirements
init|=
name|providerParameters
operator|.
name|getKeyRequirements
argument_list|()
decl_stmt|;
name|STSPropertiesMBean
name|stsProperties
init|=
name|providerParameters
operator|.
name|getStsProperties
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
name|SYMMETRIC_KEY_KEYTYPE
operator|.
name|equals
argument_list|(
name|keyType
argument_list|)
condition|)
block|{
name|Crypto
name|crypto
init|=
name|stsProperties
operator|.
name|getEncryptionCrypto
argument_list|()
decl_stmt|;
name|EncryptionProperties
name|encryptionProperties
init|=
name|providerParameters
operator|.
name|getEncryptionProperties
argument_list|()
decl_stmt|;
name|String
name|encryptionName
init|=
name|encryptionProperties
operator|.
name|getEncryptionName
argument_list|()
decl_stmt|;
if|if
condition|(
name|encryptionName
operator|==
literal|null
condition|)
block|{
comment|// Fall back on the STS encryption name
name|encryptionName
operator|=
name|stsProperties
operator|.
name|getEncryptionUsername
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|encryptionName
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"No encryption Name is configured for Symmetric KeyType"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"No Encryption Name is configured"
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
name|CryptoType
name|cryptoType
init|=
literal|null
decl_stmt|;
comment|// Check for using of service endpoint (AppliesTo) as certificate identifier
if|if
condition|(
name|STSConstants
operator|.
name|USE_ENDPOINT_AS_CERT_ALIAS
operator|.
name|equals
argument_list|(
name|encryptionName
argument_list|)
condition|)
block|{
if|if
condition|(
name|providerParameters
operator|.
name|getAppliesToAddress
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|STSException
argument_list|(
literal|"AppliesTo is not initilaized for encryption name "
operator|+
name|STSConstants
operator|.
name|USE_ENDPOINT_AS_CERT_ALIAS
argument_list|)
throw|;
block|}
name|cryptoType
operator|=
operator|new
name|CryptoType
argument_list|(
name|CryptoType
operator|.
name|TYPE
operator|.
name|ENDPOINT
argument_list|)
expr_stmt|;
name|cryptoType
operator|.
name|setEndpoint
argument_list|(
name|providerParameters
operator|.
name|getAppliesToAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cryptoType
operator|=
operator|new
name|CryptoType
argument_list|(
name|CryptoType
operator|.
name|TYPE
operator|.
name|ALIAS
argument_list|)
expr_stmt|;
name|cryptoType
operator|.
name|setAlias
argument_list|(
name|encryptionName
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|X509Certificate
index|[]
name|certs
init|=
name|crypto
operator|.
name|getX509Certificates
argument_list|(
name|cryptoType
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|certs
operator|==
literal|null
operator|)
operator|||
operator|(
name|certs
operator|.
name|length
operator|==
literal|0
operator|)
condition|)
block|{
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Encryption certificate is not found for alias: "
operator|+
name|encryptionName
argument_list|)
throw|;
block|}
name|KeyInfoBean
name|keyInfo
init|=
name|createEncryptedKeyKeyInfo
argument_list|(
name|certs
index|[
literal|0
index|]
argument_list|,
name|secret
argument_list|,
name|doc
argument_list|,
name|encryptionProperties
argument_list|,
name|crypto
argument_list|)
decl_stmt|;
return|return
name|keyInfo
return|;
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
throw|throw
operator|new
name|STSException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
elseif|else
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
name|ReceivedKey
name|receivedKey
init|=
name|keyRequirements
operator|.
name|getReceivedKey
argument_list|()
decl_stmt|;
comment|// Validate UseKey trust
if|if
condition|(
name|stsProperties
operator|.
name|isValidateUseKey
argument_list|()
operator|&&
name|stsProperties
operator|.
name|getSignatureCrypto
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|receivedKey
operator|.
name|getX509Cert
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Collection
argument_list|<
name|Pattern
argument_list|>
name|constraints
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
name|stsProperties
operator|.
name|getSignatureCrypto
argument_list|()
operator|.
name|verifyTrust
argument_list|(
operator|new
name|X509Certificate
index|[]
block|{
name|receivedKey
operator|.
name|getX509Cert
argument_list|()
block|}
argument_list|,
literal|false
argument_list|,
name|constraints
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Error in trust validation of UseKey: "
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Error in trust validation of UseKey"
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|receivedKey
operator|.
name|getPublicKey
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|stsProperties
operator|.
name|getSignatureCrypto
argument_list|()
operator|.
name|verifyTrust
argument_list|(
name|receivedKey
operator|.
name|getPublicKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Error in trust validation of UseKey: "
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Error in trust validation of UseKey"
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
block|}
block|}
return|return
name|createPublicKeyKeyInfo
argument_list|(
name|receivedKey
operator|.
name|getX509Cert
argument_list|()
argument_list|,
name|receivedKey
operator|.
name|getPublicKey
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Create a KeyInfoBean that contains an X.509 certificate or Public Key      */
specifier|protected
specifier|static
name|KeyInfoBean
name|createPublicKeyKeyInfo
parameter_list|(
name|X509Certificate
name|certificate
parameter_list|,
name|PublicKey
name|publicKey
parameter_list|)
block|{
name|KeyInfoBean
name|keyInfo
init|=
operator|new
name|KeyInfoBean
argument_list|()
decl_stmt|;
if|if
condition|(
name|certificate
operator|!=
literal|null
condition|)
block|{
name|keyInfo
operator|.
name|setCertificate
argument_list|(
name|certificate
argument_list|)
expr_stmt|;
name|keyInfo
operator|.
name|setCertIdentifer
argument_list|(
name|CERT_IDENTIFIER
operator|.
name|X509_CERT
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|publicKey
operator|!=
literal|null
condition|)
block|{
name|keyInfo
operator|.
name|setPublicKey
argument_list|(
name|publicKey
argument_list|)
expr_stmt|;
name|keyInfo
operator|.
name|setCertIdentifer
argument_list|(
name|CERT_IDENTIFIER
operator|.
name|KEY_VALUE
argument_list|)
expr_stmt|;
block|}
return|return
name|keyInfo
return|;
block|}
comment|/**      * Create an EncryptedKey KeyInfo.      */
specifier|protected
specifier|static
name|KeyInfoBean
name|createEncryptedKeyKeyInfo
parameter_list|(
name|X509Certificate
name|certificate
parameter_list|,
name|byte
index|[]
name|secret
parameter_list|,
name|Document
name|doc
parameter_list|,
name|EncryptionProperties
name|encryptionProperties
parameter_list|,
name|Crypto
name|encryptionCrypto
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|KeyInfoBean
name|keyInfo
init|=
operator|new
name|KeyInfoBean
argument_list|()
decl_stmt|;
comment|// Create an EncryptedKey
name|WSSecEncryptedKey
name|encrKey
init|=
operator|new
name|WSSecEncryptedKey
argument_list|()
decl_stmt|;
name|encrKey
operator|.
name|setKeyIdentifierType
argument_list|(
name|encryptionProperties
operator|.
name|getKeyIdentifierType
argument_list|()
argument_list|)
expr_stmt|;
name|encrKey
operator|.
name|setEphemeralKey
argument_list|(
name|secret
argument_list|)
expr_stmt|;
name|encrKey
operator|.
name|setSymmetricEncAlgorithm
argument_list|(
name|encryptionProperties
operator|.
name|getEncryptionAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|encrKey
operator|.
name|setUseThisCert
argument_list|(
name|certificate
argument_list|)
expr_stmt|;
name|encrKey
operator|.
name|setKeyEncAlgo
argument_list|(
name|encryptionProperties
operator|.
name|getKeyWrapAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|encrKey
operator|.
name|prepare
argument_list|(
name|doc
argument_list|,
name|encryptionCrypto
argument_list|)
expr_stmt|;
name|Element
name|encryptedKeyElement
init|=
name|encrKey
operator|.
name|getEncryptedKeyElement
argument_list|()
decl_stmt|;
comment|// Append the EncryptedKey to a KeyInfo element
name|Element
name|keyInfoElement
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|WSConstants
operator|.
name|SIG_NS
argument_list|,
name|WSConstants
operator|.
name|SIG_PREFIX
operator|+
literal|":"
operator|+
name|WSConstants
operator|.
name|KEYINFO_LN
argument_list|)
decl_stmt|;
name|keyInfoElement
operator|.
name|setAttributeNS
argument_list|(
name|WSConstants
operator|.
name|XMLNS_NS
argument_list|,
literal|"xmlns:"
operator|+
name|WSConstants
operator|.
name|SIG_PREFIX
argument_list|,
name|WSConstants
operator|.
name|SIG_NS
argument_list|)
expr_stmt|;
name|keyInfoElement
operator|.
name|appendChild
argument_list|(
name|encryptedKeyElement
argument_list|)
expr_stmt|;
name|keyInfo
operator|.
name|setElement
argument_list|(
name|keyInfoElement
argument_list|)
expr_stmt|;
return|return
name|keyInfo
return|;
block|}
block|}
end_class

end_unit

