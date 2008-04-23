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
name|processors
operator|.
name|wsdl
operator|.
name|WSDLToCorbaProcessor
import|;
end_import

begin_comment
comment|/**  * This class can augment a plain WSDL definition with CORBA binding  * information, and can take WSDL CORBA binding information and convert it into  * the equivalent CORBA IDL.  */
end_comment

begin_class
specifier|public
class|class
name|WSDLToIDL
extends|extends
name|AbstractCXFToolContainer
block|{
specifier|static
specifier|final
name|String
name|TOOL_NAME
init|=
literal|"wsdltoidl"
decl_stmt|;
specifier|private
specifier|static
name|String
index|[]
name|args
decl_stmt|;
name|String
name|idlOutput
decl_stmt|;
name|String
name|wsdlOutput
decl_stmt|;
specifier|public
name|WSDLToIDL
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
name|WSDLToCorbaProcessor
name|corbaProcessor
init|=
operator|new
name|WSDLToCorbaProcessor
argument_list|()
decl_stmt|;
name|ProcessorEnvironment
name|env
init|=
literal|null
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
name|env
operator|=
operator|new
name|ProcessorEnvironment
argument_list|()
expr_stmt|;
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
name|args
argument_list|)
expr_stmt|;
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
name|doc
operator|.
name|hasParameter
argument_list|(
literal|"corba"
argument_list|)
condition|)
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_CORBA
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|doc
operator|.
name|hasParameter
argument_list|(
literal|"idl"
argument_list|)
condition|)
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_IDL
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
name|initialise
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|validate
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|corbaProcessor
operator|.
name|setEnvironment
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|corbaProcessor
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
name|System
operator|.
name|err
operator|.
name|println
argument_list|()
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
argument_list|()
expr_stmt|;
block|}
throw|throw
name|ex
throw|;
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
if|if
condition|(
name|isVerboseOn
argument_list|()
condition|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
throw|throw
operator|new
name|ToolException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|initialise
parameter_list|(
name|ProcessorEnvironment
name|env
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
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_BINDING
argument_list|)
condition|)
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_BINDING
argument_list|,
name|doc
operator|.
name|getParameter
argument_list|(
literal|"binding"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_PORTTYPE
argument_list|)
condition|)
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_PORTTYPE
argument_list|,
name|doc
operator|.
name|getParameter
argument_list|(
literal|"porttype"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|)
condition|)
block|{
name|String
name|wsdlname
init|=
name|doc
operator|.
name|getParameter
argument_list|(
literal|"wsdlurl"
argument_list|)
decl_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|wsdlname
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_NAMESPACE
argument_list|)
condition|)
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_NAMESPACE
argument_list|,
name|doc
operator|.
name|getParameter
argument_list|(
literal|"namespace"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|env
operator|.
name|optionSet
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_WSDLOUTPUTFILE
argument_list|)
condition|)
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_WSDLOUTPUTFILE
argument_list|,
name|doc
operator|.
name|getParameter
argument_list|(
literal|"wsdloutputfile"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|env
operator|.
name|optionSet
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_IDLOUTPUTFILE
argument_list|)
condition|)
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_IDLOUTPUTFILE
argument_list|,
name|doc
operator|.
name|getParameter
argument_list|(
literal|"idloutputfile"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|)
condition|)
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|,
name|doc
operator|.
name|getParameter
argument_list|(
literal|"outputdir"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// need to add wrapped
block|}
specifier|public
specifier|static
name|void
name|run
parameter_list|(
name|String
index|[]
name|arguments
parameter_list|)
throws|throws
name|Exception
block|{
name|ToolRunner
operator|.
name|runTool
argument_list|(
name|WSDLToIDL
operator|.
name|class
argument_list|,
name|WSDLToIDL
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|ToolCorbaConstants
operator|.
name|TOOLSPECS_BASE
operator|+
literal|"wsdl2idl.xml"
argument_list|)
argument_list|,
literal|false
argument_list|,
name|arguments
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|arguments
parameter_list|)
block|{
try|try
block|{
name|run
argument_list|(
name|arguments
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
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|validate
parameter_list|(
name|ProcessorEnvironment
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
condition|)
block|{
name|dir
operator|.
name|mkdir
argument_list|()
expr_stmt|;
block|}
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

