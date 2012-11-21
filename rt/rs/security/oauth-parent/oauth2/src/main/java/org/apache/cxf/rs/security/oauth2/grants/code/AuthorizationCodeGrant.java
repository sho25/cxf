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
name|net
operator|.
name|URI
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
name|AccessTokenGrant
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

begin_comment
comment|/**  * Base Authorization Code Grant representation, captures the code   * and the redirect URI this code has been returned to, visible to the client  */
end_comment

begin_class
specifier|public
class|class
name|AuthorizationCodeGrant
implements|implements
name|AccessTokenGrant
block|{
specifier|private
name|String
name|code
decl_stmt|;
specifier|private
name|String
name|redirectUri
decl_stmt|;
specifier|public
name|AuthorizationCodeGrant
parameter_list|(
name|String
name|code
parameter_list|)
block|{
name|this
operator|.
name|code
operator|=
name|code
expr_stmt|;
block|}
specifier|public
name|AuthorizationCodeGrant
parameter_list|(
name|String
name|code
parameter_list|,
name|URI
name|uri
parameter_list|)
block|{
name|this
operator|.
name|code
operator|=
name|code
expr_stmt|;
name|redirectUri
operator|=
name|uri
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
comment|/**      * Sets the redirect URI, if set then the client is expected to       * include the same URI during the access token request      * @param redirectUri redirect URI      */
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
comment|/**      * Gets the authorization code      * @return the code      */
specifier|public
name|String
name|getCode
parameter_list|()
block|{
return|return
name|code
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|OAuthConstants
operator|.
name|AUTHORIZATION_CODE_GRANT
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|toMap
parameter_list|()
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
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
name|map
operator|.
name|putSingle
argument_list|(
name|OAuthConstants
operator|.
name|GRANT_TYPE
argument_list|,
name|OAuthConstants
operator|.
name|AUTHORIZATION_CODE_GRANT
argument_list|)
expr_stmt|;
name|map
operator|.
name|putSingle
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_CODE_VALUE
argument_list|,
name|code
argument_list|)
expr_stmt|;
if|if
condition|(
name|redirectUri
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|putSingle
argument_list|(
name|OAuthConstants
operator|.
name|REDIRECT_URI
argument_list|,
name|redirectUri
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
block|}
end_class

end_unit

