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
name|io
operator|.
name|Serializable
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
comment|/**  * This bean represents a resource owner authorization challenge.  * Typically, an HTML view will be returned to a resource owner who  * will authorize or deny the third-party client  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"authorizationData"
argument_list|,
name|namespace
operator|=
literal|"http://org.apache.cxf.rs.security.oauth"
argument_list|)
specifier|public
class|class
name|OAuthAuthorizationData
implements|implements
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|7755998413495017637L
decl_stmt|;
specifier|private
name|String
name|clientId
decl_stmt|;
specifier|private
name|String
name|endUserName
decl_stmt|;
specifier|private
name|String
name|redirectUri
decl_stmt|;
specifier|private
name|String
name|state
decl_stmt|;
specifier|private
name|String
name|proposedScope
decl_stmt|;
specifier|private
name|String
name|authenticityToken
decl_stmt|;
specifier|private
name|String
name|replyTo
decl_stmt|;
specifier|private
name|String
name|applicationName
decl_stmt|;
specifier|private
name|String
name|applicationWebUri
decl_stmt|;
specifier|private
name|String
name|applicationDescription
decl_stmt|;
specifier|private
name|String
name|applicationLogoUri
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Property
argument_list|>
name|extraApplicationProperties
init|=
operator|new
name|LinkedList
argument_list|<
name|Property
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|?
extends|extends
name|Permission
argument_list|>
name|permissions
decl_stmt|;
specifier|public
name|OAuthAuthorizationData
parameter_list|()
block|{     }
comment|/**      * Sets the client application name      * @return application name      */
specifier|public
name|String
name|getApplicationName
parameter_list|()
block|{
return|return
name|applicationName
return|;
block|}
comment|/**      * Sets the client application name      * @param applicationName application name      */
specifier|public
name|void
name|setApplicationName
parameter_list|(
name|String
name|applicationName
parameter_list|)
block|{
name|this
operator|.
name|applicationName
operator|=
name|applicationName
expr_stmt|;
block|}
comment|/**      * Gets the list of scopes translated to {@link Permission} instances      * requested by the client application      * @return the list of scopes      */
specifier|public
name|List
argument_list|<
name|?
extends|extends
name|Permission
argument_list|>
name|getPermissions
parameter_list|()
block|{
return|return
name|permissions
return|;
block|}
comment|/**      * Gets the list of scopes translated to {@link Permission} instances      * @return the list of scopses      **/
specifier|public
name|void
name|setPermissions
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|Permission
argument_list|>
name|permissions
parameter_list|)
block|{
name|this
operator|.
name|permissions
operator|=
name|permissions
expr_stmt|;
block|}
comment|/**      * Sets the authenticity token linking the authorization       * challenge to the current end user session      *       * @param authenticityToken the session authenticity token       */
specifier|public
name|void
name|setAuthenticityToken
parameter_list|(
name|String
name|authenticityToken
parameter_list|)
block|{
name|this
operator|.
name|authenticityToken
operator|=
name|authenticityToken
expr_stmt|;
block|}
comment|/**      * Gets the authenticity token linking the authorization       * challenge to the current end user session      * @return the session authenticity token      */
specifier|public
name|String
name|getAuthenticityToken
parameter_list|()
block|{
return|return
name|authenticityToken
return|;
block|}
comment|/**      * Sets the application description      * @param applicationDescription the description      */
specifier|public
name|void
name|setApplicationDescription
parameter_list|(
name|String
name|applicationDescription
parameter_list|)
block|{
name|this
operator|.
name|applicationDescription
operator|=
name|applicationDescription
expr_stmt|;
block|}
comment|/**      * Gets the application description      * @return the description      */
specifier|public
name|String
name|getApplicationDescription
parameter_list|()
block|{
return|return
name|applicationDescription
return|;
block|}
comment|/**      * Sets the client id which needs to be retained in a hidden form field      * @param clientId the client id      */
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
comment|/**      * Gets the client id which needs to be retained in a hidden form field      * @return the client id      */
specifier|public
name|String
name|getClientId
parameter_list|()
block|{
return|return
name|clientId
return|;
block|}
comment|/**      * Sets the redirect uri which needs to be retained in a hidden form field      * @param redirectUri the redirect uri      */
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
comment|/**      * Gets the redirect uri which needs to be retained in a hidden form field      * @return the redirect uri      */
specifier|public
name|String
name|getRedirectUri
parameter_list|()
block|{
return|return
name|redirectUri
return|;
block|}
comment|/**      * Sets the client state token which needs to be retained in a hidden form field      * @param state the state      */
specifier|public
name|void
name|setState
parameter_list|(
name|String
name|state
parameter_list|)
block|{
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
block|}
comment|/**      * Gets the client state token which needs to be retained in a hidden form field      * @return      */
specifier|public
name|String
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
comment|/**      * Sets the application web URI      * @param applicationWebUri the application URI      */
specifier|public
name|void
name|setApplicationWebUri
parameter_list|(
name|String
name|applicationWebUri
parameter_list|)
block|{
name|this
operator|.
name|applicationWebUri
operator|=
name|applicationWebUri
expr_stmt|;
block|}
comment|/**      * Gets the application web URI      * @return the application URI      */
specifier|public
name|String
name|getApplicationWebUri
parameter_list|()
block|{
return|return
name|applicationWebUri
return|;
block|}
comment|/**      * Sets the application logo URI      * @param applicationLogoUri the logo URI      */
specifier|public
name|void
name|setApplicationLogoUri
parameter_list|(
name|String
name|applicationLogoUri
parameter_list|)
block|{
name|this
operator|.
name|applicationLogoUri
operator|=
name|applicationLogoUri
expr_stmt|;
block|}
comment|/**      * Gets the application logo URI      * @return the logo URI      */
specifier|public
name|String
name|getApplicationLogoUri
parameter_list|()
block|{
return|return
name|applicationLogoUri
return|;
block|}
comment|/**      * Sets the requested scope which needs to be retained in a hidden form field      * @param proposedScope the scope      */
specifier|public
name|void
name|setProposedScope
parameter_list|(
name|String
name|proposedScope
parameter_list|)
block|{
name|this
operator|.
name|proposedScope
operator|=
name|proposedScope
expr_stmt|;
block|}
comment|/**      * Gets the requested scope which needs to be retained in a hidden form field      * @return the scope      */
specifier|public
name|String
name|getProposedScope
parameter_list|()
block|{
return|return
name|proposedScope
return|;
block|}
comment|/**      * Sets the absolute URI where the authorization decision data       * will need to be sent to      * @param replyTo authorization decision handler URI      */
specifier|public
name|void
name|setReplyTo
parameter_list|(
name|String
name|replyTo
parameter_list|)
block|{
name|this
operator|.
name|replyTo
operator|=
name|replyTo
expr_stmt|;
block|}
comment|/**      * Gets the absolute URI where the authorization decision data       * will need to be sent to      * @return authorization decision handler URI      */
specifier|public
name|String
name|getReplyTo
parameter_list|()
block|{
return|return
name|replyTo
return|;
block|}
specifier|public
name|List
argument_list|<
name|Property
argument_list|>
name|getExtraApplicationProperties
parameter_list|()
block|{
return|return
name|extraApplicationProperties
return|;
block|}
specifier|public
name|void
name|setExtraApplicationProperties
parameter_list|(
name|List
argument_list|<
name|Property
argument_list|>
name|extraApplicationProperties
parameter_list|)
block|{
name|this
operator|.
name|extraApplicationProperties
operator|=
name|extraApplicationProperties
expr_stmt|;
block|}
specifier|public
name|String
name|getEndUserName
parameter_list|()
block|{
return|return
name|endUserName
return|;
block|}
specifier|public
name|void
name|setEndUserName
parameter_list|(
name|String
name|endUserName
parameter_list|)
block|{
name|this
operator|.
name|endUserName
operator|=
name|endUserName
expr_stmt|;
block|}
block|}
end_class

end_unit

