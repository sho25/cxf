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
name|http_jetty
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FilterInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FilterOutputStream
import|;
end_import

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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|security
operator|.
name|GeneralSecurityException
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
name|BusFactory
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
name|ReflectionUtil
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
name|configuration
operator|.
name|jsse
operator|.
name|TLSServerParameters
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
name|configuration
operator|.
name|security
operator|.
name|CertificateConstraintsType
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
name|continuations
operator|.
name|ContinuationProvider
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
name|continuations
operator|.
name|SuspendedInvocationException
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
name|io
operator|.
name|CachedOutputStream
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
name|io
operator|.
name|CopyingOutputStream
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
name|ExchangeImpl
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
name|message
operator|.
name|MessageImpl
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
name|http
operator|.
name|HTTPSession
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
name|http_jetty
operator|.
name|continuations
operator|.
name|JettyContinuationProvider
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
name|https
operator|.
name|CertConstraintsJaxBUtils
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
name|transports
operator|.
name|http
operator|.
name|StemMatchingQueryHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|http
operator|.
name|Generator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|io
operator|.
name|AbstractConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|AbstractHttpConnection
operator|.
name|Output
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|ClassUtils
import|;
end_import

begin_class
specifier|public
class|class
name|JettyHTTPDestination
extends|extends
name|AbstractHTTPDestination
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
name|JettyHTTPDestination
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|JettyHTTPServerEngine
name|engine
decl_stmt|;
specifier|protected
name|JettyHTTPServerEngineFactory
name|serverEngineFactory
decl_stmt|;
specifier|protected
name|ServletContext
name|servletContext
decl_stmt|;
specifier|protected
name|URL
name|nurl
decl_stmt|;
specifier|protected
name|ClassLoader
name|loader
decl_stmt|;
comment|/**      * This variable signifies that finalizeConfig() has been called.      * It gets called after this object has been spring configured.      * It is used to automatically reinitialize things when resources      * are reset, such as setTlsServerParameters().      */
specifier|private
name|boolean
name|configFinalized
decl_stmt|;
comment|/**      * Constructor, using Jetty server engine.      *       * @param b the associated Bus      * @param ci the associated conduit initiator      * @param endpointInfo the endpoint info of the destination      * @param serverEngineFactory       * @throws IOException      */
specifier|public
name|JettyHTTPDestination
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|DestinationRegistry
name|registry
parameter_list|,
name|EndpointInfo
name|ei
parameter_list|,
name|JettyHTTPServerEngineFactory
name|serverEngineFactory
parameter_list|)
throws|throws
name|IOException
block|{
comment|//Add the defualt port if the address is missing it
name|super
argument_list|(
name|bus
argument_list|,
name|registry
argument_list|,
name|ei
argument_list|,
name|getAddressValue
argument_list|(
name|ei
argument_list|,
literal|true
argument_list|)
operator|.
name|getAddress
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|serverEngineFactory
operator|=
name|serverEngineFactory
expr_stmt|;
name|nurl
operator|=
operator|new
name|URL
argument_list|(
name|endpointInfo
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|loader
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ClassLoader
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LOG
return|;
block|}
specifier|public
name|void
name|setServletContext
parameter_list|(
name|ServletContext
name|sc
parameter_list|)
block|{
name|servletContext
operator|=
name|sc
expr_stmt|;
block|}
comment|/**      * Post-configure retreival of server engine.      */
specifier|protected
name|void
name|retrieveEngine
parameter_list|()
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
name|engine
operator|=
name|serverEngineFactory
operator|.
name|retrieveJettyHTTPServerEngine
argument_list|(
name|nurl
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|engine
operator|==
literal|null
condition|)
block|{
name|engine
operator|=
name|serverEngineFactory
operator|.
name|createJettyHTTPServerEngine
argument_list|(
name|nurl
operator|.
name|getHost
argument_list|()
argument_list|,
name|nurl
operator|.
name|getPort
argument_list|()
argument_list|,
name|nurl
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
block|}
assert|assert
name|engine
operator|!=
literal|null
assert|;
name|TLSServerParameters
name|serverParameters
init|=
name|engine
operator|.
name|getTlsServerParameters
argument_list|()
decl_stmt|;
if|if
condition|(
name|serverParameters
operator|!=
literal|null
operator|&&
name|serverParameters
operator|.
name|getCertConstraints
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|CertificateConstraintsType
name|constraints
init|=
name|serverParameters
operator|.
name|getCertConstraints
argument_list|()
decl_stmt|;
if|if
condition|(
name|constraints
operator|!=
literal|null
condition|)
block|{
name|certConstraints
operator|=
name|CertConstraintsJaxBUtils
operator|.
name|createCertConstraints
argument_list|(
name|constraints
argument_list|)
expr_stmt|;
block|}
block|}
comment|// When configuring for "http", however, it is still possible that
comment|// Spring configuration has configured the port for https.
if|if
condition|(
operator|!
name|nurl
operator|.
name|getProtocol
argument_list|()
operator|.
name|equals
argument_list|(
name|engine
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Port "
operator|+
name|engine
operator|.
name|getPort
argument_list|()
operator|+
literal|" is configured with wrong protocol \""
operator|+
name|engine
operator|.
name|getProtocol
argument_list|()
operator|+
literal|"\" for \""
operator|+
name|nurl
operator|+
literal|"\""
argument_list|)
throw|;
block|}
block|}
comment|/**      * This method is used to finalize the configuration      * after the configuration items have been set.      *      */
specifier|public
name|void
name|finalizeConfig
parameter_list|()
block|{
assert|assert
operator|!
name|configFinalized
assert|;
try|try
block|{
name|retrieveEngine
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
name|configFinalized
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * Activate receipt of incoming messages.      */
specifier|protected
name|void
name|activate
parameter_list|()
block|{
name|super
operator|.
name|activate
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Activating receipt of incoming messages"
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
literal|null
decl_stmt|;
try|try
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
name|endpointInfo
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|engine
operator|.
name|addServant
argument_list|(
name|url
argument_list|,
operator|new
name|JettyHTTPHandler
argument_list|(
name|this
argument_list|,
name|contextMatchOnExact
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Deactivate receipt of incoming messages.      */
specifier|protected
name|void
name|deactivate
parameter_list|()
block|{
name|super
operator|.
name|deactivate
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Deactivating receipt of incoming messages"
argument_list|)
expr_stmt|;
name|engine
operator|.
name|removeServant
argument_list|(
name|nurl
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|getBasePathForFullAddress
parameter_list|(
name|String
name|addr
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
name|addr
argument_list|)
operator|.
name|getPath
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
name|String
name|removeTrailingSeparator
parameter_list|(
name|String
name|addr
parameter_list|)
block|{
if|if
condition|(
name|addr
operator|!=
literal|null
operator|&&
name|addr
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|&&
name|addr
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
operator|==
name|addr
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|)
block|{
return|return
name|addr
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|addr
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|addr
return|;
block|}
block|}
specifier|private
specifier|synchronized
name|String
name|updateEndpointAddress
parameter_list|(
name|String
name|addr
parameter_list|)
block|{
comment|// only update the EndpointAddress if the base path is equal
comment|// make sure we don't broke the get operation?parament query
name|String
name|address
init|=
name|removeTrailingSeparator
argument_list|(
name|endpointInfo
operator|.
name|getAddress
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|getBasePathForFullAddress
argument_list|(
name|address
argument_list|)
operator|.
name|equals
argument_list|(
name|removeTrailingSeparator
argument_list|(
name|getStem
argument_list|(
name|getBasePathForFullAddress
argument_list|(
name|addr
argument_list|)
argument_list|)
argument_list|)
argument_list|)
condition|)
block|{
name|endpointInfo
operator|.
name|setAddress
argument_list|(
name|addr
argument_list|)
expr_stmt|;
block|}
return|return
name|address
return|;
block|}
specifier|protected
name|void
name|doService
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|resp
parameter_list|)
throws|throws
name|IOException
block|{
name|doService
argument_list|(
name|servletContext
argument_list|,
name|req
argument_list|,
name|resp
argument_list|)
expr_stmt|;
block|}
specifier|static
name|AbstractConnection
name|getConnectionForRequest
parameter_list|(
name|Request
name|r
parameter_list|)
block|{
try|try
block|{
return|return
operator|(
name|AbstractConnection
operator|)
name|r
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getConnection"
argument_list|)
operator|.
name|invoke
argument_list|(
name|r
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
name|void
name|setHeadFalse
parameter_list|(
name|AbstractConnection
name|con
parameter_list|)
block|{
try|try
block|{
name|Generator
name|gen
init|=
operator|(
name|Generator
operator|)
name|con
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getGenerator"
argument_list|)
operator|.
name|invoke
argument_list|(
name|con
argument_list|)
decl_stmt|;
name|gen
operator|.
name|setHead
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore - can continue
block|}
block|}
specifier|protected
name|void
name|doService
parameter_list|(
name|ServletContext
name|context
parameter_list|,
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|resp
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|context
operator|=
name|servletContext
expr_stmt|;
block|}
name|Request
name|baseRequest
init|=
operator|(
name|req
operator|instanceof
name|Request
operator|)
condition|?
operator|(
name|Request
operator|)
name|req
else|:
name|getCurrentRequest
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
literal|"HEAD"
operator|.
name|equals
argument_list|(
name|req
operator|.
name|getMethod
argument_list|()
argument_list|)
condition|)
block|{
comment|//bug in Jetty with persistent connections that if a HEAD is
comment|//sent, a _head flag is never reset
name|AbstractConnection
name|c
init|=
name|getConnectionForRequest
argument_list|(
name|baseRequest
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|setHeadFalse
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|getServer
argument_list|()
operator|.
name|isSetRedirectURL
argument_list|()
condition|)
block|{
name|resp
operator|.
name|sendRedirect
argument_list|(
name|getServer
argument_list|()
operator|.
name|getRedirectURL
argument_list|()
argument_list|)
expr_stmt|;
name|resp
operator|.
name|flushBuffer
argument_list|()
expr_stmt|;
name|baseRequest
operator|.
name|setHandled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return;
block|}
name|QueryHandlerRegistry
name|queryHandlerRegistry
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|QueryHandlerRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|req
operator|.
name|getQueryString
argument_list|()
operator|&&
name|queryHandlerRegistry
operator|!=
literal|null
condition|)
block|{
name|String
name|reqAddr
init|=
name|req
operator|.
name|getRequestURL
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|requestURL
init|=
name|reqAddr
operator|+
literal|"?"
operator|+
name|req
operator|.
name|getQueryString
argument_list|()
decl_stmt|;
name|String
name|pathInfo
init|=
name|req
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
for|for
control|(
name|QueryHandler
name|qh
range|:
name|queryHandlerRegistry
operator|.
name|getHandlers
argument_list|()
control|)
block|{
name|boolean
name|recognized
init|=
name|qh
operator|instanceof
name|StemMatchingQueryHandler
condition|?
operator|(
operator|(
name|StemMatchingQueryHandler
operator|)
name|qh
operator|)
operator|.
name|isRecognizedQuery
argument_list|(
name|requestURL
argument_list|,
name|pathInfo
argument_list|,
name|endpointInfo
argument_list|,
name|contextMatchOnExact
argument_list|()
argument_list|)
else|:
name|qh
operator|.
name|isRecognizedQuery
argument_list|(
name|requestURL
argument_list|,
name|pathInfo
argument_list|,
name|endpointInfo
argument_list|)
decl_stmt|;
if|if
condition|(
name|recognized
condition|)
block|{
comment|//replace the endpointInfo address with request url only for get wsdl
name|String
name|errorMsg
init|=
literal|null
decl_stmt|;
name|CachedOutputStream
name|out
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
synchronized|synchronized
init|(
name|endpointInfo
init|)
block|{
name|String
name|oldAddress
init|=
name|updateEndpointAddress
argument_list|(
name|reqAddr
argument_list|)
decl_stmt|;
name|resp
operator|.
name|setContentType
argument_list|(
name|qh
operator|.
name|getResponseContentType
argument_list|(
name|requestURL
argument_list|,
name|pathInfo
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|qh
operator|.
name|writeResponse
argument_list|(
name|requestURL
argument_list|,
name|pathInfo
argument_list|,
name|endpointInfo
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"writeResponse failed: "
argument_list|,
name|ex
argument_list|)
expr_stmt|;
name|errorMsg
operator|=
name|ex
operator|.
name|getMessage
argument_list|()
expr_stmt|;
block|}
name|endpointInfo
operator|.
name|setAddress
argument_list|(
name|oldAddress
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|errorMsg
operator|!=
literal|null
condition|)
block|{
name|resp
operator|.
name|sendError
argument_list|(
literal|500
argument_list|,
name|errorMsg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|writeCacheTo
argument_list|(
name|resp
operator|.
name|getOutputStream
argument_list|()
argument_list|)
expr_stmt|;
name|resp
operator|.
name|getOutputStream
argument_list|()
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|baseRequest
operator|.
name|setHandled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
comment|// REVISIT: service on executor if associated with endpoint
name|ClassLoaderHolder
name|origLoader
init|=
literal|null
decl_stmt|;
name|Bus
name|origBus
init|=
name|BusFactory
operator|.
name|getAndSetThreadDefaultBus
argument_list|(
name|bus
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
name|origLoader
operator|=
name|ClassLoaderUtils
operator|.
name|setThreadContextClassloader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
name|serviceRequest
argument_list|(
name|context
argument_list|,
name|req
argument_list|,
name|resp
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|origBus
operator|!=
name|bus
condition|)
block|{
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|origBus
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|origLoader
operator|!=
literal|null
condition|)
block|{
name|origLoader
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|serviceRequest
parameter_list|(
specifier|final
name|ServletContext
name|context
parameter_list|,
specifier|final
name|HttpServletRequest
name|req
parameter_list|,
specifier|final
name|HttpServletResponse
name|resp
parameter_list|)
throws|throws
name|IOException
block|{
name|Request
name|baseRequest
init|=
operator|(
name|req
operator|instanceof
name|Request
operator|)
condition|?
operator|(
name|Request
operator|)
name|req
else|:
name|getCurrentRequest
argument_list|()
decl_stmt|;
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
name|Message
name|inMessage
init|=
name|retrieveFromContinuation
argument_list|(
name|req
argument_list|)
decl_stmt|;
if|if
condition|(
name|inMessage
operator|==
literal|null
condition|)
block|{
name|inMessage
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
name|setupMessage
argument_list|(
name|inMessage
argument_list|,
name|context
argument_list|,
name|req
argument_list|,
name|resp
argument_list|)
expr_stmt|;
operator|(
operator|(
name|MessageImpl
operator|)
name|inMessage
operator|)
operator|.
name|setDestination
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|ExchangeImpl
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setInMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setSession
argument_list|(
operator|new
name|HTTPSession
argument_list|(
name|req
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|incomingObserver
operator|.
name|onMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
name|resp
operator|.
name|flushBuffer
argument_list|()
expr_stmt|;
name|baseRequest
operator|.
name|setHandled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SuspendedInvocationException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|.
name|getRuntimeException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
name|ex
operator|.
name|getRuntimeException
argument_list|()
throw|;
block|}
comment|//else nothing to do
block|}
catch|catch
parameter_list|(
name|Fault
name|ex
parameter_list|)
block|{
name|Throwable
name|cause
init|=
name|ex
operator|.
name|getCause
argument_list|()
decl_stmt|;
if|if
condition|(
name|cause
operator|instanceof
name|RuntimeException
condition|)
block|{
throw|throw
operator|(
name|RuntimeException
operator|)
name|cause
throw|;
block|}
else|else
block|{
throw|throw
name|ex
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|ex
parameter_list|)
block|{
throw|throw
name|ex
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
name|OutputStream
name|flushHeaders
parameter_list|(
name|Message
name|outMessage
parameter_list|,
name|boolean
name|getStream
parameter_list|)
throws|throws
name|IOException
block|{
name|OutputStream
name|out
init|=
name|super
operator|.
name|flushHeaders
argument_list|(
name|outMessage
argument_list|,
name|getStream
argument_list|)
decl_stmt|;
return|return
name|wrapOutput
argument_list|(
name|out
argument_list|)
return|;
block|}
specifier|private
name|OutputStream
name|wrapOutput
parameter_list|(
name|OutputStream
name|out
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|out
operator|instanceof
name|Output
condition|)
block|{
name|out
operator|=
operator|new
name|JettyOutputStream
argument_list|(
operator|(
name|Output
operator|)
name|out
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
return|return
name|out
return|;
block|}
specifier|static
class|class
name|JettyOutputStream
extends|extends
name|FilterOutputStream
implements|implements
name|CopyingOutputStream
block|{
specifier|final
name|Output
name|out
decl_stmt|;
specifier|public
name|JettyOutputStream
parameter_list|(
name|Output
name|o
parameter_list|)
block|{
name|super
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|out
operator|=
name|o
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|copyFrom
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|CountingInputStream
name|c
init|=
operator|new
name|CountingInputStream
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|out
operator|.
name|sendContent
argument_list|(
name|c
argument_list|)
expr_stmt|;
return|return
name|c
operator|.
name|getCount
argument_list|()
return|;
block|}
block|}
specifier|static
class|class
name|CountingInputStream
extends|extends
name|FilterInputStream
block|{
name|int
name|count
decl_stmt|;
specifier|public
name|CountingInputStream
parameter_list|(
name|InputStream
name|in
parameter_list|)
block|{
name|super
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getCount
parameter_list|()
block|{
return|return
name|count
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|i
init|=
name|super
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|!=
operator|-
literal|1
condition|)
block|{
operator|++
name|count
expr_stmt|;
block|}
return|return
name|i
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|i
init|=
name|super
operator|.
name|read
argument_list|(
name|b
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|!=
operator|-
literal|1
condition|)
block|{
name|count
operator|+=
name|i
expr_stmt|;
block|}
return|return
name|i
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|i
init|=
name|super
operator|.
name|read
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|!=
operator|-
literal|1
condition|)
block|{
name|count
operator|+=
name|i
expr_stmt|;
block|}
return|return
name|i
return|;
block|}
block|}
specifier|public
name|ServerEngine
name|getEngine
parameter_list|()
block|{
return|return
name|engine
return|;
block|}
specifier|private
name|String
name|getStem
parameter_list|(
name|String
name|baseURI
parameter_list|)
block|{
return|return
name|baseURI
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|baseURI
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|Message
name|retrieveFromContinuation
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|)
block|{
return|return
operator|(
name|Message
operator|)
name|req
operator|.
name|getAttribute
argument_list|(
name|CXF_CONTINUATION_MESSAGE
argument_list|)
return|;
block|}
specifier|protected
name|void
name|setupContinuation
parameter_list|(
name|Message
name|inMessage
parameter_list|,
specifier|final
name|HttpServletRequest
name|req
parameter_list|,
specifier|final
name|HttpServletResponse
name|resp
parameter_list|)
block|{
if|if
condition|(
name|engine
operator|.
name|getContinuationsEnabled
argument_list|()
condition|)
block|{
name|inMessage
operator|.
name|put
argument_list|(
name|ContinuationProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|JettyContinuationProvider
argument_list|(
name|req
argument_list|,
name|resp
argument_list|,
name|inMessage
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|AbstractConnection
name|getCurrentConnection
parameter_list|()
block|{
comment|// AbstractHttpConnection on Jetty 7.6, HttpConnection on Jetty<=7.5
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
literal|null
decl_stmt|;
try|try
block|{
name|cls
operator|=
name|ClassUtils
operator|.
name|forName
argument_list|(
literal|"org.eclipse.jetty.server.AbstractHttpConnection"
argument_list|,
name|AbstractConnection
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore
block|}
if|if
condition|(
name|cls
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|cls
operator|=
name|ClassUtils
operator|.
name|forName
argument_list|(
literal|"org.eclipse.jetty.server.HttpConnection"
argument_list|,
name|AbstractConnection
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
try|try
block|{
return|return
operator|(
name|AbstractConnection
operator|)
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|cls
operator|.
name|getMethod
argument_list|(
literal|"getCurrentConnection"
argument_list|)
argument_list|)
operator|.
name|invoke
argument_list|(
literal|null
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|Request
name|getCurrentRequest
parameter_list|()
block|{
name|AbstractConnection
name|con
init|=
name|getCurrentConnection
argument_list|()
decl_stmt|;
try|try
block|{
return|return
operator|(
name|Request
operator|)
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|con
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getRequest"
argument_list|)
argument_list|)
operator|.
name|invoke
argument_list|(
name|con
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

