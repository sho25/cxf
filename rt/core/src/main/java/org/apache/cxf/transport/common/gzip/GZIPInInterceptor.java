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
name|InputStream
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
name|zip
operator|.
name|GZIPInputStream
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
name|endpoint
operator|.
name|Endpoint
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
name|AttachmentInInterceptor
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
comment|/**  * CXF interceptor that uncompresses those incoming messages that have "gzip"  * content-encoding. An instance of this class should be added as an in and  * inFault interceptor on clients that need to talk to a service that returns  * gzipped responses or on services that want to accept gzipped requests. For  * clients, you probably also want to use  * {@link org.apache.cxf.transports.http.configuration.HTTPClientPolicy#setAcceptEncoding}  * to let the server know you can handle compressed responses. To compress  * outgoing messages, see {@link GZIPOutInterceptor}. This class was originally  * based on one of the CXF samples (configuration_interceptor).  */
end_comment

begin_class
specifier|public
class|class
name|GZIPInInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
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
name|GZIPInInterceptor
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
name|GZIPInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|GZIPInInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|RECEIVE
argument_list|)
expr_stmt|;
name|addBefore
argument_list|(
name|AttachmentInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
comment|// check for Content-Encoding header - we are only interested in
comment|// messages that say they are gzipped.
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
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|contentEncoding
init|=
name|HttpHeaderHelper
operator|.
name|getHeader
argument_list|(
name|protocolHeaders
argument_list|,
name|HttpHeaderHelper
operator|.
name|CONTENT_ENCODING
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentEncoding
operator|==
literal|null
condition|)
block|{
name|contentEncoding
operator|=
name|protocolHeaders
operator|.
name|get
argument_list|(
name|GZIPOutInterceptor
operator|.
name|SOAP_JMS_CONTENTENCODING
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|contentEncoding
operator|!=
literal|null
operator|&&
operator|(
name|contentEncoding
operator|.
name|contains
argument_list|(
literal|"gzip"
argument_list|)
operator|||
name|contentEncoding
operator|.
name|contains
argument_list|(
literal|"x-gzip"
argument_list|)
operator|)
condition|)
block|{
try|try
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Uncompressing response"
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// wrap an unzipping stream around the original one
name|GZIPInputStream
name|zipInput
init|=
operator|new
name|GZIPInputStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|zipInput
argument_list|)
expr_stmt|;
comment|// remove content encoding header as we've now dealt with it
for|for
control|(
name|String
name|key
range|:
name|protocolHeaders
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|key
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"Content-Encoding"
argument_list|)
condition|)
block|{
name|protocolHeaders
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
comment|//record the fact that is worked so future requests will
comment|//automatically be FI enabled
name|Endpoint
name|ep
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|ep
operator|.
name|put
argument_list|(
name|GZIPOutInterceptor
operator|.
name|USE_GZIP_KEY
argument_list|,
name|GZIPOutInterceptor
operator|.
name|UseGzip
operator|.
name|YES
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
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
literal|"COULD_NOT_UNZIP"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

