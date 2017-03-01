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
name|wsdl
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
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Definition
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
name|JAXBException
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|common
operator|.
name|WSDLUtils
import|;
end_import

begin_class
specifier|public
class|class
name|WSDLToCorbaProcessor
extends|extends
name|WSDLToProcessor
block|{
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|WSDLToCorbaProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
name|WSDLToCorbaBinding
name|wsdlToCorbaBinding
decl_stmt|;
name|WSDLToIDLAction
name|idlAction
decl_stmt|;
name|Definition
name|definition
decl_stmt|;
name|String
name|bindName
decl_stmt|;
name|String
name|outputfile
decl_stmt|;
name|String
name|outputdir
init|=
literal|"."
decl_stmt|;
name|String
name|wsdlOutput
decl_stmt|;
name|String
name|idlOutput
decl_stmt|;
name|ProcessorEnvironment
name|env
decl_stmt|;
specifier|public
name|void
name|process
parameter_list|()
throws|throws
name|ToolException
block|{
name|Definition
name|def
init|=
literal|null
decl_stmt|;
name|env
operator|=
name|getEnvironment
argument_list|()
expr_stmt|;
try|try
block|{
comment|// if the corba option is specified - generates wsdl
if|if
condition|(
name|env
operator|.
name|optionSet
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_CORBA
argument_list|)
condition|)
block|{
name|wsdlToCorbaBinding
operator|=
operator|new
name|WSDLToCorbaBinding
argument_list|()
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
name|CFG_IDL
argument_list|)
condition|)
block|{
comment|// if idl option specified it generated idl
name|idlAction
operator|=
operator|new
name|WSDLToIDLAction
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|wsdlToCorbaBinding
operator|==
literal|null
operator|&&
name|idlAction
operator|==
literal|null
condition|)
block|{
name|wsdlToCorbaBinding
operator|=
operator|new
name|WSDLToCorbaBinding
argument_list|()
expr_stmt|;
name|idlAction
operator|=
operator|new
name|WSDLToIDLAction
argument_list|()
expr_stmt|;
block|}
name|setOutputFile
argument_list|()
expr_stmt|;
name|String
name|filename
init|=
name|getFileBase
argument_list|(
name|env
operator|.
name|get
argument_list|(
literal|"wsdlurl"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|wsdlOutput
operator|==
literal|null
operator|)
operator|&&
operator|(
name|wsdlToCorbaBinding
operator|!=
literal|null
operator|)
condition|)
block|{
name|wsdlOutput
operator|=
name|filename
operator|+
literal|"-corba.wsdl"
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|idlOutput
operator|==
literal|null
operator|)
operator|&&
operator|(
name|idlAction
operator|!=
literal|null
operator|)
condition|)
block|{
name|idlOutput
operator|=
name|filename
operator|+
literal|".idl"
expr_stmt|;
block|}
if|if
condition|(
name|wsdlToCorbaBinding
operator|!=
literal|null
condition|)
block|{
name|wsdltoCorba
argument_list|()
expr_stmt|;
name|def
operator|=
name|wsdlToCorbaBinding
operator|.
name|generateCORBABinding
argument_list|()
expr_stmt|;
name|writeToWSDL
argument_list|(
name|def
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|idlAction
operator|!=
literal|null
condition|)
block|{
name|wsdltoIdl
argument_list|()
expr_stmt|;
name|idlAction
operator|.
name|generateIDL
argument_list|(
name|def
argument_list|)
expr_stmt|;
name|writeToIDL
argument_list|(
name|def
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ToolException
name|ex
parameter_list|)
block|{
throw|throw
name|ex
throw|;
block|}
catch|catch
parameter_list|(
name|JAXBException
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
specifier|private
name|void
name|writeToWSDL
parameter_list|(
name|Definition
name|def
parameter_list|)
throws|throws
name|ToolException
block|{
try|try
block|{
name|WSDLUtils
operator|.
name|writeWSDL
argument_list|(
name|def
argument_list|,
name|outputdir
argument_list|,
name|wsdlOutput
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_WRITE_WSDL"
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
name|t
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|writeToIDL
parameter_list|(
name|Definition
name|def
parameter_list|)
throws|throws
name|ToolException
block|{      }
specifier|public
name|void
name|wsdltoCorba
parameter_list|()
block|{
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
name|wsdlToCorbaBinding
operator|.
name|setBindingName
argument_list|(
name|env
operator|.
name|get
argument_list|(
literal|"binding"
argument_list|)
operator|.
name|toString
argument_list|()
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
name|wsdlToCorbaBinding
operator|.
name|addInterfaceName
argument_list|(
name|env
operator|.
name|get
argument_list|(
literal|"porttype"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_PORTTYPE
argument_list|)
operator|)
operator|&&
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
name|wsdlToCorbaBinding
operator|.
name|mapBindingToInterface
argument_list|(
name|env
operator|.
name|get
argument_list|(
literal|"porttype"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|,
name|env
operator|.
name|get
argument_list|(
literal|"binding"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
operator|!
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_PORTTYPE
argument_list|)
operator|)
operator|&&
operator|!
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
name|wsdlToCorbaBinding
operator|.
name|setAllBindings
argument_list|(
literal|true
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
name|wsdlToCorbaBinding
operator|.
name|setWsdlFile
argument_list|(
name|env
operator|.
name|get
argument_list|(
literal|"wsdlurl"
argument_list|)
operator|.
name|toString
argument_list|()
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
name|wsdlToCorbaBinding
operator|.
name|setNamespace
argument_list|(
name|env
operator|.
name|get
argument_list|(
literal|"namespace"
argument_list|)
operator|.
name|toString
argument_list|()
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
name|CFG_ADDRESS
argument_list|)
condition|)
block|{
name|wsdlToCorbaBinding
operator|.
name|setAddress
argument_list|(
name|env
operator|.
name|get
argument_list|(
literal|"address"
argument_list|)
operator|.
name|toString
argument_list|()
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
name|CFG_ADDRESSFILE
argument_list|)
condition|)
block|{
name|wsdlToCorbaBinding
operator|.
name|setAddressFile
argument_list|(
name|env
operator|.
name|get
argument_list|(
literal|"addressfile"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// need to add wrapped
name|wsdlToCorbaBinding
operator|.
name|setOutputDirectory
argument_list|(
name|getOutputDir
argument_list|()
argument_list|)
expr_stmt|;
name|wsdlToCorbaBinding
operator|.
name|setOutputFile
argument_list|(
name|wsdlOutput
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|wsdltoIdl
parameter_list|()
block|{
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
name|idlAction
operator|.
name|setBindingName
argument_list|(
name|env
operator|.
name|get
argument_list|(
literal|"binding"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|wsdlToCorbaBinding
operator|!=
literal|null
condition|)
block|{
name|String
name|portType
init|=
literal|null
decl_stmt|;
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
name|portType
operator|=
name|env
operator|.
name|get
argument_list|(
literal|"porttype"
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
if|if
condition|(
name|portType
operator|!=
literal|null
condition|)
block|{
name|String
name|bindingName
init|=
name|wsdlToCorbaBinding
operator|.
name|getMappedBindingName
argument_list|(
name|portType
argument_list|)
decl_stmt|;
if|if
condition|(
name|bindingName
operator|!=
literal|null
condition|)
block|{
name|idlAction
operator|.
name|setBindingName
argument_list|(
name|bindingName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|//try to get the binding name from the wsdlToCorbaBinding
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|String
argument_list|>
name|bindingNames
init|=
name|wsdlToCorbaBinding
operator|.
name|getGeneratedBindingNames
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|bindingNames
operator|!=
literal|null
operator|)
operator|&&
operator|(
operator|!
name|bindingNames
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
name|idlAction
operator|.
name|setBindingName
argument_list|(
name|bindingNames
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|bindingNames
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Warning: Generating idl only for the binding "
operator|+
name|bindingNames
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// generate idl for all bindings.
name|idlAction
operator|.
name|setGenerateAllBindings
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|idlAction
operator|.
name|setGenerateAllBindings
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
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
name|name
init|=
name|env
operator|.
name|get
argument_list|(
literal|"wsdlurl"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|idlAction
operator|.
name|setWsdlFile
argument_list|(
name|name
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
name|CFG_VERBOSE
argument_list|)
condition|)
block|{
name|idlAction
operator|.
name|setVerboseOn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
name|idlAction
operator|.
name|setOutputDirectory
argument_list|(
name|getOutputDir
argument_list|()
argument_list|)
expr_stmt|;
name|idlAction
operator|.
name|setOutputFile
argument_list|(
name|idlOutput
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getOutputDir
parameter_list|()
block|{
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
name|outputdir
operator|=
name|env
operator|.
name|get
argument_list|(
literal|"outputdir"
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
name|File
name|fileOutputDir
init|=
operator|new
name|File
argument_list|(
name|outputdir
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|fileOutputDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|fileOutputDir
operator|.
name|mkdir
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|outputdir
return|;
block|}
specifier|private
name|void
name|setOutputFile
parameter_list|()
block|{
name|wsdlOutput
operator|=
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_WSDLOUTPUTFILE
argument_list|)
expr_stmt|;
name|idlOutput
operator|=
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_IDLOUTPUTFILE
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
name|wsdlOutput
operator|==
literal|null
operator|)
operator|&&
operator|(
name|idlOutput
operator|==
literal|null
operator|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Using default wsdl/idl filenames..."
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getFileBase
parameter_list|(
name|String
name|wsdlUrl
parameter_list|)
block|{
name|String
name|fileBase
init|=
name|wsdlUrl
decl_stmt|;
name|StringTokenizer
name|tok
init|=
operator|new
name|StringTokenizer
argument_list|(
name|wsdlUrl
argument_list|,
literal|"\\/"
argument_list|)
decl_stmt|;
while|while
condition|(
name|tok
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|fileBase
operator|=
name|tok
operator|.
name|nextToken
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|fileBase
operator|.
name|endsWith
argument_list|(
literal|".wsdl"
argument_list|)
condition|)
block|{
name|fileBase
operator|=
name|fileBase
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|fileBase
operator|.
name|length
argument_list|()
operator|-
literal|5
argument_list|)
expr_stmt|;
block|}
return|return
name|fileBase
return|;
block|}
block|}
end_class

end_unit

