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
name|java2wsdl
operator|.
name|generator
operator|.
name|wsdl11
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedOutputStream
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
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|OutputStream
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
name|Set
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
name|Import
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|WSDLException
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
name|javax
operator|.
name|wsdl
operator|.
name|xml
operator|.
name|WSDLWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|helpers
operator|.
name|DOMUtils
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
name|SchemaInfo
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
name|staxutils
operator|.
name|StaxUtils
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
name|java2wsdl
operator|.
name|generator
operator|.
name|AbstractGenerator
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
name|wsdl11
operator|.
name|ServiceWSDLBuilder
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
name|WSDLDefinitionBuilder
import|;
end_import

begin_class
specifier|public
class|class
name|WSDL11Generator
extends|extends
name|AbstractGenerator
argument_list|<
name|Definition
argument_list|>
block|{
specifier|public
name|Definition
name|generate
parameter_list|(
specifier|final
name|File
name|dir
parameter_list|)
block|{
name|File
name|file
init|=
name|getOutputBase
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|==
literal|null
operator|&&
name|dir
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|file
operator|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
name|getServiceModel
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|".wsdl"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|file
operator|=
name|dir
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|dir
operator|==
literal|null
condition|)
block|{
name|file
operator|=
operator|new
name|File
argument_list|(
name|getServiceModel
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|".wsdl"
argument_list|)
expr_stmt|;
block|}
name|File
name|outputdir
init|=
name|createOutputDir
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|Definition
name|def
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Writer
name|os
init|=
operator|new
name|FileWriterUtil
argument_list|(
name|file
operator|.
name|getParent
argument_list|()
argument_list|,
name|getOutputStreamCreator
argument_list|()
argument_list|)
operator|.
name|getWriter
argument_list|(
name|file
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|WSDLWriter
name|wsdlWriter
init|=
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newWSDLWriter
argument_list|()
decl_stmt|;
name|ServiceWSDLBuilder
name|builder
init|=
operator|new
name|ServiceWSDLBuilder
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|getServiceModel
argument_list|()
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setUseSchemaImports
argument_list|(
name|this
operator|.
name|allowImports
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|name
init|=
name|file
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|".wsdl"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|".wsdl"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|setBaseFileName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|SchemaInfo
argument_list|>
name|imports
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|SchemaInfo
argument_list|>
argument_list|()
decl_stmt|;
name|def
operator|=
name|builder
operator|.
name|build
argument_list|(
name|imports
argument_list|)
expr_stmt|;
name|wsdlWriter
operator|.
name|writeWSDL
argument_list|(
name|def
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|def
operator|.
name|getImports
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|Import
name|wsdlImport
range|:
name|WSDLDefinitionBuilder
operator|.
name|getImports
argument_list|(
name|def
argument_list|)
control|)
block|{
name|Definition
name|wsdlDef
init|=
name|wsdlImport
operator|.
name|getDefinition
argument_list|()
decl_stmt|;
name|File
name|wsdlFile
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|wsdlImport
operator|.
name|getLocationURI
argument_list|()
argument_list|)
condition|)
block|{
name|wsdlFile
operator|=
operator|new
name|File
argument_list|(
name|outputdir
argument_list|,
name|wsdlImport
operator|.
name|getLocationURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|wsdlFile
operator|=
operator|new
name|File
argument_list|(
name|outputdir
argument_list|,
name|wsdlDef
operator|.
name|getQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|".wsdl"
argument_list|)
expr_stmt|;
block|}
name|OutputStream
name|wsdlOs
init|=
literal|null
decl_stmt|;
try|try
block|{
name|wsdlOs
operator|=
operator|new
name|BufferedOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|wsdlFile
argument_list|)
argument_list|)
expr_stmt|;
name|wsdlWriter
operator|.
name|writeWSDL
argument_list|(
name|wsdlDef
argument_list|,
name|wsdlOs
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|wsdlOs
operator|!=
literal|null
condition|)
block|{
name|wsdlOs
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|SchemaInfo
argument_list|>
name|imp
range|:
name|imports
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|File
name|impfile
init|=
operator|new
name|File
argument_list|(
name|file
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|imp
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|Element
name|el
init|=
name|imp
operator|.
name|getValue
argument_list|()
operator|.
name|getElement
argument_list|()
decl_stmt|;
name|updateImports
argument_list|(
name|el
argument_list|,
name|imports
argument_list|)
expr_stmt|;
name|os
operator|=
operator|new
name|FileWriterUtil
argument_list|(
name|impfile
operator|.
name|getParent
argument_list|()
argument_list|,
name|getToolContext
argument_list|()
operator|.
name|get
argument_list|(
name|OutputStreamCreator
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|getWriter
argument_list|(
name|impfile
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|StaxUtils
operator|.
name|writeTo
argument_list|(
name|el
argument_list|,
name|os
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|customizing
argument_list|(
name|outputdir
argument_list|,
name|name
argument_list|,
name|imports
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSDLException
name|wex
parameter_list|)
block|{
name|wex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|fnfe
parameter_list|)
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
literal|"Output file "
operator|+
name|file
operator|+
literal|" not found"
argument_list|,
name|fnfe
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
name|def
return|;
block|}
specifier|private
name|void
name|updateImports
parameter_list|(
name|Element
name|el
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|SchemaInfo
argument_list|>
name|imports
parameter_list|)
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|imps
init|=
name|DOMUtils
operator|.
name|getChildrenWithName
argument_list|(
name|el
argument_list|,
name|WSDLConstants
operator|.
name|NS_SCHEMA_XSD
argument_list|,
literal|"import"
argument_list|)
decl_stmt|;
for|for
control|(
name|Element
name|e
range|:
name|imps
control|)
block|{
name|String
name|ns
init|=
name|e
operator|.
name|getAttribute
argument_list|(
literal|"namespace"
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|SchemaInfo
argument_list|>
name|ent
range|:
name|imports
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|ent
operator|.
name|getValue
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|ns
argument_list|)
condition|)
block|{
name|e
operator|.
name|setAttribute
argument_list|(
literal|"schemaLocation"
argument_list|,
name|ent
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|customizing
parameter_list|(
specifier|final
name|File
name|outputdir
parameter_list|,
specifier|final
name|String
name|wsdlName
parameter_list|,
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|imports
parameter_list|)
block|{
name|DateTypeCustomGenerator
name|generator
init|=
operator|new
name|DateTypeCustomGenerator
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setWSDLName
argument_list|(
name|wsdlName
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|getServiceModel
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setAllowImports
argument_list|(
name|allowImports
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|addSchemaFiles
argument_list|(
name|imports
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setToolContext
argument_list|(
name|getToolContext
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|outputdir
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

