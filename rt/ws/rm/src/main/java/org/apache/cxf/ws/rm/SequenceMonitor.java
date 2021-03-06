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
name|Deque
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
name|ConcurrentLinkedDeque
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_class
specifier|public
class|class
name|SequenceMonitor
block|{
specifier|private
specifier|static
specifier|final
name|long
name|DEFAULT_MONITOR_INTERVAL
init|=
literal|60000L
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|SequenceMonitor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|long
name|monitorInterval
init|=
name|DEFAULT_MONITOR_INTERVAL
decl_stmt|;
specifier|private
name|long
name|firstCheck
decl_stmt|;
specifier|private
specifier|final
name|Deque
argument_list|<
name|Long
argument_list|>
name|receiveTimes
init|=
operator|new
name|ConcurrentLinkedDeque
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|acknowledgeMessage
parameter_list|()
block|{
name|long
name|now
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
if|if
condition|(
literal|0L
operator|==
name|firstCheck
condition|)
block|{
name|firstCheck
operator|=
name|now
operator|+
name|monitorInterval
expr_stmt|;
block|}
name|receiveTimes
operator|.
name|add
argument_list|(
name|now
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getMPM
parameter_list|()
block|{
name|long
name|now
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|int
name|mpm
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|firstCheck
operator|>
literal|0L
operator|&&
name|now
operator|>=
name|firstCheck
condition|)
block|{
name|long
name|threshold
init|=
name|now
operator|-
name|monitorInterval
decl_stmt|;
while|while
condition|(
operator|!
name|receiveTimes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|receiveTimes
operator|.
name|getFirst
argument_list|()
operator|<=
name|threshold
condition|)
block|{
name|receiveTimes
operator|.
name|removeFirst
argument_list|()
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
name|mpm
operator|=
name|receiveTimes
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
return|return
name|mpm
return|;
block|}
specifier|public
name|long
name|getLastArrivalTime
parameter_list|()
block|{
return|return
operator|!
name|receiveTimes
operator|.
name|isEmpty
argument_list|()
condition|?
name|receiveTimes
operator|.
name|getLast
argument_list|()
else|:
literal|0L
return|;
block|}
specifier|protected
name|void
name|setMonitorInterval
parameter_list|(
name|long
name|i
parameter_list|)
block|{
if|if
condition|(
name|receiveTimes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|firstCheck
operator|=
literal|0L
expr_stmt|;
name|monitorInterval
operator|=
name|i
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Cannot change monitor interval at this point."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

