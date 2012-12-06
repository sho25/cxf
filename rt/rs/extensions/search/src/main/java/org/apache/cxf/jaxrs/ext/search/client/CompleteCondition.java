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
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|client
package|;
end_package

begin_comment
comment|/**  * Part of fluent interface of {@link SearchConditionBuilder}.  */
end_comment

begin_interface
specifier|public
interface|interface
name|CompleteCondition
block|{
comment|/** Conjunct current expression with another */
name|PartialCondition
name|and
parameter_list|()
function_decl|;
comment|/** shortcut for and().is() */
name|Property
name|and
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/** Disjunct current expression with another */
name|PartialCondition
name|or
parameter_list|()
function_decl|;
comment|/** shortcut for or().is() */
name|Property
name|or
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/** Finalize condition construction and build search condition query. */
name|String
name|query
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

