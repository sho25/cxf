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
name|TypeMappingType
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
name|XmlSchemaType
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|VisitorBase
implements|implements
name|Visitor
block|{
specifier|protected
specifier|static
name|ScopeNameCollection
name|scopedNames
decl_stmt|;
specifier|protected
name|WSDLASTVisitor
name|wsdlVisitor
decl_stmt|;
specifier|protected
name|XmlSchemaCollection
name|schemas
decl_stmt|;
specifier|protected
name|TypeMappingType
name|typeMap
decl_stmt|;
specifier|protected
name|ModuleToNSMapper
name|mapper
decl_stmt|;
specifier|protected
name|WSDLSchemaManager
name|manager
decl_stmt|;
specifier|protected
name|DeferredActionCollection
name|deferredActions
decl_stmt|;
specifier|protected
name|XmlSchema
name|schema
decl_stmt|;
specifier|protected
name|Definition
name|definition
decl_stmt|;
specifier|private
name|XmlSchemaType
name|schemaType
decl_stmt|;
specifier|private
name|CorbaType
name|corbaType
decl_stmt|;
specifier|private
name|Scope
name|fullyQualifiedName
decl_stmt|;
specifier|private
name|Scope
name|scope
decl_stmt|;
specifier|public
name|VisitorBase
parameter_list|(
name|Scope
name|scopeRef
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
name|wsdlVisitor
operator|=
name|wsdlASTVisitor
expr_stmt|;
name|schemas
operator|=
name|wsdlVisitor
operator|.
name|getSchemas
argument_list|()
expr_stmt|;
name|scopedNames
operator|=
name|wsdlVisitor
operator|.
name|getScopedNames
argument_list|()
expr_stmt|;
name|deferredActions
operator|=
name|wsdlVisitor
operator|.
name|getDeferredActions
argument_list|()
expr_stmt|;
name|typeMap
operator|=
name|wsdlVisitor
operator|.
name|getTypeMap
argument_list|()
expr_stmt|;
name|manager
operator|=
name|wsdlVisitor
operator|.
name|getManager
argument_list|()
expr_stmt|;
name|mapper
operator|=
name|wsdlVisitor
operator|.
name|getModuleToNSMapper
argument_list|()
expr_stmt|;
name|scope
operator|=
name|scopeRef
expr_stmt|;
name|scope
operator|.
name|setPrefix
argument_list|(
name|wsdlASTVisitor
operator|.
name|getPragmaPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|fullyQualifiedName
operator|=
literal|null
expr_stmt|;
name|schemaType
operator|=
literal|null
expr_stmt|;
name|corbaType
operator|=
literal|null
expr_stmt|;
name|definition
operator|=
name|defn
expr_stmt|;
name|schema
operator|=
name|schemaRef
expr_stmt|;
block|}
specifier|public
specifier|abstract
name|void
name|visit
parameter_list|(
name|AST
name|node
parameter_list|)
function_decl|;
specifier|protected
name|void
name|setSchemaType
parameter_list|(
name|XmlSchemaType
name|type
parameter_list|)
block|{
name|schemaType
operator|=
name|type
expr_stmt|;
block|}
specifier|public
name|XmlSchemaType
name|getSchemaType
parameter_list|()
block|{
return|return
name|schemaType
return|;
block|}
specifier|protected
name|void
name|setCorbaType
parameter_list|(
name|CorbaType
name|type
parameter_list|)
block|{
name|corbaType
operator|=
name|type
expr_stmt|;
block|}
specifier|public
name|CorbaType
name|getCorbaType
parameter_list|()
block|{
return|return
name|corbaType
return|;
block|}
specifier|public
name|Scope
name|getScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
specifier|public
specifier|static
name|ScopeNameCollection
name|getScopedNames
parameter_list|()
block|{
return|return
name|scopedNames
return|;
block|}
specifier|public
name|void
name|setFullyQualifiedName
parameter_list|(
name|Scope
name|declaredName
parameter_list|)
block|{
name|fullyQualifiedName
operator|=
name|declaredName
expr_stmt|;
block|}
specifier|public
name|Scope
name|getFullyQualifiedName
parameter_list|()
block|{
return|return
name|fullyQualifiedName
return|;
block|}
specifier|public
name|WSDLASTVisitor
name|getWsdlVisitor
parameter_list|()
block|{
return|return
name|wsdlVisitor
return|;
block|}
block|}
end_class

end_unit

