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
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Marshaller
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
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
name|Document
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
name|Binding
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
name|binding
operator|.
name|soap
operator|.
name|SoapVersion
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|util
operator|.
name|PackageUtils
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
name|interceptor
operator|.
name|Fault
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
name|BindingFaultFactory
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
name|SequenceType
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|SoapFaultFactory
implements|implements
name|BindingFaultFactory
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|SoapFaultFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WS_RM_PACKAGE
init|=
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|SequenceType
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|SoapVersion
name|version
decl_stmt|;
specifier|public
name|SoapFaultFactory
parameter_list|(
name|Binding
name|binding
parameter_list|)
block|{
name|version
operator|=
operator|(
operator|(
name|SoapBinding
operator|)
name|binding
operator|)
operator|.
name|getSoapVersion
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Fault
name|createFault
parameter_list|(
name|SequenceFault
name|sf
parameter_list|)
block|{
name|Fault
name|f
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|version
operator|==
name|Soap11
operator|.
name|getInstance
argument_list|()
condition|)
block|{
name|f
operator|=
name|createSoap11Fault
argument_list|(
name|sf
argument_list|)
expr_stmt|;
comment|// so we can encode the SequenceFault as header
name|f
operator|.
name|initCause
argument_list|(
name|sf
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|f
operator|=
name|createSoap12Fault
argument_list|(
name|sf
argument_list|)
expr_stmt|;
block|}
return|return
name|f
return|;
block|}
name|Fault
name|createSoap11Fault
parameter_list|(
name|SequenceFault
name|sf
parameter_list|)
block|{
name|SoapFault
name|fault
init|=
operator|new
name|SoapFault
argument_list|(
name|sf
operator|.
name|getReason
argument_list|()
argument_list|,
name|sf
operator|.
name|isSender
argument_list|()
condition|?
name|version
operator|.
name|getSender
argument_list|()
else|:
name|version
operator|.
name|getReceiver
argument_list|()
argument_list|)
decl_stmt|;
name|fault
operator|.
name|setSubCode
argument_list|(
name|sf
operator|.
name|getSubCode
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|fault
return|;
block|}
name|Fault
name|createSoap12Fault
parameter_list|(
name|SequenceFault
name|sf
parameter_list|)
block|{
name|SoapFault
name|fault
init|=
operator|(
name|SoapFault
operator|)
name|createSoap11Fault
argument_list|(
name|sf
argument_list|)
decl_stmt|;
name|Object
name|detail
init|=
name|sf
operator|.
name|getDetail
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|detail
condition|)
block|{
return|return
name|fault
return|;
block|}
try|try
block|{
name|setDetail
argument_list|(
name|fault
argument_list|,
name|detail
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LogUtils
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"MARSHAL_FAULT_DETAIL_EXC"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
name|fault
return|;
block|}
name|void
name|setDetail
parameter_list|(
name|SoapFault
name|fault
parameter_list|,
name|Object
name|detail
parameter_list|)
throws|throws
name|Exception
block|{
name|DocumentBuilderFactory
name|factory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|factory
operator|.
name|newDocumentBuilder
argument_list|()
operator|.
name|newDocument
argument_list|()
decl_stmt|;
name|Element
name|elem
init|=
literal|null
decl_stmt|;
name|JAXBContext
name|ctx
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|WS_RM_PACKAGE
argument_list|)
decl_stmt|;
name|Marshaller
name|m
init|=
name|ctx
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
if|if
condition|(
name|RMConstants
operator|.
name|getInvalidAcknowledgmentFaultCode
argument_list|()
operator|.
name|equals
argument_list|(
name|fault
operator|.
name|getSubCode
argument_list|()
argument_list|)
condition|)
block|{
name|SequenceAcknowledgement
name|ack
init|=
operator|(
name|SequenceAcknowledgement
operator|)
name|detail
decl_stmt|;
name|m
operator|.
name|marshal
argument_list|(
name|ack
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|RMConstants
operator|.
name|getCreateSequenceRefusedFaultCode
argument_list|()
operator|.
name|equals
argument_list|(
name|fault
operator|.
name|getSubCode
argument_list|()
argument_list|)
condition|)
block|{
name|Identifier
name|id
init|=
operator|(
name|Identifier
operator|)
name|detail
decl_stmt|;
name|m
operator|.
name|marshal
argument_list|(
name|id
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
name|elem
operator|=
operator|(
name|Element
operator|)
name|doc
operator|.
name|getChildNodes
argument_list|()
operator|.
name|item
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|fault
operator|.
name|setDetail
argument_list|(
name|elem
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|(
name|Fault
name|f
parameter_list|)
block|{
name|SoapFault
name|sf
init|=
operator|(
name|SoapFault
operator|)
name|f
decl_stmt|;
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"SEQ_FAULT_MSG"
argument_list|,
name|LOG
argument_list|,
operator|new
name|Object
index|[]
block|{
name|sf
operator|.
name|getReason
argument_list|()
block|,
name|sf
operator|.
name|getFaultCode
argument_list|()
block|,
name|sf
operator|.
name|getSubCode
argument_list|()
block|}
argument_list|)
decl_stmt|;
return|return
name|msg
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

