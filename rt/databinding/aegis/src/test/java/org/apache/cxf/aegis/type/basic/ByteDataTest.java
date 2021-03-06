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
name|aegis
operator|.
name|type
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
name|aegis
operator|.
name|AbstractAegisTest
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
name|aegis
operator|.
name|services
operator|.
name|DataService
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
name|wsdl
operator|.
name|WSDLConstants
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
name|ByteDataTest
extends|extends
name|AbstractAegisTest
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
name|DataService
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBeanService
parameter_list|()
throws|throws
name|Exception
block|{
name|Node
name|response
init|=
name|invoke
argument_list|(
literal|"DataService"
argument_list|,
literal|"GetData.xml"
argument_list|)
decl_stmt|;
name|addNamespace
argument_list|(
literal|"s"
argument_list|,
literal|"http://services.aegis.cxf.apache.org"
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//s:return/s:data"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|response
operator|=
name|invoke
argument_list|(
literal|"DataService"
argument_list|,
literal|"EchoData.xml"
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//s:return/s:data"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBeanServiceWSDL
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|doc
init|=
name|getWSDLDocument
argument_list|(
literal|"DataService"
argument_list|)
decl_stmt|;
name|addNamespace
argument_list|(
literal|"wsdl"
argument_list|,
name|WSDLConstants
operator|.
name|NS_WSDL11
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"wsdlsoap"
argument_list|,
name|WSDLConstants
operator|.
name|NS_SOAP11
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"xsd"
argument_list|,
name|WSDLConstants
operator|.
name|NS_SCHEMA_XSD
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:element[@name='data'][@type='xsd:base64Binary']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

