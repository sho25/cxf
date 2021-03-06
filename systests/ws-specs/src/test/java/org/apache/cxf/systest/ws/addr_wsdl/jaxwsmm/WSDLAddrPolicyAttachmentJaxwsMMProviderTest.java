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
name|addr_wsdl
operator|.
name|jaxwsmm
package|;
end_package

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
name|List
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
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|messaging
operator|.
name|AsyncMessaging
import|;
end_import

begin_import
import|import
name|messaging
operator|.
name|AsyncMessagingService
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
name|interceptor
operator|.
name|Interceptor
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
name|jaxws
operator|.
name|JaxWsServerFactoryBean
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
name|message
operator|.
name|Message
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
name|systest
operator|.
name|ws
operator|.
name|util
operator|.
name|ConnectionHelper
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusTestServerBase
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
name|policy
operator|.
name|PolicyInInterceptor
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
name|policy
operator|.
name|PolicyOutInterceptor
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|WSDLAddrPolicyAttachmentJaxwsMMProviderTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
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
specifier|public
specifier|static
specifier|final
name|String
name|DECOUPLED
init|=
name|allocatePort
argument_list|(
literal|"decoupled"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|WSDLAddrPolicyAttachmentJaxwsMMProviderTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ADDRESS
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/AsyncMessagingServiceProvider"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WSDL_ADDRESS
init|=
name|ADDRESS
operator|+
literal|"?wsdl"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|ENDPOINT_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://messaging/"
argument_list|,
literal|"AsyncMessagingService"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
class|class
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/systest/ws/addr_wsdl/jaxwsmm/server.xml"
argument_list|)
decl_stmt|;
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|JaxWsServerFactoryBean
name|serviceFactory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|serviceFactory
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setServiceClass
argument_list|(
name|MessageProviderWithAddressingPolicy
operator|.
name|class
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setWsdlLocation
argument_list|(
literal|"wsdl_systest_wsspec/addr-jaxwsmm.wsdl"
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setAddress
argument_list|(
name|ADDRESS
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Server
name|provider
init|=
name|serviceFactory
operator|.
name|create
argument_list|()
decl_stmt|;
name|EndpointInfo
name|ei
init|=
name|provider
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Started server at: "
operator|+
name|ei
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|testInterceptors
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
try|try
block|{
name|Server
name|s
init|=
operator|new
name|Server
argument_list|()
decl_stmt|;
name|s
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"done!"
argument_list|)
expr_stmt|;
block|}
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
literal|"server did not launch correctly"
argument_list|,
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
block|}
specifier|private
specifier|static
name|void
name|testInterceptors
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|boolean
name|hasServerIn
init|=
literal|false
decl_stmt|;
name|boolean
name|hasServerOut
init|=
literal|false
decl_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|inInterceptors
init|=
name|b
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
for|for
control|(
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|i
range|:
name|inInterceptors
control|)
block|{
if|if
condition|(
name|i
operator|instanceof
name|PolicyInInterceptor
condition|)
block|{
name|hasServerIn
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
name|hasServerIn
argument_list|)
expr_stmt|;
for|for
control|(
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|i
range|:
name|b
operator|.
name|getOutInterceptors
argument_list|()
control|)
block|{
if|if
condition|(
name|i
operator|instanceof
name|PolicyOutInterceptor
condition|)
block|{
name|hasServerOut
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
name|hasServerOut
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUsingAddressing
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/systest/ws/policy/addr-inline-policy-old.xml"
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|URL
name|wsdlURL
init|=
operator|new
name|URL
argument_list|(
name|WSDL_ADDRESS
argument_list|)
decl_stmt|;
name|AsyncMessagingService
name|ams
init|=
operator|new
name|AsyncMessagingService
argument_list|(
name|wsdlURL
argument_list|,
name|ENDPOINT_NAME
argument_list|)
decl_stmt|;
name|AsyncMessaging
name|am
init|=
name|ams
operator|.
name|getAsyncMessagingImplPort
argument_list|()
decl_stmt|;
name|ConnectionHelper
operator|.
name|setKeepAliveConnection
argument_list|(
name|am
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|testInterceptors
argument_list|(
name|bus
argument_list|)
expr_stmt|;
comment|// oneway
name|am
operator|.
name|deliver
argument_list|(
literal|"This is a test"
argument_list|)
expr_stmt|;
name|am
operator|.
name|deliver
argument_list|(
literal|"This is another test"
argument_list|)
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

