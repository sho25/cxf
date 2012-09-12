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
operator|.
name|asyncclient
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|concurrent
operator|.
name|Future
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLEngine
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpVersion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|protocol
operator|.
name|ClientContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|concurrent
operator|.
name|BasicFuture
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|concurrent
operator|.
name|FutureCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|nio
operator|.
name|client
operator|.
name|DefaultHttpAsyncClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|conn
operator|.
name|ClientAsyncConnectionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|conn
operator|.
name|scheme
operator|.
name|AsyncScheme
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|conn
operator|.
name|scheme
operator|.
name|AsyncSchemeRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|conn
operator|.
name|ssl
operator|.
name|SSLLayeringStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|protocol
operator|.
name|HttpAsyncRequestProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|protocol
operator|.
name|HttpAsyncResponseConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|reactor
operator|.
name|IOSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|params
operator|.
name|HttpConnectionParams
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|params
operator|.
name|HttpParams
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|params
operator|.
name|HttpProtocolParams
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|params
operator|.
name|SyncBasicHttpParams
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|HttpContext
import|;
end_import

begin_class
specifier|public
class|class
name|CXFAsyncRequester
block|{
specifier|private
specifier|final
name|ClientAsyncConnectionManager
name|caConMan
decl_stmt|;
specifier|public
name|CXFAsyncRequester
parameter_list|(
name|ClientAsyncConnectionManager
name|caConMan
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|caConMan
operator|=
name|caConMan
expr_stmt|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Future
argument_list|<
name|T
argument_list|>
name|execute
parameter_list|(
specifier|final
name|AsyncHTTPConduit
name|conduit
parameter_list|,
specifier|final
name|URI
name|uri
parameter_list|,
specifier|final
name|long
name|connectionTimeout
parameter_list|,
specifier|final
name|HttpAsyncRequestProducer
name|requestProducer
parameter_list|,
specifier|final
name|HttpAsyncResponseConsumer
argument_list|<
name|T
argument_list|>
name|responseConsumer
parameter_list|,
specifier|final
name|HttpContext
name|context
parameter_list|,
specifier|final
name|FutureCallback
argument_list|<
name|T
argument_list|>
name|callback
parameter_list|)
block|{
if|if
condition|(
name|requestProducer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"HTTP request producer may not be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|responseConsumer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"HTTP response consumer may not be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"HTTP context may not be null"
argument_list|)
throw|;
block|}
name|BasicFuture
argument_list|<
name|T
argument_list|>
name|future
init|=
operator|new
name|BasicFuture
argument_list|<
name|T
argument_list|>
argument_list|(
name|callback
argument_list|)
decl_stmt|;
specifier|final
name|AsyncSchemeRegistry
name|reg
init|=
operator|new
name|AsyncSchemeRegistry
argument_list|()
decl_stmt|;
name|reg
operator|.
name|register
argument_list|(
operator|new
name|AsyncScheme
argument_list|(
literal|"http"
argument_list|,
literal|80
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"https"
operator|.
name|equals
argument_list|(
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
specifier|final
name|SSLContext
name|sslcontext
init|=
name|conduit
operator|.
name|getSSLContext
argument_list|()
decl_stmt|;
name|reg
operator|.
name|register
argument_list|(
operator|new
name|AsyncScheme
argument_list|(
literal|"https"
argument_list|,
literal|443
argument_list|,
operator|new
name|SSLLayeringStrategy
argument_list|(
name|sslcontext
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|initializeEngine
parameter_list|(
name|SSLEngine
name|engine
parameter_list|)
block|{
name|conduit
operator|.
name|initializeSSLEngine
argument_list|(
name|sslcontext
argument_list|,
name|engine
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|verifySession
parameter_list|(
specifier|final
name|IOSession
name|iosession
parameter_list|,
specifier|final
name|SSLSession
name|sslsession
parameter_list|)
throws|throws
name|SSLException
block|{
name|super
operator|.
name|verifySession
argument_list|(
name|iosession
argument_list|,
name|sslsession
argument_list|)
expr_stmt|;
name|iosession
operator|.
name|setAttribute
argument_list|(
literal|"cxf.handshake.done"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|CXFHttpRequest
name|req
init|=
operator|(
name|CXFHttpRequest
operator|)
name|iosession
operator|.
name|removeAttribute
argument_list|(
name|CXFHttpRequest
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|req
operator|!=
literal|null
condition|)
block|{
name|req
operator|.
name|getOutputStream
argument_list|()
operator|.
name|setSSLSession
argument_list|(
name|sslsession
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GeneralSecurityException
name|e
parameter_list|)
block|{
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
name|DefaultHttpAsyncClient
name|dhac
init|=
operator|new
name|DefaultHttpAsyncClient
argument_list|(
name|caConMan
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|HttpParams
name|createHttpParams
parameter_list|()
block|{
name|HttpParams
name|params
init|=
operator|new
name|SyncBasicHttpParams
argument_list|()
decl_stmt|;
name|HttpProtocolParams
operator|.
name|setVersion
argument_list|(
name|params
argument_list|,
name|HttpVersion
operator|.
name|HTTP_1_1
argument_list|)
expr_stmt|;
name|HttpConnectionParams
operator|.
name|setTcpNoDelay
argument_list|(
name|params
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|HttpConnectionParams
operator|.
name|setSocketBufferSize
argument_list|(
name|params
argument_list|,
literal|16332
argument_list|)
expr_stmt|;
return|return
name|params
return|;
block|}
block|}
decl_stmt|;
name|context
operator|.
name|setAttribute
argument_list|(
name|ClientContext
operator|.
name|SCHEME_REGISTRY
argument_list|,
name|reg
argument_list|)
expr_stmt|;
name|dhac
operator|.
name|execute
argument_list|(
name|requestProducer
argument_list|,
name|responseConsumer
argument_list|,
name|context
argument_list|,
name|callback
argument_list|)
expr_stmt|;
return|return
name|future
return|;
block|}
block|}
end_class

end_unit

