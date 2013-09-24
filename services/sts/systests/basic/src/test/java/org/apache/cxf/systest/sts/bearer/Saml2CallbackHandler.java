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
name|systest
operator|.
name|sts
operator|.
name|bearer
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
name|builder
operator|.
name|SAML2Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|common
operator|.
name|SAMLVersion
import|;
end_import

begin_comment
comment|/**  * Create a SAML2 Bearer Assertion.  */
end_comment

begin_class
specifier|public
class|class
name|Saml2CallbackHandler
implements|implements
name|CallbackHandler
block|{
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
name|callback
operator|.
name|setSamlVersion
argument_list|(
name|SAMLVersion
operator|.
name|VERSION_20
argument_list|)
expr_stmt|;
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
literal|"uid=alice"
decl_stmt|;
name|String
name|confirmationMethod
init|=
name|SAML2Constants
operator|.
name|CONF_BEARER
decl_stmt|;
name|SubjectBean
name|subjectBean
init|=
operator|new
name|SubjectBean
argument_list|(
name|subjectName
argument_list|,
literal|null
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
name|AttributeStatementBean
name|attrBean
init|=
operator|new
name|AttributeStatementBean
argument_list|()
decl_stmt|;
if|if
condition|(
name|subjectBean
operator|!=
literal|null
condition|)
block|{
name|attrBean
operator|.
name|setSubject
argument_list|(
name|subjectBean
argument_list|)
expr_stmt|;
block|}
name|AttributeBean
name|attributeBean
init|=
operator|new
name|AttributeBean
argument_list|()
decl_stmt|;
name|attributeBean
operator|.
name|setQualifiedName
argument_list|(
literal|"role"
argument_list|)
expr_stmt|;
name|attributeBean
operator|.
name|addAttributeValue
argument_list|(
literal|"user"
argument_list|)
expr_stmt|;
name|attrBean
operator|.
name|setSamlAttributes
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|attributeBean
argument_list|)
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
end_class

end_unit

