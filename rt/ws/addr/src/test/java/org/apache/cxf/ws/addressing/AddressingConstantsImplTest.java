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
name|impl
operator|.
name|AddressingConstantsImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|AddressingConstantsImplTest
extends|extends
name|Assert
block|{
specifier|private
name|AddressingConstants
name|constants
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|constants
operator|=
operator|new
name|AddressingConstantsImpl
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetNamespaceURI
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
literal|"http://www.w3.org/2005/08/addressing"
argument_list|,
name|constants
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetWSDLNamespaceURI
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
literal|"http://www.w3.org/2006/05/addressing/wsdl"
argument_list|,
name|constants
operator|.
name|getWSDLNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetWSDLExtensibility
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2006/05/addressing/wsdl"
argument_list|,
literal|"UsingAddressing"
argument_list|)
argument_list|,
name|constants
operator|.
name|getWSDLExtensibilityQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetWSDLActionQName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2006/05/addressing/wsdl"
argument_list|,
literal|"Action"
argument_list|)
argument_list|,
name|constants
operator|.
name|getWSDLActionQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetAnonymousURI
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
literal|"http://www.w3.org/2005/08/addressing/anonymous"
argument_list|,
name|constants
operator|.
name|getAnonymousURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetNoneURI
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
literal|"http://www.w3.org/2005/08/addressing/none"
argument_list|,
name|constants
operator|.
name|getNoneURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetFromQName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2005/08/addressing"
argument_list|,
literal|"From"
argument_list|)
argument_list|,
name|constants
operator|.
name|getFromQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetToQName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2005/08/addressing"
argument_list|,
literal|"To"
argument_list|)
argument_list|,
name|constants
operator|.
name|getToQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetReplyToQName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2005/08/addressing"
argument_list|,
literal|"ReplyTo"
argument_list|)
argument_list|,
name|constants
operator|.
name|getReplyToQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetFaultToQName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2005/08/addressing"
argument_list|,
literal|"FaultTo"
argument_list|)
argument_list|,
name|constants
operator|.
name|getFaultToQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetActionQName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2005/08/addressing"
argument_list|,
literal|"Action"
argument_list|)
argument_list|,
name|constants
operator|.
name|getActionQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetMessageIDQName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2005/08/addressing"
argument_list|,
literal|"MessageID"
argument_list|)
argument_list|,
name|constants
operator|.
name|getMessageIDQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetRelationshipReply
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
literal|"http://www.w3.org/2005/08/addressing/reply"
argument_list|,
name|constants
operator|.
name|getRelationshipReply
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetRelatesToQName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2005/08/addressing"
argument_list|,
literal|"RelatesTo"
argument_list|)
argument_list|,
name|constants
operator|.
name|getRelatesToQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetRelationshipTypeQName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2005/08/addressing"
argument_list|,
literal|"RelationshipType"
argument_list|)
argument_list|,
name|constants
operator|.
name|getRelationshipTypeQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetMetadataQName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2005/08/addressing"
argument_list|,
literal|"Metadata"
argument_list|)
argument_list|,
name|constants
operator|.
name|getMetadataQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetAddressQName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2005/08/addressing"
argument_list|,
literal|"Address"
argument_list|)
argument_list|,
name|constants
operator|.
name|getAddressQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetIsReferenceParameterQName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2005/08/addressing"
argument_list|,
literal|"IsReferenceParameter"
argument_list|)
argument_list|,
name|constants
operator|.
name|getIsReferenceParameterQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetInvalidMapQName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2005/08/addressing"
argument_list|,
literal|"InvalidMessageAddressingProperty"
argument_list|)
argument_list|,
name|constants
operator|.
name|getInvalidMapQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMapRequiredQName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2005/08/addressing"
argument_list|,
literal|"MessageAddressingPropertyRequired"
argument_list|)
argument_list|,
name|constants
operator|.
name|getMapRequiredQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDestinationUnreachableQName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2005/08/addressing"
argument_list|,
literal|"DestinationUnreachable"
argument_list|)
argument_list|,
name|constants
operator|.
name|getDestinationUnreachableQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testActionNotSupportedQName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2005/08/addressing"
argument_list|,
literal|"ActionNotSupported"
argument_list|)
argument_list|,
name|constants
operator|.
name|getActionNotSupportedQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEndpointUnavailableQName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2005/08/addressing"
argument_list|,
literal|"EndpointUnavailable"
argument_list|)
argument_list|,
name|constants
operator|.
name|getEndpointUnavailableQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDefaultFaultAction
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
literal|"http://www.w3.org/2005/08/addressing/fault"
argument_list|,
name|constants
operator|.
name|getDefaultFaultAction
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testActionNotSupportedText
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
literal|"Action {0} not supported"
argument_list|,
name|constants
operator|.
name|getActionNotSupportedText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDestinationUnreachableText
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
literal|"Destination {0} unreachable"
argument_list|,
name|constants
operator|.
name|getDestinationUnreachableText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEndpointUnavailableText
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
literal|"Endpoint {0} unavailable"
argument_list|,
name|constants
operator|.
name|getEndpointUnavailableText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetInvalidMapText
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
literal|"Invalid Message Addressing Property {0}"
argument_list|,
name|constants
operator|.
name|getInvalidMapText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMapRequiredText
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
literal|"Message Addressing Property {0} required"
argument_list|,
name|constants
operator|.
name|getMapRequiredText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDuplicateMessageIDText
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected constant"
argument_list|,
literal|"Duplicate Message ID {0}"
argument_list|,
name|constants
operator|.
name|getDuplicateMessageIDText
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

