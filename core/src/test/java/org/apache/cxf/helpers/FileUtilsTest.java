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
name|helpers
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
name|nio
operator|.
name|file
operator|.
name|Files
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
name|Path
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
name|Paths
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
name|java
operator|.
name|util
operator|.
name|Optional
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|FileUtilsTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testTempIODirExists
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|originaltmpdir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
decl_stmt|;
try|try
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|,
literal|"dummy"
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|createTempFile
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"please set java.io.tempdir to an existing directory"
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|,
name|originaltmpdir
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadLines
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|basedir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|)
decl_stmt|;
if|if
condition|(
name|basedir
operator|==
literal|null
condition|)
block|{
name|basedir
operator|=
operator|new
name|File
argument_list|(
literal|"."
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
expr_stmt|;
block|}
name|Optional
argument_list|<
name|Path
argument_list|>
name|p
init|=
name|Files
operator|.
name|find
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|basedir
argument_list|)
argument_list|,
literal|20
argument_list|,
parameter_list|(
name|path
parameter_list|,
name|attrs
parameter_list|)
lambda|->
name|path
operator|.
name|getFileName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"FileUtilsTest.java"
argument_list|)
argument_list|)
operator|.
name|findFirst
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|lines
init|=
name|FileUtils
operator|.
name|readLines
argument_list|(
name|p
operator|.
name|get
argument_list|()
operator|.
name|toFile
argument_list|()
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|lines
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetFiles
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|URL
name|resource
init|=
name|FileUtilsTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"FileUtilsTest.class"
argument_list|)
decl_stmt|;
name|File
name|directory
init|=
name|Paths
operator|.
name|get
argument_list|(
name|resource
operator|.
name|toURI
argument_list|()
argument_list|)
operator|.
name|getParent
argument_list|()
operator|.
name|toFile
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|directory
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|File
argument_list|>
name|foundFiles
init|=
name|FileUtils
operator|.
name|getFilesUsingSuffix
argument_list|(
name|directory
argument_list|,
literal|".class"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
operator|!
name|foundFiles
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|File
argument_list|>
name|foundFiles2
init|=
name|FileUtils
operator|.
name|getFiles
argument_list|(
name|directory
argument_list|,
literal|".*\\.class$"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|foundFiles
argument_list|,
name|foundFiles2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

