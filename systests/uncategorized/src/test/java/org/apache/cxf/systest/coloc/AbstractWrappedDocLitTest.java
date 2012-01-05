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
name|coloc
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|BadRecordLitFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
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
name|hello_world_soap_http
operator|.
name|GreeterImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|NoSuchCodeLitFault
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

begin_comment
comment|//import org.junit.Ignore;
end_comment

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * This class invokes the service described in /wsdl/greeter_control.wsdl.  * This WSDL contains operations with in-out parameters.  * It sets up the a client in "getPort()" to send requests to the  * server which is listening on port 9001 (SOAP/HTTP).  * The subclass defines where CXF configuration and the  * target server (transport, etc).  *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractWrappedDocLitTest
extends|extends
name|AbstractColocTest
block|{
specifier|static
specifier|final
name|QName
name|SERVICE_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SOAPService"
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|QName
name|PORT_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SoapPort"
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|WSDL_LOCATION
init|=
literal|"/wsdl/hello_world.wsdl"
decl_stmt|;
specifier|private
name|Greeter
name|port
decl_stmt|;
specifier|private
name|GreeterImpl
name|impl
init|=
operator|new
name|GreeterImpl
argument_list|()
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|port
operator|=
name|getPort
argument_list|(
name|getServiceQname
argument_list|()
argument_list|,
name|getPortQName
argument_list|()
argument_list|,
name|getWsdlLocation
argument_list|()
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTwoWayOperation
parameter_list|()
block|{
for|for
control|(
name|int
name|idx
init|=
literal|0
init|;
name|idx
operator|<
literal|2
condition|;
name|idx
operator|++
control|)
block|{
name|verifySayHi
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|verifyGreetMe
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOneWayOperation
parameter_list|()
block|{
for|for
control|(
name|int
name|idx
init|=
literal|0
init|;
name|idx
operator|<
literal|2
condition|;
name|idx
operator|++
control|)
block|{
name|verifyGreetMeOneway
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFault
parameter_list|()
block|{
for|for
control|(
name|int
name|idx
init|=
literal|0
init|;
name|idx
operator|<
literal|2
condition|;
name|idx
operator|++
control|)
block|{
name|verifyTestDocLitFault
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|verifyTestDocLitFault
parameter_list|(
name|Greeter
name|proxy
parameter_list|)
block|{
try|try
block|{
name|proxy
operator|.
name|testDocLitFault
argument_list|(
name|BadRecordLitFault
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw a BadRecordLitFault Exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BadRecordLitFault
name|brlf
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|BadRecordLitFault
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|brlf
operator|.
name|getFaultInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchCodeLitFault
name|nsclf
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Should not throw a NoSuchCodeLitFault Exception"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|verifyGreetMeOneway
parameter_list|(
name|Greeter
name|proxy
parameter_list|)
block|{
name|int
name|count
init|=
name|impl
operator|.
name|getInvocationCount
argument_list|()
decl_stmt|;
name|proxy
operator|.
name|greetMeOneWay
argument_list|(
literal|"oneWay"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Count Should not be same"
argument_list|,
name|count
operator|!=
name|impl
operator|.
name|getInvocationCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|verifySayHi
parameter_list|(
name|Greeter
name|greeterPort
parameter_list|)
block|{
name|String
name|resp
init|=
name|greeterPort
operator|.
name|sayHi
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bonjour"
argument_list|,
name|resp
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|verifyGreetMe
parameter_list|(
name|Greeter
name|greeterPort
parameter_list|)
block|{
name|String
name|resp
init|=
name|greeterPort
operator|.
name|greetMe
argument_list|(
literal|"BART"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello BART"
argument_list|,
name|resp
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Object
name|getServiceImpl
parameter_list|()
block|{
return|return
name|impl
return|;
block|}
specifier|protected
name|String
name|getWsdlLocation
parameter_list|()
block|{
return|return
name|WSDL_LOCATION
return|;
block|}
specifier|protected
name|QName
name|getServiceQname
parameter_list|()
block|{
return|return
name|SERVICE_NAME
return|;
block|}
specifier|protected
name|QName
name|getPortQName
parameter_list|()
block|{
return|return
name|PORT_NAME
return|;
block|}
specifier|protected
name|boolean
name|isFaultCodeCheckEnabled
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

