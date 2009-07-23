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
name|misc
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
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|i18n
operator|.
name|Message
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
name|AbstractCXFToolContainer
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
name|CommandInterfaceUtils
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
name|common
operator|.
name|toolspec
operator|.
name|ToolRunner
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
name|toolspec
operator|.
name|ToolSpec
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
name|toolspec
operator|.
name|parser
operator|.
name|BadUsageException
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
name|toolspec
operator|.
name|parser
operator|.
name|CommandDocument
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
name|toolspec
operator|.
name|parser
operator|.
name|ErrorVisitor
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
name|misc
operator|.
name|processor
operator|.
name|WSDLToServiceProcessor
import|;
end_import

begin_class
specifier|public
class|class
name|WSDLToService
extends|extends
name|AbstractCXFToolContainer
block|{
specifier|static
specifier|final
name|String
name|TOOL_NAME
init|=
literal|"wsdl2service"
decl_stmt|;
specifier|public
name|WSDLToService
parameter_list|(
name|ToolSpec
name|toolspec
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|TOOL_NAME
argument_list|,
name|toolspec
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Set
name|getArrayKeys
parameter_list|()
block|{
return|return
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
return|;
block|}
specifier|public
name|void
name|execute
parameter_list|(
name|boolean
name|exitOnFinish
parameter_list|)
block|{
name|WSDLToServiceProcessor
name|processor
init|=
operator|new
name|WSDLToServiceProcessor
argument_list|()
decl_stmt|;
try|try
block|{
name|super
operator|.
name|execute
argument_list|(
name|exitOnFinish
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|hasInfoOption
argument_list|()
condition|)
block|{
name|ToolContext
name|env
init|=
operator|new
name|ToolContext
argument_list|()
decl_stmt|;
name|env
operator|.
name|setParameters
argument_list|(
name|getParametersMap
argument_list|(
name|getArrayKeys
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|isVerboseOn
argument_list|()
condition|)
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_VERBOSE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_CMD_ARG
argument_list|,
name|getArgument
argument_list|()
argument_list|)
expr_stmt|;
name|validate
argument_list|(
name|env
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
block|}
block|}
catch|catch
parameter_list|(
name|ToolException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|BadUsageException
condition|)
block|{
name|printUsageException
argument_list|(
name|TOOL_NAME
argument_list|,
operator|(
name|BadUsageException
operator|)
name|ex
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|err
operator|.
name|println
argument_list|()
expr_stmt|;
name|err
operator|.
name|println
argument_list|(
literal|"WSDLToService Error : "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isVerboseOn
argument_list|()
condition|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|(
name|err
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|err
operator|.
name|println
argument_list|()
expr_stmt|;
name|err
operator|.
name|println
argument_list|(
literal|"WSDLToService Error : "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isVerboseOn
argument_list|()
condition|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|(
name|err
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|tearDown
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|validate
parameter_list|(
name|ToolContext
name|env
parameter_list|)
throws|throws
name|ToolException
block|{
name|String
name|outdir
init|=
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|)
decl_stmt|;
if|if
condition|(
name|outdir
operator|!=
literal|null
condition|)
block|{
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|outdir
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|dir
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|dir
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"DIRECTORY_COULD_NOT_BE_CREATED"
argument_list|,
name|LOG
argument_list|,
name|outdir
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|dir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"NOT_A_DIRECTORY"
argument_list|,
name|LOG
argument_list|,
name|outdir
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
block|}
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|pargs
parameter_list|)
block|{
name|CommandInterfaceUtils
operator|.
name|commandCommonMain
argument_list|()
expr_stmt|;
try|try
block|{
name|ToolRunner
operator|.
name|runTool
argument_list|(
name|WSDLToService
operator|.
name|class
argument_list|,
name|WSDLToService
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"wsdl2service.xml"
argument_list|)
argument_list|,
literal|false
argument_list|,
name|pargs
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error : "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|()
expr_stmt|;
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|checkParams
parameter_list|(
name|ErrorVisitor
name|errors
parameter_list|)
throws|throws
name|ToolException
block|{
name|CommandDocument
name|doc
init|=
name|super
operator|.
name|getCommandDocument
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|doc
operator|.
name|hasParameter
argument_list|(
literal|"wsdlurl"
argument_list|)
condition|)
block|{
name|errors
operator|.
name|add
argument_list|(
operator|new
name|ErrorVisitor
operator|.
name|UserError
argument_list|(
literal|"WSDL/SCHEMA URL has to be specified"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|errors
operator|.
name|getErrors
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"PARAMETER_MISSING"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
operator|new
name|BadUsageException
argument_list|(
name|getUsage
argument_list|()
argument_list|,
name|errors
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

