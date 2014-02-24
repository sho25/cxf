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
name|servlet
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
name|InputStream
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
name|Collections
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Properties
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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|Filter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterConfig
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
name|ServletConfig
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
name|ServletException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletOutputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
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
name|HttpServlet
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
name|HttpServletResponse
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
name|Bus
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|helpers
operator|.
name|IOUtils
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
name|resource
operator|.
name|ResourceManager
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
specifier|public
specifier|abstract
class|class
name|AbstractHTTPServlet
extends|extends
name|HttpServlet
implements|implements
name|Filter
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|8357252743467075117L
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
name|AbstractHTTPServlet
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|AbstractHTTPServlet
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * List of well-known HTTP 1.1 verbs, with POST and GET being the most used verbs at the top       */
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|KNOWN_HTTP_VERBS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"POST"
block|,
literal|"GET"
block|,
literal|"PUT"
block|,
literal|"DELETE"
block|,
literal|"HEAD"
block|,
literal|"OPTIONS"
block|,
literal|"TRACE"
block|}
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|STATIC_RESOURCES_PARAMETER
init|=
literal|"static-resources-list"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|STATIC_WELCOME_FILE_PARAMETER
init|=
literal|"static-welcome-file"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|STATIC_RESOURCES_MAP_RESOURCE
init|=
literal|"/cxfServletStaticResourcesMap.txt"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REDIRECTS_PARAMETER
init|=
literal|"redirects-list"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REDIRECT_SERVLET_NAME_PARAMETER
init|=
literal|"redirect-servlet-name"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REDIRECT_SERVLET_PATH_PARAMETER
init|=
literal|"redirect-servlet-path"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REDIRECT_ATTRIBUTES_PARAMETER
init|=
literal|"redirect-attributes"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REDIRECT_QUERY_CHECK_PARAMETER
init|=
literal|"redirect-query-check"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|DEFAULT_STATIC_CONTENT_TYPES
decl_stmt|;
static|static
block|{
name|DEFAULT_STATIC_CONTENT_TYPES
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|DEFAULT_STATIC_CONTENT_TYPES
operator|.
name|put
argument_list|(
literal|"html"
argument_list|,
literal|"text/html"
argument_list|)
expr_stmt|;
name|DEFAULT_STATIC_CONTENT_TYPES
operator|.
name|put
argument_list|(
literal|"txt"
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
name|DEFAULT_STATIC_CONTENT_TYPES
operator|.
name|put
argument_list|(
literal|"css"
argument_list|,
literal|"text/css"
argument_list|)
expr_stmt|;
name|DEFAULT_STATIC_CONTENT_TYPES
operator|.
name|put
argument_list|(
literal|"pdf"
argument_list|,
literal|"application/pdf"
argument_list|)
expr_stmt|;
name|DEFAULT_STATIC_CONTENT_TYPES
operator|.
name|put
argument_list|(
literal|"xsd"
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
name|DEFAULT_STATIC_CONTENT_TYPES
operator|.
name|put
argument_list|(
literal|"js"
argument_list|,
literal|"application/javascript"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|List
argument_list|<
name|Pattern
argument_list|>
name|staticResourcesList
decl_stmt|;
specifier|private
name|String
name|staticWelcomeFile
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Pattern
argument_list|>
name|redirectList
decl_stmt|;
specifier|private
name|String
name|dispatcherServletPath
decl_stmt|;
specifier|private
name|String
name|dispatcherServletName
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|redirectAttributes
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|staticContentTypes
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|DEFAULT_STATIC_CONTENT_TYPES
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|redirectQueryCheck
decl_stmt|;
specifier|public
name|void
name|init
parameter_list|(
name|ServletConfig
name|servletConfig
parameter_list|)
throws|throws
name|ServletException
block|{
name|super
operator|.
name|init
argument_list|(
name|servletConfig
argument_list|)
expr_stmt|;
name|staticResourcesList
operator|=
name|parseListSequence
argument_list|(
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
name|STATIC_RESOURCES_PARAMETER
argument_list|)
argument_list|)
expr_stmt|;
name|staticWelcomeFile
operator|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
name|STATIC_WELCOME_FILE_PARAMETER
argument_list|)
expr_stmt|;
name|redirectList
operator|=
name|parseListSequence
argument_list|(
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
name|REDIRECTS_PARAMETER
argument_list|)
argument_list|)
expr_stmt|;
name|redirectQueryCheck
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
name|REDIRECT_QUERY_CHECK_PARAMETER
argument_list|)
argument_list|)
expr_stmt|;
name|dispatcherServletName
operator|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
name|REDIRECT_SERVLET_NAME_PARAMETER
argument_list|)
expr_stmt|;
name|dispatcherServletPath
operator|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
name|REDIRECT_SERVLET_PATH_PARAMETER
argument_list|)
expr_stmt|;
name|redirectAttributes
operator|=
name|parseMapSequence
argument_list|(
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
name|REDIRECT_ATTRIBUTES_PARAMETER
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|finalizeServletInit
parameter_list|(
name|ServletConfig
name|servletConfig
parameter_list|)
block|{
name|InputStream
name|is
init|=
name|getResourceAsStream
argument_list|(
literal|"/WEB-INF"
operator|+
name|STATIC_RESOURCES_MAP_RESOURCE
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
name|is
operator|=
name|getResourceAsStream
argument_list|(
name|STATIC_RESOURCES_MAP_RESOURCE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|name
range|:
name|props
operator|.
name|stringPropertyNames
argument_list|()
control|)
block|{
name|staticContentTypes
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|props
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
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
literal|"STATIC_RESOURCES_MAP_LOAD_FAILURE"
argument_list|,
name|BUNDLE
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|InputStream
name|getResourceAsStream
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|InputStream
name|is
init|=
name|ClassLoaderUtils
operator|.
name|getResourceAsStream
argument_list|(
name|path
argument_list|,
name|AbstractHTTPServlet
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
operator|&&
name|getBus
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ResourceManager
name|rm
init|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|rm
operator|!=
literal|null
condition|)
block|{
name|is
operator|=
name|rm
operator|.
name|resolveResource
argument_list|(
name|path
argument_list|,
name|InputStream
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|is
return|;
block|}
specifier|public
specifier|final
name|void
name|init
parameter_list|(
specifier|final
name|FilterConfig
name|filterConfig
parameter_list|)
throws|throws
name|ServletException
block|{
name|init
argument_list|(
operator|new
name|ServletConfig
argument_list|()
block|{
specifier|public
name|String
name|getServletName
parameter_list|()
block|{
return|return
name|filterConfig
operator|.
name|getFilterName
argument_list|()
return|;
block|}
specifier|public
name|ServletContext
name|getServletContext
parameter_list|()
block|{
return|return
name|filterConfig
operator|.
name|getServletContext
argument_list|()
return|;
block|}
specifier|public
name|String
name|getInitParameter
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|filterConfig
operator|.
name|getInitParameter
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|Enumeration
argument_list|<
name|String
argument_list|>
name|getInitParameterNames
parameter_list|()
block|{
return|return
name|filterConfig
operator|.
name|getInitParameterNames
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
name|List
argument_list|<
name|Pattern
argument_list|>
name|parseListSequence
parameter_list|(
name|String
name|values
parameter_list|)
block|{
if|if
condition|(
name|values
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Pattern
argument_list|>
name|list
init|=
operator|new
name|LinkedList
argument_list|<
name|Pattern
argument_list|>
argument_list|()
decl_stmt|;
name|String
index|[]
name|pathValues
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|values
argument_list|,
literal|" "
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|value
range|:
name|pathValues
control|)
block|{
name|String
name|theValue
init|=
name|value
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|theValue
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
name|theValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|list
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|protected
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parseMapSequence
parameter_list|(
name|String
name|sequence
parameter_list|)
block|{
if|if
condition|(
name|sequence
operator|!=
literal|null
condition|)
block|{
name|sequence
operator|=
name|sequence
operator|.
name|trim
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|String
index|[]
name|pairs
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|sequence
argument_list|,
literal|" "
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|pair
range|:
name|pairs
control|)
block|{
name|String
name|thePair
init|=
name|pair
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|thePair
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
continue|continue;
block|}
name|String
index|[]
name|value
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|thePair
argument_list|,
literal|"="
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|length
operator|==
literal|2
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|value
index|[
literal|0
index|]
operator|.
name|trim
argument_list|()
argument_list|,
name|value
index|[
literal|1
index|]
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|map
operator|.
name|put
argument_list|(
name|thePair
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|map
return|;
block|}
else|else
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
block|}
specifier|protected
name|void
name|doPost
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|ServletException
block|{
name|handleRequest
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doGet
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|ServletException
block|{
name|handleRequest
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doDelete
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
name|handleRequest
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doPut
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
name|handleRequest
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doHead
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
name|handleRequest
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doOptions
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
name|handleRequest
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      *       * javax.http.servlet.HttpServlet does not let to override the code which deals with      * unrecognized HTTP verbs such as PATCH (being standardized), WebDav ones, etc.      * Thus we let CXF servlets process unrecognized HTTP verbs directly, otherwise we delegate      * to HttpService        */
annotation|@
name|Override
specifier|public
name|void
name|service
parameter_list|(
name|ServletRequest
name|req
parameter_list|,
name|ServletResponse
name|res
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
name|HttpServletRequest
name|request
decl_stmt|;
name|HttpServletResponse
name|response
decl_stmt|;
try|try
block|{
name|request
operator|=
operator|(
name|HttpServletRequest
operator|)
name|req
expr_stmt|;
name|response
operator|=
operator|(
name|HttpServletResponse
operator|)
name|res
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Unrecognized HTTP request or response object"
argument_list|)
throw|;
block|}
name|String
name|method
init|=
name|request
operator|.
name|getMethod
argument_list|()
decl_stmt|;
if|if
condition|(
name|KNOWN_HTTP_VERBS
operator|.
name|contains
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|super
operator|.
name|service
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|handleRequest
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|handleRequest
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|ServletException
block|{
if|if
condition|(
operator|(
name|dispatcherServletPath
operator|!=
literal|null
operator|||
name|dispatcherServletName
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|redirectList
operator|!=
literal|null
operator|&&
name|matchPath
argument_list|(
name|redirectList
argument_list|,
name|request
argument_list|)
operator|||
name|redirectList
operator|==
literal|null
operator|)
condition|)
block|{
comment|// if no redirectList is provided then this servlet is redirecting only
name|redirect
argument_list|(
name|request
argument_list|,
name|response
argument_list|,
name|request
operator|.
name|getPathInfo
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|boolean
name|staticResourcesMatch
init|=
name|staticResourcesList
operator|!=
literal|null
operator|&&
name|matchPath
argument_list|(
name|staticResourcesList
argument_list|,
name|request
argument_list|)
decl_stmt|;
name|boolean
name|staticWelcomeFileMatch
init|=
name|staticWelcomeFile
operator|!=
literal|null
operator|&&
operator|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|request
operator|.
name|getPathInfo
argument_list|()
argument_list|)
operator|||
name|request
operator|.
name|getPathInfo
argument_list|()
operator|.
name|equals
argument_list|(
literal|"/"
argument_list|)
operator|)
decl_stmt|;
if|if
condition|(
name|staticResourcesMatch
operator|||
name|staticWelcomeFileMatch
condition|)
block|{
name|serveStaticContent
argument_list|(
name|request
argument_list|,
name|response
argument_list|,
name|staticWelcomeFileMatch
condition|?
name|staticWelcomeFile
else|:
name|request
operator|.
name|getPathInfo
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|invoke
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|matchPath
parameter_list|(
name|List
argument_list|<
name|Pattern
argument_list|>
name|values
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|)
block|{
name|String
name|path
init|=
name|request
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
name|path
operator|=
literal|"/"
expr_stmt|;
block|}
if|if
condition|(
name|redirectQueryCheck
condition|)
block|{
name|String
name|queryString
init|=
name|request
operator|.
name|getQueryString
argument_list|()
decl_stmt|;
if|if
condition|(
name|queryString
operator|!=
literal|null
operator|&&
name|queryString
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|path
operator|+=
literal|"?"
operator|+
name|queryString
expr_stmt|;
block|}
block|}
for|for
control|(
name|Pattern
name|pattern
range|:
name|values
control|)
block|{
if|if
condition|(
name|pattern
operator|.
name|matcher
argument_list|(
name|path
argument_list|)
operator|.
name|matches
argument_list|()
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
specifier|protected
specifier|abstract
name|Bus
name|getBus
parameter_list|()
function_decl|;
specifier|protected
name|void
name|serveStaticContent
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|,
name|String
name|pathInfo
parameter_list|)
throws|throws
name|ServletException
block|{
name|InputStream
name|is
init|=
name|getResourceAsStream
argument_list|(
name|pathInfo
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Static resource "
operator|+
name|pathInfo
operator|+
literal|" is not available"
argument_list|)
throw|;
block|}
try|try
block|{
name|int
name|ind
init|=
name|pathInfo
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
if|if
condition|(
name|ind
operator|!=
operator|-
literal|1
operator|&&
name|ind
operator|<
name|pathInfo
operator|.
name|length
argument_list|()
condition|)
block|{
name|String
name|type
init|=
name|getStaticResourceContentType
argument_list|(
name|pathInfo
operator|.
name|substring
argument_list|(
name|ind
operator|+
literal|1
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|response
operator|.
name|setContentType
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
block|}
name|ServletOutputStream
name|os
init|=
name|response
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Static resource "
operator|+
name|pathInfo
operator|+
literal|" can not be written to the output stream"
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|String
name|getStaticResourceContentType
parameter_list|(
name|String
name|extension
parameter_list|)
block|{
return|return
name|staticContentTypes
operator|.
name|get
argument_list|(
name|extension
argument_list|)
return|;
block|}
specifier|protected
name|void
name|redirect
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|,
name|String
name|pathInfo
parameter_list|)
throws|throws
name|ServletException
block|{
name|String
name|theServletPath
init|=
name|dispatcherServletPath
operator|==
literal|null
condition|?
literal|"/"
else|:
name|dispatcherServletPath
decl_stmt|;
name|ServletContext
name|sc
init|=
name|super
operator|.
name|getServletContext
argument_list|()
decl_stmt|;
name|RequestDispatcher
name|rd
init|=
name|dispatcherServletName
operator|!=
literal|null
condition|?
name|sc
operator|.
name|getNamedDispatcher
argument_list|(
name|dispatcherServletName
argument_list|)
else|:
name|sc
operator|.
name|getRequestDispatcher
argument_list|(
operator|(
name|theServletPath
operator|+
name|pathInfo
operator|)
operator|.
name|replace
argument_list|(
literal|"//"
argument_list|,
literal|"/"
argument_list|)
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
name|errorMessage
init|=
literal|"No RequestDispatcher can be created for path "
operator|+
name|pathInfo
decl_stmt|;
if|if
condition|(
name|dispatcherServletName
operator|!=
literal|null
condition|)
block|{
name|errorMessage
operator|+=
literal|", dispatcher name: "
operator|+
name|dispatcherServletName
expr_stmt|;
block|}
throw|throw
operator|new
name|ServletException
argument_list|(
name|errorMessage
argument_list|)
throw|;
block|}
try|try
block|{
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
name|redirectAttributes
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|request
operator|.
name|setAttribute
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
name|HttpServletRequestFilter
name|servletRequest
init|=
operator|new
name|HttpServletRequestFilter
argument_list|(
name|request
argument_list|,
name|pathInfo
argument_list|,
name|theServletPath
argument_list|)
decl_stmt|;
name|rd
operator|.
name|forward
argument_list|(
name|servletRequest
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"RequestDispatcher for path "
operator|+
name|pathInfo
operator|+
literal|" has failed"
argument_list|)
throw|;
block|}
block|}
specifier|protected
specifier|abstract
name|void
name|invoke
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|ServletException
function_decl|;
specifier|private
specifier|static
class|class
name|HttpServletRequestFilter
extends|extends
name|HttpServletRequestWrapper
block|{
specifier|private
name|String
name|pathInfo
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
name|pathInfo
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
name|pathInfo
operator|=
name|pathInfo
expr_stmt|;
name|this
operator|.
name|servletPath
operator|=
name|servletPath
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
name|pathInfo
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRequestURI
parameter_list|()
block|{
name|String
name|contextPath
init|=
name|getContextPath
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"/"
operator|.
name|equals
argument_list|(
name|contextPath
argument_list|)
condition|)
block|{
name|contextPath
operator|=
literal|""
expr_stmt|;
block|}
return|return
name|contextPath
operator|+
operator|(
name|servletPath
operator|+
name|pathInfo
operator|)
operator|.
name|replace
argument_list|(
literal|"//"
argument_list|,
literal|"/"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|AbstractHTTPDestination
operator|.
name|SERVICE_REDIRECTION
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"true"
return|;
block|}
return|return
name|super
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

