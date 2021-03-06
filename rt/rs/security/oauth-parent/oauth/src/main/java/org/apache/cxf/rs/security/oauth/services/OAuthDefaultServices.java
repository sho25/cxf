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
name|Consumes
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
name|Context
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

begin_comment
comment|/**  * Default OAuth service implementation  */
end_comment

begin_class
annotation|@
name|Path
argument_list|(
literal|"/"
argument_list|)
specifier|public
class|class
name|OAuthDefaultServices
block|{
specifier|private
name|AuthorizationRequestService
name|authorizeService
init|=
operator|new
name|AuthorizationRequestService
argument_list|()
decl_stmt|;
specifier|private
name|AccessTokenService
name|accessTokenService
init|=
operator|new
name|AccessTokenService
argument_list|()
decl_stmt|;
specifier|private
name|RequestTokenService
name|requestTokenService
init|=
operator|new
name|RequestTokenService
argument_list|()
decl_stmt|;
specifier|public
name|OAuthDefaultServices
parameter_list|()
block|{     }
annotation|@
name|Context
specifier|public
name|void
name|setMessageContext
parameter_list|(
name|MessageContext
name|mc
parameter_list|)
block|{
name|this
operator|.
name|authorizeService
operator|.
name|setMessageContext
argument_list|(
name|mc
argument_list|)
expr_stmt|;
name|this
operator|.
name|accessTokenService
operator|.
name|setMessageContext
argument_list|(
name|mc
argument_list|)
expr_stmt|;
name|this
operator|.
name|requestTokenService
operator|.
name|setMessageContext
argument_list|(
name|mc
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setAuthorizationService
parameter_list|(
name|AuthorizationRequestService
name|service
parameter_list|)
block|{
name|this
operator|.
name|authorizeService
operator|=
name|service
expr_stmt|;
block|}
specifier|public
name|void
name|setAccessTokenService
parameter_list|(
name|AccessTokenService
name|service
parameter_list|)
block|{
name|this
operator|.
name|accessTokenService
operator|=
name|service
expr_stmt|;
block|}
specifier|public
name|void
name|setRequestTokenservice
parameter_list|(
name|RequestTokenService
name|service
parameter_list|)
block|{
name|this
operator|.
name|requestTokenService
operator|=
name|service
expr_stmt|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/initiate"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/x-www-form-urlencoded"
argument_list|)
specifier|public
name|Response
name|getRequestToken
parameter_list|()
block|{
return|return
name|requestTokenService
operator|.
name|getRequestToken
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/initiate"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/x-www-form-urlencoded"
argument_list|)
specifier|public
name|Response
name|getRequestTokenWithGET
parameter_list|()
block|{
return|return
name|requestTokenService
operator|.
name|getRequestToken
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/authorize"
argument_list|)
annotation|@
name|Produces
argument_list|(
block|{
literal|"application/xhtml+xml"
block|,
literal|"text/html"
block|,
literal|"application/xml"
block|,
literal|"application/json"
block|}
argument_list|)
specifier|public
name|Response
name|authorize
parameter_list|()
block|{
return|return
name|authorizeService
operator|.
name|authorize
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/authorize/decision"
argument_list|)
specifier|public
name|Response
name|authorizeDecision
parameter_list|()
block|{
return|return
name|authorizeService
operator|.
name|authorizeDecision
argument_list|()
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/authorize/decision"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"application/x-www-form-urlencoded"
argument_list|)
specifier|public
name|Response
name|authorizeDecisionForm
parameter_list|()
block|{
return|return
name|authorizeService
operator|.
name|authorizeDecision
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/token"
argument_list|)
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
name|accessTokenService
operator|.
name|getAccessToken
argument_list|()
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/token"
argument_list|)
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
name|accessTokenService
operator|.
name|getAccessToken
argument_list|()
return|;
block|}
block|}
end_class

end_unit

