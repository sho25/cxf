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
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|Collection
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
name|ExecutionException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
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
name|handler
operator|.
name|MessageContext
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
name|soap
operator|.
name|SOAPFaultException
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
name|annotations
operator|.
name|UseAsyncMethod
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
name|attachment
operator|.
name|AttachmentImpl
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
name|continuations
operator|.
name|Continuation
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
name|continuations
operator|.
name|ContinuationProvider
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
name|helpers
operator|.
name|CastUtils
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
name|message
operator|.
name|Attachment
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
name|FaultMode
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
name|MessageImpl
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
name|invoker
operator|.
name|Factory
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
name|invoker
operator|.
name|FactoryInvoker
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
name|invoker
operator|.
name|SingletonFactory
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractJAXWSMethodInvoker
extends|extends
name|FactoryInvoker
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ASYNC_METHOD
init|=
literal|"org.apache.cxf.jaxws.async.method"
decl_stmt|;
specifier|public
name|AbstractJAXWSMethodInvoker
parameter_list|(
specifier|final
name|Object
name|bean
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|SingletonFactory
argument_list|(
name|bean
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AbstractJAXWSMethodInvoker
parameter_list|(
name|Factory
name|factory
parameter_list|)
block|{
name|super
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|SOAPFaultException
name|findSoapFaultException
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|instanceof
name|SOAPFaultException
condition|)
block|{
return|return
operator|(
name|SOAPFaultException
operator|)
name|ex
return|;
block|}
if|if
condition|(
name|ex
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|findSoapFaultException
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|Method
name|adjustMethodAndParams
parameter_list|(
name|Method
name|m
parameter_list|,
name|Exchange
name|ex
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|params
parameter_list|)
block|{
name|UseAsyncMethod
name|uam
init|=
name|m
operator|.
name|getAnnotation
argument_list|(
name|UseAsyncMethod
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|uam
operator|!=
literal|null
condition|)
block|{
name|BindingOperationInfo
name|bop
init|=
name|ex
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
name|Method
name|ret
init|=
name|bop
operator|.
name|getProperty
argument_list|(
name|ASYNC_METHOD
argument_list|,
name|Method
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ret
operator|==
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|ptypes
index|[]
init|=
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[
name|m
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|+
literal|1
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|m
operator|.
name|getParameterTypes
argument_list|()
argument_list|,
literal|0
argument_list|,
name|ptypes
argument_list|,
literal|0
argument_list|,
name|m
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|ptypes
index|[
name|m
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
index|]
operator|=
name|AsyncHandler
operator|.
name|class
expr_stmt|;
try|try
block|{
name|ret
operator|=
name|m
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|getMethod
argument_list|(
name|m
operator|.
name|getName
argument_list|()
operator|+
literal|"Async"
argument_list|,
name|ptypes
argument_list|)
expr_stmt|;
name|bop
operator|.
name|setProperty
argument_list|(
name|ASYNC_METHOD
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
block|}
if|if
condition|(
name|ret
operator|!=
literal|null
condition|)
block|{
name|JaxwsServerHandler
name|h
init|=
name|ex
operator|.
name|get
argument_list|(
name|JaxwsServerHandler
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|h
operator|!=
literal|null
condition|)
block|{
return|return
name|ret
return|;
block|}
name|ContinuationProvider
name|cp
init|=
name|ex
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|ContinuationProvider
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|cp
operator|==
literal|null
operator|&&
name|uam
operator|.
name|always
argument_list|()
condition|)
block|{
name|JaxwsServerHandler
name|handler
init|=
operator|new
name|JaxwsServerHandler
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|JaxwsServerHandler
operator|.
name|class
argument_list|,
name|handler
argument_list|)
expr_stmt|;
name|params
operator|.
name|add
argument_list|(
name|handler
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
elseif|else
if|if
condition|(
name|cp
operator|!=
literal|null
operator|&&
name|cp
operator|.
name|getContinuation
argument_list|()
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Continuation
name|c
init|=
name|cp
operator|.
name|getContinuation
argument_list|()
decl_stmt|;
name|c
operator|.
name|suspend
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|JaxwsServerHandler
name|handler
init|=
operator|new
name|JaxwsServerHandler
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|JaxwsServerHandler
operator|.
name|class
argument_list|,
name|handler
argument_list|)
expr_stmt|;
name|params
operator|.
name|add
argument_list|(
name|handler
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
block|}
block|}
return|return
name|m
return|;
block|}
class|class
name|JaxwsServerHandler
implements|implements
name|AsyncHandler
argument_list|<
name|Object
argument_list|>
block|{
name|Response
argument_list|<
name|Object
argument_list|>
name|r
decl_stmt|;
name|Continuation
name|continuation
decl_stmt|;
name|boolean
name|done
decl_stmt|;
specifier|public
name|JaxwsServerHandler
parameter_list|(
name|Continuation
name|c
parameter_list|)
block|{
name|continuation
operator|=
name|c
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|void
name|handleResponse
parameter_list|(
name|Response
argument_list|<
name|Object
argument_list|>
name|res
parameter_list|)
block|{
name|r
operator|=
name|res
expr_stmt|;
name|done
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|continuation
operator|!=
literal|null
condition|)
block|{
name|continuation
operator|.
name|resume
argument_list|()
expr_stmt|;
block|}
name|notifyAll
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|hasContinuation
parameter_list|()
block|{
return|return
name|continuation
operator|!=
literal|null
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|waitForDone
parameter_list|()
block|{
while|while
condition|(
operator|!
name|done
condition|)
block|{
try|try
block|{
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
comment|//ignore
block|}
block|}
block|}
specifier|public
name|boolean
name|isDone
parameter_list|()
block|{
return|return
name|done
return|;
block|}
specifier|public
name|Object
name|getObject
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|r
operator|.
name|get
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|invoke
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Object
name|serviceObject
parameter_list|,
name|Method
name|m
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|params
parameter_list|)
block|{
name|JaxwsServerHandler
name|h
init|=
name|exchange
operator|.
name|get
argument_list|(
name|JaxwsServerHandler
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|h
operator|!=
literal|null
operator|&&
name|h
operator|.
name|isDone
argument_list|()
condition|)
block|{
name|BindingOperationInfo
name|bop
init|=
name|exchange
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|bop
operator|.
name|isUnwrapped
argument_list|()
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
operator|.
name|getWrappedOperation
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
return|return
operator|new
name|MessageContentsList
argument_list|(
name|h
operator|.
name|getObject
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|ex
parameter_list|)
block|{
name|exchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|put
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|,
name|FaultMode
operator|.
name|CHECKED_APPLICATION_FAULT
argument_list|)
expr_stmt|;
throw|throw
name|createFault
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
argument_list|,
name|m
argument_list|,
name|params
argument_list|,
literal|true
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
name|createFault
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
argument_list|,
name|m
argument_list|,
name|params
argument_list|,
literal|false
argument_list|)
throw|;
block|}
block|}
name|Object
name|o
init|=
name|super
operator|.
name|invoke
argument_list|(
name|exchange
argument_list|,
name|serviceObject
argument_list|,
name|m
argument_list|,
name|params
argument_list|)
decl_stmt|;
if|if
condition|(
name|h
operator|!=
literal|null
operator|&&
operator|!
name|h
operator|.
name|hasContinuation
argument_list|()
condition|)
block|{
name|h
operator|.
name|waitForDone
argument_list|()
expr_stmt|;
name|BindingOperationInfo
name|bop
init|=
name|exchange
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|bop
operator|.
name|isUnwrapped
argument_list|()
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
operator|.
name|getWrappedOperation
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
return|return
operator|new
name|MessageContentsList
argument_list|(
name|h
operator|.
name|getObject
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|ex
parameter_list|)
block|{
name|exchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|put
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|,
name|FaultMode
operator|.
name|CHECKED_APPLICATION_FAULT
argument_list|)
expr_stmt|;
throw|throw
name|createFault
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
argument_list|,
name|m
argument_list|,
name|params
argument_list|,
literal|true
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
name|createFault
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
argument_list|,
name|m
argument_list|,
name|params
argument_list|,
literal|false
argument_list|)
throw|;
block|}
block|}
return|return
name|o
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Fault
name|createFault
parameter_list|(
name|Throwable
name|ex
parameter_list|,
name|Method
name|m
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|params
parameter_list|,
name|boolean
name|checked
parameter_list|)
block|{
comment|//map the JAX-WS faults
name|SOAPFaultException
name|sfe
init|=
name|findSoapFaultException
argument_list|(
name|ex
argument_list|)
decl_stmt|;
if|if
condition|(
name|sfe
operator|!=
literal|null
condition|)
block|{
name|SoapFault
name|fault
init|=
operator|new
name|SoapFault
argument_list|(
name|sfe
operator|.
name|getFault
argument_list|()
operator|.
name|getFaultString
argument_list|()
argument_list|,
name|ex
argument_list|,
name|sfe
operator|.
name|getFault
argument_list|()
operator|.
name|getFaultCodeAsQName
argument_list|()
argument_list|)
decl_stmt|;
name|fault
operator|.
name|setRole
argument_list|(
name|sfe
operator|.
name|getFault
argument_list|()
operator|.
name|getFaultActor
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|sfe
operator|.
name|getFault
argument_list|()
operator|.
name|hasDetail
argument_list|()
condition|)
block|{
name|fault
operator|.
name|setDetail
argument_list|(
name|sfe
operator|.
name|getFault
argument_list|()
operator|.
name|getDetail
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|fault
return|;
block|}
return|return
name|super
operator|.
name|createFault
argument_list|(
name|ex
argument_list|,
name|m
argument_list|,
name|params
argument_list|,
name|checked
argument_list|)
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|removeHandlerProperties
parameter_list|(
name|WrappedMessageContext
name|ctx
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Scope
argument_list|>
name|scopes
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|ctx
operator|.
name|get
argument_list|(
name|WrappedMessageContext
operator|.
name|SCOPES
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|handlerScopedStuff
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
if|if
condition|(
name|scopes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Scope
argument_list|>
name|scope
range|:
name|scopes
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|scope
operator|.
name|getValue
argument_list|()
operator|==
name|Scope
operator|.
name|HANDLER
condition|)
block|{
name|handlerScopedStuff
operator|.
name|put
argument_list|(
name|scope
operator|.
name|getKey
argument_list|()
argument_list|,
name|ctx
operator|.
name|get
argument_list|(
name|scope
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|String
name|key
range|:
name|handlerScopedStuff
operator|.
name|keySet
argument_list|()
control|)
block|{
name|ctx
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|handlerScopedStuff
return|;
block|}
specifier|protected
name|void
name|addHandlerProperties
parameter_list|(
name|WrappedMessageContext
name|ctx
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|handlerScopedStuff
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|key
range|:
name|handlerScopedStuff
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|ctx
operator|.
name|put
argument_list|(
name|key
operator|.
name|getKey
argument_list|()
argument_list|,
name|key
operator|.
name|getValue
argument_list|()
argument_list|,
name|Scope
operator|.
name|HANDLER
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Message
name|createResponseMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Message
name|m
init|=
name|exchange
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|m
operator|==
literal|null
operator|&&
operator|!
name|exchange
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
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
name|m
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|m
operator|=
name|ep
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOutMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
return|return
name|m
return|;
block|}
specifier|protected
name|void
name|updateWebServiceContext
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|MessageContext
name|ctx
parameter_list|)
block|{
comment|// Guard against wrong type associated with header list.
comment|// Need to copy header only if the message is going out.
if|if
condition|(
name|ctx
operator|.
name|containsKey
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|)
operator|&&
name|ctx
operator|.
name|get
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|)
operator|instanceof
name|List
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|ctx
operator|.
name|get
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
operator|&&
operator|!
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|SoapMessage
name|sm
init|=
operator|(
name|SoapMessage
operator|)
name|createResponseMessage
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|sm
operator|!=
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|?
argument_list|>
name|iter
init|=
name|list
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|sm
operator|.
name|getHeaders
argument_list|()
operator|.
name|add
argument_list|(
operator|(
name|Header
operator|)
name|iter
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
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
name|Message
name|out
init|=
name|exchange
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|out
operator|.
name|containsKey
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|heads
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|exchange
operator|.
name|getOutMessage
argument_list|()
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|heads
operator|.
name|containsKey
argument_list|(
literal|"Content-Type"
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ct
init|=
name|heads
operator|.
name|get
argument_list|(
literal|"Content-Type"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOutMessage
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|ct
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|heads
operator|.
name|remove
argument_list|(
literal|"Content-Type"
argument_list|)
expr_stmt|;
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|dataHandlers
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|out
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|OUTBOUND_MESSAGE_ATTACHMENTS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|dataHandlers
operator|!=
literal|null
operator|&&
operator|!
name|dataHandlers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
init|=
name|out
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
if|if
condition|(
name|attachments
operator|==
literal|null
condition|)
block|{
name|attachments
operator|=
operator|new
name|ArrayList
argument_list|<
name|Attachment
argument_list|>
argument_list|()
expr_stmt|;
name|out
operator|.
name|setAttachments
argument_list|(
name|attachments
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|entry
range|:
name|dataHandlers
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Attachment
name|att
init|=
operator|new
name|AttachmentImpl
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|attachments
operator|.
name|add
argument_list|(
name|att
argument_list|)
expr_stmt|;
block|}
block|}
name|out
operator|.
name|remove
argument_list|(
name|MessageContext
operator|.
name|OUTBOUND_MESSAGE_ATTACHMENTS
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_function
specifier|protected
name|void
name|updateHeader
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|MessageContext
name|ctx
parameter_list|)
block|{
if|if
condition|(
name|ctx
operator|.
name|containsKey
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|)
operator|&&
name|ctx
operator|.
name|get
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|)
operator|instanceof
name|List
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|ctx
operator|.
name|get
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
operator|&&
operator|!
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|SoapMessage
name|sm
init|=
operator|(
name|SoapMessage
operator|)
name|createResponseMessage
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|sm
operator|!=
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|?
argument_list|>
name|iter
init|=
name|list
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Header
name|header
init|=
operator|(
name|Header
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|header
operator|.
name|getDirection
argument_list|()
operator|!=
name|Header
operator|.
name|Direction
operator|.
name|DIRECTION_IN
operator|&&
operator|!
name|header
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
literal|"http://docs.oasis-open.org/wss/2004/01/"
operator|+
literal|"oasis-200401-wss-wssecurity-secext-1.0.xsd"
argument_list|)
operator|&&
operator|!
name|header
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
literal|"http://docs.oasis-open.org/"
operator|+
literal|"wss/oasis-wss-wssecurity-secext-1.1.xsd"
argument_list|)
condition|)
block|{
comment|//don't copy over security header, out interceptor chain will take care of it.
name|sm
operator|.
name|getHeaders
argument_list|()
operator|.
name|add
argument_list|(
name|header
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_function

unit|}  }
end_unit

