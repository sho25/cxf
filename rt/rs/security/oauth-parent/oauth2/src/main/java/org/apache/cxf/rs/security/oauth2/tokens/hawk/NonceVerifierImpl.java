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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|tokens
operator|.
name|hawk
package|;
end_package

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
name|util
operator|.
name|StringUtils
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
name|oauth2
operator|.
name|provider
operator|.
name|OAuthServiceException
import|;
end_import

begin_class
specifier|public
class|class
name|NonceVerifierImpl
implements|implements
name|NonceVerifier
block|{
specifier|private
name|NonceStore
name|nonceStore
decl_stmt|;
specifier|private
name|long
name|allowedWindow
decl_stmt|;
specifier|public
name|void
name|verifyNonce
parameter_list|(
name|String
name|tokenKey
parameter_list|,
name|String
name|clientNonceString
parameter_list|,
name|String
name|clientTimestampString
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|clientNonceString
argument_list|)
operator|||
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|clientTimestampString
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"Nonce or timestamp is not available"
argument_list|)
throw|;
block|}
name|long
name|serverClock
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|long
name|clientTimestamp
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|clientTimestampString
argument_list|)
decl_stmt|;
name|NonceHistory
name|nonceHistory
init|=
name|nonceStore
operator|.
name|getNonceHistory
argument_list|(
name|tokenKey
argument_list|)
decl_stmt|;
name|Nonce
name|nonce
init|=
operator|new
name|Nonce
argument_list|(
name|clientNonceString
argument_list|,
name|clientTimestamp
argument_list|)
decl_stmt|;
if|if
condition|(
name|nonceHistory
operator|==
literal|null
condition|)
block|{
name|long
name|requestTimeDelta
init|=
name|serverClock
operator|-
name|clientTimestamp
decl_stmt|;
name|nonceStore
operator|.
name|initNonceHistory
argument_list|(
name|tokenKey
argument_list|,
name|nonce
argument_list|,
name|requestTimeDelta
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|checkAdjustedRequestTime
argument_list|(
name|serverClock
argument_list|,
name|clientTimestamp
argument_list|,
name|nonceHistory
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|nonceHistory
operator|.
name|addNonce
argument_list|(
name|nonce
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"Duplicate nonce"
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
name|void
name|checkAdjustedRequestTime
parameter_list|(
name|long
name|serverClock
parameter_list|,
name|long
name|clientTimestamp
parameter_list|,
name|NonceHistory
name|nonceHistory
parameter_list|)
block|{
name|long
name|adjustedRequestTime
init|=
name|clientTimestamp
operator|+
name|nonceHistory
operator|.
name|getRequestTimeDelta
argument_list|()
decl_stmt|;
name|long
name|requestDelta
init|=
name|Math
operator|.
name|abs
argument_list|(
name|serverClock
operator|-
name|adjustedRequestTime
argument_list|)
decl_stmt|;
if|if
condition|(
name|requestDelta
operator|>
name|allowedWindow
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"Timestamp is invalid"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setAllowedWindow
parameter_list|(
name|long
name|allowedWindow
parameter_list|)
block|{
name|this
operator|.
name|allowedWindow
operator|=
name|allowedWindow
expr_stmt|;
block|}
specifier|public
name|void
name|setNonceStore
parameter_list|(
name|NonceStore
name|nonceStore
parameter_list|)
block|{
name|this
operator|.
name|nonceStore
operator|=
name|nonceStore
expr_stmt|;
block|}
block|}
end_class

end_unit

