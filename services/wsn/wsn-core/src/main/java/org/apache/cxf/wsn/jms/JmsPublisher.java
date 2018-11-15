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
name|wsn
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|jms
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Destination
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageProducer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Topic
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
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|advisory
operator|.
name|ConsumerEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|advisory
operator|.
name|ConsumerEventSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|advisory
operator|.
name|ConsumerListener
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
name|wsn
operator|.
name|AbstractPublisher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|b_2
operator|.
name|InvalidTopicExpressionFaultType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|b_2
operator|.
name|NotificationMessageHolderType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|b_2
operator|.
name|Notify
import|;
end_import

begin_import
import|import
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|b_2
operator|.
name|TopicExpressionType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|br_2
operator|.
name|PublisherRegistrationFailedFaultType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|br_2
operator|.
name|RegisterPublisher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|br_2
operator|.
name|ResourceNotDestroyedFaultType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|brw_2
operator|.
name|PublisherRegistrationFailedFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|brw_2
operator|.
name|PublisherRegistrationRejectedFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|brw_2
operator|.
name|ResourceNotDestroyedFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|bw_2
operator|.
name|InvalidTopicExpressionFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|bw_2
operator|.
name|TopicNotSupportedFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsrf
operator|.
name|rw_2
operator|.
name|ResourceUnknownFault
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|JmsPublisher
extends|extends
name|AbstractPublisher
implements|implements
name|ConsumerListener
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JmsPublisher
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Connection
name|connection
decl_stmt|;
specifier|private
name|JmsTopicExpressionConverter
name|topicConverter
decl_stmt|;
specifier|private
name|JAXBContext
name|jaxbContext
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ConsumerEventSource
argument_list|>
name|advisories
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Destination
argument_list|,
name|Object
argument_list|>
name|producers
decl_stmt|;
specifier|public
name|JmsPublisher
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|topicConverter
operator|=
operator|new
name|JmsTopicExpressionConverter
argument_list|()
expr_stmt|;
try|try
block|{
name|jaxbContext
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|Notify
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to create JAXB context"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Connection
name|getConnection
parameter_list|()
block|{
return|return
name|connection
return|;
block|}
specifier|public
name|void
name|setConnection
parameter_list|(
name|Connection
name|connection
parameter_list|)
block|{
name|this
operator|.
name|connection
operator|=
name|connection
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|notify
parameter_list|(
name|NotificationMessageHolderType
name|messageHolder
parameter_list|)
block|{
name|Session
name|session
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Topic
name|topic
init|=
name|topicConverter
operator|.
name|toActiveMQTopic
argument_list|(
name|messageHolder
operator|.
name|getTopic
argument_list|()
argument_list|)
decl_stmt|;
name|session
operator|=
name|connection
operator|.
name|createSession
argument_list|(
literal|false
argument_list|,
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
argument_list|)
expr_stmt|;
name|MessageProducer
name|producer
init|=
name|session
operator|.
name|createProducer
argument_list|(
name|topic
argument_list|)
decl_stmt|;
name|Notify
name|notify
init|=
operator|new
name|Notify
argument_list|()
decl_stmt|;
name|notify
operator|.
name|getNotificationMessage
argument_list|()
operator|.
name|add
argument_list|(
name|messageHolder
argument_list|)
expr_stmt|;
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|jaxbContext
operator|.
name|createMarshaller
argument_list|()
operator|.
name|marshal
argument_list|(
name|notify
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|session
operator|.
name|createTextMessage
argument_list|(
name|writer
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|producer
operator|.
name|send
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
decl||
name|JAXBException
decl||
name|InvalidTopicException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Error dispatching message"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Error closing session"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|validatePublisher
parameter_list|(
name|RegisterPublisher
name|registerPublisherRequest
parameter_list|)
throws|throws
name|InvalidTopicExpressionFault
throws|,
name|PublisherRegistrationFailedFault
throws|,
name|PublisherRegistrationRejectedFault
throws|,
name|ResourceUnknownFault
throws|,
name|TopicNotSupportedFault
block|{
name|super
operator|.
name|validatePublisher
argument_list|(
name|registerPublisherRequest
argument_list|)
expr_stmt|;
try|try
block|{
name|topicConverter
operator|.
name|toActiveMQTopic
argument_list|(
name|topic
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidTopicException
name|e
parameter_list|)
block|{
name|InvalidTopicExpressionFaultType
name|fault
init|=
operator|new
name|InvalidTopicExpressionFaultType
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|InvalidTopicExpressionFault
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|fault
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|start
parameter_list|()
throws|throws
name|PublisherRegistrationFailedFault
block|{
if|if
condition|(
name|demand
condition|)
block|{
try|try
block|{
name|producers
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|advisories
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|TopicExpressionType
name|topic
range|:
name|this
operator|.
name|topic
control|)
block|{
name|ConsumerEventSource
name|advisory
init|=
operator|new
name|ConsumerEventSource
argument_list|(
name|connection
argument_list|,
name|topicConverter
operator|.
name|toActiveMQTopic
argument_list|(
name|topic
argument_list|)
argument_list|)
decl_stmt|;
name|advisory
operator|.
name|setConsumerListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|advisory
operator|.
name|start
argument_list|()
expr_stmt|;
name|advisories
operator|.
name|add
argument_list|(
name|advisory
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|PublisherRegistrationFailedFaultType
name|fault
init|=
operator|new
name|PublisherRegistrationFailedFaultType
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|PublisherRegistrationFailedFault
argument_list|(
literal|"Error starting demand-based publisher"
argument_list|,
name|fault
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
specifier|protected
name|void
name|destroy
parameter_list|()
throws|throws
name|ResourceNotDestroyedFault
block|{
try|try
block|{
if|if
condition|(
name|advisories
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ConsumerEventSource
name|advisory
range|:
name|advisories
control|)
block|{
name|advisory
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|ResourceNotDestroyedFaultType
name|fault
init|=
operator|new
name|ResourceNotDestroyedFaultType
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|ResourceNotDestroyedFault
argument_list|(
literal|"Error destroying publisher"
argument_list|,
name|fault
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|super
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|synchronized
name|void
name|onConsumerEvent
parameter_list|(
name|ConsumerEvent
name|event
parameter_list|)
block|{
name|Object
name|producer
init|=
name|producers
operator|.
name|get
argument_list|(
name|event
operator|.
name|getDestination
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|event
operator|.
name|getConsumerCount
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|producer
operator|==
literal|null
condition|)
block|{
comment|// start subscription
name|producer
operator|=
name|startSubscription
argument_list|(
name|topicConverter
operator|.
name|toTopicExpression
argument_list|(
operator|(
name|Topic
operator|)
name|event
operator|.
name|getDestination
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|producers
operator|.
name|put
argument_list|(
name|event
operator|.
name|getDestination
argument_list|()
argument_list|,
name|producer
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|producer
operator|!=
literal|null
condition|)
block|{
name|Object
name|sub
init|=
name|producers
operator|.
name|remove
argument_list|(
name|event
operator|.
name|getDestination
argument_list|()
argument_list|)
decl_stmt|;
comment|// destroy subscription
name|stopSubscription
argument_list|(
name|sub
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
specifier|abstract
name|Object
name|startSubscription
parameter_list|(
name|TopicExpressionType
name|topic
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|void
name|stopSubscription
parameter_list|(
name|Object
name|sub
parameter_list|)
function_decl|;
block|}
end_class

end_unit

