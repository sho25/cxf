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
name|jaxws
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
name|lang
operator|.
name|reflect
operator|.
name|UndeclaredThrowableException
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
name|util
operator|.
name|HashMap
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
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|binding
operator|.
name|soap
operator|.
name|Soap11
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
name|greeter_control
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
name|cxf
operator|.
name|greeter_control
operator|.
name|GreeterService
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
name|XMLUtils
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
name|XPathUtils
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

begin_class
specifier|public
class|class
name|ClientServerGreeterNoWsdlTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
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
name|ServerGreeterNoWsdl
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
name|testInvocation
parameter_list|()
throws|throws
name|Exception
block|{
name|GreeterService
name|service
init|=
operator|new
name|GreeterService
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
try|try
block|{
name|Greeter
name|greeter
init|=
name|service
operator|.
name|getGreeterPort
argument_list|()
decl_stmt|;
name|String
name|greeting
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"Bonjour"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Bonjour"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UndeclaredThrowableException
name|ex
parameter_list|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|ex
operator|.
name|getCause
argument_list|()
throw|;
block|}
block|}
comment|// test the http get invocation
annotation|@
name|Test
specifier|public
name|void
name|testGetGreetMe
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpURLConnection
name|httpConnection
init|=
name|getHttpConnection
argument_list|(
literal|"http://localhost:9020/SoapContext/GreeterPort/greetMe/requestType/cxf"
argument_list|)
decl_stmt|;
name|httpConnection
operator|.
name|connect
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|httpConnection
operator|.
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/xml; charset=utf-8"
argument_list|,
name|httpConnection
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"OK"
argument_list|,
name|httpConnection
operator|.
name|getResponseMessage
argument_list|()
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
name|httpConnection
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|XMLUtils
operator|.
name|parse
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ns
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ns
operator|.
name|put
argument_list|(
literal|"soap"
argument_list|,
name|Soap11
operator|.
name|SOAP_NAMESPACE
argument_list|)
expr_stmt|;
name|ns
operator|.
name|put
argument_list|(
literal|"ns2"
argument_list|,
literal|"http://cxf.apache.org/greeter_control/types"
argument_list|)
expr_stmt|;
name|XPathUtils
name|xu
init|=
operator|new
name|XPathUtils
argument_list|(
name|ns
argument_list|)
decl_stmt|;
name|Node
name|body
init|=
operator|(
name|Node
operator|)
name|xu
operator|.
name|getValue
argument_list|(
literal|"/soap:Envelope/soap:Body"
argument_list|,
name|doc
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|String
name|response
init|=
operator|(
name|String
operator|)
name|xu
operator|.
name|getValue
argument_list|(
literal|"//ns2:greetMeResponse/ns2:responseType/text()"
argument_list|,
name|body
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello cxf"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

