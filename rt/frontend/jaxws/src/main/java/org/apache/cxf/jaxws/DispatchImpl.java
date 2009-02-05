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
name|jaxws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|HttpURLConnection
import|;
end_import

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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|FutureTask
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
name|activation
operator|.
name|DataSource
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
name|Detail
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
name|SOAPException
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
name|SOAPFactory
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
name|SOAPFault
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|AsyncHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Binding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingProvider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Dispatch
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|MessageContext
operator|.
name|Scope
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|http
operator|.
name|HTTPBinding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|http
operator|.
name|HTTPException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPBinding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPFaultException
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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|interceptor
operator|.
name|SoapPreProtocolOutInterceptor
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
name|Client
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
name|ConduitSelector
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
name|UpfrontConduitSelector
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
name|Interceptor
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
name|InterceptorProvider
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
name|MessageSenderInterceptor
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
name|context
operator|.
name|WrappedMessageContext
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
name|handler
operator|.
name|logical
operator|.
name|DispatchLogicalHandlerInterceptor
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
name|handler
operator|.
name|soap
operator|.
name|DispatchSOAPHandlerInterceptor
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
name|interceptors
operator|.
name|DispatchInDatabindingInterceptor
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
name|interceptors
operator|.
name|DispatchOutDatabindingInterceptor
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
name|support
operator|.
name|JaxWsEndpointImpl
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
name|phase
operator|.
name|PhaseInterceptorChain
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
name|PhaseManager
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
name|transport
operator|.
name|MessageObserver
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
name|workqueue
operator|.
name|WorkQueueManager
import|;
end_import

begin_class
specifier|public
class|class
name|DispatchImpl
parameter_list|<
name|T
parameter_list|>
extends|extends
name|BindingProviderImpl
implements|implements
name|Dispatch
argument_list|<
name|T
argument_list|>
implements|,
name|MessageObserver
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
name|DispatchImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FINISHED
init|=
literal|"exchange.finished"
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|InterceptorProvider
name|iProvider
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|T
argument_list|>
name|cl
decl_stmt|;
specifier|private
name|Executor
name|executor
decl_stmt|;
specifier|private
name|JAXBContext
name|context
decl_stmt|;
specifier|private
name|Service
operator|.
name|Mode
name|mode
decl_stmt|;
specifier|private
name|ConduitSelector
name|conduitSelector
decl_stmt|;
name|DispatchImpl
parameter_list|(
name|Bus
name|b
parameter_list|,
name|Client
name|client
parameter_list|,
name|Service
operator|.
name|Mode
name|m
parameter_list|,
name|JAXBContext
name|ctx
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|Executor
name|e
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|JaxWsEndpointImpl
operator|)
name|client
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|bus
operator|=
name|b
expr_stmt|;
name|this
operator|.
name|iProvider
operator|=
name|client
expr_stmt|;
name|executor
operator|=
name|e
expr_stmt|;
name|context
operator|=
name|ctx
expr_stmt|;
name|cl
operator|=
name|clazz
expr_stmt|;
name|mode
operator|=
name|m
expr_stmt|;
name|getConduitSelector
argument_list|()
operator|.
name|setEndpoint
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|setupEndpointAddressContext
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|DispatchImpl
parameter_list|(
name|Bus
name|b
parameter_list|,
name|Client
name|cl
parameter_list|,
name|Service
operator|.
name|Mode
name|m
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|Executor
name|e
parameter_list|)
block|{
name|this
argument_list|(
name|b
argument_list|,
name|cl
argument_list|,
name|m
argument_list|,
literal|null
argument_list|,
name|clazz
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setupEndpointAddressContext
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
comment|//NOTE for jms transport the address would be null
if|if
condition|(
literal|null
operator|!=
name|endpoint
operator|&&
literal|null
operator|!=
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
name|this
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|T
name|invoke
parameter_list|(
name|T
name|obj
parameter_list|)
block|{
return|return
name|invoke
argument_list|(
name|obj
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
name|T
name|invoke
parameter_list|(
name|T
name|obj
parameter_list|,
name|boolean
name|isOneWay
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Dispatch: invoke called"
argument_list|)
expr_stmt|;
block|}
name|Bus
name|origBus
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
try|try
block|{
name|Endpoint
name|endpoint
init|=
name|getEndpoint
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|endpoint
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setContent
argument_list|(
name|JAXBContext
operator|.
name|class
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|reqContext
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|WrappedMessageContext
name|ctx
init|=
operator|new
name|WrappedMessageContext
argument_list|(
name|reqContext
argument_list|,
literal|null
argument_list|,
name|Scope
operator|.
name|APPLICATION
argument_list|)
decl_stmt|;
name|ctx
operator|.
name|putAll
argument_list|(
name|this
operator|.
name|getRequestContext
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|respContext
init|=
name|this
operator|.
name|getResponseContext
argument_list|()
decl_stmt|;
comment|// clear the response context's hold information
comment|// Not call the clear Context is to avoid the error
comment|// that getResponseContext() would be called by Client code first
name|respContext
operator|.
name|clear
argument_list|()
expr_stmt|;
name|message
operator|.
name|putAll
argument_list|(
name|reqContext
argument_list|)
expr_stmt|;
comment|//need to do context mapping from jax-ws to cxf message
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setOneWay
argument_list|(
name|isOneWay
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOutMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|setExchangeProperties
argument_list|(
name|exchange
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|obj
argument_list|)
expr_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|SOAPMessage
condition|)
block|{
name|message
operator|.
name|setContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|Source
condition|)
block|{
name|message
operator|.
name|setContent
argument_list|(
name|Source
operator|.
name|class
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|DataSource
condition|)
block|{
name|message
operator|.
name|setContent
argument_list|(
name|DataSource
operator|.
name|class
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|PhaseInterceptorChain
name|chain
init|=
name|getDispatchOutChain
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|message
operator|.
name|setInterceptorChain
argument_list|(
name|chain
argument_list|)
expr_stmt|;
comment|// setup conduit selector
name|prepareConduitSelector
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|// execute chain
name|chain
operator|.
name|doIntercept
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|Exception
name|exp
init|=
name|message
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|exp
operator|==
literal|null
operator|&&
name|exchange
operator|.
name|getInMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exp
operator|=
name|exchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exp
operator|!=
literal|null
condition|)
block|{
name|getConduitSelector
argument_list|()
operator|.
name|complete
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|getBinding
argument_list|()
operator|instanceof
name|SOAPBinding
operator|&&
name|exp
operator|instanceof
name|Fault
condition|)
block|{
try|try
block|{
name|SOAPFault
name|soapFault
init|=
name|SOAPFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createFault
argument_list|()
decl_stmt|;
name|Fault
name|fault
init|=
operator|(
name|Fault
operator|)
name|exp
decl_stmt|;
name|soapFault
operator|.
name|setFaultCode
argument_list|(
name|fault
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
name|soapFault
operator|.
name|setFaultString
argument_list|(
name|fault
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|fault
operator|.
name|getDetail
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Detail
name|det
init|=
name|soapFault
operator|.
name|addDetail
argument_list|()
decl_stmt|;
name|Element
name|fd
init|=
name|fault
operator|.
name|getDetail
argument_list|()
decl_stmt|;
name|Node
name|child
init|=
name|fd
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
name|Node
name|next
init|=
name|child
operator|.
name|getNextSibling
argument_list|()
decl_stmt|;
name|det
operator|.
name|appendChild
argument_list|(
name|det
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|importNode
argument_list|(
name|child
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|child
operator|=
name|next
expr_stmt|;
block|}
block|}
name|SOAPFaultException
name|ex
init|=
operator|new
name|SOAPFaultException
argument_list|(
name|soapFault
argument_list|)
decl_stmt|;
name|ex
operator|.
name|initCause
argument_list|(
name|exp
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|getBinding
argument_list|()
operator|instanceof
name|HTTPBinding
condition|)
block|{
name|HTTPException
name|exception
init|=
operator|new
name|HTTPException
argument_list|(
name|HttpURLConnection
operator|.
name|HTTP_INTERNAL_ERROR
argument_list|)
decl_stmt|;
name|exception
operator|.
name|initCause
argument_list|(
name|exp
argument_list|)
expr_stmt|;
throw|throw
name|exception
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|exp
argument_list|)
throw|;
block|}
block|}
comment|// correlate response
if|if
condition|(
name|getConduitSelector
argument_list|()
operator|.
name|selectConduit
argument_list|(
name|message
argument_list|)
operator|.
name|getBackChannel
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// process partial response and wait for decoupled response
block|}
else|else
block|{
comment|// process response: send was synchronous so when we get here we can assume that the
comment|// Exchange's inbound message is set and had been passed through the inbound
comment|// interceptor chain.
block|}
if|if
condition|(
operator|!
name|isOneWay
condition|)
block|{
synchronized|synchronized
init|(
name|exchange
init|)
block|{
name|Message
name|inMsg
init|=
name|waitResponse
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|respContext
operator|.
name|putAll
argument_list|(
name|inMsg
argument_list|)
expr_stmt|;
name|getConduitSelector
argument_list|()
operator|.
name|complete
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
name|cl
operator|.
name|cast
argument_list|(
name|inMsg
operator|.
name|getContent
argument_list|(
name|Object
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
finally|finally
block|{
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|origBus
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Message
name|waitResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
while|while
condition|(
operator|!
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|exchange
operator|.
name|get
argument_list|(
name|FINISHED
argument_list|)
argument_list|)
condition|)
block|{
try|try
block|{
name|exchange
operator|.
name|wait
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|//TODO - timeout
block|}
block|}
name|Message
name|inMsg
init|=
name|exchange
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|inMsg
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|exchange
operator|.
name|wait
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|//TODO - timeout
block|}
name|inMsg
operator|=
name|exchange
operator|.
name|getInMessage
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|inMsg
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
comment|//TODO - exceptions
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|inMsg
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|inMsg
return|;
block|}
specifier|private
name|PhaseInterceptorChain
name|getDispatchOutChain
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|PhaseManager
name|pm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PhaseManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|PhaseInterceptorChain
name|chain
init|=
operator|new
name|PhaseInterceptorChain
argument_list|(
name|pm
operator|.
name|getOutPhases
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|>
name|il
init|=
name|bus
operator|.
name|getOutInterceptors
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Interceptors contributed by bus: "
operator|+
name|il
argument_list|)
expr_stmt|;
block|}
name|chain
operator|.
name|add
argument_list|(
name|il
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|>
name|i2
init|=
name|iProvider
operator|.
name|getOutInterceptors
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Interceptors contributed by client: "
operator|+
name|i2
argument_list|)
expr_stmt|;
block|}
name|chain
operator|.
name|add
argument_list|(
name|i2
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|instanceof
name|JaxWsEndpointImpl
condition|)
block|{
name|Binding
name|jaxwsBinding
init|=
operator|(
operator|(
name|JaxWsEndpointImpl
operator|)
name|endpoint
operator|)
operator|.
name|getJaxwsBinding
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getBinding
argument_list|()
operator|instanceof
name|SoapBinding
condition|)
block|{
name|chain
operator|.
name|add
argument_list|(
operator|new
name|DispatchSOAPHandlerInterceptor
argument_list|(
name|jaxwsBinding
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// TODO: what for non soap bindings?
block|}
name|chain
operator|.
name|add
argument_list|(
operator|new
name|DispatchLogicalHandlerInterceptor
argument_list|(
name|jaxwsBinding
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getBinding
argument_list|()
operator|instanceof
name|SOAPBinding
condition|)
block|{
name|chain
operator|.
name|add
argument_list|(
operator|new
name|SoapPreProtocolOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|chain
operator|.
name|add
argument_list|(
operator|new
name|MessageSenderInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
operator|new
name|DispatchOutDatabindingInterceptor
argument_list|(
name|mode
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|chain
return|;
block|}
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|getEndpoint
argument_list|()
decl_stmt|;
name|message
operator|=
name|endpoint
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|PhaseManager
name|pm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PhaseManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|PhaseInterceptorChain
name|chain
init|=
operator|new
name|PhaseInterceptorChain
argument_list|(
name|pm
operator|.
name|getInPhases
argument_list|()
argument_list|)
decl_stmt|;
name|message
operator|.
name|setInterceptorChain
argument_list|(
name|chain
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|>
name|il
init|=
name|bus
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Interceptors contributed by bus: "
operator|+
name|il
argument_list|)
expr_stmt|;
block|}
name|chain
operator|.
name|add
argument_list|(
name|il
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|>
name|i2
init|=
name|iProvider
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Interceptors contributed by client: "
operator|+
name|i2
argument_list|)
expr_stmt|;
block|}
name|chain
operator|.
name|add
argument_list|(
name|i2
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|instanceof
name|JaxWsEndpointImpl
condition|)
block|{
name|Binding
name|jaxwsBinding
init|=
operator|(
operator|(
name|JaxWsEndpointImpl
operator|)
name|endpoint
operator|)
operator|.
name|getJaxwsBinding
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getBinding
argument_list|()
operator|instanceof
name|SoapBinding
condition|)
block|{
name|chain
operator|.
name|add
argument_list|(
operator|new
name|DispatchSOAPHandlerInterceptor
argument_list|(
name|jaxwsBinding
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|DispatchLogicalHandlerInterceptor
name|slhi
init|=
operator|new
name|DispatchLogicalHandlerInterceptor
argument_list|(
name|jaxwsBinding
argument_list|,
name|Phase
operator|.
name|USER_LOGICAL
argument_list|)
decl_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|slhi
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|Interceptor
argument_list|>
name|inInterceptors
init|=
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|>
argument_list|()
decl_stmt|;
name|inInterceptors
operator|.
name|add
argument_list|(
operator|new
name|DispatchInDatabindingInterceptor
argument_list|(
name|cl
argument_list|,
name|mode
argument_list|)
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|inInterceptors
argument_list|)
expr_stmt|;
comment|// execute chain
name|Bus
name|origBus
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
try|try
block|{
name|chain
operator|.
name|doIntercept
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
synchronized|synchronized
init|(
name|message
operator|.
name|getExchange
argument_list|()
init|)
block|{
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|FINISHED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setInMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|origBus
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Executor
name|getExecutor
parameter_list|()
block|{
if|if
condition|(
name|executor
operator|==
literal|null
condition|)
block|{
name|executor
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|WorkQueueManager
operator|.
name|class
argument_list|)
operator|.
name|getAutomaticWorkQueue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|executor
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Can't not get executor"
argument_list|)
expr_stmt|;
block|}
return|return
name|executor
return|;
block|}
specifier|private
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|getConduitSelector
argument_list|()
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
specifier|public
name|Future
argument_list|<
name|?
argument_list|>
name|invokeAsync
parameter_list|(
name|T
name|obj
parameter_list|,
name|AsyncHandler
argument_list|<
name|T
argument_list|>
name|asyncHandler
parameter_list|)
block|{
name|FutureTask
argument_list|<
name|T
argument_list|>
name|f
init|=
operator|new
name|FutureTask
argument_list|<
name|T
argument_list|>
argument_list|(
operator|new
name|DispatchAsyncCallable
argument_list|<
name|T
argument_list|>
argument_list|(
name|this
argument_list|,
name|obj
argument_list|,
name|asyncHandler
argument_list|)
argument_list|)
decl_stmt|;
name|getExecutor
argument_list|()
operator|.
name|execute
argument_list|(
name|f
argument_list|)
expr_stmt|;
return|return
name|f
return|;
block|}
specifier|public
name|Response
argument_list|<
name|T
argument_list|>
name|invokeAsync
parameter_list|(
name|T
name|obj
parameter_list|)
block|{
name|FutureTask
argument_list|<
name|T
argument_list|>
name|f
init|=
operator|new
name|FutureTask
argument_list|<
name|T
argument_list|>
argument_list|(
operator|new
name|DispatchAsyncCallable
argument_list|<
name|T
argument_list|>
argument_list|(
name|this
argument_list|,
name|obj
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|getExecutor
argument_list|()
operator|.
name|execute
argument_list|(
name|f
argument_list|)
expr_stmt|;
return|return
operator|new
name|AsyncResponse
argument_list|<
name|T
argument_list|>
argument_list|(
name|f
argument_list|,
name|cl
argument_list|)
return|;
block|}
specifier|public
name|void
name|invokeOneWay
parameter_list|(
name|T
name|obj
parameter_list|)
block|{
name|invoke
argument_list|(
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|ConduitSelector
name|getConduitSelector
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|==
name|conduitSelector
condition|)
block|{
name|conduitSelector
operator|=
operator|new
name|UpfrontConduitSelector
argument_list|()
expr_stmt|;
block|}
return|return
name|conduitSelector
return|;
block|}
specifier|public
name|void
name|setConduitSelector
parameter_list|(
name|ConduitSelector
name|selector
parameter_list|)
block|{
name|conduitSelector
operator|=
name|selector
expr_stmt|;
block|}
specifier|protected
name|void
name|prepareConduitSelector
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|ConduitSelector
operator|.
name|class
argument_list|,
name|getConduitSelector
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setExchangeProperties
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|exchange
operator|.
name|put
argument_list|(
name|Service
operator|.
name|Mode
operator|.
name|class
argument_list|,
name|mode
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|Class
operator|.
name|class
argument_list|,
name|cl
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
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
name|exchange
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
name|exchange
operator|.
name|put
argument_list|(
name|MessageObserver
operator|.
name|class
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
name|bus
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|EndpointInfo
name|endpointInfo
init|=
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
name|QName
name|serviceQName
init|=
name|endpointInfo
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|exchange
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
name|endpointInfo
operator|.
name|getService
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|exchange
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
name|QName
name|portQName
init|=
name|endpointInfo
operator|.
name|getName
argument_list|()
decl_stmt|;
name|exchange
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
comment|// do nothing
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
name|exchange
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
block|}
end_class

end_unit

