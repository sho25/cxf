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
name|transfer
operator|.
name|dialect
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
name|ws
operator|.
name|transfer
operator|.
name|Create
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
name|transfer
operator|.
name|Delete
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
name|transfer
operator|.
name|Get
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
name|transfer
operator|.
name|Put
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
name|transfer
operator|.
name|Representation
import|;
end_import

begin_comment
comment|/**  * The interface for a Dialect objects.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Dialect
block|{
comment|/**      * Method for processing incoming Get message by Dialect extension.      * @param body Get body      * @param representation XML representation stored in the ResourceManager      * @return Representation, which will be returned in response.      */
name|Object
name|processGet
parameter_list|(
name|Get
name|body
parameter_list|,
name|Representation
name|representation
parameter_list|)
function_decl|;
comment|/**      * Method for processing incoming Put message by Dialect extension.      * @param body Put body      * @param representation XML representation stored in the ResourceManager      * @return Representation, which will be stored in ResourceManager.      */
name|Representation
name|processPut
parameter_list|(
name|Put
name|body
parameter_list|,
name|Representation
name|representation
parameter_list|)
function_decl|;
comment|/**      * Method for processing incoming Delete message by Dialect extension.      * @param body Delete body      * @param representation XML representation stored in the ResourceManager      * @return Representation, which will be stored in ResourceManager.      */
name|boolean
name|processDelete
parameter_list|(
name|Delete
name|body
parameter_list|,
name|Representation
name|representation
parameter_list|)
function_decl|;
comment|/**      * Method for processing incoming Create message by Dialect extension.      * @param body Create body      * @return Representation, which will be stored in ResourceManager.      */
name|Representation
name|processCreate
parameter_list|(
name|Create
name|body
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

