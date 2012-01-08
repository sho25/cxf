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
name|Writer
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
name|Collection
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
name|TreeMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Binding
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
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|PortType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Types
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
name|extensions
operator|.
name|ExtensibilityElement
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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|antlr
operator|.
name|ASTVisitor
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
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|CorbaConstants
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
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|TypeMappingType
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
name|CastUtils
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaCollection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaForm
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|constants
operator|.
name|Constants
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|WSDLASTVisitor
implements|implements
name|ASTVisitor
block|{
name|Definition
name|definition
decl_stmt|;
name|XmlSchema
name|schema
decl_stmt|;
name|XmlSchemaCollection
name|schemas
decl_stmt|;
name|TypeMappingType
name|typeMap
decl_stmt|;
name|ScopeNameCollection
name|scopedNames
decl_stmt|;
name|ScopeNameCollection
name|recursionList
decl_stmt|;
name|DeferredActionCollection
name|deferredActions
decl_stmt|;
name|String
name|targetNamespace
decl_stmt|;
specifier|private
name|boolean
name|declaredWSAImport
decl_stmt|;
specifier|private
name|boolean
name|supportPolymorphicFactories
decl_stmt|;
specifier|private
name|XmlSchemaType
name|sequenceOctetType
decl_stmt|;
specifier|private
name|boolean
name|boundedStringOverride
decl_stmt|;
specifier|private
name|String
name|idlFile
decl_stmt|;
specifier|private
name|String
name|outputDir
decl_stmt|;
specifier|private
name|String
name|importSchemaFilename
decl_stmt|;
specifier|private
name|boolean
name|schemaGenerated
decl_stmt|;
specifier|private
name|ModuleToNSMapper
name|moduleToNSMapper
decl_stmt|;
specifier|private
name|WSDLSchemaManager
name|manager
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Scope
argument_list|,
name|List
argument_list|<
name|Scope
argument_list|>
argument_list|>
name|inheritScopeMap
decl_stmt|;
specifier|private
name|String
name|pragmaPrefix
decl_stmt|;
specifier|public
name|WSDLASTVisitor
parameter_list|(
name|String
name|tns
parameter_list|,
name|String
name|schemans
parameter_list|,
name|String
name|corbatypemaptns
parameter_list|,
name|String
name|pragmaPrefix
parameter_list|)
throws|throws
name|WSDLException
throws|,
name|JAXBException
block|{
name|manager
operator|=
operator|new
name|WSDLSchemaManager
argument_list|()
expr_stmt|;
name|definition
operator|=
name|manager
operator|.
name|createWSDLDefinition
argument_list|(
name|tns
argument_list|)
expr_stmt|;
name|inheritScopeMap
operator|=
operator|new
name|TreeMap
argument_list|<
name|Scope
argument_list|,
name|List
argument_list|<
name|Scope
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|targetNamespace
operator|=
name|tns
expr_stmt|;
name|schemas
operator|=
operator|new
name|XmlSchemaCollection
argument_list|()
expr_stmt|;
name|scopedNames
operator|=
operator|new
name|ScopeNameCollection
argument_list|()
expr_stmt|;
name|deferredActions
operator|=
operator|new
name|DeferredActionCollection
argument_list|()
expr_stmt|;
if|if
condition|(
name|schemans
operator|==
literal|null
condition|)
block|{
name|schemans
operator|=
name|tns
expr_stmt|;
block|}
name|schema
operator|=
name|manager
operator|.
name|createXmlSchemaForDefinition
argument_list|(
name|definition
argument_list|,
name|schemans
argument_list|,
name|schemas
argument_list|)
expr_stmt|;
name|declaredWSAImport
operator|=
literal|false
expr_stmt|;
name|typeMap
operator|=
name|manager
operator|.
name|createCorbaTypeMap
argument_list|(
name|definition
argument_list|,
name|corbatypemaptns
argument_list|)
expr_stmt|;
comment|// idl:sequence<octet> maps to xsd:base64Binary by default
name|sequenceOctetType
operator|=
name|schemas
operator|.
name|getTypeByQName
argument_list|(
name|Constants
operator|.
name|XSD_BASE64
argument_list|)
expr_stmt|;
comment|// treat bounded corba:string/corba:wstring as unbounded if set to true
name|setBoundedStringOverride
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|moduleToNSMapper
operator|=
operator|new
name|ModuleToNSMapper
argument_list|()
expr_stmt|;
name|this
operator|.
name|setPragmaPrefix
argument_list|(
name|pragmaPrefix
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WSDLASTVisitor
parameter_list|(
name|String
name|tns
parameter_list|,
name|String
name|schemans
parameter_list|,
name|String
name|corbatypemaptns
parameter_list|)
throws|throws
name|WSDLException
throws|,
name|JAXBException
block|{
name|this
argument_list|(
name|tns
argument_list|,
name|schemans
argument_list|,
name|corbatypemaptns
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|visit
parameter_list|(
name|AST
name|node
parameter_list|)
block|{
comment|//<specification> ::=<definition>+
while|while
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|Scope
name|rootScope
init|=
operator|new
name|Scope
argument_list|()
decl_stmt|;
name|DefinitionVisitor
name|definitionVisitor
init|=
operator|new
name|DefinitionVisitor
argument_list|(
name|rootScope
argument_list|,
name|definition
argument_list|,
name|schema
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|definitionVisitor
operator|.
name|visit
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|node
operator|=
name|node
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|manager
operator|.
name|attachSchemaToWSDL
argument_list|(
name|definition
argument_list|,
name|schema
argument_list|,
name|isSchemaGenerated
argument_list|()
argument_list|)
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
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setSchemaGenerated
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|schemaGenerated
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSchemaGenerated
parameter_list|()
block|{
return|return
name|schemaGenerated
return|;
block|}
specifier|public
name|void
name|updateSchemaNamespace
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|schema
operator|.
name|setTargetNamespace
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setQualified
parameter_list|(
name|boolean
name|qualified
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|qualified
condition|)
block|{
name|XmlSchemaForm
name|form
init|=
name|XmlSchemaForm
operator|.
name|QUALIFIED
decl_stmt|;
name|schema
operator|.
name|setAttributeFormDefault
argument_list|(
name|form
argument_list|)
expr_stmt|;
name|schema
operator|.
name|setElementFormDefault
argument_list|(
name|form
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setSupportPolymorphicFactories
parameter_list|(
name|boolean
name|support
parameter_list|)
throws|throws
name|Exception
block|{
name|supportPolymorphicFactories
operator|=
name|support
expr_stmt|;
block|}
specifier|public
name|boolean
name|getSupportPolymorphicFactories
parameter_list|()
block|{
return|return
name|supportPolymorphicFactories
return|;
block|}
specifier|public
name|void
name|setIdlFile
parameter_list|(
name|String
name|idl
parameter_list|)
block|{
name|idlFile
operator|=
name|idl
expr_stmt|;
block|}
specifier|public
name|String
name|getIdlFile
parameter_list|()
block|{
return|return
name|idlFile
return|;
block|}
specifier|public
name|Map
argument_list|<
name|Scope
argument_list|,
name|List
argument_list|<
name|Scope
argument_list|>
argument_list|>
name|getInheritedScopeMap
parameter_list|()
block|{
return|return
name|inheritScopeMap
return|;
block|}
specifier|public
name|void
name|setOutputDir
parameter_list|(
name|String
name|outDir
parameter_list|)
block|{
name|outputDir
operator|=
name|outDir
expr_stmt|;
block|}
specifier|public
name|String
name|getOutputDir
parameter_list|()
block|{
return|return
name|outputDir
return|;
block|}
specifier|public
name|Definition
name|getDefinition
parameter_list|()
block|{
return|return
name|definition
return|;
block|}
specifier|public
name|WSDLSchemaManager
name|getManager
parameter_list|()
block|{
return|return
name|manager
return|;
block|}
specifier|public
name|XmlSchema
name|getSchema
parameter_list|()
block|{
return|return
name|schema
return|;
block|}
specifier|public
name|XmlSchemaCollection
name|getSchemas
parameter_list|()
block|{
return|return
name|schemas
return|;
block|}
specifier|public
name|ScopeNameCollection
name|getScopedNames
parameter_list|()
block|{
return|return
name|scopedNames
return|;
block|}
specifier|public
name|ScopeNameCollection
name|getRecursionList
parameter_list|()
block|{
return|return
name|recursionList
return|;
block|}
specifier|public
name|DeferredActionCollection
name|getDeferredActions
parameter_list|()
block|{
return|return
name|deferredActions
return|;
block|}
specifier|public
name|TypeMappingType
name|getTypeMap
parameter_list|()
block|{
return|return
name|typeMap
return|;
block|}
specifier|public
name|XmlSchemaType
name|getSequenceOctetType
parameter_list|()
block|{
return|return
name|sequenceOctetType
return|;
block|}
specifier|public
name|void
name|setImportSchema
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
name|importSchemaFilename
operator|=
name|filename
expr_stmt|;
block|}
specifier|public
name|String
name|getImportSchemaFilename
parameter_list|()
block|{
return|return
name|importSchemaFilename
return|;
block|}
specifier|public
name|void
name|setSequenceOctetType
parameter_list|(
name|String
name|type
parameter_list|)
throws|throws
name|Exception
block|{
name|XmlSchemaType
name|stype
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|equals
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_SEQUENCE_OCTET_TYPE_BASE64BINARY
argument_list|)
condition|)
block|{
name|stype
operator|=
name|schemas
operator|.
name|getTypeByQName
argument_list|(
name|Constants
operator|.
name|XSD_BASE64
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|equals
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_SEQUENCE_OCTET_TYPE_HEXBINARY
argument_list|)
condition|)
block|{
name|stype
operator|=
name|schemas
operator|.
name|getTypeByQName
argument_list|(
name|Constants
operator|.
name|XSD_HEXBIN
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
literal|"WSDLASTVisitor: Invalid XmlSchemaType specified "
operator|+
literal|"for idl:sequence<octet> mapping."
argument_list|)
throw|;
block|}
name|sequenceOctetType
operator|=
name|stype
expr_stmt|;
block|}
specifier|public
name|boolean
name|getBoundedStringOverride
parameter_list|()
block|{
return|return
name|boundedStringOverride
return|;
block|}
specifier|public
name|void
name|setBoundedStringOverride
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|boundedStringOverride
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|Binding
index|[]
name|getCorbaBindings
parameter_list|()
block|{
name|List
argument_list|<
name|Binding
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|Binding
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|QName
argument_list|,
name|Binding
argument_list|>
name|bindings
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|definition
operator|.
name|getBindings
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Binding
name|binding
range|:
name|bindings
operator|.
name|values
argument_list|()
control|)
block|{
name|List
argument_list|<
name|ExtensibilityElement
argument_list|>
name|extElements
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|binding
operator|.
name|getExtensibilityElements
argument_list|()
argument_list|)
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
name|extElements
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|ExtensibilityElement
name|el
init|=
name|extElements
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|el
operator|.
name|getElementType
argument_list|()
operator|.
name|equals
argument_list|(
name|CorbaConstants
operator|.
name|NE_CORBA_BINDING
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|binding
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
return|return
name|result
operator|.
name|toArray
argument_list|(
operator|new
name|Binding
index|[
name|result
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|writeDefinition
parameter_list|(
name|Writer
name|writer
parameter_list|)
throws|throws
name|Exception
block|{
name|writeDefinition
argument_list|(
name|definition
argument_list|,
name|writer
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|writeDefinition
parameter_list|(
name|Definition
name|def
parameter_list|,
name|Writer
name|writer
parameter_list|)
throws|throws
name|Exception
block|{
name|WSDLUtils
operator|.
name|writeWSDL
argument_list|(
name|def
argument_list|,
name|writer
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|writeSchemaDefinition
parameter_list|(
name|Definition
name|definit
parameter_list|,
name|Writer
name|writer
parameter_list|)
throws|throws
name|Exception
block|{
name|Definition
name|def
init|=
name|manager
operator|.
name|createWSDLDefinition
argument_list|(
name|targetNamespace
operator|+
literal|"-types"
argument_list|)
decl_stmt|;
name|def
operator|.
name|createTypes
argument_list|()
expr_stmt|;
name|def
operator|.
name|setTypes
argument_list|(
name|definit
operator|.
name|getTypes
argument_list|()
argument_list|)
expr_stmt|;
name|WSDLUtils
operator|.
name|writeSchema
argument_list|(
name|def
argument_list|,
name|writer
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|writeSchema
parameter_list|(
name|XmlSchema
name|schemaRef
parameter_list|,
name|Writer
name|writer
parameter_list|)
throws|throws
name|Exception
block|{
comment|//REVISIT, it should be easier to  write out the schema directly, but currently,
comment|//the XmlSchemaSerializer throws a NullPointerException, when setting up namespaces!!!
comment|//schemaRef.write(writer);
name|Definition
name|defn
init|=
name|manager
operator|.
name|createWSDLDefinition
argument_list|(
name|schemaRef
operator|.
name|getTargetNamespace
argument_list|()
argument_list|)
decl_stmt|;
name|manager
operator|.
name|attachSchemaToWSDL
argument_list|(
name|defn
argument_list|,
name|schemaRef
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|writeSchemaDefinition
argument_list|(
name|defn
argument_list|,
name|writer
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// REVISIT - When CXF corrects the wsdlValidator - will switch back on the
comment|// validation of the generated wsdls.
specifier|public
name|boolean
name|writeDefinitions
parameter_list|(
name|Writer
name|writer
parameter_list|,
name|Writer
name|schemaWriter
parameter_list|,
name|Writer
name|logicalWriter
parameter_list|,
name|Writer
name|physicalWriter
parameter_list|,
name|String
name|schemaFilename
parameter_list|,
name|String
name|logicalFile
parameter_list|,
name|String
name|physicalFile
parameter_list|)
throws|throws
name|Exception
block|{
name|Definition
name|logicalDef
init|=
name|getLogicalDefinition
argument_list|(
name|schemaFilename
argument_list|,
name|schemaWriter
argument_list|)
decl_stmt|;
name|Definition
name|physicalDef
init|=
literal|null
decl_stmt|;
comment|// schema only
if|if
condition|(
operator|(
name|schemaFilename
operator|!=
literal|null
operator|||
name|importSchemaFilename
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|logicalFile
operator|==
literal|null
operator|&&
name|physicalFile
operator|==
literal|null
operator|)
condition|)
block|{
name|physicalDef
operator|=
name|getPhysicalDefinition
argument_list|(
name|logicalDef
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|physicalDef
operator|=
name|getPhysicalDefinition
argument_list|(
name|logicalDef
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|// write out logical file -L and physical in default
if|if
condition|(
name|logicalFile
operator|!=
literal|null
operator|&&
name|physicalFile
operator|==
literal|null
condition|)
block|{
name|writeDefinition
argument_list|(
name|logicalDef
argument_list|,
name|logicalWriter
argument_list|)
expr_stmt|;
name|manager
operator|.
name|addWSDLDefinitionImport
argument_list|(
name|physicalDef
argument_list|,
name|logicalDef
argument_list|,
literal|"logicaltns"
argument_list|,
name|logicalFile
argument_list|)
expr_stmt|;
name|writeDefinition
argument_list|(
name|physicalDef
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|logicalFile
operator|!=
literal|null
operator|&&
name|physicalFile
operator|!=
literal|null
condition|)
block|{
comment|// write both logical -L and physical files -P
name|writeDefinition
argument_list|(
name|logicalDef
argument_list|,
name|logicalWriter
argument_list|)
expr_stmt|;
name|manager
operator|.
name|addWSDLDefinitionImport
argument_list|(
name|physicalDef
argument_list|,
name|logicalDef
argument_list|,
literal|"logicaltns"
argument_list|,
name|logicalFile
argument_list|)
expr_stmt|;
name|writeDefinition
argument_list|(
name|physicalDef
argument_list|,
name|physicalWriter
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|logicalFile
operator|==
literal|null
operator|&&
name|physicalFile
operator|!=
literal|null
condition|)
block|{
comment|// write pyhsical file -P and logical in default
name|writeDefinition
argument_list|(
name|logicalDef
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|manager
operator|.
name|addWSDLDefinitionImport
argument_list|(
name|physicalDef
argument_list|,
name|logicalDef
argument_list|,
literal|"logicaltns"
argument_list|,
name|getIdlFile
argument_list|()
argument_list|)
expr_stmt|;
name|writeDefinition
argument_list|(
name|physicalDef
argument_list|,
name|physicalWriter
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|(
name|logicalFile
operator|==
literal|null
operator|&&
name|physicalFile
operator|==
literal|null
operator|)
operator|&&
operator|(
name|schemaFilename
operator|!=
literal|null
operator|||
name|importSchemaFilename
operator|!=
literal|null
operator|)
condition|)
block|{
comment|// write out the schema file -T and default of logical
comment|// and physical together.
name|writeDefinition
argument_list|(
name|physicalDef
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|logicalFile
operator|==
literal|null
operator|&&
name|physicalFile
operator|==
literal|null
operator|&&
name|schemaFilename
operator|==
literal|null
condition|)
block|{
name|writeDefinition
argument_list|(
name|definition
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
comment|// Gets the logical definition for a file - an import will be added for the
comment|// schema types if -T is used and a separate schema file generated.
comment|// if -n is used an import will be added for the schema types and no types generated.
specifier|private
name|Definition
name|getLogicalDefinition
parameter_list|(
name|String
name|schemaFilename
parameter_list|,
name|Writer
name|schemaWriter
parameter_list|)
throws|throws
name|WSDLException
throws|,
name|JAXBException
throws|,
name|Exception
block|{
name|Definition
name|def
init|=
name|manager
operator|.
name|createWSDLDefinition
argument_list|(
name|targetNamespace
argument_list|)
decl_stmt|;
comment|// checks for -T option.
if|if
condition|(
name|schemaFilename
operator|!=
literal|null
condition|)
block|{
name|writeSchemaDefinition
argument_list|(
name|definition
argument_list|,
name|schemaWriter
argument_list|)
expr_stmt|;
name|manager
operator|.
name|addWSDLSchemaImport
argument_list|(
name|def
argument_list|,
name|schema
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
name|schemaFilename
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// checks for -n option
if|if
condition|(
name|importSchemaFilename
operator|==
literal|null
condition|)
block|{
name|Types
name|types
init|=
name|definition
operator|.
name|getTypes
argument_list|()
decl_stmt|;
name|def
operator|.
name|setTypes
argument_list|(
name|types
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|manager
operator|.
name|addWSDLSchemaImport
argument_list|(
name|def
argument_list|,
name|schema
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
name|importSchemaFilename
argument_list|)
expr_stmt|;
block|}
block|}
name|Collection
argument_list|<
name|PortType
argument_list|>
name|portTypes
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|definition
operator|.
name|getAllPortTypes
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|PortType
name|port
range|:
name|portTypes
control|)
block|{
name|def
operator|.
name|addPortType
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
name|Collection
argument_list|<
name|Message
argument_list|>
name|messages
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|definition
operator|.
name|getMessages
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Message
name|msg
range|:
name|messages
control|)
block|{
name|def
operator|.
name|addMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
name|Collection
argument_list|<
name|String
argument_list|>
name|namespaces
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|definition
operator|.
name|getNamespaces
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|namespace
range|:
name|namespaces
control|)
block|{
name|String
name|prefix
init|=
name|definition
operator|.
name|getPrefix
argument_list|(
name|namespace
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|"corba"
operator|.
name|equals
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|def
operator|.
name|addNamespace
argument_list|(
name|prefix
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|def
operator|.
name|removeNamespace
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
block|}
block|}
name|Collection
argument_list|<
name|Import
argument_list|>
name|imports
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|definition
operator|.
name|getImports
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Import
name|importType
range|:
name|imports
control|)
block|{
name|def
operator|.
name|addImport
argument_list|(
name|importType
argument_list|)
expr_stmt|;
block|}
name|def
operator|.
name|setDocumentationElement
argument_list|(
name|definition
operator|.
name|getDocumentationElement
argument_list|()
argument_list|)
expr_stmt|;
name|def
operator|.
name|setDocumentBaseURI
argument_list|(
name|definition
operator|.
name|getDocumentBaseURI
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|def
return|;
block|}
comment|// Write the physical definitions to a file.
specifier|private
name|Definition
name|getPhysicalDefinition
parameter_list|(
name|Definition
name|logicalDef
parameter_list|,
name|boolean
name|schemaOnly
parameter_list|)
throws|throws
name|WSDLException
throws|,
name|JAXBException
block|{
name|Definition
name|def
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|schemaOnly
condition|)
block|{
name|def
operator|=
name|logicalDef
expr_stmt|;
block|}
else|else
block|{
name|def
operator|=
name|manager
operator|.
name|createWSDLDefinition
argument_list|(
name|targetNamespace
argument_list|)
expr_stmt|;
block|}
name|Collection
argument_list|<
name|String
argument_list|>
name|namespaces
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|definition
operator|.
name|getNamespaces
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|namespace
range|:
name|namespaces
control|)
block|{
name|String
name|prefix
init|=
name|definition
operator|.
name|getPrefix
argument_list|(
name|namespace
argument_list|)
decl_stmt|;
name|def
operator|.
name|addNamespace
argument_list|(
name|prefix
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
block|}
name|Collection
argument_list|<
name|Binding
argument_list|>
name|bindings
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|definition
operator|.
name|getAllBindings
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Binding
name|binding
range|:
name|bindings
control|)
block|{
name|def
operator|.
name|addBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
block|}
name|Collection
argument_list|<
name|Service
argument_list|>
name|services
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|definition
operator|.
name|getAllServices
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Service
name|service
range|:
name|services
control|)
block|{
name|def
operator|.
name|addService
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
name|Collection
argument_list|<
name|ExtensibilityElement
argument_list|>
name|extns
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|definition
operator|.
name|getExtensibilityElements
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ExtensibilityElement
name|ext
range|:
name|extns
control|)
block|{
name|def
operator|.
name|addExtensibilityElement
argument_list|(
name|ext
argument_list|)
expr_stmt|;
block|}
name|def
operator|.
name|setExtensionRegistry
argument_list|(
name|definition
operator|.
name|getExtensionRegistry
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|def
return|;
block|}
specifier|public
name|boolean
name|getDeclaredWSAImport
parameter_list|()
block|{
return|return
name|declaredWSAImport
return|;
block|}
specifier|public
name|void
name|setDeclaredWSAImport
parameter_list|(
name|boolean
name|declaredImport
parameter_list|)
block|{
name|declaredWSAImport
operator|=
name|declaredImport
expr_stmt|;
block|}
specifier|public
name|void
name|setModuleToNSMapping
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
block|{
name|moduleToNSMapper
operator|.
name|setDefaultMapping
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|moduleToNSMapper
operator|.
name|setUserMapping
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ModuleToNSMapper
name|getModuleToNSMapper
parameter_list|()
block|{
return|return
name|moduleToNSMapper
return|;
block|}
specifier|public
name|void
name|setExcludedModules
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|modules
parameter_list|)
block|{
name|moduleToNSMapper
operator|.
name|setExcludedModuleMap
argument_list|(
name|modules
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setPragmaPrefix
parameter_list|(
name|String
name|pragmaPrefix
parameter_list|)
block|{
name|this
operator|.
name|pragmaPrefix
operator|=
name|pragmaPrefix
expr_stmt|;
block|}
specifier|public
name|String
name|getPragmaPrefix
parameter_list|()
block|{
return|return
name|pragmaPrefix
return|;
block|}
block|}
end_class

end_unit

