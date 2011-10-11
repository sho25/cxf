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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|OAuthProblemException
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
name|RequestHandler
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
name|model
operator|.
name|ClassResourceInfo
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
name|message
operator|.
name|Message
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
name|security
operator|.
name|SecurityContext
import|;
end_import

begin_class
annotation|@
name|Provider
specifier|public
class|class
name|OAuthRequestFilter
extends|extends
name|AbstractAuthFilter
implements|implements
name|RequestHandler
block|{
annotation|@
name|Context
specifier|private
name|MessageContext
name|mc
decl_stmt|;
specifier|public
name|Response
name|handleRequest
parameter_list|(
name|Message
name|m
parameter_list|,
name|ClassResourceInfo
name|resourceClass
parameter_list|)
block|{
try|try
block|{
name|OAuthInfo
name|info
init|=
name|handleOAuthRequest
argument_list|(
name|mc
operator|.
name|getHttpServletRequest
argument_list|()
argument_list|)
decl_stmt|;
name|setSecurityContext
argument_list|(
name|m
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OAuthProblemException
name|e
parameter_list|)
block|{
return|return
name|Response
operator|.
name|status
argument_list|(
literal|401
argument_list|)
operator|.
name|header
argument_list|(
literal|"WWW-Authenticate"
argument_list|,
literal|"OAuth"
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
name|Response
operator|.
name|status
argument_list|(
literal|401
argument_list|)
operator|.
name|header
argument_list|(
literal|"WWW-Authenticate"
argument_list|,
literal|"OAuth"
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|setSecurityContext
parameter_list|(
name|Message
name|m
parameter_list|,
name|OAuthInfo
name|info
parameter_list|)
block|{
name|SecurityContext
name|sc
init|=
name|createSecurityContext
argument_list|(
name|mc
operator|.
name|getHttpServletRequest
argument_list|()
argument_list|,
name|info
argument_list|)
decl_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|,
name|sc
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

