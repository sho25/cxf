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
name|Iterator
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
name|BindingOperation
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
name|Fault
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Operation
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
name|ExtensionRegistry
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
name|BindingType
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
name|CorbaTypeImpl
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
name|XmlSchemaType
import|;
end_import

begin_class
specifier|public
class|class
name|PortTypeVisitor
extends|extends
name|VisitorBase
block|{
name|ExtensionRegistry
name|extReg
decl_stmt|;
name|PortType
name|portType
decl_stmt|;
name|Definition
name|rootDefinition
decl_stmt|;
specifier|public
name|PortTypeVisitor
parameter_list|(
name|Scope
name|scope
parameter_list|,
name|Definition
name|defn
parameter_list|,
name|XmlSchema
name|schemaRef
parameter_list|,
name|WSDLASTVisitor
name|wsdlASTVisitor
parameter_list|)
block|{
name|super
argument_list|(
name|scope
argument_list|,
name|defn
argument_list|,
name|schemaRef
argument_list|,
name|wsdlASTVisitor
argument_list|)
expr_stmt|;
name|extReg
operator|=
name|definition
operator|.
name|getExtensionRegistry
argument_list|()
expr_stmt|;
name|rootDefinition
operator|=
name|wsdlASTVisitor
operator|.
name|getDefinition
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
name|boolean
name|accept
parameter_list|(
name|AST
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_interface
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|visit
parameter_list|(
name|AST
name|node
parameter_list|)
block|{
comment|//<interface> ::=<interface_dcl>
comment|//               |<forward_dcl>
comment|//<interface_dcl> ::=<interface_header> "{"<interface_body> "}"
comment|//<forward_dcl> ::= ["abstract" | "local"] "interface"<identifier>
comment|//<interface_header> ::= ["abstract" | "local"] "interface"<identifier>
comment|//                        [<interface_inheritance_spec>]
comment|//<interface_body> ::=<export>*
comment|//<export> ::=<type_dcl> ";"
comment|//            |<const_dcl> ";"
comment|//            |<except_dcl> ";"
comment|//            |<attr_dcl> ";"
comment|//            |<op_dcl> ";"
comment|//<interface_inheritance_spec> ::= ":"<interface_name> { ","<interface_name> }*
comment|//<interface_name> ::=<scoped_name>
name|AST
name|identifierNode
init|=
name|node
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
if|if
condition|(
name|identifierNode
operator|.
name|getText
argument_list|()
operator|.
name|equals
argument_list|(
literal|"local"
argument_list|)
condition|)
block|{
name|identifierNode
operator|=
name|identifierNode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
comment|// Check if its a forward declaration
if|if
condition|(
name|identifierNode
operator|.
name|getFirstChild
argument_list|()
operator|==
literal|null
operator|&&
name|identifierNode
operator|.
name|getNextSibling
argument_list|()
operator|==
literal|null
condition|)
block|{
name|visitForwardDeclaredInterface
argument_list|(
name|identifierNode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|visitInterface
argument_list|(
name|identifierNode
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Visits a fully declared interface
specifier|private
name|void
name|visitInterface
parameter_list|(
name|AST
name|identifierNode
parameter_list|)
block|{
try|try
block|{
name|String
name|interfaceName
init|=
name|identifierNode
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Scope
name|interfaceScope
init|=
operator|new
name|Scope
argument_list|(
name|getScope
argument_list|()
argument_list|,
name|interfaceName
argument_list|)
decl_stmt|;
name|portType
operator|=
name|definition
operator|.
name|createPortType
argument_list|()
expr_stmt|;
name|String
name|portTypeName
init|=
name|interfaceScope
operator|.
name|toString
argument_list|()
decl_stmt|;
name|XmlSchema
name|newSchema
init|=
name|schema
decl_stmt|;
if|if
condition|(
operator|!
name|mapper
operator|.
name|isDefaultMapping
argument_list|()
condition|)
block|{
name|portTypeName
operator|=
name|interfaceScope
operator|.
name|tail
argument_list|()
expr_stmt|;
comment|//add a schema based on the interface
name|String
name|tns
init|=
name|mapper
operator|.
name|map
argument_list|(
name|interfaceScope
argument_list|)
decl_stmt|;
name|newSchema
operator|=
name|manager
operator|.
name|createXmlSchemaForDefinition
argument_list|(
name|definition
argument_list|,
name|tns
argument_list|,
name|schemas
argument_list|)
expr_stmt|;
name|definition
operator|.
name|addNamespace
argument_list|(
name|interfaceScope
operator|.
name|toString
argument_list|(
literal|"_"
argument_list|)
argument_list|,
name|tns
argument_list|)
expr_stmt|;
block|}
name|String
name|tns
init|=
name|definition
operator|.
name|getTargetNamespace
argument_list|()
decl_stmt|;
name|portType
operator|.
name|setQName
argument_list|(
operator|new
name|QName
argument_list|(
name|tns
argument_list|,
name|portTypeName
argument_list|)
argument_list|)
expr_stmt|;
name|definition
operator|.
name|addPortType
argument_list|(
name|portType
argument_list|)
expr_stmt|;
name|portType
operator|.
name|setUndefined
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Binding
name|binding
init|=
name|createBinding
argument_list|(
name|interfaceScope
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|AST
name|specNode
init|=
name|identifierNode
operator|.
name|getNextSibling
argument_list|()
decl_stmt|;
if|if
condition|(
name|specNode
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LCURLY
condition|)
block|{
name|specNode
operator|=
name|specNode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
name|AST
name|exportNode
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|specNode
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|RCURLY
condition|)
block|{
name|exportNode
operator|=
name|specNode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|specNode
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|COLON
condition|)
block|{
name|exportNode
operator|=
name|visitInterfaceInheritanceSpec
argument_list|(
name|specNode
argument_list|,
name|binding
argument_list|,
name|interfaceScope
argument_list|)
expr_stmt|;
name|exportNode
operator|=
name|exportNode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|exportNode
operator|=
name|specNode
expr_stmt|;
block|}
while|while
condition|(
name|exportNode
operator|!=
literal|null
operator|&&
name|exportNode
operator|.
name|getType
argument_list|()
operator|!=
name|IDLTokenTypes
operator|.
name|RCURLY
condition|)
block|{
if|if
condition|(
name|TypeDclVisitor
operator|.
name|accept
argument_list|(
name|exportNode
argument_list|)
condition|)
block|{
name|TypeDclVisitor
name|visitor
init|=
operator|new
name|TypeDclVisitor
argument_list|(
name|interfaceScope
argument_list|,
name|definition
argument_list|,
name|newSchema
argument_list|,
name|wsdlVisitor
argument_list|)
decl_stmt|;
name|visitor
operator|.
name|visit
argument_list|(
name|exportNode
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ConstVisitor
operator|.
name|accept
argument_list|(
name|exportNode
argument_list|)
condition|)
block|{
name|ConstVisitor
name|visitor
init|=
operator|new
name|ConstVisitor
argument_list|(
name|interfaceScope
argument_list|,
name|definition
argument_list|,
name|newSchema
argument_list|,
name|wsdlVisitor
argument_list|)
decl_stmt|;
name|visitor
operator|.
name|visit
argument_list|(
name|exportNode
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ExceptionVisitor
operator|.
name|accept
argument_list|(
name|exportNode
argument_list|)
condition|)
block|{
name|ExceptionVisitor
name|visitor
init|=
operator|new
name|ExceptionVisitor
argument_list|(
name|interfaceScope
argument_list|,
name|definition
argument_list|,
name|newSchema
argument_list|,
name|wsdlVisitor
argument_list|)
decl_stmt|;
name|visitor
operator|.
name|visit
argument_list|(
name|exportNode
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|AttributeVisitor
operator|.
name|accept
argument_list|(
name|exportNode
argument_list|)
condition|)
block|{
name|AttributeVisitor
name|attributeVisitor
init|=
operator|new
name|AttributeVisitor
argument_list|(
name|interfaceScope
argument_list|,
name|definition
argument_list|,
name|newSchema
argument_list|,
name|wsdlVisitor
argument_list|,
name|portType
argument_list|,
name|binding
argument_list|)
decl_stmt|;
name|attributeVisitor
operator|.
name|visit
argument_list|(
name|exportNode
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|OperationVisitor
operator|.
name|accept
argument_list|(
name|interfaceScope
argument_list|,
name|definition
argument_list|,
name|newSchema
argument_list|,
name|exportNode
argument_list|,
name|wsdlVisitor
argument_list|)
condition|)
block|{
name|OperationVisitor
name|visitor
init|=
operator|new
name|OperationVisitor
argument_list|(
name|interfaceScope
argument_list|,
name|definition
argument_list|,
name|newSchema
argument_list|,
name|wsdlVisitor
argument_list|,
name|portType
argument_list|,
name|binding
argument_list|)
decl_stmt|;
name|visitor
operator|.
name|visit
argument_list|(
name|exportNode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"[InterfaceVisitor] Invalid IDL: unknown element "
operator|+
name|exportNode
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|exportNode
operator|=
name|exportNode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
comment|// Once we've finished declaring the interface, we should make sure it has been removed
comment|// from the list of scopedNames so that we indicate that is no longer simply forward
comment|// declared.
name|Scope
name|scopedName
init|=
operator|new
name|Scope
argument_list|(
name|getScope
argument_list|()
argument_list|,
name|identifierNode
argument_list|)
decl_stmt|;
name|scopedNames
operator|.
name|remove
argument_list|(
name|scopedName
argument_list|)
expr_stmt|;
if|if
condition|(
name|wsdlVisitor
operator|.
name|getDeferredActions
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|handleDeferredActions
argument_list|(
name|wsdlVisitor
operator|.
name|getDeferredActions
argument_list|()
argument_list|,
name|scopedName
argument_list|,
name|identifierNode
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|mapper
operator|.
name|isDefaultMapping
argument_list|()
condition|)
block|{
name|manager
operator|.
name|deferAttachSchemaToWSDL
argument_list|(
name|definition
argument_list|,
name|newSchema
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|//manager.attachSchemaToWSDL(definition, newSchema, false);
block|}
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
specifier|private
name|void
name|handleDeferredActions
parameter_list|(
name|DeferredActionCollection
name|deferredActions
parameter_list|,
name|Scope
name|scopedName
parameter_list|,
name|AST
name|identifierNode
parameter_list|)
block|{
name|List
argument_list|<
name|DeferredAction
argument_list|>
name|list
init|=
name|deferredActions
operator|.
name|getActions
argument_list|(
name|scopedName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|list
operator|!=
literal|null
operator|)
operator|&&
operator|!
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|XmlSchemaType
name|stype
init|=
literal|null
decl_stmt|;
name|CorbaTypeImpl
name|ctype
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ObjectReferenceVisitor
operator|.
name|accept
argument_list|(
name|getScope
argument_list|()
argument_list|,
name|schema
argument_list|,
name|definition
argument_list|,
name|identifierNode
argument_list|,
name|wsdlVisitor
argument_list|)
condition|)
block|{
name|ObjectReferenceVisitor
name|visitor
init|=
operator|new
name|ObjectReferenceVisitor
argument_list|(
name|getScope
argument_list|()
argument_list|,
name|definition
argument_list|,
name|schema
argument_list|,
name|wsdlVisitor
argument_list|)
decl_stmt|;
name|visitor
operator|.
name|visit
argument_list|(
name|identifierNode
argument_list|)
expr_stmt|;
name|stype
operator|=
name|visitor
operator|.
name|getSchemaType
argument_list|()
expr_stmt|;
name|ctype
operator|=
name|visitor
operator|.
name|getCorbaType
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|DeferredAction
name|action
range|:
name|list
control|)
block|{
if|if
condition|(
name|action
operator|instanceof
name|SchemaDeferredAction
operator|&&
operator|(
name|stype
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|ctype
operator|!=
literal|null
operator|)
condition|)
block|{
name|SchemaDeferredAction
name|schemaAction
init|=
operator|(
name|SchemaDeferredAction
operator|)
name|action
decl_stmt|;
name|schemaAction
operator|.
name|execute
argument_list|(
name|stype
argument_list|,
name|ctype
argument_list|)
expr_stmt|;
block|}
block|}
name|deferredActions
operator|.
name|removeScope
argument_list|(
name|scopedName
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Binding
name|createBinding
parameter_list|(
name|String
name|scopedPortTypeName
parameter_list|)
block|{
name|StringBuilder
name|bname
init|=
operator|new
name|StringBuilder
argument_list|(
name|scopedPortTypeName
operator|+
literal|"CORBABinding"
argument_list|)
decl_stmt|;
name|QName
name|bqname
init|=
operator|new
name|QName
argument_list|(
name|rootDefinition
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
name|bname
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|queryBinding
argument_list|(
name|bqname
argument_list|)
condition|)
block|{
name|bname
operator|.
name|append
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|bqname
operator|=
operator|new
name|QName
argument_list|(
name|rootDefinition
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
name|bname
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Binding
name|binding
init|=
name|rootDefinition
operator|.
name|createBinding
argument_list|()
decl_stmt|;
name|binding
operator|.
name|setPortType
argument_list|(
name|portType
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setQName
argument_list|(
name|bqname
argument_list|)
expr_stmt|;
try|try
block|{
name|BindingType
name|bindingType
init|=
operator|(
name|BindingType
operator|)
name|extReg
operator|.
name|createExtension
argument_list|(
name|Binding
operator|.
name|class
argument_list|,
name|CorbaConstants
operator|.
name|NE_CORBA_BINDING
argument_list|)
decl_stmt|;
name|String
name|pragmaPrefix
init|=
operator|(
name|this
operator|.
name|getWsdlVisitor
argument_list|()
operator|.
name|getPragmaPrefix
argument_list|()
operator|!=
literal|null
operator|&&
name|this
operator|.
name|getWsdlVisitor
argument_list|()
operator|.
name|getPragmaPrefix
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|)
condition|?
name|this
operator|.
name|getWsdlVisitor
argument_list|()
operator|.
name|getPragmaPrefix
argument_list|()
operator|+
literal|"/"
else|:
literal|""
decl_stmt|;
name|bindingType
operator|.
name|setRepositoryID
argument_list|(
name|CorbaConstants
operator|.
name|REPO_STRING
operator|+
name|pragmaPrefix
operator|+
name|scopedPortTypeName
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
name|CorbaConstants
operator|.
name|IDL_VERSION
argument_list|)
expr_stmt|;
name|binding
operator|.
name|addExtensibilityElement
argument_list|(
name|bindingType
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSDLException
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
name|binding
operator|.
name|setUndefined
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|rootDefinition
operator|.
name|addBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
return|return
name|binding
return|;
block|}
specifier|private
name|boolean
name|queryBinding
parameter_list|(
name|QName
name|bqname
parameter_list|)
block|{
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
name|getBindings
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
if|if
condition|(
name|binding
operator|.
name|getQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
name|bqname
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|AST
name|visitInterfaceInheritanceSpec
parameter_list|(
name|AST
name|interfaceInheritanceSpecNode
parameter_list|,
name|Binding
name|binding
parameter_list|,
name|Scope
name|childScope
parameter_list|)
block|{
comment|//<interface_inheritance_spec> ::= ":"<interface_name> { ","<interface_name> }*
name|AST
name|interfaceNameNode
init|=
name|interfaceInheritanceSpecNode
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
name|BindingType
name|corbaBinding
init|=
name|findCorbaBinding
argument_list|(
name|binding
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Scope
argument_list|>
name|inheritedScopes
init|=
operator|new
name|ArrayList
argument_list|<
name|Scope
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|interfaceNameNode
operator|!=
literal|null
condition|)
block|{
comment|//check for porttypes in current& parent scopes
name|Scope
name|interfaceScope
init|=
literal|null
decl_stmt|;
name|PortType
name|intf
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ScopedNameVisitor
operator|.
name|isFullyScopedName
argument_list|(
name|interfaceNameNode
argument_list|)
condition|)
block|{
name|interfaceScope
operator|=
name|ScopedNameVisitor
operator|.
name|getFullyScopedName
argument_list|(
operator|new
name|Scope
argument_list|()
argument_list|,
name|interfaceNameNode
argument_list|)
expr_stmt|;
name|intf
operator|=
name|findPortType
argument_list|(
name|interfaceScope
argument_list|)
expr_stmt|;
block|}
name|Scope
name|currentScope
init|=
name|getScope
argument_list|()
decl_stmt|;
while|while
condition|(
name|intf
operator|==
literal|null
operator|&&
name|currentScope
operator|!=
name|currentScope
operator|.
name|getParent
argument_list|()
condition|)
block|{
if|if
condition|(
name|ScopedNameVisitor
operator|.
name|isFullyScopedName
argument_list|(
name|interfaceNameNode
argument_list|)
condition|)
block|{
name|interfaceScope
operator|=
name|ScopedNameVisitor
operator|.
name|getFullyScopedName
argument_list|(
name|currentScope
argument_list|,
name|interfaceNameNode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|interfaceScope
operator|=
operator|new
name|Scope
argument_list|(
name|currentScope
argument_list|,
name|interfaceNameNode
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|intf
operator|=
name|findPortType
argument_list|(
name|interfaceScope
argument_list|)
expr_stmt|;
name|currentScope
operator|=
name|currentScope
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|intf
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|ScopedNameVisitor
operator|.
name|isFullyScopedName
argument_list|(
name|interfaceNameNode
argument_list|)
condition|)
block|{
name|interfaceScope
operator|=
name|ScopedNameVisitor
operator|.
name|getFullyScopedName
argument_list|(
operator|new
name|Scope
argument_list|()
argument_list|,
name|interfaceNameNode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|interfaceScope
operator|=
operator|new
name|Scope
argument_list|(
operator|new
name|Scope
argument_list|()
argument_list|,
name|interfaceNameNode
argument_list|)
expr_stmt|;
block|}
name|intf
operator|=
name|findPortType
argument_list|(
name|interfaceScope
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|intf
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"[InterfaceVisitor] Unknown Interface: "
operator|+
name|interfaceNameNode
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|Scope
name|defnScope
init|=
name|interfaceScope
operator|.
name|getParent
argument_list|()
decl_stmt|;
name|Definition
name|defn
init|=
name|manager
operator|.
name|getWSDLDefinition
argument_list|(
name|mapper
operator|.
name|map
argument_list|(
name|defnScope
argument_list|)
argument_list|)
decl_stmt|;
name|inheritedScopes
operator|.
name|add
argument_list|(
name|interfaceScope
argument_list|)
expr_stmt|;
if|if
condition|(
name|defn
operator|!=
literal|null
operator|&&
operator|!
name|defn
operator|.
name|getTargetNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|definition
operator|.
name|getTargetNamespace
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|key
init|=
name|defnScope
operator|.
name|toString
argument_list|(
literal|"_"
argument_list|)
decl_stmt|;
name|String
name|fileName
init|=
name|getWsdlVisitor
argument_list|()
operator|.
name|getOutputDir
argument_list|()
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"file.separator"
argument_list|)
operator|+
name|key
decl_stmt|;
name|manager
operator|.
name|addWSDLDefinitionImport
argument_list|(
name|definition
argument_list|,
name|defn
argument_list|,
name|key
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
block|}
name|Binding
name|inheritedBinding
init|=
name|findBinding
argument_list|(
name|intf
argument_list|)
decl_stmt|;
name|BindingType
name|inheritedCorbaBinding
init|=
name|findCorbaBinding
argument_list|(
name|inheritedBinding
argument_list|)
decl_stmt|;
name|corbaBinding
operator|.
name|getBases
argument_list|()
operator|.
name|add
argument_list|(
name|inheritedCorbaBinding
operator|.
name|getRepositoryID
argument_list|()
argument_list|)
expr_stmt|;
comment|//add all the operations of the inherited port type.
for|for
control|(
name|Operation
name|op
range|:
name|CastUtils
operator|.
name|cast
argument_list|(
name|intf
operator|.
name|getOperations
argument_list|()
argument_list|,
name|Operation
operator|.
name|class
argument_list|)
control|)
block|{
comment|//check to see all the inherited namespaces are added.
name|String
name|inputNS
init|=
name|op
operator|.
name|getInput
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|.
name|getQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|manager
operator|.
name|addWSDLDefinitionNamespace
argument_list|(
name|definition
argument_list|,
name|mapper
operator|.
name|mapNSToPrefix
argument_list|(
name|inputNS
argument_list|)
argument_list|,
name|inputNS
argument_list|)
expr_stmt|;
comment|// Make sure we import the wsdl for the input namespace
if|if
condition|(
name|definition
operator|.
name|getImports
argument_list|()
operator|.
name|get
argument_list|(
name|inputNS
argument_list|)
operator|==
literal|null
operator|&&
operator|!
name|mapper
operator|.
name|isDefaultMapping
argument_list|()
operator|&&
operator|!
name|definition
operator|.
name|getTargetNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|inputNS
argument_list|)
condition|)
block|{
name|manager
operator|.
name|addWSDLDefinitionImport
argument_list|(
name|definition
argument_list|,
name|manager
operator|.
name|getWSDLDefinition
argument_list|(
name|inputNS
argument_list|)
argument_list|,
name|mapper
operator|.
name|mapNSToPrefix
argument_list|(
name|inputNS
argument_list|)
argument_list|,
name|manager
operator|.
name|getImportedWSDLDefinitionFile
argument_list|(
name|inputNS
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|op
operator|.
name|getOutput
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|outputNS
init|=
name|op
operator|.
name|getOutput
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|.
name|getQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|manager
operator|.
name|addWSDLDefinitionNamespace
argument_list|(
name|definition
argument_list|,
name|mapper
operator|.
name|mapNSToPrefix
argument_list|(
name|outputNS
argument_list|)
argument_list|,
name|outputNS
argument_list|)
expr_stmt|;
comment|// Make sure we import the wsdl for the output namespace
if|if
condition|(
name|definition
operator|.
name|getImports
argument_list|()
operator|.
name|get
argument_list|(
name|outputNS
argument_list|)
operator|==
literal|null
operator|&&
operator|!
name|mapper
operator|.
name|isDefaultMapping
argument_list|()
operator|&&
operator|!
name|definition
operator|.
name|getTargetNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|outputNS
argument_list|)
condition|)
block|{
name|manager
operator|.
name|addWSDLDefinitionImport
argument_list|(
name|definition
argument_list|,
name|manager
operator|.
name|getWSDLDefinition
argument_list|(
name|outputNS
argument_list|)
argument_list|,
name|mapper
operator|.
name|mapNSToPrefix
argument_list|(
name|outputNS
argument_list|)
argument_list|,
name|manager
operator|.
name|getImportedWSDLDefinitionFile
argument_list|(
name|outputNS
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Iterator
argument_list|<
name|Fault
argument_list|>
name|faults
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|op
operator|.
name|getFaults
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
init|;
name|faults
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|faultNS
init|=
name|faults
operator|.
name|next
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|.
name|getQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|manager
operator|.
name|addWSDLDefinitionNamespace
argument_list|(
name|definition
argument_list|,
name|mapper
operator|.
name|mapNSToPrefix
argument_list|(
name|faultNS
argument_list|)
argument_list|,
name|faultNS
argument_list|)
expr_stmt|;
comment|// Make sure we import the wsdl for the fault namespace
if|if
condition|(
name|definition
operator|.
name|getImports
argument_list|()
operator|.
name|get
argument_list|(
name|faultNS
argument_list|)
operator|==
literal|null
operator|&&
operator|!
name|mapper
operator|.
name|isDefaultMapping
argument_list|()
operator|&&
operator|!
name|definition
operator|.
name|getTargetNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|faultNS
argument_list|)
condition|)
block|{
name|manager
operator|.
name|addWSDLDefinitionImport
argument_list|(
name|definition
argument_list|,
name|manager
operator|.
name|getWSDLDefinition
argument_list|(
name|faultNS
argument_list|)
argument_list|,
name|mapper
operator|.
name|mapNSToPrefix
argument_list|(
name|faultNS
argument_list|)
argument_list|,
name|manager
operator|.
name|getImportedWSDLDefinitionFile
argument_list|(
name|faultNS
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|portType
operator|.
name|addOperation
argument_list|(
name|op
argument_list|)
expr_stmt|;
block|}
comment|//add all the binding extensions of the inherited corba binding
for|for
control|(
name|Iterator
argument_list|<
name|BindingOperation
argument_list|>
name|it
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|inheritedBinding
operator|.
name|getBindingOperations
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|binding
operator|.
name|addBindingOperation
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|interfaceNameNode
operator|=
name|interfaceNameNode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|(
operator|!
name|inheritedScopes
operator|.
name|isEmpty
argument_list|()
operator|)
operator|&&
operator|(
name|wsdlVisitor
operator|.
name|getInheritedScopeMap
argument_list|()
operator|!=
literal|null
operator|)
condition|)
block|{
name|wsdlVisitor
operator|.
name|getInheritedScopeMap
argument_list|()
operator|.
name|put
argument_list|(
name|childScope
argument_list|,
name|inheritedScopes
argument_list|)
expr_stmt|;
block|}
return|return
name|interfaceInheritanceSpecNode
operator|.
name|getNextSibling
argument_list|()
return|;
block|}
specifier|private
name|void
name|visitForwardDeclaredInterface
parameter_list|(
name|AST
name|identifierNode
parameter_list|)
block|{
name|String
name|interfaceName
init|=
name|identifierNode
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Scope
name|interfaceScope
init|=
operator|new
name|Scope
argument_list|(
name|getScope
argument_list|()
argument_list|,
name|interfaceName
argument_list|)
decl_stmt|;
name|ScopeNameCollection
name|scopedNames
init|=
name|wsdlVisitor
operator|.
name|getScopedNames
argument_list|()
decl_stmt|;
if|if
condition|(
name|scopedNames
operator|.
name|getScope
argument_list|(
name|interfaceScope
argument_list|)
operator|==
literal|null
condition|)
block|{
name|scopedNames
operator|.
name|add
argument_list|(
name|interfaceScope
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|PortType
name|findPortType
parameter_list|(
name|Scope
name|intfScope
parameter_list|)
block|{
name|String
name|tns
init|=
name|mapper
operator|.
name|map
argument_list|(
name|intfScope
operator|.
name|getParent
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|intfName
init|=
name|intfScope
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Definition
name|defn
init|=
name|definition
decl_stmt|;
if|if
condition|(
name|tns
operator|!=
literal|null
condition|)
block|{
name|defn
operator|=
name|manager
operator|.
name|getWSDLDefinition
argument_list|(
name|tns
argument_list|)
expr_stmt|;
name|intfName
operator|=
name|intfScope
operator|.
name|tail
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|defn
operator|!=
literal|null
condition|)
block|{
name|tns
operator|=
name|defn
operator|.
name|getTargetNamespace
argument_list|()
expr_stmt|;
name|QName
name|name
init|=
operator|new
name|QName
argument_list|(
name|tns
argument_list|,
name|intfName
argument_list|)
decl_stmt|;
return|return
name|defn
operator|.
name|getPortType
argument_list|(
name|name
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|Binding
name|findBinding
parameter_list|(
name|PortType
name|intf
parameter_list|)
block|{
name|Object
index|[]
name|bindings
init|=
name|rootDefinition
operator|.
name|getBindings
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|toArray
argument_list|()
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
name|bindings
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Binding
name|binding
init|=
operator|(
name|Binding
operator|)
name|bindings
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|binding
operator|.
name|getPortType
argument_list|()
operator|.
name|getQName
argument_list|()
operator|.
name|equals
argument_list|(
name|intf
operator|.
name|getQName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|binding
return|;
block|}
block|}
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"[InterfaceVisitor] Couldn't find binding for porttype "
operator|+
name|intf
operator|.
name|getQName
argument_list|()
argument_list|)
throw|;
block|}
specifier|private
name|BindingType
name|findCorbaBinding
parameter_list|(
name|Binding
name|binding
parameter_list|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|binding
operator|.
name|getExtensibilityElements
argument_list|()
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
name|list
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|list
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|instanceof
name|BindingType
condition|)
block|{
return|return
operator|(
name|BindingType
operator|)
name|list
operator|.
name|get
argument_list|(
name|i
argument_list|)
return|;
block|}
block|}
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"[InterfaceVisitor] Couldn't find Corba binding in Binding "
operator|+
name|binding
operator|.
name|getQName
argument_list|()
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

