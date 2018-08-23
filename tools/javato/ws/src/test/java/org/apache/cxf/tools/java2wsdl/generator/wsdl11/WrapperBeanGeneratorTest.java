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
name|java2wsdl
operator|.
name|generator
operator|.
name|wsdl11
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
name|lang
operator|.
name|reflect
operator|.
name|Field
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
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlList
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
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
name|java2wsdl
operator|.
name|processor
operator|.
name|JavaToWSDLProcessor
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
name|Rule
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
name|junit
operator|.
name|rules
operator|.
name|ExternalResource
import|;
end_import

begin_class
specifier|public
class|class
name|WrapperBeanGeneratorTest
extends|extends
name|ProcessorTestBase
block|{
name|JavaToWSDLProcessor
name|processor
init|=
operator|new
name|JavaToWSDLProcessor
argument_list|()
decl_stmt|;
name|ClassLoader
name|classLoader
decl_stmt|;
comment|//CHECKSTYLE:OFF
annotation|@
name|Rule
specifier|public
name|ExternalResource
name|envRule
init|=
operator|new
name|ExternalResource
argument_list|()
block|{
specifier|protected
name|void
name|before
parameter_list|()
throws|throws
name|Throwable
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.class.path"
argument_list|,
name|getClassPath
argument_list|()
operator|+
name|tmpDir
operator|.
name|getRoot
argument_list|()
operator|.
name|getCanonicalPath
argument_list|()
operator|+
name|File
operator|.
name|separatorChar
argument_list|)
expr_stmt|;
name|classLoader
operator|=
operator|new
name|URLClassLoader
argument_list|(
operator|new
name|URL
index|[]
block|{
name|tmpDir
operator|.
name|getRoot
argument_list|()
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
block|}
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|//CHECKSTYLE:ON
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|processor
operator|.
name|setEnvironment
argument_list|(
name|env
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
specifier|private
name|ServiceInfo
name|getServiceInfo
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getServiceBuilder
argument_list|()
operator|.
name|createService
argument_list|()
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGenInAnotherPackage
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testingClass
init|=
literal|"org.apache.cxf.tools.fortest.withannotation.doc.GreeterNoWrapperBean"
decl_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLASSNAME
argument_list|,
name|testingClass
argument_list|)
expr_stmt|;
name|WrapperBeanGenerator
name|generator
init|=
operator|new
name|WrapperBeanGenerator
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setToolContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|getServiceInfo
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|String
name|pkgBase
init|=
literal|"org/apache/cxf"
decl_stmt|;
name|File
name|requestWrapperClass
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
name|pkgBase
operator|+
literal|"/EchoDataBean.java"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|requestWrapperClass
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|contents
init|=
operator|new
name|String
argument_list|(
name|Files
operator|.
name|readAllBytes
argument_list|(
name|requestWrapperClass
operator|.
name|toPath
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|contents
operator|.
name|indexOf
argument_list|(
literal|"org.apache.cxf.tools.fortest.withannotation.doc"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|File
name|responseWrapperClass
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
name|pkgBase
operator|+
literal|"/EchoDataBeanResponse.java"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|responseWrapperClass
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|requestWrapperClass
operator|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
name|pkgBase
operator|+
literal|"/SayHi.java"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|requestWrapperClass
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|responseWrapperClass
operator|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
name|pkgBase
operator|+
literal|"/SayHiResponse.java"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|responseWrapperClass
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
name|testArray
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testingClass
init|=
literal|"org.apache.cxf.tools.fortest.withannotation.doc.GreeterArray"
decl_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLASSNAME
argument_list|,
name|testingClass
argument_list|)
expr_stmt|;
name|WrapperBeanGenerator
name|generator
init|=
operator|new
name|WrapperBeanGenerator
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setToolContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|getServiceInfo
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|String
name|pkgBase
init|=
literal|"org/apache/cxf/tools/fortest/withannotation/doc/jaxws"
decl_stmt|;
name|File
name|requestWrapperClass
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
name|pkgBase
operator|+
literal|"/SayIntArray.java"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|requestWrapperClass
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|contents
init|=
operator|new
name|String
argument_list|(
name|Files
operator|.
name|readAllBytes
argument_list|(
name|requestWrapperClass
operator|.
name|toPath
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|contents
operator|.
name|indexOf
argument_list|(
literal|"int[]"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|File
name|responseWrapperClass
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
name|pkgBase
operator|+
literal|"/SayIntArrayResponse.java"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|responseWrapperClass
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|contents
operator|=
operator|new
name|String
argument_list|(
name|Files
operator|.
name|readAllBytes
argument_list|(
name|responseWrapperClass
operator|.
name|toPath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|contents
operator|.
name|indexOf
argument_list|(
literal|"_return"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|requestWrapperClass
operator|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
name|pkgBase
operator|+
literal|"/SayStringArray.java"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|requestWrapperClass
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|responseWrapperClass
operator|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
name|pkgBase
operator|+
literal|"/SayStringArrayResponse.java"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|responseWrapperClass
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|requestWrapperClass
operator|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
name|pkgBase
operator|+
literal|"/SayTestDataBeanArray.java"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|requestWrapperClass
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|responseWrapperClass
operator|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
name|pkgBase
operator|+
literal|"/SayTestDataBeanArrayResponse.java"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|responseWrapperClass
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|contents
operator|=
operator|new
name|String
argument_list|(
name|Files
operator|.
name|readAllBytes
argument_list|(
name|requestWrapperClass
operator|.
name|toPath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|contents
operator|.
name|indexOf
argument_list|(
literal|"org.apache.cxf.tools.fortest.withannotation.doc.TestDataBean[]"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGenJaxbAnno
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testingClass
init|=
literal|"org.apache.cxf.tools.fortest.withannotation.doc.SayHiNoWrapperBean"
decl_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLASSNAME
argument_list|,
name|testingClass
argument_list|)
expr_stmt|;
name|WrapperBeanGenerator
name|generator
init|=
operator|new
name|WrapperBeanGenerator
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setToolContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|getServiceInfo
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
name|String
name|java9PlusFolder
init|=
name|output
operator|.
name|getParent
argument_list|()
operator|+
literal|"/java9"
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
literal|":"
operator|+
name|java9PlusFolder
operator|+
literal|"/jaxb-api-2.3.0.jar"
operator|+
literal|":"
operator|+
name|java9PlusFolder
operator|+
literal|"/jaxws-api-2.3.0.jar"
operator|+
literal|":"
operator|+
name|java9PlusFolder
operator|+
literal|"/geronimo-ws-metadata_2.0_spec-1.1.3.jar"
argument_list|)
expr_stmt|;
block|}
name|generator
operator|.
name|generate
argument_list|(
name|output
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
literal|"org.apache.cxf.SayHi"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|clz
argument_list|)
expr_stmt|;
name|Field
name|field
init|=
name|clz
operator|.
name|getDeclaredField
argument_list|(
literal|"arg0"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|field
operator|.
name|getAnnotation
argument_list|(
name|XmlList
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGenGeneric
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testingClass
init|=
literal|"org.apache.cxf.tools.fortest.withannotation.doc.EchoGenericNoWrapperBean"
decl_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLASSNAME
argument_list|,
name|testingClass
argument_list|)
expr_stmt|;
name|WrapperBeanGenerator
name|generator
init|=
operator|new
name|WrapperBeanGenerator
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setToolContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|getServiceInfo
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|String
name|pkgBase
init|=
literal|"org/apache/cxf"
decl_stmt|;
name|File
name|requestWrapperClass
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
name|pkgBase
operator|+
literal|"/EchoGeneric.java"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|requestWrapperClass
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|contents
init|=
operator|new
name|String
argument_list|(
name|Files
operator|.
name|readAllBytes
argument_list|(
name|requestWrapperClass
operator|.
name|toPath
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|contents
operator|.
name|indexOf
argument_list|(
literal|"public java.util.List<java.lang.String> get"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|File
name|responseWrapperClass
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
name|pkgBase
operator|+
literal|"/EchoGenericResponse.java"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|responseWrapperClass
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|contents
operator|=
operator|new
name|String
argument_list|(
name|Files
operator|.
name|readAllBytes
argument_list|(
name|responseWrapperClass
operator|.
name|toPath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|contents
operator|.
name|indexOf
argument_list|(
literal|"public java.util.List<java.lang.String> getReturn()"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

