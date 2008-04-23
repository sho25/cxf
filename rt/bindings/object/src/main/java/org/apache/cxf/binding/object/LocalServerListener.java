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
name|binding
operator|.
name|object
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
name|Binding
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
name|endpoint
operator|.
name|Endpoint
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
name|Service
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
name|BindingInfo
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
name|ChainInitiationObserver
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
name|local
operator|.
name|LocalTransportFactory
import|;
end_import

begin_class
specifier|public
class|class
name|LocalServerListener
implements|implements
name|ServerLifeCycleListener
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
name|LocalServerListener
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|DestinationFactory
name|destinationFactory
decl_stmt|;
specifier|private
name|BindingFactory
name|bindingFactory
decl_stmt|;
specifier|private
name|ObjectBindingConfiguration
name|configuration
init|=
operator|new
name|ObjectBindingConfiguration
argument_list|()
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|public
name|LocalServerListener
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|BindingFactory
name|bindingFactory
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|bindingFactory
operator|=
name|bindingFactory
expr_stmt|;
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
block|}
specifier|public
name|void
name|startServer
parameter_list|(
name|Server
name|server
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|Service
name|service
init|=
name|endpoint
operator|.
name|getService
argument_list|()
decl_stmt|;
comment|// synthesize a new binding
name|BindingInfo
name|bi
init|=
name|bindingFactory
operator|.
name|createBindingInfo
argument_list|(
name|service
argument_list|,
name|ObjectBindingFactory
operator|.
name|BINDING_ID
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|Binding
name|binding
init|=
name|bindingFactory
operator|.
name|createBinding
argument_list|(
name|bi
argument_list|)
decl_stmt|;
name|String
name|uri
init|=
literal|"local://"
operator|+
name|server
operator|.
name|toString
argument_list|()
decl_stmt|;
name|EndpointInfo
name|ei
init|=
operator|new
name|EndpointInfo
argument_list|()
decl_stmt|;
name|ei
operator|.
name|setAddress
argument_list|(
name|uri
argument_list|)
expr_stmt|;
try|try
block|{
comment|// Register a new Destination locally for the Server
name|Destination
name|destination
init|=
name|getDestinationFactory
argument_list|()
operator|.
name|getDestination
argument_list|(
name|ei
argument_list|)
decl_stmt|;
name|destination
operator|.
name|setMessageObserver
argument_list|(
operator|new
name|OverrideBindingObserver
argument_list|(
name|endpoint
argument_list|,
name|binding
argument_list|,
name|bus
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e1
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
literal|"Could not create local destination."
argument_list|,
name|e1
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|stopServer
parameter_list|(
name|Server
name|server
parameter_list|)
block|{
name|String
name|uri
init|=
literal|"local://"
operator|+
name|server
operator|.
name|toString
argument_list|()
decl_stmt|;
name|EndpointInfo
name|ei
init|=
operator|new
name|EndpointInfo
argument_list|()
decl_stmt|;
name|ei
operator|.
name|setAddress
argument_list|(
name|uri
argument_list|)
expr_stmt|;
try|try
block|{
name|Destination
name|destination
init|=
name|getDestinationFactory
argument_list|()
operator|.
name|getDestination
argument_list|(
name|ei
argument_list|)
decl_stmt|;
name|destination
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
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
literal|"Could not shutdown local destination."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|DestinationFactory
name|getDestinationFactory
parameter_list|()
block|{
if|if
condition|(
name|destinationFactory
operator|==
literal|null
condition|)
block|{
name|retrieveDF
argument_list|()
expr_stmt|;
block|}
return|return
name|destinationFactory
return|;
block|}
specifier|private
specifier|synchronized
name|void
name|retrieveDF
parameter_list|()
block|{
if|if
condition|(
name|destinationFactory
operator|==
literal|null
condition|)
block|{
name|DestinationFactoryManager
name|dfm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|destinationFactory
operator|=
name|dfm
operator|.
name|getDestinationFactory
argument_list|(
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BusException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
specifier|public
name|ObjectBindingConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|ObjectBindingConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|OverrideBindingObserver
extends|extends
name|ChainInitiationObserver
block|{
specifier|private
name|Binding
name|binding
decl_stmt|;
specifier|public
name|OverrideBindingObserver
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Binding
name|binding
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|bus
argument_list|)
expr_stmt|;
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Binding
name|getBinding
parameter_list|()
block|{
return|return
name|binding
return|;
block|}
block|}
block|}
end_class

end_unit

