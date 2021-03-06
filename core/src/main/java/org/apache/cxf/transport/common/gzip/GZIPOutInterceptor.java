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
name|common
operator|.
name|gzip
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
name|ResourceBundle
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
name|TreeMap
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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|GZIPOutputStream
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
name|i18n
operator|.
name|BundleUtils
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
name|helpers
operator|.
name|CastUtils
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
name|HttpHeaderHelper
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
name|interceptor
operator|.
name|MessageSenderInterceptor
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
name|AbstractThresholdOutputStream
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
name|Exchange
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
name|phase
operator|.
name|AbstractPhaseInterceptor
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
name|phase
operator|.
name|Phase
import|;
end_import

begin_comment
comment|/**  * CXF interceptor that compresses outgoing messages using gzip and sets the  * HTTP Content-Encoding header appropriately. An instance of this class should  * be added as an out interceptor on clients that need to talk to a service that  * accepts gzip-encoded requests or on a service that wants to be able to return  * compressed responses. In server mode, the interceptor only compresses  * responses if the client indicated (via an Accept-Encoding header on the  * request) that it can understand them. To handle gzip-encoded input messages,  * see {@link GZIPInInterceptor}. This interceptor supports a compression  * {@link #threshold} (default 1kB) - messages smaller than this threshold will  * not be compressed. To force compression of all messages, set the threshold to  * 0. This class was originally based on one of the CXF samples  * (configuration_interceptor).  */
end_comment

begin_class
specifier|public
class|class
name|GZIPOutInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
comment|/**      * Enum giving the possible values for whether we should gzip a particular      * message.      */
specifier|public
enum|enum
name|UseGzip
block|{
name|NO
block|,
name|YES
block|,
name|FORCE
block|}
comment|/**      * regular expression that matches any encoding with a      * q-value of 0 (or 0.0, 0.00, etc.).      */
specifier|public
specifier|static
specifier|final
name|Pattern
name|ZERO_Q
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|";\\s*q=0(?:\\.0+)?$"
argument_list|)
decl_stmt|;
comment|/**      * regular expression which can split encodings      */
specifier|public
specifier|static
specifier|final
name|Pattern
name|ENCODINGS
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"[,\\s]*,\\s*"
argument_list|)
decl_stmt|;
comment|/**      * Key under which we store the original output stream on the message, for      * use by the ending interceptor.      */
specifier|public
specifier|static
specifier|final
name|String
name|ORIGINAL_OUTPUT_STREAM_KEY
init|=
name|GZIPOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".originalOutputStream"
decl_stmt|;
comment|/**      * Key under which we store an indication of whether compression is      * permitted or required, for use by the ending interceptor.      */
specifier|public
specifier|static
specifier|final
name|String
name|USE_GZIP_KEY
init|=
name|GZIPOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".useGzip"
decl_stmt|;
comment|/**      * Key under which we store the name which should be used for the      * content-encoding of the outgoing message. Typically "gzip" but may be      * "x-gzip" if we are processing a response message and this is the name      * given by the client in Accept-Encoding.      */
specifier|public
specifier|static
specifier|final
name|String
name|GZIP_ENCODING_KEY
init|=
name|GZIPOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".gzipEncoding"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SOAP_JMS_CONTENTENCODING
init|=
literal|"SOAPJMS_contentEncoding"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|GZIPOutInterceptor
operator|.
name|class
argument_list|)
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
name|GZIPOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Compression threshold in bytes - messages smaller than this will not be      * compressed.      */
specifier|private
name|int
name|threshold
init|=
literal|1024
decl_stmt|;
specifier|private
name|boolean
name|force
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|supportedPayloadContentTypes
decl_stmt|;
specifier|public
name|GZIPOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PREPARE_SEND
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|MessageSenderInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|GZIPOutInterceptor
parameter_list|(
name|int
name|threshold
parameter_list|)
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PREPARE_SEND
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|MessageSenderInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|threshold
operator|=
name|threshold
expr_stmt|;
block|}
specifier|public
name|void
name|setThreshold
parameter_list|(
name|int
name|threshold
parameter_list|)
block|{
name|this
operator|.
name|threshold
operator|=
name|threshold
expr_stmt|;
block|}
specifier|public
name|int
name|getThreshold
parameter_list|()
block|{
return|return
name|threshold
return|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|UseGzip
name|use
init|=
name|gzipPermitted
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|use
operator|!=
name|UseGzip
operator|.
name|NO
condition|)
block|{
comment|// remember the original output stream, we will write compressed
comment|// data to this later
name|OutputStream
name|os
init|=
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|os
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|message
operator|.
name|put
argument_list|(
name|ORIGINAL_OUTPUT_STREAM_KEY
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|USE_GZIP_KEY
argument_list|,
name|use
argument_list|)
expr_stmt|;
comment|// new stream to cache the message
name|GZipThresholdOutputStream
name|cs
init|=
operator|new
name|GZipThresholdOutputStream
argument_list|(
name|threshold
argument_list|,
name|os
argument_list|,
name|use
operator|==
name|UseGzip
operator|.
name|FORCE
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|cs
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Checks whether we can, cannot or must use gzip compression on this output      * message. Gzip is always permitted if the message is a client request. If      * the message is a server response we check the Accept-Encoding header of      * the corresponding request message - with no Accept-Encoding we assume      * that gzip is not permitted. For the full gory details, see<a      * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.3">section      * 14.3 of RFC 2616</a> (HTTP 1.1).      *      * @param message the outgoing message.      * @return whether to attempt gzip compression for this message.      * @throws Fault if the Accept-Encoding header does not allow any encoding      *                 that we can support (identity, gzip or x-gzip).      */
specifier|public
name|UseGzip
name|gzipPermitted
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|UseGzip
name|permitted
init|=
name|UseGzip
operator|.
name|NO
decl_stmt|;
if|if
condition|(
name|supportedPayloadContentTypes
operator|!=
literal|null
operator|&&
name|message
operator|.
name|containsKey
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
operator|&&
operator|!
name|supportedPayloadContentTypes
operator|.
name|contains
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|permitted
return|;
block|}
if|if
condition|(
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Requestor role, so gzip enabled"
argument_list|)
expr_stmt|;
name|Object
name|o
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|USE_GZIP_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|UseGzip
condition|)
block|{
name|permitted
operator|=
operator|(
name|UseGzip
operator|)
name|o
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
name|permitted
operator|=
name|UseGzip
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|permitted
operator|=
name|force
condition|?
name|UseGzip
operator|.
name|YES
else|:
name|UseGzip
operator|.
name|NO
expr_stmt|;
block|}
name|message
operator|.
name|put
argument_list|(
name|GZIP_ENCODING_KEY
argument_list|,
literal|"gzip"
argument_list|)
expr_stmt|;
name|addHeader
argument_list|(
name|message
argument_list|,
literal|"Accept-Encoding"
argument_list|,
literal|"gzip;q=1.0, identity; q=0.5, *;q=0"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Response role, checking accept-encoding"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|Message
name|request
init|=
name|exchange
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|requestHeaders
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|request
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|requestHeaders
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|acceptEncodingHeader
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|HttpHeaderHelper
operator|.
name|getHeader
argument_list|(
name|requestHeaders
argument_list|,
name|HttpHeaderHelper
operator|.
name|ACCEPT_ENCODING
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|jmsEncodingHeader
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|requestHeaders
operator|.
name|get
argument_list|(
name|SOAP_JMS_CONTENTENCODING
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|jmsEncodingHeader
operator|!=
literal|null
operator|&&
name|jmsEncodingHeader
operator|.
name|contains
argument_list|(
literal|"gzip"
argument_list|)
condition|)
block|{
name|permitted
operator|=
name|UseGzip
operator|.
name|YES
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|GZIP_ENCODING_KEY
argument_list|,
literal|"gzip"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|acceptEncodingHeader
operator|!=
literal|null
condition|)
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
literal|"Accept-Encoding header: "
operator|+
name|acceptEncodingHeader
argument_list|)
expr_stmt|;
block|}
comment|// Accept-Encoding is a comma separated list of entries, so
comment|// we split it into its component parts and build two
comment|// lists, one with all the "q=0" encodings and the other
comment|// with the rest (no q, or q=<non-zero>).
name|List
argument_list|<
name|String
argument_list|>
name|zeros
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|nonZeros
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|3
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|headerLine
range|:
name|acceptEncodingHeader
control|)
block|{
name|String
index|[]
name|encodings
init|=
name|ENCODINGS
operator|.
name|split
argument_list|(
name|headerLine
operator|.
name|trim
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|enc
range|:
name|encodings
control|)
block|{
name|Matcher
name|m
init|=
name|ZERO_Q
operator|.
name|matcher
argument_list|(
name|enc
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|find
argument_list|()
condition|)
block|{
name|zeros
operator|.
name|add
argument_list|(
name|enc
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|m
operator|.
name|start
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|enc
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|nonZeros
operator|.
name|add
argument_list|(
name|enc
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|enc
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|nonZeros
operator|.
name|add
argument_list|(
name|enc
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// identity encoding is permitted if (a) it is not
comment|// specifically disabled by an identity;q=0 and (b) if
comment|// there is a *;q=0 then there is also an explicit
comment|// identity[;q=<non-zero>]
comment|//
comment|// [x-]gzip is permitted if (a) there is an explicit
comment|// [x-]gzip[;q=<non-zero>], or (b) there is a
comment|// *[;q=<non-zero>] and no [x-]gzip;q=0 to disable it.
name|boolean
name|identityEnabled
init|=
operator|!
name|zeros
operator|.
name|contains
argument_list|(
literal|"identity"
argument_list|)
operator|&&
operator|(
operator|!
name|zeros
operator|.
name|contains
argument_list|(
literal|"*"
argument_list|)
operator|||
name|nonZeros
operator|.
name|contains
argument_list|(
literal|"identity"
argument_list|)
operator|)
decl_stmt|;
name|boolean
name|gzipEnabled
init|=
name|nonZeros
operator|.
name|contains
argument_list|(
literal|"gzip"
argument_list|)
operator|||
operator|(
name|nonZeros
operator|.
name|contains
argument_list|(
literal|"*"
argument_list|)
operator|&&
operator|!
name|zeros
operator|.
name|contains
argument_list|(
literal|"gzip"
argument_list|)
operator|)
decl_stmt|;
name|boolean
name|xGzipEnabled
init|=
name|nonZeros
operator|.
name|contains
argument_list|(
literal|"x-gzip"
argument_list|)
operator|||
operator|(
name|nonZeros
operator|.
name|contains
argument_list|(
literal|"*"
argument_list|)
operator|&&
operator|!
name|zeros
operator|.
name|contains
argument_list|(
literal|"x-gzip"
argument_list|)
operator|)
decl_stmt|;
if|if
condition|(
name|identityEnabled
operator|&&
operator|!
name|gzipEnabled
operator|&&
operator|!
name|xGzipEnabled
condition|)
block|{
name|permitted
operator|=
name|UseGzip
operator|.
name|NO
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|identityEnabled
operator|&&
name|gzipEnabled
condition|)
block|{
name|permitted
operator|=
name|UseGzip
operator|.
name|YES
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|GZIP_ENCODING_KEY
argument_list|,
literal|"gzip"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|identityEnabled
operator|&&
name|xGzipEnabled
condition|)
block|{
name|permitted
operator|=
name|UseGzip
operator|.
name|YES
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|GZIP_ENCODING_KEY
argument_list|,
literal|"x-gzip"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|identityEnabled
operator|&&
name|gzipEnabled
condition|)
block|{
name|permitted
operator|=
name|UseGzip
operator|.
name|FORCE
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|GZIP_ENCODING_KEY
argument_list|,
literal|"gzip"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|identityEnabled
operator|&&
name|xGzipEnabled
condition|)
block|{
name|permitted
operator|=
name|UseGzip
operator|.
name|FORCE
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|GZIP_ENCODING_KEY
argument_list|,
literal|"x-gzip"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"NO_SUPPORTED_ENCODING"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"No accept-encoding header"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
literal|"gzip permitted: "
operator|+
name|permitted
argument_list|)
expr_stmt|;
block|}
return|return
name|permitted
return|;
block|}
specifier|static
class|class
name|GZipThresholdOutputStream
extends|extends
name|AbstractThresholdOutputStream
block|{
name|Message
name|message
decl_stmt|;
name|GZipThresholdOutputStream
parameter_list|(
name|int
name|t
parameter_list|,
name|OutputStream
name|orig
parameter_list|,
name|boolean
name|force
parameter_list|,
name|Message
name|msg
parameter_list|)
block|{
name|super
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|super
operator|.
name|wrappedStream
operator|=
name|orig
expr_stmt|;
name|message
operator|=
name|msg
expr_stmt|;
if|if
condition|(
name|force
condition|)
block|{
name|setupGZip
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|setupGZip
parameter_list|()
block|{          }
annotation|@
name|Override
specifier|public
name|void
name|thresholdNotReached
parameter_list|()
block|{
comment|//nothing
name|LOG
operator|.
name|fine
argument_list|(
literal|"Message is smaller than compression threshold, not compressing."
argument_list|)
expr_stmt|;
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
name|LOG
operator|.
name|fine
argument_list|(
literal|"Compressing message."
argument_list|)
expr_stmt|;
comment|// Set the Content-Encoding HTTP header
name|String
name|enc
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|GZIP_ENCODING_KEY
argument_list|)
decl_stmt|;
name|addHeader
argument_list|(
name|message
argument_list|,
literal|"Content-Encoding"
argument_list|,
name|enc
argument_list|)
expr_stmt|;
comment|// if this is a response message, add the Vary header
if|if
condition|(
operator|!
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
argument_list|)
condition|)
block|{
name|addHeader
argument_list|(
name|message
argument_list|,
literal|"Vary"
argument_list|,
literal|"Accept-Encoding"
argument_list|)
expr_stmt|;
block|}
comment|// gzip the result
name|GZIPOutputStream
name|zipOutput
init|=
operator|new
name|GZIPOutputStream
argument_list|(
name|wrappedStream
argument_list|)
decl_stmt|;
name|wrappedStream
operator|=
name|zipOutput
expr_stmt|;
block|}
block|}
comment|/**      * Adds a value to a header. If the given header name is not currently      * set in the message, an entry is created with the given single value.      * If the header is already set, the value is appended to the first      * element of the list, following a comma.      *      * @param message the message      * @param name the header to set      * @param value the value to add      */
specifier|private
specifier|static
name|void
name|addHeader
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|protocolHeaders
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|protocolHeaders
operator|==
literal|null
condition|)
block|{
name|protocolHeaders
operator|=
operator|new
name|TreeMap
argument_list|<>
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|protocolHeaders
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|header
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|protocolHeaders
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|header
operator|==
literal|null
condition|)
block|{
name|header
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|protocolHeaders
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|header
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|header
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|header
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|header
operator|.
name|set
argument_list|(
literal|0
argument_list|,
name|header
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|+
literal|","
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setForce
parameter_list|(
name|boolean
name|force
parameter_list|)
block|{
name|this
operator|.
name|force
operator|=
name|force
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getSupportedPayloadContentTypes
parameter_list|()
block|{
return|return
name|supportedPayloadContentTypes
return|;
block|}
specifier|public
name|void
name|setSupportedPayloadContentTypes
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|supportedPayloadContentTypes
parameter_list|)
block|{
name|this
operator|.
name|supportedPayloadContentTypes
operator|=
name|supportedPayloadContentTypes
expr_stmt|;
block|}
block|}
end_class

end_unit

