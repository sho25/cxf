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
name|services
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
name|jws
operator|.
name|WebService
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
name|ws
operator|.
name|BindingType
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
name|AbstractSubscription
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
name|jms
operator|.
name|JmsPublisher
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
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|b_2
operator|.
name|FilterType
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
name|SubscribeResponse
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
name|b_2
operator|.
name|Unsubscribe
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
name|bw_2
operator|.
name|NotificationProducer
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
name|SubscriptionManager
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|endpointInterface
operator|=
literal|"org.oasis_open.docs.wsn.brw_2.PublisherRegistrationManager"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/wsn/jaxws"
argument_list|,
name|serviceName
operator|=
literal|"PublisherRegistrationManagerService"
argument_list|,
name|portName
operator|=
literal|"PublisherRegistrationManagerPort"
argument_list|)
annotation|@
name|BindingType
argument_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPBinding
operator|.
name|SOAP12HTTP_BINDING
argument_list|)
specifier|public
class|class
name|JaxwsPublisher
extends|extends
name|JmsPublisher
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
name|JaxwsPublisher
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|JaxwsNotificationBroker
name|notificationBroker
decl_stmt|;
specifier|private
name|NotificationProducer
name|notificationProducer
decl_stmt|;
specifier|public
name|JaxwsPublisher
parameter_list|(
name|String
name|name
parameter_list|,
name|JaxwsNotificationBroker
name|notificationBroker
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|notificationBroker
operator|=
name|notificationBroker
expr_stmt|;
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
name|super
operator|.
name|start
argument_list|()
expr_stmt|;
if|if
condition|(
name|demand
condition|)
block|{
name|notificationProducer
operator|=
name|WSNHelper
operator|.
name|getInstance
argument_list|()
operator|.
name|getPort
argument_list|(
name|publisherReference
argument_list|,
name|NotificationProducer
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|startSubscription
parameter_list|(
name|TopicExpressionType
name|topic
parameter_list|)
block|{
try|try
block|{
name|Subscribe
name|subscribeRequest
init|=
operator|new
name|Subscribe
argument_list|()
decl_stmt|;
name|subscribeRequest
operator|.
name|setConsumerReference
argument_list|(
name|notificationBroker
operator|.
name|getEpr
argument_list|()
argument_list|)
expr_stmt|;
name|subscribeRequest
operator|.
name|setFilter
argument_list|(
operator|new
name|FilterType
argument_list|()
argument_list|)
expr_stmt|;
name|subscribeRequest
operator|.
name|getFilter
argument_list|()
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|JAXBElement
argument_list|<
name|TopicExpressionType
argument_list|>
argument_list|(
name|AbstractSubscription
operator|.
name|QNAME_TOPIC_EXPRESSION
argument_list|,
name|TopicExpressionType
operator|.
name|class
argument_list|,
name|topic
argument_list|)
argument_list|)
expr_stmt|;
name|SubscribeResponse
name|response
init|=
name|notificationProducer
operator|.
name|subscribe
argument_list|(
name|subscribeRequest
argument_list|)
decl_stmt|;
return|return
name|WSNHelper
operator|.
name|getInstance
argument_list|()
operator|.
name|getPort
argument_list|(
name|response
operator|.
name|getSubscriptionReference
argument_list|()
argument_list|,
name|SubscriptionManager
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
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
literal|"Error while subscribing on-demand publisher"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|stopSubscription
parameter_list|(
name|Object
name|sub
parameter_list|)
block|{
try|try
block|{
operator|(
operator|(
name|SubscriptionManager
operator|)
name|sub
operator|)
operator|.
name|unsubscribe
argument_list|(
operator|new
name|Unsubscribe
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
literal|"Error while unsubscribing on-demand publisher"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

