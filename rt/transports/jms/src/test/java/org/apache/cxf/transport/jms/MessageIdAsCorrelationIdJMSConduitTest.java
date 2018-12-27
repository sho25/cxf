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
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
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
name|pool
operator|.
name|PooledConnectionFactory
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
name|ExchangeImpl
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
name|message
operator|.
name|MessageImpl
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
name|jms
operator|.
name|util
operator|.
name|TestReceiver
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_comment
comment|/**  * Checks if a CXF client works correlates requests and responses correctly if the server sets the message id  * as correlation id on the response message  */
end_comment

begin_class
specifier|public
class|class
name|MessageIdAsCorrelationIdJMSConduitTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SERVICE_QUEUE
init|=
literal|"test"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BROKER_URI
init|=
literal|"vm://localhost?broker.persistent=false"
decl_stmt|;
specifier|private
name|ConnectionFactory
name|connectionFactory
init|=
operator|new
name|PooledConnectionFactory
argument_list|(
name|BROKER_URI
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testSendReceiveWithTempReplyQueue
parameter_list|()
throws|throws
name|Exception
block|{
name|sendAndReceive
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSendReceive
parameter_list|()
throws|throws
name|Exception
block|{
name|sendAndReceive
argument_list|(
literal|true
argument_list|,
literal|"testreply"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|sendAndReceive
parameter_list|(
name|boolean
name|synchronous
parameter_list|,
name|String
name|replyDestination
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|EndpointReferenceType
name|target
init|=
operator|new
name|EndpointReferenceType
argument_list|()
decl_stmt|;
name|TestReceiver
name|receiver
init|=
operator|new
name|TestReceiver
argument_list|(
name|connectionFactory
argument_list|,
name|SERVICE_QUEUE
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|receiver
operator|.
name|runAsync
argument_list|()
expr_stmt|;
name|JMSConfiguration
name|jmsConfig
init|=
operator|new
name|JMSConfiguration
argument_list|()
decl_stmt|;
name|jmsConfig
operator|.
name|setTargetDestination
argument_list|(
name|SERVICE_QUEUE
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setUseConduitIdSelector
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setReplyDestination
argument_list|(
name|replyDestination
argument_list|)
expr_stmt|;
name|JMSConduit
name|conduit
init|=
operator|new
name|JMSConduit
argument_list|(
name|target
argument_list|,
name|jmsConfig
argument_list|,
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setSynchronous
argument_list|(
name|synchronous
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setOutMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|sendExchange
argument_list|(
name|exchange
argument_list|,
literal|"Request"
argument_list|)
expr_stmt|;
name|waitForAsyncReply
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|receiver
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No reply received within 2 seconds"
argument_list|,
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
expr_stmt|;
name|JMSMessageHeadersType
name|inHeaders
init|=
operator|(
name|JMSMessageHeadersType
operator|)
name|exchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_CLIENT_RESPONSE_HEADERS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|receiver
operator|.
name|getRequestMessageId
argument_list|()
argument_list|,
name|inHeaders
operator|.
name|getJMSCorrelationID
argument_list|()
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|waitForAsyncReply
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|InterruptedException
block|{
for|for
control|(
name|int
name|count
init|=
literal|0
init|;
name|exchange
operator|.
name|getInMessage
argument_list|()
operator|==
literal|null
operator|&&
name|count
operator|<=
literal|20
condition|;
name|count
operator|++
control|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|100L
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

