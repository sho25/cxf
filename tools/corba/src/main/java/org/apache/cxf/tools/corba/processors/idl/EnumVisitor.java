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
name|Enum
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
name|Enumerator
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
name|XmlSchemaEnumerationFacet
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
name|EnumVisitor
extends|extends
name|VisitorBase
block|{
specifier|public
name|EnumVisitor
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
name|LITERAL_enum
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
name|enumNode
parameter_list|)
block|{
comment|//<enum_type> ::= "enum"<identifier> "{"<enumerator> {","<enumerator>}* "}"
comment|//<enumerator> ::=<identifier>
name|AST
name|enumNameNode
init|=
name|enumNode
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
name|Scope
name|enumNameScope
init|=
operator|new
name|Scope
argument_list|(
name|getScope
argument_list|()
argument_list|,
name|enumNameNode
argument_list|)
decl_stmt|;
comment|// xmlschema:enum
name|XmlSchemaSimpleType
name|enumSchemaSimpleType
init|=
operator|new
name|XmlSchemaSimpleType
argument_list|(
name|schema
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|enumSchemaSimpleType
operator|.
name|setName
argument_list|(
name|mapper
operator|.
name|mapToQName
argument_list|(
name|enumNameScope
argument_list|)
argument_list|)
expr_stmt|;
name|XmlSchemaSimpleTypeRestriction
name|enumSchemaSimpleTypeRestriction
init|=
operator|new
name|XmlSchemaSimpleTypeRestriction
argument_list|()
decl_stmt|;
name|enumSchemaSimpleTypeRestriction
operator|.
name|setBaseTypeName
argument_list|(
name|Constants
operator|.
name|XSD_STRING
argument_list|)
expr_stmt|;
comment|//XmlSchemaSimpleTypeContent xmlSchemaSimpleTypeContent = enumSchemaSimpleTypeRestriction;
name|enumSchemaSimpleType
operator|.
name|setContent
argument_list|(
name|enumSchemaSimpleTypeRestriction
argument_list|)
expr_stmt|;
comment|// corba:enum
name|Enum
name|corbaEnum
init|=
operator|new
name|Enum
argument_list|()
decl_stmt|;
name|corbaEnum
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
name|enumNameScope
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|corbaEnum
operator|.
name|setRepositoryID
argument_list|(
name|enumNameScope
operator|.
name|toIDLRepositoryID
argument_list|()
argument_list|)
expr_stmt|;
name|corbaEnum
operator|.
name|setType
argument_list|(
name|enumSchemaSimpleType
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
name|AST
name|node
init|=
name|enumNameNode
operator|.
name|getNextSibling
argument_list|()
decl_stmt|;
while|while
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
comment|// xmlschema:enumeration
name|XmlSchemaEnumerationFacet
name|enumeration
init|=
operator|new
name|XmlSchemaEnumerationFacet
argument_list|()
decl_stmt|;
name|enumeration
operator|.
name|setValue
argument_list|(
name|node
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|enumSchemaSimpleTypeRestriction
operator|.
name|getFacets
argument_list|()
operator|.
name|add
argument_list|(
name|enumeration
argument_list|)
expr_stmt|;
comment|// corba:enumerator
name|Enumerator
name|enumerator
init|=
operator|new
name|Enumerator
argument_list|()
decl_stmt|;
name|enumerator
operator|.
name|setValue
argument_list|(
name|node
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|corbaEnum
operator|.
name|getEnumerator
argument_list|()
operator|.
name|add
argument_list|(
name|enumerator
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
comment|// add corbaType
name|typeMap
operator|.
name|getStructOrExceptionOrUnion
argument_list|()
operator|.
name|add
argument_list|(
name|corbaEnum
argument_list|)
expr_stmt|;
comment|// REVISIT: are there assignments needed?
name|setSchemaType
argument_list|(
name|enumSchemaSimpleType
argument_list|)
expr_stmt|;
name|setCorbaType
argument_list|(
name|corbaEnum
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

