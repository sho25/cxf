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
name|jaxws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|WSDLException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|factory
operator|.
name|WSDLFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|xml
operator|.
name|WSDLReader
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
name|Endpoint
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
name|WebServiceException
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
name|BusFactory
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
name|catalog
operator|.
name|OASISCatalogManager
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
name|cxf
operator|.
name|wsdl
operator|.
name|WSDLManager
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
name|wsdl11
operator|.
name|CatalogWSDLLocator
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
name|wsdl11
operator|.
name|WSDLManagerImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world
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
name|hello_world
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
name|hello_world
operator|.
name|services
operator|.
name|SOAPService
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
name|OASISCatalogTest
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
name|OASISCatalogTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world/services"
argument_list|,
literal|"SOAPService"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world/services"
argument_list|,
literal|"SoapPort"
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testWSDLPublishWithCatalogs
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|ep
init|=
name|Endpoint
operator|.
name|publish
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort"
argument_list|,
operator|new
name|GreeterImpl
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|String
name|result
init|=
name|readUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort?"
operator|+
literal|"xsd=hello_world_schema2.xsd"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
argument_list|,
name|result
operator|.
name|contains
argument_list|(
literal|"xsd=hello_world_schema.xsd"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
argument_list|,
name|result
operator|.
name|contains
argument_list|(
literal|"xsd=hello_world_schema3.xsd"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
argument_list|,
name|result
operator|.
name|contains
argument_list|(
literal|"xsd=d/hello_world_schema4.xsd"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
name|readUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort?"
operator|+
literal|"xsd=hello_world_schema3.xsd"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|length
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|result
operator|=
name|readUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort?"
operator|+
literal|"xsd=d/hello_world_schema4.xsd"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
argument_list|,
name|result
operator|.
name|contains
argument_list|(
literal|"xsd=d/d/hello_world_schema4.xsd"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
name|readUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort"
operator|+
literal|"?xsd=hello_world_schema.xsd"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
argument_list|,
name|result
operator|.
name|contains
argument_list|(
literal|"xsd=http://apache.org/hello_world/types2/hello_world_schema2.xsd"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
name|readUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort"
operator|+
literal|"?wsdl=hello_world_messages_catalog.wsdl"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
argument_list|,
name|result
operator|.
name|contains
argument_list|(
literal|"xsd=hello_world_schema.xsd"
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|ep
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * This is test case for https://issues.apache.org/jira/browse/CXF-6234      *      * It's using paths that will be rewritten by following catalog rule:      *      *&lt;rewriteSystem systemIdStartString="http://apache.org/hello_world/types2/"      *          rewritePrefix="/wsdl/others/"/&gt;      *      */
annotation|@
name|Test
specifier|public
name|void
name|testWSDLPublishWithCatalogsRewritePaths
parameter_list|()
block|{
name|Endpoint
name|ep
init|=
name|Endpoint
operator|.
name|publish
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort"
argument_list|,
operator|new
name|GreeterImpl
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
comment|// schemas in the same directory as WSDL
name|String
name|result
init|=
name|readUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort?"
operator|+
literal|"xsd=http://apache.org/hello_world/types2/hello_world_schema2.xsd"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
argument_list|,
name|result
operator|.
name|contains
argument_list|(
literal|"xsd=http://apache.org/hello_world/types2/hello_world_schema.xsd"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
argument_list|,
name|result
operator|.
name|contains
argument_list|(
literal|"xsd=http://apache.org/hello_world/types2/hello_world_schema3.xsd"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
argument_list|,
name|result
operator|.
name|contains
argument_list|(
literal|"xsd=http://apache.org/hello_world/types2/d/hello_world_schema4.xsd"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
name|readUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort?"
operator|+
literal|"xsd=http://apache.org/hello_world/types2/hello_world_schema.xsd"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
argument_list|,
name|result
operator|.
name|contains
argument_list|(
literal|"xsd=http://apache.org/hello_world/types2/hello_world_schema2.xsd"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
name|readUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort?"
operator|+
literal|"xsd=http://apache.org/hello_world/types2/hello_world_schema3.xsd"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|length
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|result
operator|=
name|readUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort?"
operator|+
literal|"xsd=http://apache.org/hello_world/types2/d/hello_world_schema4.xsd"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
argument_list|,
name|result
operator|.
name|contains
argument_list|(
literal|"xsd=http://apache.org/hello_world/types2/d/d/hello_world_schema4.xsd"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
name|readUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort?"
operator|+
literal|"xsd=http://apache.org/hello_world/types2/d/d/hello_world_schema4.xsd"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|result
operator|.
name|contains
argument_list|(
literal|"schemaLocation"
argument_list|)
argument_list|)
expr_stmt|;
comment|// schemas in separate directory which is not subdirectory of WSDL dir
name|result
operator|=
name|readUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort?"
operator|+
literal|"wsdl=http://apache.org/hello_world/types2/hello_world_messages_catalog.wsdl"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
argument_list|,
name|result
operator|.
name|contains
argument_list|(
literal|"xsd=http://apache.org/hello_world/schemas-in-separate-dir/schema.xsd"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
name|readUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort?"
operator|+
literal|"xsd=http://apache.org/hello_world/schemas-in-separate-dir/schema.xsd"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
argument_list|,
name|result
operator|.
name|contains
argument_list|(
literal|"xsd=http://apache.org/hello_world/schemas-in-separate-dir/d/included.xsd"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
name|readUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort?"
operator|+
literal|"xsd=http://apache.org/hello_world/schemas-in-separate-dir/d/included.xsd"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
argument_list|,
name|result
operator|.
name|contains
argument_list|(
literal|"xsd=http://apache.org/hello_world/schemas-in-separate-dir/d/d/included.xsd"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
name|readUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort?"
operator|+
literal|"xsd=http://apache.org/hello_world/schemas-in-separate-dir/d/d/included.xsd"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|result
argument_list|,
name|result
operator|.
name|contains
argument_list|(
literal|"schemaLocation"
argument_list|)
argument_list|)
expr_stmt|;
comment|// rewrite rule that doesn't begin with 'classpath:' but contains only the path
name|result
operator|=
name|readUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort?"
operator|+
literal|"xsd=http://apache.org/hello_world/schemas-in-separate-dir-non-cp/another-schema.xsd"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
argument_list|,
name|result
operator|.
name|contains
argument_list|(
literal|"xsd=http://apache.org/hello_world/schemas-in-separate-dir-non-cp/d/"
operator|+
literal|"another-included.xsd"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
name|readUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort?"
operator|+
literal|"xsd=http://apache.org/hello_world/schemas-in-separate-dir-non-cp/d/another-included.xsd"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
argument_list|,
name|result
operator|.
name|contains
argument_list|(
literal|"xsd=http://apache.org/hello_world/schemas-in-separate-dir-non-cp/d/d/"
operator|+
literal|"another-included.xsd"
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|ep
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClientWithDefaultCatalog
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/catalog/hello_world_services.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|Greeter
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|greeter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClientWithoutCatalog
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/catalog/hello_world_services.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
comment|// set Catalog on the Bus
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
name|OASISCatalogManager
name|catalog
init|=
operator|new
name|OASISCatalogManager
argument_list|()
decl_stmt|;
name|bus
operator|.
name|setExtension
argument_list|(
name|catalog
argument_list|,
name|OASISCatalogManager
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// prevent cache from papering over the lack of a schema.
name|WSDLManagerImpl
name|mgr
init|=
operator|(
name|WSDLManagerImpl
operator|)
name|bus
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|mgr
operator|.
name|setDisableSchemaCache
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Test did not fail as expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
comment|// update catalog dynamically now
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|jaxwscatalog
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResources
argument_list|(
literal|"META-INF/jax-ws-catalog.xml"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|jaxwscatalog
argument_list|)
expr_stmt|;
while|while
condition|(
name|jaxwscatalog
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|URL
name|url
init|=
name|jaxwscatalog
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|catalog
operator|.
name|loadCatalog
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|Greeter
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|greeter
argument_list|)
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWSDLLocatorWithDefaultCatalog
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/catalog/hello_world_services.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|WSDLFactory
name|wsdlFactory
init|=
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|WSDLReader
name|wsdlReader
init|=
name|wsdlFactory
operator|.
name|newWSDLReader
argument_list|()
decl_stmt|;
name|CatalogWSDLLocator
name|wsdlLocator
init|=
operator|new
name|CatalogWSDLLocator
argument_list|(
name|wsdl
operator|.
name|toString
argument_list|()
argument_list|,
name|OASISCatalogManager
operator|.
name|getCatalogManager
argument_list|(
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|wsdlReader
operator|.
name|setFeature
argument_list|(
literal|"javax.wsdl.verbose"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|wsdlReader
operator|.
name|readWSDL
argument_list|(
name|wsdlLocator
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWSDLLocatorWithoutCatalog
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/catalog/hello_world_services.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|WSDLFactory
name|wsdlFactory
init|=
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|WSDLReader
name|wsdlReader
init|=
name|wsdlFactory
operator|.
name|newWSDLReader
argument_list|()
decl_stmt|;
name|wsdlReader
operator|.
name|setFeature
argument_list|(
literal|"javax.wsdl.verbose"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|OASISCatalogManager
name|catalog
init|=
operator|new
name|OASISCatalogManager
argument_list|()
decl_stmt|;
name|CatalogWSDLLocator
name|wsdlLocator
init|=
operator|new
name|CatalogWSDLLocator
argument_list|(
name|wsdl
operator|.
name|toString
argument_list|()
argument_list|,
name|catalog
argument_list|)
decl_stmt|;
try|try
block|{
name|wsdlReader
operator|.
name|readWSDL
argument_list|(
name|wsdlLocator
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Test did not fail as expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSDLException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
specifier|private
name|String
name|readUrl
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|String
name|content
init|=
literal|null
decl_stmt|;
try|try
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|address
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|url
operator|.
name|getContent
argument_list|()
argument_list|)
expr_stmt|;
name|content
operator|=
name|IOUtils
operator|.
name|toString
argument_list|(
operator|(
name|InputStream
operator|)
name|url
operator|.
name|getContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|(
literal|"Couldn't read URL: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|content
return|;
block|}
block|}
end_class

end_unit

