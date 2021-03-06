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
name|binding
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
name|interceptor
operator|.
name|InterceptorProvider
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
name|service
operator|.
name|model
operator|.
name|BindingInfo
import|;
end_import

begin_comment
comment|/**  * A Binding provides interceptors and message creation logic for a  * specific protocol binding.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Binding
extends|extends
name|InterceptorProvider
block|{
comment|/**      * Create a Message for this Binding.      * @return the Binding message      */
name|Message
name|createMessage
parameter_list|()
function_decl|;
comment|/**      * Create a Message form the message.      *      * @param m the message used for creating a binding message      * @return the Binding message      */
name|Message
name|createMessage
parameter_list|(
name|Message
name|m
parameter_list|)
function_decl|;
comment|/**      * Get the BindingInfo for this binding.      *      * @return the BingdingInfo Object      */
name|BindingInfo
name|getBindingInfo
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

