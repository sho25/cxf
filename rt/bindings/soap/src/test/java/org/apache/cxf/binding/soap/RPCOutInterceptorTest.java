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
name|util
operator|.
name|Arrays
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|RPCOutInterceptor
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
name|MessageContentsList
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
name|Service
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
name|BindingInfo
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
name|BindingOperationInfo
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
name|ServiceInfo
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
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IMocksControl
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
name|assertNull
import|;
end_import

begin_class
specifier|public
class|class
name|RPCOutInterceptorTest
extends|extends
name|TestBase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TNS
init|=
literal|"http://apache.org/hello_world_rpclit"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OPNAME
init|=
literal|"sendReceiveData"
decl_stmt|;
specifier|private
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|(
literal|64
operator|*
literal|1024
argument_list|)
decl_stmt|;
specifier|private
name|IMocksControl
name|control
init|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|ServiceInfo
name|si
init|=
name|getMockedServiceModel
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl_soap/hello_world_rpc_lit.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|BindingInfo
name|bi
init|=
name|si
operator|.
name|getBinding
argument_list|(
operator|new
name|QName
argument_list|(
name|TNS
argument_list|,
literal|"Greeter_SOAPBinding_RPCLit"
argument_list|)
argument_list|)
decl_stmt|;
name|BindingOperationInfo
name|boi
init|=
name|bi
operator|.
name|getOperation
argument_list|(
operator|new
name|QName
argument_list|(
name|TNS
argument_list|,
name|OPNAME
argument_list|)
argument_list|)
decl_stmt|;
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getOutput
argument_list|()
operator|.
name|getMessagePartByIndex
argument_list|(
literal|0
argument_list|)
operator|.
name|setIndex
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|boi
argument_list|)
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|Service
name|service
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Service
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|service
operator|.
name|isEmpty
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|JAXBDataBinding
name|dataBinding
init|=
operator|new
name|JAXBDataBinding
argument_list|(
name|MyComplexStruct
operator|.
name|class
argument_list|)
decl_stmt|;
name|service
operator|.
name|getDataBinding
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|dataBinding
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|service
operator|.
name|getServiceInfos
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|ServiceInfo
argument_list|>
name|list
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|si
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|list
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|soapMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|Service
operator|.
name|class
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|SCHEMA_VALIDATION_ENABLED
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|MyComplexStruct
name|mcs
init|=
operator|new
name|MyComplexStruct
argument_list|()
decl_stmt|;
name|mcs
operator|.
name|setElem1
argument_list|(
literal|"elem1"
argument_list|)
expr_stmt|;
name|mcs
operator|.
name|setElem2
argument_list|(
literal|"elem2"
argument_list|)
expr_stmt|;
name|mcs
operator|.
name|setElem3
argument_list|(
literal|45
argument_list|)
expr_stmt|;
name|MessageContentsList
name|param
init|=
operator|new
name|MessageContentsList
argument_list|()
decl_stmt|;
name|param
operator|.
name|add
argument_list|(
name|mcs
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|setContent
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|param
argument_list|)
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
name|testWriteOutbound
parameter_list|()
throws|throws
name|Exception
block|{
name|RPCOutInterceptor
name|interceptor
init|=
operator|new
name|RPCOutInterceptor
argument_list|()
decl_stmt|;
name|soapMessage
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|XMLOutputFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createXMLStreamWriter
argument_list|(
name|baos
argument_list|)
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|soapMessage
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|getContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
operator|.
name|flush
argument_list|()
expr_stmt|;
name|baos
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
name|StaxUtils
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
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"sendReceiveData"
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
literal|null
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
name|toNextText
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"elem1"
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
name|testWriteInbound
parameter_list|()
throws|throws
name|Exception
block|{
name|RPCOutInterceptor
name|interceptor
init|=
operator|new
name|RPCOutInterceptor
argument_list|()
decl_stmt|;
name|soapMessage
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|XMLOutputFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createXMLStreamWriter
argument_list|(
name|baos
argument_list|)
argument_list|)
expr_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|soapMessage
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|getContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
operator|.
name|flush
argument_list|()
expr_stmt|;
name|baos
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
name|StaxUtils
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
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"sendReceiveDataResponse"
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
literal|null
argument_list|,
literal|"out"
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
literal|"elem1"
argument_list|,
name|reader
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

