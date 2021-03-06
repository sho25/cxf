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
name|concurrent
operator|.
name|Future
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
name|jws
operator|.
name|WebService
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
name|AsyncHandler
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
name|Response
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
name|greeter_control
operator|.
name|types
operator|.
name|StartGreeterResponse
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
name|StopGreeterResponse
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"ControlService"
argument_list|,
name|portName
operator|=
literal|"ControlPort"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.cxf.greeter_control.Control"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/greeter_control"
argument_list|)
specifier|public
class|class
name|ControlImpl
implements|implements
name|Control
block|{
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
name|ControlImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Greeter
argument_list|>
name|implementors
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Endpoint
argument_list|>
name|endpoints
decl_stmt|;
name|ControlImpl
parameter_list|()
block|{
name|implementors
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|implementors
operator|.
name|put
argument_list|(
name|FailoverTest
operator|.
name|REPLICA_A
argument_list|,
operator|new
name|GreeterImplA
argument_list|()
argument_list|)
expr_stmt|;
name|implementors
operator|.
name|put
argument_list|(
name|FailoverTest
operator|.
name|REPLICA_B
argument_list|,
operator|new
name|GreeterImplB
argument_list|()
argument_list|)
expr_stmt|;
name|implementors
operator|.
name|put
argument_list|(
name|FailoverTest
operator|.
name|REPLICA_C
argument_list|,
operator|new
name|GreeterImplC
argument_list|()
argument_list|)
expr_stmt|;
name|implementors
operator|.
name|put
argument_list|(
name|FailoverTest
operator|.
name|REPLICA_D
argument_list|,
operator|new
name|GreeterImplD
argument_list|()
argument_list|)
expr_stmt|;
name|implementors
operator|.
name|put
argument_list|(
name|FailoverTest
operator|.
name|REPLICA_E
argument_list|,
operator|new
name|GreeterImplE
argument_list|()
argument_list|)
expr_stmt|;
name|endpoints
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|startGreeter
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|endpoints
operator|.
name|put
argument_list|(
name|address
argument_list|,
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementors
operator|.
name|get
argument_list|(
name|address
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Published greeter endpoint on: "
operator|+
name|address
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|stopGreeter
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|endpoints
operator|.
name|get
argument_list|(
name|address
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|endpoint
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Stopping Greeter endpoint on: "
operator|+
name|address
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"No endpoint active for: "
operator|+
name|address
argument_list|)
expr_stmt|;
block|}
name|endpoint
operator|=
literal|null
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|//--Irrelevant Boilerplate
specifier|public
name|void
name|setFaultLocation
parameter_list|(
name|FaultLocation
name|fl
parameter_list|)
block|{
comment|// never called
block|}
specifier|public
name|Future
argument_list|<
name|?
argument_list|>
name|startGreeterAsync
parameter_list|(
name|String
name|requestType
parameter_list|,
name|AsyncHandler
argument_list|<
name|StartGreeterResponse
argument_list|>
name|asyncHandler
parameter_list|)
block|{
comment|// never called
return|return
literal|null
return|;
block|}
specifier|public
name|Response
argument_list|<
name|StartGreeterResponse
argument_list|>
name|startGreeterAsync
parameter_list|(
name|String
name|requestType
parameter_list|)
block|{
comment|// never called
return|return
literal|null
return|;
block|}
specifier|public
name|Response
argument_list|<
name|StopGreeterResponse
argument_list|>
name|stopGreeterAsync
parameter_list|(
name|String
name|requestType
parameter_list|)
block|{
comment|// never called
return|return
literal|null
return|;
block|}
specifier|public
name|Future
argument_list|<
name|?
argument_list|>
name|stopGreeterAsync
parameter_list|(
name|String
name|requestType
parameter_list|,
name|AsyncHandler
argument_list|<
name|StopGreeterResponse
argument_list|>
name|asyncHandler
parameter_list|)
block|{
comment|// never called
return|return
literal|null
return|;
block|}
specifier|public
name|Response
argument_list|<
name|?
argument_list|>
name|setFaultLocationAsync
parameter_list|(
name|FaultLocation
name|in
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Future
argument_list|<
name|?
argument_list|>
name|setFaultLocationAsync
parameter_list|(
name|FaultLocation
name|in
parameter_list|,
name|AsyncHandler
argument_list|<
name|?
argument_list|>
name|asyncHandler
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Response
argument_list|<
name|?
argument_list|>
name|setRobustInOnlyModeAsync
parameter_list|(
name|boolean
name|in
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Future
argument_list|<
name|?
argument_list|>
name|setRobustInOnlyModeAsync
parameter_list|(
name|boolean
name|in
parameter_list|,
name|AsyncHandler
argument_list|<
name|?
argument_list|>
name|asyncHandler
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setRobustInOnlyMode
parameter_list|(
name|boolean
name|in
parameter_list|)
block|{      }
block|}
end_class

end_unit

