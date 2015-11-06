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
comment|/**  * A default implementation to create a JWTClaims object. The Subject name is the name  * of the current principal.   */
end_comment

begin_class
specifier|public
class|class
name|DefaultJWTClaimsProvider
implements|implements
name|JWTClaimsProvider
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
name|DefaultJWTClaimsProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|useX500CN
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
name|claims
operator|.
name|setIssuer
argument_list|(
literal|"DoubleItSTSIssuer"
argument_list|)
expr_stmt|;
name|Date
name|currentDate
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|claims
operator|.
name|setIssuedAt
argument_list|(
name|currentDate
operator|.
name|getTime
argument_list|()
operator|/
literal|1000L
argument_list|)
expr_stmt|;
name|long
name|currentTime
init|=
name|currentDate
operator|.
name|getTime
argument_list|()
operator|+
literal|300L
operator|*
literal|1000L
decl_stmt|;
name|currentDate
operator|.
name|setTime
argument_list|(
name|currentTime
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setExpiryTime
argument_list|(
name|currentDate
operator|.
name|getTime
argument_list|()
operator|/
literal|1000L
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
comment|/**      * Get the Principal (which is used as the Subject). By default, we check the following (in order):      *  - A valid OnBehalfOf principal      *  - A valid ActAs principal      *  - A valid principal associated with a token received as ValidateTarget      *  - The principal associated with the request. We don't need to check to see if it is "valid" here, as it      *    is not parsed by the STS (but rather the WS-Security layer).      */
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
block|}
end_class

end_unit

