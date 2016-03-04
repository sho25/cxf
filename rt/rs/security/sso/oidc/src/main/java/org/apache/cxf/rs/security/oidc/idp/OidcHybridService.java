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
name|idp
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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|Path
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
name|OAuthRedirectionState
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
name|utils
operator|.
name|OAuthConstants
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"authorize-hybrid"
argument_list|)
specifier|public
class|class
name|OidcHybridService
extends|extends
name|OidcImplicitService
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CODE_AT_RESPONSE_TYPE
init|=
literal|"code token"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CODE_ID_TOKEN_RESPONSE_TYPE
init|=
literal|"code id_token"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CODE_ID_TOKEN_AT_RESPONSE_TYPE
init|=
literal|"code id_token token"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|IMPLICIT_RESPONSE_TYPES
decl_stmt|;
static|static
block|{
name|IMPLICIT_RESPONSE_TYPES
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|IMPLICIT_RESPONSE_TYPES
operator|.
name|put
argument_list|(
name|CODE_AT_RESPONSE_TYPE
argument_list|,
name|OAuthConstants
operator|.
name|TOKEN_RESPONSE_TYPE
argument_list|)
expr_stmt|;
name|IMPLICIT_RESPONSE_TYPES
operator|.
name|put
argument_list|(
name|CODE_ID_TOKEN_RESPONSE_TYPE
argument_list|,
name|ID_TOKEN_RESPONSE_TYPE
argument_list|)
expr_stmt|;
name|IMPLICIT_RESPONSE_TYPES
operator|.
name|put
argument_list|(
name|CODE_ID_TOKEN_AT_RESPONSE_TYPE
argument_list|,
name|ID_TOKEN_AT_RESPONSE_TYPE
argument_list|)
expr_stmt|;
name|IMPLICIT_RESPONSE_TYPES
operator|.
name|put
argument_list|(
name|ID_TOKEN_RESPONSE_TYPE
argument_list|,
name|ID_TOKEN_RESPONSE_TYPE
argument_list|)
expr_stmt|;
name|IMPLICIT_RESPONSE_TYPES
operator|.
name|put
argument_list|(
name|ID_TOKEN_AT_RESPONSE_TYPE
argument_list|,
name|ID_TOKEN_AT_RESPONSE_TYPE
argument_list|)
expr_stmt|;
block|}
specifier|private
name|OidcAuthorizationCodeService
name|codeService
decl_stmt|;
specifier|public
name|OidcHybridService
parameter_list|()
block|{
name|this
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|OidcHybridService
parameter_list|(
name|boolean
name|hybridOnly
parameter_list|)
block|{
name|super
argument_list|(
name|getResponseTypes
argument_list|(
name|hybridOnly
argument_list|)
argument_list|,
literal|"Hybrid"
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|getResponseTypes
parameter_list|(
name|boolean
name|hybridOnly
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|types
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|CODE_AT_RESPONSE_TYPE
argument_list|,
name|CODE_ID_TOKEN_RESPONSE_TYPE
argument_list|,
name|CODE_ID_TOKEN_AT_RESPONSE_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|hybridOnly
condition|)
block|{
name|types
operator|.
name|add
argument_list|(
name|ID_TOKEN_RESPONSE_TYPE
argument_list|)
expr_stmt|;
name|types
operator|.
name|add
argument_list|(
name|ID_TOKEN_AT_RESPONSE_TYPE
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|types
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|canAccessTokenBeReturned
parameter_list|(
name|String
name|responseType
parameter_list|)
block|{
return|return
name|ID_TOKEN_AT_RESPONSE_TYPE
operator|.
name|equals
argument_list|(
name|responseType
argument_list|)
operator|||
name|CODE_AT_RESPONSE_TYPE
operator|.
name|equals
argument_list|(
name|responseType
argument_list|)
operator|||
name|CODE_ID_TOKEN_AT_RESPONSE_TYPE
operator|.
name|equals
argument_list|(
name|responseType
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|StringBuilder
name|prepareGrant
parameter_list|(
name|OAuthRedirectionState
name|state
parameter_list|,
name|Client
name|client
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|approvedScope
parameter_list|,
name|UserSubject
name|userSubject
parameter_list|,
name|ServerAccessToken
name|preAuthorizedToken
parameter_list|)
block|{
name|String
name|actualResponseType
init|=
name|state
operator|.
name|getResponseType
argument_list|()
decl_stmt|;
name|state
operator|.
name|setResponseType
argument_list|(
name|IMPLICIT_RESPONSE_TYPES
operator|.
name|get
argument_list|(
name|actualResponseType
argument_list|)
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb
init|=
name|super
operator|.
name|prepareGrant
argument_list|(
name|state
argument_list|,
name|client
argument_list|,
name|requestedScope
argument_list|,
name|approvedScope
argument_list|,
name|userSubject
argument_list|,
name|preAuthorizedToken
argument_list|)
decl_stmt|;
if|if
condition|(
name|actualResponseType
operator|.
name|startsWith
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_CODE_VALUE
argument_list|)
condition|)
block|{
name|state
operator|.
name|setResponseType
argument_list|(
name|OAuthConstants
operator|.
name|CODE_RESPONSE_TYPE
argument_list|)
expr_stmt|;
name|String
name|code
init|=
name|codeService
operator|.
name|getGrantCode
argument_list|(
name|state
argument_list|,
name|client
argument_list|,
name|requestedScope
argument_list|,
name|approvedScope
argument_list|,
name|userSubject
argument_list|,
name|preAuthorizedToken
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_CODE_VALUE
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|code
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
return|;
block|}
specifier|public
name|void
name|setCodeService
parameter_list|(
name|OidcAuthorizationCodeService
name|codeService
parameter_list|)
block|{
name|this
operator|.
name|codeService
operator|=
name|codeService
expr_stmt|;
block|}
block|}
end_class

end_unit

