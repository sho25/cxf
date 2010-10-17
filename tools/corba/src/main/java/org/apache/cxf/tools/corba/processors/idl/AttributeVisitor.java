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
name|Binding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|BindingInput
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|BindingOperation
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|BindingOutput
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
name|wsdl
operator|.
name|Input
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Operation
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Output
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Part
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|PortType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|WSDLException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|ExtensionRegistry
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
name|ArgType
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
name|AttributeVisitor
extends|extends
name|VisitorBase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|GETTER_PREFIX
init|=
literal|"_get_"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SETTER_PREFIX
init|=
literal|"_set_"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RESULT_POSTFIX
init|=
literal|"Result"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RESPONSE_POSTFIX
init|=
literal|"Response"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PART_NAME
init|=
literal|"parameters"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PARAM_NAME
init|=
literal|"_arg"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RETURN_PARAM_NAME
init|=
literal|"return"
decl_stmt|;
specifier|private
name|ExtensionRegistry
name|extReg
decl_stmt|;
specifier|private
name|PortType
name|portType
decl_stmt|;
specifier|private
name|Binding
name|binding
decl_stmt|;
specifier|public
name|AttributeVisitor
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
name|PortType
name|wsdlPortType
parameter_list|,
name|Binding
name|wsdlBinding
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
name|extReg
operator|=
name|definition
operator|.
name|getExtensionRegistry
argument_list|()
expr_stmt|;
name|portType
operator|=
name|wsdlPortType
expr_stmt|;
name|binding
operator|=
name|wsdlBinding
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
name|LITERAL_readonly
operator|||
name|node
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_attribute
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
name|attributeNode
parameter_list|)
block|{
comment|//<attr_dcl> ::= ["readonly"] "attribute"<param_type_spec><simple_declarator>
comment|//                {","<simple_declarator>}*
name|AST
name|node
init|=
name|attributeNode
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
name|AST
name|readonlyNode
init|=
literal|null
decl_stmt|;
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
if|if
condition|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_readonly
condition|)
block|{
name|readonlyNode
operator|=
name|node
expr_stmt|;
name|typeNode
operator|=
name|readonlyNode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|typeNode
operator|=
name|node
expr_stmt|;
block|}
name|nameNode
operator|=
name|TypesUtils
operator|.
name|getCorbaTypeNameNode
argument_list|(
name|typeNode
argument_list|)
expr_stmt|;
while|while
condition|(
name|nameNode
operator|!=
literal|null
condition|)
block|{
comment|// getter is generated for readonly and readwrite attributes
name|generateGetter
argument_list|(
name|typeNode
argument_list|,
name|nameNode
argument_list|)
expr_stmt|;
comment|// setter is generated only for readwrite attributes
if|if
condition|(
name|readonlyNode
operator|==
literal|null
condition|)
block|{
name|generateSetter
argument_list|(
name|typeNode
argument_list|,
name|nameNode
argument_list|)
expr_stmt|;
block|}
name|nameNode
operator|=
name|nameNode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|generateGetter
parameter_list|(
name|AST
name|typeNode
parameter_list|,
name|AST
name|nameNode
parameter_list|)
block|{
comment|// generate wrapped doc element in parameter
name|XmlSchemaElement
name|inParameters
init|=
name|generateWrappedDocElement
argument_list|(
literal|null
argument_list|,
name|GETTER_PREFIX
operator|+
name|nameNode
operator|.
name|toString
argument_list|()
argument_list|,
name|PARAM_NAME
argument_list|)
decl_stmt|;
comment|// generate wrapped doc element out parameter
name|XmlSchemaElement
name|outParameters
init|=
name|generateWrappedDocElement
argument_list|(
name|typeNode
argument_list|,
name|GETTER_PREFIX
operator|+
name|nameNode
operator|.
name|toString
argument_list|()
operator|+
name|RESULT_POSTFIX
argument_list|,
name|RETURN_PARAM_NAME
argument_list|)
decl_stmt|;
comment|// generate input message
name|Message
name|inMsg
init|=
name|generateMessage
argument_list|(
name|inParameters
argument_list|,
name|GETTER_PREFIX
operator|+
name|nameNode
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
comment|// generate output message
name|Message
name|outMsg
init|=
name|generateMessage
argument_list|(
name|outParameters
argument_list|,
name|GETTER_PREFIX
operator|+
name|nameNode
operator|.
name|toString
argument_list|()
operator|+
name|RESPONSE_POSTFIX
argument_list|)
decl_stmt|;
comment|// generate operation
name|String
name|name
init|=
name|GETTER_PREFIX
operator|+
name|nameNode
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Operation
name|op
init|=
name|generateOperation
argument_list|(
name|name
argument_list|,
name|inMsg
argument_list|,
name|outMsg
argument_list|)
decl_stmt|;
comment|// generate corba return param
name|ArgType
name|corbaReturn
init|=
name|generateCorbaReturnParam
argument_list|(
name|typeNode
argument_list|)
decl_stmt|;
comment|// generate corba operation
name|OperationType
name|corbaOp
init|=
name|generateCorbaOperation
argument_list|(
name|op
argument_list|,
literal|null
argument_list|,
name|corbaReturn
argument_list|)
decl_stmt|;
comment|// generate binding
name|generateCorbaBindingOperation
argument_list|(
name|binding
argument_list|,
name|op
argument_list|,
name|corbaOp
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|generateSetter
parameter_list|(
name|AST
name|typeNode
parameter_list|,
name|AST
name|nameNode
parameter_list|)
block|{
comment|// generate wrapped doc element in parameter
name|XmlSchemaElement
name|inParameters
init|=
name|generateWrappedDocElement
argument_list|(
name|typeNode
argument_list|,
name|SETTER_PREFIX
operator|+
name|nameNode
operator|.
name|toString
argument_list|()
argument_list|,
name|PARAM_NAME
argument_list|)
decl_stmt|;
comment|// generate wrapped doc element out parameter
name|XmlSchemaElement
name|outParameters
init|=
name|generateWrappedDocElement
argument_list|(
literal|null
argument_list|,
name|SETTER_PREFIX
operator|+
name|nameNode
operator|.
name|toString
argument_list|()
operator|+
name|RESULT_POSTFIX
argument_list|,
name|RETURN_PARAM_NAME
argument_list|)
decl_stmt|;
comment|// generate input message
name|Message
name|inMsg
init|=
name|generateMessage
argument_list|(
name|inParameters
argument_list|,
name|SETTER_PREFIX
operator|+
name|nameNode
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
comment|// generate output message
name|Message
name|outMsg
init|=
name|generateMessage
argument_list|(
name|outParameters
argument_list|,
name|SETTER_PREFIX
operator|+
name|nameNode
operator|.
name|toString
argument_list|()
operator|+
name|RESPONSE_POSTFIX
argument_list|)
decl_stmt|;
comment|// generate operation
name|String
name|name
init|=
name|SETTER_PREFIX
operator|+
name|nameNode
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Operation
name|op
init|=
name|generateOperation
argument_list|(
name|name
argument_list|,
name|inMsg
argument_list|,
name|outMsg
argument_list|)
decl_stmt|;
comment|// generate corba return param
name|ParamType
name|corbaParam
init|=
name|generateCorbaParam
argument_list|(
name|typeNode
argument_list|)
decl_stmt|;
comment|// generate corba operation
name|OperationType
name|corbaOp
init|=
name|generateCorbaOperation
argument_list|(
name|op
argument_list|,
name|corbaParam
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|// generate binding
name|generateCorbaBindingOperation
argument_list|(
name|binding
argument_list|,
name|op
argument_list|,
name|corbaOp
argument_list|)
expr_stmt|;
block|}
comment|/** Generate a wrapped doc style XmlSchemaElement containing one element.      *      * I.e.: generateWrappedDocElement(null, "foo", "bar");      *<xs:element name="foo">      *<xs:complexType>      *<xs:sequence>      *</xs:sequence>      *</xs:complexType>      *</xs:element>      *      * i.e.: generateWrappedDocElement(type, "foo", "bar");      *<xs:element name="foo">      *<xs:complexType>      *<xs:sequence>      *<xs:element name="bar" type="xs:short">      *</xs:element>      *</xs:sequence>      *</xs:complexType>      *</xs:element>       *      * @param typeNode is the type of the element wrapped in the sequence, no element is created if null.      * @param name is the name of the wrapping element.      * @param paramName is the name of the  wrapping element.      * @return the wrapping element.      */
specifier|private
name|XmlSchemaElement
name|generateWrappedDocElement
parameter_list|(
name|AST
name|typeNode
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|paramName
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
if|if
condition|(
name|typeNode
operator|!=
literal|null
condition|)
block|{
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
name|stype
init|=
name|visitor
operator|.
name|getSchemaType
argument_list|()
decl_stmt|;
name|Scope
name|fqName
init|=
name|visitor
operator|.
name|getFullyQualifiedName
argument_list|()
decl_stmt|;
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
name|AttributeDeferredAction
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|element
operator|.
name|setName
argument_list|(
name|paramName
argument_list|)
expr_stmt|;
block|}
name|XmlSchemaSequence
name|sequence
init|=
operator|new
name|XmlSchemaSequence
argument_list|()
decl_stmt|;
if|if
condition|(
name|typeNode
operator|!=
literal|null
condition|)
block|{
name|sequence
operator|.
name|getItems
argument_list|()
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
name|XmlSchemaComplexType
name|complex
init|=
operator|new
name|XmlSchemaComplexType
argument_list|(
name|schema
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|complex
operator|.
name|setParticle
argument_list|(
name|sequence
argument_list|)
expr_stmt|;
name|XmlSchemaElement
name|result
init|=
operator|new
name|XmlSchemaElement
argument_list|(
name|schema
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|result
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|result
operator|.
name|setSchemaType
argument_list|(
name|complex
argument_list|)
expr_stmt|;
name|schema
operator|.
name|getItems
argument_list|()
operator|.
name|add
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|private
name|Message
name|generateMessage
parameter_list|(
name|XmlSchemaElement
name|element
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|Part
name|part
init|=
name|definition
operator|.
name|createPart
argument_list|()
decl_stmt|;
name|part
operator|.
name|setName
argument_list|(
name|PART_NAME
argument_list|)
expr_stmt|;
name|part
operator|.
name|setElementName
argument_list|(
name|element
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|result
init|=
name|definition
operator|.
name|createMessage
argument_list|()
decl_stmt|;
name|result
operator|.
name|setQName
argument_list|(
operator|new
name|QName
argument_list|(
name|definition
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|addPart
argument_list|(
name|part
argument_list|)
expr_stmt|;
name|result
operator|.
name|setUndefined
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|definition
operator|.
name|addMessage
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|private
name|Operation
name|generateOperation
parameter_list|(
name|String
name|name
parameter_list|,
name|Message
name|inputMsg
parameter_list|,
name|Message
name|outputMsg
parameter_list|)
block|{
name|Input
name|input
init|=
name|definition
operator|.
name|createInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|setName
argument_list|(
name|inputMsg
operator|.
name|getQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|input
operator|.
name|setMessage
argument_list|(
name|inputMsg
argument_list|)
expr_stmt|;
name|Output
name|output
init|=
name|definition
operator|.
name|createOutput
argument_list|()
decl_stmt|;
name|output
operator|.
name|setName
argument_list|(
name|outputMsg
operator|.
name|getQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|output
operator|.
name|setMessage
argument_list|(
name|outputMsg
argument_list|)
expr_stmt|;
name|Operation
name|result
init|=
name|definition
operator|.
name|createOperation
argument_list|()
decl_stmt|;
name|result
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|result
operator|.
name|setInput
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|result
operator|.
name|setOutput
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|result
operator|.
name|setUndefined
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|portType
operator|.
name|addOperation
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|private
name|ArgType
name|generateCorbaReturnParam
parameter_list|(
name|AST
name|type
parameter_list|)
block|{
name|ArgType
name|param
init|=
operator|new
name|ArgType
argument_list|()
decl_stmt|;
name|param
operator|.
name|setName
argument_list|(
name|RETURN_PARAM_NAME
argument_list|)
expr_stmt|;
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
name|type
argument_list|)
expr_stmt|;
name|CorbaTypeImpl
name|corbaType
init|=
name|visitor
operator|.
name|getCorbaType
argument_list|()
decl_stmt|;
if|if
condition|(
name|corbaType
operator|!=
literal|null
condition|)
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
else|else
block|{
name|wsdlVisitor
operator|.
name|getDeferredActions
argument_list|()
operator|.
name|add
argument_list|(
name|visitor
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|,
operator|new
name|AttributeDeferredAction
argument_list|(
name|param
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|param
return|;
block|}
specifier|private
name|ParamType
name|generateCorbaParam
parameter_list|(
name|AST
name|type
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
name|PARAM_NAME
argument_list|)
expr_stmt|;
name|param
operator|.
name|setMode
argument_list|(
name|ModeType
operator|.
name|IN
argument_list|)
expr_stmt|;
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
name|type
argument_list|)
expr_stmt|;
name|CorbaTypeImpl
name|corbaType
init|=
name|visitor
operator|.
name|getCorbaType
argument_list|()
decl_stmt|;
if|if
condition|(
name|corbaType
operator|!=
literal|null
condition|)
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
else|else
block|{
name|wsdlVisitor
operator|.
name|getDeferredActions
argument_list|()
operator|.
name|add
argument_list|(
name|visitor
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|,
operator|new
name|AttributeDeferredAction
argument_list|(
name|param
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|param
return|;
block|}
comment|/** Generates a corba:operation in the corba:binding container within a wsdl:binding.      *      * Only one (or none) corba parameter and only one (or none) corba return parameter are supported.      *      * @param op is the wsdl operation to bind.      * @param param is the corba parameter, none if null.      * @param arg is the corba return parameter, none if null.      * @return the generated corba:operation.      */
specifier|private
name|OperationType
name|generateCorbaOperation
parameter_list|(
name|Operation
name|op
parameter_list|,
name|ParamType
name|param
parameter_list|,
name|ArgType
name|arg
parameter_list|)
block|{
name|OperationType
name|operation
init|=
operator|new
name|OperationType
argument_list|()
decl_stmt|;
try|try
block|{
name|operation
operator|=
operator|(
name|OperationType
operator|)
name|extReg
operator|.
name|createExtension
argument_list|(
name|BindingOperation
operator|.
name|class
argument_list|,
name|CorbaConstants
operator|.
name|NE_CORBA_OPERATION
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSDLException
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
name|operation
operator|.
name|setName
argument_list|(
name|op
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|param
operator|!=
literal|null
condition|)
block|{
name|operation
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
if|if
condition|(
name|arg
operator|!=
literal|null
condition|)
block|{
name|operation
operator|.
name|setReturn
argument_list|(
name|arg
argument_list|)
expr_stmt|;
block|}
return|return
name|operation
return|;
block|}
specifier|private
name|BindingOperation
name|generateCorbaBindingOperation
parameter_list|(
name|Binding
name|wsdlBinding
parameter_list|,
name|Operation
name|op
parameter_list|,
name|OperationType
name|corbaOp
parameter_list|)
block|{
name|BindingInput
name|bindingInput
init|=
name|definition
operator|.
name|createBindingInput
argument_list|()
decl_stmt|;
name|bindingInput
operator|.
name|setName
argument_list|(
name|op
operator|.
name|getInput
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|BindingOutput
name|bindingOutput
init|=
name|definition
operator|.
name|createBindingOutput
argument_list|()
decl_stmt|;
name|bindingOutput
operator|.
name|setName
argument_list|(
name|op
operator|.
name|getOutput
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|BindingOperation
name|bindingOperation
init|=
name|definition
operator|.
name|createBindingOperation
argument_list|()
decl_stmt|;
name|bindingOperation
operator|.
name|addExtensibilityElement
argument_list|(
name|corbaOp
argument_list|)
expr_stmt|;
name|bindingOperation
operator|.
name|setOperation
argument_list|(
name|op
argument_list|)
expr_stmt|;
name|bindingOperation
operator|.
name|setName
argument_list|(
name|op
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|bindingOperation
operator|.
name|setBindingInput
argument_list|(
name|bindingInput
argument_list|)
expr_stmt|;
name|bindingOperation
operator|.
name|setBindingOutput
argument_list|(
name|bindingOutput
argument_list|)
expr_stmt|;
name|binding
operator|.
name|addBindingOperation
argument_list|(
name|bindingOperation
argument_list|)
expr_stmt|;
return|return
name|bindingOperation
return|;
block|}
block|}
end_class

end_unit

