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
name|jaxrs
operator|.
name|security
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
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|login
operator|.
name|Configuration
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
name|Response
operator|.
name|ResponseBuilder
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
name|interceptor
operator|.
name|security
operator|.
name|AuthenticationException
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
name|interceptor
operator|.
name|security
operator|.
name|JAASLoginInterceptor
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
name|interceptor
operator|.
name|security
operator|.
name|NamePasswordCallbackHandler
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
name|HttpHeadersImpl
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
name|HttpUtils
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
name|message
operator|.
name|Message
import|;
end_import

begin_class
annotation|@
name|PreMatching
specifier|public
class|class
name|JAASAuthenticationFilter
implements|implements
name|ContainerRequestFilter
block|{
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|MediaType
argument_list|>
name|HTML_MEDIA_TYPES
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XHTML_XML_TYPE
argument_list|,
name|MediaType
operator|.
name|TEXT_HTML_TYPE
argument_list|)
decl_stmt|;
specifier|private
name|URI
name|redirectURI
decl_stmt|;
specifier|private
name|String
name|realmName
decl_stmt|;
specifier|private
name|boolean
name|ignoreBasePath
init|=
literal|true
decl_stmt|;
specifier|private
name|JAASLoginInterceptor
name|interceptor
init|=
operator|new
name|JAASLoginInterceptor
argument_list|()
block|{
specifier|protected
name|CallbackHandler
name|getCallbackHandler
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|password
parameter_list|)
block|{
return|return
name|JAASAuthenticationFilter
operator|.
name|this
operator|.
name|getCallbackHandler
argument_list|(
name|name
argument_list|,
name|password
argument_list|)
return|;
block|}
block|}
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
name|void
name|setRolePrefix
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|interceptor
operator|.
name|setRolePrefix
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setIgnoreBasePath
parameter_list|(
name|boolean
name|ignore
parameter_list|)
block|{
name|this
operator|.
name|ignoreBasePath
operator|=
name|ignore
expr_stmt|;
block|}
specifier|public
name|void
name|setContextName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|interceptor
operator|.
name|setContextName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setLoginConfig
parameter_list|(
name|Configuration
name|config
parameter_list|)
block|{
name|interceptor
operator|.
name|setLoginConfig
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setRedirectURI
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|redirectURI
operator|=
name|URI
operator|.
name|create
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setRealmName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|realmName
operator|=
name|name
expr_stmt|;
block|}
specifier|protected
name|CallbackHandler
name|getCallbackHandler
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|password
parameter_list|)
block|{
return|return
operator|new
name|NamePasswordCallbackHandler
argument_list|(
name|name
argument_list|,
name|password
argument_list|)
return|;
block|}
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|context
parameter_list|)
block|{
name|Message
name|m
init|=
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
try|try
block|{
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthenticationException
name|ex
parameter_list|)
block|{
name|context
operator|.
name|abortWith
argument_list|(
name|handleAuthenticationException
argument_list|(
name|ex
argument_list|,
name|m
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|ex
parameter_list|)
block|{
name|context
operator|.
name|abortWith
argument_list|(
name|handleAuthenticationException
argument_list|(
name|ex
argument_list|,
name|m
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Response
name|handleAuthenticationException
parameter_list|(
name|SecurityException
name|ex
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|HttpHeaders
name|headers
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
if|if
condition|(
name|redirectURI
operator|!=
literal|null
operator|&&
name|isRedirectPossible
argument_list|(
name|headers
argument_list|)
condition|)
block|{
name|URI
name|finalRedirectURI
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|redirectURI
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|String
name|endpointAddress
init|=
name|HttpUtils
operator|.
name|getEndpointAddress
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|Object
name|basePathProperty
init|=
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|BASE_PATH
argument_list|)
decl_stmt|;
if|if
condition|(
name|ignoreBasePath
operator|&&
name|basePathProperty
operator|!=
literal|null
operator|&&
operator|!
literal|"/"
operator|.
name|equals
argument_list|(
name|basePathProperty
argument_list|)
condition|)
block|{
name|int
name|index
init|=
name|endpointAddress
operator|.
name|lastIndexOf
argument_list|(
name|basePathProperty
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|!=
operator|-
literal|1
condition|)
block|{
name|endpointAddress
operator|=
name|endpointAddress
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
block|}
name|finalRedirectURI
operator|=
name|UriBuilder
operator|.
name|fromUri
argument_list|(
name|endpointAddress
argument_list|)
operator|.
name|path
argument_list|(
name|redirectURI
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|finalRedirectURI
operator|=
name|redirectURI
expr_stmt|;
block|}
return|return
name|Response
operator|.
name|status
argument_list|(
name|getRedirectStatus
argument_list|()
argument_list|)
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|LOCATION
argument_list|,
name|finalRedirectURI
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
else|else
block|{
name|ResponseBuilder
name|builder
init|=
name|Response
operator|.
name|status
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|UNAUTHORIZED
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|authHeader
init|=
name|headers
operator|.
name|getRequestHeader
argument_list|(
name|HttpHeaders
operator|.
name|AUTHORIZATION
argument_list|)
decl_stmt|;
if|if
condition|(
name|authHeader
operator|!=
literal|null
operator|&&
name|authHeader
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// should HttpHeadersImpl do it ?
name|String
index|[]
name|authValues
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|authHeader
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|" "
argument_list|)
decl_stmt|;
if|if
condition|(
name|authValues
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|authValues
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"Basic"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|realmName
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" realm=\""
argument_list|)
operator|.
name|append
argument_list|(
name|realmName
argument_list|)
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|WWW_AUTHENTICATE
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
block|}
specifier|protected
name|Response
operator|.
name|Status
name|getRedirectStatus
parameter_list|()
block|{
return|return
name|Response
operator|.
name|Status
operator|.
name|TEMPORARY_REDIRECT
return|;
block|}
specifier|protected
name|boolean
name|isRedirectPossible
parameter_list|(
name|HttpHeaders
name|headers
parameter_list|)
block|{
name|List
argument_list|<
name|MediaType
argument_list|>
name|clientTypes
init|=
name|headers
operator|.
name|getAcceptableMediaTypes
argument_list|()
decl_stmt|;
return|return
operator|!
name|JAXRSUtils
operator|.
name|intersectMimeTypes
argument_list|(
name|clientTypes
argument_list|,
name|HTML_MEDIA_TYPES
argument_list|,
literal|false
argument_list|)
operator|.
name|isEmpty
argument_list|()
return|;
block|}
block|}
end_class

end_unit

