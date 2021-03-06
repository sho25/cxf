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
name|binding
operator|.
name|soap
package|;
end_package

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
name|LinkedList
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
name|java
operator|.
name|util
operator|.
name|ResourceBundle
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
name|interceptor
operator|.
name|Fault
import|;
end_import

begin_class
specifier|public
class|class
name|SoapFault
extends|extends
name|Fault
block|{
specifier|public
specifier|static
specifier|final
name|QName
name|ATTACHMENT_IO
init|=
operator|new
name|QName
argument_list|(
name|Soap12
operator|.
name|SOAP_NAMESPACE
argument_list|,
literal|"AttachmentIOError"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|5775857720028582429L
decl_stmt|;
comment|/**      * "The message was incorrectly formed or did not contain the appropriate      * information in order to succeed." -- SOAP 1.2 Spec      */
comment|/**      * A SOAP 1.2 only fault code.<p/> "The message could not be processed for      * reasons attributable to the processing of the message rather than to the      * contents of the message itself." -- SOAP 1.2 Spec<p/> If this message is      * used in a SOAP 1.1 Fault it will most likely (depending on the      * FaultHandler) be mapped to "Sender" instead.      */
specifier|private
name|List
argument_list|<
name|QName
argument_list|>
name|subCodes
decl_stmt|;
specifier|private
name|String
name|role
decl_stmt|;
specifier|private
name|String
name|node
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|SoapFault
parameter_list|(
name|Message
name|message
parameter_list|,
name|Throwable
name|throwable
parameter_list|,
name|QName
name|faultCode
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|throwable
argument_list|,
name|faultCode
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SoapFault
parameter_list|(
name|Message
name|message
parameter_list|,
name|QName
name|faultCode
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|faultCode
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SoapFault
parameter_list|(
name|String
name|message
parameter_list|,
name|QName
name|faultCode
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|Message
argument_list|(
name|message
argument_list|,
operator|(
name|ResourceBundle
operator|)
literal|null
argument_list|)
argument_list|,
name|faultCode
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SoapFault
parameter_list|(
name|String
name|message
parameter_list|,
name|ResourceBundle
name|bundle
parameter_list|,
name|QName
name|faultCode
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|Message
argument_list|(
name|message
argument_list|,
name|bundle
argument_list|)
argument_list|,
name|faultCode
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SoapFault
parameter_list|(
name|String
name|message
parameter_list|,
name|ResourceBundle
name|bundle
parameter_list|,
name|Throwable
name|t
parameter_list|,
name|QName
name|faultCode
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|Message
argument_list|(
name|message
argument_list|,
name|bundle
argument_list|)
argument_list|,
name|t
argument_list|,
name|faultCode
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SoapFault
parameter_list|(
name|String
name|message
parameter_list|,
name|ResourceBundle
name|bundle
parameter_list|,
name|QName
name|faultCode
parameter_list|,
name|Object
modifier|...
name|params
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|Message
argument_list|(
name|message
argument_list|,
name|bundle
argument_list|,
name|params
argument_list|)
argument_list|,
name|faultCode
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SoapFault
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|,
name|QName
name|faultCode
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|Message
argument_list|(
name|message
argument_list|,
operator|(
name|ResourceBundle
operator|)
literal|null
argument_list|)
argument_list|,
name|t
argument_list|,
name|faultCode
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getCodeString
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|defaultPrefix
parameter_list|)
block|{
return|return
name|getFaultCodeString
argument_list|(
name|prefix
argument_list|,
name|defaultPrefix
argument_list|,
name|getFaultCode
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|String
name|getSubCodeString
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|defaultPrefix
parameter_list|)
block|{
return|return
name|getFaultCodeString
argument_list|(
name|prefix
argument_list|,
name|defaultPrefix
argument_list|,
name|getRootSubCode
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|String
name|getFaultCodeString
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|defaultPrefix
parameter_list|,
name|QName
name|fCode
parameter_list|)
block|{
name|String
name|codePrefix
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|codePrefix
operator|=
name|fCode
operator|.
name|getPrefix
argument_list|()
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|codePrefix
argument_list|)
condition|)
block|{
name|codePrefix
operator|=
name|defaultPrefix
expr_stmt|;
block|}
block|}
else|else
block|{
name|codePrefix
operator|=
name|prefix
expr_stmt|;
block|}
return|return
name|codePrefix
operator|+
literal|":"
operator|+
name|fCode
operator|.
name|getLocalPart
argument_list|()
return|;
block|}
specifier|private
name|QName
name|getRootSubCode
parameter_list|()
block|{
return|return
name|subCodes
operator|!=
literal|null
operator|&&
operator|!
name|subCodes
operator|.
name|isEmpty
argument_list|()
condition|?
name|subCodes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
else|:
literal|null
return|;
block|}
specifier|private
name|void
name|setRootSubCode
parameter_list|(
name|QName
name|subCode
parameter_list|)
block|{
if|if
condition|(
name|subCodes
operator|==
literal|null
condition|)
block|{
name|subCodes
operator|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|subCodes
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|subCodes
operator|.
name|add
argument_list|(
name|subCode
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getReason
parameter_list|()
block|{
return|return
name|getMessage
argument_list|()
return|;
block|}
comment|/**      * Returns the fault actor.      *      * @return the fault actor.      */
specifier|public
name|String
name|getRole
parameter_list|()
block|{
return|return
name|role
return|;
block|}
comment|/**      * Sets the fault actor.      *      * @param actor the actor.      */
specifier|public
name|void
name|setRole
parameter_list|(
name|String
name|actor
parameter_list|)
block|{
name|this
operator|.
name|role
operator|=
name|actor
expr_stmt|;
block|}
specifier|public
name|String
name|getNode
parameter_list|()
block|{
return|return
name|node
return|;
block|}
specifier|public
name|void
name|setNode
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|this
operator|.
name|node
operator|=
name|n
expr_stmt|;
block|}
comment|/**      * Returns the SubCode for the Fault Code. If there are more than one Subcode entries      * in this fault, the first Subcode is returned.      *      * @return The SubCode element as detailed by the SOAP 1.2 spec.      */
specifier|public
name|QName
name|getSubCode
parameter_list|()
block|{
return|return
name|getRootSubCode
argument_list|()
return|;
block|}
comment|/**      * Returns the SubCode list for the Fault Code.      *      * @return The SubCode element list as detailed by the SOAP 1.2 spec.      */
specifier|public
name|List
argument_list|<
name|QName
argument_list|>
name|getSubCodes
parameter_list|()
block|{
return|return
name|subCodes
return|;
block|}
comment|/**      * Sets the SubCode for the Fault Code. If there are more than one Subcode entries      * in this fault, the first Subcode is set while the other entries are removed.      *      * @param subCode The SubCode element as detailed by the SOAP 1.2 spec.      */
specifier|public
name|void
name|setSubCode
parameter_list|(
name|QName
name|subCode
parameter_list|)
block|{
name|setRootSubCode
argument_list|(
name|subCode
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the SubCode list for the Fault Code.      *      * @param subCodes The SubCode element list as detailed by the SOAP 1.2 spec.      */
specifier|public
name|void
name|setSubCodes
parameter_list|(
name|List
argument_list|<
name|QName
argument_list|>
name|subCodes
parameter_list|)
block|{
name|this
operator|.
name|subCodes
operator|=
name|subCodes
expr_stmt|;
block|}
comment|/**      * Appends the SubCode to the SubCode list.      *      * @param subCode The SubCode element as detailed by the SOAP 1.2 spec.      */
specifier|public
name|void
name|addSubCode
parameter_list|(
name|QName
name|subCode
parameter_list|)
block|{
if|if
condition|(
name|subCodes
operator|==
literal|null
condition|)
block|{
name|subCodes
operator|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|subCodes
operator|.
name|add
argument_list|(
name|subCode
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getNamespaces
parameter_list|()
block|{
return|return
name|namespaces
return|;
block|}
specifier|public
name|void
name|setNamespaces
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
block|{
name|this
operator|.
name|namespaces
operator|=
name|namespaces
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|updateSoap12FaultCodes
parameter_list|(
name|SoapFault
name|f
parameter_list|)
block|{
comment|//per Soap 1.2 spec, the fault code MUST be one of the 5 values specified in the spec.
comment|//Soap 1.1 allows the soap fault code to be arbitrary (recommends the 4 values in the spec, but
comment|//explicitely mentions that it can be extended to include additional codes).   Soap 1.2 however
comment|//requires the use of one of the 5 defined codes.  Additional detail or more specific information
comment|//can be transferred via the SubCodes.
name|QName
name|fc
init|=
name|f
operator|.
name|getFaultCode
argument_list|()
decl_stmt|;
name|SoapVersion
name|v
init|=
name|Soap12
operator|.
name|getInstance
argument_list|()
decl_stmt|;
if|if
condition|(
name|fc
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|Soap12
operator|.
name|SOAP_NAMESPACE
argument_list|)
operator|&&
operator|(
name|fc
operator|.
name|equals
argument_list|(
name|v
operator|.
name|getReceiver
argument_list|()
argument_list|)
operator|||
name|fc
operator|.
name|equals
argument_list|(
name|v
operator|.
name|getSender
argument_list|()
argument_list|)
operator|||
name|fc
operator|.
name|equals
argument_list|(
name|v
operator|.
name|getMustUnderstand
argument_list|()
argument_list|)
operator|||
name|fc
operator|.
name|equals
argument_list|(
name|v
operator|.
name|getDateEncodingUnknown
argument_list|()
argument_list|)
operator|||
name|fc
operator|.
name|equals
argument_list|(
name|v
operator|.
name|getVersionMismatch
argument_list|()
argument_list|)
operator|)
condition|)
block|{
comment|//valid fault codes, don't change anything
return|return;
block|}
name|f
operator|.
name|setFaultCode
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
operator|.
name|getReceiver
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|f
operator|.
name|getSubCodes
argument_list|()
operator|==
literal|null
condition|)
block|{
name|f
operator|.
name|setRootSubCode
argument_list|(
name|fc
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|f
operator|.
name|getSubCodes
argument_list|()
operator|.
name|contains
argument_list|(
name|fc
argument_list|)
condition|)
block|{
name|f
operator|.
name|getSubCodes
argument_list|()
operator|.
name|add
argument_list|(
name|fc
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|SoapFault
name|createFault
parameter_list|(
name|Fault
name|f
parameter_list|,
name|SoapVersion
name|v
parameter_list|)
block|{
if|if
condition|(
name|f
operator|instanceof
name|SoapFault
condition|)
block|{
comment|//make sure the fault code is per spec
comment|//if it's one of our internal codes, map it to the proper soap code
if|if
condition|(
name|f
operator|.
name|getFaultCode
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|Fault
operator|.
name|FAULT_CODE_CLIENT
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|QName
name|fc
init|=
name|f
operator|.
name|getFaultCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|Fault
operator|.
name|FAULT_CODE_CLIENT
operator|.
name|equals
argument_list|(
name|fc
argument_list|)
condition|)
block|{
name|fc
operator|=
name|v
operator|.
name|getSender
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Fault
operator|.
name|FAULT_CODE_SERVER
operator|.
name|equals
argument_list|(
name|fc
argument_list|)
condition|)
block|{
name|fc
operator|=
name|v
operator|.
name|getReceiver
argument_list|()
expr_stmt|;
block|}
name|f
operator|.
name|setFaultCode
argument_list|(
name|fc
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|v
operator|==
name|Soap12
operator|.
name|getInstance
argument_list|()
condition|)
block|{
name|updateSoap12FaultCodes
argument_list|(
operator|(
name|SoapFault
operator|)
name|f
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|SoapFault
operator|)
name|f
return|;
block|}
name|QName
name|fc
init|=
name|f
operator|.
name|getFaultCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|Fault
operator|.
name|FAULT_CODE_CLIENT
operator|.
name|equals
argument_list|(
name|fc
argument_list|)
condition|)
block|{
name|fc
operator|=
name|v
operator|.
name|getSender
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Fault
operator|.
name|FAULT_CODE_SERVER
operator|.
name|equals
argument_list|(
name|fc
argument_list|)
condition|)
block|{
name|fc
operator|=
name|v
operator|.
name|getReceiver
argument_list|()
expr_stmt|;
block|}
name|SoapFault
name|soapFault
init|=
operator|new
name|SoapFault
argument_list|(
operator|new
name|Message
argument_list|(
name|f
operator|.
name|getMessage
argument_list|()
argument_list|,
operator|(
name|ResourceBundle
operator|)
literal|null
argument_list|)
argument_list|,
name|f
operator|.
name|getCause
argument_list|()
argument_list|,
name|fc
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|==
name|Soap12
operator|.
name|getInstance
argument_list|()
condition|)
block|{
name|updateSoap12FaultCodes
argument_list|(
name|soapFault
argument_list|)
expr_stmt|;
block|}
name|soapFault
operator|.
name|setDetail
argument_list|(
name|f
operator|.
name|getDetail
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|soapFault
return|;
block|}
block|}
end_class

end_unit

