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
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
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
name|AbstractCreatePullPoint
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
name|AbstractNotificationBroker
import|;
end_import

begin_comment
comment|/**  * Starts up an instance of a WS-Notification service  */
end_comment

begin_class
specifier|public
class|class
name|Service
block|{
name|String
name|rootURL
init|=
literal|"http://0.0.0.0:9000/wsn"
decl_stmt|;
name|String
name|activeMqUrl
init|=
literal|"vm:(broker:(tcp://localhost:6000)?persistent=false)"
decl_stmt|;
name|String
name|userName
decl_stmt|;
name|String
name|password
decl_stmt|;
name|boolean
name|jmxEnable
init|=
literal|true
decl_stmt|;
name|AbstractCreatePullPoint
name|createPullPointServer
decl_stmt|;
name|AbstractNotificationBroker
name|notificationBrokerServer
decl_stmt|;
specifier|public
name|Service
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|args
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
if|if
condition|(
literal|"-brokerUrl"
operator|.
name|equals
argument_list|(
name|args
index|[
name|x
index|]
argument_list|)
condition|)
block|{
name|activeMqUrl
operator|=
name|args
index|[
operator|++
name|x
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"-userName"
operator|.
name|equals
argument_list|(
name|args
index|[
name|x
index|]
argument_list|)
condition|)
block|{
name|userName
operator|=
name|args
index|[
operator|++
name|x
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"-password"
operator|.
name|equals
argument_list|(
name|args
index|[
name|x
index|]
argument_list|)
condition|)
block|{
name|password
operator|=
name|args
index|[
operator|++
name|x
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"-rootUrl"
operator|.
name|equals
argument_list|(
name|args
index|[
name|x
index|]
argument_list|)
condition|)
block|{
name|rootURL
operator|=
name|args
index|[
operator|++
name|x
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"-jmxEnable"
operator|.
name|equals
argument_list|(
name|args
index|[
name|x
index|]
argument_list|)
condition|)
block|{
name|jmxEnable
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|args
index|[
operator|++
name|x
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * @param args      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
operator|new
name|Service
argument_list|(
name|args
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|ActiveMQConnectionFactory
name|activemq
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
name|userName
argument_list|,
name|password
argument_list|,
name|activeMqUrl
argument_list|)
decl_stmt|;
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
name|rootURL
operator|+
literal|"/NotificationBroker"
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
name|rootURL
operator|+
literal|"/CreatePullPoint"
argument_list|)
expr_stmt|;
name|createPullPointServer
operator|.
name|init
argument_list|()
expr_stmt|;
if|if
condition|(
name|jmxEnable
condition|)
block|{
name|MBeanServer
name|mbs
init|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
decl_stmt|;
name|mbs
operator|.
name|registerMBean
argument_list|(
name|notificationBrokerServer
argument_list|,
name|notificationBrokerServer
operator|.
name|getMBeanName
argument_list|()
argument_list|)
expr_stmt|;
name|mbs
operator|.
name|registerMBean
argument_list|(
name|createPullPointServer
argument_list|,
name|createPullPointServer
operator|.
name|getMBeanName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

