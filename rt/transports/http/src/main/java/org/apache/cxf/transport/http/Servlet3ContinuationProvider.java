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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|Servlet3ContinuationProvider
implements|implements
name|ContinuationProvider
block|{
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
name|Object
name|obj
decl_stmt|;
specifier|private
name|ContinuationCallback
name|callback
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
name|context
operator|=
name|req
operator|.
name|startAsync
argument_list|(
name|req
argument_list|,
name|resp
argument_list|)
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
return|return
literal|true
return|;
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
name|callback
operator|.
name|onComplete
argument_list|()
expr_stmt|;
block|}
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
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

