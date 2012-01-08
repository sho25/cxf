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
name|transport
operator|.
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Cookie
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
name|HttpServletRequestWrapper
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
name|HttpSession
import|;
end_import

begin_class
specifier|public
class|class
name|HttpServletRequestSnapshot
extends|extends
name|HttpServletRequestWrapper
block|{
specifier|private
name|String
name|authType
decl_stmt|;
specifier|private
name|String
name|characterEncoding
decl_stmt|;
specifier|private
name|int
name|contentLength
decl_stmt|;
specifier|private
name|String
name|contentType
decl_stmt|;
specifier|private
name|String
name|contextPath
decl_stmt|;
specifier|private
name|Cookie
index|[]
name|cookies
decl_stmt|;
specifier|private
name|String
name|localAddr
decl_stmt|;
specifier|private
name|Locale
name|local
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|private
name|Enumeration
name|locals
decl_stmt|;
specifier|private
name|String
name|localName
decl_stmt|;
specifier|private
name|int
name|localPort
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|String
name|method
decl_stmt|;
specifier|private
name|String
name|pathInfo
decl_stmt|;
specifier|private
name|String
name|pathTranslated
decl_stmt|;
specifier|private
name|String
name|protocol
decl_stmt|;
specifier|private
name|String
name|queryString
decl_stmt|;
specifier|private
name|String
name|remoteAddr
decl_stmt|;
specifier|private
name|String
name|remoteHost
decl_stmt|;
specifier|private
name|int
name|remotePort
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|String
name|remoteUser
decl_stmt|;
specifier|private
name|String
name|requestURI
decl_stmt|;
specifier|private
name|StringBuffer
name|requestURL
decl_stmt|;
specifier|private
name|String
name|schema
decl_stmt|;
specifier|private
name|String
name|serverName
decl_stmt|;
specifier|private
name|int
name|serverPort
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|String
name|servletPath
decl_stmt|;
specifier|private
name|HttpSession
name|session
decl_stmt|;
specifier|private
name|Principal
name|principal
decl_stmt|;
specifier|private
name|Enumeration
argument_list|<
name|String
argument_list|>
name|requestHeaderNames
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Enumeration
argument_list|<
name|String
argument_list|>
argument_list|>
name|headersMap
init|=
operator|new
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Enumeration
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|requestedSessionId
decl_stmt|;
specifier|public
name|HttpServletRequestSnapshot
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
name|super
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|authType
operator|=
name|request
operator|.
name|getAuthType
argument_list|()
expr_stmt|;
name|characterEncoding
operator|=
name|request
operator|.
name|getCharacterEncoding
argument_list|()
expr_stmt|;
name|contentLength
operator|=
name|request
operator|.
name|getContentLength
argument_list|()
expr_stmt|;
name|contentType
operator|=
name|request
operator|.
name|getContentType
argument_list|()
expr_stmt|;
name|contextPath
operator|=
name|request
operator|.
name|getContextPath
argument_list|()
expr_stmt|;
name|cookies
operator|=
name|request
operator|.
name|getCookies
argument_list|()
expr_stmt|;
name|requestHeaderNames
operator|=
name|request
operator|.
name|getHeaderNames
argument_list|()
expr_stmt|;
name|Enumeration
argument_list|<
name|String
argument_list|>
name|tmp
init|=
name|request
operator|.
name|getHeaderNames
argument_list|()
decl_stmt|;
while|while
condition|(
name|tmp
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|key
init|=
name|tmp
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|headersMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|request
operator|.
name|getHeaders
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|localAddr
operator|=
name|request
operator|.
name|getLocalAddr
argument_list|()
expr_stmt|;
name|local
operator|=
name|request
operator|.
name|getLocale
argument_list|()
expr_stmt|;
name|localName
operator|=
name|request
operator|.
name|getLocalName
argument_list|()
expr_stmt|;
name|localPort
operator|=
name|request
operator|.
name|getLocalPort
argument_list|()
expr_stmt|;
name|method
operator|=
name|request
operator|.
name|getMethod
argument_list|()
expr_stmt|;
name|pathInfo
operator|=
name|request
operator|.
name|getPathInfo
argument_list|()
expr_stmt|;
name|pathTranslated
operator|=
name|request
operator|.
name|getPathTranslated
argument_list|()
expr_stmt|;
name|protocol
operator|=
name|request
operator|.
name|getProtocol
argument_list|()
expr_stmt|;
name|queryString
operator|=
name|request
operator|.
name|getQueryString
argument_list|()
expr_stmt|;
name|remoteAddr
operator|=
name|request
operator|.
name|getRemoteAddr
argument_list|()
expr_stmt|;
name|remoteHost
operator|=
name|request
operator|.
name|getRemoteHost
argument_list|()
expr_stmt|;
name|remotePort
operator|=
name|request
operator|.
name|getRemotePort
argument_list|()
expr_stmt|;
name|remoteUser
operator|=
name|request
operator|.
name|getRemoteUser
argument_list|()
expr_stmt|;
name|requestURI
operator|=
name|request
operator|.
name|getRequestURI
argument_list|()
expr_stmt|;
name|requestURL
operator|=
name|request
operator|.
name|getRequestURL
argument_list|()
expr_stmt|;
name|requestedSessionId
operator|=
name|request
operator|.
name|getRequestedSessionId
argument_list|()
expr_stmt|;
name|schema
operator|=
name|request
operator|.
name|getScheme
argument_list|()
expr_stmt|;
name|serverName
operator|=
name|request
operator|.
name|getServerName
argument_list|()
expr_stmt|;
name|serverPort
operator|=
name|request
operator|.
name|getServerPort
argument_list|()
expr_stmt|;
name|servletPath
operator|=
name|request
operator|.
name|getServletPath
argument_list|()
expr_stmt|;
if|if
condition|(
name|request
operator|.
name|isRequestedSessionIdValid
argument_list|()
condition|)
block|{
name|session
operator|=
name|request
operator|.
name|getSession
argument_list|()
expr_stmt|;
block|}
name|principal
operator|=
name|request
operator|.
name|getUserPrincipal
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getAuthType
parameter_list|()
block|{
return|return
name|this
operator|.
name|authType
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getContextPath
parameter_list|()
block|{
return|return
name|this
operator|.
name|contextPath
return|;
block|}
annotation|@
name|Override
specifier|public
name|Cookie
index|[]
name|getCookies
parameter_list|()
block|{
return|return
name|this
operator|.
name|cookies
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|headersMap
operator|.
name|get
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
operator|&&
name|headersMap
operator|.
name|get
argument_list|(
name|name
argument_list|)
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
return|return
name|headersMap
operator|.
name|get
argument_list|(
name|name
argument_list|)
operator|.
name|nextElement
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
annotation|@
name|Override
specifier|public
name|Enumeration
name|getHeaderNames
parameter_list|()
block|{
return|return
name|this
operator|.
name|requestHeaderNames
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
annotation|@
name|Override
specifier|public
name|Enumeration
name|getHeaders
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|headersMap
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getMethod
parameter_list|()
block|{
return|return
name|this
operator|.
name|method
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getPathInfo
parameter_list|()
block|{
return|return
name|this
operator|.
name|pathInfo
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getPathTranslated
parameter_list|()
block|{
return|return
name|this
operator|.
name|pathTranslated
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getQueryString
parameter_list|()
block|{
return|return
name|this
operator|.
name|queryString
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRemoteUser
parameter_list|()
block|{
return|return
name|this
operator|.
name|remoteUser
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRequestURI
parameter_list|()
block|{
return|return
name|this
operator|.
name|requestURI
return|;
block|}
annotation|@
name|Override
specifier|public
name|StringBuffer
name|getRequestURL
parameter_list|()
block|{
return|return
name|this
operator|.
name|requestURL
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRequestedSessionId
parameter_list|()
block|{
return|return
name|this
operator|.
name|requestedSessionId
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getServletPath
parameter_list|()
block|{
return|return
name|this
operator|.
name|servletPath
return|;
block|}
annotation|@
name|Override
specifier|public
name|HttpSession
name|getSession
parameter_list|()
block|{
return|return
name|this
operator|.
name|session
return|;
block|}
annotation|@
name|Override
specifier|public
name|Principal
name|getUserPrincipal
parameter_list|()
block|{
return|return
name|this
operator|.
name|principal
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getCharacterEncoding
parameter_list|()
block|{
return|return
name|this
operator|.
name|characterEncoding
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getContentLength
parameter_list|()
block|{
return|return
name|this
operator|.
name|contentLength
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
name|this
operator|.
name|contentType
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getLocalAddr
parameter_list|()
block|{
return|return
name|this
operator|.
name|localAddr
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getLocalName
parameter_list|()
block|{
return|return
name|this
operator|.
name|localName
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getLocalPort
parameter_list|()
block|{
return|return
name|this
operator|.
name|localPort
return|;
block|}
annotation|@
name|Override
specifier|public
name|Locale
name|getLocale
parameter_list|()
block|{
return|return
name|this
operator|.
name|local
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
annotation|@
name|Override
specifier|public
name|Enumeration
name|getLocales
parameter_list|()
block|{
return|return
name|this
operator|.
name|locals
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|this
operator|.
name|protocol
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRemoteAddr
parameter_list|()
block|{
return|return
name|this
operator|.
name|remoteAddr
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRemoteHost
parameter_list|()
block|{
return|return
name|this
operator|.
name|remoteHost
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getRemotePort
parameter_list|()
block|{
return|return
name|this
operator|.
name|remotePort
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
name|this
operator|.
name|schema
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getServerName
parameter_list|()
block|{
return|return
name|this
operator|.
name|serverName
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getServerPort
parameter_list|()
block|{
return|return
name|this
operator|.
name|serverPort
return|;
block|}
block|}
end_class

end_unit

