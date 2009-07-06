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
name|uri
package|;
end_package

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
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|JMSEndpointTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testBasicQueue
parameter_list|()
throws|throws
name|Exception
block|{
name|JMSEndpoint
name|endpoint
init|=
name|resolveEndpoint
argument_list|(
literal|"jms:queue:Foo.Bar"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|JMSQueueEndpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getDestinationName
argument_list|()
argument_list|,
literal|"Foo.Bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getJmsVariant
argument_list|()
argument_list|,
name|JMSURIConstants
operator|.
name|QUEUE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testQueueParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|JMSEndpoint
name|endpoint
init|=
name|resolveEndpoint
argument_list|(
literal|"jms:queue:Foo.Bar?foo=bar&foo2=bar2"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|JMSQueueEndpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getDestinationName
argument_list|()
argument_list|,
literal|"Foo.Bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getJmsVariant
argument_list|()
argument_list|,
name|JMSURIConstants
operator|.
name|QUEUE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getParameters
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getParameter
argument_list|(
literal|"foo"
argument_list|)
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getParameter
argument_list|(
literal|"foo2"
argument_list|)
argument_list|,
literal|"bar2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBasicTopic
parameter_list|()
throws|throws
name|Exception
block|{
name|JMSEndpoint
name|endpoint
init|=
name|resolveEndpoint
argument_list|(
literal|"jms:topic:Foo.Bar"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|JMSTopicEndpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getDestinationName
argument_list|()
argument_list|,
literal|"Foo.Bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getJmsVariant
argument_list|()
argument_list|,
name|JMSURIConstants
operator|.
name|TOPIC
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTopicParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|JMSEndpoint
name|endpoint
init|=
name|resolveEndpoint
argument_list|(
literal|"jms:topic:Foo.Bar?foo=bar&foo2=bar2"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|JMSTopicEndpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getParameters
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getParameter
argument_list|(
literal|"foo"
argument_list|)
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getParameter
argument_list|(
literal|"foo2"
argument_list|)
argument_list|,
literal|"bar2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBasicJNDI
parameter_list|()
throws|throws
name|Exception
block|{
name|JMSEndpoint
name|endpoint
init|=
name|resolveEndpoint
argument_list|(
literal|"jms:jndi:Foo.Bar"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|JMSJNDIEndpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getDestinationName
argument_list|()
argument_list|,
literal|"Foo.Bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getJmsVariant
argument_list|()
argument_list|,
name|JMSURIConstants
operator|.
name|JNDI
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJNDIParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|JMSEndpoint
name|endpoint
init|=
name|resolveEndpoint
argument_list|(
literal|"jms:jndi:Foo.Bar?"
operator|+
literal|"jndiInitialContextFactory"
operator|+
literal|"=org.apache.activemq.jndi.ActiveMQInitialContextFactory"
operator|+
literal|"&jndiConnectionFactoryName=ConnectionFactory"
operator|+
literal|"&jndiURL=tcp://localhost:61616"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|JMSJNDIEndpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getParameters
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getDestinationName
argument_list|()
argument_list|,
literal|"Foo.Bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getJndiInitialContextFactory
argument_list|()
argument_list|,
literal|"org.apache.activemq.jndi.ActiveMQInitialContextFactory"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getJndiConnectionFactoryName
argument_list|()
argument_list|,
literal|"ConnectionFactory"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getJndiURL
argument_list|()
argument_list|,
literal|"tcp://localhost:61616"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJNDIWithAdditionalParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|JMSEndpoint
name|endpoint
init|=
name|resolveEndpoint
argument_list|(
literal|"jms:jndi:Foo.Bar?"
operator|+
literal|"jndiInitialContextFactory"
operator|+
literal|"=org.apache.activemq.jndi.ActiveMQInitialContextFactory"
operator|+
literal|"&jndiConnectionFactoryName=ConnectionFactory"
operator|+
literal|"&jndiURL=tcp://localhost:61616"
operator|+
literal|"&jndi-com.sun.jndi.someParameter=someValue"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|JMSJNDIEndpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getParameters
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getJndiInitialContextFactory
argument_list|()
argument_list|,
literal|"org.apache.activemq.jndi.ActiveMQInitialContextFactory"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getJndiConnectionFactoryName
argument_list|()
argument_list|,
literal|"ConnectionFactory"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getJndiURL
argument_list|()
argument_list|,
literal|"tcp://localhost:61616"
argument_list|)
expr_stmt|;
name|Map
name|addParas
init|=
name|endpoint
operator|.
name|getJndiParameters
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|addParas
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|addParas
operator|.
name|get
argument_list|(
literal|"com.sun.jndi.someParameter"
argument_list|)
argument_list|,
literal|"someValue"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSharedParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|JMSEndpoint
name|endpoint
init|=
name|resolveEndpoint
argument_list|(
literal|"jms:queue:Foo.Bar?"
operator|+
literal|"deliveryMode=NON_PERSISTENT"
operator|+
literal|"&timeToLive=100"
operator|+
literal|"&priority=5"
operator|+
literal|"&replyToName=foo.bar2"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|JMSQueueEndpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getParameters
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getDeliveryMode
argument_list|()
argument_list|,
name|JMSURIConstants
operator|.
name|DELIVERYMODE_NON_PERSISTENT
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getTimeToLive
argument_list|()
argument_list|,
literal|100
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getPriority
argument_list|()
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getReplyToName
argument_list|()
argument_list|,
literal|"foo.bar2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRequestUri
parameter_list|()
throws|throws
name|Exception
block|{
name|JMSEndpoint
name|endpoint
init|=
name|resolveEndpoint
argument_list|(
literal|"jms:jndi:Foo.Bar?"
operator|+
literal|"jndiInitialContextFactory"
operator|+
literal|"=org.apache.activemq.jndi.ActiveMQInitialContextFactory"
operator|+
literal|"&foo=bar"
operator|+
literal|"&foo2=bar2"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|JMSJNDIEndpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getParameters
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|String
name|requestUri
init|=
name|endpoint
operator|.
name|getRequestURI
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|requestUri
operator|.
name|startsWith
argument_list|(
literal|"jms:jndi:Foo.Bar?"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|requestUri
operator|.
name|contains
argument_list|(
literal|"foo=bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|requestUri
operator|.
name|contains
argument_list|(
literal|"foo2=bar2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|JMSEndpoint
name|resolveEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|JMSEndpoint
name|endpoint
init|=
literal|null
decl_stmt|;
try|try
block|{
name|endpoint
operator|=
name|JMSEndpointParser
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

