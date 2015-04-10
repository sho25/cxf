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
name|grants
operator|.
name|jwt
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

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
name|MultivaluedMap
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
name|utils
operator|.
name|HttpUtils
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
name|jws
operator|.
name|JwsJwtCompactConsumer
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
name|common
operator|.
name|Client
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
name|ServerAccessToken
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
name|UserSubject
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
name|utils
operator|.
name|OAuthConstants
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
name|utils
operator|.
name|OAuthUtils
import|;
end_import

begin_comment
comment|/**  * The "JWT Bearer" grant handler  */
end_comment

begin_class
specifier|public
class|class
name|JwtBearerGrantHandler
extends|extends
name|AbstractJwtHandler
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ENCODED_JWT_BEARER_GRANT
decl_stmt|;
static|static
block|{
comment|//  AccessTokenService may be configured with the form provider
comment|// which will not decode by default - so listing both the actual
comment|// and encoded grant type value will help
name|ENCODED_JWT_BEARER_GRANT
operator|=
name|HttpUtils
operator|.
name|urlEncode
argument_list|(
name|Constants
operator|.
name|JWT_BEARER_GRANT
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JwtBearerGrantHandler
parameter_list|()
block|{
name|super
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Constants
operator|.
name|JWT_BEARER_GRANT
argument_list|,
name|ENCODED_JWT_BEARER_GRANT
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAccessToken
name|createAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|String
name|assertion
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|Constants
operator|.
name|CLIENT_GRANT_ASSERTION_PARAM
argument_list|)
decl_stmt|;
if|if
condition|(
name|assertion
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_GRANT
argument_list|)
throw|;
block|}
try|try
block|{
name|JwsJwtCompactConsumer
name|jwsReader
init|=
name|getJwsReader
argument_list|(
name|assertion
argument_list|)
decl_stmt|;
name|JwtToken
name|jwtToken
init|=
name|jwsReader
operator|.
name|getJwtToken
argument_list|()
decl_stmt|;
name|validateSignature
argument_list|(
name|jwtToken
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|jwsReader
operator|.
name|getUnsignedEncodedSequence
argument_list|()
argument_list|,
name|jwsReader
operator|.
name|getDecodedSignature
argument_list|()
argument_list|)
expr_stmt|;
name|validateClaims
argument_list|(
name|client
argument_list|,
name|jwtToken
operator|.
name|getClaims
argument_list|()
argument_list|)
expr_stmt|;
name|UserSubject
name|grantSubject
init|=
operator|new
name|UserSubject
argument_list|(
name|jwtToken
operator|.
name|getClaims
argument_list|()
operator|.
name|getSubject
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|doCreateAccessToken
argument_list|(
name|client
argument_list|,
name|grantSubject
argument_list|,
name|Constants
operator|.
name|JWT_BEARER_GRANT
argument_list|,
name|OAuthUtils
operator|.
name|parseScope
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|SCOPE
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OAuthServiceException
name|ex
parameter_list|)
block|{
throw|throw
name|ex
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_GRANT
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|JwsJwtCompactConsumer
name|getJwsReader
parameter_list|(
name|String
name|assertion
parameter_list|)
block|{
return|return
operator|new
name|JwsJwtCompactConsumer
argument_list|(
name|assertion
argument_list|)
return|;
block|}
block|}
end_class

end_unit

