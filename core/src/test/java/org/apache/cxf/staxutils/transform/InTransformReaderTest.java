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
operator|.
name|transform
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|staxutils
operator|.
name|PartialXMLStreamReader
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

begin_class
specifier|public
class|class
name|InTransformReaderTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testReadWithDefaultNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<test xmlns=\"http://bar\"/>"
operator|.
name|getBytes
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
name|is
argument_list|)
decl_stmt|;
name|reader
operator|=
operator|new
name|InTransformReader
argument_list|(
name|reader
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"{http://bar}test"
argument_list|,
literal|"test2"
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|reader
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|bos
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<test2 xmlns=\"\"/>"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReplaceSimpleElement
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<ns:test xmlns:ns=\"http://bar\"><ns:a>1</ns:a></ns:test>"
operator|.
name|getBytes
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
name|is
argument_list|)
decl_stmt|;
name|reader
operator|=
operator|new
name|InTransformReader
argument_list|(
name|reader
argument_list|,
literal|null
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"{http://bar}a"
argument_list|,
literal|"{http://bar}a=1 2 3"
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|reader
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|bos
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<ns:test xmlns:ns=\"http://bar\"><ns:a>1 2 3</ns:a></ns:test>"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTransformAndReplaceSimpleElement
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<ns:test xmlns:ns=\"http://bar\"><ns:a>1</ns:a></ns:test>"
operator|.
name|getBytes
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
name|is
argument_list|)
decl_stmt|;
name|reader
operator|=
operator|new
name|InTransformReader
argument_list|(
name|reader
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"{http://bar}*"
argument_list|,
literal|"{http://foo}*"
argument_list|)
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"{http://bar}a"
argument_list|,
literal|"{http://bar}a=1 2 3"
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|reader
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|bos
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<ns:test xmlns:ns=\"http://foo\"><ns:a>1 2 3</ns:a></ns:test>"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadWithParentDefaultNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<test xmlns=\"http://bar\"><ns:subtest xmlns:ns=\"http://bar1\"/></test>"
operator|.
name|getBytes
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
name|is
argument_list|)
decl_stmt|;
name|reader
operator|=
operator|new
name|InTransformReader
argument_list|(
name|reader
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"{http://bar1}subtest"
argument_list|,
literal|"subtest"
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|reader
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|bos
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<test xmlns=\"http://bar\"><subtest xmlns=\"\"/></test>"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|// additional test cases
annotation|@
name|Test
specifier|public
name|void
name|testReadWithComplexRequestSameNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|InTransformReader
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"../resources/complexReqIn1.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|inMap
operator|.
name|put
argument_list|(
literal|"{http://cxf.apache.org/transform/header/element}*"
argument_list|,
literal|"{http://cxf.apache.org/transform/header/element}*"
argument_list|)
expr_stmt|;
name|reader
operator|=
operator|new
name|InTransformReader
argument_list|(
name|reader
argument_list|,
name|inMap
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|XMLStreamReader
name|reader2
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|InTransformReader
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"../resources/complexReq1.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|TransformTestUtils
operator|.
name|verifyReaders
argument_list|(
name|reader2
argument_list|,
name|reader
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadWithComplexRequestMultipleNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|InTransformReader
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"../resources/complexReqIn2.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|inMap
operator|.
name|put
argument_list|(
literal|"{http://cxf.apache.org/transform/header/element}*"
argument_list|,
literal|"{http://cxf.apache.org/transform/header/otherelement}*"
argument_list|)
expr_stmt|;
name|inMap
operator|.
name|put
argument_list|(
literal|"{http://cxf.apache.org/transform/test}*"
argument_list|,
literal|"{http://cxf.apache.org/transform/othertest}*"
argument_list|)
expr_stmt|;
name|reader
operator|=
operator|new
name|InTransformReader
argument_list|(
name|reader
argument_list|,
name|inMap
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|XMLStreamReader
name|reader2
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|InTransformReader
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"../resources/complexReq2.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|TransformTestUtils
operator|.
name|verifyReaders
argument_list|(
name|reader2
argument_list|,
name|reader
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadWithComplexTransformationNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|InTransformReader
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"../resources/complexReqIn3.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|inMap
operator|.
name|put
argument_list|(
literal|"{http://cxf.apache.org/transform/header/element}*"
argument_list|,
literal|"{http://cxf.apache.org/transform/header/otherelement}*"
argument_list|)
expr_stmt|;
name|inMap
operator|.
name|put
argument_list|(
literal|"{http://cxf.apache.org/transform/test}*"
argument_list|,
literal|"{http://cxf.apache.org/transform/othertest}*"
argument_list|)
expr_stmt|;
name|inMap
operator|.
name|put
argument_list|(
literal|"{http://schemas.xmlsoap.org/soap/envelope/}Envelope"
argument_list|,
literal|"{http://schemas.xmlsoap.org/soap/envelope/}TheEnvelope"
argument_list|)
expr_stmt|;
comment|// set the block original reader flag to true
name|reader
operator|=
operator|new
name|InTransformReader
argument_list|(
name|reader
argument_list|,
name|inMap
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|XMLStreamReader
name|reader2
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|InTransformReader
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"../resources/complexReq3.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|TransformTestUtils
operator|.
name|verifyReaders
argument_list|(
name|reader2
argument_list|,
name|reader
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadPartialWithComplexRequestSameNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|InTransformReader
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"../resources/complexReqIn1.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|inMap
operator|.
name|put
argument_list|(
literal|"{http://cxf.apache.org/transform/header/element}*"
argument_list|,
literal|"{http://cxf.apache.org/transform/header/element}*"
argument_list|)
expr_stmt|;
name|reader
operator|=
operator|new
name|InTransformReader
argument_list|(
name|reader
argument_list|,
name|inMap
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|QName
name|bodyTag
init|=
operator|new
name|QName
argument_list|(
literal|"http://schemas.xmlsoap.org/soap/envelope/"
argument_list|,
literal|"Body"
argument_list|)
decl_stmt|;
name|PartialXMLStreamReader
name|filteredReader
init|=
operator|new
name|PartialXMLStreamReader
argument_list|(
name|reader
argument_list|,
name|bodyTag
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|reader2
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|InTransformReader
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"../resources/complexReq1partial.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|TransformTestUtils
operator|.
name|verifyReaders
argument_list|(
name|reader2
argument_list|,
name|filteredReader
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPartialReadWithComplexRequestMultipleNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|InTransformReader
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"../resources/complexReqIn2.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|inMap
operator|.
name|put
argument_list|(
literal|"{http://cxf.apache.org/transform/header/element}*"
argument_list|,
literal|"{http://cxf.apache.org/transform/header/otherelement}*"
argument_list|)
expr_stmt|;
name|inMap
operator|.
name|put
argument_list|(
literal|"{http://cxf.apache.org/transform/test}*"
argument_list|,
literal|"{http://cxf.apache.org/transform/othertest}*"
argument_list|)
expr_stmt|;
name|reader
operator|=
operator|new
name|InTransformReader
argument_list|(
name|reader
argument_list|,
name|inMap
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|QName
name|bodyTag
init|=
operator|new
name|QName
argument_list|(
literal|"http://schemas.xmlsoap.org/soap/envelope/"
argument_list|,
literal|"Body"
argument_list|)
decl_stmt|;
name|PartialXMLStreamReader
name|filteredReader
init|=
operator|new
name|PartialXMLStreamReader
argument_list|(
name|reader
argument_list|,
name|bodyTag
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|reader2
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|InTransformReader
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"../resources/complexReq2partial.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|TransformTestUtils
operator|.
name|verifyReaders
argument_list|(
name|reader2
argument_list|,
name|filteredReader
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPartialReadWithComplexTransformationNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|InTransformReader
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"../resources/complexReqIn3.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|inMap
operator|.
name|put
argument_list|(
literal|"{http://cxf.apache.org/transform/header/element}*"
argument_list|,
literal|"{http://cxf.apache.org/transform/header/otherelement}*"
argument_list|)
expr_stmt|;
name|inMap
operator|.
name|put
argument_list|(
literal|"{http://cxf.apache.org/transform/test}*"
argument_list|,
literal|"{http://cxf.apache.org/transform/othertest}*"
argument_list|)
expr_stmt|;
name|inMap
operator|.
name|put
argument_list|(
literal|"{http://schemas.xmlsoap.org/soap/envelope/}Envelope"
argument_list|,
literal|"{http://schemas.xmlsoap.org/soap/envelope/}TheEnvelope"
argument_list|)
expr_stmt|;
comment|// set the block original reader flag to true
name|reader
operator|=
operator|new
name|InTransformReader
argument_list|(
name|reader
argument_list|,
name|inMap
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|QName
name|bodyTag
init|=
operator|new
name|QName
argument_list|(
literal|"http://schemas.xmlsoap.org/soap/envelope/"
argument_list|,
literal|"Body"
argument_list|)
decl_stmt|;
name|PartialXMLStreamReader
name|filteredReader
init|=
operator|new
name|PartialXMLStreamReader
argument_list|(
name|reader
argument_list|,
name|bodyTag
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|reader2
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|InTransformReader
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"../resources/complexReq3partial.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|TransformTestUtils
operator|.
name|verifyReaders
argument_list|(
name|reader2
argument_list|,
name|filteredReader
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadWithReplaceAppend
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|transformElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|transformElements
operator|.
name|put
argument_list|(
literal|"requestValue"
argument_list|,
literal|"{http://cxf.apache.org/hello_world_soap_http/types}requestType"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|appendElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|appendElements
operator|.
name|put
argument_list|(
literal|"requestValue"
argument_list|,
literal|"{http://cxf.apache.org/hello_world_soap_http/types}greetMe"
argument_list|)
expr_stmt|;
name|TransformTestUtils
operator|.
name|transformInStreamAndCompare
argument_list|(
literal|"../resources/greetMeReqIn1.xml"
argument_list|,
literal|"../resources/greetMeReq.xml"
argument_list|,
name|transformElements
argument_list|,
name|appendElements
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadWithReplaceAppendDelete
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|transformElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|transformElements
operator|.
name|put
argument_list|(
literal|"requestValue"
argument_list|,
literal|"{http://cxf.apache.org/hello_world_soap_http/types}requestType"
argument_list|)
expr_stmt|;
name|transformElements
operator|.
name|put
argument_list|(
literal|"{http://cxf.apache.org/hello_world_soap_http/types}requestDate"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|appendElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|appendElements
operator|.
name|put
argument_list|(
literal|"requestValue"
argument_list|,
literal|"{http://cxf.apache.org/hello_world_soap_http/types}greetMe"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|dropElements
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|dropElements
operator|.
name|add
argument_list|(
literal|"value"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|transformAttributes
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|transformAttributes
operator|.
name|put
argument_list|(
literal|"num"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|transformAttributes
operator|.
name|put
argument_list|(
literal|"nombre"
argument_list|,
literal|"{http://cxf.apache.org/hello_world_soap_http/types}name"
argument_list|)
expr_stmt|;
name|TransformTestUtils
operator|.
name|transformInStreamAndCompare
argument_list|(
literal|"../resources/greetMeReqIn2.xml"
argument_list|,
literal|"../resources/greetMeReq.xml"
argument_list|,
name|transformElements
argument_list|,
name|appendElements
argument_list|,
name|dropElements
argument_list|,
name|transformAttributes
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadWithChangeNamespaces
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|transformElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|transformElements
operator|.
name|put
argument_list|(
literal|"*"
argument_list|,
literal|"{http://cxf.apache.org/hello_world_soap_http/types}*"
argument_list|)
expr_stmt|;
name|TransformTestUtils
operator|.
name|transformInStreamAndCompare
argument_list|(
literal|"../resources/greetMeReqIn3.xml"
argument_list|,
literal|"../resources/greetMeReq.xml"
argument_list|,
name|transformElements
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadWithDeleteAttributes
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|transformAttributes
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|transformAttributes
operator|.
name|put
argument_list|(
literal|"{http://www.w3.org/2001/XMLSchema-instance}type"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|TransformTestUtils
operator|.
name|transformInStreamAndCompare
argument_list|(
literal|"../resources/greetMeReqIn4.xml"
argument_list|,
literal|"../resources/greetMeReq.xml"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|transformAttributes
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadWithAppendPreInclude1
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|appendElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|appendElements
operator|.
name|put
argument_list|(
literal|"{http://xml.amazon.com/AWSECommerceService/2004-08-01}ItemId"
argument_list|,
literal|"{http://xml.amazon.com/AWSECommerceService/2004-08-01}IdType=ASIN"
argument_list|)
expr_stmt|;
name|TransformTestUtils
operator|.
name|transformInStreamAndCompare
argument_list|(
literal|"../resources/amazonIn1.xml"
argument_list|,
literal|"../resources/amazon.xml"
argument_list|,
literal|null
argument_list|,
name|appendElements
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadWithAppendPreInclude2
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|appendElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|appendElements
operator|.
name|put
argument_list|(
literal|"{http://xml.amazon.com/AWSECommerceService/2004-08-01}ItemId"
argument_list|,
literal|"{http://xml.amazon.com/AWSECommerceService/2004-08-01}IdType=ASIN"
argument_list|)
expr_stmt|;
name|TransformTestUtils
operator|.
name|transformInStreamAndCompare
argument_list|(
literal|"../resources/amazonIn1nospace.xml"
argument_list|,
literal|"../resources/amazon.xml"
argument_list|,
literal|null
argument_list|,
name|appendElements
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadWithAppendPreWrap1
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|transformElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|transformElements
operator|.
name|put
argument_list|(
literal|"payload"
argument_list|,
literal|"{http://www.w3.org/2003/05/soap-envelope}Envelope"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|appendElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|appendElements
operator|.
name|put
argument_list|(
literal|"{http://apache.org/cxf/calculator/types}add"
argument_list|,
literal|"{http://www.w3.org/2003/05/soap-envelope}Body"
argument_list|)
expr_stmt|;
name|TransformTestUtils
operator|.
name|transformInStreamAndCompare
argument_list|(
literal|"../resources/AddRequestIn2.xml"
argument_list|,
literal|"../resources/AddRequest2.xml"
argument_list|,
name|transformElements
argument_list|,
name|appendElements
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadWithAppendPreWrap2
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|transformElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|transformElements
operator|.
name|put
argument_list|(
literal|"payload"
argument_list|,
literal|"{http://www.w3.org/2003/05/soap-envelope}Envelope"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|appendElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|appendElements
operator|.
name|put
argument_list|(
literal|"{http://apache.org/cxf/calculator/types}add"
argument_list|,
literal|"{http://www.w3.org/2003/05/soap-envelope}Body"
argument_list|)
expr_stmt|;
name|TransformTestUtils
operator|.
name|transformInStreamAndCompare
argument_list|(
literal|"../resources/AddRequestIn2nospace.xml"
argument_list|,
literal|"../resources/AddRequest2.xml"
argument_list|,
name|transformElements
argument_list|,
name|appendElements
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadWithAppendPostInclude1
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|appendElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|appendElements
operator|.
name|put
argument_list|(
literal|"{http://xml.amazon.com/AWSECommerceService/2004-08-01}Request/"
argument_list|,
literal|"{http://xml.amazon.com/AWSECommerceService/2004-08-01}ItemId=0486411214"
argument_list|)
expr_stmt|;
name|TransformTestUtils
operator|.
name|transformInStreamAndCompare
argument_list|(
literal|"../resources/amazonIn2.xml"
argument_list|,
literal|"../resources/amazon.xml"
argument_list|,
literal|null
argument_list|,
name|appendElements
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadWithAppendPostInclude2
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|appendElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|appendElements
operator|.
name|put
argument_list|(
literal|"{http://xml.amazon.com/AWSECommerceService/2004-08-01}Request/"
argument_list|,
literal|"{http://xml.amazon.com/AWSECommerceService/2004-08-01}ItemId=0486411214"
argument_list|)
expr_stmt|;
name|TransformTestUtils
operator|.
name|transformInStreamAndCompare
argument_list|(
literal|"../resources/amazonIn2nospace.xml"
argument_list|,
literal|"../resources/amazon.xml"
argument_list|,
literal|null
argument_list|,
name|appendElements
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadWithAppendPostWrap1
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|appendElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|appendElements
operator|.
name|put
argument_list|(
literal|"{http://www.w3.org/2003/05/soap-envelope}Body/"
argument_list|,
literal|"{http://apache.org/cxf/calculator/types}add"
argument_list|)
expr_stmt|;
name|TransformTestUtils
operator|.
name|transformInStreamAndCompare
argument_list|(
literal|"../resources/AddRequestIn1.xml"
argument_list|,
literal|"../resources/AddRequest.xml"
argument_list|,
literal|null
argument_list|,
name|appendElements
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadWithAppendPostWrap2
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|appendElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|appendElements
operator|.
name|put
argument_list|(
literal|"{http://www.w3.org/2003/05/soap-envelope}Body/"
argument_list|,
literal|"{http://apache.org/cxf/calculator/types}add"
argument_list|)
expr_stmt|;
name|TransformTestUtils
operator|.
name|transformInStreamAndCompare
argument_list|(
literal|"../resources/AddRequestIn1nospace.xml"
argument_list|,
literal|"../resources/AddRequest.xml"
argument_list|,
literal|null
argument_list|,
name|appendElements
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadWithAppendPostWrapReplaceDrop
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|transformElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|transformElements
operator|.
name|put
argument_list|(
literal|"payload"
argument_list|,
literal|"{http://www.w3.org/2003/05/soap-envelope}Envelope"
argument_list|)
expr_stmt|;
name|transformElements
operator|.
name|put
argument_list|(
literal|"params"
argument_list|,
literal|"{http://apache.org/cxf/calculator/types}add"
argument_list|)
expr_stmt|;
name|transformElements
operator|.
name|put
argument_list|(
literal|"i1"
argument_list|,
literal|"{http://apache.org/cxf/calculator/types}arg0"
argument_list|)
expr_stmt|;
name|transformElements
operator|.
name|put
argument_list|(
literal|"i2"
argument_list|,
literal|"{http://apache.org/cxf/calculator/types}arg1"
argument_list|)
expr_stmt|;
name|transformElements
operator|.
name|put
argument_list|(
literal|"i3"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|appendElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|appendElements
operator|.
name|put
argument_list|(
literal|"payload/"
argument_list|,
literal|"{http://www.w3.org/2003/05/soap-envelope}Body"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|dropElements
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|dropElements
operator|.
name|add
argument_list|(
literal|"param"
argument_list|)
expr_stmt|;
name|TransformTestUtils
operator|.
name|transformInStreamAndCompare
argument_list|(
literal|"../resources/AddRequestIn3.xml"
argument_list|,
literal|"../resources/AddRequest3.xml"
argument_list|,
name|transformElements
argument_list|,
name|appendElements
argument_list|,
name|dropElements
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOldSTSTransform
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|transformElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|transformElements
operator|.
name|put
argument_list|(
literal|"{http://docs.oasis-open.org/ws-sx/ws-trust/200512}*"
argument_list|,
literal|"{http://schemas.xmlsoap.org/ws/2005/02/trust}*"
argument_list|)
expr_stmt|;
name|TransformTestUtils
operator|.
name|transformInStreamAndCompare
argument_list|(
literal|"../resources/wstrustReqSTRCIn1.xml"
argument_list|,
literal|"../resources/wstrustReqSTRC.xml"
argument_list|,
name|transformElements
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPreservePrefixBindings
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|transformElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|transformElements
operator|.
name|put
argument_list|(
literal|"{urn:abc}*"
argument_list|,
literal|"{urn:a}*"
argument_list|)
expr_stmt|;
name|TransformTestUtils
operator|.
name|transformInStreamAndCompare
argument_list|(
literal|"../resources/multiNSIn1.xml"
argument_list|,
literal|"../resources/multiNS.xml"
argument_list|,
name|transformElements
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadNamespaceWithDuplicatePrefixes
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|transformElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|transformElements
operator|.
name|put
argument_list|(
literal|"{http://bar.com/foo}*"
argument_list|,
literal|"{http://bar.com/foobar}*"
argument_list|)
expr_stmt|;
name|TransformTestUtils
operator|.
name|transformInStreamAndCompare
argument_list|(
literal|"../resources/multiNS2In1.xml"
argument_list|,
literal|"../resources/multiNS2.xml"
argument_list|,
name|transformElements
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

