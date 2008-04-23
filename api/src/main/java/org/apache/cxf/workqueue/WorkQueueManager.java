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

begin_interface
specifier|public
interface|interface
name|WorkQueueManager
block|{
enum|enum
name|ThreadingModel
block|{
name|SINGLE_THREADED
block|,
name|MULTI_THREADED
block|}
empty_stmt|;
comment|/**      * Get the manager's work queue.      * @return AutomaticWorkQueue      */
name|AutomaticWorkQueue
name|getAutomaticWorkQueue
parameter_list|()
function_decl|;
comment|/**      * Get the threading model.      * @return ThreadingModel - either<code>SINGLE_THREADED</code>      * or<code>MULTI_THREADED</code>.      */
name|ThreadingModel
name|getThreadingModel
parameter_list|()
function_decl|;
comment|/**      * Set the threading model.      * @param model either<code>SINGLE_THREADED</code>      * or<code>MULTI_THREADED</code>.      */
name|void
name|setThreadingModel
parameter_list|(
name|ThreadingModel
name|model
parameter_list|)
function_decl|;
comment|/**      * Shuts down the manager's work queue. If      *<code>processRemainingTasks</code> is true, waits for the work queue to      * shutdown before returning.      * @param processRemainingTasks - whether or not to wait for completion      */
name|void
name|shutdown
parameter_list|(
name|boolean
name|processRemainingTasks
parameter_list|)
function_decl|;
comment|/**      * Only returns after workqueue has been shutdown.      *      */
name|void
name|run
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

