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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStore
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivateKey
import|;
end_import

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
name|wss4j
operator|.
name|common
operator|.
name|crypto
operator|.
name|Crypto
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
name|crypto
operator|.
name|CryptoType
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
name|crypto
operator|.
name|Merlin
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
name|OpenSAMLUtil
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
name|AudienceRestrictionBean
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
name|ConditionsBean
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
name|SubjectConfirmationDataBean
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
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|util
operator|.
name|Loader
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
name|dom
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
name|wss4j
operator|.
name|dom
operator|.
name|WSSConfig
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
name|saml
operator|.
name|common
operator|.
name|SignableSAMLObject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|common
operator|.
name|xml
operator|.
name|SAMLConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Status
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|security
operator|.
name|x509
operator|.
name|BasicX509Credential
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xmlsec
operator|.
name|keyinfo
operator|.
name|impl
operator|.
name|X509KeyInfoGeneratorFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xmlsec
operator|.
name|signature
operator|.
name|KeyInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xmlsec
operator|.
name|signature
operator|.
name|Signature
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xmlsec
operator|.
name|signature
operator|.
name|support
operator|.
name|SignatureConstants
import|;
end_import

begin_comment
comment|/**  * Some unit tests for the SAMLProtocolResponseValidator and the SAMLSSOResponseValidator  */
end_comment

begin_class
specifier|public
class|class
name|CombinedValidatorTest
extends|extends
name|org
operator|.
name|junit
operator|.
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|DocumentBuilderFactory
name|DOC_BUILDER_FACTORY
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
static|static
block|{
name|WSSConfig
operator|.
name|init
argument_list|()
expr_stmt|;
name|OpenSAMLUtil
operator|.
name|initSamlEngine
argument_list|()
expr_stmt|;
name|DOC_BUILDER_FACTORY
operator|.
name|setNamespaceAware
argument_list|(
literal|true
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
name|testSuccessfulValidation
parameter_list|()
throws|throws
name|Exception
block|{
name|DocumentBuilder
name|docBuilder
init|=
name|DOC_BUILDER_FACTORY
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
name|Response
name|response
init|=
name|createResponse
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|Element
name|responseElement
init|=
name|OpenSAMLUtil
operator|.
name|toDom
argument_list|(
name|response
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|doc
operator|.
name|appendChild
argument_list|(
name|responseElement
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|responseElement
argument_list|)
expr_stmt|;
name|Response
name|marshalledResponse
init|=
operator|(
name|Response
operator|)
name|OpenSAMLUtil
operator|.
name|fromDom
argument_list|(
name|responseElement
argument_list|)
decl_stmt|;
name|Crypto
name|issuerCrypto
init|=
operator|new
name|Merlin
argument_list|()
decl_stmt|;
name|KeyStore
name|keyStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|KeyStore
operator|.
name|getDefaultType
argument_list|()
argument_list|)
decl_stmt|;
name|ClassLoader
name|loader
init|=
name|Loader
operator|.
name|getClassLoader
argument_list|(
name|CombinedValidatorTest
operator|.
name|class
argument_list|)
decl_stmt|;
name|InputStream
name|input
init|=
name|Merlin
operator|.
name|loadInputStream
argument_list|(
name|loader
argument_list|,
literal|"alice.jks"
argument_list|)
decl_stmt|;
name|keyStore
operator|.
name|load
argument_list|(
name|input
argument_list|,
literal|"password"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Merlin
operator|)
name|issuerCrypto
operator|)
operator|.
name|setKeyStore
argument_list|(
name|keyStore
argument_list|)
expr_stmt|;
comment|// Validate the Response
name|SAMLProtocolResponseValidator
name|validator
init|=
operator|new
name|SAMLProtocolResponseValidator
argument_list|()
decl_stmt|;
name|validator
operator|.
name|validateSamlResponse
argument_list|(
name|marshalledResponse
argument_list|,
name|issuerCrypto
argument_list|,
operator|new
name|KeystorePasswordCallback
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test SSO validation
name|SAMLSSOResponseValidator
name|ssoValidator
init|=
operator|new
name|SAMLSSOResponseValidator
argument_list|()
decl_stmt|;
name|ssoValidator
operator|.
name|setIssuerIDP
argument_list|(
literal|"http://cxf.apache.org/issuer"
argument_list|)
expr_stmt|;
name|ssoValidator
operator|.
name|setAssertionConsumerURL
argument_list|(
literal|"http://recipient.apache.org"
argument_list|)
expr_stmt|;
name|ssoValidator
operator|.
name|setClientAddress
argument_list|(
literal|"http://apache.org"
argument_list|)
expr_stmt|;
name|ssoValidator
operator|.
name|setRequestId
argument_list|(
literal|"12345"
argument_list|)
expr_stmt|;
name|ssoValidator
operator|.
name|setSpIdentifier
argument_list|(
literal|"http://service.apache.org"
argument_list|)
expr_stmt|;
comment|// Parse the response
name|SSOValidatorResponse
name|ssoResponse
init|=
name|ssoValidator
operator|.
name|validateSamlResponse
argument_list|(
name|marshalledResponse
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|SamlAssertionWrapper
name|parsedAssertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|ssoResponse
operator|.
name|getAssertionElement
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"alice"
argument_list|,
name|parsedAssertion
operator|.
name|getSubjectName
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
name|testWrappingAttack3
parameter_list|()
throws|throws
name|Exception
block|{
name|DocumentBuilder
name|docBuilder
init|=
name|DOC_BUILDER_FACTORY
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
name|Response
name|response
init|=
name|createResponse
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|Element
name|responseElement
init|=
name|OpenSAMLUtil
operator|.
name|toDom
argument_list|(
name|response
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|doc
operator|.
name|appendChild
argument_list|(
name|responseElement
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|responseElement
argument_list|)
expr_stmt|;
comment|// Get Assertion Element
name|Element
name|assertionElement
init|=
operator|(
name|Element
operator|)
name|responseElement
operator|.
name|getElementsByTagNameNS
argument_list|(
name|SAMLConstants
operator|.
name|SAML20_NS
argument_list|,
literal|"Assertion"
argument_list|)
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|assertionElement
argument_list|)
expr_stmt|;
comment|// Clone it, strip the Signature, modify the Subject, change Subj Conf
name|Element
name|clonedAssertion
init|=
operator|(
name|Element
operator|)
name|assertionElement
operator|.
name|cloneNode
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|clonedAssertion
operator|.
name|setAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"ID"
argument_list|,
literal|"_12345623562"
argument_list|)
expr_stmt|;
name|Element
name|sigElement
init|=
operator|(
name|Element
operator|)
name|clonedAssertion
operator|.
name|getElementsByTagNameNS
argument_list|(
name|WSConstants
operator|.
name|SIG_NS
argument_list|,
literal|"Signature"
argument_list|)
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|clonedAssertion
operator|.
name|removeChild
argument_list|(
name|sigElement
argument_list|)
expr_stmt|;
name|Element
name|subjElement
init|=
operator|(
name|Element
operator|)
name|clonedAssertion
operator|.
name|getElementsByTagNameNS
argument_list|(
name|SAMLConstants
operator|.
name|SAML20_NS
argument_list|,
literal|"Subject"
argument_list|)
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Element
name|subjNameIdElement
init|=
operator|(
name|Element
operator|)
name|subjElement
operator|.
name|getElementsByTagNameNS
argument_list|(
name|SAMLConstants
operator|.
name|SAML20_NS
argument_list|,
literal|"NameID"
argument_list|)
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|subjNameIdElement
operator|.
name|setTextContent
argument_list|(
literal|"bob"
argument_list|)
expr_stmt|;
name|Element
name|subjConfElement
init|=
operator|(
name|Element
operator|)
name|subjElement
operator|.
name|getElementsByTagNameNS
argument_list|(
name|SAMLConstants
operator|.
name|SAML20_NS
argument_list|,
literal|"SubjectConfirmation"
argument_list|)
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|subjConfElement
operator|.
name|setAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Method"
argument_list|,
name|SAML2Constants
operator|.
name|CONF_SENDER_VOUCHES
argument_list|)
expr_stmt|;
comment|// Now insert the modified cloned Assertion into the Response before actual assertion
name|responseElement
operator|.
name|insertBefore
argument_list|(
name|clonedAssertion
argument_list|,
name|assertionElement
argument_list|)
expr_stmt|;
comment|// System.out.println(DOM2Writer.nodeToString(responseElement));
name|Response
name|marshalledResponse
init|=
operator|(
name|Response
operator|)
name|OpenSAMLUtil
operator|.
name|fromDom
argument_list|(
name|responseElement
argument_list|)
decl_stmt|;
name|Crypto
name|issuerCrypto
init|=
operator|new
name|Merlin
argument_list|()
decl_stmt|;
name|KeyStore
name|keyStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|KeyStore
operator|.
name|getDefaultType
argument_list|()
argument_list|)
decl_stmt|;
name|ClassLoader
name|loader
init|=
name|Loader
operator|.
name|getClassLoader
argument_list|(
name|CombinedValidatorTest
operator|.
name|class
argument_list|)
decl_stmt|;
name|InputStream
name|input
init|=
name|Merlin
operator|.
name|loadInputStream
argument_list|(
name|loader
argument_list|,
literal|"alice.jks"
argument_list|)
decl_stmt|;
name|keyStore
operator|.
name|load
argument_list|(
name|input
argument_list|,
literal|"password"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Merlin
operator|)
name|issuerCrypto
operator|)
operator|.
name|setKeyStore
argument_list|(
name|keyStore
argument_list|)
expr_stmt|;
comment|// Validate the Response
name|SAMLProtocolResponseValidator
name|validator
init|=
operator|new
name|SAMLProtocolResponseValidator
argument_list|()
decl_stmt|;
name|validator
operator|.
name|validateSamlResponse
argument_list|(
name|marshalledResponse
argument_list|,
name|issuerCrypto
argument_list|,
operator|new
name|KeystorePasswordCallback
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test SSO validation
name|SAMLSSOResponseValidator
name|ssoValidator
init|=
operator|new
name|SAMLSSOResponseValidator
argument_list|()
decl_stmt|;
name|ssoValidator
operator|.
name|setIssuerIDP
argument_list|(
literal|"http://cxf.apache.org/issuer"
argument_list|)
expr_stmt|;
name|ssoValidator
operator|.
name|setAssertionConsumerURL
argument_list|(
literal|"http://recipient.apache.org"
argument_list|)
expr_stmt|;
name|ssoValidator
operator|.
name|setClientAddress
argument_list|(
literal|"http://apache.org"
argument_list|)
expr_stmt|;
name|ssoValidator
operator|.
name|setRequestId
argument_list|(
literal|"12345"
argument_list|)
expr_stmt|;
name|ssoValidator
operator|.
name|setSpIdentifier
argument_list|(
literal|"http://service.apache.org"
argument_list|)
expr_stmt|;
comment|// Parse the response
name|SSOValidatorResponse
name|ssoResponse
init|=
name|ssoValidator
operator|.
name|validateSamlResponse
argument_list|(
name|marshalledResponse
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|SamlAssertionWrapper
name|parsedAssertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|ssoResponse
operator|.
name|getAssertionElement
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"alice"
argument_list|,
name|parsedAssertion
operator|.
name|getSubjectName
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
name|testSuccessfulSignedValidation
parameter_list|()
throws|throws
name|Exception
block|{
name|DocumentBuilder
name|docBuilder
init|=
name|DOC_BUILDER_FACTORY
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
name|Response
name|response
init|=
name|createResponse
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|Crypto
name|issuerCrypto
init|=
operator|new
name|Merlin
argument_list|()
decl_stmt|;
name|KeyStore
name|keyStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|KeyStore
operator|.
name|getDefaultType
argument_list|()
argument_list|)
decl_stmt|;
name|ClassLoader
name|loader
init|=
name|Loader
operator|.
name|getClassLoader
argument_list|(
name|CombinedValidatorTest
operator|.
name|class
argument_list|)
decl_stmt|;
name|InputStream
name|input
init|=
name|Merlin
operator|.
name|loadInputStream
argument_list|(
name|loader
argument_list|,
literal|"alice.jks"
argument_list|)
decl_stmt|;
name|keyStore
operator|.
name|load
argument_list|(
name|input
argument_list|,
literal|"password"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Merlin
operator|)
name|issuerCrypto
operator|)
operator|.
name|setKeyStore
argument_list|(
name|keyStore
argument_list|)
expr_stmt|;
name|signResponse
argument_list|(
name|response
argument_list|,
literal|"alice"
argument_list|,
literal|"password"
argument_list|,
name|issuerCrypto
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Element
name|responseElement
init|=
name|OpenSAMLUtil
operator|.
name|toDom
argument_list|(
name|response
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|doc
operator|.
name|appendChild
argument_list|(
name|responseElement
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|responseElement
argument_list|)
expr_stmt|;
name|Response
name|marshalledResponse
init|=
operator|(
name|Response
operator|)
name|OpenSAMLUtil
operator|.
name|fromDom
argument_list|(
name|responseElement
argument_list|)
decl_stmt|;
comment|// Validate the Response
name|SAMLProtocolResponseValidator
name|validator
init|=
operator|new
name|SAMLProtocolResponseValidator
argument_list|()
decl_stmt|;
name|validator
operator|.
name|validateSamlResponse
argument_list|(
name|marshalledResponse
argument_list|,
name|issuerCrypto
argument_list|,
operator|new
name|KeystorePasswordCallback
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test SSO validation
name|SAMLSSOResponseValidator
name|ssoValidator
init|=
operator|new
name|SAMLSSOResponseValidator
argument_list|()
decl_stmt|;
name|ssoValidator
operator|.
name|setIssuerIDP
argument_list|(
literal|"http://cxf.apache.org/issuer"
argument_list|)
expr_stmt|;
name|ssoValidator
operator|.
name|setAssertionConsumerURL
argument_list|(
literal|"http://recipient.apache.org"
argument_list|)
expr_stmt|;
name|ssoValidator
operator|.
name|setClientAddress
argument_list|(
literal|"http://apache.org"
argument_list|)
expr_stmt|;
name|ssoValidator
operator|.
name|setRequestId
argument_list|(
literal|"12345"
argument_list|)
expr_stmt|;
name|ssoValidator
operator|.
name|setSpIdentifier
argument_list|(
literal|"http://service.apache.org"
argument_list|)
expr_stmt|;
comment|// Parse the response
name|SSOValidatorResponse
name|ssoResponse
init|=
name|ssoValidator
operator|.
name|validateSamlResponse
argument_list|(
name|marshalledResponse
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|SamlAssertionWrapper
name|parsedAssertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|ssoResponse
operator|.
name|getAssertionElement
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"alice"
argument_list|,
name|parsedAssertion
operator|.
name|getSubjectName
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
name|testEnforceResponseSigned
parameter_list|()
throws|throws
name|Exception
block|{
name|DocumentBuilder
name|docBuilder
init|=
name|DOC_BUILDER_FACTORY
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
name|Response
name|response
init|=
name|createResponse
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|Element
name|responseElement
init|=
name|OpenSAMLUtil
operator|.
name|toDom
argument_list|(
name|response
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|doc
operator|.
name|appendChild
argument_list|(
name|responseElement
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|responseElement
argument_list|)
expr_stmt|;
name|Response
name|marshalledResponse
init|=
operator|(
name|Response
operator|)
name|OpenSAMLUtil
operator|.
name|fromDom
argument_list|(
name|responseElement
argument_list|)
decl_stmt|;
name|Crypto
name|issuerCrypto
init|=
operator|new
name|Merlin
argument_list|()
decl_stmt|;
name|KeyStore
name|keyStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|KeyStore
operator|.
name|getDefaultType
argument_list|()
argument_list|)
decl_stmt|;
name|ClassLoader
name|loader
init|=
name|Loader
operator|.
name|getClassLoader
argument_list|(
name|CombinedValidatorTest
operator|.
name|class
argument_list|)
decl_stmt|;
name|InputStream
name|input
init|=
name|Merlin
operator|.
name|loadInputStream
argument_list|(
name|loader
argument_list|,
literal|"alice.jks"
argument_list|)
decl_stmt|;
name|keyStore
operator|.
name|load
argument_list|(
name|input
argument_list|,
literal|"password"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Merlin
operator|)
name|issuerCrypto
operator|)
operator|.
name|setKeyStore
argument_list|(
name|keyStore
argument_list|)
expr_stmt|;
comment|// Validate the Response
name|SAMLProtocolResponseValidator
name|validator
init|=
operator|new
name|SAMLProtocolResponseValidator
argument_list|()
decl_stmt|;
name|validator
operator|.
name|validateSamlResponse
argument_list|(
name|marshalledResponse
argument_list|,
name|issuerCrypto
argument_list|,
operator|new
name|KeystorePasswordCallback
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test SSO validation
name|SAMLSSOResponseValidator
name|ssoValidator
init|=
operator|new
name|SAMLSSOResponseValidator
argument_list|()
decl_stmt|;
name|ssoValidator
operator|.
name|setIssuerIDP
argument_list|(
literal|"http://cxf.apache.org/issuer"
argument_list|)
expr_stmt|;
name|ssoValidator
operator|.
name|setAssertionConsumerURL
argument_list|(
literal|"http://recipient.apache.org"
argument_list|)
expr_stmt|;
name|ssoValidator
operator|.
name|setClientAddress
argument_list|(
literal|"http://apache.org"
argument_list|)
expr_stmt|;
name|ssoValidator
operator|.
name|setRequestId
argument_list|(
literal|"12345"
argument_list|)
expr_stmt|;
name|ssoValidator
operator|.
name|setSpIdentifier
argument_list|(
literal|"http://service.apache.org"
argument_list|)
expr_stmt|;
name|ssoValidator
operator|.
name|setEnforceResponseSigned
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Parse the response
try|try
block|{
name|ssoValidator
operator|.
name|validateSamlResponse
argument_list|(
name|marshalledResponse
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on an unsigned Response"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
specifier|private
name|Response
name|createResponse
parameter_list|(
name|Document
name|doc
parameter_list|)
throws|throws
name|Exception
block|{
name|Status
name|status
init|=
name|SAML2PResponseComponentBuilder
operator|.
name|createStatus
argument_list|(
name|SAMLProtocolResponseValidator
operator|.
name|SAML2_STATUSCODE_SUCCESS
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
name|SAML2PResponseComponentBuilder
operator|.
name|createSAMLResponse
argument_list|(
literal|"http://cxf.apache.org/saml"
argument_list|,
literal|"http://cxf.apache.org/issuer"
argument_list|,
name|status
argument_list|)
decl_stmt|;
name|response
operator|.
name|setDestination
argument_list|(
literal|"http://recipient.apache.org"
argument_list|)
expr_stmt|;
comment|// Create an AuthenticationAssertion
name|SAML2CallbackHandler
name|callbackHandler
init|=
operator|new
name|SAML2CallbackHandler
argument_list|()
decl_stmt|;
name|callbackHandler
operator|.
name|setStatement
argument_list|(
name|SAML2CallbackHandler
operator|.
name|Statement
operator|.
name|AUTHN
argument_list|)
expr_stmt|;
name|callbackHandler
operator|.
name|setIssuer
argument_list|(
literal|"http://cxf.apache.org/issuer"
argument_list|)
expr_stmt|;
name|callbackHandler
operator|.
name|setConfirmationMethod
argument_list|(
name|SAML2Constants
operator|.
name|CONF_BEARER
argument_list|)
expr_stmt|;
name|callbackHandler
operator|.
name|setSubjectName
argument_list|(
literal|"alice"
argument_list|)
expr_stmt|;
name|SubjectConfirmationDataBean
name|subjectConfirmationData
init|=
operator|new
name|SubjectConfirmationDataBean
argument_list|()
decl_stmt|;
name|subjectConfirmationData
operator|.
name|setAddress
argument_list|(
literal|"http://apache.org"
argument_list|)
expr_stmt|;
name|subjectConfirmationData
operator|.
name|setInResponseTo
argument_list|(
literal|"12345"
argument_list|)
expr_stmt|;
name|subjectConfirmationData
operator|.
name|setNotAfter
argument_list|(
operator|new
name|DateTime
argument_list|()
operator|.
name|plusMinutes
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|subjectConfirmationData
operator|.
name|setRecipient
argument_list|(
literal|"http://recipient.apache.org"
argument_list|)
expr_stmt|;
name|callbackHandler
operator|.
name|setSubjectConfirmationData
argument_list|(
name|subjectConfirmationData
argument_list|)
expr_stmt|;
name|ConditionsBean
name|conditions
init|=
operator|new
name|ConditionsBean
argument_list|()
decl_stmt|;
name|conditions
operator|.
name|setNotBefore
argument_list|(
operator|new
name|DateTime
argument_list|()
argument_list|)
expr_stmt|;
name|conditions
operator|.
name|setNotAfter
argument_list|(
operator|new
name|DateTime
argument_list|()
operator|.
name|plusMinutes
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|AudienceRestrictionBean
name|audienceRestriction
init|=
operator|new
name|AudienceRestrictionBean
argument_list|()
decl_stmt|;
name|audienceRestriction
operator|.
name|setAudienceURIs
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"http://service.apache.org"
argument_list|)
argument_list|)
expr_stmt|;
name|conditions
operator|.
name|setAudienceRestrictions
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|audienceRestriction
argument_list|)
argument_list|)
expr_stmt|;
name|callbackHandler
operator|.
name|setConditions
argument_list|(
name|conditions
argument_list|)
expr_stmt|;
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
name|callbackHandler
argument_list|,
name|samlCallback
argument_list|)
expr_stmt|;
name|SamlAssertionWrapper
name|assertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|samlCallback
argument_list|)
decl_stmt|;
name|Crypto
name|issuerCrypto
init|=
operator|new
name|Merlin
argument_list|()
decl_stmt|;
name|KeyStore
name|keyStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|KeyStore
operator|.
name|getDefaultType
argument_list|()
argument_list|)
decl_stmt|;
name|ClassLoader
name|loader
init|=
name|Loader
operator|.
name|getClassLoader
argument_list|(
name|CombinedValidatorTest
operator|.
name|class
argument_list|)
decl_stmt|;
name|InputStream
name|input
init|=
name|Merlin
operator|.
name|loadInputStream
argument_list|(
name|loader
argument_list|,
literal|"alice.jks"
argument_list|)
decl_stmt|;
name|keyStore
operator|.
name|load
argument_list|(
name|input
argument_list|,
literal|"password"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Merlin
operator|)
name|issuerCrypto
operator|)
operator|.
name|setKeyStore
argument_list|(
name|keyStore
argument_list|)
expr_stmt|;
name|assertion
operator|.
name|signAssertion
argument_list|(
literal|"alice"
argument_list|,
literal|"password"
argument_list|,
name|issuerCrypto
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|response
operator|.
name|getAssertions
argument_list|()
operator|.
name|add
argument_list|(
name|assertion
operator|.
name|getSaml2
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
specifier|private
name|void
name|signResponse
parameter_list|(
name|Response
name|response
parameter_list|,
name|String
name|issuerKeyName
parameter_list|,
name|String
name|issuerKeyPassword
parameter_list|,
name|Crypto
name|issuerCrypto
parameter_list|,
name|boolean
name|useKeyInfo
parameter_list|)
throws|throws
name|Exception
block|{
comment|//
comment|// Create the signature
comment|//
name|Signature
name|signature
init|=
name|OpenSAMLUtil
operator|.
name|buildSignature
argument_list|()
decl_stmt|;
name|signature
operator|.
name|setCanonicalizationAlgorithm
argument_list|(
name|SignatureConstants
operator|.
name|ALGO_ID_C14N_EXCL_OMIT_COMMENTS
argument_list|)
expr_stmt|;
comment|// prepare to sign the SAML token
name|CryptoType
name|cryptoType
init|=
operator|new
name|CryptoType
argument_list|(
name|CryptoType
operator|.
name|TYPE
operator|.
name|ALIAS
argument_list|)
decl_stmt|;
name|cryptoType
operator|.
name|setAlias
argument_list|(
name|issuerKeyName
argument_list|)
expr_stmt|;
name|X509Certificate
index|[]
name|issuerCerts
init|=
name|issuerCrypto
operator|.
name|getX509Certificates
argument_list|(
name|cryptoType
argument_list|)
decl_stmt|;
if|if
condition|(
name|issuerCerts
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"No issuer certs were found to sign the SAML Assertion using issuer name: "
operator|+
name|issuerKeyName
argument_list|)
throw|;
block|}
name|String
name|sigAlgo
init|=
name|SignatureConstants
operator|.
name|ALGO_ID_SIGNATURE_RSA_SHA1
decl_stmt|;
name|String
name|pubKeyAlgo
init|=
name|issuerCerts
index|[
literal|0
index|]
operator|.
name|getPublicKey
argument_list|()
operator|.
name|getAlgorithm
argument_list|()
decl_stmt|;
if|if
condition|(
name|pubKeyAlgo
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DSA"
argument_list|)
condition|)
block|{
name|sigAlgo
operator|=
name|SignatureConstants
operator|.
name|ALGO_ID_SIGNATURE_DSA
expr_stmt|;
block|}
name|PrivateKey
name|privateKey
init|=
name|issuerCrypto
operator|.
name|getPrivateKey
argument_list|(
name|issuerKeyName
argument_list|,
name|issuerKeyPassword
argument_list|)
decl_stmt|;
name|signature
operator|.
name|setSignatureAlgorithm
argument_list|(
name|sigAlgo
argument_list|)
expr_stmt|;
name|BasicX509Credential
name|signingCredential
init|=
operator|new
name|BasicX509Credential
argument_list|(
name|issuerCerts
index|[
literal|0
index|]
argument_list|,
name|privateKey
argument_list|)
decl_stmt|;
name|signature
operator|.
name|setSigningCredential
argument_list|(
name|signingCredential
argument_list|)
expr_stmt|;
if|if
condition|(
name|useKeyInfo
condition|)
block|{
name|X509KeyInfoGeneratorFactory
name|kiFactory
init|=
operator|new
name|X509KeyInfoGeneratorFactory
argument_list|()
decl_stmt|;
name|kiFactory
operator|.
name|setEmitEntityCertificate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|KeyInfo
name|keyInfo
init|=
name|kiFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|generate
argument_list|(
name|signingCredential
argument_list|)
decl_stmt|;
name|signature
operator|.
name|setKeyInfo
argument_list|(
name|keyInfo
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|org
operator|.
name|opensaml
operator|.
name|security
operator|.
name|SecurityException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Error generating KeyInfo from signing credential"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
comment|// add the signature to the assertion
name|SignableSAMLObject
name|signableObject
init|=
operator|(
name|SignableSAMLObject
operator|)
name|response
decl_stmt|;
name|signableObject
operator|.
name|setSignature
argument_list|(
name|signature
argument_list|)
expr_stmt|;
name|signableObject
operator|.
name|releaseDOM
argument_list|()
expr_stmt|;
name|signableObject
operator|.
name|releaseChildrenDOM
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

