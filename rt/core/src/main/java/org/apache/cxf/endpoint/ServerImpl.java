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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|management
operator|.
name|JMException
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
name|BusException
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
name|binding
operator|.
name|BindingFactory
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
name|management
operator|.
name|InstrumentationManager
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
name|transport
operator|.
name|Destination
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
name|transport
operator|.
name|DestinationFactory
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
name|transport
operator|.
name|DestinationFactoryManager
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
name|transport
operator|.
name|MessageObserver
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
name|transport
operator|.
name|MultipleEndpointObserver
import|;
end_import

begin_class
specifier|public
class|class
name|ServerImpl
implements|implements
name|Server
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|ServerImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Destination
name|destination
decl_stmt|;
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
specifier|private
name|ServerRegistry
name|serverRegistry
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|ServerLifeCycleManager
name|slcMgr
decl_stmt|;
specifier|private
name|InstrumentationManager
name|iMgr
decl_stmt|;
specifier|private
name|BindingFactory
name|bindingFactory
decl_stmt|;
specifier|private
name|MessageObserver
name|messageObserver
decl_stmt|;
specifier|private
name|ManagedEndpoint
name|mep
decl_stmt|;
specifier|public
name|ServerImpl
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|DestinationFactory
name|destinationFactory
parameter_list|,
name|MessageObserver
name|observer
parameter_list|)
throws|throws
name|BusException
throws|,
name|IOException
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
name|this
operator|.
name|messageObserver
operator|=
name|observer
expr_stmt|;
name|initDestination
argument_list|(
name|destinationFactory
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ServerImpl
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|DestinationFactory
name|destinationFactory
parameter_list|,
name|BindingFactory
name|bindingFactory
parameter_list|)
throws|throws
name|BusException
throws|,
name|IOException
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
name|this
operator|.
name|bindingFactory
operator|=
name|bindingFactory
expr_stmt|;
name|initDestination
argument_list|(
name|destinationFactory
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initDestination
parameter_list|(
name|DestinationFactory
name|destinationFactory
parameter_list|)
throws|throws
name|BusException
throws|,
name|IOException
block|{
name|EndpointInfo
name|ei
init|=
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
comment|//Treat local transport as a special case, transports loaded by transportId can be replaced
comment|//by local transport when the publishing address is a local transport protocol.
comment|//Of course its not an ideal situation here to use a hard-coded prefix. To be refactored.
if|if
condition|(
name|destinationFactory
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|ei
operator|.
name|getAddress
argument_list|()
operator|!=
literal|null
operator|&&
name|ei
operator|.
name|getAddress
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"local://"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|destinationFactory
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
operator|.
name|getDestinationFactoryForUri
argument_list|(
name|ei
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|destinationFactory
operator|==
literal|null
condition|)
block|{
name|destinationFactory
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
operator|.
name|getDestinationFactory
argument_list|(
name|ei
operator|.
name|getTransportId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|destination
operator|=
name|destinationFactory
operator|.
name|getDestination
argument_list|(
name|ei
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Setting the server's publish address to be "
operator|+
name|ei
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|serverRegistry
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ServerRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|mep
operator|=
operator|new
name|ManagedEndpoint
argument_list|(
name|bus
argument_list|,
name|endpoint
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|slcMgr
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ServerLifeCycleManager
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|slcMgr
operator|!=
literal|null
condition|)
block|{
name|slcMgr
operator|.
name|registerListener
argument_list|(
name|mep
argument_list|)
expr_stmt|;
block|}
name|iMgr
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|InstrumentationManager
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|iMgr
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|iMgr
operator|.
name|register
argument_list|(
name|mep
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMException
name|jmex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Registering ManagedEndpoint failed."
argument_list|,
name|jmex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|Destination
name|getDestination
parameter_list|()
block|{
return|return
name|destination
return|;
block|}
specifier|public
name|void
name|setDestination
parameter_list|(
name|Destination
name|destination
parameter_list|)
block|{
name|this
operator|.
name|destination
operator|=
name|destination
expr_stmt|;
block|}
specifier|public
name|void
name|start
parameter_list|()
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Server is starting."
argument_list|)
expr_stmt|;
if|if
condition|(
name|messageObserver
operator|!=
literal|null
condition|)
block|{
name|destination
operator|.
name|setMessageObserver
argument_list|(
name|messageObserver
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bindingFactory
operator|.
name|addListener
argument_list|(
name|destination
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
comment|// register the active server to run
if|if
condition|(
literal|null
operator|!=
name|serverRegistry
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"register the server to serverRegistry "
argument_list|)
expr_stmt|;
name|serverRegistry
operator|.
name|register
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|slcMgr
operator|!=
literal|null
condition|)
block|{
name|slcMgr
operator|.
name|startServer
argument_list|(
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
name|LOG
operator|.
name|fine
argument_list|(
literal|"Server is stopping."
argument_list|)
expr_stmt|;
if|if
condition|(
name|slcMgr
operator|!=
literal|null
condition|)
block|{
name|slcMgr
operator|.
name|stopServer
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|serverRegistry
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"unregister the server to serverRegistry "
argument_list|)
expr_stmt|;
name|serverRegistry
operator|.
name|unregister
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|MessageObserver
name|mo
init|=
name|getDestination
argument_list|()
operator|.
name|getMessageObserver
argument_list|()
decl_stmt|;
if|if
condition|(
name|mo
operator|instanceof
name|MultipleEndpointObserver
condition|)
block|{
operator|(
operator|(
name|MultipleEndpointObserver
operator|)
name|mo
operator|)
operator|.
name|getEndpoints
argument_list|()
operator|.
name|remove
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
operator|(
operator|(
name|MultipleEndpointObserver
operator|)
name|mo
operator|)
operator|.
name|getEndpoints
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
block|}
name|getDestination
argument_list|()
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|getDestination
argument_list|()
operator|.
name|setMessageObserver
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{
name|stop
argument_list|()
expr_stmt|;
if|if
condition|(
name|iMgr
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|iMgr
operator|.
name|unregister
argument_list|(
name|mep
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMException
name|jmex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Unregistering ManagedEndpoint failed."
argument_list|,
name|jmex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
specifier|public
name|MessageObserver
name|getMessageObserver
parameter_list|()
block|{
return|return
name|messageObserver
return|;
block|}
specifier|public
name|void
name|setMessageObserver
parameter_list|(
name|MessageObserver
name|messageObserver
parameter_list|)
block|{
name|this
operator|.
name|messageObserver
operator|=
name|messageObserver
expr_stmt|;
block|}
block|}
end_class

end_unit

