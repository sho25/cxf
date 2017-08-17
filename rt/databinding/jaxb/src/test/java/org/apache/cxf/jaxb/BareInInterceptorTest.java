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
package|;
end_package

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
name|List
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
name|Bus
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
name|BusFactory
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
name|Binding
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
name|BindingFactory
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
name|BindingFactoryManager
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
name|endpoint
operator|.
name|Endpoint
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
name|endpoint
operator|.
name|EndpointImpl
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
name|Interceptor
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
name|phase
operator|.
name|PhaseInterceptorChain
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
name|EndpointInfo
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
name|StaxUtils
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
name|wsdl
operator|.
name|interceptors
operator|.
name|BareInInterceptor
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
name|wsdl11
operator|.
name|WSDLServiceFactory
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

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|createNiceControl
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expect
import|;
end_import

begin_class
specifier|public
class|class
name|BareInInterceptorTest
extends|extends
name|Assert
block|{
name|PhaseInterceptorChain
name|chain
decl_stmt|;
name|MessageImpl
name|message
decl_stmt|;
name|Bus
name|bus
decl_stmt|;
name|ServiceInfo
name|serviceInfo
decl_stmt|;
name|BindingInfo
name|bindingInfo
decl_stmt|;
name|Service
name|service
decl_stmt|;
name|EndpointInfo
name|endpointInfo
decl_stmt|;
name|EndpointImpl
name|endpoint
decl_stmt|;
name|BindingOperationInfo
name|operation
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
name|bus
operator|=
name|BusFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createBus
argument_list|()
expr_stmt|;
name|BindingFactoryManager
name|bfm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|IMocksControl
name|control
init|=
name|createNiceControl
argument_list|()
decl_stmt|;
name|BindingFactory
name|bf
init|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|Binding
name|binding
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Binding
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|bf
operator|.
name|createBinding
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|andStubReturn
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|binding
operator|.
name|getInFaultInterceptors
argument_list|()
argument_list|)
operator|.
name|andStubReturn
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|binding
operator|.
name|getOutFaultInterceptors
argument_list|()
argument_list|)
operator|.
name|andStubReturn
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|bfm
operator|.
name|registerBindingFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
argument_list|,
name|bf
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInterceptorInbound
parameter_list|()
throws|throws
name|Exception
block|{
name|setUpUsingHelloWorld
argument_list|()
expr_stmt|;
name|BareInInterceptor
name|interceptor
init|=
operator|new
name|BareInInterceptor
argument_list|()
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|XMLInputFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createXMLStreamReader
argument_list|(
name|getTestStream
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"resources/GreetMeDocLiteralReq.xml"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|INBOUND_MESSAGE
argument_list|,
name|Message
operator|.
name|INBOUND_MESSAGE
argument_list|)
expr_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|message
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|parameters
init|=
name|message
operator|.
name|getContent
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parameters
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|obj
init|=
name|parameters
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|obj
operator|instanceof
name|GreetMe
argument_list|)
expr_stmt|;
name|GreetMe
name|greet
init|=
operator|(
name|GreetMe
operator|)
name|obj
decl_stmt|;
name|assertEquals
argument_list|(
literal|"TestSOAPInputPMessage"
argument_list|,
name|greet
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
name|testInterceptorInbound1
parameter_list|()
throws|throws
name|Exception
block|{
name|setUpUsingDocLit
argument_list|()
expr_stmt|;
name|BareInInterceptor
name|interceptor
init|=
operator|new
name|BareInInterceptor
argument_list|()
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|XMLInputFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createXMLStreamReader
argument_list|(
name|getTestStream
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"resources/sayHiDocLitBareReq.xml"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|INBOUND_MESSAGE
argument_list|,
name|Message
operator|.
name|INBOUND_MESSAGE
argument_list|)
expr_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|message
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|parameters
init|=
name|message
operator|.
name|getContent
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parameters
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|obj
init|=
name|parameters
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|obj
operator|instanceof
name|TradePriceData
argument_list|)
expr_stmt|;
name|TradePriceData
name|greet
init|=
operator|(
name|TradePriceData
operator|)
name|obj
decl_stmt|;
name|assertTrue
argument_list|(
literal|1.0
operator|==
name|greet
operator|.
name|getTickerPrice
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF"
argument_list|,
name|greet
operator|.
name|getTickerSymbol
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInterceptorInboundBareNoParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|setUpUsingDocLit
argument_list|()
expr_stmt|;
name|BareInInterceptor
name|interceptor
init|=
operator|new
name|BareInInterceptor
argument_list|()
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|XMLInputFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createXMLStreamReader
argument_list|(
name|getTestStream
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"resources/bareNoParamDocLitBareReq.xml"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|XMLStreamReader
name|reader
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// skip to the end element of soap body, so that we can serve an empty request to
comment|// interceptor
name|StaxUtils
operator|.
name|skipToStartOfElement
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|StaxUtils
operator|.
name|nextEvent
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|INBOUND_MESSAGE
argument_list|,
name|Message
operator|.
name|INBOUND_MESSAGE
argument_list|)
expr_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|message
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|parameters
init|=
name|message
operator|.
name|getContent
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInterceptorOutbound
parameter_list|()
throws|throws
name|Exception
block|{
name|setUpUsingHelloWorld
argument_list|()
expr_stmt|;
name|BareInInterceptor
name|interceptor
init|=
operator|new
name|BareInInterceptor
argument_list|()
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|XMLInputFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createXMLStreamReader
argument_list|(
name|getTestStream
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"resources/GreetMeDocLiteralResp.xml"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|message
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
name|message
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|parameters
init|=
name|message
operator|.
name|getContent
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parameters
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|obj
init|=
name|parameters
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|obj
operator|instanceof
name|GreetMeResponse
argument_list|)
expr_stmt|;
name|GreetMeResponse
name|greet
init|=
operator|(
name|GreetMeResponse
operator|)
name|obj
decl_stmt|;
name|assertEquals
argument_list|(
literal|"TestSOAPOutputPMessage"
argument_list|,
name|greet
operator|.
name|getResponseType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setUpUsingHelloWorld
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ns
init|=
literal|"http://apache.org/hello_world_soap_http"
decl_stmt|;
name|WSDLServiceFactory
name|factory
init|=
operator|new
name|WSDLServiceFactory
argument_list|(
name|bus
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/jaxb/hello_world.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|,
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
literal|"SOAPService"
argument_list|)
argument_list|)
decl_stmt|;
name|service
operator|=
name|factory
operator|.
name|create
argument_list|()
expr_stmt|;
name|endpointInfo
operator|=
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getEndpoint
argument_list|(
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
literal|"SoapPort"
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|new
name|EndpointImpl
argument_list|(
name|bus
argument_list|,
name|service
argument_list|,
name|endpointInfo
argument_list|)
expr_stmt|;
name|JAXBDataBinding
name|db
init|=
operator|new
name|JAXBDataBinding
argument_list|()
decl_stmt|;
name|db
operator|.
name|setContext
argument_list|(
name|JAXBContext
operator|.
name|newInstance
argument_list|(
operator|new
name|Class
index|[]
block|{
name|GreetMe
operator|.
name|class
block|,
name|GreetMeResponse
operator|.
name|class
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|service
operator|.
name|setDataBinding
argument_list|(
name|db
argument_list|)
expr_stmt|;
name|operation
operator|=
name|endpointInfo
operator|.
name|getBinding
argument_list|()
operator|.
name|getOperation
argument_list|(
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
literal|"greetMe"
argument_list|)
argument_list|)
expr_stmt|;
name|operation
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getInput
argument_list|()
operator|.
name|getMessagePartByIndex
argument_list|(
literal|0
argument_list|)
operator|.
name|setTypeClass
argument_list|(
name|GreetMe
operator|.
name|class
argument_list|)
expr_stmt|;
name|operation
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
name|setTypeClass
argument_list|(
name|GreetMeResponse
operator|.
name|class
argument_list|)
expr_stmt|;
name|message
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
name|message
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|exchange
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
name|exchange
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|Binding
operator|.
name|class
argument_list|,
name|endpoint
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setUpUsingDocLit
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ns
init|=
literal|"http://apache.org/hello_world_doc_lit_bare"
decl_stmt|;
name|WSDLServiceFactory
name|factory
init|=
operator|new
name|WSDLServiceFactory
argument_list|(
name|bus
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/jaxb/doc_lit_bare.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|,
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
literal|"SOAPService"
argument_list|)
argument_list|)
decl_stmt|;
name|service
operator|=
name|factory
operator|.
name|create
argument_list|()
expr_stmt|;
name|endpointInfo
operator|=
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getEndpoint
argument_list|(
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
literal|"SoapPort"
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|new
name|EndpointImpl
argument_list|(
name|bus
argument_list|,
name|service
argument_list|,
name|endpointInfo
argument_list|)
expr_stmt|;
name|JAXBDataBinding
name|db
init|=
operator|new
name|JAXBDataBinding
argument_list|()
decl_stmt|;
name|db
operator|.
name|setContext
argument_list|(
name|JAXBContext
operator|.
name|newInstance
argument_list|(
operator|new
name|Class
index|[]
block|{
name|TradePriceData
operator|.
name|class
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|service
operator|.
name|setDataBinding
argument_list|(
name|db
argument_list|)
expr_stmt|;
name|operation
operator|=
name|endpointInfo
operator|.
name|getBinding
argument_list|()
operator|.
name|getOperation
argument_list|(
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
literal|"SayHi"
argument_list|)
argument_list|)
expr_stmt|;
name|operation
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getInput
argument_list|()
operator|.
name|getMessagePartByIndex
argument_list|(
literal|0
argument_list|)
operator|.
name|setTypeClass
argument_list|(
name|TradePriceData
operator|.
name|class
argument_list|)
expr_stmt|;
name|operation
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
name|setTypeClass
argument_list|(
name|TradePriceData
operator|.
name|class
argument_list|)
expr_stmt|;
name|message
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
name|message
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|exchange
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
name|exchange
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|Binding
operator|.
name|class
argument_list|,
name|endpoint
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|InputStream
name|getTestStream
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|,
name|String
name|file
parameter_list|)
block|{
return|return
name|clz
operator|.
name|getResourceAsStream
argument_list|(
name|file
argument_list|)
return|;
block|}
specifier|public
name|XMLStreamReader
name|getXMLStreamReader
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
return|return
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|is
argument_list|)
return|;
block|}
specifier|public
name|XMLStreamWriter
name|getXMLStreamWriter
parameter_list|(
name|OutputStream
name|os
parameter_list|)
block|{
return|return
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|os
argument_list|)
return|;
block|}
block|}
end_class

end_unit

