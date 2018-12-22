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
name|io
operator|.
name|BufferedReader
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
name|io
operator|.
name|InputStreamReader
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
name|Socket
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|WSDLQueryTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BareServer
operator|.
name|PORT
decl_stmt|;
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
name|BareServer
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
name|testCorrectHostHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|sendQuery
argument_list|(
literal|"localhost:"
operator|+
name|PORT
argument_list|,
literal|"HTTP/1.1 200 OK"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCorrectHostNoPortHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|sendQuery
argument_list|(
literal|"localhost"
argument_list|,
literal|"HTTP/1.1 200 OK"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBogusHostHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|sendQuery
argument_list|(
literal|"foobar:"
operator|+
name|PORT
argument_list|,
literal|"HTTP/1.1 200 OK"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBogusHostBogusPortHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|sendQuery
argument_list|(
literal|"foobar:666"
argument_list|,
literal|"HTTP/1.1 200 OK"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithBogusHostNoPortHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|sendQuery
argument_list|(
literal|"foobar"
argument_list|,
literal|"HTTP/1.1 200 OK"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|sendQuery
parameter_list|(
name|String
name|hostHeader
parameter_list|,
name|String
name|expectedResponseLine
parameter_list|)
throws|throws
name|Exception
block|{
name|Socket
name|s
init|=
operator|new
name|Socket
argument_list|(
literal|"localhost"
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|PORT
argument_list|)
argument_list|)
decl_stmt|;
name|OutputStream
name|os
init|=
name|s
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|os
operator|.
name|write
argument_list|(
literal|"GET /SoapContext/GreeterPort?wsdl HTTP/1.1\r\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
operator|(
literal|"Host:"
operator|+
name|hostHeader
operator|+
literal|"\r\n\r\n"
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
name|s
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected response"
argument_list|,
name|expectedResponseLine
argument_list|,
name|line
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
name|s
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

