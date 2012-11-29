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
name|RealmParser
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
name|model
operator|.
name|StatusType
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
name|ValidateOperation
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
comment|/**  * An implementation of the ValidateOperation interface.  */
end_comment

begin_class
specifier|public
class|class
name|TokenValidateOperation
extends|extends
name|AbstractOperation
implements|implements
name|ValidateOperation
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
name|TokenValidateOperation
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|RequestSecurityTokenResponseType
name|validate
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
name|TokenRequirements
name|tokenRequirements
init|=
name|requestParser
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
if|if
condition|(
name|validateTarget
operator|==
literal|null
operator|||
name|validateTarget
operator|.
name|getToken
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|STSException
argument_list|(
literal|"No element presented for validation"
argument_list|,
name|STSException
operator|.
name|INVALID_REQUEST
argument_list|)
throw|;
block|}
if|if
condition|(
name|tokenRequirements
operator|.
name|getTokenType
argument_list|()
operator|==
literal|null
condition|)
block|{
name|tokenRequirements
operator|.
name|setTokenType
argument_list|(
name|STSConstants
operator|.
name|STATUS
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Received TokenType is null, falling back to default token type: "
operator|+
name|STSConstants
operator|.
name|STATUS
argument_list|)
expr_stmt|;
block|}
comment|// Get the realm of the request
name|String
name|realm
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|stsProperties
operator|.
name|getRealmParser
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|RealmParser
name|realmParser
init|=
name|stsProperties
operator|.
name|getRealmParser
argument_list|()
decl_stmt|;
name|realm
operator|=
name|realmParser
operator|.
name|parseRealm
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
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
name|tokenResponse
operator|=
operator|new
name|TokenValidatorResponse
argument_list|()
expr_stmt|;
name|validateTarget
operator|.
name|setState
argument_list|(
name|STATE
operator|.
name|INVALID
argument_list|)
expr_stmt|;
name|tokenResponse
operator|.
name|setToken
argument_list|(
name|validateTarget
argument_list|)
expr_stmt|;
block|}
comment|//
comment|// Create a new token (if requested)
comment|//
name|TokenProviderResponse
name|tokenProviderResponse
init|=
literal|null
decl_stmt|;
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
name|tokenResponse
operator|.
name|getToken
argument_list|()
operator|.
name|getState
argument_list|()
operator|==
name|STATE
operator|.
name|VALID
operator|&&
operator|!
name|STSConstants
operator|.
name|STATUS
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
condition|)
block|{
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
name|processValidToken
argument_list|(
name|providerParameters
argument_list|,
name|validateTarget
argument_list|,
name|tokenResponse
argument_list|)
expr_stmt|;
comment|// Check if the requested claims can be handled by the configured claim handlers
name|RequestClaimCollection
name|requestedClaims
init|=
name|providerParameters
operator|.
name|getRequestedPrimaryClaims
argument_list|()
decl_stmt|;
name|checkClaimsSupport
argument_list|(
name|requestedClaims
argument_list|)
expr_stmt|;
name|requestedClaims
operator|=
name|providerParameters
operator|.
name|getRequestedSecondaryClaims
argument_list|()
expr_stmt|;
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
name|realm
operator|=
name|providerParameters
operator|.
name|getRealm
argument_list|()
expr_stmt|;
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
name|tokenProviderResponse
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
name|tokenProviderResponse
operator|==
literal|null
operator|||
name|tokenProviderResponse
operator|.
name|getToken
argument_list|()
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"No Token Provider has been found that can handle this token"
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
block|}
comment|// prepare response
try|try
block|{
return|return
name|createResponse
argument_list|(
name|tokenResponse
argument_list|,
name|tokenProviderResponse
argument_list|,
name|tokenRequirements
argument_list|)
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
name|TokenValidatorResponse
name|tokenResponse
parameter_list|,
name|TokenProviderResponse
name|tokenProviderResponse
parameter_list|,
name|TokenRequirements
name|tokenRequirements
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
name|boolean
name|valid
init|=
name|tokenResponse
operator|.
name|getToken
argument_list|()
operator|.
name|getState
argument_list|()
operator|==
name|STATE
operator|.
name|VALID
decl_stmt|;
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
name|valid
operator|||
name|STSConstants
operator|.
name|STATUS
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
condition|)
block|{
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
name|tokenType
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
block|}
comment|// Status
name|StatusType
name|statusType
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createStatusType
argument_list|()
decl_stmt|;
if|if
condition|(
name|valid
condition|)
block|{
name|statusType
operator|.
name|setCode
argument_list|(
name|STSConstants
operator|.
name|VALID_CODE
argument_list|)
expr_stmt|;
name|statusType
operator|.
name|setReason
argument_list|(
name|STSConstants
operator|.
name|VALID_REASON
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|statusType
operator|.
name|setCode
argument_list|(
name|STSConstants
operator|.
name|INVALID_CODE
argument_list|)
expr_stmt|;
name|statusType
operator|.
name|setReason
argument_list|(
name|STSConstants
operator|.
name|INVALID_REASON
argument_list|)
expr_stmt|;
block|}
name|JAXBElement
argument_list|<
name|StatusType
argument_list|>
name|status
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createStatus
argument_list|(
name|statusType
argument_list|)
decl_stmt|;
name|response
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|status
argument_list|)
expr_stmt|;
comment|// RequestedSecurityToken
if|if
condition|(
name|valid
operator|&&
operator|!
name|STSConstants
operator|.
name|STATUS
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
operator|&&
name|tokenProviderResponse
operator|!=
literal|null
operator|&&
name|tokenProviderResponse
operator|.
name|getToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
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
name|requestedTokenType
operator|.
name|setAny
argument_list|(
name|tokenProviderResponse
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
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
comment|// Lifetime
name|LifetimeType
name|lifetime
init|=
name|createLifetime
argument_list|(
name|tokenProviderResponse
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
if|if
condition|(
name|returnReferences
condition|)
block|{
comment|// RequestedAttachedReference
name|TokenReference
name|attachedReference
init|=
name|tokenProviderResponse
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
name|tokenProviderResponse
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
name|tokenProviderResponse
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
name|tokenProviderResponse
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
block|}
return|return
name|response
return|;
block|}
block|}
end_class

end_unit

