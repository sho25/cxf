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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
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
extends|extends
name|OAuthRedirectionState
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
name|endUserName
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
name|String
argument_list|>
name|applicationCertificates
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraApplicationProperties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|implicitFlow
decl_stmt|;
specifier|private
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|permissions
decl_stmt|;
specifier|private
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|alreadyAuthorizedPermissions
decl_stmt|;
specifier|private
name|boolean
name|hidePreauthorizedScopesInForm
decl_stmt|;
specifier|public
name|OAuthAuthorizationData
parameter_list|()
block|{     }
comment|/**      * Get the client application name      * @return application name      */
specifier|public
name|String
name|getApplicationName
parameter_list|()
block|{
return|return
name|applicationName
return|;
block|}
comment|/**      * Set the client application name      * @param applicationName application name      */
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
comment|/**      * Get the list of scopes translated to {@link Permission} instances      * requested by the client application      * @return the list of scopes      */
specifier|public
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|getPermissions
parameter_list|()
block|{
return|return
name|permissions
return|;
block|}
comment|/**      * Set the list of scopes translated to {@link OAuthPermission} instances      * @return the list of scopes      **/
specifier|public
name|void
name|setPermissions
parameter_list|(
name|List
argument_list|<
name|OAuthPermission
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
comment|/**       * Get the list of scopes already approved by a user      * @return the list of approved scopes      */
specifier|public
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|getAlreadyAuthorizedPermissions
parameter_list|()
block|{
return|return
name|alreadyAuthorizedPermissions
return|;
block|}
comment|/**      * Set the list of scopes already approved by a user      * @param permissions the list of approved scopes      */
specifier|public
name|void
name|setAlreadyAuthorizedPermissions
parameter_list|(
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|perms
parameter_list|)
block|{
name|this
operator|.
name|alreadyAuthorizedPermissions
operator|=
name|perms
expr_stmt|;
block|}
comment|/**      * Set the authenticity token linking the authorization       * challenge to the current end user session      *       * @param authenticityToken the session authenticity token       */
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
comment|/**      * Get the authenticity token linking the authorization       * challenge to the current end user session      * @return the session authenticity token      */
specifier|public
name|String
name|getAuthenticityToken
parameter_list|()
block|{
return|return
name|authenticityToken
return|;
block|}
comment|/**      * Set the application description      * @param applicationDescription the description      */
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
comment|/**      * Get the application description      * @return the description      */
specifier|public
name|String
name|getApplicationDescription
parameter_list|()
block|{
return|return
name|applicationDescription
return|;
block|}
comment|/**      * Set the application web URI      * @param applicationWebUri the application URI      */
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
comment|/**      * Get the application web URI      * @return the application URI      */
specifier|public
name|String
name|getApplicationWebUri
parameter_list|()
block|{
return|return
name|applicationWebUri
return|;
block|}
comment|/**      * Set the application logo URI      * @param applicationLogoUri the logo URI      */
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
comment|/**      * Get the application logo URI      * @return the logo URI      */
specifier|public
name|String
name|getApplicationLogoUri
parameter_list|()
block|{
return|return
name|applicationLogoUri
return|;
block|}
comment|/**      * Set the absolute URI where the authorization decision data       * will need to be sent to      * @param replyTo authorization decision handler URI      */
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
comment|/**      * Get the absolute URI where the authorization decision data       * will need to be sent to      * @return authorization decision handler URI      */
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
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
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getApplicationCertificates
parameter_list|()
block|{
return|return
name|applicationCertificates
return|;
block|}
specifier|public
name|void
name|setApplicationCertificates
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|applicationCertificates
parameter_list|)
block|{
name|this
operator|.
name|applicationCertificates
operator|=
name|applicationCertificates
expr_stmt|;
block|}
specifier|public
name|boolean
name|isImplicitFlow
parameter_list|()
block|{
return|return
name|implicitFlow
return|;
block|}
specifier|public
name|void
name|setImplicitFlow
parameter_list|(
name|boolean
name|implicitFlow
parameter_list|)
block|{
name|this
operator|.
name|implicitFlow
operator|=
name|implicitFlow
expr_stmt|;
block|}
specifier|public
name|boolean
name|isHidePreauthorizedScopesInForm
parameter_list|()
block|{
return|return
name|hidePreauthorizedScopesInForm
return|;
block|}
specifier|public
name|void
name|setHidePreauthorizedScopesInForm
parameter_list|(
name|boolean
name|hidePreauthorizedScopesInForm
parameter_list|)
block|{
name|this
operator|.
name|hidePreauthorizedScopesInForm
operator|=
name|hidePreauthorizedScopesInForm
expr_stmt|;
block|}
block|}
end_class

end_unit

