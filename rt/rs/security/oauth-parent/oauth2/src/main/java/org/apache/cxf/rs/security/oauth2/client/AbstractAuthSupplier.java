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
name|client
package|;
end_package

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
name|ClientAccessToken
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractAuthSupplier
block|{
specifier|private
name|ClientAccessToken
name|clientAccessToken
init|=
operator|new
name|ClientAccessToken
argument_list|()
decl_stmt|;
specifier|protected
name|AbstractAuthSupplier
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|clientAccessToken
operator|=
operator|new
name|ClientAccessToken
argument_list|()
expr_stmt|;
name|clientAccessToken
operator|.
name|setTokenType
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setAccessToken
parameter_list|(
name|String
name|accessToken
parameter_list|)
block|{
name|clientAccessToken
operator|.
name|setTokenKey
argument_list|(
name|accessToken
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|createAuthorizationHeader
parameter_list|()
block|{
return|return
name|OAuthClientUtils
operator|.
name|createAuthorizationHeader
argument_list|(
name|getClientAccessToken
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|ClientAccessToken
name|getClientAccessToken
parameter_list|()
block|{
return|return
name|clientAccessToken
return|;
block|}
specifier|protected
name|void
name|setClientAccessToken
parameter_list|(
name|ClientAccessToken
name|clientAccessToken
parameter_list|)
block|{
name|this
operator|.
name|clientAccessToken
operator|=
name|clientAccessToken
expr_stmt|;
block|}
block|}
end_class

end_unit

