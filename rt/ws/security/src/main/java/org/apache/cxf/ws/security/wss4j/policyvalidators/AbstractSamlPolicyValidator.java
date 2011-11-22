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
operator|.
name|policyvalidators
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
name|Certificate
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
name|List
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
name|message
operator|.
name|Message
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
name|message
operator|.
name|MessageUtils
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
name|policy
operator|.
name|SPConstants
operator|.
name|IncludeTokenType
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
name|policy
operator|.
name|model
operator|.
name|Token
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
name|WSDerivedKeyTokenPrincipal
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
name|WSSecurityEngineResult
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
name|SAMLKeyInfo
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
name|OpenSAMLUtil
import|;
end_import

begin_comment
comment|/**  * Some abstract functionality for validating SAML Assertions  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractSamlPolicyValidator
extends|extends
name|AbstractTokenPolicyValidator
block|{
comment|/**      * Check to see if a token is required or not.      * @param token the token      * @param message The message      * @return true if the token is required      */
specifier|protected
name|boolean
name|isTokenRequired
parameter_list|(
name|Token
name|token
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|IncludeTokenType
name|inclusion
init|=
name|token
operator|.
name|getInclusion
argument_list|()
decl_stmt|;
if|if
condition|(
name|inclusion
operator|==
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_NEVER
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|inclusion
operator|==
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_ALWAYS
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
name|boolean
name|initiator
init|=
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|initiator
operator|&&
operator|(
name|inclusion
operator|==
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_ALWAYS_TO_INITIATOR
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|initiator
operator|&&
operator|(
name|inclusion
operator|==
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_ONCE
operator|||
name|inclusion
operator|==
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_ALWAYS_TO_RECIPIENT
operator|)
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
block|}
comment|/**      * Check the holder-of-key requirements against the received assertion. The subject      * credential of the SAML Assertion must have been used to sign some portion of      * the message, thus showing proof-of-possession of the private/secret key. Alternatively,      * the subject credential of the SAML Assertion must match a client certificate credential      * when 2-way TLS is used.      * @param assertionWrapper the SAML Assertion wrapper object      * @param signedResults a list of all of the signed results      */
specifier|public
name|boolean
name|checkHolderOfKey
parameter_list|(
name|AssertionWrapper
name|assertionWrapper
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
parameter_list|,
name|Certificate
index|[]
name|tlsCerts
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|confirmationMethods
init|=
name|assertionWrapper
operator|.
name|getConfirmationMethods
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|confirmationMethod
range|:
name|confirmationMethods
control|)
block|{
if|if
condition|(
name|OpenSAMLUtil
operator|.
name|isMethodHolderOfKey
argument_list|(
name|confirmationMethod
argument_list|)
condition|)
block|{
if|if
condition|(
name|tlsCerts
operator|==
literal|null
operator|&&
operator|(
name|signedResults
operator|==
literal|null
operator|||
name|signedResults
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|SAMLKeyInfo
name|subjectKeyInfo
init|=
name|assertionWrapper
operator|.
name|getSubjectKeyInfo
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|compareCredentials
argument_list|(
name|subjectKeyInfo
argument_list|,
name|signedResults
argument_list|,
name|tlsCerts
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Compare the credentials of the assertion to the credentials used in 2-way TLS or those      * used to verify signatures.      * Return true on a match      * @param subjectKeyInfo the SAMLKeyInfo object      * @param signedResults a list of all of the signed results      * @return true if the credentials of the assertion were used to verify a signature      */
specifier|private
name|boolean
name|compareCredentials
parameter_list|(
name|SAMLKeyInfo
name|subjectKeyInfo
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
parameter_list|,
name|Certificate
index|[]
name|tlsCerts
parameter_list|)
block|{
name|X509Certificate
index|[]
name|subjectCerts
init|=
name|subjectKeyInfo
operator|.
name|getCerts
argument_list|()
decl_stmt|;
name|PublicKey
name|subjectPublicKey
init|=
name|subjectKeyInfo
operator|.
name|getPublicKey
argument_list|()
decl_stmt|;
name|byte
index|[]
name|subjectSecretKey
init|=
name|subjectKeyInfo
operator|.
name|getSecret
argument_list|()
decl_stmt|;
comment|//
comment|// Try to match the TLS certs first
comment|//
if|if
condition|(
name|tlsCerts
operator|!=
literal|null
operator|&&
name|tlsCerts
operator|.
name|length
operator|>
literal|0
operator|&&
name|subjectCerts
operator|!=
literal|null
operator|&&
name|subjectCerts
operator|.
name|length
operator|>
literal|0
operator|&&
name|tlsCerts
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
name|subjectCerts
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|tlsCerts
operator|!=
literal|null
operator|&&
name|tlsCerts
operator|.
name|length
operator|>
literal|0
operator|&&
name|subjectPublicKey
operator|!=
literal|null
operator|&&
name|tlsCerts
index|[
literal|0
index|]
operator|.
name|getPublicKey
argument_list|()
operator|.
name|equals
argument_list|(
name|subjectPublicKey
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|//
comment|// Now try the message-level signatures
comment|//
for|for
control|(
name|WSSecurityEngineResult
name|signedResult
range|:
name|signedResults
control|)
block|{
name|X509Certificate
index|[]
name|certs
init|=
operator|(
name|X509Certificate
index|[]
operator|)
name|signedResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_X509_CERTIFICATES
argument_list|)
decl_stmt|;
name|PublicKey
name|publicKey
init|=
operator|(
name|PublicKey
operator|)
name|signedResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_PUBLIC_KEY
argument_list|)
decl_stmt|;
name|byte
index|[]
name|secretKey
init|=
operator|(
name|byte
index|[]
operator|)
name|signedResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_SECRET
argument_list|)
decl_stmt|;
if|if
condition|(
name|certs
operator|!=
literal|null
operator|&&
name|certs
operator|.
name|length
operator|>
literal|0
operator|&&
name|subjectCerts
operator|!=
literal|null
operator|&&
name|subjectCerts
operator|.
name|length
operator|>
literal|0
operator|&&
name|certs
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
name|subjectCerts
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|publicKey
operator|!=
literal|null
operator|&&
name|publicKey
operator|.
name|equals
argument_list|(
name|subjectPublicKey
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|checkSecretKey
argument_list|(
name|secretKey
argument_list|,
name|subjectSecretKey
argument_list|,
name|signedResult
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
specifier|private
name|boolean
name|checkSecretKey
parameter_list|(
name|byte
index|[]
name|secretKey
parameter_list|,
name|byte
index|[]
name|subjectSecretKey
parameter_list|,
name|WSSecurityEngineResult
name|signedResult
parameter_list|)
block|{
if|if
condition|(
name|secretKey
operator|!=
literal|null
operator|&&
name|subjectSecretKey
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|Arrays
operator|.
name|equals
argument_list|(
name|secretKey
argument_list|,
name|subjectSecretKey
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
name|Principal
name|principal
init|=
operator|(
name|Principal
operator|)
name|signedResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_PRINCIPAL
argument_list|)
decl_stmt|;
if|if
condition|(
name|principal
operator|instanceof
name|WSDerivedKeyTokenPrincipal
condition|)
block|{
name|secretKey
operator|=
operator|(
operator|(
name|WSDerivedKeyTokenPrincipal
operator|)
name|principal
operator|)
operator|.
name|getSecret
argument_list|()
expr_stmt|;
if|if
condition|(
name|Arrays
operator|.
name|equals
argument_list|(
name|secretKey
argument_list|,
name|subjectSecretKey
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

