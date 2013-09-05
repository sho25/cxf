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
name|delegation
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
comment|/**  * This interface controls whether the STS allows an authenticated user to get a token  * OnBehalfOf or ActAs another token.  */
end_comment

begin_interface
specifier|public
interface|interface
name|TokenDelegationHandler
block|{
comment|/**      * Return true if this TokenDelegationHandler implementation is capable of handling the      * ReceivedToken argument.      */
name|boolean
name|canHandleToken
parameter_list|(
name|ReceivedToken
name|delegateTarget
parameter_list|)
function_decl|;
comment|/**      * See if delegation is allowed for a Token using the given TokenDelegationParameters.      */
name|TokenDelegationResponse
name|isDelegationAllowed
parameter_list|(
name|TokenDelegationParameters
name|tokenParameters
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

