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
name|message
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
name|Bus
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
name|binding
operator|.
name|Binding
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
name|BindingOperationInfo
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
name|Session
import|;
end_import

begin_interface
specifier|public
interface|interface
name|Exchange
extends|extends
name|StringMap
block|{
comment|/**      * Returns the inbound message for the exchange. On the client-side, this       * is the response. On the server-side, this is the request.      *       * @return the inboubnd message      */
name|Message
name|getInMessage
parameter_list|()
function_decl|;
name|void
name|setInMessage
parameter_list|(
name|Message
name|m
parameter_list|)
function_decl|;
comment|/**      * Returns the outbound message for the exchange. On the client-side, this       * is the request. On the server-side, this is the response. During the       * inbound message processing, the outbound message is null.      *       * @return the outbound message      */
name|Message
name|getOutMessage
parameter_list|()
function_decl|;
name|void
name|setOutMessage
parameter_list|(
name|Message
name|m
parameter_list|)
function_decl|;
name|Message
name|getInFaultMessage
parameter_list|()
function_decl|;
name|void
name|setInFaultMessage
parameter_list|(
name|Message
name|m
parameter_list|)
function_decl|;
name|Message
name|getOutFaultMessage
parameter_list|()
function_decl|;
name|void
name|setOutFaultMessage
parameter_list|(
name|Message
name|m
parameter_list|)
function_decl|;
name|Session
name|getSession
parameter_list|()
function_decl|;
comment|/**      * @return the associated incoming Destination (may be anonymous)      */
name|Destination
name|getDestination
parameter_list|()
function_decl|;
comment|/**      * @param destination the associated incoming Destination      */
name|void
name|setDestination
parameter_list|(
name|Destination
name|destination
parameter_list|)
function_decl|;
comment|/**      * @param message the associated message      * @return the associated outgoing Conduit (may be anonymous)      */
name|Conduit
name|getConduit
parameter_list|(
name|Message
name|message
parameter_list|)
function_decl|;
comment|/**      * @param conduit the associated outgoing Conduit       */
name|void
name|setConduit
parameter_list|(
name|Conduit
name|conduit
parameter_list|)
function_decl|;
comment|/**      * Determines if the exchange is one-way.      *       * @return true if the exchange is known to be a one-way exchange      */
name|boolean
name|isOneWay
parameter_list|()
function_decl|;
comment|/**      * Determines if the exchange requires the frontend to wait for a       * response. Transports can then optimize themselves to process the       * response immediately instead of using a background thread or similar.      *       * @return true if the frontend will wait for the response      */
name|boolean
name|isSynchronous
parameter_list|()
function_decl|;
name|void
name|setSynchronous
parameter_list|(
name|boolean
name|b
parameter_list|)
function_decl|;
comment|/**      *       * @param b true if the exchange is known to be a one-way exchange      */
name|void
name|setOneWay
parameter_list|(
name|boolean
name|b
parameter_list|)
function_decl|;
comment|/**      * {@inheritDoc}      */
name|void
name|clear
parameter_list|()
function_decl|;
name|Bus
name|getBus
parameter_list|()
function_decl|;
name|Service
name|getService
parameter_list|()
function_decl|;
name|Endpoint
name|getEndpoint
parameter_list|()
function_decl|;
name|Binding
name|getBinding
parameter_list|()
function_decl|;
name|BindingOperationInfo
name|getBindingOperationInfo
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

