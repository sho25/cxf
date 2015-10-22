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
name|services
operator|.
name|AuthorizationCodeGrantService
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/login"
argument_list|)
specifier|public
class|class
name|OidcAuthorizationCodeService
extends|extends
name|AuthorizationCodeGrantService
block|{
specifier|private
specifier|static
specifier|final
name|String
name|OPEN_ID_CONNECT_SCOPE
init|=
literal|"openid"
decl_stmt|;
specifier|private
name|boolean
name|skipAuthorizationWithOidcScope
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|boolean
name|canAuthorizationBeSkipped
parameter_list|(
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
name|OAuthPermission
argument_list|>
name|permissions
parameter_list|)
block|{
comment|// No need to challenge the authenticated user with the authorization form
comment|// if all the client application redirecting a user needs is to get this user authenticated
comment|// with OIDC IDP
return|return
name|requestedScope
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|skipAuthorizationWithOidcScope
operator|&&
name|OPEN_ID_CONNECT_SCOPE
operator|.
name|equals
argument_list|(
name|requestedScope
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|setSkipAuthorizationWithOidcScope
parameter_list|(
name|boolean
name|skipAuthorizationWithOidcScope
parameter_list|)
block|{
name|this
operator|.
name|skipAuthorizationWithOidcScope
operator|=
name|skipAuthorizationWithOidcScope
expr_stmt|;
block|}
block|}
end_class

end_unit

