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
name|Alias
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
name|Anonstring
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
name|Anonwstring
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
name|CorbaType
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
name|XmlSchemaMaxLengthFacet
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
name|XmlSchemaSimpleType
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
name|XmlSchemaSimpleTypeRestriction
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
class|class
name|StringVisitor
extends|extends
name|VisitorBase
block|{
specifier|private
name|AST
name|stringNode
decl_stmt|;
specifier|private
name|AST
name|boundNode
decl_stmt|;
specifier|private
name|AST
name|identifierNode
decl_stmt|;
specifier|private
name|Scope
name|stringScopedName
decl_stmt|;
specifier|public
name|StringVisitor
parameter_list|(
name|Scope
name|scope
parameter_list|,
name|Definition
name|definition
parameter_list|,
name|XmlSchema
name|schemaRef
parameter_list|,
name|WSDLASTVisitor
name|wsdlVisitor
parameter_list|,
name|AST
name|identifierNodeRef
parameter_list|)
block|{
name|super
argument_list|(
name|scope
argument_list|,
name|definition
argument_list|,
name|schemaRef
argument_list|,
name|wsdlVisitor
argument_list|)
expr_stmt|;
name|stringNode
operator|=
literal|null
expr_stmt|;
name|boundNode
operator|=
literal|null
expr_stmt|;
name|identifierNode
operator|=
name|identifierNodeRef
expr_stmt|;
name|stringScopedName
operator|=
literal|null
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
operator|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_string
operator|)
operator|||
operator|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_wstring
operator|)
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
specifier|static
name|boolean
name|isBounded
parameter_list|(
name|AST
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|.
name|getFirstChild
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
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
comment|//<string_type> ::= "string" "<"<positive_int_const> ">"
comment|//                 | "string"
comment|//<wstring_type> ::= "wstring" "<"<positive_int_const> ">"
comment|//                  | "wstring"
name|stringNode
operator|=
name|node
expr_stmt|;
name|boundNode
operator|=
name|stringNode
operator|.
name|getFirstChild
argument_list|()
expr_stmt|;
comment|//get chance to check if bound is symbol name which defined as const,
comment|//if so, replace the symbol name with defined const
if|if
condition|(
name|boundNode
operator|!=
literal|null
condition|)
block|{
name|String
name|constValue
init|=
name|TypesUtils
operator|.
name|getConstValueByName
argument_list|(
name|boundNode
argument_list|,
name|typeMap
argument_list|)
decl_stmt|;
if|if
condition|(
name|constValue
operator|!=
literal|null
condition|)
block|{
name|boundNode
operator|.
name|setText
argument_list|(
name|constValue
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|identifierNode
operator|==
literal|null
condition|)
block|{
name|stringScopedName
operator|=
name|TypesUtils
operator|.
name|generateAnonymousScopedName
argument_list|(
name|getScope
argument_list|()
argument_list|,
name|schema
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|identifierNode
operator|.
name|getFirstChild
argument_list|()
operator|==
literal|null
condition|)
block|{
name|stringScopedName
operator|=
operator|new
name|Scope
argument_list|(
name|getScope
argument_list|()
argument_list|,
name|identifierNode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// array of anonymous bounded string
name|Scope
name|anonScope
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
name|stringScopedName
operator|=
name|TypesUtils
operator|.
name|generateAnonymousScopedName
argument_list|(
name|anonScope
argument_list|,
name|schema
argument_list|)
expr_stmt|;
name|identifierNode
operator|=
literal|null
expr_stmt|;
block|}
block|}
if|if
condition|(
name|boundNode
operator|!=
literal|null
operator|&&
operator|!
name|wsdlVisitor
operator|.
name|getBoundedStringOverride
argument_list|()
condition|)
block|{
if|if
condition|(
name|identifierNode
operator|!=
literal|null
condition|)
block|{
comment|// bounded string/wstring
name|visitBoundedString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// anonymous bounded string/wstring
name|visitAnonBoundedString
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// unbounded string/wstring
name|visitUnboundedString
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|visitAnonBoundedString
parameter_list|()
block|{
comment|// xmlschema:bounded anon string
name|XmlSchemaSimpleType
name|simpleType
init|=
operator|new
name|XmlSchemaSimpleType
argument_list|(
name|schema
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|simpleType
operator|.
name|setName
argument_list|(
name|stringScopedName
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|XmlSchemaSimpleTypeRestriction
name|restriction
init|=
operator|new
name|XmlSchemaSimpleTypeRestriction
argument_list|()
decl_stmt|;
name|restriction
operator|.
name|setBaseTypeName
argument_list|(
name|Constants
operator|.
name|XSD_STRING
argument_list|)
expr_stmt|;
name|XmlSchemaMaxLengthFacet
name|maxLengthFacet
init|=
operator|new
name|XmlSchemaMaxLengthFacet
argument_list|()
decl_stmt|;
name|maxLengthFacet
operator|.
name|setValue
argument_list|(
name|boundNode
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|restriction
operator|.
name|getFacets
argument_list|()
operator|.
name|add
argument_list|(
name|maxLengthFacet
argument_list|)
expr_stmt|;
name|simpleType
operator|.
name|setContent
argument_list|(
name|restriction
argument_list|)
expr_stmt|;
name|setSchemaType
argument_list|(
name|simpleType
argument_list|)
expr_stmt|;
name|CorbaType
name|anon
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|stringNode
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_string
condition|)
block|{
comment|// corba:anonstring
name|Anonstring
name|anonstring
init|=
operator|new
name|Anonstring
argument_list|()
decl_stmt|;
name|anonstring
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
name|stringScopedName
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|anonstring
operator|.
name|setBound
argument_list|(
operator|new
name|Long
argument_list|(
name|boundNode
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|anonstring
operator|.
name|setType
argument_list|(
name|simpleType
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
name|anon
operator|=
name|anonstring
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|stringNode
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_wstring
condition|)
block|{
comment|// corba:anonwstring
name|Anonwstring
name|anonwstring
init|=
operator|new
name|Anonwstring
argument_list|()
decl_stmt|;
name|anonwstring
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
name|stringScopedName
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|anonwstring
operator|.
name|setBound
argument_list|(
operator|new
name|Long
argument_list|(
name|boundNode
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|anonwstring
operator|.
name|setType
argument_list|(
name|simpleType
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
name|anon
operator|=
name|anonwstring
expr_stmt|;
block|}
else|else
block|{
comment|// should never get here
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"StringVisitor attempted to visit an invalid node"
argument_list|)
throw|;
block|}
comment|// add corba:anonstring
name|typeMap
operator|.
name|getStructOrExceptionOrUnion
argument_list|()
operator|.
name|add
argument_list|(
name|anon
argument_list|)
expr_stmt|;
name|setCorbaType
argument_list|(
name|anon
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|visitBoundedString
parameter_list|()
block|{
comment|// xmlschema:bounded string
name|XmlSchemaSimpleType
name|simpleType
init|=
operator|new
name|XmlSchemaSimpleType
argument_list|(
name|schema
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|simpleType
operator|.
name|setName
argument_list|(
name|stringScopedName
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|XmlSchemaSimpleTypeRestriction
name|restriction
init|=
operator|new
name|XmlSchemaSimpleTypeRestriction
argument_list|()
decl_stmt|;
name|restriction
operator|.
name|setBaseTypeName
argument_list|(
name|Constants
operator|.
name|XSD_STRING
argument_list|)
expr_stmt|;
name|XmlSchemaMaxLengthFacet
name|maxLengthFacet
init|=
operator|new
name|XmlSchemaMaxLengthFacet
argument_list|()
decl_stmt|;
name|maxLengthFacet
operator|.
name|setValue
argument_list|(
name|boundNode
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|restriction
operator|.
name|getFacets
argument_list|()
operator|.
name|add
argument_list|(
name|maxLengthFacet
argument_list|)
expr_stmt|;
name|simpleType
operator|.
name|setContent
argument_list|(
name|restriction
argument_list|)
expr_stmt|;
name|setSchemaType
argument_list|(
name|simpleType
argument_list|)
expr_stmt|;
name|Scope
name|anonstringScopedName
init|=
operator|new
name|Scope
argument_list|(
name|getScope
argument_list|()
argument_list|,
literal|"_Anon1_"
operator|+
name|stringScopedName
operator|.
name|tail
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|anonstringName
init|=
name|anonstringScopedName
operator|.
name|toString
argument_list|()
decl_stmt|;
name|CorbaType
name|anon
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|stringNode
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_string
condition|)
block|{
comment|// corba:anonstring
name|Anonstring
name|anonstring
init|=
operator|new
name|Anonstring
argument_list|()
decl_stmt|;
name|anonstring
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
name|anonstringName
argument_list|)
argument_list|)
expr_stmt|;
name|anonstring
operator|.
name|setBound
argument_list|(
operator|new
name|Long
argument_list|(
name|boundNode
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|anonstring
operator|.
name|setType
argument_list|(
name|simpleType
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
name|anon
operator|=
name|anonstring
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|stringNode
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_wstring
condition|)
block|{
comment|// corba:anonwstring
name|Anonwstring
name|anonwstring
init|=
operator|new
name|Anonwstring
argument_list|()
decl_stmt|;
name|anonwstring
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
name|anonstringName
argument_list|)
argument_list|)
expr_stmt|;
name|anonwstring
operator|.
name|setBound
argument_list|(
operator|new
name|Long
argument_list|(
name|boundNode
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|anonwstring
operator|.
name|setType
argument_list|(
name|simpleType
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
name|anon
operator|=
name|anonwstring
expr_stmt|;
block|}
else|else
block|{
comment|// should never get here
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"StringVisitor attempted to visit an invalid node"
argument_list|)
throw|;
block|}
comment|// add corba:anonstring
name|typeMap
operator|.
name|getStructOrExceptionOrUnion
argument_list|()
operator|.
name|add
argument_list|(
name|anon
argument_list|)
expr_stmt|;
comment|// corba:alias
name|Alias
name|alias
init|=
operator|new
name|Alias
argument_list|()
decl_stmt|;
name|alias
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
name|stringScopedName
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|alias
operator|.
name|setBasetype
argument_list|(
name|anon
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
name|alias
operator|.
name|setType
argument_list|(
name|simpleType
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
name|alias
operator|.
name|setRepositoryID
argument_list|(
name|stringScopedName
operator|.
name|toIDLRepositoryID
argument_list|()
argument_list|)
expr_stmt|;
comment|// add corba:alias
name|setCorbaType
argument_list|(
name|alias
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|visitUnboundedString
parameter_list|()
block|{
comment|// schema type
name|setSchemaType
argument_list|(
name|schemas
operator|.
name|getTypeByQName
argument_list|(
name|Constants
operator|.
name|XSD_STRING
argument_list|)
argument_list|)
expr_stmt|;
comment|// corba type
name|CorbaType
name|corbaString
init|=
operator|new
name|CorbaType
argument_list|()
decl_stmt|;
if|if
condition|(
name|stringNode
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_string
condition|)
block|{
name|corbaString
operator|.
name|setQName
argument_list|(
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
argument_list|)
expr_stmt|;
name|corbaString
operator|.
name|setName
argument_list|(
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|stringNode
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_wstring
condition|)
block|{
name|corbaString
operator|.
name|setQName
argument_list|(
name|CorbaConstants
operator|.
name|NT_CORBA_WSTRING
argument_list|)
expr_stmt|;
name|corbaString
operator|.
name|setName
argument_list|(
name|CorbaConstants
operator|.
name|NT_CORBA_WSTRING
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// should never get here
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"StringVisitor attempted to visit an invalid node"
argument_list|)
throw|;
block|}
name|corbaString
operator|.
name|setType
argument_list|(
name|Constants
operator|.
name|XSD_STRING
argument_list|)
expr_stmt|;
name|setCorbaType
argument_list|(
name|corbaString
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

