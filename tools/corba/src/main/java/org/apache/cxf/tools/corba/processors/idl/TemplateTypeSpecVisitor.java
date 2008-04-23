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
name|TemplateTypeSpecVisitor
extends|extends
name|VisitorBase
block|{
specifier|private
name|AST
name|identifierNode
decl_stmt|;
specifier|public
name|TemplateTypeSpecVisitor
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
name|boolean
name|result
init|=
name|SequenceVisitor
operator|.
name|accept
argument_list|(
name|node
argument_list|)
operator|||
name|StringVisitor
operator|.
name|accept
argument_list|(
name|node
argument_list|)
operator|||
name|FixedVisitor
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
comment|//<template_type_spec> ::=<sequence_type>
comment|//                        |<string_type>
comment|//                        |<wide_string_type>
comment|//                        |<fixed_pt_type>
name|Visitor
name|visitor
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|SequenceVisitor
operator|.
name|accept
argument_list|(
name|node
argument_list|)
condition|)
block|{
comment|//<sequence_type>
name|visitor
operator|=
operator|new
name|SequenceVisitor
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
name|identifierNode
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|StringVisitor
operator|.
name|accept
argument_list|(
name|node
argument_list|)
condition|)
block|{
comment|//<string_type>
comment|//<wstring_type>
name|visitor
operator|=
operator|new
name|StringVisitor
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
name|identifierNode
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|FixedVisitor
operator|.
name|accept
argument_list|(
name|node
argument_list|)
condition|)
block|{
comment|//<fixed_pt_type>
name|visitor
operator|=
operator|new
name|FixedVisitor
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
name|identifierNode
argument_list|)
expr_stmt|;
block|}
name|visitor
operator|.
name|visit
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|setSchemaType
argument_list|(
name|visitor
operator|.
name|getSchemaType
argument_list|()
argument_list|)
expr_stmt|;
name|setCorbaType
argument_list|(
name|visitor
operator|.
name|getCorbaType
argument_list|()
argument_list|)
expr_stmt|;
name|setFullyQualifiedName
argument_list|(
name|visitor
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

