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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Form
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
name|jaxrs
operator|.
name|client
operator|.
name|WebClient
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
name|client
operator|.
name|Consumer
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
name|client
operator|.
name|OAuthClientUtils
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
name|IdToken
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
name|UserInfo
import|;
end_import

begin_class
specifier|public
class|class
name|UserInfoClient
extends|extends
name|AbstractTokenValidator
block|{
specifier|private
name|boolean
name|sendTokenAsFormParameter
decl_stmt|;
specifier|private
name|WebClient
name|profileClient
decl_stmt|;
specifier|private
name|boolean
name|getUserInfoFromJwt
decl_stmt|;
specifier|public
name|UserInfo
name|getUserInfo
parameter_list|(
name|ClientAccessToken
name|at
parameter_list|,
name|IdToken
name|idToken
parameter_list|,
name|Consumer
name|client
parameter_list|)
block|{
if|if
condition|(
operator|!
name|sendTokenAsFormParameter
condition|)
block|{
name|OAuthClientUtils
operator|.
name|setAuthorizationHeader
argument_list|(
name|profileClient
argument_list|,
name|at
argument_list|)
expr_stmt|;
if|if
condition|(
name|getUserInfoFromJwt
condition|)
block|{
name|String
name|jwt
init|=
name|profileClient
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|getUserInfoFromJwt
argument_list|(
name|jwt
argument_list|,
name|idToken
argument_list|,
name|client
argument_list|)
return|;
block|}
else|else
block|{
name|UserInfo
name|profile
init|=
name|profileClient
operator|.
name|get
argument_list|(
name|UserInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|validateUserInfo
argument_list|(
name|profile
argument_list|,
name|idToken
argument_list|,
name|client
argument_list|)
expr_stmt|;
return|return
name|profile
return|;
block|}
block|}
else|else
block|{
name|Form
name|form
init|=
operator|new
name|Form
argument_list|()
operator|.
name|param
argument_list|(
literal|"access_token"
argument_list|,
name|at
operator|.
name|getTokenKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|getUserInfoFromJwt
condition|)
block|{
name|String
name|jwt
init|=
name|profileClient
operator|.
name|form
argument_list|(
name|form
argument_list|)
operator|.
name|readEntity
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|getUserInfoFromJwt
argument_list|(
name|jwt
argument_list|,
name|idToken
argument_list|,
name|client
argument_list|)
return|;
block|}
else|else
block|{
name|UserInfo
name|profile
init|=
name|profileClient
operator|.
name|form
argument_list|(
name|form
argument_list|)
operator|.
name|readEntity
argument_list|(
name|UserInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|validateUserInfo
argument_list|(
name|profile
argument_list|,
name|idToken
argument_list|,
name|client
argument_list|)
expr_stmt|;
return|return
name|profile
return|;
block|}
block|}
block|}
specifier|public
name|UserInfo
name|getUserInfoFromJwt
parameter_list|(
name|String
name|profileJwtToken
parameter_list|,
name|IdToken
name|idToken
parameter_list|,
name|Consumer
name|client
parameter_list|)
block|{
name|JwtToken
name|jwt
init|=
name|getUserInfoJwt
argument_list|(
name|profileJwtToken
argument_list|,
name|client
argument_list|)
decl_stmt|;
return|return
name|getUserInfoFromJwt
argument_list|(
name|jwt
argument_list|,
name|idToken
argument_list|,
name|client
argument_list|)
return|;
block|}
specifier|public
name|UserInfo
name|getUserInfoFromJwt
parameter_list|(
name|JwtToken
name|jwt
parameter_list|,
name|IdToken
name|idToken
parameter_list|,
name|Consumer
name|client
parameter_list|)
block|{
name|UserInfo
name|profile
init|=
operator|new
name|UserInfo
argument_list|(
name|jwt
operator|.
name|getClaims
argument_list|()
operator|.
name|asMap
argument_list|()
argument_list|)
decl_stmt|;
name|validateUserInfo
argument_list|(
name|profile
argument_list|,
name|idToken
argument_list|,
name|client
argument_list|)
expr_stmt|;
return|return
name|profile
return|;
block|}
specifier|public
name|JwtToken
name|getUserInfoJwt
parameter_list|(
name|String
name|profileJwtToken
parameter_list|,
name|Consumer
name|client
parameter_list|)
block|{
return|return
name|getJwtToken
argument_list|(
name|profileJwtToken
argument_list|)
return|;
block|}
specifier|public
name|void
name|validateUserInfo
parameter_list|(
name|UserInfo
name|profile
parameter_list|,
name|IdToken
name|idToken
parameter_list|,
name|Consumer
name|client
parameter_list|)
block|{
name|validateJwtClaims
argument_list|(
name|profile
argument_list|,
name|client
operator|.
name|getClientId
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// validate subject
if|if
condition|(
operator|!
name|idToken
operator|.
name|getSubject
argument_list|()
operator|.
name|equals
argument_list|(
name|profile
operator|.
name|getSubject
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"Invalid subject"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setUserInfoServiceClient
parameter_list|(
name|WebClient
name|client
parameter_list|)
block|{
name|this
operator|.
name|profileClient
operator|=
name|client
expr_stmt|;
block|}
specifier|public
name|void
name|setSendTokenAsFormParameter
parameter_list|(
name|boolean
name|sendTokenAsFormParameter
parameter_list|)
block|{
name|this
operator|.
name|sendTokenAsFormParameter
operator|=
name|sendTokenAsFormParameter
expr_stmt|;
block|}
specifier|public
name|void
name|setGetUserInfoFromJwt
parameter_list|(
name|boolean
name|getUserInfoFromJwt
parameter_list|)
block|{
name|this
operator|.
name|getUserInfoFromJwt
operator|=
name|getUserInfoFromJwt
expr_stmt|;
block|}
block|}
end_class

end_unit

