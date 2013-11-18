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
name|ServletConfig
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
name|classloader
operator|.
name|ClassLoaderUtils
operator|.
name|ClassLoaderHolder
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
name|DestinationRegistry
import|;
end_import

begin_class
specifier|public
class|class
name|ServletController
block|{
specifier|public
specifier|static
specifier|final
name|String
name|AUTH_SERVICE_LIST
init|=
literal|"auth.service.list"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|AUTH_SERVICE_LIST_REALM
init|=
literal|"auth.service.list.realm"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|DEFAULT_LISTINGS_CLASSIFIER
init|=
literal|"/services"
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
name|ServletController
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|HTTP_PREFIX
init|=
literal|"http"
decl_stmt|;
specifier|protected
name|boolean
name|isHideServiceList
decl_stmt|;
specifier|protected
name|boolean
name|isAuthServiceListPage
decl_stmt|;
specifier|protected
name|boolean
name|disableAddressUpdates
init|=
literal|true
decl_stmt|;
specifier|protected
name|String
name|authServiceListPageRealm
decl_stmt|;
specifier|protected
name|String
name|forcedBaseAddress
decl_stmt|;
specifier|protected
name|String
name|serviceListRelativePath
init|=
name|DEFAULT_LISTINGS_CLASSIFIER
decl_stmt|;
specifier|protected
name|ServletConfig
name|servletConfig
decl_stmt|;
specifier|protected
name|DestinationRegistry
name|destinationRegistry
decl_stmt|;
specifier|protected
name|HttpServlet
name|serviceListGenerator
decl_stmt|;
specifier|public
name|ServletController
parameter_list|(
name|DestinationRegistry
name|destinationRegistry
parameter_list|,
name|ServletConfig
name|config
parameter_list|,
name|HttpServlet
name|serviceListGenerator
parameter_list|)
block|{
name|this
operator|.
name|servletConfig
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|destinationRegistry
operator|=
name|destinationRegistry
expr_stmt|;
name|this
operator|.
name|serviceListGenerator
operator|=
name|serviceListGenerator
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setServiceListRelativePath
parameter_list|(
name|String
name|relativePath
parameter_list|)
block|{
name|serviceListRelativePath
operator|=
name|relativePath
expr_stmt|;
block|}
specifier|protected
name|String
name|getBaseURL
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
return|return
name|forcedBaseAddress
operator|==
literal|null
condition|?
name|BaseUrlHelper
operator|.
name|getBaseURL
argument_list|(
name|request
argument_list|)
else|:
name|forcedBaseAddress
return|;
block|}
specifier|protected
name|void
name|setBaseURLAttribute
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
name|request
operator|.
name|setAttribute
argument_list|(
name|Message
operator|.
name|BASE_PATH
argument_list|,
name|getBaseURL
argument_list|(
name|request
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|updateDestination
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|AbstractHTTPDestination
name|d
parameter_list|)
block|{
name|String
name|base
init|=
name|getBaseURL
argument_list|(
name|request
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|d
init|)
block|{
name|String
name|ad
init|=
name|d
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|ad
operator|==
literal|null
operator|&&
name|d
operator|.
name|getAddress
argument_list|()
operator|!=
literal|null
operator|&&
name|d
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ad
operator|=
name|d
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
expr_stmt|;
if|if
condition|(
name|ad
operator|==
literal|null
condition|)
block|{
name|ad
operator|=
literal|"/"
expr_stmt|;
block|}
block|}
comment|// Using HTTP_PREFIX check is safe for ServletController
comment|// URI.create(ad).isRelative() can be used - a bit more expensive though
if|if
condition|(
name|ad
operator|!=
literal|null
operator|&&
operator|!
name|ad
operator|.
name|startsWith
argument_list|(
name|HTTP_PREFIX
argument_list|)
condition|)
block|{
if|if
condition|(
name|disableAddressUpdates
condition|)
block|{
name|request
operator|.
name|setAttribute
argument_list|(
literal|"org.apache.cxf.transport.endpoint.address"
argument_list|,
name|base
operator|+
name|ad
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|BaseUrlHelper
operator|.
name|setAddress
argument_list|(
name|d
argument_list|,
name|base
operator|+
name|ad
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|init
parameter_list|()
block|{
if|if
condition|(
name|servletConfig
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|String
name|hideServiceList
init|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
literal|"hide-service-list-page"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|hideServiceList
argument_list|)
condition|)
block|{
name|this
operator|.
name|isHideServiceList
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|hideServiceList
argument_list|)
expr_stmt|;
block|}
name|String
name|authServiceListPage
init|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
literal|"service-list-page-authenticate"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|authServiceListPage
argument_list|)
condition|)
block|{
name|this
operator|.
name|isAuthServiceListPage
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|authServiceListPage
argument_list|)
expr_stmt|;
block|}
name|String
name|authServiceListRealm
init|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
literal|"service-list-page-authenticate-realm"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|authServiceListRealm
argument_list|)
condition|)
block|{
name|this
operator|.
name|authServiceListPageRealm
operator|=
name|authServiceListRealm
expr_stmt|;
block|}
name|String
name|isDisableAddressUpdates
init|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
literal|"disable-address-updates"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|isDisableAddressUpdates
argument_list|)
condition|)
block|{
name|this
operator|.
name|disableAddressUpdates
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|isDisableAddressUpdates
argument_list|)
expr_stmt|;
block|}
name|String
name|isForcedBaseAddress
init|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
literal|"base-address"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|isForcedBaseAddress
argument_list|)
condition|)
block|{
name|this
operator|.
name|forcedBaseAddress
operator|=
name|isForcedBaseAddress
expr_stmt|;
block|}
try|try
block|{
name|serviceListGenerator
operator|.
name|init
argument_list|(
name|servletConfig
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ServletException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|String
name|serviceListPath
init|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
literal|"service-list-path"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|serviceListPath
argument_list|)
condition|)
block|{
name|this
operator|.
name|serviceListRelativePath
operator|=
name|serviceListPath
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|filter
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|res
parameter_list|)
throws|throws
name|ServletException
block|{
return|return
name|invoke
argument_list|(
name|request
argument_list|,
name|res
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
name|void
name|invoke
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|res
parameter_list|)
throws|throws
name|ServletException
block|{
name|invoke
argument_list|(
name|request
argument_list|,
name|res
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|invoke
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|res
parameter_list|,
name|boolean
name|returnErrors
parameter_list|)
throws|throws
name|ServletException
block|{
try|try
block|{
name|String
name|pathInfo
init|=
name|request
operator|.
name|getPathInfo
argument_list|()
operator|==
literal|null
condition|?
literal|""
else|:
name|request
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
name|AbstractHTTPDestination
name|d
init|=
name|destinationRegistry
operator|.
name|getDestinationForPath
argument_list|(
name|pathInfo
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|d
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|isHideServiceList
operator|&&
operator|(
name|request
operator|.
name|getRequestURI
argument_list|()
operator|.
name|endsWith
argument_list|(
name|serviceListRelativePath
argument_list|)
operator|||
name|request
operator|.
name|getRequestURI
argument_list|()
operator|.
name|endsWith
argument_list|(
name|serviceListRelativePath
operator|+
literal|"/"
argument_list|)
operator|||
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|pathInfo
argument_list|)
operator|||
literal|"/"
operator|.
name|equals
argument_list|(
name|pathInfo
argument_list|)
operator|)
condition|)
block|{
if|if
condition|(
name|isAuthServiceListPage
condition|)
block|{
name|setAuthServiceListPageAttribute
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
name|setBaseURLAttribute
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|serviceListGenerator
operator|.
name|service
argument_list|(
name|request
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|d
operator|=
name|destinationRegistry
operator|.
name|checkRestfulRequest
argument_list|(
name|pathInfo
argument_list|)
expr_stmt|;
if|if
condition|(
name|d
operator|==
literal|null
operator|||
name|d
operator|.
name|getMessageObserver
argument_list|()
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|returnErrors
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Can't find the the request for "
operator|+
name|request
operator|.
name|getRequestURL
argument_list|()
operator|+
literal|"'s Observer "
argument_list|)
expr_stmt|;
name|generateNotFound
argument_list|(
name|request
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
block|}
block|}
if|if
condition|(
name|d
operator|!=
literal|null
condition|)
block|{
name|Bus
name|bus
init|=
name|d
operator|.
name|getBus
argument_list|()
decl_stmt|;
name|ClassLoaderHolder
name|orig
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|ClassLoader
name|loader
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ClassLoader
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|loader
operator|==
literal|null
condition|)
block|{
name|ResourceManager
name|manager
init|=
name|bus
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
name|manager
operator|!=
literal|null
condition|)
block|{
name|loader
operator|=
name|manager
operator|.
name|resolveResource
argument_list|(
literal|""
argument_list|,
name|ClassLoader
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
comment|//need to set the context classloader to the loader of the bundle
name|orig
operator|=
name|ClassLoaderUtils
operator|.
name|setThreadContextClassloader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
block|}
name|updateDestination
argument_list|(
name|request
argument_list|,
name|d
argument_list|)
expr_stmt|;
name|invokeDestination
argument_list|(
name|request
argument_list|,
name|res
argument_list|,
name|d
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|orig
operator|!=
literal|null
condition|)
block|{
name|orig
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|setAuthServiceListPageAttribute
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
name|request
operator|.
name|setAttribute
argument_list|(
name|ServletController
operator|.
name|AUTH_SERVICE_LIST
argument_list|,
name|this
operator|.
name|isAuthServiceListPage
argument_list|)
expr_stmt|;
name|request
operator|.
name|setAttribute
argument_list|(
name|ServletController
operator|.
name|AUTH_SERVICE_LIST_REALM
argument_list|,
name|this
operator|.
name|authServiceListPageRealm
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|invokeDestination
parameter_list|(
specifier|final
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|,
name|AbstractHTTPDestination
name|d
parameter_list|)
throws|throws
name|ServletException
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Service http request on thread: "
operator|+
name|Thread
operator|.
name|currentThread
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|d
operator|.
name|invoke
argument_list|(
name|servletConfig
argument_list|,
name|servletConfig
operator|.
name|getServletContext
argument_list|()
argument_list|,
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Finished servicing http request on thread: "
operator|+
name|Thread
operator|.
name|currentThread
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|generateNotFound
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|res
parameter_list|)
throws|throws
name|IOException
block|{
name|res
operator|.
name|setStatus
argument_list|(
literal|404
argument_list|)
expr_stmt|;
name|res
operator|.
name|setContentType
argument_list|(
literal|"text/html"
argument_list|)
expr_stmt|;
name|res
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<html><body>No service was found.</body></html>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

