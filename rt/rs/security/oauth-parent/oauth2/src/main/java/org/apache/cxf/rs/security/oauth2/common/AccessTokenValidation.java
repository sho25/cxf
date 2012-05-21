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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_comment
comment|// Represents the information about the validated ServerAccessToken.
end_comment

begin_comment
comment|// The problem with reading specific ServerAccessToken instances is that
end_comment

begin_comment
comment|// the (JAXB) reader needs to be specifically aware of the concrete token
end_comment

begin_comment
comment|// classes like BearerAccessToken, etc, even though classes like BearerAccessToken
end_comment

begin_comment
comment|// will not add anything useful to the filter protecting the application.
end_comment

begin_comment
comment|//TODO: consider simply extending ServerAccessToken,
end_comment

begin_comment
comment|// though this will require relaxing a bit the ServerAccessToken model
end_comment

begin_comment
comment|// (introduce default constructors, etc)
end_comment

begin_class
annotation|@
name|XmlRootElement
specifier|public
class|class
name|AccessTokenValidation
block|{
specifier|private
name|String
name|clientId
decl_stmt|;
specifier|private
name|UserSubject
name|clientSubject
decl_stmt|;
specifier|private
name|String
name|tokenKey
decl_stmt|;
specifier|private
name|String
name|tokenType
decl_stmt|;
specifier|private
name|String
name|tokenGrantType
decl_stmt|;
specifier|private
name|long
name|tokenIssuedAt
decl_stmt|;
specifier|private
name|long
name|tokenLifetime
decl_stmt|;
specifier|private
name|UserSubject
name|tokenSubject
decl_stmt|;
specifier|private
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|tokenScopes
init|=
operator|new
name|LinkedList
argument_list|<
name|OAuthPermission
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|AccessTokenValidation
parameter_list|()
block|{              }
specifier|public
name|AccessTokenValidation
parameter_list|(
name|ServerAccessToken
name|token
parameter_list|)
block|{
name|this
operator|.
name|clientId
operator|=
name|token
operator|.
name|getClient
argument_list|()
operator|.
name|getClientId
argument_list|()
expr_stmt|;
name|this
operator|.
name|clientSubject
operator|=
name|token
operator|.
name|getClient
argument_list|()
operator|.
name|getSubject
argument_list|()
expr_stmt|;
name|this
operator|.
name|tokenKey
operator|=
name|token
operator|.
name|getTokenKey
argument_list|()
expr_stmt|;
name|this
operator|.
name|tokenType
operator|=
name|token
operator|.
name|getTokenType
argument_list|()
expr_stmt|;
name|this
operator|.
name|tokenGrantType
operator|=
name|token
operator|.
name|getGrantType
argument_list|()
expr_stmt|;
name|this
operator|.
name|tokenIssuedAt
operator|=
name|token
operator|.
name|getIssuedAt
argument_list|()
expr_stmt|;
name|this
operator|.
name|tokenLifetime
operator|=
name|token
operator|.
name|getLifetime
argument_list|()
expr_stmt|;
name|this
operator|.
name|tokenSubject
operator|=
name|token
operator|.
name|getSubject
argument_list|()
expr_stmt|;
name|this
operator|.
name|tokenScopes
operator|=
name|token
operator|.
name|getScopes
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getClientId
parameter_list|()
block|{
return|return
name|clientId
return|;
block|}
specifier|public
name|void
name|setClientId
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|this
operator|.
name|clientId
operator|=
name|clientId
expr_stmt|;
block|}
specifier|public
name|UserSubject
name|getClientSubject
parameter_list|()
block|{
return|return
name|clientSubject
return|;
block|}
specifier|public
name|void
name|setClientSubject
parameter_list|(
name|UserSubject
name|clientSubject
parameter_list|)
block|{
name|this
operator|.
name|clientSubject
operator|=
name|clientSubject
expr_stmt|;
block|}
specifier|public
name|String
name|getTokenKey
parameter_list|()
block|{
return|return
name|tokenKey
return|;
block|}
specifier|public
name|void
name|setTokenKey
parameter_list|(
name|String
name|tokenId
parameter_list|)
block|{
name|this
operator|.
name|tokenKey
operator|=
name|tokenId
expr_stmt|;
block|}
specifier|public
name|UserSubject
name|getTokenSubject
parameter_list|()
block|{
return|return
name|tokenSubject
return|;
block|}
specifier|public
name|void
name|setTokenSubject
parameter_list|(
name|UserSubject
name|tokenSubject
parameter_list|)
block|{
name|this
operator|.
name|tokenSubject
operator|=
name|tokenSubject
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|getTokenScopes
parameter_list|()
block|{
return|return
name|tokenScopes
return|;
block|}
specifier|public
name|void
name|setTokenScopes
parameter_list|(
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|tokenPermissions
parameter_list|)
block|{
name|this
operator|.
name|tokenScopes
operator|=
name|tokenPermissions
expr_stmt|;
block|}
specifier|public
name|String
name|getTokenGrantType
parameter_list|()
block|{
return|return
name|tokenGrantType
return|;
block|}
specifier|public
name|void
name|setTokenGrantType
parameter_list|(
name|String
name|tokenGrantType
parameter_list|)
block|{
name|this
operator|.
name|tokenGrantType
operator|=
name|tokenGrantType
expr_stmt|;
block|}
specifier|public
name|long
name|getTokenIssuedAt
parameter_list|()
block|{
return|return
name|tokenIssuedAt
return|;
block|}
specifier|public
name|void
name|setTokenIssuedAt
parameter_list|(
name|long
name|tokenIssuedAt
parameter_list|)
block|{
name|this
operator|.
name|tokenIssuedAt
operator|=
name|tokenIssuedAt
expr_stmt|;
block|}
specifier|public
name|long
name|getTokenLifetime
parameter_list|()
block|{
return|return
name|tokenLifetime
return|;
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
specifier|public
name|String
name|getTokenType
parameter_list|()
block|{
return|return
name|tokenType
return|;
block|}
specifier|public
name|void
name|setTokenType
parameter_list|(
name|String
name|tokenType
parameter_list|)
block|{
name|this
operator|.
name|tokenType
operator|=
name|tokenType
expr_stmt|;
block|}
block|}
end_class

end_unit

