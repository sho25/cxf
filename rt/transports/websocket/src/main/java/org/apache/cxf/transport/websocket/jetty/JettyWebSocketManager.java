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
name|websocket
operator|.
name|jetty
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
name|http_jetty
operator|.
name|JettyHTTPDestination
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
name|CXFNonSpringServlet
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
name|websocket
operator|.
name|WebSocketFactory
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
name|websocket
operator|.
name|WebSocketFactory
operator|.
name|Acceptor
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|JettyWebSocketManager
block|{
specifier|private
name|WebSocketFactory
name|webSocketFactory
decl_stmt|;
comment|// either servlet is set or destination + servletContext are set
specifier|private
name|CXFNonSpringServlet
name|servlet
decl_stmt|;
specifier|private
name|AbstractHTTPDestination
name|destination
decl_stmt|;
specifier|private
name|ServletContext
name|servletContext
decl_stmt|;
specifier|public
name|void
name|init
parameter_list|(
name|JettyWebSocketHandler
name|handler
parameter_list|,
name|JettyHTTPDestination
name|dest
parameter_list|)
block|{
name|this
operator|.
name|destination
operator|=
name|dest
expr_stmt|;
comment|//TODO customize websocket factory configuration options when using the destination.
name|webSocketFactory
operator|=
operator|new
name|WebSocketFactory
argument_list|(
operator|(
name|Acceptor
operator|)
name|handler
argument_list|,
literal|8192
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|init
parameter_list|(
name|CXFNonSpringServlet
name|srvlt
parameter_list|,
name|ServletConfig
name|sc
parameter_list|)
throws|throws
name|ServletException
block|{
name|this
operator|.
name|servlet
operator|=
name|srvlt
expr_stmt|;
try|try
block|{
name|String
name|bs
init|=
name|srvlt
operator|.
name|getInitParameter
argument_list|(
literal|"bufferSize"
argument_list|)
decl_stmt|;
name|webSocketFactory
operator|=
operator|new
name|WebSocketFactory
argument_list|(
operator|(
name|Acceptor
operator|)
name|srvlt
argument_list|,
name|bs
operator|==
literal|null
condition|?
literal|8192
else|:
name|Integer
operator|.
name|parseInt
argument_list|(
name|bs
argument_list|)
argument_list|)
expr_stmt|;
name|webSocketFactory
operator|.
name|start
argument_list|()
expr_stmt|;
name|String
name|max
init|=
name|srvlt
operator|.
name|getInitParameter
argument_list|(
literal|"maxIdleTime"
argument_list|)
decl_stmt|;
if|if
condition|(
name|max
operator|!=
literal|null
condition|)
block|{
name|webSocketFactory
operator|.
name|setMaxIdleTime
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|max
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|max
operator|=
name|srvlt
operator|.
name|getInitParameter
argument_list|(
literal|"maxTextMessageSize"
argument_list|)
expr_stmt|;
if|if
condition|(
name|max
operator|!=
literal|null
condition|)
block|{
name|webSocketFactory
operator|.
name|setMaxTextMessageSize
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|max
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|max
operator|=
name|srvlt
operator|.
name|getInitParameter
argument_list|(
literal|"maxBinaryMessageSize"
argument_list|)
expr_stmt|;
if|if
condition|(
name|max
operator|!=
literal|null
condition|)
block|{
name|webSocketFactory
operator|.
name|setMaxBinaryMessageSize
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|max
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|e
operator|instanceof
name|ServletException
condition|?
operator|(
name|ServletException
operator|)
name|e
else|:
operator|new
name|ServletException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
name|void
name|setServletContext
parameter_list|(
name|ServletContext
name|servletContext
parameter_list|)
block|{
name|this
operator|.
name|servletContext
operator|=
name|servletContext
expr_stmt|;
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{
try|try
block|{
name|webSocketFactory
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
specifier|public
name|boolean
name|acceptWebSocket
parameter_list|(
name|ServletRequest
name|req
parameter_list|,
name|ServletResponse
name|res
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|HttpServletRequest
name|request
init|=
operator|(
name|HttpServletRequest
operator|)
name|req
decl_stmt|;
name|HttpServletResponse
name|response
init|=
operator|(
name|HttpServletResponse
operator|)
name|res
decl_stmt|;
if|if
condition|(
name|webSocketFactory
operator|.
name|acceptWebSocket
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
operator|||
name|response
operator|.
name|isCommitted
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|service
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
if|if
condition|(
name|destination
operator|!=
literal|null
condition|)
block|{
name|destination
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|servletContext
argument_list|,
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|servlet
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|CXFJettyWebSocketServletService
operator|)
name|servlet
operator|)
operator|.
name|serviceInternal
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

