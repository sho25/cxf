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
name|ProtocolException
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
name|SocketException
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
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|HttpsURLConnection
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
name|common
operator|.
name|util
operator|.
name|SystemPropertyAction
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
name|TLSClientParameters
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
name|message
operator|.
name|MessageUtils
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
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|URLConnectionHTTPConduit
extends|extends
name|HTTPConduit
block|{
specifier|public
specifier|static
specifier|final
name|String
name|HTTPURL_CONNECTION_METHOD_REFLECTION
init|=
literal|"use.httpurlconnection.method.reflection"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|boolean
name|DEFAULT_USE_REFLECTION
decl_stmt|;
static|static
block|{
name|DEFAULT_USE_REFLECTION
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|SystemPropertyAction
operator|.
name|getProperty
argument_list|(
name|HTTPURL_CONNECTION_METHOD_REFLECTION
argument_list|,
literal|"false"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This field holds the connection factory, which primarily is used to      * factor out SSL specific code from this implementation.      *<p>      * This field is "protected" to facilitate some contrived UnitTesting so      * that an extended class may alter its value with an EasyMock URLConnection      * Factory.      */
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
name|defaultAddress
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|URLConnection
name|connect
init|=
name|defaultAddress
operator|.
name|getURL
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
name|Address
name|address
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
name|address
operator|.
name|getURL
argument_list|()
decl_stmt|;
name|URI
name|uri
init|=
name|address
operator|.
name|getURI
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
name|message
operator|.
name|put
argument_list|(
literal|"http.scheme"
argument_list|,
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|)
expr_stmt|;
comment|// check tlsClientParameters from message header
name|TLSClientParameters
name|clientParameters
init|=
name|message
operator|.
name|get
argument_list|(
name|TLSClientParameters
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|clientParameters
operator|==
literal|null
condition|)
block|{
name|clientParameters
operator|=
name|tlsClientParameters
expr_stmt|;
block|}
return|return
name|connectionFactory
operator|.
name|createConnection
argument_list|(
name|clientParameters
argument_list|,
name|proxy
operator|!=
literal|null
condition|?
name|proxy
else|:
name|address
operator|.
name|getDefaultProxy
argument_list|()
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
name|Address
name|address
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
name|address
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
try|try
block|{
name|connection
operator|.
name|setRequestMethod
argument_list|(
name|httpRequestMethod
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|net
operator|.
name|ProtocolException
name|ex
parameter_list|)
block|{
name|Object
name|o
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|HTTPURL_CONNECTION_METHOD_REFLECTION
argument_list|)
decl_stmt|;
name|boolean
name|b
init|=
name|DEFAULT_USE_REFLECTION
decl_stmt|;
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
name|b
operator|=
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|b
condition|)
block|{
try|try
block|{
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
name|f
init|=
name|ReflectionUtil
operator|.
name|getDeclaredField
argument_list|(
name|HttpURLConnection
operator|.
name|class
argument_list|,
literal|"method"
argument_list|)
decl_stmt|;
if|if
condition|(
name|connection
operator|instanceof
name|HttpsURLConnection
condition|)
block|{
try|try
block|{
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
name|f2
init|=
name|ReflectionUtil
operator|.
name|getDeclaredField
argument_list|(
name|connection
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"delegate"
argument_list|)
decl_stmt|;
name|Object
name|c
init|=
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|f2
argument_list|)
operator|.
name|get
argument_list|(
name|connection
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|instanceof
name|HttpURLConnection
condition|)
block|{
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|f
argument_list|)
operator|.
name|set
argument_list|(
name|c
argument_list|,
name|httpRequestMethod
argument_list|)
expr_stmt|;
block|}
name|f2
operator|=
name|ReflectionUtil
operator|.
name|getDeclaredField
argument_list|(
name|c
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"httpsURLConnection"
argument_list|)
expr_stmt|;
name|HttpsURLConnection
name|c2
init|=
operator|(
name|HttpsURLConnection
operator|)
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|f2
argument_list|)
operator|.
name|get
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|f
argument_list|)
operator|.
name|set
argument_list|(
name|c2
argument_list|,
name|httpRequestMethod
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
name|logStackTrace
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|f
argument_list|)
operator|.
name|set
argument_list|(
name|connection
argument_list|,
name|httpRequestMethod
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|HTTPURL_CONNECTION_METHOD_REFLECTION
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|logStackTrace
argument_list|(
name|t
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
else|else
block|{
throw|throw
name|ex
throw|;
block|}
block|}
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
name|message
operator|.
name|put
argument_list|(
name|KEY_HTTP_CONNECTION_ADDRESS
argument_list|,
name|address
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
throws|throws
name|IOException
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
try|try
block|{
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
block|}
specifier|private
specifier|static
name|URI
name|computeURI
parameter_list|(
name|Message
name|message
parameter_list|,
name|HttpURLConnection
name|connection
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|Address
name|address
init|=
operator|(
name|Address
operator|)
name|message
operator|.
name|get
argument_list|(
name|KEY_HTTP_CONNECTION_ADDRESS
argument_list|)
decl_stmt|;
return|return
name|address
operator|!=
literal|null
condition|?
name|address
operator|.
name|getURI
argument_list|()
else|:
name|connection
operator|.
name|getURL
argument_list|()
operator|.
name|toURI
argument_list|()
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
throws|throws
name|URISyntaxException
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
name|computeURI
argument_list|(
name|message
argument_list|,
name|connection
argument_list|)
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
specifier|private
name|OutputStream
name|connectAndGetOutputStream
parameter_list|(
name|Boolean
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|OutputStream
name|cout
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|b
operator|!=
literal|null
operator|&&
name|b
condition|)
block|{
name|String
name|method
init|=
name|connection
operator|.
name|getRequestMethod
argument_list|()
decl_stmt|;
name|connection
operator|.
name|connect
argument_list|()
expr_stmt|;
try|try
block|{
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
name|f
init|=
name|ReflectionUtil
operator|.
name|getDeclaredField
argument_list|(
name|HttpURLConnection
operator|.
name|class
argument_list|,
literal|"method"
argument_list|)
decl_stmt|;
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|f
argument_list|)
operator|.
name|set
argument_list|(
name|connection
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
name|cout
operator|=
name|connection
operator|.
name|getOutputStream
argument_list|()
expr_stmt|;
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|f
argument_list|)
operator|.
name|set
argument_list|(
name|connection
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|logStackTrace
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|cout
operator|=
name|connection
operator|.
name|getOutputStream
argument_list|()
expr_stmt|;
block|}
return|return
name|cout
return|;
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
name|OutputStream
name|cout
init|=
literal|null
decl_stmt|;
try|try
block|{
try|try
block|{
name|cout
operator|=
name|connection
operator|.
name|getOutputStream
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ProtocolException
name|pe
parameter_list|)
block|{
name|Boolean
name|b
init|=
operator|(
name|Boolean
operator|)
name|outMessage
operator|.
name|get
argument_list|(
name|HTTPURL_CONNECTION_METHOD_REFLECTION
argument_list|)
decl_stmt|;
name|cout
operator|=
name|connectAndGetOutputStream
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SocketException
name|e
parameter_list|)
block|{
if|if
condition|(
literal|"Socket Closed"
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
condition|)
block|{
name|connection
operator|.
name|connect
argument_list|()
expr_stmt|;
name|cout
operator|=
name|connectAndGetOutputStream
argument_list|(
operator|(
name|Boolean
operator|)
name|outMessage
operator|.
name|get
argument_list|(
name|HTTPURL_CONNECTION_METHOD_REFLECTION
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
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
name|cout
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
name|cout
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
name|URLConnectionHTTPConduit
operator|.
name|this
operator|.
name|getClient
argument_list|()
operator|.
name|getChunkLength
argument_list|()
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
argument_list|,
literal|false
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
comment|// [CXF-6227] do not call connection.setFixedLengthStreamingMode(i)
comment|// to prevent https://bugs.openjdk.java.net/browse/JDK-8044726
block|}
specifier|protected
name|void
name|handleNoOutput
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
literal|"POST"
operator|.
name|equals
argument_list|(
name|getMethod
argument_list|()
argument_list|)
condition|)
block|{
name|connection
operator|.
name|getOutputStream
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
name|Address
name|address
decl_stmt|;
try|try
block|{
if|if
condition|(
name|defaultAddress
operator|.
name|getString
argument_list|()
operator|.
name|equals
argument_list|(
name|newURL
argument_list|)
condition|)
block|{
name|address
operator|=
name|defaultAddress
expr_stmt|;
block|}
else|else
block|{
name|address
operator|=
operator|new
name|Address
argument_list|(
name|newURL
argument_list|)
expr_stmt|;
block|}
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
name|address
argument_list|,
name|cp
argument_list|)
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|address
operator|.
name|getURI
argument_list|()
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
name|Boolean
name|b
init|=
operator|(
name|Boolean
operator|)
name|outMessage
operator|.
name|get
argument_list|(
name|HTTPURL_CONNECTION_METHOD_REFLECTION
argument_list|)
decl_stmt|;
name|OutputStream
name|out
init|=
name|connectAndGetOutputStream
argument_list|(
name|b
argument_list|)
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

