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
name|claims
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

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
name|ArrayList
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
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|Claim
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
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|ClaimCollection
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
name|IdentityMapper
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
name|RealmSupport
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
name|Relationship
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
name|saml
operator|.
name|SamlAssertionWrapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|core
operator|.
name|xml
operator|.
name|XMLObject
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
comment|/**  * This class holds various ClaimsHandler implementations.  */
end_comment

begin_class
specifier|public
class|class
name|ClaimsManager
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
name|ClaimsManager
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ClaimsParser
argument_list|>
name|claimParsers
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ClaimsHandler
argument_list|>
name|claimHandlers
decl_stmt|;
specifier|private
name|List
argument_list|<
name|URI
argument_list|>
name|supportedClaimTypes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|stopProcessingOnException
init|=
literal|true
decl_stmt|;
specifier|private
name|IdentityMapper
name|identityMapper
decl_stmt|;
specifier|public
name|IdentityMapper
name|getIdentityMapper
parameter_list|()
block|{
return|return
name|identityMapper
return|;
block|}
specifier|public
name|void
name|setIdentityMapper
parameter_list|(
name|IdentityMapper
name|identityMapper
parameter_list|)
block|{
name|this
operator|.
name|identityMapper
operator|=
name|identityMapper
expr_stmt|;
block|}
specifier|public
name|boolean
name|isStopProcessingOnException
parameter_list|()
block|{
return|return
name|stopProcessingOnException
return|;
block|}
specifier|public
name|void
name|setStopProcessingOnException
parameter_list|(
name|boolean
name|stopProcessingOnException
parameter_list|)
block|{
name|this
operator|.
name|stopProcessingOnException
operator|=
name|stopProcessingOnException
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|URI
argument_list|>
name|getSupportedClaimTypes
parameter_list|()
block|{
return|return
name|supportedClaimTypes
return|;
block|}
specifier|public
name|List
argument_list|<
name|ClaimsParser
argument_list|>
name|getClaimParsers
parameter_list|()
block|{
return|return
name|claimParsers
return|;
block|}
specifier|public
name|List
argument_list|<
name|ClaimsHandler
argument_list|>
name|getClaimHandlers
parameter_list|()
block|{
return|return
name|claimHandlers
return|;
block|}
specifier|public
name|void
name|setClaimParsers
parameter_list|(
name|List
argument_list|<
name|ClaimsParser
argument_list|>
name|claimParsers
parameter_list|)
block|{
name|this
operator|.
name|claimParsers
operator|=
name|claimParsers
expr_stmt|;
block|}
specifier|public
name|void
name|setClaimHandlers
parameter_list|(
name|List
argument_list|<
name|ClaimsHandler
argument_list|>
name|claimHandlers
parameter_list|)
block|{
name|this
operator|.
name|claimHandlers
operator|=
name|claimHandlers
expr_stmt|;
if|if
condition|(
name|claimHandlers
operator|==
literal|null
condition|)
block|{
name|supportedClaimTypes
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|ClaimsHandler
name|handler
range|:
name|claimHandlers
control|)
block|{
name|supportedClaimTypes
operator|.
name|addAll
argument_list|(
name|handler
operator|.
name|getSupportedClaimTypes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|ProcessedClaimCollection
name|retrieveClaimValues
parameter_list|(
name|ClaimCollection
name|primaryClaims
parameter_list|,
name|ClaimCollection
name|secondaryClaims
parameter_list|,
name|ClaimsParameters
name|parameters
parameter_list|)
block|{
if|if
condition|(
name|primaryClaims
operator|==
literal|null
operator|&&
name|secondaryClaims
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|primaryClaims
operator|!=
literal|null
operator|&&
name|secondaryClaims
operator|==
literal|null
condition|)
block|{
return|return
name|retrieveClaimValues
argument_list|(
name|primaryClaims
argument_list|,
name|parameters
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|secondaryClaims
operator|!=
literal|null
operator|&&
name|primaryClaims
operator|==
literal|null
condition|)
block|{
return|return
name|retrieveClaimValues
argument_list|(
name|secondaryClaims
argument_list|,
name|parameters
argument_list|)
return|;
block|}
comment|// Here we have two sets of claims
if|if
condition|(
name|primaryClaims
operator|.
name|getDialect
argument_list|()
operator|!=
literal|null
operator|&&
name|primaryClaims
operator|.
name|getDialect
argument_list|()
operator|.
name|equals
argument_list|(
name|secondaryClaims
operator|.
name|getDialect
argument_list|()
argument_list|)
condition|)
block|{
comment|// Matching dialects - so we must merge them
name|ClaimCollection
name|mergedClaims
init|=
name|mergeClaims
argument_list|(
name|primaryClaims
argument_list|,
name|secondaryClaims
argument_list|)
decl_stmt|;
return|return
name|retrieveClaimValues
argument_list|(
name|mergedClaims
argument_list|,
name|parameters
argument_list|)
return|;
block|}
comment|// If the dialects don't match then just return all Claims
name|ProcessedClaimCollection
name|claims
init|=
name|retrieveClaimValues
argument_list|(
name|primaryClaims
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|ProcessedClaimCollection
name|claims2
init|=
name|retrieveClaimValues
argument_list|(
name|secondaryClaims
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|ProcessedClaimCollection
name|returnedClaims
init|=
operator|new
name|ProcessedClaimCollection
argument_list|()
decl_stmt|;
if|if
condition|(
name|claims
operator|!=
literal|null
condition|)
block|{
name|returnedClaims
operator|.
name|addAll
argument_list|(
name|claims
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|claims2
operator|!=
literal|null
condition|)
block|{
name|returnedClaims
operator|.
name|addAll
argument_list|(
name|claims2
argument_list|)
expr_stmt|;
block|}
return|return
name|returnedClaims
return|;
block|}
specifier|public
name|ProcessedClaimCollection
name|retrieveClaimValues
parameter_list|(
name|ClaimCollection
name|claims
parameter_list|,
name|ClaimsParameters
name|parameters
parameter_list|)
block|{
if|if
condition|(
name|claims
operator|==
literal|null
operator|||
name|claims
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Relationship
name|relationship
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|parameters
operator|.
name|getAdditionalProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|relationship
operator|=
operator|(
name|Relationship
operator|)
name|parameters
operator|.
name|getAdditionalProperties
argument_list|()
operator|.
name|get
argument_list|(
name|Relationship
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|relationship
operator|==
literal|null
operator|||
name|relationship
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|Relationship
operator|.
name|FED_TYPE_IDENTITY
argument_list|)
condition|)
block|{
comment|// Federate identity. Identity already mapped.
comment|// Call all configured claims handlers to retrieve the required claims
name|ProcessedClaimCollection
name|returnCollection
init|=
name|handleClaims
argument_list|(
name|claims
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|validateClaimValues
argument_list|(
name|claims
argument_list|,
name|returnCollection
argument_list|)
expr_stmt|;
return|return
name|returnCollection
return|;
block|}
comment|// Federate claims
name|ClaimsMapper
name|claimsMapper
init|=
name|relationship
operator|.
name|getClaimsMapper
argument_list|()
decl_stmt|;
if|if
condition|(
name|claimsMapper
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
name|SEVERE
argument_list|,
literal|"ClaimsMapper required to federate claims but not configured."
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"ClaimsMapper required to federate claims but not configured"
argument_list|,
name|STSException
operator|.
name|BAD_REQUEST
argument_list|)
throw|;
block|}
comment|// Get the claims of the received token (only SAML supported)
comment|// Consider refactoring to use a CallbackHandler and keep ClaimsManager token independent
name|SamlAssertionWrapper
name|assertion
init|=
operator|(
name|SamlAssertionWrapper
operator|)
name|parameters
operator|.
name|getAdditionalProperties
argument_list|()
operator|.
name|get
argument_list|(
name|SamlAssertionWrapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ProcessedClaim
argument_list|>
name|claimList
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
name|claimList
operator|=
name|this
operator|.
name|parseClaimsInAssertion
argument_list|(
name|assertion
operator|.
name|getSaml2
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|claimList
operator|=
name|this
operator|.
name|parseClaimsInAssertion
argument_list|(
name|assertion
operator|.
name|getSaml1
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ProcessedClaimCollection
name|sourceClaims
init|=
operator|new
name|ProcessedClaimCollection
argument_list|()
decl_stmt|;
name|sourceClaims
operator|.
name|addAll
argument_list|(
name|claimList
argument_list|)
expr_stmt|;
name|ProcessedClaimCollection
name|targetClaims
init|=
name|claimsMapper
operator|.
name|mapClaims
argument_list|(
name|relationship
operator|.
name|getSourceRealm
argument_list|()
argument_list|,
name|sourceClaims
argument_list|,
name|relationship
operator|.
name|getTargetRealm
argument_list|()
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|validateClaimValues
argument_list|(
name|claims
argument_list|,
name|targetClaims
argument_list|)
expr_stmt|;
return|return
name|targetClaims
return|;
block|}
specifier|private
name|ProcessedClaimCollection
name|handleClaims
parameter_list|(
name|ClaimCollection
name|claims
parameter_list|,
name|ClaimsParameters
name|parameters
parameter_list|)
block|{
name|ProcessedClaimCollection
name|returnCollection
init|=
operator|new
name|ProcessedClaimCollection
argument_list|()
decl_stmt|;
if|if
condition|(
name|claimHandlers
operator|==
literal|null
condition|)
block|{
return|return
name|returnCollection
return|;
block|}
name|Principal
name|originalPrincipal
init|=
name|parameters
operator|.
name|getPrincipal
argument_list|()
decl_stmt|;
for|for
control|(
name|ClaimsHandler
name|handler
range|:
name|claimHandlers
control|)
block|{
name|ClaimCollection
name|supportedClaims
init|=
name|filterHandlerClaims
argument_list|(
name|claims
argument_list|,
name|handler
operator|.
name|getSupportedClaimTypes
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|supportedClaims
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|isCurrentRealmSupported
argument_list|(
name|handler
argument_list|,
name|parameters
argument_list|)
condition|)
block|{
name|ProcessedClaimCollection
name|claimCollection
init|=
literal|null
decl_stmt|;
try|try
block|{
name|claimCollection
operator|=
name|handler
operator|.
name|retrieveClaimValues
argument_list|(
name|supportedClaims
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
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
name|INFO
argument_list|,
literal|"Failed retrieving claims from ClaimsHandler "
operator|+
name|handler
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|ex
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|isStopProcessingOnException
argument_list|()
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
block|}
finally|finally
block|{
comment|// set original principal again, otherwise wrong principal passed to next claim handler in the list
comment|// if no mapping required or wrong source principal used for next identity mapping
name|parameters
operator|.
name|setPrincipal
argument_list|(
name|originalPrincipal
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|claimCollection
operator|!=
literal|null
operator|&&
operator|!
name|claimCollection
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|returnCollection
operator|.
name|addAll
argument_list|(
name|claimCollection
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|returnCollection
return|;
block|}
specifier|private
name|boolean
name|isCurrentRealmSupported
parameter_list|(
name|ClaimsHandler
name|handler
parameter_list|,
name|ClaimsParameters
name|parameters
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|handler
operator|instanceof
name|RealmSupport
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|RealmSupport
name|handlerRealmSupport
init|=
operator|(
name|RealmSupport
operator|)
name|handler
decl_stmt|;
comment|// Check whether the handler supports the current realm
if|if
condition|(
name|handlerRealmSupport
operator|.
name|getSupportedRealms
argument_list|()
operator|!=
literal|null
operator|&&
name|handlerRealmSupport
operator|.
name|getSupportedRealms
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|&&
name|handlerRealmSupport
operator|.
name|getSupportedRealms
argument_list|()
operator|.
name|indexOf
argument_list|(
name|parameters
operator|.
name|getRealm
argument_list|()
argument_list|)
operator|==
operator|-
literal|1
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
name|FINER
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finer
argument_list|(
literal|"Handler '"
operator|+
name|handlerRealmSupport
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"' doesn't support"
operator|+
literal|" realm '"
operator|+
name|parameters
operator|.
name|getRealm
argument_list|()
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
comment|// If handler realm is configured and different from current realm
comment|// do an identity mapping
if|if
condition|(
name|handlerRealmSupport
operator|.
name|getHandlerRealm
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|handlerRealmSupport
operator|.
name|getHandlerRealm
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|parameters
operator|.
name|getRealm
argument_list|()
argument_list|)
condition|)
block|{
name|Principal
name|targetPrincipal
init|=
literal|null
decl_stmt|;
try|try
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
literal|"Mapping user '"
operator|+
name|parameters
operator|.
name|getPrincipal
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"' ["
operator|+
name|parameters
operator|.
name|getRealm
argument_list|()
operator|+
literal|"] to realm '"
operator|+
name|handlerRealmSupport
operator|.
name|getHandlerRealm
argument_list|()
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
name|targetPrincipal
operator|=
name|doMapping
argument_list|(
name|parameters
operator|.
name|getRealm
argument_list|()
argument_list|,
name|parameters
operator|.
name|getPrincipal
argument_list|()
argument_list|,
name|handlerRealmSupport
operator|.
name|getHandlerRealm
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
literal|"Failed to map user '"
operator|+
name|parameters
operator|.
name|getPrincipal
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"' ["
operator|+
name|parameters
operator|.
name|getRealm
argument_list|()
operator|+
literal|"] to realm '"
operator|+
name|handlerRealmSupport
operator|.
name|getHandlerRealm
argument_list|()
operator|+
literal|"'"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Failed to map user for claims handler"
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
if|if
condition|(
name|targetPrincipal
operator|==
literal|null
operator|||
name|targetPrincipal
operator|.
name|getName
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
literal|"Null. Failed to map user '"
operator|+
name|parameters
operator|.
name|getPrincipal
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"' ["
operator|+
name|parameters
operator|.
name|getRealm
argument_list|()
operator|+
literal|"] to realm '"
operator|+
name|handlerRealmSupport
operator|.
name|getHandlerRealm
argument_list|()
operator|+
literal|"'"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Principal '"
operator|+
name|targetPrincipal
operator|.
name|getName
argument_list|()
operator|+
literal|"' passed to handler '"
operator|+
name|handlerRealmSupport
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
name|parameters
operator|.
name|setPrincipal
argument_list|(
name|targetPrincipal
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINER
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finer
argument_list|(
literal|"Handler '"
operator|+
name|handlerRealmSupport
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"' doesn't require"
operator|+
literal|" identity mapping '"
operator|+
name|parameters
operator|.
name|getRealm
argument_list|()
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|ClaimCollection
name|filterHandlerClaims
parameter_list|(
name|ClaimCollection
name|claims
parameter_list|,
name|List
argument_list|<
name|URI
argument_list|>
name|handlerClaimTypes
parameter_list|)
block|{
name|ClaimCollection
name|supportedClaims
init|=
operator|new
name|ClaimCollection
argument_list|()
decl_stmt|;
name|supportedClaims
operator|.
name|setDialect
argument_list|(
name|claims
operator|.
name|getDialect
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Claim
name|claim
range|:
name|claims
control|)
block|{
if|if
condition|(
name|handlerClaimTypes
operator|.
name|contains
argument_list|(
name|claim
operator|.
name|getClaimType
argument_list|()
argument_list|)
condition|)
block|{
name|supportedClaims
operator|.
name|add
argument_list|(
name|claim
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|supportedClaims
return|;
block|}
specifier|private
name|boolean
name|validateClaimValues
parameter_list|(
name|ClaimCollection
name|requestedClaims
parameter_list|,
name|ProcessedClaimCollection
name|claims
parameter_list|)
block|{
for|for
control|(
name|Claim
name|claim
range|:
name|requestedClaims
control|)
block|{
name|URI
name|claimType
init|=
name|claim
operator|.
name|getClaimType
argument_list|()
decl_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
if|if
condition|(
operator|!
name|claim
operator|.
name|isOptional
argument_list|()
condition|)
block|{
for|for
control|(
name|ProcessedClaim
name|c
range|:
name|claims
control|)
block|{
if|if
condition|(
name|c
operator|.
name|getClaimType
argument_list|()
operator|.
name|equals
argument_list|(
name|claimType
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Mandatory claim not found: "
operator|+
name|claim
operator|.
name|getClaimType
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Mandatory claim '"
operator|+
name|claim
operator|.
name|getClaimType
argument_list|()
operator|+
literal|"' not found"
argument_list|)
throw|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|protected
name|List
argument_list|<
name|ProcessedClaim
argument_list|>
name|parseClaimsInAssertion
parameter_list|(
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml1
operator|.
name|core
operator|.
name|Assertion
name|assertion
parameter_list|)
block|{
name|List
argument_list|<
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml1
operator|.
name|core
operator|.
name|AttributeStatement
argument_list|>
name|attributeStatements
init|=
name|assertion
operator|.
name|getAttributeStatements
argument_list|()
decl_stmt|;
if|if
condition|(
name|attributeStatements
operator|==
literal|null
operator|||
name|attributeStatements
operator|.
name|isEmpty
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
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|"No attribute statements found"
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|ProcessedClaimCollection
name|collection
init|=
operator|new
name|ProcessedClaimCollection
argument_list|()
decl_stmt|;
for|for
control|(
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml1
operator|.
name|core
operator|.
name|AttributeStatement
name|statement
range|:
name|attributeStatements
control|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|"parsing statement: "
operator|+
name|statement
operator|.
name|getElementQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml1
operator|.
name|core
operator|.
name|Attribute
argument_list|>
name|attributes
init|=
name|statement
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
for|for
control|(
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml1
operator|.
name|core
operator|.
name|Attribute
name|attribute
range|:
name|attributes
control|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|"parsing attribute: "
operator|+
name|attribute
operator|.
name|getAttributeName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ProcessedClaim
name|c
init|=
operator|new
name|ProcessedClaim
argument_list|()
decl_stmt|;
name|c
operator|.
name|setIssuer
argument_list|(
name|assertion
operator|.
name|getIssuer
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|.
name|setClaimType
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|attribute
operator|.
name|getAttributeName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|c
operator|.
name|setClaimType
argument_list|(
operator|new
name|URI
argument_list|(
name|attribute
operator|.
name|getAttributeName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Invalid attribute name in attributestatement: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
for|for
control|(
name|XMLObject
name|attributeValue
range|:
name|attribute
operator|.
name|getAttributeValues
argument_list|()
control|)
block|{
name|Element
name|attributeValueElement
init|=
name|attributeValue
operator|.
name|getDOM
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|attributeValueElement
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|" ["
operator|+
name|value
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
name|c
operator|.
name|addValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
name|collection
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|collection
return|;
block|}
specifier|protected
name|List
argument_list|<
name|ProcessedClaim
argument_list|>
name|parseClaimsInAssertion
parameter_list|(
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Assertion
name|assertion
parameter_list|)
block|{
name|List
argument_list|<
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|AttributeStatement
argument_list|>
name|attributeStatements
init|=
name|assertion
operator|.
name|getAttributeStatements
argument_list|()
decl_stmt|;
if|if
condition|(
name|attributeStatements
operator|==
literal|null
operator|||
name|attributeStatements
operator|.
name|isEmpty
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
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|"No attribute statements found"
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|ProcessedClaim
argument_list|>
name|collection
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|AttributeStatement
name|statement
range|:
name|attributeStatements
control|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|"parsing statement: "
operator|+
name|statement
operator|.
name|getElementQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Attribute
argument_list|>
name|attributes
init|=
name|statement
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
for|for
control|(
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Attribute
name|attribute
range|:
name|attributes
control|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|"parsing attribute: "
operator|+
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ProcessedClaim
name|c
init|=
operator|new
name|ProcessedClaim
argument_list|()
decl_stmt|;
name|c
operator|.
name|setClaimType
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|c
operator|.
name|setIssuer
argument_list|(
name|assertion
operator|.
name|getIssuer
argument_list|()
operator|.
name|getNameQualifier
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|XMLObject
name|attributeValue
range|:
name|attribute
operator|.
name|getAttributeValues
argument_list|()
control|)
block|{
name|Element
name|attributeValueElement
init|=
name|attributeValue
operator|.
name|getDOM
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|attributeValueElement
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|" ["
operator|+
name|value
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
name|c
operator|.
name|addValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
name|collection
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|collection
return|;
block|}
comment|/**      * This method merges the primary claims with the secondary claims (of the same dialect).      * This facilitates handling claims from a service via wst:SecondaryParameters/wst:Claims      * with any client-specific claims sent in wst:RequestSecurityToken/wst:Claims      */
specifier|private
name|ClaimCollection
name|mergeClaims
parameter_list|(
name|ClaimCollection
name|primaryClaims
parameter_list|,
name|ClaimCollection
name|secondaryClaims
parameter_list|)
block|{
name|ClaimCollection
name|parsedClaims
init|=
operator|new
name|ClaimCollection
argument_list|()
decl_stmt|;
name|parsedClaims
operator|.
name|addAll
argument_list|(
name|secondaryClaims
argument_list|)
expr_stmt|;
comment|// Merge claims
name|ClaimCollection
name|mergedClaims
init|=
operator|new
name|ClaimCollection
argument_list|()
decl_stmt|;
name|mergedClaims
operator|.
name|setDialect
argument_list|(
name|primaryClaims
operator|.
name|getDialect
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Claim
name|claim
range|:
name|primaryClaims
control|)
block|{
name|Claim
name|matchingClaim
init|=
literal|null
decl_stmt|;
comment|// Search for a matching claim via the ClaimType URI
for|for
control|(
name|Claim
name|secondaryClaim
range|:
name|parsedClaims
control|)
block|{
if|if
condition|(
name|secondaryClaim
operator|.
name|getClaimType
argument_list|()
operator|.
name|equals
argument_list|(
name|claim
operator|.
name|getClaimType
argument_list|()
argument_list|)
condition|)
block|{
name|matchingClaim
operator|=
name|secondaryClaim
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|matchingClaim
operator|==
literal|null
condition|)
block|{
name|mergedClaims
operator|.
name|add
argument_list|(
name|claim
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Claim
name|mergedClaim
init|=
operator|new
name|Claim
argument_list|()
decl_stmt|;
name|mergedClaim
operator|.
name|setClaimType
argument_list|(
name|claim
operator|.
name|getClaimType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|claim
operator|.
name|getValues
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|claim
operator|.
name|getValues
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|mergedClaim
operator|.
name|setValues
argument_list|(
name|claim
operator|.
name|getValues
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|matchingClaim
operator|.
name|getValues
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|matchingClaim
operator|.
name|getValues
argument_list|()
operator|.
name|isEmpty
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
literal|"Secondary claim value "
operator|+
name|matchingClaim
operator|.
name|getValues
argument_list|()
operator|+
literal|" ignored in favour of primary claim value"
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|matchingClaim
operator|.
name|getValues
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|matchingClaim
operator|.
name|getValues
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|mergedClaim
operator|.
name|setValues
argument_list|(
name|matchingClaim
operator|.
name|getValues
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|mergedClaims
operator|.
name|add
argument_list|(
name|mergedClaim
argument_list|)
expr_stmt|;
comment|// Remove from parsed Claims
name|parsedClaims
operator|.
name|remove
argument_list|(
name|matchingClaim
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Now add in any claims from the parsed claims that weren't merged
name|mergedClaims
operator|.
name|addAll
argument_list|(
name|parsedClaims
argument_list|)
expr_stmt|;
return|return
name|mergedClaims
return|;
block|}
specifier|protected
name|Principal
name|doMapping
parameter_list|(
name|String
name|sourceRealm
parameter_list|,
name|Principal
name|sourcePrincipal
parameter_list|,
name|String
name|targetRealm
parameter_list|)
block|{
return|return
name|this
operator|.
name|identityMapper
operator|.
name|mapPrincipal
argument_list|(
name|sourceRealm
argument_list|,
name|sourcePrincipal
argument_list|,
name|targetRealm
argument_list|)
return|;
block|}
block|}
end_class

end_unit

