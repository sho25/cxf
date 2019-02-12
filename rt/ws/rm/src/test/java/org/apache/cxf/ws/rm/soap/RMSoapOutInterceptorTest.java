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
name|soap
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|MessageFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPMessage
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
name|binding
operator|.
name|soap
operator|.
name|SoapFault
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
name|soap
operator|.
name|SoapMessage
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
name|headers
operator|.
name|Header
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
name|ExchangeImpl
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
name|message
operator|.
name|MessageImpl
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
name|MessageUtils
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
name|AddressingProperties
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
name|Names
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
name|RM10Constants
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
name|RMConstants
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
name|RMContextUtils
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
name|RMProperties
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
name|SequenceFault
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
name|ObjectFactory
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|RMSoapOutInterceptorTest
block|{
specifier|private
specifier|static
specifier|final
name|Long
name|ONE
init|=
name|Long
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Long
name|TEN
init|=
name|Long
operator|.
name|valueOf
argument_list|(
literal|10
argument_list|)
decl_stmt|;
specifier|private
name|SequenceType
name|s1
decl_stmt|;
specifier|private
name|SequenceType
name|s2
decl_stmt|;
specifier|private
name|SequenceAcknowledgement
name|ack1
decl_stmt|;
specifier|private
name|SequenceAcknowledgement
name|ack2
decl_stmt|;
specifier|private
name|AckRequestedType
name|ar1
decl_stmt|;
specifier|private
name|AckRequestedType
name|ar2
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testGetUnderstoodHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|RMSoapOutInterceptor
name|codec
init|=
operator|new
name|RMSoapOutInterceptor
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|QName
argument_list|>
name|headers
init|=
name|codec
operator|.
name|getUnderstoodHeaders
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"expected Sequence header"
argument_list|,
name|headers
operator|.
name|contains
argument_list|(
name|RM10Constants
operator|.
name|SEQUENCE_QNAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"expected SequenceAcknowledgment header"
argument_list|,
name|headers
operator|.
name|contains
argument_list|(
name|RM10Constants
operator|.
name|SEQUENCE_ACK_QNAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"expected AckRequested header"
argument_list|,
name|headers
operator|.
name|contains
argument_list|(
name|RM10Constants
operator|.
name|ACK_REQUESTED_QNAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncode
parameter_list|()
throws|throws
name|Exception
block|{
name|RMSoapOutInterceptor
name|codec
init|=
operator|new
name|RMSoapOutInterceptor
argument_list|()
decl_stmt|;
name|setUpOutbound
argument_list|()
expr_stmt|;
name|SoapMessage
name|message
init|=
name|setupOutboundMessage
argument_list|()
decl_stmt|;
comment|// no RM headers
name|codec
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|verifyHeaders
argument_list|(
name|message
argument_list|,
operator|new
name|String
index|[]
block|{}
argument_list|)
expr_stmt|;
comment|// one sequence header
name|message
operator|=
name|setupOutboundMessage
argument_list|()
expr_stmt|;
name|RMProperties
name|rmps
init|=
name|RMContextUtils
operator|.
name|retrieveRMProperties
argument_list|(
name|message
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|rmps
operator|.
name|setSequence
argument_list|(
name|s1
argument_list|)
expr_stmt|;
name|codec
operator|.
name|encode
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|verifyHeaders
argument_list|(
name|message
argument_list|,
operator|new
name|String
index|[]
block|{
name|RMConstants
operator|.
name|SEQUENCE_NAME
block|}
argument_list|)
expr_stmt|;
comment|// one acknowledgment header
name|message
operator|=
name|setupOutboundMessage
argument_list|()
expr_stmt|;
name|rmps
operator|=
name|RMContextUtils
operator|.
name|retrieveRMProperties
argument_list|(
name|message
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|SequenceAcknowledgement
argument_list|>
name|acks
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|acks
operator|.
name|add
argument_list|(
name|ack1
argument_list|)
expr_stmt|;
name|rmps
operator|.
name|setAcks
argument_list|(
name|acks
argument_list|)
expr_stmt|;
name|codec
operator|.
name|encode
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|verifyHeaders
argument_list|(
name|message
argument_list|,
operator|new
name|String
index|[]
block|{
name|RMConstants
operator|.
name|SEQUENCE_ACK_NAME
block|}
argument_list|)
expr_stmt|;
comment|// two acknowledgment headers
name|message
operator|=
name|setupOutboundMessage
argument_list|()
expr_stmt|;
name|rmps
operator|=
name|RMContextUtils
operator|.
name|retrieveRMProperties
argument_list|(
name|message
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|acks
operator|.
name|add
argument_list|(
name|ack2
argument_list|)
expr_stmt|;
name|rmps
operator|.
name|setAcks
argument_list|(
name|acks
argument_list|)
expr_stmt|;
name|codec
operator|.
name|encode
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|verifyHeaders
argument_list|(
name|message
argument_list|,
operator|new
name|String
index|[]
block|{
name|RMConstants
operator|.
name|SEQUENCE_ACK_NAME
block|,
name|RMConstants
operator|.
name|SEQUENCE_ACK_NAME
block|}
argument_list|)
expr_stmt|;
comment|// one ack requested header
name|message
operator|=
name|setupOutboundMessage
argument_list|()
expr_stmt|;
name|rmps
operator|=
name|RMContextUtils
operator|.
name|retrieveRMProperties
argument_list|(
name|message
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|AckRequestedType
argument_list|>
name|requested
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|requested
operator|.
name|add
argument_list|(
name|ar1
argument_list|)
expr_stmt|;
name|rmps
operator|.
name|setAcksRequested
argument_list|(
name|requested
argument_list|)
expr_stmt|;
name|codec
operator|.
name|encode
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|verifyHeaders
argument_list|(
name|message
argument_list|,
operator|new
name|String
index|[]
block|{
name|RMConstants
operator|.
name|ACK_REQUESTED_NAME
block|}
argument_list|)
expr_stmt|;
comment|// two ack requested headers
name|message
operator|=
name|setupOutboundMessage
argument_list|()
expr_stmt|;
name|rmps
operator|=
name|RMContextUtils
operator|.
name|retrieveRMProperties
argument_list|(
name|message
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|requested
operator|.
name|add
argument_list|(
name|ar2
argument_list|)
expr_stmt|;
name|rmps
operator|.
name|setAcksRequested
argument_list|(
name|requested
argument_list|)
expr_stmt|;
name|codec
operator|.
name|encode
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|verifyHeaders
argument_list|(
name|message
argument_list|,
operator|new
name|String
index|[]
block|{
name|RMConstants
operator|.
name|ACK_REQUESTED_NAME
block|,
name|RMConstants
operator|.
name|ACK_REQUESTED_NAME
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncodeFault
parameter_list|()
throws|throws
name|Exception
block|{
name|RMSoapOutInterceptor
name|codec
init|=
operator|new
name|RMSoapOutInterceptor
argument_list|()
decl_stmt|;
name|setUpOutbound
argument_list|()
expr_stmt|;
name|SoapMessage
name|message
init|=
name|setupOutboundFaultMessage
argument_list|()
decl_stmt|;
comment|// no RM headers and no fault
name|codec
operator|.
name|encode
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|verifyHeaders
argument_list|(
name|message
argument_list|,
operator|new
name|String
index|[]
block|{}
argument_list|)
expr_stmt|;
comment|// fault is not a SoapFault
name|message
operator|=
name|setupOutboundFaultMessage
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|MessageUtils
operator|.
name|isFault
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|Exception
name|ex
init|=
operator|new
name|RuntimeException
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
name|ex
argument_list|)
expr_stmt|;
name|codec
operator|.
name|encode
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|verifyHeaders
argument_list|(
name|message
argument_list|,
operator|new
name|String
index|[]
block|{}
argument_list|)
expr_stmt|;
comment|// fault is a SoapFault but does not have a SequenceFault cause
name|message
operator|=
name|setupOutboundFaultMessage
argument_list|()
expr_stmt|;
name|SoapFault
name|f
init|=
operator|new
name|SoapFault
argument_list|(
literal|"REASON"
argument_list|,
name|RM10Constants
operator|.
name|UNKNOWN_SEQUENCE_FAULT_QNAME
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
name|f
argument_list|)
expr_stmt|;
name|codec
operator|.
name|encode
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|verifyHeaders
argument_list|(
name|message
argument_list|,
operator|new
name|String
index|[]
block|{}
argument_list|)
expr_stmt|;
comment|// fault is a SoapFault and has a SequenceFault cause
name|message
operator|=
name|setupOutboundFaultMessage
argument_list|()
expr_stmt|;
name|SequenceFault
name|sf
init|=
operator|new
name|SequenceFault
argument_list|(
literal|"REASON"
argument_list|)
decl_stmt|;
name|sf
operator|.
name|setFaultCode
argument_list|(
name|RM10Constants
operator|.
name|UNKNOWN_SEQUENCE_FAULT_QNAME
argument_list|)
expr_stmt|;
name|Identifier
name|sid
init|=
operator|new
name|Identifier
argument_list|()
decl_stmt|;
name|sid
operator|.
name|setValue
argument_list|(
literal|"SID"
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setSender
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|f
operator|.
name|initCause
argument_list|(
name|sf
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
name|f
argument_list|)
expr_stmt|;
name|codec
operator|.
name|encode
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|verifyHeaders
argument_list|(
name|message
argument_list|,
operator|new
name|String
index|[]
block|{
name|RMConstants
operator|.
name|SEQUENCE_FAULT_NAME
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setUpOutbound
parameter_list|()
block|{
name|ObjectFactory
name|factory
init|=
operator|new
name|ObjectFactory
argument_list|()
decl_stmt|;
name|s1
operator|=
name|factory
operator|.
name|createSequenceType
argument_list|()
expr_stmt|;
name|Identifier
name|sid
init|=
name|factory
operator|.
name|createIdentifier
argument_list|()
decl_stmt|;
name|sid
operator|.
name|setValue
argument_list|(
literal|"sequence1"
argument_list|)
expr_stmt|;
name|s1
operator|.
name|setIdentifier
argument_list|(
name|sid
argument_list|)
expr_stmt|;
name|s1
operator|.
name|setMessageNumber
argument_list|(
name|ONE
argument_list|)
expr_stmt|;
name|s2
operator|=
name|factory
operator|.
name|createSequenceType
argument_list|()
expr_stmt|;
name|sid
operator|=
name|factory
operator|.
name|createIdentifier
argument_list|()
expr_stmt|;
name|sid
operator|.
name|setValue
argument_list|(
literal|"sequence2"
argument_list|)
expr_stmt|;
name|s2
operator|.
name|setIdentifier
argument_list|(
name|sid
argument_list|)
expr_stmt|;
name|s2
operator|.
name|setMessageNumber
argument_list|(
name|TEN
argument_list|)
expr_stmt|;
name|ack1
operator|=
name|factory
operator|.
name|createSequenceAcknowledgement
argument_list|()
expr_stmt|;
name|SequenceAcknowledgement
operator|.
name|AcknowledgementRange
name|r
init|=
name|factory
operator|.
name|createSequenceAcknowledgementAcknowledgementRange
argument_list|()
decl_stmt|;
name|r
operator|.
name|setLower
argument_list|(
name|ONE
argument_list|)
expr_stmt|;
name|r
operator|.
name|setUpper
argument_list|(
name|ONE
argument_list|)
expr_stmt|;
name|ack1
operator|.
name|getAcknowledgementRange
argument_list|()
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|ack1
operator|.
name|setIdentifier
argument_list|(
name|s1
operator|.
name|getIdentifier
argument_list|()
argument_list|)
expr_stmt|;
name|ack2
operator|=
name|factory
operator|.
name|createSequenceAcknowledgement
argument_list|()
expr_stmt|;
name|r
operator|=
name|factory
operator|.
name|createSequenceAcknowledgementAcknowledgementRange
argument_list|()
expr_stmt|;
name|r
operator|.
name|setLower
argument_list|(
name|ONE
argument_list|)
expr_stmt|;
name|r
operator|.
name|setUpper
argument_list|(
name|TEN
argument_list|)
expr_stmt|;
name|ack2
operator|.
name|getAcknowledgementRange
argument_list|()
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|ack2
operator|.
name|setIdentifier
argument_list|(
name|s2
operator|.
name|getIdentifier
argument_list|()
argument_list|)
expr_stmt|;
name|ar1
operator|=
name|factory
operator|.
name|createAckRequestedType
argument_list|()
expr_stmt|;
name|ar1
operator|.
name|setIdentifier
argument_list|(
name|s1
operator|.
name|getIdentifier
argument_list|()
argument_list|)
expr_stmt|;
name|ar2
operator|=
name|factory
operator|.
name|createAckRequestedType
argument_list|()
expr_stmt|;
name|ar2
operator|.
name|setIdentifier
argument_list|(
name|s2
operator|.
name|getIdentifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|SoapMessage
name|setupOutboundMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|SoapMessage
name|soapMessage
init|=
operator|new
name|SoapMessage
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|RMProperties
name|rmps
init|=
operator|new
name|RMProperties
argument_list|()
decl_stmt|;
name|rmps
operator|.
name|exposeAs
argument_list|(
name|RM10Constants
operator|.
name|NAMESPACE_URI
argument_list|)
expr_stmt|;
name|RMContextUtils
operator|.
name|storeRMProperties
argument_list|(
name|soapMessage
argument_list|,
name|rmps
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|AddressingProperties
name|maps
init|=
operator|new
name|AddressingProperties
argument_list|()
decl_stmt|;
name|RMContextUtils
operator|.
name|storeMAPs
argument_list|(
name|maps
argument_list|,
name|soapMessage
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setOutMessage
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|setExchange
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|MessageFactory
name|factory
init|=
name|MessageFactory
operator|.
name|newInstance
argument_list|(
name|SOAPConstants
operator|.
name|SOAP_1_1_PROTOCOL
argument_list|)
decl_stmt|;
name|SOAPMessage
name|soap
init|=
name|factory
operator|.
name|createMessage
argument_list|()
decl_stmt|;
name|QName
name|bodyName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org"
argument_list|,
literal|"dummy"
argument_list|,
literal|"d"
argument_list|)
decl_stmt|;
name|soap
operator|.
name|getSOAPBody
argument_list|()
operator|.
name|addBodyElement
argument_list|(
name|bodyName
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|setContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|,
name|soap
argument_list|)
expr_stmt|;
return|return
name|soapMessage
return|;
block|}
specifier|private
name|SoapMessage
name|setupOutboundFaultMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|RMProperties
name|rmps
init|=
operator|new
name|RMProperties
argument_list|()
decl_stmt|;
name|rmps
operator|.
name|exposeAs
argument_list|(
name|RM10Constants
operator|.
name|NAMESPACE_URI
argument_list|)
expr_stmt|;
name|RMContextUtils
operator|.
name|storeRMProperties
argument_list|(
name|message
argument_list|,
name|rmps
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|AddressingProperties
name|maps
init|=
operator|new
name|AddressingProperties
argument_list|()
decl_stmt|;
name|RMContextUtils
operator|.
name|storeMAPs
argument_list|(
name|maps
argument_list|,
name|message
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setInMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|message
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
name|SoapMessage
name|soapMessage
init|=
operator|new
name|SoapMessage
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|ex
operator|.
name|setOutFaultMessage
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|setExchange
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|MessageFactory
name|factory
init|=
name|MessageFactory
operator|.
name|newInstance
argument_list|(
name|SOAPConstants
operator|.
name|SOAP_1_1_PROTOCOL
argument_list|)
decl_stmt|;
name|SOAPMessage
name|soap
init|=
name|factory
operator|.
name|createMessage
argument_list|()
decl_stmt|;
name|soap
operator|.
name|getSOAPBody
argument_list|()
operator|.
name|addFault
argument_list|()
expr_stmt|;
name|soapMessage
operator|.
name|setContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|,
name|soap
argument_list|)
expr_stmt|;
return|return
name|soapMessage
return|;
block|}
specifier|private
name|void
name|verifyHeaders
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|String
modifier|...
name|names
parameter_list|)
block|{
name|List
argument_list|<
name|Header
argument_list|>
name|headers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|message
operator|.
name|getHeaders
argument_list|()
argument_list|)
decl_stmt|;
comment|// check all expected headers are present
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|boolean
name|found
init|=
literal|false
decl_stmt|;
name|Iterator
argument_list|<
name|Header
argument_list|>
name|iter
init|=
name|headers
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Header
name|header
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Object
name|obj
init|=
name|header
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|String
name|namespace
init|=
name|header
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|String
name|localName
init|=
name|header
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|elem
init|=
operator|(
name|Element
operator|)
name|obj
decl_stmt|;
name|namespace
operator|=
name|elem
operator|.
name|getNamespaceURI
argument_list|()
expr_stmt|;
name|localName
operator|=
name|elem
operator|.
name|getLocalName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|RM10Constants
operator|.
name|NAMESPACE_URI
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
operator|&&
name|localName
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
name|iter
operator|.
name|remove
argument_list|()
expr_stmt|;
break|break;
block|}
elseif|else
if|if
condition|(
name|Names
operator|.
name|WSA_NAMESPACE_NAME
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
operator|&&
name|localName
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
name|iter
operator|.
name|remove
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
name|assertTrue
argument_list|(
literal|"Could not find header element "
operator|+
name|name
argument_list|,
name|found
argument_list|)
expr_stmt|;
block|}
comment|// no other headers should be present
name|assertTrue
argument_list|(
name|headers
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

