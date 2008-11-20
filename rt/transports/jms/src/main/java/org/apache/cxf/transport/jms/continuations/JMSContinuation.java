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
name|jms
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Timer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimerTask
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
name|SuspendedInvocationException
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
name|MessageObserver
import|;
end_import

begin_class
specifier|public
class|class
name|JMSContinuation
implements|implements
name|Continuation
block|{
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|Message
name|inMessage
decl_stmt|;
specifier|private
name|MessageObserver
name|incomingObserver
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|JMSContinuation
argument_list|>
name|continuations
decl_stmt|;
specifier|private
name|Object
name|userObject
decl_stmt|;
specifier|private
name|boolean
name|isNew
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|isPending
decl_stmt|;
specifier|private
name|boolean
name|isResumed
decl_stmt|;
specifier|private
name|Timer
name|timer
init|=
operator|new
name|Timer
argument_list|()
decl_stmt|;
specifier|public
name|JMSContinuation
parameter_list|(
name|Bus
name|b
parameter_list|,
name|Message
name|m
parameter_list|,
name|MessageObserver
name|observer
parameter_list|,
name|Collection
argument_list|<
name|JMSContinuation
argument_list|>
name|cList
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
name|inMessage
operator|=
name|m
expr_stmt|;
name|incomingObserver
operator|=
name|observer
expr_stmt|;
name|continuations
operator|=
name|cList
expr_stmt|;
block|}
specifier|public
name|Object
name|getObject
parameter_list|()
block|{
return|return
name|userObject
return|;
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
name|void
name|reset
parameter_list|()
block|{
name|cancelTimerTask
argument_list|()
expr_stmt|;
name|isNew
operator|=
literal|true
expr_stmt|;
name|isPending
operator|=
literal|false
expr_stmt|;
name|isResumed
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|void
name|resume
parameter_list|()
block|{
name|cancelTimerTask
argument_list|()
expr_stmt|;
name|doResume
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|doResume
parameter_list|()
block|{
if|if
condition|(
name|isResumed
operator|||
operator|!
name|isPending
condition|)
block|{
return|return;
block|}
name|continuations
operator|.
name|remove
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
try|try
block|{
name|incomingObserver
operator|.
name|onMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|isResumed
operator|=
literal|true
expr_stmt|;
name|isPending
operator|=
literal|false
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setObject
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|userObject
operator|=
name|o
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
condition|)
block|{
return|return
literal|false
return|;
block|}
name|continuations
operator|.
name|add
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|isNew
operator|=
literal|false
expr_stmt|;
name|isResumed
operator|=
literal|false
expr_stmt|;
name|isPending
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|timeout
operator|>
literal|0
condition|)
block|{
name|createTimerTask
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|SuspendedInvocationException
argument_list|()
throw|;
block|}
specifier|protected
name|void
name|createTimerTask
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|timer
operator|.
name|schedule
argument_list|(
operator|new
name|TimerTask
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
synchronized|synchronized
init|(
name|JMSContinuation
operator|.
name|this
init|)
block|{
name|doResume
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|cancelTimerTask
parameter_list|()
block|{
name|timer
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

