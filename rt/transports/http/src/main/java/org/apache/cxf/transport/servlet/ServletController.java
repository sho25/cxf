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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|interceptor
operator|.
name|Fault
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
name|ServletController
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
name|ServletController
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|ServletTransportFactory
name|transport
decl_stmt|;
specifier|private
name|ServletContext
name|servletContext
decl_stmt|;
specifier|private
name|ServletConfig
name|servletConfig
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
specifier|volatile
name|String
name|lastBase
init|=
literal|""
decl_stmt|;
specifier|public
name|ServletController
parameter_list|(
name|ServletTransportFactory
name|df
parameter_list|,
name|ServletConfig
name|config
parameter_list|,
name|ServletContext
name|context
parameter_list|,
name|Bus
name|b
parameter_list|)
block|{
name|super
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|this
operator|.
name|transport
operator|=
name|df
expr_stmt|;
name|this
operator|.
name|servletConfig
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|servletContext
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|bus
operator|=
name|b
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
name|ServletController
parameter_list|()
block|{     }
name|String
name|getLastBaseURL
parameter_list|()
block|{
return|return
name|lastBase
return|;
block|}
specifier|protected
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
if|if
condition|(
name|base
operator|.
name|equals
argument_list|(
name|lastBase
argument_list|)
condition|)
block|{
return|return;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|paths
init|=
name|transport
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
name|ServletDestination
name|d2
init|=
name|transport
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
operator|==
literal|null
operator|&&
name|d2
operator|.
name|getAddress
argument_list|()
operator|!=
literal|null
operator|&&
name|d2
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
name|d2
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
block|}
if|if
condition|(
name|ad
operator|!=
literal|null
operator|&&
operator|(
name|ad
operator|.
name|equals
argument_list|(
name|path
argument_list|)
operator|||
name|ad
operator|.
name|equals
argument_list|(
name|lastBase
operator|+
name|path
argument_list|)
operator|)
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
name|lastBase
operator|=
name|base
expr_stmt|;
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
name|EndpointInfo
name|ei
init|=
operator|new
name|EndpointInfo
argument_list|()
decl_stmt|;
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
name|ei
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|ServletDestination
name|d
init|=
name|getDestination
argument_list|(
name|ei
operator|.
name|getAddress
argument_list|()
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
operator|)
condition|)
block|{
name|updateDests
argument_list|(
name|request
argument_list|)
expr_stmt|;
if|if
condition|(
name|request
operator|.
name|getParameter
argument_list|(
literal|"stylesheet"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|renderStyleSheet
argument_list|(
name|request
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"false"
operator|.
name|equals
argument_list|(
name|request
operator|.
name|getParameter
argument_list|(
literal|"formatted"
argument_list|)
argument_list|)
condition|)
block|{
name|generateUnformattedServiceList
argument_list|(
name|request
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|generateServiceList
argument_list|(
name|request
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|d
operator|=
name|checkRestfulRequest
argument_list|(
name|request
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
literal|"Can't find the request for "
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
name|ei
operator|=
name|d
operator|.
name|getEndpointInfo
argument_list|()
expr_stmt|;
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
operator|&&
literal|null
operator|!=
name|request
operator|.
name|getQueryString
argument_list|()
operator|&&
name|request
operator|.
name|getQueryString
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|&&
name|bus
operator|.
name|getExtension
argument_list|(
name|QueryHandlerRegistry
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
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
comment|// update the EndPoint Address with request url
name|updateDests
argument_list|(
name|request
argument_list|)
expr_stmt|;
for|for
control|(
name|QueryHandler
name|qh
range|:
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
name|res
operator|.
name|setContentType
argument_list|(
name|qh
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
name|qh
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
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LogUtils
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|Level
operator|.
name|WARNING
argument_list|,
name|qh
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" Exception caught writing response."
argument_list|,
name|e
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
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return;
block|}
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
block|}
catch|catch
parameter_list|(
name|Fault
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|RuntimeException
condition|)
block|{
throw|throw
operator|(
name|RuntimeException
operator|)
name|ex
operator|.
name|getCause
argument_list|()
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
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
specifier|protected
name|ServletDestination
name|getDestination
parameter_list|(
name|String
name|address
parameter_list|)
block|{
return|return
operator|(
name|ServletDestination
operator|)
name|transport
operator|.
name|getDestinationForPath
argument_list|(
name|address
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|protected
name|ServletDestination
name|checkRestfulRequest
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
throws|throws
name|IOException
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
name|int
name|len
init|=
operator|-
literal|1
decl_stmt|;
name|ServletDestination
name|ret
init|=
literal|null
decl_stmt|;
for|for
control|(
name|String
name|path
range|:
name|transport
operator|.
name|getDestinationsPaths
argument_list|()
control|)
block|{
if|if
condition|(
name|address
operator|.
name|startsWith
argument_list|(
name|path
argument_list|)
operator|&&
name|path
operator|.
name|length
argument_list|()
operator|>
name|len
condition|)
block|{
name|ret
operator|=
name|transport
operator|.
name|getDestinationForPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|len
operator|=
name|path
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
if|if
condition|(
name|title
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
literal|"<title>"
operator|+
name|title
operator|+
literal|"</title>"
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
literal|"<title>CXF - Service list</title>"
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
literal|"</head><body>"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ServletDestination
argument_list|>
name|destinations
init|=
name|getServletDestinations
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
comment|//TODO : we may introduce a bus extension instead
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|atomMap
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|bus
operator|.
name|getProperty
argument_list|(
literal|"org.apache.cxf.extensions.logging.atom.pull"
argument_list|)
decl_stmt|;
name|writeSOAPEndpoints
argument_list|(
name|response
argument_list|,
name|destinations
argument_list|,
name|atomMap
argument_list|)
expr_stmt|;
name|writeRESTfulEndpoints
argument_list|(
name|response
argument_list|,
name|destinations
argument_list|,
name|atomMap
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
name|List
argument_list|<
name|ServletDestination
argument_list|>
name|destinations
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|atomMap
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
name|ServletDestination
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
if|if
condition|(
name|oi
operator|.
name|getProperty
argument_list|(
literal|"operation.is.synthetic"
argument_list|)
operator|!=
name|Boolean
operator|.
name|TRUE
condition|)
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
name|addAtomLinkIfNeeded
argument_list|(
name|address
argument_list|,
name|atomMap
argument_list|,
name|response
operator|.
name|getWriter
argument_list|()
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
name|List
argument_list|<
name|ServletDestination
argument_list|>
name|destinations
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|atomMap
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|ServletDestination
argument_list|>
name|restfulDests
init|=
operator|new
name|ArrayList
argument_list|<
name|ServletDestination
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ServletDestination
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
name|ServletDestination
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
name|addAtomLinkIfNeeded
argument_list|(
name|address
argument_list|,
name|atomMap
argument_list|,
name|response
operator|.
name|getWriter
argument_list|()
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
specifier|private
specifier|static
name|void
name|addAtomLinkIfNeeded
parameter_list|(
name|String
name|address
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extMap
parameter_list|,
name|PrintWriter
name|pw
parameter_list|)
block|{
name|String
name|atomAddress
init|=
name|getExtensionEndpointAddress
argument_list|(
name|address
argument_list|,
name|extMap
argument_list|)
decl_stmt|;
if|if
condition|(
name|atomAddress
operator|!=
literal|null
condition|)
block|{
name|pw
operator|.
name|write
argument_list|(
literal|"<br/><span class=\"field\">Atom Log Feed :</span> "
operator|+
literal|"<a href=\""
operator|+
name|atomAddress
operator|+
literal|"\">"
operator|+
name|atomAddress
operator|+
literal|"</a>"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|String
name|getExtensionEndpointAddress
parameter_list|(
name|String
name|endpointAddress
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extMap
parameter_list|)
block|{
if|if
condition|(
name|extMap
operator|!=
literal|null
condition|)
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
name|extMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|endpointAddress
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
name|endpointAddress
operator|=
name|endpointAddress
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|endpointAddress
operator|.
name|length
argument_list|()
operator|-
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|endpointAddress
operator|+=
name|entry
operator|.
name|getValue
argument_list|()
expr_stmt|;
return|return
name|endpointAddress
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|renderStyleSheet
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
literal|"text/css; charset=UTF-8"
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"servicelist.css"
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|IOUtils
operator|.
name|copy
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|,
name|response
operator|.
name|getOutputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|ServletDestination
argument_list|>
name|getServletDestinations
parameter_list|()
block|{
name|List
argument_list|<
name|ServletDestination
argument_list|>
name|destinations
init|=
operator|new
name|LinkedList
argument_list|<
name|ServletDestination
argument_list|>
argument_list|(
name|transport
operator|.
name|getDestinations
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|destinations
argument_list|,
operator|new
name|Comparator
argument_list|<
name|ServletDestination
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|ServletDestination
name|o1
parameter_list|,
name|ServletDestination
name|o2
parameter_list|)
block|{
if|if
condition|(
name|o1
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
if|if
condition|(
name|o2
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
return|return
name|o1
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
operator|.
name|compareTo
argument_list|(
name|o2
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
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|destinations
return|;
block|}
specifier|protected
name|void
name|generateUnformattedServiceList
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
literal|"text/plain; charset=UTF-8"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ServletDestination
argument_list|>
name|destinations
init|=
name|getServletDestinations
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
name|writeUnformattedSOAPEndpoints
argument_list|(
name|response
argument_list|,
name|destinations
argument_list|,
name|request
operator|.
name|getParameter
argument_list|(
literal|"wsdlList"
argument_list|)
argument_list|)
expr_stmt|;
name|writeUnformattedRESTfulEndpoints
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
literal|"No services have been found."
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|writeUnformattedSOAPEndpoints
parameter_list|(
name|HttpServletResponse
name|response
parameter_list|,
name|List
argument_list|<
name|ServletDestination
argument_list|>
name|destinations
parameter_list|,
name|Object
name|renderParam
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|renderWsdlList
init|=
literal|"true"
operator|.
name|equals
argument_list|(
name|renderParam
argument_list|)
decl_stmt|;
for|for
control|(
name|ServletDestination
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
name|getInterface
argument_list|()
condition|)
block|{
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
name|address
argument_list|)
expr_stmt|;
if|if
condition|(
name|renderWsdlList
condition|)
block|{
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"?wsdl"
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
literal|'\n'
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
literal|'\n'
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|writeUnformattedRESTfulEndpoints
parameter_list|(
name|HttpServletResponse
name|response
parameter_list|,
name|List
argument_list|<
name|ServletDestination
argument_list|>
name|destinations
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|ServletDestination
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
name|address
operator|+
literal|"?_wadl&_type=xml"
argument_list|)
expr_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|'\n'
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
name|ServletDestination
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
name|servletContext
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
specifier|private
name|void
name|init
parameter_list|()
block|{
name|transport
operator|.
name|setServletController
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

