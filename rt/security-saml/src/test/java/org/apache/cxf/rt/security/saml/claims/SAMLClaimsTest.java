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
operator|.
name|claims
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|cxf
operator|.
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|Claim
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
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|ClaimCollection
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
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|SAMLClaim
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
name|rt
operator|.
name|security
operator|.
name|saml
operator|.
name|utils
operator|.
name|SAMLUtils
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
name|SAMLUtil
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
name|builder
operator|.
name|SAML2Constants
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|SAMLClaimsTest
block|{
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSAML2Claims
parameter_list|()
throws|throws
name|Exception
block|{
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
name|SAMLClaim
operator|.
name|SAML_ROLE_ATTRIBUTENAME_DEFAULT
argument_list|)
expr_stmt|;
name|attributeBean
operator|.
name|setNameFormat
argument_list|(
name|SAML2Constants
operator|.
name|ATTRNAME_FORMAT_UNSPECIFIED
argument_list|)
expr_stmt|;
name|attributeBean
operator|.
name|addAttributeValue
argument_list|(
literal|"employee"
argument_list|)
expr_stmt|;
name|SamlCallbackHandler
name|samlCallbackHandler
init|=
operator|new
name|SamlCallbackHandler
argument_list|()
decl_stmt|;
name|samlCallbackHandler
operator|.
name|setAttributes
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|attributeBean
argument_list|)
argument_list|)
expr_stmt|;
comment|// Create the SAML Assertion via the CallbackHandler
name|SAMLCallback
name|samlCallback
init|=
operator|new
name|SAMLCallback
argument_list|()
decl_stmt|;
name|SAMLUtil
operator|.
name|doSAMLCallback
argument_list|(
name|samlCallbackHandler
argument_list|,
name|samlCallback
argument_list|)
expr_stmt|;
name|SamlAssertionWrapper
name|samlAssertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|samlCallback
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|newDocument
argument_list|()
decl_stmt|;
name|samlAssertion
operator|.
name|toDOM
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|ClaimCollection
name|claims
init|=
name|SAMLUtils
operator|.
name|getClaims
argument_list|(
name|samlAssertion
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|claims
operator|.
name|getDialect
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|claims
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Check Claim values
name|Claim
name|claim
init|=
name|claims
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|claim
operator|.
name|getClaimType
argument_list|()
argument_list|,
name|SAMLClaim
operator|.
name|SAML_ROLE_ATTRIBUTENAME_DEFAULT
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|claim
operator|.
name|getValues
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|claim
operator|.
name|getValues
argument_list|()
operator|.
name|contains
argument_list|(
literal|"employee"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Check SAMLClaim values
name|assertTrue
argument_list|(
name|claim
operator|instanceof
name|SAMLClaim
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SAMLClaim
operator|.
name|SAML_ROLE_ATTRIBUTENAME_DEFAULT
argument_list|,
operator|(
operator|(
name|SAMLClaim
operator|)
name|claim
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SAML2Constants
operator|.
name|ATTRNAME_FORMAT_UNSPECIFIED
argument_list|,
operator|(
operator|(
name|SAMLClaim
operator|)
name|claim
operator|)
operator|.
name|getNameFormat
argument_list|()
argument_list|)
expr_stmt|;
comment|// Check roles
name|Set
argument_list|<
name|Principal
argument_list|>
name|roles
init|=
name|SAMLUtils
operator|.
name|parseRolesFromClaims
argument_list|(
name|claims
argument_list|,
name|SAMLClaim
operator|.
name|SAML_ROLE_ATTRIBUTENAME_DEFAULT
argument_list|,
name|SAML2Constants
operator|.
name|ATTRNAME_FORMAT_UNSPECIFIED
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|roles
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Principal
name|p
init|=
name|roles
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"employee"
argument_list|,
name|p
operator|.
name|getName
argument_list|()
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
name|testSAML2MultipleRoles
parameter_list|()
throws|throws
name|Exception
block|{
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
name|SAMLClaim
operator|.
name|SAML_ROLE_ATTRIBUTENAME_DEFAULT
argument_list|)
expr_stmt|;
name|attributeBean
operator|.
name|setNameFormat
argument_list|(
name|SAML2Constants
operator|.
name|ATTRNAME_FORMAT_UNSPECIFIED
argument_list|)
expr_stmt|;
name|attributeBean
operator|.
name|addAttributeValue
argument_list|(
literal|"employee"
argument_list|)
expr_stmt|;
name|attributeBean
operator|.
name|addAttributeValue
argument_list|(
literal|"boss"
argument_list|)
expr_stmt|;
name|SamlCallbackHandler
name|samlCallbackHandler
init|=
operator|new
name|SamlCallbackHandler
argument_list|()
decl_stmt|;
name|samlCallbackHandler
operator|.
name|setAttributes
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|attributeBean
argument_list|)
argument_list|)
expr_stmt|;
comment|// Create the SAML Assertion via the CallbackHandler
name|SAMLCallback
name|samlCallback
init|=
operator|new
name|SAMLCallback
argument_list|()
decl_stmt|;
name|SAMLUtil
operator|.
name|doSAMLCallback
argument_list|(
name|samlCallbackHandler
argument_list|,
name|samlCallback
argument_list|)
expr_stmt|;
name|SamlAssertionWrapper
name|samlAssertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|samlCallback
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|newDocument
argument_list|()
decl_stmt|;
name|samlAssertion
operator|.
name|toDOM
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|ClaimCollection
name|claims
init|=
name|SAMLUtils
operator|.
name|getClaims
argument_list|(
name|samlAssertion
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|claims
operator|.
name|getDialect
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|claims
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Check Claim values
name|Claim
name|claim
init|=
name|claims
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|claim
operator|.
name|getClaimType
argument_list|()
argument_list|,
name|SAMLClaim
operator|.
name|SAML_ROLE_ATTRIBUTENAME_DEFAULT
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|claim
operator|.
name|getValues
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|claim
operator|.
name|getValues
argument_list|()
operator|.
name|contains
argument_list|(
literal|"employee"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|claim
operator|.
name|getValues
argument_list|()
operator|.
name|contains
argument_list|(
literal|"boss"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Check SAMLClaim values
name|assertTrue
argument_list|(
name|claim
operator|instanceof
name|SAMLClaim
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SAMLClaim
operator|.
name|SAML_ROLE_ATTRIBUTENAME_DEFAULT
argument_list|,
operator|(
operator|(
name|SAMLClaim
operator|)
name|claim
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SAML2Constants
operator|.
name|ATTRNAME_FORMAT_UNSPECIFIED
argument_list|,
operator|(
operator|(
name|SAMLClaim
operator|)
name|claim
operator|)
operator|.
name|getNameFormat
argument_list|()
argument_list|)
expr_stmt|;
comment|// Check roles
name|Set
argument_list|<
name|Principal
argument_list|>
name|roles
init|=
name|SAMLUtils
operator|.
name|parseRolesFromClaims
argument_list|(
name|claims
argument_list|,
name|SAMLClaim
operator|.
name|SAML_ROLE_ATTRIBUTENAME_DEFAULT
argument_list|,
name|SAML2Constants
operator|.
name|ATTRNAME_FORMAT_UNSPECIFIED
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|roles
operator|.
name|size
argument_list|()
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
name|testSAML2MultipleClaims
parameter_list|()
throws|throws
name|Exception
block|{
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
name|SAMLClaim
operator|.
name|SAML_ROLE_ATTRIBUTENAME_DEFAULT
argument_list|)
expr_stmt|;
name|attributeBean
operator|.
name|setNameFormat
argument_list|(
name|SAML2Constants
operator|.
name|ATTRNAME_FORMAT_UNSPECIFIED
argument_list|)
expr_stmt|;
name|attributeBean
operator|.
name|addAttributeValue
argument_list|(
literal|"employee"
argument_list|)
expr_stmt|;
name|AttributeBean
name|attributeBean2
init|=
operator|new
name|AttributeBean
argument_list|()
decl_stmt|;
name|attributeBean2
operator|.
name|setQualifiedName
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/givenname"
argument_list|)
expr_stmt|;
name|attributeBean2
operator|.
name|setNameFormat
argument_list|(
name|SAML2Constants
operator|.
name|ATTRNAME_FORMAT_UNSPECIFIED
argument_list|)
expr_stmt|;
name|attributeBean2
operator|.
name|addAttributeValue
argument_list|(
literal|"smith"
argument_list|)
expr_stmt|;
name|SamlCallbackHandler
name|samlCallbackHandler
init|=
operator|new
name|SamlCallbackHandler
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|AttributeBean
argument_list|>
name|attributes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|attributes
operator|.
name|add
argument_list|(
name|attributeBean
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|add
argument_list|(
name|attributeBean2
argument_list|)
expr_stmt|;
name|samlCallbackHandler
operator|.
name|setAttributes
argument_list|(
name|attributes
argument_list|)
expr_stmt|;
comment|// Create the SAML Assertion via the CallbackHandler
name|SAMLCallback
name|samlCallback
init|=
operator|new
name|SAMLCallback
argument_list|()
decl_stmt|;
name|SAMLUtil
operator|.
name|doSAMLCallback
argument_list|(
name|samlCallbackHandler
argument_list|,
name|samlCallback
argument_list|)
expr_stmt|;
name|SamlAssertionWrapper
name|samlAssertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|samlCallback
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|newDocument
argument_list|()
decl_stmt|;
name|samlAssertion
operator|.
name|toDOM
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|ClaimCollection
name|claims
init|=
name|SAMLUtils
operator|.
name|getClaims
argument_list|(
name|samlAssertion
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|claims
operator|.
name|getDialect
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|claims
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Check roles
name|Set
argument_list|<
name|Principal
argument_list|>
name|roles
init|=
name|SAMLUtils
operator|.
name|parseRolesFromClaims
argument_list|(
name|claims
argument_list|,
name|SAMLClaim
operator|.
name|SAML_ROLE_ATTRIBUTENAME_DEFAULT
argument_list|,
name|SAML2Constants
operator|.
name|ATTRNAME_FORMAT_UNSPECIFIED
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|roles
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Principal
name|p
init|=
name|roles
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"employee"
argument_list|,
name|p
operator|.
name|getName
argument_list|()
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
name|testSAML1Claims
parameter_list|()
throws|throws
name|Exception
block|{
name|AttributeBean
name|attributeBean
init|=
operator|new
name|AttributeBean
argument_list|()
decl_stmt|;
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
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims"
argument_list|)
expr_stmt|;
name|attributeBean
operator|.
name|addAttributeValue
argument_list|(
literal|"employee"
argument_list|)
expr_stmt|;
name|SamlCallbackHandler
name|samlCallbackHandler
init|=
operator|new
name|SamlCallbackHandler
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|samlCallbackHandler
operator|.
name|setAttributes
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|attributeBean
argument_list|)
argument_list|)
expr_stmt|;
comment|// Create the SAML Assertion via the CallbackHandler
name|SAMLCallback
name|samlCallback
init|=
operator|new
name|SAMLCallback
argument_list|()
decl_stmt|;
name|SAMLUtil
operator|.
name|doSAMLCallback
argument_list|(
name|samlCallbackHandler
argument_list|,
name|samlCallback
argument_list|)
expr_stmt|;
name|SamlAssertionWrapper
name|samlAssertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|samlCallback
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|newDocument
argument_list|()
decl_stmt|;
name|samlAssertion
operator|.
name|toDOM
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|ClaimCollection
name|claims
init|=
name|SAMLUtils
operator|.
name|getClaims
argument_list|(
name|samlAssertion
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|claims
operator|.
name|getDialect
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|claims
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Check Claim values
name|Claim
name|claim
init|=
name|claims
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|claim
operator|.
name|getClaimType
argument_list|()
argument_list|,
name|SAMLClaim
operator|.
name|SAML_ROLE_ATTRIBUTENAME_DEFAULT
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|claim
operator|.
name|getValues
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|claim
operator|.
name|getValues
argument_list|()
operator|.
name|contains
argument_list|(
literal|"employee"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Check SAMLClaim values
name|assertTrue
argument_list|(
name|claim
operator|instanceof
name|SAMLClaim
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"role"
argument_list|,
operator|(
operator|(
name|SAMLClaim
operator|)
name|claim
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// Check roles
name|Set
argument_list|<
name|Principal
argument_list|>
name|roles
init|=
name|SAMLUtils
operator|.
name|parseRolesFromClaims
argument_list|(
name|claims
argument_list|,
literal|"role"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|roles
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Principal
name|p
init|=
name|roles
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"employee"
argument_list|,
name|p
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

