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
name|jaxws
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|calculator
operator|.
name|CalculatorImpl
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
name|EndpointException
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
name|jaxws
operator|.
name|support
operator|.
name|JaxWsServiceFactoryBean
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
name|factory
operator|.
name|ReflectionServiceFactoryBean
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
name|ServiceModelUtil
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
name|GreeterImpl
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
name|RPCLitGreeterImpl
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
name|ServiceModelUtilsTest
extends|extends
name|AbstractJaxWsTest
block|{
name|Message
name|message
decl_stmt|;
name|Exchange
name|exchange
decl_stmt|;
name|ReflectionServiceFactoryBean
name|bean
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
name|setUpBus
argument_list|()
expr_stmt|;
name|message
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
name|exchange
operator|=
operator|new
name|ExchangeImpl
argument_list|()
expr_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|bean
operator|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
expr_stmt|;
name|bean
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Service
name|getService
parameter_list|(
name|URL
name|wsdl
parameter_list|,
name|Class
name|implClz
parameter_list|,
name|QName
name|port
parameter_list|)
throws|throws
name|EndpointException
block|{
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setWsdlURL
argument_list|(
name|wsdl
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setServiceClass
argument_list|(
name|implClz
argument_list|)
expr_stmt|;
name|Service
name|service
init|=
name|bean
operator|.
name|create
argument_list|()
decl_stmt|;
name|EndpointInfo
name|endpointInfo
init|=
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
name|port
argument_list|)
decl_stmt|;
name|Endpoint
name|endpoint
init|=
operator|new
name|EndpointImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|service
argument_list|,
name|endpointInfo
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
return|return
name|service
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetOperationInputPartNamesWrapped
parameter_list|()
throws|throws
name|Exception
block|{
name|getService
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/hello_world.wsdl"
argument_list|)
argument_list|,
name|GreeterImpl
operator|.
name|class
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SoapPort"
argument_list|)
argument_list|)
expr_stmt|;
name|BindingOperationInfo
name|operation
init|=
name|ServiceModelUtil
operator|.
name|getOperation
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|,
literal|"greetMe"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|operation
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|ServiceModelUtil
operator|.
name|getOperationInputPartNames
argument_list|(
name|operation
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"requestType"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|operation
operator|=
name|ServiceModelUtil
operator|.
name|getOperation
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|,
literal|"sayHi"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|operation
argument_list|)
expr_stmt|;
name|names
operator|=
name|ServiceModelUtil
operator|.
name|getOperationInputPartNames
argument_list|(
name|operation
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|names
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
name|testGetOperationInputPartNamesWrapped2
parameter_list|()
throws|throws
name|Exception
block|{
name|getService
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/calculator.wsdl"
argument_list|)
argument_list|,
name|CalculatorImpl
operator|.
name|class
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://apache.org/cxf/calculator"
argument_list|,
literal|"CalculatorPort"
argument_list|)
argument_list|)
expr_stmt|;
name|BindingOperationInfo
name|operation
init|=
name|ServiceModelUtil
operator|.
name|getOperation
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|,
literal|"add"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|operation
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|ServiceModelUtil
operator|.
name|getOperationInputPartNames
argument_list|(
name|operation
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"arg0"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"arg1"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetOperationInputPartNamesBare
parameter_list|()
throws|throws
name|Exception
block|{
name|getService
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/hello_world_xml_bare.wsdl"
argument_list|)
argument_list|,
name|org
operator|.
name|apache
operator|.
name|hello_world_xml_http
operator|.
name|bare
operator|.
name|GreeterImpl
operator|.
name|class
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_xml_http/bare"
argument_list|,
literal|"XMLPort"
argument_list|)
argument_list|)
expr_stmt|;
name|BindingOperationInfo
name|operation
init|=
name|ServiceModelUtil
operator|.
name|getOperation
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|,
literal|"greetMe"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|operation
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|ServiceModelUtil
operator|.
name|getOperationInputPartNames
argument_list|(
name|operation
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"requestType"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|operation
operator|=
name|ServiceModelUtil
operator|.
name|getOperation
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|,
literal|"sayHi"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|operation
argument_list|)
expr_stmt|;
name|names
operator|=
name|ServiceModelUtil
operator|.
name|getOperationInputPartNames
argument_list|(
name|operation
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|names
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
name|testGetOperationInputPartNamesRpc
parameter_list|()
throws|throws
name|Exception
block|{
name|getService
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/hello_world_rpc_lit.wsdl"
argument_list|)
argument_list|,
name|RPCLitGreeterImpl
operator|.
name|class
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"SoapPortRPCLit"
argument_list|)
argument_list|)
expr_stmt|;
name|BindingOperationInfo
name|operation
init|=
name|ServiceModelUtil
operator|.
name|getOperation
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|,
literal|"greetMe"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|operation
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|ServiceModelUtil
operator|.
name|getOperationInputPartNames
argument_list|(
name|operation
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"in"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|operation
operator|=
name|ServiceModelUtil
operator|.
name|getOperation
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|,
literal|"sayHi"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|operation
argument_list|)
expr_stmt|;
name|names
operator|=
name|ServiceModelUtil
operator|.
name|getOperationInputPartNames
argument_list|(
name|operation
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|operation
operator|=
name|ServiceModelUtil
operator|.
name|getOperation
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|,
literal|"greetUs"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|operation
argument_list|)
expr_stmt|;
name|names
operator|=
name|ServiceModelUtil
operator|.
name|getOperationInputPartNames
argument_list|(
name|operation
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|//System.err.println(names);
block|}
block|}
end_class

end_unit

