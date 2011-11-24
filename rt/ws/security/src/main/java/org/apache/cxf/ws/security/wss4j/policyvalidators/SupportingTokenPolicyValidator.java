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
name|ws
operator|.
name|security
operator|.
name|wss4j
operator|.
name|policyvalidators
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
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|ws
operator|.
name|policy
operator|.
name|AssertionInfoMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|WSSecurityEngineResult
import|;
end_import

begin_comment
comment|/**  * Validate a WS-SecurityPolicy corresponding to a SupportingToken.  */
end_comment

begin_interface
specifier|public
interface|interface
name|SupportingTokenPolicyValidator
block|{
comment|/**      * Set the list of UsernameToken results      */
name|void
name|setUsernameTokenResults
parameter_list|(
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|utResultsList
parameter_list|,
name|boolean
name|valUsernameToken
parameter_list|)
function_decl|;
comment|/**      * Set the list of SAMLToken results      */
name|void
name|setSAMLTokenResults
parameter_list|(
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|samlResultsList
parameter_list|)
function_decl|;
comment|/**      * Set the Timestamp element      */
name|void
name|setTimestampElement
parameter_list|(
name|Element
name|timestampElement
parameter_list|)
function_decl|;
comment|/**      * Validate a particular policy from the AssertionInfoMap argument. Return true if the policy is valid.      */
name|boolean
name|validatePolicy
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|Message
name|message
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|results
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|encryptedResults
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

