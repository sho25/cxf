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
name|staxutils
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
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
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
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|MessageFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPPart
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|W3CDOMStreamReaderTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|RESULT
init|=
literal|"<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<SOAP-ENV:Header/><SOAP-ENV:Body/>"
operator|+
literal|"<Test xmlns=\"http://example.org/types\">"
operator|+
literal|"<argument>foobar</argument></Test></SOAP-ENV:Envelope>"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testReader
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayInputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<Test xmlns=\"http://example.org/types\"><argument>foobar</argument></Test>"
operator|.
name|getBytes
argument_list|(
literal|"utf-8"
argument_list|)
argument_list|)
decl_stmt|;
name|DocumentBuilderFactory
name|docBuilderFactory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|docBuilderFactory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DocumentBuilder
name|docBuilder
init|=
name|docBuilderFactory
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|MessageFactory
name|factory
init|=
name|MessageFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|SOAPMessage
name|msg
init|=
name|factory
operator|.
name|createMessage
argument_list|()
decl_stmt|;
name|SOAPPart
name|part
init|=
name|msg
operator|.
name|getSOAPPart
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
name|docBuilder
operator|.
name|parse
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|W3CDOMStreamWriter
name|writer
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|(
name|part
operator|.
name|getEnvelope
argument_list|()
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|)
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|reader
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|StaxUtils
operator|.
name|toString
argument_list|(
name|writer
operator|.
name|getDocument
argument_list|()
argument_list|)
operator|.
name|endsWith
argument_list|(
name|RESULT
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTopLevelText
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayInputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<t:Test xmlns:t=\"http://example.org/types\">gorilla</t:Test>"
operator|.
name|getBytes
argument_list|(
literal|"utf-8"
argument_list|)
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|Element
name|e
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|e
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|reader
operator|.
name|getElementText
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"gorilla"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

