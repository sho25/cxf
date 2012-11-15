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
name|corba
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
name|corba
operator|.
name|CorbaDestination
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
name|corba
operator|.
name|CorbaMessage
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
name|corba
operator|.
name|CorbaStreamable
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
name|corba
operator|.
name|CorbaTypeMap
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
name|corba
operator|.
name|runtime
operator|.
name|CorbaStreamReader
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
name|corba
operator|.
name|types
operator|.
name|CorbaHandlerUtils
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
name|corba
operator|.
name|types
operator|.
name|CorbaObjectHandler
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
name|corba
operator|.
name|types
operator|.
name|CorbaTypeEventProducer
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
name|corba
operator|.
name|types
operator|.
name|HandlerIterator
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
name|corba
operator|.
name|types
operator|.
name|ParameterEventProducer
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
name|corba
operator|.
name|types
operator|.
name|WrappedParameterSequenceEventProducer
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
name|corba
operator|.
name|utils
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
name|binding
operator|.
name|corba
operator|.
name|utils
operator|.
name|CorbaAnyHelper
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
name|corba
operator|.
name|utils
operator|.
name|CorbaUtils
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
name|corba
operator|.
name|wsdl
operator|.
name|ModeType
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
name|corba
operator|.
name|wsdl
operator|.
name|OperationType
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
name|corba
operator|.
name|wsdl
operator|.
name|ParamType
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
name|InterfaceInfo
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
name|omg
operator|.
name|CORBA
operator|.
name|Any
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|NVList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ORB
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ServerRequest
import|;
end_import

begin_class
specifier|public
class|class
name|CorbaStreamInInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
name|CorbaStreamInInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
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
name|ContextUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|handleReply
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|handleRequest
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|handleReply
parameter_list|(
name|Message
name|msg
parameter_list|)
block|{
name|ORB
name|orb
decl_stmt|;
name|ServiceInfo
name|service
decl_stmt|;
name|CorbaDestination
name|destination
decl_stmt|;
if|if
condition|(
name|msg
operator|.
name|getDestination
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|destination
operator|=
operator|(
name|CorbaDestination
operator|)
name|msg
operator|.
name|getDestination
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|destination
operator|=
operator|(
name|CorbaDestination
operator|)
name|msg
operator|.
name|getExchange
argument_list|()
operator|.
name|getDestination
argument_list|()
expr_stmt|;
block|}
name|service
operator|=
name|destination
operator|.
name|getBindingInfo
argument_list|()
operator|.
name|getService
argument_list|()
expr_stmt|;
name|CorbaMessage
name|message
init|=
operator|(
name|CorbaMessage
operator|)
name|msg
decl_stmt|;
if|if
condition|(
name|message
operator|.
name|getStreamableException
argument_list|()
operator|!=
literal|null
operator|||
name|message
operator|.
name|getSystemException
argument_list|()
operator|!=
literal|null
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
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
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
name|CorbaMessage
name|outMessage
init|=
operator|(
name|CorbaMessage
operator|)
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
name|orb
operator|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|ORB
operator|.
name|class
argument_list|)
expr_stmt|;
name|HandlerIterator
name|paramIterator
init|=
operator|new
name|HandlerIterator
argument_list|(
name|outMessage
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|CorbaTypeEventProducer
name|eventProducer
init|=
literal|null
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
name|bindingOpInfo
init|=
name|exchange
operator|.
name|get
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|BindingMessageInfo
name|msgInfo
init|=
name|bindingOpInfo
operator|.
name|getOutput
argument_list|()
decl_stmt|;
name|boolean
name|wrap
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|bindingOpInfo
operator|.
name|isUnwrappedCapable
argument_list|()
condition|)
block|{
name|wrap
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|wrap
condition|)
block|{
comment|// wrapper element around our args
comment|// REVISIT, bravi, message name same as the element name
name|QName
name|wrapperElementQName
init|=
name|msgInfo
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|eventProducer
operator|=
operator|new
name|WrappedParameterSequenceEventProducer
argument_list|(
name|wrapperElementQName
argument_list|,
name|paramIterator
argument_list|,
name|service
argument_list|,
name|orb
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|eventProducer
operator|=
operator|new
name|ParameterEventProducer
argument_list|(
name|paramIterator
argument_list|,
name|service
argument_list|,
name|orb
argument_list|)
expr_stmt|;
block|}
name|CorbaStreamReader
name|reader
init|=
operator|new
name|CorbaStreamReader
argument_list|(
name|eventProducer
argument_list|)
decl_stmt|;
name|message
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
block|}
specifier|private
name|void
name|handleRequest
parameter_list|(
name|Message
name|msg
parameter_list|)
block|{
name|ORB
name|orb
decl_stmt|;
name|ServiceInfo
name|service
decl_stmt|;
name|CorbaDestination
name|destination
decl_stmt|;
if|if
condition|(
name|msg
operator|.
name|getDestination
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|destination
operator|=
operator|(
name|CorbaDestination
operator|)
name|msg
operator|.
name|getDestination
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|destination
operator|=
operator|(
name|CorbaDestination
operator|)
name|msg
operator|.
name|getExchange
argument_list|()
operator|.
name|getDestination
argument_list|()
expr_stmt|;
block|}
name|service
operator|=
name|destination
operator|.
name|getBindingInfo
argument_list|()
operator|.
name|getService
argument_list|()
expr_stmt|;
name|CorbaMessage
name|message
init|=
operator|(
name|CorbaMessage
operator|)
name|msg
decl_stmt|;
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|CorbaTypeMap
name|typeMap
init|=
name|message
operator|.
name|getCorbaTypeMap
argument_list|()
decl_stmt|;
name|BindingInfo
name|bInfo
init|=
name|destination
operator|.
name|getBindingInfo
argument_list|()
decl_stmt|;
name|InterfaceInfo
name|info
init|=
name|bInfo
operator|.
name|getInterface
argument_list|()
decl_stmt|;
name|String
name|opName
init|=
name|exchange
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|BindingOperationInfo
argument_list|>
name|i
init|=
name|bInfo
operator|.
name|getOperations
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|OperationType
name|opType
init|=
literal|null
decl_stmt|;
name|BindingOperationInfo
name|bopInfo
init|=
literal|null
decl_stmt|;
name|QName
name|opQName
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|i
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|bopInfo
operator|=
name|i
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
name|bopInfo
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
argument_list|)
condition|)
block|{
name|opType
operator|=
name|bopInfo
operator|.
name|getExtensor
argument_list|(
name|OperationType
operator|.
name|class
argument_list|)
expr_stmt|;
name|opQName
operator|=
name|bopInfo
operator|.
name|getName
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|opType
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Couldn't find the binding operation for "
operator|+
name|opName
argument_list|)
throw|;
block|}
name|orb
operator|=
name|exchange
operator|.
name|get
argument_list|(
name|ORB
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServerRequest
name|request
init|=
name|exchange
operator|.
name|get
argument_list|(
name|ServerRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|NVList
name|list
init|=
name|prepareArguments
argument_list|(
name|message
argument_list|,
name|info
argument_list|,
name|opType
argument_list|,
name|opQName
argument_list|,
name|typeMap
argument_list|,
name|destination
argument_list|,
name|service
argument_list|)
decl_stmt|;
name|request
operator|.
name|arguments
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|message
operator|.
name|setList
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|HandlerIterator
name|paramIterator
init|=
operator|new
name|HandlerIterator
argument_list|(
name|message
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|CorbaTypeEventProducer
name|eventProducer
init|=
literal|null
decl_stmt|;
name|BindingMessageInfo
name|msgInfo
init|=
name|bopInfo
operator|.
name|getInput
argument_list|()
decl_stmt|;
name|boolean
name|wrap
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|bopInfo
operator|.
name|isUnwrappedCapable
argument_list|()
condition|)
block|{
name|wrap
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|wrap
condition|)
block|{
comment|// wrapper element around our args
name|QName
name|wrapperElementQName
init|=
name|msgInfo
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|eventProducer
operator|=
operator|new
name|WrappedParameterSequenceEventProducer
argument_list|(
name|wrapperElementQName
argument_list|,
name|paramIterator
argument_list|,
name|service
argument_list|,
name|orb
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|eventProducer
operator|=
operator|new
name|ParameterEventProducer
argument_list|(
name|paramIterator
argument_list|,
name|service
argument_list|,
name|orb
argument_list|)
expr_stmt|;
block|}
name|CorbaStreamReader
name|reader
init|=
operator|new
name|CorbaStreamReader
argument_list|(
name|eventProducer
argument_list|)
decl_stmt|;
name|message
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
block|}
specifier|protected
name|NVList
name|prepareArguments
parameter_list|(
name|CorbaMessage
name|corbaMsg
parameter_list|,
name|InterfaceInfo
name|info
parameter_list|,
name|OperationType
name|opType
parameter_list|,
name|QName
name|opQName
parameter_list|,
name|CorbaTypeMap
name|typeMap
parameter_list|,
name|CorbaDestination
name|destination
parameter_list|,
name|ServiceInfo
name|service
parameter_list|)
block|{
name|BindingInfo
name|bInfo
init|=
name|destination
operator|.
name|getBindingInfo
argument_list|()
decl_stmt|;
name|EndpointInfo
name|eptInfo
init|=
name|destination
operator|.
name|getEndPointInfo
argument_list|()
decl_stmt|;
name|BindingOperationInfo
name|bOpInfo
init|=
name|bInfo
operator|.
name|getOperation
argument_list|(
name|opQName
argument_list|)
decl_stmt|;
name|OperationInfo
name|opInfo
init|=
name|bOpInfo
operator|.
name|getOperationInfo
argument_list|()
decl_stmt|;
name|Exchange
name|exg
init|=
name|corbaMsg
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|exg
operator|.
name|put
argument_list|(
name|BindingInfo
operator|.
name|class
argument_list|,
name|bInfo
argument_list|)
expr_stmt|;
name|exg
operator|.
name|put
argument_list|(
name|InterfaceInfo
operator|.
name|class
argument_list|,
name|info
argument_list|)
expr_stmt|;
name|exg
operator|.
name|put
argument_list|(
name|EndpointInfo
operator|.
name|class
argument_list|,
name|eptInfo
argument_list|)
expr_stmt|;
name|exg
operator|.
name|put
argument_list|(
name|EndpointReferenceType
operator|.
name|class
argument_list|,
name|destination
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|exg
operator|.
name|put
argument_list|(
name|ServiceInfo
operator|.
name|class
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|exg
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|bOpInfo
argument_list|)
expr_stmt|;
name|exg
operator|.
name|put
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|,
name|opInfo
argument_list|)
expr_stmt|;
name|exg
operator|.
name|put
argument_list|(
name|MessageInfo
operator|.
name|class
argument_list|,
name|opInfo
operator|.
name|getInput
argument_list|()
argument_list|)
expr_stmt|;
name|exg
operator|.
name|put
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|opQName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|exg
operator|.
name|setInMessage
argument_list|(
name|corbaMsg
argument_list|)
expr_stmt|;
name|corbaMsg
operator|.
name|put
argument_list|(
name|MessageInfo
operator|.
name|class
argument_list|,
name|opInfo
operator|.
name|getInput
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ParamType
argument_list|>
name|paramTypes
init|=
name|opType
operator|.
name|getParam
argument_list|()
decl_stmt|;
name|CorbaStreamable
index|[]
name|arguments
init|=
operator|new
name|CorbaStreamable
index|[
name|paramTypes
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|NVList
name|list
init|=
name|prepareDIIArgsList
argument_list|(
name|corbaMsg
argument_list|,
name|bOpInfo
argument_list|,
name|arguments
argument_list|,
name|paramTypes
argument_list|,
name|typeMap
argument_list|,
name|exg
operator|.
name|get
argument_list|(
name|ORB
operator|.
name|class
argument_list|)
argument_list|,
name|service
argument_list|)
decl_stmt|;
return|return
name|list
return|;
block|}
specifier|protected
name|NVList
name|prepareDIIArgsList
parameter_list|(
name|CorbaMessage
name|corbaMsg
parameter_list|,
name|BindingOperationInfo
name|boi
parameter_list|,
name|CorbaStreamable
index|[]
name|streamables
parameter_list|,
name|List
argument_list|<
name|ParamType
argument_list|>
name|paramTypes
parameter_list|,
name|CorbaTypeMap
name|map
parameter_list|,
name|ORB
name|orb
parameter_list|,
name|ServiceInfo
name|service
parameter_list|)
block|{
try|try
block|{
comment|// Build the list of DII arguments, returns, and exceptions
name|NVList
name|list
init|=
name|orb
operator|.
name|create_list
argument_list|(
name|streamables
operator|.
name|length
argument_list|)
decl_stmt|;
name|OperationInfo
name|opInfo
init|=
name|boi
operator|.
name|getOperationInfo
argument_list|()
decl_stmt|;
name|MessageInfo
name|input
init|=
name|opInfo
operator|.
name|getInput
argument_list|()
decl_stmt|;
name|MessageInfo
name|output
init|=
name|opInfo
operator|.
name|getOutput
argument_list|()
decl_stmt|;
name|String
name|inWrapNSUri
init|=
literal|null
decl_stmt|;
name|String
name|outWrapNSUri
init|=
literal|null
decl_stmt|;
name|boolean
name|wrap
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|boi
operator|.
name|isUnwrappedCapable
argument_list|()
condition|)
block|{
name|wrap
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|input
operator|!=
literal|null
condition|)
block|{
name|inWrapNSUri
operator|=
name|getWrappedParamNamespace
argument_list|(
name|input
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|CorbaUtils
operator|.
name|isElementFormQualified
argument_list|(
name|service
argument_list|,
name|inWrapNSUri
argument_list|)
condition|)
block|{
name|inWrapNSUri
operator|=
literal|""
expr_stmt|;
block|}
block|}
if|if
condition|(
name|output
operator|!=
literal|null
condition|)
block|{
name|outWrapNSUri
operator|=
name|getWrappedParamNamespace
argument_list|(
name|output
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|CorbaUtils
operator|.
name|isElementFormQualified
argument_list|(
name|service
argument_list|,
name|outWrapNSUri
argument_list|)
condition|)
block|{
name|outWrapNSUri
operator|=
literal|""
expr_stmt|;
block|}
block|}
block|}
name|int
name|inMsgIndex
init|=
literal|0
decl_stmt|;
name|int
name|outMsgIndex
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|paramTypes
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|ParamType
name|param
init|=
name|paramTypes
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|QName
name|paramIdlType
init|=
name|param
operator|.
name|getIdltype
argument_list|()
decl_stmt|;
name|QName
name|paramName
decl_stmt|;
name|ModeType
name|paramMode
init|=
name|param
operator|.
name|getMode
argument_list|()
decl_stmt|;
if|if
condition|(
name|paramMode
operator|.
name|value
argument_list|()
operator|.
name|equals
argument_list|(
literal|"in"
argument_list|)
condition|)
block|{
if|if
condition|(
name|wrap
condition|)
block|{
name|paramName
operator|=
operator|new
name|QName
argument_list|(
name|inWrapNSUri
argument_list|,
name|param
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|paramName
operator|=
name|getMessageParamQName
argument_list|(
name|input
argument_list|,
name|param
operator|.
name|getName
argument_list|()
argument_list|,
name|inMsgIndex
argument_list|)
expr_stmt|;
name|inMsgIndex
operator|++
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|wrap
condition|)
block|{
name|paramName
operator|=
operator|new
name|QName
argument_list|(
name|outWrapNSUri
argument_list|,
name|param
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|paramName
operator|=
name|getMessageParamQName
argument_list|(
name|output
argument_list|,
name|param
operator|.
name|getName
argument_list|()
argument_list|,
name|outMsgIndex
argument_list|)
expr_stmt|;
name|outMsgIndex
operator|++
expr_stmt|;
block|}
block|}
name|CorbaObjectHandler
name|obj
init|=
name|CorbaHandlerUtils
operator|.
name|initializeObjectHandler
argument_list|(
name|orb
argument_list|,
name|paramName
argument_list|,
name|paramIdlType
argument_list|,
name|map
argument_list|,
name|service
argument_list|)
decl_stmt|;
name|streamables
index|[
name|i
index|]
operator|=
name|corbaMsg
operator|.
name|createStreamableObject
argument_list|(
name|obj
argument_list|,
name|paramName
argument_list|)
expr_stmt|;
name|Any
name|value
init|=
name|CorbaAnyHelper
operator|.
name|createAny
argument_list|(
name|orb
argument_list|)
decl_stmt|;
if|if
condition|(
name|paramMode
operator|.
name|value
argument_list|()
operator|.
name|equals
argument_list|(
literal|"in"
argument_list|)
condition|)
block|{
name|streamables
index|[
name|i
index|]
operator|.
name|setMode
argument_list|(
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ARG_IN
operator|.
name|value
argument_list|)
expr_stmt|;
name|streamables
index|[
name|i
index|]
operator|.
name|getObject
argument_list|()
operator|.
name|setIntoAny
argument_list|(
name|value
argument_list|,
name|streamables
index|[
name|i
index|]
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|paramMode
operator|.
name|value
argument_list|()
operator|.
name|equals
argument_list|(
literal|"out"
argument_list|)
condition|)
block|{
name|streamables
index|[
name|i
index|]
operator|.
name|setMode
argument_list|(
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ARG_OUT
operator|.
name|value
argument_list|)
expr_stmt|;
name|streamables
index|[
name|i
index|]
operator|.
name|getObject
argument_list|()
operator|.
name|setIntoAny
argument_list|(
name|value
argument_list|,
name|streamables
index|[
name|i
index|]
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|streamables
index|[
name|i
index|]
operator|.
name|setMode
argument_list|(
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ARG_INOUT
operator|.
name|value
argument_list|)
expr_stmt|;
name|streamables
index|[
name|i
index|]
operator|.
name|getObject
argument_list|()
operator|.
name|setIntoAny
argument_list|(
name|value
argument_list|,
name|streamables
index|[
name|i
index|]
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|list
operator|.
name|add_value
argument_list|(
name|streamables
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
argument_list|,
name|value
argument_list|,
name|streamables
index|[
name|i
index|]
operator|.
name|getMode
argument_list|()
argument_list|)
expr_stmt|;
name|corbaMsg
operator|.
name|addStreamableArgument
argument_list|(
name|streamables
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|QName
name|getMessageParamQName
parameter_list|(
name|MessageInfo
name|msgInfo
parameter_list|,
name|String
name|paramName
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|QName
name|paramQName
init|=
literal|null
decl_stmt|;
name|MessagePartInfo
name|part
init|=
name|msgInfo
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|part
operator|!=
literal|null
operator|&&
name|part
operator|.
name|isElement
argument_list|()
condition|)
block|{
name|paramQName
operator|=
name|part
operator|.
name|getElementQName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|part
operator|!=
literal|null
condition|)
block|{
name|paramQName
operator|=
name|part
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
return|return
name|paramQName
return|;
block|}
specifier|protected
name|String
name|getWrappedParamNamespace
parameter_list|(
name|MessageInfo
name|msgInfo
parameter_list|)
block|{
name|MessagePartInfo
name|part
init|=
name|msgInfo
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|part
operator|.
name|isElement
argument_list|()
condition|)
block|{
return|return
name|part
operator|.
name|getElementQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|part
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

