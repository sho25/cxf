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
name|http_undertow
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|HttpURLConnection
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
name|net
operator|.
name|URLConnection
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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|configuration
operator|.
name|Configurer
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
name|jsse
operator|.
name|TLSServerParameters
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
name|spring
operator|.
name|ConfigurerImpl
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
name|helpers
operator|.
name|CastUtils
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
name|helpers
operator|.
name|IOUtils
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
name|management
operator|.
name|InstrumentationManager
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
name|TestUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IMocksControl
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
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|UndertowHTTPServerEngineTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|int
name|PORT1
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|UndertowHTTPServerEngineTest
operator|.
name|class
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|PORT2
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|UndertowHTTPServerEngineTest
operator|.
name|class
argument_list|,
literal|2
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|PORT3
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|UndertowHTTPServerEngineTest
operator|.
name|class
argument_list|,
literal|3
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|UndertowHTTPServerEngineFactory
name|factory
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|bus
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
expr_stmt|;
name|Configurer
name|configurer
init|=
operator|new
name|ConfigurerImpl
argument_list|()
decl_stmt|;
name|bus
operator|.
name|getExtension
argument_list|(
name|Configurer
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|configurer
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|InstrumentationManager
name|iManager
init|=
name|control
operator|.
name|createMock
argument_list|(
name|InstrumentationManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|iManager
operator|.
name|getMBeanServer
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|bus
operator|.
name|getExtension
argument_list|(
name|InstrumentationManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|iManager
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|factory
operator|=
operator|new
name|UndertowHTTPServerEngineFactory
argument_list|()
expr_stmt|;
name|factory
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEngineRetrieval
parameter_list|()
throws|throws
name|Exception
block|{
name|UndertowHTTPServerEngine
name|engine
init|=
name|factory
operator|.
name|createUndertowHTTPServerEngine
argument_list|(
name|PORT1
argument_list|,
literal|"http"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Engine references for the same port should point to the same instance"
argument_list|,
name|engine
operator|==
name|factory
operator|.
name|retrieveUndertowHTTPServerEngine
argument_list|(
name|PORT1
argument_list|)
argument_list|)
expr_stmt|;
name|UndertowHTTPServerEngineFactory
operator|.
name|destroyForPort
argument_list|(
name|PORT1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHttpAndHttps
parameter_list|()
throws|throws
name|Exception
block|{
name|UndertowHTTPServerEngine
name|engine
init|=
name|factory
operator|.
name|createUndertowHTTPServerEngine
argument_list|(
name|PORT1
argument_list|,
literal|"http"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Protocol must be http"
argument_list|,
literal|"http"
operator|.
name|equals
argument_list|(
name|engine
operator|.
name|getProtocol
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|engine
operator|=
operator|new
name|UndertowHTTPServerEngine
argument_list|()
expr_stmt|;
name|engine
operator|.
name|setPort
argument_list|(
name|PORT2
argument_list|)
expr_stmt|;
name|engine
operator|.
name|setMaxIdleTime
argument_list|(
literal|30000
argument_list|)
expr_stmt|;
name|engine
operator|.
name|setTlsServerParameters
argument_list|(
operator|new
name|TLSServerParameters
argument_list|()
argument_list|)
expr_stmt|;
name|engine
operator|.
name|finalizeConfig
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|UndertowHTTPServerEngine
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|UndertowHTTPServerEngine
argument_list|>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|engine
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setEnginesList
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|engine
operator|=
name|factory
operator|.
name|createUndertowHTTPServerEngine
argument_list|(
name|PORT2
argument_list|,
literal|"https"
argument_list|)
expr_stmt|;
name|UndertowHTTPTestHandler
name|handler1
init|=
operator|new
name|UndertowHTTPTestHandler
argument_list|(
literal|"string1"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|engine
operator|.
name|addServant
argument_list|(
operator|new
name|URL
argument_list|(
literal|"https://localhost:"
operator|+
name|PORT2
operator|+
literal|"/test"
argument_list|)
argument_list|,
name|handler1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Protocol must be https"
argument_list|,
literal|"https"
operator|.
name|equals
argument_list|(
name|engine
operator|.
name|getProtocol
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong maxIdleTime."
argument_list|,
literal|30000
argument_list|,
name|engine
operator|.
name|getMaxIdleTime
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setTLSServerParametersForPort
argument_list|(
name|PORT1
argument_list|,
operator|new
name|TLSServerParameters
argument_list|()
argument_list|)
expr_stmt|;
name|engine
operator|=
name|factory
operator|.
name|createUndertowHTTPServerEngine
argument_list|(
name|PORT1
argument_list|,
literal|"https"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Protocol must be https"
argument_list|,
literal|"https"
operator|.
name|equals
argument_list|(
name|engine
operator|.
name|getProtocol
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setTLSServerParametersForPort
argument_list|(
name|PORT3
argument_list|,
operator|new
name|TLSServerParameters
argument_list|()
argument_list|)
expr_stmt|;
name|engine
operator|=
name|factory
operator|.
name|createUndertowHTTPServerEngine
argument_list|(
name|PORT3
argument_list|,
literal|"https"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Protocol must be https"
argument_list|,
literal|"https"
operator|.
name|equals
argument_list|(
name|engine
operator|.
name|getProtocol
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|UndertowHTTPServerEngineFactory
operator|.
name|destroyForPort
argument_list|(
name|PORT1
argument_list|)
expr_stmt|;
name|UndertowHTTPServerEngineFactory
operator|.
name|destroyForPort
argument_list|(
name|PORT2
argument_list|)
expr_stmt|;
name|UndertowHTTPServerEngineFactory
operator|.
name|destroyForPort
argument_list|(
name|PORT3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testaddServants
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|urlStr
init|=
literal|"http://localhost:"
operator|+
name|PORT1
operator|+
literal|"/hello/test"
decl_stmt|;
name|String
name|urlStr2
init|=
literal|"http://localhost:"
operator|+
name|PORT1
operator|+
literal|"/hello233/test"
decl_stmt|;
name|UndertowHTTPServerEngine
name|engine
init|=
name|factory
operator|.
name|createUndertowHTTPServerEngine
argument_list|(
name|PORT1
argument_list|,
literal|"http"
argument_list|)
decl_stmt|;
name|engine
operator|.
name|setMaxIdleTime
argument_list|(
literal|30000
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addServant
argument_list|(
operator|new
name|URL
argument_list|(
name|urlStr
argument_list|)
argument_list|,
operator|new
name|UndertowHTTPTestHandler
argument_list|(
literal|"string1"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong maxIdleTime."
argument_list|,
literal|30000
argument_list|,
name|engine
operator|.
name|getMaxIdleTime
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|response
init|=
literal|null
decl_stmt|;
name|response
operator|=
name|getResponse
argument_list|(
name|urlStr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The undertow http handler did not take effect"
argument_list|,
name|response
argument_list|,
literal|"string1"
argument_list|)
expr_stmt|;
try|try
block|{
name|engine
operator|.
name|addServant
argument_list|(
operator|new
name|URL
argument_list|(
name|urlStr
argument_list|)
argument_list|,
operator|new
name|UndertowHTTPTestHandler
argument_list|(
literal|"string2"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"We don't support to publish the two service at the same context path"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Get a wrong exception message"
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"hello/test"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|engine
operator|.
name|addServant
argument_list|(
operator|new
name|URL
argument_list|(
name|urlStr
operator|+
literal|"/test"
argument_list|)
argument_list|,
operator|new
name|UndertowHTTPTestHandler
argument_list|(
literal|"string2"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"We don't support to publish the two service at the same context path"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Get a wrong exception message"
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"hello/test/test"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|engine
operator|.
name|addServant
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT1
operator|+
literal|"/hello"
argument_list|)
argument_list|,
operator|new
name|UndertowHTTPTestHandler
argument_list|(
literal|"string2"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"We don't support to publish the two service at the same context path"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Get a wrong exception message"
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"hello"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
comment|// check if the system property change could work
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.apache.cxf.transports.http_undertow.DontCheckUrl"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addServant
argument_list|(
operator|new
name|URL
argument_list|(
name|urlStr
operator|+
literal|"/test"
argument_list|)
argument_list|,
operator|new
name|UndertowHTTPTestHandler
argument_list|(
literal|"string2"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
comment|// clean up the System property setting
name|System
operator|.
name|clearProperty
argument_list|(
literal|"org.apache.cxf.transports.http_undertow.DontCheckUrl"
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addServant
argument_list|(
operator|new
name|URL
argument_list|(
name|urlStr2
argument_list|)
argument_list|,
operator|new
name|UndertowHTTPTestHandler
argument_list|(
literal|"string2"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|s
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"org.xnio:type=Xnio,provider=\"nio\""
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find Undertow Server: "
operator|+
name|s
argument_list|,
literal|1
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|engine
operator|.
name|removeServant
argument_list|(
operator|new
name|URL
argument_list|(
name|urlStr
argument_list|)
argument_list|)
expr_stmt|;
name|engine
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|response
operator|=
name|getResponse
argument_list|(
name|urlStr2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The undertow http handler did not take effect"
argument_list|,
name|response
argument_list|,
literal|"string2"
argument_list|)
expr_stmt|;
comment|// set the get request
name|UndertowHTTPServerEngineFactory
operator|.
name|destroyForPort
argument_list|(
name|PORT1
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test that multiple UndertowHTTPServerEngine instances can be used simultaneously      * without having name collisions.      */
annotation|@
name|Test
specifier|public
name|void
name|testJmxSupport
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|urlStr
init|=
literal|"http://localhost:"
operator|+
name|PORT1
operator|+
literal|"/hello/test"
decl_stmt|;
name|String
name|urlStr2
init|=
literal|"http://localhost:"
operator|+
name|PORT2
operator|+
literal|"/hello/test"
decl_stmt|;
name|UndertowHTTPServerEngine
name|engine
init|=
name|factory
operator|.
name|createUndertowHTTPServerEngine
argument_list|(
name|PORT1
argument_list|,
literal|"http"
argument_list|)
decl_stmt|;
name|UndertowHTTPServerEngine
name|engine2
init|=
name|factory
operator|.
name|createUndertowHTTPServerEngine
argument_list|(
name|PORT2
argument_list|,
literal|"http"
argument_list|)
decl_stmt|;
name|UndertowHTTPTestHandler
name|handler1
init|=
operator|new
name|UndertowHTTPTestHandler
argument_list|(
literal|"string1"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|UndertowHTTPTestHandler
name|handler2
init|=
operator|new
name|UndertowHTTPTestHandler
argument_list|(
literal|"string2"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|engine
operator|.
name|addServant
argument_list|(
operator|new
name|URL
argument_list|(
name|urlStr
argument_list|)
argument_list|,
name|handler1
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|s
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"org.xnio:type=Xnio,provider=\"nio\""
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 1 Undertow Server: "
operator|+
name|s
argument_list|,
literal|1
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|engine2
operator|.
name|addServant
argument_list|(
operator|new
name|URL
argument_list|(
name|urlStr2
argument_list|)
argument_list|,
name|handler2
argument_list|)
expr_stmt|;
name|s
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"org.xnio:type=Xnio,provider=\"nio\",worker=\"*\""
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 2 Undertow Server: "
operator|+
name|s
argument_list|,
literal|2
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|engine
operator|.
name|removeServant
argument_list|(
operator|new
name|URL
argument_list|(
name|urlStr
argument_list|)
argument_list|)
expr_stmt|;
name|engine2
operator|.
name|removeServant
argument_list|(
operator|new
name|URL
argument_list|(
name|urlStr2
argument_list|)
argument_list|)
expr_stmt|;
name|engine
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|s
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"org.xnio:type=Xnio,provider=\"nio\",worker=\"*\""
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 2 Undertow Server: "
operator|+
name|s
argument_list|,
literal|1
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|engine2
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|s
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"org.xnio:type=Xnio,provider=\"nio\",worker=\"*\""
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 0 Undertow Server: "
operator|+
name|s
argument_list|,
literal|0
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|UndertowHTTPServerEngineFactory
operator|.
name|destroyForPort
argument_list|(
name|PORT1
argument_list|)
expr_stmt|;
name|UndertowHTTPServerEngineFactory
operator|.
name|destroyForPort
argument_list|(
name|PORT2
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getResponse
parameter_list|(
name|String
name|target
parameter_list|)
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|target
argument_list|)
decl_stmt|;
name|URLConnection
name|connection
init|=
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|connection
operator|instanceof
name|HttpURLConnection
argument_list|)
expr_stmt|;
name|connection
operator|.
name|connect
argument_list|()
expr_stmt|;
name|InputStream
name|in
init|=
name|connection
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|buffer
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit
