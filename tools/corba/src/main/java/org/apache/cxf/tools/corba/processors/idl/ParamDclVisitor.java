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
name|ModeType
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
name|OperationType
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
name|ParamType
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
name|ParamDclVisitor
extends|extends
name|VisitorBase
block|{
specifier|private
name|XmlSchemaSequence
name|inWrappingSequence
decl_stmt|;
specifier|private
name|XmlSchemaSequence
name|outWrappingSequence
decl_stmt|;
specifier|private
name|OperationType
name|corbaOperation
decl_stmt|;
specifier|public
name|ParamDclVisitor
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
name|XmlSchemaSequence
name|inWrapSeq
parameter_list|,
name|XmlSchemaSequence
name|outWrapSeq
parameter_list|,
name|OperationType
name|corbaOp
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
name|inWrappingSequence
operator|=
name|inWrapSeq
expr_stmt|;
name|outWrappingSequence
operator|=
name|outWrapSeq
expr_stmt|;
name|corbaOperation
operator|=
name|corbaOp
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
literal|false
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|int
name|type
init|=
name|node
operator|.
name|getType
argument_list|()
decl_stmt|;
name|result
operator|=
name|type
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_in
operator|||
name|type
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_out
operator|||
name|type
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_inout
expr_stmt|;
block|}
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
comment|//<param_dcl> ::=<param_attribute><param_type_spec><simple_declarator>
comment|//<param_attribute> ::= "in"
comment|//                     | "out"
comment|//                     | "inout"
name|AST
name|typeNode
init|=
name|node
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
name|AST
name|nameNode
init|=
name|TypesUtils
operator|.
name|getCorbaTypeNameNode
argument_list|(
name|typeNode
argument_list|)
decl_stmt|;
name|ParamTypeSpecVisitor
name|visitor
init|=
operator|new
name|ParamTypeSpecVisitor
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
name|typeNode
argument_list|)
expr_stmt|;
name|XmlSchemaType
name|schemaType
init|=
name|visitor
operator|.
name|getSchemaType
argument_list|()
decl_stmt|;
name|CorbaTypeImpl
name|corbaType
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
switch|switch
condition|(
name|node
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|IDLTokenTypes
operator|.
name|LITERAL_in
case|:
name|addElement
argument_list|(
name|inWrappingSequence
argument_list|,
name|schemaType
argument_list|,
name|nameNode
operator|.
name|toString
argument_list|()
argument_list|,
name|fullyQualifiedName
argument_list|)
expr_stmt|;
name|addCorbaParam
argument_list|(
name|corbaType
argument_list|,
name|ModeType
operator|.
name|IN
argument_list|,
name|nameNode
operator|.
name|toString
argument_list|()
argument_list|,
name|fullyQualifiedName
argument_list|)
expr_stmt|;
break|break;
case|case
name|IDLTokenTypes
operator|.
name|LITERAL_out
case|:
name|addElement
argument_list|(
name|outWrappingSequence
argument_list|,
name|schemaType
argument_list|,
name|nameNode
operator|.
name|toString
argument_list|()
argument_list|,
name|fullyQualifiedName
argument_list|)
expr_stmt|;
name|addCorbaParam
argument_list|(
name|corbaType
argument_list|,
name|ModeType
operator|.
name|OUT
argument_list|,
name|nameNode
operator|.
name|toString
argument_list|()
argument_list|,
name|fullyQualifiedName
argument_list|)
expr_stmt|;
break|break;
case|case
name|IDLTokenTypes
operator|.
name|LITERAL_inout
case|:
name|addElement
argument_list|(
name|inWrappingSequence
argument_list|,
name|schemaType
argument_list|,
name|nameNode
operator|.
name|toString
argument_list|()
argument_list|,
name|fullyQualifiedName
argument_list|)
expr_stmt|;
name|addElement
argument_list|(
name|outWrappingSequence
argument_list|,
name|schemaType
argument_list|,
name|nameNode
operator|.
name|toString
argument_list|()
argument_list|,
name|fullyQualifiedName
argument_list|)
expr_stmt|;
name|addCorbaParam
argument_list|(
name|corbaType
argument_list|,
name|ModeType
operator|.
name|INOUT
argument_list|,
name|nameNode
operator|.
name|toString
argument_list|()
argument_list|,
name|fullyQualifiedName
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"[ParamDclVisitor: illegal IDL!]"
argument_list|)
throw|;
block|}
name|setSchemaType
argument_list|(
name|schemaType
argument_list|)
expr_stmt|;
name|setCorbaType
argument_list|(
name|corbaType
argument_list|)
expr_stmt|;
block|}
specifier|private
name|XmlSchemaElement
name|addElement
parameter_list|(
name|XmlSchemaSequence
name|schemaSequence
parameter_list|,
name|XmlSchemaType
name|schemaType
parameter_list|,
name|String
name|name
parameter_list|,
name|Scope
name|fullyQualifiedName
parameter_list|)
block|{
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
name|element
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|schemaType
operator|==
literal|null
condition|)
block|{
name|ParamDeferredAction
name|elementAction
decl_stmt|;
if|if
condition|(
name|mapper
operator|.
name|isDefaultMapping
argument_list|()
condition|)
block|{
name|elementAction
operator|=
operator|new
name|ParamDeferredAction
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|elementAction
operator|=
operator|new
name|ParamDeferredAction
argument_list|(
name|element
argument_list|,
name|fullyQualifiedName
operator|.
name|getParent
argument_list|()
argument_list|,
name|schema
argument_list|,
name|schemas
argument_list|,
name|manager
argument_list|,
name|mapper
argument_list|)
expr_stmt|;
block|}
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
comment|//ParamDeferredAction elementAction =
comment|//    new ParamDeferredAction(element);
comment|//wsdlVisitor.getDeferredActions().add(fullyQualifiedName, elementAction);
block|}
else|else
block|{
name|element
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
name|element
operator|.
name|setNillable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
name|schemaSequence
operator|.
name|getItems
argument_list|()
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
return|return
name|element
return|;
block|}
specifier|private
name|void
name|addCorbaParam
parameter_list|(
name|CorbaTypeImpl
name|corbaType
parameter_list|,
name|ModeType
name|mode
parameter_list|,
name|String
name|partName
parameter_list|,
name|Scope
name|fullyQualifiedName
parameter_list|)
block|{
name|ParamType
name|param
init|=
operator|new
name|ParamType
argument_list|()
decl_stmt|;
name|param
operator|.
name|setName
argument_list|(
name|partName
argument_list|)
expr_stmt|;
name|param
operator|.
name|setMode
argument_list|(
name|mode
argument_list|)
expr_stmt|;
if|if
condition|(
name|corbaType
operator|==
literal|null
condition|)
block|{
name|ParamDeferredAction
name|paramAction
init|=
operator|new
name|ParamDeferredAction
argument_list|(
name|param
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
name|paramAction
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|param
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
name|corbaOperation
operator|.
name|getParam
argument_list|()
operator|.
name|add
argument_list|(
name|param
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

