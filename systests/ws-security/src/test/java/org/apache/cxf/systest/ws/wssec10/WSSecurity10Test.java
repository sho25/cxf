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
name|ws
operator|.
name|wssec10
package|;
end_package

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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

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
name|frontend
operator|.
name|ClientProxy
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
name|systest
operator|.
name|ws
operator|.
name|common
operator|.
name|SecurityTestUtil
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
name|systest
operator|.
name|ws
operator|.
name|wssec10
operator|.
name|server
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusClientServerTestBase
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
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameters
import|;
end_import

begin_import
import|import
name|wssec
operator|.
name|wssec10
operator|.
name|IPingService
import|;
end_import

begin_import
import|import
name|wssec
operator|.
name|wssec10
operator|.
name|PingService
import|;
end_import

begin_comment
comment|/**  * It tests both DOM + StAX clients against the DOM server  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|value
operator|=
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|class
argument_list|)
specifier|public
class|class
name|WSSecurity10Test
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|SSL_PORT
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|INPUT
init|=
literal|"foo"
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|unrestrictedPoliciesInstalled
decl_stmt|;
static|static
block|{
name|unrestrictedPoliciesInstalled
operator|=
name|SecurityTestUtil
operator|.
name|checkUnrestrictedPoliciesInstalled
argument_list|()
expr_stmt|;
block|}
empty_stmt|;
specifier|final
name|TestParam
name|test
decl_stmt|;
specifier|public
name|WSSecurity10Test
parameter_list|(
name|TestParam
name|type
parameter_list|)
block|{
name|this
operator|.
name|test
operator|=
name|type
expr_stmt|;
block|}
specifier|static
class|class
name|TestParam
block|{
specifier|final
name|String
name|prefix
decl_stmt|;
specifier|final
name|boolean
name|streaming
decl_stmt|;
specifier|public
name|TestParam
parameter_list|(
name|String
name|p
parameter_list|,
name|boolean
name|b
parameter_list|)
block|{
name|prefix
operator|=
name|p
expr_stmt|;
name|streaming
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|prefix
operator|+
literal|":"
operator|+
operator|(
name|streaming
condition|?
literal|"streaming"
else|:
literal|"dom"
operator|)
return|;
block|}
block|}
annotation|@
name|Parameters
argument_list|(
name|name
operator|=
literal|"{0}"
argument_list|)
specifier|public
specifier|static
name|Collection
argument_list|<
name|TestParam
index|[]
argument_list|>
name|data
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|TestParam
index|[]
index|[]
block|{
block|{
operator|new
name|TestParam
argument_list|(
literal|"UserName"
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"UserNameOverTransport"
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"MutualCertificate10SignEncrypt"
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"MutualCertificate10SignEncryptRsa15TripleDes"
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"UserName"
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"UserNameOverTransport"
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"MutualCertificate10SignEncrypt"
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"MutualCertificate10SignEncryptRsa15TripleDes"
argument_list|,
literal|true
argument_list|)
block|}
block|}
argument_list|)
return|;
block|}
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|unrestrictedPoliciesInstalled
condition|)
block|{
name|createStaticBus
argument_list|(
literal|"org/apache/cxf/systest/ws/wssec10/client.xml"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|createStaticBus
argument_list|(
literal|"org/apache/cxf/systest/ws/wssec10/client_restricted.xml"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|AfterClass
specifier|public
specifier|static
name|void
name|cleanup
parameter_list|()
throws|throws
name|Exception
block|{
name|SecurityTestUtil
operator|.
name|cleanup
argument_list|()
expr_stmt|;
name|stopAllServers
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClientServerDOM
parameter_list|()
block|{
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|getStaticBus
argument_list|()
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|getStaticBus
argument_list|()
argument_list|)
expr_stmt|;
name|URL
name|wsdlLocation
init|=
literal|null
decl_stmt|;
name|PingService
name|svc
init|=
literal|null
decl_stmt|;
name|wsdlLocation
operator|=
name|getWsdlLocation
argument_list|(
name|test
operator|.
name|prefix
argument_list|)
expr_stmt|;
name|svc
operator|=
operator|new
name|PingService
argument_list|(
name|wsdlLocation
argument_list|)
expr_stmt|;
specifier|final
name|IPingService
name|port
init|=
name|svc
operator|.
name|getPort
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://WSSec/wssec10"
argument_list|,
name|test
operator|.
name|prefix
operator|+
literal|"_IPingService"
argument_list|)
argument_list|,
name|IPingService
operator|.
name|class
argument_list|)
decl_stmt|;
name|Client
name|cl
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
decl_stmt|;
if|if
condition|(
name|test
operator|.
name|streaming
condition|)
block|{
comment|// Streaming
operator|(
operator|(
name|BindingProvider
operator|)
name|port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENABLE_STREAMING_SECURITY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|port
operator|)
operator|.
name|getResponseContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENABLE_STREAMING_SECURITY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
name|HTTPConduit
name|http
init|=
operator|(
name|HTTPConduit
operator|)
name|cl
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|HTTPClientPolicy
name|httpClientPolicy
init|=
operator|new
name|HTTPClientPolicy
argument_list|()
decl_stmt|;
name|httpClientPolicy
operator|.
name|setConnectionTimeout
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|httpClientPolicy
operator|.
name|setReceiveTimeout
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|http
operator|.
name|setClient
argument_list|(
name|httpClientPolicy
argument_list|)
expr_stmt|;
name|String
name|output
init|=
name|port
operator|.
name|echo
argument_list|(
name|INPUT
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|INPUT
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|cl
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|static
name|URL
name|getWsdlLocation
parameter_list|(
name|String
name|portPrefix
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
literal|"UserNameOverTransport"
operator|.
name|equals
argument_list|(
name|portPrefix
argument_list|)
condition|)
block|{
return|return
operator|new
name|URL
argument_list|(
literal|"https://localhost:"
operator|+
name|SSL_PORT
operator|+
literal|"/"
operator|+
name|portPrefix
operator|+
literal|"?wsdl"
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"UserName"
operator|.
name|equals
argument_list|(
name|portPrefix
argument_list|)
condition|)
block|{
return|return
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/"
operator|+
name|portPrefix
operator|+
literal|"?wsdl"
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"MutualCertificate10SignEncrypt"
operator|.
name|equals
argument_list|(
name|portPrefix
argument_list|)
condition|)
block|{
return|return
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/"
operator|+
name|portPrefix
operator|+
literal|"?wsdl"
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"MutualCertificate10SignEncryptRsa15TripleDes"
operator|.
name|equals
argument_list|(
name|portPrefix
argument_list|)
condition|)
block|{
return|return
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/"
operator|+
name|portPrefix
operator|+
literal|"?wsdl"
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|mue
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

