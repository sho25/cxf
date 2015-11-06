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
name|clustering
operator|.
name|circuitbreaker
package|;
end_package

begin_comment
comment|/**  * Basic abstract interface for circuit breaker implementation.  */
end_comment

begin_interface
specifier|public
interface|interface
name|CircuitBreaker
block|{
comment|/**      * Is request is allowed to go through (is circuit breaker closed or opened).      * @return "false" if circuit breaker is open, "true" otherwise      */
name|boolean
name|allowRequest
parameter_list|()
function_decl|;
comment|/**      * Reports about failure conditions to circuit breaker.      * @param cause exception happened (could be null in case the error is deducted      * from response status/code).      */
name|void
name|markFailure
parameter_list|(
name|Throwable
name|cause
parameter_list|)
function_decl|;
comment|/**      * Reports about successful invocation to circuit breaker.      */
name|void
name|markSuccess
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

