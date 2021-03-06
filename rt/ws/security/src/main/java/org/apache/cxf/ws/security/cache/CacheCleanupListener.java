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
name|security
operator|.
name|cache
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

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
name|endpoint
operator|.
name|Client
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
name|ClientLifeCycleListener
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
name|ws
operator|.
name|security
operator|.
name|SecurityConstants
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
name|security
operator|.
name|tokenstore
operator|.
name|TokenStore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|cache
operator|.
name|ReplayCache
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|CacheCleanupListener
implements|implements
name|ServerLifeCycleListener
implements|,
name|ClientLifeCycleListener
block|{
specifier|public
name|CacheCleanupListener
parameter_list|()
block|{     }
specifier|public
name|void
name|clientCreated
parameter_list|(
name|Client
name|client
parameter_list|)
block|{     }
specifier|public
name|void
name|startServer
parameter_list|(
name|Server
name|server
parameter_list|)
block|{     }
specifier|public
name|void
name|clientDestroyed
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
name|shutdownResources
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
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
name|shutdownResources
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|shutdownResources
parameter_list|(
name|EndpointInfo
name|info
parameter_list|)
block|{
name|TokenStore
name|ts
init|=
operator|(
name|TokenStore
operator|)
name|info
operator|.
name|getProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_STORE_CACHE_INSTANCE
argument_list|)
decl_stmt|;
if|if
condition|(
name|ts
operator|instanceof
name|Closeable
condition|)
block|{
name|close
argument_list|(
operator|(
name|Closeable
operator|)
name|ts
argument_list|)
expr_stmt|;
block|}
name|ReplayCache
name|rc
init|=
operator|(
name|ReplayCache
operator|)
name|info
operator|.
name|getProperty
argument_list|(
name|SecurityConstants
operator|.
name|NONCE_CACHE_INSTANCE
argument_list|)
decl_stmt|;
if|if
condition|(
name|rc
operator|!=
literal|null
condition|)
block|{
name|close
argument_list|(
name|rc
argument_list|)
expr_stmt|;
block|}
name|rc
operator|=
operator|(
name|ReplayCache
operator|)
name|info
operator|.
name|getProperty
argument_list|(
name|SecurityConstants
operator|.
name|TIMESTAMP_CACHE_INSTANCE
argument_list|)
expr_stmt|;
if|if
condition|(
name|rc
operator|!=
literal|null
condition|)
block|{
name|close
argument_list|(
name|rc
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|close
parameter_list|(
name|Closeable
name|ts
parameter_list|)
block|{
try|try
block|{
name|ts
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|//ignore, we're shutting down and nothing we can do
block|}
block|}
block|}
end_class

end_unit

