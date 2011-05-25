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
name|clustering
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ConnectException
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
name|logging
operator|.
name|Logger
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
name|clustering
operator|.
name|AbstractStaticFailoverStrategy
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
name|clustering
operator|.
name|FailoverTargetSelector
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
name|clustering
operator|.
name|RandomStrategy
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
name|clustering
operator|.
name|SequentialStrategy
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
name|ConduitSelector
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
name|ClusteredGreeterService
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
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|soap
operator|.
name|MAPCodec
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
name|Before
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
comment|/**  * Tests failover within a static cluster.  */
end_comment

begin_class
specifier|public
class|class
name|FailoverAddressOverrideTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT_0
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|0
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PORT_A
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PORT_B
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|2
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PORT_C
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|3
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PORT_D
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|4
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PORT_EXTRA
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|99
argument_list|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|REPLICA_A
init|=
literal|"http://localhost:"
operator|+
name|PORT_A
operator|+
literal|"/SoapContext/ReplicatedPortA"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|REPLICA_B
init|=
literal|"http://localhost:"
operator|+
name|PORT_B
operator|+
literal|"/SoapContext/ReplicatedPortB"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|REPLICA_C
init|=
literal|"http://localhost:"
operator|+
name|PORT_C
operator|+
literal|"/SoapContext/ReplicatedPortC"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|REPLICA_D
init|=
literal|"http://localhost:"
operator|+
name|PORT_D
operator|+
literal|"/SoapContext/ReplicatedPortD"
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
name|FailoverAddressOverrideTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FAILOVER_CONFIG
init|=
literal|"org/apache/cxf/systest/clustering/failover_address_override.xml"
decl_stmt|;
specifier|protected
name|Bus
name|bus
decl_stmt|;
specifier|protected
name|Greeter
name|greeter
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|targets
decl_stmt|;
specifier|private
name|Control
name|control
decl_stmt|;
specifier|private
name|MAPAggregator
name|mapAggregator
decl_stmt|;
specifier|private
name|MAPCodec
name|mapCodec
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
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|getConfig
parameter_list|()
block|{
return|return
name|FAILOVER_CONFIG
return|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|targets
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
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
name|getConfig
argument_list|()
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
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
if|if
condition|(
literal|null
operator|!=
name|control
condition|)
block|{
for|for
control|(
name|String
name|address
range|:
name|targets
control|)
block|{
name|assertTrue
argument_list|(
literal|"Failed to stop greeter"
argument_list|,
name|control
operator|.
name|stopGreeter
argument_list|(
name|address
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|targets
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|bus
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
name|testOverriddenSequentialStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|startTarget
argument_list|(
name|REPLICA_C
argument_list|)
expr_stmt|;
name|setupGreeterA
argument_list|()
expr_stmt|;
name|verifyStrategy
argument_list|(
name|greeter
argument_list|,
name|SequentialStrategy
operator|.
name|class
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|String
name|response
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"fred"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"expected non-null response"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"response from unexpected target: "
operator|+
name|response
argument_list|,
name|response
operator|.
name|endsWith
argument_list|(
name|REPLICA_C
argument_list|)
argument_list|)
expr_stmt|;
name|verifyCurrentEndpoint
argument_list|(
name|REPLICA_C
argument_list|)
expr_stmt|;
name|stopTarget
argument_list|(
name|REPLICA_C
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOverriddenRandomStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|startTarget
argument_list|(
name|REPLICA_B
argument_list|)
expr_stmt|;
name|setupGreeterC
argument_list|()
expr_stmt|;
name|verifyStrategy
argument_list|(
name|greeter
argument_list|,
name|RandomStrategy
operator|.
name|class
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|String
name|response
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"fred"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"expected non-null response"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"response from unexpected target: "
operator|+
name|response
argument_list|,
name|response
operator|.
name|endsWith
argument_list|(
name|REPLICA_B
argument_list|)
argument_list|)
expr_stmt|;
name|verifyCurrentEndpoint
argument_list|(
name|REPLICA_B
argument_list|)
expr_stmt|;
name|stopTarget
argument_list|(
name|REPLICA_B
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnreachableAddresses
parameter_list|()
throws|throws
name|Exception
block|{
name|startTarget
argument_list|(
name|REPLICA_A
argument_list|)
expr_stmt|;
name|setupGreeterB
argument_list|()
expr_stmt|;
name|verifyStrategy
argument_list|(
name|greeter
argument_list|,
name|SequentialStrategy
operator|.
name|class
argument_list|,
literal|2
argument_list|)
expr_stmt|;
try|try
block|{
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"fred"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Throwable
name|cause
init|=
name|e
decl_stmt|;
while|while
condition|(
name|cause
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cause
operator|=
name|cause
operator|.
name|getCause
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
operator|(
name|cause
operator|instanceof
name|ConnectException
operator|)
condition|)
block|{
if|if
condition|(
name|cause
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
operator|&&
name|cause
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"404"
argument_list|)
condition|)
block|{
return|return;
block|}
throw|throw
name|e
throw|;
block|}
block|}
name|stopTarget
argument_list|(
name|REPLICA_A
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|startTarget
parameter_list|(
name|String
name|address
parameter_list|)
throws|throws
name|Exception
block|{
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
name|PORT_0
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"starting replicated target: "
operator|+
name|address
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"starting replicated target: "
operator|+
name|address
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
name|address
argument_list|)
argument_list|)
expr_stmt|;
name|targets
operator|.
name|add
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|stopTarget
parameter_list|(
name|String
name|address
parameter_list|)
block|{
if|if
condition|(
name|control
operator|!=
literal|null
operator|&&
name|targets
operator|.
name|contains
argument_list|(
name|address
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"starting replicated target: "
operator|+
name|address
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed to start greeter"
argument_list|,
name|control
operator|.
name|stopGreeter
argument_list|(
name|address
argument_list|)
argument_list|)
expr_stmt|;
name|targets
operator|.
name|remove
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|verifyCurrentEndpoint
parameter_list|(
name|String
name|replica
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"unexpected current endpoint"
argument_list|,
name|replica
argument_list|,
name|getCurrentEndpoint
argument_list|(
name|greeter
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|getCurrentEndpoint
parameter_list|(
name|Object
name|proxy
parameter_list|)
block|{
return|return
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|proxy
argument_list|)
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
return|;
block|}
specifier|protected
name|void
name|setupGreeterA
parameter_list|()
throws|throws
name|Exception
block|{
name|greeter
operator|=
operator|new
name|ClusteredGreeterService
argument_list|()
operator|.
name|getReplicatedPortA
argument_list|()
expr_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT_A
argument_list|)
expr_stmt|;
name|verifyConduitSelector
argument_list|(
name|greeter
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setupGreeterB
parameter_list|()
throws|throws
name|Exception
block|{
name|greeter
operator|=
operator|new
name|ClusteredGreeterService
argument_list|()
operator|.
name|getReplicatedPortB
argument_list|()
expr_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT_B
argument_list|)
expr_stmt|;
name|verifyConduitSelector
argument_list|(
name|greeter
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setupGreeterC
parameter_list|()
throws|throws
name|Exception
block|{
name|greeter
operator|=
operator|new
name|ClusteredGreeterService
argument_list|()
operator|.
name|getReplicatedPortC
argument_list|()
expr_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT_C
argument_list|)
expr_stmt|;
name|verifyConduitSelector
argument_list|(
name|greeter
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|verifyConduitSelector
parameter_list|(
name|Greeter
name|g
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"unexpected conduit slector"
argument_list|,
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|g
argument_list|)
operator|.
name|getConduitSelector
argument_list|()
operator|instanceof
name|FailoverTargetSelector
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|verifyStrategy
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Class
name|clz
parameter_list|,
name|int
name|count
parameter_list|)
block|{
name|ConduitSelector
name|conduitSelector
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|proxy
argument_list|)
operator|.
name|getConduitSelector
argument_list|()
decl_stmt|;
if|if
condition|(
name|conduitSelector
operator|instanceof
name|FailoverTargetSelector
condition|)
block|{
name|AbstractStaticFailoverStrategy
name|strategy
init|=
call|(
name|AbstractStaticFailoverStrategy
call|)
argument_list|(
operator|(
name|FailoverTargetSelector
operator|)
name|conduitSelector
argument_list|)
operator|.
name|getStrategy
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"unexpected strategy"
argument_list|,
name|clz
operator|.
name|isInstance
argument_list|(
name|strategy
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|alternates
init|=
name|strategy
operator|.
name|getAlternateAddresses
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"expected alternate addresses"
argument_list|,
name|alternates
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected alternate addresses"
argument_list|,
name|count
argument_list|,
name|alternates
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fail
argument_list|(
literal|"unexpected conduit selector: "
operator|+
name|conduitSelector
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|enableWSAForCurrentEndpoint
parameter_list|()
block|{
name|Endpoint
name|provider
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|mapAggregator
operator|=
operator|new
name|MAPAggregator
argument_list|()
expr_stmt|;
name|mapCodec
operator|=
operator|new
name|MAPCodec
argument_list|()
expr_stmt|;
name|provider
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|mapAggregator
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|mapCodec
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|mapAggregator
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|mapCodec
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|mapAggregator
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|mapCodec
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|mapAggregator
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|mapCodec
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|boolean
name|isWSAEnabledForCurrentEndpoint
parameter_list|()
block|{
name|Endpoint
name|provider
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|boolean
name|enabledIn
init|=
name|provider
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|contains
argument_list|(
name|mapAggregator
argument_list|)
operator|&&
name|provider
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|contains
argument_list|(
name|mapCodec
argument_list|)
operator|&&
name|provider
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|contains
argument_list|(
name|mapAggregator
argument_list|)
operator|&&
name|provider
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|contains
argument_list|(
name|mapCodec
argument_list|)
decl_stmt|;
name|boolean
name|enabledOut
init|=
name|provider
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|contains
argument_list|(
name|mapAggregator
argument_list|)
operator|&&
name|provider
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|contains
argument_list|(
name|mapCodec
argument_list|)
operator|&&
name|provider
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|contains
argument_list|(
name|mapAggregator
argument_list|)
operator|&&
name|provider
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|contains
argument_list|(
name|mapCodec
argument_list|)
decl_stmt|;
return|return
name|enabledIn
operator|&&
name|enabledOut
return|;
block|}
block|}
end_class

end_unit

