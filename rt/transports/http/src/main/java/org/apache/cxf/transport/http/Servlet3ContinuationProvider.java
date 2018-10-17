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
name|transport
operator|.
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|AsyncContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|AsyncEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|AsyncListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletOutputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|WriteListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|PropertyUtils
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
name|ContinuationCallback
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
name|PhaseInterceptorChain
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|Servlet3ContinuationProvider
implements|implements
name|ContinuationProvider
block|{
specifier|static
specifier|final
name|boolean
name|IS_31
decl_stmt|;
static|static
block|{
name|boolean
name|is31
init|=
literal|false
decl_stmt|;
try|try
block|{
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
literal|"javax.servlet.WriteListener"
argument_list|,
name|HttpServletRequest
operator|.
name|class
argument_list|)
expr_stmt|;
name|is31
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|is31
operator|=
literal|false
expr_stmt|;
block|}
name|IS_31
operator|=
name|is31
expr_stmt|;
block|}
name|HttpServletRequest
name|req
decl_stmt|;
name|HttpServletResponse
name|resp
decl_stmt|;
name|Message
name|inMessage
decl_stmt|;
name|Servlet3Continuation
name|continuation
decl_stmt|;
specifier|public
name|Servlet3ContinuationProvider
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|resp
parameter_list|,
name|Message
name|inMessage
parameter_list|)
block|{
name|this
operator|.
name|inMessage
operator|=
name|inMessage
expr_stmt|;
name|this
operator|.
name|req
operator|=
name|req
expr_stmt|;
name|this
operator|.
name|resp
operator|=
name|resp
expr_stmt|;
block|}
specifier|public
name|void
name|complete
parameter_list|()
block|{
if|if
condition|(
name|continuation
operator|!=
literal|null
condition|)
block|{
name|continuation
operator|.
name|reset
argument_list|()
expr_stmt|;
name|continuation
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|Continuation
name|getContinuation
parameter_list|()
block|{
if|if
condition|(
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|continuation
operator|==
literal|null
condition|)
block|{
name|continuation
operator|=
name|IS_31
condition|?
operator|new
name|Servlet31Continuation
argument_list|()
else|:
operator|new
name|Servlet3Continuation
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|continuation
operator|.
name|startAsyncAgain
argument_list|()
expr_stmt|;
block|}
return|return
name|continuation
return|;
block|}
specifier|public
class|class
name|Servlet3Continuation
implements|implements
name|Continuation
implements|,
name|AsyncListener
block|{
specifier|private
specifier|static
specifier|final
name|String
name|BLOCK_RESTART
init|=
literal|"org.apache.cxf.continuation.block.restart"
decl_stmt|;
name|AsyncContext
name|context
decl_stmt|;
specifier|volatile
name|boolean
name|isNew
init|=
literal|true
decl_stmt|;
specifier|volatile
name|boolean
name|isResumed
decl_stmt|;
specifier|volatile
name|boolean
name|isPending
decl_stmt|;
specifier|volatile
name|boolean
name|isComplete
decl_stmt|;
specifier|volatile
name|boolean
name|isTimeout
decl_stmt|;
specifier|volatile
name|Object
name|obj
decl_stmt|;
specifier|private
name|ContinuationCallback
name|callback
decl_stmt|;
specifier|private
name|boolean
name|blockRestart
decl_stmt|;
specifier|public
name|Servlet3Continuation
parameter_list|()
block|{
name|req
operator|.
name|setAttribute
argument_list|(
name|AbstractHTTPDestination
operator|.
name|CXF_CONTINUATION_MESSAGE
argument_list|,
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
argument_list|)
expr_stmt|;
name|callback
operator|=
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|ContinuationCallback
operator|.
name|class
argument_list|)
expr_stmt|;
name|blockRestart
operator|=
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|inMessage
operator|.
name|getContextualProperty
argument_list|(
name|BLOCK_RESTART
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|=
name|req
operator|.
name|startAsync
argument_list|()
expr_stmt|;
name|context
operator|.
name|addListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|void
name|startAsyncAgain
parameter_list|()
block|{
if|if
condition|(
name|blockRestart
condition|)
block|{
return|return;
block|}
name|AsyncContext
name|old
init|=
name|context
decl_stmt|;
try|try
block|{
name|context
operator|=
name|req
operator|.
name|startAsync
argument_list|()
expr_stmt|;
name|context
operator|.
name|addListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|isComplete
operator|=
literal|false
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|ex
parameter_list|)
block|{
name|context
operator|=
name|old
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|suspend
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
if|if
condition|(
name|isPending
operator|&&
name|timeout
operator|!=
literal|0
condition|)
block|{
name|long
name|currentTimeout
init|=
name|context
operator|.
name|getTimeout
argument_list|()
decl_stmt|;
name|timeout
operator|=
name|currentTimeout
operator|+
name|timeout
expr_stmt|;
block|}
else|else
block|{
name|isPending
operator|=
literal|true
expr_stmt|;
block|}
name|isNew
operator|=
literal|false
expr_stmt|;
name|isResumed
operator|=
literal|false
expr_stmt|;
name|context
operator|.
name|setTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
name|updateMessageForSuspend
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|protected
name|void
name|updateMessageForSuspend
parameter_list|()
block|{
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|suspend
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|redispatch
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isComplete
condition|)
block|{
name|context
operator|.
name|dispatch
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|resume
parameter_list|()
block|{
name|isResumed
operator|=
literal|true
expr_stmt|;
name|isPending
operator|=
literal|false
expr_stmt|;
name|redispatch
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|isComplete
operator|=
literal|true
expr_stmt|;
try|try
block|{
name|context
operator|.
name|complete
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
name|isPending
operator|=
literal|false
expr_stmt|;
name|isResumed
operator|=
literal|false
expr_stmt|;
name|isNew
operator|=
literal|false
expr_stmt|;
name|isTimeout
operator|=
literal|false
expr_stmt|;
name|obj
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|callback
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Exception
name|ex
init|=
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
name|Throwable
name|cause
init|=
name|isCausedByIO
argument_list|(
name|ex
argument_list|)
decl_stmt|;
if|if
condition|(
name|cause
operator|!=
literal|null
operator|&&
name|isClientDisconnected
argument_list|(
name|cause
argument_list|)
condition|)
block|{
name|callback
operator|.
name|onDisconnect
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|boolean
name|isNew
parameter_list|()
block|{
return|return
name|isNew
return|;
block|}
specifier|public
name|boolean
name|isPending
parameter_list|()
block|{
return|return
name|isPending
return|;
block|}
specifier|public
name|boolean
name|isResumed
parameter_list|()
block|{
return|return
name|isResumed
return|;
block|}
specifier|public
name|Object
name|getObject
parameter_list|()
block|{
return|return
name|obj
return|;
block|}
specifier|public
name|void
name|setObject
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|obj
operator|=
name|o
expr_stmt|;
block|}
specifier|public
name|void
name|onComplete
parameter_list|(
name|AsyncEvent
name|event
parameter_list|)
throws|throws
name|IOException
block|{
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|.
name|remove
argument_list|(
name|AbstractHTTPDestination
operator|.
name|CXF_CONTINUATION_MESSAGE
argument_list|)
expr_stmt|;
if|if
condition|(
name|callback
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Exception
name|ex
init|=
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ex
operator|==
literal|null
condition|)
block|{
name|callback
operator|.
name|onComplete
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|callback
operator|.
name|onError
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
name|isResumed
operator|=
literal|false
expr_stmt|;
name|isPending
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|void
name|onError
parameter_list|(
name|AsyncEvent
name|event
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|callback
operator|!=
literal|null
condition|)
block|{
name|callback
operator|.
name|onError
argument_list|(
name|event
operator|.
name|getThrowable
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|onStartAsync
parameter_list|(
name|AsyncEvent
name|event
parameter_list|)
throws|throws
name|IOException
block|{         }
specifier|public
name|void
name|onTimeout
parameter_list|(
name|AsyncEvent
name|event
parameter_list|)
throws|throws
name|IOException
block|{
name|resume
argument_list|()
expr_stmt|;
name|isTimeout
operator|=
literal|true
expr_stmt|;
block|}
specifier|private
name|Throwable
name|isCausedByIO
parameter_list|(
specifier|final
name|Exception
name|ex
parameter_list|)
block|{
name|Throwable
name|cause
init|=
name|ex
decl_stmt|;
while|while
condition|(
name|cause
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|cause
operator|instanceof
name|IOException
operator|)
condition|)
block|{
name|cause
operator|=
name|cause
operator|.
name|getCause
argument_list|()
expr_stmt|;
block|}
return|return
name|cause
return|;
block|}
specifier|private
name|boolean
name|isClientDisconnected
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|String
name|exName
init|=
operator|(
name|String
operator|)
name|inMessage
operator|.
name|getContextualProperty
argument_list|(
literal|"disconnected.client.exception.class"
argument_list|)
decl_stmt|;
if|if
condition|(
name|exName
operator|!=
literal|null
condition|)
block|{
return|return
name|exName
operator|.
name|equals
argument_list|(
name|IOException
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|exName
operator|.
name|equals
argument_list|(
name|ex
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isReadyForWrite
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|protected
name|ServletOutputStream
name|getOutputStream
parameter_list|()
block|{
try|try
block|{
return|return
name|resp
operator|.
name|getOutputStream
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
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
annotation|@
name|Override
specifier|public
name|boolean
name|isTimeout
parameter_list|()
block|{
return|return
name|isTimeout
return|;
block|}
block|}
specifier|public
class|class
name|Servlet31Continuation
extends|extends
name|Servlet3Continuation
block|{
specifier|public
name|Servlet31Continuation
parameter_list|()
block|{         }
annotation|@
name|Override
specifier|protected
name|void
name|updateMessageForSuspend
parameter_list|()
block|{
name|Message
name|currentMessage
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentMessage
operator|.
name|get
argument_list|(
name|WriteListener
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
comment|// CXF Continuation WriteListener will likely need to be introduced
comment|// for NIO supported with non-Servlet specific mechanisms
name|getOutputStream
argument_list|()
operator|.
name|setWriteListener
argument_list|(
name|currentMessage
operator|.
name|get
argument_list|(
name|WriteListener
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|currentMessage
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|suspend
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|suspend
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isReadyForWrite
parameter_list|()
block|{
return|return
name|getOutputStream
argument_list|()
operator|.
name|isReady
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

