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
name|policy
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
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Assertion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Policy
import|;
end_import

begin_comment
comment|/**  * Used by the Policy engine to select the Policy alternative to use.  *   * By default, the Policy engine uses a "Minimal" policy alternative selector  * that finds the alternative with the smallest Collection of Assertions to  * assert.  */
end_comment

begin_interface
specifier|public
interface|interface
name|AlternativeSelector
block|{
comment|/**      *       * @param policy The full policy to consider       * @param engine The policy engine calling the selector      * @param assertor Additional asserter (such as the transport) that may be       *                 able to handle some of the assertions      * @param request On the server out bound side, this will contain the alternatives      *                from the request that were successfully met by the request.  The      *                selector should use these to help narrow down the alternative to      *                use.      * @return      */
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|selectAlternative
parameter_list|(
name|Policy
name|policy
parameter_list|,
name|PolicyEngine
name|engine
parameter_list|,
name|Assertor
name|assertor
parameter_list|,
name|List
argument_list|<
name|List
argument_list|<
name|Assertion
argument_list|>
argument_list|>
name|request
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

