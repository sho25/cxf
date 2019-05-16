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
name|type_test
operator|.
name|soap
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Holder
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
name|type_test
operator|.
name|AbstractTypeTestClient5
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|type_test
operator|.
name|types1
operator|.
name|FixedArray
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
name|SOAPDocLitClientTypeTest
extends|extends
name|AbstractTypeTestClient5
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|WSDL_PATH
init|=
literal|"/wsdl/type_test/type_test_doclit_soap.wsdl"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|QName
name|SERVICE_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/type_test/doc"
argument_list|,
literal|"SOAPService"
argument_list|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|QName
name|PORT_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/type_test/doc"
argument_list|,
literal|"SOAPPort"
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|PORT
init|=
name|SOAPDocLitServerImpl
operator|.
name|PORT
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|updatePort
parameter_list|()
throws|throws
name|Exception
block|{
name|updateAddressPort
argument_list|(
name|docClient
argument_list|,
name|PORT
argument_list|)
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
name|boolean
name|ok
init|=
name|launchServer
argument_list|(
name|SOAPDocLitServerImpl
operator|.
name|class
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"failed to launch server"
argument_list|,
name|ok
argument_list|)
expr_stmt|;
name|initClient
argument_list|(
name|SERVICE_NAME
argument_list|,
name|PORT_NAME
argument_list|,
name|WSDL_PATH
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testValidationFailureOnServerOut
parameter_list|()
throws|throws
name|Exception
block|{
name|FixedArray
name|x
init|=
operator|new
name|FixedArray
argument_list|()
decl_stmt|;
name|FixedArray
name|yOrig
init|=
operator|new
name|FixedArray
argument_list|()
decl_stmt|;
name|Collections
operator|.
name|addAll
argument_list|(
name|x
operator|.
name|getItem
argument_list|()
argument_list|,
literal|24
argument_list|,
literal|42
argument_list|,
literal|2008
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|addAll
argument_list|(
name|yOrig
operator|.
name|getItem
argument_list|()
argument_list|,
literal|24
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|Holder
argument_list|<
name|FixedArray
argument_list|>
name|y
init|=
operator|new
name|Holder
argument_list|<>
argument_list|(
name|yOrig
argument_list|)
decl_stmt|;
name|Holder
argument_list|<
name|FixedArray
argument_list|>
name|z
init|=
operator|new
name|Holder
argument_list|<>
argument_list|()
decl_stmt|;
try|try
block|{
name|docClient
operator|.
name|testFixedArray
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|z
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Marshalling"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

