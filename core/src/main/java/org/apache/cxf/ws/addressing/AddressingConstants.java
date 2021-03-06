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
name|java
operator|.
name|util
operator|.
name|ResourceBundle
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
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|BundleUtils
import|;
end_import

begin_comment
comment|/**  * Encapsulation of version-specific WS-Addressing constants.  */
end_comment

begin_class
specifier|public
class|class
name|AddressingConstants
block|{
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|AddressingConstants
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|AddressingConstants
parameter_list|()
block|{     }
comment|/**      * @return namespace defined by the normative WS-Addressing Core schema      */
specifier|public
name|String
name|getNamespaceURI
parameter_list|()
block|{
return|return
name|Names
operator|.
name|WSA_NAMESPACE_NAME
return|;
block|}
comment|/**      * @return namespace defined by the normative WS-Addressing WSDL bindings      * schema      */
specifier|public
name|String
name|getWSDLNamespaceURI
parameter_list|()
block|{
return|return
name|Names
operator|.
name|WSA_NAMESPACE_WSDL_NAME
return|;
block|}
comment|/**      * @return QName of the WSDL extensiblity element      */
specifier|public
name|QName
name|getWSDLExtensibilityQName
parameter_list|()
block|{
return|return
name|Names
operator|.
name|WSAW_USING_ADDRESSING_QNAME
return|;
block|}
comment|/**      * @return QName of the wsaw:Action element      */
specifier|public
name|QName
name|getWSDLActionQName
parameter_list|()
block|{
return|return
name|Names
operator|.
name|WSAW_ACTION_QNAME
return|;
block|}
comment|/**      * @return Anonymous address URI      */
specifier|public
name|String
name|getAnonymousURI
parameter_list|()
block|{
return|return
name|Names
operator|.
name|WSA_ANONYMOUS_ADDRESS
return|;
block|}
comment|/**      * @return None address URI      */
specifier|public
name|String
name|getNoneURI
parameter_list|()
block|{
return|return
name|Names
operator|.
name|WSA_NONE_ADDRESS
return|;
block|}
comment|/**      * @return QName of the From addressing header      */
specifier|public
name|QName
name|getFromQName
parameter_list|()
block|{
return|return
name|Names
operator|.
name|WSA_FROM_QNAME
return|;
block|}
comment|/**      * @return QName of the To addressing header      */
specifier|public
name|QName
name|getToQName
parameter_list|()
block|{
return|return
name|Names
operator|.
name|WSA_TO_QNAME
return|;
block|}
comment|/**      * @return QName of the ReplyTo addressing header      */
specifier|public
name|QName
name|getReplyToQName
parameter_list|()
block|{
return|return
name|Names
operator|.
name|WSA_REPLYTO_QNAME
return|;
block|}
comment|/**      * @return QName of the FaultTo addressing header      */
specifier|public
name|QName
name|getFaultToQName
parameter_list|()
block|{
return|return
name|Names
operator|.
name|WSA_FAULTTO_QNAME
return|;
block|}
comment|/**      * @return QName of the Action addressing header      */
specifier|public
name|QName
name|getActionQName
parameter_list|()
block|{
return|return
name|Names
operator|.
name|WSA_ACTION_QNAME
return|;
block|}
comment|/**      * @return QName of the MessageID addressing header      */
specifier|public
name|QName
name|getMessageIDQName
parameter_list|()
block|{
return|return
name|Names
operator|.
name|WSA_MESSAGEID_QNAME
return|;
block|}
comment|/**      * @return Default value for RelationshipType indicating a reply      * to the related message      */
specifier|public
name|String
name|getRelationshipReply
parameter_list|()
block|{
return|return
name|Names
operator|.
name|WSA_RELATIONSHIP_REPLY
return|;
block|}
comment|/**      * @return QName of the RelatesTo addressing header      */
specifier|public
name|QName
name|getRelatesToQName
parameter_list|()
block|{
return|return
name|Names
operator|.
name|WSA_RELATESTO_QNAME
return|;
block|}
comment|/**      * @return QName of the Relationship addressing header      */
specifier|public
name|QName
name|getRelationshipTypeQName
parameter_list|()
block|{
return|return
name|Names
operator|.
name|WSA_RELATIONSHIPTYPE_QNAME
return|;
block|}
comment|/**      * @return QName of the Metadata      */
specifier|public
name|QName
name|getMetadataQName
parameter_list|()
block|{
return|return
name|Names
operator|.
name|WSA_METADATA_QNAME
return|;
block|}
comment|/**      * @return QName of the Address      */
specifier|public
name|QName
name|getAddressQName
parameter_list|()
block|{
return|return
name|Names
operator|.
name|WSA_ADDRESS_QNAME
return|;
block|}
comment|/**      * @return QName of the reference parameter marker      */
specifier|public
name|QName
name|getIsReferenceParameterQName
parameter_list|()
block|{
return|return
name|Names
operator|.
name|WSA_IS_REFERENCE_PARAMETER_QNAME
return|;
block|}
comment|/**      * @return QName of the Invalid Message Addressing Property fault subcode      */
specifier|public
name|QName
name|getInvalidMapQName
parameter_list|()
block|{
return|return
name|Names
operator|.
name|INVALID_MAP_QNAME
return|;
block|}
comment|/**      * @return QName of the Message Addressing Property Required fault subcode      */
specifier|public
name|QName
name|getMapRequiredQName
parameter_list|()
block|{
return|return
name|Names
operator|.
name|MAP_REQUIRED_QNAME
return|;
block|}
comment|/**      * @return QName of the Destination Unreachable fault subcode      */
specifier|public
name|QName
name|getDestinationUnreachableQName
parameter_list|()
block|{
return|return
name|Names
operator|.
name|DESTINATION_UNREACHABLE_QNAME
return|;
block|}
comment|/**      * @return QName of the Action Not Supported fault subcode      */
specifier|public
name|QName
name|getActionNotSupportedQName
parameter_list|()
block|{
return|return
name|Names
operator|.
name|ACTION_NOT_SUPPORTED_QNAME
return|;
block|}
comment|/**      * @return QName of the Endpoint Unavailable fault subcode      */
specifier|public
name|QName
name|getEndpointUnavailableQName
parameter_list|()
block|{
return|return
name|Names
operator|.
name|ENDPOINT_UNAVAILABLE_QNAME
return|;
block|}
comment|/**      * @return Default Fault Action      */
specifier|public
name|String
name|getDefaultFaultAction
parameter_list|()
block|{
return|return
name|Names
operator|.
name|WSA_DEFAULT_FAULT_ACTION
return|;
block|}
comment|/**      * @return Action Not Supported text      */
specifier|public
name|String
name|getActionNotSupportedText
parameter_list|()
block|{
return|return
name|BUNDLE
operator|.
name|getString
argument_list|(
literal|"ACTION_NOT_SUPPORTED_MSG"
argument_list|)
return|;
block|}
comment|/**      * @return Destination Unreachable text      */
specifier|public
name|String
name|getDestinationUnreachableText
parameter_list|()
block|{
return|return
name|BUNDLE
operator|.
name|getString
argument_list|(
literal|"DESTINATION_UNREACHABLE_MSG"
argument_list|)
return|;
block|}
comment|/**      * @return Endpoint Unavailable text      */
specifier|public
name|String
name|getEndpointUnavailableText
parameter_list|()
block|{
return|return
name|BUNDLE
operator|.
name|getString
argument_list|(
literal|"ENDPOINT_UNAVAILABLE_MSG"
argument_list|)
return|;
block|}
comment|/**      * @return Invalid Message Addressing Property text      */
specifier|public
name|String
name|getInvalidMapText
parameter_list|()
block|{
return|return
name|BUNDLE
operator|.
name|getString
argument_list|(
literal|"INVALID_MAP_MSG"
argument_list|)
return|;
block|}
comment|/**      * @return Message Addressing Property Required text      */
specifier|public
name|String
name|getMapRequiredText
parameter_list|()
block|{
return|return
name|BUNDLE
operator|.
name|getString
argument_list|(
literal|"MAP_REQUIRED_MSG"
argument_list|)
return|;
block|}
comment|/**      * @return Duplicate Message ID text      */
specifier|public
name|String
name|getDuplicateMessageIDText
parameter_list|()
block|{
return|return
name|BUNDLE
operator|.
name|getString
argument_list|(
literal|"DUPLICATE_MESSAGE_ID_MSG"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

