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
name|oidc
operator|.
name|rp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|UriBuilder
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
name|client
operator|.
name|ClientTokenContextManager
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
name|oidc
operator|.
name|common
operator|.
name|IdToken
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"rp"
argument_list|)
specifier|public
class|class
name|OidcRpAuthenticationService
block|{
specifier|private
name|ClientTokenContextManager
name|stateManager
decl_stmt|;
specifier|private
name|String
name|defaultLocation
decl_stmt|;
annotation|@
name|Context
specifier|private
name|MessageContext
name|mc
decl_stmt|;
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"signin"
argument_list|)
annotation|@
name|Consumes
argument_list|(
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED
argument_list|)
specifier|public
name|Response
name|completeScriptAuthentication
parameter_list|(
annotation|@
name|Context
name|IdToken
name|idToken
parameter_list|)
block|{
name|OidcClientTokenContextImpl
name|ctx
init|=
operator|new
name|OidcClientTokenContextImpl
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|setIdToken
argument_list|(
name|idToken
argument_list|)
expr_stmt|;
return|return
name|completeAuthentication
argument_list|(
name|ctx
argument_list|)
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"complete"
argument_list|)
specifier|public
name|Response
name|completeAuthentication
parameter_list|(
annotation|@
name|Context
name|OidcClientTokenContext
name|oidcContext
parameter_list|)
block|{
name|stateManager
operator|.
name|setClientTokenContext
argument_list|(
name|mc
argument_list|,
name|oidcContext
argument_list|)
expr_stmt|;
name|URI
name|redirectUri
init|=
literal|null
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|state
init|=
name|oidcContext
operator|.
name|getState
argument_list|()
decl_stmt|;
name|String
name|location
init|=
name|state
operator|!=
literal|null
condition|?
name|state
operator|.
name|getFirst
argument_list|(
literal|"state"
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|location
operator|==
literal|null
operator|&&
name|defaultLocation
operator|!=
literal|null
condition|)
block|{
name|String
name|basePath
init|=
operator|(
name|String
operator|)
name|mc
operator|.
name|get
argument_list|(
literal|"http.base.path"
argument_list|)
decl_stmt|;
name|redirectUri
operator|=
name|UriBuilder
operator|.
name|fromUri
argument_list|(
name|basePath
argument_list|)
operator|.
name|path
argument_list|(
name|defaultLocation
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|redirectUri
operator|=
name|URI
operator|.
name|create
argument_list|(
name|location
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|redirectUri
operator|!=
literal|null
condition|)
block|{
return|return
name|Response
operator|.
name|seeOther
argument_list|(
name|redirectUri
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|Response
operator|.
name|ok
argument_list|(
name|oidcContext
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
specifier|public
name|void
name|setDefaultLocation
parameter_list|(
name|String
name|defaultLocation
parameter_list|)
block|{
name|this
operator|.
name|defaultLocation
operator|=
name|defaultLocation
expr_stmt|;
block|}
specifier|public
name|void
name|setStateManager
parameter_list|(
name|ClientTokenContextManager
name|stateManager
parameter_list|)
block|{
name|this
operator|.
name|stateManager
operator|=
name|stateManager
expr_stmt|;
block|}
block|}
end_class

end_unit

