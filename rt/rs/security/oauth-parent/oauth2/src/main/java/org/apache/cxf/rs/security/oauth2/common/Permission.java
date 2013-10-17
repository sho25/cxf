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

begin_comment
comment|/**  * Base permission description  * @see OAuthAuthorizationData  */
end_comment

begin_class
specifier|public
class|class
name|Permission
implements|implements
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|8988574955042726083L
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
specifier|public
name|Permission
parameter_list|()
block|{              }
specifier|public
name|Permission
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
comment|/**      * Indicates if this permission has been allocated by default or not.      * Authorization View handlers may use this property in order to restrict      * the list of scopes which may be refused to non-default scopes only.      * For example, the read-only check-box controls can be used to represent      * the default scopes       * @param isDefault true if the permission has been allocated by default      */
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
block|}
end_class

end_unit

