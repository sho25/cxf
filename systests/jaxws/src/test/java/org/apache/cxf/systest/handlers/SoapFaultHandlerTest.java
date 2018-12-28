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
name|handlers
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|DataOutputStream
import|;
end_import

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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|test
operator|.
name|AbstractCXFSpringTest
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
name|SoapFaultHandlerTest
extends|extends
name|AbstractCXFSpringTest
block|{
specifier|static
name|String
name|port
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
literal|"SoapFaultHandler"
argument_list|)
decl_stmt|;
specifier|static
name|String
name|addNumbersAddress
init|=
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/SpringEndpoint"
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|String
index|[]
name|getConfigLocations
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"classpath:/org/apache/cxf/systest/handlers/soap_fault_beans.xml"
block|}
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFaultThrowingHandler
parameter_list|()
throws|throws
name|Exception
block|{
comment|// set the post request using url connection
name|URL
name|postUrl
init|=
operator|new
name|URL
argument_list|(
name|addNumbersAddress
argument_list|)
decl_stmt|;
name|HttpURLConnection
name|connection
init|=
operator|(
name|HttpURLConnection
operator|)
name|postUrl
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|connection
operator|.
name|setRequestMethod
argument_list|(
literal|"POST"
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setDoOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setDoInput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setRequestProperty
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"text/xml; charset=UTF-8"
argument_list|)
expr_stmt|;
name|connection
operator|.
name|connect
argument_list|()
expr_stmt|;
name|DataOutputStream
name|out
init|=
operator|new
name|DataOutputStream
argument_list|(
name|connection
operator|.
name|getOutputStream
argument_list|()
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"resources/GreetMeDocLiteralReq.xml"
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|copyAndCloseInput
argument_list|(
name|is
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|InputStream
name|response
init|=
name|getInputStream
argument_list|(
name|connection
argument_list|)
decl_stmt|;
comment|// get the response fault message
name|String
name|result
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|response
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
comment|// just make sure the custom namespace is working
name|assertTrue
argument_list|(
literal|"The custom namespace is not working."
argument_list|,
name|result
operator|.
name|indexOf
argument_list|(
literal|"cxf:Provider"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|InputStream
name|getInputStream
parameter_list|(
name|HttpURLConnection
name|connection
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|in
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|connection
operator|.
name|getResponseCode
argument_list|()
operator|>=
name|HttpURLConnection
operator|.
name|HTTP_BAD_REQUEST
condition|)
block|{
name|in
operator|=
name|connection
operator|.
name|getErrorStream
argument_list|()
expr_stmt|;
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
try|try
block|{
comment|// just in case - but this will most likely cause an exception
name|in
operator|=
name|connection
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
else|else
block|{
name|in
operator|=
name|connection
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
block|}
return|return
name|in
return|;
block|}
block|}
end_class

end_unit

