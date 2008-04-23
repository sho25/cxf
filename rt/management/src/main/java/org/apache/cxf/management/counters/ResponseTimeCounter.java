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
name|management
operator|.
name|counters
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
import|;
end_import

begin_class
specifier|public
class|class
name|ResponseTimeCounter
implements|implements
name|ResponseTimeCounterMBean
implements|,
name|Counter
block|{
specifier|private
name|ObjectName
name|objectName
decl_stmt|;
specifier|private
name|AtomicInteger
name|invocations
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
specifier|private
name|long
name|totalHandlingTime
decl_stmt|;
specifier|private
name|long
name|maxHandlingTime
decl_stmt|;
specifier|private
name|long
name|minHandlingTime
init|=
name|Integer
operator|.
name|MAX_VALUE
decl_stmt|;
specifier|public
name|ResponseTimeCounter
parameter_list|(
name|ObjectName
name|on
parameter_list|)
block|{
name|objectName
operator|=
name|on
expr_stmt|;
block|}
specifier|public
name|void
name|increase
parameter_list|(
name|MessageHandlingTimeRecorder
name|mhtr
parameter_list|)
block|{
name|invocations
operator|.
name|getAndIncrement
argument_list|()
expr_stmt|;
name|long
name|handlingTime
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|mhtr
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
comment|// We can count the response time
if|if
condition|(
name|mhtr
operator|.
name|getEndTime
argument_list|()
operator|>
literal|0
condition|)
block|{
name|handlingTime
operator|=
name|mhtr
operator|.
name|getHandlingTime
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|handlingTime
operator|=
name|mhtr
operator|.
name|getHandlingTime
argument_list|()
expr_stmt|;
block|}
name|totalHandlingTime
operator|=
name|totalHandlingTime
operator|+
name|handlingTime
expr_stmt|;
if|if
condition|(
name|maxHandlingTime
operator|<
name|handlingTime
condition|)
block|{
name|maxHandlingTime
operator|=
name|handlingTime
expr_stmt|;
block|}
if|if
condition|(
name|minHandlingTime
operator|>
name|handlingTime
condition|)
block|{
name|minHandlingTime
operator|=
name|handlingTime
expr_stmt|;
block|}
block|}
specifier|public
name|ObjectName
name|getObjectName
parameter_list|()
block|{
return|return
name|objectName
return|;
block|}
specifier|public
name|Number
name|getAvgResponseTime
parameter_list|()
block|{
return|return
call|(
name|int
call|)
argument_list|(
name|totalHandlingTime
operator|/
name|invocations
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Number
name|getMaxResponseTime
parameter_list|()
block|{
return|return
name|maxHandlingTime
return|;
block|}
specifier|public
name|Number
name|getMinResponseTime
parameter_list|()
block|{
return|return
name|minHandlingTime
return|;
block|}
specifier|public
name|Number
name|getNumInvocations
parameter_list|()
block|{
return|return
name|invocations
operator|.
name|get
argument_list|()
return|;
block|}
block|}
end_class

end_unit

