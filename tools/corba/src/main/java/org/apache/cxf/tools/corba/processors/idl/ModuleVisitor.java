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
name|ModuleVisitor
extends|extends
name|VisitorBase
block|{
specifier|public
name|ModuleVisitor
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
block|}
specifier|public
name|void
name|visit
parameter_list|(
name|AST
name|node
parameter_list|)
block|{
comment|//<module> ::= "module"<identifier> "{"<definition>+ "}"
name|AST
name|identifierNode
init|=
name|node
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
name|AST
name|definitionNode
init|=
name|identifierNode
operator|.
name|getNextSibling
argument_list|()
decl_stmt|;
while|while
condition|(
name|definitionNode
operator|!=
literal|null
condition|)
block|{
name|Scope
name|moduleScope
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
if|if
condition|(
operator|!
name|mapper
operator|.
name|containsExcludedModule
argument_list|(
name|moduleScope
operator|.
name|toString
argument_list|(
name|ToolCorbaConstants
operator|.
name|MODULE_SEPARATOR
argument_list|)
argument_list|)
condition|)
block|{
name|DefinitionVisitor
name|definitionVisitor
init|=
operator|new
name|DefinitionVisitor
argument_list|(
name|moduleScope
argument_list|,
name|definition
argument_list|,
name|schema
argument_list|,
name|wsdlVisitor
argument_list|)
decl_stmt|;
name|definitionVisitor
operator|.
name|visit
argument_list|(
name|definitionNode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//REVISIT, need to import excluded references.
block|}
name|definitionNode
operator|=
name|definitionNode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

