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
name|services
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|WebApplicationException
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
name|MediaType
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
name|MultivaluedMap
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|provider
operator|.
name|OAuthDataProvider
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

begin_comment
comment|/**  * Abstract utility class which OAuth services extend  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractOAuthService
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AbstractOAuthService
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|MessageContext
name|mc
decl_stmt|;
specifier|private
name|OAuthDataProvider
name|dataProvider
decl_stmt|;
specifier|private
name|boolean
name|blockUnsecureRequests
decl_stmt|;
annotation|@
name|Context
specifier|public
name|void
name|setMessageContext
parameter_list|(
name|MessageContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|mc
operator|=
name|context
expr_stmt|;
block|}
specifier|public
name|MessageContext
name|getMessageContext
parameter_list|()
block|{
return|return
name|mc
return|;
block|}
specifier|public
name|void
name|setDataProvider
parameter_list|(
name|OAuthDataProvider
name|dataProvider
parameter_list|)
block|{
name|this
operator|.
name|dataProvider
operator|=
name|dataProvider
expr_stmt|;
block|}
specifier|public
name|OAuthDataProvider
name|getDataProvider
parameter_list|()
block|{
return|return
name|dataProvider
return|;
block|}
specifier|protected
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getQueryParameters
parameter_list|()
block|{
return|return
name|getMessageContext
argument_list|()
operator|.
name|getUriInfo
argument_list|()
operator|.
name|getQueryParameters
argument_list|()
return|;
block|}
specifier|protected
name|Client
name|getClient
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
return|return
name|getClient
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_ID
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|Client
name|getClient
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|Client
name|client
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|clientId
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|client
operator|=
name|dataProvider
operator|.
name|getClient
argument_list|(
name|clientId
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OAuthServiceException
name|ex
parameter_list|)
block|{
comment|// log it
block|}
block|}
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|reportInvalidRequestError
argument_list|(
literal|"Client ID is invalid"
argument_list|)
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
specifier|protected
name|void
name|checkTransportSecurity
parameter_list|()
block|{
if|if
condition|(
operator|!
name|mc
operator|.
name|getSecurityContext
argument_list|()
operator|.
name|isSecure
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Unsecure HTTP, Transport Layer Security is recommended"
argument_list|)
expr_stmt|;
if|if
condition|(
name|blockUnsecureRequests
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
block|}
block|}
specifier|protected
name|void
name|reportInvalidRequestError
parameter_list|(
name|String
name|errorDescription
parameter_list|)
block|{
name|OAuthError
name|error
init|=
operator|new
name|OAuthError
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_REQUEST
argument_list|,
name|errorDescription
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|Response
operator|.
name|status
argument_list|(
literal|400
argument_list|)
operator|.
name|type
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
operator|.
name|entity
argument_list|(
name|error
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
throw|;
block|}
specifier|public
name|void
name|setBlockUnsecureRequests
parameter_list|(
name|boolean
name|blockUnsecureRequests
parameter_list|)
block|{
name|this
operator|.
name|blockUnsecureRequests
operator|=
name|blockUnsecureRequests
expr_stmt|;
block|}
block|}
end_class

end_unit

