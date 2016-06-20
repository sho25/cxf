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
name|management
operator|.
name|interceptor
package|;
end_package

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
name|management
operator|.
name|MalformedObjectNameException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|management
operator|.
name|ManagementConstants
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
name|management
operator|.
name|counters
operator|.
name|Counter
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
name|management
operator|.
name|counters
operator|.
name|CounterRepository
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
name|management
operator|.
name|counters
operator|.
name|MessageHandlingTimeRecorder
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
name|service
operator|.
name|model
operator|.
name|OperationInfo
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractMessageResponseTimeInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
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
name|AbstractMessageResponseTimeInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|QUESTION_MARK
init|=
literal|"?"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ESCAPED_QUESTION_MARK
init|=
literal|"\\?"
decl_stmt|;
name|AbstractMessageResponseTimeInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|boolean
name|isClient
parameter_list|(
name|Message
name|msg
parameter_list|)
block|{
return|return
name|msg
operator|==
literal|null
condition|?
literal|false
else|:
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|msg
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|void
name|beginHandlingMessage
parameter_list|(
name|Exchange
name|ex
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|ex
condition|)
block|{
return|return;
block|}
name|MessageHandlingTimeRecorder
name|mhtr
init|=
name|ex
operator|.
name|get
argument_list|(
name|MessageHandlingTimeRecorder
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|mhtr
condition|)
block|{
name|mhtr
operator|.
name|beginHandling
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|mhtr
operator|=
operator|new
name|MessageHandlingTimeRecorder
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|mhtr
operator|.
name|beginHandling
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|endHandlingMessage
parameter_list|(
name|Exchange
name|ex
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|ex
condition|)
block|{
return|return;
block|}
name|MessageHandlingTimeRecorder
name|mhtr
init|=
name|ex
operator|.
name|get
argument_list|(
name|MessageHandlingTimeRecorder
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|mhtr
condition|)
block|{
name|mhtr
operator|.
name|endHandling
argument_list|()
expr_stmt|;
name|mhtr
operator|.
name|setFaultMode
argument_list|(
name|ex
operator|.
name|get
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|increaseCounter
argument_list|(
name|ex
argument_list|,
name|mhtr
argument_list|)
expr_stmt|;
block|}
comment|// else can't get the MessageHandling Infor
block|}
specifier|protected
name|void
name|setOneWayMessage
parameter_list|(
name|Exchange
name|ex
parameter_list|)
block|{
name|MessageHandlingTimeRecorder
name|mhtr
init|=
name|ex
operator|.
name|get
argument_list|(
name|MessageHandlingTimeRecorder
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|mhtr
condition|)
block|{
name|mhtr
operator|=
operator|new
name|MessageHandlingTimeRecorder
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mhtr
operator|.
name|endHandling
argument_list|()
expr_stmt|;
block|}
name|mhtr
operator|.
name|setOneWay
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|increaseCounter
argument_list|(
name|ex
argument_list|,
name|mhtr
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|increaseCounter
parameter_list|(
name|Exchange
name|ex
parameter_list|,
name|MessageHandlingTimeRecorder
name|mhtr
parameter_list|)
block|{
name|Bus
name|bus
init|=
name|ex
operator|.
name|getBus
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|bus
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"CAN_NOT_GET_BUS_FROM_EXCHANGE"
argument_list|)
expr_stmt|;
name|bus
operator|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
expr_stmt|;
block|}
name|CounterRepository
name|cr
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|CounterRepository
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|cr
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"NO_COUNTER_REPOSITORY"
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
name|ObjectName
name|serviceCountername
init|=
name|this
operator|.
name|getServiceCounterName
argument_list|(
name|ex
argument_list|)
decl_stmt|;
name|cr
operator|.
name|increaseCounter
argument_list|(
name|serviceCountername
argument_list|,
name|mhtr
argument_list|)
expr_stmt|;
name|ObjectName
name|operationCounter
init|=
name|this
operator|.
name|getOperationCounterName
argument_list|(
name|ex
argument_list|,
name|serviceCountername
argument_list|)
decl_stmt|;
name|cr
operator|.
name|increaseCounter
argument_list|(
name|operationCounter
argument_list|,
name|mhtr
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|ObjectName
name|getServiceCounterName
parameter_list|(
name|Exchange
name|ex
parameter_list|)
block|{
name|Bus
name|bus
init|=
name|ex
operator|.
name|getBus
argument_list|()
decl_stmt|;
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|ObjectName
name|serviceCounterName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ex
operator|.
name|get
argument_list|(
literal|"org.apache.cxf.management.service.counter.name"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
operator|(
name|String
operator|)
name|ex
operator|.
name|get
argument_list|(
literal|"org.apache.cxf.management.service.counter.name"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|serviceCounterName
operator|=
operator|new
name|ObjectName
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedObjectNameException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"CREATE_COUNTER_OBJECTNAME_FAILED"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Service
name|service
init|=
name|ex
operator|.
name|getService
argument_list|()
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|ex
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|serviceCounterName
operator|=
operator|(
name|ObjectName
operator|)
name|endpoint
operator|.
name|get
argument_list|(
literal|"javax.management.ObjectName"
argument_list|)
expr_stmt|;
if|if
condition|(
name|serviceCounterName
operator|==
literal|null
condition|)
block|{
name|String
name|serviceName
init|=
literal|"\""
operator|+
name|escapePatternChars
argument_list|(
name|service
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|+
literal|"\""
decl_stmt|;
name|String
name|portName
init|=
literal|"\""
operator|+
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"\""
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|DEFAULT_DOMAIN_NAME
operator|+
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|BUS_ID_PROP
operator|+
literal|"="
operator|+
name|bus
operator|.
name|getId
argument_list|()
operator|+
literal|","
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|ex
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|isClient
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|TYPE_PROP
operator|+
literal|"="
operator|+
name|Counter
operator|.
name|PERFORMANCE_COUNTER
operator|+
literal|".Client,"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|TYPE_PROP
operator|+
literal|"="
operator|+
name|Counter
operator|.
name|PERFORMANCE_COUNTER
operator|+
literal|".Server,"
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|SERVICE_NAME_PROP
operator|+
literal|"="
operator|+
name|serviceName
operator|+
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|PORT_NAME_PROP
operator|+
literal|"="
operator|+
name|portName
argument_list|)
expr_stmt|;
try|try
block|{
name|serviceCounterName
operator|=
operator|new
name|ObjectName
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|put
argument_list|(
literal|"javax.management.ObjectName"
argument_list|,
name|serviceCounterName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedObjectNameException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"CREATE_COUNTER_OBJECTNAME_FAILED"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|serviceCounterName
return|;
block|}
specifier|protected
name|boolean
name|isServiceCounterEnabled
parameter_list|(
name|Exchange
name|ex
parameter_list|)
block|{
name|Bus
name|bus
init|=
name|ex
operator|.
name|getBus
argument_list|()
decl_stmt|;
name|CounterRepository
name|counterRepo
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|CounterRepository
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|counterRepo
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ObjectName
name|serviceCounterName
init|=
name|getServiceCounterName
argument_list|(
name|ex
argument_list|)
decl_stmt|;
name|Counter
name|serviceCounter
init|=
name|counterRepo
operator|.
name|getCounter
argument_list|(
name|serviceCounterName
argument_list|)
decl_stmt|;
comment|//If serviceCounter is null, we need to wait ResponseTimeOutInterceptor to create it , hence set to true
return|return
name|serviceCounter
operator|==
literal|null
operator|||
name|serviceCounter
operator|.
name|isEnabled
argument_list|()
return|;
block|}
specifier|protected
name|ObjectName
name|getOperationCounterName
parameter_list|(
name|Exchange
name|ex
parameter_list|,
name|ObjectName
name|sericeCounterName
parameter_list|)
block|{
name|BindingOperationInfo
name|bop
init|=
name|ex
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
name|OperationInfo
name|opInfo
init|=
name|bop
operator|==
literal|null
condition|?
literal|null
else|:
name|bop
operator|.
name|getOperationInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|opInfo
operator|!=
literal|null
condition|)
block|{
name|ObjectName
name|o
init|=
name|opInfo
operator|.
name|getProperty
argument_list|(
literal|"javax.management.ObjectName"
argument_list|,
name|ObjectName
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
return|return
name|o
return|;
block|}
block|}
name|String
name|operationName
init|=
name|opInfo
operator|==
literal|null
condition|?
literal|null
else|:
literal|"\""
operator|+
name|opInfo
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"\""
decl_stmt|;
if|if
condition|(
name|operationName
operator|==
literal|null
condition|)
block|{
name|Object
name|nameProperty
init|=
name|ex
operator|.
name|get
argument_list|(
literal|"org.apache.cxf.resource.operation.name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|nameProperty
operator|!=
literal|null
condition|)
block|{
name|operationName
operator|=
literal|"\""
operator|+
name|escapePatternChars
argument_list|(
name|nameProperty
operator|.
name|toString
argument_list|()
argument_list|)
operator|+
literal|"\""
expr_stmt|;
block|}
block|}
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
name|sericeCounterName
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|operationName
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|","
operator|+
name|ManagementConstants
operator|.
name|OPERATION_NAME_PROP
operator|+
literal|"="
operator|+
name|operationName
argument_list|)
expr_stmt|;
block|}
name|String
name|operationCounterName
init|=
name|buffer
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ObjectName
name|operationCounter
init|=
literal|null
decl_stmt|;
try|try
block|{
name|operationCounter
operator|=
operator|new
name|ObjectName
argument_list|(
name|operationCounterName
argument_list|)
expr_stmt|;
if|if
condition|(
name|opInfo
operator|!=
literal|null
condition|)
block|{
name|opInfo
operator|.
name|setProperty
argument_list|(
literal|"javax.management.ObjectName"
argument_list|,
name|operationCounter
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MalformedObjectNameException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"CREATE_COUNTER_OBJECTNAME_FAILED"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|operationCounter
return|;
block|}
specifier|protected
name|String
name|escapePatternChars
parameter_list|(
name|String
name|value
parameter_list|)
block|{
comment|// This can be replaced if really needed with pattern-based matching
if|if
condition|(
name|value
operator|.
name|lastIndexOf
argument_list|(
name|QUESTION_MARK
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|value
operator|=
name|value
operator|.
name|replace
argument_list|(
name|QUESTION_MARK
argument_list|,
name|ESCAPED_QUESTION_MARK
argument_list|)
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
block|}
end_class

end_unit

