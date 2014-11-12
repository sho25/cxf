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
name|oidc
operator|.
name|common
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

begin_class
specifier|public
class|class
name|UserIdToken
extends|extends
name|JwtClaims
block|{
specifier|public
specifier|static
specifier|final
name|String
name|AUTH_TIME_CLAIM
init|=
literal|"auth_time"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NONCE_CLAIM
init|=
literal|"nonce"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACR_CLAIM
init|=
literal|"acr"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|AZP_CLAIM
init|=
literal|"azp"
decl_stmt|;
specifier|public
name|UserIdToken
parameter_list|()
block|{     }
specifier|public
name|UserIdToken
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|claims
parameter_list|)
block|{
name|super
argument_list|(
name|claims
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setAuthenticationTime
parameter_list|(
name|Long
name|time
parameter_list|)
block|{
name|setProperty
argument_list|(
name|AUTH_TIME_CLAIM
argument_list|,
name|time
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Long
name|getAuthenticationTime
parameter_list|()
block|{
return|return
name|getLongProperty
argument_list|(
name|AUTH_TIME_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|void
name|setNonce
parameter_list|(
name|String
name|nonce
parameter_list|)
block|{
name|setProperty
argument_list|(
name|NONCE_CLAIM
argument_list|,
name|nonce
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getNonce
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|NONCE_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|void
name|setAuthenticationContextRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|setProperty
argument_list|(
name|ACR_CLAIM
argument_list|,
name|ref
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getAuthenticationContextRef
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|ACR_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|void
name|setAuthorizedParty
parameter_list|(
name|String
name|azp
parameter_list|)
block|{
name|setProperty
argument_list|(
name|AZP_CLAIM
argument_list|,
name|azp
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getAuthorizedParty
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|AZP_CLAIM
argument_list|)
return|;
block|}
block|}
end_class

end_unit

