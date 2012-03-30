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

begin_comment
comment|/**  * Represents the extended client view of {@link AccessToken}.  * It may contain the actual scope value assigned to the access token,  * the refresh token key, and other properties such as when this token   * will expire, etc.  */
end_comment

begin_class
specifier|public
class|class
name|ClientAccessToken
extends|extends
name|AccessToken
block|{
specifier|private
name|String
name|scope
decl_stmt|;
specifier|private
name|String
name|rToken
decl_stmt|;
specifier|public
name|ClientAccessToken
parameter_list|(
name|String
name|tokenType
parameter_list|,
name|String
name|tokenKey
parameter_list|)
block|{
name|super
argument_list|(
name|tokenType
argument_list|,
name|tokenKey
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the actual scope assigned to the access token.      * For example, it can be down-scoped in which case the client      * may need to adjust the way it works with the end user.       * @param approvedScope the actual scope      */
specifier|public
name|void
name|setApprovedScope
parameter_list|(
name|String
name|approvedScope
parameter_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|approvedScope
expr_stmt|;
block|}
comment|/**      * Gets the actual scope assigned to the access token.      * @return the scope      */
specifier|public
name|String
name|getApprovedScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
comment|/**      * Sets the refresh token key the client can use to obtain a new      * access token      * @param refreshToken the refresh token      */
specifier|public
name|void
name|setRefreshToken
parameter_list|(
name|String
name|refreshToken
parameter_list|)
block|{
name|this
operator|.
name|rToken
operator|=
name|refreshToken
expr_stmt|;
block|}
comment|/**      * Gets the refresh token key the client can use to obtain a new      * access token      * @return the refresh token      */
specifier|public
name|String
name|getRefreshToken
parameter_list|()
block|{
return|return
name|rToken
return|;
block|}
block|}
end_class

end_unit

