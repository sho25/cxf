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
name|binding
operator|.
name|xml
operator|.
name|interceptor
package|;
end_package

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
name|binding
operator|.
name|xml
operator|.
name|XMLFault
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
name|bindings
operator|.
name|xformat
operator|.
name|XMLBindingMessageFormat
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
name|endpoint
operator|.
name|Endpoint
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
name|AbstractInDatabindingInterceptor
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
name|DocLiteralInInterceptor
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
name|phase
operator|.
name|Phase
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
name|service
operator|.
name|model
operator|.
name|BindingInfo
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
name|service
operator|.
name|model
operator|.
name|BindingMessageInfo
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
name|service
operator|.
name|model
operator|.
name|BindingOperationInfo
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
name|service
operator|.
name|model
operator|.
name|MessagePartInfo
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
name|service
operator|.
name|model
operator|.
name|OperationInfo
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
name|staxutils
operator|.
name|DepthXMLStreamReader
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
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_class
specifier|public
class|class
name|XMLMessageInInterceptor
extends|extends
name|AbstractInDatabindingInterceptor
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
name|XMLMessageInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|XMLMessageInInterceptor
parameter_list|()
block|{
name|this
argument_list|(
name|Phase
operator|.
name|UNMARSHAL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|XMLMessageInInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
name|addBefore
argument_list|(
name|DocLiteralInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
if|if
condition|(
name|isGET
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"XMLMessageInInterceptor skipped in HTTP GET method"
argument_list|)
expr_stmt|;
return|return;
block|}
name|Endpoint
name|ep
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|xsr
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
name|DepthXMLStreamReader
name|reader
init|=
operator|new
name|DepthXMLStreamReader
argument_list|(
name|xsr
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|reader
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
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
argument_list|(
literal|"NO_OPERATION_ELEMENT"
argument_list|,
name|LOG
argument_list|)
argument_list|)
throw|;
block|}
name|Exchange
name|ex
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|QName
name|startQName
init|=
name|reader
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// handling xml fault message
if|if
condition|(
name|startQName
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
name|XMLFault
operator|.
name|XML_FAULT_ROOT
argument_list|)
condition|)
block|{
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|abort
argument_list|()
expr_stmt|;
if|if
condition|(
name|ep
operator|.
name|getInFaultObserver
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ep
operator|.
name|getInFaultObserver
argument_list|()
operator|.
name|onMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
comment|// handling xml normal inbound message
name|BindingOperationInfo
name|boi
init|=
name|ex
operator|.
name|get
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|isRequestor
init|=
name|isRequestor
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|boi
operator|==
literal|null
condition|)
block|{
name|BindingInfo
name|service
init|=
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|boi
operator|=
name|getBindingOperationInfo
argument_list|(
name|isRequestor
argument_list|,
name|startQName
argument_list|,
name|service
argument_list|,
name|xsr
argument_list|)
expr_stmt|;
if|if
condition|(
name|boi
operator|!=
literal|null
condition|)
block|{
name|ex
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|boi
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|,
name|boi
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setOneWay
argument_list|(
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|isOneWay
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|BindingMessageInfo
name|bmi
init|=
name|isRequestor
condition|?
name|boi
operator|.
name|getOutput
argument_list|()
else|:
name|boi
operator|.
name|getInput
argument_list|()
decl_stmt|;
if|if
condition|(
name|hasRootNode
argument_list|(
name|bmi
argument_list|,
name|startQName
argument_list|)
condition|)
block|{
try|try
block|{
name|xsr
operator|.
name|nextTag
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|xse
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
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
argument_list|(
literal|"STAX_READ_EXC"
argument_list|,
name|LOG
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
block|}
specifier|private
name|BindingOperationInfo
name|getBindingOperationInfo
parameter_list|(
name|boolean
name|isRequestor
parameter_list|,
name|QName
name|startQName
parameter_list|,
name|BindingInfo
name|bi
parameter_list|,
name|XMLStreamReader
name|xsr
parameter_list|)
block|{
for|for
control|(
name|BindingOperationInfo
name|boi
range|:
name|bi
operator|.
name|getOperations
argument_list|()
control|)
block|{
name|BindingMessageInfo
name|bmi
decl_stmt|;
if|if
condition|(
operator|!
name|isRequestor
condition|)
block|{
name|bmi
operator|=
name|boi
operator|.
name|getInput
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|bmi
operator|=
name|boi
operator|.
name|getOutput
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|hasRootNode
argument_list|(
name|bmi
argument_list|,
name|startQName
argument_list|)
condition|)
block|{
comment|//Consume The rootNode tag
try|try
block|{
name|xsr
operator|.
name|nextTag
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|xse
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
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
argument_list|(
literal|"STAX_READ_EXC"
argument_list|,
name|LOG
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|boi
return|;
block|}
else|else
block|{
name|Collection
argument_list|<
name|MessagePartInfo
argument_list|>
name|bodyParts
init|=
name|bmi
operator|.
name|getMessageParts
argument_list|()
decl_stmt|;
if|if
condition|(
name|bodyParts
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|MessagePartInfo
name|p
init|=
name|bodyParts
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|getConcreteName
argument_list|()
operator|.
name|equals
argument_list|(
name|startQName
argument_list|)
condition|)
block|{
return|return
name|boi
return|;
block|}
block|}
block|}
block|}
for|for
control|(
name|BindingOperationInfo
name|boi
range|:
name|bi
operator|.
name|getOperations
argument_list|()
control|)
block|{
if|if
condition|(
name|startQName
operator|.
name|equals
argument_list|(
name|boi
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
comment|//Consume The rootNode tag
try|try
block|{
name|xsr
operator|.
name|nextTag
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|xse
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
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
argument_list|(
literal|"STAX_READ_EXC"
argument_list|,
name|LOG
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|boi
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|boolean
name|hasRootNode
parameter_list|(
name|BindingMessageInfo
name|bmi
parameter_list|,
name|QName
name|elName
parameter_list|)
block|{
name|XMLBindingMessageFormat
name|xmf
init|=
name|bmi
operator|.
name|getExtensor
argument_list|(
name|XMLBindingMessageFormat
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|bmi
operator|.
name|getMessageParts
argument_list|()
operator|.
name|size
argument_list|()
operator|!=
literal|1
operator|&&
name|xmf
operator|!=
literal|null
operator|&&
name|xmf
operator|.
name|getRootNode
argument_list|()
operator|.
name|equals
argument_list|(
name|elName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

