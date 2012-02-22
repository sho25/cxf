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
name|oauth
operator|.
name|grants
operator|.
name|code
package|;
end_package

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
name|UUID
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
name|rs
operator|.
name|security
operator|.
name|oauth
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
name|oauth
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
name|oauth
operator|.
name|provider
operator|.
name|AccessTokenGrantHandler
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
name|oauth
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
name|oauth
operator|.
name|tokens
operator|.
name|bearer
operator|.
name|BearerAccessToken
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
name|oauth
operator|.
name|utils
operator|.
name|MD5SequenceGenerator
import|;
end_import

begin_class
specifier|public
class|class
name|AuthorizationCodeGrantHandler
implements|implements
name|AccessTokenGrantHandler
block|{
specifier|private
specifier|static
specifier|final
name|String
name|GRANT_TYPE
init|=
literal|"grant_type"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|AUTHORIZATION_CODE_GRANT
init|=
literal|"authorization_code"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REDIRECT_URI
init|=
literal|"redirect_uri"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|DEFAULT_TOKEN_LIFETIME
init|=
literal|3600L
decl_stmt|;
specifier|private
name|AuthorizationCodeDataProvider
name|codeProvider
decl_stmt|;
specifier|private
name|long
name|tokenLifetime
init|=
name|DEFAULT_TOKEN_LIFETIME
decl_stmt|;
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSupportedGrantTypes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|AUTHORIZATION_CODE_GRANT
argument_list|)
return|;
block|}
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
name|ServerAuthorizationCodeGrant
name|grant
init|=
name|codeProvider
operator|.
name|removeCodeGrant
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|GRANT_TYPE
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|grant
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|expectedRedirectUri
init|=
name|grant
operator|.
name|getRedirectUri
argument_list|()
decl_stmt|;
if|if
condition|(
name|expectedRedirectUri
operator|!=
literal|null
condition|)
block|{
name|String
name|providedRedirectUri
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|REDIRECT_URI
argument_list|)
decl_stmt|;
if|if
condition|(
name|providedRedirectUri
operator|==
literal|null
operator|||
operator|!
name|providedRedirectUri
operator|.
name|equals
argument_list|(
name|expectedRedirectUri
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"invalid_request"
argument_list|)
throw|;
block|}
block|}
name|BearerAccessToken
name|token
init|=
operator|new
name|BearerAccessToken
argument_list|(
name|client
argument_list|,
name|generateTokenKey
argument_list|()
argument_list|,
name|tokenLifetime
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|/
literal|1000
argument_list|)
decl_stmt|;
name|token
operator|.
name|setScopes
argument_list|(
name|grant
operator|.
name|getApprovedScopes
argument_list|()
argument_list|)
expr_stmt|;
name|token
operator|.
name|setSubject
argument_list|(
name|grant
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|token
return|;
block|}
specifier|public
name|void
name|setCodeProvider
parameter_list|(
name|AuthorizationCodeDataProvider
name|codeProvider
parameter_list|)
block|{
name|this
operator|.
name|codeProvider
operator|=
name|codeProvider
expr_stmt|;
block|}
specifier|protected
name|String
name|generateTokenKey
parameter_list|()
throws|throws
name|OAuthServiceException
block|{
try|try
block|{
name|byte
index|[]
name|bytes
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
decl_stmt|;
return|return
operator|new
name|MD5SequenceGenerator
argument_list|()
operator|.
name|generate
argument_list|(
name|bytes
argument_list|)
return|;
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
literal|"server_error"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setTokenLifetime
parameter_list|(
name|long
name|tokenLifetime
parameter_list|)
block|{
name|this
operator|.
name|tokenLifetime
operator|=
name|tokenLifetime
expr_stmt|;
block|}
block|}
end_class

end_unit

