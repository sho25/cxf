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
name|Iterator
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
name|AbstractSoapInterceptor
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
name|transport
operator|.
name|Destination
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
name|ContextUtils
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
name|EndpointReferenceType
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

begin_comment
comment|/**  * Utility interceptor for dealing with faults occurred during processing  * the one way requests with WSA FaultTo EPR pointing to a decoupled destination.  *  * Note that this interceptor is not currently installed by default.  * It can be installed using @InInterceptors and @OutInterceptors  * annotations or explicitly added to the list of interceptors.  */
end_comment

begin_class
specifier|public
class|class
name|DecoupledFaultHandler
extends|extends
name|AbstractSoapInterceptor
block|{
specifier|public
specifier|static
specifier|final
name|String
name|WSA_ACTION
init|=
literal|"http://schemas.xmlsoap.org/wsdl/soap/envelope/fault"
decl_stmt|;
specifier|public
name|DecoupledFaultHandler
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_PROTOCOL
argument_list|)
expr_stmt|;
name|addBefore
argument_list|(
name|MAPCodec
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
name|SoapMessage
name|message
parameter_list|)
block|{
comment|// complete
block|}
comment|// Ideally, this code will instead be executed as part of the Fault chain
comment|// but at the moment PhaseInterceptorChain needs to be tricked that this is
comment|// a two way request for a fault chain be invoked
specifier|public
name|void
name|handleFault
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
if|if
condition|(
operator|!
name|ContextUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
specifier|final
name|AddressingProperties
name|maps
init|=
name|ContextUtils
operator|.
name|retrieveMAPs
argument_list|(
name|inMessage
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|maps
operator|!=
literal|null
operator|&&
operator|!
name|ContextUtils
operator|.
name|isGenericAddress
argument_list|(
name|maps
operator|.
name|getFaultTo
argument_list|()
argument_list|)
condition|)
block|{
comment|//Just keep the wsa headers to remove the not understand headers
if|if
condition|(
name|exchange
operator|.
name|getOutMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|message
operator|=
operator|(
name|SoapMessage
operator|)
name|exchange
operator|.
name|getOutMessage
argument_list|()
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|Header
argument_list|>
name|iterator
init|=
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Header
name|header
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|isWSAHeader
argument_list|(
name|header
argument_list|)
condition|)
block|{
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
name|exchange
operator|.
name|setOneWay
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOutMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|//manually set the action
name|message
operator|.
name|put
argument_list|(
name|ContextUtils
operator|.
name|ACTION
argument_list|,
name|WSA_ACTION
argument_list|)
expr_stmt|;
name|Destination
name|destination
init|=
name|createDecoupledDestination
argument_list|(
name|exchange
argument_list|,
name|maps
operator|.
name|getFaultTo
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setDestination
argument_list|(
name|destination
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|Destination
name|createDecoupledDestination
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|EndpointReferenceType
name|epr
parameter_list|)
block|{
return|return
name|ContextUtils
operator|.
name|createDecoupledDestination
argument_list|(
name|exchange
argument_list|,
name|epr
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isWSAHeader
parameter_list|(
name|Header
name|header
parameter_list|)
block|{
return|return
name|header
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|startsWith
argument_list|(
name|Names
operator|.
name|WSA_NAMESPACE_NAME
argument_list|)
return|;
block|}
block|}
end_class

end_unit

