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
name|wadlto
operator|.
name|jaxrs
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
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|wadlto
operator|.
name|WADLToJava
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
name|WADLToJavaTest
extends|extends
name|ProcessorTestBase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCodeGenInterfaces
parameter_list|()
block|{
try|try
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-d"
block|,
name|output
operator|.
name|getCanonicalPath
argument_list|()
block|,
literal|"-p"
block|,
literal|"custom.service"
block|,
literal|"-tMap"
block|,
literal|"{http://www.w3.org/2001/XMLSchema}date=java.util.List..String"
block|,
literal|"-async getName,delete"
block|,
literal|"-inheritResourceParams first"
block|,
literal|"-compile"
block|,
name|getLocation
argument_list|(
literal|"/wadl/bookstore.xml"
argument_list|)
block|,             }
decl_stmt|;
name|WADLToJava
name|tool
init|=
operator|new
name|WADLToJava
argument_list|(
name|args
argument_list|)
decl_stmt|;
name|tool
operator|.
name|run
argument_list|(
operator|new
name|ToolContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|output
operator|.
name|list
argument_list|()
argument_list|)
expr_stmt|;
name|verifyFiles
argument_list|(
literal|"java"
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|"superbooks"
argument_list|,
literal|"custom.service"
argument_list|)
expr_stmt|;
name|verifyFiles
argument_list|(
literal|"class"
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|"superbooks"
argument_list|,
literal|"custom.service"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGenerateJAXBToString
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-d"
block|,
name|output
operator|.
name|getCanonicalPath
argument_list|()
block|,
literal|"-p"
block|,
literal|"custom.service"
block|,
literal|"-async getName,delete"
block|,
literal|"-compile"
block|,
literal|"-xjc-XtoString"
block|,
name|getLocation
argument_list|(
literal|"/wadl/bookstore.xml"
argument_list|)
block|,             }
decl_stmt|;
name|WADLToJava
name|tool
init|=
operator|new
name|WADLToJava
argument_list|(
name|args
argument_list|)
decl_stmt|;
name|tool
operator|.
name|run
argument_list|(
operator|new
name|ToolContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|output
operator|.
name|list
argument_list|()
argument_list|)
expr_stmt|;
name|verifyFiles
argument_list|(
literal|"java"
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|"superbooks"
argument_list|,
literal|"custom.service"
argument_list|)
expr_stmt|;
name|verifyFiles
argument_list|(
literal|"class"
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|"superbooks"
argument_list|,
literal|"custom.service"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|schemaClassFiles
init|=
name|getSchemaClassFiles
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|schemaClassFiles
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|schemaClassFiles
control|)
block|{
name|c
operator|.
name|getMethod
argument_list|(
literal|"toString"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getSchemaClassFiles
parameter_list|()
throws|throws
name|Exception
block|{
name|URLClassLoader
name|cl
init|=
operator|new
name|URLClassLoader
argument_list|(
operator|new
name|URL
index|[]
block|{
name|output
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
decl_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|files
init|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|files
operator|.
name|add
argument_list|(
name|cl
operator|.
name|loadClass
argument_list|(
literal|"superbooks.EnumType"
argument_list|)
argument_list|)
expr_stmt|;
name|files
operator|.
name|add
argument_list|(
name|cl
operator|.
name|loadClass
argument_list|(
literal|"superbooks.Book"
argument_list|)
argument_list|)
expr_stmt|;
name|files
operator|.
name|add
argument_list|(
name|cl
operator|.
name|loadClass
argument_list|(
literal|"superbooks.TheBook2"
argument_list|)
argument_list|)
expr_stmt|;
name|files
operator|.
name|add
argument_list|(
name|cl
operator|.
name|loadClass
argument_list|(
literal|"superbooks.Chapter"
argument_list|)
argument_list|)
expr_stmt|;
name|cl
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|files
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGenerateJAXBToStringAndEqualsAndHashCode
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-d"
block|,
name|output
operator|.
name|getCanonicalPath
argument_list|()
block|,
literal|"-p"
block|,
literal|"custom.service"
block|,
literal|"-async getName,delete"
block|,
literal|"-compile"
block|,
literal|"-xjc-XtoString"
block|,
literal|"-xjc-Xequals"
block|,
literal|"-xjc-XhashCode"
block|,
name|getLocation
argument_list|(
literal|"/wadl/bookstore.xml"
argument_list|)
block|,             }
decl_stmt|;
name|WADLToJava
name|tool
init|=
operator|new
name|WADLToJava
argument_list|(
name|args
argument_list|)
decl_stmt|;
name|tool
operator|.
name|run
argument_list|(
operator|new
name|ToolContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|output
operator|.
name|list
argument_list|()
argument_list|)
expr_stmt|;
name|verifyFiles
argument_list|(
literal|"java"
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|"superbooks"
argument_list|,
literal|"custom.service"
argument_list|)
expr_stmt|;
name|verifyFiles
argument_list|(
literal|"class"
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|"superbooks"
argument_list|,
literal|"custom.service"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|schemaClassFiles
init|=
name|getSchemaClassFiles
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|schemaClassFiles
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|schemaClassFiles
control|)
block|{
name|c
operator|.
name|getMethod
argument_list|(
literal|"toString"
argument_list|)
expr_stmt|;
name|c
operator|.
name|getMethod
argument_list|(
literal|"hashCode"
argument_list|)
expr_stmt|;
name|c
operator|.
name|getMethod
argument_list|(
literal|"equals"
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|verifyFiles
parameter_list|(
name|String
name|ext
parameter_list|,
name|boolean
name|subresourceExpected
parameter_list|,
name|boolean
name|interfacesAndImpl
parameter_list|,
name|String
name|schemaPackage
parameter_list|,
name|String
name|resourcePackage
parameter_list|)
block|{
name|List
argument_list|<
name|File
argument_list|>
name|files
init|=
name|FileUtils
operator|.
name|getFilesRecurse
argument_list|(
name|output
argument_list|,
literal|".+\\."
operator|+
name|ext
operator|+
literal|"$"
argument_list|)
decl_stmt|;
name|int
name|size
init|=
name|interfacesAndImpl
condition|?
literal|11
else|:
literal|10
decl_stmt|;
if|if
condition|(
operator|!
name|subresourceExpected
condition|)
block|{
name|size
operator|--
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|size
argument_list|,
name|files
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|doVerifyTypes
argument_list|(
name|files
argument_list|,
name|schemaPackage
argument_list|,
name|ext
argument_list|)
expr_stmt|;
if|if
condition|(
name|subresourceExpected
condition|)
block|{
name|assertTrue
argument_list|(
name|checkContains
argument_list|(
name|files
argument_list|,
name|resourcePackage
operator|+
literal|".FormInterface."
operator|+
name|ext
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|checkContains
argument_list|(
name|files
argument_list|,
name|resourcePackage
operator|+
literal|".BookStore."
operator|+
name|ext
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|interfacesAndImpl
condition|)
block|{
if|if
condition|(
name|subresourceExpected
condition|)
block|{
name|assertTrue
argument_list|(
name|checkContains
argument_list|(
name|files
argument_list|,
name|resourcePackage
operator|+
literal|".FormInterfaceImpl."
operator|+
name|ext
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|checkContains
argument_list|(
name|files
argument_list|,
name|resourcePackage
operator|+
literal|".BookStoreImpl."
operator|+
name|ext
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|doVerifyTypes
parameter_list|(
name|List
argument_list|<
name|File
argument_list|>
name|files
parameter_list|,
name|String
name|schemaPackage
parameter_list|,
name|String
name|ext
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|checkContains
argument_list|(
name|files
argument_list|,
name|schemaPackage
operator|+
literal|".EnumType."
operator|+
name|ext
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|checkContains
argument_list|(
name|files
argument_list|,
name|schemaPackage
operator|+
literal|".Book."
operator|+
name|ext
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|checkContains
argument_list|(
name|files
argument_list|,
name|schemaPackage
operator|+
literal|".TheBook2."
operator|+
name|ext
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|checkContains
argument_list|(
name|files
argument_list|,
name|schemaPackage
operator|+
literal|".Chapter."
operator|+
name|ext
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|checkContains
argument_list|(
name|files
argument_list|,
name|schemaPackage
operator|+
literal|".ObjectFactory."
operator|+
name|ext
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|checkContains
argument_list|(
name|files
argument_list|,
name|schemaPackage
operator|+
literal|".package-info."
operator|+
name|ext
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|checkContains
parameter_list|(
name|List
argument_list|<
name|File
argument_list|>
name|clsFiles
parameter_list|,
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|File
name|f
range|:
name|clsFiles
control|)
block|{
if|if
condition|(
name|checkFileContains
argument_list|(
name|f
argument_list|,
name|name
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|boolean
name|checkFileContains
parameter_list|(
name|File
name|f
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|f
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|replace
argument_list|(
name|File
operator|.
name|separatorChar
argument_list|,
literal|'.'
argument_list|)
operator|.
name|endsWith
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|protected
name|String
name|getLocation
parameter_list|(
name|String
name|wsdlFile
parameter_list|)
throws|throws
name|URISyntaxException
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|wsdlFile
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

