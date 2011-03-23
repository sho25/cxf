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
operator|.
name|persistence
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|DestinationSequence
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
name|rm
operator|.
name|Identifier
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
name|rm
operator|.
name|SourceSequence
import|;
end_import

begin_interface
specifier|public
interface|interface
name|RMStore
block|{
comment|/**      * Create a source sequence in the persistent store, with the sequence attributes as specified in the      *<code>RMSourceSequence</code> object.      * @param seq the sequence      */
name|void
name|createSourceSequence
parameter_list|(
name|SourceSequence
name|seq
parameter_list|)
function_decl|;
comment|/**      * Create a destination sequence in the persistent store, with the sequence attributes as specified in the      *<code>RMSDestinationSequence</code> object.      * @param seq the sequence      */
name|void
name|createDestinationSequence
parameter_list|(
name|DestinationSequence
name|seq
parameter_list|)
function_decl|;
comment|/**      * Retrieve the source sequence with the specified identifier from persistent store.       * @param seq the sequence      * @return the sequence if present; otherwise null      */
name|SourceSequence
name|getSourceSequence
parameter_list|(
name|Identifier
name|seq
parameter_list|)
function_decl|;
comment|/**      * Retrieve the destination sequence with the specified identifier from persistent store.       * @param seq the sequence      * @return the sequence if present; otherwise null      */
name|DestinationSequence
name|getDestinationSequence
parameter_list|(
name|Identifier
name|seq
parameter_list|)
function_decl|;
comment|/**      * Remove the source sequence with the specified identifier from persistent store.       * @param seq the sequence      */
name|void
name|removeSourceSequence
parameter_list|(
name|Identifier
name|seq
parameter_list|)
function_decl|;
comment|/**      * Remove the destination sequence with the specified identifier from persistent store.       * @param seq the sequence      */
name|void
name|removeDestinationSequence
parameter_list|(
name|Identifier
name|seq
parameter_list|)
function_decl|;
comment|/**      * Retrieves all sequences managed by the identified RM source endpoint       * from persistent store.      *       * @param endpointIdentifier the identifier for the source      * @return the collection of sequences      */
name|Collection
argument_list|<
name|SourceSequence
argument_list|>
name|getSourceSequences
parameter_list|(
name|String
name|endpointIdentifier
parameter_list|)
function_decl|;
comment|/**      * Retrieves all sequences managed by the identified RM destination endpoint       * from persistent store.      *       * @param endpointIdentifier the identifier for the destination      * @return the collection of sequences      */
name|Collection
argument_list|<
name|DestinationSequence
argument_list|>
name|getDestinationSequences
parameter_list|(
name|String
name|endpointIdentifier
parameter_list|)
function_decl|;
comment|/**      * Retrieves the outbound/inbound messages stored for the source/destination sequence with       * the given identifier.      * @param sid the source sequence identifier      * @param outbound true if the message is outbound      * @return the collection of messages      * *       */
name|Collection
argument_list|<
name|RMMessage
argument_list|>
name|getMessages
parameter_list|(
name|Identifier
name|sid
parameter_list|,
name|boolean
name|outbound
parameter_list|)
function_decl|;
comment|/**      * Called by an RM source upon processing an outbound message. The<code>RMMessage</code>      * parameter is null for non application (RM protocol) messages.      *       * @param seq the source sequence       * @param msg the outgoing message      */
name|void
name|persistOutgoing
parameter_list|(
name|SourceSequence
name|seq
parameter_list|,
name|RMMessage
name|msg
parameter_list|)
function_decl|;
comment|/**     * Called by an RM source upon processing an outbound message. The<code>RMMessage</code>      * parameter is null for non application (RM protocol) messages.      *      * @param seq the destination sequence     * @param msg the incoming message     */
name|void
name|persistIncoming
parameter_list|(
name|DestinationSequence
name|seq
parameter_list|,
name|RMMessage
name|msg
parameter_list|)
function_decl|;
comment|/**      * Removes the messages with the given message numbers and identifiers from the store of      * outbound/inbound messages.      *       * @param sid the identifier of the source sequence      * @param messageNrs the collection of message numbers      * @param outbound true if the message is outbound      */
name|void
name|removeMessages
parameter_list|(
name|Identifier
name|sid
parameter_list|,
name|Collection
argument_list|<
name|Long
argument_list|>
name|messageNrs
parameter_list|,
name|boolean
name|outbound
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

