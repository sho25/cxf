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
name|io
operator|.
name|InputStream
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
name|stream
operator|.
name|XMLInputFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
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
name|BusFactory
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|ReadHeadersInterceptor
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
name|interceptor
operator|.
name|StartBodyInterceptor
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
name|SequenceAcknowledgement
operator|.
name|AcknowledgementRange
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
name|assertEquals
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
name|assertNotNull
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
name|assertNull
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
name|RMSoapInInterceptorTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SEQ_IDENTIFIER
init|=
literal|"http://Business456.com/RM/ABC"
decl_stmt|;
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
name|MSG1_MESSAGE_NUMBER
init|=
name|ONE
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Long
name|MSG2_MESSAGE_NUMBER
init|=
name|Long
operator|.
name|valueOf
argument_list|(
literal|2
argument_list|)
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
name|RMSoapInInterceptor
name|codec
init|=
operator|new
name|RMSoapInInterceptor
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
name|testDecodeSequence
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|SoapMessage
name|message
init|=
name|setUpInboundMessage
argument_list|(
literal|"resources/Message1.xml"
argument_list|)
decl_stmt|;
name|RMSoapInInterceptor
name|codec
init|=
operator|new
name|RMSoapInInterceptor
argument_list|()
decl_stmt|;
name|codec
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
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
literal|false
argument_list|)
decl_stmt|;
name|SequenceType
name|st
init|=
name|rmps
operator|.
name|getSequence
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|st
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|st
operator|.
name|getIdentifier
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|,
name|SEQ_IDENTIFIER
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|st
operator|.
name|getMessageNumber
argument_list|()
argument_list|,
name|MSG1_MESSAGE_NUMBER
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|rmps
operator|.
name|getAcks
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|rmps
operator|.
name|getAcksRequested
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDecodeAcknowledgements
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|SoapMessage
name|message
init|=
name|setUpInboundMessage
argument_list|(
literal|"resources/Acknowledgment.xml"
argument_list|)
decl_stmt|;
name|RMSoapInInterceptor
name|codec
init|=
operator|new
name|RMSoapInInterceptor
argument_list|()
decl_stmt|;
name|codec
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
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
literal|false
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|SequenceAcknowledgement
argument_list|>
name|acks
init|=
name|rmps
operator|.
name|getAcks
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|acks
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|acks
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SequenceAcknowledgement
name|ack
init|=
name|acks
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|ack
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ack
operator|.
name|getIdentifier
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|,
name|SEQ_IDENTIFIER
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|ack
operator|.
name|getAcknowledgementRange
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|AcknowledgementRange
name|r1
init|=
name|ack
operator|.
name|getAcknowledgementRange
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|AcknowledgementRange
name|r2
init|=
name|ack
operator|.
name|getAcknowledgementRange
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|verifyRange
argument_list|(
name|r1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|verifyRange
argument_list|(
name|r2
argument_list|,
literal|3
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|rmps
operator|.
name|getSequence
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|rmps
operator|.
name|getAcksRequested
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDecodeAcknowledgements2
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|SoapMessage
name|message
init|=
name|setUpInboundMessage
argument_list|(
literal|"resources/Acknowledgment2.xml"
argument_list|)
decl_stmt|;
name|RMSoapInInterceptor
name|codec
init|=
operator|new
name|RMSoapInInterceptor
argument_list|()
decl_stmt|;
name|codec
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
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
literal|false
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|SequenceAcknowledgement
argument_list|>
name|acks
init|=
name|rmps
operator|.
name|getAcks
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|acks
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|acks
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SequenceAcknowledgement
name|ack
init|=
name|acks
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|ack
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ack
operator|.
name|getAcknowledgementRange
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|AcknowledgementRange
name|r1
init|=
name|ack
operator|.
name|getAcknowledgementRange
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|verifyRange
argument_list|(
name|r1
argument_list|,
literal|1
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|rmps
operator|.
name|getSequence
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|rmps
operator|.
name|getAcksRequested
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifyRange
parameter_list|(
name|AcknowledgementRange
name|r
parameter_list|,
name|int
name|i
parameter_list|,
name|int
name|j
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|r
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|assertNotNull
argument_list|(
name|r
operator|.
name|getLower
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|i
argument_list|,
name|r
operator|.
name|getLower
argument_list|()
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|j
operator|>
literal|0
condition|)
block|{
name|assertNotNull
argument_list|(
name|r
operator|.
name|getUpper
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|j
argument_list|,
name|r
operator|.
name|getUpper
argument_list|()
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDecodeAcksRequested
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|SoapMessage
name|message
init|=
name|setUpInboundMessage
argument_list|(
literal|"resources/Retransmission.xml"
argument_list|)
decl_stmt|;
name|RMSoapInInterceptor
name|codec
init|=
operator|new
name|RMSoapInInterceptor
argument_list|()
decl_stmt|;
name|codec
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
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
literal|false
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|AckRequestedType
argument_list|>
name|requested
init|=
name|rmps
operator|.
name|getAcksRequested
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|requested
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|requested
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|AckRequestedType
name|ar
init|=
name|requested
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|ar
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ar
operator|.
name|getIdentifier
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|,
name|SEQ_IDENTIFIER
argument_list|)
expr_stmt|;
name|SequenceType
name|s
init|=
name|rmps
operator|.
name|getSequence
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|s
operator|.
name|getIdentifier
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|,
name|SEQ_IDENTIFIER
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|s
operator|.
name|getMessageNumber
argument_list|()
argument_list|,
name|MSG2_MESSAGE_NUMBER
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|rmps
operator|.
name|getAcks
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|SoapMessage
name|setUpInboundMessage
parameter_list|(
name|String
name|resource
parameter_list|)
throws|throws
name|XMLStreamException
block|{
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
name|soapMessage
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|SCHEMA_VALIDATION_ENABLED
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|RMSoapInInterceptorTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|resource
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|XMLStreamReader
name|reader
init|=
name|XMLInputFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createXMLStreamReader
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|soapMessage
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|reader
argument_list|)
expr_stmt|;
name|ReadHeadersInterceptor
name|rji
init|=
operator|new
name|ReadHeadersInterceptor
argument_list|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|)
decl_stmt|;
name|rji
operator|.
name|handleMessage
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
name|StartBodyInterceptor
name|sbi
init|=
operator|new
name|StartBodyInterceptor
argument_list|()
decl_stmt|;
name|sbi
operator|.
name|handleMessage
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
return|return
name|soapMessage
return|;
block|}
block|}
end_class

end_unit

