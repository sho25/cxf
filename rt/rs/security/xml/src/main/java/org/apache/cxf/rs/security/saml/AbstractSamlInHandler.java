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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PublicKey
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
name|Certificate
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
name|ArrayList
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
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|container
operator|.
name|ContainerRequestFilter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|container
operator|.
name|PreMatching
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|jaxrs
operator|.
name|utils
operator|.
name|ExceptionUtils
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
name|jaxrs
operator|.
name|utils
operator|.
name|JAXRSUtils
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
name|MessageUtils
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
name|rs
operator|.
name|security
operator|.
name|common
operator|.
name|CryptoLoader
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
name|rs
operator|.
name|security
operator|.
name|common
operator|.
name|SecurityUtils
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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|authorization
operator|.
name|SecurityContextProvider
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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|authorization
operator|.
name|SecurityContextProviderImpl
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
name|security
operator|.
name|SecurityContext
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
name|security
operator|.
name|transport
operator|.
name|TLSSessionInfo
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
name|staxutils
operator|.
name|StaxUtils
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
name|ws
operator|.
name|security
operator|.
name|SecurityConstants
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
name|SAMLKeyInfo
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
name|dom
operator|.
name|WSDocInfo
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
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|handler
operator|.
name|RequestData
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
name|handler
operator|.
name|WSHandlerConstants
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
name|saml
operator|.
name|WSSSAMLKeyInfoProcessor
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
name|validate
operator|.
name|Credential
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
name|validate
operator|.
name|SamlAssertionValidator
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
name|validate
operator|.
name|Validator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|signature
operator|.
name|XMLSignature
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

begin_class
annotation|@
name|PreMatching
specifier|public
specifier|abstract
class|class
name|AbstractSamlInHandler
implements|implements
name|ContainerRequestFilter
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AbstractSamlInHandler
operator|.
name|class
argument_list|)
decl_stmt|;
static|static
block|{
name|WSSConfig
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
specifier|private
name|Validator
name|samlValidator
init|=
operator|new
name|SamlAssertionValidator
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|keyInfoMustBeAvailable
init|=
literal|true
decl_stmt|;
specifier|private
name|SecurityContextProvider
name|scProvider
init|=
operator|new
name|SecurityContextProviderImpl
argument_list|()
decl_stmt|;
specifier|public
name|void
name|setValidator
parameter_list|(
name|Validator
name|validator
parameter_list|)
block|{
name|samlValidator
operator|=
name|validator
expr_stmt|;
block|}
specifier|public
name|void
name|setSecurityContextProvider
parameter_list|(
name|SecurityContextProvider
name|p
parameter_list|)
block|{
name|scProvider
operator|=
name|p
expr_stmt|;
block|}
specifier|protected
name|void
name|validateToken
parameter_list|(
name|Message
name|message
parameter_list|,
name|InputStream
name|tokenStream
parameter_list|)
block|{
name|Element
name|token
init|=
name|readToken
argument_list|(
name|message
argument_list|,
name|tokenStream
argument_list|)
decl_stmt|;
name|validateToken
argument_list|(
name|message
argument_list|,
name|token
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Element
name|readToken
parameter_list|(
name|Message
name|message
parameter_list|,
name|InputStream
name|tokenStream
parameter_list|)
block|{
try|try
block|{
name|Document
name|doc
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|tokenStream
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|doc
operator|.
name|getDocumentElement
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|throwFault
argument_list|(
literal|"Assertion can not be read as XML document"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|void
name|validateToken
parameter_list|(
name|Message
name|message
parameter_list|,
name|Element
name|tokenElement
parameter_list|)
block|{
name|validateToken
argument_list|(
name|message
argument_list|,
name|toWrapper
argument_list|(
name|tokenElement
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|SamlAssertionWrapper
name|toWrapper
parameter_list|(
name|Element
name|tokenElement
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|SamlAssertionWrapper
argument_list|(
name|tokenElement
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|throwFault
argument_list|(
literal|"Assertion can not be validated"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|void
name|validateToken
parameter_list|(
name|Message
name|message
parameter_list|,
name|SamlAssertionWrapper
name|assertion
parameter_list|)
block|{
try|try
block|{
name|RequestData
name|data
init|=
operator|new
name|RequestData
argument_list|()
decl_stmt|;
comment|// Add Audience Restrictions for SAML
name|configureAudienceRestriction
argument_list|(
name|message
argument_list|,
name|data
argument_list|)
expr_stmt|;
if|if
condition|(
name|assertion
operator|.
name|isSigned
argument_list|()
condition|)
block|{
name|WSSConfig
name|cfg
init|=
name|WSSConfig
operator|.
name|getNewInstance
argument_list|()
decl_stmt|;
name|data
operator|.
name|setWssConfig
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|data
operator|.
name|setCallbackHandler
argument_list|(
name|SecurityUtils
operator|.
name|getCallbackHandler
argument_list|(
name|message
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|data
operator|.
name|setSigVerCrypto
argument_list|(
operator|new
name|CryptoLoader
argument_list|()
operator|.
name|getCrypto
argument_list|(
name|message
argument_list|,
name|SecurityConstants
operator|.
name|SIGNATURE_CRYPTO
argument_list|,
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|throwFault
argument_list|(
literal|"Crypto can not be loaded"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
name|data
operator|.
name|setEnableRevocation
argument_list|(
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|ENABLE_REVOCATION
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Signature
name|sig
init|=
name|assertion
operator|.
name|getSignature
argument_list|()
decl_stmt|;
name|WSDocInfo
name|docInfo
init|=
operator|new
name|WSDocInfo
argument_list|(
name|sig
operator|.
name|getDOM
argument_list|()
operator|.
name|getOwnerDocument
argument_list|()
argument_list|)
decl_stmt|;
name|SAMLKeyInfo
name|samlKeyInfo
init|=
literal|null
decl_stmt|;
name|KeyInfo
name|keyInfo
init|=
name|sig
operator|.
name|getKeyInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|keyInfo
operator|!=
literal|null
condition|)
block|{
name|samlKeyInfo
operator|=
name|SAMLUtil
operator|.
name|getCredentialFromKeyInfo
argument_list|(
name|keyInfo
operator|.
name|getDOM
argument_list|()
argument_list|,
operator|new
name|WSSSAMLKeyInfoProcessor
argument_list|(
name|data
argument_list|,
name|docInfo
argument_list|)
argument_list|,
name|data
operator|.
name|getSigVerCrypto
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|keyInfoMustBeAvailable
condition|)
block|{
name|samlKeyInfo
operator|=
name|createKeyInfoFromDefaultAlias
argument_list|(
name|data
operator|.
name|getSigVerCrypto
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertion
operator|.
name|verifySignature
argument_list|(
name|samlKeyInfo
argument_list|)
expr_stmt|;
name|assertion
operator|.
name|parseSubject
argument_list|(
operator|new
name|WSSSAMLKeyInfoProcessor
argument_list|(
name|data
argument_list|,
literal|null
argument_list|)
argument_list|,
name|data
operator|.
name|getSigVerCrypto
argument_list|()
argument_list|,
name|data
operator|.
name|getCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getTLSCertificates
argument_list|(
name|message
argument_list|)
operator|==
literal|null
condition|)
block|{
name|throwFault
argument_list|(
literal|"Assertion must be signed"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|samlValidator
operator|!=
literal|null
condition|)
block|{
name|Credential
name|credential
init|=
operator|new
name|Credential
argument_list|()
decl_stmt|;
name|credential
operator|.
name|setSamlAssertion
argument_list|(
name|assertion
argument_list|)
expr_stmt|;
name|samlValidator
operator|.
name|validate
argument_list|(
name|credential
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
name|checkSubjectConfirmationData
argument_list|(
name|message
argument_list|,
name|assertion
argument_list|)
expr_stmt|;
name|setSecurityContext
argument_list|(
name|message
argument_list|,
name|assertion
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|throwFault
argument_list|(
literal|"Assertion can not be validated"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|configureAudienceRestriction
parameter_list|(
name|Message
name|msg
parameter_list|,
name|RequestData
name|reqData
parameter_list|)
block|{
comment|// Add Audience Restrictions for SAML
name|boolean
name|enableAudienceRestriction
init|=
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|msg
argument_list|,
name|SecurityConstants
operator|.
name|AUDIENCE_RESTRICTION_VALIDATION
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|enableAudienceRestriction
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|audiences
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|msg
operator|.
name|getContextualProperty
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|REQUEST_URL
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|audiences
operator|.
name|add
argument_list|(
operator|(
name|String
operator|)
name|msg
operator|.
name|getContextualProperty
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|REQUEST_URL
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|reqData
operator|.
name|setAudienceRestrictions
argument_list|(
name|audiences
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|SAMLKeyInfo
name|createKeyInfoFromDefaultAlias
parameter_list|(
name|Crypto
name|sigCrypto
parameter_list|)
throws|throws
name|WSSecurityException
block|{
try|try
block|{
name|X509Certificate
index|[]
name|certs
init|=
name|SecurityUtils
operator|.
name|getCertificates
argument_list|(
name|sigCrypto
argument_list|,
name|sigCrypto
operator|.
name|getDefaultX509Identifier
argument_list|()
argument_list|)
decl_stmt|;
name|SAMLKeyInfo
name|samlKeyInfo
init|=
operator|new
name|SAMLKeyInfo
argument_list|(
operator|new
name|X509Certificate
index|[]
block|{
name|certs
index|[
literal|0
index|]
block|}
argument_list|)
decl_stmt|;
name|samlKeyInfo
operator|.
name|setPublicKey
argument_list|(
name|certs
index|[
literal|0
index|]
operator|.
name|getPublicKey
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|samlKeyInfo
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Error in loading the certificates: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILED_SIGNATURE
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|checkSubjectConfirmationData
parameter_list|(
name|Message
name|message
parameter_list|,
name|SamlAssertionWrapper
name|assertion
parameter_list|)
block|{
name|Certificate
index|[]
name|tlsCerts
init|=
name|getTLSCertificates
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|checkHolderOfKey
argument_list|(
name|message
argument_list|,
name|assertion
argument_list|,
name|tlsCerts
argument_list|)
condition|)
block|{
name|throwFault
argument_list|(
literal|"Holder Of Key claim fails"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|checkSenderVouches
argument_list|(
name|message
argument_list|,
name|assertion
argument_list|,
name|tlsCerts
argument_list|)
condition|)
block|{
name|throwFault
argument_list|(
literal|"Sender vouchers claim fails"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|checkBearer
argument_list|(
name|assertion
argument_list|,
name|tlsCerts
argument_list|)
condition|)
block|{
name|throwFault
argument_list|(
literal|"Bearer claim fails"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|setSecurityContext
parameter_list|(
name|Message
name|message
parameter_list|,
name|SamlAssertionWrapper
name|wrapper
parameter_list|)
block|{
if|if
condition|(
name|scProvider
operator|!=
literal|null
condition|)
block|{
name|SecurityContext
name|sc
init|=
name|scProvider
operator|.
name|getSecurityContext
argument_list|(
name|message
argument_list|,
name|wrapper
argument_list|)
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|,
name|sc
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Certificate
index|[]
name|getTLSCertificates
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|TLSSessionInfo
name|tlsInfo
init|=
name|message
operator|.
name|get
argument_list|(
name|TLSSessionInfo
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|tlsInfo
operator|!=
literal|null
condition|?
name|tlsInfo
operator|.
name|getPeerCertificates
argument_list|()
else|:
literal|null
return|;
block|}
specifier|protected
name|void
name|throwFault
parameter_list|(
name|String
name|error
parameter_list|,
name|Exception
name|ex
parameter_list|)
block|{
comment|// TODO: get bundle resource message once this filter is moved
comment|// to rt/rs/security
name|LOG
operator|.
name|warning
argument_list|(
name|error
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|JAXRSUtils
operator|.
name|toResponseBuilder
argument_list|(
literal|401
argument_list|)
operator|.
name|entity
argument_list|(
name|error
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
name|response
argument_list|)
throw|;
block|}
comment|/**      * Check the sender-vouches requirements against the received assertion. The SAML      * Assertion and the request body must be signed by the same signature.      */
specifier|protected
name|boolean
name|checkSenderVouches
parameter_list|(
name|Message
name|message
parameter_list|,
name|SamlAssertionWrapper
name|assertionWrapper
parameter_list|,
name|Certificate
index|[]
name|tlsCerts
parameter_list|)
block|{
comment|//
comment|// If we have a 2-way TLS connection, then we don't have to check that the
comment|// assertion + body are signed
comment|// If no body is available (ex, with GET) then consider validating that
comment|// the base64-encoded token is signed by the same signature
comment|//
if|if
condition|(
name|tlsCerts
operator|!=
literal|null
operator|&&
name|tlsCerts
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|confirmationMethods
init|=
name|assertionWrapper
operator|.
name|getConfirmationMethods
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|confirmationMethod
range|:
name|confirmationMethods
control|)
block|{
if|if
condition|(
name|OpenSAMLUtil
operator|.
name|isMethodSenderVouches
argument_list|(
name|confirmationMethod
argument_list|)
condition|)
block|{
name|Element
name|signedElement
init|=
name|message
operator|.
name|getContent
argument_list|(
name|Element
operator|.
name|class
argument_list|)
decl_stmt|;
name|Node
name|assertionParent
init|=
name|assertionWrapper
operator|.
name|getElement
argument_list|()
operator|.
name|getParentNode
argument_list|()
decl_stmt|;
comment|// if we have a shared parent signed node then we can assume both
comment|// this SAML assertion and the main payload have been signed by the same
comment|// signature
if|if
condition|(
name|assertionParent
operator|!=
name|signedElement
condition|)
block|{
comment|// if not then try to compare if the same cert/key was used to sign SAML token
comment|// and the payload
name|XMLSignature
name|signature
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLSignature
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|signature
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|SAMLKeyInfo
name|subjectKeyInfo
init|=
name|assertionWrapper
operator|.
name|getSignatureKeyInfo
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|compareCredentials
argument_list|(
name|subjectKeyInfo
argument_list|,
name|signature
argument_list|,
name|tlsCerts
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|protected
name|boolean
name|checkHolderOfKey
parameter_list|(
name|Message
name|message
parameter_list|,
name|SamlAssertionWrapper
name|assertionWrapper
parameter_list|,
name|Certificate
index|[]
name|tlsCerts
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|confirmationMethods
init|=
name|assertionWrapper
operator|.
name|getConfirmationMethods
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|confirmationMethod
range|:
name|confirmationMethods
control|)
block|{
if|if
condition|(
name|OpenSAMLUtil
operator|.
name|isMethodHolderOfKey
argument_list|(
name|confirmationMethod
argument_list|)
condition|)
block|{
name|XMLSignature
name|sig
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLSignature
operator|.
name|class
argument_list|)
decl_stmt|;
name|SAMLKeyInfo
name|subjectKeyInfo
init|=
name|assertionWrapper
operator|.
name|getSubjectKeyInfo
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|compareCredentials
argument_list|(
name|subjectKeyInfo
argument_list|,
name|sig
argument_list|,
name|tlsCerts
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Compare the credentials of the assertion to the credentials used in 2-way TLS or those      * used to verify signatures.      * Return true on a match      * @param subjectKeyInfo the SAMLKeyInfo object      * @param signedResults a list of all of the signed results      * @return true if the credentials of the assertion were used to verify a signature      */
specifier|private
name|boolean
name|compareCredentials
parameter_list|(
name|SAMLKeyInfo
name|subjectKeyInfo
parameter_list|,
name|XMLSignature
name|sig
parameter_list|,
name|Certificate
index|[]
name|tlsCerts
parameter_list|)
block|{
name|X509Certificate
index|[]
name|subjectCerts
init|=
name|subjectKeyInfo
operator|.
name|getCerts
argument_list|()
decl_stmt|;
name|PublicKey
name|subjectPublicKey
init|=
name|subjectKeyInfo
operator|.
name|getPublicKey
argument_list|()
decl_stmt|;
comment|//
comment|// Try to match the TLS certs first
comment|//
if|if
condition|(
name|tlsCerts
operator|!=
literal|null
operator|&&
name|tlsCerts
operator|.
name|length
operator|>
literal|0
operator|&&
name|subjectCerts
operator|!=
literal|null
operator|&&
name|subjectCerts
operator|.
name|length
operator|>
literal|0
operator|&&
name|tlsCerts
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
name|subjectCerts
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|tlsCerts
operator|!=
literal|null
operator|&&
name|tlsCerts
operator|.
name|length
operator|>
literal|0
operator|&&
name|subjectPublicKey
operator|!=
literal|null
operator|&&
name|tlsCerts
index|[
literal|0
index|]
operator|.
name|getPublicKey
argument_list|()
operator|.
name|equals
argument_list|(
name|subjectPublicKey
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|sig
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|//
comment|// Now try the message-level signatures
comment|//
try|try
block|{
name|X509Certificate
index|[]
name|certs
init|=
operator|new
name|X509Certificate
index|[]
block|{
name|sig
operator|.
name|getKeyInfo
argument_list|()
operator|.
name|getX509Certificate
argument_list|()
block|}
decl_stmt|;
name|PublicKey
name|publicKey
init|=
name|sig
operator|.
name|getKeyInfo
argument_list|()
operator|.
name|getPublicKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|certs
operator|!=
literal|null
operator|&&
name|certs
operator|.
name|length
operator|>
literal|0
operator|&&
name|subjectCerts
operator|!=
literal|null
operator|&&
name|subjectCerts
operator|.
name|length
operator|>
literal|0
operator|&&
name|certs
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
name|subjectCerts
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|publicKey
operator|!=
literal|null
operator|&&
name|publicKey
operator|.
name|equals
argument_list|(
name|subjectPublicKey
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|false
return|;
block|}
specifier|protected
name|boolean
name|checkBearer
parameter_list|(
name|SamlAssertionWrapper
name|assertionWrapper
parameter_list|,
name|Certificate
index|[]
name|tlsCerts
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|confirmationMethods
init|=
name|assertionWrapper
operator|.
name|getConfirmationMethods
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|confirmationMethod
range|:
name|confirmationMethods
control|)
block|{
name|boolean
name|isBearer
init|=
name|isMethodBearer
argument_list|(
name|confirmationMethod
argument_list|)
decl_stmt|;
if|if
condition|(
name|isBearer
operator|&&
operator|!
name|assertionWrapper
operator|.
name|isSigned
argument_list|()
operator|&&
operator|(
name|tlsCerts
operator|==
literal|null
operator|||
name|tlsCerts
operator|.
name|length
operator|==
literal|0
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// do some more validation - time based, etc
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|boolean
name|isMethodBearer
parameter_list|(
name|String
name|confirmMethod
parameter_list|)
block|{
return|return
name|confirmMethod
operator|!=
literal|null
operator|&&
name|confirmMethod
operator|.
name|startsWith
argument_list|(
literal|"urn:oasis:names:tc:SAML:"
argument_list|)
operator|&&
name|confirmMethod
operator|.
name|endsWith
argument_list|(
literal|":cm:bearer"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setKeyInfoMustBeAvailable
parameter_list|(
name|boolean
name|keyInfoMustBeAvailable
parameter_list|)
block|{
name|this
operator|.
name|keyInfoMustBeAvailable
operator|=
name|keyInfoMustBeAvailable
expr_stmt|;
block|}
block|}
end_class

end_unit

