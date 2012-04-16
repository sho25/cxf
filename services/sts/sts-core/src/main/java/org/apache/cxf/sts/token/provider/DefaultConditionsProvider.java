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
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|util
operator|.
name|XmlSchemaDateFormat
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
return|return
name|getConditions
argument_list|(
name|providerParameters
operator|.
name|getAppliesToAddress
argument_list|()
argument_list|,
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getLifetime
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Get a ConditionsBean object.      */
specifier|public
name|ConditionsBean
name|getConditions
parameter_list|(
name|String
name|appliesToAddress
parameter_list|,
name|Lifetime
name|tokenLifetime
parameter_list|)
block|{
name|ConditionsBean
name|conditions
init|=
operator|new
name|ConditionsBean
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
try|try
block|{
name|XmlSchemaDateFormat
name|fmt
init|=
operator|new
name|XmlSchemaDateFormat
argument_list|()
decl_stmt|;
name|Date
name|creationTime
init|=
name|fmt
operator|.
name|parse
argument_list|(
name|tokenLifetime
operator|.
name|getCreated
argument_list|()
argument_list|)
decl_stmt|;
name|Date
name|expirationTime
init|=
name|fmt
operator|.
name|parse
argument_list|(
name|tokenLifetime
operator|.
name|getExpires
argument_list|()
argument_list|)
decl_stmt|;
comment|// Check to see if the created time is in the future
name|Date
name|validCreation
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|long
name|currentTime
init|=
name|validCreation
operator|.
name|getTime
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
operator|.
name|setTime
argument_list|(
name|currentTime
operator|+
name|futureTimeToLive
operator|*
literal|1000
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|creationTime
operator|!=
literal|null
operator|&&
name|creationTime
operator|.
name|after
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
name|expirationTime
operator|.
name|getTime
argument_list|()
operator|-
name|creationTime
operator|.
name|getTime
argument_list|()
decl_stmt|;
if|if
condition|(
name|requestedLifetime
operator|>
operator|(
name|getMaxLifetime
argument_list|()
operator|*
literal|1000L
operator|)
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
operator|/
literal|1000L
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
operator|.
name|setTime
argument_list|(
name|creationTime
operator|.
name|getTime
argument_list|()
operator|+
operator|(
name|getMaxLifetime
argument_list|()
operator|*
literal|1000L
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
name|DateTime
name|creationDateTime
init|=
operator|new
name|DateTime
argument_list|(
name|creationTime
operator|.
name|getTime
argument_list|()
argument_list|)
decl_stmt|;
name|DateTime
name|expirationDateTime
init|=
operator|new
name|DateTime
argument_list|(
name|expirationTime
operator|.
name|getTime
argument_list|()
argument_list|)
decl_stmt|;
name|conditions
operator|.
name|setNotAfter
argument_list|(
name|expirationDateTime
argument_list|)
expr_stmt|;
name|conditions
operator|.
name|setNotBefore
argument_list|(
name|creationDateTime
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Failed to parse life time element: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|conditions
operator|.
name|setTokenPeriodMinutes
argument_list|(
call|(
name|int
call|)
argument_list|(
name|lifetime
operator|/
literal|60L
argument_list|)
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
call|(
name|int
call|)
argument_list|(
name|lifetime
operator|/
literal|60L
argument_list|)
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
name|conditions
operator|.
name|setAudienceURI
argument_list|(
name|appliesToAddress
argument_list|)
expr_stmt|;
return|return
name|conditions
return|;
block|}
block|}
end_class

end_unit

