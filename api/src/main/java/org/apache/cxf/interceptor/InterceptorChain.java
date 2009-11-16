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
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ListIterator
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
name|transport
operator|.
name|MessageObserver
import|;
end_import

begin_comment
comment|/**  * Base interface for all interceptor chains.  An interceptor chain is an  * ordered list of interceptors associated with one portion of the message  * processing pipeline. Interceptor chains are defined for a client's request   * processing, response processing, and incoming SOAP fault processing. Interceptor   * chains are defined for a service's request processing, response processing, and   * outgoing SOAP fault processing.  */
end_comment

begin_interface
specifier|public
interface|interface
name|InterceptorChain
extends|extends
name|Iterable
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
block|{
enum|enum
name|State
block|{
name|PAUSED
block|,
name|EXECUTING
block|,
name|COMPLETE
block|,
name|ABORTED
block|,     }
empty_stmt|;
name|String
name|STARTING_AFTER_INTERCEPTOR_ID
init|=
literal|"starting_after_interceptor_id"
decl_stmt|;
name|String
name|STARTING_AT_INTERCEPTOR_ID
init|=
literal|"starting_at_interceptor_id"
decl_stmt|;
comment|/**      * Adds a single interceptor to the interceptor chain.      *       * @param i the interceptor to add      */
name|void
name|add
parameter_list|(
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|i
parameter_list|)
function_decl|;
comment|/**      * Adds multiple interceptors to the interceptor chain.       * @param i the interceptors to add to the chain      */
name|void
name|add
parameter_list|(
name|Collection
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|i
parameter_list|)
function_decl|;
name|void
name|remove
parameter_list|(
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|i
parameter_list|)
function_decl|;
name|boolean
name|doIntercept
parameter_list|(
name|Message
name|message
parameter_list|)
function_decl|;
name|boolean
name|doInterceptStartingAfter
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|startingAfterInterceptorID
parameter_list|)
function_decl|;
name|boolean
name|doInterceptStartingAt
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|startingAtInterceptorID
parameter_list|)
function_decl|;
name|void
name|pause
parameter_list|()
function_decl|;
name|void
name|resume
parameter_list|()
function_decl|;
name|void
name|reset
parameter_list|()
function_decl|;
name|ListIterator
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getIterator
parameter_list|()
function_decl|;
name|MessageObserver
name|getFaultObserver
parameter_list|()
function_decl|;
name|void
name|setFaultObserver
parameter_list|(
name|MessageObserver
name|i
parameter_list|)
function_decl|;
name|void
name|abort
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

