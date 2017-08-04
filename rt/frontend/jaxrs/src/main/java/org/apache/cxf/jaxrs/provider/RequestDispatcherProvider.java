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
name|Level
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
name|utils
operator|.
name|ExceptionUtils
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
name|ResourceUtils
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
name|WEBAPP_BASE_PATH_PARAMETER
init|=
literal|"webapp.base.path"
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
specifier|static
specifier|final
name|String
name|MESSAGE_RESOURCE_PATH_PROPERTY
init|=
literal|"redirect.resource.path"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_RESOURCE_EXTENSION
init|=
literal|".jsp"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_LOCATION_PREFIX
init|=
literal|"/WEB-INF/"
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
name|Map
argument_list|<
name|?
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
argument_list|,
name|String
argument_list|>
name|enumResources
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|useClassNames
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
specifier|private
name|boolean
name|useCurrentServlet
decl_stmt|;
specifier|private
name|boolean
name|saveParametersAsAttributes
decl_stmt|;
specifier|private
name|boolean
name|logRedirects
decl_stmt|;
specifier|private
name|boolean
name|strictPathCheck
decl_stmt|;
specifier|private
name|String
name|locationPrefix
decl_stmt|;
specifier|private
name|String
name|resourceExtension
decl_stmt|;
specifier|private
name|boolean
name|includeResource
decl_stmt|;
specifier|private
name|MessageContext
name|mc
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
name|void
name|setStrictPathCheck
parameter_list|(
name|boolean
name|use
parameter_list|)
block|{
name|strictPathCheck
operator|=
name|use
expr_stmt|;
block|}
specifier|public
name|void
name|setUseClassNames
parameter_list|(
name|boolean
name|use
parameter_list|)
block|{
name|useClassNames
operator|=
name|use
expr_stmt|;
block|}
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
specifier|private
name|String
name|getClassResourceName
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|String
name|resourceName
init|=
name|doGetClassResourceName
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|resourceName
operator|==
literal|null
condition|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|in
range|:
name|type
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
name|resourceName
operator|=
name|doGetClassResourceName
argument_list|(
name|in
argument_list|)
expr_stmt|;
if|if
condition|(
name|resourceName
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
block|}
return|return
name|resourceName
return|;
block|}
specifier|private
name|String
name|doGetClassResourceName
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|String
name|simpleName
init|=
name|StringUtils
operator|.
name|uncapitalize
argument_list|(
name|type
operator|.
name|getSimpleName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|thePrefix
init|=
name|locationPrefix
operator|==
literal|null
condition|?
name|DEFAULT_LOCATION_PREFIX
else|:
name|locationPrefix
decl_stmt|;
name|String
name|theExtension
init|=
name|resourceExtension
operator|==
literal|null
condition|?
name|DEFAULT_RESOURCE_EXTENSION
else|:
name|resourceExtension
decl_stmt|;
name|String
name|resourceName
init|=
name|thePrefix
operator|+
name|simpleName
operator|+
name|theExtension
decl_stmt|;
if|if
condition|(
name|ResourceUtils
operator|.
name|getClasspathResourceURL
argument_list|(
name|resourceName
argument_list|,
name|RequestDispatcherProvider
operator|.
name|class
argument_list|,
name|getBus
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
name|resourceName
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
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
name|useClassNames
operator|&&
name|getClassResourceName
argument_list|(
name|type
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|resourcePath
operator|!=
literal|null
operator|||
name|classResourceSupported
argument_list|(
name|type
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
name|boolean
name|result
init|=
name|strictPathCheck
condition|?
name|path
operator|.
name|endsWith
argument_list|(
name|requestPath
argument_list|)
else|:
name|path
operator|.
name|contains
argument_list|(
name|requestPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
name|mc
operator|!=
literal|null
operator|&&
name|mc
operator|.
name|get
argument_list|(
name|MESSAGE_RESOURCE_PATH_PROPERTY
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|private
name|boolean
name|classResourceSupported
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|String
name|typeName
init|=
name|type
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|isEnum
argument_list|()
condition|)
block|{
for|for
control|(
name|Object
name|o
range|:
name|enumResources
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|o
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|typeName
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
for|for
control|(
name|String
name|name
range|:
name|classResources
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|typeName
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
else|else
block|{
return|return
name|classResources
operator|.
name|containsKey
argument_list|(
name|typeName
argument_list|)
return|;
block|}
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
name|HttpServletRequest
name|servletRequest
init|=
name|mc
operator|.
name|getHttpServletRequest
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|getResourcePath
argument_list|(
name|clazz
argument_list|,
name|o
argument_list|)
decl_stmt|;
name|String
name|theServletPath
init|=
name|servletPath
operator|!=
literal|null
condition|?
name|servletPath
else|:
name|useCurrentServlet
condition|?
name|servletRequest
operator|.
name|getServletPath
argument_list|()
else|:
literal|"/"
decl_stmt|;
if|if
condition|(
name|theServletPath
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
operator|&&
name|path
operator|!=
literal|null
operator|&&
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|theServletPath
operator|=
name|theServletPath
operator|.
name|length
argument_list|()
operator|==
literal|1
condition|?
literal|""
else|:
name|theServletPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|theServletPath
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|theServletPath
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
operator|&&
name|path
operator|!=
literal|null
operator|&&
operator|!
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
literal|"/"
operator|+
name|path
expr_stmt|;
block|}
name|RequestDispatcher
name|rd
init|=
name|getRequestDispatcher
argument_list|(
name|sc
argument_list|,
name|clazz
argument_list|,
name|theServletPath
operator|+
name|path
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|includeResource
condition|)
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
block|}
name|HttpServletRequestFilter
name|requestFilter
init|=
operator|new
name|HttpServletRequestFilter
argument_list|(
name|servletRequest
argument_list|,
name|path
argument_list|,
name|theServletPath
argument_list|,
name|saveParametersAsAttributes
argument_list|)
decl_stmt|;
name|String
name|attributeName
init|=
name|getBeanName
argument_list|(
name|o
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
name|requestFilter
operator|.
name|setAttribute
argument_list|(
name|attributeName
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
name|requestFilter
operator|.
name|getSession
argument_list|(
literal|true
argument_list|)
operator|.
name|setAttribute
argument_list|(
name|attributeName
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
name|setRequestParameters
argument_list|(
name|requestFilter
argument_list|)
expr_stmt|;
name|logRedirection
argument_list|(
name|path
argument_list|,
name|attributeName
argument_list|,
name|o
argument_list|)
expr_stmt|;
if|if
condition|(
name|includeResource
condition|)
block|{
name|rd
operator|.
name|include
argument_list|(
name|requestFilter
argument_list|,
name|mc
operator|.
name|getHttpServletResponse
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|rd
operator|.
name|forward
argument_list|(
name|requestFilter
argument_list|,
name|mc
operator|.
name|getHttpServletResponse
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|LOG
operator|.
name|warning
argument_list|(
name|ExceptionUtils
operator|.
name|getStackTrace
argument_list|(
name|ex
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
name|ex
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|logRedirection
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|attributeName
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
name|Level
name|level
init|=
name|logRedirects
condition|?
name|Level
operator|.
name|INFO
else|:
name|Level
operator|.
name|FINE
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|level
argument_list|)
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
literal|"RESPONSE_REDIRECTED_TO"
argument_list|,
name|BUNDLE
argument_list|,
name|o
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|attributeName
argument_list|,
name|path
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|level
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|getResourcePath
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
name|String
name|currentResourcePath
init|=
name|getPathFromMessageContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentResourcePath
operator|!=
literal|null
condition|)
block|{
return|return
name|currentResourcePath
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
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|resourcePaths
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|path
operator|.
name|endsWith
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|entry
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|enumResources
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|classResources
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|name
init|=
name|cls
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|cls
operator|.
name|isEnum
argument_list|()
condition|)
block|{
name|String
name|enumResource
init|=
name|enumResources
operator|.
name|get
argument_list|(
name|o
argument_list|)
decl_stmt|;
if|if
condition|(
name|enumResource
operator|!=
literal|null
condition|)
block|{
return|return
name|enumResource
return|;
block|}
name|name
operator|+=
literal|"."
operator|+
name|o
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|String
name|clsResourcePath
init|=
name|classResources
operator|.
name|get
argument_list|(
name|name
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
block|}
if|if
condition|(
name|useClassNames
condition|)
block|{
return|return
name|getClassResourceName
argument_list|(
name|cls
argument_list|)
return|;
block|}
return|return
name|resourcePath
return|;
block|}
specifier|private
name|String
name|getPathFromMessageContext
parameter_list|()
block|{
if|if
condition|(
name|mc
operator|!=
literal|null
condition|)
block|{
name|Object
name|resourcePathProp
init|=
name|mc
operator|.
name|get
argument_list|(
name|MESSAGE_RESOURCE_PATH_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|resourcePathProp
operator|!=
literal|null
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|locationPrefix
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|locationPrefix
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|resourcePathProp
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|resourceExtension
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|resourceExtension
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
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
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
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
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
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
specifier|public
name|void
name|setLogRedirects
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|logRedirects
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
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
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
return|return
name|name
return|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|resourceClass
init|=
name|bean
operator|.
name|getClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|useClassNames
operator|&&
name|doGetClassResourceName
argument_list|(
name|resourceClass
argument_list|)
operator|==
literal|null
condition|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
range|:
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
if|if
condition|(
name|doGetClassResourceName
argument_list|(
name|cls
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|resourceClass
operator|=
name|cls
expr_stmt|;
break|break;
block|}
block|}
block|}
return|return
name|resourceClass
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
operator|!
name|segments
operator|.
name|isEmpty
argument_list|()
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
name|request
operator|.
name|setParameter
argument_list|(
name|WEBAPP_BASE_PATH_PARAMETER
argument_list|,
operator|(
name|String
operator|)
name|mc
operator|.
name|get
argument_list|(
literal|"http.base.path"
argument_list|)
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
specifier|public
name|void
name|setClassResources
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|resources
parameter_list|)
block|{
name|this
operator|.
name|classResources
operator|=
name|resources
expr_stmt|;
block|}
specifier|public
name|void
name|setSaveParametersAsAttributes
parameter_list|(
name|boolean
name|saveParametersAsAttributes
parameter_list|)
block|{
name|this
operator|.
name|saveParametersAsAttributes
operator|=
name|saveParametersAsAttributes
expr_stmt|;
block|}
specifier|public
name|void
name|setEnumResources
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
argument_list|,
name|String
argument_list|>
name|enumResources
parameter_list|)
block|{
name|this
operator|.
name|enumResources
operator|=
name|enumResources
expr_stmt|;
block|}
specifier|public
name|void
name|setUseCurrentServlet
parameter_list|(
name|boolean
name|useCurrentServlet
parameter_list|)
block|{
name|this
operator|.
name|useCurrentServlet
operator|=
name|useCurrentServlet
expr_stmt|;
block|}
specifier|public
name|void
name|setIncludeResource
parameter_list|(
name|boolean
name|includeResource
parameter_list|)
block|{
name|this
operator|.
name|includeResource
operator|=
name|includeResource
expr_stmt|;
block|}
specifier|public
name|void
name|setLocationPrefix
parameter_list|(
name|String
name|locationPrefix
parameter_list|)
block|{
name|this
operator|.
name|locationPrefix
operator|=
name|locationPrefix
expr_stmt|;
block|}
specifier|public
name|void
name|setResourceExtension
parameter_list|(
name|String
name|resourceExtension
parameter_list|)
block|{
name|this
operator|.
name|resourceExtension
operator|=
name|resourceExtension
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
specifier|private
name|boolean
name|saveParamsAsAttributes
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
parameter_list|,
name|boolean
name|saveParamsAsAttributes
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
name|this
operator|.
name|saveParamsAsAttributes
operator|=
name|saveParamsAsAttributes
expr_stmt|;
name|params
operator|=
operator|new
name|HashMap
argument_list|<>
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
name|doSetParameters
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
name|doSetParameters
argument_list|(
name|name
argument_list|,
name|values
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|values
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doSetParameters
parameter_list|(
name|String
name|name
parameter_list|,
name|String
index|[]
name|values
parameter_list|)
block|{
if|if
condition|(
name|saveParamsAsAttributes
condition|)
block|{
name|super
operator|.
name|setAttribute
argument_list|(
name|name
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|params
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
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

