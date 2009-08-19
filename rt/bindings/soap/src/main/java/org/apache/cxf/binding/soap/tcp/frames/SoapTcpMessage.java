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
operator|.
name|frames
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
name|ByteArrayOutputStream
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
name|UnsupportedEncodingException
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
name|Hashtable
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
name|DataCodingUtils
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
name|SoapTcpOutputStream
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|SoapTcpMessage
block|{
specifier|private
name|List
argument_list|<
name|SoapTcpFrame
argument_list|>
name|frames
decl_stmt|;
specifier|private
name|SoapTcpMessage
parameter_list|()
block|{
name|frames
operator|=
operator|new
name|ArrayList
argument_list|<
name|SoapTcpFrame
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
name|SoapTcpMessage
name|createSoapTcpMessage
parameter_list|(
name|SoapTcpFrame
name|frame
parameter_list|)
block|{
name|SoapTcpMessage
name|soapTcpMessage
init|=
operator|new
name|SoapTcpMessage
argument_list|()
decl_stmt|;
name|soapTcpMessage
operator|.
name|getFrames
argument_list|()
operator|.
name|add
argument_list|(
name|frame
argument_list|)
expr_stmt|;
return|return
name|soapTcpMessage
return|;
block|}
specifier|public
specifier|static
name|SoapTcpMessage
name|createSoapTcpMessage
parameter_list|(
name|List
argument_list|<
name|SoapTcpFrame
argument_list|>
name|frames
parameter_list|)
block|{
name|SoapTcpMessage
name|soapTcpMessage
init|=
operator|new
name|SoapTcpMessage
argument_list|()
decl_stmt|;
name|soapTcpMessage
operator|.
name|getFrames
argument_list|()
operator|.
name|addAll
argument_list|(
name|frames
argument_list|)
expr_stmt|;
return|return
name|soapTcpMessage
return|;
block|}
specifier|public
specifier|static
name|SoapTcpMessage
name|createSoapTcpMessage
parameter_list|(
name|String
name|message
parameter_list|,
name|int
name|channelId
parameter_list|)
block|{
name|SoapTcpMessage
name|soapTcpMessage
init|=
operator|new
name|SoapTcpMessage
argument_list|()
decl_stmt|;
try|try
block|{
name|byte
index|[]
name|msgContent
init|=
name|message
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|int
name|numOfFrames
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|ceil
argument_list|(
operator|(
name|float
operator|)
name|msgContent
operator|.
name|length
operator|/
operator|(
name|float
operator|)
name|SoapTcpOutputStream
operator|.
name|CHUNK_SIZE
argument_list|)
decl_stmt|;
if|if
condition|(
name|numOfFrames
operator|>
literal|1
condition|)
block|{
name|int
name|offset
init|=
literal|0
decl_stmt|;
name|byte
index|[]
name|payload
init|=
operator|new
name|byte
index|[
name|SoapTcpOutputStream
operator|.
name|CHUNK_SIZE
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|numOfFrames
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|==
name|numOfFrames
condition|)
block|{
name|payload
operator|=
operator|new
name|byte
index|[
name|msgContent
operator|.
name|length
operator|%
name|SoapTcpOutputStream
operator|.
name|CHUNK_SIZE
index|]
expr_stmt|;
block|}
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|payload
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|payload
index|[
name|j
index|]
operator|=
name|msgContent
index|[
name|offset
operator|+
name|j
index|]
expr_stmt|;
block|}
name|SoapTcpFrame
name|frame
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|i
operator|==
literal|1
condition|)
block|{
name|frame
operator|=
name|createSoapTcpFrame
argument_list|(
name|SoapTcpFrameHeader
operator|.
name|MESSAGE_START_CHUNK
argument_list|,
name|payload
argument_list|,
name|channelId
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|i
operator|<
name|numOfFrames
condition|)
block|{
name|frame
operator|=
name|createSoapTcpFrame
argument_list|(
name|SoapTcpFrameHeader
operator|.
name|MESSAGE_CHUNK
argument_list|,
name|payload
argument_list|,
name|channelId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|frame
operator|=
name|createSoapTcpFrame
argument_list|(
name|SoapTcpFrameHeader
operator|.
name|MESSAGE_END_CHUNK
argument_list|,
name|payload
argument_list|,
name|channelId
argument_list|)
expr_stmt|;
block|}
name|soapTcpMessage
operator|.
name|frames
operator|.
name|add
argument_list|(
name|frame
argument_list|)
expr_stmt|;
name|offset
operator|+=
name|SoapTcpOutputStream
operator|.
name|CHUNK_SIZE
expr_stmt|;
block|}
block|}
else|else
block|{
name|soapTcpMessage
operator|.
name|frames
operator|.
name|add
argument_list|(
name|createSoapTcpFrame
argument_list|(
name|SoapTcpFrameHeader
operator|.
name|SINGLE_FRAME_MESSAGE
argument_list|,
name|msgContent
argument_list|,
name|channelId
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|soapTcpMessage
return|;
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
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|SoapTcpMessage
name|createErrorMessage
parameter_list|(
name|int
name|code
parameter_list|,
name|int
name|subCode
parameter_list|,
name|String
name|description
parameter_list|,
name|int
name|channelId
parameter_list|)
block|{
name|SoapTcpMessage
name|soapTcpMessage
init|=
operator|new
name|SoapTcpMessage
argument_list|()
decl_stmt|;
name|SoapTcpFrame
name|frame
init|=
operator|new
name|SoapTcpFrame
argument_list|()
decl_stmt|;
name|SoapTcpFrameHeader
name|header
init|=
operator|new
name|SoapTcpFrameHeader
argument_list|()
decl_stmt|;
name|header
operator|.
name|setChannelId
argument_list|(
name|channelId
argument_list|)
expr_stmt|;
name|header
operator|.
name|setFrameType
argument_list|(
name|SoapTcpFrameHeader
operator|.
name|ERROR_MESSAGE
argument_list|)
expr_stmt|;
name|frame
operator|.
name|setHeader
argument_list|(
name|header
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|DataCodingUtils
operator|.
name|writeInts4
argument_list|(
name|baos
argument_list|,
name|code
argument_list|,
name|subCode
argument_list|)
expr_stmt|;
name|byte
index|[]
name|strByteArray
init|=
name|description
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|DataCodingUtils
operator|.
name|writeInt8
argument_list|(
name|baos
argument_list|,
name|strByteArray
operator|.
name|length
argument_list|)
expr_stmt|;
name|baos
operator|.
name|write
argument_list|(
name|strByteArray
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
name|frame
operator|.
name|setPayload
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|soapTcpMessage
operator|.
name|getFrames
argument_list|()
operator|.
name|add
argument_list|(
name|frame
argument_list|)
expr_stmt|;
return|return
name|soapTcpMessage
return|;
block|}
specifier|public
name|void
name|setChannelId
parameter_list|(
name|int
name|channelId
parameter_list|)
block|{
for|for
control|(
name|SoapTcpFrame
name|frame
range|:
name|frames
control|)
block|{
name|frame
operator|.
name|setChannelId
argument_list|(
name|channelId
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|int
name|getChannelId
parameter_list|()
block|{
if|if
condition|(
name|frames
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
name|frames
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getChannelId
argument_list|()
return|;
block|}
return|return
operator|-
literal|1
return|;
block|}
specifier|public
name|void
name|setFrames
parameter_list|(
name|List
argument_list|<
name|SoapTcpFrame
argument_list|>
name|frames
parameter_list|)
block|{
name|this
operator|.
name|frames
operator|=
name|frames
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|SoapTcpFrame
argument_list|>
name|getFrames
parameter_list|()
block|{
return|return
name|frames
return|;
block|}
specifier|public
name|String
name|getContent
parameter_list|()
block|{
name|StringBuffer
name|result
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|SoapTcpFrame
name|frame
range|:
name|frames
control|)
block|{
name|result
operator|.
name|append
argument_list|(
operator|new
name|String
argument_list|(
name|frame
operator|.
name|getPayload
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|InputStream
name|getContentAsStream
parameter_list|()
block|{
name|int
name|buffLength
init|=
literal|0
decl_stmt|;
for|for
control|(
name|SoapTcpFrame
name|frame
range|:
name|frames
control|)
block|{
name|buffLength
operator|+=
name|frame
operator|.
name|getPayload
argument_list|()
operator|.
name|length
expr_stmt|;
block|}
name|byte
name|buffer
index|[]
init|=
operator|new
name|byte
index|[
name|buffLength
index|]
decl_stmt|;
name|int
name|index
init|=
literal|0
decl_stmt|;
name|byte
name|payload
index|[]
init|=
literal|null
decl_stmt|;
for|for
control|(
name|SoapTcpFrame
name|frame
range|:
name|frames
control|)
block|{
name|payload
operator|=
name|frame
operator|.
name|getPayload
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|payload
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|buffer
index|[
name|index
index|]
operator|=
name|payload
index|[
name|i
index|]
expr_stmt|;
name|index
operator|++
expr_stmt|;
block|}
block|}
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|buffer
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|SoapTcpFrame
name|createSoapTcpFrame
parameter_list|(
name|int
name|frameType
parameter_list|,
name|byte
index|[]
name|payload
parameter_list|,
name|int
name|channelId
parameter_list|)
block|{
name|SoapTcpFrame
name|frame
init|=
operator|new
name|SoapTcpFrame
argument_list|()
decl_stmt|;
name|SoapTcpFrameHeader
name|header
init|=
operator|new
name|SoapTcpFrameHeader
argument_list|()
decl_stmt|;
name|SoapTcpFrameContentDescription
name|contentDesc
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|frameType
operator|==
name|SoapTcpFrameHeader
operator|.
name|SINGLE_FRAME_MESSAGE
operator|||
name|frameType
operator|==
name|SoapTcpFrameHeader
operator|.
name|MESSAGE_START_CHUNK
condition|)
block|{
name|contentDesc
operator|=
operator|new
name|SoapTcpFrameContentDescription
argument_list|()
expr_stmt|;
name|contentDesc
operator|.
name|setContentId
argument_list|(
literal|0
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
block|}
name|header
operator|.
name|setChannelId
argument_list|(
name|channelId
argument_list|)
expr_stmt|;
name|header
operator|.
name|setFrameType
argument_list|(
name|frameType
argument_list|)
expr_stmt|;
name|header
operator|.
name|setContentDescription
argument_list|(
name|contentDesc
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
name|payload
argument_list|)
expr_stmt|;
return|return
name|frame
return|;
block|}
block|}
end_class

end_unit

