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
name|rm
package|;
end_package

begin_comment
comment|/**  * A container for WS-RM message constants.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|RMMessageConstants
block|{
comment|/**      * Used to cache outbound RM properties in message.      */
specifier|public
specifier|static
specifier|final
name|String
name|RM_PROPERTIES_OUTBOUND
init|=
literal|"org.apache.cxf.ws.rm.outbound"
decl_stmt|;
comment|/**      * Used to cache inbound RM properties in message.      */
specifier|public
specifier|static
specifier|final
name|String
name|RM_PROPERTIES_INBOUND
init|=
literal|"org.apache.cxf.ws.rm.inbound"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ORIGINAL_REQUESTOR_ROLE
init|=
literal|"org.apache.cxf.client.original"
decl_stmt|;
comment|/** Message content must be an instance of {@link CachedOutputStream}. */
specifier|public
specifier|static
specifier|final
name|String
name|SAVED_CONTENT
init|=
literal|"org.apache.cxf.ws.rm.content"
decl_stmt|;
comment|/** Variable holds reference to source streams of the attachments.      * It must be an instance of {@link Closeable}. */
specifier|public
specifier|static
specifier|final
name|String
name|ATTACHMENTS_CLOSEABLE
init|=
literal|"org.apache.cxf.ws.rm.attachment.closeable"
decl_stmt|;
comment|/** Retransmission in progress flag (Boolean.TRUE if in progress). */
specifier|public
specifier|static
specifier|final
name|String
name|RM_RETRANSMISSION
init|=
literal|"org.apache.cxf.ws.rm.retransmitting"
decl_stmt|;
comment|/** Boolean property TRUE for a chain used to capture a message. */
specifier|public
specifier|static
specifier|final
name|String
name|MESSAGE_CAPTURE
init|=
literal|"org.apache.cxf.rm.capture"
decl_stmt|;
comment|/** Client callback (must be instance of {@link MessageCallback}). */
specifier|public
specifier|static
specifier|final
name|String
name|RM_CLIENT_CALLBACK
init|=
literal|"org.apache.cxf.rm.clientCallback"
decl_stmt|;
comment|/** Mode for requesting acknowledgements ({@link org.apache.cxf.ws.rm.managerAckRequestMode} value,      *  overrides SourcePolicy configuration). */
specifier|public
specifier|static
specifier|final
name|String
name|ACK_REQUEST_MODE
init|=
literal|"org.apache.cxf.rm.ackRequestMode"
decl_stmt|;
specifier|static
specifier|final
name|String
name|RM_PROTOCOL_VARIATION
init|=
literal|"org.apache.cxf.ws.rm.protocol"
decl_stmt|;
comment|// keep this constant in the ws-rm package until it finds a general use outside of ws-rm
specifier|static
specifier|final
name|String
name|DELIVERING_ROBUST_ONEWAY
init|=
literal|"org.apache.cxf.oneway.robust.delivering"
decl_stmt|;
comment|/**      * Prevents instantiation.      */
specifier|private
name|RMMessageConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

