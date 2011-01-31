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
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchema
import|;
end_import

begin_class
specifier|public
class|class
name|TypeDclVisitor
extends|extends
name|VisitorBase
block|{
specifier|public
name|TypeDclVisitor
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
name|boolean
name|result
init|=
name|TypedefVisitor
operator|.
name|accept
argument_list|(
name|node
argument_list|)
operator|||
name|StructVisitor
operator|.
name|accept
argument_list|(
name|node
argument_list|)
operator|||
name|UnionVisitor
operator|.
name|accept
argument_list|(
name|node
argument_list|)
operator|||
name|EnumVisitor
operator|.
name|accept
argument_list|(
name|node
argument_list|)
decl_stmt|;
return|return
name|result
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
comment|//<type_dcl> ::= "typedef"<type_declarator>
comment|//              |<struct_type>
comment|//              |<union_type>
comment|//              |<enum_type>
comment|//              | "native"<simple_declarator>
comment|//              |<constr_forward_decl>
name|Visitor
name|visitor
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|TypedefVisitor
operator|.
name|accept
argument_list|(
name|node
argument_list|)
condition|)
block|{
comment|// "typedef"<type_declarator>
name|visitor
operator|=
operator|new
name|TypedefVisitor
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
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|StructVisitor
operator|.
name|accept
argument_list|(
name|node
argument_list|)
condition|)
block|{
comment|//<struct_type>
name|visitor
operator|=
operator|new
name|StructVisitor
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
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|UnionVisitor
operator|.
name|accept
argument_list|(
name|node
argument_list|)
condition|)
block|{
comment|//<union_type>
name|visitor
operator|=
operator|new
name|UnionVisitor
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
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|EnumVisitor
operator|.
name|accept
argument_list|(
name|node
argument_list|)
condition|)
block|{
comment|//<enum_type>
name|visitor
operator|=
operator|new
name|EnumVisitor
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
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_native
condition|)
block|{
comment|// "native"<simple_declarator>
comment|//
comment|// native type not supported
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"[TypeDclVisitor: native type not supported!]"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_typeprefix
condition|)
block|{
comment|// typeprefix<scoped_name><string_literal>
comment|// typeprefix not supported
comment|// keyword since CORBA 3.0
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"[TypeDclVisitor: typeprefix not supported!]"
argument_list|)
throw|;
block|}
comment|// TODO forward declaration<constr_forward_declaration>
name|visitor
operator|.
name|visit
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

