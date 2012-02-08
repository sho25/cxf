begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|oauth
operator|.
name|server
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|OAuth
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|utils
operator|.
name|OAuthConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|security
operator|.
name|core
operator|.
name|Authentication
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|security
operator|.
name|web
operator|.
name|authentication
operator|.
name|SavedRequestAwareAuthenticationSuccessHandler
import|;
end_import

begin_class
specifier|public
class|class
name|AuthenticationSuccessfullHandler
extends|extends
name|SavedRequestAwareAuthenticationSuccessHandler
block|{
specifier|private
name|String
name|confirmationUrl
decl_stmt|;
specifier|public
name|void
name|onAuthenticationSuccess
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|,
name|Authentication
name|authentication
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
name|super
operator|.
name|onAuthenticationSuccess
argument_list|(
name|request
argument_list|,
name|response
argument_list|,
name|authentication
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|determineTargetUrl
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
block|{
name|String
name|oauthToken
init|=
name|request
operator|.
name|getParameter
argument_list|(
name|OAuth
operator|.
name|OAUTH_TOKEN
argument_list|)
decl_stmt|;
name|String
name|authToken
init|=
name|request
operator|.
name|getParameter
argument_list|(
name|OAuthConstants
operator|.
name|AUTHENTICITY_TOKEN
argument_list|)
decl_stmt|;
name|String
name|decision
init|=
name|request
operator|.
name|getParameter
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_DECISION_KEY
argument_list|)
decl_stmt|;
name|String
name|xScope
init|=
name|request
operator|.
name|getParameter
argument_list|(
name|OAuthConstants
operator|.
name|X_OAUTH_SCOPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|oauthToken
argument_list|)
condition|)
block|{
return|return
name|super
operator|.
name|determineTargetUrl
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
return|;
block|}
name|StringBuffer
name|url
init|=
operator|new
name|StringBuffer
argument_list|(
name|confirmationUrl
argument_list|)
operator|.
name|append
argument_list|(
literal|"?"
argument_list|)
operator|.
name|append
argument_list|(
name|OAuth
operator|.
name|OAUTH_TOKEN
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|oauthToken
argument_list|)
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
operator|.
name|append
argument_list|(
name|OAuthConstants
operator|.
name|AUTHENTICITY_TOKEN
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|authToken
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|decision
argument_list|)
condition|)
block|{
name|url
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
operator|.
name|append
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_DECISION_KEY
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|decision
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|xScope
argument_list|)
condition|)
block|{
name|url
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
operator|.
name|append
argument_list|(
name|OAuthConstants
operator|.
name|X_OAUTH_SCOPE
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|xScope
argument_list|)
expr_stmt|;
block|}
return|return
name|url
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|void
name|setConfirmationUrl
parameter_list|(
name|String
name|confirmationUrl
parameter_list|)
block|{
name|this
operator|.
name|confirmationUrl
operator|=
name|confirmationUrl
expr_stmt|;
block|}
block|}
end_class

end_unit

