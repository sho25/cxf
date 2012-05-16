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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|sso
package|;
end_package

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
name|message
operator|.
name|MessageImpl
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
name|opensaml
operator|.
name|common
operator|.
name|SAMLVersion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|AuthnContextClassRef
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|AuthnContextComparisonTypeEnumeration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|AuthnRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Issuer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|NameIDPolicy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|RequestedAuthnContext
import|;
end_import

begin_comment
comment|/**  * Some unit tests for the SamlpRequestComponentBuilder and AuthnRequestBuilder  */
end_comment

begin_class
specifier|public
class|class
name|AuthnRequestBuilderTest
extends|extends
name|org
operator|.
name|junit
operator|.
name|Assert
block|{
static|static
block|{
name|OpenSAMLUtil
operator|.
name|initSamlEngine
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
name|testCreateAuthnRequest
parameter_list|()
throws|throws
name|Exception
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
name|Issuer
name|issuer
init|=
name|SamlpRequestComponentBuilder
operator|.
name|createIssuer
argument_list|(
literal|"http://localhost:9001/app"
argument_list|)
decl_stmt|;
name|NameIDPolicy
name|nameIDPolicy
init|=
name|SamlpRequestComponentBuilder
operator|.
name|createNameIDPolicy
argument_list|(
literal|true
argument_list|,
literal|"urn:oasis:names:tc:SAML:2.0:nameid-format:persistent"
argument_list|,
literal|"Issuer"
argument_list|)
decl_stmt|;
name|AuthnContextClassRef
name|authnCtxClassRef
init|=
name|SamlpRequestComponentBuilder
operator|.
name|createAuthnCtxClassRef
argument_list|(
literal|"urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport"
argument_list|)
decl_stmt|;
name|RequestedAuthnContext
name|authnCtx
init|=
name|SamlpRequestComponentBuilder
operator|.
name|createRequestedAuthnCtxPolicy
argument_list|(
name|AuthnContextComparisonTypeEnumeration
operator|.
name|EXACT
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|authnCtxClassRef
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|AuthnRequest
name|authnRequest
init|=
name|SamlpRequestComponentBuilder
operator|.
name|createAuthnRequest
argument_list|(
literal|"http://localhost:9001/sso"
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST"
argument_list|,
name|SAMLVersion
operator|.
name|VERSION_20
argument_list|,
name|issuer
argument_list|,
name|nameIDPolicy
argument_list|,
name|authnCtx
argument_list|)
decl_stmt|;
name|Element
name|policyElement
init|=
name|OpenSAMLUtil
operator|.
name|toDom
argument_list|(
name|authnRequest
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|doc
operator|.
name|appendChild
argument_list|(
name|policyElement
argument_list|)
expr_stmt|;
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
name|testAuthnRequestBuilder
parameter_list|()
throws|throws
name|Exception
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
name|AuthnRequestBuilder
name|authnRequestBuilder
init|=
operator|new
name|DefaultAuthnRequestBuilder
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|AuthnRequest
name|authnRequest
init|=
name|authnRequestBuilder
operator|.
name|createAuthnRequest
argument_list|(
name|message
argument_list|,
literal|"http://localhost:9001/app"
argument_list|,
literal|"http://localhost:9001/sso"
argument_list|)
decl_stmt|;
name|Element
name|policyElement
init|=
name|OpenSAMLUtil
operator|.
name|toDom
argument_list|(
name|authnRequest
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|doc
operator|.
name|appendChild
argument_list|(
name|policyElement
argument_list|)
expr_stmt|;
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

