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

begin_comment
comment|/**  * Represents a registered third-party Client application  */
end_comment

begin_class
specifier|public
class|class
name|Client
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
literal|5550840247125850922L
decl_stmt|;
specifier|private
name|String
name|clientId
decl_stmt|;
specifier|private
name|ClientCredential
name|clientCred
decl_stmt|;
specifier|private
name|String
name|applicationName
decl_stmt|;
specifier|private
name|String
name|applicationDescription
decl_stmt|;
specifier|private
name|String
name|applicationWebUri
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
name|redirectUris
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|isConfidential
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|allowedGrantTypes
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|registeredScopes
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|registeredAudiences
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
name|properties
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
name|UserSubject
name|subject
decl_stmt|;
specifier|public
name|Client
parameter_list|()
block|{              }
specifier|public
name|Client
parameter_list|(
name|String
name|clientId
parameter_list|,
name|String
name|clientCred
parameter_list|,
name|boolean
name|isConfidential
parameter_list|)
block|{
name|this
operator|.
name|clientId
operator|=
name|clientId
expr_stmt|;
name|this
operator|.
name|clientCred
operator|=
name|clientCred
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|ClientCredential
argument_list|(
name|clientCred
argument_list|)
expr_stmt|;
name|this
operator|.
name|isConfidential
operator|=
name|isConfidential
expr_stmt|;
block|}
specifier|public
name|Client
parameter_list|(
name|String
name|clientId
parameter_list|,
name|String
name|clientCred
parameter_list|,
name|boolean
name|isConfidential
parameter_list|,
name|String
name|applicationName
parameter_list|,
name|String
name|applicationWebUri
parameter_list|)
block|{
name|this
argument_list|(
name|clientId
argument_list|,
name|clientCred
argument_list|,
name|isConfidential
argument_list|)
expr_stmt|;
name|this
operator|.
name|applicationName
operator|=
name|applicationName
expr_stmt|;
name|this
operator|.
name|applicationWebUri
operator|=
name|applicationWebUri
expr_stmt|;
block|}
specifier|public
name|Client
parameter_list|(
name|String
name|clientId
parameter_list|,
name|ClientCredential
name|clientCred
parameter_list|,
name|boolean
name|isConfidential
parameter_list|,
name|String
name|applicationName
parameter_list|,
name|String
name|applicationWebUri
parameter_list|)
block|{
name|this
operator|.
name|clientId
operator|=
name|clientId
expr_stmt|;
name|this
operator|.
name|clientCred
operator|=
name|clientCred
expr_stmt|;
name|this
operator|.
name|isConfidential
operator|=
name|isConfidential
expr_stmt|;
name|this
operator|.
name|applicationName
operator|=
name|applicationName
expr_stmt|;
name|this
operator|.
name|applicationWebUri
operator|=
name|applicationWebUri
expr_stmt|;
block|}
comment|/**      * Gets the client registration id      * @return the consumer key      */
specifier|public
name|String
name|getClientId
parameter_list|()
block|{
return|return
name|clientId
return|;
block|}
specifier|public
name|void
name|setClientId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|clientId
operator|=
name|id
expr_stmt|;
block|}
comment|/**      * Get the client credential.      * If it is a certificate or public key and not null then       * it has to be a Base64 encoded representation      * @return the credential      */
specifier|public
name|ClientCredential
name|getClientCredential
parameter_list|()
block|{
return|return
name|clientCred
return|;
block|}
specifier|public
name|void
name|setClientCredential
parameter_list|(
name|ClientCredential
name|cred
parameter_list|)
block|{
name|this
operator|.
name|clientCred
operator|=
name|cred
expr_stmt|;
block|}
comment|/**      * Gets the name of the third-party application      * this client represents      * @return the application name      */
specifier|public
name|String
name|getApplicationName
parameter_list|()
block|{
return|return
name|applicationName
return|;
block|}
comment|/**      * Sets the name of the third-party application      * this client represents      * @param applicationName the name      */
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
comment|/**      * Gets the public URI of the third-party application.      * @return the application URI      */
specifier|public
name|String
name|getApplicationWebUri
parameter_list|()
block|{
return|return
name|applicationWebUri
return|;
block|}
comment|/**      * Sets the public URI of the third-party application.      * @param applicationWebUri the application URI      */
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
comment|/**      * Sets the description of the third-party application.      * @param applicationDescription the description      */
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
comment|/**      * Gets the description of the third-party application.      * @return the application description      */
specifier|public
name|String
name|getApplicationDescription
parameter_list|()
block|{
return|return
name|applicationDescription
return|;
block|}
comment|/**      * Sets the URI pointing to a logo image of the client application      * @param logoPath the logo URI      */
specifier|public
name|void
name|setApplicationLogoUri
parameter_list|(
name|String
name|logoPath
parameter_list|)
block|{
name|this
operator|.
name|applicationLogoUri
operator|=
name|logoPath
expr_stmt|;
block|}
comment|/**      * Get the URI pointing to a logo image of the client application      * @return the logo URI      */
specifier|public
name|String
name|getApplicationLogoUri
parameter_list|()
block|{
return|return
name|applicationLogoUri
return|;
block|}
comment|/**      * Sets the confidentiality status of this client application.      * This can be used to restrict which OAuth2 flows this client      * can participate in.      *       * @param isConf true if the client is confidential      */
specifier|public
name|void
name|setConfidential
parameter_list|(
name|boolean
name|isConf
parameter_list|)
block|{
name|this
operator|.
name|isConfidential
operator|=
name|isConf
expr_stmt|;
block|}
comment|/**      * Gets the confidentiality status of this client application.      * @return the confidentiality status      */
specifier|public
name|boolean
name|isConfidential
parameter_list|()
block|{
return|return
name|isConfidential
return|;
block|}
comment|/**      * Sets a list of URIs the AuthorizationService      * may return the authorization code to.      * @param redirectUris the redirect uris      */
specifier|public
name|void
name|setRedirectUris
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|redirectUris
parameter_list|)
block|{
name|this
operator|.
name|redirectUris
operator|=
name|redirectUris
expr_stmt|;
block|}
comment|/**      * Gets a list of URIs the AuthorizationService      * may return the authorization code to      * @return the redirect uris      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRedirectUris
parameter_list|()
block|{
return|return
name|redirectUris
return|;
block|}
comment|/**      * Sets the list of access token grant types this client      * can use to obtain the access tokens.      * @param allowedGrantTypes the list of grant types      */
specifier|public
name|void
name|setAllowedGrantTypes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|allowedGrantTypes
parameter_list|)
block|{
name|this
operator|.
name|allowedGrantTypes
operator|=
name|allowedGrantTypes
expr_stmt|;
block|}
comment|/**      * Gets the list of access token grant types this client      * can use to obtain the access tokens.      * @return the list of grant types      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getAllowedGrantTypes
parameter_list|()
block|{
return|return
name|allowedGrantTypes
return|;
block|}
comment|/**      * Sets the {@link UserSubject} representing this Client       * authentication, may be setup during the registration.       *      * @param subject the user subject      */
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
comment|/**      * Gets the {@link UserSubject} representing this Client       * authentication      * @return the user subject      */
specifier|public
name|UserSubject
name|getSubject
parameter_list|()
block|{
return|return
name|subject
return|;
block|}
comment|/**      * Get the list of additional client properties      * @return the list of properties      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
comment|/**      * Set the list of additional client properties      * @param properties the properties      */
specifier|public
name|void
name|setProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
comment|/**      * Get the list of registered scopes      * @return scopes      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRegisteredScopes
parameter_list|()
block|{
return|return
name|registeredScopes
return|;
block|}
comment|/**      * Set the list of registered scopes.       * Registering the scopes will allow the clients not to include the scopes      * and delegate to the runtime to enforce that the current request scopes are      * a subset of the pre-registered scopes.      *       * Client Registration service is expected to reject unknown scopes.       * @param registeredScopes the scopes      */
specifier|public
name|void
name|setRegisteredScopes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|registeredScopes
parameter_list|)
block|{
name|this
operator|.
name|registeredScopes
operator|=
name|registeredScopes
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRegisteredAudiences
parameter_list|()
block|{
return|return
name|registeredAudiences
return|;
block|}
specifier|public
name|void
name|setRegisteredAudiences
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|registeredAudiences
parameter_list|)
block|{
name|this
operator|.
name|registeredAudiences
operator|=
name|registeredAudiences
expr_stmt|;
block|}
block|}
end_class

end_unit

