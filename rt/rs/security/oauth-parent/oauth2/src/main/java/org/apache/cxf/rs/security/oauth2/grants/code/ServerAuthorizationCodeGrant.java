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
name|OAuthUtils
import|;
end_import

begin_comment
comment|/**  * The Authorization Code Grant representation visible to the server  */
end_comment

begin_class
specifier|public
class|class
name|ServerAuthorizationCodeGrant
extends|extends
name|AuthorizationCodeGrant
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|5004608901535459036L
decl_stmt|;
specifier|private
name|long
name|issuedAt
decl_stmt|;
specifier|private
name|long
name|expiresIn
decl_stmt|;
specifier|private
name|Client
name|client
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|approvedScopes
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|requestedScopes
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
specifier|private
name|String
name|audience
decl_stmt|;
specifier|private
name|String
name|nonce
decl_stmt|;
specifier|private
name|String
name|clientCodeChallenge
decl_stmt|;
specifier|public
name|ServerAuthorizationCodeGrant
parameter_list|()
block|{              }
specifier|public
name|ServerAuthorizationCodeGrant
parameter_list|(
name|Client
name|client
parameter_list|,
name|long
name|lifetime
parameter_list|)
block|{
name|this
argument_list|(
name|client
argument_list|,
name|OAuthUtils
operator|.
name|generateRandomTokenKey
argument_list|()
argument_list|,
name|lifetime
argument_list|,
name|OAuthUtils
operator|.
name|getIssuedAt
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|expiresIn
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
name|expiresIn
operator|=
name|expiresIn
expr_stmt|;
name|this
operator|.
name|issuedAt
operator|=
name|issuedAt
expr_stmt|;
block|}
comment|/**      * Returns the time (in seconds) this grant was issued at      * @return the seconds      */
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
name|void
name|setIssuedAt
parameter_list|(
name|long
name|issuedAt
parameter_list|)
block|{
name|this
operator|.
name|issuedAt
operator|=
name|issuedAt
expr_stmt|;
block|}
comment|/**      * Returns the number of seconds this grant can be valid after it was issued      * @return the seconds this grant will be valid for      */
annotation|@
name|Deprecated
specifier|public
name|long
name|getLifetime
parameter_list|()
block|{
return|return
name|expiresIn
return|;
block|}
comment|/**      * Returns the number of seconds this grant can be valid after it was issued      * @return the seconds this grant will be valid for      */
specifier|public
name|long
name|getExpiresIn
parameter_list|()
block|{
return|return
name|expiresIn
return|;
block|}
specifier|public
name|void
name|setExpiresIn
parameter_list|(
name|long
name|expiresIn
parameter_list|)
block|{
name|this
operator|.
name|expiresIn
operator|=
name|expiresIn
expr_stmt|;
block|}
comment|/**      * Returns the reference to {@link Client}      * @return the client      */
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
comment|/**      * Sets the scopes explicitly approved by the end user.      * If this list is empty then the end user had no way to down-scope.       * @param approvedScope the approved scopes      */
specifier|public
name|void
name|setApprovedScopes
parameter_list|(
name|List
argument_list|<
name|String
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
comment|/**      * Gets the scopes explicitly approved by the end user      * @return the approved scopes      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getApprovedScopes
parameter_list|()
block|{
return|return
name|approvedScopes
return|;
block|}
comment|/**      * Sets the user subject representing the end user      * @param subject the subject      */
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
comment|/**      * Gets the user subject representing the end user      * @return the subject      */
specifier|public
name|UserSubject
name|getSubject
parameter_list|()
block|{
return|return
name|subject
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
specifier|public
name|String
name|getClientCodeChallenge
parameter_list|()
block|{
return|return
name|clientCodeChallenge
return|;
block|}
specifier|public
name|void
name|setClientCodeChallenge
parameter_list|(
name|String
name|clientCodeChallenge
parameter_list|)
block|{
name|this
operator|.
name|clientCodeChallenge
operator|=
name|clientCodeChallenge
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRequestedScopes
parameter_list|()
block|{
return|return
name|requestedScopes
return|;
block|}
specifier|public
name|void
name|setRequestedScopes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|requestedScopes
parameter_list|)
block|{
name|this
operator|.
name|requestedScopes
operator|=
name|requestedScopes
expr_stmt|;
block|}
specifier|public
name|String
name|getNonce
parameter_list|()
block|{
return|return
name|nonce
return|;
block|}
specifier|public
name|void
name|setNonce
parameter_list|(
name|String
name|nonce
parameter_list|)
block|{
name|this
operator|.
name|nonce
operator|=
name|nonce
expr_stmt|;
block|}
block|}
end_class

end_unit

