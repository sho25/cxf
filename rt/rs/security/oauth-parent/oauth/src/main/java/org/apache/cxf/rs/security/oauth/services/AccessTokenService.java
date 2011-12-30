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
name|services
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
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
name|POST
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
name|Path
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
name|Produces
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

begin_comment
comment|/**  * This resource will replace a request token with a new access token which  * will complete the OAuth flow. The third-party consumer will use the access  * token to access end user resources.  */
end_comment

begin_class
annotation|@
name|Path
argument_list|(
literal|"/token"
argument_list|)
specifier|public
class|class
name|AccessTokenService
extends|extends
name|AbstractOAuthService
block|{
specifier|private
name|AccessTokenHandler
name|handler
init|=
operator|new
name|AccessTokenHandler
argument_list|()
decl_stmt|;
specifier|public
name|void
name|setAccessTokenHandler
parameter_list|(
name|AccessTokenHandler
name|h
parameter_list|)
block|{
name|this
operator|.
name|handler
operator|=
name|h
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"application/x-www-form-urlencoded"
argument_list|)
specifier|public
name|Response
name|getAccessTokenWithGET
parameter_list|()
block|{
return|return
name|getAccessToken
argument_list|()
return|;
block|}
annotation|@
name|POST
annotation|@
name|Produces
argument_list|(
literal|"application/x-www-form-urlencoded"
argument_list|)
specifier|public
name|Response
name|getAccessToken
parameter_list|()
block|{
return|return
name|handler
operator|.
name|handle
argument_list|(
name|getMessageContext
argument_list|()
argument_list|,
name|getDataProvider
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

