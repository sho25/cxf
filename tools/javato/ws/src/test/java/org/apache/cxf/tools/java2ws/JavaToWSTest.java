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
name|java2ws
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
name|net
operator|.
name|URISyntaxException
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
name|net
operator|.
name|URLClassLoader
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
name|common
operator|.
name|util
operator|.
name|Compiler
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
name|FileUtils
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
name|ToolContext
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
name|ToolTestBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|JavaToWSTest
extends|extends
name|ToolTestBase
block|{
specifier|protected
name|String
name|cp
decl_stmt|;
specifier|protected
name|ToolContext
name|env
decl_stmt|;
specifier|protected
name|File
name|output
decl_stmt|;
specifier|protected
name|File
name|classDir
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUpResource
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|env
operator|=
operator|new
name|ToolContext
argument_list|()
expr_stmt|;
name|cp
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.class.path"
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
name|output
operator|=
operator|new
name|File
argument_list|(
name|url
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.class.path"
argument_list|,
name|getClassPath
argument_list|()
argument_list|)
expr_stmt|;
name|output
operator|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"/generated/"
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|mkDir
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|classDir
operator|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"/classes/"
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|mkDir
argument_list|(
name|classDir
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.class.path"
argument_list|,
name|cp
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|removeDir
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|output
operator|=
literal|null
expr_stmt|;
block|}
specifier|private
name|File
name|outputFile
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|output
operator|.
name|getPath
argument_list|()
operator|+
name|File
operator|.
name|separator
operator|+
name|name
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testVersionOutput
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-v"
block|}
decl_stmt|;
name|JavaToWS
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|getStdOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFlagWSDL
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|wsdlFile
init|=
name|outputFile
argument_list|(
literal|"tmp.wsdl"
argument_list|)
decl_stmt|;
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-wsdl"
block|,
literal|"-o"
block|,
name|wsdlFile
operator|.
name|getAbsolutePath
argument_list|()
block|,
literal|"-s"
block|,
name|output
operator|.
name|getPath
argument_list|()
block|,
literal|"-client"
block|,
literal|"-server"
block|,
literal|"org.apache.hello_world_soap12_http.Greeter"
block|}
decl_stmt|;
name|JavaToWS
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|checkStdErr
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed to generate WSDL file"
argument_list|,
name|wsdlFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkStdErr
parameter_list|()
block|{
name|String
name|err
init|=
name|getStdErr
argument_list|()
decl_stmt|;
if|if
condition|(
name|err
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
literal|"errors: "
argument_list|,
literal|""
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJaxwsFrontend
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|wsdlFile
init|=
name|outputFile
argument_list|(
literal|"tmp.wsdl"
argument_list|)
decl_stmt|;
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-wsdl"
block|,
literal|"-o"
block|,
name|output
operator|.
name|getPath
argument_list|()
operator|+
literal|"/tmp.wsdl"
block|,
literal|"-s"
block|,
name|output
operator|.
name|getPath
argument_list|()
block|,
literal|"-frontend"
block|,
literal|"jaxws"
block|,
literal|"-client"
block|,
literal|"-server"
block|,
literal|"-address"
block|,
literal|"http://localhost:1234/test"
block|,
literal|"org.apache.hello_world_doc_lit.Greeter"
block|}
decl_stmt|;
name|JavaToWS
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
comment|//checkStdErr();
name|assertTrue
argument_list|(
literal|"Failed to generate WSDL file"
argument_list|,
name|wsdlFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|str
init|=
name|FileUtils
operator|.
name|getStringFromFile
argument_list|(
name|wsdlFile
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Port address in generated wsdl is not correct"
argument_list|,
name|str
operator|.
name|indexOf
argument_list|(
literal|"http://localhost:1234/test"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
name|File
name|client
init|=
name|outputFile
argument_list|(
literal|"org/apache/hello_world_doc_lit/GreeterClient.java"
argument_list|)
decl_stmt|;
name|str
operator|=
name|FileUtils
operator|.
name|getStringFromFile
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Address generated in client side code is not correct"
argument_list|,
name|str
operator|.
name|indexOf
argument_list|(
literal|"http://localhost:1234/test"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
name|File
name|server
init|=
name|outputFile
argument_list|(
literal|"org/apache/hello_world_doc_lit/GreeterServer.java"
argument_list|)
decl_stmt|;
name|str
operator|=
name|FileUtils
operator|.
name|getStringFromFile
argument_list|(
name|server
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Address generated in server side code is not correct"
argument_list|,
name|str
operator|.
name|indexOf
argument_list|(
literal|"http://localhost:1234/test"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
name|File
name|impl
init|=
name|outputFile
argument_list|(
literal|"org/apache/hello_world_doc_lit/GreeterImpl.java"
argument_list|)
decl_stmt|;
name|Compiler
name|compiler
init|=
operator|new
name|Compiler
argument_list|()
decl_stmt|;
name|String
index|[]
name|files
init|=
operator|new
name|String
index|[]
block|{
name|client
operator|.
name|getAbsoluteFile
argument_list|()
operator|.
name|toString
argument_list|()
block|,
name|server
operator|.
name|getAbsoluteFile
argument_list|()
operator|.
name|toString
argument_list|()
block|,
name|impl
operator|.
name|getAbsoluteFile
argument_list|()
operator|.
name|toString
argument_list|()
block|}
decl_stmt|;
name|compiler
operator|.
name|compileFiles
argument_list|(
name|files
argument_list|,
name|this
operator|.
name|classDir
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimpleFrontend
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-wsdl"
block|,
literal|"-o"
block|,
name|output
operator|.
name|getPath
argument_list|()
operator|+
literal|"/tmp.wsdl"
block|,
literal|"-s"
block|,
name|output
operator|.
name|getPath
argument_list|()
block|,
literal|"-frontend"
block|,
literal|"simple"
block|,
literal|"-client"
block|,
literal|"-server"
block|,
literal|"-address"
block|,
literal|"http://localhost:1234/test"
block|,
literal|"org.apache.cxf.tools.fortest.simple.Hello"
block|}
decl_stmt|;
name|JavaToWS
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|File
name|client
init|=
name|outputFile
argument_list|(
literal|"org/apache/cxf/tools/fortest/simple/HelloPortTypeClient.java"
argument_list|)
decl_stmt|;
name|File
name|server
init|=
name|outputFile
argument_list|(
literal|"org/apache/cxf/tools/fortest/simple/HelloPortTypeServer.java"
argument_list|)
decl_stmt|;
name|File
name|impl
init|=
name|outputFile
argument_list|(
literal|"org/apache/cxf/tools/fortest/simple/HelloPortTypeImpl.java"
argument_list|)
decl_stmt|;
name|File
name|wsdl
init|=
name|outputFile
argument_list|(
literal|"tmp.wsdl"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Failed to generate client file for simple front end "
argument_list|,
name|client
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed to generate server file for simple front end "
argument_list|,
name|server
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed to generate impl file for simple front end "
argument_list|,
name|impl
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed to generate wsdl file for simple front end "
argument_list|,
name|wsdl
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|str
init|=
name|FileUtils
operator|.
name|getStringFromFile
argument_list|(
name|client
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Address generated in client side code is not correct"
argument_list|,
name|str
operator|.
name|indexOf
argument_list|(
literal|"http://localhost:1234/test"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
name|str
operator|=
name|FileUtils
operator|.
name|getStringFromFile
argument_list|(
name|server
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Address generated in server side code is not correct"
argument_list|,
name|str
operator|.
name|indexOf
argument_list|(
literal|"http://localhost:1234/test"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
name|str
operator|=
name|FileUtils
operator|.
name|getStringFromFile
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Address generated in wsdl is not correct"
argument_list|,
name|str
operator|.
name|indexOf
argument_list|(
literal|"http://localhost:1234/test"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
name|Compiler
name|compiler
init|=
operator|new
name|Compiler
argument_list|()
decl_stmt|;
name|String
index|[]
name|files
init|=
operator|new
name|String
index|[]
block|{
name|client
operator|.
name|getAbsoluteFile
argument_list|()
operator|.
name|toString
argument_list|()
block|,
name|server
operator|.
name|getAbsoluteFile
argument_list|()
operator|.
name|toString
argument_list|()
block|,
name|impl
operator|.
name|getAbsoluteFile
argument_list|()
operator|.
name|toString
argument_list|()
block|}
decl_stmt|;
name|compiler
operator|.
name|compileFiles
argument_list|(
name|files
argument_list|,
name|this
operator|.
name|classDir
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMissingBeans
parameter_list|()
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-wsdl"
block|,
literal|"-o"
block|,
name|output
operator|.
name|getPath
argument_list|()
operator|+
literal|"/tmp.wsdl"
block|,
literal|"-verbose"
block|,
literal|"-s"
block|,
name|output
operator|.
name|getPath
argument_list|()
block|,
literal|"-frontend"
block|,
literal|"jaxws"
block|,
literal|"-client"
block|,
literal|"-server"
block|,
literal|"-beans"
block|,
literal|"nobodyHome.xml"
block|,
literal|"-beans"
block|,
literal|"nothing.xml"
block|,
literal|"org.apache.hello_world_doc_lit.Greeter"
block|}
decl_stmt|;
name|JavaToWS
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|String
name|err
init|=
name|getStdErr
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Missing file error message"
argument_list|,
name|err
operator|.
name|indexOf
argument_list|(
literal|"Unable to open bean definition file nobodyHome.xml"
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClassNoWebServiceAnno
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|wsdlFile
init|=
name|outputFile
argument_list|(
literal|"tmp.wsdl"
argument_list|)
decl_stmt|;
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-wsdl"
block|,
literal|"-o"
block|,
name|output
operator|.
name|getPath
argument_list|()
operator|+
literal|"/tmp.wsdl"
block|,
literal|"-verbose"
block|,
literal|"-s"
block|,
name|output
operator|.
name|getPath
argument_list|()
block|,
literal|"-frontend"
block|,
literal|"jaxws"
block|,
literal|"-client"
block|,
literal|"-server"
block|,
literal|"org.apache.cxf.tools.fortest.HelloWithNoAnno"
block|}
decl_stmt|;
name|JavaToWS
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed to generate WSDL file"
argument_list|,
name|wsdlFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Class does not carry WebService error should be detected"
argument_list|,
name|getStdErr
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"does not carry a WebService annotation"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClassWithRMI
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|wsdlFile
init|=
name|outputFile
argument_list|(
literal|"tmp.wsdl"
argument_list|)
decl_stmt|;
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-wsdl"
block|,
literal|"-o"
block|,
name|output
operator|.
name|getPath
argument_list|()
operator|+
literal|"/tmp.wsdl"
block|,
literal|"-verbose"
block|,
literal|"-s"
block|,
name|output
operator|.
name|getPath
argument_list|()
block|,
literal|"-frontend"
block|,
literal|"jaxws"
block|,
literal|"-client"
block|,
literal|"-server"
block|,
literal|"org.apache.cxf.tools.fortest.HelloRMI"
block|}
decl_stmt|;
name|JavaToWS
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed to generate WSDL file"
argument_list|,
name|wsdlFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Parameter or return type implemented java.rmi.Remote interface error should be detected"
argument_list|,
name|getStdErr
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"implemented the java.rmi.Remote interface"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGenServerAndClient
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|client
init|=
name|outputFile
argument_list|(
literal|"org/apache/hello_world_soap12_http/GreeterClient.java"
argument_list|)
decl_stmt|;
name|File
name|server
init|=
name|outputFile
argument_list|(
literal|"org/apache/hello_world_soap12_http/GreeterServer.java"
argument_list|)
decl_stmt|;
name|File
name|impl
init|=
name|outputFile
argument_list|(
literal|"org/apache/hello_world_soap12_http/GreeterImpl.java"
argument_list|)
decl_stmt|;
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-s"
block|,
name|output
operator|.
name|getPath
argument_list|()
block|,
literal|"-client"
block|,
literal|"-server"
block|,
literal|"org.apache.hello_world_soap12_http.Greeter"
block|}
decl_stmt|;
name|JavaToWS
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|checkStdErr
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Client was not generated"
argument_list|,
name|client
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Greeter_GreeterPort_Server.java was not generated"
argument_list|,
name|server
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Impl was not generated"
argument_list|,
name|impl
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|implContent
init|=
name|FileUtils
operator|.
name|getStringFromFile
argument_list|(
name|impl
argument_list|)
decl_stmt|;
name|int
name|idx
init|=
name|implContent
operator|.
name|indexOf
argument_list|(
literal|"serviceName"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"serviceName annotation was not generated"
argument_list|,
name|idx
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
name|implContent
operator|=
name|implContent
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|11
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
name|implContent
operator|=
name|implContent
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"serviceName annotation was not generated\n"
operator|+
name|implContent
argument_list|,
name|implContent
operator|.
name|startsWith
argument_list|(
literal|"\"GreeterService\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGenServerAndImpl
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|server
init|=
name|outputFile
argument_list|(
literal|"org/apache/hello_world_soap12_http/GreeterServer.java"
argument_list|)
decl_stmt|;
name|File
name|impl
init|=
name|outputFile
argument_list|(
literal|"org/apache/hello_world_soap12_http/GreeterImpl.java"
argument_list|)
decl_stmt|;
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-s"
block|,
name|output
operator|.
name|getPath
argument_list|()
block|,
literal|"-server"
block|,
literal|"org.apache.hello_world_soap12_http.Greeter"
block|}
decl_stmt|;
name|JavaToWS
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|checkStdErr
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"GreeterServer.java was not generated"
argument_list|,
name|server
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"GreeterImpl.java was not generated"
argument_list|,
name|impl
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGenWrapperBean
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-s"
block|,
name|output
operator|.
name|getPath
argument_list|()
block|,
literal|"-wrapperbean"
block|,
literal|"-server"
block|,
literal|"org.apache.cxf.tools.java2ws.fortest.Calculator"
block|}
decl_stmt|;
name|JavaToWS
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|checkStdErr
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvalidFlag
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-frontend"
block|,
literal|"tmp"
block|,
literal|"-wsdl"
block|,
literal|"-o"
block|,
name|output
operator|.
name|getPath
argument_list|()
operator|+
literal|"/tmp.wsdl"
block|,
literal|"org.apache.hello_world_soap12_http.Greeter"
block|}
decl_stmt|;
name|JavaToWS
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"invalid frontend flag should be detected"
argument_list|,
name|getStdErr
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"is not a valid frontend,"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvalidFlag2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-frontend"
block|,
literal|"simple"
block|,
literal|"-wrapperbean"
block|,
literal|"-wsdl"
block|,
literal|"-o"
block|,
name|output
operator|.
name|getPath
argument_list|()
operator|+
literal|"/tmp.wsdl"
block|,
literal|"org.apache.hello_world_soap12_http.Greeter"
block|}
decl_stmt|;
name|JavaToWS
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"wrapperbean flag error should be detected"
argument_list|,
name|getStdErr
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"-wrapperbean is only valid for the jaxws front end."
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvalidFlag3
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-databinding"
block|,
literal|"jaxb"
block|,
literal|"-frontend"
block|,
literal|"simple"
block|,
literal|"-wsdl"
block|,
literal|"-o"
block|,
name|output
operator|.
name|getPath
argument_list|()
operator|+
literal|"/tmp.wsdl"
block|,
literal|"org.apache.hello_world_soap12_http.Greeter"
block|}
decl_stmt|;
name|JavaToWS
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"jaxb databinding warning should be detected"
argument_list|,
name|getStdErr
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"Simple front end only supports aegis databinding"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testImplClassWithoutSei
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|wsdlFile
init|=
name|outputFile
argument_list|(
literal|"tmp.wsdl"
argument_list|)
decl_stmt|;
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-wsdl"
block|,
literal|"-o"
block|,
name|output
operator|.
name|getPath
argument_list|()
operator|+
literal|"/tmp.wsdl"
block|,
literal|"-verbose"
block|,
literal|"-s"
block|,
name|output
operator|.
name|getPath
argument_list|()
block|,
literal|"-frontend"
block|,
literal|"jaxws"
block|,
literal|"-client"
block|,
literal|"-server"
block|,
literal|"org.apache.cxf.tools.fortest.GreeterImpl"
block|}
decl_stmt|;
name|JavaToWS
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed to generate WSDL file"
argument_list|,
name|wsdlFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|sei
init|=
name|outputFile
argument_list|(
literal|"org/apache/cxf/tools/fortest/GreeterImpl_PortType.java"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Failed to generate SEI file : GreeterImpl_PortType.java"
argument_list|,
name|sei
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|client
init|=
name|outputFile
argument_list|(
literal|"org/apache/cxf/tools/fortest/GreeterImpl_PortTypeClient.java"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Failed to generate client file : GreeterImpl_PortTypeClient.java"
argument_list|,
name|client
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|server
init|=
name|outputFile
argument_list|(
literal|"org/apache/cxf/tools/fortest/GreeterImpl_PortTypeServer.java"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Failed to generate SEI file : GreeterImpl_PortTypeServer.java"
argument_list|,
name|server
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testXmlList
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-o"
block|,
name|output
operator|.
name|getPath
argument_list|()
operator|+
literal|"/xml-list.wsdl"
block|,
literal|"-verbose"
block|,
literal|"-wsdl"
block|,
literal|"org.apache.cxf.tools.fortest.xmllist.AddNumbersPortType"
block|}
decl_stmt|;
name|JavaToWS
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|output
operator|.
name|getPath
argument_list|()
operator|+
literal|"/xml-list.wsdl"
argument_list|)
decl_stmt|;
name|String
name|str
init|=
name|FileUtils
operator|.
name|getStringFromFile
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Java2wsdl did not generate xsd:list element"
argument_list|,
name|str
operator|.
name|indexOf
argument_list|(
literal|"xs:list"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testXmlAttachementRef
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-o"
block|,
name|output
operator|.
name|getPath
argument_list|()
operator|+
literal|"/swa-ref.wsdl"
block|,
literal|"-verbose"
block|,
literal|"-wsdl"
block|,
literal|"org.apache.attachment.AddNumbersImpl"
block|}
decl_stmt|;
name|JavaToWS
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|output
operator|.
name|getPath
argument_list|()
operator|+
literal|"/AddNumbers.wsdl"
argument_list|)
decl_stmt|;
name|String
name|str
init|=
name|FileUtils
operator|.
name|getStringFromFile
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|String
name|swaImport
init|=
literal|"http://ws-i.org/profiles/basic/1.1/xsd"
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Java2wsdl did not generate swaRef type element"
argument_list|,
name|str
operator|.
name|indexOf
argument_list|(
literal|":swaRef"
argument_list|)
operator|>
operator|-
literal|1
operator|&&
name|str
operator|.
name|indexOf
argument_list|(
name|swaImport
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testXmlJavaTypeAdapter
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-o"
block|,
name|output
operator|.
name|getPath
argument_list|()
operator|+
literal|"/xmladapter.wsdl"
block|,
literal|"-verbose"
block|,
literal|"-wsdl"
block|,
literal|"org.apache.xmladapter.GreeterImpl"
block|}
decl_stmt|;
name|JavaToWS
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|output
operator|.
name|getPath
argument_list|()
operator|+
literal|"/xmladapter.wsdl"
argument_list|)
decl_stmt|;
name|String
name|str
init|=
name|FileUtils
operator|.
name|getStringFromFile
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|String
name|expected
init|=
literal|"<xs:element  minOccurs=\"0\"  name=\"arg0\"  type=\"xs:string\"/>"
decl_stmt|;
name|assertTrue
argument_list|(
literal|"@XmlJavaTypeAdapter in SEI dose not take effect"
argument_list|,
name|str
operator|.
name|indexOf
argument_list|(
name|expected
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|getClassPath
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|ClassLoader
name|loader
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
name|StringBuffer
name|classPath
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
if|if
condition|(
name|loader
operator|instanceof
name|URLClassLoader
condition|)
block|{
name|URLClassLoader
name|urlLoader
init|=
operator|(
name|URLClassLoader
operator|)
name|loader
decl_stmt|;
for|for
control|(
name|URL
name|url
range|:
name|urlLoader
operator|.
name|getURLs
argument_list|()
control|)
block|{
name|File
name|file
decl_stmt|;
name|file
operator|=
operator|new
name|File
argument_list|(
name|url
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|filename
init|=
name|file
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
if|if
condition|(
name|filename
operator|.
name|indexOf
argument_list|(
literal|"junit"
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
name|classPath
operator|.
name|append
argument_list|(
name|filename
argument_list|)
expr_stmt|;
name|classPath
operator|.
name|append
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"path.separator"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|classPath
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

