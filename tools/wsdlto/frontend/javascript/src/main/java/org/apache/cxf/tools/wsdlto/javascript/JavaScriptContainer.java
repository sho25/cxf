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
name|wsdlto
operator|.
name|javascript
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
name|HashMap
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
name|LinkedHashMap
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|javax
operator|.
name|wsdl
operator|.
name|Definition
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
name|common
operator|.
name|WSDLConstants
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
name|util
operator|.
name|PropertiesLoaderUtils
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
name|util
operator|.
name|StringUtils
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
name|xmlschema
operator|.
name|SchemaCollection
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
name|service
operator|.
name|model
operator|.
name|InterfaceInfo
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
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
name|URIParserUtil
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
name|ServiceValidator
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
name|WSDLToJavaContainer
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
name|AbstractWSDLBuilder
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
name|FrontEndProfile
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
name|PluginLoader
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
name|wsdl11
operator|.
name|WSDLServiceBuilder
import|;
end_import

begin_class
specifier|public
class|class
name|JavaScriptContainer
extends|extends
name|WSDLToJavaContainer
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TOOL_NAME
init|=
literal|"wsdl2js"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SERVICE_VALIDATOR
init|=
literal|"META-INF/tools.service.validator.xml"
decl_stmt|;
specifier|public
name|JavaScriptContainer
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
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getArrayKeys
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|set
operator|.
name|add
argument_list|(
name|ToolConstants
operator|.
name|CFG_JSPACKAGEPREFIX
argument_list|)
expr_stmt|;
return|return
name|set
return|;
block|}
specifier|public
name|WSDLConstants
operator|.
name|WSDLVersion
name|getWSDLVersion
parameter_list|()
block|{
name|String
name|version
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDL_VERSION
argument_list|)
decl_stmt|;
return|return
name|WSDLConstants
operator|.
name|getVersion
argument_list|(
name|version
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|ToolException
block|{
name|buildToolContext
argument_list|()
expr_stmt|;
name|validate
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|WSDLConstants
operator|.
name|WSDLVersion
name|version
init|=
name|getWSDLVersion
argument_list|()
decl_stmt|;
name|String
name|wsdlURL
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ServiceInfo
argument_list|>
name|serviceList
init|=
operator|(
name|List
argument_list|<
name|ServiceInfo
argument_list|>
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|SERVICE_LIST
argument_list|)
decl_stmt|;
if|if
condition|(
name|serviceList
operator|==
literal|null
condition|)
block|{
name|serviceList
operator|=
operator|new
name|ArrayList
argument_list|<
name|ServiceInfo
argument_list|>
argument_list|()
expr_stmt|;
name|PluginLoader
name|pluginLoader
init|=
name|PluginLoader
operator|.
name|getInstance
argument_list|()
decl_stmt|;
comment|// for JavaScript generation, we always use JAX-WS.
name|FrontEndProfile
name|frontend
init|=
name|pluginLoader
operator|.
name|getFrontEndProfile
argument_list|(
literal|"jaxws"
argument_list|)
decl_stmt|;
comment|// Build the ServiceModel from the WSDLModel
if|if
condition|(
name|version
operator|==
name|WSDLConstants
operator|.
name|WSDLVersion
operator|.
name|WSDL11
condition|)
block|{
name|AbstractWSDLBuilder
argument_list|<
name|Definition
argument_list|>
name|builder
init|=
name|frontend
operator|.
name|getWSDLBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|build
argument_list|(
name|URIParserUtil
operator|.
name|getAbsoluteURI
argument_list|(
name|wsdlURL
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|customize
argument_list|()
expr_stmt|;
name|Definition
name|definition
init|=
name|builder
operator|.
name|getWSDLModel
argument_list|()
decl_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Definition
operator|.
name|class
argument_list|,
name|definition
argument_list|)
expr_stmt|;
name|builder
operator|.
name|validate
argument_list|(
name|definition
argument_list|)
expr_stmt|;
name|WSDLServiceBuilder
name|serviceBuilder
init|=
operator|new
name|WSDLServiceBuilder
argument_list|(
name|getBus
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|serviceName
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_SERVICENAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|serviceName
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|ServiceInfo
argument_list|>
name|services
init|=
name|serviceBuilder
operator|.
name|buildServices
argument_list|(
name|definition
argument_list|,
name|getServiceQName
argument_list|(
name|definition
argument_list|)
argument_list|)
decl_stmt|;
name|serviceList
operator|.
name|addAll
argument_list|(
name|services
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|definition
operator|.
name|getServices
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|serviceList
operator|=
name|serviceBuilder
operator|.
name|buildServices
argument_list|(
name|definition
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|serviceList
operator|=
name|serviceBuilder
operator|.
name|buildMockServices
argument_list|(
name|definition
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// TODO: wsdl2.0 support
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|InterfaceInfo
argument_list|>
name|interfaces
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|InterfaceInfo
argument_list|>
argument_list|()
decl_stmt|;
name|ServiceInfo
name|service0
init|=
name|serviceList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|SchemaCollection
name|schemaCollection
init|=
name|service0
operator|.
name|getXmlSchemaCollection
argument_list|()
decl_stmt|;
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|XML_SCHEMA_COLLECTION
argument_list|,
name|schemaCollection
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|PORTTYPE_MAP
argument_list|,
name|interfaces
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|ClassCollector
operator|.
name|class
argument_list|,
operator|new
name|ClassCollector
argument_list|()
argument_list|)
expr_stmt|;
name|WSDLToJavaScriptProcessor
name|processor
init|=
operator|new
name|WSDLToJavaScriptProcessor
argument_list|()
decl_stmt|;
for|for
control|(
name|ServiceInfo
name|service
range|:
name|serviceList
control|)
block|{
name|context
operator|.
name|put
argument_list|(
name|ServiceInfo
operator|.
name|class
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|validate
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setEnvironment
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|execute
parameter_list|(
name|boolean
name|exitOnFinish
parameter_list|)
throws|throws
name|ToolException
block|{
try|try
block|{
if|if
condition|(
name|getArgument
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|super
operator|.
name|execute
argument_list|(
name|exitOnFinish
argument_list|)
expr_stmt|;
block|}
name|execute
argument_list|()
expr_stmt|;
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
throw|throw
operator|new
name|ToolException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
finally|finally
block|{
name|tearDown
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setNamespaceJavascriptPrefixes
parameter_list|(
name|ToolContext
name|env
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsPrefixMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_JSPACKAGEPREFIX
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|pns
init|=
literal|null
decl_stmt|;
try|try
block|{
name|pns
operator|=
operator|(
name|String
index|[]
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_JSPACKAGEPREFIX
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"INVALID_PREFIX_MAPPING"
argument_list|,
name|LOG
argument_list|,
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_JSPACKAGEPREFIX
argument_list|)
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
for|for
control|(
name|String
name|jsprefix
range|:
name|pns
control|)
block|{
name|int
name|pos
init|=
name|jsprefix
operator|.
name|indexOf
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|ns
init|=
name|jsprefix
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
decl_stmt|;
name|jsprefix
operator|=
name|jsprefix
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
expr_stmt|;
name|nsPrefixMap
operator|.
name|put
argument_list|(
name|ns
argument_list|,
name|jsprefix
argument_list|)
expr_stmt|;
block|}
block|}
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_JSPREFIXMAP
argument_list|,
name|nsPrefixMap
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
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
if|if
condition|(
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_COMPILE
argument_list|)
condition|)
block|{
name|String
name|clsdir
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
name|CFG_CLASSDIR
argument_list|)
decl_stmt|;
if|if
condition|(
name|clsdir
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
name|clsdir
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
name|clsdir
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
name|String
name|wsdl
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
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|wsdl
argument_list|)
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"NO_WSDL_URL"
argument_list|,
name|LOG
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
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|URIParserUtil
operator|.
name|normalize
argument_list|(
name|wsdl
argument_list|)
argument_list|)
expr_stmt|;
name|String
index|[]
name|bindingFiles
decl_stmt|;
try|try
block|{
name|bindingFiles
operator|=
operator|(
name|String
index|[]
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_BINDING
argument_list|)
expr_stmt|;
if|if
condition|(
name|bindingFiles
operator|==
literal|null
condition|)
block|{
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
name|bindingFiles
operator|=
operator|new
name|String
index|[
literal|1
index|]
expr_stmt|;
name|bindingFiles
index|[
literal|0
index|]
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
name|CFG_BINDING
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|bindingFiles
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|bindingFiles
index|[
name|i
index|]
operator|=
name|URIParserUtil
operator|.
name|getAbsoluteURI
argument_list|(
name|bindingFiles
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_BINDING
argument_list|,
name|bindingFiles
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|buildToolContext
parameter_list|()
block|{
name|context
operator|=
name|getContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|addParameters
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
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|)
operator|==
literal|null
condition|)
block|{
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|,
literal|"."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|context
operator|.
name|containsKey
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDL_VERSION
argument_list|)
condition|)
block|{
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDL_VERSION
argument_list|,
name|WSDLConstants
operator|.
name|WSDL11
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_SUPPRESS_WARNINGS
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|setNamespaceJavascriptPrefixes
argument_list|(
name|context
argument_list|)
expr_stmt|;
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
specifier|public
name|void
name|validate
parameter_list|(
specifier|final
name|ServiceInfo
name|service
parameter_list|)
throws|throws
name|ToolException
block|{
for|for
control|(
name|ServiceValidator
name|validator
range|:
name|getServiceValidators
argument_list|()
control|)
block|{
name|service
operator|.
name|setProperty
argument_list|(
name|ToolContext
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|validator
operator|.
name|setService
argument_list|(
name|service
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|validator
operator|.
name|isValid
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
name|validator
operator|.
name|getErrorMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
specifier|public
name|List
argument_list|<
name|ServiceValidator
argument_list|>
name|getServiceValidators
parameter_list|()
block|{
name|List
argument_list|<
name|ServiceValidator
argument_list|>
name|validators
init|=
operator|new
name|ArrayList
argument_list|<
name|ServiceValidator
argument_list|>
argument_list|()
decl_stmt|;
name|Properties
name|initialExtensions
init|=
literal|null
decl_stmt|;
try|try
block|{
name|initialExtensions
operator|=
name|PropertiesLoaderUtils
operator|.
name|loadAllProperties
argument_list|(
name|SERVICE_VALIDATOR
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
for|for
control|(
name|Object
name|element
range|:
name|initialExtensions
operator|.
name|values
argument_list|()
control|)
block|{
name|String
name|validatorClass
init|=
operator|(
name|String
operator|)
name|element
decl_stmt|;
try|try
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Found service validator : "
operator|+
name|validatorClass
argument_list|)
expr_stmt|;
block|}
name|ServiceValidator
name|validator
init|=
operator|(
name|ServiceValidator
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|validatorClass
argument_list|,
literal|true
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|validators
operator|.
name|add
argument_list|(
name|validator
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"EXTENSION_ADD_FAILED_MSG"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|validators
return|;
block|}
block|}
end_class

end_unit

