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
name|bus
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
name|java
operator|.
name|net
operator|.
name|URL
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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|Client
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
name|frontend
operator|.
name|ClientProxy
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
name|transport
operator|.
name|http
operator|.
name|HTTPConduit
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
name|transports
operator|.
name|http
operator|.
name|configuration
operator|.
name|ConnectionType
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
name|transports
operator|.
name|http
operator|.
name|configuration
operator|.
name|HTTPClientPolicy
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
name|Greeter
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
name|SOAPService
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
name|assertNotNull
import|;
end_import

begin_class
specifier|public
class|class
name|BusShutdownTest
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
name|BusShutdownTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testStartWorkShutdownWork
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|wsdlUrl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/hello_world.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"wsdl resource was not found"
argument_list|,
name|wsdlUrl
argument_list|)
expr_stmt|;
comment|// Since this test always fails in linux box, try to use other port
name|String
name|serverAddr
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/greeter/port"
decl_stmt|;
name|makeTwoWayCallOnNewBus
argument_list|(
name|wsdlUrl
argument_list|,
name|serverAddr
argument_list|)
expr_stmt|;
comment|// verify sutdown cleans the slate and reverts to null state
comment|//
comment|// This test should not need to workaroundHangWithDifferentAddr,
comment|// and when using ADDR, the test should not need:
comment|//  org.apache.cxf.transports.http_jetty.DontClosePort
comment|//
name|String
name|workaroundHangWithDifferentAddr
init|=
name|serverAddr
decl_stmt|;
comment|// reusing same address will cause failure, hang on client invoke
comment|//possibleWorkaroundHandWithDifferentAddr = ADDR.replace('8', '9');
name|makeTwoWayCallOnNewBus
argument_list|(
name|wsdlUrl
argument_list|,
name|workaroundHangWithDifferentAddr
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|makeTwoWayCallOnNewBus
parameter_list|(
name|URL
name|wsdlUrl
parameter_list|,
name|String
name|address
parameter_list|)
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|()
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|Endpoint
name|ep
init|=
name|createService
argument_list|(
name|address
argument_list|)
decl_stmt|;
name|doWork
argument_list|(
name|wsdlUrl
argument_list|,
name|address
argument_list|)
expr_stmt|;
comment|// this should revert the JVM to its original state pending gc
name|ep
operator|.
name|stop
argument_list|()
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doWork
parameter_list|(
name|URL
name|wsdlUrl
parameter_list|,
name|String
name|address
parameter_list|)
block|{
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|(
name|wsdlUrl
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|Greeter
name|greeter
init|=
name|service
operator|.
name|getSoapPort
argument_list|()
decl_stmt|;
comment|// overwrite client address
name|InvocationHandler
name|handler
init|=
name|Proxy
operator|.
name|getInvocationHandler
argument_list|(
name|greeter
argument_list|)
decl_stmt|;
name|BindingProvider
name|bp
init|=
operator|(
name|BindingProvider
operator|)
name|handler
decl_stmt|;
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
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
decl_stmt|;
name|HTTPConduit
name|c
init|=
operator|(
name|HTTPConduit
operator|)
name|client
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|c
operator|.
name|setClient
argument_list|(
operator|new
name|HTTPClientPolicy
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|.
name|getClient
argument_list|()
operator|.
name|setConnection
argument_list|(
name|ConnectionType
operator|.
name|CLOSE
argument_list|)
expr_stmt|;
comment|// invoke twoway call
name|greeter
operator|.
name|sayHi
argument_list|()
expr_stmt|;
block|}
specifier|private
name|Endpoint
name|createService
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|Object
name|impl
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
return|return
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|impl
argument_list|)
return|;
block|}
block|}
end_class

end_unit

