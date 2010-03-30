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
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|XMLStreamConstants
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
name|apache
operator|.
name|cxf
operator|.
name|databinding
operator|.
name|source
operator|.
name|SourceDataBinding
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
name|InterfaceInfo
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
name|MessageInfo
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
name|MessageInfo
operator|.
name|Type
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
name|service
operator|.
name|model
operator|.
name|OperationInfo
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
name|easymock
operator|.
name|classextension
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
name|classextension
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

begin_comment
comment|/**  * Unit test for testing DocLiteralInInterceptor to use Source Data Binding   *   */
end_comment

begin_class
specifier|public
class|class
name|DocLiteralInInterceptorTest
extends|extends
name|Assert
block|{
specifier|protected
name|IMocksControl
name|control
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
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
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
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnmarshalSourceData
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
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"resources/multiPartDocLitBareReq.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|XMLStreamConstants
operator|.
name|START_ELEMENT
argument_list|,
name|reader
operator|.
name|nextTag
argument_list|()
argument_list|)
expr_stmt|;
name|XMLStreamReader
name|filteredReader
init|=
operator|new
name|PartialXMLStreamReader
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
decl_stmt|;
comment|// advance the xml reader to the message parts
name|StaxUtils
operator|.
name|read
argument_list|(
name|filteredReader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|XMLStreamConstants
operator|.
name|START_ELEMENT
argument_list|,
name|reader
operator|.
name|nextTag
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|service
operator|.
name|getDataBinding
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|SourceDataBinding
argument_list|()
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|service
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|0
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|OperationInfo
name|operationInfo
init|=
operator|new
name|OperationInfo
argument_list|()
decl_stmt|;
name|MessageInfo
name|messageInfo
init|=
operator|new
name|MessageInfo
argument_list|(
name|operationInfo
argument_list|,
name|Type
operator|.
name|INPUT
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://foo.com"
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
decl_stmt|;
name|messageInfo
operator|.
name|addMessagePart
argument_list|(
operator|new
name|MessagePartInfo
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://foo.com"
argument_list|,
literal|"partInfo1"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|messageInfo
operator|.
name|addMessagePart
argument_list|(
operator|new
name|MessagePartInfo
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://foo.com"
argument_list|,
literal|"partInfo2"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|messageInfo
operator|.
name|addMessagePart
argument_list|(
operator|new
name|MessagePartInfo
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://foo.com"
argument_list|,
literal|"partInfo3"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|messageInfo
operator|.
name|addMessagePart
argument_list|(
operator|new
name|MessagePartInfo
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://foo.com"
argument_list|,
literal|"partInfo4"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|operationInfo
operator|.
name|setInput
argument_list|(
literal|"inputName"
argument_list|,
name|messageInfo
argument_list|)
expr_stmt|;
name|BindingOperationInfo
name|boi
init|=
operator|new
name|BindingOperationInfo
argument_list|(
literal|null
argument_list|,
name|operationInfo
argument_list|)
decl_stmt|;
name|exchange
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
name|EndpointInfo
name|endpointInfo
init|=
name|control
operator|.
name|createMock
argument_list|(
name|EndpointInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|BindingInfo
name|binding
init|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|endpointInfo
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpointInfo
operator|.
name|getBinding
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|binding
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|binding
operator|.
name|getProperties
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpointInfo
operator|.
name|getProperties
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpoint
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|0
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|ServiceInfo
name|serviceInfo
init|=
name|control
operator|.
name|createMock
argument_list|(
name|ServiceInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpointInfo
operator|.
name|getService
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|serviceInfo
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|serviceInfo
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://foo.com"
argument_list|,
literal|"service"
argument_list|)
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|InterfaceInfo
name|interfaceInfo
init|=
name|control
operator|.
name|createMock
argument_list|(
name|InterfaceInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|serviceInfo
operator|.
name|getInterface
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|interfaceInfo
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|interfaceInfo
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://foo.com"
argument_list|,
literal|"interface"
argument_list|)
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpointInfo
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://foo.com"
argument_list|,
literal|"endpoint"
argument_list|)
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpointInfo
operator|.
name|getProperty
argument_list|(
literal|"URI"
argument_list|,
name|URI
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|URI
argument_list|(
literal|"dummy"
argument_list|)
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|OperationInfo
argument_list|>
name|operations
init|=
operator|new
name|ArrayList
argument_list|<
name|OperationInfo
argument_list|>
argument_list|()
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|interfaceInfo
operator|.
name|getOperations
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|operations
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|SCHEMA_VALIDATION_ENABLED
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|reader
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
operator|new
name|DocLiteralInInterceptor
argument_list|()
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|MessageContentsList
name|params
init|=
operator|(
name|MessageContentsList
operator|)
name|m
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
literal|4
argument_list|,
name|params
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"StringDefaultInputElem"
argument_list|,
operator|(
operator|(
name|DOMSource
operator|)
name|params
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getNode
argument_list|()
operator|.
name|getFirstChild
argument_list|()
operator|.
name|getNodeName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"IntParamInElem"
argument_list|,
operator|(
operator|(
name|DOMSource
operator|)
name|params
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|)
operator|.
name|getNode
argument_list|()
operator|.
name|getFirstChild
argument_list|()
operator|.
name|getNodeName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

