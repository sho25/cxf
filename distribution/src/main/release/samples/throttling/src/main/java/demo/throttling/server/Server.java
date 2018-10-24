begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|throttling
operator|.
name|server
package|;
end_package

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
name|HashMap
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
name|Map
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
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|MetricRegistry
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
name|bus
operator|.
name|CXFBusFactory
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
name|metrics
operator|.
name|MetricsFeature
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
name|metrics
operator|.
name|codahale
operator|.
name|CodahaleMetricsProvider
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
name|throttling
operator|.
name|ThrottleResponse
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
name|throttling
operator|.
name|ThrottlingFeature
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
name|throttling
operator|.
name|ThrottlingManager
import|;
end_import

begin_class
specifier|public
class|class
name|Server
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Customer
argument_list|>
name|customers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|protected
name|Server
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Starting Server"
argument_list|)
expr_stmt|;
name|customers
operator|.
name|put
argument_list|(
literal|"Tom"
argument_list|,
operator|new
name|Customer
operator|.
name|PremiumCustomer
argument_list|(
literal|"Tom"
argument_list|)
argument_list|)
expr_stmt|;
name|customers
operator|.
name|put
argument_list|(
literal|"Rob"
argument_list|,
operator|new
name|Customer
operator|.
name|PreferredCustomer
argument_list|(
literal|"Rob"
argument_list|)
argument_list|)
expr_stmt|;
name|customers
operator|.
name|put
argument_list|(
literal|"Vince"
argument_list|,
operator|new
name|Customer
operator|.
name|RegularCustomer
argument_list|(
literal|"Vince"
argument_list|)
argument_list|)
expr_stmt|;
name|customers
operator|.
name|put
argument_list|(
literal|"Malcolm"
argument_list|,
operator|new
name|Customer
operator|.
name|CheapCustomer
argument_list|(
literal|"Malcolm"
argument_list|)
argument_list|)
expr_stmt|;
name|customers
operator|.
name|put
argument_list|(
literal|"Jonas"
argument_list|,
operator|new
name|Customer
operator|.
name|TrialCustomer
argument_list|(
literal|"Jonas"
argument_list|)
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
literal|"bus.jmx.usePlatformMBeanServer"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"bus.jmx.enabled"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|Bus
name|b
init|=
operator|new
name|CXFBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
literal|null
argument_list|,
name|properties
argument_list|)
decl_stmt|;
name|MetricRegistry
name|registry
init|=
operator|new
name|MetricRegistry
argument_list|()
decl_stmt|;
name|CodahaleMetricsProvider
operator|.
name|setupJMXReporter
argument_list|(
name|b
argument_list|,
name|registry
argument_list|)
expr_stmt|;
name|b
operator|.
name|setExtension
argument_list|(
name|registry
argument_list|,
name|MetricRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|ThrottlingManager
name|manager
init|=
operator|new
name|ThrottlingManager
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ThrottleResponse
name|getThrottleResponse
parameter_list|(
name|String
name|phase
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|ThrottleResponse
name|r
init|=
operator|new
name|ThrottleResponse
argument_list|()
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|get
argument_list|(
literal|"THROTTLED"
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|m
operator|.
name|put
argument_list|(
literal|"THROTTLED"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Customer
name|c
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Customer
operator|.
name|class
argument_list|)
decl_stmt|;
name|c
operator|.
name|throttle
argument_list|(
name|r
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getDecisionPhases
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|b
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|CustomerMetricsInterceptor
argument_list|(
name|registry
argument_list|,
name|customers
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|implementor
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:9001/SoapContext/SoapPort"
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|,
operator|new
name|MetricsFeature
argument_list|()
argument_list|,
operator|new
name|ThrottlingFeature
argument_list|(
name|manager
argument_list|)
argument_list|)
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
throws|throws
name|Exception
block|{
operator|new
name|Server
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Server ready..."
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5
operator|*
literal|60
operator|*
literal|1000
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Server exiting"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

