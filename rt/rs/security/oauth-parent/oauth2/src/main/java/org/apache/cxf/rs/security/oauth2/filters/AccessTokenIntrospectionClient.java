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
name|filters
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
name|ws
operator|.
name|rs
operator|.
name|WebApplicationException
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
name|jaxrs
operator|.
name|ext
operator|.
name|MessageContext
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
name|impl
operator|.
name|MetadataMap
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
name|AccessTokenValidation
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
name|OAuthPermission
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
name|TokenIntrospection
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
name|AccessTokenValidator
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

begin_class
specifier|public
class|class
name|AccessTokenIntrospectionClient
implements|implements
name|AccessTokenValidator
block|{
specifier|private
name|WebClient
name|tokenValidatorClient
decl_stmt|;
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSupportedAuthorizationSchemes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|OAuthConstants
operator|.
name|BEARER_AUTHORIZATION_SCHEME
argument_list|)
return|;
block|}
specifier|public
name|AccessTokenValidation
name|validateAccessToken
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|String
name|authScheme
parameter_list|,
name|String
name|authSchemeData
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraProps
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|fromClient
argument_list|(
name|tokenValidatorClient
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|props
operator|.
name|putSingle
argument_list|(
name|OAuthConstants
operator|.
name|TOKEN_ID
argument_list|,
name|authSchemeData
argument_list|)
expr_stmt|;
try|try
block|{
name|TokenIntrospection
name|response
init|=
name|client
operator|.
name|post
argument_list|(
name|props
argument_list|,
name|TokenIntrospection
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|convertIntrospectionToValidation
argument_list|(
name|response
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
name|AccessTokenValidation
name|convertIntrospectionToValidation
parameter_list|(
name|TokenIntrospection
name|response
parameter_list|)
block|{
name|AccessTokenValidation
name|atv
init|=
operator|new
name|AccessTokenValidation
argument_list|()
decl_stmt|;
name|atv
operator|.
name|setInitialValidationSuccessful
argument_list|(
name|response
operator|.
name|isActive
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|response
operator|.
name|isActive
argument_list|()
condition|)
block|{
return|return
name|atv
return|;
block|}
name|atv
operator|.
name|setClientId
argument_list|(
name|response
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|atv
operator|.
name|setTokenIssuedAt
argument_list|(
name|response
operator|.
name|getIat
argument_list|()
argument_list|)
expr_stmt|;
name|atv
operator|.
name|setTokenLifetime
argument_list|(
name|response
operator|.
name|getExp
argument_list|()
operator|-
name|response
operator|.
name|getIat
argument_list|()
argument_list|)
expr_stmt|;
name|atv
operator|.
name|setAudience
argument_list|(
name|response
operator|.
name|getAud
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|response
operator|.
name|getScope
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|scopes
init|=
name|response
operator|.
name|getScope
argument_list|()
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|perms
init|=
operator|new
name|LinkedList
argument_list|<
name|OAuthPermission
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|scopes
control|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|perms
operator|.
name|add
argument_list|(
operator|new
name|OAuthPermission
argument_list|(
name|s
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|atv
operator|.
name|setTokenScopes
argument_list|(
name|perms
argument_list|)
expr_stmt|;
block|}
return|return
name|atv
return|;
block|}
specifier|public
name|void
name|setTokenValidatorClient
parameter_list|(
name|WebClient
name|tokenValidatorClient
parameter_list|)
block|{
name|this
operator|.
name|tokenValidatorClient
operator|=
name|tokenValidatorClient
expr_stmt|;
block|}
block|}
end_class

end_unit

