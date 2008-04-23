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
name|binding
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
name|endpoint
operator|.
name|Endpoint
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
name|service
operator|.
name|Service
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
name|service
operator|.
name|model
operator|.
name|BindingInfo
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
name|transport
operator|.
name|Destination
import|;
end_import

begin_comment
comment|/**  * A factory interface for creating Bindings from BindingInfo metadata.  */
end_comment

begin_interface
specifier|public
interface|interface
name|BindingFactory
block|{
comment|/**      * Create a Binding from the BindingInfo metadata.      *       * @param binding      * @return the Binding object      */
name|Binding
name|createBinding
parameter_list|(
name|BindingInfo
name|binding
parameter_list|)
function_decl|;
comment|/**      * Create a "default" BindingInfo object for the service. Can return a subclass.       * @param service      * @param namespace      * @param configObject - binding specific configuration object      * @return the BindingInfo object      */
name|BindingInfo
name|createBindingInfo
parameter_list|(
name|Service
name|service
parameter_list|,
name|String
name|namespace
parameter_list|,
name|Object
name|configObject
parameter_list|)
function_decl|;
comment|/**      * Set the destionation's message observer which is created by using the endpoint to      * listen the incoming message      * @param d the destination that will be set the MessageObserver       * @param e the endpoint to build up the MessageObserver            */
name|void
name|addListener
parameter_list|(
name|Destination
name|d
parameter_list|,
name|Endpoint
name|e
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

