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
name|xml
operator|.
name|interceptor
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Marshaller
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
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|xml
operator|.
name|XMLConstants
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
name|xml
operator|.
name|XMLFault
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
name|interceptor
operator|.
name|Fault
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
name|DepthXMLStreamReader
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
name|hello_world_doc_lit
operator|.
name|PingMeFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_doc_lit
operator|.
name|types
operator|.
name|FaultDetail
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
name|XMLFaultOutInterceptorTest
extends|extends
name|TestBase
block|{
name|XMLFaultOutInterceptor
name|out
init|=
operator|new
name|XMLFaultOutInterceptor
argument_list|()
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testFault
parameter_list|()
throws|throws
name|Exception
block|{
name|FaultDetail
name|detail
init|=
operator|new
name|FaultDetail
argument_list|()
decl_stmt|;
name|detail
operator|.
name|setMajor
argument_list|(
operator|(
name|short
operator|)
literal|2
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setMinor
argument_list|(
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
name|PingMeFault
name|fault
init|=
operator|new
name|PingMeFault
argument_list|(
literal|"TEST_FAULT"
argument_list|,
name|detail
argument_list|)
decl_stmt|;
name|XMLFault
name|xmlFault
init|=
name|XMLFault
operator|.
name|createFault
argument_list|(
operator|new
name|Fault
argument_list|(
name|fault
argument_list|)
argument_list|)
decl_stmt|;
name|Element
name|el
init|=
name|xmlFault
operator|.
name|getOrCreateDetail
argument_list|()
decl_stmt|;
name|JAXBContext
name|ctx
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|FaultDetail
operator|.
name|class
argument_list|)
decl_stmt|;
name|Marshaller
name|m
init|=
name|ctx
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
name|m
operator|.
name|marshal
argument_list|(
name|detail
argument_list|,
name|el
argument_list|)
expr_stmt|;
name|OutputStream
name|outputStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|xmlMessage
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|outputStream
argument_list|)
expr_stmt|;
name|XMLStreamWriter
name|writer
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|outputStream
argument_list|)
decl_stmt|;
name|xmlMessage
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|xmlMessage
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
name|xmlFault
argument_list|)
expr_stmt|;
name|out
operator|.
name|handleMessage
argument_list|(
name|xmlMessage
argument_list|)
expr_stmt|;
name|outputStream
operator|.
name|flush
argument_list|()
expr_stmt|;
name|XMLStreamReader
name|reader
init|=
name|getXMLReader
argument_list|()
decl_stmt|;
name|DepthXMLStreamReader
name|dxr
init|=
operator|new
name|DepthXMLStreamReader
argument_list|(
name|reader
argument_list|)
decl_stmt|;
name|dxr
operator|.
name|nextTag
argument_list|()
expr_stmt|;
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|dxr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|XMLConstants
operator|.
name|NS_XML_FORMAT
argument_list|,
name|dxr
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|XMLFault
operator|.
name|XML_FAULT_ROOT
argument_list|,
name|dxr
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|dxr
operator|.
name|nextTag
argument_list|()
expr_stmt|;
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|dxr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|XMLFault
operator|.
name|XML_FAULT_STRING
argument_list|,
name|dxr
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|fault
operator|.
name|toString
argument_list|()
argument_list|,
name|dxr
operator|.
name|getElementText
argument_list|()
argument_list|)
expr_stmt|;
name|dxr
operator|.
name|nextTag
argument_list|()
expr_stmt|;
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|dxr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|XMLFault
operator|.
name|XML_FAULT_DETAIL
argument_list|,
name|dxr
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|dxr
operator|.
name|nextTag
argument_list|()
expr_stmt|;
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|dxr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"faultDetail"
argument_list|,
name|dxr
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|XMLStreamReader
name|getXMLReader
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|o
init|=
operator|(
name|ByteArrayOutputStream
operator|)
name|xmlMessage
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|InputStream
name|in
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|o
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|in
argument_list|)
return|;
block|}
block|}
end_class

end_unit

