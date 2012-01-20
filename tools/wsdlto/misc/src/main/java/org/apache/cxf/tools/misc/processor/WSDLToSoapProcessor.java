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
name|misc
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

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
name|BindingFault
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
name|Fault
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
name|xml
operator|.
name|WSDLWriter
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|soap
operator|.
name|SOAPBindingUtil
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
name|soap
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|SoapBinding
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
name|soap
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|SoapBody
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
name|soap
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|SoapFault
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
name|soap
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|SoapOperation
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
name|common
operator|.
name|WSDLConstants
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
name|common
operator|.
name|i18n
operator|.
name|Message
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
name|helpers
operator|.
name|CastUtils
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
name|common
operator|.
name|ToolConstants
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
name|common
operator|.
name|ToolException
import|;
end_import

begin_class
specifier|public
class|class
name|WSDLToSoapProcessor
extends|extends
name|AbstractWSDLToProcessor
block|{
specifier|private
specifier|static
specifier|final
name|String
name|NEW_FILE_NAME_MODIFIER
init|=
literal|"-soapbinding"
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|QName
argument_list|,
name|PortType
argument_list|>
name|portTypes
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
name|void
name|process
parameter_list|()
throws|throws
name|ToolException
block|{
name|init
argument_list|()
expr_stmt|;
name|validate
argument_list|()
expr_stmt|;
name|doAppendBinding
argument_list|()
expr_stmt|;
block|}
specifier|private
name|boolean
name|isSOAP12
parameter_list|()
block|{
return|return
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_SOAP12
argument_list|)
return|;
block|}
specifier|private
name|void
name|validate
parameter_list|()
throws|throws
name|ToolException
block|{
if|if
condition|(
name|isBindingExisted
argument_list|()
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"BINDING_ALREADY_EXIST"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|isPortTypeExisted
argument_list|()
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"PORTTYPE_NOT_EXIST"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|nameSpaceCheck
argument_list|()
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"SOAPBINDING_STYLE_NOT_PROVIDED"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
if|if
condition|(
name|WSDLConstants
operator|.
name|RPC
operator|.
name|equalsIgnoreCase
argument_list|(
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_STYLE
argument_list|)
argument_list|)
condition|)
block|{
name|Collection
argument_list|<
name|Operation
argument_list|>
name|ops
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|portType
operator|.
name|getOperations
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Operation
name|op
range|:
name|ops
control|)
block|{
name|Input
name|input
init|=
name|op
operator|.
name|getInput
argument_list|()
decl_stmt|;
if|if
condition|(
name|input
operator|!=
literal|null
operator|&&
name|input
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|Part
argument_list|>
name|parts
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|input
operator|.
name|getMessage
argument_list|()
operator|.
name|getParts
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Part
name|part
range|:
name|parts
control|)
block|{
if|if
condition|(
name|part
operator|.
name|getTypeName
argument_list|()
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|part
operator|.
name|getTypeName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"RPC_PART_ILLEGAL"
argument_list|,
name|LOG
argument_list|,
operator|new
name|Object
index|[]
block|{
name|part
operator|.
name|getName
argument_list|()
block|}
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
block|}
block|}
name|Output
name|output
init|=
name|op
operator|.
name|getOutput
argument_list|()
decl_stmt|;
if|if
condition|(
name|output
operator|!=
literal|null
operator|&&
name|output
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|Part
argument_list|>
name|parts
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|output
operator|.
name|getMessage
argument_list|()
operator|.
name|getParts
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Part
name|part
range|:
name|parts
control|)
block|{
if|if
condition|(
name|part
operator|.
name|getTypeName
argument_list|()
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|part
operator|.
name|getTypeName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"RPC_PART_ILLEGAL"
argument_list|,
name|LOG
argument_list|,
operator|new
name|Object
index|[]
block|{
name|part
operator|.
name|getName
argument_list|()
block|}
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
block|}
block|}
block|}
block|}
block|}
specifier|private
name|boolean
name|isPortTypeExisted
parameter_list|()
block|{
name|portTypes
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|wsdlDefinition
operator|.
name|getPortTypes
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|portTypes
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|QName
name|existPortQName
range|:
name|portTypes
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|existPortName
init|=
name|existPortQName
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
if|if
condition|(
name|existPortName
operator|.
name|equals
argument_list|(
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_PORTTYPE
argument_list|)
argument_list|)
condition|)
block|{
name|portType
operator|=
name|portTypes
operator|.
name|get
argument_list|(
name|existPortQName
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
return|return
operator|(
name|portType
operator|==
literal|null
operator|)
condition|?
literal|false
else|:
literal|true
return|;
block|}
specifier|private
name|boolean
name|isBindingExisted
parameter_list|()
block|{
name|Map
argument_list|<
name|QName
argument_list|,
name|Binding
argument_list|>
name|bindings
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|wsdlDefinition
operator|.
name|getBindings
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|bindings
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|QName
name|existBindingQName
range|:
name|bindings
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|existBindingName
init|=
name|existBindingQName
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|String
name|bindingName
init|=
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_BINDING
argument_list|)
decl_stmt|;
if|if
condition|(
name|bindingName
operator|.
name|equals
argument_list|(
name|existBindingName
argument_list|)
condition|)
block|{
name|binding
operator|=
name|bindings
operator|.
name|get
argument_list|(
name|existBindingQName
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|binding
operator|==
literal|null
operator|)
condition|?
literal|false
else|:
literal|true
return|;
block|}
specifier|private
name|boolean
name|nameSpaceCheck
parameter_list|()
block|{
if|if
condition|(
name|WSDLConstants
operator|.
name|RPC
operator|.
name|equalsIgnoreCase
argument_list|(
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_STYLE
argument_list|)
argument_list|)
operator|&&
operator|!
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_NAMESPACE
argument_list|)
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
specifier|protected
name|void
name|init
parameter_list|()
throws|throws
name|ToolException
block|{
name|parseWSDL
argument_list|(
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doAppendBinding
parameter_list|()
throws|throws
name|ToolException
block|{
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|binding
operator|=
name|wsdlDefinition
operator|.
name|createBinding
argument_list|()
expr_stmt|;
name|binding
operator|.
name|setQName
argument_list|(
operator|new
name|QName
argument_list|(
name|wsdlDefinition
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_BINDING
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setUndefined
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setPortType
argument_list|(
name|portType
argument_list|)
expr_stmt|;
block|}
name|setSoapBindingExtElement
argument_list|()
expr_stmt|;
name|addBindingOperation
argument_list|()
expr_stmt|;
name|wsdlDefinition
operator|.
name|addBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|WSDLWriter
name|wsdlWriter
init|=
name|wsdlFactory
operator|.
name|newWSDLWriter
argument_list|()
decl_stmt|;
name|Writer
name|outputWriter
init|=
name|getOutputWriter
argument_list|(
name|NEW_FILE_NAME_MODIFIER
argument_list|)
decl_stmt|;
try|try
block|{
name|wsdlWriter
operator|.
name|writeWSDL
argument_list|(
name|wsdlDefinition
argument_list|,
name|outputWriter
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSDLException
name|wse
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_WRITE_WSDL"
argument_list|,
name|LOG
argument_list|,
name|wse
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
try|try
block|{
name|outputWriter
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"PORTTYPE_NOT_EXIST"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|setSoapBindingExtElement
parameter_list|()
throws|throws
name|ToolException
block|{
if|if
condition|(
name|extReg
operator|==
literal|null
condition|)
block|{
name|extReg
operator|=
name|wsdlFactory
operator|.
name|newPopulatedExtensionRegistry
argument_list|()
expr_stmt|;
block|}
name|SOAPBindingUtil
operator|.
name|addSOAPNamespace
argument_list|(
name|wsdlDefinition
argument_list|,
name|isSOAP12
argument_list|()
argument_list|)
expr_stmt|;
name|SoapBinding
name|soapBinding
init|=
literal|null
decl_stmt|;
try|try
block|{
name|soapBinding
operator|=
name|SOAPBindingUtil
operator|.
name|createSoapBinding
argument_list|(
name|extReg
argument_list|,
name|isSOAP12
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSDLException
name|wse
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_CREATE_SOAPBINDING"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|wse
argument_list|)
throw|;
block|}
name|soapBinding
operator|.
name|setStyle
argument_list|(
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_STYLE
argument_list|)
argument_list|)
expr_stmt|;
name|binding
operator|.
name|addExtensibilityElement
argument_list|(
name|soapBinding
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|void
name|addBindingOperation
parameter_list|()
throws|throws
name|ToolException
block|{
comment|/**          * This method won't do unique operation name checking on portType The          * WS-I Basic Profile[17] R2304 requires that operations within a          * wsdl:portType have unique values for their name attribute so mapping          * of WS-I compliant WSDLdescriptions will not generate Java interfaces          * with overloaded methods. However, for backwards compatibility, JAX-WS          * supports operation name overloading provided the overloading does not          * cause conflicts (as specified in the Java Language Specification[25])          * in the mapped Java service endpoint interface declaration.          */
name|List
argument_list|<
name|Operation
argument_list|>
name|ops
init|=
name|portType
operator|.
name|getOperations
argument_list|()
decl_stmt|;
for|for
control|(
name|Operation
name|op
range|:
name|ops
control|)
block|{
name|BindingOperation
name|bindingOperation
init|=
name|wsdlDefinition
operator|.
name|createBindingOperation
argument_list|()
decl_stmt|;
name|setSoapOperationExtElement
argument_list|(
name|bindingOperation
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
if|if
condition|(
name|op
operator|.
name|getInput
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|bindingOperation
operator|.
name|setBindingInput
argument_list|(
name|getBindingInput
argument_list|(
name|op
operator|.
name|getInput
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|op
operator|.
name|getOutput
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|bindingOperation
operator|.
name|setBindingOutput
argument_list|(
name|getBindingOutput
argument_list|(
name|op
operator|.
name|getOutput
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|op
operator|.
name|getFaults
argument_list|()
operator|!=
literal|null
operator|&&
name|op
operator|.
name|getFaults
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|addSoapFaults
argument_list|(
name|op
argument_list|,
name|bindingOperation
argument_list|)
expr_stmt|;
block|}
name|bindingOperation
operator|.
name|setOperation
argument_list|(
name|op
argument_list|)
expr_stmt|;
name|binding
operator|.
name|addBindingOperation
argument_list|(
name|bindingOperation
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|setSoapOperationExtElement
parameter_list|(
name|BindingOperation
name|bo
parameter_list|)
throws|throws
name|ToolException
block|{
if|if
condition|(
name|extReg
operator|==
literal|null
condition|)
block|{
name|extReg
operator|=
name|wsdlFactory
operator|.
name|newPopulatedExtensionRegistry
argument_list|()
expr_stmt|;
block|}
name|SoapOperation
name|soapOperation
init|=
literal|null
decl_stmt|;
try|try
block|{
name|soapOperation
operator|=
name|SOAPBindingUtil
operator|.
name|createSoapOperation
argument_list|(
name|extReg
argument_list|,
name|isSOAP12
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSDLException
name|wse
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_CREATE_SOAPBINDING"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|wse
argument_list|)
throw|;
block|}
name|soapOperation
operator|.
name|setStyle
argument_list|(
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_STYLE
argument_list|)
argument_list|)
expr_stmt|;
name|soapOperation
operator|.
name|setSoapActionURI
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|bo
operator|.
name|addExtensibilityElement
argument_list|(
name|soapOperation
argument_list|)
expr_stmt|;
block|}
specifier|private
name|BindingInput
name|getBindingInput
parameter_list|(
name|Input
name|input
parameter_list|)
throws|throws
name|ToolException
block|{
name|BindingInput
name|bi
init|=
name|wsdlDefinition
operator|.
name|createBindingInput
argument_list|()
decl_stmt|;
name|bi
operator|.
name|setName
argument_list|(
name|input
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// As command line won't specify the details of body/header for message
comment|// parts
comment|// All input message's parts will be added into one soap body element
name|bi
operator|.
name|addExtensibilityElement
argument_list|(
name|getSoapBody
argument_list|(
name|BindingInput
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|bi
return|;
block|}
specifier|private
name|BindingOutput
name|getBindingOutput
parameter_list|(
name|Output
name|output
parameter_list|)
throws|throws
name|ToolException
block|{
name|BindingOutput
name|bo
init|=
name|wsdlDefinition
operator|.
name|createBindingOutput
argument_list|()
decl_stmt|;
name|bo
operator|.
name|setName
argument_list|(
name|output
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// As command line won't specify the details of body/header for message
comment|// parts
comment|// All output message's parts will be added into one soap body element
name|bo
operator|.
name|addExtensibilityElement
argument_list|(
name|getSoapBody
argument_list|(
name|BindingOutput
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|bo
return|;
block|}
specifier|private
name|SoapBody
name|getSoapBody
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|parent
parameter_list|)
throws|throws
name|ToolException
block|{
if|if
condition|(
name|extReg
operator|==
literal|null
condition|)
block|{
name|extReg
operator|=
name|wsdlFactory
operator|.
name|newPopulatedExtensionRegistry
argument_list|()
expr_stmt|;
block|}
name|SoapBody
name|soapBody
init|=
literal|null
decl_stmt|;
try|try
block|{
name|soapBody
operator|=
name|SOAPBindingUtil
operator|.
name|createSoapBody
argument_list|(
name|extReg
argument_list|,
name|parent
argument_list|,
name|isSOAP12
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSDLException
name|wse
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_CREATE_SOAPBINDING"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|wse
argument_list|)
throw|;
block|}
name|soapBody
operator|.
name|setUse
argument_list|(
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_USE
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|WSDLConstants
operator|.
name|RPC
operator|.
name|equalsIgnoreCase
argument_list|(
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_STYLE
argument_list|)
argument_list|)
operator|&&
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_NAMESPACE
argument_list|)
condition|)
block|{
name|soapBody
operator|.
name|setNamespaceURI
argument_list|(
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_NAMESPACE
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|soapBody
return|;
block|}
specifier|private
name|void
name|addSoapFaults
parameter_list|(
name|Operation
name|op
parameter_list|,
name|BindingOperation
name|bindingOperation
parameter_list|)
throws|throws
name|ToolException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Fault
argument_list|>
name|faults
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|op
operator|.
name|getFaults
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Fault
name|fault
range|:
name|faults
operator|.
name|values
argument_list|()
control|)
block|{
name|BindingFault
name|bf
init|=
name|wsdlDefinition
operator|.
name|createBindingFault
argument_list|()
decl_stmt|;
name|bf
operator|.
name|setName
argument_list|(
name|fault
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|setSoapFaultExtElement
argument_list|(
name|bf
argument_list|)
expr_stmt|;
name|bindingOperation
operator|.
name|addBindingFault
argument_list|(
name|bf
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|setSoapFaultExtElement
parameter_list|(
name|BindingFault
name|bf
parameter_list|)
throws|throws
name|ToolException
block|{
if|if
condition|(
name|extReg
operator|==
literal|null
condition|)
block|{
name|extReg
operator|=
name|wsdlFactory
operator|.
name|newPopulatedExtensionRegistry
argument_list|()
expr_stmt|;
block|}
name|SoapFault
name|soapFault
init|=
literal|null
decl_stmt|;
try|try
block|{
name|soapFault
operator|=
name|SOAPBindingUtil
operator|.
name|createSoapFault
argument_list|(
name|extReg
argument_list|,
name|isSOAP12
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSDLException
name|wse
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_CREATE_SOAPBINDING"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|wse
argument_list|)
throw|;
block|}
name|soapFault
operator|.
name|setName
argument_list|(
name|bf
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|soapFault
operator|.
name|setUse
argument_list|(
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_USE
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|WSDLConstants
operator|.
name|RPC
operator|.
name|equalsIgnoreCase
argument_list|(
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_STYLE
argument_list|)
argument_list|)
operator|&&
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_NAMESPACE
argument_list|)
condition|)
block|{
name|soapFault
operator|.
name|setNamespaceURI
argument_list|(
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_NAMESPACE
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|bf
operator|.
name|addExtensibilityElement
argument_list|(
name|soapFault
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

