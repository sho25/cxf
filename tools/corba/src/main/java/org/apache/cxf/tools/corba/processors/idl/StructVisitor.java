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
name|Definition
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
name|CorbaType
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
name|MemberType
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
name|Struct
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
name|ReferenceConstants
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
name|XmlSchemaComplexType
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
name|XmlSchemaElement
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
name|XmlSchemaSequence
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
name|StructVisitor
extends|extends
name|VisitorBase
block|{
specifier|public
name|StructVisitor
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
name|wsdlVisitor
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
name|wsdlVisitor
argument_list|)
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
name|LITERAL_struct
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
comment|//<struct_type> ::= "struct"<identifier> "{"<member_list> "}"
comment|//<member_list> ::=<member>+
comment|//<member> ::=<type_spec><declarators> ";"
name|AST
name|identifierNode
init|=
name|node
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
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
name|visitForwardDeclaredStruct
argument_list|(
name|identifierNode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|visitDeclaredStruct
argument_list|(
name|identifierNode
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|visitDeclaredStruct
parameter_list|(
name|AST
name|identifierNode
parameter_list|)
block|{
name|Scope
name|structScope
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
comment|// xmlschema:struct
name|XmlSchemaComplexType
name|complexType
init|=
operator|new
name|XmlSchemaComplexType
argument_list|(
name|schema
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|complexType
operator|.
name|setName
argument_list|(
name|mapper
operator|.
name|mapToQName
argument_list|(
name|structScope
argument_list|)
argument_list|)
expr_stmt|;
name|XmlSchemaSequence
name|sequence
init|=
operator|new
name|XmlSchemaSequence
argument_list|()
decl_stmt|;
name|complexType
operator|.
name|setParticle
argument_list|(
name|sequence
argument_list|)
expr_stmt|;
comment|// corba:struct
name|Struct
name|struct
init|=
operator|new
name|Struct
argument_list|()
decl_stmt|;
name|struct
operator|.
name|setQName
argument_list|(
operator|new
name|QName
argument_list|(
name|typeMap
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
name|structScope
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|struct
operator|.
name|setType
argument_list|(
name|complexType
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
name|struct
operator|.
name|setRepositoryID
argument_list|(
name|structScope
operator|.
name|toIDLRepositoryID
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|recursiveAdd
init|=
name|addRecursiveScopedName
argument_list|(
name|identifierNode
argument_list|)
decl_stmt|;
comment|// struct members
name|visitStructMembers
argument_list|(
name|identifierNode
argument_list|,
name|struct
argument_list|,
name|sequence
argument_list|,
name|structScope
argument_list|)
expr_stmt|;
if|if
condition|(
name|recursiveAdd
condition|)
block|{
name|removeRecursiveScopedName
argument_list|(
name|identifierNode
argument_list|)
expr_stmt|;
block|}
comment|// add corbaType
name|typeMap
operator|.
name|getStructOrExceptionOrUnion
argument_list|()
operator|.
name|add
argument_list|(
name|struct
argument_list|)
expr_stmt|;
comment|// REVISIT: are there assignment needed?
name|setSchemaType
argument_list|(
name|complexType
argument_list|)
expr_stmt|;
name|setCorbaType
argument_list|(
name|struct
argument_list|)
expr_stmt|;
comment|// Need to check if the struct was forward declared
name|processForwardStructActions
argument_list|(
name|structScope
argument_list|)
expr_stmt|;
comment|// Once we've finished declaring the struct, we should make sure it has been removed from
comment|// the list of scopedNames so that we inidicate that is no longer simply forward declared.
name|scopedNames
operator|.
name|remove
argument_list|(
name|structScope
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|visitStructMembers
parameter_list|(
name|AST
name|identifierNode
parameter_list|,
name|Struct
name|struct
parameter_list|,
name|XmlSchemaSequence
name|sequence
parameter_list|,
name|Scope
name|structScope
parameter_list|)
block|{
name|AST
name|memberTypeNode
init|=
name|identifierNode
operator|.
name|getNextSibling
argument_list|()
decl_stmt|;
while|while
condition|(
name|memberTypeNode
operator|!=
literal|null
condition|)
block|{
name|AST
name|memberNode
init|=
name|TypesUtils
operator|.
name|getCorbaTypeNameNode
argument_list|(
name|memberTypeNode
argument_list|)
decl_stmt|;
name|XmlSchemaType
name|schemaType
init|=
literal|null
decl_stmt|;
name|CorbaType
name|corbaType
init|=
literal|null
decl_stmt|;
name|Scope
name|fqName
init|=
literal|null
decl_stmt|;
try|try
block|{
name|TypesVisitor
name|visitor
init|=
operator|new
name|TypesVisitor
argument_list|(
name|structScope
argument_list|,
name|definition
argument_list|,
name|schema
argument_list|,
name|wsdlVisitor
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|visitor
operator|.
name|visit
argument_list|(
name|memberTypeNode
argument_list|)
expr_stmt|;
name|schemaType
operator|=
name|visitor
operator|.
name|getSchemaType
argument_list|()
expr_stmt|;
name|corbaType
operator|=
name|visitor
operator|.
name|getCorbaType
argument_list|()
expr_stmt|;
name|fqName
operator|=
name|visitor
operator|.
name|getFullyQualifiedName
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
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
comment|// Handle multiple struct member declarators
comment|//<declarators> :==<declarator> { ","<declarator> }*
comment|//
comment|// A multiple declarator must be an identifier (i.e. of type IDENT)
comment|// and cannot be a previous declared (or forward declared) type
comment|// (hence the ScopedNameVisitor.accept() call).
while|while
condition|(
name|memberNode
operator|!=
literal|null
operator|&&
name|memberNode
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|IDENT
operator|&&
operator|!
name|ScopedNameVisitor
operator|.
name|accept
argument_list|(
name|structScope
argument_list|,
name|definition
argument_list|,
name|schema
argument_list|,
name|memberNode
argument_list|,
name|wsdlVisitor
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|XmlSchemaType
name|memberSchemaType
init|=
name|schemaType
decl_stmt|;
name|CorbaType
name|memberCorbaType
init|=
name|corbaType
decl_stmt|;
comment|// needed for anonymous arrays in structs
if|if
condition|(
name|ArrayVisitor
operator|.
name|accept
argument_list|(
name|memberNode
argument_list|)
condition|)
block|{
name|Scope
name|anonScope
init|=
operator|new
name|Scope
argument_list|(
name|structScope
argument_list|,
name|TypesUtils
operator|.
name|getCorbaTypeNameNode
argument_list|(
name|memberTypeNode
argument_list|)
argument_list|)
decl_stmt|;
name|ArrayVisitor
name|arrayVisitor
init|=
operator|new
name|ArrayVisitor
argument_list|(
name|anonScope
argument_list|,
name|definition
argument_list|,
name|schema
argument_list|,
name|wsdlVisitor
argument_list|,
literal|null
argument_list|,
name|fqName
argument_list|)
decl_stmt|;
name|arrayVisitor
operator|.
name|setSchemaType
argument_list|(
name|schemaType
argument_list|)
expr_stmt|;
name|arrayVisitor
operator|.
name|setCorbaType
argument_list|(
name|corbaType
argument_list|)
expr_stmt|;
name|arrayVisitor
operator|.
name|visit
argument_list|(
name|memberNode
argument_list|)
expr_stmt|;
name|memberSchemaType
operator|=
name|arrayVisitor
operator|.
name|getSchemaType
argument_list|()
expr_stmt|;
name|memberCorbaType
operator|=
name|arrayVisitor
operator|.
name|getCorbaType
argument_list|()
expr_stmt|;
name|fqName
operator|=
name|arrayVisitor
operator|.
name|getFullyQualifiedName
argument_list|()
expr_stmt|;
block|}
name|XmlSchemaElement
name|member
init|=
name|createXmlSchemaElement
argument_list|(
name|memberNode
argument_list|,
name|memberSchemaType
argument_list|,
name|fqName
argument_list|)
decl_stmt|;
name|sequence
operator|.
name|getItems
argument_list|()
operator|.
name|add
argument_list|(
name|member
argument_list|)
expr_stmt|;
name|MemberType
name|memberType
init|=
name|createMemberType
argument_list|(
name|memberNode
argument_list|,
name|memberCorbaType
argument_list|,
name|fqName
argument_list|)
decl_stmt|;
name|struct
operator|.
name|getMember
argument_list|()
operator|.
name|add
argument_list|(
name|memberType
argument_list|)
expr_stmt|;
name|memberNode
operator|=
name|memberNode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
name|memberTypeNode
operator|=
name|memberNode
expr_stmt|;
block|}
block|}
specifier|private
name|XmlSchemaElement
name|createXmlSchemaElement
parameter_list|(
name|AST
name|memberNode
parameter_list|,
name|XmlSchemaType
name|schemaType
parameter_list|,
name|Scope
name|fqName
parameter_list|)
block|{
comment|// xmlschema:member
name|XmlSchemaElement
name|member
init|=
operator|new
name|XmlSchemaElement
argument_list|(
name|schema
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|memberName
init|=
name|memberNode
operator|.
name|toString
argument_list|()
decl_stmt|;
name|member
operator|.
name|setName
argument_list|(
name|memberName
argument_list|)
expr_stmt|;
name|member
operator|.
name|setSchemaType
argument_list|(
name|schemaType
argument_list|)
expr_stmt|;
if|if
condition|(
name|schemaType
operator|!=
literal|null
condition|)
block|{
name|member
operator|.
name|setSchemaTypeName
argument_list|(
name|schemaType
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|schemaType
operator|.
name|getQName
argument_list|()
operator|.
name|equals
argument_list|(
name|ReferenceConstants
operator|.
name|WSADDRESSING_TYPE
argument_list|)
condition|)
block|{
name|member
operator|.
name|setNillable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|wsdlVisitor
operator|.
name|getDeferredActions
argument_list|()
operator|.
name|add
argument_list|(
name|fqName
argument_list|,
operator|new
name|StructDeferredAction
argument_list|(
name|member
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|member
return|;
block|}
specifier|private
name|MemberType
name|createMemberType
parameter_list|(
name|AST
name|memberNode
parameter_list|,
name|CorbaType
name|corbaType
parameter_list|,
name|Scope
name|fqName
parameter_list|)
block|{
comment|// corba:member
name|String
name|memberName
init|=
name|memberNode
operator|.
name|toString
argument_list|()
decl_stmt|;
name|MemberType
name|memberType
init|=
operator|new
name|MemberType
argument_list|()
decl_stmt|;
name|memberType
operator|.
name|setName
argument_list|(
name|memberName
argument_list|)
expr_stmt|;
if|if
condition|(
name|corbaType
operator|!=
literal|null
condition|)
block|{
name|memberType
operator|.
name|setIdltype
argument_list|(
name|corbaType
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|wsdlVisitor
operator|.
name|getDeferredActions
argument_list|()
operator|.
name|add
argument_list|(
name|fqName
argument_list|,
operator|new
name|StructDeferredAction
argument_list|(
name|memberType
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|memberType
return|;
block|}
specifier|private
name|void
name|visitForwardDeclaredStruct
parameter_list|(
name|AST
name|identifierNode
parameter_list|)
block|{
name|String
name|structName
init|=
name|identifierNode
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Scope
name|structScope
init|=
operator|new
name|Scope
argument_list|(
name|getScope
argument_list|()
argument_list|,
name|structName
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
name|structScope
argument_list|)
operator|==
literal|null
condition|)
block|{
name|scopedNames
operator|.
name|add
argument_list|(
name|structScope
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Process any actions that were defered for a forward declared struct
specifier|private
name|void
name|processForwardStructActions
parameter_list|(
name|Scope
name|structScope
parameter_list|)
block|{
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
name|DeferredActionCollection
name|deferredActions
init|=
name|wsdlVisitor
operator|.
name|getDeferredActions
argument_list|()
decl_stmt|;
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
name|structScope
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
name|getSchemaType
argument_list|()
decl_stmt|;
name|CorbaType
name|ctype
init|=
name|getCorbaType
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|DeferredAction
argument_list|>
name|iterator
init|=
name|list
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|SchemaDeferredAction
name|action
init|=
operator|(
name|SchemaDeferredAction
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|action
operator|.
name|execute
argument_list|(
name|stype
argument_list|,
name|ctype
argument_list|)
expr_stmt|;
block|}
name|iterator
operator|=
name|list
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|boolean
name|addRecursiveScopedName
parameter_list|(
name|AST
name|identifierNode
parameter_list|)
block|{
name|String
name|structName
init|=
name|identifierNode
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Scope
name|structScope
init|=
operator|new
name|Scope
argument_list|(
name|getScope
argument_list|()
argument_list|,
name|structName
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
name|structScope
argument_list|)
operator|==
literal|null
condition|)
block|{
name|scopedNames
operator|.
name|add
argument_list|(
name|structScope
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|void
name|removeRecursiveScopedName
parameter_list|(
name|AST
name|identifierNode
parameter_list|)
block|{
name|String
name|structName
init|=
name|identifierNode
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Scope
name|structScope
init|=
operator|new
name|Scope
argument_list|(
name|getScope
argument_list|()
argument_list|,
name|structName
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
name|scopedNames
operator|.
name|remove
argument_list|(
name|structScope
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

