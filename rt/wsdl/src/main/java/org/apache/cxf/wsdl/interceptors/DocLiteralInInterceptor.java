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
name|wsdl
operator|.
name|interceptors
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
name|XMLStreamConstants
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
name|databinding
operator|.
name|DataReader
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
name|message
operator|.
name|MessageContentsList
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
name|Service
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
name|MessageInfo
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
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
name|ServiceModelUtil
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaElement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|constants
operator|.
name|Constants
import|;
end_import

begin_class
specifier|public
class|class
name|DocLiteralInInterceptor
extends|extends
name|AbstractInDatabindingInterceptor
block|{
specifier|public
specifier|static
specifier|final
name|String
name|KEEP_PARAMETERS_WRAPPER
init|=
name|DocLiteralInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".DocLiteralInInterceptor.keep-parameters-wrapper"
decl_stmt|;
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
name|DocLiteralInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|DocLiteralInInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|UNMARSHAL
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
block|{
if|if
condition|(
name|isGET
argument_list|(
name|message
argument_list|)
operator|&&
name|message
operator|.
name|getContent
argument_list|(
name|List
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"DocLiteralInInterceptor skipped in HTTP GET method"
argument_list|)
expr_stmt|;
return|return;
block|}
name|DepthXMLStreamReader
name|xmlReader
init|=
name|getXMLStreamReader
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|DataReader
argument_list|<
name|XMLStreamReader
argument_list|>
name|dr
init|=
name|getDataReader
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|MessageContentsList
name|parameters
init|=
operator|new
name|MessageContentsList
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|BindingOperationInfo
name|bop
init|=
name|exchange
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
name|boolean
name|client
init|=
name|isRequestor
argument_list|(
name|message
argument_list|)
decl_stmt|;
comment|//if body is empty and we have BindingOperationInfo, we do not need to match
comment|//operation anymore, just return
if|if
condition|(
name|bop
operator|!=
literal|null
operator|&&
operator|!
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|xmlReader
argument_list|)
condition|)
block|{
comment|// body may be empty for partial response to decoupled request
return|return;
block|}
name|Service
name|service
init|=
name|ServiceModelUtil
operator|.
name|getService
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
decl_stmt|;
name|bop
operator|=
name|getBindingOperationInfo
argument_list|(
name|xmlReader
argument_list|,
name|exchange
argument_list|,
name|bop
argument_list|,
name|client
argument_list|)
expr_stmt|;
name|boolean
name|forceDocLitBare
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|bop
operator|!=
literal|null
operator|&&
name|bop
operator|.
name|getBinding
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|forceDocLitBare
operator|=
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|bop
operator|.
name|getBinding
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"soap.force.doclit.bare"
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
operator|!
name|forceDocLitBare
operator|&&
name|bop
operator|!=
literal|null
operator|&&
name|bop
operator|.
name|isUnwrappedCapable
argument_list|()
condition|)
block|{
name|ServiceInfo
name|si
init|=
name|bop
operator|.
name|getBinding
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
comment|// Wrapped case
name|MessageInfo
name|msgInfo
init|=
name|setMessage
argument_list|(
name|message
argument_list|,
name|bop
argument_list|,
name|client
argument_list|,
name|si
argument_list|)
decl_stmt|;
name|setDataReaderValidation
argument_list|(
name|service
argument_list|,
name|message
argument_list|,
name|dr
argument_list|)
expr_stmt|;
comment|// Determine if we should keep the parameters wrapper
if|if
condition|(
name|shouldWrapParameters
argument_list|(
name|msgInfo
argument_list|,
name|message
argument_list|)
condition|)
block|{
name|QName
name|startQName
init|=
name|xmlReader
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|msgInfo
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
throw|throw
operator|new
name|Fault
argument_list|(
literal|"UNEXPECTED_WRAPPER_ELEMENT"
argument_list|,
name|LOG
argument_list|,
literal|null
argument_list|,
name|startQName
argument_list|,
name|msgInfo
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getConcreteName
argument_list|()
argument_list|)
throw|;
block|}
name|Object
name|wrappedObject
init|=
name|dr
operator|.
name|read
argument_list|(
name|msgInfo
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|xmlReader
argument_list|)
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
name|msgInfo
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|wrappedObject
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Unwrap each part individually if we don't have a wrapper
name|bop
operator|=
name|bop
operator|.
name|getUnwrappedOperation
argument_list|()
expr_stmt|;
name|msgInfo
operator|=
name|setMessage
argument_list|(
name|message
argument_list|,
name|bop
argument_list|,
name|client
argument_list|,
name|si
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|messageParts
init|=
name|msgInfo
operator|.
name|getMessageParts
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|MessagePartInfo
argument_list|>
name|itr
init|=
name|messageParts
operator|.
name|iterator
argument_list|()
decl_stmt|;
comment|// advance just past the wrapped element so we don't get
comment|// stuck
if|if
condition|(
name|xmlReader
operator|.
name|getEventType
argument_list|()
operator|==
name|XMLStreamConstants
operator|.
name|START_ELEMENT
condition|)
block|{
name|StaxUtils
operator|.
name|nextEvent
argument_list|(
name|xmlReader
argument_list|)
expr_stmt|;
block|}
comment|// loop through each child element
name|getPara
argument_list|(
name|xmlReader
argument_list|,
name|dr
argument_list|,
name|parameters
argument_list|,
name|itr
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|//Bare style
name|BindingMessageInfo
name|msgInfo
init|=
literal|null
decl_stmt|;
name|Endpoint
name|ep
init|=
name|exchange
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|ServiceInfo
name|si
init|=
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
if|if
condition|(
name|bop
operator|!=
literal|null
condition|)
block|{
comment|//for xml binding or client side
if|if
condition|(
name|client
condition|)
block|{
name|msgInfo
operator|=
name|bop
operator|.
name|getOutput
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|msgInfo
operator|=
name|bop
operator|.
name|getInput
argument_list|()
expr_stmt|;
if|if
condition|(
name|bop
operator|.
name|getOutput
argument_list|()
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setOneWay
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|msgInfo
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|setMessage
argument_list|(
name|message
argument_list|,
name|bop
argument_list|,
name|client
argument_list|,
name|si
argument_list|,
name|msgInfo
operator|.
name|getMessageInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Collection
argument_list|<
name|OperationInfo
argument_list|>
name|operations
init|=
literal|null
decl_stmt|;
name|operations
operator|=
operator|new
name|ArrayList
argument_list|<
name|OperationInfo
argument_list|>
argument_list|()
expr_stmt|;
name|operations
operator|.
name|addAll
argument_list|(
name|si
operator|.
name|getInterface
argument_list|()
operator|.
name|getOperations
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|xmlReader
operator|==
literal|null
operator|||
operator|!
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|xmlReader
argument_list|)
condition|)
block|{
comment|// empty input
name|getBindingOperationForEmptyBody
argument_list|(
name|operations
argument_list|,
name|ep
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
return|return;
block|}
name|setDataReaderValidation
argument_list|(
name|service
argument_list|,
name|message
argument_list|,
name|dr
argument_list|)
expr_stmt|;
name|int
name|paramNum
init|=
literal|0
decl_stmt|;
do|do
block|{
name|QName
name|elName
init|=
name|xmlReader
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Object
name|o
init|=
literal|null
decl_stmt|;
name|MessagePartInfo
name|p
decl_stmt|;
if|if
condition|(
operator|!
name|client
operator|&&
name|msgInfo
operator|!=
literal|null
operator|&&
name|msgInfo
operator|.
name|getMessageParts
argument_list|()
operator|!=
literal|null
operator|&&
name|msgInfo
operator|.
name|getMessageParts
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|//no input messagePartInfo
return|return;
block|}
if|if
condition|(
name|msgInfo
operator|!=
literal|null
operator|&&
name|msgInfo
operator|.
name|getMessageParts
argument_list|()
operator|!=
literal|null
operator|&&
name|msgInfo
operator|.
name|getMessageParts
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|msgInfo
operator|.
name|getMessageParts
argument_list|()
operator|.
name|size
argument_list|()
operator|>
name|paramNum
condition|)
block|{
name|p
operator|=
name|msgInfo
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
name|paramNum
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|p
operator|=
literal|null
expr_stmt|;
block|}
block|}
else|else
block|{
name|p
operator|=
name|findMessagePart
argument_list|(
name|exchange
argument_list|,
name|operations
argument_list|,
name|elName
argument_list|,
name|client
argument_list|,
name|paramNum
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|forceDocLitBare
condition|)
block|{
comment|//Make sure the elName found on the wire is actually OK for
comment|//the purpose we need it
name|validatePart
argument_list|(
name|p
argument_list|,
name|elName
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
name|o
operator|=
name|dr
operator|.
name|read
argument_list|(
name|p
argument_list|,
name|xmlReader
argument_list|)
expr_stmt|;
if|if
condition|(
name|forceDocLitBare
operator|&&
name|parameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// webservice provider does not need to ensure size
name|parameters
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|parameters
operator|.
name|put
argument_list|(
name|p
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
name|paramNum
operator|++
expr_stmt|;
if|if
condition|(
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
operator|==
literal|null
operator|||
name|o
operator|==
name|xmlReader
condition|)
block|{
name|xmlReader
operator|=
literal|null
expr_stmt|;
block|}
block|}
do|while
condition|(
name|xmlReader
operator|!=
literal|null
operator|&&
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|xmlReader
argument_list|)
condition|)
do|;
block|}
name|message
operator|.
name|setContent
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Fault
name|f
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|f
operator|.
name|setFaultCode
argument_list|(
name|Fault
operator|.
name|FAULT_CODE_CLIENT
argument_list|)
expr_stmt|;
block|}
throw|throw
name|f
throw|;
block|}
block|}
specifier|private
name|void
name|getBindingOperationForEmptyBody
parameter_list|(
name|Collection
argument_list|<
name|OperationInfo
argument_list|>
name|operations
parameter_list|,
name|Endpoint
name|ep
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// TO DO : check duplicate operation with no input and also check if the action matches
for|for
control|(
name|OperationInfo
name|op
range|:
name|operations
control|)
block|{
name|MessageInfo
name|bmsg
init|=
name|op
operator|.
name|getInput
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|bparts
init|=
name|bmsg
operator|.
name|getMessageParts
argument_list|()
decl_stmt|;
if|if
condition|(
name|bparts
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|||
operator|(
name|bparts
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|Constants
operator|.
name|XSD_ANYTYPE
operator|.
name|equals
argument_list|(
name|bparts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTypeQName
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|BindingOperationInfo
name|boi
init|=
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getOperation
argument_list|(
name|op
argument_list|)
decl_stmt|;
name|exchange
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
name|exchange
operator|.
name|put
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|,
name|op
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOneWay
argument_list|(
name|op
operator|.
name|isOneWay
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|BindingOperationInfo
name|getBindingOperationInfo
parameter_list|(
name|DepthXMLStreamReader
name|xmlReader
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|BindingOperationInfo
name|bop
parameter_list|,
name|boolean
name|client
parameter_list|)
block|{
comment|//bop might be a unwrapped, wrap it back so that we can get correct info
if|if
condition|(
name|bop
operator|!=
literal|null
operator|&&
name|bop
operator|.
name|isUnwrapped
argument_list|()
condition|)
block|{
name|bop
operator|=
name|bop
operator|.
name|getWrappedOperation
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|bop
operator|==
literal|null
condition|)
block|{
name|QName
name|startQName
init|=
name|xmlReader
operator|==
literal|null
condition|?
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/jaxws/provider"
argument_list|,
literal|"invoke"
argument_list|)
else|:
name|xmlReader
operator|.
name|getName
argument_list|()
decl_stmt|;
name|bop
operator|=
name|getBindingOperationInfo
argument_list|(
name|exchange
argument_list|,
name|startQName
argument_list|,
name|client
argument_list|)
expr_stmt|;
block|}
return|return
name|bop
return|;
block|}
specifier|private
name|void
name|validatePart
parameter_list|(
name|MessagePartInfo
name|p
parameter_list|,
name|QName
name|elName
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
if|if
condition|(
name|p
operator|==
literal|null
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
literal|"NO_PART_FOUND"
argument_list|,
name|LOG
argument_list|,
name|elName
argument_list|)
argument_list|,
name|Fault
operator|.
name|FAULT_CODE_CLIENT
argument_list|)
throw|;
block|}
name|boolean
name|synth
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|getMessageInfo
argument_list|()
operator|!=
literal|null
operator|&&
name|p
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getOperation
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|OperationInfo
name|op
init|=
name|p
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getOperation
argument_list|()
decl_stmt|;
name|Boolean
name|b
init|=
operator|(
name|Boolean
operator|)
name|op
operator|.
name|getProperty
argument_list|(
literal|"operation.is.synthetic"
argument_list|)
decl_stmt|;
if|if
condition|(
name|b
operator|!=
literal|null
condition|)
block|{
name|synth
operator|=
name|b
expr_stmt|;
block|}
block|}
if|if
condition|(
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|m
argument_list|,
literal|"soap.no.validate.parts"
argument_list|,
literal|false
argument_list|)
condition|)
block|{
comment|// something like a Provider service or similar that is forcing a
comment|// doc/lit/bare on an endpoint that may not really be doc/lit/bare.
comment|// we need to just let these through per spec so the endpoint
comment|// can process it
name|synth
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|synth
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|p
operator|.
name|isElement
argument_list|()
condition|)
block|{
if|if
condition|(
name|p
operator|.
name|getConcreteName
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|elName
operator|.
name|equals
argument_list|(
name|p
operator|.
name|getConcreteName
argument_list|()
argument_list|)
operator|&&
operator|!
name|synth
condition|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
literal|"UNEXPECTED_ELEMENT"
argument_list|,
name|LOG
argument_list|,
literal|null
argument_list|,
name|elName
argument_list|,
name|p
operator|.
name|getConcreteName
argument_list|()
argument_list|)
throw|;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|!
operator|(
name|elName
operator|.
name|equals
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|elName
operator|.
name|equals
argument_list|(
name|p
operator|.
name|getConcreteName
argument_list|()
argument_list|)
operator|)
operator|&&
operator|!
name|synth
condition|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
literal|"UNEXPECTED_ELEMENT"
argument_list|,
name|LOG
argument_list|,
literal|null
argument_list|,
name|elName
argument_list|,
name|p
operator|.
name|getConcreteName
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
name|void
name|getPara
parameter_list|(
name|DepthXMLStreamReader
name|xmlReader
parameter_list|,
name|DataReader
argument_list|<
name|XMLStreamReader
argument_list|>
name|dr
parameter_list|,
name|MessageContentsList
name|parameters
parameter_list|,
name|Iterator
argument_list|<
name|MessagePartInfo
argument_list|>
name|itr
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|boolean
name|hasNext
init|=
literal|true
decl_stmt|;
while|while
condition|(
name|itr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|MessagePartInfo
name|part
init|=
name|itr
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|hasNext
condition|)
block|{
name|hasNext
operator|=
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|xmlReader
argument_list|)
expr_stmt|;
block|}
name|Object
name|obj
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|hasNext
condition|)
block|{
name|QName
name|rname
init|=
name|xmlReader
operator|.
name|getName
argument_list|()
decl_stmt|;
while|while
condition|(
name|part
operator|!=
literal|null
operator|&&
operator|!
name|rname
operator|.
name|equals
argument_list|(
name|part
operator|.
name|getConcreteName
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|part
operator|.
name|getXmlSchema
argument_list|()
operator|instanceof
name|XmlSchemaElement
condition|)
block|{
comment|//TODO - should check minOccurs=0 and throw validation exception
comment|//thing if the part needs to be here
name|parameters
operator|.
name|put
argument_list|(
name|part
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|itr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|part
operator|=
name|itr
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|part
operator|=
literal|null
expr_stmt|;
block|}
block|}
if|if
condition|(
name|part
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|rname
operator|.
name|equals
argument_list|(
name|part
operator|.
name|getConcreteName
argument_list|()
argument_list|)
condition|)
block|{
name|obj
operator|=
name|dr
operator|.
name|read
argument_list|(
name|part
argument_list|,
name|xmlReader
argument_list|)
expr_stmt|;
block|}
block|}
name|parameters
operator|.
name|put
argument_list|(
name|part
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|MessageInfo
name|setMessage
parameter_list|(
name|Message
name|message
parameter_list|,
name|BindingOperationInfo
name|operation
parameter_list|,
name|boolean
name|requestor
parameter_list|,
name|ServiceInfo
name|si
parameter_list|)
block|{
name|MessageInfo
name|msgInfo
init|=
name|getMessageInfo
argument_list|(
name|message
argument_list|,
name|operation
argument_list|,
name|requestor
argument_list|)
decl_stmt|;
return|return
name|setMessage
argument_list|(
name|message
argument_list|,
name|operation
argument_list|,
name|requestor
argument_list|,
name|si
argument_list|,
name|msgInfo
argument_list|)
return|;
block|}
specifier|protected
name|BindingOperationInfo
name|getBindingOperationInfo
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|QName
name|name
parameter_list|,
name|boolean
name|client
parameter_list|)
block|{
name|BindingOperationInfo
name|bop
init|=
name|ServiceModelUtil
operator|.
name|getOperationForWrapperElement
argument_list|(
name|exchange
argument_list|,
name|name
argument_list|,
name|client
argument_list|)
decl_stmt|;
if|if
condition|(
name|bop
operator|==
literal|null
condition|)
block|{
name|bop
operator|=
name|super
operator|.
name|getBindingOperationInfo
argument_list|(
name|exchange
argument_list|,
name|name
argument_list|,
name|client
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|bop
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|bop
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|,
name|bop
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|bop
return|;
block|}
specifier|protected
name|boolean
name|shouldWrapParameters
parameter_list|(
name|MessageInfo
name|msgInfo
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|Object
name|keepParametersWrapperFlag
init|=
name|message
operator|.
name|get
argument_list|(
name|KEEP_PARAMETERS_WRAPPER
argument_list|)
decl_stmt|;
if|if
condition|(
name|keepParametersWrapperFlag
operator|==
literal|null
condition|)
block|{
return|return
name|msgInfo
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTypeClass
argument_list|()
operator|!=
literal|null
return|;
block|}
else|else
block|{
return|return
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|keepParametersWrapperFlag
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

