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
name|WSU_NAMESPACE_URI
init|=
name|Constants
operator|.
name|URI_WSU_NS
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSU_ID_ATTR_NAME
init|=
name|Constants
operator|.
name|ATTR_ID
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|POLICY_OVERRIDE
init|=
literal|"org.apache.cxf.ws.policy.override"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|POLICY_IN_INTERCEPTOR_ID
init|=
literal|"org.apache.cxf.ws.policy.PolicyInInterceptor"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|POLICY_OUT_INTERCEPTOR_ID
init|=
literal|"org.apache.cxf.ws.policy.PolicyOutInterceptor"
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
argument_list|<>
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
name|QName
name|qn
init|=
name|DOMUtils
operator|.
name|getElementQName
argument_list|(
name|el
argument_list|)
decl_stmt|;
if|if
condition|(
name|localName
operator|.
name|equals
argument_list|(
name|qn
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
name|Constants
operator|.
name|isInPolicyNS
argument_list|(
name|qn
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
name|boolean
name|isOptional
parameter_list|(
name|Element
name|e
parameter_list|)
block|{
name|Attr
name|at
init|=
name|findOptionalAttribute
argument_list|(
name|e
argument_list|)
decl_stmt|;
if|if
condition|(
name|at
operator|!=
literal|null
condition|)
block|{
name|String
name|v
init|=
name|at
operator|.
name|getValue
argument_list|()
decl_stmt|;
return|return
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|v
argument_list|)
operator|||
literal|"1"
operator|.
name|equals
argument_list|(
name|v
argument_list|)
return|;
block|}
return|return
literal|false
return|;
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
name|Constants
operator|.
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
name|boolean
name|isIgnorable
parameter_list|(
name|Element
name|e
parameter_list|)
block|{
name|Attr
name|at
init|=
name|findIgnorableAttribute
argument_list|(
name|e
argument_list|)
decl_stmt|;
if|if
condition|(
name|at
operator|!=
literal|null
condition|)
block|{
name|String
name|v
init|=
name|at
operator|.
name|getValue
argument_list|()
decl_stmt|;
return|return
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|v
argument_list|)
operator|||
literal|"1"
operator|.
name|equals
argument_list|(
name|v
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
specifier|static
name|Attr
name|findIgnorableAttribute
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
name|Constants
operator|.
name|isIgnorableAttribute
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
name|nd
operator|instanceof
name|Element
condition|)
block|{
name|QName
name|qn
init|=
name|DOMUtils
operator|.
name|getElementQName
argument_list|(
operator|(
name|Element
operator|)
name|nd
argument_list|)
decl_stmt|;
if|if
condition|(
name|Constants
operator|.
name|isPolicyElement
argument_list|(
name|qn
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
block|}
end_class

end_unit

