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
name|ByteArrayInputStream
import|;
end_import

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
name|PrintStream
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
name|Iterator
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

begin_class
specifier|public
specifier|final
class|class
name|SoapTcpUtils
block|{
specifier|private
name|SoapTcpUtils
parameter_list|()
block|{              }
specifier|public
specifier|static
name|void
name|writeSoapTcpMessage
parameter_list|(
specifier|final
name|OutputStream
name|out
parameter_list|,
specifier|final
name|SoapTcpMessage
name|msg
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|SoapTcpFrame
name|frame
range|:
name|msg
operator|.
name|getFrames
argument_list|()
control|)
block|{
name|writeMessageFrame
argument_list|(
name|out
argument_list|,
name|frame
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Method  that writes single SoapTcpFrame      * @param out      * @param frame      * @throws IOException      */
specifier|public
specifier|static
name|void
name|writeMessageFrame
parameter_list|(
specifier|final
name|OutputStream
name|out
parameter_list|,
specifier|final
name|SoapTcpFrame
name|frame
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|frame
operator|!=
literal|null
condition|)
block|{
specifier|final
name|SoapTcpFrameHeader
name|header
init|=
name|frame
operator|.
name|getHeader
argument_list|()
decl_stmt|;
specifier|final
name|byte
name|payload
index|[]
init|=
name|frame
operator|.
name|getPayload
argument_list|()
decl_stmt|;
if|if
condition|(
name|header
operator|!=
literal|null
operator|&&
name|payload
operator|!=
literal|null
condition|)
block|{
name|header
operator|.
name|write
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|DataCodingUtils
operator|.
name|writeInt8
argument_list|(
name|out
argument_list|,
name|payload
operator|.
name|length
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|payload
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Method that reads single SoapTcpFrame      * @param inputStream      * @return      * @throws IOException      */
specifier|public
specifier|static
name|SoapTcpFrame
name|readMessageFrame
parameter_list|(
specifier|final
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|SoapTcpFrame
name|frame
init|=
operator|new
name|SoapTcpFrame
argument_list|()
decl_stmt|;
specifier|final
name|SoapTcpFrameHeader
name|header
init|=
operator|new
name|SoapTcpFrameHeader
argument_list|()
decl_stmt|;
name|frame
operator|.
name|setHeader
argument_list|(
name|header
argument_list|)
expr_stmt|;
specifier|final
name|int
name|response
index|[]
init|=
operator|new
name|int
index|[
literal|2
index|]
decl_stmt|;
comment|//[0] channel-id, [1] message-id
name|DataCodingUtils
operator|.
name|readInts4
argument_list|(
name|inputStream
argument_list|,
name|response
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|frame
operator|.
name|setChannelId
argument_list|(
name|response
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|header
operator|.
name|setChannelId
argument_list|(
name|response
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|header
operator|.
name|setFrameType
argument_list|(
name|response
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|response
index|[
literal|1
index|]
condition|)
block|{
case|case
name|SoapTcpFrameHeader
operator|.
name|SINGLE_FRAME_MESSAGE
case|:
name|header
operator|.
name|setContentDescription
argument_list|(
name|readContentDescription
argument_list|(
name|inputStream
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|SoapTcpFrameHeader
operator|.
name|MESSAGE_START_CHUNK
case|:
name|header
operator|.
name|setContentDescription
argument_list|(
name|readContentDescription
argument_list|(
name|inputStream
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|SoapTcpFrameHeader
operator|.
name|MESSAGE_CHUNK
case|:
break|break;
case|case
name|SoapTcpFrameHeader
operator|.
name|MESSAGE_END_CHUNK
case|:
break|break;
case|case
name|SoapTcpFrameHeader
operator|.
name|ERROR_MESSAGE
case|:
break|break;
case|case
name|SoapTcpFrameHeader
operator|.
name|NULL_MESSAGE
case|:
break|break;
default|default:
block|}
specifier|final
name|int
name|payloadLength
init|=
name|DataCodingUtils
operator|.
name|readInt8
argument_list|(
name|inputStream
argument_list|)
decl_stmt|;
specifier|final
name|byte
name|payload
index|[]
init|=
operator|new
name|byte
index|[
name|payloadLength
index|]
decl_stmt|;
if|if
condition|(
name|inputStream
operator|.
name|read
argument_list|(
name|payload
argument_list|,
literal|0
argument_list|,
name|payload
operator|.
name|length
argument_list|)
operator|!=
name|payloadLength
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|()
throw|;
block|}
name|frame
operator|.
name|setPayload
argument_list|(
name|payload
argument_list|)
expr_stmt|;
return|return
name|frame
return|;
block|}
specifier|private
specifier|static
name|SoapTcpFrameContentDescription
name|readContentDescription
parameter_list|(
specifier|final
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|int
name|response
index|[]
init|=
operator|new
name|int
index|[
literal|2
index|]
decl_stmt|;
name|DataCodingUtils
operator|.
name|readInts4
argument_list|(
name|inputStream
argument_list|,
name|response
argument_list|,
literal|2
argument_list|)
expr_stmt|;
comment|//[0] content-id, [1] number-of-parameters
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
name|response
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
specifier|final
name|int
name|numOfParams
init|=
name|response
index|[
literal|1
index|]
decl_stmt|;
specifier|final
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|numOfParams
condition|;
name|i
operator|++
control|)
block|{
name|DataCodingUtils
operator|.
name|readInts4
argument_list|(
name|inputStream
argument_list|,
name|response
argument_list|,
literal|2
argument_list|)
expr_stmt|;
comment|//[0] parameter-id, [1] string-length
if|if
condition|(
name|response
index|[
literal|1
index|]
operator|>
literal|0
condition|)
block|{
specifier|final
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|response
index|[
literal|1
index|]
index|]
decl_stmt|;
if|if
condition|(
name|inputStream
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|>
literal|0
condition|)
block|{
specifier|final
name|String
name|value
init|=
operator|new
name|String
argument_list|(
name|buffer
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|response
index|[
literal|0
index|]
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|//System.out.println("parameter-id = " + response[0] + " parameter-value = " + value);
block|}
block|}
block|}
name|contentDesc
operator|.
name|setParameters
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
return|return
name|contentDesc
return|;
block|}
comment|/**      * Method that parse SoapTcpFrame payload to find important tag.       *        * @param responseFrame frame that will be examinated      * @param elementName a tag to look for       * @return true If payload contains that tag then method return true      * otherwise return false;      */
specifier|public
specifier|static
name|boolean
name|checkSingleFrameResponse
parameter_list|(
specifier|final
name|SoapTcpFrame
name|responseFrame
parameter_list|,
specifier|final
name|String
name|elementName
parameter_list|)
block|{
if|if
condition|(
name|responseFrame
operator|!=
literal|null
operator|&&
name|responseFrame
operator|.
name|getHeader
argument_list|()
operator|.
name|getFrameType
argument_list|()
operator|==
name|SoapTcpFrameHeader
operator|.
name|SINGLE_FRAME_MESSAGE
condition|)
block|{
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|responseFrame
operator|.
name|getPayload
argument_list|()
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|xmlReader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|bais
argument_list|)
decl_stmt|;
try|try
block|{
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
operator|&&
name|xmlReader
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
name|elementName
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
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
return|return
literal|false
return|;
block|}
comment|/**      * Method that print SoapTcpFrame      * @param out      * @param frame      */
specifier|public
specifier|static
name|void
name|printSoapTcpFrame
parameter_list|(
specifier|final
name|OutputStream
name|out
parameter_list|,
specifier|final
name|SoapTcpFrame
name|frame
parameter_list|)
block|{
if|if
condition|(
name|frame
operator|!=
literal|null
condition|)
block|{
specifier|final
name|PrintStream
name|writer
init|=
operator|(
name|PrintStream
operator|)
name|out
decl_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"channel-id: "
operator|+
name|frame
operator|.
name|getChannelId
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|SoapTcpFrameHeader
name|header
init|=
name|frame
operator|.
name|getHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|header
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"frameType: "
operator|+
name|header
operator|.
name|getFrameType
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|SoapTcpFrameContentDescription
name|contentDesc
init|=
name|header
operator|.
name|getContentDescription
argument_list|()
decl_stmt|;
if|if
condition|(
name|contentDesc
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"content-id: "
operator|+
name|contentDesc
operator|.
name|getContentId
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|parameters
init|=
name|contentDesc
operator|.
name|getParameters
argument_list|()
decl_stmt|;
if|if
condition|(
name|parameters
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|keys
init|=
name|parameters
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"parameters"
argument_list|)
expr_stmt|;
while|while
condition|(
name|keys
operator|.
name|hasNext
argument_list|()
condition|)
block|{
specifier|final
name|Integer
name|key
init|=
name|keys
operator|.
name|next
argument_list|()
decl_stmt|;
specifier|final
name|String
name|value
init|=
name|parameters
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|writer
operator|.
name|println
argument_list|(
name|key
operator|+
literal|" : "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|final
name|byte
name|payload
index|[]
init|=
name|frame
operator|.
name|getPayload
argument_list|()
decl_stmt|;
if|if
condition|(
name|payload
operator|!=
literal|null
condition|)
block|{
try|try
block|{
specifier|final
name|String
name|messageContent
init|=
operator|new
name|String
argument_list|(
name|payload
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"messageContent:"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
name|messageContent
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
block|}
block|}
block|}
block|}
end_class

end_unit

