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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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

begin_comment
comment|/**  * Holder for WS-Addressing names (of headers, namespaces etc.).  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|Names
block|{
specifier|public
specifier|static
specifier|final
name|String
name|WSA_NAMESPACE_NAME
init|=
literal|"http://www.w3.org/2005/08/addressing"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_NAMESPACE_WSDL_NAME
init|=
literal|"http://www.w3.org/2006/05/addressing/wsdl"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_NAMESPACE_WSDL_NAME_OLD
init|=
literal|"http://www.w3.org/2005/02/addressing/wsdl"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_NAMESPACE_WSDL_METADATA
init|=
literal|"http://www.w3.org/2007/05/addressing/metadata"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_NAMESPACE_PATTERN
init|=
literal|"/addressing"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_REFERENCE_PARAMETERS_NAME
init|=
literal|"ReferenceParameters"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSA_REFERENCE_PARAMETERS_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|WSA_REFERENCE_PARAMETERS_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_IS_REFERENCE_PARAMETER_NAME
init|=
literal|"IsReferenceParameter"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSA_IS_REFERENCE_PARAMETER_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|WSA_IS_REFERENCE_PARAMETER_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_ADDRESS_NAME
init|=
literal|"Address"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSA_ADDRESS_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|WSA_ADDRESS_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_METADATA_NAME
init|=
literal|"Metadata"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSA_METADATA_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|WSA_METADATA_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_FROM_NAME
init|=
literal|"From"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSA_FROM_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|WSA_FROM_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_TO_NAME
init|=
literal|"To"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSA_TO_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|WSA_TO_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_REPLYTO_NAME
init|=
literal|"ReplyTo"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSA_REPLYTO_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|WSA_REPLYTO_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_FAULTTO_NAME
init|=
literal|"FaultTo"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSA_FAULTTO_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
literal|"FaultTo"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_ACTION_NAME
init|=
literal|"Action"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSA_ACTION_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|WSA_ACTION_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_MESSAGEID_NAME
init|=
literal|"MessageID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSA_MESSAGEID_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|WSA_MESSAGEID_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_REPLY_NAME
init|=
literal|"reply"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_RELATIONSHIP_DELIMITER
init|=
literal|"/"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_RELATIONSHIP_REPLY
init|=
name|WSA_NAMESPACE_NAME
operator|+
name|WSA_RELATIONSHIP_DELIMITER
operator|+
name|WSA_REPLY_NAME
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_RELATESTO_NAME
init|=
literal|"RelatesTo"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSA_RELATESTO_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|WSA_RELATESTO_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_RELATIONSHIPTYPE_NAME
init|=
literal|"RelationshipType"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSA_RELATIONSHIPTYPE_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|WSA_RELATIONSHIPTYPE_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_ANONYMOUS_ADDRESS
init|=
name|WSA_NAMESPACE_NAME
operator|+
literal|"/anonymous"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_NONE_ADDRESS
init|=
name|WSA_NAMESPACE_NAME
operator|+
literal|"/none"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_UNSPECIFIED_RELATIONSHIP
init|=
name|WSA_NAMESPACE_NAME
operator|+
literal|"/unspecified"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_FAULT_DELIMITER
init|=
literal|"/fault"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_DEFAULT_FAULT_ACTION
init|=
name|WSA_NAMESPACE_NAME
operator|+
name|WSA_FAULT_DELIMITER
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSAW_ACTION_NAME
init|=
literal|"Action"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSAW_ACTION_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_WSDL_NAME
argument_list|,
name|WSAW_ACTION_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSAW_USING_ADDRESSING_NAME
init|=
literal|"UsingAddressing"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSAW_USING_ADDRESSING_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_WSDL_NAME
argument_list|,
name|WSAW_USING_ADDRESSING_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSDL_INSTANCE_NAMESPACE_NAME
init|=
literal|"http://www.w3.org/2004/08/wsdl-instance"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INVALID_MAP_NAME
init|=
literal|"InvalidMessageAddressingProperty"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|INVALID_MAP_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|INVALID_MAP_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MAP_REQUIRED_NAME
init|=
literal|"MessageAddressingPropertyRequired"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|MAP_REQUIRED_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|MAP_REQUIRED_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DESTINATION_UNREACHABLE_NAME
init|=
literal|"DestinationUnreachable"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|DESTINATION_UNREACHABLE_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|DESTINATION_UNREACHABLE_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_NOT_SUPPORTED_NAME
init|=
literal|"ActionNotSupported"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ACTION_NOT_SUPPORTED_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|ACTION_NOT_SUPPORTED_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ENDPOINT_UNAVAILABLE_NAME
init|=
literal|"EndpointUnavailable"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ENDPOINT_UNAVAILABLE_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|ENDPOINT_UNAVAILABLE_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DUPLICATE_MESSAGE_ID_NAME
init|=
literal|"DuplicateMessageID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|DUPLICATE_MESSAGE_ID_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|DUPLICATE_MESSAGE_ID_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_MISMATCH_NAME
init|=
literal|"ActionMismatch"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ACTION_MISMATCH_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|ACTION_MISMATCH_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_REQUIRED_NAME
init|=
literal|"MessageAddressingHeaderRequired"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|HEADER_REQUIRED_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|HEADER_REQUIRED_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ONLY_ANONYMOUS_ADDRESS_SUPPORTED_NAME
init|=
literal|"OnlyAnonymousAddressSupported"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ONLY_ANONYMOUS_ADDRESS_SUPPORTED_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|ONLY_ANONYMOUS_ADDRESS_SUPPORTED_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ONLY_NONANONYMOUS_ADDRESS_SUPPORTED_NAME
init|=
literal|"OnlyNonAnonymousAddressSupported"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ONLY_NONANONYMOUS_ADDRESS_SUPPORTED_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|ONLY_NONANONYMOUS_ADDRESS_SUPPORTED_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INVALID_CARDINALITY_NAME
init|=
literal|"InvalidCardinality"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|INVALID_CARDINALITY_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSA_NAMESPACE_NAME
argument_list|,
name|INVALID_CARDINALITY_NAME
argument_list|)
decl_stmt|;
comment|/**      * The set of headers understood by the protocol binding.      */
specifier|public
specifier|static
specifier|final
name|Set
argument_list|<
name|QName
argument_list|>
name|HEADERS
decl_stmt|;
static|static
block|{
name|Set
argument_list|<
name|QName
argument_list|>
name|headers
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|add
argument_list|(
name|WSA_FROM_QNAME
argument_list|)
expr_stmt|;
name|headers
operator|.
name|add
argument_list|(
name|WSA_TO_QNAME
argument_list|)
expr_stmt|;
name|headers
operator|.
name|add
argument_list|(
name|WSA_REPLYTO_QNAME
argument_list|)
expr_stmt|;
name|headers
operator|.
name|add
argument_list|(
name|WSA_FAULTTO_QNAME
argument_list|)
expr_stmt|;
name|headers
operator|.
name|add
argument_list|(
name|WSA_ACTION_QNAME
argument_list|)
expr_stmt|;
name|headers
operator|.
name|add
argument_list|(
name|WSA_MESSAGEID_QNAME
argument_list|)
expr_stmt|;
name|HEADERS
operator|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|headers
argument_list|)
expr_stmt|;
block|}
comment|/**      * Prevents instantiation.      */
specifier|private
name|Names
parameter_list|()
block|{     }
block|}
end_class

end_unit

