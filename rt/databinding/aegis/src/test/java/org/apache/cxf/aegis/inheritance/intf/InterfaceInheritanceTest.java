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
name|inheritance
operator|.
name|intf
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
name|frontend
operator|.
name|ClientProxyFactoryBean
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
name|service
operator|.
name|Service
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
name|service
operator|.
name|invoker
operator|.
name|BeanInvoker
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
name|assertFalse
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
name|assertNotNull
import|;
end_import

begin_comment
comment|/**  * This test ensures that we're handling inheritance of interfaces correctly.  * Since we can't do multiple parent inheritance in XML schema, which interfaces  * require, we just don't allow interface inheritance period.  */
end_comment

begin_class
specifier|public
class|class
name|InterfaceInheritanceTest
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
name|Server
name|server
init|=
name|createService
argument_list|(
name|IInterfaceService
operator|.
name|class
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|service
operator|.
name|setInvoker
argument_list|(
operator|new
name|BeanInvoker
argument_list|(
operator|new
name|InterfaceService
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClient
parameter_list|()
throws|throws
name|Exception
block|{
name|ClientProxyFactoryBean
name|proxyFac
init|=
operator|new
name|ClientProxyFactoryBean
argument_list|()
decl_stmt|;
name|proxyFac
operator|.
name|setAddress
argument_list|(
literal|"local://IInterfaceService"
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|setupAegis
argument_list|(
name|proxyFac
operator|.
name|getClientFactoryBean
argument_list|()
argument_list|)
expr_stmt|;
name|IInterfaceService
name|client
init|=
name|proxyFac
operator|.
name|create
argument_list|(
name|IInterfaceService
operator|.
name|class
argument_list|)
decl_stmt|;
name|IChild
name|child
init|=
name|client
operator|.
name|getChild
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|child
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"child"
argument_list|,
name|child
operator|.
name|getChildName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"parent"
argument_list|,
name|child
operator|.
name|getParentName
argument_list|()
argument_list|)
expr_stmt|;
name|IParent
name|parent
init|=
name|client
operator|.
name|getChildViaParent
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"parent"
argument_list|,
name|parent
operator|.
name|getParentName
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|parent
operator|instanceof
name|IChild
argument_list|)
expr_stmt|;
name|IGrandChild
name|grandChild
init|=
name|client
operator|.
name|getGrandChild
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"parent"
argument_list|,
name|grandChild
operator|.
name|getParentName
argument_list|()
argument_list|)
expr_stmt|;
name|Document
name|wsdl
init|=
name|getWSDLDocument
argument_list|(
literal|"IInterfaceService"
argument_list|)
decl_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:complexType[@name='IGrandChild']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:complexType[@name='IGrandChild']//xsd:element[@name='grandChildName']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:complexType[@name='IGrandChild']//xsd:element[@name='childName'][1]"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"//xsd:complexType[@name='IGrandChild']//xsd:element[@name='childName'][2]"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:complexType[@name='IChild']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:complexType[@name='IParent']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"//xsd:complexType[@name='IChild'][@abstract='true']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

