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
name|attachment
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
name|java
operator|.
name|util
operator|.
name|Properties
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
name|activation
operator|.
name|DataSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|MimeBodyPart
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|MimeMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|MimeMultipart
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

begin_class
specifier|public
class|class
name|AttachmentSerializerTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testMessageWriteXopOn
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestMessageWrite
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMessageWriteXopOff
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestMessageWrite
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestMessageWrite
parameter_list|(
name|boolean
name|xop
parameter_list|)
throws|throws
name|Exception
block|{
name|MessageImpl
name|msg
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|atts
init|=
operator|new
name|ArrayList
argument_list|<
name|Attachment
argument_list|>
argument_list|()
decl_stmt|;
name|AttachmentImpl
name|a
init|=
operator|new
name|AttachmentImpl
argument_list|(
literal|"test.xml"
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"my.wav"
argument_list|)
decl_stmt|;
name|ByteArrayDataSource
name|ds
init|=
operator|new
name|ByteArrayDataSource
argument_list|(
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
decl_stmt|;
name|a
operator|.
name|setDataHandler
argument_list|(
operator|new
name|DataHandler
argument_list|(
name|ds
argument_list|)
argument_list|)
expr_stmt|;
name|atts
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
name|atts
argument_list|)
expr_stmt|;
comment|// Set the SOAP content type
name|msg
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/soap+xml"
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|AttachmentSerializer
name|serializer
init|=
operator|new
name|AttachmentSerializer
argument_list|(
name|msg
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|xop
condition|)
block|{
comment|// default is "on"
name|serializer
operator|.
name|setXop
argument_list|(
name|xop
argument_list|)
expr_stmt|;
block|}
name|serializer
operator|.
name|writeProlog
argument_list|()
expr_stmt|;
name|String
name|ct
init|=
operator|(
name|String
operator|)
name|msg
operator|.
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ct
operator|.
name|indexOf
argument_list|(
literal|"multipart/related;"
argument_list|)
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ct
operator|.
name|indexOf
argument_list|(
literal|"start=\"<root.message@cxf.apache.org>\""
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ct
operator|.
name|indexOf
argument_list|(
literal|"start-info=\"application/soap+xml\""
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|"<soap:Body/>"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|serializer
operator|.
name|writeAttachments
argument_list|()
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|DataSource
name|source
init|=
operator|new
name|ByteArrayDataSource
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|,
name|ct
argument_list|)
decl_stmt|;
name|MimeMultipart
name|mpart
init|=
operator|new
name|MimeMultipart
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|Session
name|session
init|=
name|Session
operator|.
name|getDefaultInstance
argument_list|(
operator|new
name|Properties
argument_list|()
argument_list|)
decl_stmt|;
name|MimeMessage
name|inMsg
init|=
operator|new
name|MimeMessage
argument_list|(
name|session
argument_list|)
decl_stmt|;
name|inMsg
operator|.
name|setContent
argument_list|(
name|mpart
argument_list|)
expr_stmt|;
name|inMsg
operator|.
name|addHeaderLine
argument_list|(
literal|"Content-Type: "
operator|+
name|ct
argument_list|)
expr_stmt|;
name|MimeMultipart
name|multipart
init|=
operator|(
name|MimeMultipart
operator|)
name|inMsg
operator|.
name|getContent
argument_list|()
decl_stmt|;
name|MimeBodyPart
name|part
init|=
operator|(
name|MimeBodyPart
operator|)
name|multipart
operator|.
name|getBodyPart
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|xop
condition|)
block|{
name|assertEquals
argument_list|(
literal|"application/xop+xml; charset=UTF-8; type=\"application/soap+xml\""
argument_list|,
name|part
operator|.
name|getHeader
argument_list|(
literal|"Content-Type"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"text/xml; charset=UTF-8; type=\"application/soap+xml\""
argument_list|,
name|part
operator|.
name|getHeader
argument_list|(
literal|"Content-Type"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"binary"
argument_list|,
name|part
operator|.
name|getHeader
argument_list|(
literal|"Content-Transfer-Encoding"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<root.message@cxf.apache.org>"
argument_list|,
name|part
operator|.
name|getHeader
argument_list|(
literal|"Content-ID"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
name|part
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|bodyOut
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|bodyOut
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<soap:Body/>"
argument_list|,
name|bodyOut
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|MimeBodyPart
name|part2
init|=
operator|(
name|MimeBodyPart
operator|)
name|multipart
operator|.
name|getBodyPart
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"application/octet-stream"
argument_list|,
name|part2
operator|.
name|getHeader
argument_list|(
literal|"Content-Type"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"binary"
argument_list|,
name|part2
operator|.
name|getHeader
argument_list|(
literal|"Content-Transfer-Encoding"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<test.xml>"
argument_list|,
name|part2
operator|.
name|getHeader
argument_list|(
literal|"Content-ID"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMessageMTOM
parameter_list|()
throws|throws
name|Exception
block|{
name|MessageImpl
name|msg
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|atts
init|=
operator|new
name|ArrayList
argument_list|<
name|Attachment
argument_list|>
argument_list|()
decl_stmt|;
name|AttachmentImpl
name|a
init|=
operator|new
name|AttachmentImpl
argument_list|(
literal|"test.xml"
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"my.wav"
argument_list|)
decl_stmt|;
name|ByteArrayDataSource
name|ds
init|=
operator|new
name|ByteArrayDataSource
argument_list|(
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
decl_stmt|;
name|a
operator|.
name|setDataHandler
argument_list|(
operator|new
name|DataHandler
argument_list|(
name|ds
argument_list|)
argument_list|)
expr_stmt|;
name|atts
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
name|atts
argument_list|)
expr_stmt|;
comment|// Set the SOAP content type
name|msg
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/soap+xml"
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|AttachmentSerializer
name|serializer
init|=
operator|new
name|AttachmentSerializer
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|serializer
operator|.
name|writeProlog
argument_list|()
expr_stmt|;
name|String
name|ct
init|=
operator|(
name|String
operator|)
name|msg
operator|.
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ct
operator|.
name|indexOf
argument_list|(
literal|"multipart/related;"
argument_list|)
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ct
operator|.
name|indexOf
argument_list|(
literal|"start=\"<root.message@cxf.apache.org>\""
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ct
operator|.
name|indexOf
argument_list|(
literal|"start-info=\"application/soap+xml\""
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|"<soap:Body/>"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|serializer
operator|.
name|writeAttachments
argument_list|()
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|DataSource
name|source
init|=
operator|new
name|ByteArrayDataSource
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|,
name|ct
argument_list|)
decl_stmt|;
name|MimeMultipart
name|mpart
init|=
operator|new
name|MimeMultipart
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|Session
name|session
init|=
name|Session
operator|.
name|getDefaultInstance
argument_list|(
operator|new
name|Properties
argument_list|()
argument_list|)
decl_stmt|;
name|MimeMessage
name|inMsg
init|=
operator|new
name|MimeMessage
argument_list|(
name|session
argument_list|)
decl_stmt|;
name|inMsg
operator|.
name|setContent
argument_list|(
name|mpart
argument_list|)
expr_stmt|;
name|inMsg
operator|.
name|addHeaderLine
argument_list|(
literal|"Content-Type: "
operator|+
name|ct
argument_list|)
expr_stmt|;
name|MimeMultipart
name|multipart
init|=
operator|(
name|MimeMultipart
operator|)
name|inMsg
operator|.
name|getContent
argument_list|()
decl_stmt|;
name|MimeBodyPart
name|part
init|=
operator|(
name|MimeBodyPart
operator|)
name|multipart
operator|.
name|getBodyPart
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"application/xop+xml; charset=UTF-8; type=\"application/soap+xml\""
argument_list|,
name|part
operator|.
name|getHeader
argument_list|(
literal|"Content-Type"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"binary"
argument_list|,
name|part
operator|.
name|getHeader
argument_list|(
literal|"Content-Transfer-Encoding"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<root.message@cxf.apache.org>"
argument_list|,
name|part
operator|.
name|getHeader
argument_list|(
literal|"Content-ID"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
name|part
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|bodyOut
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|bodyOut
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<soap:Body/>"
argument_list|,
name|bodyOut
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|MimeBodyPart
name|part2
init|=
operator|(
name|MimeBodyPart
operator|)
name|multipart
operator|.
name|getBodyPart
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"application/octet-stream"
argument_list|,
name|part2
operator|.
name|getHeader
argument_list|(
literal|"Content-Type"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"binary"
argument_list|,
name|part2
operator|.
name|getHeader
argument_list|(
literal|"Content-Transfer-Encoding"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<test.xml>"
argument_list|,
name|part2
operator|.
name|getHeader
argument_list|(
literal|"Content-ID"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

