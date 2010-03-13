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

begin_comment
comment|//CHECKSTYLE:OFF
end_comment

begin_comment
comment|/**  * Can be used to build plain or complex/composite search conditions.  *<p>  * Google Collections<a href="http://google-collections.googlecode.com/svn/trunk/javadoc/com/google/common/base/Predicate.html">Predicate</a>  * might've been used instead, but it is a too generic and its apply method is not quite needed here  *</p>  *    * @param<T> Type of the object which will be checked by SearchCondition instance  *    */
end_comment

begin_comment
comment|//CHECKSTYLE:ON
end_comment

begin_interface
specifier|public
interface|interface
name|SearchCondition
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Checks if the given pojo instance meets this search condition      *       * @param pojo the object which will be checked      * @return true if the pojo meets this search condition, false - otherwise      */
name|boolean
name|isMet
parameter_list|(
name|T
name|pojo
parameter_list|)
function_decl|;
comment|/**      * Returns a list of pojos matching the condition      * @param pojos list of pojos      * @return list of the matching pojos or null if none have been found      */
name|List
argument_list|<
name|T
argument_list|>
name|findAll
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|pojos
parameter_list|)
function_decl|;
comment|/**      * Some SearchConditions may use instance of T to capture the actual search criteria      * thus making it simpler to implement isMet(T). In some cases, the code which is given      * SearchCondition may find it more efficient to directly deal with the captured state      * for a more efficient lookup of matching data/records as opposed to calling      * SearchCondition.isMet for every instance of T it knows about.       *        * @return T the captured search criteria, can be null       */
name|T
name|getCondition
parameter_list|()
function_decl|;
comment|/**      * List of conditions this SearchCondition may represent        * @return list of conditions, can be null      */
name|List
argument_list|<
name|SearchCondition
argument_list|<
name|T
argument_list|>
argument_list|>
name|getConditions
parameter_list|()
function_decl|;
comment|/**      * Type of condition this SearchCondition represents      * @return condition type      */
name|ConditionType
name|getConditionType
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

