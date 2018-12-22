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
name|stax_transform_feature
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
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
name|LoggingOutInterceptor
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
name|LogEvent
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
name|LogEventSender
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
name|interceptor
operator|.
name|transform
operator|.
name|TransformInInterceptor
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
name|transform
operator|.
name|TransformOutInterceptor
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
name|interceptor
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
comment|/**  * Tests whether the stax transformer is correctly engaged and it does not interfere with logging.  * This test uses a simple transformation. More complex transformation tests are found in the api package.  *  */
end_comment

begin_class
specifier|public
class|class
name|StaxTransformFeatureTest
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
name|StaxTransformFeatureTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|GREETER_PORT_ADDRESS
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/GreeterPort"
decl_stmt|;
specifier|private
specifier|static
name|TestLoggingEventSender
name|serverlogIn
init|=
operator|new
name|TestLoggingEventSender
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|TestLoggingEventSender
name|serverlogOut
init|=
operator|new
name|TestLoggingEventSender
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|TransformInInterceptor
name|servertransIn
init|=
operator|new
name|TransformInInterceptor
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|TransformOutInterceptor
name|servertransOut
init|=
operator|new
name|TransformOutInterceptor
argument_list|()
decl_stmt|;
specifier|private
name|Greeter
name|greeter
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
name|bus
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|(
name|serverlogIn
argument_list|)
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|(
name|serverlogOut
argument_list|)
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|(
name|serverlogOut
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|inElements
operator|.
name|put
argument_list|(
literal|"{http://cxf.apache.org/greeter_control/types}dontPingMe"
argument_list|,
literal|"{http://cxf.apache.org/greeter_control/types}pingMe"
argument_list|)
expr_stmt|;
name|servertransIn
operator|.
name|setInTransformElements
argument_list|(
name|inElements
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|servertransIn
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|outElements
operator|.
name|put
argument_list|(
literal|"{http://cxf.apache.org/greeter_control/types}faultDetail"
argument_list|,
literal|"{http://cxf.apache.org/greeter_control/types}noFaultDetail"
argument_list|)
expr_stmt|;
name|servertransOut
operator|.
name|setOutTransformElements
argument_list|(
name|outElements
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|servertransOut
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|servertransOut
argument_list|)
expr_stmt|;
name|GreeterImpl
name|implementor
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|ep
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|GREETER_PORT_ADDRESS
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
comment|// force the info logging for this test
name|LOG
operator|.
name|setLevel
argument_list|(
name|Level
operator|.
name|INFO
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
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|reset
parameter_list|()
block|{
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
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTransformWithLogging
parameter_list|()
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
name|TestLoggingEventSender
name|logIn
init|=
operator|new
name|TestLoggingEventSender
argument_list|()
decl_stmt|;
name|bus
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|(
name|logIn
argument_list|)
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|(
name|logIn
argument_list|)
argument_list|)
expr_stmt|;
name|TestLoggingEventSender
name|logOut
init|=
operator|new
name|TestLoggingEventSender
argument_list|()
decl_stmt|;
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|(
name|logOut
argument_list|)
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|(
name|logOut
argument_list|)
argument_list|)
expr_stmt|;
name|TransformInInterceptor
name|transIn
init|=
operator|new
name|TransformInInterceptor
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|inElements
operator|.
name|put
argument_list|(
literal|"{http://cxf.apache.org/greeter_control/types}noFaultDetail"
argument_list|,
literal|"{http://cxf.apache.org/greeter_control/types}faultDetail"
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|transIn
argument_list|)
expr_stmt|;
name|TransformOutInterceptor
name|transOut
init|=
operator|new
name|TransformOutInterceptor
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|outElements
operator|.
name|put
argument_list|(
literal|"{http://cxf.apache.org/greeter_control/types}pingMe"
argument_list|,
literal|"{http://cxf.apache.org/greeter_control/types}dontPingMe"
argument_list|)
expr_stmt|;
name|transOut
operator|.
name|setOutTransformElements
argument_list|(
name|outElements
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|transOut
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|transOut
argument_list|)
expr_stmt|;
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
comment|// ping 1: request-response transformation
name|greeter
operator|.
name|pingMe
argument_list|()
expr_stmt|;
name|verifyPayload
argument_list|(
name|logOut
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"dontPingMe"
argument_list|)
expr_stmt|;
name|verifyPayload
argument_list|(
name|logIn
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"pingMeResponse"
argument_list|)
expr_stmt|;
name|verifyPayload
argument_list|(
name|serverlogIn
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"dontPingMe"
argument_list|)
expr_stmt|;
name|verifyPayload
argument_list|(
name|serverlogOut
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"pingMeResponse"
argument_list|)
expr_stmt|;
name|serverlogOut
operator|.
name|cleaerMessage
argument_list|()
expr_stmt|;
name|serverlogIn
operator|.
name|cleaerMessage
argument_list|()
expr_stmt|;
name|logOut
operator|.
name|cleaerMessage
argument_list|()
expr_stmt|;
name|logIn
operator|.
name|cleaerMessage
argument_list|()
expr_stmt|;
comment|// ping 2: request-fault transformation
try|try
block|{
name|greeter
operator|.
name|pingMe
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Ping should have failed"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Pings succeed only every other time."
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|verifyPayload
argument_list|(
name|logOut
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"dontPingMe"
argument_list|)
expr_stmt|;
name|verifyPayload
argument_list|(
name|logIn
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"noFaultDetail"
argument_list|)
expr_stmt|;
name|verifyPayload
argument_list|(
name|serverlogIn
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"dontPingMe"
argument_list|)
expr_stmt|;
name|verifyPayload
argument_list|(
name|serverlogOut
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"noFaultDetail"
argument_list|)
expr_stmt|;
comment|// ping 3: idle
name|greeter
operator|.
name|pingMe
argument_list|()
expr_stmt|;
name|serverlogOut
operator|.
name|cleaerMessage
argument_list|()
expr_stmt|;
name|serverlogIn
operator|.
name|cleaerMessage
argument_list|()
expr_stmt|;
name|logOut
operator|.
name|cleaerMessage
argument_list|()
expr_stmt|;
name|logIn
operator|.
name|cleaerMessage
argument_list|()
expr_stmt|;
comment|// ping 4: request-fault transformation with skipOnFault
name|transOut
operator|.
name|setSkipOnFault
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|servertransOut
operator|.
name|setSkipOnFault
argument_list|(
literal|true
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
literal|"Ping should have failed"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Pings succeed only every other time."
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|verifyPayload
argument_list|(
name|logOut
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"dontPingMe"
argument_list|)
expr_stmt|;
name|verifyPayload
argument_list|(
name|logIn
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"faultDetail"
argument_list|)
expr_stmt|;
name|verifyPayload
argument_list|(
name|serverlogIn
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"dontPingMe"
argument_list|)
expr_stmt|;
name|verifyPayload
argument_list|(
name|serverlogOut
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"faultDetail"
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
specifier|private
name|void
name|verifyPayload
parameter_list|(
name|String
name|m
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"message not logged"
argument_list|,
name|m
argument_list|)
expr_stmt|;
comment|// the entire soap envelope is logged
name|assertTrue
argument_list|(
name|m
argument_list|,
name|m
operator|.
name|indexOf
argument_list|(
literal|"<soap:Envelope"
argument_list|)
operator|>=
literal|0
operator|&&
name|m
operator|.
name|indexOf
argument_list|(
literal|"</soap:Envelope>"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
comment|// the transformed body is logged
name|assertTrue
argument_list|(
name|value
operator|+
literal|" must be found in payload: "
operator|+
name|m
argument_list|,
name|m
operator|.
name|indexOf
argument_list|(
name|value
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|static
class|class
name|TestLoggingEventSender
implements|implements
name|LogEventSender
block|{
specifier|private
name|String
name|logMessage
decl_stmt|;
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|logMessage
return|;
block|}
specifier|public
name|void
name|cleaerMessage
parameter_list|()
block|{
name|logMessage
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|send
parameter_list|(
name|LogEvent
name|event
parameter_list|)
block|{
name|logMessage
operator|=
name|event
operator|.
name|getPayload
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

