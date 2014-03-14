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
name|wssc
package|;
end_package

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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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

begin_comment
comment|/**  * SecureConversation tests.  */
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
name|WSSCTest
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
name|PORT2
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|2
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|STAX_PORT
init|=
name|allocatePort
argument_list|(
name|StaxServer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|STAX_PORT2
init|=
name|allocatePort
argument_list|(
name|StaxServer
operator|.
name|class
argument_list|,
literal|2
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OUT
init|=
literal|"CXF : ping"
decl_stmt|;
specifier|private
specifier|static
name|wssec
operator|.
name|wssc
operator|.
name|PingService
name|svc
decl_stmt|;
specifier|private
specifier|static
name|Bus
name|bus
decl_stmt|;
specifier|final
name|TestParam
name|test
decl_stmt|;
specifier|public
name|WSSCTest
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
specifier|final
name|String
name|port
decl_stmt|;
specifier|public
name|TestParam
parameter_list|(
name|String
name|p
parameter_list|,
name|String
name|port
parameter_list|,
name|boolean
name|b
parameter_list|)
block|{
name|prefix
operator|=
name|p
expr_stmt|;
name|this
operator|.
name|port
operator|=
name|port
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
name|port
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
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork
name|launchServer
argument_list|(
name|StaxServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|bus
operator|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/systest/ws/wssc/client.xml"
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|svc
operator|=
operator|new
name|wssec
operator|.
name|wssc
operator|.
name|PingService
argument_list|()
expr_stmt|;
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
literal|"SecureConversation_UserNameOverTransport_IPingService"
argument_list|,
name|PORT2
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"SecureConversation_MutualCertificate10SignEncrypt_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"AC_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"ADC_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"ADC-ES_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_A_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_AD_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_AD-ES_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"UXC_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"UXDC_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"UXDC-SEES_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_UX_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_UXD_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_UXD-SEES_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XC_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XDC_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XDC_IPingService1"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XDC-ES_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XDC-SEES_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_X_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_X10_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_XD_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_XD-SEES_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_XD-ES_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"SecureConversation_UserNameOverTransport_IPingService"
argument_list|,
name|PORT2
argument_list|,
literal|true
argument_list|)
block|}
block|,
comment|// TODO Endorsing streaming not supported
comment|// {new TestParam("SecureConversation_MutualCertificate10SignEncrypt_IPingService", PORT, true)},
block|{
operator|new
name|TestParam
argument_list|(
literal|"AC_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"ADC_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"ADC-ES_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_A_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_AD_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_AD-ES_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"UXC_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"UXDC_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"UXDC-SEES_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_UX_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_UXD_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_UXD-SEES_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
comment|// TODO Streaming endorsing not working
comment|// {new TestParam("XC_IPingService", PORT, true)},
comment|// {new TestParam("XDC_IPingService", PORT, true)},
comment|// {new TestParam("XDC_IPingService1", PORT, true)},
comment|// {new TestParam("XDC-ES_IPingService", PORT, true)},
comment|// {new TestParam("XDC-SEES_IPingService", PORT, true)},
comment|// {new TestParam("_X_IPingService", PORT, true)},
block|{
operator|new
name|TestParam
argument_list|(
literal|"_X10_IPingService"
argument_list|,
name|PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
comment|// TODO Streaming endorsing not working
comment|// {new TestParam("_XD_IPingService", PORT, true)},
comment|// {new TestParam("_XD-SEES_IPingService", PORT, true)},
comment|// {new TestParam("_XD-ES_IPingService", PORT, true)},
block|{
operator|new
name|TestParam
argument_list|(
literal|"SecureConversation_UserNameOverTransport_IPingService"
argument_list|,
name|STAX_PORT2
argument_list|,
literal|false
argument_list|)
block|}
block|,
comment|// TODO StAX Policy Validation error caused by incorrect DOM message
comment|// {new TestParam("SecureConversation_MutualCertificate10SignEncrypt_IPingService",
comment|//               STAX_PORT, false)},
block|{
operator|new
name|TestParam
argument_list|(
literal|"AC_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"ADC_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"ADC-ES_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_A_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_AD_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_AD-ES_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"UXC_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"UXDC_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"UXDC-SEES_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_UX_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_UXD_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_UXD-SEES_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XC_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XDC_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XDC_IPingService1"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XDC-ES_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XDC-SEES_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_X_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_X10_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_XD_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_XD-SEES_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_XD-ES_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"SecureConversation_UserNameOverTransport_IPingService"
argument_list|,
name|STAX_PORT2
argument_list|,
literal|true
argument_list|)
block|}
block|,
comment|// TODO Endorsing derived keys not supported.
comment|// {new TestParam("SecureConversation_MutualCertificate10SignEncrypt_IPingService",
comment|//               STAX_PORT, true)},
block|{
operator|new
name|TestParam
argument_list|(
literal|"AC_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"ADC_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"ADC-ES_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_A_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_AD_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_AD-ES_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"UXC_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"UXDC_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"UXDC-SEES_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_UX_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_UXD_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"_UXD-SEES_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
comment|// TODO Streaming endorsing not working
comment|// {new TestParam("XC_IPingService", STAX_PORT, true)},
comment|// {new TestParam("XDC_IPingService", STAX_PORT, true)},
comment|// {new TestParam("XDC_IPingService1", STAX_PORT, true)},
comment|// {new TestParam("XDC-ES_IPingService", STAX_PORT, true)},
comment|// {new TestParam("XDC-SEES_IPingService", STAX_PORT, true)},
comment|// {new TestParam("_X_IPingService", STAX_PORT, true)},
block|{
operator|new
name|TestParam
argument_list|(
literal|"_X10_IPingService"
argument_list|,
name|STAX_PORT
argument_list|,
literal|true
argument_list|)
block|}
block|,
comment|// TODO Streaming endorsing not working
comment|// {new TestParam("_XD_IPingService", STAX_PORT, true)},
comment|// {new TestParam("_XD-SEES_IPingService", STAX_PORT, true)},
comment|// {new TestParam("_XD-ES_IPingService", STAX_PORT, true)},
block|}
argument_list|)
return|;
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
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|stopAllServers
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSecureConversation
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|wssec
operator|.
name|wssc
operator|.
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
literal|"http://WSSec/wssc"
argument_list|,
name|test
operator|.
name|prefix
argument_list|)
argument_list|,
name|wssec
operator|.
name|wssc
operator|.
name|IPingService
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|PORT2
operator|.
name|equals
argument_list|(
name|test
operator|.
name|port
argument_list|)
operator|||
name|STAX_PORT2
operator|.
name|equals
argument_list|(
name|test
operator|.
name|port
argument_list|)
condition|)
block|{
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
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
literal|"https://localhost:"
operator|+
name|test
operator|.
name|port
operator|+
literal|"/"
operator|+
name|test
operator|.
name|prefix
argument_list|)
expr_stmt|;
block|}
else|else
block|{
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
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
literal|"http://localhost:"
operator|+
name|test
operator|.
name|port
operator|+
literal|"/"
operator|+
name|test
operator|.
name|prefix
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|test
operator|.
name|prefix
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'_'
condition|)
block|{
comment|//MS would like the _ versions to send a cancel
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
name|STS_TOKEN_DO_CANCEL
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|test
operator|.
name|streaming
condition|)
block|{
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
name|wssec
operator|.
name|wssc
operator|.
name|PingRequest
name|params
init|=
operator|new
name|wssec
operator|.
name|wssc
operator|.
name|PingRequest
argument_list|()
decl_stmt|;
name|org
operator|.
name|xmlsoap
operator|.
name|ping
operator|.
name|Ping
name|ping
init|=
operator|new
name|org
operator|.
name|xmlsoap
operator|.
name|ping
operator|.
name|Ping
argument_list|()
decl_stmt|;
name|ping
operator|.
name|setOrigin
argument_list|(
literal|"CXF"
argument_list|)
expr_stmt|;
name|ping
operator|.
name|setScenario
argument_list|(
literal|"Scenario5"
argument_list|)
expr_stmt|;
name|ping
operator|.
name|setText
argument_list|(
literal|"ping"
argument_list|)
expr_stmt|;
name|params
operator|.
name|setPing
argument_list|(
name|ping
argument_list|)
expr_stmt|;
try|try
block|{
name|wssec
operator|.
name|wssc
operator|.
name|PingResponse
name|output
init|=
name|port
operator|.
name|ping
argument_list|(
name|params
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|OUT
argument_list|,
name|output
operator|.
name|getPingResponse
argument_list|()
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Error doing "
operator|+
name|test
operator|.
name|prefix
argument_list|,
name|ex
argument_list|)
throw|;
block|}
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|port
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

