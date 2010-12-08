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
name|http_osgi
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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Set
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|service
operator|.
name|model
operator|.
name|OperationInfo
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
name|servlet
operator|.
name|AbstractServletController
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
name|transports
operator|.
name|http
operator|.
name|QueryHandler
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
name|transports
operator|.
name|http
operator|.
name|QueryHandlerRegistry
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
name|wsdl
operator|.
name|http
operator|.
name|AddressType
import|;
end_import

begin_class
specifier|public
class|class
name|OsgiServletController
extends|extends
name|AbstractServletController
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
name|OsgiServlet
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|DestinationRegistry
name|destinationRegistry
decl_stmt|;
specifier|public
name|OsgiServletController
parameter_list|(
name|DestinationRegistry
name|destinationRegistry
parameter_list|,
name|ServletConfig
name|config
parameter_list|)
block|{
name|super
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|this
operator|.
name|destinationRegistry
operator|=
name|destinationRegistry
expr_stmt|;
block|}
specifier|private
specifier|synchronized
name|void
name|updateDests
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
if|if
condition|(
name|disableAddressUpdates
condition|)
block|{
return|return;
block|}
name|String
name|base
init|=
name|forcedBaseAddress
operator|==
literal|null
condition|?
name|getBaseURL
argument_list|(
name|request
argument_list|)
else|:
name|forcedBaseAddress
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|paths
init|=
name|destinationRegistry
operator|.
name|getDestinationsPaths
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|path
range|:
name|paths
control|)
block|{
name|AbstractHTTPDestination
name|d2
init|=
name|destinationRegistry
operator|.
name|getDestinationForPath
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|String
name|ad
init|=
name|d2
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
operator|.
name|equals
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|d2
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|setAddress
argument_list|(
name|base
operator|+
name|path
argument_list|)
expr_stmt|;
if|if
condition|(
name|d2
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getExtensor
argument_list|(
name|AddressType
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|d2
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getExtensor
argument_list|(
name|AddressType
operator|.
name|class
argument_list|)
operator|.
name|setLocation
argument_list|(
name|base
operator|+
name|path
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
try|try
block|{
name|String
name|address
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
name|address
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
operator|)
operator|||
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
literal|"/"
operator|.
name|equals
argument_list|(
name|request
operator|.
name|getPathInfo
argument_list|()
argument_list|)
condition|)
block|{
name|updateDests
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|generateServiceList
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
name|address
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
else|else
block|{
comment|// the request should be a restful service request
name|updateDests
argument_list|(
name|request
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
block|}
block|}
else|else
block|{
name|EndpointInfo
name|ei
init|=
name|d
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
operator|(
operator|(
name|OsgiDestination
operator|)
name|d
operator|)
operator|.
name|getBus
argument_list|()
decl_stmt|;
name|ClassLoader
name|orig
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
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
name|ClassLoader
name|loader
init|=
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
decl_stmt|;
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
comment|//need to set the context classloader to the loader of the bundle
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
block|}
name|Iterable
argument_list|<
name|QueryHandler
argument_list|>
name|queryHandlers
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|QueryHandlerRegistry
operator|.
name|class
argument_list|)
operator|.
name|getHandlers
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|request
operator|.
name|getQueryString
argument_list|()
argument_list|)
operator|&&
name|queryHandlers
operator|!=
literal|null
condition|)
block|{
comment|// update the EndPoint Address with request url
if|if
condition|(
literal|"GET"
operator|.
name|equals
argument_list|(
name|request
operator|.
name|getMethod
argument_list|()
argument_list|)
condition|)
block|{
name|updateDests
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
name|String
name|ctxUri
init|=
name|request
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
name|String
name|baseUri
init|=
name|request
operator|.
name|getRequestURL
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"?"
operator|+
name|request
operator|.
name|getQueryString
argument_list|()
decl_stmt|;
name|QueryHandler
name|selectedHandler
init|=
name|findQueryHandler
argument_list|(
name|queryHandlers
argument_list|,
name|ei
argument_list|,
name|ctxUri
argument_list|,
name|baseUri
argument_list|)
decl_stmt|;
if|if
condition|(
name|selectedHandler
operator|!=
literal|null
condition|)
block|{
name|respondUsingQueryHandler
argument_list|(
name|selectedHandler
argument_list|,
name|res
argument_list|,
name|ei
argument_list|,
name|ctxUri
argument_list|,
name|baseUri
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
elseif|else
if|if
condition|(
literal|"/"
operator|.
name|equals
argument_list|(
name|address
argument_list|)
operator|||
name|address
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|updateDests
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
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
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|orig
argument_list|)
expr_stmt|;
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
block|}
specifier|private
name|QueryHandler
name|findQueryHandler
parameter_list|(
name|Iterable
argument_list|<
name|QueryHandler
argument_list|>
name|handlers
parameter_list|,
name|EndpointInfo
name|ei
parameter_list|,
name|String
name|ctxUri
parameter_list|,
name|String
name|baseUri
parameter_list|)
block|{
for|for
control|(
name|QueryHandler
name|qh
range|:
name|handlers
control|)
block|{
if|if
condition|(
name|qh
operator|.
name|isRecognizedQuery
argument_list|(
name|baseUri
argument_list|,
name|ctxUri
argument_list|,
name|ei
argument_list|)
condition|)
block|{
return|return
name|qh
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|respondUsingQueryHandler
parameter_list|(
name|QueryHandler
name|selectedHandler
parameter_list|,
name|HttpServletResponse
name|res
parameter_list|,
name|EndpointInfo
name|ei
parameter_list|,
name|String
name|ctxUri
parameter_list|,
name|String
name|baseUri
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
name|res
operator|.
name|setContentType
argument_list|(
name|selectedHandler
operator|.
name|getResponseContentType
argument_list|(
name|baseUri
argument_list|,
name|ctxUri
argument_list|)
argument_list|)
expr_stmt|;
name|OutputStream
name|out
init|=
name|res
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|selectedHandler
operator|.
name|writeResponse
argument_list|(
name|baseUri
argument_list|,
name|ctxUri
argument_list|,
name|ei
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
name|selectedHandler
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" Exception caught writing response: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ServletException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|generateServiceList
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|IOException
block|{
name|response
operator|.
name|setContentType
argument_list|(
literal|"text/html; charset=UTF-8"
argument_list|)
expr_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" "
operator|+
literal|"\"http://www.w3.org/TR/html4/loose.dtd\">"
argument_list|)
expr_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<HTML><HEAD>"
argument_list|)
expr_stmt|;
if|if
condition|(
name|serviceListStyleSheet
operator|!=
literal|null
condition|)
block|{
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<LINK type=\"text/css\" rel=\"stylesheet\" href=\""
operator|+
name|request
operator|.
name|getContextPath
argument_list|()
operator|+
literal|"/"
operator|+
name|serviceListStyleSheet
operator|+
literal|"\">"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<LINK type=\"text/css\" rel=\"stylesheet\" href=\""
operator|+
name|request
operator|.
name|getRequestURI
argument_list|()
operator|+
literal|"/?stylesheet=1\">"
argument_list|)
expr_stmt|;
block|}
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<meta http-equiv=content-type content=\"text/html; charset=UTF-8\">"
argument_list|)
expr_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<title>CXF - Service list</title>"
argument_list|)
expr_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"</head><body>"
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|AbstractHTTPDestination
argument_list|>
name|destinations
init|=
name|destinationRegistry
operator|.
name|getDestinations
argument_list|()
decl_stmt|;
if|if
condition|(
name|destinations
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|writeSOAPEndpoints
argument_list|(
name|response
argument_list|,
name|destinations
argument_list|)
expr_stmt|;
name|writeRESTfulEndpoints
argument_list|(
name|response
argument_list|,
name|destinations
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<span class=\"heading\">No services have been found.</span>"
argument_list|)
expr_stmt|;
block|}
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"</body></html>"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|writeSOAPEndpoints
parameter_list|(
name|HttpServletResponse
name|response
parameter_list|,
name|Collection
argument_list|<
name|AbstractHTTPDestination
argument_list|>
name|destinations
parameter_list|)
throws|throws
name|IOException
block|{
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<span class=\"heading\">Available SOAP services:</span><br/>"
argument_list|)
expr_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<table "
operator|+
operator|(
name|serviceListStyleSheet
operator|==
literal|null
condition|?
literal|"cellpadding=\"1\" cellspacing=\"1\" border=\"1\" width=\"100%\""
else|:
literal|""
operator|)
operator|+
literal|">"
argument_list|)
expr_stmt|;
for|for
control|(
name|AbstractHTTPDestination
name|sd
range|:
name|destinations
control|)
block|{
if|if
condition|(
literal|null
operator|!=
name|sd
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|&&
literal|null
operator|!=
name|sd
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getInterface
argument_list|()
condition|)
block|{
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<tr><td>"
argument_list|)
expr_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<span class=\"porttypename\">"
operator|+
name|sd
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"</span>"
argument_list|)
expr_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<ul>"
argument_list|)
expr_stmt|;
for|for
control|(
name|OperationInfo
name|oi
range|:
name|sd
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|.
name|getOperations
argument_list|()
control|)
block|{
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<li>"
operator|+
name|oi
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"</li>"
argument_list|)
expr_stmt|;
block|}
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"</ul>"
argument_list|)
expr_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"</td><td>"
argument_list|)
expr_stmt|;
name|String
name|address
init|=
name|sd
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
decl_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<span class=\"field\">Endpoint address:</span> "
operator|+
literal|"<span class=\"value\">"
operator|+
name|address
operator|+
literal|"</span>"
argument_list|)
expr_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<br/><span class=\"field\">WSDL :</span> "
operator|+
literal|"<a href=\""
operator|+
name|address
operator|+
literal|"?wsdl\">"
operator|+
name|sd
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"</a>"
argument_list|)
expr_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<br/><span class=\"field\">Target namespace:</span> "
operator|+
literal|"<span class=\"value\">"
operator|+
name|sd
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getTargetNamespace
argument_list|()
operator|+
literal|"</span>"
argument_list|)
expr_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"</td></tr>"
argument_list|)
expr_stmt|;
block|}
block|}
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"</table><br/><br/>"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|writeRESTfulEndpoints
parameter_list|(
name|HttpServletResponse
name|response
parameter_list|,
name|Collection
argument_list|<
name|AbstractHTTPDestination
argument_list|>
name|destinations
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|AbstractHTTPDestination
argument_list|>
name|restfulDests
init|=
operator|new
name|ArrayList
argument_list|<
name|AbstractHTTPDestination
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|AbstractHTTPDestination
name|sd
range|:
name|destinations
control|)
block|{
comment|// use some more reasonable check - though this one seems to be the only option at the moment
if|if
condition|(
literal|null
operator|==
name|sd
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getInterface
argument_list|()
condition|)
block|{
name|restfulDests
operator|.
name|add
argument_list|(
name|sd
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|restfulDests
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<span class=\"heading\">Available RESTful services:</span><br/>"
argument_list|)
expr_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<table "
operator|+
operator|(
name|serviceListStyleSheet
operator|==
literal|null
condition|?
literal|"cellpadding=\"1\" cellspacing=\"1\" border=\"1\" width=\"100%\""
else|:
literal|""
operator|)
operator|+
literal|">"
argument_list|)
expr_stmt|;
for|for
control|(
name|AbstractHTTPDestination
name|sd
range|:
name|destinations
control|)
block|{
if|if
condition|(
literal|null
operator|==
name|sd
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getInterface
argument_list|()
condition|)
block|{
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<tr><td>"
argument_list|)
expr_stmt|;
name|String
name|address
init|=
name|sd
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
decl_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<span class=\"field\">Endpoint address:</span> "
operator|+
literal|"<span class=\"value\">"
operator|+
name|address
operator|+
literal|"</span>"
argument_list|)
expr_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<br/><span class=\"field\">WADL :</span> "
operator|+
literal|"<a href=\""
operator|+
name|address
operator|+
literal|"?_wadl&_type=xml\">"
operator|+
name|address
operator|+
literal|"?_wadl&type=xml"
operator|+
literal|"</a>"
argument_list|)
expr_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"</td></tr>"
argument_list|)
expr_stmt|;
block|}
block|}
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"</table>"
argument_list|)
expr_stmt|;
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

