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
name|CaseType
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
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|Union
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
name|Unionbranch
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
name|XmlSchemaChoice
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
name|XmlSchemaType
import|;
end_import

begin_class
specifier|public
class|class
name|UnionVisitor
extends|extends
name|VisitorBase
block|{
specifier|public
name|UnionVisitor
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
name|LITERAL_union
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
name|unionNode
parameter_list|)
block|{
comment|//<union_type> ::= "union"<identifier> "switch" "("<switch_type_spec> ")"
comment|//                  "{"<switch_body> "}"
comment|//<switch_type_spec> ::=<integer_type>
comment|//                      |<char_type>
comment|//                      |<boolean_type>
comment|//                      |<enum_type>
comment|//                      |<scoped_type>
comment|//<switch_body> ::=<case>+
comment|//<case> ::=<case_label>+<element_spec> ";"
comment|//<case_label> ::= "case"<const_expr> ":"
comment|//                | "default" ":"
comment|//<element_spec> ::=<type_spec><declarator>
name|AST
name|identifierNode
init|=
name|unionNode
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
name|visitForwardDeclaredUnion
argument_list|(
name|identifierNode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|visitDeclaredUnion
argument_list|(
name|identifierNode
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|visitDeclaredUnion
parameter_list|(
name|AST
name|identifierNode
parameter_list|)
block|{
name|Scope
name|unionScope
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
name|AST
name|discriminatorNode
init|=
name|identifierNode
operator|.
name|getNextSibling
argument_list|()
decl_stmt|;
name|AST
name|caseNode
init|=
name|discriminatorNode
operator|.
name|getNextSibling
argument_list|()
decl_stmt|;
comment|// xmlschema:union
name|XmlSchemaComplexType
name|unionSchemaComplexType
init|=
operator|new
name|XmlSchemaComplexType
argument_list|(
name|schema
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|unionSchemaComplexType
operator|.
name|setName
argument_list|(
name|mapper
operator|.
name|mapToQName
argument_list|(
name|unionScope
argument_list|)
argument_list|)
expr_stmt|;
comment|// REVISIT
comment|// TEMPORARILY
comment|// using TypesVisitor to visit<const_type>
comment|// it should be visited by a SwitchTypeSpecVisitor
name|TypesVisitor
name|visitor
init|=
operator|new
name|TypesVisitor
argument_list|(
name|getScope
argument_list|()
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
name|discriminatorNode
argument_list|)
expr_stmt|;
name|CorbaTypeImpl
name|ctype
init|=
name|visitor
operator|.
name|getCorbaType
argument_list|()
decl_stmt|;
name|Scope
name|fullyQualifiedName
init|=
name|visitor
operator|.
name|getFullyQualifiedName
argument_list|()
decl_stmt|;
name|XmlSchemaChoice
name|choice
init|=
operator|new
name|XmlSchemaChoice
argument_list|()
decl_stmt|;
name|choice
operator|.
name|setMinOccurs
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|choice
operator|.
name|setMaxOccurs
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|unionSchemaComplexType
operator|.
name|setParticle
argument_list|(
name|choice
argument_list|)
expr_stmt|;
comment|// corba:union
name|Union
name|corbaUnion
init|=
operator|new
name|Union
argument_list|()
decl_stmt|;
name|corbaUnion
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
name|unionScope
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|corbaUnion
operator|.
name|setRepositoryID
argument_list|(
name|unionScope
operator|.
name|toIDLRepositoryID
argument_list|()
argument_list|)
expr_stmt|;
name|corbaUnion
operator|.
name|setType
argument_list|(
name|unionSchemaComplexType
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|ctype
operator|!=
literal|null
condition|)
block|{
name|corbaUnion
operator|.
name|setDiscriminator
argument_list|(
name|ctype
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Discriminator type is forward declared.
name|UnionDeferredAction
name|unionDiscriminatorAction
init|=
operator|new
name|UnionDeferredAction
argument_list|(
name|corbaUnion
argument_list|)
decl_stmt|;
name|wsdlVisitor
operator|.
name|getDeferredActions
argument_list|()
operator|.
name|add
argument_list|(
name|fullyQualifiedName
argument_list|,
name|unionDiscriminatorAction
argument_list|)
expr_stmt|;
block|}
name|boolean
name|recursiveAdd
init|=
name|addRecursiveScopedName
argument_list|(
name|identifierNode
argument_list|)
decl_stmt|;
name|processCaseNodes
argument_list|(
name|caseNode
argument_list|,
name|unionScope
argument_list|,
name|choice
argument_list|,
name|corbaUnion
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
name|corbaUnion
argument_list|)
expr_stmt|;
comment|// REVISIT: are these assignments needed?
name|setSchemaType
argument_list|(
name|unionSchemaComplexType
argument_list|)
expr_stmt|;
name|setCorbaType
argument_list|(
name|corbaUnion
argument_list|)
expr_stmt|;
comment|// Need to check if the union was forward declared
name|processForwardUnionActions
argument_list|(
name|unionScope
argument_list|)
expr_stmt|;
comment|// Once we've finished declaring the union, we should make sure it has been removed from
comment|// the list of scopedNames so that we indicate that is no longer simply forward declared.
name|scopedNames
operator|.
name|remove
argument_list|(
name|unionScope
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|processCaseNodes
parameter_list|(
name|AST
name|caseNode
parameter_list|,
name|Scope
name|scope
parameter_list|,
name|XmlSchemaChoice
name|choice
parameter_list|,
name|Union
name|corbaUnion
parameter_list|)
block|{
while|while
condition|(
name|caseNode
operator|!=
literal|null
condition|)
block|{
name|AST
name|typeNode
init|=
literal|null
decl_stmt|;
name|AST
name|nameNode
init|=
literal|null
decl_stmt|;
name|AST
name|labelNode
init|=
literal|null
decl_stmt|;
comment|// xmlschema:element
name|XmlSchemaElement
name|element
init|=
operator|new
name|XmlSchemaElement
argument_list|(
name|schema
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|// corba:unionbranch
name|Unionbranch
name|unionBranch
init|=
operator|new
name|Unionbranch
argument_list|()
decl_stmt|;
if|if
condition|(
name|caseNode
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_default
condition|)
block|{
comment|// default:
name|unionBranch
operator|.
name|setDefault
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|typeNode
operator|=
name|caseNode
operator|.
name|getFirstChild
argument_list|()
expr_stmt|;
name|nameNode
operator|=
name|typeNode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// case:
name|createCase
argument_list|(
name|caseNode
argument_list|,
name|unionBranch
argument_list|)
expr_stmt|;
name|labelNode
operator|=
name|caseNode
operator|.
name|getFirstChild
argument_list|()
expr_stmt|;
if|if
condition|(
name|labelNode
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_case
condition|)
block|{
name|labelNode
operator|=
name|labelNode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
name|typeNode
operator|=
name|labelNode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
name|nameNode
operator|=
name|typeNode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
name|TypesVisitor
name|visitor
init|=
operator|new
name|TypesVisitor
argument_list|(
name|scope
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
name|typeNode
argument_list|)
expr_stmt|;
name|XmlSchemaType
name|stype
init|=
name|visitor
operator|.
name|getSchemaType
argument_list|()
decl_stmt|;
name|CorbaTypeImpl
name|ctype
init|=
name|visitor
operator|.
name|getCorbaType
argument_list|()
decl_stmt|;
name|Scope
name|fullyQualifiedName
init|=
name|visitor
operator|.
name|getFullyQualifiedName
argument_list|()
decl_stmt|;
comment|// needed for anonymous arrays in unions
if|if
condition|(
name|ArrayVisitor
operator|.
name|accept
argument_list|(
name|nameNode
argument_list|)
condition|)
block|{
name|Scope
name|anonScope
init|=
operator|new
name|Scope
argument_list|(
name|scope
argument_list|,
name|TypesUtils
operator|.
name|getCorbaTypeNameNode
argument_list|(
name|nameNode
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
name|fullyQualifiedName
argument_list|)
decl_stmt|;
name|arrayVisitor
operator|.
name|setSchemaType
argument_list|(
name|stype
argument_list|)
expr_stmt|;
name|arrayVisitor
operator|.
name|setCorbaType
argument_list|(
name|ctype
argument_list|)
expr_stmt|;
name|arrayVisitor
operator|.
name|visit
argument_list|(
name|nameNode
argument_list|)
expr_stmt|;
name|stype
operator|=
name|arrayVisitor
operator|.
name|getSchemaType
argument_list|()
expr_stmt|;
name|ctype
operator|=
name|arrayVisitor
operator|.
name|getCorbaType
argument_list|()
expr_stmt|;
name|fullyQualifiedName
operator|=
name|visitor
operator|.
name|getFullyQualifiedName
argument_list|()
expr_stmt|;
block|}
comment|// xmlschema:element
name|element
operator|.
name|setName
argument_list|(
name|nameNode
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|stype
operator|!=
literal|null
condition|)
block|{
name|element
operator|.
name|setSchemaTypeName
argument_list|(
name|stype
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|stype
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
name|element
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
name|UnionDeferredAction
name|elementAction
init|=
operator|new
name|UnionDeferredAction
argument_list|(
name|element
argument_list|)
decl_stmt|;
name|wsdlVisitor
operator|.
name|getDeferredActions
argument_list|()
operator|.
name|add
argument_list|(
name|fullyQualifiedName
argument_list|,
name|elementAction
argument_list|)
expr_stmt|;
block|}
name|choice
operator|.
name|getItems
argument_list|()
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
comment|// corba:unionbranch
name|unionBranch
operator|.
name|setName
argument_list|(
name|nameNode
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|ctype
operator|!=
literal|null
condition|)
block|{
name|unionBranch
operator|.
name|setIdltype
argument_list|(
name|ctype
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// its type is forward declared.
name|UnionDeferredAction
name|unionBranchAction
init|=
operator|new
name|UnionDeferredAction
argument_list|(
name|unionBranch
argument_list|)
decl_stmt|;
name|wsdlVisitor
operator|.
name|getDeferredActions
argument_list|()
operator|.
name|add
argument_list|(
name|fullyQualifiedName
argument_list|,
name|unionBranchAction
argument_list|)
expr_stmt|;
block|}
name|corbaUnion
operator|.
name|getUnionbranch
argument_list|()
operator|.
name|add
argument_list|(
name|unionBranch
argument_list|)
expr_stmt|;
name|caseNode
operator|=
name|caseNode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|createCase
parameter_list|(
name|AST
name|caseNode
parameter_list|,
name|Unionbranch
name|unionBranch
parameter_list|)
block|{
name|AST
name|node
init|=
name|caseNode
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
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
name|LITERAL_case
condition|)
block|{
comment|// corba:case
name|CaseType
name|caseType
init|=
operator|new
name|CaseType
argument_list|()
decl_stmt|;
name|caseType
operator|.
name|setLabel
argument_list|(
name|node
operator|.
name|getNextSibling
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|unionBranch
operator|.
name|getCase
argument_list|()
operator|.
name|add
argument_list|(
name|caseType
argument_list|)
expr_stmt|;
comment|// recursive call
name|createCase
argument_list|(
name|node
argument_list|,
name|unionBranch
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// corba:case
name|CaseType
name|caseType
init|=
operator|new
name|CaseType
argument_list|()
decl_stmt|;
name|caseType
operator|.
name|setLabel
argument_list|(
name|node
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|unionBranch
operator|.
name|getCase
argument_list|()
operator|.
name|add
argument_list|(
name|caseType
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|visitForwardDeclaredUnion
parameter_list|(
name|AST
name|identifierNode
parameter_list|)
block|{
name|String
name|unionName
init|=
name|identifierNode
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Scope
name|unionScope
init|=
operator|new
name|Scope
argument_list|(
name|getScope
argument_list|()
argument_list|,
name|unionName
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
name|unionScope
argument_list|)
operator|==
literal|null
condition|)
block|{
name|scopedNames
operator|.
name|add
argument_list|(
name|unionScope
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Process any actions that were defered for a forward declared union
specifier|private
name|void
name|processForwardUnionActions
parameter_list|(
name|Scope
name|unionScope
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
name|unionScope
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
name|CorbaTypeImpl
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

