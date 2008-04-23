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
name|extension
operator|.
name|BusExtension
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
implements|implements
name|BusExtension
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
name|String
name|namespaceURI
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|POLICY_ELEM_NAME
init|=
literal|"Policy"
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
name|POLICYREFERENCE_ELEM_NAME
init|=
literal|"PolicyReference"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|POLICYATTACHMENT_ELEM_NAME
init|=
literal|"PolicyAttachment"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|APPLIESTO_ELEM_NAME
init|=
literal|"AppliesTo"
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
name|QName
name|policyElemQName
decl_stmt|;
specifier|private
specifier|static
name|QName
name|allElemQName
decl_stmt|;
specifier|private
specifier|static
name|QName
name|exactlyOneElemQName
decl_stmt|;
specifier|private
specifier|static
name|QName
name|policyReferenceElemQName
decl_stmt|;
specifier|private
specifier|static
name|QName
name|policyAttachmentElemQName
decl_stmt|;
specifier|private
specifier|static
name|QName
name|appliesToElemQName
decl_stmt|;
specifier|private
specifier|static
name|QName
name|optionalAttrQName
decl_stmt|;
specifier|private
specifier|static
name|QName
name|policyURIsAttrQName
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WSU_NAMESPACE_URI
init|=
literal|"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WSU_ID_ATTR_NAME
init|=
literal|"Id"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|WSU_ID_ATTR_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSU_NAMESPACE_URI
argument_list|,
name|WSU_ID_ATTR_NAME
argument_list|)
decl_stmt|;
specifier|public
name|PolicyConstants
parameter_list|()
block|{
name|setNamespace
argument_list|(
name|NAMESPACE_WS_POLICY
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getRegistrationType
parameter_list|()
block|{
return|return
name|PolicyConstants
operator|.
name|class
return|;
block|}
specifier|public
name|void
name|setNamespace
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|namespaceURI
operator|=
name|uri
expr_stmt|;
comment|// update qnames
name|policyElemQName
operator|=
operator|new
name|QName
argument_list|(
name|namespaceURI
argument_list|,
name|POLICY_ELEM_NAME
argument_list|)
expr_stmt|;
name|allElemQName
operator|=
operator|new
name|QName
argument_list|(
name|namespaceURI
argument_list|,
name|ALL_ELEM_NAME
argument_list|)
expr_stmt|;
name|exactlyOneElemQName
operator|=
operator|new
name|QName
argument_list|(
name|namespaceURI
argument_list|,
name|EXACTLYONE_ELEM_NAME
argument_list|)
expr_stmt|;
name|policyReferenceElemQName
operator|=
operator|new
name|QName
argument_list|(
name|namespaceURI
argument_list|,
name|POLICYREFERENCE_ELEM_NAME
argument_list|)
expr_stmt|;
name|policyAttachmentElemQName
operator|=
operator|new
name|QName
argument_list|(
name|namespaceURI
argument_list|,
name|POLICYATTACHMENT_ELEM_NAME
argument_list|)
expr_stmt|;
name|appliesToElemQName
operator|=
operator|new
name|QName
argument_list|(
name|namespaceURI
argument_list|,
name|APPLIESTO_ELEM_NAME
argument_list|)
expr_stmt|;
name|optionalAttrQName
operator|=
operator|new
name|QName
argument_list|(
name|namespaceURI
argument_list|,
name|OPTIONAL_ATTR_NAME
argument_list|)
expr_stmt|;
name|policyURIsAttrQName
operator|=
operator|new
name|QName
argument_list|(
name|namespaceURI
argument_list|,
name|POLICYURIS_ATTR_NAME
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespaceURI
return|;
block|}
specifier|public
name|String
name|getWSUNamespace
parameter_list|()
block|{
return|return
name|WSU_NAMESPACE_URI
return|;
block|}
specifier|public
name|String
name|getPolicyElemName
parameter_list|()
block|{
return|return
name|POLICY_ELEM_NAME
return|;
block|}
specifier|public
name|String
name|getAllElemName
parameter_list|()
block|{
return|return
name|ALL_ELEM_NAME
return|;
block|}
specifier|public
name|String
name|getExactlyOneElemName
parameter_list|()
block|{
return|return
name|EXACTLYONE_ELEM_NAME
return|;
block|}
specifier|public
name|String
name|getPolicyReferenceElemName
parameter_list|()
block|{
return|return
name|POLICYREFERENCE_ELEM_NAME
return|;
block|}
specifier|public
name|String
name|getPolicyAttachmentElemName
parameter_list|()
block|{
return|return
name|POLICYATTACHMENT_ELEM_NAME
return|;
block|}
specifier|public
name|String
name|getAppliesToElemName
parameter_list|()
block|{
return|return
name|APPLIESTO_ELEM_NAME
return|;
block|}
specifier|public
name|String
name|getOptionalAttrName
parameter_list|()
block|{
return|return
name|OPTIONAL_ATTR_NAME
return|;
block|}
specifier|public
name|String
name|getPolicyURIsAttrName
parameter_list|()
block|{
return|return
name|POLICYURIS_ATTR_NAME
return|;
block|}
specifier|public
name|String
name|getIdAttrName
parameter_list|()
block|{
return|return
name|WSU_ID_ATTR_NAME
return|;
block|}
specifier|public
name|QName
name|getPolicyElemQName
parameter_list|()
block|{
return|return
name|policyElemQName
return|;
block|}
specifier|public
name|QName
name|getAllElemQName
parameter_list|()
block|{
return|return
name|allElemQName
return|;
block|}
specifier|public
name|QName
name|getExactlyOneElemQName
parameter_list|()
block|{
return|return
name|exactlyOneElemQName
return|;
block|}
specifier|public
name|QName
name|getPolicyReferenceElemQName
parameter_list|()
block|{
return|return
name|policyReferenceElemQName
return|;
block|}
specifier|public
name|QName
name|getPolicyAttachmentElemQName
parameter_list|()
block|{
return|return
name|policyAttachmentElemQName
return|;
block|}
specifier|public
name|QName
name|getAppliesToElemQName
parameter_list|()
block|{
return|return
name|appliesToElemQName
return|;
block|}
specifier|public
name|QName
name|getOptionalAttrQName
parameter_list|()
block|{
return|return
name|optionalAttrQName
return|;
block|}
specifier|public
name|QName
name|getPolicyURIsAttrQName
parameter_list|()
block|{
return|return
name|policyURIsAttrQName
return|;
block|}
specifier|public
name|QName
name|getIdAttrQName
parameter_list|()
block|{
return|return
name|WSU_ID_ATTR_QNAME
return|;
block|}
block|}
end_class

end_unit

