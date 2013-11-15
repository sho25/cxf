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
name|testutil
operator|.
name|common
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
name|lang
operator|.
name|reflect
operator|.
name|InvocationHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Proxy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingProvider
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
name|ReflectionUtil
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
name|Assert
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractClientServerTestBase
extends|extends
name|Assert
block|{
specifier|private
specifier|static
name|List
argument_list|<
name|ServerLauncher
argument_list|>
name|launchers
init|=
operator|new
name|ArrayList
argument_list|<
name|ServerLauncher
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|stopAllServers
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|passed
init|=
literal|true
decl_stmt|;
for|for
control|(
name|ServerLauncher
name|sl
range|:
name|launchers
control|)
block|{
try|try
block|{
name|sl
operator|.
name|signalStop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
for|for
control|(
name|ServerLauncher
name|sl
range|:
name|launchers
control|)
block|{
try|try
block|{
name|passed
operator|=
name|passed
operator|&&
name|sl
operator|.
name|stopServer
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
name|launchers
operator|.
name|clear
argument_list|()
expr_stmt|;
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server failed"
argument_list|,
name|passed
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|boolean
name|launchServer
parameter_list|(
name|AbstractTestServerBase
name|base
parameter_list|)
block|{
name|boolean
name|ok
init|=
literal|false
decl_stmt|;
try|try
block|{
name|ServerLauncher
name|sl
init|=
operator|new
name|ServerLauncher
argument_list|(
name|base
argument_list|)
decl_stmt|;
name|ok
operator|=
name|sl
operator|.
name|launchServer
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server failed to launch"
argument_list|,
name|ok
argument_list|)
expr_stmt|;
name|launchers
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|sl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"failed to launch server "
operator|+
name|base
argument_list|)
expr_stmt|;
block|}
return|return
name|ok
return|;
block|}
specifier|public
specifier|static
name|boolean
name|launchServer
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|)
block|{
name|boolean
name|ok
init|=
literal|false
decl_stmt|;
try|try
block|{
name|ServerLauncher
name|sl
init|=
operator|new
name|ServerLauncher
argument_list|(
name|clz
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|ok
operator|=
name|sl
operator|.
name|launchServer
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server failed to launch"
argument_list|,
name|ok
argument_list|)
expr_stmt|;
name|launchers
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|sl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"failed to launch server "
operator|+
name|clz
argument_list|)
expr_stmt|;
block|}
return|return
name|ok
return|;
block|}
specifier|public
specifier|static
name|boolean
name|launchServer
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|,
name|boolean
name|inProcess
parameter_list|)
block|{
name|boolean
name|ok
init|=
literal|false
decl_stmt|;
try|try
block|{
name|ServerLauncher
name|sl
init|=
operator|new
name|ServerLauncher
argument_list|(
name|clz
operator|.
name|getName
argument_list|()
argument_list|,
name|inProcess
argument_list|)
decl_stmt|;
name|ok
operator|=
name|sl
operator|.
name|launchServer
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server failed to launch"
argument_list|,
name|ok
argument_list|)
expr_stmt|;
name|launchers
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|sl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"failed to launch server "
operator|+
name|clz
argument_list|)
expr_stmt|;
block|}
return|return
name|ok
return|;
block|}
specifier|public
specifier|static
name|boolean
name|launchServer
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
parameter_list|,
name|String
index|[]
name|args
parameter_list|)
block|{
return|return
name|launchServer
argument_list|(
name|clz
argument_list|,
name|props
argument_list|,
name|args
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|launchServer
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
parameter_list|,
name|String
index|[]
name|args
parameter_list|,
name|boolean
name|inProcess
parameter_list|)
block|{
name|boolean
name|ok
init|=
literal|false
decl_stmt|;
try|try
block|{
name|ServerLauncher
name|sl
init|=
operator|new
name|ServerLauncher
argument_list|(
name|clz
operator|.
name|getName
argument_list|()
argument_list|,
name|props
argument_list|,
name|args
argument_list|,
name|inProcess
argument_list|)
decl_stmt|;
name|ok
operator|=
name|sl
operator|.
name|launchServer
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server failed to launch"
argument_list|,
name|ok
argument_list|)
expr_stmt|;
name|launchers
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|sl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"failed to launch server "
operator|+
name|clz
argument_list|)
expr_stmt|;
block|}
return|return
name|ok
return|;
block|}
comment|// extra methods to help support the dynamic port allocations
specifier|protected
name|void
name|setAddress
parameter_list|(
name|Object
name|o
parameter_list|,
name|String
name|address
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|BindingProvider
condition|)
block|{
operator|(
operator|(
name|BindingProvider
operator|)
name|o
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
name|address
argument_list|)
expr_stmt|;
block|}
name|Client
name|c
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Client
condition|)
block|{
name|c
operator|=
operator|(
name|Client
operator|)
name|o
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|InvocationHandler
name|i
init|=
name|Proxy
operator|.
name|getInvocationHandler
argument_list|(
name|o
argument_list|)
decl_stmt|;
name|c
operator|=
operator|(
name|Client
operator|)
name|i
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getClient"
argument_list|)
operator|.
name|invoke
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
block|}
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
try|try
block|{
specifier|final
name|Method
name|m
init|=
name|o
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredMethod
argument_list|(
literal|"getClient"
argument_list|)
decl_stmt|;
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|c
operator|=
operator|(
name|Client
operator|)
name|m
operator|.
name|invoke
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
block|}
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|updateAddressPort
parameter_list|(
name|Object
name|o
parameter_list|,
name|String
name|port
parameter_list|)
throws|throws
name|NumberFormatException
throws|,
name|MalformedURLException
block|{
name|updateAddressPort
argument_list|(
name|o
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|updateAddressPort
parameter_list|(
name|Object
name|o
parameter_list|,
name|int
name|port
parameter_list|)
throws|throws
name|MalformedURLException
block|{
name|String
name|address
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|BindingProvider
condition|)
block|{
name|address
operator|=
operator|(
operator|(
name|BindingProvider
operator|)
name|o
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|get
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|Client
condition|)
block|{
name|Client
name|c
init|=
operator|(
name|Client
operator|)
name|o
decl_stmt|;
name|address
operator|=
name|c
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|address
operator|!=
literal|null
condition|)
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|address
argument_list|)
decl_stmt|;
name|url
operator|=
operator|new
name|URL
argument_list|(
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|,
name|url
operator|.
name|getHost
argument_list|()
argument_list|,
name|port
argument_list|,
name|url
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
name|setAddress
argument_list|(
name|o
argument_list|,
name|url
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//maybe simple frontend proxy?
block|}
specifier|protected
specifier|static
name|String
name|allocatePort
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|s
argument_list|)
return|;
block|}
specifier|protected
specifier|static
name|String
name|allocatePort
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|cls
argument_list|)
return|;
block|}
specifier|protected
specifier|static
name|String
name|allocatePort
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|int
name|count
parameter_list|)
block|{
return|return
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|cls
argument_list|,
name|count
argument_list|)
return|;
block|}
block|}
end_class

end_unit

