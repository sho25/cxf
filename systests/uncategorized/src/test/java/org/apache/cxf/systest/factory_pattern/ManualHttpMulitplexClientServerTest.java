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
name|systest
operator|.
name|factory_pattern
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Proxy
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
name|BindingProvider
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
name|Endpoint
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
name|Service
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
name|wsaddressing
operator|.
name|W3CEndpointReference
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
name|soap
operator|.
name|SoapBindingFactory
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
name|factory_pattern
operator|.
name|IsEvenResponse
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
name|factory_pattern
operator|.
name|Number
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
name|factory_pattern
operator|.
name|NumberFactory
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
name|factory_pattern
operator|.
name|NumberFactoryService
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
name|factory_pattern
operator|.
name|NumberService
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
name|ServiceImpl
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
name|spi
operator|.
name|ProviderImpl
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
name|ServiceDelegateAccessor
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusClientServerTestBase
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusTestServerBase
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
name|testutil
operator|.
name|common
operator|.
name|TestUtil
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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

begin_class
specifier|public
class|class
name|ManualHttpMulitplexClientServerTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|ManualHttpMulitplexClientServerTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FACTORY_ADDRESS
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/NumberFactoryService/NumberFactoryPort"
decl_stmt|;
specifier|public
specifier|static
class|class
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
name|Endpoint
name|ep
decl_stmt|;
name|ManualNumberFactoryImpl
name|implementor
decl_stmt|;
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|setBus
argument_list|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|)
expr_stmt|;
name|implementor
operator|=
operator|new
name|ManualNumberFactoryImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|ep
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|FACTORY_ADDRESS
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|ep
operator|.
name|stop
argument_list|()
expr_stmt|;
name|ep
operator|=
literal|null
expr_stmt|;
name|implementor
operator|.
name|stop
argument_list|()
expr_stmt|;
name|implementor
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
try|try
block|{
name|Server
name|s
init|=
operator|new
name|Server
argument_list|()
decl_stmt|;
name|s
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"done!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|createStaticBus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithManualMultiplexEprCreation
parameter_list|()
throws|throws
name|Exception
block|{
name|NumberFactoryService
name|service
init|=
operator|new
name|NumberFactoryService
argument_list|()
decl_stmt|;
name|NumberFactory
name|nfact
init|=
name|service
operator|.
name|getNumberFactoryPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|nfact
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|W3CEndpointReference
name|w3cEpr
init|=
name|nfact
operator|.
name|create
argument_list|(
literal|"2"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"reference"
argument_list|,
name|w3cEpr
argument_list|)
expr_stmt|;
comment|// use the epr info only
comment|// no wsdl so default generated soap/http binding will be used
comment|// address url must come from the calling context
name|EndpointReferenceType
name|epr
init|=
name|ProviderImpl
operator|.
name|convertToInternal
argument_list|(
name|w3cEpr
argument_list|)
decl_stmt|;
name|QName
name|serviceName
init|=
name|EndpointReferenceUtils
operator|.
name|getServiceName
argument_list|(
name|epr
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|Service
name|numService
init|=
name|Service
operator|.
name|create
argument_list|(
name|serviceName
argument_list|)
decl_stmt|;
name|String
name|portString
init|=
name|EndpointReferenceUtils
operator|.
name|getPortName
argument_list|(
name|epr
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
name|serviceName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|portString
argument_list|)
decl_stmt|;
name|numService
operator|.
name|addPort
argument_list|(
name|portName
argument_list|,
name|SoapBindingFactory
operator|.
name|SOAP_11_BINDING
argument_list|,
literal|"http://foo"
argument_list|)
expr_stmt|;
name|Number
name|num
init|=
name|numService
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|Number
operator|.
name|class
argument_list|)
decl_stmt|;
name|setupContextWithEprAddress
argument_list|(
name|epr
argument_list|,
name|num
argument_list|)
expr_stmt|;
name|IsEvenResponse
name|numResp
init|=
name|num
operator|.
name|isEven
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"2 is even"
argument_list|,
name|numResp
operator|.
name|isEven
argument_list|()
argument_list|)
expr_stmt|;
comment|// try again with the address from another epr
name|w3cEpr
operator|=
name|nfact
operator|.
name|create
argument_list|(
literal|"3"
argument_list|)
expr_stmt|;
name|epr
operator|=
name|ProviderImpl
operator|.
name|convertToInternal
argument_list|(
name|w3cEpr
argument_list|)
expr_stmt|;
name|setupContextWithEprAddress
argument_list|(
name|epr
argument_list|,
name|num
argument_list|)
expr_stmt|;
name|numResp
operator|=
name|num
operator|.
name|isEven
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
literal|"3 is not even"
argument_list|,
name|numResp
operator|.
name|isEven
argument_list|()
argument_list|)
expr_stmt|;
comment|// try again with the address from another epr
name|w3cEpr
operator|=
name|nfact
operator|.
name|create
argument_list|(
literal|"6"
argument_list|)
expr_stmt|;
name|epr
operator|=
name|ProviderImpl
operator|.
name|convertToInternal
argument_list|(
name|w3cEpr
argument_list|)
expr_stmt|;
name|setupContextWithEprAddress
argument_list|(
name|epr
argument_list|,
name|num
argument_list|)
expr_stmt|;
name|numResp
operator|=
name|num
operator|.
name|isEven
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"6 is even"
argument_list|,
name|numResp
operator|.
name|isEven
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithGetPortExtensionHttp
parameter_list|()
throws|throws
name|Exception
block|{
name|NumberFactoryService
name|service
init|=
operator|new
name|NumberFactoryService
argument_list|()
decl_stmt|;
name|NumberFactory
name|factory
init|=
name|service
operator|.
name|getNumberFactoryPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|factory
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|W3CEndpointReference
name|w3cEpr
init|=
name|factory
operator|.
name|create
argument_list|(
literal|"20"
argument_list|)
decl_stmt|;
name|EndpointReferenceType
name|numberTwoRef
init|=
name|ProviderImpl
operator|.
name|convertToInternal
argument_list|(
name|w3cEpr
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"reference"
argument_list|,
name|numberTwoRef
argument_list|)
expr_stmt|;
comment|// use getPort with epr api on service
name|NumberService
name|numService
init|=
operator|new
name|NumberService
argument_list|()
decl_stmt|;
name|ServiceImpl
name|serviceImpl
init|=
name|ServiceDelegateAccessor
operator|.
name|get
argument_list|(
name|numService
argument_list|)
decl_stmt|;
name|Number
name|num
init|=
name|serviceImpl
operator|.
name|getPort
argument_list|(
name|numberTwoRef
argument_list|,
name|Number
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"20 is even"
argument_list|,
name|num
operator|.
name|isEven
argument_list|()
operator|.
name|isEven
argument_list|()
argument_list|)
expr_stmt|;
name|w3cEpr
operator|=
name|factory
operator|.
name|create
argument_list|(
literal|"23"
argument_list|)
expr_stmt|;
name|EndpointReferenceType
name|numberTwentyThreeRef
init|=
name|ProviderImpl
operator|.
name|convertToInternal
argument_list|(
name|w3cEpr
argument_list|)
decl_stmt|;
name|num
operator|=
name|serviceImpl
operator|.
name|getPort
argument_list|(
name|numberTwentyThreeRef
argument_list|,
name|Number
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"23 is not even"
argument_list|,
name|num
operator|.
name|isEven
argument_list|()
operator|.
name|isEven
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setupContextWithEprAddress
parameter_list|(
name|EndpointReferenceType
name|epr
parameter_list|,
name|Number
name|num
parameter_list|)
block|{
name|String
name|address
init|=
name|EndpointReferenceUtils
operator|.
name|getAddress
argument_list|(
name|epr
argument_list|)
decl_stmt|;
name|InvocationHandler
name|handler
init|=
name|Proxy
operator|.
name|getInvocationHandler
argument_list|(
name|num
argument_list|)
decl_stmt|;
name|BindingProvider
name|bp
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|handler
operator|instanceof
name|BindingProvider
condition|)
block|{
name|bp
operator|=
operator|(
name|BindingProvider
operator|)
name|handler
expr_stmt|;
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
name|address
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

