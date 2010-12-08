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
name|http_jaxws_spi
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

begin_class
specifier|public
class|class
name|JAXWSHttpSpiDestination
extends|extends
name|AbstractHTTPDestination
block|{
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JAXWSHttpSpiDestination
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|public
name|JAXWSHttpSpiDestination
parameter_list|(
name|Bus
name|b
parameter_list|,
name|DestinationRegistry
name|registry
parameter_list|,
name|EndpointInfo
name|ei
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|b
argument_list|,
name|registry
argument_list|,
name|ei
argument_list|,
name|ei
operator|.
name|getAddress
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LOG
return|;
block|}
comment|/**      * This is called by handlers for servicing requests      *       * @param context      * @param req      * @param resp      * @throws IOException      */
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
name|endpointInfo
operator|.
name|getAddress
argument_list|()
decl_stmt|;
name|endpointInfo
operator|.
name|setAddress
argument_list|(
name|reqAddr
argument_list|)
expr_stmt|;
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
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return;
block|}
block|}
block|}
try|try
block|{
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|serviceRequest
argument_list|(
name|req
argument_list|,
name|resp
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|serviceRequest
parameter_list|(
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
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|setupMessage
argument_list|(
name|inMessage
argument_list|,
literal|null
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
comment|// else nothing to do
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
name|String
name|getBasePath
parameter_list|(
name|String
name|contextPath
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|contextPath
operator|+
name|getAddress
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
end_class

end_unit

