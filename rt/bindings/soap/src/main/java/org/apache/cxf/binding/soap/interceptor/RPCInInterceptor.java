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
name|soap
operator|.
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|BareInInterceptor
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
name|interceptor
operator|.
name|URIMappingInterceptor
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
name|EndpointInfo
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
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|extensions
operator|.
name|soap
operator|.
name|SoapBody
import|;
end_import

begin_class
specifier|public
class|class
name|RPCInInterceptor
extends|extends
name|AbstractInDatabindingInterceptor
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|SOAP12_RESULT
init|=
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2003/05/soap-rpc"
argument_list|,
literal|"result"
argument_list|)
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
name|RPCInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|RPCInInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|UNMARSHAL
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|URIMappingInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|BindingOperationInfo
name|getOperation
parameter_list|(
name|Message
name|message
parameter_list|,
name|QName
name|opName
parameter_list|)
block|{
name|BindingOperationInfo
name|bop
init|=
name|ServiceModelUtil
operator|.
name|getOperation
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|,
name|opName
argument_list|)
decl_stmt|;
if|if
condition|(
name|bop
operator|==
literal|null
condition|)
block|{
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
if|if
condition|(
name|ep
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
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
name|boolean
name|output
init|=
operator|!
name|isRequestor
argument_list|(
name|message
argument_list|)
decl_stmt|;
for|for
control|(
name|BindingOperationInfo
name|info
range|:
name|service
operator|.
name|getOperations
argument_list|()
control|)
block|{
if|if
condition|(
name|info
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
name|opName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|SoapBody
name|body
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|output
condition|)
block|{
name|body
operator|=
name|info
operator|.
name|getOutput
argument_list|()
operator|.
name|getExtensor
argument_list|(
name|SoapBody
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|body
operator|=
name|info
operator|.
name|getInput
argument_list|()
operator|.
name|getExtensor
argument_list|(
name|SoapBody
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|body
operator|!=
literal|null
operator|&&
name|opName
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|body
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|info
return|;
block|}
block|}
block|}
block|}
return|return
name|bop
return|;
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
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"RPCInInterceptor skipped in HTTP GET method"
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
name|BindingOperationInfo
name|operation
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|xmlReader
argument_list|)
condition|)
block|{
name|message
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
operator|new
name|RuntimeException
argument_list|(
literal|"There must be a method name element."
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|opName
init|=
name|xmlReader
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
name|isRequestor
argument_list|(
name|message
argument_list|)
operator|&&
name|opName
operator|.
name|endsWith
argument_list|(
literal|"Response"
argument_list|)
condition|)
block|{
name|opName
operator|=
name|opName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|opName
operator|.
name|length
argument_list|()
operator|-
literal|8
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
operator|==
literal|null
condition|)
block|{
name|operation
operator|=
name|getOperation
argument_list|(
name|message
argument_list|,
operator|new
name|QName
argument_list|(
name|xmlReader
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|opName
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|operation
operator|==
literal|null
condition|)
block|{
comment|// it's doc-lit-bare
operator|new
name|BareInInterceptor
argument_list|()
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
name|setMessage
argument_list|(
name|message
argument_list|,
name|operation
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|operation
operator|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|MessageInfo
name|msg
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
argument_list|,
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|msg
operator|=
name|operation
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getInput
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|msg
operator|=
name|operation
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getOutput
argument_list|()
expr_stmt|;
block|}
name|message
operator|.
name|put
argument_list|(
name|MessageInfo
operator|.
name|class
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|MessageContentsList
name|parameters
init|=
operator|new
name|MessageContentsList
argument_list|()
decl_stmt|;
name|StaxUtils
operator|.
name|nextEvent
argument_list|(
name|xmlReader
argument_list|)
expr_stmt|;
name|boolean
name|hasNext
init|=
literal|true
decl_stmt|;
name|Iterator
argument_list|<
name|MessagePartInfo
argument_list|>
name|itr
init|=
name|msg
operator|.
name|getMessageParts
argument_list|()
operator|.
name|iterator
argument_list|()
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
if|if
condition|(
name|hasNext
condition|)
block|{
name|QName
name|qn
init|=
name|xmlReader
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|qn
operator|.
name|equals
argument_list|(
name|SOAP12_RESULT
argument_list|)
condition|)
block|{
comment|//just ignore this.   The parts should work correctly.
try|try
block|{
while|while
condition|(
name|xmlReader
operator|.
name|getEventType
argument_list|()
operator|!=
name|XMLStreamReader
operator|.
name|END_ELEMENT
condition|)
block|{
name|xmlReader
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
name|xmlReader
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
name|StaxUtils
operator|.
name|toNextElement
argument_list|(
name|xmlReader
argument_list|)
expr_stmt|;
name|qn
operator|=
name|xmlReader
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
comment|// WSI-BP states that RPC/Lit part accessors should be completely unqualified
comment|// However, older toolkits (Axis 1.x) are qualifying them.   We'll go
comment|// ahead and just match on the localpart.   The RPCOutInterceptor
comment|// will always generate WSI-BP compliant messages so it's unknown if
comment|// the non-WSI-BP toolkits will be able to understand the CXF
comment|// generated messages if they are expecting it to be qualified.
name|Iterator
argument_list|<
name|MessagePartInfo
argument_list|>
name|partItr
init|=
name|msg
operator|.
name|getMessageParts
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|qn
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
name|part
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
name|partItr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|part
operator|=
name|partItr
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|qn
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
literal|"UNKNOWN_RPC_LIT_PART"
argument_list|,
name|LOG
argument_list|,
name|qn
argument_list|)
argument_list|)
throw|;
block|}
comment|//honor JAXBAnnotation
name|part
operator|.
name|setProperty
argument_list|(
literal|"honor.jaxb.annotations"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|parameters
operator|.
name|put
argument_list|(
name|part
argument_list|,
name|dr
operator|.
name|read
argument_list|(
name|part
argument_list|,
name|xmlReader
argument_list|)
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
specifier|private
name|void
name|setMessage
parameter_list|(
name|Message
name|message
parameter_list|,
name|BindingOperationInfo
name|operation
parameter_list|)
block|{
name|Exchange
name|ex
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|operation
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
name|operation
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setOneWay
argument_list|(
name|operation
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|isOneWay
argument_list|()
argument_list|)
expr_stmt|;
comment|//Set standard MessageContext properties required by JAX_WS, but not specific to JAX_WS.
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_OPERATION
argument_list|,
name|operation
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ServiceInfo
name|si
init|=
name|operation
operator|.
name|getBinding
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|QName
name|serviceQName
init|=
name|si
operator|.
name|getName
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_SERVICE
argument_list|,
name|serviceQName
argument_list|)
expr_stmt|;
name|QName
name|interfaceQName
init|=
name|si
operator|.
name|getInterface
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_INTERFACE
argument_list|,
name|interfaceQName
argument_list|)
expr_stmt|;
name|EndpointInfo
name|endpointInfo
init|=
name|ex
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
name|QName
name|portQName
init|=
name|endpointInfo
operator|.
name|getName
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_PORT
argument_list|,
name|portQName
argument_list|)
expr_stmt|;
name|URI
name|wsdlDescription
init|=
name|endpointInfo
operator|.
name|getProperty
argument_list|(
literal|"URI"
argument_list|,
name|URI
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|wsdlDescription
operator|==
literal|null
condition|)
block|{
name|String
name|address
init|=
name|endpointInfo
operator|.
name|getAddress
argument_list|()
decl_stmt|;
try|try
block|{
name|wsdlDescription
operator|=
operator|new
name|URI
argument_list|(
name|address
operator|+
literal|"?wsdl"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|//do nothing
block|}
name|endpointInfo
operator|.
name|setProperty
argument_list|(
literal|"URI"
argument_list|,
name|wsdlDescription
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_DESCRIPTION
argument_list|,
name|wsdlDescription
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

