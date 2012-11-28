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
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
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
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|saml
operator|.
name|ext
operator|.
name|OpenSAMLUtil
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
comment|/**  * Some unit tests to create a XACML Request using the RequestComponentBuilder.  */
end_comment

begin_class
specifier|public
class|class
name|RequestComponentBuilderTest
extends|extends
name|org
operator|.
name|junit
operator|.
name|Assert
block|{
specifier|private
name|DocumentBuilder
name|docBuilder
decl_stmt|;
static|static
block|{
name|OpenSAMLUtil
operator|.
name|initSamlEngine
argument_list|()
expr_stmt|;
block|}
specifier|public
name|RequestComponentBuilderTest
parameter_list|()
throws|throws
name|ParserConfigurationException
block|{
name|DocumentBuilderFactory
name|docBuilderFactory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|docBuilderFactory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|docBuilder
operator|=
name|docBuilderFactory
operator|.
name|newDocumentBuilder
argument_list|()
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testCreateXACMLRequest
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|doc
init|=
name|docBuilder
operator|.
name|newDocument
argument_list|()
decl_stmt|;
comment|// Subject
name|AttributeValueType
name|subjectIdAttributeValue
init|=
name|RequestComponentBuilder
operator|.
name|createAttributeValueType
argument_list|(
literal|"alice-user@apache.org"
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
name|RFC_822_NAME
argument_list|,
literal|null
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|subjectIdAttributeValue
argument_list|)
argument_list|)
decl_stmt|;
name|AttributeValueType
name|subjectGroupAttributeValue
init|=
name|RequestComponentBuilder
operator|.
name|createAttributeValueType
argument_list|(
literal|"manager"
argument_list|)
decl_stmt|;
name|AttributeType
name|subjectGroupAttribute
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
literal|"admin-user@apache.org"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|subjectGroupAttributeValue
argument_list|)
argument_list|)
decl_stmt|;
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
name|subjectIdAttribute
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|add
argument_list|(
name|subjectGroupAttribute
argument_list|)
expr_stmt|;
name|SubjectType
name|subject
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
name|AttributeValueType
name|resourceAttributeValue
init|=
name|RequestComponentBuilder
operator|.
name|createAttributeValueType
argument_list|(
literal|"{http://www.example.org/contract/DoubleIt}DoubleIt"
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
name|clear
argument_list|()
expr_stmt|;
name|attributes
operator|.
name|add
argument_list|(
name|resourceAttribute
argument_list|)
expr_stmt|;
name|ResourceType
name|resource
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
literal|"execute"
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
name|action
init|=
name|RequestComponentBuilder
operator|.
name|createActionType
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
name|subject
argument_list|)
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|resource
argument_list|)
argument_list|,
name|action
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Element
name|policyElement
init|=
name|OpenSAMLUtil
operator|.
name|toDom
argument_list|(
name|request
argument_list|,
name|doc
argument_list|)
decl_stmt|;
comment|// String outputString = DOM2Writer.nodeToString(policyElement);
name|assertNotNull
argument_list|(
name|policyElement
argument_list|)
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testEnvironment
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|doc
init|=
name|docBuilder
operator|.
name|newDocument
argument_list|()
decl_stmt|;
comment|// Subject
name|AttributeValueType
name|subjectIdAttributeValue
init|=
name|RequestComponentBuilder
operator|.
name|createAttributeValueType
argument_list|(
literal|"alice-user@apache.org"
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
name|RFC_822_NAME
argument_list|,
literal|null
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|subjectIdAttributeValue
argument_list|)
argument_list|)
decl_stmt|;
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
name|subjectIdAttribute
argument_list|)
expr_stmt|;
name|SubjectType
name|subject
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
name|AttributeValueType
name|resourceAttributeValue
init|=
name|RequestComponentBuilder
operator|.
name|createAttributeValueType
argument_list|(
literal|"{http://www.example.org/contract/DoubleIt}DoubleIt"
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
name|clear
argument_list|()
expr_stmt|;
name|attributes
operator|.
name|add
argument_list|(
name|resourceAttribute
argument_list|)
expr_stmt|;
name|ResourceType
name|resource
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
literal|"execute"
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
name|action
init|=
name|RequestComponentBuilder
operator|.
name|createActionType
argument_list|(
name|attributes
argument_list|)
decl_stmt|;
comment|// Environment
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
name|clear
argument_list|()
expr_stmt|;
name|attributes
operator|.
name|add
argument_list|(
name|environmentAttribute
argument_list|)
expr_stmt|;
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
name|subject
argument_list|)
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|resource
argument_list|)
argument_list|,
name|action
argument_list|,
name|environmentType
argument_list|)
decl_stmt|;
name|Element
name|policyElement
init|=
name|OpenSAMLUtil
operator|.
name|toDom
argument_list|(
name|request
argument_list|,
name|doc
argument_list|)
decl_stmt|;
comment|// String outputString = DOM2Writer.nodeToString(policyElement);
name|assertNotNull
argument_list|(
name|policyElement
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

