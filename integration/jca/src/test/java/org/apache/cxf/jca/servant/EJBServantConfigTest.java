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
name|jca
operator|.
name|servant
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
name|junit
operator|.
name|Assert
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
name|assertNull
import|;
end_import

begin_class
specifier|public
class|class
name|EJBServantConfigTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testNoWsdl
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|value
init|=
literal|"{http://apache.org/hello_world_soap_http}Greeter"
decl_stmt|;
name|QName
name|result
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"Greeter"
argument_list|)
decl_stmt|;
name|EJBServantConfig
name|config
init|=
operator|new
name|EJBServantConfig
argument_list|(
literal|null
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|result
argument_list|,
name|config
operator|.
name|getServiceName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|config
operator|.
name|getWsdlURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoWsdlNoLocalPart
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|value
init|=
literal|"{http://apache.org/hello_world_soap_http}"
decl_stmt|;
name|QName
name|result
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|EJBServantConfig
name|config
init|=
operator|new
name|EJBServantConfig
argument_list|(
literal|null
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|result
argument_list|,
name|config
operator|.
name|getServiceName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|config
operator|.
name|getWsdlURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoWsdlNoNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|value
init|=
literal|"Greeter"
decl_stmt|;
name|QName
name|result
init|=
operator|new
name|QName
argument_list|(
literal|""
argument_list|,
literal|"Greeter"
argument_list|)
decl_stmt|;
name|EJBServantConfig
name|config
init|=
operator|new
name|EJBServantConfig
argument_list|(
literal|null
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|result
argument_list|,
name|config
operator|.
name|getServiceName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|config
operator|.
name|getWsdlURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAllNull
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|value
init|=
literal|""
decl_stmt|;
name|EJBServantConfig
name|config
init|=
operator|new
name|EJBServantConfig
argument_list|(
literal|null
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|config
operator|.
name|getServiceName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|config
operator|.
name|getWsdlURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithNullWsdl
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|value
init|=
literal|"@"
decl_stmt|;
name|EJBServantConfig
name|config
init|=
operator|new
name|EJBServantConfig
argument_list|(
literal|null
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|config
operator|.
name|getServiceName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|config
operator|.
name|getWsdlURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithNullServiceName
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|value
init|=
literal|"@wsdl/hello_world.wsdl"
decl_stmt|;
name|EJBServantConfig
name|config
init|=
operator|new
name|EJBServantConfig
argument_list|(
literal|null
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|config
operator|.
name|getServiceName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"wsdl/hello_world.wsdl"
argument_list|,
name|config
operator|.
name|getWsdlURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFullValue
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|value
init|=
literal|"{http://apache.org/hello_world_soap_http}SOAPService@file:/wsdl/hello_world.wsdl"
decl_stmt|;
name|QName
name|result
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SOAPService"
argument_list|)
decl_stmt|;
name|EJBServantConfig
name|config
init|=
operator|new
name|EJBServantConfig
argument_list|(
literal|null
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"file:/wsdl/hello_world.wsdl"
argument_list|,
name|config
operator|.
name|getWsdlURL
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|result
argument_list|,
name|config
operator|.
name|getServiceName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetServiceClassName
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|value
init|=
literal|"{http://apache.org/hello_world_soap_http}Greeter@file:"
decl_stmt|;
name|EJBServantConfig
name|config
init|=
operator|new
name|EJBServantConfig
argument_list|(
literal|"GreeterBean"
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|EJBEndpoint
name|endpoint
init|=
operator|new
name|EJBEndpoint
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.hello_world_soap_http.Greeter"
argument_list|,
name|endpoint
operator|.
name|getServiceClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

