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
name|manager
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
name|addressing
operator|.
name|ReferenceParametersType
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
comment|/**  * Interface for managing resource representations.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ResourceManager
block|{
comment|/**      * Returns Representation object given by reference parameter.      * @param ref Reference parameter returned by create method.      * @return Representation object containing the XML resource.      * @see ResourceManager#create(org.apache.cxf.ws.transfer.Representation)       */
name|Representation
name|get
parameter_list|(
name|ReferenceParametersType
name|ref
parameter_list|)
function_decl|;
comment|/**      * Deletes Representation object given by reference parameter.      * @param ref Reference parameter returned by create method.      * @see ResourceManager#create(org.apache.cxf.ws.transfer.Representation)      */
name|void
name|delete
parameter_list|(
name|ReferenceParametersType
name|ref
parameter_list|)
function_decl|;
comment|/**      * Replaces Representation object given by reference parameter with newRepresentation.      * @param ref Reference parameter returned by create method.      * @param newRepresentation New Representation object, which will replace the old one.      * @see ResourceManager#create(org.apache.cxf.ws.transfer.Representation)      */
name|void
name|put
parameter_list|(
name|ReferenceParametersType
name|ref
parameter_list|,
name|Representation
name|newRepresentation
parameter_list|)
function_decl|;
comment|/**      * Creates new Representation object from initRepresenation.      * @param initRepresentation Representation object containing initial XML resource.      * @return Reference parameter for newly created Representation object.      */
name|ReferenceParametersType
name|create
parameter_list|(
name|Representation
name|initRepresentation
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

