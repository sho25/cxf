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
comment|/**  * Base Token representation  */
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
specifier|public
name|String
name|getApprovedScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
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

