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
name|util
operator|.
name|SystemPropertyAction
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
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|EventLoopGroup
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|nio
operator|.
name|NioEventLoopGroup
import|;
end_import

begin_class
specifier|public
class|class
name|NettyHttpConduitFactory
implements|implements
name|HTTPConduitFactory
block|{
comment|//CXF specific
specifier|public
specifier|static
specifier|final
name|String
name|USE_POLICY
init|=
literal|"org.apache.cxf.transport.http.netty.usePolicy"
decl_stmt|;
specifier|public
specifier|static
enum|enum
name|UseAsyncPolicy
block|{
name|ALWAYS
block|,
name|ASYNC_ONLY
block|,
name|NEVER
block|}
empty_stmt|;
name|UseAsyncPolicy
name|policy
decl_stmt|;
specifier|public
name|NettyHttpConduitFactory
parameter_list|()
block|{
name|Object
name|st
init|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
name|USE_POLICY
argument_list|)
decl_stmt|;
if|if
condition|(
name|st
operator|instanceof
name|UseAsyncPolicy
condition|)
block|{
name|policy
operator|=
operator|(
name|UseAsyncPolicy
operator|)
name|st
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|st
operator|instanceof
name|String
condition|)
block|{
name|policy
operator|=
name|UseAsyncPolicy
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|st
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//policy = UseAsyncPolicy.ALWAYS;
name|policy
operator|=
name|UseAsyncPolicy
operator|.
name|ASYNC_ONLY
expr_stmt|;
block|}
block|}
specifier|public
name|UseAsyncPolicy
name|getUseAsyncPolicy
parameter_list|()
block|{
return|return
name|policy
return|;
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
name|Bus
name|bus
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
comment|// need to check if the EventLoopGroup is created or not
comment|// if not create a new EventLoopGroup for it
name|EventLoopGroup
name|eventLoopGroup
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|EventLoopGroup
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|eventLoopGroup
operator|==
literal|null
condition|)
block|{
specifier|final
name|EventLoopGroup
name|group
init|=
operator|new
name|NioEventLoopGroup
argument_list|()
decl_stmt|;
comment|// register a BusLifeCycleListener for it
name|bus
operator|.
name|setExtension
argument_list|(
name|group
argument_list|,
name|EventLoopGroup
operator|.
name|class
argument_list|)
expr_stmt|;
name|registerBusLifeListener
argument_list|(
name|bus
argument_list|,
name|group
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|NettyHttpConduit
argument_list|(
name|bus
argument_list|,
name|localInfo
argument_list|,
name|target
argument_list|,
name|this
argument_list|)
return|;
block|}
specifier|public
name|HTTPConduit
name|createConduit
parameter_list|(
name|Bus
name|bus
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
name|createConduit
argument_list|(
literal|null
argument_list|,
name|bus
argument_list|,
name|localInfo
argument_list|,
name|target
argument_list|)
return|;
block|}
specifier|protected
name|void
name|registerBusLifeListener
parameter_list|(
name|Bus
name|bus
parameter_list|,
specifier|final
name|EventLoopGroup
name|group
parameter_list|)
block|{
name|BusLifeCycleManager
name|lifeCycleManager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|lifeCycleManager
condition|)
block|{
name|lifeCycleManager
operator|.
name|registerLifeCycleListener
argument_list|(
operator|new
name|BusLifeCycleListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|initComplete
parameter_list|()
block|{
comment|// do nothing here
block|}
annotation|@
name|Override
specifier|public
name|void
name|preShutdown
parameter_list|()
block|{
comment|// do nothing here
block|}
annotation|@
name|Override
specifier|public
name|void
name|postShutdown
parameter_list|()
block|{
comment|// shutdown the EventLoopGroup
name|group
operator|.
name|shutdownGracefully
argument_list|()
operator|.
name|syncUninterruptibly
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

