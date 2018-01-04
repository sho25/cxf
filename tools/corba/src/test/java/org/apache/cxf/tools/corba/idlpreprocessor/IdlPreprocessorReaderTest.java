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
name|corba
operator|.
name|idlpreprocessor
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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|LineNumberReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|IdlPreprocessorReaderTest
extends|extends
name|Assert
block|{
specifier|private
name|URL
name|findTestResource
parameter_list|(
name|String
name|spec
parameter_list|)
block|{
name|String
name|location
init|=
literal|"/idlpreprocessor/"
operator|+
name|spec
decl_stmt|;
return|return
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|location
argument_list|)
return|;
block|}
specifier|private
class|class
name|ClassPathIncludeResolver
implements|implements
name|IncludeResolver
block|{
specifier|public
name|URL
name|findSystemInclude
parameter_list|(
name|String
name|spec
parameter_list|)
block|{
return|return
name|findUserInclude
argument_list|(
name|spec
argument_list|)
return|;
block|}
specifier|public
name|URL
name|findUserInclude
parameter_list|(
name|String
name|spec
parameter_list|)
block|{
return|return
name|findTestResource
argument_list|(
name|spec
argument_list|)
return|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolvedInA
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|location
init|=
literal|"A.idl"
decl_stmt|;
specifier|final
name|IdlPreprocessorReader
name|includeReader
init|=
name|createPreprocessorReader
argument_list|(
name|location
argument_list|)
decl_stmt|;
specifier|final
name|String
name|expectedResultLocation
init|=
literal|"A-resolved.idl"
decl_stmt|;
name|assertExpectedPreprocessingResult
argument_list|(
name|expectedResultLocation
argument_list|,
name|includeReader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultiFileResolve
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|location
init|=
literal|"B.idl"
decl_stmt|;
specifier|final
name|IdlPreprocessorReader
name|includeReader
init|=
name|createPreprocessorReader
argument_list|(
name|location
argument_list|)
decl_stmt|;
specifier|final
name|String
name|expectedResultLocation
init|=
literal|"B-resolved.idl"
decl_stmt|;
name|assertExpectedPreprocessingResult
argument_list|(
name|expectedResultLocation
argument_list|,
name|includeReader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIfElseHandling
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|location
init|=
literal|"C.idl"
decl_stmt|;
specifier|final
name|IdlPreprocessorReader
name|includeReader
init|=
name|createPreprocessorReader
argument_list|(
name|location
argument_list|)
decl_stmt|;
specifier|final
name|String
name|expectedResultLocation
init|=
literal|"C-resolved.idl"
decl_stmt|;
name|assertExpectedPreprocessingResult
argument_list|(
name|expectedResultLocation
argument_list|,
name|includeReader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMaximumIncludeDepthIsDetected
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|String
name|location
init|=
literal|"MaximumIncludeDepthExceeded.idl"
decl_stmt|;
try|try
block|{
specifier|final
name|IdlPreprocessorReader
name|preprocessor
init|=
name|createPreprocessorReader
argument_list|(
name|location
argument_list|)
decl_stmt|;
name|consumeReader
argument_list|(
name|preprocessor
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"exeeding maximum include depth is not detected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PreprocessingException
name|ex
parameter_list|)
block|{
name|String
name|msg
init|=
name|ex
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|Pattern
operator|.
name|matches
argument_list|(
literal|".*more than .* nested #includes.*"
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnresolvableInclude
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|String
name|location
init|=
literal|"UnresolvableInclude.idl"
decl_stmt|;
try|try
block|{
specifier|final
name|IdlPreprocessorReader
name|preprocessor
init|=
name|createPreprocessorReader
argument_list|(
name|location
argument_list|)
decl_stmt|;
name|consumeReader
argument_list|(
name|preprocessor
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"unresolvable include not detected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PreprocessingException
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
name|indexOf
argument_list|(
literal|"nirvana.idl"
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ex
operator|.
name|getLine
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ex
operator|.
name|getUrl
argument_list|()
operator|.
name|getPath
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"/UnresolvableInclude.idl"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDefaultIncludeResolver
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|location
init|=
literal|"B.idl"
decl_stmt|;
comment|// uses<> notation for include
specifier|final
name|URL
name|orig
init|=
name|findTestResource
argument_list|(
name|location
argument_list|)
decl_stmt|;
specifier|final
name|File
name|origFile
init|=
operator|new
name|File
argument_list|(
name|orig
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|origFile
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|origFile
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|indexOf
argument_list|(
name|location
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|DefaultIncludeResolver
name|includeResolver
init|=
operator|new
name|DefaultIncludeResolver
argument_list|(
name|dir
argument_list|)
decl_stmt|;
specifier|final
name|DefineState
name|defineState
init|=
operator|new
name|DefineState
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|IdlPreprocessorReader
name|includeReader
init|=
operator|new
name|IdlPreprocessorReader
argument_list|(
name|orig
argument_list|,
name|location
argument_list|,
name|includeResolver
argument_list|,
name|defineState
argument_list|)
decl_stmt|;
specifier|final
name|String
name|expectedResultLocation
init|=
literal|"B-resolved.idl"
decl_stmt|;
name|assertExpectedPreprocessingResult
argument_list|(
name|expectedResultLocation
argument_list|,
name|includeReader
argument_list|)
expr_stmt|;
block|}
specifier|private
name|IdlPreprocessorReader
name|createPreprocessorReader
parameter_list|(
specifier|final
name|String
name|location
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|URL
name|orig
init|=
name|findTestResource
argument_list|(
name|location
argument_list|)
decl_stmt|;
specifier|final
name|ClassPathIncludeResolver
name|includeResolver
init|=
operator|new
name|ClassPathIncludeResolver
argument_list|()
decl_stmt|;
specifier|final
name|DefineState
name|defineState
init|=
operator|new
name|DefineState
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|IdlPreprocessorReader
argument_list|(
name|orig
argument_list|,
name|location
argument_list|,
name|includeResolver
argument_list|,
name|defineState
argument_list|)
return|;
block|}
specifier|private
name|void
name|assertExpectedPreprocessingResult
parameter_list|(
specifier|final
name|String
name|expectedResultLocation
parameter_list|,
specifier|final
name|IdlPreprocessorReader
name|includeReader
parameter_list|)
throws|throws
name|UnsupportedEncodingException
throws|,
name|IOException
block|{
name|LineNumberReader
name|oReader
init|=
operator|new
name|LineNumberReader
argument_list|(
name|includeReader
argument_list|)
decl_stmt|;
name|InputStream
name|resolved
init|=
name|findTestResource
argument_list|(
name|expectedResultLocation
argument_list|)
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|LineNumberReader
name|rReader
init|=
operator|new
name|LineNumberReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|resolved
argument_list|,
literal|"ISO-8859-1"
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|boolean
name|eof
init|=
literal|false
decl_stmt|;
do|do
block|{
name|int
name|line
init|=
name|rReader
operator|.
name|getLineNumber
argument_list|()
operator|+
literal|1
decl_stmt|;
name|String
name|actualLine
init|=
name|oReader
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|String
name|expectedLine
init|=
name|rReader
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"difference in line "
operator|+
name|line
argument_list|,
name|expectedLine
argument_list|,
name|actualLine
argument_list|)
expr_stmt|;
name|eof
operator|=
name|actualLine
operator|==
literal|null
operator|||
name|expectedLine
operator|==
literal|null
expr_stmt|;
block|}
do|while
condition|(
operator|!
name|eof
condition|)
do|;
block|}
finally|finally
block|{
name|rReader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|consumeReader
parameter_list|(
specifier|final
name|Reader
name|includeReader
parameter_list|)
throws|throws
name|IOException
block|{
name|LineNumberReader
name|oReader
init|=
operator|new
name|LineNumberReader
argument_list|(
name|includeReader
argument_list|)
decl_stmt|;
name|String
name|line
init|=
literal|null
decl_stmt|;
do|do
block|{
name|line
operator|=
name|oReader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
do|while
condition|(
name|line
operator|!=
literal|null
condition|)
do|;
block|}
block|}
end_class

end_unit

