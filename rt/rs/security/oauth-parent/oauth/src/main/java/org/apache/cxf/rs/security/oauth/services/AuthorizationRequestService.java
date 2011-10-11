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
name|rs
operator|.
name|security
operator|.
name|oauth
operator|.
name|data
operator|.
name|OAuthAuthorizationData
import|;
end_import

begin_comment
comment|/**  * This resource handles the End User authorising  * or denying the Client to access its resources.  * If End User approves the access this resource will  * redirect End User back to the Client, supplying   * a request token verifier (aka authorization code)  */
end_comment

begin_class
specifier|public
class|class
name|AuthorizationRequestService
extends|extends
name|AbstractOAuthService
block|{
specifier|private
name|AuthorizationRequestHandler
name|handler
init|=
operator|new
name|AuthorizationRequestHandler
argument_list|()
decl_stmt|;
specifier|public
name|void
name|setAuthorizationRequestHandler
parameter_list|(
name|AuthorizationRequestHandler
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
name|Response
name|response
init|=
name|handler
operator|.
name|handle
argument_list|(
name|getHttpRequest
argument_list|()
argument_list|,
name|getDataProvider
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|response
operator|.
name|getEntity
argument_list|()
operator|instanceof
name|OAuthAuthorizationData
condition|)
block|{
name|String
name|replyTo
init|=
name|getUriInfo
argument_list|()
operator|.
name|getBaseUriBuilder
argument_list|()
operator|.
name|path
argument_list|(
literal|"authorizeDecision"
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
operator|(
operator|(
name|OAuthAuthorizationData
operator|)
name|response
operator|.
name|getEntity
argument_list|()
operator|)
operator|.
name|setReplyTo
argument_list|(
name|replyTo
argument_list|)
expr_stmt|;
block|}
return|return
name|response
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/authorizeDecision"
argument_list|)
specifier|public
name|Response
name|authorizeDecision
parameter_list|()
block|{
return|return
name|authorize
argument_list|()
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/authorizeDecision"
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
name|authorizeDecision
argument_list|()
return|;
block|}
block|}
end_class

end_unit

