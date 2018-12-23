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
name|addr_responses
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
name|soap
operator|.
name|SOAPFaultException
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
name|AbstractWSATestBase
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

begin_class
specifier|public
class|class
name|WSAResponsesClientServerTest
extends|extends
name|AbstractWSATestBase
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
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|createBus
argument_list|()
expr_stmt|;
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
name|testWSAResponses
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|setupInLogging
argument_list|()
expr_stmt|;
name|this
operator|.
name|setupOutLogging
argument_list|()
expr_stmt|;
name|URL
name|wsdlURL
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/wsa/responses?wsdl"
argument_list|)
decl_stmt|;
name|QName
name|serviceQName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/systest/wsa/responses"
argument_list|,
literal|"HelloService"
argument_list|)
decl_stmt|;
name|HelloService
name|service
init|=
operator|new
name|HelloService
argument_list|(
name|wsdlURL
argument_list|,
name|serviceQName
argument_list|)
decl_stmt|;
try|try
block|{
name|service
operator|.
name|getHelloPort
argument_list|()
operator|.
name|sayHi
argument_list|(
literal|"helloWorld"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expect exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|String
name|expectedDetail
init|=
literal|"A header representing a Message Addressing Property is not valid"
decl_stmt|;
if|if
condition|(
name|e
operator|instanceof
name|SOAPFaultException
condition|)
block|{
name|assertTrue
argument_list|(
literal|"Expect fault deail : "
operator|+
name|expectedDetail
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
name|expectedDetail
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

