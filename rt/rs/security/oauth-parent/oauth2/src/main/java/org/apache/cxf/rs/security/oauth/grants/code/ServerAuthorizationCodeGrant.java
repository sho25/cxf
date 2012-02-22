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
name|oauth
operator|.
name|common
operator|.
name|UserSubject
import|;
end_import

begin_comment
comment|/**  * Authorization Code Token representation  */
end_comment

begin_class
specifier|public
class|class
name|ServerAuthorizationCodeGrant
extends|extends
name|AuthorizationCodeGrant
block|{
specifier|private
name|long
name|issuedAt
decl_stmt|;
specifier|private
name|long
name|lifetime
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
name|approvedScopes
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
specifier|private
name|UserSubject
name|subject
decl_stmt|;
specifier|public
name|ServerAuthorizationCodeGrant
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|code
parameter_list|,
name|long
name|lifetime
parameter_list|,
name|long
name|issuedAt
parameter_list|)
block|{
name|super
argument_list|(
name|code
argument_list|)
expr_stmt|;
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
name|this
operator|.
name|lifetime
operator|=
name|lifetime
expr_stmt|;
name|this
operator|.
name|issuedAt
operator|=
name|issuedAt
expr_stmt|;
block|}
specifier|public
name|long
name|getIssuedAt
parameter_list|()
block|{
return|return
name|issuedAt
return|;
block|}
specifier|public
name|long
name|getLifetime
parameter_list|()
block|{
return|return
name|lifetime
return|;
block|}
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
name|setApprovedScopes
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
name|approvedScopes
operator|=
name|scopes
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|getApprovedScopes
parameter_list|()
block|{
return|return
name|approvedScopes
return|;
block|}
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
specifier|public
name|UserSubject
name|getSubject
parameter_list|()
block|{
return|return
name|subject
return|;
block|}
block|}
end_class

end_unit

