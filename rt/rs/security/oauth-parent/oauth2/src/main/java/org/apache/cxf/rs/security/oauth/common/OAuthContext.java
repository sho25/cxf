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
name|oauth
operator|.
name|common
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

begin_comment
comment|/**  * Captures the information which custom filters may use to further protect the endpoints  */
end_comment

begin_class
specifier|public
class|class
name|OAuthContext
block|{
specifier|private
name|UserSubject
name|subject
decl_stmt|;
specifier|private
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|permissions
decl_stmt|;
specifier|public
name|OAuthContext
parameter_list|(
name|UserSubject
name|subject
parameter_list|,
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|perms
parameter_list|)
block|{
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
name|this
operator|.
name|permissions
operator|=
name|perms
expr_stmt|;
block|}
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
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|getPermissions
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|permissions
argument_list|)
return|;
block|}
block|}
end_class

end_unit

