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
name|util
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
name|IOException
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

begin_class
specifier|public
class|class
name|FileWriterUtilTest
extends|extends
name|Assert
block|{
specifier|private
name|void
name|cleanDir
parameter_list|(
name|File
name|dir
parameter_list|)
block|{
try|try
block|{
for|for
control|(
name|File
name|fl
range|:
name|dir
operator|.
name|listFiles
argument_list|()
control|)
block|{
if|if
condition|(
name|fl
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|cleanDir
argument_list|(
name|fl
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fl
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
name|dir
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetFile
parameter_list|()
throws|throws
name|Exception
block|{
name|FileWriterUtil
name|fileWriter
init|=
literal|null
decl_stmt|;
name|String
name|tmpDir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
decl_stmt|;
name|File
name|targetDir
init|=
operator|new
name|File
argument_list|(
name|tmpDir
operator|+
name|File
operator|.
name|separator
operator|+
literal|"target"
argument_list|)
decl_stmt|;
try|try
block|{
name|targetDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|fileWriter
operator|=
operator|new
name|FileWriterUtil
argument_list|(
name|targetDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fileWriter
operator|.
name|getWriter
argument_list|(
literal|"com.iona.test"
argument_list|,
literal|"A.java"
argument_list|)
expr_stmt|;
name|String
name|packPath
init|=
literal|"/com/iona/test/A.java"
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
name|File
operator|.
name|separatorChar
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|targetDir
operator|.
name|getAbsolutePath
argument_list|()
operator|+
name|packPath
decl_stmt|;
name|assertNotNull
argument_list|(
operator|new
name|File
argument_list|(
name|path
argument_list|)
operator|.
name|getName
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
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|cleanDir
argument_list|(
name|targetDir
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetWriter
parameter_list|()
throws|throws
name|Exception
block|{
name|FileWriterUtil
name|fileWriter
init|=
literal|null
decl_stmt|;
name|String
name|tmpDir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
decl_stmt|;
name|File
name|targetDir
init|=
operator|new
name|File
argument_list|(
name|tmpDir
operator|+
name|File
operator|.
name|separator
operator|+
literal|"target"
argument_list|)
decl_stmt|;
try|try
block|{
name|targetDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|fileWriter
operator|=
operator|new
name|FileWriterUtil
argument_list|(
name|targetDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|fileWriter
operator|.
name|getWriter
argument_list|(
literal|"com.iona.test.SAMPLE"
argument_list|,
literal|"A.java"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|cleanDir
argument_list|(
name|targetDir
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

