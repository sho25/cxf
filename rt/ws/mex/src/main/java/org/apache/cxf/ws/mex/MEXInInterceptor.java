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
name|mex
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
name|Bus
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
name|BusException
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
name|SoapBindingConstants
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
name|endpoint
operator|.
name|EndpointException
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
name|Server
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
name|jaxws
operator|.
name|JAXWSMethodInvoker
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
name|jaxws
operator|.
name|JaxWsServerFactoryBean
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
name|AbstractPhaseInterceptor
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
name|JAXWSAConstants
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
name|MAPAggregator
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
name|WSAddressingFeature
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
name|policy
operator|.
name|AssertionInfoMap
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|MEXInInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|final
name|MEXEndpoint
name|ep
decl_stmt|;
name|Endpoint
name|mexEndpoint
decl_stmt|;
specifier|public
name|MEXInInterceptor
parameter_list|(
name|Server
name|serv
parameter_list|)
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_PROTOCOL
argument_list|)
expr_stmt|;
name|ep
operator|=
operator|new
name|MEXEndpoint
argument_list|(
name|serv
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
name|String
name|action
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|SoapBindingConstants
operator|.
name|SOAP_ACTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|action
operator|==
literal|null
condition|)
block|{
name|AddressingProperties
name|inProps
init|=
operator|(
name|AddressingProperties
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|JAXWSAConstants
operator|.
name|SERVER_ADDRESSING_PROPERTIES_INBOUND
argument_list|)
decl_stmt|;
if|if
condition|(
name|inProps
operator|!=
literal|null
condition|)
block|{
name|action
operator|=
name|inProps
operator|.
name|getAction
argument_list|()
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
literal|"http://schemas.xmlsoap.org/ws/2004/09/transfer/Get"
operator|.
name|equals
argument_list|(
name|action
argument_list|)
operator|||
literal|"http://schemas.xmlsoap.org/ws/2004/09/mex/GetMetadata/Request"
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|message
operator|.
name|remove
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
expr_stmt|;
name|Exchange
name|ex
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|Service
operator|.
name|class
argument_list|,
name|endpoint
operator|.
name|getService
argument_list|()
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|Binding
operator|.
name|class
argument_list|,
name|endpoint
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|endpoint
operator|.
name|getBinding
argument_list|()
operator|.
name|getBindingInfo
argument_list|()
operator|.
name|getOperation
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://mex.ws.cxf.apache.org/"
argument_list|,
literal|"Get2004"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|ex
operator|.
name|remove
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|MAPAggregator
operator|.
name|ACTION_VERIFIED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|endpoint
operator|.
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|endpoint
operator|.
name|getBinding
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|MEXJaxWsServerFactoryBean
extends|extends
name|JaxWsServerFactoryBean
block|{
specifier|public
name|MEXJaxWsServerFactoryBean
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|setServiceClass
argument_list|(
name|MEXEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|setServiceName
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://mex.ws.cxf.apache.org/"
argument_list|,
literal|"MEXEndpoint"
argument_list|)
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Endpoint
name|createEndpoint
parameter_list|()
throws|throws
name|BusException
throws|,
name|EndpointException
block|{
name|Endpoint
name|ep
init|=
name|super
operator|.
name|createEndpoint
argument_list|()
decl_stmt|;
operator|new
name|WSAddressingFeature
argument_list|()
operator|.
name|initialize
argument_list|(
name|ep
argument_list|,
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ep
return|;
block|}
block|}
specifier|private
specifier|synchronized
name|Endpoint
name|createEndpoint
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|mexEndpoint
operator|==
literal|null
condition|)
block|{
name|MEXJaxWsServerFactoryBean
name|factory
init|=
operator|new
name|MEXJaxWsServerFactoryBean
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|Endpoint
name|endpoint
init|=
name|factory
operator|.
name|createEndpoint
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|setInvoker
argument_list|(
operator|new
name|JAXWSMethodInvoker
argument_list|(
name|ep
argument_list|)
argument_list|)
expr_stmt|;
name|mexEndpoint
operator|=
name|endpoint
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
return|return
name|mexEndpoint
return|;
block|}
block|}
end_class

end_unit

