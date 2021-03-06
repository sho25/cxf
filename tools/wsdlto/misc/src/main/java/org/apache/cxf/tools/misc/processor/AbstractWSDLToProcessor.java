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
operator|.
name|processor
package|;
end_package

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
name|Writer
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
name|Map
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
name|wsdl
operator|.
name|extensions
operator|.
name|ExtensionRegistry
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|factory
operator|.
name|WSDLFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXParseException
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
name|Bus
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
name|BusFactory
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
name|util
operator|.
name|ClassCollector
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
name|util
operator|.
name|FileWriterUtil
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
name|util
operator|.
name|OutputStreamCreator
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
name|validator
operator|.
name|internal
operator|.
name|WSDL11Validator
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
name|wsdlto
operator|.
name|core
operator|.
name|WSDLDefinitionBuilder
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
name|wsdl
operator|.
name|WSDLExtensibilityPlugin
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
name|wsdl
operator|.
name|WSDLManager
import|;
end_import

begin_class
specifier|public
class|class
name|AbstractWSDLToProcessor
implements|implements
name|Processor
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
name|AbstractWSDLToProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|WSDL_FILE_NAME_EXT
init|=
literal|".wsdl"
decl_stmt|;
specifier|protected
name|Definition
name|wsdlDefinition
decl_stmt|;
specifier|protected
name|ToolContext
name|env
decl_stmt|;
specifier|protected
name|WSDLFactory
name|wsdlFactory
decl_stmt|;
specifier|protected
name|ExtensionRegistry
name|extReg
decl_stmt|;
specifier|protected
name|ClassCollector
name|classColletor
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|WSDLExtensibilityPlugin
argument_list|>
name|wsdlPlugins
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|protected
name|Writer
name|getOutputWriter
parameter_list|(
name|String
name|newNameExt
parameter_list|)
throws|throws
name|ToolException
block|{
name|Writer
name|writer
init|=
literal|null
decl_stmt|;
name|String
name|newName
init|=
literal|null
decl_stmt|;
name|String
name|outputDir
decl_stmt|;
if|if
condition|(
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTFILE
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|newName
operator|=
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTFILE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|oldName
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
name|CFG_WSDLURL
argument_list|)
decl_stmt|;
name|int
name|position
init|=
name|oldName
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|position
operator|<
literal|0
condition|)
block|{
name|position
operator|=
name|oldName
operator|.
name|lastIndexOf
argument_list|(
literal|'\\'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|position
operator|>=
literal|0
condition|)
block|{
name|oldName
operator|=
name|oldName
operator|.
name|substring
argument_list|(
name|position
operator|+
literal|1
argument_list|,
name|oldName
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|oldName
operator|.
name|toLowerCase
argument_list|()
operator|.
name|indexOf
argument_list|(
name|WSDL_FILE_NAME_EXT
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|newName
operator|=
name|oldName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|oldName
operator|.
name|length
argument_list|()
operator|-
literal|5
argument_list|)
operator|+
name|newNameExt
operator|+
name|WSDL_FILE_NAME_EXT
expr_stmt|;
block|}
else|else
block|{
name|newName
operator|=
name|oldName
operator|+
name|newNameExt
expr_stmt|;
block|}
block|}
if|if
condition|(
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|outputDir
operator|=
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
expr_stmt|;
if|if
condition|(
operator|!
operator|(
literal|"/"
operator|.
name|equals
argument_list|(
name|outputDir
operator|.
name|substring
argument_list|(
name|outputDir
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
operator|||
literal|"\\"
operator|.
name|equals
argument_list|(
name|outputDir
operator|.
name|substring
argument_list|(
name|outputDir
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
operator|)
condition|)
block|{
name|outputDir
operator|=
name|outputDir
operator|+
literal|"/"
expr_stmt|;
block|}
block|}
else|else
block|{
name|outputDir
operator|=
literal|"./"
expr_stmt|;
block|}
name|FileWriterUtil
name|fw
init|=
operator|new
name|FileWriterUtil
argument_list|(
name|outputDir
argument_list|,
name|env
operator|.
name|get
argument_list|(
name|OutputStreamCreator
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|writer
operator|=
name|fw
operator|.
name|getWriter
argument_list|(
literal|""
argument_list|,
name|newName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_WRITE_FILE"
argument_list|,
name|LOG
argument_list|,
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|)
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"file.seperator"
argument_list|)
operator|+
name|newName
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|ioe
argument_list|)
throw|;
block|}
return|return
name|writer
return|;
block|}
specifier|protected
name|void
name|parseWSDL
parameter_list|(
name|String
name|wsdlURL
parameter_list|)
throws|throws
name|ToolException
block|{
name|Bus
name|bus
init|=
name|env
operator|.
name|get
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
name|bus
operator|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
name|WSDLDefinitionBuilder
name|builder
init|=
operator|new
name|WSDLDefinitionBuilder
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|wsdlDefinition
operator|=
name|builder
operator|.
name|build
argument_list|(
name|wsdlURL
argument_list|)
expr_stmt|;
if|if
condition|(
name|env
operator|.
name|fullValidateWSDL
argument_list|()
condition|)
block|{
name|validate
argument_list|(
name|wsdlDefinition
argument_list|,
name|env
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
name|WSDLManager
name|mgr
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|mgr
operator|.
name|removeDefinition
argument_list|(
name|wsdlDefinition
argument_list|)
expr_stmt|;
name|wsdlFactory
operator|=
name|mgr
operator|.
name|getWSDLFactory
argument_list|()
expr_stmt|;
name|extReg
operator|=
name|mgr
operator|.
name|getExtensionRegistry
argument_list|()
expr_stmt|;
name|wsdlPlugins
operator|=
name|builder
operator|.
name|getWSDLPlugins
argument_list|()
expr_stmt|;
block|}
specifier|public
name|WSDLExtensibilityPlugin
name|getWSDLPlugin
parameter_list|(
specifier|final
name|String
name|key
parameter_list|,
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'-'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|clz
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|WSDLExtensibilityPlugin
name|plugin
init|=
name|wsdlPlugins
operator|.
name|get
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|plugin
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"FOUND_NO_WSDL_PLUGIN"
argument_list|,
name|LOG
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|,
name|clz
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|plugin
return|;
block|}
specifier|protected
name|void
name|init
parameter_list|()
throws|throws
name|ToolException
block|{      }
specifier|public
name|Definition
name|getWSDLDefinition
parameter_list|()
block|{
return|return
name|this
operator|.
name|wsdlDefinition
return|;
block|}
specifier|public
name|void
name|process
parameter_list|()
throws|throws
name|ToolException
block|{     }
specifier|public
name|void
name|validateWSDL
parameter_list|()
throws|throws
name|ToolException
block|{
if|if
condition|(
name|env
operator|.
name|fullValidateWSDL
argument_list|()
condition|)
block|{
name|WSDL11Validator
name|validator
init|=
operator|new
name|WSDL11Validator
argument_list|(
name|this
operator|.
name|wsdlDefinition
argument_list|,
name|this
operator|.
name|env
argument_list|)
decl_stmt|;
name|validator
operator|.
name|isValid
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setEnvironment
parameter_list|(
name|ToolContext
name|penv
parameter_list|)
block|{
name|this
operator|.
name|env
operator|=
name|penv
expr_stmt|;
block|}
specifier|public
name|ToolContext
name|getEnvironment
parameter_list|()
block|{
return|return
name|this
operator|.
name|env
return|;
block|}
specifier|public
name|void
name|error
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|env
operator|.
name|isVerbose
argument_list|()
condition|)
block|{
name|exception
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Parsing schema error: \n"
operator|+
name|exception
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|fatalError
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|env
operator|.
name|isVerbose
argument_list|()
condition|)
block|{
name|exception
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Parsing schema fatal error: \n"
operator|+
name|exception
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|info
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|env
operator|.
name|isVerbose
argument_list|()
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Parsing schema info: "
operator|+
name|exception
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|warning
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|env
operator|.
name|isVerbose
argument_list|()
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Parsing schema warning "
operator|+
name|exception
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|validate
parameter_list|(
specifier|final
name|Definition
name|def
parameter_list|,
name|ToolContext
name|context
parameter_list|,
name|Bus
name|bus
parameter_list|)
throws|throws
name|ToolException
block|{
return|return
operator|new
name|WSDL11Validator
argument_list|(
name|def
argument_list|,
name|context
argument_list|,
name|bus
argument_list|)
operator|.
name|isValid
argument_list|()
return|;
block|}
block|}
end_class

end_unit

