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
name|LinkedHashMap
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

begin_comment
comment|/**  * Captures the information associated with the code grant registration request.  * @see ServerAuthorizationCodeGrant  */
end_comment

begin_class
specifier|public
class|class
name|AuthorizationCodeRegistration
block|{
specifier|private
name|Client
name|client
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
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
name|approvedScope
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
specifier|private
name|String
name|redirectUri
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
name|responseType
decl_stmt|;
specifier|private
name|String
name|clientCodeChallenge
decl_stmt|;
specifier|private
name|boolean
name|preauthorizedTokenAvailable
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraProperties
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * Sets the {@link Client} reference      * @param client the client      */
specifier|public
name|void
name|setClient
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
comment|/**      * Gets {@link Client} reference      * @return the client      */
specifier|public
name|Client
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
comment|/**      * Sets the redirect URI      * @param redirectUri the redirect URI      */
specifier|public
name|void
name|setRedirectUri
parameter_list|(
name|String
name|redirectUri
parameter_list|)
block|{
name|this
operator|.
name|redirectUri
operator|=
name|redirectUri
expr_stmt|;
block|}
comment|/**      * Gets the redirect URI      * @return the redirect URI      */
specifier|public
name|String
name|getRedirectUri
parameter_list|()
block|{
return|return
name|redirectUri
return|;
block|}
comment|/**      * Sets the scopes request by the client      * @param requestedScope the requested scopes      */
specifier|public
name|void
name|setRequestedScope
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
parameter_list|)
block|{
name|this
operator|.
name|requestedScope
operator|=
name|requestedScope
expr_stmt|;
block|}
comment|/**      * Gets the scopes request by the client      * @return the requested scopes      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRequestedScope
parameter_list|()
block|{
return|return
name|requestedScope
return|;
block|}
comment|/**      * Sets the scopes explicitly approved by the end user.      * If this list is empty then the end user had no way to down-scope.      * @param approvedScope the approved scopes      */
specifier|public
name|void
name|setApprovedScope
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|approvedScope
parameter_list|)
block|{
name|this
operator|.
name|approvedScope
operator|=
name|approvedScope
expr_stmt|;
block|}
comment|/**      * Gets the scopes explicitly approved by the end user      * @return the approved scopes      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getApprovedScope
parameter_list|()
block|{
return|return
name|approvedScope
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
specifier|public
name|boolean
name|isPreauthorizedTokenAvailable
parameter_list|()
block|{
return|return
name|preauthorizedTokenAvailable
return|;
block|}
specifier|public
name|void
name|setPreauthorizedTokenAvailable
parameter_list|(
name|boolean
name|preauthorizedTokenAvailable
parameter_list|)
block|{
name|this
operator|.
name|preauthorizedTokenAvailable
operator|=
name|preauthorizedTokenAvailable
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getExtraProperties
parameter_list|()
block|{
return|return
name|extraProperties
return|;
block|}
specifier|public
name|void
name|setExtraProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraProperties
parameter_list|)
block|{
name|this
operator|.
name|extraProperties
operator|=
name|extraProperties
expr_stmt|;
block|}
specifier|public
name|String
name|getResponseType
parameter_list|()
block|{
return|return
name|responseType
return|;
block|}
specifier|public
name|void
name|setResponseType
parameter_list|(
name|String
name|responseType
parameter_list|)
block|{
name|this
operator|.
name|responseType
operator|=
name|responseType
expr_stmt|;
block|}
block|}
end_class

end_unit

