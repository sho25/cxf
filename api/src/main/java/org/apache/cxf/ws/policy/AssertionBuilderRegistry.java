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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|extension
operator|.
name|Registry
import|;
end_import

begin_comment
comment|/**  * AssertionBuilderRegistry is used to manage AssertionBuilders and  * create Assertion objects from given xml elements.  */
end_comment

begin_interface
specifier|public
interface|interface
name|AssertionBuilderRegistry
extends|extends
name|Registry
argument_list|<
name|QName
argument_list|,
name|AssertionBuilder
argument_list|>
block|{
comment|/**      * Returns an assertion that is built using the specified xml element.      *       * @param element the element from which to build an Assertion.      * @return an Assertion that is built using the specified element.      */
name|PolicyAssertion
name|build
parameter_list|(
name|Element
name|element
parameter_list|)
function_decl|;
comment|/**      * Indicates if unknown assertions should simply be ignored.      * If set to false, the policy engine will throw an exception upon      * encountering an assertion type for which no AssertionBuilder      * has been registered.      * @return      */
name|boolean
name|isIgnoreUnknownAssertions
parameter_list|()
function_decl|;
comment|/**      * Indicates if unknown assertions should simply be ignored.      * If set to false, the policy engine will throw an exception upon      * encountering an assertion type for which no AssertionBuilder      * has been registered.       * @param ignoreUnknownAssertions iff unknown assertions should be ignored      */
name|void
name|setIgnoreUnknownAssertions
parameter_list|(
name|boolean
name|ignoreUnknownAssertions
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

