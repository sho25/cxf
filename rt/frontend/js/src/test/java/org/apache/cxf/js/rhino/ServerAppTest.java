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
name|js
operator|.
name|rhino
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|PrintStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
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
name|ServerAppTest
extends|extends
name|Assert
block|{
specifier|private
name|String
name|epAddr
init|=
literal|"http://cxf.apache.org/"
decl_stmt|;
specifier|private
name|ProviderFactory
name|phMock
decl_stmt|;
specifier|private
name|String
name|emptyFile
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
name|phMock
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ProviderFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|emptyFile
operator|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"empty/empty.js"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|getPath
argument_list|()
expr_stmt|;
block|}
specifier|private
name|ServerApp
name|createServerApp
parameter_list|()
block|{
return|return
operator|new
name|ServerApp
argument_list|()
block|{
specifier|protected
name|ProviderFactory
name|createProviderFactory
parameter_list|()
block|{
return|return
name|phMock
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoArgs
parameter_list|()
block|{
name|EasyMock
operator|.
name|replay
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
try|try
block|{
name|ServerApp
name|app
init|=
name|createServerApp
argument_list|()
decl_stmt|;
name|String
index|[]
name|args
init|=
block|{}
decl_stmt|;
name|app
operator|.
name|start
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected exception did not occur"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"wrong exception message"
argument_list|,
name|ServerApp
operator|.
name|NO_FILES_ERR
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|EasyMock
operator|.
name|verify
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUknownOption
parameter_list|()
block|{
name|EasyMock
operator|.
name|replay
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
try|try
block|{
name|ServerApp
name|app
init|=
name|createServerApp
argument_list|()
decl_stmt|;
name|String
index|[]
name|args
init|=
block|{
literal|"-x"
block|}
decl_stmt|;
name|app
operator|.
name|start
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected exception did not occur"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
name|ServerApp
operator|.
name|UNKNOWN_OPTION
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|EasyMock
operator|.
name|verify
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMissingOptionA
parameter_list|()
block|{
name|EasyMock
operator|.
name|replay
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
try|try
block|{
name|ServerApp
name|app
init|=
name|createServerApp
argument_list|()
decl_stmt|;
name|String
index|[]
name|args
init|=
block|{
literal|"-a"
block|}
decl_stmt|;
name|app
operator|.
name|start
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected exception did not occur"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"wrong exception message"
argument_list|,
name|ServerApp
operator|.
name|WRONG_ADDR_ERR
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|EasyMock
operator|.
name|verify
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBrokenOptionA
parameter_list|()
block|{
name|EasyMock
operator|.
name|replay
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
try|try
block|{
name|ServerApp
name|app
init|=
name|createServerApp
argument_list|()
decl_stmt|;
name|String
index|[]
name|args
init|=
block|{
literal|"-a"
block|,
literal|"not-a-url"
block|}
decl_stmt|;
name|app
operator|.
name|start
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected exception did not occur"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"wrong exception message"
argument_list|,
name|ServerApp
operator|.
name|WRONG_ADDR_ERR
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|EasyMock
operator|.
name|verify
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMissingOptionB
parameter_list|()
block|{
name|EasyMock
operator|.
name|replay
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
try|try
block|{
name|ServerApp
name|app
init|=
name|createServerApp
argument_list|()
decl_stmt|;
name|String
index|[]
name|args
init|=
block|{
literal|"-b"
block|}
decl_stmt|;
name|app
operator|.
name|start
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected exception did not occur"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"wrong exception message"
argument_list|,
name|ServerApp
operator|.
name|WRONG_BASE_ERR
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|EasyMock
operator|.
name|verify
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBrokenOptionB
parameter_list|()
block|{
name|EasyMock
operator|.
name|replay
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
try|try
block|{
name|ServerApp
name|app
init|=
name|createServerApp
argument_list|()
decl_stmt|;
name|String
index|[]
name|args
init|=
block|{
literal|"-b"
block|,
literal|"not-a-url"
block|}
decl_stmt|;
name|app
operator|.
name|start
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected exception did not occur"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"wrong exception message"
argument_list|,
name|ServerApp
operator|.
name|WRONG_BASE_ERR
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|EasyMock
operator|.
name|verify
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFileOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|phMock
operator|.
name|createAndPublish
argument_list|(
operator|new
name|File
argument_list|(
name|emptyFile
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
name|ServerApp
name|app
init|=
name|createServerApp
argument_list|()
decl_stmt|;
name|String
index|[]
name|args
init|=
block|{
name|emptyFile
block|}
decl_stmt|;
name|app
operator|.
name|start
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOptionsAB
parameter_list|()
throws|throws
name|Exception
block|{
name|phMock
operator|.
name|createAndPublish
argument_list|(
operator|new
name|File
argument_list|(
name|emptyFile
argument_list|)
argument_list|,
name|epAddr
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
name|ServerApp
name|app
init|=
name|createServerApp
argument_list|()
decl_stmt|;
name|String
index|[]
name|args
init|=
block|{
literal|"-a"
block|,
name|epAddr
block|,
literal|"-b"
block|,
name|epAddr
block|,
name|emptyFile
block|}
decl_stmt|;
name|app
operator|.
name|start
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOptionA
parameter_list|()
throws|throws
name|Exception
block|{
name|phMock
operator|.
name|createAndPublish
argument_list|(
operator|new
name|File
argument_list|(
name|emptyFile
argument_list|)
argument_list|,
name|epAddr
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
name|ServerApp
name|app
init|=
name|createServerApp
argument_list|()
decl_stmt|;
name|String
index|[]
name|args
init|=
block|{
literal|"-a"
block|,
name|epAddr
block|,
name|emptyFile
block|}
decl_stmt|;
name|app
operator|.
name|start
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOptionAWithOptionV
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|bout
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|PrintStream
name|pout
init|=
operator|new
name|PrintStream
argument_list|(
name|bout
argument_list|)
decl_stmt|;
name|PrintStream
name|orig
init|=
name|System
operator|.
name|out
decl_stmt|;
try|try
block|{
name|System
operator|.
name|setOut
argument_list|(
name|pout
argument_list|)
expr_stmt|;
name|phMock
operator|.
name|createAndPublish
argument_list|(
operator|new
name|File
argument_list|(
name|emptyFile
argument_list|)
argument_list|,
name|epAddr
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
name|ServerApp
name|app
init|=
name|createServerApp
argument_list|()
decl_stmt|;
name|String
index|[]
name|args
init|=
block|{
literal|"-a"
block|,
name|epAddr
block|,
literal|"-v"
block|,
name|emptyFile
block|}
decl_stmt|;
name|app
operator|.
name|start
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
name|pout
operator|.
name|flush
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|String
argument_list|(
name|bout
operator|.
name|toByteArray
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"processing file"
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|setOut
argument_list|(
name|orig
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOptionB
parameter_list|()
throws|throws
name|Exception
block|{
name|phMock
operator|.
name|createAndPublish
argument_list|(
operator|new
name|File
argument_list|(
name|emptyFile
argument_list|)
argument_list|,
name|epAddr
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
name|ServerApp
name|app
init|=
name|createServerApp
argument_list|()
decl_stmt|;
name|String
index|[]
name|args
init|=
block|{
literal|"-b"
block|,
name|epAddr
block|,
name|emptyFile
block|}
decl_stmt|;
name|app
operator|.
name|start
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOptionBWithOptionV
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|bout
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|PrintStream
name|pout
init|=
operator|new
name|PrintStream
argument_list|(
name|bout
argument_list|)
decl_stmt|;
name|PrintStream
name|orig
init|=
name|System
operator|.
name|out
decl_stmt|;
try|try
block|{
name|System
operator|.
name|setOut
argument_list|(
name|pout
argument_list|)
expr_stmt|;
name|phMock
operator|.
name|createAndPublish
argument_list|(
operator|new
name|File
argument_list|(
name|emptyFile
argument_list|)
argument_list|,
name|epAddr
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
name|ServerApp
name|app
init|=
name|createServerApp
argument_list|()
decl_stmt|;
name|String
index|[]
name|args
init|=
block|{
literal|"-b"
block|,
name|epAddr
block|,
literal|"-v"
block|,
name|emptyFile
block|}
decl_stmt|;
name|app
operator|.
name|start
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|String
argument_list|(
name|bout
operator|.
name|toByteArray
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"processing file"
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|setOut
argument_list|(
name|orig
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDirectory
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|emptyFile
argument_list|)
decl_stmt|;
name|String
name|dir
init|=
name|f
operator|.
name|getParent
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|dir
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|checkOrder
argument_list|(
name|phMock
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|phMock
operator|.
name|createAndPublish
argument_list|(
operator|new
name|File
argument_list|(
name|emptyFile
argument_list|)
argument_list|,
name|epAddr
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|String
name|file
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"empty/empty2.jsx"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|phMock
operator|.
name|createAndPublish
argument_list|(
operator|new
name|File
argument_list|(
name|file
argument_list|)
argument_list|,
name|epAddr
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|file
operator|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"empty/empty3.js"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|getPath
argument_list|()
expr_stmt|;
name|phMock
operator|.
name|createAndPublish
argument_list|(
operator|new
name|File
argument_list|(
name|file
argument_list|)
argument_list|,
name|epAddr
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|file
operator|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"empty/empty4.jsx"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|getPath
argument_list|()
expr_stmt|;
name|phMock
operator|.
name|createAndPublish
argument_list|(
operator|new
name|File
argument_list|(
name|file
argument_list|)
argument_list|,
name|epAddr
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
name|ServerApp
name|app
init|=
name|createServerApp
argument_list|()
decl_stmt|;
name|String
index|[]
name|args
init|=
block|{
literal|"-b"
block|,
name|epAddr
block|,
name|dir
block|}
decl_stmt|;
name|app
operator|.
name|start
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|phMock
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

