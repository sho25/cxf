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
name|http_jetty
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
name|IOUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|classextension
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
name|classextension
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

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|Connector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|Handler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|handler
operator|.
name|ContextHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|nio
operator|.
name|SelectChannelConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|security
operator|.
name|SslSocketConnector
import|;
end_import

begin_class
specifier|public
class|class
name|JettyHTTPServerEngineTest
extends|extends
name|Assert
block|{
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|JettyHTTPServerEngineFactory
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
name|factory
operator|=
operator|new
name|JettyHTTPServerEngineFactory
argument_list|()
expr_stmt|;
name|factory
operator|.
name|setBus
argument_list|(
name|bus
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
name|control
operator|.
name|replay
argument_list|()
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
name|JettyHTTPServerEngine
name|engine
init|=
name|factory
operator|.
name|createJettyHTTPServerEngine
argument_list|(
literal|9234
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
name|retrieveJettyHTTPServerEngine
argument_list|(
literal|9234
argument_list|)
argument_list|)
expr_stmt|;
name|factory
operator|.
name|destroyForPort
argument_list|(
literal|1234
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
name|JettyHTTPServerEngine
name|engine
init|=
name|factory
operator|.
name|createJettyHTTPServerEngine
argument_list|(
literal|9234
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
name|JettyHTTPServerEngine
argument_list|()
expr_stmt|;
name|engine
operator|.
name|setPort
argument_list|(
literal|9235
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
name|JettyHTTPServerEngine
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|JettyHTTPServerEngine
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
name|createJettyHTTPServerEngine
argument_list|(
literal|9235
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
literal|9234
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
name|createJettyHTTPServerEngine
argument_list|(
literal|9234
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
literal|9236
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
name|createJettyHTTPServerEngine
argument_list|(
literal|9236
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
name|destroyForPort
argument_list|(
literal|9234
argument_list|)
expr_stmt|;
name|factory
operator|.
name|destroyForPort
argument_list|(
literal|9235
argument_list|)
expr_stmt|;
name|factory
operator|.
name|destroyForPort
argument_list|(
literal|9236
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetConnector
parameter_list|()
throws|throws
name|Exception
block|{
name|JettyHTTPServerEngine
name|engine
init|=
operator|new
name|JettyHTTPServerEngine
argument_list|()
decl_stmt|;
name|Connector
name|conn
init|=
operator|new
name|SslSocketConnector
argument_list|()
decl_stmt|;
name|engine
operator|.
name|setConnector
argument_list|(
name|conn
argument_list|)
expr_stmt|;
name|engine
operator|.
name|setPort
argument_list|(
literal|9000
argument_list|)
expr_stmt|;
try|try
block|{
name|engine
operator|.
name|finalizeConfig
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"We should get the connector not set with TSLServerParameter exception."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// expect the excepion
block|}
name|engine
operator|=
operator|new
name|JettyHTTPServerEngine
argument_list|()
expr_stmt|;
name|conn
operator|=
operator|new
name|SelectChannelConnector
argument_list|()
expr_stmt|;
name|conn
operator|.
name|setPort
argument_list|(
literal|9002
argument_list|)
expr_stmt|;
name|engine
operator|.
name|setConnector
argument_list|(
name|conn
argument_list|)
expr_stmt|;
name|engine
operator|.
name|setPort
argument_list|(
literal|9000
argument_list|)
expr_stmt|;
try|try
block|{
name|engine
operator|.
name|finalizeConfig
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"We should get the connector not set right port exception."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// expect the exception
block|}
name|engine
operator|=
operator|new
name|JettyHTTPServerEngine
argument_list|()
expr_stmt|;
name|conn
operator|=
operator|new
name|SslSocketConnector
argument_list|()
expr_stmt|;
name|conn
operator|.
name|setPort
argument_list|(
literal|9003
argument_list|)
expr_stmt|;
name|engine
operator|.
name|setConnector
argument_list|(
name|conn
argument_list|)
expr_stmt|;
name|engine
operator|.
name|setPort
argument_list|(
literal|9003
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
literal|"http://localhost:9234/hello/test"
decl_stmt|;
name|String
name|urlStr2
init|=
literal|"http://localhost:9234/hello233/test"
decl_stmt|;
name|JettyHTTPServerEngine
name|engine
init|=
name|factory
operator|.
name|createJettyHTTPServerEngine
argument_list|(
literal|9234
argument_list|,
literal|"http"
argument_list|)
decl_stmt|;
name|JettyHTTPTestHandler
name|handler1
init|=
operator|new
name|JettyHTTPTestHandler
argument_list|(
literal|"string1"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|JettyHTTPTestHandler
name|handler2
init|=
operator|new
name|JettyHTTPTestHandler
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
literal|"The jetty http handler did not take effect"
argument_list|,
name|response
argument_list|,
literal|"string1"
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
name|handler2
argument_list|)
expr_stmt|;
name|response
operator|=
name|getResponse
argument_list|(
name|urlStr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The jetty http handler did not take effect"
argument_list|,
name|response
argument_list|,
literal|"string1string2"
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
name|handler2
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
literal|"The jetty http handler did not take effect"
argument_list|,
name|response
argument_list|,
literal|"string2"
argument_list|)
expr_stmt|;
comment|// set the get request
name|factory
operator|.
name|destroyForPort
argument_list|(
literal|9234
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetHandlers
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:9235/hello/test"
argument_list|)
decl_stmt|;
name|JettyHTTPTestHandler
name|handler1
init|=
operator|new
name|JettyHTTPTestHandler
argument_list|(
literal|"string1"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|JettyHTTPTestHandler
name|handler2
init|=
operator|new
name|JettyHTTPTestHandler
argument_list|(
literal|"string2"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|JettyHTTPServerEngine
name|engine
init|=
operator|new
name|JettyHTTPServerEngine
argument_list|()
decl_stmt|;
name|engine
operator|.
name|setPort
argument_list|(
literal|9235
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Handler
argument_list|>
name|handlers
init|=
operator|new
name|ArrayList
argument_list|<
name|Handler
argument_list|>
argument_list|()
decl_stmt|;
name|handlers
operator|.
name|add
argument_list|(
name|handler1
argument_list|)
expr_stmt|;
name|engine
operator|.
name|setHandlers
argument_list|(
name|handlers
argument_list|)
expr_stmt|;
name|engine
operator|.
name|finalizeConfig
argument_list|()
expr_stmt|;
name|engine
operator|.
name|addServant
argument_list|(
name|url
argument_list|,
name|handler2
argument_list|)
expr_stmt|;
name|String
name|response
init|=
literal|null
decl_stmt|;
try|try
block|{
name|response
operator|=
name|getResponse
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"the jetty http handler1 did not take effect"
argument_list|,
name|response
argument_list|,
literal|"string1string2"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Can't get the reponse from the server "
operator|+
name|ex
argument_list|)
expr_stmt|;
block|}
name|engine
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetContextHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|urlStr
init|=
literal|"http://localhost:9234/hello/test"
decl_stmt|;
name|JettyHTTPServerEngine
name|engine
init|=
name|factory
operator|.
name|createJettyHTTPServerEngine
argument_list|(
literal|9234
argument_list|,
literal|"http"
argument_list|)
decl_stmt|;
name|ContextHandler
name|contextHandler
init|=
name|engine
operator|.
name|getContextHandler
argument_list|(
operator|new
name|URL
argument_list|(
name|urlStr
argument_list|)
argument_list|)
decl_stmt|;
comment|// can't find the context handler here
name|assertNull
argument_list|(
name|contextHandler
argument_list|)
expr_stmt|;
name|JettyHTTPTestHandler
name|handler1
init|=
operator|new
name|JettyHTTPTestHandler
argument_list|(
literal|"string1"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|JettyHTTPTestHandler
name|handler2
init|=
operator|new
name|JettyHTTPTestHandler
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
name|contextHandler
operator|=
name|engine
operator|.
name|getContextHandler
argument_list|(
operator|new
name|URL
argument_list|(
name|urlStr
argument_list|)
argument_list|)
expr_stmt|;
name|contextHandler
operator|.
name|setHandler
argument_list|(
name|handler2
argument_list|)
expr_stmt|;
name|contextHandler
operator|.
name|start
argument_list|()
expr_stmt|;
name|String
name|response
init|=
literal|null
decl_stmt|;
try|try
block|{
name|response
operator|=
name|getResponse
argument_list|(
name|urlStr
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Can't get the reponse from the server "
operator|+
name|ex
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"the jetty http handler did not take effect"
argument_list|,
name|response
argument_list|,
literal|"string2"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|destroyForPort
argument_list|(
literal|9234
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJettyHTTPHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|urlStr1
init|=
literal|"http://localhost:9236/hello/test"
decl_stmt|;
name|String
name|urlStr2
init|=
literal|"http://localhost:9236/hello/test2"
decl_stmt|;
name|JettyHTTPServerEngine
name|engine
init|=
name|factory
operator|.
name|createJettyHTTPServerEngine
argument_list|(
literal|9236
argument_list|,
literal|"http"
argument_list|)
decl_stmt|;
name|ContextHandler
name|contextHandler
init|=
name|engine
operator|.
name|getContextHandler
argument_list|(
operator|new
name|URL
argument_list|(
name|urlStr1
argument_list|)
argument_list|)
decl_stmt|;
comment|// can't find the context handler here
name|assertNull
argument_list|(
name|contextHandler
argument_list|)
expr_stmt|;
name|JettyHTTPHandler
name|handler1
init|=
operator|new
name|JettyHTTPTestHandler
argument_list|(
literal|"test"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|JettyHTTPHandler
name|handler2
init|=
operator|new
name|JettyHTTPTestHandler
argument_list|(
literal|"test2"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|engine
operator|.
name|addServant
argument_list|(
operator|new
name|URL
argument_list|(
name|urlStr1
argument_list|)
argument_list|,
name|handler1
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
name|handler2
argument_list|)
expr_stmt|;
name|String
name|response
init|=
literal|null
decl_stmt|;
try|try
block|{
name|response
operator|=
name|getResponse
argument_list|(
name|urlStr1
operator|+
literal|"/test"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Can't get the reponse from the server "
operator|+
name|ex
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"the jetty http handler did not take effect"
argument_list|,
name|response
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
try|try
block|{
name|response
operator|=
name|getResponse
argument_list|(
name|urlStr2
operator|+
literal|"/test"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Can't get the reponse from the server "
operator|+
name|ex
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"the jetty http handler did not take effect"
argument_list|,
name|response
argument_list|,
literal|"test2"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|destroyForPort
argument_list|(
literal|9236
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

