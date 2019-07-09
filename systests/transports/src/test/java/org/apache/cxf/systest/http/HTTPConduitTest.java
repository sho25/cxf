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
name|http
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
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|LogManager
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
name|hello_world
operator|.
name|Greeter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world
operator|.
name|services
operator|.
name|SOAPService
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
name|assertNotNull
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_comment
comment|/**  * This class tests several issues and Conduit policies based  * on a set up of redirecting servers.  *<pre>  *  * Http Redirection:  *  * Rethwel(http:9004) ----> Mortimer (http:9000)  *  * Redirect Loop:  *  * Hurlon (http:9006) ----> Abost(http:9007) ----\  *   ^                                            |  *   |-------------------------------------------/  */
end_comment

begin_class
specifier|public
class|class
name|HTTPConduitTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|private
specifier|static
specifier|final
name|boolean
name|IN_PROCESS
init|=
literal|true
decl_stmt|;
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|servers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|addrMap
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world"
argument_list|,
literal|"SOAPService"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|rethwelQ
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world"
argument_list|,
literal|"Rethwel"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|mortimerQ
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world"
argument_list|,
literal|"Mortimer"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|hurlonQ
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world"
argument_list|,
literal|"Hurlon"
argument_list|)
decl_stmt|;
specifier|public
name|HTTPConduitTest
parameter_list|()
block|{     }
specifier|public
specifier|static
name|String
name|getPort
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|BusServer
operator|.
name|PORTMAP
operator|.
name|get
argument_list|(
name|s
argument_list|)
return|;
block|}
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|allocatePorts
parameter_list|()
block|{
name|BusServer
operator|.
name|resetPortMap
argument_list|()
expr_stmt|;
name|addrMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|addrMap
operator|.
name|put
argument_list|(
literal|"Mortimer"
argument_list|,
literal|"http://localhost:"
operator|+
name|getPort
argument_list|(
literal|"PORT0"
argument_list|)
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|addrMap
operator|.
name|put
argument_list|(
literal|"Rethwel"
argument_list|,
literal|"http://localhost:"
operator|+
name|getPort
argument_list|(
literal|"PORT1"
argument_list|)
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|addrMap
operator|.
name|put
argument_list|(
literal|"Abost"
argument_list|,
literal|"http://localhost:"
operator|+
name|getPort
argument_list|(
literal|"PORT2"
argument_list|)
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|addrMap
operator|.
name|put
argument_list|(
literal|"Hurlon"
argument_list|,
literal|"http://localhost:"
operator|+
name|getPort
argument_list|(
literal|"PORT3"
argument_list|)
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|servers
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * This function is used to start up a server. It only "starts" a      * server if it hasn't been started before, hence its static nature.      *<p>      * This approach is used to start the needed servers for a particular test      * instead of starting them all in "startServers". This single needed      * server approach allieviates the pain in starting them all just to run      * a particular test in the debugger.      */
specifier|public
specifier|synchronized
name|boolean
name|startServer
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|servers
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|URL
name|serverC
init|=
name|Server
operator|.
name|class
operator|.
name|getResource
argument_list|(
name|name
operator|+
literal|".cxf"
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|boolean
name|server
init|=
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|null
argument_list|,
operator|new
name|String
index|[]
block|{
name|name
block|,
name|addrMap
operator|.
name|get
argument_list|(
name|name
argument_list|)
block|,
name|serverC
operator|.
name|toString
argument_list|()
block|}
argument_list|,
name|IN_PROCESS
argument_list|)
decl_stmt|;
if|if
condition|(
name|server
condition|)
block|{
name|servers
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
return|return
name|server
return|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|cleanUp
parameter_list|()
block|{
name|Bus
name|b
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|b
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|b
operator|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|b
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
comment|//methods that a subclass can override to inject a Proxy into the flow
comment|//and assert the proxy was appropriately called
specifier|protected
name|void
name|configureProxy
parameter_list|(
name|Client
name|c
parameter_list|)
block|{     }
specifier|protected
name|void
name|resetProxyCount
parameter_list|()
block|{     }
specifier|protected
name|void
name|assertProxyRequestCount
parameter_list|(
name|int
name|i
parameter_list|)
block|{     }
specifier|private
name|Greeter
name|getMortimerGreeter
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"greeting.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"WSDL is null"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Service is null"
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|Greeter
name|mortimer
init|=
name|service
operator|.
name|getPort
argument_list|(
name|mortimerQ
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Port is null"
argument_list|,
name|mortimer
argument_list|)
expr_stmt|;
name|updateAddressPort
argument_list|(
name|mortimer
argument_list|,
name|getPort
argument_list|(
literal|"PORT0"
argument_list|)
argument_list|)
expr_stmt|;
name|configureProxy
argument_list|(
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|mortimer
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|mortimer
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBasicConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|startServer
argument_list|(
literal|"Mortimer"
argument_list|)
expr_stmt|;
name|Greeter
name|mortimer
init|=
name|getMortimerGreeter
argument_list|()
decl_stmt|;
name|String
name|answer
init|=
name|mortimer
operator|.
name|sayHi
argument_list|()
decl_stmt|;
name|answer
operator|=
name|mortimer
operator|.
name|sayHi
argument_list|()
expr_stmt|;
name|answer
operator|=
name|mortimer
operator|.
name|sayHi
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpected answer: "
operator|+
name|answer
argument_list|,
literal|"Bonjour from Mortimer"
operator|.
name|equals
argument_list|(
name|answer
argument_list|)
argument_list|)
expr_stmt|;
name|assertProxyRequestCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLogLevelIssueCXF3466
parameter_list|()
throws|throws
name|Exception
block|{
name|startServer
argument_list|(
literal|"Mortimer"
argument_list|)
expr_stmt|;
name|Greeter
name|mortimer
init|=
name|getMortimerGreeter
argument_list|()
decl_stmt|;
name|Logger
name|rootLogger
init|=
name|LogManager
operator|.
name|getLogManager
argument_list|()
operator|.
name|getLogger
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|Level
name|oldLevel
init|=
name|rootLogger
operator|.
name|getLevel
argument_list|()
decl_stmt|;
name|rootLogger
operator|.
name|setLevel
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
expr_stmt|;
try|try
block|{
comment|// Will throw exception Stream is closed if bug is present
name|mortimer
operator|.
name|sayHi
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|rootLogger
operator|.
name|setLevel
argument_list|(
name|oldLevel
argument_list|)
expr_stmt|;
block|}
name|assertProxyRequestCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
comment|/**      * This methods tests that a conduit that is not configured      * to follow redirects will not. The default is not to      * follow redirects.      * Rethwel redirects to Mortimer.      *      * Note: Unfortunately, the invocation will      * "fail" for any number of other reasons.      */
annotation|@
name|Test
specifier|public
name|void
name|testHttp2HttpRedirectFail
parameter_list|()
throws|throws
name|Exception
block|{
name|startServer
argument_list|(
literal|"Mortimer"
argument_list|)
expr_stmt|;
name|startServer
argument_list|(
literal|"Rethwel"
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"greeting.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"WSDL is null"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Service is null"
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|Greeter
name|rethwel
init|=
name|service
operator|.
name|getPort
argument_list|(
name|rethwelQ
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Port is null"
argument_list|,
name|rethwel
argument_list|)
expr_stmt|;
name|updateAddressPort
argument_list|(
name|rethwel
argument_list|,
name|getPort
argument_list|(
literal|"PORT1"
argument_list|)
argument_list|)
expr_stmt|;
name|configureProxy
argument_list|(
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|rethwel
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|answer
init|=
literal|null
decl_stmt|;
try|try
block|{
name|answer
operator|=
name|rethwel
operator|.
name|sayHi
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Redirect didn't fail. Got answer: "
operator|+
name|answer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//e.printStackTrace();
block|}
name|assertProxyRequestCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
comment|/**      * We use this class to reset the default bus.      * Note: This may not always work in the future.      * I was lucky in that "defaultBus" is actually a      * protected static.      */
class|class
name|DefaultBusFactory
extends|extends
name|SpringBusFactory
block|{
specifier|public
name|Bus
name|createBus
parameter_list|(
name|URL
name|config
parameter_list|)
block|{
name|Bus
name|bus
init|=
name|super
operator|.
name|createBus
argument_list|(
name|config
argument_list|,
literal|true
argument_list|)
decl_stmt|;
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
return|return
name|bus
return|;
block|}
block|}
comment|/**      * This method tests if http to http redirects work.      * Rethwel redirects to Mortimer.      */
annotation|@
name|Test
specifier|public
name|void
name|testHttp2HttpRedirect
parameter_list|()
throws|throws
name|Exception
block|{
name|startServer
argument_list|(
literal|"Mortimer"
argument_list|)
expr_stmt|;
name|startServer
argument_list|(
literal|"Rethwel"
argument_list|)
expr_stmt|;
name|URL
name|config
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"Http2HttpRedirect.cxf"
argument_list|)
decl_stmt|;
comment|// We go through the back door, setting the default bus.
operator|new
name|DefaultBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"greeting.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"WSDL is null"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Service is null"
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|Greeter
name|rethwel
init|=
name|service
operator|.
name|getPort
argument_list|(
name|rethwelQ
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|rethwel
argument_list|,
name|getPort
argument_list|(
literal|"PORT1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Port is null"
argument_list|,
name|rethwel
argument_list|)
expr_stmt|;
name|configureProxy
argument_list|(
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|rethwel
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|answer
init|=
name|rethwel
operator|.
name|sayHi
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpected answer: "
operator|+
name|answer
argument_list|,
literal|"Bonjour from Mortimer"
operator|.
name|equals
argument_list|(
name|answer
argument_list|)
argument_list|)
expr_stmt|;
name|assertProxyRequestCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
comment|/**      * This methods tests that a redirection loop will fail.      * Hurlon redirects to Abost, which redirects to Hurlon.      *      * Note: Unfortunately, the invocation may "fail" for any      * number of reasons.      */
annotation|@
name|Test
specifier|public
name|void
name|testHttp2HttpLoopRedirectFail
parameter_list|()
throws|throws
name|Exception
block|{
name|startServer
argument_list|(
literal|"Abost"
argument_list|)
expr_stmt|;
name|startServer
argument_list|(
literal|"Hurlon"
argument_list|)
expr_stmt|;
name|URL
name|config
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"Http2HttpLoopRedirectFail.cxf"
argument_list|)
decl_stmt|;
comment|// We go through the back door, setting the default bus.
operator|new
name|DefaultBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"greeting.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"WSDL is null"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Service is null"
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|Greeter
name|hurlon
init|=
name|service
operator|.
name|getPort
argument_list|(
name|hurlonQ
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Port is null"
argument_list|,
name|hurlon
argument_list|)
expr_stmt|;
name|updateAddressPort
argument_list|(
name|hurlon
argument_list|,
name|getPort
argument_list|(
literal|"PORT3"
argument_list|)
argument_list|)
expr_stmt|;
name|configureProxy
argument_list|(
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|hurlon
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|answer
init|=
literal|null
decl_stmt|;
try|try
block|{
name|answer
operator|=
name|hurlon
operator|.
name|sayHi
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Redirect didn't fail. Got answer: "
operator|+
name|answer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// This exception will be one of not being able to
comment|// read from the StreamReader
comment|//e.printStackTrace();
block|}
name|assertProxyRequestCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

