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
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
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
name|wsn
operator|.
name|EndpointManager
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
name|JmsNotificationBroker
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
name|jms
operator|.
name|JmsSubscription
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|endpointInterface
operator|=
literal|"org.oasis_open.docs.wsn.brw_2.NotificationBroker"
argument_list|,
name|targetNamespace
operator|=
literal|"http://docs.oasis-open.org/wsn/brw-2"
argument_list|,
name|serviceName
operator|=
literal|"NotificationBroker"
argument_list|,
name|portName
operator|=
literal|"NotificationBrokerPort"
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
name|JaxwsNotificationBroker
extends|extends
name|JmsNotificationBroker
implements|implements
name|JaxwsNotificationBrokerMBean
block|{
specifier|public
name|JaxwsNotificationBroker
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
argument_list|(
name|name
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JaxwsNotificationBroker
parameter_list|(
name|String
name|name
parameter_list|,
name|ConnectionFactory
name|connectionFactory
parameter_list|)
block|{
name|this
argument_list|(
name|name
argument_list|,
name|connectionFactory
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JaxwsNotificationBroker
parameter_list|(
name|String
name|name
parameter_list|,
name|ConnectionFactory
name|connectionFactory
parameter_list|,
name|EndpointManager
name|epManager
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|connectionFactory
argument_list|)
expr_stmt|;
if|if
condition|(
name|epManager
operator|==
literal|null
condition|)
block|{
name|manager
operator|=
operator|new
name|JaxwsEndpointManager
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|manager
operator|=
name|epManager
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|JmsSubscription
name|createJmsSubscription
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|JaxwsSubscription
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|JmsPublisher
name|createJmsPublisher
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|JaxwsPublisher
argument_list|(
name|name
argument_list|,
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

