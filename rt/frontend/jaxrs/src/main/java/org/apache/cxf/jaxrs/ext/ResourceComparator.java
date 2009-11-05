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
name|jaxrs
operator|.
name|model
operator|.
name|ClassResourceInfo
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
name|jaxrs
operator|.
name|model
operator|.
name|OperationResourceInfo
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

begin_comment
comment|/**  * Can be used to affect the way the JAXRS selection algorithm chooses  * between multiple matching resource classes and methods   *  */
end_comment

begin_interface
specifier|public
interface|interface
name|ResourceComparator
block|{
comment|/**      * Compares two resource classes      * @param cri1 First resource class      * @param cri2 Second resource class      * @param message incoming message      * @return -1 if cri1< cri2, 1 if if cri1> cri2, 0 otherwise       */
name|int
name|compare
parameter_list|(
name|ClassResourceInfo
name|cri1
parameter_list|,
name|ClassResourceInfo
name|cri2
parameter_list|,
name|Message
name|message
parameter_list|)
function_decl|;
comment|/**      * Compares two resource methods      * @param oper1 First resource method      * @param oper2 Second resource method      * @param message incoming message      * @return -1 if oper1< oper2, 1 if if oper1> oper2, 0 otherwise       */
name|int
name|compare
parameter_list|(
name|OperationResourceInfo
name|oper1
parameter_list|,
name|OperationResourceInfo
name|oper2
parameter_list|,
name|Message
name|message
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

