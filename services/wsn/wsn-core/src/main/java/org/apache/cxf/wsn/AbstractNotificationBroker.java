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
name|net
operator|.
name|URI
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
name|Map
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
name|ConcurrentHashMap
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
name|jws
operator|.
name|Oneway
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebResult
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
name|management
operator|.
name|ObjectName
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|wsaddressing
operator|.
name|W3CEndpointReference
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
name|util
operator|.
name|IdGenerator
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
name|GetCurrentMessage
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
name|GetCurrentMessageResponse
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
name|NoCurrentMessageOnTopicFaultType
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
name|RegisterPublisherResponse
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
name|NotificationBroker
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
name|MultipleTopicsSpecifiedFault
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
name|NoCurrentMessageOnTopicFault
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
name|oasis_open
operator|.
name|docs
operator|.
name|wsrf
operator|.
name|rp_2
operator|.
name|GetResourcePropertyResponse
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
name|rp_2
operator|.
name|InvalidResourcePropertyQNameFaultType
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
name|rpw_2
operator|.
name|GetResourceProperty
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
name|rpw_2
operator|.
name|InvalidResourcePropertyQNameFault
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
name|ResourceUnavailableFault
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
annotation|@
name|WebService
argument_list|(
name|endpointInterface
operator|=
literal|"org.oasis_open.docs.wsn.brw_2.NotificationBroker"
argument_list|)
specifier|public
specifier|abstract
class|class
name|AbstractNotificationBroker
extends|extends
name|AbstractEndpoint
implements|implements
name|NotificationBroker
implements|,
name|NotificationBrokerMBean
implements|,
name|GetResourceProperty
block|{
specifier|public
specifier|static
specifier|final
name|String
name|NAMESPACE_URI
init|=
literal|"http://docs.oasis-open.org/wsn/b-2"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PREFIX
init|=
literal|"wsnt"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|TOPIC_EXPRESSION_QNAME
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE_URI
argument_list|,
literal|"TopicExpression"
argument_list|,
name|PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|FIXED_TOPIC_SET_QNAME
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE_URI
argument_list|,
literal|"FixedTopicSet"
argument_list|,
name|PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|TOPIC_EXPRESSION_DIALECT_QNAME
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE_URI
argument_list|,
literal|"TopicExpressionDialect"
argument_list|,
name|PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|TOPIC_SET_QNAME
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE_URI
argument_list|,
literal|"TopicSet"
argument_list|,
name|PREFIX
argument_list|)
decl_stmt|;
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
name|AbstractNotificationBroker
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|IdGenerator
name|idGenerator
decl_stmt|;
specifier|private
name|AbstractPublisher
name|anonymousPublisher
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|AbstractPublisher
argument_list|>
name|publishers
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|AbstractSubscription
argument_list|>
name|subscriptions
decl_stmt|;
specifier|public
name|AbstractNotificationBroker
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
name|idGenerator
operator|=
operator|new
name|IdGenerator
argument_list|()
expr_stmt|;
name|subscriptions
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|publishers
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ObjectName
name|getMBeanName
parameter_list|()
block|{
try|try
block|{
return|return
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.cxf.service.wsn:type=WSNotificationBroker"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|Exception
block|{
name|register
argument_list|()
expr_stmt|;
name|anonymousPublisher
operator|=
name|createPublisher
argument_list|(
literal|"AnonymousPublisher"
argument_list|)
expr_stmt|;
name|anonymousPublisher
operator|.
name|setAddress
argument_list|(
operator|new
name|URI
argument_list|(
name|getAddress
argument_list|()
argument_list|)
operator|.
name|resolve
argument_list|(
name|anonymousPublisher
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|anonymousPublisher
operator|.
name|register
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
name|anonymousPublisher
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|unregister
argument_list|()
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getPublisher
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|publishers
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSubscriptions
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|subscriptions
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|EndpointMBean
name|getPublisher
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|publishers
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|EndpointMBean
name|getSubscription
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|subscriptions
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|EndpointMBean
name|getAnonymousPublisher
parameter_list|()
block|{
return|return
name|anonymousPublisher
return|;
block|}
comment|/**      *      * @param notify      */
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"Notify"
argument_list|)
annotation|@
name|Oneway
specifier|public
name|void
name|notify
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"Notify"
argument_list|,
name|targetNamespace
operator|=
literal|"http://docs.oasis-open.org/wsn/b-1"
argument_list|,
name|partName
operator|=
literal|"Notify"
argument_list|)
name|Notify
name|notify
parameter_list|)
block|{
name|LOGGER
operator|.
name|finest
argument_list|(
literal|"Notify"
argument_list|)
expr_stmt|;
name|handleNotify
argument_list|(
name|notify
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|handleNotify
parameter_list|(
name|Notify
name|notify
parameter_list|)
block|{
for|for
control|(
name|NotificationMessageHolderType
name|messageHolder
range|:
name|notify
operator|.
name|getNotificationMessage
argument_list|()
control|)
block|{
name|W3CEndpointReference
name|producerReference
init|=
name|messageHolder
operator|.
name|getProducerReference
argument_list|()
decl_stmt|;
name|AbstractPublisher
name|publisher
init|=
name|getPublisher
argument_list|(
name|producerReference
argument_list|)
decl_stmt|;
if|if
condition|(
name|publisher
operator|!=
literal|null
condition|)
block|{
name|publisher
operator|.
name|notify
argument_list|(
name|messageHolder
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|AbstractPublisher
name|getPublisher
parameter_list|(
name|W3CEndpointReference
name|producerReference
parameter_list|)
block|{
name|AbstractPublisher
name|publisher
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|producerReference
operator|!=
literal|null
condition|)
block|{
name|String
name|address
init|=
name|WSNHelper
operator|.
name|getInstance
argument_list|()
operator|.
name|getWSAAddress
argument_list|(
name|producerReference
argument_list|)
decl_stmt|;
name|publisher
operator|=
name|publishers
operator|.
name|get
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|publisher
operator|==
literal|null
condition|)
block|{
name|publisher
operator|=
name|anonymousPublisher
expr_stmt|;
block|}
return|return
name|publisher
return|;
block|}
comment|/**      *      * @param subscribeRequest      * @return returns org.oasis_open.docs.wsn.b_1.SubscribeResponse      * @throws SubscribeCreationFailedFault      * @throws InvalidTopicExpressionFault      * @throws TopicNotSupportedFault      * @throws InvalidFilterFault      * @throws InvalidProducerPropertiesExpressionFault      * @throws ResourceUnknownFault      * @throws InvalidMessageContentExpressionFault      * @throws TopicExpressionDialectUnknownFault      * @throws UnacceptableInitialTerminationTimeFault      */
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"Subscribe"
argument_list|)
annotation|@
name|WebResult
argument_list|(
name|name
operator|=
literal|"SubscribeResponse"
argument_list|,
name|targetNamespace
operator|=
literal|"http://docs.oasis-open.org/wsn/b-1"
argument_list|,
name|partName
operator|=
literal|"SubscribeResponse"
argument_list|)
specifier|public
name|SubscribeResponse
name|subscribe
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"Subscribe"
argument_list|,
name|targetNamespace
operator|=
literal|"http://docs.oasis-open.org/wsn/b-1"
argument_list|,
name|partName
operator|=
literal|"SubscribeRequest"
argument_list|)
name|Subscribe
name|subscribeRequest
parameter_list|)
comment|//CHECKSTYLE:OFF - WS-Notification spec throws a lot of faults
throws|throws
name|InvalidFilterFault
throws|,
name|InvalidMessageContentExpressionFault
throws|,
name|InvalidProducerPropertiesExpressionFault
throws|,
name|InvalidTopicExpressionFault
throws|,
name|ResourceUnknownFault
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
comment|//CHECKSTYLE:ON
name|LOGGER
operator|.
name|finest
argument_list|(
literal|"Subscribe"
argument_list|)
expr_stmt|;
return|return
name|handleSubscribe
argument_list|(
name|subscribeRequest
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|SubscribeResponse
name|handleSubscribe
parameter_list|(
name|Subscribe
name|subscribeRequest
parameter_list|,
name|EndpointManager
name|manager
parameter_list|)
comment|//CHECKSTYLE:OFF - WS-Notification spec throws a lot of faults
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
comment|//CHECKSTYLE:ON
name|AbstractSubscription
name|subscription
init|=
literal|null
decl_stmt|;
name|boolean
name|success
init|=
literal|false
decl_stmt|;
try|try
block|{
name|subscription
operator|=
name|createSubscription
argument_list|(
name|idGenerator
operator|.
name|generateSanitizedId
argument_list|()
argument_list|)
expr_stmt|;
name|subscription
operator|.
name|setBroker
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|subscriptions
operator|.
name|put
argument_list|(
name|subscription
operator|.
name|getAddress
argument_list|()
argument_list|,
name|subscription
argument_list|)
expr_stmt|;
name|subscription
operator|.
name|create
argument_list|(
name|subscribeRequest
argument_list|)
expr_stmt|;
if|if
condition|(
name|manager
operator|!=
literal|null
condition|)
block|{
name|subscription
operator|.
name|setManager
argument_list|(
name|manager
argument_list|)
expr_stmt|;
block|}
name|subscription
operator|.
name|register
argument_list|()
expr_stmt|;
name|SubscribeResponse
name|response
init|=
operator|new
name|SubscribeResponse
argument_list|()
decl_stmt|;
name|response
operator|.
name|setSubscriptionReference
argument_list|(
name|subscription
operator|.
name|getEpr
argument_list|()
argument_list|)
expr_stmt|;
name|success
operator|=
literal|true
expr_stmt|;
return|return
name|response
return|;
block|}
catch|catch
parameter_list|(
name|EndpointRegistrationException
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
literal|"Unable to register new endpoint"
argument_list|,
name|e
argument_list|)
expr_stmt|;
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
literal|"Unable to register new endpoint"
argument_list|,
name|fault
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
operator|!
name|success
operator|&&
name|subscription
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|subscription
operator|.
name|getAddress
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|subscriptions
operator|.
name|remove
argument_list|(
name|subscription
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|subscription
operator|.
name|unsubscribe
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnableToDestroySubscriptionFault
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
literal|"Error destroying subscription"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|unsubscribe
parameter_list|(
name|String
name|address
parameter_list|)
throws|throws
name|UnableToDestroySubscriptionFault
block|{
name|AbstractSubscription
name|subscription
init|=
name|subscriptions
operator|.
name|remove
argument_list|(
name|address
argument_list|)
decl_stmt|;
if|if
condition|(
name|subscription
operator|!=
literal|null
condition|)
block|{
name|subscription
operator|.
name|unsubscribe
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      *      * @param getCurrentMessageRequest      * @return returns org.oasis_open.docs.wsn.b_1.GetCurrentMessageResponse      * @throws MultipleTopicsSpecifiedFault      * @throws TopicNotSupportedFault      * @throws InvalidTopicExpressionFault      * @throws ResourceUnknownFault      * @throws TopicExpressionDialectUnknownFault      * @throws NoCurrentMessageOnTopicFault      */
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"GetCurrentMessage"
argument_list|)
annotation|@
name|WebResult
argument_list|(
name|name
operator|=
literal|"GetCurrentMessageResponse"
argument_list|,
name|targetNamespace
operator|=
literal|"http://docs.oasis-open.org/wsn/b-1"
argument_list|,
name|partName
operator|=
literal|"GetCurrentMessageResponse"
argument_list|)
specifier|public
name|GetCurrentMessageResponse
name|getCurrentMessage
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"GetCurrentMessage"
argument_list|,
name|targetNamespace
operator|=
literal|"http://docs.oasis-open.org/wsn/b-1"
argument_list|,
name|partName
operator|=
literal|"GetCurrentMessageRequest"
argument_list|)
name|GetCurrentMessage
name|getCurrentMessageRequest
parameter_list|)
comment|//CHECKSTYLE:OFF - WS-Notification spec throws a lot of faults
throws|throws
name|InvalidTopicExpressionFault
throws|,
name|MultipleTopicsSpecifiedFault
throws|,
name|NoCurrentMessageOnTopicFault
throws|,
name|ResourceUnknownFault
throws|,
name|TopicExpressionDialectUnknownFault
throws|,
name|TopicNotSupportedFault
block|{
comment|//CHECKSTYLE:ON
name|LOGGER
operator|.
name|finest
argument_list|(
literal|"GetCurrentMessage"
argument_list|)
expr_stmt|;
name|NoCurrentMessageOnTopicFaultType
name|fault
init|=
operator|new
name|NoCurrentMessageOnTopicFaultType
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|NoCurrentMessageOnTopicFault
argument_list|(
literal|"There is no current message on this topic."
argument_list|,
name|fault
argument_list|)
throw|;
block|}
comment|/**      *      * @param registerPublisherRequest      * @return returns org.oasis_open.docs.wsn.br_1.RegisterPublisherResponse      * @throws PublisherRegistrationRejectedFault      * @throws InvalidTopicExpressionFault      * @throws TopicNotSupportedFault      * @throws ResourceUnknownFault      * @throws PublisherRegistrationFailedFault      */
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"RegisterPublisher"
argument_list|)
annotation|@
name|WebResult
argument_list|(
name|name
operator|=
literal|"RegisterPublisherResponse"
argument_list|,
name|targetNamespace
operator|=
literal|"http://docs.oasis-open.org/wsn/br-1"
argument_list|,
name|partName
operator|=
literal|"RegisterPublisherResponse"
argument_list|)
specifier|public
name|RegisterPublisherResponse
name|registerPublisher
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"RegisterPublisher"
argument_list|,
name|targetNamespace
operator|=
literal|"http://docs.oasis-open.org/wsn/br-1"
argument_list|,
name|partName
operator|=
literal|"RegisterPublisherRequest"
argument_list|)
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
name|LOGGER
operator|.
name|finest
argument_list|(
literal|"RegisterPublisher"
argument_list|)
expr_stmt|;
return|return
name|handleRegisterPublisher
argument_list|(
name|registerPublisherRequest
argument_list|)
return|;
block|}
specifier|public
name|RegisterPublisherResponse
name|handleRegisterPublisher
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
name|AbstractPublisher
name|publisher
init|=
literal|null
decl_stmt|;
name|boolean
name|success
init|=
literal|false
decl_stmt|;
try|try
block|{
name|publisher
operator|=
name|createPublisher
argument_list|(
name|idGenerator
operator|.
name|generateSanitizedId
argument_list|()
argument_list|)
expr_stmt|;
name|publisher
operator|.
name|register
argument_list|()
expr_stmt|;
name|publisher
operator|.
name|create
argument_list|(
name|registerPublisherRequest
argument_list|)
expr_stmt|;
name|RegisterPublisherResponse
name|response
init|=
operator|new
name|RegisterPublisherResponse
argument_list|()
decl_stmt|;
name|response
operator|.
name|setPublisherRegistrationReference
argument_list|(
name|publisher
operator|.
name|getEpr
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|publisher
operator|.
name|getPublisherReference
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|publishers
operator|.
name|put
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
name|getPublisherReference
argument_list|()
argument_list|)
argument_list|,
name|publisher
argument_list|)
expr_stmt|;
block|}
name|success
operator|=
literal|true
expr_stmt|;
return|return
name|response
return|;
block|}
catch|catch
parameter_list|(
name|EndpointRegistrationException
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
literal|"Unable to register new endpoint"
argument_list|,
name|e
argument_list|)
expr_stmt|;
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
literal|"Unable to register new endpoint"
argument_list|,
name|fault
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
operator|!
name|success
operator|&&
name|publisher
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|publisher
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceNotDestroyedFault
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
literal|"Error destroying publisher"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
specifier|abstract
name|AbstractPublisher
name|createPublisher
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|AbstractSubscription
name|createSubscription
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
annotation|@
name|WebResult
argument_list|(
name|name
operator|=
literal|"GetResourcePropertyResponse"
argument_list|,
name|targetNamespace
operator|=
literal|"http://docs.oasis-open.org/wsrf/rp-2"
argument_list|,
name|partName
operator|=
literal|"GetResourcePropertyResponse"
argument_list|)
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"GetResourceProperty"
argument_list|)
specifier|public
name|GetResourcePropertyResponse
name|getResourceProperty
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|partName
operator|=
literal|"GetResourcePropertyRequest"
argument_list|,
name|name
operator|=
literal|"GetResourceProperty"
argument_list|,
name|targetNamespace
operator|=
literal|"http://docs.oasis-open.org/wsrf/rp-2"
argument_list|)
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
name|getResourcePropertyRequest
parameter_list|)
throws|throws
name|ResourceUnavailableFault
throws|,
name|ResourceUnknownFault
throws|,
name|InvalidResourcePropertyQNameFault
block|{
name|LOGGER
operator|.
name|finest
argument_list|(
literal|"GetResourceProperty"
argument_list|)
expr_stmt|;
return|return
name|handleGetResourceProperty
argument_list|(
name|getResourcePropertyRequest
argument_list|)
return|;
block|}
specifier|protected
name|GetResourcePropertyResponse
name|handleGetResourceProperty
parameter_list|(
name|QName
name|property
parameter_list|)
throws|throws
name|ResourceUnavailableFault
throws|,
name|ResourceUnknownFault
throws|,
name|InvalidResourcePropertyQNameFault
block|{
name|InvalidResourcePropertyQNameFaultType
name|fault
init|=
operator|new
name|InvalidResourcePropertyQNameFaultType
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|InvalidResourcePropertyQNameFault
argument_list|(
literal|"Invalid resource property QName: "
operator|+
name|property
argument_list|,
name|fault
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

