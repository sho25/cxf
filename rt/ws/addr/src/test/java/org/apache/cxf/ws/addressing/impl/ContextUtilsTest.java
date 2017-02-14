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
name|ws
operator|.
name|addressing
operator|.
name|impl
package|;
end_package

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
name|ws
operator|.
name|WebFault
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
name|SoapBindingConstants
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
name|SoapFault
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
name|Extensible
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
name|FaultInfo
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
name|ws
operator|.
name|addressing
operator|.
name|AttributedURIType
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
name|ws
operator|.
name|addressing
operator|.
name|ContextUtils
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
name|ws
operator|.
name|addressing
operator|.
name|JAXWSAConstants
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
name|ws
operator|.
name|addressing
operator|.
name|Names
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
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|ContextUtilsTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|WSA_ACTION_QNAME
init|=
operator|new
name|QName
argument_list|(
name|JAXWSAConstants
operator|.
name|NS_WSA
argument_list|,
name|Names
operator|.
name|WSAW_ACTION_NAME
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|OLD_WSDL_WSA_ACTION_QNAME
init|=
operator|new
name|QName
argument_list|(
name|Names
operator|.
name|WSA_NAMESPACE_WSDL_NAME_OLD
argument_list|,
name|Names
operator|.
name|WSAW_ACTION_NAME
argument_list|)
decl_stmt|;
specifier|private
name|IMocksControl
name|control
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
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
name|Test
specifier|public
name|void
name|testGetActionFromExtensible
parameter_list|()
block|{
name|Map
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
name|attributes
init|=
operator|new
name|HashMap
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Extensible
name|ext
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Extensible
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ext
operator|.
name|getExtensionAttributes
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|attributes
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|attributes
operator|.
name|put
argument_list|(
name|WSA_ACTION_QNAME
argument_list|,
literal|"urn:foo:test:2"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ext
operator|.
name|getExtensionAttribute
argument_list|(
name|JAXWSAConstants
operator|.
name|WSAW_ACTION_QNAME
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"urn:foo:test:1"
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|String
name|action
init|=
name|InternalContextUtils
operator|.
name|getAction
argument_list|(
name|ext
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"urn:foo:test:1"
argument_list|,
name|action
argument_list|)
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|attributes
operator|.
name|clear
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ext
operator|.
name|getExtensionAttributes
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|attributes
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ext
operator|.
name|getExtensionAttribute
argument_list|(
name|JAXWSAConstants
operator|.
name|WSAW_ACTION_QNAME
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|put
argument_list|(
name|WSA_ACTION_QNAME
argument_list|,
literal|"urn:foo:test:2"
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|=
name|InternalContextUtils
operator|.
name|getAction
argument_list|(
name|ext
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"urn:foo:test:2"
argument_list|,
name|action
argument_list|)
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|attributes
operator|.
name|clear
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ext
operator|.
name|getExtensionAttributes
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|attributes
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ext
operator|.
name|getExtensionAttribute
argument_list|(
name|JAXWSAConstants
operator|.
name|WSAW_ACTION_QNAME
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|put
argument_list|(
name|OLD_WSDL_WSA_ACTION_QNAME
argument_list|,
literal|"urn:foo:test:3"
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|=
name|InternalContextUtils
operator|.
name|getAction
argument_list|(
name|ext
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"urn:foo:test:3"
argument_list|,
name|action
argument_list|)
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|attributes
operator|.
name|clear
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ext
operator|.
name|getExtensionAttributes
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|attributes
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ext
operator|.
name|getExtensionAttribute
argument_list|(
name|JAXWSAConstants
operator|.
name|WSAW_ACTION_QNAME
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|=
name|InternalContextUtils
operator|.
name|getAction
argument_list|(
name|ext
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|action
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetActionFromMessage
parameter_list|()
block|{
name|Message
name|msg
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
decl_stmt|;
name|QName
name|mqname
init|=
operator|new
name|QName
argument_list|(
literal|"http://foo.com"
argument_list|,
literal|"bar"
argument_list|)
decl_stmt|;
name|QName
name|fqname
init|=
operator|new
name|QName
argument_list|(
literal|"urn:foo:test:4"
argument_list|,
literal|"fault"
argument_list|)
decl_stmt|;
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
name|OUTPUT
argument_list|,
name|mqname
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
literal|"partInfo"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|operationInfo
operator|.
name|setOutput
argument_list|(
literal|"outputName"
argument_list|,
name|messageInfo
argument_list|)
expr_stmt|;
name|FaultInfo
name|faultInfo
init|=
operator|new
name|FaultInfo
argument_list|(
name|fqname
argument_list|,
name|mqname
argument_list|,
name|operationInfo
argument_list|)
decl_stmt|;
name|operationInfo
operator|.
name|addFault
argument_list|(
name|faultInfo
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
comment|// test 1 : retrieving the normal action prop from the message
name|EasyMock
operator|.
name|expect
argument_list|(
name|msg
operator|.
name|getExchange
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|exchange
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|exchange
operator|.
name|getBindingOperationInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|boi
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|msg
operator|.
name|get
argument_list|(
name|ContextUtils
operator|.
name|ACTION
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"urn:foo:test:1"
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|AttributedURIType
name|action
init|=
name|InternalContextUtils
operator|.
name|getAction
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"urn:foo:test:1"
argument_list|,
name|action
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// test 2 : retrieving the normal soap action prop from the message
name|EasyMock
operator|.
name|expect
argument_list|(
name|msg
operator|.
name|getExchange
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|exchange
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|exchange
operator|.
name|getBindingOperationInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|boi
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|msg
operator|.
name|get
argument_list|(
name|SoapBindingConstants
operator|.
name|SOAP_ACTION
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"urn:foo:test:2"
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|=
name|InternalContextUtils
operator|.
name|getAction
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"urn:foo:test:2"
argument_list|,
name|action
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// test 3 : retrieving the action prop from the message info
name|EasyMock
operator|.
name|expect
argument_list|(
name|msg
operator|.
name|getExchange
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|exchange
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|exchange
operator|.
name|getBindingOperationInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|boi
argument_list|)
expr_stmt|;
name|messageInfo
operator|.
name|setProperty
argument_list|(
name|ContextUtils
operator|.
name|ACTION
argument_list|,
literal|"urn:foo:test:3"
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|=
name|InternalContextUtils
operator|.
name|getAction
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"urn:foo:test:3"
argument_list|,
name|action
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// test 4 : retrieving the action for a fault without message part
name|SoapFault
name|fault
init|=
operator|new
name|SoapFault
argument_list|(
literal|"faulty service"
argument_list|,
operator|new
name|RuntimeException
argument_list|()
argument_list|,
name|fqname
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|msg
operator|.
name|getExchange
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|exchange
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|msg
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|fault
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|exchange
operator|.
name|getBindingOperationInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|boi
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|=
name|InternalContextUtils
operator|.
name|getAction
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// test 5 : retrieving the action for a fault with matching message part
name|faultInfo
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
literal|"faultInfo"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|faultInfo
operator|.
name|getMessagePart
argument_list|(
literal|0
argument_list|)
operator|.
name|setTypeClass
argument_list|(
name|RuntimeException
operator|.
name|class
argument_list|)
expr_stmt|;
name|faultInfo
operator|.
name|addExtensionAttribute
argument_list|(
name|Names
operator|.
name|WSAW_ACTION_QNAME
argument_list|,
literal|"urn:foo:test:4"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|msg
operator|.
name|getExchange
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|exchange
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|msg
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|fault
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|exchange
operator|.
name|getBindingOperationInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|boi
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|=
name|InternalContextUtils
operator|.
name|getAction
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"urn:foo:test:4"
argument_list|,
name|action
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// test 6 : retrieving the action for a ws-addr fault with matching message part
name|fault
operator|=
operator|new
name|SoapFault
argument_list|(
literal|"Action Mismatch"
argument_list|,
operator|new
name|QName
argument_list|(
name|Names
operator|.
name|WSA_NAMESPACE_NAME
argument_list|,
name|Names
operator|.
name|ACTION_MISMATCH_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|msg
operator|.
name|getExchange
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|exchange
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|msg
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|fault
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|exchange
operator|.
name|getBindingOperationInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|boi
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|=
name|InternalContextUtils
operator|.
name|getAction
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Names
operator|.
name|WSA_DEFAULT_FAULT_ACTION
argument_list|,
name|action
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// test 7 : retrieve the action for a fault matching the fault class with the WebFault annotation
name|fault
operator|=
operator|new
name|SoapFault
argument_list|(
literal|"faulty service"
argument_list|,
operator|new
name|TestFault
argument_list|()
argument_list|,
name|Fault
operator|.
name|FAULT_CODE_SERVER
argument_list|)
expr_stmt|;
name|faultInfo
operator|.
name|addMessagePart
argument_list|(
operator|new
name|MessagePartInfo
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://foo.com:7"
argument_list|,
literal|"faultInfo"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|faultInfo
operator|.
name|getMessagePart
argument_list|(
literal|0
argument_list|)
operator|.
name|setTypeClass
argument_list|(
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
name|faultInfo
operator|.
name|getMessagePart
argument_list|(
literal|0
argument_list|)
operator|.
name|setConcreteName
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo:test:7"
argument_list|,
literal|"testFault"
argument_list|)
argument_list|)
expr_stmt|;
name|faultInfo
operator|.
name|addExtensionAttribute
argument_list|(
name|Names
operator|.
name|WSAW_ACTION_QNAME
argument_list|,
literal|"urn:foo:test:7"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|msg
operator|.
name|getExchange
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|exchange
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|msg
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|fault
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|exchange
operator|.
name|getBindingOperationInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|boi
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|=
name|InternalContextUtils
operator|.
name|getAction
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"urn:foo:test:7"
argument_list|,
name|action
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|WebFault
argument_list|(
name|name
operator|=
literal|"testFault"
argument_list|,
name|targetNamespace
operator|=
literal|"urn:foo:test:7"
argument_list|)
specifier|public
class|class
name|TestFault
extends|extends
name|Exception
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
block|}
block|}
end_class

end_unit

