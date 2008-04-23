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
name|xmlbeans
operator|.
name|basic
package|;
end_package

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
name|xmlbeans
operator|.
name|AbstractXmlBeansTest
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
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|BasicTest
extends|extends
name|AbstractXmlBeansTest
block|{
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
name|createService
argument_list|(
name|TestService
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|"TestService"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBasicInvoke
parameter_list|()
throws|throws
name|Exception
block|{
name|Node
name|response
init|=
name|invoke
argument_list|(
literal|"TestService"
argument_list|,
literal|"bean11.xml"
argument_list|)
decl_stmt|;
name|addNamespace
argument_list|(
literal|"ns1"
argument_list|,
literal|"http://basic.xmlbeans.cxf.apache.org/"
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/s:Envelope/s:Body/ns1:echoAddressResponse"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//ns1:echoAddressResponse/ns1:return"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//ns1:echoAddressResponse/ns1:return/country"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//ns1:echoAddressResponse/ns1:return/country[text()='Mars']"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWSDL
parameter_list|()
throws|throws
name|Exception
block|{
name|Node
name|doc
init|=
name|getWSDLDocument
argument_list|(
literal|"TestService"
argument_list|)
decl_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:types/xsd:schema"
operator|+
literal|"[@targetNamespace='http://cxf.apache.org/databinding/xmlbeans/test']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:types/xsd:schema"
operator|+
literal|"[@targetNamespace='http://cxf.apache.org/databinding/xmlbeans/test']"
operator|+
literal|"/xsd:complexType[@name='Address']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

