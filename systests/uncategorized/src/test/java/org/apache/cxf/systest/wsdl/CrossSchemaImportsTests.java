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
name|wsdl
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
name|apache
operator|.
name|cxf
operator|.
name|Bus
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
name|endpoint
operator|.
name|Server
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
name|test
operator|.
name|TestUtilities
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
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"classpath:crossSchemaBeans.xml"
block|}
argument_list|)
specifier|public
class|class
name|CrossSchemaImportsTests
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
specifier|private
name|TestUtilities
name|testUtilities
decl_stmt|;
specifier|public
name|CrossSchemaImportsTests
parameter_list|()
block|{
name|testUtilities
operator|=
operator|new
name|TestUtilities
argument_list|(
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJaxbCrossSchemaImport
parameter_list|()
throws|throws
name|Exception
block|{
name|testUtilities
operator|.
name|setBus
argument_list|(
operator|(
name|Bus
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"cxf"
argument_list|)
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|addDefaultNamespaces
argument_list|()
expr_stmt|;
name|Server
name|s
init|=
name|testUtilities
operator|.
name|getServerForService
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/type_test/doc"
argument_list|,
literal|"TypeTestPortTypeService"
argument_list|)
argument_list|)
decl_stmt|;
name|Document
name|wsdl
init|=
name|testUtilities
operator|.
name|getWSDLDocument
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|testUtilities
operator|.
name|assertValid
argument_list|(
literal|"//xsd:schema[@targetNamespace='http://apache.org/type_test/doc']/"
operator|+
literal|"xsd:import[@namespace='http://apache.org/type_test/types1']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|LifeCycleListenerTester
operator|.
name|getInitCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

