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
name|namespace
operator|.
name|QName
import|;
end_import

begin_comment
comment|/**  * Holder for WS-RM names (of headers, namespaces etc.).  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|RMConstants
block|{
specifier|public
specifier|static
specifier|final
name|String
name|NAMESPACE_PREFIX
init|=
literal|"wsrm"
decl_stmt|;
comment|// WSDL
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_NAME
init|=
literal|"SequenceAbstractService"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INTERFACE_NAME
init|=
literal|"SequenceAbstractPortType"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BINDING_NAME
init|=
literal|"SequenceAbstractSoapBinding"
decl_stmt|;
comment|// element and header names
specifier|public
specifier|static
specifier|final
name|String
name|SEQUENCE_NAME
init|=
literal|"Sequence"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SEQUENCE_FAULT_NAME
init|=
literal|"SequenceFault"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SEQUENCE_ACK_NAME
init|=
literal|"SequenceAcknowledgement"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACK_REQUESTED_NAME
init|=
literal|"AckRequested"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RMASSERTION_NAME
init|=
literal|"RMAssertion"
decl_stmt|;
comment|// fault codes
specifier|public
specifier|static
specifier|final
name|String
name|UNKNOWN_SEQUENCE_FAULT_CODE
init|=
literal|"UnknownSequence"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SEQUENCE_TERMINATED_FAULT_CODE
init|=
literal|"SequenceTerminated"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INVALID_ACKNOWLEDGMENT_FAULT_CODE
init|=
literal|"InvalidAcknowledgement"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MESSAGE_NUMBER_ROLLOVER_FAULT_CODE
init|=
literal|"MessageNumberRollover"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CREATE_SEQUENCE_REFUSED_FAULT_CODE
init|=
literal|"CreateSequenceRefused"
decl_stmt|;
comment|// WS-RM 1.1 only
specifier|public
specifier|static
specifier|final
name|String
name|SEQUENCE_CLOSED_FAULT_CODE
init|=
literal|"SequenceClosed"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSRM_REQUIRED_FAULT_CODE
init|=
literal|"WSRMRequired"
decl_stmt|;
specifier|public
specifier|abstract
name|String
name|getWSRMNamespace
parameter_list|()
function_decl|;
comment|// actions access methods
specifier|public
specifier|abstract
name|String
name|getCreateSequenceAction
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|String
name|getCreateSequenceResponseAction
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|String
name|getCloseSequenceAction
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|String
name|getTerminateSequenceAction
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|String
name|getAckRequestedAction
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|String
name|getSequenceAckAction
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|String
name|getSequenceInfoAction
parameter_list|()
function_decl|;
comment|// service model constants access methods
specifier|public
specifier|abstract
name|QName
name|getPortName
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|QName
name|getCreateSequenceOperationName
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|QName
name|getCreateSequenceResponseOperationName
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|QName
name|getCreateSequenceOnewayOperationName
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|QName
name|getCreateSequenceResponseOnewayOperationName
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|QName
name|getCloseSequenceOperationName
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|QName
name|getTerminateSequenceOperationName
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|QName
name|getTerminateSequenceAnonymousOperationName
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|QName
name|getSequenceAckOperationName
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|QName
name|getAckRequestedOperationName
parameter_list|()
function_decl|;
comment|// fault codes access methods
specifier|public
specifier|abstract
name|QName
name|getUnknownSequenceFaultCode
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|QName
name|getSequenceTerminatedFaultCode
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|QName
name|getInvalidAcknowledgmentFaultCode
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|QName
name|getMessageNumberRolloverFaultCode
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|QName
name|getCreateSequenceRefusedFaultCode
parameter_list|()
function_decl|;
comment|/**      * Get SequenceClosed fault code.      *      * @return code, or<code>null</code> if not supported      */
specifier|public
specifier|abstract
name|QName
name|getSequenceClosedFaultCode
parameter_list|()
function_decl|;
comment|/**      * Get WSRMRequired fault code.      *      * @return code, or<code>null</code> if not supported      */
specifier|public
specifier|abstract
name|QName
name|getWSRMRequiredFaultCode
parameter_list|()
function_decl|;
block|}
end_class

end_unit

