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
name|continuations
package|;
end_package

begin_comment
comment|/**  * Callback that receives continuation status updates.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ContinuationCallback
block|{
comment|/**      * This method is called when the container completes writing the response to the client      */
name|void
name|onComplete
parameter_list|()
function_decl|;
comment|/**      * This method is called when the exception gets propagated to the container      * @param error the propagated exception instance      */
name|void
name|onError
parameter_list|(
name|Throwable
name|error
parameter_list|)
function_decl|;
comment|/**      * This method may be called if the container detects that the client has disconnected      */
name|void
name|onDisconnect
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

