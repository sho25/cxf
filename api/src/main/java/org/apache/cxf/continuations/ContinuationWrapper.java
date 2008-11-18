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
comment|/**  * Represents transport-neutral suspended invocation instances   * or continuations  */
end_comment

begin_interface
specifier|public
interface|interface
name|ContinuationWrapper
block|{
comment|/**       * This method will suspend the request for the timeout or until resume is      * called      *       * @param timeout. A timeout of< 0 will cause an immediate return.      * A timeout of 0 will wait indefinitely.      * @return True if resume called or false if timeout.      */
name|boolean
name|suspend
parameter_list|(
name|long
name|timeout
parameter_list|)
function_decl|;
comment|/**       * Resume a suspended request        */
name|void
name|resume
parameter_list|()
function_decl|;
comment|/**       * Reset the continuation      */
name|void
name|reset
parameter_list|()
function_decl|;
comment|/**       * Is this a newly created Continuation.      * @return True if the continuation has just been created and has not yet suspended the request.      */
name|boolean
name|isNew
parameter_list|()
function_decl|;
comment|/**       * Get the pending status      * @return True if the continuation has been suspended.      */
name|boolean
name|isPending
parameter_list|()
function_decl|;
comment|/**       * Get the resumed status      * @return True if the continuation is has been resumed.      */
name|boolean
name|isResumed
parameter_list|()
function_decl|;
comment|/**       * Get arbitrary object associated with the continuation for context      *       * @return An arbitrary object associated with the continuation      */
name|Object
name|getObject
parameter_list|()
function_decl|;
comment|/**       * Sets arbitrary object associated with the continuation for context      *       * @param o An arbitrary object to associate with the continuation      */
name|void
name|setObject
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

