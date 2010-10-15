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
name|PushbackInputStream
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
name|Collections
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
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|SAXParser
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|SAXParserFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|helpers
operator|.
name|DefaultHandler
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
name|ExchangeImpl
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
name|message
operator|.
name|XMLMessage
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
name|Before
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
name|AttachmentDeserializerTest
extends|extends
name|Assert
block|{
specifier|private
name|MessageImpl
name|msg
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|msg
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoBoundaryInCT
parameter_list|()
throws|throws
name|Exception
block|{
comment|//CXF-2623
name|String
name|message
init|=
literal|"SomeHeader: foo\n"
operator|+
literal|"------=_Part_34950_1098328613.1263781527359\n"
operator|+
literal|"Content-Type: text/xml; charset=UTF-8\n"
operator|+
literal|"Content-Transfer-Encoding: binary\n"
operator|+
literal|"Content-Id:<318731183421.1263781527359.IBM.WEBSERVICES@auhpap02>\n"
operator|+
literal|"\n"
operator|+
literal|"<envelope/>\n"
operator|+
literal|"------=_Part_34950_1098328613.1263781527359\n"
operator|+
literal|"Content-Type: text/xml\n"
operator|+
literal|"Content-Transfer-Encoding: binary\n"
operator|+
literal|"Content-Id:<b86a5f2d-e7af-4e5e-b71a-9f6f2307cab0>\n"
operator|+
literal|"\n"
operator|+
literal|"<message>\n"
operator|+
literal|"------=_Part_34950_1098328613.1263781527359--"
decl_stmt|;
name|Matcher
name|m
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^--(\\S*)$"
argument_list|)
operator|.
name|matcher
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|m
operator|.
name|find
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^--(\\S*)$"
argument_list|,
name|Pattern
operator|.
name|MULTILINE
argument_list|)
operator|.
name|matcher
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|m
operator|.
name|find
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
name|msg
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|message
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"multipart/related"
argument_list|)
expr_stmt|;
name|AttachmentDeserializer
name|ad
init|=
operator|new
name|AttachmentDeserializer
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|ad
operator|.
name|initializeAttachments
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|msg
operator|.
name|getAttachments
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLazyAttachmentCollection
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
literal|"mimedata2"
argument_list|)
decl_stmt|;
name|String
name|ct
init|=
literal|"multipart/related; type=\"application/xop+xml\"; "
operator|+
literal|"start=\"<soap.xml@xfire.codehaus.org>\"; "
operator|+
literal|"start-info=\"text/xml; charset=utf-8\"; "
operator|+
literal|"boundary=\"----=_Part_4_701508.1145579811786\""
decl_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|ct
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|is
argument_list|)
expr_stmt|;
name|AttachmentDeserializer
name|deserializer
init|=
operator|new
name|AttachmentDeserializer
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|deserializer
operator|.
name|initializeAttachments
argument_list|()
expr_stmt|;
name|InputStream
name|attBody
init|=
name|msg
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|attBody
operator|!=
name|is
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attBody
operator|instanceof
name|DelegatingInputStream
argument_list|)
expr_stmt|;
name|attBody
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|msg
operator|.
name|getAttachments
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeserializerMtom
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
literal|"mimedata"
argument_list|)
decl_stmt|;
name|String
name|ct
init|=
literal|"multipart/related; type=\"application/xop+xml\"; "
operator|+
literal|"start=\"<soap.xml@xfire.codehaus.org>\"; "
operator|+
literal|"start-info=\"text/xml; charset=utf-8\"; "
operator|+
literal|"boundary=\"----=_Part_4_701508.1145579811786\""
decl_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|ct
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|is
argument_list|)
expr_stmt|;
name|AttachmentDeserializer
name|deserializer
init|=
operator|new
name|AttachmentDeserializer
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|deserializer
operator|.
name|initializeAttachments
argument_list|()
expr_stmt|;
name|InputStream
name|attBody
init|=
name|msg
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|attBody
operator|!=
name|is
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attBody
operator|instanceof
name|DelegatingInputStream
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|atts
init|=
name|msg
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|atts
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Attachment
argument_list|>
name|itr
init|=
name|atts
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|itr
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|Attachment
name|a
init|=
name|itr
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|InputStream
name|attIs
init|=
name|a
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
comment|// check the cached output stream
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|attBody
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"<env:Envelope"
argument_list|)
argument_list|)
expr_stmt|;
comment|// try streaming a character off the wire
name|assertTrue
argument_list|(
name|attIs
operator|.
name|read
argument_list|()
operator|==
literal|'/'
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attIs
operator|.
name|read
argument_list|()
operator|==
literal|'9'
argument_list|)
expr_stmt|;
comment|//        Attachment invalid = atts.get("INVALID");
comment|//        assertNull(invalid.getDataHandler().getInputStream());
comment|//
comment|//        assertTrue(attIs instanceof ByteArrayInputStream);
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeserializerMtomWithAxis2StyleBoundaries
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
literal|"axis2_mimedata"
argument_list|)
decl_stmt|;
name|String
name|ct
init|=
literal|"multipart/related; type=\"application/xop+xml\"; "
operator|+
literal|"start=\"<soap.xml@xfire.codehaus.org>\"; "
operator|+
literal|"start-info=\"text/xml; charset=utf-8\"; "
operator|+
literal|"boundary=MIMEBoundaryurn_uuid_6BC4984D5D38EB283C1177616488109"
decl_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|ct
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|is
argument_list|)
expr_stmt|;
name|AttachmentDeserializer
name|deserializer
init|=
operator|new
name|AttachmentDeserializer
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|deserializer
operator|.
name|initializeAttachments
argument_list|()
expr_stmt|;
name|InputStream
name|attBody
init|=
name|msg
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|attBody
operator|!=
name|is
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attBody
operator|instanceof
name|DelegatingInputStream
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|atts
init|=
name|msg
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|atts
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Attachment
argument_list|>
name|itr
init|=
name|atts
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|itr
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|Attachment
name|a
init|=
name|itr
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|InputStream
name|attIs
init|=
name|a
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
comment|// check the cached output stream
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|attBody
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"<env:Envelope"
argument_list|)
argument_list|)
expr_stmt|;
comment|// try streaming a character off the wire
name|assertTrue
argument_list|(
name|attIs
operator|.
name|read
argument_list|()
operator|==
literal|'/'
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attIs
operator|.
name|read
argument_list|()
operator|==
literal|'9'
argument_list|)
expr_stmt|;
comment|//        Attachment invalid = atts.get("INVALID");
comment|//        assertNull(invalid.getDataHandler().getInputStream());
comment|//
comment|//        assertTrue(attIs instanceof ByteArrayInputStream);
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeserializerSwA
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
literal|"swadata"
argument_list|)
decl_stmt|;
name|String
name|ct
init|=
literal|"multipart/related; type=\"text/xml\"; "
operator|+
literal|"start=\"<86048FF3556694F7DA1918466DDF8143>\";    "
operator|+
literal|"boundary=\"----=_Part_0_14158819.1167275505862\""
decl_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|ct
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|is
argument_list|)
expr_stmt|;
name|AttachmentDeserializer
name|deserializer
init|=
operator|new
name|AttachmentDeserializer
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|deserializer
operator|.
name|initializeAttachments
argument_list|()
expr_stmt|;
name|InputStream
name|attBody
init|=
name|msg
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|attBody
operator|!=
name|is
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attBody
operator|instanceof
name|DelegatingInputStream
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|atts
init|=
name|msg
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|atts
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Attachment
argument_list|>
name|itr
init|=
name|atts
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|itr
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|Attachment
name|a
init|=
name|itr
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|InputStream
name|attIs
init|=
name|a
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
comment|// check the cached output stream
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|attBody
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"<?xml"
argument_list|)
argument_list|)
expr_stmt|;
comment|// try streaming a character off the wire
name|assertTrue
argument_list|(
name|attIs
operator|.
name|read
argument_list|()
operator|==
literal|'f'
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attIs
operator|.
name|read
argument_list|()
operator|==
literal|'o'
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attIs
operator|.
name|read
argument_list|()
operator|==
literal|'o'
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attIs
operator|.
name|read
argument_list|()
operator|==
literal|'b'
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attIs
operator|.
name|read
argument_list|()
operator|==
literal|'a'
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attIs
operator|.
name|read
argument_list|()
operator|==
literal|'r'
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attIs
operator|.
name|read
argument_list|()
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeserializerSwAWithoutBoundryInContentType
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
literal|"swadata"
argument_list|)
decl_stmt|;
name|String
name|ct
init|=
literal|"multipart/related; type=\"text/xml\"; "
decl_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|ct
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|is
argument_list|)
expr_stmt|;
name|AttachmentDeserializer
name|deserializer
init|=
operator|new
name|AttachmentDeserializer
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|deserializer
operator|.
name|initializeAttachments
argument_list|()
expr_stmt|;
name|InputStream
name|attBody
init|=
name|msg
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|attBody
operator|!=
name|is
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attBody
operator|instanceof
name|DelegatingInputStream
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|atts
init|=
name|msg
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|atts
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Attachment
argument_list|>
name|itr
init|=
name|atts
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|itr
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|Attachment
name|a
init|=
name|itr
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|InputStream
name|attIs
init|=
name|a
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
comment|// check the cached output stream
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|attBody
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"<?xml"
argument_list|)
argument_list|)
expr_stmt|;
comment|// try streaming a character off the wire
name|assertTrue
argument_list|(
name|attIs
operator|.
name|read
argument_list|()
operator|==
literal|'f'
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attIs
operator|.
name|read
argument_list|()
operator|==
literal|'o'
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attIs
operator|.
name|read
argument_list|()
operator|==
literal|'o'
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attIs
operator|.
name|read
argument_list|()
operator|==
literal|'b'
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attIs
operator|.
name|read
argument_list|()
operator|==
literal|'a'
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attIs
operator|.
name|read
argument_list|()
operator|==
literal|'r'
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attIs
operator|.
name|read
argument_list|()
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|itr
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeserializerWithCachedFile
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
literal|"mimedata"
argument_list|)
decl_stmt|;
name|String
name|ct
init|=
literal|"multipart/related; type=\"application/xop+xml\"; "
operator|+
literal|"start=\"<soap.xml@xfire.codehaus.org>\"; "
operator|+
literal|"start-info=\"text/xml; charset=utf-8\"; "
operator|+
literal|"boundary=\"----=_Part_4_701508.1145579811786\""
decl_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|ct
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|is
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|AttachmentDeserializer
operator|.
name|ATTACHMENT_MEMORY_THRESHOLD
argument_list|,
literal|"10"
argument_list|)
expr_stmt|;
name|AttachmentDeserializer
name|deserializer
init|=
operator|new
name|AttachmentDeserializer
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|deserializer
operator|.
name|initializeAttachments
argument_list|()
expr_stmt|;
name|InputStream
name|attBody
init|=
name|msg
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|attBody
operator|!=
name|is
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attBody
operator|instanceof
name|DelegatingInputStream
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|atts
init|=
name|msg
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|atts
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Attachment
argument_list|>
name|itr
init|=
name|atts
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|itr
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|Attachment
name|a
init|=
name|itr
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|InputStream
name|attIs
init|=
name|a
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|itr
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|attIs
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|size
argument_list|()
operator|>
literal|1000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSmallStream
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|messageBytes
init|=
operator|(
literal|"------=_Part_1\n\nJJJJ\n------=_Part_1\n\n"
operator|+
literal|"Content-Transfer-Encoding: binary\n\n=3D=3D=3D\n------=_Part_1\n"
operator|)
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|PushbackInputStream
name|pushbackStream
init|=
operator|new
name|PushbackInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|messageBytes
argument_list|)
argument_list|,
literal|2048
argument_list|)
decl_stmt|;
name|pushbackStream
operator|.
name|read
argument_list|(
operator|new
name|byte
index|[
literal|4096
index|]
argument_list|,
literal|0
argument_list|,
literal|4015
argument_list|)
expr_stmt|;
name|pushbackStream
operator|.
name|unread
argument_list|(
name|messageBytes
argument_list|)
expr_stmt|;
name|pushbackStream
operator|.
name|read
argument_list|(
operator|new
name|byte
index|[
literal|72
index|]
argument_list|)
expr_stmt|;
name|MimeBodyPartInputStream
name|m
init|=
operator|new
name|MimeBodyPartInputStream
argument_list|(
name|pushbackStream
argument_list|,
literal|"------=_Part_1"
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|2048
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|m
operator|.
name|read
argument_list|(
operator|new
name|byte
index|[
literal|1000
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|m
operator|.
name|read
argument_list|(
operator|new
name|byte
index|[
literal|1000
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|m
operator|.
name|read
argument_list|(
operator|new
name|byte
index|[
literal|1000
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCXF2542
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"------=_Part_0_2180223.1203118300920\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"Content-Type: application/xop+xml; charset=UTF-8; type=\"text/xml\"\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"Content-Transfer-Encoding: 8bit\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"Content-ID:<soap.xml@xfire.codehaus.org>\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" "
operator|+
literal|"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
operator|+
literal|"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
operator|+
literal|"<soap:Body><getNextMessage xmlns=\"http://foo.bar\" /></soap:Body>"
operator|+
literal|"</soap:Envelope>\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"------=_Part_0_2180223.1203118300920--\n"
argument_list|)
expr_stmt|;
name|InputStream
name|rawInputStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|MessageImpl
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|rawInputStream
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"multipart/related; type=\"application/xop+xml\"; "
operator|+
literal|"start=\"<soap.xml@xfire.codehaus.org>\"; "
operator|+
literal|"start-info=\"text/xml\"; boundary=\"----=_Part_0_2180223.1203118300920\""
argument_list|)
expr_stmt|;
operator|new
name|AttachmentDeserializer
argument_list|(
name|message
argument_list|)
operator|.
name|initializeAttachments
argument_list|()
expr_stmt|;
name|InputStream
name|inputStreamWithoutAttachments
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
name|SAXParser
name|parser
init|=
name|SAXParserFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newSAXParser
argument_list|()
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|(
name|inputStreamWithoutAttachments
argument_list|,
operator|new
name|DefaultHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|imitateAttachmentInInterceptorForMessageWithMissingBoundary
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayInputStream
name|inputStream
decl_stmt|;
name|String
name|contentType
init|=
literal|"multipart/mixed;boundary=abc123"
decl_stmt|;
name|String
name|data
init|=
literal|"--abc123\r\n\r\n<Document></Document>\r\n\r\n"
decl_stmt|;
name|Message
name|message
decl_stmt|;
name|inputStream
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|=
operator|new
name|XMLMessage
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|inputStream
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|AttachmentDeserializer
operator|.
name|ATTACHMENT_DIRECTORY
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|AttachmentDeserializer
operator|.
name|ATTACHMENT_MEMORY_THRESHOLD
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|AttachmentDeserializer
operator|.
name|THRESHOLD
argument_list|)
argument_list|)
expr_stmt|;
name|AttachmentDeserializer
name|ad
init|=
operator|new
name|AttachmentDeserializer
argument_list|(
name|message
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"multipart/mixed"
argument_list|)
argument_list|)
decl_stmt|;
name|ad
operator|.
name|initializeAttachments
argument_list|()
expr_stmt|;
name|message
operator|.
name|getAttachments
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDoesntReturnZero
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|contentType
init|=
literal|"multipart/mixed;boundary=----=_Part_1"
decl_stmt|;
name|byte
index|[]
name|messageBytes
init|=
operator|(
literal|"------=_Part_1\n\n"
operator|+
literal|"JJJJ\n"
operator|+
literal|"------=_Part_1"
operator|+
literal|"\n\nContent-Transfer-Encoding: binary\n\n"
operator|+
literal|"ABCD1\r\n"
operator|+
literal|"------=_Part_1"
operator|+
literal|"\n\nContent-Transfer-Encoding: binary\n\n"
operator|+
literal|"ABCD2\r\n"
operator|+
literal|"------=_Part_1"
operator|+
literal|"\n\nContent-Transfer-Encoding: binary\n\n"
operator|+
literal|"ABCD3\r\n"
operator|+
literal|"------=_Part_1--"
operator|)
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|in
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|messageBytes
argument_list|)
block|{
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
block|{
return|return
name|super
operator|.
name|read
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
operator|>=
literal|2
condition|?
literal|2
else|:
name|len
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|AttachmentDeserializer
operator|.
name|ATTACHMENT_DIRECTORY
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|AttachmentDeserializer
operator|.
name|ATTACHMENT_MEMORY_THRESHOLD
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|AttachmentDeserializer
operator|.
name|THRESHOLD
argument_list|)
argument_list|)
expr_stmt|;
name|AttachmentDeserializer
name|ad
init|=
operator|new
name|AttachmentDeserializer
argument_list|(
name|message
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"multipart/mixed"
argument_list|)
argument_list|)
decl_stmt|;
name|ad
operator|.
name|initializeAttachments
argument_list|()
expr_stmt|;
name|String
name|s
init|=
name|getString
argument_list|(
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"JJJJ"
argument_list|,
name|s
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|count
init|=
literal|1
decl_stmt|;
for|for
control|(
name|Attachment
name|a
range|:
name|message
operator|.
name|getAttachments
argument_list|()
control|)
block|{
name|s
operator|=
name|getString
argument_list|(
name|a
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ABCD"
operator|+
name|count
operator|++
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getString
parameter_list|(
name|InputStream
name|ins
parameter_list|)
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|bout
init|=
operator|new
name|ByteArrayOutputStream
argument_list|(
literal|100
argument_list|)
decl_stmt|;
name|byte
name|b
index|[]
init|=
operator|new
name|byte
index|[
literal|100
index|]
decl_stmt|;
name|int
name|i
init|=
name|ins
operator|.
name|read
argument_list|(
name|b
argument_list|)
decl_stmt|;
while|while
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|bout
operator|.
name|write
argument_list|(
name|b
argument_list|,
literal|0
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|i
operator|=
name|ins
operator|.
name|read
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Should not be 0"
argument_list|)
throw|;
block|}
return|return
name|bout
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

