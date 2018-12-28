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
name|utils
operator|.
name|OrbConfig
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

begin_class
specifier|public
class|class
name|CorbaDestinationTest
extends|extends
name|Assert
block|{
specifier|protected
specifier|static
name|TestUtils
name|testUtils
decl_stmt|;
name|EndpointInfo
name|endpointInfo
decl_stmt|;
name|OrbConfig
name|orbConfig
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
name|testUtils
operator|=
operator|new
name|TestUtils
argument_list|()
expr_stmt|;
name|orbConfig
operator|=
operator|new
name|OrbConfig
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDestination
parameter_list|()
throws|throws
name|Exception
block|{
name|endpointInfo
operator|=
name|testUtils
operator|.
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
name|CorbaDestination
name|destination
init|=
operator|new
name|CorbaDestination
argument_list|(
name|endpointInfo
argument_list|,
name|orbConfig
argument_list|)
decl_stmt|;
name|EndpointReferenceType
name|rtype
init|=
name|destination
operator|.
name|getAddress
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"EndpointReferenceType should not be null"
argument_list|,
name|rtype
argument_list|)
expr_stmt|;
name|BindingInfo
name|bindingInfo
init|=
name|destination
operator|.
name|getBindingInfo
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"BindingInfo should not be null"
argument_list|,
name|bindingInfo
argument_list|)
expr_stmt|;
name|EndpointInfo
name|e2
init|=
name|destination
operator|.
name|getEndPointInfo
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"EndpointInfo should not be null"
argument_list|,
name|e2
argument_list|)
expr_stmt|;
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|CorbaServerConduit
name|serverConduit
init|=
operator|(
name|CorbaServerConduit
operator|)
name|destination
operator|.
name|getBackChannel
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"CorbaServerConduit should not be null"
argument_list|,
name|serverConduit
argument_list|)
expr_stmt|;
block|}
comment|/*     @Test     public void testSetMessageObserverActivate() throws Exception {        endpointInfo = testUtils.setupServiceInfo("http://cxf.apache.org/bindings/corba/simple",                         "/wsdl/simpleIdl.wsdl", "SimpleCORBAService",                         "SimpleCORBAPort");        CorbaDestination destination = new CorbaDestination(endpointInfo);        String addr = destination.getAddressType().getLocation();        assertEquals(addr, "corbaloc::localhost:40000/Simple");         Bus bus = BusFactory.newInstance().getDefaultBus();        Service service = new ServiceImpl();        Endpoint endpoint = new EndpointImpl(bus, service, endpointInfo);        MessageObserver observer = new ChainInitiationObserver(endpoint, bus);        destination.setMessageObserver(observer);        assertNotNull("orb should not be null",  destination.getOrb());         try {            File file = new File("endpoint.ior");            assertEquals(true,file.exists());        } finally {            new File("endpoint.ior").deleteOnExit();        }         addr = destination.getAddressType().getLocation();        addr = addr.substring(0,4);        assertEquals(addr, "IOR:");        destination.shutdown();    }*/
block|}
end_class

end_unit

