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
name|List
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
comment|/**  * The<code>InterceptorProvider</code> interface is implemented by objects  * that have interceptor chains associated with them. The methods in this  * interface provide the ability to add and remove interceptors to the chains  * of the InterceptorProvider.  */
end_comment

begin_interface
specifier|public
interface|interface
name|InterceptorProvider
block|{
comment|/**      * Returns the list of interceptors attached to the incoming interceptor      * chain of the object.      * @return<code>List<Interceptor></code> incoming interceptor chain      */
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getInInterceptors
parameter_list|()
function_decl|;
comment|/**      * Returns the list of interceptors attached to the outgoing interceptor      * chain of the object.      * @return<code>List<Interceptor></code> outgoing interceptor chain      */
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getOutInterceptors
parameter_list|()
function_decl|;
comment|/**      * Returns the list of interceptors attached to the incoming fault interceptor      * chain of the object.      * @return<code>List<Interceptor></code> incoming fault interceptor chain      */
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getInFaultInterceptors
parameter_list|()
function_decl|;
comment|/**      * Returns the list of interceptors attached to the outgoing fault interceptor      * chain of the object.      * @return<code>List<Interceptor></code> outgoing fault interceptor chain      */
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getOutFaultInterceptors
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

