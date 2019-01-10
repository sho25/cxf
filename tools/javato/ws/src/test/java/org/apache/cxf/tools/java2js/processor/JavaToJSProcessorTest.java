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
name|java2js
operator|.
name|processor
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|JavaUtils
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
name|ProcessorTestBase
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
name|java2js
operator|.
name|JavaToJS
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
name|core
operator|.
name|DataBindingProfile
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
name|core
operator|.
name|FrontEndProfile
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
name|core
operator|.
name|PluginLoader
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
name|frontend
operator|.
name|jaxws
operator|.
name|JAXWSContainer
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

begin_class
specifier|public
class|class
name|JavaToJSProcessorTest
extends|extends
name|ProcessorTestBase
block|{
name|JavaToJSProcessor
name|processor
init|=
operator|new
name|JavaToJSProcessor
argument_list|()
decl_stmt|;
name|String
name|classPath
init|=
literal|""
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|startUp
parameter_list|()
throws|throws
name|Exception
block|{
name|env
operator|=
operator|new
name|ToolContext
argument_list|()
expr_stmt|;
name|classPath
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.class.path"
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
if|if
condition|(
name|JavaUtils
operator|.
name|isJava9Compatible
argument_list|()
condition|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.apache.cxf.common.util.Compiler-fork"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
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
name|classPath
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimpleClass
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
name|CFG_OUTPUTFILE
argument_list|,
name|output
operator|.
name|getPath
argument_list|()
operator|+
literal|"/doc_wrapped_bare.js"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLASSNAME
argument_list|,
literal|"org.apache.cxf.tools.fortest.simple.Hello"
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setEnvironment
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|()
expr_stmt|;
name|File
name|jsFile
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"doc_wrapped_bare.js"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Fail to generate JS file: "
operator|+
name|jsFile
operator|.
name|toString
argument_list|()
argument_list|,
name|jsFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// need some additional validation.
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDocLitUseClassPathFlag
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|classFile
init|=
operator|new
name|java
operator|.
name|io
operator|.
name|File
argument_list|(
name|output
operator|.
name|getCanonicalPath
argument_list|()
operator|+
literal|"/classes"
argument_list|)
decl_stmt|;
name|classFile
operator|.
name|mkdir
argument_list|()
expr_stmt|;
if|if
condition|(
name|JavaUtils
operator|.
name|isJava9Compatible
argument_list|()
condition|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.apache.cxf.common.util.Compiler-fork"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|String
name|java9PlusFolder
init|=
name|output
operator|.
name|getParent
argument_list|()
operator|+
name|java
operator|.
name|io
operator|.
name|File
operator|.
name|separator
operator|+
literal|"java9"
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.class.path"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.class.path"
argument_list|)
operator|+
name|java
operator|.
name|io
operator|.
name|File
operator|.
name|pathSeparator
operator|+
name|java9PlusFolder
operator|+
name|java
operator|.
name|io
operator|.
name|File
operator|.
name|separator
operator|+
literal|"*"
argument_list|)
expr_stmt|;
block|}
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_COMPILE
argument_list|,
name|ToolConstants
operator|.
name|CFG_COMPILE
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
name|output
operator|.
name|getCanonicalPath
argument_list|()
operator|+
literal|"/classes"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|FrontEndProfile
operator|.
name|class
argument_list|,
name|PluginLoader
operator|.
name|getInstance
argument_list|()
operator|.
name|getFrontEndProfile
argument_list|(
literal|"jaxws"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|DataBindingProfile
operator|.
name|class
argument_list|,
name|PluginLoader
operator|.
name|getInstance
argument_list|()
operator|.
name|getDataBindingProfile
argument_list|(
literal|"jaxb"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|,
name|output
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_PACKAGENAME
argument_list|,
literal|"org.apache.cxf.classpath"
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
name|output
operator|.
name|getCanonicalPath
argument_list|()
operator|+
literal|"/classes"
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
literal|"/wsdl/hello_world_doc_lit.wsdl"
argument_list|)
argument_list|)
expr_stmt|;
name|JAXWSContainer
name|w2jProcessor
init|=
operator|new
name|JAXWSContainer
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|w2jProcessor
operator|.
name|setContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|w2jProcessor
operator|.
name|execute
argument_list|()
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.class.path"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
comment|//      test flag
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
literal|"java2wsdl.js"
block|,
literal|"-jsutils"
block|,
literal|"-cp"
block|,
name|classFile
operator|.
name|getCanonicalPath
argument_list|()
block|,
literal|"-d"
block|,
name|output
operator|.
name|getPath
argument_list|()
block|,
literal|"org.apache.cxf.classpath.Greeter"
block|}
decl_stmt|;
name|JavaToJS
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|File
name|jsFile
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"java2wsdl.js"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Generate JS Fail"
argument_list|,
name|jsFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

