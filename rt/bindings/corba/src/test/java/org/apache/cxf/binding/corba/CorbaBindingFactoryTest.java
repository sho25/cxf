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
name|corba
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|CorbaConstants
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
name|transport
operator|.
name|Conduit
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
name|Destination
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
name|MessageObserver
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
name|EndpointReferenceType
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
name|CorbaBindingFactoryTest
extends|extends
name|Assert
block|{
specifier|protected
name|Bus
name|bus
decl_stmt|;
specifier|protected
name|EndpointInfo
name|endpointInfo
decl_stmt|;
specifier|protected
name|EndpointReferenceType
name|target
decl_stmt|;
specifier|protected
name|MessageObserver
name|observer
decl_stmt|;
specifier|protected
name|Message
name|inMessage
decl_stmt|;
name|CorbaBindingFactory
name|factory
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
name|getDefaultBus
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
name|factory
operator|=
operator|(
name|CorbaBindingFactory
operator|)
name|bfm
operator|.
name|getBindingFactory
argument_list|(
literal|"http://cxf.apache.org/bindings/corba"
argument_list|)
expr_stmt|;
name|bfm
operator|.
name|registerBindingFactory
argument_list|(
name|CorbaConstants
operator|.
name|NU_WSDL_CORBA
argument_list|,
name|factory
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setupServiceInfo
parameter_list|(
name|String
name|ns
parameter_list|,
name|String
name|wsdl
parameter_list|,
name|String
name|serviceName
parameter_list|,
name|String
name|portName
parameter_list|)
block|{
name|URL
name|wsdlUrl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|wsdl
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdlUrl
argument_list|)
expr_stmt|;
name|WSDLServiceFactory
name|f
init|=
operator|new
name|WSDLServiceFactory
argument_list|(
name|bus
argument_list|,
name|wsdlUrl
operator|.
name|toString
argument_list|()
argument_list|,
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|serviceName
argument_list|)
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
name|f
operator|.
name|create
argument_list|()
decl_stmt|;
name|endpointInfo
operator|=
name|service
operator|.
name|getEndpointInfo
argument_list|(
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|portName
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateBinding
parameter_list|()
throws|throws
name|Exception
block|{
name|IMocksControl
name|control
init|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
decl_stmt|;
name|BindingInfo
name|bindingInfo
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
name|CorbaBinding
name|binding
init|=
operator|(
name|CorbaBinding
operator|)
name|factory
operator|.
name|createBinding
argument_list|(
name|bindingInfo
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|CorbaBinding
operator|.
name|class
operator|.
name|isInstance
argument_list|(
name|binding
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|inInterceptors
init|=
name|binding
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|inInterceptors
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|outInterceptors
init|=
name|binding
operator|.
name|getOutInterceptors
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|outInterceptors
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|inInterceptors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|outInterceptors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetCorbaConduit
parameter_list|()
throws|throws
name|Exception
block|{
name|setupServiceInfo
argument_list|(
literal|"http://cxf.apache.org/bindings/corba/simple"
argument_list|,
literal|"/wsdl_corbabinding/simpleIdl.wsdl"
argument_list|,
literal|"SimpleCORBAService"
argument_list|,
literal|"SimpleCORBAPort"
argument_list|)
expr_stmt|;
name|Conduit
name|conduit
init|=
name|factory
operator|.
name|getConduit
argument_list|(
name|endpointInfo
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|conduit
argument_list|)
expr_stmt|;
name|conduit
operator|=
name|factory
operator|.
name|getConduit
argument_list|(
name|endpointInfo
argument_list|,
literal|null
argument_list|,
name|bus
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|conduit
argument_list|)
expr_stmt|;
name|target
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|EndpointReferenceType
operator|.
name|class
argument_list|)
expr_stmt|;
name|conduit
operator|=
name|factory
operator|.
name|getConduit
argument_list|(
name|endpointInfo
argument_list|,
name|target
argument_list|,
name|bus
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|conduit
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetDestination
parameter_list|()
throws|throws
name|Exception
block|{
name|setupServiceInfo
argument_list|(
literal|"http://cxf.apache.org/bindings/corba/simple"
argument_list|,
literal|"/wsdl_corbabinding/simpleIdl.wsdl"
argument_list|,
literal|"SimpleCORBAService"
argument_list|,
literal|"SimpleCORBAPort"
argument_list|)
expr_stmt|;
name|Destination
name|destination
init|=
name|factory
operator|.
name|getDestination
argument_list|(
name|endpointInfo
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|destination
argument_list|)
expr_stmt|;
name|target
operator|=
name|destination
operator|.
name|getAddress
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTransportIds
parameter_list|()
throws|throws
name|Exception
block|{
name|setupServiceInfo
argument_list|(
literal|"http://cxf.apache.org/bindings/corba/simple"
argument_list|,
literal|"/wsdl_corbabinding/simpleIdl.wsdl"
argument_list|,
literal|"SimpleCORBAService"
argument_list|,
literal|"SimpleCORBAPort"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|strs
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|strs
operator|.
name|add
argument_list|(
literal|"one"
argument_list|)
expr_stmt|;
name|strs
operator|.
name|add
argument_list|(
literal|"two"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setTransportIds
argument_list|(
name|strs
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|retStrs
init|=
name|factory
operator|.
name|getTransportIds
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|retStrs
argument_list|)
expr_stmt|;
name|String
name|str
init|=
name|retStrs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"one"
argument_list|,
name|str
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|str
operator|=
name|retStrs
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"two"
argument_list|,
name|str
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
name|testGetUriPrefixes
parameter_list|()
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|prefixes
init|=
name|factory
operator|.
name|getUriPrefixes
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Prefixes should not be null"
argument_list|,
name|prefixes
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
comment|// check this
annotation|@
name|Test
specifier|public
name|void
name|testSetBus
parameter_list|()
throws|throws
name|Exception
block|{
name|factory
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

