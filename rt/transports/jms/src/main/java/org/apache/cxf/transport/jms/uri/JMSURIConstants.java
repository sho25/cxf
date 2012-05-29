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
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|JMSURIConstants
block|{
comment|// common constants
specifier|public
specifier|static
specifier|final
name|String
name|QUEUE
init|=
literal|"queue"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC
init|=
literal|"topic"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JNDI
init|=
literal|"jndi"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JNDI_TOPIC
init|=
literal|"jndi-topic"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|QUEUE_PREFIX
init|=
literal|"queue:"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_PREFIX
init|=
literal|"topic:"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JNDI_PREFIX
init|=
literal|"jndi:"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|DeliveryModeType
name|DELIVERYMODE_PERSISTENT
init|=
name|DeliveryModeType
operator|.
name|PERSISTENT
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|DeliveryModeType
name|DELIVERYMODE_NON_PERSISTENT
init|=
name|DeliveryModeType
operator|.
name|NON_PERSISTENT
decl_stmt|;
comment|// shared parameters
specifier|public
specifier|static
specifier|final
name|String
name|DELIVERYMODE_PARAMETER_NAME
init|=
literal|"deliveryMode"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TIMETOLIVE_PARAMETER_NAME
init|=
literal|"timeToLive"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PRIORITY_PARAMETER_NAME
init|=
literal|"priority"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPLYTONAME_PARAMETER_NAME
init|=
literal|"replyToName"
decl_stmt|;
comment|// The new configuration to set the message type of jms message body
specifier|public
specifier|static
specifier|final
name|String
name|MESSAGE_TYPE_PARAMETER_NAME
init|=
literal|"messageType"
decl_stmt|;
comment|// default parameters
specifier|public
specifier|static
specifier|final
name|DeliveryModeType
name|DELIVERYMODE_DEFAULT
init|=
name|DELIVERYMODE_PERSISTENT
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|long
name|TIMETOLIVE_DEFAULT
init|=
name|Message
operator|.
name|DEFAULT_TIME_TO_LIVE
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|PRIORITY_DEFAULT
init|=
name|Message
operator|.
name|DEFAULT_PRIORITY
decl_stmt|;
comment|// jndi parameters ? need to be sure.
specifier|public
specifier|static
specifier|final
name|String
name|JNDICONNECTIONFACTORYNAME_PARAMETER_NAME
init|=
literal|"jndiConnectionFactoryName"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JNDIINITIALCONTEXTFACTORY_PARAMETER_NAME
init|=
literal|"jndiInitialContextFactory"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JNDIURL_PARAMETER_NAME
init|=
literal|"jndiURL"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JNDI_PARAMETER_NAME_PREFIX
init|=
literal|"jndi-"
decl_stmt|;
comment|// queue and topic parameters
specifier|public
specifier|static
specifier|final
name|String
name|TOPICREPLYTONAME_PARAMETER_NAME
init|=
literal|"topicReplyToName"
decl_stmt|;
specifier|private
name|JMSURIConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

