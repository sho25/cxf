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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|EndpointImpl
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
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|WSDLAddressRewriteTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testWithSameAddress
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
literal|null
decl_stmt|;
try|try
block|{
name|endpoint
operator|=
name|publishEndpoint
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|String
name|soapAddressLine
init|=
name|getSoapAddressLine
argument_list|(
literal|"localhost"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|soapAddressLine
operator|.
name|contains
argument_list|(
literal|"address location=\"http://localhost"
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
try|try
block|{
name|endpoint
operator|=
name|publishEndpoint
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|String
name|soapAddressLine
init|=
name|getSoapAddressLine
argument_list|(
literal|"localhost"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|soapAddressLine
operator|.
name|contains
argument_list|(
literal|"address location=\"http://localhost"
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithEquivalentAddress
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
literal|null
decl_stmt|;
try|try
block|{
name|endpoint
operator|=
name|publishEndpoint
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|String
name|soapAddressLine
init|=
name|getSoapAddressLine
argument_list|(
literal|"127.0.0.1"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|soapAddressLine
operator|.
name|contains
argument_list|(
literal|"address location=\"http://localhost"
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
comment|//now test enabling the autoRewrite; this should be used when having multiple
comment|//addresses (belonging to different networks) for a single server instance
try|try
block|{
name|endpoint
operator|=
name|publishEndpoint
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|String
name|soapAddressLine
init|=
name|getSoapAddressLine
argument_list|(
literal|"127.0.0.1"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|soapAddressLine
operator|.
name|contains
argument_list|(
literal|"address location=\"http://127.0.0.1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|String
name|getSoapAddressLine
parameter_list|(
name|String
name|address
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
name|address
argument_list|,
literal|9020
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
name|address
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
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
if|if
condition|(
name|line
operator|.
name|contains
argument_list|(
literal|"address location=\""
argument_list|)
condition|)
block|{
break|break;
block|}
block|}
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
return|return
name|line
return|;
block|}
specifier|private
name|Endpoint
name|publishEndpoint
parameter_list|(
name|boolean
name|autoRewriteSoapAddress
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|Endpoint
operator|.
name|publish
argument_list|(
literal|"http://localhost:9020/SoapContext/GreeterPort"
argument_list|,
operator|new
name|GreeterImpl
argument_list|()
argument_list|)
decl_stmt|;
name|EndpointInfo
name|ei
init|=
operator|(
operator|(
name|EndpointImpl
operator|)
name|endpoint
operator|)
operator|.
name|getServer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
name|ei
operator|.
name|setProperty
argument_list|(
literal|"autoRewriteSoapAddress"
argument_list|,
name|autoRewriteSoapAddress
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

