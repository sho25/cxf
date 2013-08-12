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
name|BusException
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
name|buslifecycle
operator|.
name|BusLifeCycleManager
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
name|ServerRegistry
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
name|management
operator|.
name|InstrumentationManager
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
name|PhaseManager
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
name|ConduitInitiatorManager
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
name|workqueue
operator|.
name|WorkQueueManager
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
name|WSDLManager
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
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
specifier|public
class|class
name|SpringBusFactoryTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testKnownExtensions
parameter_list|()
throws|throws
name|BusException
block|{
name|Bus
name|bus
init|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|checkBindingExtensions
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|DestinationFactoryManager
name|dfm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No destination factory manager"
argument_list|,
name|dfm
argument_list|)
expr_stmt|;
name|ConduitInitiatorManager
name|cim
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ConduitInitiatorManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No conduit initiator manager"
argument_list|,
name|cim
argument_list|)
expr_stmt|;
name|checkTransportFactories
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|checkOtherCoreExtensions
argument_list|(
name|bus
argument_list|)
expr_stmt|;
comment|//you should include instumentation extenstion to get the instrumentation manager
name|assertNotNull
argument_list|(
literal|"No instrumentation manager"
argument_list|,
name|bus
operator|.
name|getExtension
argument_list|(
name|InstrumentationManager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadBusWithServletApplicationContext
parameter_list|()
throws|throws
name|BusException
block|{
name|ClassPathXmlApplicationContext
name|ctx
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"/org/apache/cxf/systest/bus/servlet.xml"
block|}
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
operator|new
name|SpringBusFactory
argument_list|(
name|ctx
argument_list|)
operator|.
name|createBus
argument_list|()
decl_stmt|;
name|checkBindingExtensions
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|checkHTTPTransportFactories
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|checkOtherCoreExtensions
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadBusWithApplicationContext
parameter_list|()
throws|throws
name|BusException
block|{
name|ClassPathXmlApplicationContext
name|ctx
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"/org/apache/cxf/systest/bus/basic.xml"
block|}
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"cxf"
argument_list|,
name|Bus
operator|.
name|class
argument_list|)
decl_stmt|;
name|ctx
operator|.
name|refresh
argument_list|()
expr_stmt|;
name|bus
operator|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"cxf"
argument_list|,
name|Bus
operator|.
name|class
argument_list|)
expr_stmt|;
name|checkBindingExtensions
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|checkHTTPTransportFactories
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|checkOtherCoreExtensions
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|checkBindingExtensions
parameter_list|(
name|Bus
name|bus
parameter_list|)
throws|throws
name|BusException
block|{
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
name|assertNotNull
argument_list|(
literal|"No binding factory manager"
argument_list|,
name|bfm
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"binding factory not available"
argument_list|,
name|bfm
operator|.
name|getBindingFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|bfm
operator|.
name|getBindingFactory
argument_list|(
literal|"http://cxf.apache.org/unknown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BusException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
specifier|private
name|void
name|checkOtherCoreExtensions
parameter_list|(
name|Bus
name|bus
parameter_list|)
throws|throws
name|BusException
block|{
name|assertNotNull
argument_list|(
literal|"No wsdl manager"
argument_list|,
name|bus
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No phase manager"
argument_list|,
name|bus
operator|.
name|getExtension
argument_list|(
name|PhaseManager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No workqueue manager"
argument_list|,
name|bus
operator|.
name|getExtension
argument_list|(
name|WorkQueueManager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No lifecycle manager"
argument_list|,
name|bus
operator|.
name|getExtension
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No service registry"
argument_list|,
name|bus
operator|.
name|getExtension
argument_list|(
name|ServerRegistry
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkHTTPTransportFactories
parameter_list|(
name|Bus
name|bus
parameter_list|)
throws|throws
name|BusException
block|{
name|ConduitInitiatorManager
name|cim
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ConduitInitiatorManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No conduit initiator manager"
argument_list|,
name|cim
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"conduit initiator not available"
argument_list|,
name|cim
operator|.
name|getConduitInitiator
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/http"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"conduit initiator not available"
argument_list|,
name|cim
operator|.
name|getConduitInitiator
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/http/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"conduit initiator not available"
argument_list|,
name|cim
operator|.
name|getConduitInitiator
argument_list|(
literal|"http://cxf.apache.org/transports/http/configuration"
argument_list|)
argument_list|)
expr_stmt|;
name|DestinationFactoryManager
name|dfm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No destination factory manager"
argument_list|,
name|dfm
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"destination factory not available"
argument_list|,
name|dfm
operator|.
name|getDestinationFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"destination factory not available"
argument_list|,
name|dfm
operator|.
name|getDestinationFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/http"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"destination factory not available"
argument_list|,
name|dfm
operator|.
name|getDestinationFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/http/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"destination factory not available"
argument_list|,
name|dfm
operator|.
name|getDestinationFactory
argument_list|(
literal|"http://cxf.apache.org/transports/http/configuration"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkTransportFactories
parameter_list|(
name|Bus
name|bus
parameter_list|)
throws|throws
name|BusException
block|{
name|DestinationFactoryManager
name|dfm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No destination factory manager"
argument_list|,
name|dfm
argument_list|)
expr_stmt|;
name|ConduitInitiatorManager
name|cim
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ConduitInitiatorManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No conduit initiator manager"
argument_list|,
name|cim
argument_list|)
expr_stmt|;
try|try
block|{
name|cim
operator|.
name|getConduitInitiator
argument_list|(
literal|"http://cxf.apache.org/unknown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BusException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
try|try
block|{
name|dfm
operator|.
name|getDestinationFactory
argument_list|(
literal|"http://cxf.apache.org/unknown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BusException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
comment|// not sure that we need this - Dan Diephouse
comment|//assertNotNull("conduit initiator not available",
comment|//cim.getConduitInitiator("http://schemas.xmlsoap.org/wsdl/soap/"));
name|assertNotNull
argument_list|(
literal|"conduit initiator not available"
argument_list|,
name|cim
operator|.
name|getConduitInitiator
argument_list|(
literal|"http://cxf.apache.org/transports/http"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"conduit initiator not available"
argument_list|,
name|cim
operator|.
name|getConduitInitiator
argument_list|(
literal|"http://cxf.apache.org/transports/jms"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"conduit initiator not available"
argument_list|,
name|cim
operator|.
name|getConduitInitiator
argument_list|(
literal|"http://cxf.apache.org/transports/jms/configuration"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"destination factory not available"
argument_list|,
name|dfm
operator|.
name|getDestinationFactory
argument_list|(
literal|"http://cxf.apache.org/transports/http"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"destination factory not available"
argument_list|,
name|dfm
operator|.
name|getDestinationFactory
argument_list|(
literal|"http://cxf.apache.org/transports/jms"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"destination factory not available"
argument_list|,
name|dfm
operator|.
name|getDestinationFactory
argument_list|(
literal|"http://cxf.apache.org/transports/jms/configuration"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

