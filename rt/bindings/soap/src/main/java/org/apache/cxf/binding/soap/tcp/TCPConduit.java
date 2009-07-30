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
name|binding
operator|.
name|soap
operator|.
name|tcp
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
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Socket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
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
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|binding
operator|.
name|soap
operator|.
name|tcp
operator|.
name|frames
operator|.
name|SoapTcpFrame
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
name|binding
operator|.
name|soap
operator|.
name|tcp
operator|.
name|frames
operator|.
name|SoapTcpFrameContentDescription
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
name|binding
operator|.
name|soap
operator|.
name|tcp
operator|.
name|frames
operator|.
name|SoapTcpFrameHeader
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
name|configuration
operator|.
name|Configurable
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
name|AbstractConduit
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
name|policy
operator|.
name|Assertor
import|;
end_import

begin_class
specifier|public
class|class
name|TCPConduit
extends|extends
name|AbstractConduit
implements|implements
name|Configurable
implements|,
name|Assertor
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
name|TCPConduit
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MAGIC_IDENTIFIER
init|=
literal|"vnd.sun.ws.tcp"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|PROTOCOL_VERSION_MAJOR
init|=
literal|1
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|PROTOCOL_VERSION_MINOR
init|=
literal|0
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|CONNECTION_MANAGEMENT_VERSION_MAJOR
init|=
literal|1
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|CONNECTION_MANAGEMENT_VERSION_MINOR
init|=
literal|0
decl_stmt|;
specifier|private
name|Socket
name|socket
decl_stmt|;
specifier|private
name|InputStream
name|in
decl_stmt|;
specifier|private
name|OutputStream
name|out
decl_stmt|;
specifier|private
name|String
name|endPointAddress
decl_stmt|;
specifier|public
name|TCPConduit
parameter_list|(
name|EndpointInfo
name|t
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|t
operator|.
name|getTarget
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|TCPConduit
parameter_list|(
name|EndpointReferenceType
name|t
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|String
name|hostName
init|=
literal|null
decl_stmt|;
name|int
name|port
init|=
literal|0
decl_stmt|;
name|String
name|address
init|=
name|t
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|address
operator|.
name|contains
argument_list|(
literal|"soap.tcp://"
argument_list|)
condition|)
block|{
name|endPointAddress
operator|=
name|address
expr_stmt|;
name|int
name|beginIndex
init|=
name|address
operator|.
name|indexOf
argument_list|(
literal|"://"
argument_list|)
decl_stmt|;
name|int
name|endIndex
init|=
name|address
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|,
name|beginIndex
operator|+
literal|1
argument_list|)
decl_stmt|;
name|hostName
operator|=
name|address
operator|.
name|substring
argument_list|(
name|beginIndex
operator|+
literal|3
argument_list|,
name|endIndex
argument_list|)
expr_stmt|;
name|beginIndex
operator|=
name|endIndex
expr_stmt|;
name|endIndex
operator|=
name|address
operator|.
name|indexOf
argument_list|(
literal|"/"
argument_list|,
name|beginIndex
argument_list|)
expr_stmt|;
name|port
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|address
operator|.
name|substring
argument_list|(
name|beginIndex
operator|+
literal|1
argument_list|,
name|endIndex
argument_list|)
argument_list|)
expr_stmt|;
comment|//System.out.println("hostName: " + hostName);
comment|//System.out.println("port: " + port);
block|}
name|socket
operator|=
operator|new
name|Socket
argument_list|(
name|hostName
argument_list|,
name|port
argument_list|)
expr_stmt|;
name|in
operator|=
name|socket
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
name|out
operator|=
name|socket
operator|.
name|getOutputStream
argument_list|()
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|MAGIC_IDENTIFIER
operator|.
name|getBytes
argument_list|(
literal|"US-ASCII"
argument_list|)
argument_list|)
expr_stmt|;
name|DataCodingUtils
operator|.
name|writeInts4
argument_list|(
name|out
argument_list|,
name|PROTOCOL_VERSION_MAJOR
argument_list|,
name|PROTOCOL_VERSION_MINOR
argument_list|,
name|CONNECTION_MANAGEMENT_VERSION_MAJOR
argument_list|,
name|CONNECTION_MANAGEMENT_VERSION_MINOR
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
specifier|final
name|int
name|version
index|[]
init|=
operator|new
name|int
index|[
literal|4
index|]
decl_stmt|;
name|DataCodingUtils
operator|.
name|readInts4
argument_list|(
name|in
argument_list|,
name|version
argument_list|,
literal|4
argument_list|)
expr_stmt|;
comment|//System.out.println("serverProtocolVersionMajor = " + version[0]);
comment|//System.out.println("serverProtocolVersionMinor = " + version[1]);
comment|//System.out.println("serverConnectionManagementVersionMajor = " + version[2]);
comment|//System.out.println("serverConnectionManagementVersionMinor = " + version[3]);
name|initSession
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|initSession
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|String
name|initSessionMessage
init|=
literal|"<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<s:Body><initiateSession xmlns=\"http://servicechannel.tcp.transport.ws.xml.sun.com/\""
operator|+
literal|" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
operator|+
literal|" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"/></s:Body></s:Envelope>"
decl_stmt|;
name|byte
index|[]
name|initSessionMessageBytes
init|=
literal|null
decl_stmt|;
try|try
block|{
name|initSessionMessageBytes
operator|=
name|initSessionMessage
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
specifier|final
name|SoapTcpFrameContentDescription
name|contentDesc
init|=
operator|new
name|SoapTcpFrameContentDescription
argument_list|()
decl_stmt|;
name|contentDesc
operator|.
name|setContentId
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|parameters
init|=
operator|new
name|Hashtable
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|0
argument_list|,
literal|"utf-8"
argument_list|)
expr_stmt|;
name|contentDesc
operator|.
name|setParameters
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
specifier|final
name|SoapTcpFrameHeader
name|header
init|=
operator|new
name|SoapTcpFrameHeader
argument_list|(
name|SoapTcpFrameHeader
operator|.
name|SINGLE_FRAME_MESSAGE
argument_list|,
name|contentDesc
argument_list|)
decl_stmt|;
name|SoapTcpFrame
name|frame
init|=
operator|new
name|SoapTcpFrame
argument_list|()
decl_stmt|;
name|frame
operator|.
name|setChannelId
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|frame
operator|.
name|setHeader
argument_list|(
name|header
argument_list|)
expr_stmt|;
name|frame
operator|.
name|setPayload
argument_list|(
name|initSessionMessageBytes
argument_list|)
expr_stmt|;
try|try
block|{
name|SoapTcpUtils
operator|.
name|writeMessageFrame
argument_list|(
name|out
argument_list|,
name|frame
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
specifier|final
name|SoapTcpFrame
name|response
init|=
name|SoapTcpUtils
operator|.
name|readMessageFrame
argument_list|(
name|in
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|SoapTcpUtils
operator|.
name|checkSingleFrameResponse
argument_list|(
name|response
argument_list|,
literal|"initiateSessionResponse"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Could not initiate SOAP/TCP connection."
argument_list|)
throw|;
block|}
comment|//SoapTcpUtils.printSoapTcpFrame(System.out, response);
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
specifier|public
name|String
name|getBeanName
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|assertMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|boolean
name|canAssert
parameter_list|(
name|QName
name|type
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|prepare
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|SoapTcpOutputStream
name|soapTcpOutputStream
init|=
operator|new
name|SoapTcpOutputStream
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
name|message
argument_list|,
name|endPointAddress
argument_list|,
name|incomingObserver
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
name|soapTcpOutputStream
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|(
name|Message
name|message
parameter_list|)
block|{              }
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
try|try
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

