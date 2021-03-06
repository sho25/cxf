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
name|javax
operator|.
name|wsdl
operator|.
name|Definition
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|factory
operator|.
name|WSDLFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|xml
operator|.
name|WSDLReader
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
name|helpers
operator|.
name|CastUtils
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
name|BindingMessageInfo
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
name|SchemaInfo
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
name|service
operator|.
name|model
operator|.
name|ServiceModelUtil
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
name|transport
operator|.
name|DestinationFactoryManager
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
name|WSDLServiceBuilder
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
name|easymock
operator|.
name|EasyMock
operator|.
name|expect
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

begin_class
specifier|public
class|class
name|ServiceModelUtilTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|WSDL_PATH
init|=
literal|"test-soap-header.wsdl"
decl_stmt|;
specifier|private
name|Definition
name|def
decl_stmt|;
specifier|private
name|Service
name|service
decl_stmt|;
specifier|private
name|ServiceInfo
name|serviceInfo
decl_stmt|;
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|BindingFactoryManager
name|bindingFactoryManager
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
name|String
name|wsdlUrl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|WSDL_PATH
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|WSDLFactory
name|wsdlFactory
init|=
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|WSDLReader
name|wsdlReader
init|=
name|wsdlFactory
operator|.
name|newWSDLReader
argument_list|()
decl_stmt|;
name|wsdlReader
operator|.
name|setFeature
argument_list|(
literal|"javax.wsdl.verbose"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|def
operator|=
name|wsdlReader
operator|.
name|readWSDL
argument_list|(
name|wsdlUrl
argument_list|)
expr_stmt|;
name|WSDLServiceBuilder
name|wsdlServiceBuilder
init|=
operator|new
name|WSDLServiceBuilder
argument_list|(
name|bus
argument_list|)
decl_stmt|;
for|for
control|(
name|Service
name|serv
range|:
name|CastUtils
operator|.
name|cast
argument_list|(
name|def
operator|.
name|getServices
argument_list|()
operator|.
name|values
argument_list|()
argument_list|,
name|Service
operator|.
name|class
argument_list|)
control|)
block|{
if|if
condition|(
name|serv
operator|!=
literal|null
condition|)
block|{
name|service
operator|=
name|serv
expr_stmt|;
break|break;
block|}
block|}
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|bus
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
expr_stmt|;
name|bindingFactoryManager
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|wsdlServiceBuilder
operator|=
operator|new
name|WSDLServiceBuilder
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bindingFactoryManager
argument_list|)
expr_stmt|;
name|DestinationFactoryManager
name|dfm
init|=
name|control
operator|.
name|createMock
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andStubReturn
argument_list|(
name|dfm
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|serviceInfo
operator|=
name|wsdlServiceBuilder
operator|.
name|buildServices
argument_list|(
name|def
argument_list|,
name|service
argument_list|)
operator|.
name|get
argument_list|(
literal|0
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
block|{      }
annotation|@
name|Test
specifier|public
name|void
name|testGetSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|BindingInfo
name|bindingInfo
init|=
literal|null
decl_stmt|;
name|bindingInfo
operator|=
name|serviceInfo
operator|.
name|getBindings
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
name|QName
name|name
init|=
operator|new
name|QName
argument_list|(
name|serviceInfo
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"inHeader"
argument_list|)
decl_stmt|;
name|BindingOperationInfo
name|inHeader
init|=
name|bindingInfo
operator|.
name|getOperation
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|BindingMessageInfo
name|input
init|=
name|inHeader
operator|.
name|getInput
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|input
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
literal|"inHeaderRequest"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|input
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"http://org.apache.cxf/headers"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|input
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|input
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isElement
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|input
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getElementQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
literal|"inHeader"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|input
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getElementQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"http://org.apache.cxf/headers"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|input
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isElement
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|input
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getElementQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
literal|"passenger"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|input
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getElementQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"http://mycompany.example.com/employees"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|input
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|isElement
argument_list|()
argument_list|)
expr_stmt|;
name|MessagePartInfo
name|messagePartInfo
init|=
name|input
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|SchemaInfo
name|schemaInfo
init|=
name|ServiceModelUtil
operator|.
name|getSchema
argument_list|(
name|serviceInfo
argument_list|,
name|messagePartInfo
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|schemaInfo
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"http://org.apache.cxf/headers"
argument_list|)
expr_stmt|;
name|messagePartInfo
operator|=
name|input
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|schemaInfo
operator|=
name|ServiceModelUtil
operator|.
name|getSchema
argument_list|(
name|serviceInfo
argument_list|,
name|messagePartInfo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|schemaInfo
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"http://mycompany.example.com/employees"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

