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
name|aegis
operator|.
name|mtom
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Attr
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
name|aegis
operator|.
name|databinding
operator|.
name|AegisDatabinding
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
name|type
operator|.
name|mtom
operator|.
name|AbstractXOPType
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
name|helpers
operator|.
name|IOUtils
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
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
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
name|aegis
operator|.
name|mtom
operator|.
name|fortest
operator|.
name|DataHandlerBean
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
name|aegis
operator|.
name|mtom
operator|.
name|fortest
operator|.
name|MtomTestImpl
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
name|aegis
operator|.
name|mtom
operator|.
name|fortest
operator|.
name|MtomTestService
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
name|apache
operator|.
name|cxf
operator|.
name|testutil
operator|.
name|common
operator|.
name|TestUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|constants
operator|.
name|Constants
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

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"classpath:mtomTestBeans.xml"
block|}
argument_list|)
specifier|public
class|class
name|MtomTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|MtomTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|MtomTestImpl
name|impl
decl_stmt|;
specifier|private
name|MtomTestService
name|client
decl_stmt|;
specifier|private
name|MtomTestService
name|jaxwsClient
decl_stmt|;
specifier|private
name|TestUtilities
name|testUtilities
decl_stmt|;
specifier|public
name|MtomTest
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
specifier|private
name|void
name|setupForTest
parameter_list|(
name|boolean
name|enableClientMTOM
parameter_list|)
throws|throws
name|Exception
block|{
name|AegisDatabinding
name|aegisBinding
init|=
operator|new
name|AegisDatabinding
argument_list|()
decl_stmt|;
name|aegisBinding
operator|.
name|setMtomEnabled
argument_list|(
name|enableClientMTOM
argument_list|)
expr_stmt|;
name|ClientProxyFactoryBean
name|proxyFac
init|=
operator|new
name|ClientProxyFactoryBean
argument_list|()
decl_stmt|;
name|proxyFac
operator|.
name|setDataBinding
argument_list|(
name|aegisBinding
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/mtom"
argument_list|)
expr_stmt|;
name|JaxWsProxyFactoryBean
name|jaxwsFac
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|jaxwsFac
operator|.
name|setDataBinding
argument_list|(
operator|new
name|AegisDatabinding
argument_list|()
argument_list|)
expr_stmt|;
name|jaxwsFac
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/jaxWsMtom"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|enableClientMTOM
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
literal|"mtom-enabled"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
name|proxyFac
operator|.
name|setProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|client
operator|=
name|proxyFac
operator|.
name|create
argument_list|(
name|MtomTestService
operator|.
name|class
argument_list|)
expr_stmt|;
name|jaxwsClient
operator|=
name|jaxwsFac
operator|.
name|create
argument_list|(
name|MtomTestService
operator|.
name|class
argument_list|)
expr_stmt|;
name|impl
operator|=
operator|(
name|MtomTestImpl
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"mtomImpl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMtomReply
parameter_list|()
throws|throws
name|Exception
block|{
name|setupForTest
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DataHandlerBean
name|dhBean
init|=
name|client
operator|.
name|produceDataHandlerBean
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|dhBean
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|dhBean
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getInputStream
argument_list|()
argument_list|,
literal|"utf-8"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|MtomTestImpl
operator|.
name|STRING_DATA
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
comment|//TODO: how do we see if MTOM actually happened?
annotation|@
name|Test
specifier|public
name|void
name|testJaxWsMtomReply
parameter_list|()
throws|throws
name|Exception
block|{
name|setupForTest
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DataHandlerBean
name|dhBean
init|=
name|jaxwsClient
operator|.
name|produceDataHandlerBean
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|dhBean
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|dhBean
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getInputStream
argument_list|()
argument_list|,
literal|"utf-8"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|MtomTestImpl
operator|.
name|STRING_DATA
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAcceptDataHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|setupForTest
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DataHandlerBean
name|dhBean
init|=
operator|new
name|DataHandlerBean
argument_list|()
decl_stmt|;
name|dhBean
operator|.
name|setName
argument_list|(
literal|"some name"
argument_list|)
expr_stmt|;
comment|// some day, we might need this to be higher than some threshold.
name|String
name|someData
init|=
literal|"This is the cereal shot from guns."
decl_stmt|;
name|DataHandler
name|dataHandler
init|=
operator|new
name|DataHandler
argument_list|(
name|someData
argument_list|,
literal|"text/plain;charset=utf-8"
argument_list|)
decl_stmt|;
name|dhBean
operator|.
name|setDataHandler
argument_list|(
name|dataHandler
argument_list|)
expr_stmt|;
name|client
operator|.
name|acceptDataHandler
argument_list|(
name|dhBean
argument_list|)
expr_stmt|;
name|DataHandlerBean
name|accepted
init|=
name|impl
operator|.
name|getLastDhBean
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|accepted
argument_list|)
expr_stmt|;
name|Object
name|o
init|=
name|accepted
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getContent
argument_list|()
decl_stmt|;
name|String
name|data
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
name|data
operator|=
operator|(
name|String
operator|)
name|o
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|InputStream
condition|)
block|{
name|data
operator|=
name|IOUtils
operator|.
name|toString
argument_list|(
operator|(
name|InputStream
operator|)
name|o
argument_list|)
expr_stmt|;
block|}
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"This is the cereal shot from guns."
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAcceptDataHandlerNoMTOM
parameter_list|()
throws|throws
name|Exception
block|{
name|setupForTest
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|DataHandlerBean
name|dhBean
init|=
operator|new
name|DataHandlerBean
argument_list|()
decl_stmt|;
name|dhBean
operator|.
name|setName
argument_list|(
literal|"some name"
argument_list|)
expr_stmt|;
comment|// some day, we might need this to be longer than some threshold.
name|String
name|someData
init|=
literal|"This is the cereal shot from guns."
decl_stmt|;
name|DataHandler
name|dataHandler
init|=
operator|new
name|DataHandler
argument_list|(
name|someData
argument_list|,
literal|"text/plain;charset=utf-8"
argument_list|)
decl_stmt|;
name|dhBean
operator|.
name|setDataHandler
argument_list|(
name|dataHandler
argument_list|)
expr_stmt|;
name|client
operator|.
name|acceptDataHandler
argument_list|(
name|dhBean
argument_list|)
expr_stmt|;
name|DataHandlerBean
name|accepted
init|=
name|impl
operator|.
name|getLastDhBean
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|accepted
argument_list|)
expr_stmt|;
name|InputStream
name|data
init|=
name|accepted
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|String
name|dataString
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|data
argument_list|,
literal|"utf-8"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"This is the cereal shot from guns."
argument_list|,
name|dataString
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMtomSchema
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
name|testUtilities
operator|.
name|addNamespace
argument_list|(
literal|"xmime"
argument_list|,
literal|"http://www.w3.org/2005/05/xmlmime"
argument_list|)
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
literal|"http://fortest.mtom.aegis.systest.cxf.apache.org/"
argument_list|,
literal|"MtomTestService"
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
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|NodeList
name|typeAttrList
init|=
name|testUtilities
operator|.
name|assertValid
argument_list|(
literal|"//xsd:complexType[@name='inputDhBean']/xsd:sequence/"
operator|+
literal|"xsd:element[@name='dataHandler']/"
operator|+
literal|"@type"
argument_list|,
name|wsdl
argument_list|)
decl_stmt|;
name|Attr
name|typeAttr
init|=
operator|(
name|Attr
operator|)
name|typeAttrList
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|typeAttrValue
init|=
name|typeAttr
operator|.
name|getValue
argument_list|()
decl_stmt|;
comment|// now, this thing is a qname with a :, and we have to work out if it's correct.
name|String
index|[]
name|pieces
init|=
name|typeAttrValue
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"base64Binary"
argument_list|,
name|pieces
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|Node
name|elementNode
init|=
name|typeAttr
operator|.
name|getOwnerElement
argument_list|()
decl_stmt|;
name|String
name|url
init|=
name|testUtilities
operator|.
name|resolveNamespacePrefix
argument_list|(
name|pieces
index|[
literal|0
index|]
argument_list|,
name|elementNode
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Constants
operator|.
name|URI_2001_SCHEMA_XSD
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|s
operator|=
name|testUtilities
operator|.
name|getServerForAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/mtomXmime"
argument_list|)
expr_stmt|;
name|wsdl
operator|=
name|testUtilities
operator|.
name|getWSDLDocument
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|typeAttrList
operator|=
name|testUtilities
operator|.
name|assertValid
argument_list|(
literal|"//xsd:complexType[@name='inputDhBean']/xsd:sequence/"
operator|+
literal|"xsd:element[@name='dataHandler']/"
operator|+
literal|"@type"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|typeAttr
operator|=
operator|(
name|Attr
operator|)
name|typeAttrList
operator|.
name|item
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|typeAttrValue
operator|=
name|typeAttr
operator|.
name|getValue
argument_list|()
expr_stmt|;
comment|// now, this thing is a qname with a :, and we have to work out if it's correct.
name|pieces
operator|=
name|typeAttrValue
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"base64Binary"
argument_list|,
name|pieces
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|elementNode
operator|=
name|typeAttr
operator|.
name|getOwnerElement
argument_list|()
expr_stmt|;
name|url
operator|=
name|testUtilities
operator|.
name|resolveNamespacePrefix
argument_list|(
name|pieces
index|[
literal|0
index|]
argument_list|,
name|elementNode
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|AbstractXOPType
operator|.
name|XML_MIME_NS
argument_list|,
name|url
argument_list|)
expr_stmt|;
comment|/* when I add a test for a custom mapping.         testUtilities.assertValid("//xsd:complexType[@name='inputDhBean']/xsd:sequence/"                                   + "xsd:element[@name='dataHandler']/"                                   + "@xmime:expectedContentType/text()",                                   wsdl);                                   */
block|}
block|}
end_class

end_unit

