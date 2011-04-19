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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|datatype
operator|.
name|Duration
import|;
end_import

begin_comment
comment|/**  * Part of fluent interface of {@link SearchConditionBuilder}.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Property
block|{
comment|/** Is textual property equal to given literal or matching given pattern? */
name|CompleteCondition
name|equalTo
parameter_list|(
name|String
name|literalOrPattern
parameter_list|)
function_decl|;
comment|/** Is numeric property equal to given double number? */
name|CompleteCondition
name|equalTo
parameter_list|(
name|double
name|number
parameter_list|)
function_decl|;
comment|/** Is numeric property equal to given long number? */
name|CompleteCondition
name|equalTo
parameter_list|(
name|long
name|number
parameter_list|)
function_decl|;
comment|/** Is date property same as given date? */
name|CompleteCondition
name|equalTo
parameter_list|(
name|Date
name|date
parameter_list|)
function_decl|;
comment|/** Is date property same as date distant from now by given period of time? */
name|CompleteCondition
name|equalTo
parameter_list|(
name|Duration
name|distanceFromNow
parameter_list|)
function_decl|;
comment|/** Is textual property different than given literal or not matching given pattern? */
name|CompleteCondition
name|notEqualTo
parameter_list|(
name|String
name|literalOrPattern
parameter_list|)
function_decl|;
comment|/** Is numeric property different than given double number? */
name|CompleteCondition
name|notEqualTo
parameter_list|(
name|double
name|number
parameter_list|)
function_decl|;
comment|/** Is numeric property different than given long number? */
name|CompleteCondition
name|notEqualTo
parameter_list|(
name|long
name|number
parameter_list|)
function_decl|;
comment|/** Is date property different than given date? */
name|CompleteCondition
name|notEqualTo
parameter_list|(
name|Date
name|date
parameter_list|)
function_decl|;
comment|/** Is date property different than date distant from now by given period of time? */
name|CompleteCondition
name|notEqualTo
parameter_list|(
name|Duration
name|distanceFromNow
parameter_list|)
function_decl|;
comment|/** Is numeric property greater than given number? */
name|CompleteCondition
name|greaterThan
parameter_list|(
name|double
name|number
parameter_list|)
function_decl|;
comment|/** Is numeric property greater than given number? */
name|CompleteCondition
name|greaterThan
parameter_list|(
name|long
name|number
parameter_list|)
function_decl|;
comment|/** Is numeric property less than given number? */
name|CompleteCondition
name|lessThan
parameter_list|(
name|double
name|number
parameter_list|)
function_decl|;
comment|/** Is numeric property less than given number? */
name|CompleteCondition
name|lessThan
parameter_list|(
name|long
name|number
parameter_list|)
function_decl|;
comment|/** Is numeric property greater or equal to given number? */
name|CompleteCondition
name|greaterOrEqualTo
parameter_list|(
name|double
name|number
parameter_list|)
function_decl|;
comment|/** Is numeric property greater or equal to given number? */
name|CompleteCondition
name|greaterOrEqualTo
parameter_list|(
name|long
name|number
parameter_list|)
function_decl|;
comment|/** Is numeric property less or equal to given number? */
name|CompleteCondition
name|lessOrEqualTo
parameter_list|(
name|double
name|number
parameter_list|)
function_decl|;
comment|/** Is numeric property less or equal to given number? */
name|CompleteCondition
name|lessOrEqualTo
parameter_list|(
name|long
name|number
parameter_list|)
function_decl|;
comment|/** Is date property after (greater than) given date? */
name|CompleteCondition
name|after
parameter_list|(
name|Date
name|date
parameter_list|)
function_decl|;
comment|/** Is date property before (less than) given date? */
name|CompleteCondition
name|before
parameter_list|(
name|Date
name|date
parameter_list|)
function_decl|;
comment|/** Is date property not before (greater or equal to) given date? */
name|CompleteCondition
name|notBefore
parameter_list|(
name|Date
name|date
parameter_list|)
function_decl|;
comment|/** Is date property not after (less or equal to) given date? */
name|CompleteCondition
name|notAfter
parameter_list|(
name|Date
name|date
parameter_list|)
function_decl|;
comment|/** Is date property after (greater than) date distant from now by given period of time? */
name|CompleteCondition
name|after
parameter_list|(
name|Duration
name|distanceFromNow
parameter_list|)
function_decl|;
comment|/** Is date property before (less than) date distant from now by given period of time? */
name|CompleteCondition
name|before
parameter_list|(
name|Duration
name|distanceFromNow
parameter_list|)
function_decl|;
comment|/** Is date property not after (less or equal to) date distant from now by given period of time? */
name|CompleteCondition
name|notAfter
parameter_list|(
name|Duration
name|distanceFromNow
parameter_list|)
function_decl|;
comment|/**      * Is date property not before (greater or equal to) date distant from now by given period of time?      */
name|CompleteCondition
name|notBefore
parameter_list|(
name|Duration
name|distanceFromNow
parameter_list|)
function_decl|;
comment|/** Is textual property lexically after (greater than) given literal? */
name|CompleteCondition
name|lexicalAfter
parameter_list|(
name|String
name|literal
parameter_list|)
function_decl|;
comment|/** Is textual property lexically before (less than) given literal? */
name|CompleteCondition
name|lexicalBefore
parameter_list|(
name|String
name|literal
parameter_list|)
function_decl|;
comment|/** Is textual property lexically not before (greater or equal to) given literal? */
name|CompleteCondition
name|lexicalNotBefore
parameter_list|(
name|String
name|literal
parameter_list|)
function_decl|;
comment|/** Is textual property lexically not after (less or equal to) given literal? */
name|CompleteCondition
name|lexicalNotAfter
parameter_list|(
name|String
name|literal
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

