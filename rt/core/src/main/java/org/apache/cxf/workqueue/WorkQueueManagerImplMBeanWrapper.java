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
name|Bus
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
name|ManagedOperation
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
literal|"WorkQueueManager"
argument_list|,
name|description
operator|=
literal|"The CXF manangement of work queues "
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
name|WorkQueueManagerImplMBeanWrapper
implements|implements
name|ManagedComponent
block|{
specifier|static
specifier|final
name|String
name|NAME_VALUE
init|=
literal|"Bus.WorkQueueManager"
decl_stmt|;
specifier|static
specifier|final
name|String
name|TYPE_VALUE
init|=
literal|"WorkQueueManager"
decl_stmt|;
specifier|private
name|WorkQueueManagerImpl
name|wqManager
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|public
name|WorkQueueManagerImplMBeanWrapper
parameter_list|(
name|WorkQueueManagerImpl
name|wq
parameter_list|)
block|{
name|wqManager
operator|=
name|wq
expr_stmt|;
name|bus
operator|=
name|wq
operator|.
name|getBus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|currencyTimeLimit
operator|=
literal|30
argument_list|)
specifier|public
name|void
name|shutdown
parameter_list|(
name|boolean
name|processRemainingWorkItems
parameter_list|)
block|{
name|wqManager
operator|.
name|shutdown
argument_list|(
name|processRemainingWorkItems
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
argument_list|)
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|BUS_ID_PROP
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|bus
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"WorkQueueManager="
argument_list|)
operator|.
name|append
argument_list|(
name|NAME_VALUE
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|TYPE_PROP
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|TYPE_VALUE
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

