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
name|Fixed
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
name|XmlSchemaFractionDigitsFacet
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
name|XmlSchemaTotalDigitsFacet
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
name|FixedVisitor
extends|extends
name|VisitorBase
block|{
specifier|private
name|AST
name|identifierNode
decl_stmt|;
specifier|public
name|FixedVisitor
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
parameter_list|,
name|AST
name|identifierNodeRef
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
name|identifierNode
operator|=
name|identifierNodeRef
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
return|return
name|node
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_fixed
return|;
block|}
specifier|public
name|void
name|visit
parameter_list|(
name|AST
name|fixedNode
parameter_list|)
block|{
comment|//      "typedef"<type_declarator>
comment|//<type_declarator> ::=<type_spec><declarators>
comment|//<type_spec> ::=<simple_type_spec>
comment|//                    |<constr_type_spec>
comment|//<simple_type_spec> ::=<base_type_spec>
comment|//                           |<template_type_spec>
comment|//                           |<scoped_name>
comment|//<base_type_spec> ::= ... omitted (integer, char, octect, etc)
comment|//<template_type_spec> ::=<sequence_type>
comment|//                             |<string_type>
comment|//                             |<wstring_type>
comment|//                             |<fixed_pt_type>
comment|//<constr_type_spec> ::=<struct_type>
comment|//                           |<union_type>
comment|//                           |<enum_type>
comment|//<declarators> ::=<declarator> {","<declarator>}*
comment|//<declarator> ::=<simple_declarator>
comment|//                     |<complex_declarator>
comment|//<simple_declarator> ::=<identifier>
comment|//<complex_declarator> ::=<array_declarator>
comment|//<array_declarator> ::=<identifier><fixed_array_size>+
comment|//<fixed_array_size> ::= "["<positive_int_const> "]"
name|AST
name|digitsNode
init|=
name|fixedNode
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
name|AST
name|scaleNode
init|=
name|digitsNode
operator|.
name|getNextSibling
argument_list|()
decl_stmt|;
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
comment|// validate digits and scale
name|Long
name|digits
init|=
name|Long
operator|.
name|valueOf
argument_list|(
name|digitsNode
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|Long
name|scale
init|=
name|Long
operator|.
name|valueOf
argument_list|(
name|scaleNode
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|digits
argument_list|<
literal|1
operator|||
name|digits
argument_list|>
literal|31
condition|)
block|{
comment|//throw IllegalIDLException();
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Digits cannot be greater than 31"
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|scale
operator|.
name|compareTo
argument_list|(
name|digits
argument_list|)
operator|>
literal|0
condition|)
block|{
comment|//throw IllegalIDLException();
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Scale cannot be greater than digits"
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// xmlschema:fixed
name|XmlSchemaSimpleType
name|fixedSimpleType
init|=
operator|new
name|XmlSchemaSimpleType
argument_list|(
name|schema
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|XmlSchemaSimpleTypeRestriction
name|fixedRestriction
init|=
operator|new
name|XmlSchemaSimpleTypeRestriction
argument_list|()
decl_stmt|;
name|fixedRestriction
operator|.
name|setBaseTypeName
argument_list|(
name|Constants
operator|.
name|XSD_DECIMAL
argument_list|)
expr_stmt|;
name|XmlSchemaTotalDigitsFacet
name|fixedTotalDigits
init|=
operator|new
name|XmlSchemaTotalDigitsFacet
argument_list|()
decl_stmt|;
name|fixedTotalDigits
operator|.
name|setValue
argument_list|(
name|digitsNode
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|XmlSchemaFractionDigitsFacet
name|fixedFractionDigits
init|=
operator|new
name|XmlSchemaFractionDigitsFacet
argument_list|()
decl_stmt|;
name|fixedFractionDigits
operator|.
name|setValue
argument_list|(
name|scaleNode
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|fixedFractionDigits
operator|.
name|setFixed
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|fixedRestriction
operator|.
name|getFacets
argument_list|()
operator|.
name|add
argument_list|(
name|fixedTotalDigits
argument_list|)
expr_stmt|;
name|fixedRestriction
operator|.
name|getFacets
argument_list|()
operator|.
name|add
argument_list|(
name|fixedFractionDigits
argument_list|)
expr_stmt|;
name|fixedSimpleType
operator|.
name|setName
argument_list|(
name|mapper
operator|.
name|mapToQName
argument_list|(
name|scopedName
argument_list|)
argument_list|)
expr_stmt|;
name|fixedSimpleType
operator|.
name|setContent
argument_list|(
name|fixedRestriction
argument_list|)
expr_stmt|;
comment|// add xmlschema:fixed
name|setSchemaType
argument_list|(
name|fixedSimpleType
argument_list|)
expr_stmt|;
comment|// corba:fixed
name|Fixed
name|corbaFixed
init|=
operator|new
name|Fixed
argument_list|()
decl_stmt|;
name|corbaFixed
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
name|scopedName
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|corbaFixed
operator|.
name|setDigits
argument_list|(
name|digits
argument_list|)
expr_stmt|;
name|corbaFixed
operator|.
name|setScale
argument_list|(
name|scale
argument_list|)
expr_stmt|;
name|corbaFixed
operator|.
name|setRepositoryID
argument_list|(
name|scopedName
operator|.
name|toIDLRepositoryID
argument_list|()
argument_list|)
expr_stmt|;
comment|//corbaFixed.setType(Constants.XSD_DECIMAL);
name|corbaFixed
operator|.
name|setType
argument_list|(
name|fixedSimpleType
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
comment|// add corba:fixed
name|setCorbaType
argument_list|(
name|corbaFixed
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

