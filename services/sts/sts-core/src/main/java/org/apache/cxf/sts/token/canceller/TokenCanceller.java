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
name|sts
operator|.
name|token
operator|.
name|canceller
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
name|sts
operator|.
name|request
operator|.
name|ReceivedToken
import|;
end_import

begin_comment
comment|/**  * An interface that can cancel a security token.  */
end_comment

begin_interface
specifier|public
interface|interface
name|TokenCanceller
block|{
comment|/**      * Return true if this TokenCanceller implementation is able to cancel a token      * that corresponds to the given token.      */
name|boolean
name|canHandleToken
parameter_list|(
name|ReceivedToken
name|cancelTarget
parameter_list|)
function_decl|;
comment|/**      * Cancel a token given a TokenCancellerParameters      */
name|TokenCancellerResponse
name|cancelToken
parameter_list|(
name|TokenCancellerParameters
name|tokenParameters
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

