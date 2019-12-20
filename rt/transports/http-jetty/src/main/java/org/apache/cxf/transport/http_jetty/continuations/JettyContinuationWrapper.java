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
name|http_jetty
operator|.
name|continuations
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicLong
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
name|transport
operator|.
name|http
operator|.
name|AbstractHTTPDestination
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|continuation
operator|.
name|ContinuationListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|continuation
operator|.
name|ContinuationSupport
import|;
end_import

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|public
class|class
name|JettyContinuationWrapper
implements|implements
name|Continuation
implements|,
name|ContinuationListener
block|{
specifier|volatile
name|boolean
name|isNew
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
name|isTimeout
decl_stmt|;
name|AtomicLong
name|pendingTimeout
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
specifier|volatile
name|Object
name|obj
decl_stmt|;
specifier|private
name|Message
name|message
decl_stmt|;
specifier|private
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|continuation
operator|.
name|Continuation
name|continuation
decl_stmt|;
specifier|private
name|ContinuationCallback
name|callback
decl_stmt|;
specifier|public
name|JettyContinuationWrapper
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|resp
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|continuation
operator|=
name|ContinuationSupport
operator|.
name|getContinuation
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|message
operator|=
name|m
expr_stmt|;
name|isNew
operator|=
name|request
operator|.
name|getAttribute
argument_list|(
name|AbstractHTTPDestination
operator|.
name|CXF_CONTINUATION_MESSAGE
argument_list|)
operator|==
literal|null
expr_stmt|;
if|if
condition|(
name|isNew
condition|)
block|{
name|request
operator|.
name|setAttribute
argument_list|(
name|AbstractHTTPDestination
operator|.
name|CXF_CONTINUATION_MESSAGE
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
argument_list|)
expr_stmt|;
name|continuation
operator|.
name|addContinuationListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|callback
operator|=
name|message
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
block|}
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
name|userObject
parameter_list|)
block|{
name|obj
operator|=
name|userObject
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
name|isPending
operator|=
literal|false
expr_stmt|;
name|continuation
operator|.
name|resume
argument_list|()
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
name|boolean
name|isExpired
parameter_list|()
block|{
return|return
name|continuation
operator|.
name|isExpired
argument_list|()
return|;
block|}
specifier|public
name|void
name|reset
parameter_list|()
block|{
try|try
block|{
name|continuation
operator|.
name|complete
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
comment|// explicit complete call does not seem to work
comment|// with the non-Servlet3 Jetty Continuation
block|}
name|obj
operator|=
literal|null
expr_stmt|;
name|pendingTimeout
operator|.
name|set
argument_list|(
literal|0L
argument_list|)
expr_stmt|;
name|isTimeout
operator|=
literal|false
expr_stmt|;
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
name|pendingTimeout
operator|.
name|addAndGet
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|pendingTimeout
operator|.
name|set
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
name|isNew
operator|=
literal|false
expr_stmt|;
name|message
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
name|continuation
operator|.
name|setTimeout
argument_list|(
name|pendingTimeout
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isPending
condition|)
block|{
name|continuation
operator|.
name|suspend
argument_list|()
expr_stmt|;
name|isPending
operator|=
literal|true
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|protected
name|Message
name|getMessage
parameter_list|()
block|{
name|Message
name|m
init|=
name|message
decl_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
operator|&&
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|m
operator|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
expr_stmt|;
block|}
return|return
name|m
return|;
block|}
specifier|public
name|void
name|onComplete
parameter_list|(
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|continuation
operator|.
name|Continuation
name|cont
parameter_list|)
block|{
name|getMessage
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
name|pendingTimeout
operator|.
name|set
argument_list|(
literal|0L
argument_list|)
expr_stmt|;
name|isResumed
operator|=
literal|false
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
name|onTimeout
parameter_list|(
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|continuation
operator|.
name|Continuation
name|cont
parameter_list|)
block|{
name|isPending
operator|=
literal|false
expr_stmt|;
name|pendingTimeout
operator|.
name|set
argument_list|(
literal|0L
argument_list|)
expr_stmt|;
name|isResumed
operator|=
literal|true
expr_stmt|;
name|isTimeout
operator|=
literal|true
expr_stmt|;
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
end_class

end_unit

