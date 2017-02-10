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
name|ws
operator|.
name|policy
operator|.
name|attachment
operator|.
name|wsdl11
package|;
end_package

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
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|extensions
operator|.
name|UnknownExtensibilityElement
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
name|Bus
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
name|injection
operator|.
name|NoJSR250Annotations
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
name|logging
operator|.
name|LogUtils
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
name|message
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
name|service
operator|.
name|model
operator|.
name|AbstractDescriptionElement
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
name|service
operator|.
name|model
operator|.
name|BindingFaultInfo
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
name|service
operator|.
name|model
operator|.
name|BindingMessageInfo
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
name|service
operator|.
name|model
operator|.
name|BindingOperationInfo
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
name|service
operator|.
name|model
operator|.
name|DescriptionInfo
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|service
operator|.
name|model
operator|.
name|Extensible
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
name|service
operator|.
name|model
operator|.
name|FaultInfo
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
name|service
operator|.
name|model
operator|.
name|MessageInfo
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
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
name|ws
operator|.
name|policy
operator|.
name|PolicyConstants
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
name|ws
operator|.
name|policy
operator|.
name|attachment
operator|.
name|AbstractPolicyProvider
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
name|ws
operator|.
name|policy
operator|.
name|attachment
operator|.
name|reference
operator|.
name|LocalServiceModelReferenceResolver
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
name|ws
operator|.
name|policy
operator|.
name|attachment
operator|.
name|reference
operator|.
name|ReferenceResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Policy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|PolicyReference
import|;
end_import

begin_comment
comment|/**  * PolicyAttachmentManager provides methods to retrieve element policies and  * calculate effective policies based on the policy subject's scope.  *   */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|Wsdl11AttachmentPolicyProvider
extends|extends
name|AbstractPolicyProvider
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|Wsdl11AttachmentPolicyProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|Wsdl11AttachmentPolicyProvider
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Wsdl11AttachmentPolicyProvider
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|super
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Policy
name|getEffectivePolicy
parameter_list|(
name|ServiceInfo
name|si
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
return|return
name|getElementPolicy
argument_list|(
name|si
argument_list|)
return|;
block|}
specifier|private
name|Policy
name|mergePolicies
parameter_list|(
name|Policy
name|p1
parameter_list|,
name|Policy
name|p2
parameter_list|)
block|{
if|if
condition|(
name|p1
operator|==
literal|null
condition|)
block|{
return|return
name|p2
return|;
block|}
elseif|else
if|if
condition|(
name|p2
operator|==
literal|null
condition|)
block|{
return|return
name|p1
return|;
block|}
return|return
name|p1
operator|.
name|merge
argument_list|(
name|p2
argument_list|)
return|;
block|}
comment|/**      * The effective policy for a WSDL endpoint policy subject includes the element policy of the       * wsdl11:port element that defines the endpoint merged with the element policy of the      * referenced wsdl11:binding element and the element policy of the referenced wsdl11:portType      * element that defines the interface of the endpoint.       *       * @param ei the EndpointInfo object identifying the endpoint      * @return the effective policy      */
specifier|public
name|Policy
name|getEffectivePolicy
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|Policy
name|p
init|=
name|getElementPolicy
argument_list|(
name|ei
argument_list|)
decl_stmt|;
name|p
operator|=
name|mergePolicies
argument_list|(
name|p
argument_list|,
name|getElementPolicy
argument_list|(
name|ei
operator|.
name|getBinding
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|=
name|mergePolicies
argument_list|(
name|p
argument_list|,
name|getElementPolicy
argument_list|(
name|ei
operator|.
name|getInterface
argument_list|()
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|p
return|;
block|}
comment|/**      * The effective policy for a WSDL operation policy subject is calculated in relation to a       * specific port, and includes the element policy of the wsdl11:portType/wsdl11:operation       * element that defines the operation merged with that of the corresponding       * wsdl11:binding/wsdl11:operation element.      *       * @param bi the BindingOperationInfo identifying the operation in relation to a port      * @return the effective policy      */
specifier|public
name|Policy
name|getEffectivePolicy
parameter_list|(
name|BindingOperationInfo
name|bi
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|DescriptionInfo
name|di
init|=
name|bi
operator|.
name|getBinding
argument_list|()
operator|.
name|getDescription
argument_list|()
decl_stmt|;
name|Policy
name|p
init|=
name|getElementPolicy
argument_list|(
name|bi
argument_list|,
literal|false
argument_list|,
name|di
argument_list|)
decl_stmt|;
name|p
operator|=
name|mergePolicies
argument_list|(
name|p
argument_list|,
name|getElementPolicy
argument_list|(
name|bi
operator|.
name|getOperationInfo
argument_list|()
argument_list|,
literal|false
argument_list|,
name|di
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|p
return|;
block|}
comment|/**      * The effective policy for a specific WSDL message (input or output) is calculated      * in relation to a specific port, and includes the element policy of the wsdl:message      * element that defines the message's type merged with the element policy of the       * wsdl11:binding and wsdl11:portType message definitions that describe the message.      * For example, the effective policy of a specific input message for a specific port      * would be the (element policies of the) wsdl11:message element defining the message type,      * the wsdl11:portType/wsdl11:operation/wsdl11:input element and the corresponding      * wsdl11:binding/wsdl11:operation/wsdl11:input element for that message.      *       * @param bmi the BindingMessageInfo identifiying the message      * @return the effective policy      */
specifier|public
name|Policy
name|getEffectivePolicy
parameter_list|(
name|BindingMessageInfo
name|bmi
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|ServiceInfo
name|si
init|=
name|bmi
operator|.
name|getBindingOperation
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|DescriptionInfo
name|di
init|=
name|si
operator|.
name|getDescription
argument_list|()
decl_stmt|;
name|Policy
name|p
init|=
name|getElementPolicy
argument_list|(
name|bmi
argument_list|,
literal|false
argument_list|,
name|di
argument_list|)
decl_stmt|;
name|MessageInfo
name|mi
init|=
name|bmi
operator|.
name|getMessageInfo
argument_list|()
decl_stmt|;
name|p
operator|=
name|mergePolicies
argument_list|(
name|p
argument_list|,
name|getElementPolicy
argument_list|(
name|mi
argument_list|,
literal|true
argument_list|,
name|di
argument_list|)
argument_list|)
expr_stmt|;
name|Extensible
name|ex
init|=
name|getMessageTypeInfo
argument_list|(
name|mi
operator|.
name|getName
argument_list|()
argument_list|,
name|di
argument_list|)
decl_stmt|;
name|p
operator|=
name|mergePolicies
argument_list|(
name|p
argument_list|,
name|getElementPolicy
argument_list|(
name|ex
argument_list|,
literal|false
argument_list|,
name|di
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|p
return|;
block|}
specifier|public
name|Policy
name|getEffectivePolicy
parameter_list|(
name|BindingFaultInfo
name|bfi
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|ServiceInfo
name|si
init|=
name|bfi
operator|.
name|getBindingOperation
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|DescriptionInfo
name|di
init|=
name|si
operator|.
name|getDescription
argument_list|()
decl_stmt|;
name|Policy
name|p
init|=
name|getElementPolicy
argument_list|(
name|bfi
argument_list|,
literal|false
argument_list|,
name|di
argument_list|)
decl_stmt|;
name|FaultInfo
name|fi
init|=
name|bfi
operator|.
name|getFaultInfo
argument_list|()
decl_stmt|;
name|p
operator|=
name|mergePolicies
argument_list|(
name|p
argument_list|,
name|getElementPolicy
argument_list|(
name|fi
argument_list|,
literal|true
argument_list|,
name|di
argument_list|)
argument_list|)
expr_stmt|;
name|Extensible
name|ex
init|=
name|getMessageTypeInfo
argument_list|(
name|fi
operator|.
name|getName
argument_list|()
argument_list|,
name|di
argument_list|)
decl_stmt|;
name|p
operator|=
name|mergePolicies
argument_list|(
name|p
argument_list|,
name|getElementPolicy
argument_list|(
name|ex
argument_list|,
literal|false
argument_list|,
name|di
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|p
return|;
block|}
name|Policy
name|getElementPolicy
parameter_list|(
name|AbstractDescriptionElement
name|adh
parameter_list|)
block|{
return|return
name|getElementPolicy
argument_list|(
name|adh
argument_list|,
literal|false
argument_list|)
return|;
block|}
name|Policy
name|getElementPolicy
parameter_list|(
name|AbstractDescriptionElement
name|adh
parameter_list|,
name|boolean
name|includeAttributes
parameter_list|)
block|{
if|if
condition|(
name|adh
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|getElementPolicy
argument_list|(
name|adh
argument_list|,
name|includeAttributes
argument_list|,
name|adh
operator|.
name|getDescription
argument_list|()
argument_list|)
return|;
block|}
name|Policy
name|getElementPolicy
parameter_list|(
name|Extensible
name|ex
parameter_list|,
name|boolean
name|includeAttributes
parameter_list|,
name|DescriptionInfo
name|di
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|ex
operator|||
literal|null
operator|==
name|di
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|di
operator|.
name|getProperty
argument_list|(
literal|"registeredPolicy"
argument_list|)
operator|==
literal|null
condition|)
block|{
name|List
argument_list|<
name|UnknownExtensibilityElement
argument_list|>
name|diext
init|=
name|di
operator|.
name|getExtensors
argument_list|(
name|UnknownExtensibilityElement
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|diext
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|UnknownExtensibilityElement
name|e
range|:
name|diext
control|)
block|{
name|String
name|uri
init|=
name|e
operator|.
name|getElement
argument_list|()
operator|.
name|getAttributeNS
argument_list|(
name|PolicyConstants
operator|.
name|WSU_NAMESPACE_URI
argument_list|,
name|PolicyConstants
operator|.
name|WSU_ID_ATTR_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|Constants
operator|.
name|isPolicyElement
argument_list|(
name|e
operator|.
name|getElementType
argument_list|()
argument_list|)
operator|&&
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|String
name|id
init|=
operator|(
name|di
operator|.
name|getBaseURI
argument_list|()
operator|==
literal|null
condition|?
name|Integer
operator|.
name|toString
argument_list|(
name|di
operator|.
name|hashCode
argument_list|()
argument_list|)
else|:
name|di
operator|.
name|getBaseURI
argument_list|()
operator|)
operator|+
literal|"#"
operator|+
name|uri
decl_stmt|;
name|Policy
name|policy
init|=
name|registry
operator|.
name|lookup
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|policy
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|policy
operator|=
name|builder
operator|.
name|getPolicy
argument_list|(
name|e
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|fragement
init|=
literal|"#"
operator|+
name|uri
decl_stmt|;
name|registry
operator|.
name|register
argument_list|(
name|fragement
argument_list|,
name|policy
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
name|id
argument_list|,
name|policy
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|policyEx
parameter_list|)
block|{
comment|//ignore the policy can not be built
name|LOG
operator|.
name|warning
argument_list|(
literal|"Failed to build the policy '"
operator|+
name|uri
operator|+
literal|"':"
operator|+
name|policyEx
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
name|di
operator|.
name|setProperty
argument_list|(
literal|"registeredPolicy"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|Policy
name|elementPolicy
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|UnknownExtensibilityElement
argument_list|>
name|extensions
init|=
name|ex
operator|.
name|getExtensors
argument_list|(
name|UnknownExtensibilityElement
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|extensions
condition|)
block|{
for|for
control|(
name|UnknownExtensibilityElement
name|e
range|:
name|extensions
control|)
block|{
name|Policy
name|p
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|Constants
operator|.
name|isPolicyElement
argument_list|(
name|e
operator|.
name|getElementType
argument_list|()
argument_list|)
condition|)
block|{
name|p
operator|=
name|builder
operator|.
name|getPolicy
argument_list|(
name|e
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Constants
operator|.
name|isPolicyRef
argument_list|(
name|e
operator|.
name|getElementType
argument_list|()
argument_list|)
condition|)
block|{
name|PolicyReference
name|ref
init|=
name|builder
operator|.
name|getPolicyReference
argument_list|(
name|e
operator|.
name|getElement
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|ref
condition|)
block|{
name|p
operator|=
name|resolveReference
argument_list|(
name|ref
argument_list|,
name|di
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
literal|null
operator|!=
name|p
condition|)
block|{
if|if
condition|(
name|elementPolicy
operator|==
literal|null
condition|)
block|{
name|elementPolicy
operator|=
operator|new
name|Policy
argument_list|()
expr_stmt|;
block|}
name|elementPolicy
operator|=
name|elementPolicy
operator|.
name|merge
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|includeAttributes
operator|&&
name|ex
operator|.
name|getExtensionAttributes
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
name|ent
range|:
name|ex
operator|.
name|getExtensionAttributes
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|Constants
operator|.
name|isPolicyURIsAttr
argument_list|(
name|ent
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|Object
name|attr
init|=
name|ent
operator|.
name|getValue
argument_list|()
decl_stmt|;
comment|// can be of type a String, a QName, a list of Srings or a list of QNames
name|String
name|uris
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|attr
operator|instanceof
name|QName
condition|)
block|{
name|uris
operator|=
operator|(
operator|(
name|QName
operator|)
name|attr
operator|)
operator|.
name|getLocalPart
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|attr
operator|instanceof
name|String
condition|)
block|{
name|uris
operator|=
operator|(
name|String
operator|)
name|attr
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|uris
condition|)
block|{
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|uris
argument_list|)
decl_stmt|;
while|while
condition|(
name|st
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|uri
init|=
name|st
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|PolicyReference
name|ref
init|=
operator|new
name|PolicyReference
argument_list|()
decl_stmt|;
name|ref
operator|.
name|setURI
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|Policy
name|p
init|=
name|resolveReference
argument_list|(
name|ref
argument_list|,
name|di
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|p
condition|)
block|{
name|elementPolicy
operator|=
name|elementPolicy
operator|==
literal|null
condition|?
operator|new
name|Policy
argument_list|()
operator|.
name|merge
argument_list|(
name|p
argument_list|)
else|:
name|elementPolicy
operator|.
name|merge
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
return|return
name|elementPolicy
return|;
block|}
name|Policy
name|resolveReference
parameter_list|(
name|PolicyReference
name|ref
parameter_list|,
name|DescriptionInfo
name|di
parameter_list|)
block|{
name|Policy
name|p
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isExternal
argument_list|(
name|ref
argument_list|)
condition|)
block|{
name|String
name|uri
init|=
name|di
operator|.
name|getBaseURI
argument_list|()
decl_stmt|;
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
name|uri
operator|=
name|Integer
operator|.
name|toString
argument_list|(
name|di
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|p
operator|=
name|resolveExternal
argument_list|(
name|ref
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|p
operator|=
name|resolveLocal
argument_list|(
name|ref
argument_list|,
name|di
argument_list|)
expr_stmt|;
block|}
name|checkResolved
argument_list|(
name|ref
argument_list|,
name|p
argument_list|)
expr_stmt|;
return|return
name|p
return|;
block|}
name|Policy
name|resolveLocal
parameter_list|(
name|PolicyReference
name|ref
parameter_list|,
name|DescriptionInfo
name|di
parameter_list|)
block|{
name|String
name|uri
init|=
name|ref
operator|.
name|getURI
argument_list|()
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|absoluteURI
init|=
name|di
operator|.
name|getBaseURI
argument_list|()
decl_stmt|;
if|if
condition|(
name|absoluteURI
operator|==
literal|null
condition|)
block|{
name|absoluteURI
operator|=
name|Integer
operator|.
name|toString
argument_list|(
name|di
operator|.
name|hashCode
argument_list|()
argument_list|)
operator|+
name|ref
operator|.
name|getURI
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|absoluteURI
operator|=
name|absoluteURI
operator|+
name|ref
operator|.
name|getURI
argument_list|()
expr_stmt|;
block|}
name|Policy
name|resolved
init|=
name|registry
operator|.
name|lookup
argument_list|(
name|absoluteURI
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|resolved
condition|)
block|{
return|return
name|resolved
return|;
block|}
name|ReferenceResolver
name|resolver
init|=
operator|new
name|LocalServiceModelReferenceResolver
argument_list|(
name|di
argument_list|,
name|builder
argument_list|)
decl_stmt|;
name|resolved
operator|=
name|resolver
operator|.
name|resolveReference
argument_list|(
name|uri
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|resolved
condition|)
block|{
name|ref
operator|.
name|setURI
argument_list|(
name|absoluteURI
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
name|absoluteURI
argument_list|,
name|resolved
argument_list|)
expr_stmt|;
block|}
return|return
name|resolved
return|;
block|}
specifier|private
name|Extensible
name|getMessageTypeInfo
parameter_list|(
name|QName
name|name
parameter_list|,
name|DescriptionInfo
name|di
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|di
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Definition
name|def
init|=
operator|(
name|Definition
operator|)
name|di
operator|.
name|getProperty
argument_list|(
literal|"org.apache.cxf.wsdl11.WSDLServiceBuilder.DEFINITION"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|def
condition|)
block|{
return|return
literal|null
return|;
block|}
name|javax
operator|.
name|wsdl
operator|.
name|Message
name|m
init|=
name|def
operator|.
name|getMessage
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|m
condition|)
block|{
name|List
argument_list|<
name|ExtensibilityElement
argument_list|>
name|extensors
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|m
operator|.
name|getExtensibilityElements
argument_list|()
argument_list|,
name|ExtensibilityElement
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|extensors
condition|)
block|{
return|return
operator|new
name|ExtensibleInfo
argument_list|(
name|extensors
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
class|class
name|ExtensibleInfo
implements|implements
name|Extensible
block|{
specifier|private
name|List
argument_list|<
name|ExtensibilityElement
argument_list|>
name|extensors
decl_stmt|;
name|ExtensibleInfo
parameter_list|(
name|List
argument_list|<
name|ExtensibilityElement
argument_list|>
name|e
parameter_list|)
block|{
name|extensors
operator|=
name|e
expr_stmt|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getExtensor
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
for|for
control|(
name|ExtensibilityElement
name|e
range|:
name|extensors
control|)
block|{
if|if
condition|(
name|cls
operator|.
name|isInstance
argument_list|(
name|e
argument_list|)
condition|)
block|{
return|return
name|cls
operator|.
name|cast
argument_list|(
name|e
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|getExtensors
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|extensors
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|T
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|extensors
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ExtensibilityElement
name|e
range|:
name|extensors
control|)
block|{
if|if
condition|(
name|cls
operator|.
name|isInstance
argument_list|(
name|e
argument_list|)
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|cls
operator|.
name|cast
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|list
return|;
block|}
specifier|public
name|void
name|addExtensionAttribute
parameter_list|(
name|QName
name|arg0
parameter_list|,
name|Object
name|arg1
parameter_list|)
block|{             }
specifier|public
name|void
name|addExtensor
parameter_list|(
name|Object
name|arg0
parameter_list|)
block|{            }
specifier|public
name|Object
name|getExtensionAttribute
parameter_list|(
name|QName
name|arg0
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Map
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
name|getExtensionAttributes
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setExtensionAttributes
parameter_list|(
name|Map
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
name|arg0
parameter_list|)
block|{           }
block|}
block|}
end_class

end_unit

