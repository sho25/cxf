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
comment|/**  * Base interface for all interceptors.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Interceptor
parameter_list|<
name|T
extends|extends
name|Message
parameter_list|>
block|{
comment|/**      * Intercepts a message.      * Interceptors should NOT invoke handleMessage or handleFault      * on the next interceptor - the interceptor chain will      * take care of this.      *      * @param message      */
name|void
name|handleMessage
parameter_list|(
name|T
name|message
parameter_list|)
throws|throws
name|Fault
function_decl|;
comment|/**      * Called for all interceptors (in reverse order) on which handleMessage      * had been successfully invoked, when normal execution of the chain was      * aborted for some reason.      *      * @param message      */
name|void
name|handleFault
parameter_list|(
name|T
name|message
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

