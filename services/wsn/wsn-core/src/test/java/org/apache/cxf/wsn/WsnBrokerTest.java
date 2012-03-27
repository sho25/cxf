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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ServerSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLClassLoader
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|NoSuchElementException
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
name|CountDownLatch
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
name|TimeUnit
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
name|JAXBElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|ActiveMQConnectionFactory
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
name|client
operator|.
name|Consumer
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
name|client
operator|.
name|CreatePullPoint
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
name|client
operator|.
name|NotificationBroker
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
name|client
operator|.
name|Publisher
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
name|client
operator|.
name|PullPoint
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
name|client
operator|.
name|Registration
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
name|client
operator|.
name|Subscription
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
name|services
operator|.
name|JaxwsCreatePullPoint
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
name|services
operator|.
name|JaxwsNotificationBroker
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
name|types
operator|.
name|CustomType
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
name|util
operator|.
name|WSNHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|TopicExpressionType
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|WsnBrokerTest
extends|extends
name|Assert
block|{
specifier|private
name|boolean
name|useExternal
decl_stmt|;
specifier|private
name|ActiveMQConnectionFactory
name|activemq
decl_stmt|;
specifier|private
name|JaxwsNotificationBroker
name|notificationBrokerServer
decl_stmt|;
specifier|private
name|JaxwsCreatePullPoint
name|createPullPointServer
decl_stmt|;
specifier|private
name|NotificationBroker
name|notificationBroker
decl_stmt|;
specifier|private
name|CreatePullPoint
name|createPullPoint
decl_stmt|;
specifier|private
name|ClassLoader
name|loader
decl_stmt|;
specifier|private
name|int
name|port1
init|=
literal|8182
decl_stmt|;
specifier|private
name|int
name|port2
decl_stmt|;
specifier|protected
specifier|abstract
name|String
name|getProviderImpl
parameter_list|()
function_decl|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|loader
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
expr_stmt|;
name|String
name|impl
init|=
name|getProviderImpl
argument_list|()
decl_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
operator|new
name|FakeClassLoader
argument_list|(
name|impl
argument_list|)
argument_list|)
expr_stmt|;
name|WSNHelper
operator|.
name|getInstance
argument_list|()
operator|.
name|setClassLoader
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"javax.xml.ws.spi.Provider"
argument_list|,
name|impl
argument_list|)
expr_stmt|;
name|port2
operator|=
name|getFreePort
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|useExternal
condition|)
block|{
name|port1
operator|=
name|getFreePort
argument_list|()
expr_stmt|;
name|int
name|brokerPort
init|=
name|getFreePort
argument_list|()
decl_stmt|;
name|activemq
operator|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm:(broker:(tcp://localhost:"
operator|+
name|brokerPort
operator|+
literal|")?persistent=false)"
argument_list|)
expr_stmt|;
name|notificationBrokerServer
operator|=
operator|new
name|JaxwsNotificationBroker
argument_list|(
literal|"WSNotificationBroker"
argument_list|,
name|activemq
argument_list|)
expr_stmt|;
name|notificationBrokerServer
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"/wsn/NotificationBroker"
argument_list|)
expr_stmt|;
name|notificationBrokerServer
operator|.
name|init
argument_list|()
expr_stmt|;
name|createPullPointServer
operator|=
operator|new
name|JaxwsCreatePullPoint
argument_list|(
literal|"CreatePullPoint"
argument_list|,
name|activemq
argument_list|)
expr_stmt|;
name|createPullPointServer
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"/wsn/CreatePullPoint"
argument_list|)
expr_stmt|;
name|createPullPointServer
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
name|notificationBroker
operator|=
operator|new
name|NotificationBroker
argument_list|(
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"/wsn/NotificationBroker"
argument_list|)
expr_stmt|;
name|createPullPoint
operator|=
operator|new
name|CreatePullPoint
argument_list|(
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"/wsn/CreatePullPoint"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|int
name|getFreePort
parameter_list|()
throws|throws
name|IOException
block|{
name|ServerSocket
name|socket
init|=
operator|new
name|ServerSocket
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|int
name|port
init|=
name|socket
operator|.
name|getLocalPort
argument_list|()
decl_stmt|;
name|socket
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|port
return|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|useExternal
condition|)
block|{
name|notificationBrokerServer
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|createPullPointServer
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
name|System
operator|.
name|clearProperty
argument_list|(
literal|"javax.xml.ws.spi.Provider"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
name|WSNHelper
operator|.
name|clearInstance
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBroker
parameter_list|()
throws|throws
name|Exception
block|{
name|TestConsumer
name|callback
init|=
operator|new
name|TestConsumer
argument_list|()
decl_stmt|;
name|Consumer
name|consumer
init|=
operator|new
name|Consumer
argument_list|(
name|callback
argument_list|,
literal|"http://localhost:"
operator|+
name|port2
operator|+
literal|"/test/consumer"
argument_list|)
decl_stmt|;
name|Subscription
name|subscription
init|=
name|notificationBroker
operator|.
name|subscribe
argument_list|(
name|consumer
argument_list|,
literal|"myTopic"
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|callback
operator|.
name|notifications
init|)
block|{
name|notificationBroker
operator|.
name|notify
argument_list|(
literal|"myTopic"
argument_list|,
operator|new
name|JAXBElement
argument_list|<
name|String
argument_list|>
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:test:org"
argument_list|,
literal|"foo"
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|callback
operator|.
name|notifications
operator|.
name|wait
argument_list|(
literal|1000000
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|callback
operator|.
name|notifications
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|NotificationMessageHolderType
name|message
init|=
name|callback
operator|.
name|notifications
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|WSNHelper
operator|.
name|getInstance
argument_list|()
operator|.
name|getWSAAddress
argument_list|(
name|subscription
operator|.
name|getEpr
argument_list|()
argument_list|)
argument_list|,
name|WSNHelper
operator|.
name|getInstance
argument_list|()
operator|.
name|getWSAAddress
argument_list|(
name|message
operator|.
name|getSubscriptionReference
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|subscription
operator|.
name|unsubscribe
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPullPoint
parameter_list|()
throws|throws
name|Exception
block|{
name|PullPoint
name|pullPoint
init|=
name|createPullPoint
operator|.
name|create
argument_list|()
decl_stmt|;
name|Subscription
name|subscription
init|=
name|notificationBroker
operator|.
name|subscribe
argument_list|(
name|pullPoint
argument_list|,
literal|"myTopic"
argument_list|)
decl_stmt|;
name|notificationBroker
operator|.
name|notify
argument_list|(
literal|"myTopic"
argument_list|,
operator|new
name|JAXBElement
argument_list|<
name|String
argument_list|>
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:test:org"
argument_list|,
literal|"foo"
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|boolean
name|received
init|=
literal|false
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
literal|50
condition|;
name|i
operator|++
control|)
block|{
name|List
argument_list|<
name|NotificationMessageHolderType
argument_list|>
name|messages
init|=
name|pullPoint
operator|.
name|getMessages
argument_list|(
literal|10
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|messages
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|received
operator|=
literal|true
expr_stmt|;
break|break;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|received
argument_list|)
expr_stmt|;
name|subscription
operator|.
name|unsubscribe
argument_list|()
expr_stmt|;
name|pullPoint
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPublisher
parameter_list|()
throws|throws
name|Exception
block|{
name|TestConsumer
name|consumerCallback
init|=
operator|new
name|TestConsumer
argument_list|()
decl_stmt|;
name|Consumer
name|consumer
init|=
operator|new
name|Consumer
argument_list|(
name|consumerCallback
argument_list|,
literal|"http://localhost:"
operator|+
name|port2
operator|+
literal|"/test/consumer"
argument_list|)
decl_stmt|;
name|Subscription
name|subscription
init|=
name|notificationBroker
operator|.
name|subscribe
argument_list|(
name|consumer
argument_list|,
literal|"myTopic"
argument_list|)
decl_stmt|;
name|PublisherCallback
name|publisherCallback
init|=
operator|new
name|PublisherCallback
argument_list|()
decl_stmt|;
name|Publisher
name|publisher
init|=
operator|new
name|Publisher
argument_list|(
name|publisherCallback
argument_list|,
literal|"http://localhost:"
operator|+
name|port2
operator|+
literal|"/test/publisher"
argument_list|)
decl_stmt|;
name|Registration
name|registration
init|=
name|notificationBroker
operator|.
name|registerPublisher
argument_list|(
name|publisher
argument_list|,
literal|"myTopic"
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|consumerCallback
operator|.
name|notifications
init|)
block|{
name|notificationBroker
operator|.
name|notify
argument_list|(
name|publisher
argument_list|,
literal|"myTopic"
argument_list|,
operator|new
name|JAXBElement
argument_list|<
name|String
argument_list|>
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:test:org"
argument_list|,
literal|"foo"
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|consumerCallback
operator|.
name|notifications
operator|.
name|wait
argument_list|(
literal|1000000
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|consumerCallback
operator|.
name|notifications
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|NotificationMessageHolderType
name|message
init|=
name|consumerCallback
operator|.
name|notifications
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|WSNHelper
operator|.
name|getInstance
argument_list|()
operator|.
name|getWSAAddress
argument_list|(
name|subscription
operator|.
name|getEpr
argument_list|()
argument_list|)
argument_list|,
name|WSNHelper
operator|.
name|getInstance
argument_list|()
operator|.
name|getWSAAddress
argument_list|(
name|message
operator|.
name|getSubscriptionReference
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|WSNHelper
operator|.
name|getInstance
argument_list|()
operator|.
name|getWSAAddress
argument_list|(
name|publisher
operator|.
name|getEpr
argument_list|()
argument_list|)
argument_list|,
name|WSNHelper
operator|.
name|getInstance
argument_list|()
operator|.
name|getWSAAddress
argument_list|(
name|message
operator|.
name|getProducerReference
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|subscription
operator|.
name|unsubscribe
argument_list|()
expr_stmt|;
name|registration
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|publisher
operator|.
name|stop
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNullPublisherReference
parameter_list|()
throws|throws
name|Exception
block|{
name|TestConsumer
name|consumerCallback
init|=
operator|new
name|TestConsumer
argument_list|()
decl_stmt|;
name|Consumer
name|consumer
init|=
operator|new
name|Consumer
argument_list|(
name|consumerCallback
argument_list|,
literal|"http://localhost:"
operator|+
name|port2
operator|+
literal|"/test/consumer"
argument_list|)
decl_stmt|;
name|Subscription
name|subscription
init|=
name|notificationBroker
operator|.
name|subscribe
argument_list|(
name|consumer
argument_list|,
literal|"myTopicNullEPR"
argument_list|)
decl_stmt|;
name|Publisher
name|publisher
init|=
operator|new
name|Publisher
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Registration
name|registration
init|=
name|notificationBroker
operator|.
name|registerPublisher
argument_list|(
name|publisher
argument_list|,
literal|"myTopicNullEPR"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|consumerCallback
operator|.
name|notifications
init|)
block|{
name|notificationBroker
operator|.
name|notify
argument_list|(
name|publisher
argument_list|,
literal|"myTopicNullEPR"
argument_list|,
operator|new
name|JAXBElement
argument_list|<
name|String
argument_list|>
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:test:org"
argument_list|,
literal|"foo"
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|consumerCallback
operator|.
name|notifications
operator|.
name|wait
argument_list|(
literal|1000000
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|consumerCallback
operator|.
name|notifications
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|NotificationMessageHolderType
name|message
init|=
name|consumerCallback
operator|.
name|notifications
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|WSNHelper
operator|.
name|getInstance
argument_list|()
operator|.
name|getWSAAddress
argument_list|(
name|subscription
operator|.
name|getEpr
argument_list|()
argument_list|)
argument_list|,
name|WSNHelper
operator|.
name|getInstance
argument_list|()
operator|.
name|getWSAAddress
argument_list|(
name|message
operator|.
name|getSubscriptionReference
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|subscription
operator|.
name|unsubscribe
argument_list|()
expr_stmt|;
name|registration
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|publisher
operator|.
name|stop
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPublisherOnDemand
parameter_list|()
throws|throws
name|Exception
block|{
name|TestConsumer
name|consumerCallback
init|=
operator|new
name|TestConsumer
argument_list|()
decl_stmt|;
name|Consumer
name|consumer
init|=
operator|new
name|Consumer
argument_list|(
name|consumerCallback
argument_list|,
literal|"http://localhost:"
operator|+
name|port2
operator|+
literal|"/test/consumer"
argument_list|)
decl_stmt|;
name|PublisherCallback
name|publisherCallback
init|=
operator|new
name|PublisherCallback
argument_list|()
decl_stmt|;
name|Publisher
name|publisher
init|=
operator|new
name|Publisher
argument_list|(
name|publisherCallback
argument_list|,
literal|"http://localhost:"
operator|+
name|port2
operator|+
literal|"/test/publisher"
argument_list|)
decl_stmt|;
name|Registration
name|registration
init|=
name|notificationBroker
operator|.
name|registerPublisher
argument_list|(
name|publisher
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"myTopic1"
argument_list|,
literal|"myTopic2"
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Subscription
name|subscription
init|=
name|notificationBroker
operator|.
name|subscribe
argument_list|(
name|consumer
argument_list|,
literal|"myTopic1"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|publisherCallback
operator|.
name|subscribed
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|consumerCallback
operator|.
name|notifications
init|)
block|{
name|notificationBroker
operator|.
name|notify
argument_list|(
name|publisher
argument_list|,
literal|"myTopic1"
argument_list|,
operator|new
name|JAXBElement
argument_list|<
name|String
argument_list|>
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:test:org"
argument_list|,
literal|"foo"
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|consumerCallback
operator|.
name|notifications
operator|.
name|wait
argument_list|(
literal|1000000
argument_list|)
expr_stmt|;
block|}
name|subscription
operator|.
name|unsubscribe
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|publisherCallback
operator|.
name|unsubscribed
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|registration
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|publisher
operator|.
name|stop
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPublisherCustomType
parameter_list|()
throws|throws
name|Exception
block|{
name|notificationBroker
operator|.
name|setExtraClasses
argument_list|(
name|CustomType
operator|.
name|class
argument_list|)
expr_stmt|;
name|TestConsumer
name|consumerCallback
init|=
operator|new
name|TestConsumer
argument_list|()
decl_stmt|;
name|Consumer
name|consumer
init|=
operator|new
name|Consumer
argument_list|(
name|consumerCallback
argument_list|,
literal|"http://localhost:"
operator|+
name|port2
operator|+
literal|"/test/consumer"
argument_list|,
name|CustomType
operator|.
name|class
argument_list|)
decl_stmt|;
name|Subscription
name|subscription
init|=
name|notificationBroker
operator|.
name|subscribe
argument_list|(
name|consumer
argument_list|,
literal|"myTopic"
argument_list|)
decl_stmt|;
name|PublisherCallback
name|publisherCallback
init|=
operator|new
name|PublisherCallback
argument_list|()
decl_stmt|;
name|Publisher
name|publisher
init|=
operator|new
name|Publisher
argument_list|(
name|publisherCallback
argument_list|,
literal|"http://localhost:"
operator|+
name|port2
operator|+
literal|"/test/publisher"
argument_list|)
decl_stmt|;
name|Registration
name|registration
init|=
name|notificationBroker
operator|.
name|registerPublisher
argument_list|(
name|publisher
argument_list|,
literal|"myTopic"
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|consumerCallback
operator|.
name|notifications
init|)
block|{
name|notificationBroker
operator|.
name|notify
argument_list|(
name|publisher
argument_list|,
literal|"myTopic"
argument_list|,
operator|new
name|CustomType
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|consumerCallback
operator|.
name|notifications
operator|.
name|wait
argument_list|(
literal|1000000
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|consumerCallback
operator|.
name|notifications
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|NotificationMessageHolderType
name|message
init|=
name|consumerCallback
operator|.
name|notifications
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|WSNHelper
operator|.
name|getInstance
argument_list|()
operator|.
name|getWSAAddress
argument_list|(
name|subscription
operator|.
name|getEpr
argument_list|()
argument_list|)
argument_list|,
name|WSNHelper
operator|.
name|getInstance
argument_list|()
operator|.
name|getWSAAddress
argument_list|(
name|message
operator|.
name|getSubscriptionReference
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|WSNHelper
operator|.
name|getInstance
argument_list|()
operator|.
name|getWSAAddress
argument_list|(
name|publisher
operator|.
name|getEpr
argument_list|()
argument_list|)
argument_list|,
name|WSNHelper
operator|.
name|getInstance
argument_list|()
operator|.
name|getWSAAddress
argument_list|(
name|message
operator|.
name|getProducerReference
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|message
operator|.
name|getMessage
argument_list|()
operator|.
name|getAny
argument_list|()
operator|instanceof
name|CustomType
argument_list|)
expr_stmt|;
name|subscription
operator|.
name|unsubscribe
argument_list|()
expr_stmt|;
name|registration
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|publisher
operator|.
name|stop
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|TestConsumer
implements|implements
name|Consumer
operator|.
name|Callback
block|{
specifier|final
name|List
argument_list|<
name|NotificationMessageHolderType
argument_list|>
name|notifications
init|=
operator|new
name|ArrayList
argument_list|<
name|NotificationMessageHolderType
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|notify
parameter_list|(
name|NotificationMessageHolderType
name|message
parameter_list|)
block|{
synchronized|synchronized
init|(
name|notifications
init|)
block|{
name|notifications
operator|.
name|add
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|notifications
operator|.
name|notify
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
class|class
name|PublisherCallback
implements|implements
name|Publisher
operator|.
name|Callback
block|{
specifier|final
name|CountDownLatch
name|subscribed
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|CountDownLatch
name|unsubscribed
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|public
name|void
name|subscribe
parameter_list|(
name|TopicExpressionType
name|topic
parameter_list|)
block|{
name|subscribed
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|unsubscribe
parameter_list|(
name|TopicExpressionType
name|topic
parameter_list|)
block|{
name|unsubscribed
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
specifier|static
class|class
name|FakeClassLoader
extends|extends
name|URLClassLoader
block|{
specifier|private
specifier|final
name|String
name|provider
decl_stmt|;
specifier|public
name|FakeClassLoader
parameter_list|(
name|String
name|provider
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|URL
index|[
literal|0
index|]
argument_list|,
name|FakeClassLoader
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|provider
operator|=
name|provider
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|getResourceAsStream
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
literal|"META-INF/services/javax.xml.ws.spi.Provider"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|provider
operator|!=
literal|null
condition|?
operator|new
name|ByteArrayInputStream
argument_list|(
name|provider
operator|.
name|getBytes
argument_list|()
argument_list|)
else|:
literal|null
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|getResourceAsStream
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|getResources
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
literal|"META-INF/services/javax.xml.ws.spi.Provider"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
operator|new
name|Enumeration
argument_list|<
name|URL
argument_list|>
argument_list|()
block|{
specifier|public
name|boolean
name|hasMoreElements
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|URL
name|nextElement
parameter_list|()
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
block|}
return|;
block|}
return|return
name|super
operator|.
name|getResources
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

