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
name|client
package|;
end_package

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
name|Endpoint
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
name|InvalidFilterFaultType
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
name|Renew
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
name|RenewResponse
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
name|b_2
operator|.
name|UnsubscribeResponse
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
name|NotifyMessageNotSupportedFault
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
name|SubscriptionManager
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

begin_comment
comment|/**  * Demand-based publisher.  *  */
end_comment

begin_class
annotation|@
name|WebService
argument_list|(
name|endpointInterface
operator|=
literal|"org.oasis_open.docs.wsn.bw_2.NotificationProducer"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/wsn/jaxws"
argument_list|,
name|serviceName
operator|=
literal|"NotificationProducerService"
argument_list|,
name|portName
operator|=
literal|"NotificationProducerPort"
argument_list|)
specifier|public
class|class
name|Publisher
implements|implements
name|NotificationProducer
implements|,
name|Referencable
block|{
specifier|public
specifier|static
specifier|final
name|String
name|WSN_URI
init|=
literal|"http://docs.oasis-open.org/wsn/b-2"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|QNAME_TOPIC_EXPRESSION
init|=
operator|new
name|QName
argument_list|(
name|WSN_URI
argument_list|,
literal|"TopicExpression"
argument_list|)
decl_stmt|;
specifier|public
interface|interface
name|Callback
block|{
name|void
name|subscribe
parameter_list|(
name|TopicExpressionType
name|topic
parameter_list|)
function_decl|;
name|void
name|unsubscribe
parameter_list|(
name|TopicExpressionType
name|topic
parameter_list|)
function_decl|;
block|}
specifier|private
specifier|final
name|Callback
name|callback
decl_stmt|;
specifier|private
specifier|final
name|String
name|address
decl_stmt|;
specifier|private
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
specifier|private
specifier|final
name|IdGenerator
name|idGenerator
init|=
operator|new
name|IdGenerator
argument_list|()
decl_stmt|;
specifier|public
name|Publisher
parameter_list|(
name|Callback
name|callback
parameter_list|,
name|String
name|address
parameter_list|)
block|{
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
if|if
condition|(
name|callback
operator|==
literal|null
operator|||
name|address
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|endpoint
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|endpoint
operator|=
name|WSNHelper
operator|.
name|getInstance
argument_list|()
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|stop
parameter_list|()
block|{
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|endpoint
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|W3CEndpointReference
name|getEpr
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|endpoint
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|this
operator|.
name|endpoint
operator|.
name|getEndpointReference
argument_list|(
name|W3CEndpointReference
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|SubscribeResponse
name|subscribe
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|partName
operator|=
literal|"SubscribeRequest"
argument_list|,
name|name
operator|=
literal|"Subscribe"
argument_list|,
name|targetNamespace
operator|=
literal|"http://docs.oasis-open.org/wsn/b-2"
argument_list|)
name|Subscribe
name|subscribeRequest
parameter_list|)
comment|//CHECKSTYLE:OFF - WS-Notification spec throws a lot of faults
throws|throws
name|InvalidTopicExpressionFault
throws|,
name|ResourceUnknownFault
throws|,
name|InvalidProducerPropertiesExpressionFault
throws|,
name|UnrecognizedPolicyRequestFault
throws|,
name|TopicExpressionDialectUnknownFault
throws|,
name|NotifyMessageNotSupportedFault
throws|,
name|InvalidFilterFault
throws|,
name|UnsupportedPolicyRequestFault
throws|,
name|InvalidMessageContentExpressionFault
throws|,
name|SubscribeCreationFailedFault
throws|,
name|TopicNotSupportedFault
throws|,
name|UnacceptableInitialTerminationTimeFault
block|{
comment|//CHECKSYTLE:ON
name|TopicExpressionType
name|topic
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|subscribeRequest
operator|.
name|getFilter
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Object
name|f
range|:
name|subscribeRequest
operator|.
name|getFilter
argument_list|()
operator|.
name|getAny
argument_list|()
control|)
block|{
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|e
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|f
operator|instanceof
name|JAXBElement
condition|)
block|{
name|e
operator|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|f
expr_stmt|;
name|f
operator|=
name|e
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|f
operator|instanceof
name|TopicExpressionType
condition|)
block|{
if|if
condition|(
operator|!
name|e
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|QNAME_TOPIC_EXPRESSION
argument_list|)
condition|)
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
literal|"Unrecognized TopicExpression: "
operator|+
name|e
argument_list|,
name|fault
argument_list|)
throw|;
block|}
name|topic
operator|=
operator|(
name|TopicExpressionType
operator|)
name|f
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|topic
operator|==
literal|null
condition|)
block|{
name|InvalidFilterFaultType
name|fault
init|=
operator|new
name|InvalidFilterFaultType
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|InvalidFilterFault
argument_list|(
literal|"Must specify a topic to subscribe on"
argument_list|,
name|fault
argument_list|)
throw|;
block|}
name|PublisherSubscription
name|pub
init|=
operator|new
name|PublisherSubscription
argument_list|(
name|topic
argument_list|)
decl_stmt|;
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
name|pub
operator|.
name|getEpr
argument_list|()
argument_list|)
expr_stmt|;
name|callback
operator|.
name|subscribe
argument_list|(
name|topic
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
specifier|protected
name|void
name|unsubscribe
parameter_list|(
name|TopicExpressionType
name|topic
parameter_list|)
block|{
name|callback
operator|.
name|unsubscribe
argument_list|(
name|topic
argument_list|)
expr_stmt|;
block|}
specifier|public
name|GetCurrentMessageResponse
name|getCurrentMessage
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|partName
operator|=
literal|"GetCurrentMessageRequest"
argument_list|,
name|name
operator|=
literal|"GetCurrentMessage"
argument_list|,
name|targetNamespace
operator|=
literal|"http://docs.oasis-open.org/wsn/b-2"
argument_list|)
name|GetCurrentMessage
name|getCurrentMessageRequest
parameter_list|)
comment|//CHECKSTYLE:OFF - WS-Notification spec throws a lot of faults
throws|throws
name|InvalidTopicExpressionFault
throws|,
name|ResourceUnknownFault
throws|,
name|TopicExpressionDialectUnknownFault
throws|,
name|MultipleTopicsSpecifiedFault
throws|,
name|NoCurrentMessageOnTopicFault
throws|,
name|TopicNotSupportedFault
block|{
comment|//CHECKSTYLE:ON
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
annotation|@
name|WebService
argument_list|(
name|endpointInterface
operator|=
literal|"org.oasis_open.docs.wsn.bw_2.SubscriptionManager"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/wsn/jaxws"
argument_list|,
name|serviceName
operator|=
literal|"SubscriptionManagerService"
argument_list|,
name|portName
operator|=
literal|"SubscriptionManagerPort"
argument_list|)
specifier|protected
class|class
name|PublisherSubscription
implements|implements
name|SubscriptionManager
block|{
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
specifier|private
specifier|final
name|TopicExpressionType
name|topic
decl_stmt|;
specifier|private
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
specifier|public
name|PublisherSubscription
parameter_list|(
name|TopicExpressionType
name|topic
parameter_list|)
block|{
name|this
operator|.
name|topic
operator|=
name|topic
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|idGenerator
operator|.
name|generateSanitizedId
argument_list|()
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|WSNHelper
operator|.
name|getInstance
argument_list|()
operator|.
name|publish
argument_list|(
name|address
operator|+
literal|"/subscriptions/"
operator|+
name|this
operator|.
name|id
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|W3CEndpointReference
name|getEpr
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|getEndpointReference
argument_list|(
name|W3CEndpointReference
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|UnsubscribeResponse
name|unsubscribe
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|partName
operator|=
literal|"UnsubscribeRequest"
argument_list|,
name|name
operator|=
literal|"Unsubscribe"
argument_list|,
name|targetNamespace
operator|=
literal|"http://docs.oasis-open.org/wsn/b-2"
argument_list|)
name|Unsubscribe
name|unsubscribeRequest
parameter_list|)
throws|throws
name|ResourceUnknownFault
throws|,
name|UnableToDestroySubscriptionFault
block|{
name|Publisher
operator|.
name|this
operator|.
name|unsubscribe
argument_list|(
name|topic
argument_list|)
expr_stmt|;
return|return
operator|new
name|UnsubscribeResponse
argument_list|()
return|;
block|}
specifier|public
name|RenewResponse
name|renew
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|partName
operator|=
literal|"RenewRequest"
argument_list|,
name|name
operator|=
literal|"Renew"
argument_list|,
name|targetNamespace
operator|=
literal|"http://docs.oasis-open.org/wsn/b-2"
argument_list|)
name|Renew
name|renewRequest
parameter_list|)
throws|throws
name|ResourceUnknownFault
throws|,
name|UnacceptableTerminationTimeFault
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

