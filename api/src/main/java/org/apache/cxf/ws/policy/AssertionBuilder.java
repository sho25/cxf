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

begin_comment
comment|/**  * AssertionBuilder is an interface used to build an Assertion object from a  * given xml element.   * Domain Policy authors write custom AssertionBuilders to build Assertions for   * domain specific assertions.   * Note that assertions can include nested policy expressions. To build these,  * it may be necessary to obtain other AssertionBuilders.  * Concrete implementations should access the AssertionBuilderRegistry as a  * Bus extension, so the registry need not passed as an argument here.  */
end_comment

begin_interface
specifier|public
interface|interface
name|AssertionBuilder
block|{
comment|/**      * Constructs an assertion from an xml element.      *       * @param element the element from which to build an assertion      * @return an Assertion built from the given element      */
name|PolicyAssertion
name|build
parameter_list|(
name|Element
name|element
parameter_list|)
function_decl|;
comment|/**      * Returns a collection of QNames describing the xml schema types for which this      * builder can build assertions.      *       * @return collection of QNames of known schema types      */
name|Collection
argument_list|<
name|QName
argument_list|>
name|getKnownElements
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

