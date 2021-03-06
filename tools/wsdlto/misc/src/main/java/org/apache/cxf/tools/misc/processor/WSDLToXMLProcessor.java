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
name|HashMap
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
name|Port
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
name|Service
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
name|ExtensibilityElement
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
name|misc
operator|.
name|processor
operator|.
name|address
operator|.
name|Address
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
name|misc
operator|.
name|processor
operator|.
name|address
operator|.
name|AddressFactory
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
name|wsdl
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
name|wsdl
operator|.
name|WSDLExtensibilityPlugin
import|;
end_import

begin_class
specifier|public
class|class
name|WSDLToXMLProcessor
extends|extends
name|AbstractWSDLToProcessor
block|{
specifier|private
specifier|static
specifier|final
name|String
name|NEW_FILE_NAME_MODIFIER
init|=
literal|"-xmlbinding"
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|QName
argument_list|,
name|Service
argument_list|>
name|services
decl_stmt|;
specifier|private
name|Service
name|service
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Port
argument_list|>
name|ports
decl_stmt|;
specifier|private
name|Port
name|port
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
name|isServicePortExisted
argument_list|()
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"SERVICE_PORT_EXIST"
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
name|doAppendBinding
argument_list|()
expr_stmt|;
name|doAppendService
argument_list|()
expr_stmt|;
name|writeToWSDL
argument_list|()
expr_stmt|;
block|}
specifier|private
name|boolean
name|isServicePortExisted
parameter_list|()
block|{
return|return
name|isServiceExisted
argument_list|()
operator|&&
name|isPortExisted
argument_list|()
return|;
block|}
specifier|private
name|boolean
name|isServiceExisted
parameter_list|()
block|{
name|services
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|wsdlDefinition
operator|.
name|getServices
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|services
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
name|Map
operator|.
name|Entry
argument_list|<
name|QName
argument_list|,
name|Service
argument_list|>
name|entry
range|:
name|services
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|serviceName
init|=
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
if|if
condition|(
name|serviceName
operator|.
name|equals
argument_list|(
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_SERVICE
argument_list|)
argument_list|)
condition|)
block|{
name|service
operator|=
name|entry
operator|.
name|getValue
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
return|return
operator|(
name|service
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
name|isPortExisted
parameter_list|()
block|{
name|ports
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|service
operator|.
name|getPorts
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|ports
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
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Port
argument_list|>
name|entry
range|:
name|ports
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|equals
argument_list|(
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_PORT
argument_list|)
argument_list|)
condition|)
block|{
name|port
operator|=
name|entry
operator|.
name|getValue
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
return|return
operator|(
name|port
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
name|Map
operator|.
name|Entry
argument_list|<
name|QName
argument_list|,
name|PortType
argument_list|>
name|entry
range|:
name|portTypes
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|existPortName
init|=
name|entry
operator|.
name|getKey
argument_list|()
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
name|entry
operator|.
name|getValue
argument_list|()
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
name|Map
operator|.
name|Entry
argument_list|<
name|QName
argument_list|,
name|Binding
argument_list|>
name|entry
range|:
name|bindings
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|existBindingName
init|=
name|entry
operator|.
name|getKey
argument_list|()
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
name|entry
operator|.
name|getValue
argument_list|()
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
if|if
condition|(
name|wsdlDefinition
operator|.
name|getNamespace
argument_list|(
name|ToolConstants
operator|.
name|XML_FORMAT_PREFIX
argument_list|)
operator|==
literal|null
condition|)
block|{
name|wsdlDefinition
operator|.
name|addNamespace
argument_list|(
name|ToolConstants
operator|.
name|XML_FORMAT_PREFIX
argument_list|,
name|ToolConstants
operator|.
name|NS_XML_FORMAT
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wsdlDefinition
operator|.
name|getNamespace
argument_list|(
name|ToolConstants
operator|.
name|XML_HTTP_PREFIX
argument_list|)
operator|==
literal|null
condition|)
block|{
name|wsdlDefinition
operator|.
name|addNamespace
argument_list|(
name|ToolConstants
operator|.
name|XML_HTTP_PREFIX
argument_list|,
name|ToolConstants
operator|.
name|NS_XML_HTTP
argument_list|)
expr_stmt|;
block|}
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
name|setXMLBindingExtElement
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
block|}
specifier|private
name|void
name|setXMLBindingExtElement
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
try|try
block|{
name|binding
operator|.
name|addExtensibilityElement
argument_list|(
name|getWSDLPlugin
argument_list|(
literal|"xml"
argument_list|,
name|Binding
operator|.
name|class
argument_list|)
operator|.
name|createExtension
argument_list|(
literal|null
argument_list|)
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
literal|"FAIL_TO_CREATE_XMLBINDING"
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
argument_list|,
name|op
operator|.
name|getName
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
argument_list|,
name|op
operator|.
name|getName
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
name|addXMLFaults
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
name|BindingInput
name|getBindingInput
parameter_list|(
name|Input
name|input
parameter_list|,
name|String
name|operationName
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
comment|//This ext element in some scenario is optional, but if provided, won't cause error
name|bi
operator|.
name|addExtensibilityElement
argument_list|(
name|getXMLBody
argument_list|(
name|BindingInput
operator|.
name|class
argument_list|,
name|operationName
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
parameter_list|,
name|String
name|operationName
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
name|bo
operator|.
name|addExtensibilityElement
argument_list|(
name|getXMLBody
argument_list|(
name|BindingOutput
operator|.
name|class
argument_list|,
name|operationName
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|bo
return|;
block|}
specifier|private
name|void
name|addXMLFaults
parameter_list|(
name|Operation
name|op
parameter_list|,
name|BindingOperation
name|bo
parameter_list|)
block|{
comment|// TODO
block|}
specifier|private
name|ExtensibilityElement
name|getXMLBody
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|,
name|String
name|operationName
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|args
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|args
operator|.
name|put
argument_list|(
name|QName
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|QName
argument_list|(
name|wsdlDefinition
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
name|operationName
argument_list|)
argument_list|)
expr_stmt|;
name|args
operator|.
name|put
argument_list|(
name|Class
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|clz
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|getWSDLPlugin
argument_list|(
literal|"xml"
argument_list|,
name|clz
argument_list|)
operator|.
name|createExtension
argument_list|(
name|args
argument_list|)
return|;
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
literal|"FAIL_TO_CREATE_XMLBINDING"
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
name|doAppendService
parameter_list|()
throws|throws
name|ToolException
block|{
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
name|service
operator|=
name|wsdlDefinition
operator|.
name|createService
argument_list|()
expr_stmt|;
name|service
operator|.
name|setQName
argument_list|(
operator|new
name|QName
argument_list|(
name|WSDLConstants
operator|.
name|WSDL_PREFIX
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
name|CFG_SERVICE
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|port
operator|==
literal|null
condition|)
block|{
name|port
operator|=
name|wsdlDefinition
operator|.
name|createPort
argument_list|()
expr_stmt|;
name|port
operator|.
name|setName
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
name|CFG_PORT
argument_list|)
argument_list|)
expr_stmt|;
name|port
operator|.
name|setBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
block|}
name|setAddrElement
argument_list|()
expr_stmt|;
name|service
operator|.
name|addPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|wsdlDefinition
operator|.
name|addService
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setAddrElement
parameter_list|()
throws|throws
name|ToolException
block|{
name|Address
name|address
init|=
name|AddressFactory
operator|.
name|getInstance
argument_list|()
operator|.
name|getAddresser
argument_list|(
literal|"xml"
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|address
operator|.
name|getNamespaces
argument_list|(
name|env
argument_list|)
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|wsdlDefinition
operator|.
name|addNamespace
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|WSDLExtensibilityPlugin
name|generator
init|=
name|getWSDLPlugin
argument_list|(
literal|"xml"
argument_list|,
name|Port
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|ExtensibilityElement
name|extElement
init|=
name|generator
operator|.
name|createExtension
argument_list|(
name|address
operator|.
name|buildAddressArguments
argument_list|(
name|env
argument_list|)
argument_list|)
decl_stmt|;
name|port
operator|.
name|addExtensibilityElement
argument_list|(
name|extElement
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
literal|"FAIL_TO_CREATE_SOAPADDRESS"
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
name|writeToWSDL
parameter_list|()
throws|throws
name|ToolException
block|{
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
literal|"FAIL_TO_CLOSE_WSDL_FILE"
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
block|}
end_class

end_unit

