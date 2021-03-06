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
name|ValidationEventHandler
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
name|databinding
operator|.
name|DataReader
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
name|StaxStreamFilter
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
name|assertFalse
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
name|assertNotNull
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|XMLStreamDataReaderTest
block|{
specifier|private
name|XMLInputFactory
name|factory
decl_stmt|;
specifier|private
name|XMLStreamReader
name|reader
decl_stmt|;
specifier|private
name|InputStream
name|is
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
name|factory
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
name|IOException
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|MyCustomHandler
name|handler
init|=
operator|new
name|MyCustomHandler
argument_list|()
decl_stmt|;
name|DataReaderImpl
argument_list|<
name|XMLStreamReader
argument_list|>
name|dr
init|=
name|newDataReader
argument_list|(
name|handler
argument_list|)
decl_stmt|;
comment|// Should fail if custom handler doesn't skip formatting error
name|Object
name|val
init|=
name|dr
operator|.
name|read
argument_list|(
name|reader
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|val
operator|instanceof
name|GreetMe
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TestSOAPInputPMessage"
argument_list|,
operator|(
operator|(
name|GreetMe
operator|)
name|val
operator|)
operator|.
name|getRequestType
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|handler
operator|.
name|getUsed
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetPropertyWithCustomExceptionHandling
parameter_list|()
throws|throws
name|Exception
block|{
name|MyCustomMarshallerHandler
name|handler
init|=
operator|new
name|MyCustomMarshallerHandler
argument_list|()
decl_stmt|;
name|DataReaderImpl
argument_list|<
name|XMLStreamReader
argument_list|>
name|dr
init|=
name|newDataReader
argument_list|(
name|handler
argument_list|)
decl_stmt|;
comment|// Should fail if custom handler doesn't skip formatting error
try|try
block|{
name|dr
operator|.
name|read
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Fault
name|f
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|f
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"My unmarshalling exception"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Check handler used
name|assertTrue
argument_list|(
name|handler
operator|.
name|getUsed
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|handler
operator|.
name|isOnMarshalComplete
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|handler
operator|.
name|isOnUnmarshalComplete
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|DataReaderImpl
argument_list|<
name|XMLStreamReader
argument_list|>
name|newDataReader
parameter_list|(
name|ValidationEventHandler
name|handler
parameter_list|)
throws|throws
name|Exception
block|{
name|JAXBDataBinding
name|db
init|=
name|getDataBinding
argument_list|(
name|GreetMe
operator|.
name|class
argument_list|)
decl_stmt|;
name|reader
operator|=
name|getTestReader
argument_list|(
literal|"../resources/SetPropertyValidationFailureReq.xml"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|DataReaderImpl
argument_list|<
name|XMLStreamReader
argument_list|>
name|dr
init|=
operator|(
name|DataReaderImpl
argument_list|<
name|XMLStreamReader
argument_list|>
operator|)
name|db
operator|.
name|createReader
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dr
argument_list|)
expr_stmt|;
comment|// Build message to set custom event handler
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|message
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|MessageImpl
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|JAXBDataBinding
operator|.
name|READER_VALIDATION_EVENT_HANDLER
argument_list|,
name|handler
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"unwrap.jaxb.element"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|dr
operator|.
name|setProperty
argument_list|(
literal|"org.apache.cxf.message.Message"
argument_list|,
name|message
argument_list|)
expr_stmt|;
return|return
name|dr
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadWrapper
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBDataBinding
name|db
init|=
name|getDataBinding
argument_list|(
name|GreetMe
operator|.
name|class
argument_list|)
decl_stmt|;
name|reader
operator|=
name|getTestReader
argument_list|(
literal|"../resources/GreetMeDocLiteralReq.xml"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|DataReader
argument_list|<
name|XMLStreamReader
argument_list|>
name|dr
init|=
name|db
operator|.
name|createReader
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dr
argument_list|)
expr_stmt|;
name|Object
name|val
init|=
name|dr
operator|.
name|read
argument_list|(
name|reader
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|val
operator|instanceof
name|GreetMe
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TestSOAPInputPMessage"
argument_list|,
operator|(
operator|(
name|GreetMe
operator|)
name|val
operator|)
operator|.
name|getRequestType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadWrapperReturn
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBDataBinding
name|db
init|=
name|getDataBinding
argument_list|(
name|GreetMeResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|reader
operator|=
name|getTestReader
argument_list|(
literal|"../resources/GreetMeDocLiteralResp.xml"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|DataReader
argument_list|<
name|XMLStreamReader
argument_list|>
name|dr
init|=
name|db
operator|.
name|createReader
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dr
argument_list|)
expr_stmt|;
name|Object
name|retValue
init|=
name|dr
operator|.
name|read
argument_list|(
name|reader
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|retValue
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|retValue
operator|instanceof
name|GreetMeResponse
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TestSOAPOutputPMessage"
argument_list|,
operator|(
operator|(
name|GreetMeResponse
operator|)
name|retValue
operator|)
operator|.
name|getResponseType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadRPC
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBDataBinding
name|db
init|=
name|getDataBinding
argument_list|(
name|MyComplexStruct
operator|.
name|class
argument_list|)
decl_stmt|;
name|QName
index|[]
name|tags
init|=
block|{
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"sendReceiveData"
argument_list|)
block|}
decl_stmt|;
name|reader
operator|=
name|getTestReader
argument_list|(
literal|"../resources/greetMeRpcLitReq.xml"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|XMLStreamReader
name|localReader
init|=
name|getTestFilteredReader
argument_list|(
name|reader
argument_list|,
name|tags
argument_list|)
decl_stmt|;
name|DataReader
argument_list|<
name|XMLStreamReader
argument_list|>
name|dr
init|=
name|db
operator|.
name|createReader
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dr
argument_list|)
expr_stmt|;
name|Object
name|val
init|=
name|dr
operator|.
name|read
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"in"
argument_list|)
argument_list|,
name|localReader
argument_list|,
name|MyComplexStruct
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|val
operator|instanceof
name|MyComplexStruct
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"this is element 1"
argument_list|,
operator|(
operator|(
name|MyComplexStruct
operator|)
name|val
operator|)
operator|.
name|getElem1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"this is element 2"
argument_list|,
operator|(
operator|(
name|MyComplexStruct
operator|)
name|val
operator|)
operator|.
name|getElem2
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|42
argument_list|,
operator|(
operator|(
name|MyComplexStruct
operator|)
name|val
operator|)
operator|.
name|getElem3
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadBare
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBDataBinding
name|db
init|=
name|getDataBinding
argument_list|(
name|TradePriceData
operator|.
name|class
argument_list|)
decl_stmt|;
name|reader
operator|=
name|getTestReader
argument_list|(
literal|"../resources/sayHiDocLitBareReq.xml"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|DataReader
argument_list|<
name|XMLStreamReader
argument_list|>
name|dr
init|=
name|db
operator|.
name|createReader
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dr
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
name|part
operator|.
name|setTypeClass
argument_list|(
name|TradePriceData
operator|.
name|class
argument_list|)
expr_stmt|;
name|Object
name|val
init|=
name|dr
operator|.
name|read
argument_list|(
name|part
argument_list|,
name|reader
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|val
operator|instanceof
name|TradePriceData
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF"
argument_list|,
operator|(
operator|(
name|TradePriceData
operator|)
name|val
operator|)
operator|.
name|getTickerSymbol
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Float
operator|.
name|valueOf
argument_list|(
literal|1.0f
argument_list|)
argument_list|,
operator|new
name|Float
argument_list|(
operator|(
operator|(
name|TradePriceData
operator|)
name|val
operator|)
operator|.
name|getTickerPrice
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|JAXBDataBinding
name|getDataBinding
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
specifier|private
name|XMLStreamReader
name|getTestFilteredReader
parameter_list|(
name|XMLStreamReader
name|r
parameter_list|,
name|QName
index|[]
name|q
parameter_list|)
throws|throws
name|Exception
block|{
name|StaxStreamFilter
name|filter
init|=
operator|new
name|StaxStreamFilter
argument_list|(
name|q
argument_list|)
decl_stmt|;
return|return
name|factory
operator|.
name|createFilteredReader
argument_list|(
name|r
argument_list|,
name|filter
argument_list|)
return|;
block|}
specifier|private
name|XMLStreamReader
name|getTestReader
parameter_list|(
name|String
name|resource
parameter_list|)
throws|throws
name|Exception
block|{
name|is
operator|=
name|getTestStream
argument_list|(
name|resource
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
return|return
name|factory
operator|.
name|createXMLStreamReader
argument_list|(
name|is
argument_list|)
return|;
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
block|}
end_class

end_unit

