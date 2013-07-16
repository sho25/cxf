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
comment|/**  * This class constructs an XACML Request given a Principal, list of roles and MessageContext, following the  * SAML 2.0 profile of XACML 2.0. The principal name is inserted as the Subject ID, and the list of roles  * associated with that principal are inserted as Subject roles. The action to send defaults to "execute". The  * resource is the WSDL Operation for a SOAP service, and the request URI for a REST service. You can also  * configure the ability to send the full request URL instead for a SOAP or REST service. The current DateTime  * is also sent in an Environment, however this can be disabled via configuration.  */
end_comment

begin_class
specifier|public
class|class
name|DefaultXACMLRequestBuilder
implements|implements
name|XACMLRequestBuilder
block|{
specifier|private
name|boolean
name|sendDateTime
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|action
init|=
literal|"execute"
decl_stmt|;
specifier|private
name|boolean
name|sendFullRequestURL
decl_stmt|;
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
name|CXFMessageParser
name|messageParser
init|=
operator|new
name|CXFMessageParser
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|String
name|issuer
init|=
name|messageParser
operator|.
name|getIssuer
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|resources
init|=
name|messageParser
operator|.
name|getResources
argument_list|(
name|sendFullRequestURL
argument_list|)
decl_stmt|;
name|String
name|actionToUse
init|=
name|messageParser
operator|.
name|getAction
argument_list|(
name|action
argument_list|)
decl_stmt|;
name|SubjectType
name|subjectType
init|=
name|createSubjectType
argument_list|(
name|principal
argument_list|,
name|roles
argument_list|,
name|issuer
argument_list|)
decl_stmt|;
name|ResourceType
name|resourceType
init|=
name|createResourceType
argument_list|(
name|resources
argument_list|)
decl_stmt|;
name|AttributeType
name|actionAttribute
init|=
name|createAttribute
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
name|actionToUse
argument_list|)
decl_stmt|;
name|ActionType
name|actionType
init|=
name|RequestComponentBuilder
operator|.
name|createActionType
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|actionAttribute
argument_list|)
argument_list|)
decl_stmt|;
return|return
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
name|createEnvironmentType
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|ResourceType
name|createResourceType
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|resources
parameter_list|)
block|{
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
name|List
argument_list|<
name|AttributeValueType
argument_list|>
name|resourceAttributes
init|=
operator|new
name|ArrayList
argument_list|<
name|AttributeValueType
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|resource
range|:
name|resources
control|)
block|{
if|if
condition|(
name|resource
operator|!=
literal|null
condition|)
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
name|resourceAttributes
operator|.
name|add
argument_list|(
name|resourceAttributeValue
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|resourceAttributes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|AttributeType
name|resourceAttribute
init|=
name|createAttribute
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
name|resourceAttributes
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
return|return
name|RequestComponentBuilder
operator|.
name|createResourceType
argument_list|(
name|attributes
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|private
name|EnvironmentType
name|createEnvironmentType
parameter_list|()
block|{
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
if|if
condition|(
name|sendDateTime
condition|)
block|{
name|AttributeType
name|environmentAttribute
init|=
name|createAttribute
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
operator|new
name|DateTime
argument_list|()
operator|.
name|toString
argument_list|()
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
return|return
name|RequestComponentBuilder
operator|.
name|createEnvironmentType
argument_list|(
name|attributes
argument_list|)
return|;
block|}
specifier|private
name|SubjectType
name|createSubjectType
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
name|String
name|issuer
parameter_list|)
block|{
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
name|attributes
operator|.
name|add
argument_list|(
name|createAttribute
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
name|principal
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|roles
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|AttributeValueType
argument_list|>
name|roleAttributes
init|=
operator|new
name|ArrayList
argument_list|<
name|AttributeValueType
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|role
range|:
name|roles
control|)
block|{
if|if
condition|(
name|role
operator|!=
literal|null
condition|)
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
name|roleAttributes
operator|.
name|add
argument_list|(
name|subjectRoleAttributeValue
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|roleAttributes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|AttributeType
name|subjectRoleAttribute
init|=
name|createAttribute
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
name|roleAttributes
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
block|}
return|return
name|RequestComponentBuilder
operator|.
name|createSubjectType
argument_list|(
name|attributes
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|private
name|AttributeType
name|createAttribute
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|issuer
parameter_list|,
name|List
argument_list|<
name|AttributeValueType
argument_list|>
name|values
parameter_list|)
block|{
return|return
name|RequestComponentBuilder
operator|.
name|createAttributeType
argument_list|(
name|id
argument_list|,
name|type
argument_list|,
name|issuer
argument_list|,
name|values
argument_list|)
return|;
block|}
specifier|private
name|AttributeType
name|createAttribute
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|issuer
parameter_list|,
name|String
name|value
parameter_list|)
block|{
return|return
name|createAttribute
argument_list|(
name|id
argument_list|,
name|type
argument_list|,
name|issuer
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|RequestComponentBuilder
operator|.
name|createAttributeValueType
argument_list|(
name|value
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Set a new Action String to use      */
specifier|public
name|void
name|setAction
parameter_list|(
name|String
name|action
parameter_list|)
block|{
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
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
annotation|@
name|Override
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
throw|throw
operator|new
name|IllegalAccessError
argument_list|(
literal|"Deprecated"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getResource
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalAccessError
argument_list|(
literal|"Deprecated"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

