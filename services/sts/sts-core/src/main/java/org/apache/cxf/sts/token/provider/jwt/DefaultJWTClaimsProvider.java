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
operator|.
name|jwt
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
name|time
operator|.
name|Duration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Instant
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|format
operator|.
name|DateTimeParseException
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
name|Iterator
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
name|UUID
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
name|x500
operator|.
name|X500Principal
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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|jwt
operator|.
name|JwtClaims
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
name|claims
operator|.
name|ClaimsUtils
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
name|ProcessedClaim
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
name|ProcessedClaimCollection
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
name|Lifetime
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
name|Participants
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
name|TokenProviderUtils
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

begin_comment
comment|/**  * A default implementation to create a JWTClaims object. The Subject name is the name  * of the current principal.  */
end_comment

begin_class
specifier|public
class|class
name|DefaultJWTClaimsProvider
implements|implements
name|JWTClaimsProvider
block|{
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_MAX_LIFETIME
init|=
literal|60L
operator|*
literal|60L
operator|*
literal|12L
decl_stmt|;
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
name|DefaultJWTClaimsProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|useX500CN
decl_stmt|;
specifier|private
name|long
name|lifetime
init|=
literal|60L
operator|*
literal|30L
decl_stmt|;
specifier|private
name|long
name|maxLifetime
init|=
name|DEFAULT_MAX_LIFETIME
decl_stmt|;
specifier|private
name|boolean
name|failLifetimeExceedance
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|acceptClientLifetime
decl_stmt|;
specifier|private
name|long
name|futureTimeToLive
init|=
literal|60L
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|claimTypeMap
decl_stmt|;
comment|/**      * Get a JwtClaims object.      */
specifier|public
name|JwtClaims
name|getJwtClaims
parameter_list|(
name|JWTClaimsProviderParameters
name|jwtClaimsProviderParameters
parameter_list|)
block|{
name|JwtClaims
name|claims
init|=
operator|new
name|JwtClaims
argument_list|()
decl_stmt|;
name|claims
operator|.
name|setSubject
argument_list|(
name|getSubjectName
argument_list|(
name|jwtClaimsProviderParameters
argument_list|)
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setTokenId
argument_list|(
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// Set the Issuer
name|String
name|issuer
init|=
name|jwtClaimsProviderParameters
operator|.
name|getIssuer
argument_list|()
decl_stmt|;
if|if
condition|(
name|issuer
operator|==
literal|null
condition|)
block|{
name|STSPropertiesMBean
name|stsProperties
init|=
name|jwtClaimsProviderParameters
operator|.
name|getProviderParameters
argument_list|()
operator|.
name|getStsProperties
argument_list|()
decl_stmt|;
name|claims
operator|.
name|setIssuer
argument_list|(
name|stsProperties
operator|.
name|getIssuer
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|claims
operator|.
name|setIssuer
argument_list|(
name|issuer
argument_list|)
expr_stmt|;
block|}
name|handleWSTrustClaims
argument_list|(
name|jwtClaimsProviderParameters
argument_list|,
name|claims
argument_list|)
expr_stmt|;
name|handleConditions
argument_list|(
name|jwtClaimsProviderParameters
argument_list|,
name|claims
argument_list|)
expr_stmt|;
name|handleAudienceRestriction
argument_list|(
name|jwtClaimsProviderParameters
argument_list|,
name|claims
argument_list|)
expr_stmt|;
name|handleActAs
argument_list|(
name|jwtClaimsProviderParameters
argument_list|,
name|claims
argument_list|)
expr_stmt|;
return|return
name|claims
return|;
block|}
specifier|protected
name|String
name|getSubjectName
parameter_list|(
name|JWTClaimsProviderParameters
name|jwtClaimsProviderParameters
parameter_list|)
block|{
name|Principal
name|principal
init|=
name|getPrincipal
argument_list|(
name|jwtClaimsProviderParameters
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
return|return
name|subjectName
return|;
block|}
comment|/**      * Get the Principal (which is used as the Subject). By default, we check the following (in order):      *  - A valid OnBehalfOf principal      *  - A valid principal associated with a token received as ValidateTarget      *  - The principal associated with the request. We don't need to check to see if it is "valid" here, as it      *    is not parsed by the STS (but rather the WS-Security layer).      */
specifier|protected
name|Principal
name|getPrincipal
parameter_list|(
name|JWTClaimsProviderParameters
name|jwtClaimsProviderParameters
parameter_list|)
block|{
name|TokenProviderParameters
name|providerParameters
init|=
name|jwtClaimsProviderParameters
operator|.
name|getProviderParameters
argument_list|()
decl_stmt|;
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
specifier|protected
name|void
name|handleWSTrustClaims
parameter_list|(
name|JWTClaimsProviderParameters
name|jwtClaimsProviderParameters
parameter_list|,
name|JwtClaims
name|claims
parameter_list|)
block|{
name|TokenProviderParameters
name|providerParameters
init|=
name|jwtClaimsProviderParameters
operator|.
name|getProviderParameters
argument_list|()
decl_stmt|;
comment|// Handle Claims
name|ProcessedClaimCollection
name|retrievedClaims
init|=
name|ClaimsUtils
operator|.
name|processClaims
argument_list|(
name|providerParameters
argument_list|)
decl_stmt|;
if|if
condition|(
name|retrievedClaims
operator|!=
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|ProcessedClaim
argument_list|>
name|claimIterator
init|=
name|retrievedClaims
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|claimIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProcessedClaim
name|claim
init|=
name|claimIterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|claim
operator|.
name|getClaimType
argument_list|()
operator|!=
literal|null
operator|&&
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
name|Object
name|claimValues
init|=
name|claim
operator|.
name|getValues
argument_list|()
decl_stmt|;
if|if
condition|(
name|claim
operator|.
name|getValues
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|claimValues
operator|=
name|claim
operator|.
name|getValues
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
name|claims
operator|.
name|setProperty
argument_list|(
name|translateClaim
argument_list|(
name|claim
operator|.
name|getClaimType
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
name|claimValues
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|void
name|handleConditions
parameter_list|(
name|JWTClaimsProviderParameters
name|jwtClaimsProviderParameters
parameter_list|,
name|JwtClaims
name|claims
parameter_list|)
block|{
name|TokenProviderParameters
name|providerParameters
init|=
name|jwtClaimsProviderParameters
operator|.
name|getProviderParameters
argument_list|()
decl_stmt|;
name|Instant
name|currentDate
init|=
name|Instant
operator|.
name|now
argument_list|()
decl_stmt|;
name|long
name|currentTime
init|=
name|currentDate
operator|.
name|getEpochSecond
argument_list|()
decl_stmt|;
comment|// Set the defaults first
name|claims
operator|.
name|setIssuedAt
argument_list|(
name|currentTime
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setNotBefore
argument_list|(
name|currentTime
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setExpiryTime
argument_list|(
name|currentTime
operator|+
name|lifetime
argument_list|)
expr_stmt|;
name|Lifetime
name|tokenLifetime
init|=
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getLifetime
argument_list|()
decl_stmt|;
if|if
condition|(
name|lifetime
operator|>
literal|0
operator|&&
name|acceptClientLifetime
operator|&&
name|tokenLifetime
operator|!=
literal|null
operator|&&
name|tokenLifetime
operator|.
name|getCreated
argument_list|()
operator|!=
literal|null
operator|&&
name|tokenLifetime
operator|.
name|getExpires
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Instant
name|creationTime
init|=
literal|null
decl_stmt|;
name|Instant
name|expirationTime
init|=
literal|null
decl_stmt|;
try|try
block|{
name|creationTime
operator|=
name|ZonedDateTime
operator|.
name|parse
argument_list|(
name|tokenLifetime
operator|.
name|getCreated
argument_list|()
argument_list|)
operator|.
name|toInstant
argument_list|()
expr_stmt|;
name|expirationTime
operator|=
name|ZonedDateTime
operator|.
name|parse
argument_list|(
name|tokenLifetime
operator|.
name|getExpires
argument_list|()
argument_list|)
operator|.
name|toInstant
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DateTimeParseException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Error in parsing Timestamp Created or Expiration Strings"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Error in parsing Timestamp Created or Expiration Strings"
argument_list|,
name|STSException
operator|.
name|INVALID_TIME
argument_list|)
throw|;
block|}
comment|// Check to see if the created time is in the future
name|Instant
name|validCreation
init|=
name|Instant
operator|.
name|now
argument_list|()
decl_stmt|;
if|if
condition|(
name|futureTimeToLive
operator|>
literal|0
condition|)
block|{
name|validCreation
operator|=
name|validCreation
operator|.
name|plusSeconds
argument_list|(
name|futureTimeToLive
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|creationTime
operator|.
name|isAfter
argument_list|(
name|validCreation
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"The Created Time is too far in the future"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"The Created Time is too far in the future"
argument_list|,
name|STSException
operator|.
name|INVALID_TIME
argument_list|)
throw|;
block|}
name|long
name|requestedLifetime
init|=
name|Duration
operator|.
name|between
argument_list|(
name|creationTime
argument_list|,
name|expirationTime
argument_list|)
operator|.
name|getSeconds
argument_list|()
decl_stmt|;
if|if
condition|(
name|requestedLifetime
operator|>
name|getMaxLifetime
argument_list|()
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"Requested lifetime ["
argument_list|)
operator|.
name|append
argument_list|(
name|requestedLifetime
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|" sec] exceed configured maximum lifetime ["
argument_list|)
operator|.
name|append
argument_list|(
name|getMaxLifetime
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|" sec]"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isFailLifetimeExceedance
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Requested lifetime exceeds maximum lifetime"
argument_list|,
name|STSException
operator|.
name|INVALID_TIME
argument_list|)
throw|;
block|}
else|else
block|{
name|expirationTime
operator|=
name|creationTime
operator|.
name|plusSeconds
argument_list|(
name|getMaxLifetime
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|long
name|creationTimeInSeconds
init|=
name|creationTime
operator|.
name|getEpochSecond
argument_list|()
decl_stmt|;
name|claims
operator|.
name|setIssuedAt
argument_list|(
name|creationTimeInSeconds
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setNotBefore
argument_list|(
name|creationTimeInSeconds
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setExpiryTime
argument_list|(
name|expirationTime
operator|.
name|getEpochSecond
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Set the audience restriction claim. The Audiences are from an AppliesTo address, and the wst:Participants      * (if either exist).      */
specifier|protected
name|void
name|handleAudienceRestriction
parameter_list|(
name|JWTClaimsProviderParameters
name|jwtClaimsProviderParameters
parameter_list|,
name|JwtClaims
name|claims
parameter_list|)
block|{
name|TokenProviderParameters
name|providerParameters
init|=
name|jwtClaimsProviderParameters
operator|.
name|getProviderParameters
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|audiences
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|appliesToAddress
init|=
name|providerParameters
operator|.
name|getAppliesToAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|appliesToAddress
operator|!=
literal|null
condition|)
block|{
name|audiences
operator|.
name|add
argument_list|(
name|appliesToAddress
argument_list|)
expr_stmt|;
block|}
name|Participants
name|participants
init|=
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getParticipants
argument_list|()
decl_stmt|;
if|if
condition|(
name|participants
operator|!=
literal|null
condition|)
block|{
name|String
name|address
init|=
name|TokenProviderUtils
operator|.
name|extractAddressFromParticipantsEPR
argument_list|(
name|participants
operator|.
name|getPrimaryParticipant
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|address
operator|!=
literal|null
condition|)
block|{
name|audiences
operator|.
name|add
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|participants
operator|.
name|getParticipants
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Object
name|participant
range|:
name|participants
operator|.
name|getParticipants
argument_list|()
control|)
block|{
if|if
condition|(
name|participant
operator|!=
literal|null
condition|)
block|{
name|address
operator|=
name|TokenProviderUtils
operator|.
name|extractAddressFromParticipantsEPR
argument_list|(
name|participant
argument_list|)
expr_stmt|;
if|if
condition|(
name|address
operator|!=
literal|null
condition|)
block|{
name|audiences
operator|.
name|add
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
name|audiences
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|claims
operator|.
name|setAudiences
argument_list|(
name|audiences
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|handleActAs
parameter_list|(
name|JWTClaimsProviderParameters
name|jwtClaimsProviderParameters
parameter_list|,
name|JwtClaims
name|claims
parameter_list|)
block|{
name|TokenProviderParameters
name|providerParameters
init|=
name|jwtClaimsProviderParameters
operator|.
name|getProviderParameters
argument_list|()
decl_stmt|;
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
name|claims
operator|.
name|setClaim
argument_list|(
literal|"ActAs"
argument_list|,
name|receivedToken
operator|.
name|getPrincipal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|String
name|translateClaim
parameter_list|(
name|String
name|claimType
parameter_list|)
block|{
if|if
condition|(
name|claimTypeMap
operator|==
literal|null
operator|||
operator|!
name|claimTypeMap
operator|.
name|containsKey
argument_list|(
name|claimType
argument_list|)
condition|)
block|{
return|return
name|claimType
return|;
block|}
return|return
name|claimTypeMap
operator|.
name|get
argument_list|(
name|claimType
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isUseX500CN
parameter_list|()
block|{
return|return
name|useX500CN
return|;
block|}
specifier|public
name|void
name|setUseX500CN
parameter_list|(
name|boolean
name|useX500CN
parameter_list|)
block|{
name|this
operator|.
name|useX500CN
operator|=
name|useX500CN
expr_stmt|;
block|}
comment|/**      * Get how long (in seconds) a client-supplied Created Element is allowed to be in the future.      * The default is 60 seconds to avoid common problems relating to clock skew.      */
specifier|public
name|long
name|getFutureTimeToLive
parameter_list|()
block|{
return|return
name|futureTimeToLive
return|;
block|}
comment|/**      * Set how long (in seconds) a client-supplied Created Element is allowed to be in the future.      * The default is 60 seconds to avoid common problems relating to clock skew.      */
specifier|public
name|void
name|setFutureTimeToLive
parameter_list|(
name|long
name|futureTimeToLive
parameter_list|)
block|{
name|this
operator|.
name|futureTimeToLive
operator|=
name|futureTimeToLive
expr_stmt|;
block|}
comment|/**      * Set the default lifetime in seconds for issued JWT tokens      * @param default lifetime in seconds      */
specifier|public
name|void
name|setLifetime
parameter_list|(
name|long
name|lifetime
parameter_list|)
block|{
name|this
operator|.
name|lifetime
operator|=
name|lifetime
expr_stmt|;
block|}
comment|/**      * Get the default lifetime in seconds for issued JWT token where requestor      * doesn't specify a lifetime element      * @return the lifetime in seconds      */
specifier|public
name|long
name|getLifetime
parameter_list|()
block|{
return|return
name|lifetime
return|;
block|}
comment|/**      * Set the maximum lifetime in seconds for issued JWT tokens      * @param maximum lifetime in seconds      */
specifier|public
name|void
name|setMaxLifetime
parameter_list|(
name|long
name|maxLifetime
parameter_list|)
block|{
name|this
operator|.
name|maxLifetime
operator|=
name|maxLifetime
expr_stmt|;
block|}
comment|/**      * Get the maximum lifetime in seconds for issued JWT token      * if requestor specifies lifetime element      * @return the maximum lifetime in seconds      */
specifier|public
name|long
name|getMaxLifetime
parameter_list|()
block|{
return|return
name|maxLifetime
return|;
block|}
comment|/**      * Is client lifetime element accepted      * Default: false      */
specifier|public
name|boolean
name|isAcceptClientLifetime
parameter_list|()
block|{
return|return
name|this
operator|.
name|acceptClientLifetime
return|;
block|}
comment|/**      * Set whether client lifetime is accepted      */
specifier|public
name|void
name|setAcceptClientLifetime
parameter_list|(
name|boolean
name|acceptClientLifetime
parameter_list|)
block|{
name|this
operator|.
name|acceptClientLifetime
operator|=
name|acceptClientLifetime
expr_stmt|;
block|}
comment|/**      * If requested lifetime exceeds shall it fail (default)      * or overwrite with maximum lifetime      */
specifier|public
name|boolean
name|isFailLifetimeExceedance
parameter_list|()
block|{
return|return
name|this
operator|.
name|failLifetimeExceedance
return|;
block|}
comment|/**      * If requested lifetime exceeds shall it fail (default)      * or overwrite with maximum lifetime      */
specifier|public
name|void
name|setFailLifetimeExceedance
parameter_list|(
name|boolean
name|failLifetimeExceedance
parameter_list|)
block|{
name|this
operator|.
name|failLifetimeExceedance
operator|=
name|failLifetimeExceedance
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getClaimTypeMap
parameter_list|()
block|{
return|return
name|claimTypeMap
return|;
block|}
comment|/**      * Specify a way to map ClaimType URIs to custom ClaimTypes      * @param claimTypeMap      */
specifier|public
name|void
name|setClaimTypeMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|claimTypeMap
parameter_list|)
block|{
name|this
operator|.
name|claimTypeMap
operator|=
name|claimTypeMap
expr_stmt|;
block|}
block|}
end_class

end_unit

