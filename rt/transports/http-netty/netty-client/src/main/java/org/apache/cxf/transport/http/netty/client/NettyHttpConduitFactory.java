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
name|transport
operator|.
name|http
operator|.
name|netty
operator|.
name|client
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
name|javax
operator|.
name|annotation
operator|.
name|Resource
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
name|buslifecycle
operator|.
name|BusLifeCycleListener
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
name|buslifecycle
operator|.
name|BusLifeCycleManager
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
name|injection
operator|.
name|NoJSR250Annotations
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
name|http
operator|.
name|HTTPConduit
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
name|http
operator|.
name|HTTPConduitFactory
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
name|http
operator|.
name|HTTPTransportFactory
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
name|addressing
operator|.
name|EndpointReferenceType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|bootstrap
operator|.
name|ClientBootstrap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|socket
operator|.
name|nio
operator|.
name|NioClientSocketChannelFactory
import|;
end_import

begin_class
annotation|@
name|NoJSR250Annotations
argument_list|(
name|unlessNull
operator|=
literal|"bus"
argument_list|)
specifier|public
class|class
name|NettyHttpConduitFactory
implements|implements
name|BusLifeCycleListener
implements|,
name|HTTPConduitFactory
block|{
name|boolean
name|isShutdown
decl_stmt|;
specifier|private
specifier|final
name|ClientBootstrap
name|bootstrap
decl_stmt|;
specifier|public
name|NettyHttpConduitFactory
parameter_list|()
block|{
comment|//TODO setup the bootstrap thread pool according to the configuration
name|bootstrap
operator|=
operator|new
name|ClientBootstrap
argument_list|(
operator|new
name|NioClientSocketChannelFactory
argument_list|()
argument_list|)
expr_stmt|;
name|bootstrap
operator|.
name|setPipelineFactory
argument_list|(
operator|new
name|NettyHttpClientPipelineFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|NettyHttpConduitFactory
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|addListener
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Resource
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|addListener
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addListener
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|b
operator|.
name|getExtension
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
operator|.
name|registerLifeCycleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|HTTPConduit
name|createConduit
parameter_list|(
name|HTTPTransportFactory
name|f
parameter_list|,
name|EndpointInfo
name|localInfo
parameter_list|,
name|EndpointReferenceType
name|target
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|NettyHttpConduit
argument_list|(
name|f
operator|.
name|getBus
argument_list|()
argument_list|,
name|localInfo
argument_list|,
name|target
argument_list|,
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|initComplete
parameter_list|()
block|{
name|isShutdown
operator|=
literal|false
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|preShutdown
parameter_list|()
block|{
name|isShutdown
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|postShutdown
parameter_list|()
block|{
comment|// shutdown the bootstrap
name|bootstrap
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|isShutdown
parameter_list|()
block|{
return|return
name|isShutdown
return|;
block|}
specifier|public
name|ClientBootstrap
name|getBootstrap
parameter_list|()
block|{
return|return
name|bootstrap
return|;
block|}
block|}
end_class

end_unit

