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
name|filters
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|data
operator|.
name|AccessToken
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
name|oauth
operator|.
name|data
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
name|oauth
operator|.
name|data
operator|.
name|OAuthPermission
import|;
end_import

begin_class
specifier|public
class|class
name|OAuthInfo
block|{
specifier|private
name|Client
name|client
decl_stmt|;
specifier|private
name|AccessToken
name|token
decl_stmt|;
specifier|private
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|permissions
decl_stmt|;
specifier|public
name|OAuthInfo
parameter_list|(
name|Client
name|client
parameter_list|,
name|AccessToken
name|token
parameter_list|,
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|permissions
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
name|this
operator|.
name|token
operator|=
name|token
expr_stmt|;
name|this
operator|.
name|permissions
operator|=
name|permissions
expr_stmt|;
block|}
specifier|public
name|Client
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
specifier|public
name|AccessToken
name|getToken
parameter_list|()
block|{
return|return
name|token
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRoles
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|authorities
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|permissions
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|OAuthPermission
name|permission
range|:
name|permissions
control|)
block|{
name|authorities
operator|.
name|addAll
argument_list|(
name|permission
operator|.
name|getRoles
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|authorities
return|;
block|}
block|}
end_class

end_unit

