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
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Proxy
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
name|net
operator|.
name|URISyntaxException
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
name|net
operator|.
name|URLConnection
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
name|io
operator|.
name|CacheAndWriteOutputStream
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
name|https
operator|.
name|HttpsURLConnectionFactory
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
name|URLConnectionHTTPConduit
extends|extends
name|HTTPConduit
block|{
comment|/**      * This field holds the connection factory, which primarily is used to       * factor out SSL specific code from this implementation.      *<p>      * This field is "protected" to facilitate some contrived UnitTesting so      * that an extended class may alter its value with an EasyMock URLConnection      * Factory.       */
specifier|protected
name|HttpsURLConnectionFactory
name|connectionFactory
decl_stmt|;
specifier|public
name|URLConnectionHTTPConduit
parameter_list|(
name|Bus
name|b
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
name|ei
argument_list|)
expr_stmt|;
name|connectionFactory
operator|=
operator|new
name|HttpsURLConnectionFactory
argument_list|()
expr_stmt|;
name|CXFAuthenticator
operator|.
name|addAuthenticator
argument_list|()
expr_stmt|;
block|}
specifier|public
name|URLConnectionHTTPConduit
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
name|connectionFactory
operator|=
operator|new
name|HttpsURLConnectionFactory
argument_list|()
expr_stmt|;
name|CXFAuthenticator
operator|.
name|addAuthenticator
argument_list|()
expr_stmt|;
block|}
comment|/**      * Close the conduit      */
specifier|public
name|void
name|close
parameter_list|()
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|defaultEndpointURI
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|URLConnection
name|connect
init|=
name|defaultEndpointURI
operator|.
name|toURL
argument_list|()
operator|.
name|openConnection
argument_list|()
decl_stmt|;
if|if
condition|(
name|connect
operator|instanceof
name|HttpURLConnection
condition|)
block|{
operator|(
operator|(
name|HttpURLConnection
operator|)
name|connect
operator|)
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
comment|//defaultEndpointURL = null;
block|}
block|}
specifier|private
name|HttpURLConnection
name|createConnection
parameter_list|(
name|Message
name|message
parameter_list|,
name|URI
name|uri
parameter_list|,
name|HTTPClientPolicy
name|csPolicy
parameter_list|)
throws|throws
name|IOException
block|{
name|URL
name|url
init|=
name|uri
operator|.
name|toURL
argument_list|()
decl_stmt|;
name|Proxy
name|proxy
init|=
name|proxyFactory
operator|.
name|createProxy
argument_list|(
name|csPolicy
argument_list|,
name|uri
argument_list|)
decl_stmt|;
return|return
name|connectionFactory
operator|.
name|createConnection
argument_list|(
name|tlsClientParameters
argument_list|,
name|proxy
argument_list|,
name|url
argument_list|)
return|;
block|}
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
name|HttpURLConnection
name|connection
init|=
name|createConnection
argument_list|(
name|message
argument_list|,
name|currentURL
argument_list|,
name|csPolicy
argument_list|)
decl_stmt|;
name|connection
operator|.
name|setDoOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|int
name|ctimeout
init|=
name|determineConnectionTimeout
argument_list|(
name|message
argument_list|,
name|csPolicy
argument_list|)
decl_stmt|;
name|connection
operator|.
name|setConnectTimeout
argument_list|(
name|ctimeout
argument_list|)
expr_stmt|;
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
name|connection
operator|.
name|setReadTimeout
argument_list|(
name|rtimeout
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setUseCaches
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// We implement redirects in this conduit. We do not
comment|// rely on the underlying URLConnection implementation
comment|// because of trust issues.
name|connection
operator|.
name|setInstanceFollowRedirects
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// If the HTTP_REQUEST_METHOD is not set, the default is "POST".
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
literal|"POST"
argument_list|)
expr_stmt|;
block|}
name|connection
operator|.
name|setRequestMethod
argument_list|(
name|httpRequestMethod
argument_list|)
expr_stmt|;
comment|// We place the connection on the message to pick it up
comment|// in the WrappedOutputStream.
name|message
operator|.
name|put
argument_list|(
name|KEY_HTTP_CONNECTION
argument_list|,
name|connection
argument_list|)
expr_stmt|;
block|}
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
block|{
name|HttpURLConnection
name|connection
init|=
operator|(
name|HttpURLConnection
operator|)
name|message
operator|.
name|get
argument_list|(
name|KEY_HTTP_CONNECTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|isChunking
operator|&&
name|chunkThreshold
operator|<=
literal|0
condition|)
block|{
name|chunkThreshold
operator|=
literal|0
expr_stmt|;
name|connection
operator|.
name|setChunkedStreamingMode
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|URLConnectionWrappedOutputStream
argument_list|(
name|message
argument_list|,
name|connection
argument_list|,
name|needToCacheRequest
argument_list|,
name|isChunking
argument_list|,
name|chunkThreshold
argument_list|,
name|getConduitName
argument_list|()
argument_list|)
return|;
block|}
class|class
name|URLConnectionWrappedOutputStream
extends|extends
name|WrappedOutputStream
block|{
name|HttpURLConnection
name|connection
decl_stmt|;
specifier|public
name|URLConnectionWrappedOutputStream
parameter_list|(
name|Message
name|message
parameter_list|,
name|HttpURLConnection
name|connection
parameter_list|,
name|boolean
name|needToCacheRequest
parameter_list|,
name|boolean
name|isChunking
parameter_list|,
name|int
name|chunkThreshold
parameter_list|,
name|String
name|conduitName
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|needToCacheRequest
argument_list|,
name|isChunking
argument_list|,
name|chunkThreshold
argument_list|,
name|conduitName
argument_list|,
name|connection
operator|.
name|getURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|connection
operator|=
name|connection
expr_stmt|;
block|}
comment|// This construction makes extending the HTTPConduit more easier
specifier|protected
name|URLConnectionWrappedOutputStream
parameter_list|(
name|URLConnectionWrappedOutputStream
name|wos
parameter_list|)
block|{
name|super
argument_list|(
name|wos
argument_list|)
expr_stmt|;
name|this
operator|.
name|connection
operator|=
name|wos
operator|.
name|connection
expr_stmt|;
block|}
specifier|protected
name|void
name|setupWrappedStream
parameter_list|()
throws|throws
name|IOException
block|{
comment|// If we need to cache for retransmission, store data in a
comment|// CacheAndWriteOutputStream. Otherwise write directly to the output stream.
if|if
condition|(
name|cachingForRetransmission
condition|)
block|{
name|cachedStream
operator|=
operator|new
name|CacheAndWriteOutputStream
argument_list|(
name|connection
operator|.
name|getOutputStream
argument_list|()
argument_list|)
expr_stmt|;
name|wrappedStream
operator|=
name|cachedStream
expr_stmt|;
block|}
else|else
block|{
name|wrappedStream
operator|=
name|connection
operator|.
name|getOutputStream
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|thresholdReached
parameter_list|()
block|{
if|if
condition|(
name|chunking
condition|)
block|{
name|connection
operator|.
name|setChunkedStreamingMode
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|onFirstWrite
parameter_list|()
throws|throws
name|IOException
block|{
name|super
operator|.
name|onFirstWrite
argument_list|()
expr_stmt|;
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
literal|"Sending "
operator|+
name|connection
operator|.
name|getRequestMethod
argument_list|()
operator|+
literal|" Message with Headers to "
operator|+
name|url
operator|+
literal|" Conduit :"
operator|+
name|conduitName
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|setProtocolHeaders
parameter_list|()
throws|throws
name|IOException
block|{
operator|new
name|Headers
argument_list|(
name|outMessage
argument_list|)
operator|.
name|setProtocolHeadersInConnection
argument_list|(
name|connection
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|HttpsURLConnectionInfo
name|getHttpsURLConnectionInfo
parameter_list|()
throws|throws
name|IOException
block|{
name|connection
operator|.
name|connect
argument_list|()
expr_stmt|;
return|return
operator|new
name|HttpsURLConnectionInfo
argument_list|(
name|connection
argument_list|)
return|;
block|}
specifier|protected
name|void
name|updateResponseHeaders
parameter_list|(
name|Message
name|inMessage
parameter_list|)
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
name|h
operator|.
name|readFromConnection
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|connection
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|cookies
operator|.
name|readFromHeaders
argument_list|(
name|h
argument_list|)
expr_stmt|;
block|}
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
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|updateCookiesBeforeRetransmit
parameter_list|()
block|{
name|Headers
name|h
init|=
operator|new
name|Headers
argument_list|()
decl_stmt|;
name|h
operator|.
name|readFromConnection
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|cookies
operator|.
name|readFromHeaders
argument_list|(
name|h
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|InputStream
name|in
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getResponseCode
argument_list|()
operator|>=
name|HttpURLConnection
operator|.
name|HTTP_BAD_REQUEST
condition|)
block|{
name|in
operator|=
name|connection
operator|.
name|getErrorStream
argument_list|()
expr_stmt|;
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
try|try
block|{
comment|// just in case - but this will most likely cause an exception
name|in
operator|=
name|connection
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
else|else
block|{
name|in
operator|=
name|connection
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
block|}
return|return
name|in
return|;
block|}
specifier|protected
name|void
name|closeInputStream
parameter_list|()
throws|throws
name|IOException
block|{
comment|//try and consume any content so that the connection might be reusable
name|InputStream
name|ins
init|=
name|connection
operator|.
name|getErrorStream
argument_list|()
decl_stmt|;
if|if
condition|(
name|ins
operator|==
literal|null
condition|)
block|{
name|ins
operator|=
name|connection
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ins
operator|!=
literal|null
condition|)
block|{
name|IOUtils
operator|.
name|consume
argument_list|(
name|ins
argument_list|)
expr_stmt|;
name|ins
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|int
name|getResponseCode
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|connection
operator|.
name|getResponseCode
argument_list|()
return|;
block|}
specifier|protected
name|String
name|getResponseMessage
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|connection
operator|.
name|getResponseMessage
argument_list|()
return|;
block|}
specifier|protected
name|InputStream
name|getPartialResponse
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|ChunkedUtil
operator|.
name|getPartialResponse
argument_list|(
name|connection
argument_list|,
name|connection
operator|.
name|getResponseCode
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|usingProxy
parameter_list|()
block|{
return|return
name|connection
operator|.
name|usingProxy
argument_list|()
return|;
block|}
specifier|protected
name|void
name|setFixedLengthStreamingMode
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|connection
operator|.
name|setFixedLengthStreamingMode
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
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
name|HTTPClientPolicy
name|cp
init|=
name|getClient
argument_list|(
name|outMessage
argument_list|)
decl_stmt|;
name|URI
name|nurl
decl_stmt|;
try|try
block|{
name|nurl
operator|=
operator|new
name|URI
argument_list|(
name|newURL
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|setupConnection
argument_list|(
name|outMessage
argument_list|,
name|nurl
argument_list|,
name|cp
argument_list|)
expr_stmt|;
name|url
operator|=
name|newURL
expr_stmt|;
name|connection
operator|=
operator|(
name|HttpURLConnection
operator|)
name|outMessage
operator|.
name|get
argument_list|(
name|KEY_HTTP_CONNECTION
argument_list|)
expr_stmt|;
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
name|OutputStream
name|out
init|=
name|connection
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|cachedStream
operator|.
name|writeCacheTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

