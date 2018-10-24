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
name|ws
operator|.
name|rm
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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
name|soap
operator|.
name|AddressingFeature
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
name|soap
operator|.
name|SOAPFaultException
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
name|ext
operator|.
name|logging
operator|.
name|LoggingFeature
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
name|greeter_control
operator|.
name|Control
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
name|greeter_control
operator|.
name|ControlService
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
name|greeter_control
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
name|cxf
operator|.
name|greeter_control
operator|.
name|GreeterService
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
name|systest
operator|.
name|ws
operator|.
name|util
operator|.
name|ConnectionHelper
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
name|ws
operator|.
name|rm
operator|.
name|RM11Constants
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
name|rm
operator|.
name|RMManager
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
name|rm
operator|.
name|feature
operator|.
name|RMFeature
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
name|rm
operator|.
name|manager
operator|.
name|AcksPolicyType
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
name|rm
operator|.
name|manager
operator|.
name|DestinationPolicyType
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
name|rm
operator|.
name|persistence
operator|.
name|jdbc
operator|.
name|RMTxStore
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
name|rmp
operator|.
name|v200502
operator|.
name|RMAssertion
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|WSRM12ServerCycleTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|WSRM12ServerCycleTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CFG_PERSISTENT
init|=
literal|"/org/apache/cxf/systest/ws/rm/persistent.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CFG_SIMPLE
init|=
literal|"/org/apache/cxf/systest/ws/rm/simple.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|DEFAULT_BASE_RETRANSMISSION_INTERVAL
init|=
literal|4000L
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|DEFAULT_ACKNOWLEDGEMENT_INTERVAL
init|=
literal|2000L
decl_stmt|;
specifier|public
specifier|static
class|class
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
name|String
name|port
decl_stmt|;
name|String
name|pfx
decl_stmt|;
name|Endpoint
name|ep
decl_stmt|;
specifier|public
name|Server
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|port
operator|=
name|args
index|[
literal|0
index|]
expr_stmt|;
name|pfx
operator|=
name|args
index|[
literal|1
index|]
expr_stmt|;
block|}
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|SpringBusFactory
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|factory
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
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
comment|//System.out.println("Created control bus " + bus);
name|ControlImpl
name|implementor
init|=
operator|new
name|ControlImpl
argument_list|()
decl_stmt|;
name|implementor
operator|.
name|setDbName
argument_list|(
name|pfx
operator|+
literal|"-server"
argument_list|)
expr_stmt|;
name|implementor
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/SoapContext/GreeterPort"
argument_list|)
expr_stmt|;
name|GreeterImpl
name|greeterImplementor
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|implementor
operator|.
name|setImplementor
argument_list|(
name|greeterImplementor
argument_list|)
expr_stmt|;
name|ep
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/SoapContext/ControlPort"
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|tearDown
parameter_list|()
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
operator|new
name|Server
argument_list|(
name|args
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|setUpBeforeClass
parameter_list|()
throws|throws
name|Exception
block|{
name|RMTxStore
operator|.
name|deleteDatabaseFiles
argument_list|(
literal|"cxf7392-server"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
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
literal|null
argument_list|,
operator|new
name|String
index|[]
block|{
name|PORT
block|,
literal|"cxf7392"
block|}
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getPrefix
parameter_list|()
block|{
return|return
literal|"cxf7392"
return|;
block|}
specifier|public
specifier|static
name|RMFeature
name|wsrm
parameter_list|()
block|{
return|return
name|wsrm
argument_list|(
name|DEFAULT_BASE_RETRANSMISSION_INTERVAL
argument_list|,
name|DEFAULT_ACKNOWLEDGEMENT_INTERVAL
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|RMFeature
name|wsrm
parameter_list|(
name|long
name|brtxInterval
parameter_list|,
name|long
name|ackInterval
parameter_list|)
block|{
name|RMAssertion
operator|.
name|BaseRetransmissionInterval
name|baseRetransmissionInterval
init|=
operator|new
name|RMAssertion
operator|.
name|BaseRetransmissionInterval
argument_list|()
decl_stmt|;
name|baseRetransmissionInterval
operator|.
name|setMilliseconds
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
name|brtxInterval
argument_list|)
argument_list|)
expr_stmt|;
name|RMAssertion
operator|.
name|AcknowledgementInterval
name|acknowledgementInterval
init|=
operator|new
name|RMAssertion
operator|.
name|AcknowledgementInterval
argument_list|()
decl_stmt|;
name|acknowledgementInterval
operator|.
name|setMilliseconds
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
name|ackInterval
argument_list|)
argument_list|)
expr_stmt|;
name|RMAssertion
name|rmAssertion
init|=
operator|new
name|RMAssertion
argument_list|()
decl_stmt|;
name|rmAssertion
operator|.
name|setAcknowledgementInterval
argument_list|(
name|acknowledgementInterval
argument_list|)
expr_stmt|;
name|rmAssertion
operator|.
name|setBaseRetransmissionInterval
argument_list|(
name|baseRetransmissionInterval
argument_list|)
expr_stmt|;
name|AcksPolicyType
name|acksPolicy
init|=
operator|new
name|AcksPolicyType
argument_list|()
decl_stmt|;
name|acksPolicy
operator|.
name|setIntraMessageThreshold
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|DestinationPolicyType
name|destinationPolicy
init|=
operator|new
name|DestinationPolicyType
argument_list|()
decl_stmt|;
name|destinationPolicy
operator|.
name|setAcksPolicy
argument_list|(
name|acksPolicy
argument_list|)
expr_stmt|;
name|RMFeature
name|feature
init|=
operator|new
name|RMFeature
argument_list|()
decl_stmt|;
name|feature
operator|.
name|setRMAssertion
argument_list|(
name|rmAssertion
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setDestinationPolicy
argument_list|(
name|destinationPolicy
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setRMNamespace
argument_list|(
name|RM11Constants
operator|.
name|NAMESPACE_URI
argument_list|)
expr_stmt|;
return|return
name|feature
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPersistentSequences
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
name|CFG_PERSISTENT
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNonPersistentSequence
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
name|CFG_SIMPLE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNonPersistentSequenceNoTransformer
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
comment|//CXF-7392
name|System
operator|.
name|setProperty
argument_list|(
literal|"javax.xml.transform.TransformerFactory"
argument_list|,
literal|"foo.snarf"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
name|CFG_SIMPLE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|clearProperty
argument_list|(
literal|"javax.xml.transform.TransformerFactory"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|runTest
parameter_list|(
name|String
name|cfg
parameter_list|,
name|boolean
name|faultOnRestart
parameter_list|)
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|bus
operator|=
name|bf
operator|.
name|createBus
argument_list|()
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|ControlService
name|cs
init|=
operator|new
name|ControlService
argument_list|()
decl_stmt|;
name|Control
name|control
init|=
name|cs
operator|.
name|getControlPort
argument_list|()
decl_stmt|;
name|ConnectionHelper
operator|.
name|setKeepAliveConnection
argument_list|(
name|control
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|updateAddressPort
argument_list|(
name|control
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Failed to start greeter"
argument_list|,
name|control
operator|.
name|startGreeter
argument_list|(
name|cfg
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"db.name"
argument_list|,
name|getPrefix
argument_list|()
operator|+
literal|"-recovery"
argument_list|)
expr_stmt|;
name|Bus
name|greeterBus
init|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|()
decl_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"db.name"
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|greeterBus
argument_list|)
expr_stmt|;
comment|// avoid early client resends
name|greeterBus
operator|.
name|getExtension
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setBaseRetransmissionInterval
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|60000
argument_list|)
argument_list|)
expr_stmt|;
name|GreeterService
name|gs
init|=
operator|new
name|GreeterService
argument_list|()
decl_stmt|;
name|Greeter
name|greeter
init|=
name|gs
operator|.
name|getGreeterPort
argument_list|(
operator|new
name|LoggingFeature
argument_list|()
argument_list|,
operator|new
name|AddressingFeature
argument_list|()
argument_list|,
name|wsrm
argument_list|()
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"one"
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"two"
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"three"
argument_list|)
expr_stmt|;
name|control
operator|.
name|stopGreeter
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
comment|//make sure greeter is down
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|control
operator|.
name|startGreeter
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
comment|//CXF-7392
if|if
condition|(
name|faultOnRestart
condition|)
block|{
try|try
block|{
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"four"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"wsrm:Identifier"
argument_list|)
argument_list|)
expr_stmt|;
comment|//expected, sequence identifier doesn't exist on other side
block|}
block|}
else|else
block|{
comment|// this should work as the sequence should be recovered on the server side
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"four"
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|Closeable
operator|)
name|greeter
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|greeterBus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|stopGreeter
argument_list|(
name|cfg
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
block|}
end_class

end_unit

