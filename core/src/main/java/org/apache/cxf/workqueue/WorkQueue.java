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
name|workqueue
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
name|Executor
import|;
end_import

begin_interface
specifier|public
interface|interface
name|WorkQueue
extends|extends
name|Executor
block|{
comment|/**      * Submits a work item for execution at some time in the future, waiting for up to a      * specified amount of time for the item to be accepted.      *      * @param work the workitem to submit for execution.      * @param timeout the maximum amount of time (in milliseconds) to wait for it to be accepted.      *      * @throws<code>RejectedExecutionException</code> if this work item cannot be accepted for execution.      * @throws<code>NullPointerException</code> if work item is null.      */
name|void
name|execute
parameter_list|(
name|Runnable
name|work
parameter_list|,
name|long
name|timeout
parameter_list|)
function_decl|;
comment|/**      * Schedules a work item for execution at some time in the future.      *      * @param work the task to submit for execution.      * @param delay the delay before the task is executed      *      * @throws<code>RejectedExecutionException</code> if this task cannot be accepted for execution.      * @throws<code>NullPointerException</code> if task is null.      */
name|void
name|schedule
parameter_list|(
name|Runnable
name|work
parameter_list|,
name|long
name|delay
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

