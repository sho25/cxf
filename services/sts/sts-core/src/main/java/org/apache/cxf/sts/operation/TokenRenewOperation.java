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
name|renewer
operator|.
name|TokenRenewer
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
name|renewer
operator|.
name|TokenRenewerParameters
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
name|renewer
operator|.
name|TokenRenewerResponse
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
name|operation
operator|.
name|RenewOperation
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

begin_comment
comment|/**  * An implementation of the IssueOperation interface to renew tokens.  */
end_comment

begin_class
specifier|public
class|class
name|TokenRenewOperation
extends|extends
name|AbstractOperation
implements|implements
name|RenewOperation
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
name|TokenRenewOperation
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|List
argument_list|<
name|TokenRenewer
argument_list|>
name|tokenRenewers
init|=
operator|new
name|ArrayList
argument_list|<
name|TokenRenewer
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|setTokenRenewers
parameter_list|(
name|List
argument_list|<
name|TokenRenewer
argument_list|>
name|tokenRenewerList
parameter_list|)
block|{
name|this
operator|.
name|tokenRenewers
operator|=
name|tokenRenewerList
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|TokenRenewer
argument_list|>
name|getTokenRenewers
parameter_list|()
block|{
return|return
name|tokenRenewers
return|;
block|}
specifier|public
name|RequestSecurityTokenResponseType
name|renew
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
name|KeyRequirements
name|keyRequirements
init|=
name|requestParser
operator|.
name|getKeyRequirements
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
name|ReceivedToken
name|renewTarget
init|=
name|tokenRequirements
operator|.
name|getRenewTarget
argument_list|()
decl_stmt|;
if|if
condition|(
name|renewTarget
operator|==
literal|null
operator|||
name|renewTarget
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
literal|"No element presented for renewal"
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
name|LOG
operator|.
name|fine
argument_list|(
literal|"Received TokenType is null"
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
comment|// Validate the request
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
name|renewTarget
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
name|renewTarget
operator|.
name|setState
argument_list|(
name|STATE
operator|.
name|INVALID
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"No Token Validator has been found that can handle this token"
operator|+
name|tokenRequirements
operator|.
name|getTokenType
argument_list|()
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
comment|// Reject an invalid token
if|if
condition|(
name|tokenResponse
operator|.
name|getToken
argument_list|()
operator|.
name|getState
argument_list|()
operator|!=
name|STATE
operator|.
name|EXPIRED
operator|&&
name|tokenResponse
operator|.
name|getToken
argument_list|()
operator|.
name|getState
argument_list|()
operator|!=
name|STATE
operator|.
name|VALID
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"The token is not valid or expired, and so it cannot be renewed"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"No Token Validator has been found that can handle this token"
operator|+
name|tokenRequirements
operator|.
name|getTokenType
argument_list|()
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
comment|//
comment|// Renew the token
comment|//
name|TokenRenewerResponse
name|tokenRenewerResponse
init|=
literal|null
decl_stmt|;
name|TokenRenewerParameters
name|renewerParameters
init|=
name|createTokenRenewerParameters
argument_list|(
name|requestParser
argument_list|,
name|context
argument_list|)
decl_stmt|;
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
name|renewerParameters
operator|.
name|setAdditionalProperties
argument_list|(
name|additionalProperties
argument_list|)
expr_stmt|;
block|}
name|renewerParameters
operator|.
name|setRealm
argument_list|(
name|tokenResponse
operator|.
name|getTokenRealm
argument_list|()
argument_list|)
expr_stmt|;
name|renewerParameters
operator|.
name|setToken
argument_list|(
name|tokenResponse
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
name|realm
operator|=
name|tokenResponse
operator|.
name|getTokenRealm
argument_list|()
expr_stmt|;
for|for
control|(
name|TokenRenewer
name|tokenRenewer
range|:
name|tokenRenewers
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
name|tokenRenewer
operator|.
name|canHandleToken
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
name|canHandle
operator|=
name|tokenRenewer
operator|.
name|canHandleToken
argument_list|(
name|tokenResponse
operator|.
name|getToken
argument_list|()
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
name|tokenRenewerResponse
operator|=
name|tokenRenewer
operator|.
name|renewToken
argument_list|(
name|renewerParameters
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
name|tokenRenewerResponse
operator|==
literal|null
operator|||
name|tokenRenewerResponse
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
literal|"No Token Renewer has been found that can handle this token"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"No token renewer found for requested token type"
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
name|EncryptionProperties
name|encryptionProperties
init|=
name|renewerParameters
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
name|tokenRenewerResponse
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
name|TokenRenewerResponse
name|tokenRenewerResponse
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
name|tokenRenewerResponse
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
name|tokenRenewerResponse
operator|.
name|getToken
argument_list|()
argument_list|,
name|tokenRenewerResponse
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
name|tokenRenewerResponse
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
name|tokenRenewerResponse
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
name|tokenRenewerResponse
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
name|tokenRenewerResponse
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
comment|// Lifetime
name|LifetimeType
name|lifetime
init|=
name|createLifetime
argument_list|(
name|tokenRenewerResponse
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
return|return
name|response
return|;
block|}
specifier|private
name|TokenRenewerParameters
name|createTokenRenewerParameters
parameter_list|(
name|RequestParser
name|requestParser
parameter_list|,
name|WebServiceContext
name|context
parameter_list|)
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
name|TokenRenewerParameters
name|renewerParameters
init|=
operator|new
name|TokenRenewerParameters
argument_list|()
decl_stmt|;
name|renewerParameters
operator|.
name|setAppliesToAddress
argument_list|(
name|providerParameters
operator|.
name|getAppliesToAddress
argument_list|()
argument_list|)
expr_stmt|;
name|renewerParameters
operator|.
name|setEncryptionProperties
argument_list|(
name|providerParameters
operator|.
name|getEncryptionProperties
argument_list|()
argument_list|)
expr_stmt|;
name|renewerParameters
operator|.
name|setKeyRequirements
argument_list|(
name|providerParameters
operator|.
name|getKeyRequirements
argument_list|()
argument_list|)
expr_stmt|;
name|renewerParameters
operator|.
name|setPrincipal
argument_list|(
name|providerParameters
operator|.
name|getPrincipal
argument_list|()
argument_list|)
expr_stmt|;
name|renewerParameters
operator|.
name|setStsProperties
argument_list|(
name|providerParameters
operator|.
name|getStsProperties
argument_list|()
argument_list|)
expr_stmt|;
name|renewerParameters
operator|.
name|setTokenRequirements
argument_list|(
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
argument_list|)
expr_stmt|;
name|renewerParameters
operator|.
name|setTokenStore
argument_list|(
name|providerParameters
operator|.
name|getTokenStore
argument_list|()
argument_list|)
expr_stmt|;
name|renewerParameters
operator|.
name|setWebServiceContext
argument_list|(
name|providerParameters
operator|.
name|getWebServiceContext
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|renewerParameters
return|;
block|}
block|}
end_class

end_unit

