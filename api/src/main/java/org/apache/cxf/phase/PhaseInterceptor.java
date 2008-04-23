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
comment|/**  * A phase interceptor participates in a PhaseInterceptorChain.  *<pre>  * The before and after properties contain a list of Ids that can control   * where in the chain the interceptor is placed relative to other interceptors  *</pre>   * @see org.apache.cxf.phase.PhaseInterceptorChain  * @author Dan Diephouse  */
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
comment|/**      * Returns a set of IDs specifying the interceptors that this interceptor should       * be placed after in the interceptor chain      * @return the ids of the interceptors      */
name|Set
argument_list|<
name|String
argument_list|>
name|getAfter
parameter_list|()
function_decl|;
comment|/**      * Returns a set of IDs specifying the interceptors that this interceptor needs       * to be before in the inteceptor chain.      * @return the ids of the interceptors       */
name|Set
argument_list|<
name|String
argument_list|>
name|getBefore
parameter_list|()
function_decl|;
comment|/**      * The ID of this interceptor.      * @return the id      */
name|String
name|getId
parameter_list|()
function_decl|;
comment|/**      * The phase of this interceptor.      * @return the phase      */
name|String
name|getPhase
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

