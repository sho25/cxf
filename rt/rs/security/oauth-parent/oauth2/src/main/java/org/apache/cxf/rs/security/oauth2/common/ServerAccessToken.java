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
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
comment|/**  * Server Access Token representation  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|ServerAccessToken
extends|extends
name|AccessToken
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|638776204861456064L
decl_stmt|;
specifier|private
name|String
name|grantType
decl_stmt|;
specifier|private
name|Client
name|client
decl_stmt|;
specifier|private
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|scopes
init|=
operator|new
name|LinkedList
argument_list|<
name|OAuthPermission
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|UserSubject
name|subject
decl_stmt|;
specifier|private
name|String
name|audience
decl_stmt|;
specifier|protected
name|ServerAccessToken
parameter_list|()
block|{              }
specifier|protected
name|ServerAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|tokenType
parameter_list|,
name|String
name|tokenKey
parameter_list|,
name|long
name|expiresIn
parameter_list|)
block|{
name|this
argument_list|(
name|client
argument_list|,
name|tokenType
argument_list|,
name|tokenKey
argument_list|,
name|expiresIn
argument_list|,
name|OAuthUtils
operator|.
name|getIssuedAt
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|ServerAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|tokenType
parameter_list|,
name|String
name|tokenKey
parameter_list|,
name|long
name|expiresIn
parameter_list|,
name|long
name|issuedAt
parameter_list|)
block|{
name|super
argument_list|(
name|tokenType
argument_list|,
name|tokenKey
argument_list|,
name|expiresIn
argument_list|,
name|issuedAt
argument_list|)
expr_stmt|;
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
specifier|protected
name|ServerAccessToken
parameter_list|(
name|ServerAccessToken
name|token
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|super
argument_list|(
name|token
operator|.
name|getTokenType
argument_list|()
argument_list|,
name|key
argument_list|,
name|token
operator|.
name|getExpiresIn
argument_list|()
argument_list|,
name|token
operator|.
name|getIssuedAt
argument_list|()
argument_list|,
name|token
operator|.
name|getRefreshToken
argument_list|()
argument_list|,
name|token
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|client
operator|=
name|token
operator|.
name|getClient
argument_list|()
expr_stmt|;
name|this
operator|.
name|grantType
operator|=
name|token
operator|.
name|getGrantType
argument_list|()
expr_stmt|;
name|this
operator|.
name|scopes
operator|=
name|token
operator|.
name|getScopes
argument_list|()
expr_stmt|;
name|this
operator|.
name|audience
operator|=
name|token
operator|.
name|getAudience
argument_list|()
expr_stmt|;
name|this
operator|.
name|subject
operator|=
name|token
operator|.
name|getSubject
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns the Client associated with this token      * @return the client      */
specifier|public
name|Client
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
specifier|public
name|void
name|setClient
parameter_list|(
name|Client
name|c
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|c
expr_stmt|;
block|}
comment|/**      * Returns a list of opaque permissions/scopes      * @return the scopes      */
specifier|public
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|getScopes
parameter_list|()
block|{
return|return
name|scopes
return|;
block|}
comment|/**      * Sets a list of opaque permissions/scopes      * @param scopes the scopes      */
specifier|public
name|void
name|setScopes
parameter_list|(
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|scopes
parameter_list|)
block|{
name|this
operator|.
name|scopes
operator|=
name|scopes
expr_stmt|;
block|}
comment|/**      * Sets a subject capturing the login name       * the end user used to login to the resource server      * when authorizing a given client request      * @param subject      */
specifier|public
name|void
name|setSubject
parameter_list|(
name|UserSubject
name|subject
parameter_list|)
block|{
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
block|}
comment|/**      * Returns a subject capturing the login name       * the end user used to login to the resource server      * when authorizing a given client request      * @return UserSubject      */
specifier|public
name|UserSubject
name|getSubject
parameter_list|()
block|{
return|return
name|subject
return|;
block|}
comment|/**      * Sets the grant type which was used to obtain the access token      * @param grantType the grant type      */
specifier|public
name|void
name|setGrantType
parameter_list|(
name|String
name|grantType
parameter_list|)
block|{
name|this
operator|.
name|grantType
operator|=
name|grantType
expr_stmt|;
block|}
comment|/**      * Returns the grant type which was used to obtain the access token      * @return the grant type      */
specifier|public
name|String
name|getGrantType
parameter_list|()
block|{
return|return
name|grantType
return|;
block|}
specifier|public
name|String
name|getAudience
parameter_list|()
block|{
return|return
name|audience
return|;
block|}
specifier|public
name|void
name|setAudience
parameter_list|(
name|String
name|audience
parameter_list|)
block|{
name|this
operator|.
name|audience
operator|=
name|audience
expr_stmt|;
block|}
specifier|protected
specifier|static
name|ServerAccessToken
name|validateTokenType
parameter_list|(
name|ServerAccessToken
name|token
parameter_list|,
name|String
name|expectedType
parameter_list|)
block|{
if|if
condition|(
operator|!
name|token
operator|.
name|getTokenType
argument_list|()
operator|.
name|equals
argument_list|(
name|expectedType
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|SERVER_ERROR
argument_list|)
throw|;
block|}
return|return
name|token
return|;
block|}
block|}
end_class

end_unit

