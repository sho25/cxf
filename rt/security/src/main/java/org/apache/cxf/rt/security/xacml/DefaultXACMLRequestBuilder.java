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
name|rt
operator|.
name|security
operator|.
name|xacml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
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
name|Collections
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
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|security
operator|.
name|SAMLSecurityContext
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
name|security
operator|.
name|SecurityContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|WSSecurityException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|SamlAssertionWrapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|DateTime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xacml
operator|.
name|ctx
operator|.
name|ActionType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xacml
operator|.
name|ctx
operator|.
name|AttributeType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xacml
operator|.
name|ctx
operator|.
name|AttributeValueType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xacml
operator|.
name|ctx
operator|.
name|EnvironmentType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xacml
operator|.
name|ctx
operator|.
name|RequestType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xacml
operator|.
name|ctx
operator|.
name|ResourceType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xacml
operator|.
name|ctx
operator|.
name|SubjectType
import|;
end_import

begin_comment
comment|/**  * This class constructs an XACML Request given a Principal, list of roles and MessageContext,  * following the SAML 2.0 profile of XACML 2.0. The principal name is inserted as the Subject ID,  * and the list of roles associated with that principal are inserted as Subject roles.  *   * The action to send defaults to "execute". The resource is the WSDL Operation for a SOAP service,  * and the request URI for a REST service. You can also configure the ability to send the full  * request URL instead for a SOAP or REST service. The current DateTime is also sent in an  * Environment, however this can be disabled via configuration.   */
end_comment

begin_class
specifier|public
class|class
name|DefaultXACMLRequestBuilder
implements|implements
name|XACMLRequestBuilder
block|{
specifier|private
name|String
name|action
init|=
literal|"execute"
decl_stmt|;
specifier|private
name|boolean
name|sendDateTime
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|sendFullRequestURL
decl_stmt|;
comment|/**      * Set a new Action String to use      */
specifier|public
name|void
name|setAction
parameter_list|(
name|String
name|newAction
parameter_list|)
block|{
name|action
operator|=
name|newAction
expr_stmt|;
block|}
comment|/**      * Get the Action String currently in use      */
specifier|public
name|String
name|getAction
parameter_list|()
block|{
return|return
name|action
return|;
block|}
comment|/**      * Create an XACML Request given a Principal, list of roles and Message.      */
specifier|public
name|RequestType
name|createRequest
parameter_list|(
name|Principal
name|principal
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|roles
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|issuer
init|=
name|getIssuer
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|resources
init|=
name|getResources
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|String
name|actionToUse
init|=
name|getAction
argument_list|(
name|message
argument_list|)
decl_stmt|;
comment|// Subject
name|List
argument_list|<
name|AttributeType
argument_list|>
name|attributes
init|=
operator|new
name|ArrayList
argument_list|<
name|AttributeType
argument_list|>
argument_list|()
decl_stmt|;
name|AttributeValueType
name|subjectIdAttributeValue
init|=
name|RequestComponentBuilder
operator|.
name|createAttributeValueType
argument_list|(
name|principal
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|AttributeType
name|subjectIdAttribute
init|=
name|RequestComponentBuilder
operator|.
name|createAttributeType
argument_list|(
name|XACMLConstants
operator|.
name|SUBJECT_ID
argument_list|,
name|XACMLConstants
operator|.
name|XS_STRING
argument_list|,
name|issuer
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|subjectIdAttributeValue
argument_list|)
argument_list|)
decl_stmt|;
name|attributes
operator|.
name|add
argument_list|(
name|subjectIdAttribute
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|role
range|:
name|roles
control|)
block|{
name|AttributeValueType
name|subjectRoleAttributeValue
init|=
name|RequestComponentBuilder
operator|.
name|createAttributeValueType
argument_list|(
name|role
argument_list|)
decl_stmt|;
name|AttributeType
name|subjectRoleAttribute
init|=
name|RequestComponentBuilder
operator|.
name|createAttributeType
argument_list|(
name|XACMLConstants
operator|.
name|SUBJECT_ROLE
argument_list|,
name|XACMLConstants
operator|.
name|XS_ANY_URI
argument_list|,
name|issuer
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|subjectRoleAttributeValue
argument_list|)
argument_list|)
decl_stmt|;
name|attributes
operator|.
name|add
argument_list|(
name|subjectRoleAttribute
argument_list|)
expr_stmt|;
block|}
name|SubjectType
name|subjectType
init|=
name|RequestComponentBuilder
operator|.
name|createSubjectType
argument_list|(
name|attributes
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|// Resource
name|attributes
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|resource
range|:
name|resources
control|)
block|{
name|AttributeValueType
name|resourceAttributeValue
init|=
name|RequestComponentBuilder
operator|.
name|createAttributeValueType
argument_list|(
name|resource
argument_list|)
decl_stmt|;
name|AttributeType
name|resourceAttribute
init|=
name|RequestComponentBuilder
operator|.
name|createAttributeType
argument_list|(
name|XACMLConstants
operator|.
name|RESOURCE_ID
argument_list|,
name|XACMLConstants
operator|.
name|XS_STRING
argument_list|,
literal|null
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|resourceAttributeValue
argument_list|)
argument_list|)
decl_stmt|;
name|attributes
operator|.
name|add
argument_list|(
name|resourceAttribute
argument_list|)
expr_stmt|;
block|}
name|ResourceType
name|resourceType
init|=
name|RequestComponentBuilder
operator|.
name|createResourceType
argument_list|(
name|attributes
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|// Action
name|AttributeValueType
name|actionAttributeValue
init|=
name|RequestComponentBuilder
operator|.
name|createAttributeValueType
argument_list|(
name|actionToUse
argument_list|)
decl_stmt|;
name|AttributeType
name|actionAttribute
init|=
name|RequestComponentBuilder
operator|.
name|createAttributeType
argument_list|(
name|XACMLConstants
operator|.
name|ACTION_ID
argument_list|,
name|XACMLConstants
operator|.
name|XS_STRING
argument_list|,
literal|null
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|actionAttributeValue
argument_list|)
argument_list|)
decl_stmt|;
name|attributes
operator|.
name|clear
argument_list|()
expr_stmt|;
name|attributes
operator|.
name|add
argument_list|(
name|actionAttribute
argument_list|)
expr_stmt|;
name|ActionType
name|actionType
init|=
name|RequestComponentBuilder
operator|.
name|createActionType
argument_list|(
name|attributes
argument_list|)
decl_stmt|;
comment|// Environment
name|attributes
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|sendDateTime
condition|)
block|{
name|DateTime
name|dateTime
init|=
operator|new
name|DateTime
argument_list|()
decl_stmt|;
name|AttributeValueType
name|environmentAttributeValue
init|=
name|RequestComponentBuilder
operator|.
name|createAttributeValueType
argument_list|(
name|dateTime
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|AttributeType
name|environmentAttribute
init|=
name|RequestComponentBuilder
operator|.
name|createAttributeType
argument_list|(
name|XACMLConstants
operator|.
name|CURRENT_DATETIME
argument_list|,
name|XACMLConstants
operator|.
name|XS_DATETIME
argument_list|,
literal|null
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|environmentAttributeValue
argument_list|)
argument_list|)
decl_stmt|;
name|attributes
operator|.
name|add
argument_list|(
name|environmentAttribute
argument_list|)
expr_stmt|;
block|}
name|EnvironmentType
name|environmentType
init|=
name|RequestComponentBuilder
operator|.
name|createEnvironmentType
argument_list|(
name|attributes
argument_list|)
decl_stmt|;
comment|// Request
name|RequestType
name|request
init|=
name|RequestComponentBuilder
operator|.
name|createRequestType
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|subjectType
argument_list|)
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|resourceType
argument_list|)
argument_list|,
name|actionType
argument_list|,
name|environmentType
argument_list|)
decl_stmt|;
return|return
name|request
return|;
block|}
comment|/**      * Get the Issuer of the SAML Assertion      */
specifier|private
name|String
name|getIssuer
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|SecurityContext
name|sc
init|=
name|message
operator|.
name|get
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|sc
operator|instanceof
name|SAMLSecurityContext
condition|)
block|{
name|Element
name|assertionElement
init|=
operator|(
operator|(
name|SAMLSecurityContext
operator|)
name|sc
operator|)
operator|.
name|getAssertionElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|assertionElement
operator|!=
literal|null
condition|)
block|{
name|SamlAssertionWrapper
name|wrapper
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|assertionElement
argument_list|)
decl_stmt|;
return|return
name|wrapper
operator|.
name|getIssuerString
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isSendDateTime
parameter_list|()
block|{
return|return
name|sendDateTime
return|;
block|}
specifier|public
name|void
name|setSendDateTime
parameter_list|(
name|boolean
name|sendDateTime
parameter_list|)
block|{
name|this
operator|.
name|sendDateTime
operator|=
name|sendDateTime
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSendFullRequestURL
parameter_list|()
block|{
return|return
name|sendFullRequestURL
return|;
block|}
comment|/**      * Whether to send the full Request URL as the resource or not. If set to true,      * the full Request URL will be sent for both a JAX-WS and JAX-RS service. If set      * to false (the default), a JAX-WS service will send the "{namespace}operation" QName,      * and a JAX-RS service will send the RequestURI (i.e. minus the initial https:<ip> prefix).      */
specifier|public
name|void
name|setSendFullRequestURL
parameter_list|(
name|boolean
name|sendFullRequestURL
parameter_list|)
block|{
name|this
operator|.
name|sendFullRequestURL
operator|=
name|sendFullRequestURL
expr_stmt|;
block|}
comment|/**      * Return the Resources that have been inserted into the Request      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getResources
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|resources
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|WSDL_OPERATION
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|resources
operator|.
name|add
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|WSDL_OPERATION
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sendFullRequestURL
condition|)
block|{
name|resources
operator|.
name|add
argument_list|(
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUEST_URL
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|resources
operator|.
name|add
argument_list|(
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUEST_URI
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|resources
return|;
block|}
specifier|public
name|String
name|getResource
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|resource
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|sendFullRequestURL
condition|)
block|{
name|resource
operator|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUEST_URL
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|WSDL_OPERATION
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|resource
operator|=
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|WSDL_OPERATION
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|resource
operator|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUEST_URI
argument_list|)
expr_stmt|;
block|}
return|return
name|resource
return|;
block|}
specifier|private
name|String
name|getAction
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|actionToUse
init|=
name|action
decl_stmt|;
comment|// For REST use the HTTP Verb
if|if
condition|(
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|WSDL_OPERATION
argument_list|)
operator|==
literal|null
operator|&&
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|actionToUse
operator|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
expr_stmt|;
block|}
return|return
name|actionToUse
return|;
block|}
block|}
end_class

end_unit

