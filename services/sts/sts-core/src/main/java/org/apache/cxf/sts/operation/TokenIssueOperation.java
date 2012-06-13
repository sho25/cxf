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
name|operation
package|;
end_package

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
name|ws
operator|.
name|WebServiceContext
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
name|claims
operator|.
name|RequestClaimCollection
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
name|RequestParser
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
name|provider
operator|.
name|TokenReference
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
name|TokenValidatorResponse
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|BinarySecretType
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
name|EntropyType
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
name|LifetimeType
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
name|RequestSecurityTokenCollectionType
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
name|RequestSecurityTokenResponseCollectionType
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
name|RequestSecurityTokenResponseType
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
name|RequestSecurityTokenType
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
name|RequestedProofTokenType
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
name|RequestedReferenceType
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
name|RequestedSecurityTokenType
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
name|operation
operator|.
name|IssueOperation
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
name|operation
operator|.
name|IssueSingleOperation
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

begin_comment
comment|/**  * An implementation of the IssueOperation interface.  */
end_comment

begin_class
specifier|public
class|class
name|TokenIssueOperation
extends|extends
name|AbstractOperation
implements|implements
name|IssueOperation
implements|,
name|IssueSingleOperation
block|{
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|TokenIssueOperation
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|RequestSecurityTokenResponseCollectionType
name|issue
parameter_list|(
name|RequestSecurityTokenType
name|request
parameter_list|,
name|WebServiceContext
name|context
parameter_list|)
block|{
name|RequestSecurityTokenResponseType
name|response
init|=
name|issueSingle
argument_list|(
name|request
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|RequestSecurityTokenResponseCollectionType
name|responseCollection
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createRequestSecurityTokenResponseCollectionType
argument_list|()
decl_stmt|;
name|responseCollection
operator|.
name|getRequestSecurityTokenResponse
argument_list|()
operator|.
name|add
argument_list|(
name|response
argument_list|)
expr_stmt|;
return|return
name|responseCollection
return|;
block|}
specifier|public
name|RequestSecurityTokenResponseCollectionType
name|issue
parameter_list|(
name|RequestSecurityTokenCollectionType
name|requestCollection
parameter_list|,
name|WebServiceContext
name|context
parameter_list|)
block|{
name|RequestSecurityTokenResponseCollectionType
name|responseCollection
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createRequestSecurityTokenResponseCollectionType
argument_list|()
decl_stmt|;
for|for
control|(
name|RequestSecurityTokenType
name|request
range|:
name|requestCollection
operator|.
name|getRequestSecurityToken
argument_list|()
control|)
block|{
name|RequestSecurityTokenResponseType
name|response
init|=
name|issueSingle
argument_list|(
name|request
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|responseCollection
operator|.
name|getRequestSecurityTokenResponse
argument_list|()
operator|.
name|add
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
return|return
name|responseCollection
return|;
block|}
specifier|public
name|RequestSecurityTokenResponseType
name|issueSingle
parameter_list|(
name|RequestSecurityTokenType
name|request
parameter_list|,
name|WebServiceContext
name|context
parameter_list|)
block|{
name|RequestParser
name|requestParser
init|=
name|parseRequest
argument_list|(
name|request
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createTokenProviderParameters
argument_list|(
name|requestParser
argument_list|,
name|context
argument_list|)
decl_stmt|;
comment|// Check if the requested claims can be handled by the configured claim handlers
name|RequestClaimCollection
name|requestedClaims
init|=
name|providerParameters
operator|.
name|getRequestedClaims
argument_list|()
decl_stmt|;
name|checkClaimsSupport
argument_list|(
name|requestedClaims
argument_list|)
expr_stmt|;
name|providerParameters
operator|.
name|setClaimsManager
argument_list|(
name|claimsManager
argument_list|)
expr_stmt|;
name|String
name|realm
init|=
name|providerParameters
operator|.
name|getRealm
argument_list|()
decl_stmt|;
name|TokenRequirements
name|tokenRequirements
init|=
name|requestParser
operator|.
name|getTokenRequirements
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
comment|// Validate OnBehalfOf token if present
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
name|validateTarget
init|=
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getOnBehalfOf
argument_list|()
decl_stmt|;
name|TokenValidatorResponse
name|tokenResponse
init|=
name|validateReceivedToken
argument_list|(
name|context
argument_list|,
name|realm
argument_list|,
name|tokenRequirements
argument_list|,
name|validateTarget
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokenResponse
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"No Token Validator has been found that can handle this token"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|validateTarget
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
name|processValidToken
argument_list|(
name|providerParameters
argument_list|,
name|validateTarget
argument_list|,
name|tokenResponse
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//[TODO] Add plugin for validation out-of-band
comment|// Example:
comment|// If the requestor is in the possession of a certificate (mutual ssl handshake)
comment|// the STS trusts the token sent in OnBehalfOf element
block|}
if|if
condition|(
name|tokenResponse
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
name|additionalProperties
init|=
name|tokenResponse
operator|.
name|getAdditionalProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|additionalProperties
operator|!=
literal|null
condition|)
block|{
name|providerParameters
operator|.
name|setAdditionalProperties
argument_list|(
name|additionalProperties
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// create token
name|TokenProviderResponse
name|tokenResponse
init|=
literal|null
decl_stmt|;
for|for
control|(
name|TokenProvider
name|tokenProvider
range|:
name|tokenProviders
control|)
block|{
name|boolean
name|canHandle
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|realm
operator|==
literal|null
condition|)
block|{
name|canHandle
operator|=
name|tokenProvider
operator|.
name|canHandleToken
argument_list|(
name|tokenType
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|canHandle
operator|=
name|tokenProvider
operator|.
name|canHandleToken
argument_list|(
name|tokenType
argument_list|,
name|realm
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|canHandle
condition|)
block|{
try|try
block|{
name|tokenResponse
operator|=
name|tokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|STSException
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
name|ex
throw|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
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
literal|"Error in providing a token"
argument_list|,
name|ex
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
break|break;
block|}
block|}
if|if
condition|(
name|tokenResponse
operator|==
literal|null
operator|||
name|tokenResponse
operator|.
name|getToken
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
literal|"No token provider found for requested token type: "
operator|+
name|tokenType
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"No token provider found for requested token type: "
operator|+
name|tokenType
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
comment|// prepare response
try|try
block|{
name|KeyRequirements
name|keyRequirements
init|=
name|requestParser
operator|.
name|getKeyRequirements
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
name|RequestSecurityTokenResponseType
name|response
init|=
name|createResponse
argument_list|(
name|encryptionProperties
argument_list|,
name|tokenResponse
argument_list|,
name|tokenRequirements
argument_list|,
name|keyRequirements
argument_list|,
name|context
argument_list|)
decl_stmt|;
return|return
name|response
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
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
literal|"Error in creating the response"
argument_list|,
name|ex
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
block|}
specifier|private
name|RequestSecurityTokenResponseType
name|createResponse
parameter_list|(
name|EncryptionProperties
name|encryptionProperties
parameter_list|,
name|TokenProviderResponse
name|tokenResponse
parameter_list|,
name|TokenRequirements
name|tokenRequirements
parameter_list|,
name|KeyRequirements
name|keyRequirements
parameter_list|,
name|WebServiceContext
name|webServiceContext
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|RequestSecurityTokenResponseType
name|response
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createRequestSecurityTokenResponseType
argument_list|()
decl_stmt|;
name|String
name|context
init|=
name|tokenRequirements
operator|.
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|response
operator|.
name|setContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
comment|// TokenType
name|JAXBElement
argument_list|<
name|String
argument_list|>
name|jaxbTokenType
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createTokenType
argument_list|(
name|tokenRequirements
operator|.
name|getTokenType
argument_list|()
argument_list|)
decl_stmt|;
name|response
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|jaxbTokenType
argument_list|)
expr_stmt|;
comment|// RequestedSecurityToken
name|RequestedSecurityTokenType
name|requestedTokenType
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createRequestedSecurityTokenType
argument_list|()
decl_stmt|;
name|JAXBElement
argument_list|<
name|RequestedSecurityTokenType
argument_list|>
name|requestedToken
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createRequestedSecurityToken
argument_list|(
name|requestedTokenType
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Encrypting Issued Token: "
operator|+
name|encryptIssuedToken
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|encryptIssuedToken
condition|)
block|{
name|requestedTokenType
operator|.
name|setAny
argument_list|(
name|tokenResponse
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|requestedTokenType
operator|.
name|setAny
argument_list|(
name|encryptToken
argument_list|(
name|tokenResponse
operator|.
name|getToken
argument_list|()
argument_list|,
name|tokenResponse
operator|.
name|getTokenId
argument_list|()
argument_list|,
name|encryptionProperties
argument_list|,
name|keyRequirements
argument_list|,
name|webServiceContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|response
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|requestedToken
argument_list|)
expr_stmt|;
if|if
condition|(
name|returnReferences
condition|)
block|{
comment|// RequestedAttachedReference
name|TokenReference
name|attachedReference
init|=
name|tokenResponse
operator|.
name|getAttachedReference
argument_list|()
decl_stmt|;
name|RequestedReferenceType
name|requestedAttachedReferenceType
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|attachedReference
operator|!=
literal|null
condition|)
block|{
name|requestedAttachedReferenceType
operator|=
name|createRequestedReference
argument_list|(
name|attachedReference
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|requestedAttachedReferenceType
operator|=
name|createRequestedReference
argument_list|(
name|tokenResponse
operator|.
name|getTokenId
argument_list|()
argument_list|,
name|tokenRequirements
operator|.
name|getTokenType
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|JAXBElement
argument_list|<
name|RequestedReferenceType
argument_list|>
name|requestedAttachedReference
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createRequestedAttachedReference
argument_list|(
name|requestedAttachedReferenceType
argument_list|)
decl_stmt|;
name|response
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|requestedAttachedReference
argument_list|)
expr_stmt|;
comment|// RequestedUnattachedReference
name|TokenReference
name|unAttachedReference
init|=
name|tokenResponse
operator|.
name|getUnAttachedReference
argument_list|()
decl_stmt|;
name|RequestedReferenceType
name|requestedUnattachedReferenceType
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|unAttachedReference
operator|!=
literal|null
condition|)
block|{
name|requestedUnattachedReferenceType
operator|=
name|createRequestedReference
argument_list|(
name|unAttachedReference
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|requestedUnattachedReferenceType
operator|=
name|createRequestedReference
argument_list|(
name|tokenResponse
operator|.
name|getTokenId
argument_list|()
argument_list|,
name|tokenRequirements
operator|.
name|getTokenType
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|JAXBElement
argument_list|<
name|RequestedReferenceType
argument_list|>
name|requestedUnattachedReference
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createRequestedUnattachedReference
argument_list|(
name|requestedUnattachedReferenceType
argument_list|)
decl_stmt|;
name|response
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|requestedUnattachedReference
argument_list|)
expr_stmt|;
block|}
comment|// AppliesTo
name|response
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|tokenRequirements
operator|.
name|getAppliesTo
argument_list|()
argument_list|)
expr_stmt|;
comment|// RequestedProofToken
if|if
condition|(
name|tokenResponse
operator|.
name|isComputedKey
argument_list|()
operator|&&
name|keyRequirements
operator|.
name|getComputedKeyAlgorithm
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|JAXBElement
argument_list|<
name|String
argument_list|>
name|computedKey
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createComputedKey
argument_list|(
name|keyRequirements
operator|.
name|getComputedKeyAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
name|RequestedProofTokenType
name|requestedProofTokenType
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createRequestedProofTokenType
argument_list|()
decl_stmt|;
name|requestedProofTokenType
operator|.
name|setAny
argument_list|(
name|computedKey
argument_list|)
expr_stmt|;
name|JAXBElement
argument_list|<
name|RequestedProofTokenType
argument_list|>
name|requestedProofToken
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createRequestedProofToken
argument_list|(
name|requestedProofTokenType
argument_list|)
decl_stmt|;
name|response
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|requestedProofToken
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tokenResponse
operator|.
name|getEntropy
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Object
name|token
init|=
name|constructSecretToken
argument_list|(
name|tokenResponse
operator|.
name|getEntropy
argument_list|()
argument_list|,
name|encryptionProperties
argument_list|,
name|keyRequirements
argument_list|)
decl_stmt|;
name|RequestedProofTokenType
name|requestedProofTokenType
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createRequestedProofTokenType
argument_list|()
decl_stmt|;
name|requestedProofTokenType
operator|.
name|setAny
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|JAXBElement
argument_list|<
name|RequestedProofTokenType
argument_list|>
name|requestedProofToken
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createRequestedProofToken
argument_list|(
name|requestedProofTokenType
argument_list|)
decl_stmt|;
name|response
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|requestedProofToken
argument_list|)
expr_stmt|;
block|}
comment|// Entropy
if|if
condition|(
name|tokenResponse
operator|.
name|isComputedKey
argument_list|()
operator|&&
name|tokenResponse
operator|.
name|getEntropy
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Object
name|token
init|=
name|constructSecretToken
argument_list|(
name|tokenResponse
operator|.
name|getEntropy
argument_list|()
argument_list|,
name|encryptionProperties
argument_list|,
name|keyRequirements
argument_list|)
decl_stmt|;
name|EntropyType
name|entropyType
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createEntropyType
argument_list|()
decl_stmt|;
name|entropyType
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|JAXBElement
argument_list|<
name|EntropyType
argument_list|>
name|entropyElement
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createEntropy
argument_list|(
name|entropyType
argument_list|)
decl_stmt|;
name|response
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|entropyElement
argument_list|)
expr_stmt|;
block|}
comment|// Lifetime
name|LifetimeType
name|lifetime
init|=
name|createLifetime
argument_list|(
name|tokenResponse
operator|.
name|getLifetime
argument_list|()
argument_list|)
decl_stmt|;
name|JAXBElement
argument_list|<
name|LifetimeType
argument_list|>
name|lifetimeType
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createLifetime
argument_list|(
name|lifetime
argument_list|)
decl_stmt|;
name|response
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|lifetimeType
argument_list|)
expr_stmt|;
comment|// KeySize
name|long
name|keySize
init|=
name|tokenResponse
operator|.
name|getKeySize
argument_list|()
decl_stmt|;
if|if
condition|(
name|keySize
operator|<=
literal|0
condition|)
block|{
name|keySize
operator|=
name|keyRequirements
operator|.
name|getKeySize
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|keyRequirements
operator|.
name|getKeySize
argument_list|()
operator|>
literal|0
condition|)
block|{
name|JAXBElement
argument_list|<
name|Long
argument_list|>
name|keySizeType
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createKeySize
argument_list|(
name|keySize
argument_list|)
decl_stmt|;
name|response
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|keySizeType
argument_list|)
expr_stmt|;
block|}
return|return
name|response
return|;
block|}
comment|/**      * Construct a token containing the secret to return to the client. The secret is returned in a       * BinarySecretType JAXBElement.      */
specifier|private
name|Object
name|constructSecretToken
parameter_list|(
name|byte
index|[]
name|secret
parameter_list|,
name|EncryptionProperties
name|encryptionProperties
parameter_list|,
name|KeyRequirements
name|keyRequirements
parameter_list|)
throws|throws
name|WSSecurityException
block|{
comment|/*if (encryptIssuedToken) {             return encryptSecret(secret, encryptionProperties, keyRequirements);         } else {         */
name|BinarySecretType
name|binarySecretType
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createBinarySecretType
argument_list|()
decl_stmt|;
name|String
name|nonce
init|=
literal|"http://docs.oasis-open.org/ws-sx/ws-trust/200512/Nonce"
decl_stmt|;
name|binarySecretType
operator|.
name|setType
argument_list|(
name|nonce
argument_list|)
expr_stmt|;
name|binarySecretType
operator|.
name|setValue
argument_list|(
name|secret
argument_list|)
expr_stmt|;
name|JAXBElement
argument_list|<
name|BinarySecretType
argument_list|>
name|binarySecret
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createBinarySecret
argument_list|(
name|binarySecretType
argument_list|)
decl_stmt|;
return|return
name|binarySecret
return|;
block|}
block|}
end_class

end_unit

