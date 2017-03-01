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
name|tools
operator|.
name|wsdlto
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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileWriter
import|;
end_import

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
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|codemodel
operator|.
name|CodeWriter
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
name|tools
operator|.
name|common
operator|.
name|ToolConstants
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
name|tools
operator|.
name|common
operator|.
name|ToolException
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
name|tools
operator|.
name|wsdlto
operator|.
name|AbstractCodeGenTest
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
name|WSDLRuntimeException
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
name|CodeGenOptionTest
extends|extends
name|AbstractCodeGenTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testFlagForGenStandAlone
parameter_list|()
throws|throws
name|Exception
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_TYPES
argument_list|,
name|ToolConstants
operator|.
name|CFG_GEN_TYPES
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_SEI
argument_list|,
name|ToolConstants
operator|.
name|CFG_GEN_SEI
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_IMPL
argument_list|,
name|ToolConstants
operator|.
name|CFG_GEN_IMPL
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_SERVICE
argument_list|,
name|ToolConstants
operator|.
name|CFG_GEN_SERVICE
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_SERVER
argument_list|,
name|ToolConstants
operator|.
name|CFG_GEN_SERVER
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_FAULT
argument_list|,
name|ToolConstants
operator|.
name|CFG_GEN_FAULT
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_ANT
argument_list|,
name|ToolConstants
operator|.
name|CFG_GEN_ANT
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|getLocation
argument_list|(
literal|"/wsdl2java_wsdl/hello_world.wsdl"
argument_list|)
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|processor
operator|.
name|execute
argument_list|()
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|greeterServer
init|=
name|classLoader
operator|.
name|loadClass
argument_list|(
literal|"org.apache.cxf.w2j.hello_world_soap_http.Greeter_SoapPort_Server"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Server should be generated"
argument_list|,
name|greeterServer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFlagForGenAdditional
parameter_list|()
throws|throws
name|Exception
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_IMPL
argument_list|,
name|ToolConstants
operator|.
name|CFG_IMPL
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_SERVER
argument_list|,
name|ToolConstants
operator|.
name|CFG_SERVER
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|getLocation
argument_list|(
literal|"/wsdl2java_wsdl/hello_world.wsdl"
argument_list|)
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|processor
operator|.
name|execute
argument_list|()
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|greeterServer
init|=
name|classLoader
operator|.
name|loadClass
argument_list|(
literal|"org.apache.cxf.w2j.hello_world_soap_http.Greeter_SoapPort_Server"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Server should be generated"
argument_list|,
name|greeterServer
argument_list|)
expr_stmt|;
block|}
comment|/**      * Testing if the wsdlList option is used then all wsdls from a given file gets processed.      */
annotation|@
name|Test
specifier|public
name|void
name|testWSDLListOptionMultipleWSDL
parameter_list|()
throws|throws
name|Exception
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_ALL
argument_list|,
name|ToolConstants
operator|.
name|CFG_ALL
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLLIST
argument_list|,
name|ToolConstants
operator|.
name|CFG_WSDLLIST
argument_list|)
expr_stmt|;
comment|// Getting the full path of the wsdl
name|String
name|wsdl1
init|=
name|getLocation
argument_list|(
literal|"/wsdl2java_wsdl/hello_world.wsdl"
argument_list|)
decl_stmt|;
name|String
name|wsdl2
init|=
name|getLocation
argument_list|(
literal|"/wsdl2java_wsdl/cardealer.wsdl"
argument_list|)
decl_stmt|;
name|doWSDLListOptionTest
argument_list|(
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|wsdl1
argument_list|,
name|wsdl2
argument_list|)
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|greeterServer
init|=
name|classLoader
operator|.
name|loadClass
argument_list|(
literal|"org.apache.cxf.w2j.hello_world_soap_http.Greeter_SoapPort_Server"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Server should be generated"
argument_list|,
name|greeterServer
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|carDealerServer
init|=
name|classLoader
operator|.
name|loadClass
argument_list|(
literal|"type_substitution.server.CarDealer_CarDealerPort_Server"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Server should be generated"
argument_list|,
name|carDealerServer
argument_list|)
expr_stmt|;
block|}
comment|/**      * Testing if the wsdlList option is used and the file contains only one WSDL, then that WSDL is processed      */
annotation|@
name|Test
specifier|public
name|void
name|testWSDLListOptionOneWSDL
parameter_list|()
throws|throws
name|Exception
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_ALL
argument_list|,
name|ToolConstants
operator|.
name|CFG_ALL
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLLIST
argument_list|,
name|ToolConstants
operator|.
name|CFG_WSDLLIST
argument_list|)
expr_stmt|;
comment|// Getting the full path of the wsdl
name|String
name|wsdl1
init|=
name|getLocation
argument_list|(
literal|"/wsdl2java_wsdl/hello_world.wsdl"
argument_list|)
decl_stmt|;
name|doWSDLListOptionTest
argument_list|(
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|wsdl1
argument_list|)
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|greeterServer
init|=
name|classLoader
operator|.
name|loadClass
argument_list|(
literal|"org.apache.cxf.w2j.hello_world_soap_http.Greeter_SoapPort_Server"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Server should be generated"
argument_list|,
name|greeterServer
argument_list|)
expr_stmt|;
block|}
comment|/**      * Testing if the wsdlList option is used and the file contains an incorrect WSDL, then an exception is      * thrown      */
annotation|@
name|Test
specifier|public
name|void
name|testWSDLListOptionIncorrectWSDLUrl
parameter_list|()
throws|throws
name|Exception
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_ALL
argument_list|,
name|ToolConstants
operator|.
name|CFG_ALL
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLLIST
argument_list|,
name|ToolConstants
operator|.
name|CFG_WSDLLIST
argument_list|)
expr_stmt|;
comment|// Getting the full path of the wsdl
name|String
name|wsdl1
init|=
name|getLocation
argument_list|(
literal|"/wsdl2java_wsdl/hello_world.wsdl"
argument_list|)
decl_stmt|;
try|try
block|{
name|doWSDLListOptionTest
argument_list|(
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|wsdl1
argument_list|,
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSDLRuntimeException
name|e
parameter_list|)
block|{
return|return;
block|}
name|fail
argument_list|()
expr_stmt|;
block|}
comment|/**      * Testing if the wsdlList option is used and a file that does not exist is specified, then an exception      * occurs      */
annotation|@
name|Test
specifier|public
name|void
name|testWSDLListOptionIncorrectFile
parameter_list|()
throws|throws
name|Exception
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_ALL
argument_list|,
name|ToolConstants
operator|.
name|CFG_ALL
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLLIST
argument_list|,
name|ToolConstants
operator|.
name|CFG_WSDLLIST
argument_list|)
expr_stmt|;
comment|// Getting the full path of the wsdl
name|String
name|wsdl1
init|=
name|getLocation
argument_list|(
literal|"/wsdl2java_wsdl/hello_world.wsdl"
argument_list|)
decl_stmt|;
try|try
block|{
name|doWSDLListOptionTest
argument_list|(
literal|"/Temp/temp.txt"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|wsdl1
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ToolException
name|e
parameter_list|)
block|{
return|return;
block|}
name|fail
argument_list|()
expr_stmt|;
block|}
comment|/**      * Performs the WSDLList option test for the specified list of parameters.      *      * @param wsdlURL The url of the wsdlList. Can be null.      * @param wsdls      * @throws IOException      * @throws ToolException      */
specifier|private
name|void
name|doWSDLListOptionTest
parameter_list|(
name|String
name|wsdlURL
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|wsdls
parameter_list|)
throws|throws
name|IOException
throws|,
name|ToolException
block|{
name|File
name|file
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|wsdlURL
operator|==
literal|null
condition|)
block|{
comment|// Creating a file containing a list of wsdls URLs in a temp folder
name|file
operator|=
name|tmpDir
operator|.
name|newFile
argument_list|(
literal|"wsdl_list.txt"
argument_list|)
expr_stmt|;
name|PrintWriter
name|writer
init|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|FileWriter
argument_list|(
name|file
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|wsdl
range|:
name|wsdls
control|)
block|{
name|writer
operator|.
name|println
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|wsdlURL
operator|=
name|file
operator|.
name|getPath
argument_list|()
expr_stmt|;
block|}
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|wsdlURL
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
try|try
block|{
name|processor
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHelloWorldExternalBindingFile
parameter_list|()
throws|throws
name|Exception
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|getLocation
argument_list|(
literal|"/wsdl2java_wsdl/hello_world_jaxws_base.wsdl"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_BINDING
argument_list|,
name|getLocation
argument_list|(
literal|"/wsdl2java_wsdl/hello_world_jaxws_binding.wsdl"
argument_list|)
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|processor
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|File
name|org
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"org"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|org
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|apache
init|=
operator|new
name|File
argument_list|(
name|org
argument_list|,
literal|"apache"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|apache
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clz
init|=
name|classLoader
operator|.
name|loadClass
argument_list|(
literal|"org.apache.cxf.w2j.hello_world_async_soap_http.GreeterAsync"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|clz
operator|.
name|getMethods
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGenFault
parameter_list|()
throws|throws
name|Exception
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|getLocation
argument_list|(
literal|"/wsdl2java_wsdl/hello_world.wsdl"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|remove
argument_list|(
name|ToolConstants
operator|.
name|CFG_COMPILE
argument_list|)
expr_stmt|;
name|env
operator|.
name|remove
argument_list|(
name|ToolConstants
operator|.
name|CFG_IMPL
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_FAULT
argument_list|,
name|ToolConstants
operator|.
name|CFG_GEN_FAULT
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|processor
operator|.
name|execute
argument_list|()
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"org/apache/cxf/w2j/hello_world_soap_http"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|file
operator|.
name|list
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetCatalog
parameter_list|()
throws|throws
name|Exception
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|getLocation
argument_list|(
literal|"/wsdl2java_wsdl/test_catalog_replaceme.wsdl"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_CATALOG
argument_list|,
name|getLocation
argument_list|(
literal|"/wsdl2java_wsdl/test_catalog.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_COMPILE
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLASSDIR
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|processor
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetCatalogPublic
parameter_list|()
throws|throws
name|Exception
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|getLocation
argument_list|(
literal|"/wsdl2java_wsdl/cxf1053/myservice.wsdl"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_CATALOG
argument_list|,
name|getLocation
argument_list|(
literal|"/wsdl2java_wsdl/cxf1053/catalog.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_COMPILE
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLASSDIR
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|processor
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
comment|/**      * Tests that, when 'mark-generated' option is set, @Generated annotations are inserted in all generated      * java classes.      */
annotation|@
name|Test
specifier|public
name|void
name|testMarkGeneratedOption
parameter_list|()
throws|throws
name|Exception
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|getLocation
argument_list|(
literal|"/wsdl2java_wsdl/hello_world.wsdl"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_MARK_GENERATED
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_COMPILE
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLASSDIR
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|processor
operator|.
name|execute
argument_list|()
expr_stmt|;
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"org"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"org directory is not found"
argument_list|,
name|dir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|dir
operator|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"apache"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"apache directory is not found"
argument_list|,
name|dir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|dir
operator|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"cxf"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"cxf directory is not found"
argument_list|,
name|dir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|dir
operator|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"w2j"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"w2j directory is not found"
argument_list|,
name|dir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|dir
operator|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"hello_world_soap_http"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"hello_world_soap_http directory is not found"
argument_list|,
name|dir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|types
init|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"types"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"types directory is not found"
argument_list|,
name|dir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|str
init|=
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"Greeter.java"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|countGeneratedAnnotations
argument_list|(
name|str
argument_list|)
argument_list|)
expr_stmt|;
name|str
operator|=
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|types
argument_list|,
literal|"SayHi.java"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|countGeneratedAnnotations
argument_list|(
name|str
argument_list|)
argument_list|)
expr_stmt|;
name|str
operator|=
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|types
argument_list|,
literal|"SayHiResponse.java"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|countGeneratedAnnotations
argument_list|(
name|str
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|int
name|countGeneratedAnnotations
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
name|int
name|idx
init|=
name|str
operator|.
name|indexOf
argument_list|(
literal|"@Generated"
argument_list|)
decl_stmt|;
while|while
condition|(
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|count
operator|++
expr_stmt|;
name|idx
operator|=
name|str
operator|.
name|indexOf
argument_list|(
literal|"@Generated"
argument_list|,
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|count
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResourceURLForWsdlLocation
parameter_list|()
throws|throws
name|Exception
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|getLocation
argument_list|(
literal|"/wsdl2java_wsdl/hello_world.wsdl"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLLOCATION
argument_list|,
literal|"/wsdl2java_wsdl/hello_world.wsdl"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_COMPILE
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLASSDIR
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|processor
operator|.
name|execute
argument_list|()
expr_stmt|;
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"org"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"org directory is not found"
argument_list|,
name|dir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|dir
operator|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"apache"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"apache directory is not found"
argument_list|,
name|dir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|dir
operator|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"cxf"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"cxf directory is not found"
argument_list|,
name|dir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|dir
operator|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"w2j"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"w2j directory is not found"
argument_list|,
name|dir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|dir
operator|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"hello_world_soap_http"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"hello_world_soap_http directory is not found"
argument_list|,
name|dir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|str
init|=
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"SOAPService.java"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|str
argument_list|,
name|str
operator|.
name|contains
argument_list|(
literal|"getResource"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncoding
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|CodeWriter
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"encoding"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//version of jaxb that doesn't support this.  We'll just not run the test.
return|return;
block|}
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|getLocation
argument_list|(
literal|"/wsdl2java_wsdl/hello_world_encoding.wsdl"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLLOCATION
argument_list|,
literal|"/wsdl2java_wsdl/hello_world_encoding.wsdl"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_ENCODING
argument_list|,
literal|"Cp1251"
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|processor
operator|.
name|execute
argument_list|()
expr_stmt|;
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"org"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"org directory is not found"
argument_list|,
name|dir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|dir
operator|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"apache"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"apache directory is not found"
argument_list|,
name|dir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|dir
operator|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"cxf"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"cxf directory is not found"
argument_list|,
name|dir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|dir
operator|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"w2j"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"w2j directory is not found"
argument_list|,
name|dir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|dir
operator|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"hello_world_soap_http"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"hello_world_soap_http directory is not found"
argument_list|,
name|dir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|str
init|=
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"SOAPService.java"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|str
argument_list|,
name|str
operator|.
name|contains
argument_list|(
literal|"getResource"
argument_list|)
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clz
init|=
name|classLoader
operator|.
name|loadClass
argument_list|(
literal|"org.apache.cxf.w2j.hello_world_soap_http.Greeter"
argument_list|)
decl_stmt|;
for|for
control|(
name|Method
name|m
range|:
name|clz
operator|.
name|getMethods
argument_list|()
control|)
block|{
name|String
name|s
init|=
name|m
operator|.
name|getName
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1039
argument_list|,
name|s
operator|.
name|charAt
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

