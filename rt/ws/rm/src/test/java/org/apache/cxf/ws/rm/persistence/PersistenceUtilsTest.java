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
name|ws
operator|.
name|rm
operator|.
name|persistence
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
name|Collection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|util
operator|.
name|ByteArrayDataSource
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
name|attachment
operator|.
name|AttachmentImpl
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
name|CachedOutputStream
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
name|Attachment
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
name|MessageImpl
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
name|rm
operator|.
name|RMMessageConstants
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
name|rm
operator|.
name|v200702
operator|.
name|SequenceAcknowledgement
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
name|rm
operator|.
name|v200702
operator|.
name|SequenceAcknowledgement
operator|.
name|AcknowledgementRange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|PersistenceUtilsTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|String
name|MULTIPART_TYPE
init|=
literal|"multipart/related; type=\"text/xml\";"
operator|+
literal|" boundary=\"uuid:74b6a245-2e17-40eb-a86c-308664e18460\"; start=\"<root."
operator|+
literal|"message@cxf.apache.org>\"; start-info=\"application/soap+xml\""
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SOAP_PART
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<data/></soap:Envelope>"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testSerialiseDeserialiseAcknowledgement
parameter_list|()
block|{
name|SequenceAcknowledgement
name|ack
init|=
operator|new
name|SequenceAcknowledgement
argument_list|()
decl_stmt|;
name|AcknowledgementRange
name|range
init|=
operator|new
name|AcknowledgementRange
argument_list|()
decl_stmt|;
name|range
operator|.
name|setLower
argument_list|(
operator|new
name|Long
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|range
operator|.
name|setUpper
argument_list|(
operator|new
name|Long
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|ack
operator|.
name|getAcknowledgementRange
argument_list|()
operator|.
name|add
argument_list|(
name|range
argument_list|)
expr_stmt|;
name|PersistenceUtils
name|utils
init|=
name|PersistenceUtils
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|utils
operator|.
name|serialiseAcknowledgment
argument_list|(
name|ack
argument_list|)
decl_stmt|;
name|SequenceAcknowledgement
name|refAck
init|=
name|utils
operator|.
name|deserialiseAcknowledgment
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|refAck
operator|.
name|getAcknowledgementRange
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|refAck
operator|.
name|getAcknowledgementRange
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|AcknowledgementRange
name|refRange
init|=
name|refAck
operator|.
name|getAcknowledgementRange
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|range
operator|.
name|getLower
argument_list|()
argument_list|,
name|refRange
operator|.
name|getLower
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|range
operator|.
name|getUpper
argument_list|()
argument_list|,
name|refRange
operator|.
name|getUpper
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncodeRMContent
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayInputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|SOAP_PART
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|RMMessage
name|rmmsg
init|=
operator|new
name|RMMessage
argument_list|()
decl_stmt|;
name|Message
name|messageImpl
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|messageImpl
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
comment|// update rmmessage
name|PersistenceUtils
operator|.
name|encodeRMContent
argument_list|(
name|rmmsg
argument_list|,
name|messageImpl
argument_list|,
name|bis
argument_list|)
expr_stmt|;
name|assertStartsWith
argument_list|(
name|rmmsg
operator|.
name|getContent
argument_list|()
operator|.
name|getInputStream
argument_list|()
argument_list|,
literal|"<soap:"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|rmmsg
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|rmmsg
operator|.
name|getContentType
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"text/xml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncodeRMContentWithAttachments
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayInputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|SOAP_PART
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|RMMessage
name|rmmsg
init|=
operator|new
name|RMMessage
argument_list|()
decl_stmt|;
name|Message
name|messageImpl
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|messageImpl
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
comment|// add attachments
name|addAttachment
argument_list|(
name|messageImpl
argument_list|)
expr_stmt|;
comment|// update rmmessage
name|PersistenceUtils
operator|.
name|encodeRMContent
argument_list|(
name|rmmsg
argument_list|,
name|messageImpl
argument_list|,
name|bis
argument_list|)
expr_stmt|;
name|assertStartsWith
argument_list|(
name|rmmsg
operator|.
name|getContent
argument_list|()
operator|.
name|getInputStream
argument_list|()
argument_list|,
literal|"--uuid:"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|rmmsg
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|rmmsg
operator|.
name|getContentType
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"multipart/related"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncodeDecodeRMContent
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayInputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|SOAP_PART
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|RMMessage
name|rmmsg
init|=
operator|new
name|RMMessage
argument_list|()
decl_stmt|;
name|Message
name|messageImpl
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|messageImpl
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
comment|// add attachments
name|addAttachment
argument_list|(
name|messageImpl
argument_list|)
expr_stmt|;
comment|// serialize
name|PersistenceUtils
operator|.
name|encodeRMContent
argument_list|(
name|rmmsg
argument_list|,
name|messageImpl
argument_list|,
name|bis
argument_list|)
expr_stmt|;
name|Message
name|messageImplRestored
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|PersistenceUtils
operator|.
name|decodeRMContent
argument_list|(
name|rmmsg
argument_list|,
name|messageImplRestored
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|messageImplRestored
operator|.
name|getAttachments
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|CachedOutputStream
name|cos
init|=
operator|(
name|CachedOutputStream
operator|)
name|messageImplRestored
operator|.
name|get
argument_list|(
name|RMMessageConstants
operator|.
name|SAVED_CONTENT
argument_list|)
decl_stmt|;
name|assertStartsWith
argument_list|(
name|cos
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|SOAP_PART
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDecodeRMContentWithAttachment
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"SerializedRMMessage.txt"
argument_list|)
decl_stmt|;
name|CachedOutputStream
name|cos
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copyAndCloseInput
argument_list|(
name|is
argument_list|,
name|cos
argument_list|)
expr_stmt|;
name|cos
operator|.
name|flush
argument_list|()
expr_stmt|;
name|RMMessage
name|msg
init|=
operator|new
name|RMMessage
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setContent
argument_list|(
name|cos
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setContentType
argument_list|(
name|MULTIPART_TYPE
argument_list|)
expr_stmt|;
name|Message
name|messageImpl
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|PersistenceUtils
operator|.
name|decodeRMContent
argument_list|(
name|msg
argument_list|,
name|messageImpl
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|messageImpl
operator|.
name|getAttachments
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|CachedOutputStream
name|cos1
init|=
operator|(
name|CachedOutputStream
operator|)
name|messageImpl
operator|.
name|get
argument_list|(
name|RMMessageConstants
operator|.
name|SAVED_CONTENT
argument_list|)
decl_stmt|;
name|assertStartsWith
argument_list|(
name|cos1
operator|.
name|getInputStream
argument_list|()
argument_list|,
literal|"<soap:Envelope"
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|addAttachment
parameter_list|(
name|Message
name|msg
parameter_list|)
throws|throws
name|IOException
block|{
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
init|=
operator|new
name|ArrayList
argument_list|<
name|Attachment
argument_list|>
argument_list|()
decl_stmt|;
name|DataHandler
name|dh
init|=
operator|new
name|DataHandler
argument_list|(
operator|new
name|ByteArrayDataSource
argument_list|(
literal|"hello world!"
argument_list|,
literal|"text/plain"
argument_list|)
argument_list|)
decl_stmt|;
name|Attachment
name|a
init|=
operator|new
name|AttachmentImpl
argument_list|(
literal|"test.xml"
argument_list|,
name|dh
argument_list|)
decl_stmt|;
name|attachments
operator|.
name|add
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setAttachments
argument_list|(
name|attachments
argument_list|)
expr_stmt|;
block|}
comment|// just read the beginning of the input and compare it against the specified string
specifier|private
specifier|static
name|boolean
name|assertStartsWith
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|String
name|starting
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
name|starting
operator|.
name|length
argument_list|()
index|]
decl_stmt|;
try|try
block|{
name|in
operator|.
name|read
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|buf
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|starting
argument_list|,
operator|new
name|String
argument_list|(
name|buf
argument_list|,
literal|"utf-8"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
finally|finally
block|{
try|try
block|{
name|in
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
comment|// ignore
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

