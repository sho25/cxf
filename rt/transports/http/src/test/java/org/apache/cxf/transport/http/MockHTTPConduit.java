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
name|URI
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

begin_class
specifier|public
class|class
name|MockHTTPConduit
extends|extends
name|HTTPConduit
block|{
specifier|public
name|MockHTTPConduit
parameter_list|(
name|Bus
name|b
parameter_list|,
name|EndpointInfo
name|ei
parameter_list|,
name|HTTPClientPolicy
name|policy
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
name|setClient
argument_list|(
name|policy
argument_list|)
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
name|Address
name|address
parameter_list|,
name|HTTPClientPolicy
name|csPolicy
parameter_list|)
throws|throws
name|IOException
block|{
comment|// TODO Auto-generated method stub
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
return|return
operator|new
name|MockWrappedOutputStream
argument_list|(
name|message
argument_list|,
name|isChunking
argument_list|,
name|isChunking
argument_list|,
name|chunkThreshold
argument_list|,
literal|"mockConduit"
argument_list|,
literal|null
argument_list|)
return|;
block|}
class|class
name|MockWrappedOutputStream
extends|extends
name|WrappedOutputStream
block|{
specifier|protected
name|MockWrappedOutputStream
parameter_list|(
name|Message
name|outMessage
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
name|outMessage
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
comment|// TODO Auto-generated method stub
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
comment|// TODO Auto-generated method stub
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
comment|// TODO Auto-generated method stub
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
comment|// TODO Auto-generated method stub
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
comment|// TODO Auto-generated method stub
return|return
literal|0
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
comment|// TODO Auto-generated method stub
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
comment|// TODO Auto-generated method stub
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
comment|// TODO Auto-generated method stub
block|}
annotation|@
name|Override
specifier|protected
name|void
name|handleResponseInternal
parameter_list|()
throws|throws
name|IOException
block|{
name|outMessage
operator|.
name|put
argument_list|(
name|Thread
operator|.
name|class
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
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
block|{
comment|// TODO Auto-generated method stub
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|usingProxy
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
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
comment|// TODO Auto-generated method stub
return|return
literal|null
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
comment|// TODO Auto-generated method stub
return|return
literal|null
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
comment|// TODO Auto-generated method stub
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
comment|// TODO Auto-generated method stub
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
comment|// TODO Auto-generated method stub
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
comment|// TODO Auto-generated method stub
block|}
block|}
block|}
end_class

end_unit

