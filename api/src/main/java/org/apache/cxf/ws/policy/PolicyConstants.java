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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Set
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
name|w3c
operator|.
name|dom
operator|.
name|Attr
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NamedNodeMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|DOMUtils
import|;
end_import

begin_comment
comment|/**  * Encapsulation of version-specific WS-Policy constants.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PolicyConstants
block|{
specifier|public
specifier|static
specifier|final
name|String
name|NAMESPACE_WS_POLICY
init|=
literal|"http://www.w3.org/ns/ws-policy"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAMESPACE_W3_200607
init|=
literal|"http://www.w3.org/2006/07/ws-policy"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAMESPACE_XMLSOAP_200409
init|=
literal|"http://schemas.xmlsoap.org/ws/2004/09/policy"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|POLICY_ELEM_NAME
init|=
literal|"Policy"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|POLICYREFERENCE_ELEM_NAME
init|=
literal|"PolicyReference"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|POLICYATTACHMENT_ELEM_NAME
init|=
literal|"PolicyAttachment"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|APPLIESTO_ELEM_NAME
init|=
literal|"AppliesTo"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSU_NAMESPACE_URI
init|=
literal|"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSU_ID_ATTR_NAME
init|=
literal|"Id"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_POLICY_OUT_INTERCEPTOR_ID
init|=
literal|"org.apache.cxf.ws.policy.ClientPolicyOutInterceptor"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_POLICY_IN_INTERCEPTOR_ID
init|=
literal|"org.apache.cxf.ws.policy.ClientPolicyInInterceptor"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_POLICY_IN_FAULT_INTERCEPTOR_ID
init|=
literal|"org.apache.cxf.ws.policy.ClientPolicyInFaultInterceptor"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SERVER_POLICY_IN_INTERCEPTOR_ID
init|=
literal|"org.apache.cxf.ws.policy.ServerPolicyInInterceptor"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SERVER_POLICY_OUT_INTERCEPTOR_ID
init|=
literal|"org.apache.cxf.ws.policy.ServerPolicyOutInterceptor"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SERVER_POLICY_OUT_FAULT_INTERCEPTOR_ID
init|=
literal|"org.apache.cxf.ws.policy.ServerPolicyOutFaultInterceptor"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_OUT_ASSERTIONS
init|=
literal|"org.apache.cxf.ws.policy.client.out.assertions"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_IN_ASSERTIONS
init|=
literal|"org.apache.cxf.ws.policy.client.in.assertions"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_INFAULT_ASSERTIONS
init|=
literal|"org.apache.cxf.ws.policy.client.infault.assertions"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SERVER_IN_ASSERTIONS
init|=
literal|"org.apache.cxf.ws.policy.server.in.assertions"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SERVER_OUT_ASSERTIONS
init|=
literal|"org.apache.cxf.ws.policy.server.out.assertions"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SERVER_OUTFAULT_ASSERTIONS
init|=
literal|"org.apache.cxf.ws.policy.server.outfault.assertions"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ALL_ELEM_NAME
init|=
literal|"All"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EXACTLYONE_ELEM_NAME
init|=
literal|"ExactlyOne"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OPTIONAL_ATTR_NAME
init|=
literal|"Optional"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|POLICYURIS_ATTR_NAME
init|=
literal|"PolicyURIs"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|SUPPORTED_NAMESPACES
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|SUPPORTED_NAMESPACES
operator|.
name|add
argument_list|(
name|NAMESPACE_WS_POLICY
argument_list|)
expr_stmt|;
name|SUPPORTED_NAMESPACES
operator|.
name|add
argument_list|(
name|NAMESPACE_W3_200607
argument_list|)
expr_stmt|;
name|SUPPORTED_NAMESPACES
operator|.
name|add
argument_list|(
name|NAMESPACE_XMLSOAP_200409
argument_list|)
expr_stmt|;
block|}
specifier|private
name|PolicyConstants
parameter_list|()
block|{
comment|//utility class
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|Element
argument_list|>
name|findAllPolicyElementsOfLocalName
parameter_list|(
name|Document
name|doc
parameter_list|,
name|String
name|localName
parameter_list|)
block|{
return|return
name|findAllPolicyElementsOfLocalName
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|,
name|localName
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|Element
argument_list|>
name|findAllPolicyElementsOfLocalName
parameter_list|(
name|Element
name|el
parameter_list|,
name|String
name|localName
parameter_list|)
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|ret
init|=
operator|new
name|LinkedList
argument_list|<
name|Element
argument_list|>
argument_list|()
decl_stmt|;
name|findAllPolicyElementsOfLocalName
argument_list|(
name|el
argument_list|,
name|localName
argument_list|,
name|ret
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
specifier|public
specifier|static
name|void
name|findAllPolicyElementsOfLocalName
parameter_list|(
name|Element
name|el
parameter_list|,
name|String
name|localName
parameter_list|,
name|List
argument_list|<
name|Element
argument_list|>
name|val
parameter_list|)
block|{
if|if
condition|(
name|localName
operator|.
name|equals
argument_list|(
name|el
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|SUPPORTED_NAMESPACES
operator|.
name|contains
argument_list|(
name|el
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|val
operator|.
name|add
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
name|el
operator|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|el
argument_list|)
expr_stmt|;
while|while
condition|(
name|el
operator|!=
literal|null
condition|)
block|{
name|findAllPolicyElementsOfLocalName
argument_list|(
name|el
argument_list|,
name|localName
argument_list|,
name|val
argument_list|)
expr_stmt|;
name|el
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|Attr
name|findOptionalAttribute
parameter_list|(
name|Element
name|e
parameter_list|)
block|{
name|NamedNodeMap
name|atts
init|=
name|e
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|atts
operator|.
name|getLength
argument_list|()
condition|;
name|x
operator|++
control|)
block|{
name|Attr
name|att
init|=
operator|(
name|Attr
operator|)
name|atts
operator|.
name|item
argument_list|(
name|x
argument_list|)
decl_stmt|;
name|QName
name|qn
init|=
operator|new
name|QName
argument_list|(
name|att
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|att
operator|.
name|getLocalName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|isOptionalAttribute
argument_list|(
name|qn
argument_list|)
condition|)
block|{
return|return
name|att
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|Element
name|findPolicyElement
parameter_list|(
name|Element
name|parent
parameter_list|)
block|{
name|Node
name|nd
init|=
name|parent
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|nd
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|POLICY_ELEM_NAME
operator|.
name|equals
argument_list|(
name|nd
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|SUPPORTED_NAMESPACES
operator|.
name|contains
argument_list|(
name|nd
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|(
name|Element
operator|)
name|nd
return|;
block|}
name|nd
operator|=
name|nd
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isOptionalAttribute
parameter_list|(
name|QName
name|qn
parameter_list|)
block|{
return|return
name|OPTIONAL_ATTR_NAME
operator|.
name|equals
argument_list|(
name|qn
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
name|SUPPORTED_NAMESPACES
operator|.
name|contains
argument_list|(
name|qn
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isPolicyElem
parameter_list|(
name|QName
name|qn
parameter_list|)
block|{
return|return
name|POLICY_ELEM_NAME
operator|.
name|equals
argument_list|(
name|qn
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
name|SUPPORTED_NAMESPACES
operator|.
name|contains
argument_list|(
name|qn
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isPolicyRefElem
parameter_list|(
name|QName
name|qn
parameter_list|)
block|{
return|return
name|POLICYREFERENCE_ELEM_NAME
operator|.
name|equals
argument_list|(
name|qn
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
name|SUPPORTED_NAMESPACES
operator|.
name|contains
argument_list|(
name|qn
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isAppliesToElem
parameter_list|(
name|QName
name|qn
parameter_list|)
block|{
return|return
name|APPLIESTO_ELEM_NAME
operator|.
name|equals
argument_list|(
name|qn
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
name|SUPPORTED_NAMESPACES
operator|.
name|contains
argument_list|(
name|qn
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isPolicyURIsAttr
parameter_list|(
name|QName
name|qn
parameter_list|)
block|{
return|return
name|POLICYURIS_ATTR_NAME
operator|.
name|equals
argument_list|(
name|qn
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
name|SUPPORTED_NAMESPACES
operator|.
name|contains
argument_list|(
name|qn
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isExactlyOne
parameter_list|(
name|QName
name|qn
parameter_list|)
block|{
return|return
name|EXACTLYONE_ELEM_NAME
operator|.
name|equals
argument_list|(
name|qn
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
name|SUPPORTED_NAMESPACES
operator|.
name|contains
argument_list|(
name|qn
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isAll
parameter_list|(
name|QName
name|qn
parameter_list|)
block|{
return|return
name|ALL_ELEM_NAME
operator|.
name|equals
argument_list|(
name|qn
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
name|SUPPORTED_NAMESPACES
operator|.
name|contains
argument_list|(
name|qn
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

