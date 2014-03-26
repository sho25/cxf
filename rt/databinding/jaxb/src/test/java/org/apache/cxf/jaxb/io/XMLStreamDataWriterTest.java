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
name|jaxb
operator|.
name|io
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
name|OutputStream
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
name|Map
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
name|XMLInputFactory
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
name|XMLOutputFactory
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
name|apache
operator|.
name|cxf
operator|.
name|databinding
operator|.
name|DataWriter
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
name|jaxb
operator|.
name|JAXBDataBinding
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
name|service
operator|.
name|model
operator|.
name|MessagePartInfo
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
name|hello_world_doc_lit_bare
operator|.
name|types
operator|.
name|TradePriceData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_rpclit
operator|.
name|types
operator|.
name|MyComplexStruct
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|types
operator|.
name|GreetMe
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|types
operator|.
name|GreetMeResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|XMLStreamDataWriterTest
extends|extends
name|Assert
block|{
specifier|private
name|ByteArrayOutputStream
name|baos
decl_stmt|;
specifier|private
name|XMLStreamWriter
name|streamWriter
decl_stmt|;
specifier|private
name|XMLInputFactory
name|inFactory
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
name|baos
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
name|XMLOutputFactory
name|factory
init|=
name|XMLOutputFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|streamWriter
operator|=
name|factory
operator|.
name|createXMLStreamWriter
argument_list|(
name|baos
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|streamWriter
argument_list|)
expr_stmt|;
name|inFactory
operator|=
name|XMLInputFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|baos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteRPCLit1
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBDataBinding
name|db
init|=
name|getTestWriterFactory
argument_list|()
decl_stmt|;
name|DataWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
name|dw
init|=
name|db
operator|.
name|createWriter
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dw
argument_list|)
expr_stmt|;
name|String
name|val
init|=
operator|new
name|String
argument_list|(
literal|"TESTOUTPUTMESSAGE"
argument_list|)
decl_stmt|;
name|QName
name|elName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit/types"
argument_list|,
literal|"in"
argument_list|)
decl_stmt|;
name|MessagePartInfo
name|part
init|=
operator|new
name|MessagePartInfo
argument_list|(
name|elName
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|part
operator|.
name|setElement
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|part
operator|.
name|setElementQName
argument_list|(
name|elName
argument_list|)
expr_stmt|;
name|dw
operator|.
name|write
argument_list|(
name|val
argument_list|,
name|part
argument_list|,
name|streamWriter
argument_list|)
expr_stmt|;
name|streamWriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|xr
init|=
name|inFactory
operator|.
name|createXMLStreamReader
argument_list|(
name|bais
argument_list|)
decl_stmt|;
name|DepthXMLStreamReader
name|reader
init|=
operator|new
name|DepthXMLStreamReader
argument_list|(
name|xr
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit/types"
argument_list|,
literal|"in"
argument_list|)
argument_list|,
name|reader
operator|.
name|getName
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
name|StaxUtils
operator|.
name|toNextText
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TESTOUTPUTMESSAGE"
argument_list|,
name|reader
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteRPCLit2
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBDataBinding
name|db
init|=
name|getTestWriterFactory
argument_list|(
name|MyComplexStruct
operator|.
name|class
argument_list|)
decl_stmt|;
name|DataWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
name|dw
init|=
name|db
operator|.
name|createWriter
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dw
argument_list|)
expr_stmt|;
name|MyComplexStruct
name|val
init|=
operator|new
name|MyComplexStruct
argument_list|()
decl_stmt|;
name|val
operator|.
name|setElem1
argument_list|(
literal|"This is element 1"
argument_list|)
expr_stmt|;
name|val
operator|.
name|setElem2
argument_list|(
literal|"This is element 2"
argument_list|)
expr_stmt|;
name|val
operator|.
name|setElem3
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|QName
name|elName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit/types"
argument_list|,
literal|"in"
argument_list|)
decl_stmt|;
name|MessagePartInfo
name|part
init|=
operator|new
name|MessagePartInfo
argument_list|(
name|elName
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|part
operator|.
name|setElement
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|part
operator|.
name|setElementQName
argument_list|(
name|elName
argument_list|)
expr_stmt|;
name|dw
operator|.
name|write
argument_list|(
name|val
argument_list|,
name|part
argument_list|,
name|streamWriter
argument_list|)
expr_stmt|;
name|streamWriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|xr
init|=
name|inFactory
operator|.
name|createXMLStreamReader
argument_list|(
name|bais
argument_list|)
decl_stmt|;
name|DepthXMLStreamReader
name|reader
init|=
operator|new
name|DepthXMLStreamReader
argument_list|(
name|xr
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit/types"
argument_list|,
literal|"in"
argument_list|)
argument_list|,
name|reader
operator|.
name|getName
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
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit/types"
argument_list|,
literal|"elem1"
argument_list|)
argument_list|,
name|reader
operator|.
name|getName
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
name|StaxUtils
operator|.
name|toNextText
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"This is element 1"
argument_list|,
name|reader
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteBare
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBDataBinding
name|db
init|=
name|getTestWriterFactory
argument_list|(
name|TradePriceData
operator|.
name|class
argument_list|)
decl_stmt|;
name|DataWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
name|dw
init|=
name|db
operator|.
name|createWriter
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dw
argument_list|)
expr_stmt|;
name|TradePriceData
name|val
init|=
operator|new
name|TradePriceData
argument_list|()
decl_stmt|;
name|val
operator|.
name|setTickerSymbol
argument_list|(
literal|"This is a symbol"
argument_list|)
expr_stmt|;
name|val
operator|.
name|setTickerPrice
argument_list|(
literal|1.0f
argument_list|)
expr_stmt|;
name|QName
name|elName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_doc_lit_bare/types"
argument_list|,
literal|"inout"
argument_list|)
decl_stmt|;
name|MessagePartInfo
name|part
init|=
operator|new
name|MessagePartInfo
argument_list|(
name|elName
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|part
operator|.
name|setElement
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|part
operator|.
name|setElementQName
argument_list|(
name|elName
argument_list|)
expr_stmt|;
name|dw
operator|.
name|write
argument_list|(
name|val
argument_list|,
name|part
argument_list|,
name|streamWriter
argument_list|)
expr_stmt|;
name|streamWriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|xr
init|=
name|inFactory
operator|.
name|createXMLStreamReader
argument_list|(
name|bais
argument_list|)
decl_stmt|;
name|DepthXMLStreamReader
name|reader
init|=
operator|new
name|DepthXMLStreamReader
argument_list|(
name|xr
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_doc_lit_bare/types"
argument_list|,
literal|"inout"
argument_list|)
argument_list|,
name|reader
operator|.
name|getName
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
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_doc_lit_bare/types"
argument_list|,
literal|"tickerSymbol"
argument_list|)
argument_list|,
name|reader
operator|.
name|getName
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
name|StaxUtils
operator|.
name|toNextText
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"This is a symbol"
argument_list|,
name|reader
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteWrapper
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBDataBinding
name|db
init|=
name|getTestWriterFactory
argument_list|(
name|GreetMe
operator|.
name|class
argument_list|)
decl_stmt|;
name|DataWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
name|dw
init|=
name|db
operator|.
name|createWriter
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dw
argument_list|)
expr_stmt|;
name|GreetMe
name|val
init|=
operator|new
name|GreetMe
argument_list|()
decl_stmt|;
name|val
operator|.
name|setRequestType
argument_list|(
literal|"Hello"
argument_list|)
expr_stmt|;
name|dw
operator|.
name|write
argument_list|(
name|val
argument_list|,
name|streamWriter
argument_list|)
expr_stmt|;
name|streamWriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|xr
init|=
name|inFactory
operator|.
name|createXMLStreamReader
argument_list|(
name|bais
argument_list|)
decl_stmt|;
name|DepthXMLStreamReader
name|reader
init|=
operator|new
name|DepthXMLStreamReader
argument_list|(
name|xr
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http/types"
argument_list|,
literal|"greetMe"
argument_list|)
argument_list|,
name|reader
operator|.
name|getName
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
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http/types"
argument_list|,
literal|"requestType"
argument_list|)
argument_list|,
name|reader
operator|.
name|getName
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
name|StaxUtils
operator|.
name|toNextText
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|reader
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteWrapperReturn
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBDataBinding
name|db
init|=
name|getTestWriterFactory
argument_list|(
name|GreetMeResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|DataWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
name|dw
init|=
name|db
operator|.
name|createWriter
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dw
argument_list|)
expr_stmt|;
name|GreetMeResponse
name|retVal
init|=
operator|new
name|GreetMeResponse
argument_list|()
decl_stmt|;
name|retVal
operator|.
name|setResponseType
argument_list|(
literal|"TESTOUTPUTMESSAGE"
argument_list|)
expr_stmt|;
name|dw
operator|.
name|write
argument_list|(
name|retVal
argument_list|,
name|streamWriter
argument_list|)
expr_stmt|;
name|streamWriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|xr
init|=
name|inFactory
operator|.
name|createXMLStreamReader
argument_list|(
name|bais
argument_list|)
decl_stmt|;
name|DepthXMLStreamReader
name|reader
init|=
operator|new
name|DepthXMLStreamReader
argument_list|(
name|xr
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http/types"
argument_list|,
literal|"greetMeResponse"
argument_list|)
argument_list|,
name|reader
operator|.
name|getName
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
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http/types"
argument_list|,
literal|"responseType"
argument_list|)
argument_list|,
name|reader
operator|.
name|getName
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
name|StaxUtils
operator|.
name|toNextText
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TESTOUTPUTMESSAGE"
argument_list|,
name|reader
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteWithNamespacePrefixMapping
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBDataBinding
name|db
init|=
name|getTestWriterFactory
argument_list|(
name|GreetMe
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nspref
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|nspref
operator|.
name|put
argument_list|(
literal|"http://apache.org/hello_world_soap_http/types"
argument_list|,
literal|"x"
argument_list|)
expr_stmt|;
name|db
operator|.
name|setNamespaceMap
argument_list|(
name|nspref
argument_list|)
expr_stmt|;
comment|// use the output stream instead of XMLStreamWriter to test
name|DataWriter
argument_list|<
name|OutputStream
argument_list|>
name|dw
init|=
name|db
operator|.
name|createWriter
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dw
argument_list|)
expr_stmt|;
name|GreetMe
name|val
init|=
operator|new
name|GreetMe
argument_list|()
decl_stmt|;
name|val
operator|.
name|setRequestType
argument_list|(
literal|"Hello"
argument_list|)
expr_stmt|;
name|dw
operator|.
name|write
argument_list|(
name|val
argument_list|,
name|baos
argument_list|)
expr_stmt|;
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|xr
init|=
name|inFactory
operator|.
name|createXMLStreamReader
argument_list|(
name|bais
argument_list|)
decl_stmt|;
name|DepthXMLStreamReader
name|reader
init|=
operator|new
name|DepthXMLStreamReader
argument_list|(
name|xr
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|QName
name|qname
init|=
name|reader
operator|.
name|getName
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http/types"
argument_list|,
literal|"greetMe"
argument_list|)
argument_list|,
name|qname
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"x"
argument_list|,
name|qname
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|reader
operator|.
name|getNamespaceCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://apache.org/hello_world_soap_http/types"
argument_list|,
name|reader
operator|.
name|getNamespaceURI
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"x"
argument_list|,
name|reader
operator|.
name|getNamespacePrefix
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|StaxUtils
operator|.
name|nextEvent
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|qname
operator|=
name|reader
operator|.
name|getName
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http/types"
argument_list|,
literal|"requestType"
argument_list|)
argument_list|,
name|qname
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"x"
argument_list|,
name|qname
operator|.
name|getPrefix
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
name|StaxUtils
operator|.
name|toNextText
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|reader
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteWithContextualNamespaceDecls
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBDataBinding
name|db
init|=
name|getTestWriterFactory
argument_list|(
name|GreetMe
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nspref
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|nspref
operator|.
name|put
argument_list|(
literal|"http://apache.org/hello_world_soap_http/types"
argument_list|,
literal|"x"
argument_list|)
expr_stmt|;
name|db
operator|.
name|setNamespaceMap
argument_list|(
name|nspref
argument_list|)
expr_stmt|;
name|db
operator|.
name|setContextualNamespaceMap
argument_list|(
name|nspref
argument_list|)
expr_stmt|;
comment|// use the output stream instead of XMLStreamWriter to test
name|DataWriter
argument_list|<
name|OutputStream
argument_list|>
name|dw
init|=
name|db
operator|.
name|createWriter
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dw
argument_list|)
expr_stmt|;
name|GreetMe
name|val
init|=
operator|new
name|GreetMe
argument_list|()
decl_stmt|;
name|val
operator|.
name|setRequestType
argument_list|(
literal|"Hello"
argument_list|)
expr_stmt|;
name|dw
operator|.
name|write
argument_list|(
name|val
argument_list|,
name|baos
argument_list|)
expr_stmt|;
name|String
name|xstr
init|=
operator|new
name|String
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
comment|// there should be no namespace decls
if|if
condition|(
operator|!
name|db
operator|.
name|getContext
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"eclipse"
argument_list|)
condition|)
block|{
comment|//bug in eclipse moxy
comment|//https://bugs.eclipse.org/bugs/show_bug.cgi?id=421463
name|assertEquals
argument_list|(
literal|"<x:greetMe><x:requestType>Hello</x:requestType></x:greetMe>"
argument_list|,
name|xstr
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|JAXBDataBinding
name|getTestWriterFactory
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|clz
parameter_list|)
throws|throws
name|Exception
block|{
name|JAXBContext
name|ctx
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|clz
argument_list|)
decl_stmt|;
return|return
operator|new
name|JAXBDataBinding
argument_list|(
name|ctx
argument_list|)
return|;
block|}
block|}
end_class

end_unit

