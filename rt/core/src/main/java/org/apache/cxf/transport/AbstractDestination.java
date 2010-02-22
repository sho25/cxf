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
name|transport
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|addressing
operator|.
name|EndpointReferenceType
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
name|wsdl
operator|.
name|EndpointReferenceUtils
import|;
end_import

begin_comment
comment|/**  * Abstract base class factoring out common Destination logic,   * allowing non-decoupled transports to be written without any  * regard for the decoupled back-channel or partial response logic.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractDestination
extends|extends
name|AbstractObservable
implements|implements
name|Destination
implements|,
name|DestinationWithEndpoint
block|{
specifier|protected
specifier|final
name|EndpointReferenceType
name|reference
decl_stmt|;
specifier|protected
specifier|final
name|EndpointInfo
name|endpointInfo
decl_stmt|;
specifier|protected
specifier|final
name|Bus
name|bus
decl_stmt|;
specifier|public
name|AbstractDestination
parameter_list|(
name|EndpointReferenceType
name|ref
parameter_list|,
name|EndpointInfo
name|ei
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|ref
argument_list|,
name|ei
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AbstractDestination
parameter_list|(
name|Bus
name|b
parameter_list|,
name|EndpointReferenceType
name|ref
parameter_list|,
name|EndpointInfo
name|ei
parameter_list|)
block|{
name|reference
operator|=
name|ref
expr_stmt|;
name|endpointInfo
operator|=
name|ei
expr_stmt|;
name|bus
operator|=
name|b
expr_stmt|;
block|}
comment|/**      * @return the reference associated with this Destination      */
specifier|public
name|EndpointReferenceType
name|getAddress
parameter_list|()
block|{
return|return
name|reference
return|;
block|}
comment|/**      * Retreive a back-channel Conduit, which must be policy-compatible      * with the current Message and associated Destination. For example      * compatible Quality of Protection must be asserted on the back-channel.      * This would generally only be an issue if the back-channel is decoupled.      *       * @param inMessage the current inbound message (null to indicate a       * disassociated back-channel)      * @param partialResponse in the decoupled case, this is expected to be the      * outbound Message to be sent over the in-built back-channel.       * @param address the backchannel address (null to indicate anonymous)      * @return a suitable Conduit      */
specifier|public
name|Conduit
name|getBackChannel
parameter_list|(
name|Message
name|inMessage
parameter_list|,
name|Message
name|partialResponse
parameter_list|,
name|EndpointReferenceType
name|address
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getInbuiltBackChannel
argument_list|(
name|inMessage
argument_list|)
return|;
block|}
comment|/**      * Shutdown the Destination, i.e. stop accepting incoming messages.      */
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
comment|// nothing to do by default
block|}
comment|/**      * @param inMessage the incoming message      * @return the inbuilt backchannel      */
specifier|protected
specifier|abstract
name|Conduit
name|getInbuiltBackChannel
parameter_list|(
name|Message
name|inMessage
parameter_list|)
function_decl|;
comment|/**      * Backchannel conduit.      */
specifier|protected
specifier|abstract
class|class
name|AbstractBackChannelConduit
extends|extends
name|AbstractConduit
block|{
specifier|public
name|AbstractBackChannelConduit
parameter_list|()
block|{
name|super
argument_list|(
name|EndpointReferenceUtils
operator|.
name|getAnonymousEndpointReference
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**          * Register a message observer for incoming messages.          *           * @param observer the observer to notify on receipt of incoming          */
specifier|public
name|void
name|setMessageObserver
parameter_list|(
name|MessageObserver
name|observer
parameter_list|)
block|{
comment|// shouldn't be called for a back channel conduit
block|}
specifier|protected
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|AbstractDestination
operator|.
name|this
operator|.
name|getLogger
argument_list|()
return|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|EndpointInfo
name|getEndpointInfo
parameter_list|()
block|{
return|return
name|endpointInfo
return|;
block|}
block|}
end_class

end_unit

