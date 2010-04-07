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
name|endpoint
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
literal|"Endpoint"
argument_list|,
name|description
operator|=
literal|"Responsible for managing server instances."
argument_list|)
specifier|public
class|class
name|ManagedEndpoint
implements|implements
name|ManagedComponent
implements|,
name|ServerLifeCycleListener
block|{
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
specifier|private
name|Server
name|server
decl_stmt|;
specifier|private
enum|enum
name|State
block|{
name|CREATED
block|,
name|STARTED
block|,
name|STOPPED
block|}
empty_stmt|;
specifier|private
name|State
name|state
init|=
name|State
operator|.
name|CREATED
decl_stmt|;
specifier|public
name|ManagedEndpoint
parameter_list|(
name|Bus
name|b
parameter_list|,
name|Endpoint
name|ep
parameter_list|,
name|Server
name|s
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
name|endpoint
operator|=
name|ep
expr_stmt|;
name|server
operator|=
name|s
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
specifier|public
name|void
name|start
parameter_list|()
block|{
if|if
condition|(
name|state
operator|==
name|State
operator|.
name|STARTED
condition|)
block|{
return|return;
block|}
name|ServerLifeCycleManager
name|mgr
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ServerLifeCycleManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|mgr
operator|!=
literal|null
condition|)
block|{
name|mgr
operator|.
name|registerListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
specifier|public
name|void
name|destroy
parameter_list|()
block|{
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Address Attribute"
argument_list|,
name|currencyTimeLimit
operator|=
literal|60
argument_list|)
specifier|public
name|String
name|getAddress
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"TransportId Attribute"
argument_list|,
name|currencyTimeLimit
operator|=
literal|60
argument_list|)
specifier|public
name|String
name|getTransportId
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Server State"
argument_list|)
specifier|public
name|String
name|getState
parameter_list|()
block|{
return|return
name|state
operator|.
name|toString
argument_list|()
return|;
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
name|ManagementConstants
operator|.
name|TYPE_PROP
operator|+
literal|"="
operator|+
literal|"Bus.Service.Endpoint,"
argument_list|)
expr_stmt|;
name|String
name|serviceName
init|=
name|ObjectName
operator|.
name|quote
argument_list|(
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|SERVICE_NAME_PROP
operator|+
literal|"="
operator|+
name|serviceName
operator|+
literal|","
argument_list|)
expr_stmt|;
name|String
name|endpointName
init|=
name|ObjectName
operator|.
name|quote
argument_list|(
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|PORT_NAME_PROP
operator|+
literal|"="
operator|+
name|endpointName
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
specifier|public
name|void
name|startServer
parameter_list|(
name|Server
name|s
parameter_list|)
block|{
if|if
condition|(
name|server
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|state
operator|=
name|State
operator|.
name|STARTED
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|stopServer
parameter_list|(
name|Server
name|s
parameter_list|)
block|{
if|if
condition|(
name|server
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|state
operator|=
name|State
operator|.
name|STOPPED
expr_stmt|;
comment|// unregister server to avoid the memory leak
name|ServerLifeCycleManager
name|mgr
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ServerLifeCycleManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|mgr
operator|!=
literal|null
condition|)
block|{
name|mgr
operator|.
name|unRegisterListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

