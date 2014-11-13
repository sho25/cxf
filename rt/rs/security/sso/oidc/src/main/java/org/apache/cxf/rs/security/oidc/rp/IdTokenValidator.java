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
name|rp
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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|jwt
operator|.
name|JwtToken
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
name|common
operator|.
name|ClientAccessToken
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
name|oidc
operator|.
name|common
operator|.
name|UserIdToken
import|;
end_import

begin_class
specifier|public
class|class
name|IdTokenValidator
extends|extends
name|AbstractTokenValidator
block|{
specifier|private
name|boolean
name|requireAtHash
init|=
literal|true
decl_stmt|;
specifier|public
name|UserIdToken
name|getIdTokenFromJwt
parameter_list|(
name|ClientAccessToken
name|at
parameter_list|,
name|String
name|clientId
parameter_list|)
block|{
name|JwtToken
name|jwt
init|=
name|getIdJwtToken
argument_list|(
name|at
argument_list|,
name|clientId
argument_list|)
decl_stmt|;
return|return
name|getIdTokenFromJwt
argument_list|(
name|jwt
argument_list|,
name|clientId
argument_list|)
return|;
block|}
specifier|public
name|UserIdToken
name|getIdTokenFromJwt
parameter_list|(
name|JwtToken
name|jwt
parameter_list|,
name|String
name|clientId
parameter_list|)
block|{
comment|//TODO: do the extra validation if needed
return|return
operator|new
name|UserIdToken
argument_list|(
name|jwt
operator|.
name|getClaims
argument_list|()
operator|.
name|asMap
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|JwtToken
name|getIdJwtToken
parameter_list|(
name|ClientAccessToken
name|at
parameter_list|,
name|String
name|clientId
parameter_list|)
block|{
name|String
name|idJwtToken
init|=
name|at
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
literal|"id_token"
argument_list|)
decl_stmt|;
name|JwtToken
name|jwt
init|=
name|getJwtToken
argument_list|(
name|idJwtToken
argument_list|,
name|clientId
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|validateJwtClaims
argument_list|(
name|jwt
operator|.
name|getClaims
argument_list|()
argument_list|,
name|clientId
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|OidcUtils
operator|.
name|validateAccessTokenHash
argument_list|(
name|at
argument_list|,
name|jwt
argument_list|,
name|requireAtHash
argument_list|)
expr_stmt|;
return|return
name|jwt
return|;
block|}
specifier|public
name|void
name|setRequireAtHash
parameter_list|(
name|boolean
name|requireAtHash
parameter_list|)
block|{
name|this
operator|.
name|requireAtHash
operator|=
name|requireAtHash
expr_stmt|;
block|}
block|}
end_class

end_unit

