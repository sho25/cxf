begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|StringReader
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
name|MessageConsumer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageListener
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
name|TextMessage
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
name|javax
operator|.
name|xml
operator|.
name|datatype
operator|.
name|XMLGregorianCalendar
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPath
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathExpression
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathExpressionException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactory
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
name|AbstractSubscription
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
name|PauseFailedFaultType
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
name|ResumeFailedFaultType
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
name|Subscribe
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
name|SubscribeCreationFailedFaultType
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
name|UnableToDestroySubscriptionFaultType
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
name|UnacceptableTerminationTimeFaultType
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
name|InvalidFilterFault
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
name|InvalidMessageContentExpressionFault
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
name|InvalidProducerPropertiesExpressionFault
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
name|PauseFailedFault
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
name|ResumeFailedFault
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
name|SubscribeCreationFailedFault
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
name|TopicExpressionDialectUnknownFault
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
name|wsn
operator|.
name|bw_2
operator|.
name|UnableToDestroySubscriptionFault
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
name|UnacceptableInitialTerminationTimeFault
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
name|UnacceptableTerminationTimeFault
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
name|UnrecognizedPolicyRequestFault
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
name|UnsupportedPolicyRequestFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|JmsSubscription
extends|extends
name|AbstractSubscription
implements|implements
name|MessageListener
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JmsSubscription
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Connection
name|connection
decl_stmt|;
specifier|private
name|Session
name|session
decl_stmt|;
specifier|private
name|JmsTopicExpressionConverter
name|topicConverter
decl_stmt|;
specifier|private
name|Topic
name|jmsTopic
decl_stmt|;
specifier|private
name|JAXBContext
name|jaxbContext
decl_stmt|;
specifier|public
name|JmsSubscription
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
specifier|protected
name|void
name|start
parameter_list|()
throws|throws
name|SubscribeCreationFailedFault
block|{
try|try
block|{
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
name|MessageConsumer
name|consumer
init|=
name|session
operator|.
name|createConsumer
argument_list|(
name|jmsTopic
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|setMessageListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
name|SubscribeCreationFailedFaultType
name|fault
init|=
operator|new
name|SubscribeCreationFailedFaultType
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|SubscribeCreationFailedFault
argument_list|(
literal|"Error starting subscription"
argument_list|,
name|fault
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|validateSubscription
parameter_list|(
name|Subscribe
name|subscribeRequest
parameter_list|)
throws|throws
name|InvalidFilterFault
throws|,
name|InvalidMessageContentExpressionFault
throws|,
name|InvalidProducerPropertiesExpressionFault
throws|,
name|InvalidTopicExpressionFault
throws|,
name|SubscribeCreationFailedFault
throws|,
name|TopicExpressionDialectUnknownFault
throws|,
name|TopicNotSupportedFault
throws|,
name|UnacceptableInitialTerminationTimeFault
throws|,
name|UnsupportedPolicyRequestFault
throws|,
name|UnrecognizedPolicyRequestFault
block|{
name|super
operator|.
name|validateSubscription
argument_list|(
name|subscribeRequest
argument_list|)
expr_stmt|;
try|try
block|{
name|jmsTopic
operator|=
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
name|pause
parameter_list|()
throws|throws
name|PauseFailedFault
block|{
if|if
condition|(
name|session
operator|==
literal|null
condition|)
block|{
name|PauseFailedFaultType
name|fault
init|=
operator|new
name|PauseFailedFaultType
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|PauseFailedFault
argument_list|(
literal|"Subscription is already paused"
argument_list|,
name|fault
argument_list|)
throw|;
block|}
else|else
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
name|PauseFailedFaultType
name|fault
init|=
operator|new
name|PauseFailedFaultType
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|PauseFailedFault
argument_list|(
literal|"Error pausing subscription"
argument_list|,
name|fault
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|session
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|resume
parameter_list|()
throws|throws
name|ResumeFailedFault
block|{
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|ResumeFailedFaultType
name|fault
init|=
operator|new
name|ResumeFailedFaultType
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|ResumeFailedFault
argument_list|(
literal|"Subscription is already running"
argument_list|,
name|fault
argument_list|)
throw|;
block|}
else|else
block|{
try|try
block|{
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
name|MessageConsumer
name|consumer
init|=
name|session
operator|.
name|createConsumer
argument_list|(
name|jmsTopic
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|setMessageListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
name|ResumeFailedFaultType
name|fault
init|=
operator|new
name|ResumeFailedFaultType
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|ResumeFailedFault
argument_list|(
literal|"Error resuming subscription"
argument_list|,
name|fault
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|renew
parameter_list|(
name|XMLGregorianCalendar
name|terminationTime
parameter_list|)
throws|throws
name|UnacceptableTerminationTimeFault
block|{
name|UnacceptableTerminationTimeFaultType
name|fault
init|=
operator|new
name|UnacceptableTerminationTimeFaultType
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|UnacceptableTerminationTimeFault
argument_list|(
literal|"TerminationTime is not supported"
argument_list|,
name|fault
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|unsubscribe
parameter_list|()
throws|throws
name|UnableToDestroySubscriptionFault
block|{
name|super
operator|.
name|unsubscribe
argument_list|()
expr_stmt|;
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
name|UnableToDestroySubscriptionFaultType
name|fault
init|=
operator|new
name|UnableToDestroySubscriptionFaultType
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|UnableToDestroySubscriptionFault
argument_list|(
literal|"Unable to unsubscribe"
argument_list|,
name|fault
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|session
operator|=
literal|null
expr_stmt|;
block|}
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
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|jmsMessage
parameter_list|)
block|{
try|try
block|{
name|TextMessage
name|text
init|=
operator|(
name|TextMessage
operator|)
name|jmsMessage
decl_stmt|;
name|Notify
name|notify
init|=
operator|(
name|Notify
operator|)
name|jaxbContext
operator|.
name|createUnmarshaller
argument_list|()
operator|.
name|unmarshal
argument_list|(
operator|new
name|StringReader
argument_list|(
name|text
operator|.
name|getText
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|NotificationMessageHolderType
argument_list|>
name|ith
init|=
name|notify
operator|.
name|getNotificationMessage
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|ith
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|NotificationMessageHolderType
name|h
init|=
name|ith
operator|.
name|next
argument_list|()
decl_stmt|;
name|Object
name|content
init|=
name|h
operator|.
name|getMessage
argument_list|()
operator|.
name|getAny
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|content
operator|instanceof
name|Element
operator|)
condition|)
block|{
name|DocumentBuilderFactory
name|factory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|factory
operator|.
name|newDocumentBuilder
argument_list|()
operator|.
name|newDocument
argument_list|()
decl_stmt|;
name|jaxbContext
operator|.
name|createMarshaller
argument_list|()
operator|.
name|marshal
argument_list|(
name|content
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|content
operator|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|doFilter
argument_list|(
operator|(
name|Element
operator|)
name|content
argument_list|)
condition|)
block|{
name|ith
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|h
operator|.
name|setTopic
argument_list|(
name|topic
argument_list|)
expr_stmt|;
name|h
operator|.
name|setSubscriptionReference
argument_list|(
name|getEpr
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|notify
operator|.
name|getNotificationMessage
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|doNotify
argument_list|(
name|notify
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
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Error notifying consumer"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|boolean
name|doFilter
parameter_list|(
name|Element
name|content
parameter_list|)
block|{
if|if
condition|(
name|contentFilter
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|contentFilter
operator|.
name|getDialect
argument_list|()
operator|.
name|equals
argument_list|(
name|XPATH1_URI
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unsupported dialect: "
operator|+
name|contentFilter
operator|.
name|getDialect
argument_list|()
argument_list|)
throw|;
block|}
try|try
block|{
name|XPathFactory
name|xpfactory
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|XPath
name|xpath
init|=
name|xpfactory
operator|.
name|newXPath
argument_list|()
decl_stmt|;
name|XPathExpression
name|exp
init|=
name|xpath
operator|.
name|compile
argument_list|(
name|contentFilter
operator|.
name|getContent
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|Boolean
name|ret
init|=
operator|(
name|Boolean
operator|)
name|exp
operator|.
name|evaluate
argument_list|(
name|content
argument_list|,
name|XPathConstants
operator|.
name|BOOLEAN
argument_list|)
decl_stmt|;
return|return
name|ret
operator|.
name|booleanValue
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|XPathExpressionException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Could not filter notification"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|protected
specifier|abstract
name|void
name|doNotify
parameter_list|(
name|Notify
name|notify
parameter_list|)
function_decl|;
block|}
end_class

end_unit

