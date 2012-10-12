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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
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
name|SoapTcpMessage
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
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|core
operator|.
name|buffer
operator|.
name|IoBuffer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|core
operator|.
name|session
operator|.
name|IoSession
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|ChannelService
block|{
specifier|private
name|ChannelService
parameter_list|()
block|{              }
specifier|public
specifier|static
name|void
name|service
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|SoapTcpMessage
name|message
parameter_list|)
block|{
name|XMLStreamReader
name|xmlReader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|xmlReader
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|message
operator|.
name|getContentAsStream
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
while|while
condition|(
name|xmlReader
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|xmlReader
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
name|xmlReader
operator|.
name|getEventType
argument_list|()
operator|==
name|XMLStreamReader
operator|.
name|START_ELEMENT
condition|)
block|{
if|if
condition|(
name|xmlReader
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"initiateSession"
argument_list|)
condition|)
block|{
name|initiateSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|xmlReader
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"openChannel"
argument_list|)
condition|)
block|{
name|String
name|targetWSURI
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|negotiatedMimeTypes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|negotiatedParams
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|xmlReader
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|xmlReader
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
name|xmlReader
operator|.
name|getEventType
argument_list|()
operator|==
name|XMLStreamReader
operator|.
name|START_ELEMENT
condition|)
block|{
if|if
condition|(
name|xmlReader
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"targetWSURI"
argument_list|)
condition|)
block|{
name|targetWSURI
operator|=
name|xmlReader
operator|.
name|getElementText
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|xmlReader
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"negotiatedMimeTypes"
argument_list|)
condition|)
block|{
name|negotiatedMimeTypes
operator|.
name|add
argument_list|(
name|xmlReader
operator|.
name|getElementText
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|xmlReader
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"negotiatedParams"
argument_list|)
condition|)
block|{
name|negotiatedParams
operator|.
name|add
argument_list|(
name|xmlReader
operator|.
name|getElementText
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|openChannel
argument_list|(
name|session
argument_list|,
name|targetWSURI
argument_list|,
name|negotiatedMimeTypes
argument_list|,
name|negotiatedParams
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|xmlReader
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"closeChannel"
argument_list|)
condition|)
block|{
name|int
name|channelId
init|=
operator|-
literal|1
decl_stmt|;
while|while
condition|(
name|xmlReader
operator|.
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
name|xmlReader
operator|.
name|getEventType
argument_list|()
operator|==
name|XMLStreamReader
operator|.
name|START_ELEMENT
operator|&&
name|xmlReader
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"channelId"
argument_list|)
condition|)
block|{
name|channelId
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|xmlReader
operator|.
name|getElementText
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|closeChannel
argument_list|(
name|session
argument_list|,
name|channelId
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|XMLStreamException
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
name|StaxUtils
operator|.
name|close
argument_list|(
name|xmlReader
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|initiateSession
parameter_list|(
name|IoSession
name|session
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"initiateSession service"
argument_list|)
expr_stmt|;
name|String
name|response
init|=
literal|"<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<s:Body><initiateSessionResponse xmlns=\"http://servicechannel.tcp.transport.ws.xml.sun.com/\""
operator|+
literal|" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/"
operator|+
literal|"XMLSchema\"/></s:Body></s:Envelope>"
decl_stmt|;
name|SoapTcpMessage
name|soapTcpMessage
init|=
name|SoapTcpMessage
operator|.
name|createSoapTcpMessage
argument_list|(
name|response
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|IoBuffer
name|buffer
init|=
name|IoBuffer
operator|.
name|allocate
argument_list|(
literal|512
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|setAutoExpand
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|SoapTcpUtils
operator|.
name|writeSoapTcpMessage
argument_list|(
name|buffer
operator|.
name|asOutputStream
argument_list|()
argument_list|,
name|soapTcpMessage
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
name|buffer
operator|.
name|flip
argument_list|()
expr_stmt|;
name|session
operator|.
name|write
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
specifier|static
name|void
name|openChannel
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|String
name|targetWSURI
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|negotiatedMimeTypes
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|negotiatedParams
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"openChannel service"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|SoapTcpChannel
argument_list|>
name|channels
init|=
operator|(
name|List
argument_list|<
name|SoapTcpChannel
argument_list|>
operator|)
name|session
operator|.
name|getAttribute
argument_list|(
literal|"channels"
argument_list|)
decl_stmt|;
name|int
name|max
init|=
literal|0
decl_stmt|;
for|for
control|(
name|SoapTcpChannel
name|channel
range|:
name|channels
control|)
block|{
if|if
condition|(
name|channel
operator|.
name|getChannelId
argument_list|()
operator|>
name|max
condition|)
block|{
name|max
operator|=
name|channel
operator|.
name|getChannelId
argument_list|()
expr_stmt|;
block|}
block|}
name|channels
operator|.
name|add
argument_list|(
operator|new
name|SoapTcpChannel
argument_list|(
name|max
operator|+
literal|1
argument_list|,
name|targetWSURI
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|response
init|=
literal|"<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Body>"
operator|+
literal|"<openChannelResponse xmlns=\"http://servicechannel.tcp.transport.ws.xml.sun.com/\""
operator|+
literal|" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org"
operator|+
literal|"/2001/XMLSchema\"><channelId xmlns=\"\">"
operator|+
operator|(
name|max
operator|+
literal|1
operator|)
operator|+
literal|"</channelId><negotiatedMimeTypes xmlns=\"\">"
operator|+
literal|"application/soap+xml</negotiatedMimeTypes><negotiatedParams xmlns=\"\">charset</negotia"
operator|+
literal|"tedParams><negotiatedParams xmlns=\"\">SOAPAction</negotiatedParams><negotiatedParams xm"
operator|+
literal|"lns=\"\">action</negotiatedParams></openChannelResponse></s:Body></s:Envelope>"
decl_stmt|;
name|SoapTcpMessage
name|soapTcpMessage
init|=
name|SoapTcpMessage
operator|.
name|createSoapTcpMessage
argument_list|(
name|response
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|IoBuffer
name|buffer
init|=
name|IoBuffer
operator|.
name|allocate
argument_list|(
literal|512
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|setAutoExpand
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|SoapTcpUtils
operator|.
name|writeSoapTcpMessage
argument_list|(
name|buffer
operator|.
name|asOutputStream
argument_list|()
argument_list|,
name|soapTcpMessage
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
name|buffer
operator|.
name|flip
argument_list|()
expr_stmt|;
name|session
operator|.
name|write
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
specifier|static
name|void
name|closeChannel
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|int
name|channelId
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"closeChannel service"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|SoapTcpChannel
argument_list|>
name|channels
init|=
operator|(
name|List
argument_list|<
name|SoapTcpChannel
argument_list|>
operator|)
name|session
operator|.
name|getAttribute
argument_list|(
literal|"channels"
argument_list|)
decl_stmt|;
for|for
control|(
name|SoapTcpChannel
name|channel
range|:
name|channels
control|)
block|{
if|if
condition|(
name|channel
operator|.
name|getChannelId
argument_list|()
operator|==
name|channelId
condition|)
block|{
name|channels
operator|.
name|remove
argument_list|(
name|channel
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
name|String
name|response
init|=
literal|"<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<s:Body><closeChannelResponse xmlns=\"http://servicechannel.tcp.transport.ws.xml.sun.com/\""
operator|+
literal|" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/"
operator|+
literal|"XMLSchema\"/></s:Body></s:Envelope>"
decl_stmt|;
name|SoapTcpMessage
name|soapTcpMessage
init|=
name|SoapTcpMessage
operator|.
name|createSoapTcpMessage
argument_list|(
name|response
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|IoBuffer
name|buffer
init|=
name|IoBuffer
operator|.
name|allocate
argument_list|(
literal|512
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|setAutoExpand
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|SoapTcpUtils
operator|.
name|writeSoapTcpMessage
argument_list|(
name|buffer
operator|.
name|asOutputStream
argument_list|()
argument_list|,
name|soapTcpMessage
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
name|buffer
operator|.
name|flip
argument_list|()
expr_stmt|;
name|session
operator|.
name|write
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

