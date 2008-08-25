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
name|integration
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
name|w3c
operator|.
name|dom
operator|.
name|NodeList
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
name|ArrayService
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
name|BeanService
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

begin_comment
comment|/**  * @author<a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>  * @since Feb 21, 2004  */
end_comment

begin_class
specifier|public
class|class
name|WrappedTest
extends|extends
name|AbstractAegisTest
block|{
specifier|private
name|ArrayService
name|arrayService
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
name|arrayService
operator|=
operator|new
name|ArrayService
argument_list|()
expr_stmt|;
name|createService
argument_list|(
name|BeanService
operator|.
name|class
argument_list|,
literal|"BeanService"
argument_list|)
expr_stmt|;
name|createService
argument_list|(
name|ArrayService
operator|.
name|class
argument_list|,
name|arrayService
argument_list|,
literal|"Array"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"urn:Array"
argument_list|,
literal|"Array"
argument_list|)
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
literal|"BeanService"
argument_list|,
literal|"bean11.xml"
argument_list|)
decl_stmt|;
name|addNamespace
argument_list|(
literal|"sb"
argument_list|,
literal|"http://services.aegis.cxf.apache.org"
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"beanz"
argument_list|,
literal|"urn:beanz"
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/s:Envelope/s:Body/sb:getSimpleBeanResponse"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//sb:getSimpleBeanResponse/sb:return"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//sb:getSimpleBeanResponse/sb:return/beanz:howdy[text()=\"howdy\"]"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//sb:getSimpleBeanResponse/sb:return/beanz:bleh[text()=\"bleh\"]"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testArrayWsdl
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|doc
init|=
name|getWSDLDocument
argument_list|(
literal|"Array"
argument_list|)
decl_stmt|;
name|NodeList
name|stuff
init|=
name|assertValid
argument_list|(
literal|"//xsd:complexType[@name='ArrayOfString-2-50']"
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|stuff
operator|.
name|getLength
argument_list|()
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
name|Node
name|doc
init|=
name|getWSDLDocument
argument_list|(
literal|"BeanService"
argument_list|)
decl_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:types"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:types/xsd:schema"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:types/"
operator|+
literal|"xsd:schema[@targetNamespace='http://services.aegis.cxf.apache.org']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:schema[@targetNamespace='http://services.aegis.cxf.apache.org']/"
operator|+
literal|"xsd:element[@name='getSubmitBean']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:complexType[@name='getSubmitBean']/xsd:sequence"
operator|+
literal|"/xsd:element[@name='bleh'][@type='xsd:string'][@minOccurs='0']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:complexType[@name='getSubmitBean']/xsd:sequence"
operator|+
literal|"/xsd:element[@name='bean'][@type='ns0:SimpleBean'][@minOccurs='0']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:types"
operator|+
literal|"/xsd:schema[@targetNamespace='http://services.aegis.cxf.apache.org']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:types"
operator|+
literal|"/xsd:schema[@targetNamespace='urn:beanz']"
operator|+
literal|"/xsd:complexType[@name=\"SimpleBean\"]"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:types"
operator|+
literal|"/xsd:schema[@targetNamespace='urn:beanz']"
operator|+
literal|"/xsd:complexType[@name=\"SimpleBean\"]/xsd:sequence/xsd:element"
operator|+
literal|"[@name=\"bleh\"][@minOccurs='0']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:types"
operator|+
literal|"/xsd:schema[@targetNamespace='urn:beanz']"
operator|+
literal|"/xsd:complexType[@name=\"SimpleBean\"]/xsd:sequence/xsd:element"
operator|+
literal|"[@name=\"howdy\"][@minOccurs='0']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:types"
operator|+
literal|"/xsd:schema[@targetNamespace='urn:beanz']"
operator|+
literal|"/xsd:complexType[@name=\"SimpleBean\"]/xsd:sequence/xsd:element"
operator|+
literal|"[@type=\"xsd:string\"]"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSubmitJDOMArray
parameter_list|()
throws|throws
name|Exception
block|{
name|org
operator|.
name|jdom
operator|.
name|xpath
operator|.
name|XPath
name|jxpathWalrus
init|=
name|org
operator|.
name|jdom
operator|.
name|xpath
operator|.
name|XPath
operator|.
name|newInstance
argument_list|(
literal|"/a:anyType/iam:walrus"
argument_list|)
decl_stmt|;
name|jxpathWalrus
operator|.
name|addNamespace
argument_list|(
literal|"a"
argument_list|,
literal|"urn:Array"
argument_list|)
expr_stmt|;
name|jxpathWalrus
operator|.
name|addNamespace
argument_list|(
literal|"iam"
argument_list|,
literal|"uri:iam"
argument_list|)
expr_stmt|;
name|jxpathWalrus
operator|.
name|addNamespace
argument_list|(
literal|"linux"
argument_list|,
literal|"uri:linux"
argument_list|)
expr_stmt|;
name|jxpathWalrus
operator|.
name|addNamespace
argument_list|(
literal|"planets"
argument_list|,
literal|"uri:planets"
argument_list|)
expr_stmt|;
name|invoke
argument_list|(
literal|"Array"
argument_list|,
literal|"/org/apache/cxf/aegis/integration/anyTypeArrayJDOM.xml"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"before items"
argument_list|,
name|arrayService
operator|.
name|getBeforeValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|arrayService
operator|.
name|getJdomArray
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|org
operator|.
name|jdom
operator|.
name|Element
name|e
init|=
operator|(
name|org
operator|.
name|jdom
operator|.
name|Element
operator|)
name|jxpathWalrus
operator|.
name|selectSingleNode
argument_list|(
name|arrayService
operator|.
name|getJdomArray
argument_list|()
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tusks"
argument_list|,
name|e
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"after items"
argument_list|,
name|arrayService
operator|.
name|getAfterValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSubmitW3CArray
parameter_list|()
throws|throws
name|Exception
block|{
name|addNamespace
argument_list|(
literal|"a"
argument_list|,
literal|"urn:Array"
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"iam"
argument_list|,
literal|"uri:iam"
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"linux"
argument_list|,
literal|"uri:linux"
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"planets"
argument_list|,
literal|"uri:planets"
argument_list|)
expr_stmt|;
name|invoke
argument_list|(
literal|"Array"
argument_list|,
literal|"/org/apache/cxf/aegis/integration/anyTypeArrayW3C.xml"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"before items"
argument_list|,
name|arrayService
operator|.
name|getBeforeValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|arrayService
operator|.
name|getW3cArray
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
name|e
init|=
name|arrayService
operator|.
name|getW3cArray
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|assertValid
argument_list|(
literal|"/a:anyType/iam:walrus"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"after items"
argument_list|,
name|arrayService
operator|.
name|getAfterValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

