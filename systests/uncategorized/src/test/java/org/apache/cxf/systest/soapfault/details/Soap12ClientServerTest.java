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
name|hello_world_soap12_http
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
name|hello_world_soap12_http
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
name|hello_world_soap12_http
operator|.
name|SOAPService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap12_http
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
name|Soap12ClientServerTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|Server12
operator|.
name|PORT
decl_stmt|;
specifier|private
specifier|final
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap12_http"
argument_list|,
literal|"SOAPService"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap12_http"
argument_list|,
literal|"SoapPort"
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
name|Server12
operator|.
name|class
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
name|element
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
literal|"org.apache.cxf.systest.soapfault.details.GreeterImpl12"
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
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/hello_world_soap12.wsdl"
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
literal|"Service is ull "
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|Greeter
name|g
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|g
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
return|return
name|g
return|;
block|}
block|}
end_class

end_unit

