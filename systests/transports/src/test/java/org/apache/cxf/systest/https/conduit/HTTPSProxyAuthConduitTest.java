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
name|systest
operator|.
name|https
operator|.
name|conduit
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|configuration
operator|.
name|security
operator|.
name|ProxyAuthorizationPolicy
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
name|transports
operator|.
name|http
operator|.
name|configuration
operator|.
name|HTTPClientPolicy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|littleshoot
operator|.
name|proxy
operator|.
name|ActivityTrackerAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|littleshoot
operator|.
name|proxy
operator|.
name|FlowContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|littleshoot
operator|.
name|proxy
operator|.
name|HttpProxyServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|littleshoot
operator|.
name|proxy
operator|.
name|ProxyAuthenticator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|littleshoot
operator|.
name|proxy
operator|.
name|impl
operator|.
name|DefaultHttpProxyServer
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpMethod
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|HTTPSProxyAuthConduitTest
extends|extends
name|HTTPSConduitTest
block|{
specifier|static
specifier|final
name|int
name|PROXY_PORT
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|allocatePort
argument_list|(
name|HTTPSProxyAuthConduitTest
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
specifier|static
name|HttpProxyServer
name|proxy
decl_stmt|;
specifier|static
name|CountingFilter
name|requestFilter
init|=
operator|new
name|CountingFilter
argument_list|()
decl_stmt|;
specifier|static
class|class
name|CountingFilter
extends|extends
name|ActivityTrackerAdapter
block|{
name|AtomicInteger
name|count
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
specifier|public
name|void
name|requestReceivedFromClient
parameter_list|(
name|FlowContext
name|flowContext
parameter_list|,
name|HttpRequest
name|httpRequest
parameter_list|)
block|{
if|if
condition|(
name|httpRequest
operator|.
name|getMethod
argument_list|()
operator|!=
name|HttpMethod
operator|.
name|CONNECT
condition|)
block|{
name|count
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|count
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getCount
parameter_list|()
block|{
return|return
name|count
operator|.
name|get
argument_list|()
return|;
block|}
block|}
specifier|public
name|HTTPSProxyAuthConduitTest
parameter_list|()
block|{     }
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|stopProxy
parameter_list|()
block|{
name|proxy
operator|.
name|stop
argument_list|()
expr_stmt|;
name|proxy
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startProxy
parameter_list|()
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"jdk.http.auth.tunneling.disabledSchemes"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|proxy
operator|=
name|DefaultHttpProxyServer
operator|.
name|bootstrap
argument_list|()
operator|.
name|withPort
argument_list|(
name|PROXY_PORT
argument_list|)
operator|.
name|withProxyAuthenticator
argument_list|(
operator|new
name|ProxyAuthenticator
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|authenticate
parameter_list|(
name|String
name|userName
parameter_list|,
name|String
name|password
parameter_list|)
block|{
return|return
literal|"password"
operator|.
name|equals
argument_list|(
name|password
argument_list|)
operator|&&
literal|"CXF"
operator|.
name|equals
argument_list|(
name|userName
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRealm
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
argument_list|)
operator|.
name|plusActivityTracker
argument_list|(
name|requestFilter
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|resetCount
parameter_list|()
block|{
name|requestFilter
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|configureProxy
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
name|HTTPConduit
name|cond
init|=
operator|(
name|HTTPConduit
operator|)
name|client
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|HTTPClientPolicy
name|pol
init|=
name|cond
operator|.
name|getClient
argument_list|()
decl_stmt|;
if|if
condition|(
name|pol
operator|==
literal|null
condition|)
block|{
name|pol
operator|=
operator|new
name|HTTPClientPolicy
argument_list|()
expr_stmt|;
name|cond
operator|.
name|setClient
argument_list|(
name|pol
argument_list|)
expr_stmt|;
block|}
name|pol
operator|.
name|setProxyServer
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|pol
operator|.
name|setProxyServerPort
argument_list|(
name|PROXY_PORT
argument_list|)
expr_stmt|;
name|ProxyAuthorizationPolicy
name|auth
init|=
operator|new
name|ProxyAuthorizationPolicy
argument_list|()
decl_stmt|;
name|auth
operator|.
name|setUserName
argument_list|(
literal|"CXF"
argument_list|)
expr_stmt|;
name|auth
operator|.
name|setPassword
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
name|cond
operator|.
name|setProxyAuthorization
argument_list|(
name|auth
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|resetProxyCount
parameter_list|()
block|{
name|requestFilter
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|assertProxyRequestCount
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Unexpected request count"
argument_list|,
name|i
argument_list|,
name|requestFilter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

