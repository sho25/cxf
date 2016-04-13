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
name|persistence
operator|.
name|ElementCollection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Entity
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Id
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
comment|/**  * Provides the complete information about a given opaque permission.  * For example, a scope parameter such as "read_calendar" will be  * translated into the instance of this class in order to provide  * the human readable description and optionally restrict it to  * a limited set of HTTP verbs and request URIs  */
end_comment

begin_class
annotation|@
name|XmlRootElement
annotation|@
name|Entity
specifier|public
class|class
name|OAuthPermission
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
literal|6486616235830491290L
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|httpVerbs
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
name|uris
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|permission
decl_stmt|;
specifier|private
name|String
name|description
decl_stmt|;
specifier|private
name|boolean
name|isDefault
decl_stmt|;
specifier|private
name|boolean
name|invisibleToClient
decl_stmt|;
specifier|public
name|OAuthPermission
parameter_list|()
block|{              }
specifier|public
name|OAuthPermission
parameter_list|(
name|String
name|permission
parameter_list|)
block|{
name|this
operator|.
name|permission
operator|=
name|permission
expr_stmt|;
block|}
specifier|public
name|OAuthPermission
parameter_list|(
name|String
name|permission
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
name|this
operator|.
name|permission
operator|=
name|permission
expr_stmt|;
block|}
comment|/**      * Sets the optional list of HTTP verbs, example,      * "GET" and "POST", etc      * @param httpVerbs the list of HTTP verbs      */
specifier|public
name|void
name|setHttpVerbs
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|httpVerbs
parameter_list|)
block|{
name|this
operator|.
name|httpVerbs
operator|=
name|httpVerbs
expr_stmt|;
block|}
comment|/**      * Gets the optional list of HTTP verbs      * @return the list of HTTP verbs      */
annotation|@
name|ElementCollection
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getHttpVerbs
parameter_list|()
block|{
return|return
name|httpVerbs
return|;
block|}
comment|/**      * Sets the optional list of relative request URIs      * @param uri the list of URIs      */
specifier|public
name|void
name|setUris
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uris
operator|=
name|uri
expr_stmt|;
block|}
comment|/**      * Gets the optional list of relative request URIs      * @return the list of URIs      */
annotation|@
name|ElementCollection
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getUris
parameter_list|()
block|{
return|return
name|uris
return|;
block|}
comment|/**      * Gets the permission description      * @return the description      */
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
comment|/**      * Sets the permission description      * @param description      */
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
comment|/**      * Get the permission value such as "read_calendar"      * @return the value      */
annotation|@
name|Id
specifier|public
name|String
name|getPermission
parameter_list|()
block|{
return|return
name|permission
return|;
block|}
comment|/**      * Sets the permission value such as "read_calendar"      * @param permission the permission value      */
specifier|public
name|void
name|setPermission
parameter_list|(
name|String
name|permission
parameter_list|)
block|{
name|this
operator|.
name|permission
operator|=
name|permission
expr_stmt|;
block|}
comment|/**      * Indicates if this permission has been allocated by default or not.      * Authorization View handlers may use this property to optimize the way the user selects the      * scopes.      * For example, assume that read', 'add' and 'update' scopes are supported and the       * 'read' scope is always allocated. This can be presented at the UI level as follows:      * the read-only check-box control will represent a 'read' scope and a user will be able to      * optionally select 'add' and/or 'update' scopes, in addition to the default 'read' one.       * @param isDefault true if the permission has been allocated by default      */
specifier|public
name|void
name|setDefault
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|this
operator|.
name|isDefault
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|boolean
name|isDefault
parameter_list|()
block|{
return|return
name|isDefault
return|;
block|}
specifier|public
name|boolean
name|isInvisibleToClient
parameter_list|()
block|{
return|return
name|invisibleToClient
return|;
block|}
comment|/**      * Set the visibility status; by default all the scopes approved by a user can       * be optionally reported to the client in access token responses. Some scopes may need      * to stay 'invisible' to client.      * @param invisibleToClient      */
specifier|public
name|void
name|setInvisibleToClient
parameter_list|(
name|boolean
name|invisibleToClient
parameter_list|)
block|{
name|this
operator|.
name|invisibleToClient
operator|=
name|invisibleToClient
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|OAuthPermission
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|OAuthPermission
name|that
init|=
operator|(
name|OAuthPermission
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|getHttpVerbs
argument_list|()
operator|!=
literal|null
operator|&&
name|that
operator|.
name|getHttpVerbs
argument_list|()
operator|==
literal|null
operator|||
name|getHttpVerbs
argument_list|()
operator|==
literal|null
operator|&&
name|that
operator|.
name|getHttpVerbs
argument_list|()
operator|!=
literal|null
operator|||
name|getHttpVerbs
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|getHttpVerbs
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|getHttpVerbs
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getUris
argument_list|()
operator|!=
literal|null
operator|&&
name|that
operator|.
name|getUris
argument_list|()
operator|==
literal|null
operator|||
name|getUris
argument_list|()
operator|==
literal|null
operator|&&
name|that
operator|.
name|getUris
argument_list|()
operator|!=
literal|null
operator|||
name|getUris
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|getUris
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|getUris
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|getPermission
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|getPermission
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getDescription
argument_list|()
operator|!=
literal|null
operator|&&
name|that
operator|.
name|getDescription
argument_list|()
operator|==
literal|null
operator|||
name|getDescription
argument_list|()
operator|==
literal|null
operator|&&
name|that
operator|.
name|getDescription
argument_list|()
operator|!=
literal|null
operator|||
name|getDescription
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|getDescription
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|getDescription
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|isInvisibleToClient
argument_list|()
operator|!=
name|that
operator|.
name|isInvisibleToClient
argument_list|()
operator|||
name|isDefault
argument_list|()
operator|!=
name|that
operator|.
name|isDefault
argument_list|()
condition|)
block|{
comment|//NOPMD
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|hashCode
init|=
literal|17
decl_stmt|;
if|if
condition|(
name|getHttpVerbs
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|hashCode
operator|=
literal|31
operator|*
name|hashCode
operator|+
name|getHttpVerbs
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|getUris
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|hashCode
operator|=
literal|31
operator|*
name|hashCode
operator|+
name|getUris
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
name|hashCode
operator|=
literal|31
operator|*
name|hashCode
operator|+
name|getPermission
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
if|if
condition|(
name|getDescription
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|hashCode
operator|=
literal|31
operator|*
name|hashCode
operator|+
name|getDescription
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
name|hashCode
operator|=
literal|31
operator|*
name|hashCode
operator|+
name|Boolean
operator|.
name|hashCode
argument_list|(
name|isInvisibleToClient
argument_list|()
argument_list|)
expr_stmt|;
name|hashCode
operator|=
literal|31
operator|*
name|hashCode
operator|+
name|Boolean
operator|.
name|hashCode
argument_list|(
name|isDefault
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|hashCode
return|;
block|}
block|}
end_class

end_unit

