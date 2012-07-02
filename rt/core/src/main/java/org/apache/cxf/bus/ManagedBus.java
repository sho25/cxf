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
name|bus
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
literal|"Bus"
argument_list|,
name|description
operator|=
literal|"Responsible for managing services."
argument_list|)
specifier|public
class|class
name|ManagedBus
implements|implements
name|ManagedComponent
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TYPE_VALUE
init|=
literal|"Bus"
decl_stmt|;
specifier|private
specifier|final
name|Bus
name|bus
decl_stmt|;
specifier|public
name|ManagedBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
specifier|public
name|void
name|shutdown
parameter_list|(
name|boolean
name|wait
parameter_list|)
block|{
name|bus
operator|.
name|shutdown
argument_list|(
name|wait
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
name|String
name|busId
init|=
name|bus
operator|.
name|getId
argument_list|()
decl_stmt|;
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
name|ManagementConstants
operator|.
name|DEFAULT_DOMAIN_NAME
operator|+
literal|":"
argument_list|)
decl_stmt|;
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
comment|// Added the instance id to make the ObjectName unique
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|INSTANCE_ID_PROP
operator|+
literal|"="
operator|+
name|bus
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
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

