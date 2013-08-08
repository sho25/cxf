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
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|stream
operator|.
name|XMLStreamWriter
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
name|events
operator|.
name|XMLEvent
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
name|Source
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
name|Attr
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
name|xml
operator|.
name|sax
operator|.
name|InputSource
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
name|DOMUtils
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
name|StaxUtilsTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testFactoryCreation
parameter_list|()
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|getTestStream
argument_list|(
literal|"./resources/amazon.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|reader
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|private
name|InputStream
name|getTestStream
parameter_list|(
name|String
name|resource
parameter_list|)
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|resource
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCommentNode
parameter_list|()
throws|throws
name|Exception
block|{
comment|//CXF-3034
name|Document
name|document
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newDocumentBuilder
argument_list|()
operator|.
name|newDocument
argument_list|()
decl_stmt|;
name|Element
name|root
init|=
name|document
operator|.
name|createElementNS
argument_list|(
literal|"urn:test"
argument_list|,
literal|"root"
argument_list|)
decl_stmt|;
name|root
operator|.
name|appendChild
argument_list|(
name|document
operator|.
name|createComment
argument_list|(
literal|"test comment"
argument_list|)
argument_list|)
expr_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|root
argument_list|)
argument_list|,
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|System
operator|.
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToNextElement
parameter_list|()
block|{
name|String
name|soapMessage
init|=
literal|"./resources/sayHiRpcLiteralReq.xml"
decl_stmt|;
name|XMLStreamReader
name|r
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|getTestStream
argument_list|(
name|soapMessage
argument_list|)
argument_list|)
decl_stmt|;
name|DepthXMLStreamReader
name|reader
init|=
operator|new
name|DepthXMLStreamReader
argument_list|(
name|r
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|reader
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Envelope"
argument_list|,
name|reader
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|StaxUtils
operator|.
name|nextEvent
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|reader
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Body"
argument_list|,
name|reader
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToNextTag
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|soapMessage
init|=
literal|"./resources/headerSoapReq.xml"
decl_stmt|;
name|XMLStreamReader
name|r
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|getTestStream
argument_list|(
name|soapMessage
argument_list|)
argument_list|)
decl_stmt|;
name|DepthXMLStreamReader
name|reader
init|=
operator|new
name|DepthXMLStreamReader
argument_list|(
name|r
argument_list|)
decl_stmt|;
name|reader
operator|.
name|nextTag
argument_list|()
expr_stmt|;
name|StaxUtils
operator|.
name|toNextTag
argument_list|(
name|reader
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://schemas.xmlsoap.org/soap/envelope/"
argument_list|,
literal|"Body"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Body"
argument_list|,
name|reader
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCopy
parameter_list|()
throws|throws
name|Exception
block|{
comment|// do the stream copying
name|String
name|soapMessage
init|=
literal|"./resources/headerSoapReq.xml"
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|getTestStream
argument_list|(
name|soapMessage
argument_list|)
argument_list|)
decl_stmt|;
name|XMLStreamWriter
name|writer
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|baos
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
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|baos
operator|.
name|flush
argument_list|()
expr_stmt|;
comment|// write output to a string
name|String
name|output
init|=
name|baos
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// re-read the input xml doc to a string
name|InputStreamReader
name|inputStreamReader
init|=
operator|new
name|InputStreamReader
argument_list|(
name|getTestStream
argument_list|(
name|soapMessage
argument_list|)
argument_list|)
decl_stmt|;
name|StringWriter
name|stringWriter
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|char
index|[]
name|buffer
init|=
operator|new
name|char
index|[
literal|4096
index|]
decl_stmt|;
name|int
name|n
init|=
literal|0
decl_stmt|;
name|n
operator|=
name|inputStreamReader
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
while|while
condition|(
name|n
operator|>
literal|0
condition|)
block|{
name|stringWriter
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|n
operator|=
name|inputStreamReader
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
name|String
name|input
init|=
name|stringWriter
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// seach for the first begin of "<soap:Envelope" to escape the apache licenses header
name|int
name|beginIndex
init|=
name|input
operator|.
name|indexOf
argument_list|(
literal|"<soap:Envelope"
argument_list|)
decl_stmt|;
name|input
operator|=
name|input
operator|.
name|substring
argument_list|(
name|beginIndex
argument_list|)
expr_stmt|;
name|beginIndex
operator|=
name|output
operator|.
name|indexOf
argument_list|(
literal|"<soap:Envelope"
argument_list|)
expr_stmt|;
name|output
operator|=
name|output
operator|.
name|substring
argument_list|(
name|beginIndex
argument_list|)
expr_stmt|;
name|output
operator|=
name|output
operator|.
name|replaceAll
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
expr_stmt|;
name|input
operator|=
name|input
operator|.
name|replaceAll
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
expr_stmt|;
comment|// compare the input and output string
name|assertEquals
argument_list|(
name|input
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCXF2468
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|newDocument
argument_list|()
decl_stmt|;
name|doc
operator|.
name|appendChild
argument_list|(
name|doc
operator|.
name|createElementNS
argument_list|(
literal|"http://blah.org/"
argument_list|,
literal|"blah"
argument_list|)
argument_list|)
expr_stmt|;
name|Element
name|foo
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
literal|"http://blah.org/"
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
name|Attr
name|attr
init|=
name|doc
operator|.
name|createAttributeNS
argument_list|(
literal|"http://www.w3.org/2001/XMLSchema-instance"
argument_list|,
literal|"xsi:nil"
argument_list|)
decl_stmt|;
name|attr
operator|.
name|setValue
argument_list|(
literal|"true"
argument_list|)
expr_stmt|;
name|foo
operator|.
name|setAttributeNodeNS
argument_list|(
name|attr
argument_list|)
expr_stmt|;
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|appendChild
argument_list|(
name|foo
argument_list|)
expr_stmt|;
name|XMLStreamReader
name|sreader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|swriter
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|sw
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|sreader
argument_list|,
name|swriter
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|swriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"No xsi namespace: "
operator|+
name|sw
operator|.
name|toString
argument_list|()
argument_list|,
name|sw
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"XMLSchema-instance"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNonNamespaceAwareParser
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|xml
init|=
literal|"<blah xmlns=\"http://blah.org/\" xmlns:snarf=\"http://snarf.org\">"
operator|+
literal|"<foo snarf:blop=\"blop\">foo</foo></blah>"
decl_stmt|;
name|StringReader
name|reader
init|=
operator|new
name|StringReader
argument_list|(
name|xml
argument_list|)
decl_stmt|;
name|DocumentBuilderFactory
name|dbf
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|dbf
operator|.
name|setNamespaceAware
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setValidating
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
operator|.
name|parse
argument_list|(
operator|new
name|InputSource
argument_list|(
name|reader
argument_list|)
argument_list|)
decl_stmt|;
name|Source
name|source
init|=
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|dbf
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|reader
operator|=
operator|new
name|StringReader
argument_list|(
name|xml
argument_list|)
expr_stmt|;
name|Document
name|docNs
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
operator|.
name|parse
argument_list|(
operator|new
name|InputSource
argument_list|(
name|reader
argument_list|)
argument_list|)
decl_stmt|;
name|Source
name|sourceNs
init|=
operator|new
name|DOMSource
argument_list|(
name|docNs
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|sreader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|swriter
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|sw
argument_list|)
decl_stmt|;
comment|//should not throw an exception
name|StaxUtils
operator|.
name|copy
argument_list|(
name|sreader
argument_list|,
name|swriter
argument_list|)
expr_stmt|;
name|swriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|swriter
operator|.
name|close
argument_list|()
expr_stmt|;
name|String
name|output
init|=
name|sw
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|contains
argument_list|(
literal|"blah"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|contains
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|contains
argument_list|(
literal|"snarf"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|contains
argument_list|(
literal|"blop"
argument_list|)
argument_list|)
expr_stmt|;
name|sreader
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|sourceNs
argument_list|)
expr_stmt|;
name|sw
operator|=
operator|new
name|StringWriter
argument_list|()
expr_stmt|;
name|swriter
operator|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|sw
argument_list|)
expr_stmt|;
comment|//should not throw an exception
name|StaxUtils
operator|.
name|copy
argument_list|(
name|sreader
argument_list|,
name|swriter
argument_list|)
expr_stmt|;
name|swriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|swriter
operator|.
name|close
argument_list|()
expr_stmt|;
name|output
operator|=
name|sw
operator|.
name|toString
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|contains
argument_list|(
literal|"blah"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|contains
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|contains
argument_list|(
literal|"snarf"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|contains
argument_list|(
literal|"blop"
argument_list|)
argument_list|)
expr_stmt|;
name|sreader
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bout
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|swriter
operator|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|bout
argument_list|)
expr_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|sreader
argument_list|,
name|swriter
argument_list|)
expr_stmt|;
name|swriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|swriter
operator|.
name|close
argument_list|()
expr_stmt|;
name|output
operator|=
name|bout
operator|.
name|toString
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|contains
argument_list|(
literal|"blah"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|contains
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|contains
argument_list|(
literal|"snarf"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|contains
argument_list|(
literal|"blop"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEmptyNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testString
init|=
literal|"<ns1:a xmlns:ns1=\"http://www.apache.org/\"><s1 xmlns=\"\">"
operator|+
literal|"abc</s1><s2 xmlns=\"\">def</s2></ns1:a>"
decl_stmt|;
name|cycleString
argument_list|(
name|testString
argument_list|)
expr_stmt|;
name|testString
operator|=
literal|"<a xmlns=\"http://www.apache.org/\"><s1 xmlns=\"\">"
operator|+
literal|"abc</s1><s2 xmlns=\"\">def</s2></a>"
expr_stmt|;
name|cycleString
argument_list|(
name|testString
argument_list|)
expr_stmt|;
name|testString
operator|=
literal|"<a xmlns=\"http://www.apache.org/\"><s1 xmlns=\"\">"
operator|+
literal|"abc</s1><s2>def</s2></a>"
expr_stmt|;
name|cycleString
argument_list|(
name|testString
argument_list|)
expr_stmt|;
name|testString
operator|=
literal|"<ns1:a xmlns:ns1=\"http://www.apache.org/\"><s1>"
operator|+
literal|"abc</s1><s2 xmlns=\"\">def</s2></ns1:a>"
expr_stmt|;
name|cycleString
argument_list|(
name|testString
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|cycleString
parameter_list|(
name|String
name|s
parameter_list|)
throws|throws
name|Exception
block|{
name|StringReader
name|reader
init|=
operator|new
name|StringReader
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|DocumentBuilderFactory
name|dbf
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|dbf
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
operator|.
name|parse
argument_list|(
operator|new
name|InputSource
argument_list|(
name|reader
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|orig
init|=
name|StaxUtils
operator|.
name|toString
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
decl_stmt|;
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|swriter
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|sw
argument_list|)
decl_stmt|;
comment|//should not throw an exception
name|StaxUtils
operator|.
name|writeDocument
argument_list|(
name|doc
argument_list|,
name|swriter
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|swriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|swriter
operator|.
name|close
argument_list|()
expr_stmt|;
name|String
name|output
init|=
name|sw
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|s
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|W3CDOMStreamWriter
name|domwriter
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|()
decl_stmt|;
name|StaxUtils
operator|.
name|writeDocument
argument_list|(
name|doc
argument_list|,
name|domwriter
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|output
operator|=
name|StaxUtils
operator|.
name|toString
argument_list|(
name|domwriter
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|orig
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRootPI
parameter_list|()
throws|throws
name|Exception
block|{
name|DocumentBuilderFactory
name|dbf
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|dbf
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
operator|.
name|parse
argument_list|(
name|getTestStream
argument_list|(
literal|"./resources/rootMaterialTest.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|swriter
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|sw
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|writeDocument
argument_list|(
name|doc
argument_list|,
name|swriter
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|swriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|swriter
operator|.
name|close
argument_list|()
expr_stmt|;
name|String
name|output
init|=
name|sw
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|contains
argument_list|(
literal|"<?pi in='the sky'?>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|contains
argument_list|(
literal|"<?e excl='gads'?>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRootPInoProlog
parameter_list|()
throws|throws
name|Exception
block|{
name|DocumentBuilderFactory
name|dbf
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|dbf
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
operator|.
name|parse
argument_list|(
name|getTestStream
argument_list|(
literal|"./resources/rootMaterialTest.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|swriter
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|sw
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|writeDocument
argument_list|(
name|doc
argument_list|,
name|swriter
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|swriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|swriter
operator|.
name|close
argument_list|()
expr_stmt|;
name|String
name|output
init|=
name|sw
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|output
operator|.
name|contains
argument_list|(
literal|"<?pi in='the sky'?>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|output
operator|.
name|contains
argument_list|(
literal|"<?e excl='gads'?>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDefaultPrefix
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|soapMessage
init|=
literal|"./resources/AddRequest.xml"
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|getTestStream
argument_list|(
name|soapMessage
argument_list|)
argument_list|)
decl_stmt|;
name|XMLStreamWriter
name|writer
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|baos
argument_list|)
decl_stmt|;
name|StaxSource
name|staxSource
init|=
operator|new
name|StaxSource
argument_list|(
name|reader
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|staxSource
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|baos
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCXF3193
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testString
init|=
literal|"<a:elem1 xmlns:a=\"test\" xmlns:b=\"test\" a:attr1=\"value\"/>"
decl_stmt|;
name|CachingXmlEventWriter
name|writer
init|=
operator|new
name|CachingXmlEventWriter
argument_list|()
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
operator|new
name|StringReader
argument_list|(
name|testString
argument_list|)
argument_list|)
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|StringWriter
name|swriter
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|xwriter
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|swriter
argument_list|)
decl_stmt|;
for|for
control|(
name|XMLEvent
name|event
range|:
name|writer
operator|.
name|getEvents
argument_list|()
control|)
block|{
name|StaxUtils
operator|.
name|writeEvent
argument_list|(
name|event
argument_list|,
name|xwriter
argument_list|)
expr_stmt|;
block|}
name|xwriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|String
name|s
init|=
name|swriter
operator|.
name|toString
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
name|s
operator|.
name|indexOf
argument_list|(
literal|"xmlns:a"
argument_list|)
decl_stmt|;
name|idx
operator|=
name|s
operator|.
name|indexOf
argument_list|(
literal|"xmlns:a"
argument_list|,
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|idx
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCopyWithEmptyNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuilder
name|in
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|in
operator|.
name|append
argument_list|(
literal|"<foo xmlns=\"http://example.com/\">"
argument_list|)
expr_stmt|;
name|in
operator|.
name|append
argument_list|(
literal|"<bar xmlns=\"\"/>"
argument_list|)
expr_stmt|;
name|in
operator|.
name|append
argument_list|(
literal|"</foo>"
argument_list|)
expr_stmt|;
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|in
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Writer
name|out
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|writer
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|out
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
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|in
operator|.
name|toString
argument_list|()
argument_list|,
name|out
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testQName
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuilder
name|in
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|in
operator|.
name|append
argument_list|(
literal|"<f:foo xmlns:f=\"http://example.com/\">"
argument_list|)
expr_stmt|;
name|in
operator|.
name|append
argument_list|(
literal|"<bar>f:Bar</bar>"
argument_list|)
expr_stmt|;
name|in
operator|.
name|append
argument_list|(
literal|"<bar> f:Bar</bar>"
argument_list|)
expr_stmt|;
name|in
operator|.
name|append
argument_list|(
literal|"<bar>x:Bar</bar>"
argument_list|)
expr_stmt|;
name|in
operator|.
name|append
argument_list|(
literal|"</f:foo>"
argument_list|)
expr_stmt|;
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|in
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|QName
name|qname
init|=
operator|new
name|QName
argument_list|(
literal|"http://example.com/"
argument_list|,
literal|"Bar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|XMLStreamReader
operator|.
name|START_ELEMENT
argument_list|,
name|reader
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|XMLStreamReader
operator|.
name|START_ELEMENT
argument_list|,
name|reader
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
comment|// first bar
name|assertEquals
argument_list|(
name|qname
argument_list|,
name|StaxUtils
operator|.
name|readQName
argument_list|(
name|reader
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|XMLStreamReader
operator|.
name|START_ELEMENT
argument_list|,
name|reader
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
comment|// second bar
name|assertEquals
argument_list|(
name|qname
argument_list|,
name|StaxUtils
operator|.
name|readQName
argument_list|(
name|reader
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|XMLStreamReader
operator|.
name|START_ELEMENT
argument_list|,
name|reader
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
comment|// third bar
try|try
block|{
name|StaxUtils
operator|.
name|readQName
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"invalid qname in mapping"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
end_class

end_unit

