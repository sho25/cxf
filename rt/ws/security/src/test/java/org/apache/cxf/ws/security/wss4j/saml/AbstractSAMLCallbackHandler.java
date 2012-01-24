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
name|security
operator|.
name|wss4j
operator|.
name|saml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
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
name|CallbackHandler
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
name|WSConstants
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
name|message
operator|.
name|WSSecEncryptedKey
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
name|SAMLCallback
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
name|bean
operator|.
name|ActionBean
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
name|ws
operator|.
name|security
operator|.
name|saml
operator|.
name|ext
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
name|ws
operator|.
name|security
operator|.
name|saml
operator|.
name|ext
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
name|ws
operator|.
name|security
operator|.
name|saml
operator|.
name|ext
operator|.
name|bean
operator|.
name|AuthenticationStatementBean
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
name|bean
operator|.
name|KeyInfoBean
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
name|bean
operator|.
name|KeyInfoBean
operator|.
name|CERT_IDENTIFIER
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
name|bean
operator|.
name|SubjectBean
import|;
end_import

begin_comment
comment|/**  * A base implementation of a Callback Handler for a SAML assertion. By default it creates an  * authentication assertion.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractSAMLCallbackHandler
implements|implements
name|CallbackHandler
block|{
specifier|public
enum|enum
name|Statement
block|{
name|AUTHN
block|,
name|ATTR
block|,
name|AUTHZ
block|}
empty_stmt|;
specifier|protected
name|String
name|subjectName
decl_stmt|;
specifier|protected
name|String
name|subjectQualifier
decl_stmt|;
specifier|protected
name|String
name|confirmationMethod
decl_stmt|;
specifier|protected
name|X509Certificate
index|[]
name|certs
decl_stmt|;
specifier|protected
name|Statement
name|statement
init|=
name|Statement
operator|.
name|AUTHN
decl_stmt|;
specifier|protected
name|CERT_IDENTIFIER
name|certIdentifier
init|=
name|CERT_IDENTIFIER
operator|.
name|X509_CERT
decl_stmt|;
specifier|protected
name|byte
index|[]
name|ephemeralKey
decl_stmt|;
specifier|public
name|void
name|setConfirmationMethod
parameter_list|(
name|String
name|confMethod
parameter_list|)
block|{
name|confirmationMethod
operator|=
name|confMethod
expr_stmt|;
block|}
specifier|public
name|void
name|setStatement
parameter_list|(
name|Statement
name|statement
parameter_list|)
block|{
name|this
operator|.
name|statement
operator|=
name|statement
expr_stmt|;
block|}
specifier|public
name|void
name|setCertIdentifier
parameter_list|(
name|CERT_IDENTIFIER
name|certIdentifier
parameter_list|)
block|{
name|this
operator|.
name|certIdentifier
operator|=
name|certIdentifier
expr_stmt|;
block|}
specifier|public
name|void
name|setCerts
parameter_list|(
name|X509Certificate
index|[]
name|certs
parameter_list|)
block|{
name|this
operator|.
name|certs
operator|=
name|certs
expr_stmt|;
block|}
specifier|public
name|byte
index|[]
name|getEphemeralKey
parameter_list|()
block|{
return|return
name|ephemeralKey
return|;
block|}
comment|/**      * Note that the SubjectBean parameter should be null for SAML2.0      */
specifier|protected
name|void
name|createAndSetStatement
parameter_list|(
name|SubjectBean
name|subjectBean
parameter_list|,
name|SAMLCallback
name|callback
parameter_list|)
block|{
if|if
condition|(
name|statement
operator|==
name|Statement
operator|.
name|AUTHN
condition|)
block|{
name|AuthenticationStatementBean
name|authBean
init|=
operator|new
name|AuthenticationStatementBean
argument_list|()
decl_stmt|;
if|if
condition|(
name|subjectBean
operator|!=
literal|null
condition|)
block|{
name|authBean
operator|.
name|setSubject
argument_list|(
name|subjectBean
argument_list|)
expr_stmt|;
block|}
name|authBean
operator|.
name|setAuthenticationMethod
argument_list|(
literal|"Password"
argument_list|)
expr_stmt|;
name|callback
operator|.
name|setAuthenticationStatementData
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|authBean
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|statement
operator|==
name|Statement
operator|.
name|ATTR
condition|)
block|{
name|AttributeStatementBean
name|attrBean
init|=
operator|new
name|AttributeStatementBean
argument_list|()
decl_stmt|;
name|AttributeBean
name|attributeBean
init|=
operator|new
name|AttributeBean
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
name|attributeBean
operator|.
name|setSimpleName
argument_list|(
literal|"role"
argument_list|)
expr_stmt|;
name|attributeBean
operator|.
name|setQualifiedName
argument_list|(
literal|"http://custom-ns"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|attributeBean
operator|.
name|setQualifiedName
argument_list|(
literal|"role"
argument_list|)
expr_stmt|;
block|}
name|attributeBean
operator|.
name|setAttributeValues
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"user"
argument_list|)
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
else|else
block|{
name|AuthDecisionStatementBean
name|authzBean
init|=
operator|new
name|AuthDecisionStatementBean
argument_list|()
decl_stmt|;
if|if
condition|(
name|subjectBean
operator|!=
literal|null
condition|)
block|{
name|authzBean
operator|.
name|setSubject
argument_list|(
name|subjectBean
argument_list|)
expr_stmt|;
block|}
name|ActionBean
name|actionBean
init|=
operator|new
name|ActionBean
argument_list|()
decl_stmt|;
name|actionBean
operator|.
name|setContents
argument_list|(
literal|"Read"
argument_list|)
expr_stmt|;
name|authzBean
operator|.
name|setActions
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|actionBean
argument_list|)
argument_list|)
expr_stmt|;
name|authzBean
operator|.
name|setResource
argument_list|(
literal|"endpoint"
argument_list|)
expr_stmt|;
name|authzBean
operator|.
name|setDecision
argument_list|(
name|AuthDecisionStatementBean
operator|.
name|Decision
operator|.
name|PERMIT
argument_list|)
expr_stmt|;
name|callback
operator|.
name|setAuthDecisionStatementData
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|authzBean
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|KeyInfoBean
name|createKeyInfo
parameter_list|()
throws|throws
name|Exception
block|{
name|KeyInfoBean
name|keyInfo
init|=
operator|new
name|KeyInfoBean
argument_list|()
decl_stmt|;
if|if
condition|(
name|statement
operator|==
name|Statement
operator|.
name|AUTHN
condition|)
block|{
name|keyInfo
operator|.
name|setCertificate
argument_list|(
name|certs
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|keyInfo
operator|.
name|setCertIdentifer
argument_list|(
name|certIdentifier
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|statement
operator|==
name|Statement
operator|.
name|ATTR
condition|)
block|{
comment|// Build a new Document
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
name|DocumentBuilder
name|docBuilder
init|=
name|docBuilderFactory
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
name|docBuilder
operator|.
name|newDocument
argument_list|()
decl_stmt|;
comment|// Create an Encrypted Key
name|WSSecEncryptedKey
name|encrKey
init|=
operator|new
name|WSSecEncryptedKey
argument_list|()
decl_stmt|;
name|encrKey
operator|.
name|setKeyIdentifierType
argument_list|(
name|WSConstants
operator|.
name|X509_KEY_IDENTIFIER
argument_list|)
expr_stmt|;
name|encrKey
operator|.
name|setUseThisCert
argument_list|(
name|certs
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|encrKey
operator|.
name|prepare
argument_list|(
name|doc
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|ephemeralKey
operator|=
name|encrKey
operator|.
name|getEphemeralKey
argument_list|()
expr_stmt|;
name|Element
name|encryptedKeyElement
init|=
name|encrKey
operator|.
name|getEncryptedKeyElement
argument_list|()
decl_stmt|;
comment|// Append the EncryptedKey to a KeyInfo element
name|Element
name|keyInfoElement
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|WSConstants
operator|.
name|SIG_NS
argument_list|,
name|WSConstants
operator|.
name|SIG_PREFIX
operator|+
literal|":"
operator|+
name|WSConstants
operator|.
name|KEYINFO_LN
argument_list|)
decl_stmt|;
name|keyInfoElement
operator|.
name|setAttributeNS
argument_list|(
name|WSConstants
operator|.
name|XMLNS_NS
argument_list|,
literal|"xmlns:"
operator|+
name|WSConstants
operator|.
name|SIG_PREFIX
argument_list|,
name|WSConstants
operator|.
name|SIG_NS
argument_list|)
expr_stmt|;
name|keyInfoElement
operator|.
name|appendChild
argument_list|(
name|encryptedKeyElement
argument_list|)
expr_stmt|;
name|keyInfo
operator|.
name|setElement
argument_list|(
name|keyInfoElement
argument_list|)
expr_stmt|;
block|}
return|return
name|keyInfo
return|;
block|}
block|}
end_class

end_unit

