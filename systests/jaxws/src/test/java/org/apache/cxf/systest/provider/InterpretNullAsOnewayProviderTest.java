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
name|provider
package|;
end_package

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
name|io
operator|.
name|OutputStream
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
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
name|Endpoint
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
name|Provider
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
name|assertEquals
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

begin_comment
comment|/**  * Testing the null response behavior of jaxws provider (jaxws 2.2 section 5.1.1)  */
end_comment

begin_class
specifier|public
class|class
name|InterpretNullAsOnewayProviderTest
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
specifier|private
specifier|static
specifier|final
name|String
name|ADDRESS1
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/test/nullable1"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ADDRESS2
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/test/nullable2"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ADDRESS3
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/test/nullable3"
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
comment|// endpoint not interpreting null as oneway
name|NullProviderService
name|servant1
init|=
operator|new
name|NullProviderService
argument_list|()
decl_stmt|;
name|Endpoint
name|ep1
init|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|ADDRESS1
argument_list|,
name|servant1
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"endpoint published"
argument_list|,
name|ep1
argument_list|)
expr_stmt|;
name|ep1
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
literal|"jaxws.provider.interpretNullAsOneway"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
comment|// endpoint interpreting null as oneway
name|NullProviderService
name|servant2
init|=
operator|new
name|NullProviderService
argument_list|()
decl_stmt|;
name|Endpoint
name|ep2
init|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|ADDRESS2
argument_list|,
name|servant2
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"endpoint published"
argument_list|,
name|ep2
argument_list|)
expr_stmt|;
name|ep2
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
literal|"jaxws.provider.interpretNullAsOneway"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
comment|// endpoint interpreting null as oneway
name|NullProviderService
name|servant3
init|=
operator|new
name|NullProviderService
argument_list|()
decl_stmt|;
name|Endpoint
name|ep3
init|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|ADDRESS3
argument_list|,
name|servant3
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"endpoint published"
argument_list|,
name|ep3
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
throws|throws
name|Exception
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceProvider
argument_list|(
name|serviceName
operator|=
literal|"NullService"
argument_list|,
name|portName
operator|=
literal|"NullPort"
argument_list|)
annotation|@
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|ServiceMode
argument_list|(
name|value
operator|=
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
argument_list|)
specifier|public
specifier|static
class|class
name|NullProviderService
implements|implements
name|Provider
argument_list|<
name|Source
argument_list|>
block|{
specifier|public
name|Source
name|invoke
parameter_list|(
name|Source
name|request
parameter_list|)
block|{
return|return
literal|null
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
annotation|@
name|Test
specifier|public
name|void
name|testNotInterpretNullAsOneway
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpURLConnection
name|conn
init|=
name|postRequest
argument_list|(
name|ADDRESS1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Soap fault must be returned"
argument_list|,
literal|400
operator|<=
name|conn
operator|.
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNotInterpretNullAsOneway2
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpURLConnection
name|conn
init|=
name|postRequest
argument_list|(
name|ADDRESS2
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Soap fault must be returned"
argument_list|,
literal|400
operator|<=
name|conn
operator|.
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInterpretNullAsOneway
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpURLConnection
name|conn
init|=
name|postRequest
argument_list|(
name|ADDRESS3
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http 202 must be returned"
argument_list|,
literal|202
argument_list|,
name|conn
operator|.
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|HttpURLConnection
name|postRequest
parameter_list|(
name|String
name|address
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
name|address
argument_list|)
decl_stmt|;
name|HttpURLConnection
name|conn
init|=
operator|(
name|HttpURLConnection
operator|)
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|conn
operator|.
name|setDoOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
name|InterpretNullAsOnewayProviderTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"resources/sayHiDocLiteralReq.xml"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"could not load test data"
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|conn
operator|.
name|setRequestMethod
argument_list|(
literal|"POST"
argument_list|)
expr_stmt|;
name|conn
operator|.
name|addRequestProperty
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
name|OutputStream
name|out
init|=
name|conn
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|conn
return|;
block|}
block|}
end_class

end_unit

