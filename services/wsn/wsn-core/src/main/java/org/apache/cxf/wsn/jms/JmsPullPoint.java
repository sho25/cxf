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
name|StringReader
import|;
end_import

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
name|List
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
name|MessageProducer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Queue
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
name|stream
operator|.
name|XMLStreamException
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
name|XMLStreamReader
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
name|staxutils
operator|.
name|StaxUtils
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
name|AbstractPullPoint
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
name|UnableToGetMessagesFaultType
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
name|UnableToGetMessagesFault
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
class|class
name|JmsPullPoint
extends|extends
name|AbstractPullPoint
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
name|JmsPullPoint
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|JAXBContext
name|jaxbContext
decl_stmt|;
specifier|private
name|Connection
name|connection
decl_stmt|;
specifier|private
name|Session
name|producerSession
decl_stmt|;
specifier|private
name|Session
name|consumerSession
decl_stmt|;
specifier|private
name|Queue
name|queue
decl_stmt|;
specifier|private
name|MessageProducer
name|producer
decl_stmt|;
specifier|private
name|MessageConsumer
name|consumer
decl_stmt|;
specifier|public
name|JmsPullPoint
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
literal|"Could not create PullEndpoint"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
specifier|synchronized
name|void
name|initSession
parameter_list|()
throws|throws
name|JMSException
block|{
if|if
condition|(
name|producerSession
operator|==
literal|null
operator|||
name|consumerSession
operator|==
literal|null
condition|)
block|{
name|producerSession
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
name|consumerSession
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
name|queue
operator|=
name|producerSession
operator|.
name|createQueue
argument_list|(
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|producer
operator|=
name|producerSession
operator|.
name|createProducer
argument_list|(
name|queue
argument_list|)
expr_stmt|;
name|consumer
operator|=
name|consumerSession
operator|.
name|createConsumer
argument_list|(
name|queue
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|synchronized
name|void
name|closeSession
parameter_list|()
block|{
if|if
condition|(
name|producerSession
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|producerSession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|inner
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
literal|"Error closing ProducerSession"
argument_list|,
name|inner
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|producerSession
operator|=
literal|null
expr_stmt|;
block|}
block|}
if|if
condition|(
name|consumerSession
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|consumerSession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|inner
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
literal|"Error closing ConsumerSession"
argument_list|,
name|inner
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|consumerSession
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
name|store
parameter_list|(
name|NotificationMessageHolderType
name|messageHolder
parameter_list|)
block|{
try|try
block|{
name|initSession
argument_list|()
expr_stmt|;
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
synchronized|synchronized
init|(
name|producerSession
init|)
block|{
name|Message
name|message
init|=
name|producerSession
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
name|WARNING
argument_list|,
literal|"Error storing message"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|closeSession
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
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
literal|"Error storing message"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|List
argument_list|<
name|NotificationMessageHolderType
argument_list|>
name|getMessages
parameter_list|(
name|int
name|max
parameter_list|)
throws|throws
name|ResourceUnknownFault
throws|,
name|UnableToGetMessagesFault
block|{
try|try
block|{
if|if
condition|(
name|max
operator|==
literal|0
condition|)
block|{
name|max
operator|=
literal|256
expr_stmt|;
block|}
name|initSession
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|NotificationMessageHolderType
argument_list|>
name|messages
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|max
condition|;
name|i
operator|++
control|)
block|{
name|Message
name|msg
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|consumerSession
init|)
block|{
name|msg
operator|=
name|consumer
operator|.
name|receiveNoWait
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|msg
operator|==
literal|null
condition|)
block|{
break|break;
block|}
name|TextMessage
name|txtMsg
init|=
operator|(
name|TextMessage
operator|)
name|msg
decl_stmt|;
name|StringReader
name|reader
init|=
operator|new
name|StringReader
argument_list|(
name|txtMsg
operator|.
name|getText
argument_list|()
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|xreader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|reader
argument_list|)
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
name|xreader
argument_list|)
decl_stmt|;
try|try
block|{
name|xreader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
comment|//ignoreable
block|}
name|messages
operator|.
name|addAll
argument_list|(
name|notify
operator|.
name|getNotificationMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|messages
return|;
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
name|INFO
argument_list|,
literal|"Error retrieving messages"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|closeSession
argument_list|()
expr_stmt|;
name|UnableToGetMessagesFaultType
name|fault
init|=
operator|new
name|UnableToGetMessagesFaultType
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|UnableToGetMessagesFault
argument_list|(
literal|"Unable to retrieve messages"
argument_list|,
name|fault
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"Error retrieving messages"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|UnableToGetMessagesFaultType
name|fault
init|=
operator|new
name|UnableToGetMessagesFaultType
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|UnableToGetMessagesFault
argument_list|(
literal|"Unable to retrieve messages"
argument_list|,
name|fault
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
block|}
end_class

end_unit

