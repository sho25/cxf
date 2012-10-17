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
name|ws
operator|.
name|discovery
operator|.
name|listeners
package|;
end_package

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
name|BusFactory
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
name|endpoint
operator|.
name|Server
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
name|endpoint
operator|.
name|ServerLifeCycleListener
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|service
operator|.
name|model
operator|.
name|InterfaceInfo
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
name|ws
operator|.
name|discovery
operator|.
name|internal
operator|.
name|WSDiscoveryServiceImpl
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|WSDiscoveryServerListener
implements|implements
name|ServerLifeCycleListener
block|{
specifier|final
name|Bus
name|bus
decl_stmt|;
specifier|volatile
name|WSDiscoveryServiceImpl
name|service
decl_stmt|;
specifier|private
specifier|static
specifier|final
class|class
name|WSDiscoveryServiceImplHolder
block|{
specifier|private
specifier|static
specifier|final
name|WSDiscoveryServiceImpl
name|INSTANCE
decl_stmt|;
static|static
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createBus
argument_list|()
decl_stmt|;
name|INSTANCE
operator|=
operator|new
name|WSDiscoveryServiceImpl
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|WSDiscoveryServerListener
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
block|}
specifier|private
specifier|synchronized
name|WSDiscoveryServiceImpl
name|getService
parameter_list|()
block|{
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
name|service
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|WSDiscoveryServiceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
name|service
operator|=
name|getStaticService
argument_list|()
expr_stmt|;
name|bus
operator|.
name|setExtension
argument_list|(
name|service
argument_list|,
name|WSDiscoveryServiceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|service
return|;
block|}
specifier|private
specifier|static
name|WSDiscoveryServiceImpl
name|getStaticService
parameter_list|()
block|{
return|return
name|WSDiscoveryServiceImplHolder
operator|.
name|INSTANCE
return|;
block|}
specifier|public
name|void
name|startServer
parameter_list|(
name|Server
name|server
parameter_list|)
block|{
name|QName
name|sn
init|=
name|getServiceQName
argument_list|(
name|server
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"http://docs.oasis-open.org/ws-dd/ns/discovery/2009/01"
operator|.
name|equals
argument_list|(
name|sn
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
name|getService
argument_list|()
operator|.
name|serverStarted
argument_list|(
name|server
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|stopServer
parameter_list|(
name|Server
name|server
parameter_list|)
block|{
name|QName
name|sn
init|=
name|getServiceQName
argument_list|(
name|server
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"http://docs.oasis-open.org/ws-dd/ns/discovery/2009/01"
operator|.
name|equals
argument_list|(
name|sn
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
name|getService
argument_list|()
operator|.
name|serverStopped
argument_list|(
name|server
argument_list|)
expr_stmt|;
block|}
specifier|private
name|QName
name|getServiceQName
parameter_list|(
name|Server
name|server
parameter_list|)
block|{
name|EndpointInfo
name|ei
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
name|InterfaceInfo
name|ii
init|=
name|ei
operator|.
name|getInterface
argument_list|()
decl_stmt|;
if|if
condition|(
name|ii
operator|!=
literal|null
condition|)
block|{
return|return
name|ii
operator|.
name|getName
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|ei
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

