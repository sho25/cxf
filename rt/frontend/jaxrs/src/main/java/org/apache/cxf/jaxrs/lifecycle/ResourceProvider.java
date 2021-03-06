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
name|lifecycle
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
name|message
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * ResourceProvider controls the life-cycle of the JAX-RS root resources.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ResourceProvider
block|{
comment|/**      * Returns the resource instance which will handle the current request      * @param m the current request message      * @return resource instance      */
name|Object
name|getInstance
parameter_list|(
name|Message
name|m
parameter_list|)
function_decl|;
comment|/**      * Releases the resource instance if needed      * @param m the current request message      * @param o resource instance      */
name|void
name|releaseInstance
parameter_list|(
name|Message
name|m
parameter_list|,
name|Object
name|o
parameter_list|)
function_decl|;
comment|/**      * Returns the Class of the resource      * @return      */
name|Class
argument_list|<
name|?
argument_list|>
name|getResourceClass
parameter_list|()
function_decl|;
comment|/**      * Indicates if the managed resource is a singleton      * @return      */
name|boolean
name|isSingleton
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

