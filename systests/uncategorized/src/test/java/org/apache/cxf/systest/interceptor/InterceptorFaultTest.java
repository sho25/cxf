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
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|Collections
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
name|logging
operator|.
name|Logger
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
name|WebServiceException
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|ext
operator|.
name|logging
operator|.
name|LoggingInInterceptor
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
name|event
operator|.
name|PrintWriterEventSender
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
name|ControlImpl
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
name|FaultThrowingInterceptor
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
name|greeter_control
operator|.
name|PingMeFault
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
name|types
operator|.
name|FaultLocation
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
name|phase
operator|.
name|Phase
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
name|PhaseComparator
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
name|HTTPClientPolicy
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
name|MAPAggregator
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
name|AfterClass
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
name|assertTrue
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
name|fail
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|InterceptorFaultTest
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
name|Server
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|InterceptorFaultTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|SOAP_FAULT_CODE
init|=
operator|new
name|QName
argument_list|(
literal|"http://schemas.xmlsoap.org/soap/envelope/"
argument_list|,
literal|"Server"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FAULT_MESSAGE
init|=
literal|"Could not send Message."
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CONTROL_PORT_ADDRESS
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/ControlPort"
decl_stmt|;
specifier|private
specifier|static
name|int
name|decoupledEndpointPort
init|=
literal|1
decl_stmt|;
specifier|private
specifier|static
name|String
name|decoupledEndpoint
decl_stmt|;
comment|/**      * Tests that a fault thrown by a server side interceptor is reported back to      * the client in appropriate form (plain Fault in case of one way requests,      * SoapFault in case of two way requests).      * Also demonstrates how an interceptor on the server out fault chain can      * distinguish different fault modes (the capability to do so is crucial to      * QOS interceptors such as the RM, addressing and policy interceptors).      *      */
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
name|ControlImpl
name|implementor
init|=
operator|new
name|ControlImpl
argument_list|()
decl_stmt|;
name|implementor
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
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
name|greeterImplementor
operator|.
name|setThrowAlways
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|greeterImplementor
operator|.
name|useLastOnewayArg
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
name|CONTROL_PORT_ADDRESS
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Published control endpoint."
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
specifier|private
name|Bus
name|controlBus
decl_stmt|;
specifier|private
name|Control
name|control
decl_stmt|;
specifier|private
name|Bus
name|greeterBus
decl_stmt|;
specifier|private
name|Greeter
name|greeter
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Phase
argument_list|>
name|inPhases
decl_stmt|;
specifier|private
name|PhaseComparator
name|comparator
decl_stmt|;
specifier|private
name|Phase
name|postUnMarshalPhase
decl_stmt|;
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
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.apache.cxf.transports.http_jetty.DontClosePort."
operator|+
name|PORT
argument_list|,
literal|"true"
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
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|createStaticBus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|reset
parameter_list|()
block|{
name|System
operator|.
name|clearProperty
argument_list|(
literal|"org.apache.cxf.transports.http_jetty.DontClosePort."
operator|+
name|PORT
argument_list|)
expr_stmt|;
name|Bus
name|b
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|b
operator|==
literal|null
condition|)
block|{
name|b
operator|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|b
operator|==
literal|null
condition|)
block|{
name|b
operator|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
expr_stmt|;
block|}
name|b
operator|.
name|shutdown
argument_list|(
literal|true
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
block|{
if|if
condition|(
literal|null
operator|!=
name|greeter
condition|)
block|{
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|greeter
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed to stop greeter."
argument_list|,
name|control
operator|.
name|stopGreeter
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|greeterBus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|greeterBus
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|control
condition|)
block|{
name|assertTrue
argument_list|(
literal|"Failed to stop greeter"
argument_list|,
name|control
operator|.
name|stopGreeter
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|control
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|controlBus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithoutAddressing
parameter_list|()
throws|throws
name|Exception
block|{
name|testWithoutAddressing
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRobustWithoutAddressing
parameter_list|()
throws|throws
name|Exception
block|{
name|testWithoutAddressing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRobustFailWithoutAddressingInUserLogicalPhase
parameter_list|()
throws|throws
name|Exception
block|{
name|setupGreeter
argument_list|(
literal|"org/apache/cxf/systest/interceptor/no-addr.xml"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|control
operator|.
name|setRobustInOnlyMode
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// behaviour is identicial for all phases
name|FaultLocation
name|location
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|greeter_control
operator|.
name|types
operator|.
name|ObjectFactory
argument_list|()
operator|.
name|createFaultLocation
argument_list|()
decl_stmt|;
name|location
operator|.
name|setPhase
argument_list|(
literal|"user-logical"
argument_list|)
expr_stmt|;
name|control
operator|.
name|setFaultLocation
argument_list|(
name|location
argument_list|)
expr_stmt|;
try|try
block|{
comment|// writer to grab the content of soap fault.
comment|// robust is not yet used at client's side, but I think it should
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
operator|(
operator|(
name|Client
operator|)
name|greeter
operator|)
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|(
operator|new
name|PrintWriterEventSender
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|writer
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// it should tell CXF to convert one-way robust out faults into real SoapFaultException
operator|(
operator|(
name|Client
operator|)
name|greeter
operator|)
operator|.
name|getEndpoint
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ROBUST_ONEWAY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"oneway"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Oneway operation unexpectedly succeded for phase "
operator|+
name|location
operator|.
name|getPhase
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|ex
parameter_list|)
block|{
comment|//expected
block|}
block|}
specifier|private
name|void
name|testWithoutAddressing
parameter_list|(
name|boolean
name|robust
parameter_list|)
throws|throws
name|Exception
block|{
name|setupGreeter
argument_list|(
literal|"org/apache/cxf/systest/interceptor/no-addr.xml"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|control
operator|.
name|setRobustInOnlyMode
argument_list|(
name|robust
argument_list|)
expr_stmt|;
comment|// all interceptors pass
name|testInterceptorsPass
argument_list|(
name|robust
argument_list|)
expr_stmt|;
comment|// behaviour is identicial for all phases
name|FaultLocation
name|location
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|greeter_control
operator|.
name|types
operator|.
name|ObjectFactory
argument_list|()
operator|.
name|createFaultLocation
argument_list|()
decl_stmt|;
comment|// test failure occuring before and after logical addressing interceptor
comment|// won't get a fault in case of oneways non-robust for the latter (partial response already sent)
name|testInterceptorFail
argument_list|(
name|inPhases
argument_list|,
name|location
argument_list|,
name|robust
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithAddressingAnonymousReplies
parameter_list|()
throws|throws
name|Exception
block|{
name|testWithAddressingAnonymousReplies
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRobustWithAddressingAnonymousReplies
parameter_list|()
throws|throws
name|Exception
block|{
name|testWithAddressingAnonymousReplies
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testWithAddressingAnonymousReplies
parameter_list|(
name|boolean
name|robust
parameter_list|)
throws|throws
name|Exception
block|{
name|setupGreeter
argument_list|(
literal|"org/apache/cxf/systest/interceptor/addr.xml"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|control
operator|.
name|setRobustInOnlyMode
argument_list|(
name|robust
argument_list|)
expr_stmt|;
comment|// all interceptors pass
name|testInterceptorsPass
argument_list|(
name|robust
argument_list|)
expr_stmt|;
comment|// test failure in phases<= Phase.UNMARSHALL
name|FaultLocation
name|location
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|greeter_control
operator|.
name|types
operator|.
name|ObjectFactory
argument_list|()
operator|.
name|createFaultLocation
argument_list|()
decl_stmt|;
name|location
operator|.
name|setAfter
argument_list|(
name|MAPAggregator
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// test failure occuring before and after logical addressing interceptor
comment|// won't get a fault in case of oneways non-robust for the latter (partial response already sent)
name|testInterceptorFail
argument_list|(
name|inPhases
argument_list|,
name|location
argument_list|,
name|robust
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testInterceptorFail
parameter_list|(
name|List
argument_list|<
name|Phase
argument_list|>
name|phases
parameter_list|,
name|FaultLocation
name|location
parameter_list|,
name|boolean
name|robust
parameter_list|)
throws|throws
name|PingMeFault
block|{
for|for
control|(
name|Phase
name|p
range|:
name|phases
control|)
block|{
name|location
operator|.
name|setPhase
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|Phase
operator|.
name|POST_INVOKE
operator|.
name|equals
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
break|break;
block|}
name|testFail
argument_list|(
name|location
argument_list|,
literal|true
argument_list|,
name|robust
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|testInterceptorsPass
parameter_list|(
name|boolean
name|robust
parameter_list|)
block|{
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"one"
argument_list|)
expr_stmt|;
comment|// wait 5 seconds for the non-robust case
if|if
condition|(
operator|!
name|robust
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
comment|// verify both the previous greetMeOneWay call and this greetMe call
name|assertEquals
argument_list|(
literal|"one"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"two"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|greeter
operator|.
name|pingMe
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PingMeFault not thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PingMeFault
name|f
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|f
operator|.
name|getFaultInfo
argument_list|()
operator|.
name|getMajor
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|f
operator|.
name|getFaultInfo
argument_list|()
operator|.
name|getMinor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|testFail
parameter_list|(
name|FaultLocation
name|location
parameter_list|,
name|boolean
name|usingAddressing
parameter_list|,
name|boolean
name|robust
parameter_list|)
throws|throws
name|PingMeFault
block|{
comment|// System.out.print("Test interceptor failing in phase: " + location.getPhase());
name|control
operator|.
name|setFaultLocation
argument_list|(
name|location
argument_list|)
expr_stmt|;
comment|// oneway reports a plain fault (although server sends a soap fault)
name|boolean
name|expectOnewayFault
init|=
name|robust
decl_stmt|;
if|if
condition|(
name|comparator
operator|.
name|compare
argument_list|(
name|getPhase
argument_list|(
name|location
operator|.
name|getPhase
argument_list|()
argument_list|)
argument_list|,
name|postUnMarshalPhase
argument_list|)
operator|<
literal|0
condition|)
block|{
name|expectOnewayFault
operator|=
literal|true
expr_stmt|;
block|}
try|try
block|{
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"oneway"
argument_list|)
expr_stmt|;
if|if
condition|(
name|expectOnewayFault
condition|)
block|{
name|fail
argument_list|(
literal|"Oneway operation unexpectedly succeded for phase "
operator|+
name|location
operator|.
name|getPhase
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|ex
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expectOnewayFault
condition|)
block|{
name|fail
argument_list|(
literal|"Oneway operation unexpectedly failed."
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|FAULT_MESSAGE
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|expectedMsg
init|=
name|getExpectedInterceptorFaultMessage
argument_list|(
name|location
operator|.
name|getPhase
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"cxf"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Twoway operation unexpectedly succeded."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|ex
parameter_list|)
block|{
name|Throwable
name|cause
init|=
name|ex
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|SoapFault
name|sf
init|=
operator|(
name|SoapFault
operator|)
name|cause
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedMsg
argument_list|,
name|sf
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SOAP_FAULT_CODE
argument_list|,
name|sf
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|greeter
operator|.
name|pingMe
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PingMeFault not thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|ex
parameter_list|)
block|{
name|Throwable
name|cause
init|=
name|ex
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|SoapFault
name|sf
init|=
operator|(
name|SoapFault
operator|)
name|cause
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedMsg
argument_list|,
name|sf
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SOAP_FAULT_CODE
argument_list|,
name|sf
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|setupGreeter
parameter_list|(
name|String
name|cfgResource
parameter_list|,
name|boolean
name|useDecoupledEndpoint
parameter_list|)
throws|throws
name|NumberFormatException
throws|,
name|MalformedURLException
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|controlBus
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
name|controlBus
argument_list|)
expr_stmt|;
name|ControlService
name|cs
init|=
operator|new
name|ControlService
argument_list|()
decl_stmt|;
name|control
operator|=
name|cs
operator|.
name|getControlPort
argument_list|()
expr_stmt|;
name|updateAddressPort
argument_list|(
name|control
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed to start greeter"
argument_list|,
name|control
operator|.
name|startGreeter
argument_list|(
name|cfgResource
argument_list|)
argument_list|)
expr_stmt|;
name|greeterBus
operator|=
name|bf
operator|.
name|createBus
argument_list|(
name|cfgResource
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|greeterBus
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Initialised greeter bus with configuration: "
operator|+
name|cfgResource
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|==
name|comparator
condition|)
block|{
name|comparator
operator|=
operator|new
name|PhaseComparator
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|==
name|inPhases
condition|)
block|{
name|inPhases
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|inPhases
operator|.
name|addAll
argument_list|(
name|greeterBus
operator|.
name|getExtension
argument_list|(
name|PhaseManager
operator|.
name|class
argument_list|)
operator|.
name|getInPhases
argument_list|()
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|inPhases
argument_list|,
name|comparator
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|==
name|postUnMarshalPhase
condition|)
block|{
name|postUnMarshalPhase
operator|=
name|getPhase
argument_list|(
name|Phase
operator|.
name|POST_UNMARSHAL
argument_list|)
expr_stmt|;
block|}
name|GreeterService
name|gs
init|=
operator|new
name|GreeterService
argument_list|()
decl_stmt|;
name|greeter
operator|=
name|gs
operator|.
name|getGreeterPort
argument_list|()
expr_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Created greeter client."
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|useDecoupledEndpoint
condition|)
block|{
return|return;
block|}
comment|// programatically configure decoupled endpoint that is guaranteed to
comment|// be unique across all test cases
name|decoupledEndpointPort
operator|++
expr_stmt|;
name|decoupledEndpoint
operator|=
literal|"http://localhost:"
operator|+
name|allocatePort
argument_list|(
literal|"decoupled-"
operator|+
name|decoupledEndpointPort
argument_list|)
operator|+
literal|"/decoupled_endpoint"
expr_stmt|;
name|Client
name|c
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
decl_stmt|;
name|HTTPConduit
name|hc
init|=
call|(
name|HTTPConduit
call|)
argument_list|(
name|c
operator|.
name|getConduit
argument_list|()
argument_list|)
decl_stmt|;
name|HTTPClientPolicy
name|cp
init|=
name|hc
operator|.
name|getClient
argument_list|()
decl_stmt|;
name|cp
operator|.
name|setDecoupledEndpoint
argument_list|(
name|decoupledEndpoint
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Using decoupled endpoint: "
operator|+
name|cp
operator|.
name|getDecoupledEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getExpectedInterceptorFaultMessage
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
return|return
name|FaultThrowingInterceptor
operator|.
name|MESSAGE_FORMAT
operator|.
name|format
argument_list|(
operator|new
name|Object
index|[]
block|{
name|phase
block|}
argument_list|)
operator|.
name|toUpperCase
argument_list|()
return|;
block|}
specifier|private
name|Phase
name|getPhase
parameter_list|(
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|Phase
name|p
range|:
name|inPhases
control|)
block|{
if|if
condition|(
name|p
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|p
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

