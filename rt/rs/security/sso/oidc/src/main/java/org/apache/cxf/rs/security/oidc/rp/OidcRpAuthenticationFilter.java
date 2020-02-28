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
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Priority
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
name|Priorities
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
name|container
operator|.
name|ContainerRequestContext
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
name|container
operator|.
name|ContainerRequestFilter
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
name|container
operator|.
name|PreMatching
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
name|HttpHeaders
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
name|MessageContextImpl
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
name|impl
operator|.
name|MetadataMap
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
name|utils
operator|.
name|FormUtils
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
name|utils
operator|.
name|JAXRSUtils
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
name|jose
operator|.
name|jwt
operator|.
name|JwtException
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
name|jose
operator|.
name|jwt
operator|.
name|JwtUtils
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
name|ClientTokenContext
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
name|PreMatching
annotation|@
name|Priority
argument_list|(
name|Priorities
operator|.
name|AUTHENTICATION
operator|+
literal|2
argument_list|)
specifier|public
class|class
name|OidcRpAuthenticationFilter
implements|implements
name|ContainerRequestFilter
block|{
annotation|@
name|Context
specifier|private
name|MessageContext
name|mc
decl_stmt|;
specifier|private
name|ClientTokenContextManager
name|stateManager
decl_stmt|;
specifier|private
name|String
name|redirectUri
decl_stmt|;
specifier|private
name|String
name|roleClaim
decl_stmt|;
specifier|private
name|boolean
name|addRequestUriAsRedirectQuery
decl_stmt|;
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|rc
parameter_list|)
block|{
if|if
condition|(
name|checkSecurityContext
argument_list|(
name|rc
argument_list|)
condition|)
block|{
return|return;
block|}
elseif|else
if|if
condition|(
name|redirectUri
operator|!=
literal|null
condition|)
block|{
specifier|final
name|UriBuilder
name|redirectBuilder
decl_stmt|;
if|if
condition|(
name|redirectUri
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
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
name|redirectBuilder
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
name|redirectUri
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|redirectUri
operator|.
name|startsWith
argument_list|(
literal|"http"
argument_list|)
condition|)
block|{
name|redirectBuilder
operator|=
name|UriBuilder
operator|.
name|fromUri
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|redirectUri
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|redirectBuilder
operator|=
name|rc
operator|.
name|getUriInfo
argument_list|()
operator|.
name|getBaseUriBuilder
argument_list|()
operator|.
name|path
argument_list|(
name|redirectUri
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|addRequestUriAsRedirectQuery
condition|)
block|{
name|redirectBuilder
operator|.
name|queryParam
argument_list|(
literal|"state"
argument_list|,
name|rc
operator|.
name|getUriInfo
argument_list|()
operator|.
name|getRequestUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|URI
name|redirectAddress
init|=
name|redirectBuilder
operator|.
name|build
argument_list|()
decl_stmt|;
name|rc
operator|.
name|abortWith
argument_list|(
name|Response
operator|.
name|seeOther
argument_list|(
name|redirectAddress
argument_list|)
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|CACHE_CONTROL
argument_list|,
literal|"no-cache, no-store"
argument_list|)
operator|.
name|header
argument_list|(
literal|"Pragma"
argument_list|,
literal|"no-cache"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|rc
operator|.
name|abortWith
argument_list|(
name|Response
operator|.
name|status
argument_list|(
literal|401
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|boolean
name|checkSecurityContext
parameter_list|(
name|ContainerRequestContext
name|rc
parameter_list|)
block|{
name|OidcClientTokenContext
name|tokenContext
init|=
operator|(
name|OidcClientTokenContext
operator|)
name|stateManager
operator|.
name|getClientTokenContext
argument_list|(
name|mc
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokenContext
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|IdToken
name|idToken
init|=
name|tokenContext
operator|.
name|getIdToken
argument_list|()
decl_stmt|;
try|try
block|{
comment|// If ID token has expired then the context is no longer valid
name|JwtUtils
operator|.
name|validateJwtExpiry
argument_list|(
name|idToken
argument_list|,
literal|0
argument_list|,
name|idToken
operator|.
name|getExpiryTime
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JwtException
name|ex
parameter_list|)
block|{
name|stateManager
operator|.
name|removeClientTokenContext
argument_list|(
operator|new
name|MessageContextImpl
argument_list|(
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|OidcClientTokenContextImpl
name|newTokenContext
init|=
operator|new
name|OidcClientTokenContextImpl
argument_list|()
decl_stmt|;
name|newTokenContext
operator|.
name|setToken
argument_list|(
name|tokenContext
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
name|newTokenContext
operator|.
name|setIdToken
argument_list|(
name|idToken
argument_list|)
expr_stmt|;
name|newTokenContext
operator|.
name|setUserInfo
argument_list|(
name|tokenContext
operator|.
name|getUserInfo
argument_list|()
argument_list|)
expr_stmt|;
name|newTokenContext
operator|.
name|setState
argument_list|(
name|toRequestState
argument_list|(
name|rc
argument_list|)
argument_list|)
expr_stmt|;
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|setContent
argument_list|(
name|ClientTokenContext
operator|.
name|class
argument_list|,
name|newTokenContext
argument_list|)
expr_stmt|;
name|OidcSecurityContext
name|oidcSecCtx
init|=
operator|new
name|OidcSecurityContext
argument_list|(
name|newTokenContext
argument_list|)
decl_stmt|;
name|oidcSecCtx
operator|.
name|setRoleClaim
argument_list|(
name|roleClaim
argument_list|)
expr_stmt|;
name|rc
operator|.
name|setSecurityContext
argument_list|(
name|oidcSecCtx
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|private
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|toRequestState
parameter_list|(
name|ContainerRequestContext
name|rc
parameter_list|)
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|requestState
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|()
decl_stmt|;
name|requestState
operator|.
name|putAll
argument_list|(
name|rc
operator|.
name|getUriInfo
argument_list|()
operator|.
name|getQueryParameters
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED_TYPE
operator|.
name|isCompatible
argument_list|(
name|rc
operator|.
name|getMediaType
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|body
init|=
name|FormUtils
operator|.
name|readBody
argument_list|(
name|rc
operator|.
name|getEntityStream
argument_list|()
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
name|FormUtils
operator|.
name|populateMapFromString
argument_list|(
name|requestState
argument_list|,
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
argument_list|,
name|body
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|rc
operator|.
name|setEntityStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|body
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|requestState
return|;
block|}
specifier|public
name|void
name|setRedirectUri
parameter_list|(
name|String
name|redirectUri
parameter_list|)
block|{
name|this
operator|.
name|redirectUri
operator|=
name|redirectUri
expr_stmt|;
block|}
specifier|public
name|void
name|setClientTokenContextManager
parameter_list|(
name|ClientTokenContextManager
name|manager
parameter_list|)
block|{
name|this
operator|.
name|stateManager
operator|=
name|manager
expr_stmt|;
block|}
specifier|public
name|void
name|setRoleClaim
parameter_list|(
name|String
name|roleClaim
parameter_list|)
block|{
name|this
operator|.
name|roleClaim
operator|=
name|roleClaim
expr_stmt|;
block|}
specifier|public
name|void
name|setAddRequestUriAsRedirectQuery
parameter_list|(
name|boolean
name|addRequestUriAsRedirectQuery
parameter_list|)
block|{
name|this
operator|.
name|addRequestUriAsRedirectQuery
operator|=
name|addRequestUriAsRedirectQuery
expr_stmt|;
block|}
block|}
end_class

end_unit

