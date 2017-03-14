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
name|ZoneOffset
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
name|Logger
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
name|bean
operator|.
name|AudienceRestrictionBean
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

begin_comment
comment|/**  * A default implementation of the ConditionsProvider interface.  */
end_comment

begin_class
specifier|public
class|class
name|DefaultConditionsProvider
implements|implements
name|ConditionsProvider
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
name|DefaultConditionsProvider
operator|.
name|class
argument_list|)
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
comment|/**      * Set the default lifetime in seconds for issued SAML tokens      * @param default lifetime in seconds      */
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
comment|/**      * Get the default lifetime in seconds for issued SAML token where requestor      * doesn't specify a lifetime element      * @return the lifetime in seconds      */
specifier|public
name|long
name|getLifetime
parameter_list|()
block|{
return|return
name|lifetime
return|;
block|}
comment|/**      * Set the maximum lifetime in seconds for issued SAML tokens      * @param maximum lifetime in seconds      */
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
comment|/**      * Get the maximum lifetime in seconds for issued SAML token      * if requestor specifies lifetime element      * @return the maximum lifetime in seconds      */
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
comment|/**      * Get a ConditionsBean object.      */
specifier|public
name|ConditionsBean
name|getConditions
parameter_list|(
name|TokenProviderParameters
name|providerParameters
parameter_list|)
block|{
name|ConditionsBean
name|conditions
init|=
operator|new
name|ConditionsBean
argument_list|()
decl_stmt|;
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
condition|)
block|{
if|if
condition|(
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
name|ZonedDateTime
name|creationTime
init|=
literal|null
decl_stmt|;
name|ZonedDateTime
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
name|ZonedDateTime
name|validCreation
init|=
name|ZonedDateTime
operator|.
name|now
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
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
name|conditions
operator|.
name|setNotAfter
argument_list|(
name|expirationTime
argument_list|)
expr_stmt|;
name|conditions
operator|.
name|setNotBefore
argument_list|(
name|creationTime
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|conditions
operator|.
name|setTokenPeriodSeconds
argument_list|(
name|lifetime
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|conditions
operator|.
name|setTokenPeriodMinutes
argument_list|(
literal|5
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|AudienceRestrictionBean
argument_list|>
name|audienceRestrictions
init|=
name|createAudienceRestrictions
argument_list|(
name|providerParameters
argument_list|)
decl_stmt|;
if|if
condition|(
name|audienceRestrictions
operator|!=
literal|null
operator|&&
operator|!
name|audienceRestrictions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|conditions
operator|.
name|setAudienceRestrictions
argument_list|(
name|audienceRestrictions
argument_list|)
expr_stmt|;
block|}
return|return
name|conditions
return|;
block|}
comment|/**      * Create a list of AudienceRestrictions to be added to the Conditions Element of the      * issued Assertion. The default behaviour is to add a single Audience URI per      * AudienceRestriction Element. The Audience URIs are from an AppliesTo address, and      * the wst:Participants (if either exist).      */
specifier|protected
name|List
argument_list|<
name|AudienceRestrictionBean
argument_list|>
name|createAudienceRestrictions
parameter_list|(
name|TokenProviderParameters
name|providerParameters
parameter_list|)
block|{
name|List
argument_list|<
name|AudienceRestrictionBean
argument_list|>
name|audienceRestrictions
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
name|AudienceRestrictionBean
name|audienceRestriction
init|=
operator|new
name|AudienceRestrictionBean
argument_list|()
decl_stmt|;
name|audienceRestriction
operator|.
name|setAudienceURIs
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|appliesToAddress
argument_list|)
argument_list|)
expr_stmt|;
name|audienceRestrictions
operator|.
name|add
argument_list|(
name|audienceRestriction
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
name|AudienceRestrictionBean
name|audienceRestriction
init|=
operator|new
name|AudienceRestrictionBean
argument_list|()
decl_stmt|;
name|audienceRestriction
operator|.
name|setAudienceURIs
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|address
argument_list|)
argument_list|)
expr_stmt|;
name|audienceRestrictions
operator|.
name|add
argument_list|(
name|audienceRestriction
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
name|AudienceRestrictionBean
name|audienceRestriction
init|=
operator|new
name|AudienceRestrictionBean
argument_list|()
decl_stmt|;
name|audienceRestriction
operator|.
name|setAudienceURIs
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|address
argument_list|)
argument_list|)
expr_stmt|;
name|audienceRestrictions
operator|.
name|add
argument_list|(
name|audienceRestriction
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|audienceRestrictions
return|;
block|}
comment|/**      * Extract an address from a Participants EPR DOM element      */
specifier|protected
name|String
name|extractAddressFromParticipantsEPR
parameter_list|(
name|Object
name|participants
parameter_list|)
block|{
return|return
name|TokenProviderUtils
operator|.
name|extractAddressFromParticipantsEPR
argument_list|(
name|participants
argument_list|)
return|;
block|}
block|}
end_class

end_unit

