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
package|;
end_package

begin_comment
comment|/**  * Interface for visitors to SearchCondition objects.  * Custom implementations can use it to convert SearchCondition into  * specific query language such as SQL, etc  */
end_comment

begin_interface
specifier|public
interface|interface
name|SearchConditionVisitor
parameter_list|<
name|T
parameter_list|>
block|{
comment|/*      * Callback providing a current SearchCondition object       */
name|void
name|visit
parameter_list|(
name|SearchCondition
argument_list|<
name|T
argument_list|>
name|sc
parameter_list|)
function_decl|;
comment|/**      * Recover the accumulated query      * @return query string      */
name|String
name|getResult
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

