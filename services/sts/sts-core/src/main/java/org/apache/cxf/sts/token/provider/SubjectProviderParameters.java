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
name|sts
operator|.
name|token
operator|.
name|provider
package|;
end_package

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
name|Document
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
name|bean
operator|.
name|AttributeStatementBean
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
name|bean
operator|.
name|AuthDecisionStatementBean
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
name|bean
operator|.
name|AuthenticationStatementBean
import|;
end_import

begin_comment
comment|/**  * The parameters that are passed through to a SubjectProvider implementation to create a Subject(Bean).  */
end_comment

begin_class
specifier|public
class|class
name|SubjectProviderParameters
block|{
specifier|private
name|TokenProviderParameters
name|providerParameters
decl_stmt|;
specifier|private
name|Document
name|doc
decl_stmt|;
specifier|private
name|byte
index|[]
name|secret
decl_stmt|;
specifier|private
name|List
argument_list|<
name|AttributeStatementBean
argument_list|>
name|attrBeanList
decl_stmt|;
specifier|private
name|List
argument_list|<
name|AuthenticationStatementBean
argument_list|>
name|authBeanList
decl_stmt|;
specifier|private
name|List
argument_list|<
name|AuthDecisionStatementBean
argument_list|>
name|authDecisionBeanList
decl_stmt|;
specifier|public
name|TokenProviderParameters
name|getProviderParameters
parameter_list|()
block|{
return|return
name|providerParameters
return|;
block|}
specifier|public
name|void
name|setProviderParameters
parameter_list|(
name|TokenProviderParameters
name|providerParameters
parameter_list|)
block|{
name|this
operator|.
name|providerParameters
operator|=
name|providerParameters
expr_stmt|;
block|}
specifier|public
name|Document
name|getDoc
parameter_list|()
block|{
return|return
name|doc
return|;
block|}
specifier|public
name|void
name|setDoc
parameter_list|(
name|Document
name|doc
parameter_list|)
block|{
name|this
operator|.
name|doc
operator|=
name|doc
expr_stmt|;
block|}
specifier|public
name|byte
index|[]
name|getSecret
parameter_list|()
block|{
return|return
name|secret
return|;
block|}
specifier|public
name|void
name|setSecret
parameter_list|(
name|byte
index|[]
name|secret
parameter_list|)
block|{
name|this
operator|.
name|secret
operator|=
name|secret
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|AttributeStatementBean
argument_list|>
name|getAttrBeanList
parameter_list|()
block|{
return|return
name|attrBeanList
return|;
block|}
specifier|public
name|void
name|setAttrBeanList
parameter_list|(
name|List
argument_list|<
name|AttributeStatementBean
argument_list|>
name|attrBeanList
parameter_list|)
block|{
name|this
operator|.
name|attrBeanList
operator|=
name|attrBeanList
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|AuthenticationStatementBean
argument_list|>
name|getAuthBeanList
parameter_list|()
block|{
return|return
name|authBeanList
return|;
block|}
specifier|public
name|void
name|setAuthBeanList
parameter_list|(
name|List
argument_list|<
name|AuthenticationStatementBean
argument_list|>
name|authBeanList
parameter_list|)
block|{
name|this
operator|.
name|authBeanList
operator|=
name|authBeanList
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|AuthDecisionStatementBean
argument_list|>
name|getAuthDecisionBeanList
parameter_list|()
block|{
return|return
name|authDecisionBeanList
return|;
block|}
specifier|public
name|void
name|setAuthDecisionBeanList
parameter_list|(
name|List
argument_list|<
name|AuthDecisionStatementBean
argument_list|>
name|authDecisionBeanList
parameter_list|)
block|{
name|this
operator|.
name|authDecisionBeanList
operator|=
name|authDecisionBeanList
expr_stmt|;
block|}
block|}
end_class

end_unit

