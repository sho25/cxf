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
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|InterceptorProvider
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
name|neethi
operator|.
name|Assertion
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|PolicyInterceptorProvider
extends|extends
name|InterceptorProvider
block|{
comment|/**      * Returns a collection of QNames describing the xml schema types of the assertions that      * this interceptor implements.      *      * @return collection of QNames of known assertion types      */
name|Collection
argument_list|<
name|QName
argument_list|>
name|getAssertionTypes
parameter_list|()
function_decl|;
comment|/**      * Return false if the message does not contain enough contextual configuration to preemtively      * support the given assertion.  Otherwise, return true.  If false, the PolicyEngine.supportsAlternative      * method will not select this policy and will attempt a different alternative.      *      * Example: If the context does not contain login information, an assertion that requires it      * could return false to allow the Alternative selection algorithms to try a different alternative.      * @param msg The contextual message, may be null if no message is in context at this point      * @param assertion      * @return      */
name|boolean
name|configurationPresent
parameter_list|(
name|Message
name|msg
parameter_list|,
name|Assertion
name|assertion
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

