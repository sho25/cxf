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
name|soapfault
operator|.
name|details
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
name|greeter_control
operator|.
name|PingMeFault
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
name|types
operator|.
name|FaultDetail
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
name|Soap11ClientServerTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|Server11
operator|.
name|class
argument_list|)
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
name|Server11
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
name|testFaultMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|Greeter
name|greeter
init|=
name|getGreeter
argument_list|()
decl_stmt|;
try|try
block|{
name|greeter
operator|.
name|sayHi
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw Exception!"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"sayHiFault Caused by: Get a wrong name<sayHi>"
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|StackTraceElement
index|[]
name|elements
init|=
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getStackTrace
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.cxf.systest.soapfault.details.GreeterImpl11"
argument_list|,
name|elements
index|[
literal|0
index|]
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|boolean
name|findNPE
init|=
literal|false
decl_stmt|;
for|for
control|(
name|StackTraceElement
name|element
range|:
name|elements
control|)
block|{
if|if
condition|(
name|element
operator|.
name|getClassName
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"java.lang.NullPointerException"
argument_list|)
operator|>
literal|0
condition|)
block|{
name|findNPE
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
name|assertTrue
argument_list|(
literal|"Cannot find the Cause of NPE"
argument_list|,
name|findNPE
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPingMeFault
parameter_list|()
throws|throws
name|Exception
block|{
name|Greeter
name|greeter
init|=
name|getGreeter
argument_list|()
decl_stmt|;
try|try
block|{
name|greeter
operator|.
name|pingMe
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw Exception!"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PingMeFault
name|ex
parameter_list|)
block|{
name|FaultDetail
name|detail
init|=
name|ex
operator|.
name|getFaultInfo
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
operator|(
name|short
operator|)
literal|2
argument_list|,
name|detail
operator|.
name|getMajor
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|short
operator|)
literal|1
argument_list|,
name|detail
operator|.
name|getMinor
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"PingMeFault raised by server"
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|StackTraceElement
index|[]
name|element
init|=
name|ex
operator|.
name|getStackTrace
argument_list|()
decl_stmt|;
comment|// The stack trace will be reset as it's a declare exception
name|assertEquals
argument_list|(
literal|"org.apache.cxf.jaxws.JaxWsClientProxy"
argument_list|,
name|element
index|[
literal|0
index|]
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Greeter
name|getGreeter
parameter_list|()
throws|throws
name|NumberFormatException
throws|,
name|MalformedURLException
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
name|Greeter
name|greeter
init|=
name|service
operator|.
name|getGreeterPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
return|return
name|greeter
return|;
block|}
block|}
end_class

end_unit

