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
name|provider
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
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ResourceBundle
import|;
end_import

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
name|servlet
operator|.
name|RequestDispatcher
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContext
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
name|PathSegment
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
name|UriInfo
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
name|MessageBodyWriter
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|BundleUtils
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
name|phase
operator|.
name|PhaseInterceptorChain
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
name|transport
operator|.
name|http
operator|.
name|AbstractHTTPDestination
import|;
end_import

begin_class
annotation|@
name|Produces
argument_list|(
literal|"text/html"
argument_list|)
annotation|@
name|Provider
specifier|public
class|class
name|RequestDispatcherProvider
extends|extends
name|AbstractConfigurableProvider
implements|implements
name|MessageBodyWriter
argument_list|<
name|Object
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|RequestDispatcherProvider
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|RequestDispatcherProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ABSOLUTE_PATH_PARAMETER
init|=
literal|"absolute.path"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BASE_PATH_PARAMETER
init|=
literal|"base.path"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RELATIVE_PATH_PARAMETER
init|=
literal|"relative.path"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST_SCOPE
init|=
literal|"request"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SESSION_SCOPE
init|=
literal|"session"
decl_stmt|;
specifier|private
name|String
name|servletContextPath
decl_stmt|;
specifier|private
name|String
name|resourcePath
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|resourcePaths
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|classResources
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
specifier|private
name|String
name|scope
init|=
name|REQUEST_SCOPE
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|beanNames
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
specifier|private
name|String
name|beanName
decl_stmt|;
specifier|private
name|String
name|dispatcherName
decl_stmt|;
specifier|private
name|String
name|servletPath
decl_stmt|;
annotation|@
name|Context
specifier|private
name|MessageContext
name|mc
decl_stmt|;
specifier|public
name|long
name|getSize
parameter_list|(
name|Object
name|t
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
return|return
operator|-
literal|1
return|;
block|}
specifier|public
name|boolean
name|isWriteable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
if|if
condition|(
name|resourcePath
operator|!=
literal|null
operator|||
name|classResources
operator|.
name|containsKey
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
name|resourcePaths
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|path
init|=
name|getRequestPath
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|requestPath
range|:
name|resourcePaths
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|path
operator|.
name|endsWith
argument_list|(
name|requestPath
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|writeTo
parameter_list|(
name|Object
name|o
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|type
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
block|{
name|ServletContext
name|sc
init|=
name|getServletContext
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|getResourcePath
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|RequestDispatcher
name|rd
init|=
name|getRequestDispatcher
argument_list|(
name|sc
argument_list|,
name|clazz
argument_list|,
name|path
argument_list|)
decl_stmt|;
try|try
block|{
name|mc
operator|.
name|put
argument_list|(
name|AbstractHTTPDestination
operator|.
name|REQUEST_REDIRECTED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|String
name|theServletPath
init|=
name|servletPath
operator|==
literal|null
condition|?
literal|"/"
else|:
name|servletPath
decl_stmt|;
name|HttpServletRequestFilter
name|servletRequest
init|=
operator|new
name|HttpServletRequestFilter
argument_list|(
name|mc
operator|.
name|getHttpServletRequest
argument_list|()
argument_list|,
name|path
argument_list|,
name|theServletPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|REQUEST_SCOPE
operator|.
name|equals
argument_list|(
name|scope
argument_list|)
condition|)
block|{
name|servletRequest
operator|.
name|setAttribute
argument_list|(
name|getBeanName
argument_list|(
name|o
argument_list|)
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SESSION_SCOPE
operator|.
name|equals
argument_list|(
name|scope
argument_list|)
condition|)
block|{
name|servletRequest
operator|.
name|getSession
argument_list|(
literal|true
argument_list|)
operator|.
name|setAttribute
argument_list|(
name|getBeanName
argument_list|(
name|o
argument_list|)
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
name|setRequestParameters
argument_list|(
name|servletRequest
argument_list|)
expr_stmt|;
name|rd
operator|.
name|forward
argument_list|(
name|servletRequest
argument_list|,
name|mc
operator|.
name|getHttpServletResponse
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|mc
operator|.
name|put
argument_list|(
name|AbstractHTTPDestination
operator|.
name|REQUEST_REDIRECTED
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
name|String
name|getResourcePath
parameter_list|(
name|String
name|clsName
parameter_list|)
block|{
name|String
name|clsResourcePath
init|=
name|classResources
operator|.
name|get
argument_list|(
name|clsName
argument_list|)
decl_stmt|;
if|if
condition|(
name|clsResourcePath
operator|!=
literal|null
condition|)
block|{
return|return
name|clsResourcePath
return|;
block|}
if|if
condition|(
name|resourcePath
operator|!=
literal|null
condition|)
block|{
return|return
name|resourcePath
return|;
block|}
name|String
name|path
init|=
name|getRequestPath
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|requestPath
range|:
name|resourcePaths
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|path
operator|.
name|endsWith
argument_list|(
name|requestPath
argument_list|)
condition|)
block|{
return|return
name|resourcePaths
operator|.
name|get
argument_list|(
name|requestPath
argument_list|)
return|;
block|}
block|}
comment|// won't happen given that isWriteable() returned true
return|return
literal|null
return|;
block|}
specifier|private
name|String
name|getRequestPath
parameter_list|()
block|{
name|Message
name|inMessage
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
return|return
operator|(
name|String
operator|)
name|inMessage
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUEST_URI
argument_list|)
return|;
block|}
specifier|protected
name|ServletContext
name|getServletContext
parameter_list|()
block|{
name|ServletContext
name|sc
init|=
name|mc
operator|.
name|getServletContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|servletContextPath
operator|!=
literal|null
condition|)
block|{
name|sc
operator|=
name|sc
operator|.
name|getContext
argument_list|(
name|servletContextPath
argument_list|)
expr_stmt|;
if|if
condition|(
name|sc
operator|==
literal|null
condition|)
block|{
name|String
name|message
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"RESOURCE_DISPATCH_NOT_FOUND"
argument_list|,
name|BUNDLE
argument_list|,
name|servletContextPath
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|severe
argument_list|(
name|message
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
block|}
return|return
name|sc
return|;
block|}
specifier|protected
name|RequestDispatcher
name|getRequestDispatcher
parameter_list|(
name|ServletContext
name|sc
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|RequestDispatcher
name|rd
init|=
name|dispatcherName
operator|!=
literal|null
condition|?
name|sc
operator|.
name|getNamedDispatcher
argument_list|(
name|dispatcherName
argument_list|)
else|:
name|sc
operator|.
name|getRequestDispatcher
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|rd
operator|==
literal|null
condition|)
block|{
name|String
name|message
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"RESOURCE_PATH_NOT_FOUND"
argument_list|,
name|BUNDLE
argument_list|,
name|path
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|severe
argument_list|(
name|message
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
return|return
name|rd
return|;
block|}
specifier|public
name|void
name|setResourcePath
parameter_list|(
name|String
name|resourcePath
parameter_list|)
block|{
name|this
operator|.
name|resourcePath
operator|=
name|resourcePath
expr_stmt|;
block|}
specifier|public
name|void
name|setServletContextPath
parameter_list|(
name|String
name|servletContextPath
parameter_list|)
block|{
name|this
operator|.
name|servletContextPath
operator|=
name|servletContextPath
expr_stmt|;
block|}
specifier|public
name|void
name|setScope
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|scope
expr_stmt|;
block|}
specifier|public
name|void
name|setBeanNames
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|beanNames
parameter_list|)
block|{
name|this
operator|.
name|beanNames
operator|=
name|beanNames
expr_stmt|;
block|}
specifier|public
name|void
name|setBeanName
parameter_list|(
name|String
name|beanName
parameter_list|)
block|{
name|this
operator|.
name|beanName
operator|=
name|beanName
expr_stmt|;
block|}
specifier|protected
name|String
name|getBeanName
parameter_list|(
name|Object
name|bean
parameter_list|)
block|{
if|if
condition|(
name|beanName
operator|!=
literal|null
condition|)
block|{
return|return
name|beanName
return|;
block|}
name|String
name|name
init|=
name|beanNames
operator|.
name|get
argument_list|(
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|name
operator|!=
literal|null
condition|?
name|name
else|:
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
return|;
block|}
specifier|protected
name|void
name|setRequestParameters
parameter_list|(
name|HttpServletRequestFilter
name|request
parameter_list|)
block|{
if|if
condition|(
name|mc
operator|!=
literal|null
condition|)
block|{
name|UriInfo
name|ui
init|=
name|mc
operator|.
name|getUriInfo
argument_list|()
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
name|ui
operator|.
name|getPathParameters
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|params
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|int
name|ind
init|=
name|value
operator|.
name|indexOf
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ind
operator|>
literal|0
condition|)
block|{
name|value
operator|=
name|value
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|ind
argument_list|)
expr_stmt|;
block|}
name|request
operator|.
name|setParameter
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|PathSegment
argument_list|>
name|segments
init|=
name|ui
operator|.
name|getPathSegments
argument_list|()
decl_stmt|;
if|if
condition|(
name|segments
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|doSetRequestParameters
argument_list|(
name|request
argument_list|,
name|segments
operator|.
name|get
argument_list|(
name|segments
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|getMatrixParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|doSetRequestParameters
argument_list|(
name|request
argument_list|,
name|ui
operator|.
name|getQueryParameters
argument_list|()
argument_list|)
expr_stmt|;
name|request
operator|.
name|setParameter
argument_list|(
name|ABSOLUTE_PATH_PARAMETER
argument_list|,
name|ui
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|request
operator|.
name|setParameter
argument_list|(
name|RELATIVE_PATH_PARAMETER
argument_list|,
name|ui
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|request
operator|.
name|setParameter
argument_list|(
name|BASE_PATH_PARAMETER
argument_list|,
name|ui
operator|.
name|getBaseUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|doSetRequestParameters
parameter_list|(
name|HttpServletRequestFilter
name|req
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|params
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|req
operator|.
name|setParameters
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setDispatcherName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|dispatcherName
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|void
name|setServletPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|servletPath
operator|=
name|path
expr_stmt|;
block|}
specifier|public
name|void
name|setResourcePaths
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|resourcePaths
parameter_list|)
block|{
name|this
operator|.
name|resourcePaths
operator|=
name|resourcePaths
expr_stmt|;
block|}
specifier|protected
specifier|static
class|class
name|HttpServletRequestFilter
extends|extends
name|HttpServletRequestWrapper
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|params
decl_stmt|;
specifier|private
name|String
name|path
decl_stmt|;
specifier|private
name|String
name|servletPath
decl_stmt|;
specifier|public
name|HttpServletRequestFilter
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|servletPath
parameter_list|)
block|{
name|super
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|servletPath
operator|=
name|servletPath
expr_stmt|;
name|params
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
argument_list|(
name|request
operator|.
name|getParameterMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getServletPath
parameter_list|()
block|{
return|return
name|servletPath
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
name|path
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
name|path
return|;
block|}
specifier|public
name|void
name|setParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|params
operator|.
name|put
argument_list|(
name|name
argument_list|,
operator|new
name|String
index|[]
block|{
name|value
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setParameters
parameter_list|(
name|String
name|name
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|)
block|{
name|params
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|values
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[]
block|{}
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getParameter
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
index|[]
name|values
init|=
name|params
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|==
literal|null
operator|||
name|values
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|values
index|[
literal|0
index|]
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|getParameterMap
parameter_list|()
block|{
return|return
name|params
return|;
block|}
block|}
block|}
end_class

end_unit

