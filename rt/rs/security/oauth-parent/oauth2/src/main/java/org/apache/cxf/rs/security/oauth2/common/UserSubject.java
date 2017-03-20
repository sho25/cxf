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
name|FetchType
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
name|persistence
operator|.
name|MapKeyColumn
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|OrderColumn
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|Base64UrlUtility
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
name|rt
operator|.
name|security
operator|.
name|crypto
operator|.
name|CryptoUtils
import|;
end_import

begin_comment
comment|/**  * Represents a login name which AuthorizationService  * may capture after the end user approved a given third party request  */
end_comment

begin_class
annotation|@
name|XmlRootElement
annotation|@
name|Entity
specifier|public
class|class
name|UserSubject
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
literal|1469694589163385689L
decl_stmt|;
specifier|private
name|String
name|login
decl_stmt|;
specifier|private
name|String
name|id
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|roles
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
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|AuthenticationMethod
name|am
decl_stmt|;
specifier|public
name|UserSubject
parameter_list|()
block|{
name|this
operator|.
name|id
operator|=
name|newId
argument_list|()
expr_stmt|;
block|}
specifier|public
name|UserSubject
parameter_list|(
name|String
name|login
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|login
operator|=
name|login
expr_stmt|;
block|}
specifier|public
name|UserSubject
parameter_list|(
name|String
name|login
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|roles
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|login
operator|=
name|login
expr_stmt|;
name|this
operator|.
name|roles
operator|=
name|roles
expr_stmt|;
block|}
specifier|public
name|UserSubject
parameter_list|(
name|String
name|login
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|login
operator|=
name|login
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
operator|!=
literal|null
condition|?
name|id
else|:
name|newId
argument_list|()
expr_stmt|;
block|}
specifier|public
name|UserSubject
parameter_list|(
name|String
name|login
parameter_list|,
name|String
name|id
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|roles
parameter_list|)
block|{
name|this
operator|.
name|login
operator|=
name|login
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
operator|!=
literal|null
condition|?
name|id
else|:
name|newId
argument_list|()
expr_stmt|;
name|this
operator|.
name|roles
operator|=
name|roles
expr_stmt|;
block|}
specifier|public
name|UserSubject
parameter_list|(
name|UserSubject
name|sub
parameter_list|)
block|{
name|this
argument_list|(
name|sub
operator|.
name|getLogin
argument_list|()
argument_list|,
name|sub
operator|.
name|getId
argument_list|()
argument_list|,
name|sub
operator|.
name|getRoles
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|properties
operator|=
name|sub
operator|.
name|getProperties
argument_list|()
expr_stmt|;
name|this
operator|.
name|am
operator|=
name|sub
operator|.
name|getAuthenticationMethod
argument_list|()
expr_stmt|;
block|}
specifier|private
name|String
name|newId
parameter_list|()
block|{
return|return
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|CryptoUtils
operator|.
name|generateSecureRandomBytes
argument_list|(
literal|16
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Return the user login name      *      * @return the login name      */
specifier|public
name|String
name|getLogin
parameter_list|()
block|{
return|return
name|login
return|;
block|}
comment|/**      * Set the user login name      *      * @param login the login name      */
specifier|public
name|void
name|setLogin
parameter_list|(
name|String
name|login
parameter_list|)
block|{
name|this
operator|.
name|login
operator|=
name|login
expr_stmt|;
block|}
comment|/**      * Return the optional list of user roles which may have      * been captured during the authentication process      *      * @return the list of roles      */
annotation|@
name|ElementCollection
argument_list|(
name|fetch
operator|=
name|FetchType
operator|.
name|EAGER
argument_list|)
annotation|@
name|OrderColumn
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRoles
parameter_list|()
block|{
return|return
name|roles
return|;
block|}
comment|/**      * Set the optional list of user roles which may have      * been captured during the authentication process      *      * @param roles the list of roles      */
specifier|public
name|void
name|setRoles
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|roles
parameter_list|)
block|{
name|this
operator|.
name|roles
operator|=
name|roles
expr_stmt|;
block|}
comment|/**      * Get the list of additional user subject properties      *      * @return the list of properties      */
annotation|@
name|ElementCollection
argument_list|(
name|fetch
operator|=
name|FetchType
operator|.
name|EAGER
argument_list|)
annotation|@
name|MapKeyColumn
argument_list|(
name|name
operator|=
literal|"name"
argument_list|)
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
comment|/**      * Set the list of additional user subject properties      *      * @param properties the properties      */
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
comment|/**      * Get the user's unique id      *      * @return the user's id      */
annotation|@
name|Id
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|this
operator|.
name|id
return|;
block|}
comment|/**      * Set the users unique id      *      * @param id the user's id      */
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|AuthenticationMethod
name|getAuthenticationMethod
parameter_list|()
block|{
return|return
name|am
return|;
block|}
specifier|public
name|void
name|setAuthenticationMethod
parameter_list|(
name|AuthenticationMethod
name|method
parameter_list|)
block|{
name|this
operator|.
name|am
operator|=
name|method
expr_stmt|;
block|}
block|}
end_class

end_unit

