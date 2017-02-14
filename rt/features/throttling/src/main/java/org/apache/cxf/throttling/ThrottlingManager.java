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
name|throttling
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
comment|/**  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|ThrottlingManager
block|{
comment|/**      * Get the list of phases where this manager will expect to have to make throttling decisions.      * For example: using BasicAuth or other protocol based header, it can be a very early in the      * chain, but for WS-Security based authentication, it would be later.      * @return      */
name|List
argument_list|<
name|String
argument_list|>
name|getDecisionPhases
parameter_list|()
function_decl|;
comment|/**      * Use information in the message to determine what throttling measures should be taken      * @param phase      * @param m      * @return      */
name|ThrottleResponse
name|getThrottleResponse
parameter_list|(
name|String
name|phase
parameter_list|,
name|Message
name|m
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

