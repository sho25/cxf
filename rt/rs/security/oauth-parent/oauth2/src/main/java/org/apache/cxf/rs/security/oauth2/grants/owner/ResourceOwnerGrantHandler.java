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
name|grants
operator|.
name|owner
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
name|MultivaluedMap
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|common
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
name|oauth2
operator|.
name|common
operator|.
name|OAuthError
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
name|oauth2
operator|.
name|common
operator|.
name|ServerAccessToken
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
name|oauth2
operator|.
name|common
operator|.
name|UserSubject
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
name|oauth2
operator|.
name|grants
operator|.
name|AbstractGrantHandler
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
name|oauth2
operator|.
name|provider
operator|.
name|OAuthServiceException
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
name|oauth2
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
name|utils
operator|.
name|OAuthUtils
import|;
end_import

begin_comment
comment|/**  * The "resource owner" grant handler  */
end_comment

begin_class
specifier|public
class|class
name|ResourceOwnerGrantHandler
extends|extends
name|AbstractGrantHandler
block|{
specifier|private
name|ResourceOwnerLoginHandler
name|loginHandler
decl_stmt|;
specifier|public
name|ResourceOwnerGrantHandler
parameter_list|()
block|{
name|super
argument_list|(
name|OAuthConstants
operator|.
name|RESOURCE_OWNER_GRANT
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ServerAccessToken
name|createAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|String
name|ownerName
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|RESOURCE_OWNER_NAME
argument_list|)
decl_stmt|;
name|String
name|ownerPassword
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|RESOURCE_OWNER_PASSWORD
argument_list|)
decl_stmt|;
if|if
condition|(
name|ownerName
operator|==
literal|null
operator|||
name|ownerPassword
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
operator|new
name|OAuthError
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_REQUEST
argument_list|)
argument_list|)
throw|;
block|}
name|UserSubject
name|subject
init|=
name|loginHandler
operator|.
name|createSubject
argument_list|(
name|client
argument_list|,
name|ownerName
argument_list|,
name|ownerPassword
argument_list|)
decl_stmt|;
if|if
condition|(
name|subject
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_GRANT
argument_list|)
throw|;
block|}
return|return
name|doCreateAccessToken
argument_list|(
name|client
argument_list|,
name|subject
argument_list|,
name|params
argument_list|)
return|;
block|}
specifier|public
name|ResourceOwnerLoginHandler
name|getLoginHandler
parameter_list|()
block|{
return|return
name|this
operator|.
name|loginHandler
return|;
block|}
specifier|public
name|void
name|setLoginHandler
parameter_list|(
name|ResourceOwnerLoginHandler
name|loginHandler
parameter_list|)
block|{
name|this
operator|.
name|loginHandler
operator|=
name|loginHandler
expr_stmt|;
block|}
specifier|public
name|void
name|setMessageContext
parameter_list|(
name|MessageContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|loginHandler
operator|!=
literal|null
condition|)
block|{
name|OAuthUtils
operator|.
name|injectContextIntoOAuthProvider
argument_list|(
name|context
argument_list|,
name|loginHandler
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

