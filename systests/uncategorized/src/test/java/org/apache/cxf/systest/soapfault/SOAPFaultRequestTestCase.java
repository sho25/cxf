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
name|cxf
operator|.
name|soapfault
operator|.
name|SoapFaultPortType
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
name|soapfault
operator|.
name|SoapFaultService
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
name|AbstractClientServerTestBase
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
import|import
name|org
operator|.
name|xmlsoap
operator|.
name|schemas
operator|.
name|soap
operator|.
name|envelope
operator|.
name|Fault
import|;
end_import

begin_class
specifier|public
class|class
name|SOAPFaultRequestTestCase
extends|extends
name|AbstractClientServerTestBase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PORT
init|=
name|Server
operator|.
name|PORT
decl_stmt|;
specifier|private
specifier|final
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/soapfault"
argument_list|,
literal|"SoapFaultPortType"
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
name|Server
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
name|testSendSoapFaultRequest
parameter_list|()
throws|throws
name|Exception
block|{
name|SoapFaultService
name|service
init|=
operator|new
name|SoapFaultService
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|SoapFaultPortType
name|soapFaultPort
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|SoapFaultPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|soapFaultPort
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|Fault
name|fault
init|=
operator|new
name|Fault
argument_list|()
decl_stmt|;
name|fault
operator|.
name|setFaultstring
argument_list|(
literal|"ClientSetFaultString"
argument_list|)
expr_stmt|;
name|fault
operator|.
name|setFaultcode
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/soapfault"
argument_list|,
literal|"ClientSetError"
argument_list|)
argument_list|)
expr_stmt|;
name|soapFaultPort
operator|.
name|soapFault
argument_list|(
name|fault
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

