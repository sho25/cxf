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
name|endpoint
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
name|Exchange
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
name|cxf
operator|.
name|transport
operator|.
name|Conduit
import|;
end_import

begin_comment
comment|/**  * Strategy for retrieving a Conduit to mediate an outbound message.  * A specific instance implementing a particular strategy may be injected  * into the Client via config.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ConduitSelector
block|{
comment|/**      * Called prior to the interceptor chain being traversed.      * This is the point at which an eager strategy would retrieve      * a Conduit.      *       * @param message the current Message      */
name|void
name|prepare
parameter_list|(
name|Message
name|message
parameter_list|)
function_decl|;
comment|/**      * Called when a Conduit is actually required.      * This is the point at which a lazy strategy would retrieve      * a Conduit.      *       * @param message      * @return the Conduit to use for mediation of the message      */
name|Conduit
name|selectConduit
parameter_list|(
name|Message
name|message
parameter_list|)
function_decl|;
comment|/**      * Called on completion of the MEP for which the Conduit was required.      * This is the point at which a one-shot strategy would dispose of      * the Conduit.      *       * @param exchange represents the completed MEP      */
name|void
name|complete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * @return the encapsulated Endpoint      */
name|Endpoint
name|getEndpoint
parameter_list|()
function_decl|;
comment|/**      * @param endpoint the Endpoint to encapsulate      */
name|void
name|setEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

