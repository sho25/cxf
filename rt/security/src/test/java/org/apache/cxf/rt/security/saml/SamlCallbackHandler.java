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
name|saml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|Callback
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|UnsupportedCallbackException
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
name|SAMLCallback
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
name|AttributeBean
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
name|SubjectBean
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
name|Version
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
name|builder
operator|.
name|SAML1Constants
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
name|builder
operator|.
name|SAML2Constants
import|;
end_import

begin_comment
comment|/**  * A CallbackHandler instance to mock up a SAML Attribute Assertion.  */
end_comment

begin_class
specifier|public
class|class
name|SamlCallbackHandler
implements|implements
name|CallbackHandler
block|{
specifier|private
name|boolean
name|saml2
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|confirmationMethod
init|=
name|SAML2Constants
operator|.
name|CONF_BEARER
decl_stmt|;
specifier|private
name|List
argument_list|<
name|AttributeBean
argument_list|>
name|attributes
decl_stmt|;
specifier|public
name|SamlCallbackHandler
parameter_list|()
block|{
comment|//
block|}
specifier|public
name|SamlCallbackHandler
parameter_list|(
name|boolean
name|saml2
parameter_list|)
block|{
name|this
operator|.
name|saml2
operator|=
name|saml2
expr_stmt|;
block|}
specifier|public
name|void
name|setConfirmationMethod
parameter_list|(
name|String
name|confirmationMethod
parameter_list|)
block|{
name|this
operator|.
name|confirmationMethod
operator|=
name|confirmationMethod
expr_stmt|;
block|}
specifier|public
name|void
name|handle
parameter_list|(
name|Callback
index|[]
name|callbacks
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnsupportedCallbackException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|callbacks
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|callbacks
index|[
name|i
index|]
operator|instanceof
name|SAMLCallback
condition|)
block|{
name|SAMLCallback
name|callback
init|=
operator|(
name|SAMLCallback
operator|)
name|callbacks
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|saml2
condition|)
block|{
name|callback
operator|.
name|setSamlVersion
argument_list|(
name|Version
operator|.
name|SAML_20
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|callback
operator|.
name|setSamlVersion
argument_list|(
name|Version
operator|.
name|SAML_11
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|setIssuer
argument_list|(
literal|"sts"
argument_list|)
expr_stmt|;
name|String
name|subjectName
init|=
literal|"uid=sts-client,o=mock-sts.com"
decl_stmt|;
name|String
name|subjectQualifier
init|=
literal|"www.mock-sts.com"
decl_stmt|;
if|if
condition|(
operator|!
name|saml2
operator|&&
name|SAML2Constants
operator|.
name|CONF_SENDER_VOUCHES
operator|.
name|equals
argument_list|(
name|confirmationMethod
argument_list|)
condition|)
block|{
name|confirmationMethod
operator|=
name|SAML1Constants
operator|.
name|CONF_SENDER_VOUCHES
expr_stmt|;
block|}
name|SubjectBean
name|subjectBean
init|=
operator|new
name|SubjectBean
argument_list|(
name|subjectName
argument_list|,
name|subjectQualifier
argument_list|,
name|confirmationMethod
argument_list|)
decl_stmt|;
name|callback
operator|.
name|setSubject
argument_list|(
name|subjectBean
argument_list|)
expr_stmt|;
if|if
condition|(
name|attributes
operator|!=
literal|null
condition|)
block|{
name|AttributeStatementBean
name|attrBean
init|=
operator|new
name|AttributeStatementBean
argument_list|()
decl_stmt|;
name|attrBean
operator|.
name|setSubject
argument_list|(
name|subjectBean
argument_list|)
expr_stmt|;
name|attrBean
operator|.
name|setSamlAttributes
argument_list|(
name|attributes
argument_list|)
expr_stmt|;
name|callback
operator|.
name|setAttributeStatementData
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|attrBean
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|List
argument_list|<
name|AttributeBean
argument_list|>
name|getAttributes
parameter_list|()
block|{
return|return
name|attributes
return|;
block|}
specifier|public
name|void
name|setAttributes
parameter_list|(
name|List
argument_list|<
name|AttributeBean
argument_list|>
name|attributes
parameter_list|)
block|{
name|this
operator|.
name|attributes
operator|=
name|attributes
expr_stmt|;
block|}
block|}
end_class

end_unit

