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
name|jca
operator|.
name|cxf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|resource
operator|.
name|NotSupportedException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|ResourceException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|ActivationSpec
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|BootstrapContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|ResourceAdapter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|ResourceAdapterInternalException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|endpoint
operator|.
name|MessageEndpointFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|work
operator|.
name|Work
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|xa
operator|.
name|XAResource
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
name|jca
operator|.
name|core
operator|.
name|resourceadapter
operator|.
name|ResourceBean
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
name|jca
operator|.
name|inbound
operator|.
name|InboundEndpoint
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
name|jca
operator|.
name|inbound
operator|.
name|MDBActivationSpec
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
name|jca
operator|.
name|inbound
operator|.
name|MDBActivationWork
import|;
end_import

begin_class
specifier|public
class|class
name|ResourceAdapterImpl
extends|extends
name|ResourceBean
implements|implements
name|ResourceAdapter
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|5318740621610762307L
decl_stmt|;
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
name|ResourceAdapterImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|BootstrapContext
name|ctx
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|Bus
argument_list|>
name|busCache
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|InboundEndpoint
argument_list|>
name|endpoints
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|InboundEndpoint
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|ResourceAdapterImpl
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|ResourceAdapterImpl
parameter_list|(
name|Properties
name|props
parameter_list|)
block|{
name|super
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|registerBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Bus "
operator|+
name|bus
operator|+
literal|" initialized and added to ResourceAdapter busCache"
argument_list|)
expr_stmt|;
name|busCache
operator|.
name|add
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Set
argument_list|<
name|Bus
argument_list|>
name|getBusCache
parameter_list|()
block|{
return|return
name|busCache
return|;
block|}
specifier|protected
name|void
name|setBusCache
parameter_list|(
name|Set
argument_list|<
name|Bus
argument_list|>
name|cache
parameter_list|)
block|{
name|this
operator|.
name|busCache
operator|=
name|cache
expr_stmt|;
block|}
specifier|public
name|void
name|start
parameter_list|(
name|BootstrapContext
name|aCtx
parameter_list|)
throws|throws
name|ResourceAdapterInternalException
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Resource Adapter is starting by appserver..."
argument_list|)
expr_stmt|;
if|if
condition|(
name|aCtx
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceAdapterInternalException
argument_list|(
literal|"BootstrapContext can not be null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|ctx
operator|=
name|aCtx
expr_stmt|;
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
literal|"Resource Adapter is being stopped by appserver..."
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|busCache
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Bus
name|bus
range|:
name|busCache
control|)
block|{
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
comment|// shutdown all the inbound endpoints
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|InboundEndpoint
argument_list|>
name|entry
range|:
name|endpoints
operator|.
name|entrySet
argument_list|()
control|)
block|{
try|try
block|{
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
literal|"Failed to stop endpoint "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|endpoints
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|XAResource
index|[]
name|getXAResources
parameter_list|(
name|ActivationSpec
index|[]
name|as
parameter_list|)
throws|throws
name|ResourceException
block|{
throw|throw
operator|new
name|NotSupportedException
argument_list|()
throw|;
block|}
specifier|public
name|void
name|endpointActivation
parameter_list|(
name|MessageEndpointFactory
name|mef
parameter_list|,
name|ActivationSpec
name|as
parameter_list|)
throws|throws
name|ResourceException
block|{
if|if
condition|(
operator|!
operator|(
name|as
operator|instanceof
name|MDBActivationSpec
operator|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Ignored unknown activation spec "
operator|+
name|as
argument_list|)
expr_stmt|;
return|return;
block|}
name|MDBActivationSpec
name|spec
init|=
operator|(
name|MDBActivationSpec
operator|)
name|as
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"CXF resource adapter is activating "
operator|+
name|spec
operator|.
name|getDisplayName
argument_list|()
argument_list|)
expr_stmt|;
name|Work
name|work
init|=
operator|new
name|MDBActivationWork
argument_list|(
name|spec
argument_list|,
name|mef
argument_list|,
name|endpoints
argument_list|)
decl_stmt|;
name|ctx
operator|.
name|getWorkManager
argument_list|()
operator|.
name|scheduleWork
argument_list|(
name|work
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|endpointDeactivation
parameter_list|(
name|MessageEndpointFactory
name|mef
parameter_list|,
name|ActivationSpec
name|as
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|as
operator|instanceof
name|MDBActivationSpec
operator|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Ignored unknown activation spec "
operator|+
name|as
argument_list|)
expr_stmt|;
return|return;
block|}
name|MDBActivationSpec
name|spec
init|=
operator|(
name|MDBActivationSpec
operator|)
name|as
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"CXF resource adapter is deactivating "
operator|+
name|spec
operator|.
name|getDisplayName
argument_list|()
argument_list|)
expr_stmt|;
name|InboundEndpoint
name|endpoint
init|=
name|endpoints
operator|.
name|remove
argument_list|(
name|spec
operator|.
name|getDisplayName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|endpoint
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
literal|"Failed to stop endpoint "
operator|+
name|spec
operator|.
name|getDisplayName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|BootstrapContext
name|getBootstrapContext
parameter_list|()
block|{
return|return
name|ctx
return|;
block|}
block|}
end_class

end_unit

