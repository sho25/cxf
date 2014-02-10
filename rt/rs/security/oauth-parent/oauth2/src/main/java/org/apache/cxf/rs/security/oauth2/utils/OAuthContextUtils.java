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
name|utils
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
name|Response
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
name|Response
operator|.
name|Status
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
name|ext
operator|.
name|MessageContext
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
name|utils
operator|.
name|ExceptionUtils
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
name|OAuthContext
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

begin_class
specifier|public
specifier|final
class|class
name|OAuthContextUtils
block|{
specifier|private
name|OAuthContextUtils
parameter_list|()
block|{     }
comment|/**      * @param mc the {@link MessageContext}      * @return the id of the UserSubject of the logged in user or resource owner      * @throws WebApplicationException with Status 401 if not authenticated      */
specifier|public
specifier|static
name|String
name|resolveUserId
parameter_list|(
specifier|final
name|MessageContext
name|mc
parameter_list|)
block|{
specifier|final
name|OAuthContext
name|oauth
init|=
name|getContext
argument_list|(
name|mc
argument_list|)
decl_stmt|;
return|return
name|oauth
operator|.
name|getSubject
argument_list|()
operator|.
name|getId
argument_list|()
return|;
block|}
comment|/**      * @param mc the {@link MessageContext}      * @return the name of the UserSubject of the logged in user or resource owner      * @throws WebApplicationException with Status 401 if not authenticated      */
specifier|public
specifier|static
name|String
name|resolveUserName
parameter_list|(
specifier|final
name|MessageContext
name|mc
parameter_list|)
block|{
specifier|final
name|OAuthContext
name|oauth
init|=
name|getContext
argument_list|(
name|mc
argument_list|)
decl_stmt|;
return|return
name|oauth
operator|.
name|getSubject
argument_list|()
operator|.
name|getLogin
argument_list|()
return|;
block|}
comment|/**      * @param mc the {@link MessageContext}      * @return the list of roles of the logged in user or resource owner      * @throws WebApplicationException with Status 401 if not authenticated      */
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|resolveUserRoles
parameter_list|(
specifier|final
name|MessageContext
name|mc
parameter_list|)
block|{
specifier|final
name|OAuthContext
name|oauth
init|=
name|getContext
argument_list|(
name|mc
argument_list|)
decl_stmt|;
return|return
name|oauth
operator|.
name|getSubject
argument_list|()
operator|.
name|getRoles
argument_list|()
return|;
block|}
comment|/**      * @param mc the {@link MessageContext}      * @param role the user role to check      * @return true if user has given role; false otherwise      * @throws WebApplicationException with Status 401 if not authenticated      */
specifier|public
specifier|static
name|boolean
name|isUserInRole
parameter_list|(
specifier|final
name|MessageContext
name|mc
parameter_list|,
specifier|final
name|String
name|role
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|userroles
init|=
name|resolveUserRoles
argument_list|(
name|mc
argument_list|)
decl_stmt|;
return|return
name|userroles
operator|.
name|contains
argument_list|(
name|role
argument_list|)
return|;
block|}
comment|/**      * @param mc the {@link MessageContext}      * @param role the role to check      * @throws WebApplicationException with Status 401 if not authenticated      * @throws WebApplicationException with Status 403 if user doesn't have needed role      */
specifier|public
specifier|static
name|void
name|assertRole
parameter_list|(
specifier|final
name|MessageContext
name|mc
parameter_list|,
specifier|final
name|String
name|role
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isUserInRole
argument_list|(
name|mc
argument_list|,
name|role
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|Status
operator|.
name|FORBIDDEN
argument_list|)
throw|;
block|}
block|}
comment|/**      * @param mc the {@link MessageContext}      * @return the list of permissions of the used access token      * @throws WebApplicationException with Status 401 if not authenticated      */
specifier|public
specifier|static
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|resolvePermissions
parameter_list|(
specifier|final
name|MessageContext
name|mc
parameter_list|)
block|{
specifier|final
name|OAuthContext
name|oauth
init|=
name|getContext
argument_list|(
name|mc
argument_list|)
decl_stmt|;
return|return
name|oauth
operator|.
name|getPermissions
argument_list|()
return|;
block|}
comment|/**      * @param mc the {@link MessageContext}      * @return the token key used to access      * @throws WebApplicationException with Status 401 if not authenticated      */
specifier|public
specifier|static
name|String
name|resolveTokenKey
parameter_list|(
name|MessageContext
name|mc
parameter_list|)
block|{
name|OAuthContext
name|oauth
init|=
name|getContext
argument_list|(
name|mc
argument_list|)
decl_stmt|;
return|return
name|oauth
operator|.
name|getTokenKey
argument_list|()
return|;
block|}
comment|/**      * @param mc the {@link MessageContext}      * @return the client registration id      * @throws WebApplicationException with Status 401 if not authenticated      */
specifier|public
specifier|static
name|String
name|resolveClient
parameter_list|(
name|MessageContext
name|mc
parameter_list|)
block|{
name|OAuthContext
name|oauth
init|=
name|getContext
argument_list|(
name|mc
argument_list|)
decl_stmt|;
return|return
name|oauth
operator|.
name|getClientId
argument_list|()
return|;
block|}
comment|/**      * @param mc the {@link MessageContext}      * @param client the desired client registration id      * @throws WebApplicationException with Status 403 if the current client id is not valid      */
specifier|public
specifier|static
name|void
name|assertClient
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|String
name|client
parameter_list|)
block|{
name|String
name|cl
init|=
name|resolveClient
argument_list|(
name|mc
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|cl
operator|==
literal|null
operator|)
operator|||
operator|!
name|cl
operator|.
name|equals
argument_list|(
name|client
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|Status
operator|.
name|FORBIDDEN
argument_list|)
throw|;
block|}
block|}
comment|/**      * @param mc the {@link MessageContext}      * @return the {@link OAuthContext} of the given {@link MessageContext}      * @throws WebApplicationException with Status 401 if not authenticated      */
specifier|public
specifier|static
name|OAuthContext
name|getContext
parameter_list|(
specifier|final
name|MessageContext
name|mc
parameter_list|)
block|{
specifier|final
name|OAuthContext
name|oauth
init|=
name|mc
operator|.
name|getContent
argument_list|(
name|OAuthContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|oauth
operator|==
literal|null
operator|)
operator|||
operator|(
name|oauth
operator|.
name|getSubject
argument_list|()
operator|==
literal|null
operator|)
operator|||
operator|(
name|oauth
operator|.
name|getSubject
argument_list|()
operator|.
name|getLogin
argument_list|()
operator|==
literal|null
operator|)
condition|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
name|Response
operator|.
name|status
argument_list|(
literal|401
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|oauth
return|;
block|}
block|}
end_class

end_unit

