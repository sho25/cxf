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
name|WSSConfig
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
name|SignableSAMLObject
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
name|Response
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
name|Status
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xml
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
name|xml
operator|.
name|security
operator|.
name|x509
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
name|xml
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
name|xml
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
name|xml
operator|.
name|signature
operator|.
name|SignatureConstants
import|;
end_import

begin_comment
comment|/**  * Some unit tests for the SAMLProtocolResponseValidator.  */
end_comment

begin_class
specifier|public
class|class
name|SAMLResponseValidatorTest
extends|extends
name|org
operator|.
name|junit
operator|.
name|Assert
block|{
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
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testCreateAndValidateResponse
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
name|Element
name|policyElement
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
name|policyElement
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|policyElement
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
name|policyElement
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
literal|null
argument_list|,
literal|null
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
name|testInvalidStatusCode
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
name|Status
name|status
init|=
name|SAML2PResponseComponentBuilder
operator|.
name|createStatus
argument_list|(
name|SAMLProtocolResponseValidator
operator|.
name|SAML1_STATUSCODE_SUCCESS
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
name|Element
name|policyElement
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
name|policyElement
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|policyElement
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
name|policyElement
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
try|try
block|{
name|validator
operator|.
name|validateSamlResponse
argument_list|(
name|marshalledResponse
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on an invalid SAML code"
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
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testResponseSignedAssertion
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
name|SAMLResponseValidatorTest
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
name|Element
name|policyElement
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
name|policyElement
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|policyElement
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
name|policyElement
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
try|try
block|{
name|validator
operator|.
name|validateSamlResponse
argument_list|(
name|marshalledResponse
argument_list|,
literal|null
argument_list|,
operator|new
name|KeystorePasswordCallback
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on no Signature Crypto"
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
comment|// Validate the Response
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
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSignedResponse
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
name|SAMLResponseValidatorTest
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
name|signResponse
argument_list|(
name|response
argument_list|,
literal|"alice"
argument_list|,
literal|"password"
argument_list|,
name|issuerCrypto
argument_list|)
expr_stmt|;
name|Element
name|policyElement
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
name|policyElement
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|policyElement
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
name|policyElement
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
try|try
block|{
name|validator
operator|.
name|validateSamlResponse
argument_list|(
name|marshalledResponse
argument_list|,
literal|null
argument_list|,
operator|new
name|KeystorePasswordCallback
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on no Signature Crypto"
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
comment|// Validate the Response
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
block|}
comment|/**      * Sign a SAML Response      * @throws Exception       */
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
argument_list|()
decl_stmt|;
name|signingCredential
operator|.
name|setEntityCertificate
argument_list|(
name|issuerCerts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|signingCredential
operator|.
name|setPrivateKey
argument_list|(
name|privateKey
argument_list|)
expr_stmt|;
name|signature
operator|.
name|setSigningCredential
argument_list|(
name|signingCredential
argument_list|)
expr_stmt|;
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
name|xml
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

