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
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * Tests the addition of WS-RM properties to application messages and the  * exchange of WS-RM protocol messages.  */
end_comment

begin_class
specifier|public
class|class
name|DecoupledClientServerOnewayTest
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
specifier|public
specifier|static
specifier|final
name|String
name|DECOUPLE_PORT
init|=
name|allocatePort
argument_list|(
name|DecoupledClientServerOnewayTest
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
name|DecoupledClientServerOnewayTest
operator|.
name|class
argument_list|)
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
argument_list|(
literal|"/org/apache/cxf/systest/ws/rm/exactlyonce-inorder-decoupled.xml"
argument_list|)
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
name|LoggingInInterceptor
name|in
init|=
operator|new
name|LoggingInInterceptor
argument_list|()
decl_stmt|;
name|in
operator|.
name|setPrettyLogging
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|LoggingOutInterceptor
name|out
init|=
operator|new
name|LoggingOutInterceptor
argument_list|()
decl_stmt|;
name|out
operator|.
name|setPrettyLogging
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|GreeterImpl
name|implementor
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|implementor
operator|.
name|useLastOnewayArg
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|implementor
operator|.
name|setDelay
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/GreeterPort"
decl_stmt|;
name|ep
operator|=
name|Endpoint
operator|.
name|create
argument_list|(
name|implementor
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|Message
operator|.
name|SCHEMA_VALIDATION_ENABLED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|ep
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|ep
operator|.
name|publish
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Published greeter endpoint."
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDecoupledOneway
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
name|bus
operator|=
name|bf
operator|.
name|createBus
argument_list|(
literal|"/org/apache/cxf/systest/ws/rm/exactlyonce-inorder-decoupled.xml"
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|LoggingInInterceptor
name|in
init|=
operator|new
name|LoggingInInterceptor
argument_list|()
decl_stmt|;
name|in
operator|.
name|setPrettyLogging
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|LoggingOutInterceptor
name|out
init|=
operator|new
name|LoggingOutInterceptor
argument_list|()
decl_stmt|;
name|out
operator|.
name|setPrettyLogging
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|GreeterService
name|gs
init|=
operator|new
name|GreeterService
argument_list|()
decl_stmt|;
specifier|final
name|Greeter
name|greeter
init|=
name|gs
operator|.
name|getGreeterPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|greeter
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|SCHEMA_VALIDATION_ENABLED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Created greeter client."
argument_list|)
expr_stmt|;
name|ConnectionHelper
operator|.
name|setKeepAliveConnection
argument_list|(
name|greeter
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
block|}
block|}
end_class

end_unit

