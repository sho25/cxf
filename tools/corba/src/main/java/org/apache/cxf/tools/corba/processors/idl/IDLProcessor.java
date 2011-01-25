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
name|processors
operator|.
name|idl
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
name|antlr
operator|.
name|TokenStreamHiddenTokenFilter
import|;
end_import

begin_import
import|import
name|antlr
operator|.
name|collections
operator|.
name|AST
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
name|corba
operator|.
name|common
operator|.
name|Processor
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
name|corba
operator|.
name|common
operator|.
name|ProcessorEnvironment
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
name|corba
operator|.
name|common
operator|.
name|ToolCorbaConstants
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
name|corba
operator|.
name|idlpreprocessor
operator|.
name|DefaultIncludeResolver
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
name|corba
operator|.
name|idlpreprocessor
operator|.
name|DefineState
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
name|corba
operator|.
name|idlpreprocessor
operator|.
name|IdlPreprocessorReader
import|;
end_import

begin_class
specifier|public
class|class
name|IDLProcessor
implements|implements
name|Processor
block|{
specifier|protected
name|ProcessorEnvironment
name|env
decl_stmt|;
specifier|protected
name|IdlPreprocessorReader
name|preprocessor
decl_stmt|;
specifier|private
name|IDLParser
name|parser
decl_stmt|;
specifier|public
name|void
name|setEnvironment
parameter_list|(
name|ProcessorEnvironment
name|penv
parameter_list|)
block|{
name|env
operator|=
name|penv
expr_stmt|;
block|}
specifier|public
name|void
name|process
parameter_list|()
throws|throws
name|ToolException
block|{
name|String
name|location
init|=
name|env
operator|.
name|get
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_IDLFILE
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|location
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
literal|"IDL file "
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|" doesn't exist"
argument_list|)
throw|;
block|}
try|try
block|{
name|URL
name|orig
init|=
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
decl_stmt|;
name|DefaultIncludeResolver
name|includeResolver
init|=
name|getDefaultIncludeResolver
argument_list|(
name|file
operator|.
name|getParentFile
argument_list|()
argument_list|)
decl_stmt|;
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
name|preprocessor
operator|=
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
expr_stmt|;
name|IDLLexer
name|lexer
init|=
operator|new
name|IDLLexer
argument_list|(
operator|new
name|java
operator|.
name|io
operator|.
name|LineNumberReader
argument_list|(
name|preprocessor
argument_list|)
argument_list|)
decl_stmt|;
name|lexer
operator|.
name|setTokenObjectClass
argument_list|(
literal|"antlr.CommonHiddenStreamToken"
argument_list|)
expr_stmt|;
name|TokenStreamHiddenTokenFilter
name|filter
init|=
operator|new
name|TokenStreamHiddenTokenFilter
argument_list|(
name|lexer
argument_list|)
decl_stmt|;
name|filter
operator|.
name|discard
argument_list|(
name|IDLTokenTypes
operator|.
name|WS
argument_list|)
expr_stmt|;
name|filter
operator|.
name|hide
argument_list|(
name|IDLTokenTypes
operator|.
name|SL_COMMENT
argument_list|)
expr_stmt|;
name|filter
operator|.
name|hide
argument_list|(
name|IDLTokenTypes
operator|.
name|ML_COMMENT
argument_list|)
expr_stmt|;
name|parser
operator|=
operator|new
name|IDLParser
argument_list|(
name|filter
argument_list|)
expr_stmt|;
name|parser
operator|.
name|setASTNodeClass
argument_list|(
literal|"antlr.CommonASTWithHiddenTokens"
argument_list|)
expr_stmt|;
name|parser
operator|.
name|specification
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|AST
name|getIDLTree
parameter_list|()
block|{
if|if
condition|(
name|parser
operator|!=
literal|null
condition|)
block|{
return|return
name|parser
operator|.
name|getAST
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
name|DefaultIncludeResolver
name|getDefaultIncludeResolver
parameter_list|(
name|File
name|currentDir
parameter_list|)
block|{
name|DefaultIncludeResolver
name|includeResolver
decl_stmt|;
if|if
condition|(
name|env
operator|.
name|optionSet
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_INCLUDEDIR
argument_list|)
condition|)
block|{
name|String
index|[]
name|includedDirs
init|=
operator|(
name|String
index|[]
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_INCLUDEDIR
argument_list|)
decl_stmt|;
name|File
index|[]
name|includeDirs
init|=
operator|new
name|File
index|[
name|includedDirs
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|includedDirs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|includeDirs
index|[
name|i
index|]
operator|=
operator|new
name|File
argument_list|(
name|includedDirs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|includeResolver
operator|=
operator|new
name|DefaultIncludeResolver
argument_list|(
name|includeDirs
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|includeResolver
operator|=
operator|new
name|DefaultIncludeResolver
argument_list|(
name|currentDir
argument_list|)
expr_stmt|;
block|}
return|return
name|includeResolver
return|;
block|}
block|}
end_class

end_unit

