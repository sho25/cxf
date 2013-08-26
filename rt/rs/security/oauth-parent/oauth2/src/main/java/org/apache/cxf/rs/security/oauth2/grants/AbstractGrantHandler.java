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
name|logging
operator|.
name|Logger
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
name|oauth2
operator|.
name|common
operator|.
name|AccessTokenRegistration
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
name|OAuthError
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
name|oauth2
operator|.
name|provider
operator|.
name|OAuthDataProvider
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
comment|/**  * Abstract access token grant handler  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractGrantHandler
implements|implements
name|AccessTokenGrantHandler
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
name|AbstractGrantHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|supportedGrants
decl_stmt|;
specifier|private
name|OAuthDataProvider
name|dataProvider
decl_stmt|;
specifier|private
name|boolean
name|partialMatchScopeValidation
decl_stmt|;
specifier|private
name|boolean
name|canSupportPublicClients
decl_stmt|;
specifier|protected
name|AbstractGrantHandler
parameter_list|(
name|String
name|grant
parameter_list|)
block|{
name|supportedGrants
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|grant
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractGrantHandler
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|grants
parameter_list|)
block|{
if|if
condition|(
name|grants
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The list of grant types can not be empty"
argument_list|)
throw|;
block|}
name|supportedGrants
operator|=
name|grants
expr_stmt|;
block|}
specifier|public
name|void
name|setDataProvider
parameter_list|(
name|OAuthDataProvider
name|dataProvider
parameter_list|)
block|{
name|this
operator|.
name|dataProvider
operator|=
name|dataProvider
expr_stmt|;
block|}
specifier|public
name|OAuthDataProvider
name|getDataProvider
parameter_list|()
block|{
return|return
name|dataProvider
return|;
block|}
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
name|unmodifiableList
argument_list|(
name|supportedGrants
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
specifier|protected
name|void
name|checkIfGrantSupported
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
name|checkIfGrantSupported
argument_list|(
name|client
argument_list|,
name|getSingleGrantType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkIfGrantSupported
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|requestedGrant
parameter_list|)
block|{
if|if
condition|(
operator|!
name|OAuthUtils
operator|.
name|isGrantSupportedForClient
argument_list|(
name|client
argument_list|,
name|canSupportPublicClients
argument_list|,
name|requestedGrant
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|UNAUTHORIZED_CLIENT
argument_list|)
throw|;
block|}
block|}
specifier|private
name|String
name|getSingleGrantType
parameter_list|()
block|{
if|if
condition|(
name|supportedGrants
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|String
name|errorMessage
init|=
literal|"Request grant type must be specified"
decl_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|errorMessage
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|500
argument_list|)
throw|;
block|}
return|return
name|supportedGrants
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
specifier|protected
name|ServerAccessToken
name|doCreateAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|UserSubject
name|subject
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
return|return
name|doCreateAccessToken
argument_list|(
name|client
argument_list|,
name|subject
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
argument_list|,
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_AUDIENCE
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|ServerAccessToken
name|doCreateAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|UserSubject
name|subject
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
parameter_list|)
block|{
return|return
name|doCreateAccessToken
argument_list|(
name|client
argument_list|,
name|subject
argument_list|,
name|getSingleGrantType
argument_list|()
argument_list|,
name|requestedScope
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|protected
name|ServerAccessToken
name|doCreateAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|UserSubject
name|subject
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
parameter_list|,
name|String
name|audience
parameter_list|)
block|{
return|return
name|doCreateAccessToken
argument_list|(
name|client
argument_list|,
name|subject
argument_list|,
name|getSingleGrantType
argument_list|()
argument_list|,
name|requestedScope
argument_list|,
name|audience
argument_list|)
return|;
block|}
specifier|protected
name|ServerAccessToken
name|doCreateAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|UserSubject
name|subject
parameter_list|,
name|String
name|requestedGrant
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
parameter_list|)
block|{
return|return
name|doCreateAccessToken
argument_list|(
name|client
argument_list|,
name|subject
argument_list|,
name|requestedGrant
argument_list|,
name|requestedScope
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|protected
name|ServerAccessToken
name|doCreateAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|UserSubject
name|subject
parameter_list|,
name|String
name|requestedGrant
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
parameter_list|,
name|String
name|audience
parameter_list|)
block|{
if|if
condition|(
operator|!
name|OAuthUtils
operator|.
name|validateScopes
argument_list|(
name|requestedScope
argument_list|,
name|client
operator|.
name|getRegisteredScopes
argument_list|()
argument_list|,
name|partialMatchScopeValidation
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
operator|new
name|OAuthError
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_SCOPE
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|OAuthUtils
operator|.
name|validateAudience
argument_list|(
name|audience
argument_list|,
name|client
operator|.
name|getRegisteredAudiences
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
operator|new
name|OAuthError
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_GRANT
argument_list|)
argument_list|)
throw|;
block|}
comment|// Check if a pre-authorized  token available
name|ServerAccessToken
name|token
init|=
name|dataProvider
operator|.
name|getPreauthorizedToken
argument_list|(
name|client
argument_list|,
name|requestedScope
argument_list|,
name|subject
argument_list|,
name|requestedGrant
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
operator|!=
literal|null
condition|)
block|{
return|return
name|token
return|;
block|}
comment|// Delegate to the data provider to create the one
name|AccessTokenRegistration
name|reg
init|=
operator|new
name|AccessTokenRegistration
argument_list|()
decl_stmt|;
name|reg
operator|.
name|setClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|reg
operator|.
name|setGrantType
argument_list|(
name|requestedGrant
argument_list|)
expr_stmt|;
name|reg
operator|.
name|setSubject
argument_list|(
name|subject
argument_list|)
expr_stmt|;
name|reg
operator|.
name|setRequestedScope
argument_list|(
name|requestedScope
argument_list|)
expr_stmt|;
name|reg
operator|.
name|setAudience
argument_list|(
name|audience
argument_list|)
expr_stmt|;
return|return
name|dataProvider
operator|.
name|createAccessToken
argument_list|(
name|reg
argument_list|)
return|;
block|}
specifier|public
name|void
name|setPartialMatchScopeValidation
parameter_list|(
name|boolean
name|partialMatchScopeValidation
parameter_list|)
block|{
name|this
operator|.
name|partialMatchScopeValidation
operator|=
name|partialMatchScopeValidation
expr_stmt|;
block|}
specifier|public
name|void
name|setCanSupportPublicClients
parameter_list|(
name|boolean
name|support
parameter_list|)
block|{
name|canSupportPublicClients
operator|=
name|support
expr_stmt|;
block|}
specifier|public
name|boolean
name|isCanSupportPublicClients
parameter_list|()
block|{
return|return
name|canSupportPublicClients
return|;
block|}
block|}
end_class

end_unit

