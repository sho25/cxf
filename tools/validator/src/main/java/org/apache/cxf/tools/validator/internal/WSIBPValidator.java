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
name|validator
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Member
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|Iterator
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
name|javax
operator|.
name|jws
operator|.
name|soap
operator|.
name|SOAPBinding
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
name|BindingOperation
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
name|Fault
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
name|SoapHeader
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
name|util
operator|.
name|CollectionUtils
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
name|util
operator|.
name|StringUtils
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
name|helpers
operator|.
name|WSDLHelper
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
name|WSIBPValidator
extends|extends
name|AbstractDefinitionValidator
block|{
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|operationMap
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|WSDLHelper
name|wsdlHelper
init|=
operator|new
name|WSDLHelper
argument_list|()
decl_stmt|;
specifier|public
name|WSIBPValidator
parameter_list|(
name|Definition
name|def
parameter_list|)
block|{
name|super
argument_list|(
name|def
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isValid
parameter_list|()
block|{
for|for
control|(
name|Method
name|m
range|:
name|getClass
argument_list|()
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|m
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"check"
argument_list|)
operator|||
name|m
operator|.
name|getModifiers
argument_list|()
operator|==
name|Member
operator|.
name|PUBLIC
condition|)
block|{
try|try
block|{
name|Boolean
name|res
init|=
operator|(
name|Boolean
operator|)
name|m
operator|.
name|invoke
argument_list|(
name|this
argument_list|,
operator|new
name|Object
index|[]
block|{}
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|res
operator|.
name|booleanValue
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|boolean
name|checkR2716
parameter_list|(
specifier|final
name|BindingOperation
name|bop
parameter_list|)
block|{
name|SoapBody
name|inSoapBody
init|=
name|SOAPBindingUtil
operator|.
name|getBindingInputSOAPBody
argument_list|(
name|bop
argument_list|)
decl_stmt|;
name|SoapBody
name|outSoapBody
init|=
name|SOAPBindingUtil
operator|.
name|getBindingOutputSOAPBody
argument_list|(
name|bop
argument_list|)
decl_stmt|;
if|if
condition|(
name|inSoapBody
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|inSoapBody
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|||
name|outSoapBody
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|outSoapBody
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|addErrorMessage
argument_list|(
name|getErrorPrefix
argument_list|(
literal|"WSI-BP-1.0 R2716"
argument_list|)
operator|+
literal|"Operation '"
operator|+
name|bop
operator|.
name|getName
argument_list|()
operator|+
literal|"' soapBody MUST NOT have namespace attribute"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|SoapHeader
name|inSoapHeader
init|=
name|SOAPBindingUtil
operator|.
name|getBindingInputSOAPHeader
argument_list|(
name|bop
argument_list|)
decl_stmt|;
name|SoapHeader
name|outSoapHeader
init|=
name|SOAPBindingUtil
operator|.
name|getBindingOutputSOAPHeader
argument_list|(
name|bop
argument_list|)
decl_stmt|;
if|if
condition|(
name|inSoapHeader
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|inSoapHeader
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|||
name|outSoapHeader
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|outSoapHeader
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|addErrorMessage
argument_list|(
name|getErrorPrefix
argument_list|(
literal|"WSI-BP-1.0 R2716"
argument_list|)
operator|+
literal|"Operation '"
operator|+
name|bop
operator|.
name|getName
argument_list|()
operator|+
literal|"' soapHeader MUST NOT have namespace attribute"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|List
argument_list|<
name|SoapFault
argument_list|>
name|soapFaults
init|=
name|SOAPBindingUtil
operator|.
name|getBindingOperationSoapFaults
argument_list|(
name|bop
argument_list|)
decl_stmt|;
for|for
control|(
name|SoapFault
name|fault
range|:
name|soapFaults
control|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|fault
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|addErrorMessage
argument_list|(
name|getErrorPrefix
argument_list|(
literal|"WSI-BP-1.0 R2716"
argument_list|)
operator|+
literal|"Operation '"
operator|+
name|bop
operator|.
name|getName
argument_list|()
operator|+
literal|"' soapFault MUST NOT have namespace attribute"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|boolean
name|checkR2717AndR2726
parameter_list|(
specifier|final
name|BindingOperation
name|bop
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|bop
condition|)
block|{
return|return
literal|true
return|;
block|}
name|SoapBody
name|inSoapBody
init|=
name|SOAPBindingUtil
operator|.
name|getBindingInputSOAPBody
argument_list|(
name|bop
argument_list|)
decl_stmt|;
name|SoapBody
name|outSoapBody
init|=
name|SOAPBindingUtil
operator|.
name|getBindingOutputSOAPBody
argument_list|(
name|bop
argument_list|)
decl_stmt|;
if|if
condition|(
name|inSoapBody
operator|!=
literal|null
operator|&&
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|inSoapBody
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|||
name|outSoapBody
operator|!=
literal|null
operator|&&
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|outSoapBody
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|addErrorMessage
argument_list|(
name|getErrorPrefix
argument_list|(
literal|"WSI-BP-1.0 R2717"
argument_list|)
operator|+
literal|"soapBody in the input/output of the binding operation '"
operator|+
name|bop
operator|.
name|getName
argument_list|()
operator|+
literal|"' MUST have namespace attribute"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|SoapHeader
name|inSoapHeader
init|=
name|SOAPBindingUtil
operator|.
name|getBindingInputSOAPHeader
argument_list|(
name|bop
argument_list|)
decl_stmt|;
name|SoapHeader
name|outSoapHeader
init|=
name|SOAPBindingUtil
operator|.
name|getBindingOutputSOAPHeader
argument_list|(
name|bop
argument_list|)
decl_stmt|;
if|if
condition|(
name|inSoapHeader
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|inSoapHeader
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|||
name|outSoapHeader
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|outSoapHeader
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|addErrorMessage
argument_list|(
name|getErrorPrefix
argument_list|(
literal|"WSI-BP-1.0 R2726"
argument_list|)
operator|+
literal|"Operation '"
operator|+
name|bop
operator|.
name|getName
argument_list|()
operator|+
literal|"' soapHeader MUST NOT have namespace attribute"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|List
argument_list|<
name|SoapFault
argument_list|>
name|soapFaults
init|=
name|SOAPBindingUtil
operator|.
name|getBindingOperationSoapFaults
argument_list|(
name|bop
argument_list|)
decl_stmt|;
for|for
control|(
name|SoapFault
name|fault
range|:
name|soapFaults
control|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|fault
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|addErrorMessage
argument_list|(
name|getErrorPrefix
argument_list|(
literal|"WSI-BP-1.0 R2726"
argument_list|)
operator|+
literal|"Operation '"
operator|+
name|bop
operator|.
name|getName
argument_list|()
operator|+
literal|"' soapFault MUST NOT have namespace attribute"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|boolean
name|checkR2201Input
parameter_list|(
specifier|final
name|Operation
name|operation
parameter_list|,
specifier|final
name|BindingOperation
name|bop
parameter_list|)
block|{
name|List
argument_list|<
name|Part
argument_list|>
name|partsList
init|=
name|wsdlHelper
operator|.
name|getInMessageParts
argument_list|(
name|operation
argument_list|)
decl_stmt|;
name|int
name|inmessagePartsCount
init|=
name|partsList
operator|.
name|size
argument_list|()
decl_stmt|;
name|SoapBody
name|soapBody
init|=
name|SOAPBindingUtil
operator|.
name|getBindingInputSOAPBody
argument_list|(
name|bop
argument_list|)
decl_stmt|;
if|if
condition|(
name|soapBody
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|parts
init|=
name|soapBody
operator|.
name|getParts
argument_list|()
decl_stmt|;
name|int
name|boundPartSize
init|=
name|parts
operator|==
literal|null
condition|?
name|inmessagePartsCount
else|:
name|parts
operator|.
name|size
argument_list|()
decl_stmt|;
name|SoapHeader
name|soapHeader
init|=
name|SOAPBindingUtil
operator|.
name|getBindingInputSOAPHeader
argument_list|(
name|bop
argument_list|)
decl_stmt|;
name|boundPartSize
operator|=
name|soapHeader
operator|!=
literal|null
operator|&&
name|soapHeader
operator|.
name|getMessage
argument_list|()
operator|.
name|equals
argument_list|(
name|operation
operator|.
name|getInput
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|.
name|getQName
argument_list|()
argument_list|)
condition|?
name|boundPartSize
operator|-
literal|1
else|:
name|boundPartSize
expr_stmt|;
if|if
condition|(
name|parts
operator|!=
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|?
argument_list|>
name|partsIte
init|=
name|parts
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|partsIte
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|partName
init|=
operator|(
name|String
operator|)
name|partsIte
operator|.
name|next
argument_list|()
decl_stmt|;
name|boolean
name|isDefined
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Part
name|part
range|:
name|partsList
control|)
block|{
if|if
condition|(
name|partName
operator|.
name|equalsIgnoreCase
argument_list|(
name|part
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|isDefined
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|isDefined
condition|)
block|{
name|addErrorMessage
argument_list|(
name|getErrorPrefix
argument_list|(
literal|"WSI-BP-1.0 R2201"
argument_list|)
operator|+
literal|"Operation '"
operator|+
name|operation
operator|.
name|getName
argument_list|()
operator|+
literal|"' soapBody parts : "
operator|+
name|partName
operator|+
literal|" not found in the message, wrong WSDL"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
else|else
block|{
if|if
condition|(
name|partsList
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|addErrorMessage
argument_list|(
name|getErrorPrefix
argument_list|(
literal|"WSI-BP-1.0 R2210"
argument_list|)
operator|+
literal|"Operation '"
operator|+
name|operation
operator|.
name|getName
argument_list|()
operator|+
literal|"' more than one part bound to body"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
name|boundPartSize
operator|>
literal|1
condition|)
block|{
name|addErrorMessage
argument_list|(
name|getErrorPrefix
argument_list|(
literal|"WSI-BP-1.0 R2201"
argument_list|)
operator|+
literal|"Operation '"
operator|+
name|operation
operator|.
name|getName
argument_list|()
operator|+
literal|"' more than one part bound to body"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|boolean
name|checkR2201Output
parameter_list|(
specifier|final
name|Operation
name|operation
parameter_list|,
specifier|final
name|BindingOperation
name|bop
parameter_list|)
block|{
name|int
name|outmessagePartsCount
init|=
name|wsdlHelper
operator|.
name|getOutMessageParts
argument_list|(
name|operation
argument_list|)
operator|.
name|size
argument_list|()
decl_stmt|;
name|SoapBody
name|soapBody
init|=
name|SOAPBindingUtil
operator|.
name|getBindingOutputSOAPBody
argument_list|(
name|bop
argument_list|)
decl_stmt|;
if|if
condition|(
name|soapBody
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|parts
init|=
name|soapBody
operator|.
name|getParts
argument_list|()
decl_stmt|;
name|int
name|boundPartSize
init|=
name|parts
operator|==
literal|null
condition|?
name|outmessagePartsCount
else|:
name|parts
operator|.
name|size
argument_list|()
decl_stmt|;
name|SoapHeader
name|soapHeader
init|=
name|SOAPBindingUtil
operator|.
name|getBindingOutputSOAPHeader
argument_list|(
name|bop
argument_list|)
decl_stmt|;
name|boundPartSize
operator|=
name|soapHeader
operator|!=
literal|null
operator|&&
name|soapHeader
operator|.
name|getMessage
argument_list|()
operator|.
name|equals
argument_list|(
name|operation
operator|.
name|getOutput
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|.
name|getQName
argument_list|()
argument_list|)
condition|?
name|boundPartSize
operator|-
literal|1
else|:
name|boundPartSize
expr_stmt|;
if|if
condition|(
name|parts
operator|!=
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|?
argument_list|>
name|partsIte
init|=
name|parts
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|partsIte
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|partName
init|=
operator|(
name|String
operator|)
name|partsIte
operator|.
name|next
argument_list|()
decl_stmt|;
name|boolean
name|isDefined
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Part
name|part
range|:
name|wsdlHelper
operator|.
name|getOutMessageParts
argument_list|(
name|operation
argument_list|)
control|)
block|{
if|if
condition|(
name|partName
operator|.
name|equalsIgnoreCase
argument_list|(
name|part
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|isDefined
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|isDefined
condition|)
block|{
name|addErrorMessage
argument_list|(
name|getErrorPrefix
argument_list|(
literal|"WSI-BP-1.0 R2201"
argument_list|)
operator|+
literal|"Operation '"
operator|+
name|operation
operator|.
name|getName
argument_list|()
operator|+
literal|"' soapBody parts : "
operator|+
name|partName
operator|+
literal|" not found in the message, wrong WSDL"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
else|else
block|{
if|if
condition|(
name|wsdlHelper
operator|.
name|getOutMessageParts
argument_list|(
name|operation
argument_list|)
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|addErrorMessage
argument_list|(
name|getErrorPrefix
argument_list|(
literal|"WSI-BP-1.0 R2210"
argument_list|)
operator|+
literal|"Operation '"
operator|+
name|operation
operator|.
name|getName
argument_list|()
operator|+
literal|"' more than one part bound to body"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
name|boundPartSize
operator|>
literal|1
condition|)
block|{
name|addErrorMessage
argument_list|(
name|getErrorPrefix
argument_list|(
literal|"WSI-BP-1.0 R2201"
argument_list|)
operator|+
literal|"Operation '"
operator|+
name|operation
operator|.
name|getName
argument_list|()
operator|+
literal|"' more than one part bound to body"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|checkBinding
parameter_list|()
block|{
for|for
control|(
name|PortType
name|portType
range|:
name|wsdlHelper
operator|.
name|getPortTypes
argument_list|(
name|def
argument_list|)
control|)
block|{
name|Iterator
argument_list|<
name|?
argument_list|>
name|ite
init|=
name|portType
operator|.
name|getOperations
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|ite
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Operation
name|operation
init|=
operator|(
name|Operation
operator|)
name|ite
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|isOverloading
argument_list|(
name|operation
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|BindingOperation
name|bop
init|=
name|wsdlHelper
operator|.
name|getBindingOperation
argument_list|(
name|def
argument_list|,
name|operation
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|bop
operator|==
literal|null
condition|)
block|{
name|addErrorMessage
argument_list|(
name|getErrorPrefix
argument_list|(
literal|"WSI-BP-1.0 R2718"
argument_list|)
operator|+
literal|"A wsdl:binding in a DESCRIPTION MUST have the same set of "
operator|+
literal|"wsdl:operations as the wsdl:portType to which it refers. "
operator|+
name|operation
operator|.
name|getName
argument_list|()
operator|+
literal|" not found in wsdl:binding."
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|Binding
name|binding
init|=
name|wsdlHelper
operator|.
name|getBinding
argument_list|(
name|bop
argument_list|,
name|def
argument_list|)
decl_stmt|;
name|String
name|bindingStyle
init|=
name|binding
operator|!=
literal|null
condition|?
name|SOAPBindingUtil
operator|.
name|getBindingStyle
argument_list|(
name|binding
argument_list|)
else|:
literal|""
decl_stmt|;
name|String
name|style
init|=
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|SOAPBindingUtil
operator|.
name|getSOAPOperationStyle
argument_list|(
name|bop
argument_list|)
argument_list|)
condition|?
name|bindingStyle
else|:
name|SOAPBindingUtil
operator|.
name|getSOAPOperationStyle
argument_list|(
name|bop
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"DOCUMENT"
operator|.
name|equalsIgnoreCase
argument_list|(
name|style
argument_list|)
operator|||
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|style
argument_list|)
condition|)
block|{
name|boolean
name|passed
init|=
name|checkR2201Input
argument_list|(
name|operation
argument_list|,
name|bop
argument_list|)
operator|&&
name|checkR2201Output
argument_list|(
name|operation
argument_list|,
name|bop
argument_list|)
operator|&&
name|checkR2716
argument_list|(
name|bop
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|passed
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|!
name|checkR2717AndR2726
argument_list|(
name|bop
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|boolean
name|isHeaderPart
parameter_list|(
specifier|final
name|BindingOperation
name|bop
parameter_list|,
specifier|final
name|Part
name|part
parameter_list|)
block|{
name|QName
name|elementName
init|=
name|part
operator|.
name|getElementName
argument_list|()
decl_stmt|;
if|if
condition|(
name|elementName
operator|!=
literal|null
condition|)
block|{
name|String
name|partName
init|=
name|elementName
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|SoapHeader
name|inSoapHeader
init|=
name|SOAPBindingUtil
operator|.
name|getBindingInputSOAPHeader
argument_list|(
name|bop
argument_list|)
decl_stmt|;
if|if
condition|(
name|inSoapHeader
operator|!=
literal|null
condition|)
block|{
return|return
name|partName
operator|.
name|equals
argument_list|(
name|inSoapHeader
operator|.
name|getPart
argument_list|()
argument_list|)
return|;
block|}
name|SoapHeader
name|outSoapHeader
init|=
name|SOAPBindingUtil
operator|.
name|getBindingOutputSOAPHeader
argument_list|(
name|bop
argument_list|)
decl_stmt|;
if|if
condition|(
name|outSoapHeader
operator|!=
literal|null
condition|)
block|{
return|return
name|partName
operator|.
name|equals
argument_list|(
name|outSoapHeader
operator|.
name|getPart
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|checkR2203And2204
parameter_list|()
block|{
name|Collection
argument_list|<
name|Binding
argument_list|>
name|bindings
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|def
operator|.
name|getBindings
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Binding
name|binding
range|:
name|bindings
control|)
block|{
name|String
name|style
init|=
name|SOAPBindingUtil
operator|.
name|getCanonicalBindingStyle
argument_list|(
name|binding
argument_list|)
decl_stmt|;
if|if
condition|(
name|binding
operator|.
name|getPortType
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|//
for|for
control|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|ite2
init|=
name|binding
operator|.
name|getPortType
argument_list|()
operator|.
name|getOperations
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|ite2
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Operation
name|operation
init|=
operator|(
name|Operation
operator|)
name|ite2
operator|.
name|next
argument_list|()
decl_stmt|;
name|BindingOperation
name|bop
init|=
name|wsdlHelper
operator|.
name|getBindingOperation
argument_list|(
name|def
argument_list|,
name|operation
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|operation
operator|.
name|getInput
argument_list|()
operator|!=
literal|null
operator|&&
name|operation
operator|.
name|getInput
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Message
name|inMess
init|=
name|operation
operator|.
name|getInput
argument_list|()
operator|.
name|getMessage
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|ite3
init|=
name|inMess
operator|.
name|getParts
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|ite3
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Part
name|p
init|=
operator|(
name|Part
operator|)
name|ite3
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|SOAPBinding
operator|.
name|Style
operator|.
name|RPC
operator|.
name|name
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|style
argument_list|)
operator|&&
name|p
operator|.
name|getTypeName
argument_list|()
operator|==
literal|null
operator|&&
operator|!
name|isHeaderPart
argument_list|(
name|bop
argument_list|,
name|p
argument_list|)
condition|)
block|{
name|addErrorMessage
argument_list|(
literal|"An rpc-literal binding in a DESCRIPTION MUST refer, "
operator|+
literal|"in its soapbind:body element(s), only to "
operator|+
literal|"wsdl:part element(s) that have been defined "
operator|+
literal|"using the type attribute."
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|SOAPBinding
operator|.
name|Style
operator|.
name|DOCUMENT
operator|.
name|name
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|style
argument_list|)
operator|&&
name|p
operator|.
name|getElementName
argument_list|()
operator|==
literal|null
condition|)
block|{
name|addErrorMessage
argument_list|(
literal|"A document-literal binding in a DESCRIPTION MUST refer, "
operator|+
literal|"in each of its soapbind:body element(s),"
operator|+
literal|"only to wsdl:part element(s)"
operator|+
literal|" that have been defined using the element attribute."
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
if|if
condition|(
name|operation
operator|.
name|getOutput
argument_list|()
operator|!=
literal|null
operator|&&
name|operation
operator|.
name|getOutput
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Message
name|outMess
init|=
name|operation
operator|.
name|getOutput
argument_list|()
operator|.
name|getMessage
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|ite3
init|=
name|outMess
operator|.
name|getParts
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|ite3
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Part
name|p
init|=
operator|(
name|Part
operator|)
name|ite3
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|style
operator|.
name|equalsIgnoreCase
argument_list|(
name|SOAPBinding
operator|.
name|Style
operator|.
name|RPC
operator|.
name|name
argument_list|()
argument_list|)
operator|&&
name|p
operator|.
name|getTypeName
argument_list|()
operator|==
literal|null
operator|&&
operator|!
name|isHeaderPart
argument_list|(
name|bop
argument_list|,
name|p
argument_list|)
condition|)
block|{
name|addErrorMessage
argument_list|(
literal|"An rpc-literal binding in a DESCRIPTION MUST refer, "
operator|+
literal|"in its soapbind:body element(s), only to "
operator|+
literal|"wsdl:part element(s) that have been defined "
operator|+
literal|"using the type attribute."
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|style
operator|.
name|equalsIgnoreCase
argument_list|(
name|SOAPBinding
operator|.
name|Style
operator|.
name|DOCUMENT
operator|.
name|name
argument_list|()
argument_list|)
operator|&&
name|p
operator|.
name|getElementName
argument_list|()
operator|==
literal|null
condition|)
block|{
name|addErrorMessage
argument_list|(
literal|"A document-literal binding in a DESCRIPTION MUST refer, "
operator|+
literal|"in each of its soapbind:body element(s),"
operator|+
literal|"only to wsdl:part element(s)"
operator|+
literal|" that have been defined using the element attribute."
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
comment|// TODO: Should also check SoapHeader/SoapHeaderFault
specifier|public
name|boolean
name|checkR2205
parameter_list|()
block|{
name|Collection
argument_list|<
name|Binding
argument_list|>
name|bindings
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|def
operator|.
name|getBindings
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Binding
name|binding
range|:
name|bindings
control|)
block|{
if|if
condition|(
operator|!
name|SOAPBindingUtil
operator|.
name|isSOAPBinding
argument_list|(
name|binding
argument_list|)
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"WSIBP Validator found<"
operator|+
name|binding
operator|.
name|getQName
argument_list|()
operator|+
literal|"> is NOT a SOAP binding"
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|binding
operator|.
name|getPortType
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|//will error later
continue|continue;
block|}
for|for
control|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|ite2
init|=
name|binding
operator|.
name|getPortType
argument_list|()
operator|.
name|getOperations
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|ite2
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Operation
name|operation
init|=
operator|(
name|Operation
operator|)
name|ite2
operator|.
name|next
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Fault
argument_list|>
name|faults
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|operation
operator|.
name|getFaults
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|CollectionUtils
operator|.
name|isEmpty
argument_list|(
name|faults
argument_list|)
condition|)
block|{
continue|continue;
block|}
for|for
control|(
name|Fault
name|fault
range|:
name|faults
control|)
block|{
name|Message
name|message
init|=
name|fault
operator|.
name|getMessage
argument_list|()
decl_stmt|;
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
name|message
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
name|getElementName
argument_list|()
operator|==
literal|null
condition|)
block|{
name|addErrorMessage
argument_list|(
name|getErrorPrefix
argument_list|(
literal|"WSI-BP-1.0 R2205"
argument_list|)
operator|+
literal|"In Message "
operator|+
name|message
operator|.
name|getQName
argument_list|()
operator|+
literal|", part "
operator|+
name|part
operator|.
name|getName
argument_list|()
operator|+
literal|" must specify a 'element' attribute"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|checkR2705
parameter_list|()
block|{
name|Collection
argument_list|<
name|Binding
argument_list|>
name|bindings
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|def
operator|.
name|getBindings
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Binding
name|binding
range|:
name|bindings
control|)
block|{
if|if
condition|(
name|SOAPBindingUtil
operator|.
name|isMixedStyle
argument_list|(
name|binding
argument_list|)
condition|)
block|{
name|addErrorMessage
argument_list|(
literal|"Mixed style, invalid WSDL"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|boolean
name|isOverloading
parameter_list|(
name|String
name|operationName
parameter_list|)
block|{
if|if
condition|(
name|operationMap
operator|.
name|contains
argument_list|(
name|operationName
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
name|operationMap
operator|.
name|add
argument_list|(
name|operationName
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|private
specifier|static
name|String
name|getErrorPrefix
parameter_list|(
name|String
name|ruleBroken
parameter_list|)
block|{
return|return
name|ruleBroken
operator|+
literal|" violation: "
return|;
block|}
block|}
end_class

end_unit

