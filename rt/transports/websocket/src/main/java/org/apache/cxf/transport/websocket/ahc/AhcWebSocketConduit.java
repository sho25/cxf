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
name|ahc
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
name|SocketTimeoutException
import|;
end_import

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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|ConcurrentHashMap
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
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|AsyncHttpClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|websocket
operator|.
name|WebSocket
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|websocket
operator|.
name|WebSocketByteListener
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|websocket
operator|.
name|WebSocketTextListener
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|websocket
operator|.
name|WebSocketUpgradeHandler
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
name|Headers
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
name|URLConnectionHTTPConduit
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
name|HttpsURLConnectionInfo
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
name|websocket
operator|.
name|WebSocketConstants
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
name|websocket
operator|.
name|WebSocketUtils
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
name|configuration
operator|.
name|HTTPClientPolicy
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|AhcWebSocketConduit
extends|extends
name|URLConnectionHTTPConduit
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
name|AhcWebSocketConduit
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|AsyncHttpClient
name|ahcclient
decl_stmt|;
specifier|private
name|WebSocket
name|websocket
decl_stmt|;
comment|//REVISIT make these keys configurable
specifier|private
name|String
name|requestIdKey
init|=
name|WebSocketConstants
operator|.
name|DEFAULT_REQUEST_ID_KEY
decl_stmt|;
specifier|private
name|String
name|responseIdKey
init|=
name|WebSocketConstants
operator|.
name|DEFAULT_RESPONSE_ID_KEY
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|RequestResponse
argument_list|>
name|uncorrelatedRequests
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|RequestResponse
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|AhcWebSocketConduit
parameter_list|(
name|Bus
name|b
parameter_list|,
name|EndpointInfo
name|ei
parameter_list|,
name|EndpointReferenceType
name|t
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|b
argument_list|,
name|ei
argument_list|,
name|t
argument_list|)
expr_stmt|;
name|ahcclient
operator|=
operator|new
name|AsyncHttpClient
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|setupConnection
parameter_list|(
name|Message
name|message
parameter_list|,
name|URI
name|currentURL
parameter_list|,
name|HTTPClientPolicy
name|csPolicy
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|s
init|=
name|currentURL
operator|.
name|getScheme
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
literal|"ws"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
operator|&&
operator|!
literal|"wss"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|MalformedURLException
argument_list|(
literal|"unknown protocol: "
operator|+
name|s
argument_list|)
throw|;
block|}
name|message
operator|.
name|put
argument_list|(
literal|"http.scheme"
argument_list|,
name|currentURL
operator|.
name|getScheme
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|httpRequestMethod
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
decl_stmt|;
if|if
condition|(
name|httpRequestMethod
operator|==
literal|null
condition|)
block|{
name|httpRequestMethod
operator|=
literal|"POST"
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|,
name|httpRequestMethod
argument_list|)
expr_stmt|;
block|}
specifier|final
name|AhcWebSocketConduitRequest
name|request
init|=
operator|new
name|AhcWebSocketConduitRequest
argument_list|(
name|currentURL
argument_list|,
name|httpRequestMethod
argument_list|)
decl_stmt|;
specifier|final
name|int
name|rtimeout
init|=
name|determineReceiveTimeout
argument_list|(
name|message
argument_list|,
name|csPolicy
argument_list|)
decl_stmt|;
name|request
operator|.
name|setReceiveTimeout
argument_list|(
name|rtimeout
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|AhcWebSocketConduitRequest
operator|.
name|class
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|OutputStream
name|createOutputStream
parameter_list|(
name|Message
name|message
parameter_list|,
name|boolean
name|needToCacheRequest
parameter_list|,
name|boolean
name|isChunking
parameter_list|,
name|int
name|chunkThreshold
parameter_list|)
throws|throws
name|IOException
block|{
name|AhcWebSocketConduitRequest
name|entity
init|=
name|message
operator|.
name|get
argument_list|(
name|AhcWebSocketConduitRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|AhcWebSocketWrappedOutputStream
name|out
init|=
operator|new
name|AhcWebSocketWrappedOutputStream
argument_list|(
name|message
argument_list|,
name|needToCacheRequest
argument_list|,
name|isChunking
argument_list|,
name|chunkThreshold
argument_list|,
name|getConduitName
argument_list|()
argument_list|,
name|entity
operator|.
name|getUri
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|out
return|;
block|}
specifier|public
class|class
name|AhcWebSocketWrappedOutputStream
extends|extends
name|WrappedOutputStream
block|{
specifier|private
name|AhcWebSocketConduitRequest
name|entity
decl_stmt|;
specifier|private
name|Response
name|response
decl_stmt|;
specifier|protected
name|AhcWebSocketWrappedOutputStream
parameter_list|(
name|Message
name|message
parameter_list|,
name|boolean
name|possibleRetransmit
parameter_list|,
name|boolean
name|isChunking
parameter_list|,
name|int
name|chunkThreshold
parameter_list|,
name|String
name|conduitName
parameter_list|,
name|URI
name|url
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|possibleRetransmit
argument_list|,
name|isChunking
argument_list|,
name|chunkThreshold
argument_list|,
name|conduitName
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|entity
operator|=
name|message
operator|.
name|get
argument_list|(
name|AhcWebSocketConduitRequest
operator|.
name|class
argument_list|)
expr_stmt|;
comment|//REVISIT how we prepare the request
name|String
name|requri
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
literal|"org.apache.cxf.request.uri"
argument_list|)
decl_stmt|;
if|if
condition|(
name|requri
operator|!=
literal|null
condition|)
block|{
comment|// jaxrs speicfies a sub-path using prop org.apache.cxf.request.uri
if|if
condition|(
name|requri
operator|.
name|startsWith
argument_list|(
literal|"ws"
argument_list|)
condition|)
block|{
name|entity
operator|.
name|setPath
argument_list|(
name|requri
operator|.
name|substring
argument_list|(
name|requri
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|,
literal|3
operator|+
name|requri
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|entity
operator|.
name|setPath
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
operator|+
name|requri
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// jaxws
name|entity
operator|.
name|setPath
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|entity
operator|.
name|setId
argument_list|(
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|uncorrelatedRequests
operator|.
name|put
argument_list|(
name|entity
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|RequestResponse
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|setupWrappedStream
parameter_list|()
throws|throws
name|IOException
block|{
name|connect
argument_list|()
expr_stmt|;
name|wrappedStream
operator|=
operator|new
name|OutputStream
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|byte
name|b
index|[]
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
comment|//REVISIT support multiple writes and flush() to write the entire block data?
comment|// or provides the fragment mode?
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
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
name|headers
operator|.
name|put
argument_list|(
literal|"Content-Type"
argument_list|,
name|entity
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|requestIdKey
argument_list|,
name|entity
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|websocket
operator|.
name|sendMessage
argument_list|(
name|WebSocketUtils
operator|.
name|buildRequest
argument_list|(
name|entity
operator|.
name|getMethod
argument_list|()
argument_list|,
name|entity
operator|.
name|getPath
argument_list|()
argument_list|,
name|headers
argument_list|,
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
throws|throws
name|IOException
block|{
comment|//REVISIT support this single byte write and use flush() to write the block data?
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{                 }
block|}
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|handleNoOutput
parameter_list|()
throws|throws
name|IOException
block|{
name|connect
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
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
name|headers
operator|.
name|put
argument_list|(
name|requestIdKey
argument_list|,
name|entity
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|websocket
operator|.
name|sendMessage
argument_list|(
name|WebSocketUtils
operator|.
name|buildRequest
argument_list|(
name|entity
operator|.
name|getMethod
argument_list|()
argument_list|,
name|entity
operator|.
name|getPath
argument_list|()
argument_list|,
name|headers
argument_list|,
literal|null
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|HttpsURLConnectionInfo
name|getHttpsURLConnectionInfo
parameter_list|()
throws|throws
name|IOException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|setProtocolHeaders
parameter_list|()
throws|throws
name|IOException
block|{
name|Headers
name|h
init|=
operator|new
name|Headers
argument_list|(
name|outMessage
argument_list|)
decl_stmt|;
name|entity
operator|.
name|setContentType
argument_list|(
name|h
operator|.
name|determineContentType
argument_list|()
argument_list|)
expr_stmt|;
comment|//REVISIT may provide an option to add other headers
comment|//          boolean addHeaders = MessageUtils.isTrue(outMessage.getContextualProperty(Headers.ADD_HEADERS_PROPERTY));
block|}
annotation|@
name|Override
specifier|protected
name|void
name|setFixedLengthStreamingMode
parameter_list|(
name|int
name|i
parameter_list|)
block|{
comment|// ignore
block|}
annotation|@
name|Override
specifier|protected
name|int
name|getResponseCode
parameter_list|()
throws|throws
name|IOException
block|{
name|Response
name|r
init|=
name|getResponse
argument_list|()
decl_stmt|;
return|return
name|r
operator|.
name|getStatusCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getResponseMessage
parameter_list|()
throws|throws
name|IOException
block|{
comment|//TODO return a generic message based on the status code
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|updateResponseHeaders
parameter_list|(
name|Message
name|inMessage
parameter_list|)
throws|throws
name|IOException
block|{
name|Headers
name|h
init|=
operator|new
name|Headers
argument_list|(
name|inMessage
argument_list|)
decl_stmt|;
name|String
name|ct
init|=
name|getResponse
argument_list|()
operator|.
name|getContentType
argument_list|()
decl_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|ct
argument_list|)
expr_stmt|;
comment|//REVISIT if we are allowing more headers, we need to add them into the cxf's headers
name|h
operator|.
name|headerMap
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|ct
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|handleResponseAsync
parameter_list|()
throws|throws
name|IOException
block|{
name|handleResponseOnWorkqueue
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|closeInputStream
parameter_list|()
throws|throws
name|IOException
block|{         }
annotation|@
name|Override
specifier|protected
name|boolean
name|usingProxy
parameter_list|()
block|{
comment|// TODO add proxy support ...
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|protected
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|Response
name|r
init|=
name|getResponse
argument_list|()
decl_stmt|;
comment|//REVISIT
return|return
operator|new
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
argument_list|(
name|r
operator|.
name|getTextEntity
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|InputStream
name|getPartialResponse
parameter_list|()
throws|throws
name|IOException
block|{
name|Response
name|r
init|=
name|getResponse
argument_list|()
decl_stmt|;
comment|//REVISIT
return|return
operator|new
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
argument_list|(
name|r
operator|.
name|getTextEntity
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|setupNewConnection
parameter_list|(
name|String
name|newURL
parameter_list|)
throws|throws
name|IOException
block|{
comment|// TODO
throw|throw
operator|new
name|IOException
argument_list|(
literal|"not supported"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|retransmitStream
parameter_list|()
throws|throws
name|IOException
block|{
comment|// TODO
throw|throw
operator|new
name|IOException
argument_list|(
literal|"not supported"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|updateCookiesBeforeRetransmit
parameter_list|()
throws|throws
name|IOException
block|{
comment|// ignore for now and may consider a specific websocket binding variant to use cookies
block|}
annotation|@
name|Override
specifier|public
name|void
name|thresholdReached
parameter_list|()
throws|throws
name|IOException
block|{
comment|// ignore
block|}
comment|//
comment|// other methods follow
comment|//
specifier|protected
name|void
name|connect
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"connecting"
argument_list|)
expr_stmt|;
if|if
condition|(
name|websocket
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|websocket
operator|=
name|ahcclient
operator|.
name|prepareGet
argument_list|(
name|url
operator|.
name|toASCIIString
argument_list|()
argument_list|)
operator|.
name|execute
argument_list|(
operator|new
name|WebSocketUpgradeHandler
operator|.
name|Builder
argument_list|()
operator|.
name|addWebSocketListener
argument_list|(
operator|new
name|AhcWebSocketListener
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"connected"
argument_list|)
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
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"unable to connect"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"already connected"
argument_list|)
expr_stmt|;
block|}
block|}
name|Response
name|getResponse
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|response
operator|==
literal|null
condition|)
block|{
name|String
name|rid
init|=
name|entity
operator|.
name|getId
argument_list|()
decl_stmt|;
name|RequestResponse
name|rr
init|=
name|uncorrelatedRequests
operator|.
name|get
argument_list|(
name|rid
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|rr
init|)
block|{
try|try
block|{
name|long
name|timetowait
init|=
name|entity
operator|.
name|getReceiveTimeout
argument_list|()
decl_stmt|;
name|response
operator|=
name|rr
operator|.
name|getResponse
argument_list|()
expr_stmt|;
if|if
condition|(
name|response
operator|==
literal|null
condition|)
block|{
name|rr
operator|.
name|wait
argument_list|(
name|timetowait
argument_list|)
expr_stmt|;
name|response
operator|=
name|rr
operator|.
name|getResponse
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
if|if
condition|(
name|response
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SocketTimeoutException
argument_list|(
literal|"Read timed out while invoking "
operator|+
name|entity
operator|.
name|getUri
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
name|response
return|;
block|}
block|}
specifier|protected
class|class
name|AhcWebSocketListener
implements|implements
name|WebSocketTextListener
implements|,
name|WebSocketByteListener
block|{
specifier|public
name|void
name|onOpen
parameter_list|(
name|WebSocket
name|ws
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"onOpen({0})"
argument_list|,
name|ws
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|onClose
parameter_list|(
name|WebSocket
name|ws
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"onCose({0})"
argument_list|,
name|ws
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|onError
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"[ws] onError"
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|onMessage
parameter_list|(
name|byte
index|[]
name|message
parameter_list|)
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
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"onMessage({0})"
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
name|Response
name|resp
init|=
operator|new
name|Response
argument_list|(
name|responseIdKey
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|RequestResponse
name|rr
init|=
name|uncorrelatedRequests
operator|.
name|get
argument_list|(
name|resp
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|rr
operator|!=
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|rr
init|)
block|{
name|rr
operator|.
name|setResponse
argument_list|(
name|resp
argument_list|)
expr_stmt|;
name|rr
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|onFragment
parameter_list|(
name|byte
index|[]
name|fragment
parameter_list|,
name|boolean
name|last
parameter_list|)
block|{
comment|//TODO
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"NOT IMPLEMENTED onFragment({0}, {1})"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|fragment
block|,
name|last
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|onMessage
parameter_list|(
name|String
name|message
parameter_list|)
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
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"onMessage({0})"
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
name|Response
name|resp
init|=
operator|new
name|Response
argument_list|(
name|responseIdKey
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|RequestResponse
name|rr
init|=
name|uncorrelatedRequests
operator|.
name|get
argument_list|(
name|resp
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|rr
operator|!=
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|rr
init|)
block|{
name|rr
operator|.
name|setResponse
argument_list|(
name|resp
argument_list|)
expr_stmt|;
name|rr
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|onFragment
parameter_list|(
name|String
name|fragment
parameter_list|,
name|boolean
name|last
parameter_list|)
block|{
comment|//TODO
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"NOT IMPLEMENTED onFragment({0}, {1})"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|fragment
block|,
name|last
block|}
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Request and Response are used to represent request and response messages transfered over the websocket
comment|//REVIST move these classes to be used in other places after finalizing their contained information.
specifier|static
class|class
name|Response
block|{
specifier|private
name|Object
name|data
decl_stmt|;
specifier|private
name|int
name|pos
decl_stmt|;
specifier|private
name|int
name|statusCode
decl_stmt|;
specifier|private
name|String
name|contentType
decl_stmt|;
specifier|private
name|String
name|id
decl_stmt|;
specifier|private
name|Object
name|entity
decl_stmt|;
specifier|public
name|Response
parameter_list|(
name|String
name|idKey
parameter_list|,
name|Object
name|data
parameter_list|)
block|{
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
name|String
name|line
init|=
name|readLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|statusCode
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|line
argument_list|)
expr_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|line
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|int
name|del
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
name|String
name|h
init|=
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|del
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|String
name|v
init|=
name|line
operator|.
name|substring
argument_list|(
name|del
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"Content-Type"
operator|.
name|equalsIgnoreCase
argument_list|(
name|h
argument_list|)
condition|)
block|{
name|contentType
operator|=
name|v
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|idKey
operator|.
name|equals
argument_list|(
name|h
argument_list|)
condition|)
block|{
name|id
operator|=
name|v
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|data
operator|instanceof
name|String
condition|)
block|{
name|entity
operator|=
operator|(
operator|(
name|String
operator|)
name|data
operator|)
operator|.
name|substring
argument_list|(
name|pos
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|data
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|entity
operator|=
operator|new
name|byte
index|[
operator|(
operator|(
name|byte
index|[]
operator|)
name|data
operator|)
operator|.
name|length
operator|-
name|pos
index|]
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|data
argument_list|,
name|pos
argument_list|,
operator|(
name|byte
index|[]
operator|)
name|entity
argument_list|,
literal|0
argument_list|,
operator|(
operator|(
name|byte
index|[]
operator|)
name|entity
operator|)
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|int
name|getStatusCode
parameter_list|()
block|{
return|return
name|statusCode
return|;
block|}
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
name|contentType
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|Object
name|getEntity
parameter_list|()
block|{
return|return
name|entity
return|;
block|}
specifier|public
name|String
name|getTextEntity
parameter_list|()
block|{
return|return
name|gettext
argument_list|(
name|entity
argument_list|)
return|;
block|}
specifier|private
name|String
name|readLine
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
name|pos
operator|<
name|length
argument_list|(
name|data
argument_list|)
condition|)
block|{
name|int
name|c
init|=
name|getchar
argument_list|(
name|data
argument_list|,
name|pos
operator|++
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|'\n'
condition|)
block|{
break|break;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
literal|'\r'
condition|)
block|{
continue|continue;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|c
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|int
name|length
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|instanceof
name|char
index|[]
condition|?
operator|(
operator|(
name|String
operator|)
name|o
operator|)
operator|.
name|length
argument_list|()
else|:
operator|(
name|o
operator|instanceof
name|byte
index|[]
condition|?
operator|(
operator|(
name|byte
index|[]
operator|)
name|o
operator|)
operator|.
name|length
else|:
literal|0
operator|)
return|;
block|}
specifier|private
name|int
name|getchar
parameter_list|(
name|Object
name|o
parameter_list|,
name|int
name|p
parameter_list|)
block|{
return|return
literal|0xff
operator|&
operator|(
name|o
operator|instanceof
name|String
condition|?
operator|(
operator|(
name|String
operator|)
name|o
operator|)
operator|.
name|charAt
argument_list|(
name|p
argument_list|)
else|:
operator|(
name|o
operator|instanceof
name|byte
index|[]
condition|?
operator|(
operator|(
name|byte
index|[]
operator|)
name|o
operator|)
index|[
name|p
index|]
else|:
operator|-
literal|1
operator|)
operator|)
return|;
block|}
specifier|private
name|String
name|gettext
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|instanceof
name|String
condition|?
operator|(
name|String
operator|)
name|o
else|:
operator|(
name|o
operator|instanceof
name|byte
index|[]
condition|?
operator|new
name|String
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|o
argument_list|)
else|:
literal|null
operator|)
return|;
block|}
block|}
specifier|static
class|class
name|RequestResponse
block|{
specifier|private
name|AhcWebSocketConduitRequest
name|request
decl_stmt|;
specifier|private
name|Response
name|response
decl_stmt|;
specifier|public
name|RequestResponse
parameter_list|(
name|AhcWebSocketConduitRequest
name|request
parameter_list|)
block|{
name|this
operator|.
name|request
operator|=
name|request
expr_stmt|;
block|}
specifier|public
name|AhcWebSocketConduitRequest
name|getRequest
parameter_list|()
block|{
return|return
name|request
return|;
block|}
specifier|public
name|Response
name|getResponse
parameter_list|()
block|{
return|return
name|response
return|;
block|}
specifier|public
name|void
name|setResponse
parameter_list|(
name|Response
name|response
parameter_list|)
block|{
name|this
operator|.
name|response
operator|=
name|response
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

