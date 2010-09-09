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
return|return
operator|new
name|Servlet3Continuation
argument_list|()
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
name|boolean
name|isNew
decl_stmt|;
name|boolean
name|isResumed
decl_stmt|;
name|boolean
name|isPending
decl_stmt|;
name|Object
name|obj
decl_stmt|;
specifier|public
name|Servlet3Continuation
parameter_list|()
block|{
name|isNew
operator|=
operator|!
name|req
operator|.
name|isAsyncStarted
argument_list|()
expr_stmt|;
if|if
condition|(
name|isNew
condition|)
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
else|else
block|{
name|context
operator|=
name|req
operator|.
name|getAsyncContext
argument_list|()
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
condition|)
block|{
return|return
literal|false
return|;
block|}
name|context
operator|.
name|setTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
name|isNew
operator|=
literal|false
expr_stmt|;
comment|// Need to get the right message which is handled in the interceptor chain
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
name|isPending
operator|=
literal|true
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
name|context
operator|.
name|dispatch
argument_list|()
expr_stmt|;
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
name|redispatch
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|context
operator|.
name|complete
argument_list|()
expr_stmt|;
name|obj
operator|=
literal|null
expr_stmt|;
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
block|{         }
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
name|isPending
operator|=
literal|false
expr_stmt|;
name|redispatch
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

