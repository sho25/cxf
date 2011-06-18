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

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|v200702
operator|.
name|AckRequestedType
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
name|v200702
operator|.
name|CreateSequenceResponseType
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
name|v200702
operator|.
name|CreateSequenceType
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
name|v200702
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
name|v200702
operator|.
name|SequenceAcknowledgement
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
name|v200702
operator|.
name|SequenceType
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
name|v200702
operator|.
name|TerminateSequenceType
import|;
end_import

begin_comment
comment|/**  * Interface for converting WS-ReliableMessaging structures to and from XML. Implementations of this interface  * provide version-specific encoding and decoding.  */
end_comment

begin_interface
specifier|public
interface|interface
name|EncoderDecoder
block|{
comment|/**      * Get the WS-ReliableMessaging namespace used by this encoder/decoder.      *       * @return URI      */
name|String
name|getWSRMNamespace
parameter_list|()
function_decl|;
comment|/**      * Get the WS-Addressing namespace used by this encoder/decoder.      *       * @return URI      */
name|String
name|getWSANamespace
parameter_list|()
function_decl|;
comment|/**      * Get the WS-ReliableMessaging constants used by this encoder/decoder.      *       * @return      */
name|RMConstants
name|getConstants
parameter_list|()
function_decl|;
comment|/**      * Get the class used for the CreateSequenceType.      *       * @return class      */
name|Class
name|getCreateSequenceType
parameter_list|()
function_decl|;
comment|/**      * Get the class used for the CreateSequenceResponseType.      *       * @return class      */
name|Class
name|getCreateSequenceResponseType
parameter_list|()
function_decl|;
comment|/**      * Get the class used for the TerminateSequenceType.      *       * @return class      */
name|Class
name|getTerminateSequenceType
parameter_list|()
function_decl|;
comment|/**      * Builds an element containing WS-RM headers. This adds the appropriate WS-RM and WS-A namespace      * declarations to the element, and then adds any WS-RM headers set in the supplied properties as child      * elements.      *       * @param rmps      * @param qname constructed element name      * @return element      */
name|Element
name|buildHeaders
parameter_list|(
name|RMProperties
name|rmps
parameter_list|,
name|QName
name|qname
parameter_list|)
throws|throws
name|JAXBException
function_decl|;
comment|/**      * Builds an element containing a WS-RM Fault. This adds the appropriate WS-RM namespace declaration to      * the element, and then adds the Fault as a child element.      *       * @param sf      * @param qname constructed element name      * @return element      */
name|Element
name|buildHeaderFault
parameter_list|(
name|SequenceFault
name|sf
parameter_list|,
name|QName
name|qname
parameter_list|)
throws|throws
name|JAXBException
function_decl|;
comment|/**      * Marshals a SequenceAcknowledgement to the appropriate external form.      *       * @param ack      * @return element      * @throws JAXBException      */
name|Element
name|encodeSequenceAcknowledgement
parameter_list|(
name|SequenceAcknowledgement
name|ack
parameter_list|)
throws|throws
name|JAXBException
function_decl|;
comment|/**      * Marshals an Identifier to the appropriate external form.      *       * @param id      * @return element      * @throws JAXBException      */
name|Element
name|encodeIdentifier
parameter_list|(
name|Identifier
name|id
parameter_list|)
throws|throws
name|JAXBException
function_decl|;
comment|/**      * Unmarshals a SequenceType, converting it if necessary to the internal form.      *       * @param elem      * @return      * @throws JAXBException      */
name|SequenceType
name|decodeSequenceType
parameter_list|(
name|Element
name|elem
parameter_list|)
throws|throws
name|JAXBException
function_decl|;
comment|/**      * Unmarshals a SequenceAcknowledgement, converting it if necessary to the internal form.      *       * @param elem      * @return      * @throws JAXBException      */
name|SequenceAcknowledgement
name|decodeSequenceAcknowledgement
parameter_list|(
name|Element
name|elem
parameter_list|)
throws|throws
name|JAXBException
function_decl|;
comment|/**      * Unmarshals a AckRequestedType, converting it if necessary to the internal form.      *       * @param elem      * @return      * @throws JAXBException      */
name|AckRequestedType
name|decodeAckRequestedType
parameter_list|(
name|Element
name|elem
parameter_list|)
throws|throws
name|JAXBException
function_decl|;
comment|/**      * Convert a CreateSequence message to the correct format for transmission.      *       * @param create      * @return converted      */
name|Object
name|convertToSend
parameter_list|(
name|CreateSequenceType
name|create
parameter_list|)
function_decl|;
comment|/**      * Convert a CreateSequenceResponse message to the correct format for transmission.      *       * @param create      * @return converted      */
name|Object
name|convertToSend
parameter_list|(
name|CreateSequenceResponseType
name|create
parameter_list|)
function_decl|;
comment|/**      * Convert a TerminateSequence message to the correct format for transmission.      *       * @param term      * @return converted      */
name|Object
name|convertToSend
parameter_list|(
name|TerminateSequenceType
name|term
parameter_list|)
function_decl|;
comment|/**      * Convert a received TerminateSequence message to internal form.      *       * @param term      * @return converted      */
name|TerminateSequenceType
name|convertReceivedTerminateSequence
parameter_list|(
name|Object
name|term
parameter_list|)
function_decl|;
comment|/**      * Convert a received CreateSequence message to internal form.      *       * @param create      * @return converted      */
name|CreateSequenceType
name|convertReceivedCreateSequence
parameter_list|(
name|Object
name|create
parameter_list|)
function_decl|;
comment|/**      * Convert a received CreateSequenceResponse message to internal form.      *       * @param create      * @return converted      */
name|CreateSequenceResponseType
name|convertReceivedCreateSequenceResponse
parameter_list|(
name|Object
name|create
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

