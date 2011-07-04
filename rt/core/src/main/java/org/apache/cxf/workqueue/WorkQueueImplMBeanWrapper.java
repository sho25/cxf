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
name|workqueue
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|JMException
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|management
operator|.
name|ManagedComponent
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
name|management
operator|.
name|ManagementConstants
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
name|management
operator|.
name|annotation
operator|.
name|ManagedAttribute
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
name|management
operator|.
name|annotation
operator|.
name|ManagedResource
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|componentName
operator|=
literal|"WorkQueue"
argument_list|,
name|description
operator|=
literal|"The CXF work queue"
argument_list|,
name|currencyTimeLimit
operator|=
literal|15
argument_list|,
name|persistPolicy
operator|=
literal|"OnUpdate"
argument_list|,
name|persistPeriod
operator|=
literal|200
argument_list|)
specifier|public
class|class
name|WorkQueueImplMBeanWrapper
implements|implements
name|ManagedComponent
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TYPE_VALUE
init|=
literal|"WorkQueues"
decl_stmt|;
specifier|private
name|AutomaticWorkQueueImpl
name|aWorkQueue
decl_stmt|;
specifier|public
name|WorkQueueImplMBeanWrapper
parameter_list|(
name|AutomaticWorkQueueImpl
name|wq
parameter_list|)
block|{
name|aWorkQueue
operator|=
name|wq
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The WorkQueueMaxSize"
argument_list|,
name|persistPolicy
operator|=
literal|"OnUpdate"
argument_list|)
specifier|public
name|long
name|getWorkQueueMaxSize
parameter_list|()
block|{
return|return
name|aWorkQueue
operator|.
name|getMaxSize
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The WorkQueue Current size"
argument_list|,
name|persistPolicy
operator|=
literal|"OnUpdate"
argument_list|)
specifier|public
name|long
name|getWorkQueueSize
parameter_list|()
block|{
return|return
name|aWorkQueue
operator|.
name|getSize
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The largest number of threads"
argument_list|)
specifier|public
name|int
name|getLargestPoolSize
parameter_list|()
block|{
return|return
name|aWorkQueue
operator|.
name|getLargestPoolSize
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The current number of threads"
argument_list|)
specifier|public
name|int
name|getPoolSize
parameter_list|()
block|{
return|return
name|aWorkQueue
operator|.
name|getPoolSize
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The number of threads currently busy"
argument_list|)
specifier|public
name|int
name|getActiveCount
parameter_list|()
block|{
return|return
name|aWorkQueue
operator|.
name|getActiveCount
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The WorkQueue has nothing to do"
argument_list|,
name|persistPolicy
operator|=
literal|"OnUpdate"
argument_list|)
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|aWorkQueue
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The WorkQueue is very busy"
argument_list|)
specifier|public
name|boolean
name|isFull
parameter_list|()
block|{
return|return
name|aWorkQueue
operator|.
name|isFull
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The WorkQueue HighWaterMark"
argument_list|,
name|persistPolicy
operator|=
literal|"OnUpdate"
argument_list|)
specifier|public
name|int
name|getHighWaterMark
parameter_list|()
block|{
return|return
name|aWorkQueue
operator|.
name|getHighWaterMark
argument_list|()
return|;
block|}
specifier|public
name|void
name|setHighWaterMark
parameter_list|(
name|int
name|hwm
parameter_list|)
block|{
name|aWorkQueue
operator|.
name|setHighWaterMark
argument_list|(
name|hwm
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The WorkQueue LowWaterMark"
argument_list|,
name|persistPolicy
operator|=
literal|"OnUpdate"
argument_list|)
specifier|public
name|int
name|getLowWaterMark
parameter_list|()
block|{
return|return
name|aWorkQueue
operator|.
name|getLowWaterMark
argument_list|()
return|;
block|}
specifier|public
name|void
name|setLowWaterMark
parameter_list|(
name|int
name|lwm
parameter_list|)
block|{
name|aWorkQueue
operator|.
name|setLowWaterMark
argument_list|(
name|lwm
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ObjectName
name|getObjectName
parameter_list|()
throws|throws
name|JMException
block|{
name|WorkQueueManager
name|mgr
init|=
name|aWorkQueue
operator|.
name|getManager
argument_list|()
decl_stmt|;
name|String
name|busId
init|=
literal|"cxf"
decl_stmt|;
if|if
condition|(
name|mgr
operator|instanceof
name|WorkQueueManagerImpl
condition|)
block|{
name|busId
operator|=
operator|(
operator|(
name|WorkQueueManagerImpl
operator|)
name|mgr
operator|)
operator|.
name|getBus
argument_list|()
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|DEFAULT_DOMAIN_NAME
operator|+
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|BUS_ID_PROP
operator|+
literal|"="
operator|+
name|busId
operator|+
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|WorkQueueManagerImplMBeanWrapper
operator|.
name|TYPE_VALUE
operator|+
literal|"="
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|WorkQueueManagerImplMBeanWrapper
operator|.
name|NAME_VALUE
operator|+
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|TYPE_PROP
operator|+
literal|"="
operator|+
name|TYPE_VALUE
operator|+
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|NAME_PROP
operator|+
literal|"="
operator|+
name|aWorkQueue
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//Use default domain name of server
return|return
operator|new
name|ObjectName
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

