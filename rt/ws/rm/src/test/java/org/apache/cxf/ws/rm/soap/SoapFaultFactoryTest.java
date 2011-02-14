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
name|binding
operator|.
name|soap
operator|.
name|Soap11
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
name|Soap12
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
name|SoapBinding
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
name|SequenceFaultType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|classextension
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|classextension
operator|.
name|IMocksControl
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|SoapFaultFactoryTest
extends|extends
name|Assert
block|{
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|SequenceFault
name|sf
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
block|}
name|SequenceFault
name|setupSequenceFault
parameter_list|(
name|boolean
name|isSender
parameter_list|,
name|QName
name|code
parameter_list|,
name|Object
name|detail
parameter_list|)
block|{
name|sf
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|SequenceFault
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|sf
operator|.
name|getReason
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"reason"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|sf
operator|.
name|isSender
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|isSender
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|sf
operator|.
name|getSubCode
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|code
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|detail
condition|)
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|sf
operator|.
name|getDetail
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|detail
argument_list|)
expr_stmt|;
name|SequenceFaultType
name|sft
init|=
operator|new
name|SequenceFaultType
argument_list|()
decl_stmt|;
name|sft
operator|.
name|setFaultCode
argument_list|(
name|RMConstants
operator|.
name|getUnknownSequenceFaultCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sf
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|createSoap11Fault
parameter_list|()
block|{
name|SoapBinding
name|sb
init|=
name|control
operator|.
name|createMock
argument_list|(
name|SoapBinding
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|sb
operator|.
name|getSoapVersion
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Soap11
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|setupSequenceFault
argument_list|(
literal|false
argument_list|,
name|RMConstants
operator|.
name|getSequenceTerminatedFaultCode
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|SoapFaultFactory
name|factory
init|=
operator|new
name|SoapFaultFactory
argument_list|(
name|sb
argument_list|)
decl_stmt|;
name|SoapFault
name|fault
init|=
operator|(
name|SoapFault
operator|)
name|factory
operator|.
name|createFault
argument_list|(
name|sf
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"reason"
argument_list|,
name|fault
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Soap11
operator|.
name|getInstance
argument_list|()
operator|.
name|getReceiver
argument_list|()
argument_list|,
name|fault
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RMConstants
operator|.
name|getSequenceTerminatedFaultCode
argument_list|()
argument_list|,
name|fault
operator|.
name|getSubCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|fault
operator|.
name|getDetail
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|sf
argument_list|,
name|fault
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|createSoap12Fault
parameter_list|()
block|{
name|SoapBinding
name|sb
init|=
name|control
operator|.
name|createMock
argument_list|(
name|SoapBinding
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|sb
operator|.
name|getSoapVersion
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|Identifier
name|id
init|=
operator|new
name|Identifier
argument_list|()
decl_stmt|;
name|id
operator|.
name|setValue
argument_list|(
literal|"sid"
argument_list|)
expr_stmt|;
name|setupSequenceFault
argument_list|(
literal|true
argument_list|,
name|RMConstants
operator|.
name|getUnknownSequenceFaultCode
argument_list|()
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|SoapFaultFactory
name|factory
init|=
operator|new
name|SoapFaultFactory
argument_list|(
name|sb
argument_list|)
decl_stmt|;
name|SoapFault
name|fault
init|=
operator|(
name|SoapFault
operator|)
name|factory
operator|.
name|createFault
argument_list|(
name|sf
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"reason"
argument_list|,
name|fault
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|,
name|fault
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RMConstants
operator|.
name|getUnknownSequenceFaultCode
argument_list|()
argument_list|,
name|fault
operator|.
name|getSubCode
argument_list|()
argument_list|)
expr_stmt|;
name|Element
name|elem
init|=
name|fault
operator|.
name|getDetail
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|RMConstants
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|elem
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Identifier"
argument_list|,
name|elem
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|fault
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|createSoap12FaultWithIdentifierDetail
parameter_list|()
block|{
name|SoapBinding
name|sb
init|=
name|control
operator|.
name|createMock
argument_list|(
name|SoapBinding
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|sb
operator|.
name|getSoapVersion
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|Identifier
name|id
init|=
operator|new
name|Identifier
argument_list|()
decl_stmt|;
name|id
operator|.
name|setValue
argument_list|(
literal|"sid"
argument_list|)
expr_stmt|;
name|setupSequenceFault
argument_list|(
literal|true
argument_list|,
name|RMConstants
operator|.
name|getUnknownSequenceFaultCode
argument_list|()
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|SoapFaultFactory
name|factory
init|=
operator|new
name|SoapFaultFactory
argument_list|(
name|sb
argument_list|)
decl_stmt|;
name|SoapFault
name|fault
init|=
operator|(
name|SoapFault
operator|)
name|factory
operator|.
name|createFault
argument_list|(
name|sf
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"reason"
argument_list|,
name|fault
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|,
name|fault
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RMConstants
operator|.
name|getUnknownSequenceFaultCode
argument_list|()
argument_list|,
name|fault
operator|.
name|getSubCode
argument_list|()
argument_list|)
expr_stmt|;
name|Element
name|elem
init|=
name|fault
operator|.
name|getDetail
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|RMConstants
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|elem
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Identifier"
argument_list|,
name|elem
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|createSoap12FaultWithAcknowledgementDetail
parameter_list|()
block|{
name|SoapBinding
name|sb
init|=
name|control
operator|.
name|createMock
argument_list|(
name|SoapBinding
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|sb
operator|.
name|getSoapVersion
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|SequenceAcknowledgement
name|ack
init|=
operator|new
name|SequenceAcknowledgement
argument_list|()
decl_stmt|;
name|Identifier
name|id
init|=
operator|new
name|Identifier
argument_list|()
decl_stmt|;
name|id
operator|.
name|setValue
argument_list|(
literal|"sid"
argument_list|)
expr_stmt|;
name|ack
operator|.
name|setIdentifier
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|SequenceAcknowledgement
operator|.
name|AcknowledgementRange
name|range
init|=
operator|new
name|SequenceAcknowledgement
operator|.
name|AcknowledgementRange
argument_list|()
decl_stmt|;
name|range
operator|.
name|setLower
argument_list|(
operator|new
name|Long
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|range
operator|.
name|setUpper
argument_list|(
operator|new
name|Long
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|ack
operator|.
name|getAcknowledgementRange
argument_list|()
operator|.
name|add
argument_list|(
name|range
argument_list|)
expr_stmt|;
name|setupSequenceFault
argument_list|(
literal|true
argument_list|,
name|RMConstants
operator|.
name|getInvalidAcknowledgmentFaultCode
argument_list|()
argument_list|,
name|ack
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|SoapFaultFactory
name|factory
init|=
operator|new
name|SoapFaultFactory
argument_list|(
name|sb
argument_list|)
decl_stmt|;
name|SoapFault
name|fault
init|=
operator|(
name|SoapFault
operator|)
name|factory
operator|.
name|createFault
argument_list|(
name|sf
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"reason"
argument_list|,
name|fault
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|,
name|fault
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RMConstants
operator|.
name|getInvalidAcknowledgmentFaultCode
argument_list|()
argument_list|,
name|fault
operator|.
name|getSubCode
argument_list|()
argument_list|)
expr_stmt|;
name|Element
name|elem
init|=
name|fault
operator|.
name|getDetail
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|RMConstants
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|elem
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"SequenceAcknowledgement"
argument_list|,
name|elem
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|createSoap12FaultWithoutDetail
parameter_list|()
block|{
name|SoapBinding
name|sb
init|=
name|control
operator|.
name|createMock
argument_list|(
name|SoapBinding
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|sb
operator|.
name|getSoapVersion
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|setupSequenceFault
argument_list|(
literal|true
argument_list|,
name|RMConstants
operator|.
name|getCreateSequenceRefusedFaultCode
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|SoapFaultFactory
name|factory
init|=
operator|new
name|SoapFaultFactory
argument_list|(
name|sb
argument_list|)
decl_stmt|;
name|SoapFault
name|fault
init|=
operator|(
name|SoapFault
operator|)
name|factory
operator|.
name|createFault
argument_list|(
name|sf
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"reason"
argument_list|,
name|fault
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|,
name|fault
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RMConstants
operator|.
name|getCreateSequenceRefusedFaultCode
argument_list|()
argument_list|,
name|fault
operator|.
name|getSubCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|fault
operator|.
name|getDetail
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|SoapBinding
name|sb
init|=
name|control
operator|.
name|createMock
argument_list|(
name|SoapBinding
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|sb
operator|.
name|getSoapVersion
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Soap11
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|SoapFault
name|fault
init|=
name|control
operator|.
name|createMock
argument_list|(
name|SoapFault
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|fault
operator|.
name|getReason
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"r"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|fault
operator|.
name|getFaultCode
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|QName
argument_list|(
literal|"ns"
argument_list|,
literal|"code"
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|fault
operator|.
name|getSubCode
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|QName
argument_list|(
literal|"ns"
argument_list|,
literal|"subcode"
argument_list|)
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|SoapFaultFactory
name|factory
init|=
operator|new
name|SoapFaultFactory
argument_list|(
name|sb
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Reason: r, code: {ns}code, subCode: {ns}subcode"
argument_list|,
name|factory
operator|.
name|toString
argument_list|(
name|fault
argument_list|)
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

