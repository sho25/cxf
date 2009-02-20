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
name|phase
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|interceptor
operator|.
name|Interceptor
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

begin_comment
comment|/**  * A phase interceptor is an intercetor that participates in a   * PhaseInterceptorChain.  * The phase property controls the phase in which the interceptor is placed.  * The before and after properties allow for fine grained control over where   * the phase the interceptor is placed. They specify the IDs of the   * interceptors that must be placed before and after the interceptor.  *  * @see org.apache.cxf.phase.PhaseInterceptorChain  * @author Dan Diephouse  */
end_comment

begin_interface
specifier|public
interface|interface
name|PhaseInterceptor
parameter_list|<
name|T
extends|extends
name|Message
parameter_list|>
extends|extends
name|Interceptor
argument_list|<
name|T
argument_list|>
block|{
comment|/**      * Returns a set containing the IDs of the interceptors that should be       * executed before this interceptor. This interceptor will be placed       * in the chain after the interceptors in the set.      * @return the IDs of the interceptors      */
name|Set
argument_list|<
name|String
argument_list|>
name|getAfter
parameter_list|()
function_decl|;
comment|/**      * Returns a set containing the IDs of the interceptors that should be       * executed after this interceptor. This interceptor will be placed in       * the inteceptor chain before the interceptors in the set.      * @return the ids of the interceptors       */
name|Set
argument_list|<
name|String
argument_list|>
name|getBefore
parameter_list|()
function_decl|;
comment|/**      * Returns the ID of this interceptor.      * @return the ID      */
name|String
name|getId
parameter_list|()
function_decl|;
comment|/**      * Returns the phase in which this interceptor is excecuted.      * @return the phase      */
name|String
name|getPhase
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

