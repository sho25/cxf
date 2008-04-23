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
name|addressing
package|;
end_package

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

begin_comment
comment|/**  * Encapsulation of version-specific WS-Addressing constants.  */
end_comment

begin_interface
specifier|public
interface|interface
name|AddressingConstants
block|{
comment|/**      * @return namespace defined by the normative WS-Addressing Core schema      */
name|String
name|getNamespaceURI
parameter_list|()
function_decl|;
comment|/**      * @return namespace defined by the normative WS-Addressing WSDL bindings      * schema      */
name|String
name|getWSDLNamespaceURI
parameter_list|()
function_decl|;
comment|/**      * @return QName of the WSDL extensiblity element      */
name|QName
name|getWSDLExtensibilityQName
parameter_list|()
function_decl|;
comment|/**      * @return QName of the wsaw:Action element      */
name|QName
name|getWSDLActionQName
parameter_list|()
function_decl|;
comment|/**      * @return Anonymous address URI      */
name|String
name|getAnonymousURI
parameter_list|()
function_decl|;
comment|/**      * @return None address URI      */
name|String
name|getNoneURI
parameter_list|()
function_decl|;
comment|/**      * @return QName of the From addressing header      */
name|QName
name|getFromQName
parameter_list|()
function_decl|;
comment|/**      * @return QName of the To addressing header      */
name|QName
name|getToQName
parameter_list|()
function_decl|;
comment|/**      * @return QName of the ReplyTo addressing header      */
name|QName
name|getReplyToQName
parameter_list|()
function_decl|;
comment|/**      * @return QName of the FaultTo addressing header      */
name|QName
name|getFaultToQName
parameter_list|()
function_decl|;
comment|/**      * @return QName of the Action addressing header      */
name|QName
name|getActionQName
parameter_list|()
function_decl|;
comment|/**      * @return QName of the MessageID addressing header      */
name|QName
name|getMessageIDQName
parameter_list|()
function_decl|;
comment|/**      * @return Default value for RelationshipType indicating a reply       * to the related message      */
name|String
name|getRelationshipReply
parameter_list|()
function_decl|;
comment|/**      * @return QName of the RelatesTo addressing header      */
name|QName
name|getRelatesToQName
parameter_list|()
function_decl|;
comment|/**      * @return QName of the Relationship addressing header      */
name|QName
name|getRelationshipTypeQName
parameter_list|()
function_decl|;
comment|/**      * @return QName of the Metadata      */
name|QName
name|getMetadataQName
parameter_list|()
function_decl|;
comment|/**      * @return QName of the Address      */
name|QName
name|getAddressQName
parameter_list|()
function_decl|;
comment|/**      * @return package name of the implementation      */
name|String
name|getPackageName
parameter_list|()
function_decl|;
comment|/**      * @return QName of the reference parameter marker      */
name|QName
name|getIsReferenceParameterQName
parameter_list|()
function_decl|;
comment|/**      * @return QName of the Invalid Message Addressing Property fault subcode      */
name|QName
name|getInvalidMapQName
parameter_list|()
function_decl|;
comment|/**      * @return QName of the Message Addressing Property Required fault subcode      */
name|QName
name|getMapRequiredQName
parameter_list|()
function_decl|;
comment|/**      * @return QName of the Destination Unreachable fault subcode      */
name|QName
name|getDestinationUnreachableQName
parameter_list|()
function_decl|;
comment|/**      * @return QName of the Action Not Supported fault subcode      */
name|QName
name|getActionNotSupportedQName
parameter_list|()
function_decl|;
comment|/**      * @return QName of the Endpoint Unavailable fault subcode      */
name|QName
name|getEndpointUnavailableQName
parameter_list|()
function_decl|;
comment|/**      * @return Default Fault Action      */
name|String
name|getDefaultFaultAction
parameter_list|()
function_decl|;
comment|/**      * @return Action Not Supported text      */
name|String
name|getActionNotSupportedText
parameter_list|()
function_decl|;
comment|/**      * @return Destination Unreachable text      */
name|String
name|getDestinationUnreachableText
parameter_list|()
function_decl|;
comment|/**      * @return Endpoint Unavailable text      */
name|String
name|getEndpointUnavailableText
parameter_list|()
function_decl|;
comment|/**      * @return Invalid Message Addressing Property text      */
name|String
name|getInvalidMapText
parameter_list|()
function_decl|;
comment|/**      * @return Message Addressing Property Required text      */
name|String
name|getMapRequiredText
parameter_list|()
function_decl|;
comment|/**      * @return Duplicate Message ID text      */
name|String
name|getDuplicateMessageIDText
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

