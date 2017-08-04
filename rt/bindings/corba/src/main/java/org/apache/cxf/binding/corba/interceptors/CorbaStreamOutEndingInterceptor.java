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
name|XMLStreamWriter
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
name|CorbaStreamWriter
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
name|ArgType
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
name|CorbaConstants
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

begin_class
specifier|public
class|class
name|CorbaStreamOutEndingInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|private
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ORB
name|orb
decl_stmt|;
specifier|private
name|CorbaTypeMap
name|typeMap
decl_stmt|;
specifier|private
name|ServiceInfo
name|service
decl_stmt|;
specifier|public
name|CorbaStreamOutEndingInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|USER_STREAM
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|msg
parameter_list|)
block|{
name|CorbaMessage
name|message
init|=
operator|(
name|CorbaMessage
operator|)
name|msg
decl_stmt|;
name|orb
operator|=
operator|(
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ORB
operator|)
name|message
operator|.
name|get
argument_list|(
name|CorbaConstants
operator|.
name|ORB
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|BindingOperationInfo
name|boi
init|=
name|exchange
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
name|service
operator|=
name|exchange
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getService
argument_list|()
expr_stmt|;
name|typeMap
operator|=
name|message
operator|.
name|getCorbaTypeMap
argument_list|()
expr_stmt|;
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
name|handleOutBoundMessage
argument_list|(
name|message
argument_list|,
name|boi
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|handleInBoundMessage
argument_list|(
name|message
argument_list|,
name|boi
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|handleOutBoundMessage
parameter_list|(
name|CorbaMessage
name|message
parameter_list|,
name|BindingOperationInfo
name|boi
parameter_list|)
block|{
name|OperationInfo
name|opInfo
init|=
name|boi
operator|.
name|getOperationInfo
argument_list|()
decl_stmt|;
name|OperationType
name|opType
init|=
name|boi
operator|.
name|getExtensor
argument_list|(
name|OperationType
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|MessageInfo
name|outMsgInfo
init|=
name|opInfo
operator|.
name|getOutput
argument_list|()
decl_stmt|;
name|String
name|wrapNSUri
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
name|outMsgInfo
operator|!=
literal|null
condition|)
block|{
name|wrapNSUri
operator|=
name|getWrappedParamNamespace
argument_list|(
name|outMsgInfo
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
name|wrapNSUri
argument_list|)
condition|)
block|{
name|wrapNSUri
operator|=
literal|""
expr_stmt|;
block|}
block|}
block|}
name|CorbaStreamWriter
name|writer
init|=
operator|(
name|CorbaStreamWriter
operator|)
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|CorbaObjectHandler
index|[]
name|objs
init|=
name|writer
operator|.
name|getCorbaObjects
argument_list|()
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
name|int
name|msgIndex
init|=
literal|0
decl_stmt|;
name|ArgType
name|returnParam
init|=
name|opType
operator|.
name|getReturn
argument_list|()
decl_stmt|;
if|if
condition|(
name|returnParam
operator|!=
literal|null
condition|)
block|{
name|QName
name|retName
decl_stmt|;
if|if
condition|(
name|wrap
condition|)
block|{
name|retName
operator|=
operator|new
name|QName
argument_list|(
name|wrapNSUri
argument_list|,
name|returnParam
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retName
operator|=
name|getMessageParamQName
argument_list|(
name|outMsgInfo
argument_list|,
name|returnParam
operator|.
name|getName
argument_list|()
argument_list|,
name|msgIndex
argument_list|)
expr_stmt|;
block|}
name|QName
name|retIdlType
init|=
name|returnParam
operator|.
name|getIdltype
argument_list|()
decl_stmt|;
name|CorbaObjectHandler
name|obj
init|=
name|CorbaHandlerUtils
operator|.
name|initializeObjectHandler
argument_list|(
name|orb
argument_list|,
name|retName
argument_list|,
name|retIdlType
argument_list|,
name|typeMap
argument_list|,
name|service
argument_list|)
decl_stmt|;
name|CorbaStreamable
name|streamable
init|=
name|message
operator|.
name|createStreamableObject
argument_list|(
name|obj
argument_list|,
name|retName
argument_list|)
decl_stmt|;
name|message
operator|.
name|setStreamableReturn
argument_list|(
name|streamable
argument_list|)
expr_stmt|;
name|msgIndex
operator|++
expr_stmt|;
block|}
for|for
control|(
name|Iterator
argument_list|<
name|ParamType
argument_list|>
name|iter
init|=
name|paramTypes
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ParamType
name|param
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|QName
name|idlType
init|=
name|param
operator|.
name|getIdltype
argument_list|()
decl_stmt|;
name|QName
name|paramName
decl_stmt|;
name|CorbaObjectHandler
name|obj
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|param
operator|.
name|getMode
argument_list|()
operator|.
name|equals
argument_list|(
name|ModeType
operator|.
name|OUT
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
name|wrapNSUri
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
name|outMsgInfo
argument_list|,
name|param
operator|.
name|getName
argument_list|()
argument_list|,
name|msgIndex
argument_list|)
expr_stmt|;
block|}
name|obj
operator|=
name|CorbaHandlerUtils
operator|.
name|initializeObjectHandler
argument_list|(
name|orb
argument_list|,
name|paramName
argument_list|,
name|idlType
argument_list|,
name|typeMap
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|msgIndex
operator|++
expr_stmt|;
block|}
else|else
block|{
name|obj
operator|=
name|objs
index|[
name|count
operator|++
index|]
expr_stmt|;
name|paramName
operator|=
name|obj
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|CorbaStreamable
name|streamable
init|=
name|message
operator|.
name|createStreamableObject
argument_list|(
name|obj
argument_list|,
name|paramName
argument_list|)
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
name|streamable
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
literal|"inout"
argument_list|)
condition|)
block|{
name|streamable
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
block|}
comment|// default mode is out
name|message
operator|.
name|addStreamableArgument
argument_list|(
name|streamable
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|handleInBoundMessage
parameter_list|(
name|CorbaMessage
name|message
parameter_list|,
name|BindingOperationInfo
name|boi
parameter_list|)
block|{
name|OperationInfo
name|opInfo
init|=
name|boi
operator|.
name|getOperationInfo
argument_list|()
decl_stmt|;
name|OperationType
name|opType
init|=
name|boi
operator|.
name|getExtensor
argument_list|(
name|OperationType
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|MessageInfo
name|msgInInfo
init|=
name|opInfo
operator|.
name|getInput
argument_list|()
decl_stmt|;
name|String
name|wrapNSUri
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
name|msgInInfo
operator|!=
literal|null
condition|)
block|{
name|wrapNSUri
operator|=
name|getWrappedParamNamespace
argument_list|(
name|msgInInfo
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
name|wrapNSUri
argument_list|)
condition|)
block|{
name|wrapNSUri
operator|=
literal|""
expr_stmt|;
block|}
block|}
block|}
name|CorbaStreamWriter
name|writer
init|=
operator|(
name|CorbaStreamWriter
operator|)
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|CorbaObjectHandler
index|[]
name|objs
init|=
name|writer
operator|.
name|getCorbaObjects
argument_list|()
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
name|int
name|msgIndex
init|=
literal|0
decl_stmt|;
name|ArgType
name|returnParam
init|=
name|opType
operator|.
name|getReturn
argument_list|()
decl_stmt|;
if|if
condition|(
name|returnParam
operator|!=
literal|null
condition|)
block|{
name|CorbaObjectHandler
name|obj
init|=
name|objs
index|[
name|count
operator|++
index|]
decl_stmt|;
name|QName
name|retName
init|=
name|obj
operator|.
name|getName
argument_list|()
decl_stmt|;
name|CorbaStreamable
name|streamable
init|=
name|message
operator|.
name|createStreamableObject
argument_list|(
name|obj
argument_list|,
name|retName
argument_list|)
decl_stmt|;
name|message
operator|.
name|setStreamableReturn
argument_list|(
name|streamable
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Iterator
argument_list|<
name|ParamType
argument_list|>
name|iter
init|=
name|paramTypes
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ParamType
name|param
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|QName
name|idlType
init|=
name|param
operator|.
name|getIdltype
argument_list|()
decl_stmt|;
name|QName
name|paramName
decl_stmt|;
name|CorbaObjectHandler
name|obj
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|param
operator|.
name|getMode
argument_list|()
operator|.
name|equals
argument_list|(
name|ModeType
operator|.
name|IN
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
name|wrapNSUri
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
name|msgInInfo
argument_list|,
name|param
operator|.
name|getName
argument_list|()
argument_list|,
name|msgIndex
argument_list|)
expr_stmt|;
block|}
name|obj
operator|=
name|CorbaHandlerUtils
operator|.
name|initializeObjectHandler
argument_list|(
name|orb
argument_list|,
name|paramName
argument_list|,
name|idlType
argument_list|,
name|typeMap
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|msgIndex
operator|++
expr_stmt|;
block|}
else|else
block|{
name|obj
operator|=
name|objs
index|[
name|count
operator|++
index|]
expr_stmt|;
name|paramName
operator|=
name|obj
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|CorbaStreamable
name|streamable
init|=
name|message
operator|.
name|createStreamableObject
argument_list|(
name|obj
argument_list|,
name|paramName
argument_list|)
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
name|streamable
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
literal|"inout"
argument_list|)
condition|)
block|{
name|streamable
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
name|streamable
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
block|}
name|message
operator|.
name|addStreamableArgument
argument_list|(
name|streamable
argument_list|)
expr_stmt|;
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
end_class

end_unit

