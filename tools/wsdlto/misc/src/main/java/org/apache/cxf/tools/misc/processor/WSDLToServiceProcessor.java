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
name|Port
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
name|WSDLExtensibilityPlugin
import|;
end_import

begin_class
specifier|public
class|class
name|WSDLToServiceProcessor
extends|extends
name|AbstractWSDLToProcessor
block|{
specifier|private
specifier|static
specifier|final
name|String
name|NEW_FILE_NAME_MODIFIER
init|=
literal|"-service"
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
if|if
condition|(
operator|!
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
literal|"BINDING_NOT_EXIST"
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
name|doAppendService
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
name|QName
name|serviceQName
range|:
name|services
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|serviceName
init|=
name|serviceQName
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
name|services
operator|.
name|get
argument_list|(
name|serviceQName
argument_list|)
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
name|String
name|portName
range|:
name|ports
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|portName
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
name|ports
operator|.
name|get
argument_list|(
name|portName
argument_list|)
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
name|bindingQName
range|:
name|bindings
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|bindingName
init|=
name|bindingQName
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|String
name|attrBinding
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
name|CFG_BINDING_ATTR
argument_list|)
decl_stmt|;
if|if
condition|(
name|attrBinding
operator|.
name|equals
argument_list|(
name|bindingName
argument_list|)
condition|)
block|{
name|binding
operator|=
name|bindings
operator|.
name|get
argument_list|(
name|bindingQName
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
argument_list|,
name|wse
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
argument_list|,
name|ioe
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|setAddrElement
parameter_list|()
throws|throws
name|ToolException
block|{
name|String
name|transport
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
name|CFG_TRANSPORT
argument_list|)
decl_stmt|;
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
name|transport
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ns
init|=
name|address
operator|.
name|getNamespaces
argument_list|(
name|env
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|ns
operator|.
name|keySet
argument_list|()
control|)
block|{
name|wsdlDefinition
operator|.
name|addNamespace
argument_list|(
name|key
argument_list|,
name|ns
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|WSDLExtensibilityPlugin
name|plugin
init|=
name|getWSDLPlugin
argument_list|(
name|transport
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
name|plugin
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
literal|"FAIL_TO_CREATE_SOAP_ADDRESS"
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
block|}
end_class

end_unit

