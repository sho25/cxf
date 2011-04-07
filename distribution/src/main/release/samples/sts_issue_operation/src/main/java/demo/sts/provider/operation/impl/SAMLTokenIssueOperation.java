begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|sts
operator|.
name|provider
operator|.
name|operation
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|KeyStore
operator|.
name|PrivateKeyEntry
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
name|CertificateException
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
name|CertificateFactory
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
name|Arrays
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
name|HashSet
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|CanonicalizationMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|DigestMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|Reference
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|SignatureMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|SignedInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|Transform
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|XMLSignature
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|XMLSignatureFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|dom
operator|.
name|DOMSignContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|keyinfo
operator|.
name|KeyInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|keyinfo
operator|.
name|KeyInfoFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|keyinfo
operator|.
name|X509Data
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|spec
operator|.
name|C14NMethodParameterSpec
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|spec
operator|.
name|TransformParameterSpec
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|MessageContext
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
name|NodeList
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
name|security
operator|.
name|SecurityToken
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
name|security
operator|.
name|UsernameToken
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
name|util
operator|.
name|Base64Utility
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
name|sts
operator|.
name|provider
operator|.
name|STSException
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|RequestSecurityTokenResponseCollectionType
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|RequestSecurityTokenResponseType
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|RequestSecurityTokenType
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|RequestedReferenceType
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|RequestedSecurityTokenType
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|UseKeyType
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|secext
operator|.
name|KeyIdentifierType
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|secext
operator|.
name|SecurityTokenReferenceType
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|xmldsig
operator|.
name|KeyInfoType
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|xmldsig
operator|.
name|X509DataType
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
name|sts
operator|.
name|provider
operator|.
name|operation
operator|.
name|IssueOperation
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
name|utils
operator|.
name|Constants
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
name|xml
operator|.
name|SAMLConstants
import|;
end_import

begin_import
import|import
name|demo
operator|.
name|sts
operator|.
name|provider
operator|.
name|cert
operator|.
name|CertificateVerifier
import|;
end_import

begin_import
import|import
name|demo
operator|.
name|sts
operator|.
name|provider
operator|.
name|cert
operator|.
name|CertificateVerifierConfig
import|;
end_import

begin_import
import|import
name|demo
operator|.
name|sts
operator|.
name|provider
operator|.
name|token
operator|.
name|TokenProvider
import|;
end_import

begin_class
specifier|public
class|class
name|SAMLTokenIssueOperation
implements|implements
name|IssueOperation
block|{
specifier|private
specifier|static
specifier|final
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|ObjectFactory
name|WS_TRUST_FACTORY
init|=
operator|new
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|ObjectFactory
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|secext
operator|.
name|ObjectFactory
name|WSSE_FACTORY
init|=
operator|new
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|secext
operator|.
name|ObjectFactory
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SIGN_FACTORY_TYPE
init|=
literal|"DOM"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JKS_INSTANCE
init|=
literal|"JKS"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|X_509
init|=
literal|"X.509"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|QNAME_WST_TOKEN_TYPE
init|=
name|WS_TRUST_FACTORY
operator|.
name|createTokenType
argument_list|(
literal|""
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|TokenProvider
argument_list|>
name|tokenProviders
decl_stmt|;
specifier|private
name|CertificateVerifierConfig
name|certificateVerifierConfig
decl_stmt|;
specifier|public
name|void
name|setTokenProviders
parameter_list|(
name|List
argument_list|<
name|TokenProvider
argument_list|>
name|tokenProviders
parameter_list|)
block|{
name|this
operator|.
name|tokenProviders
operator|=
name|tokenProviders
expr_stmt|;
block|}
specifier|public
name|void
name|setCertificateVerifierConfig
parameter_list|(
name|CertificateVerifierConfig
name|certificateVerifierConfig
parameter_list|)
block|{
name|this
operator|.
name|certificateVerifierConfig
operator|=
name|certificateVerifierConfig
expr_stmt|;
block|}
specifier|public
name|RequestSecurityTokenResponseCollectionType
name|issue
parameter_list|(
name|RequestSecurityTokenType
name|request
parameter_list|,
name|WebServiceContext
name|context
parameter_list|)
block|{
name|String
name|tokenType
init|=
name|SAMLConstants
operator|.
name|SAML20_NS
decl_stmt|;
name|X509Certificate
name|certificate
init|=
literal|null
decl_stmt|;
name|String
name|username
init|=
literal|null
decl_stmt|;
comment|// parse input arguments
for|for
control|(
name|Object
name|requestObject
range|:
name|request
operator|.
name|getAny
argument_list|()
control|)
block|{
comment|// certificate
try|try
block|{
if|if
condition|(
name|certificate
operator|==
literal|null
condition|)
block|{
name|certificate
operator|=
name|getCertificateFromRequest
argument_list|(
name|requestObject
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|CertificateException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Can't extract X509 certificate from request"
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// TokenType
if|if
condition|(
name|requestObject
operator|instanceof
name|JAXBElement
condition|)
block|{
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|jaxbElement
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|requestObject
decl_stmt|;
if|if
condition|(
name|QNAME_WST_TOKEN_TYPE
operator|.
name|equals
argument_list|(
name|jaxbElement
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|tokenType
operator|=
operator|(
name|String
operator|)
name|jaxbElement
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|certificate
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|context
operator|==
literal|null
operator|||
name|context
operator|.
name|getMessageContext
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|STSException
argument_list|(
literal|"No message context found"
argument_list|)
throw|;
block|}
comment|//find the username
name|MessageContext
name|ctx
init|=
name|context
operator|.
name|getMessageContext
argument_list|()
decl_stmt|;
name|UsernameToken
name|unt
init|=
operator|(
name|UsernameToken
operator|)
name|ctx
operator|.
name|get
argument_list|(
name|SecurityToken
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|unt
operator|!=
literal|null
condition|)
block|{
name|username
operator|=
name|unt
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
comment|// check input arguments
if|if
condition|(
name|certificate
operator|!=
literal|null
condition|)
block|{
comment|// certificate
try|try
block|{
name|verifyCertificate
argument_list|(
name|certificate
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Can't verify X509 certificate from request"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|// create token
name|TokenProvider
name|tokenProvider
init|=
literal|null
decl_stmt|;
for|for
control|(
name|TokenProvider
name|tp
range|:
name|tokenProviders
control|)
block|{
if|if
condition|(
name|tokenType
operator|.
name|equals
argument_list|(
name|tp
operator|.
name|getTokenType
argument_list|()
argument_list|)
condition|)
block|{
name|tokenProvider
operator|=
name|tp
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|tokenProvider
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|STSException
argument_list|(
literal|"No token provider found for requested token type: "
operator|+
name|tokenType
argument_list|)
throw|;
block|}
name|Element
name|elementToken
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|certificate
operator|!=
literal|null
condition|)
block|{
name|elementToken
operator|=
name|tokenProvider
operator|.
name|createToken
argument_list|(
name|certificate
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|elementToken
operator|=
name|tokenProvider
operator|.
name|createToken
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
name|String
name|tokenId
init|=
name|tokenProvider
operator|.
name|getTokenId
argument_list|(
name|elementToken
argument_list|)
decl_stmt|;
name|signSAML
argument_list|(
name|elementToken
argument_list|,
name|tokenId
argument_list|)
expr_stmt|;
comment|// prepare response
name|RequestSecurityTokenResponseType
name|response
init|=
name|wrapAssertionToResponse
argument_list|(
name|tokenProvider
operator|.
name|getResponseTokentype
argument_list|()
argument_list|,
name|elementToken
argument_list|,
name|tokenId
argument_list|)
decl_stmt|;
name|RequestSecurityTokenResponseCollectionType
name|responseCollection
init|=
name|WS_TRUST_FACTORY
operator|.
name|createRequestSecurityTokenResponseCollectionType
argument_list|()
decl_stmt|;
name|responseCollection
operator|.
name|getRequestSecurityTokenResponse
argument_list|()
operator|.
name|add
argument_list|(
name|response
argument_list|)
expr_stmt|;
return|return
name|responseCollection
return|;
block|}
specifier|private
name|void
name|verifyCertificate
parameter_list|(
name|X509Certificate
name|certificate
parameter_list|)
throws|throws
name|Exception
block|{
name|KeyStore
name|ks
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|JKS_INSTANCE
argument_list|)
decl_stmt|;
name|ks
operator|.
name|load
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|certificateVerifierConfig
operator|.
name|getStorePath
argument_list|()
argument_list|)
argument_list|,
name|certificateVerifierConfig
operator|.
name|getStorePwd
argument_list|()
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|X509Certificate
argument_list|>
name|trustedRootCerts
init|=
operator|new
name|HashSet
argument_list|<
name|X509Certificate
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|alias
range|:
name|certificateVerifierConfig
operator|.
name|getTrustCertAliases
argument_list|()
control|)
block|{
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|Certificate
name|stsCert
init|=
name|ks
operator|.
name|getCertificate
argument_list|(
name|alias
argument_list|)
decl_stmt|;
name|trustedRootCerts
operator|.
name|add
argument_list|(
operator|(
name|X509Certificate
operator|)
name|stsCert
argument_list|)
expr_stmt|;
block|}
name|CertificateVerifier
operator|.
name|verifyCertificate
argument_list|(
name|certificate
argument_list|,
name|trustedRootCerts
argument_list|,
name|certificateVerifierConfig
operator|.
name|isVerifySelfSignedCert
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|RequestSecurityTokenResponseType
name|wrapAssertionToResponse
parameter_list|(
name|String
name|tokenType
parameter_list|,
name|Element
name|samlAssertion
parameter_list|,
name|String
name|tokenId
parameter_list|)
block|{
name|RequestSecurityTokenResponseType
name|response
init|=
name|WS_TRUST_FACTORY
operator|.
name|createRequestSecurityTokenResponseType
argument_list|()
decl_stmt|;
comment|// TokenType
name|JAXBElement
argument_list|<
name|String
argument_list|>
name|jaxbTokenType
init|=
name|WS_TRUST_FACTORY
operator|.
name|createTokenType
argument_list|(
name|tokenType
argument_list|)
decl_stmt|;
name|response
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|jaxbTokenType
argument_list|)
expr_stmt|;
comment|// RequestedSecurityToken
name|RequestedSecurityTokenType
name|requestedTokenType
init|=
name|WS_TRUST_FACTORY
operator|.
name|createRequestedSecurityTokenType
argument_list|()
decl_stmt|;
name|JAXBElement
argument_list|<
name|RequestedSecurityTokenType
argument_list|>
name|requestedToken
init|=
name|WS_TRUST_FACTORY
operator|.
name|createRequestedSecurityToken
argument_list|(
name|requestedTokenType
argument_list|)
decl_stmt|;
name|requestedTokenType
operator|.
name|setAny
argument_list|(
name|samlAssertion
argument_list|)
expr_stmt|;
name|response
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|requestedToken
argument_list|)
expr_stmt|;
comment|// RequestedAttachedReference
name|RequestedReferenceType
name|requestedReferenceType
init|=
name|WS_TRUST_FACTORY
operator|.
name|createRequestedReferenceType
argument_list|()
decl_stmt|;
name|SecurityTokenReferenceType
name|securityTokenReferenceType
init|=
name|WSSE_FACTORY
operator|.
name|createSecurityTokenReferenceType
argument_list|()
decl_stmt|;
name|KeyIdentifierType
name|keyIdentifierType
init|=
name|WSSE_FACTORY
operator|.
name|createKeyIdentifierType
argument_list|()
decl_stmt|;
name|keyIdentifierType
operator|.
name|setValue
argument_list|(
name|tokenId
argument_list|)
expr_stmt|;
name|JAXBElement
argument_list|<
name|KeyIdentifierType
argument_list|>
name|keyIdentifier
init|=
name|WSSE_FACTORY
operator|.
name|createKeyIdentifier
argument_list|(
name|keyIdentifierType
argument_list|)
decl_stmt|;
name|securityTokenReferenceType
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|keyIdentifier
argument_list|)
expr_stmt|;
name|requestedReferenceType
operator|.
name|setSecurityTokenReference
argument_list|(
name|securityTokenReferenceType
argument_list|)
expr_stmt|;
name|JAXBElement
argument_list|<
name|RequestedReferenceType
argument_list|>
name|requestedAttachedReference
init|=
name|WS_TRUST_FACTORY
operator|.
name|createRequestedAttachedReference
argument_list|(
name|requestedReferenceType
argument_list|)
decl_stmt|;
name|response
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|requestedAttachedReference
argument_list|)
expr_stmt|;
comment|// RequestedUnattachedReference
name|JAXBElement
argument_list|<
name|RequestedReferenceType
argument_list|>
name|requestedUnattachedReference
init|=
name|WS_TRUST_FACTORY
operator|.
name|createRequestedUnattachedReference
argument_list|(
name|requestedReferenceType
argument_list|)
decl_stmt|;
name|response
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|requestedUnattachedReference
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
specifier|private
name|X509Certificate
name|getCertificateFromRequest
parameter_list|(
name|Object
name|requestObject
parameter_list|)
throws|throws
name|CertificateException
block|{
name|UseKeyType
name|useKeyType
init|=
name|extractType
argument_list|(
name|requestObject
argument_list|,
name|UseKeyType
operator|.
name|class
argument_list|)
decl_stmt|;
name|byte
index|[]
name|x509
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|useKeyType
condition|)
block|{
name|KeyInfoType
name|keyInfoType
init|=
name|extractType
argument_list|(
name|useKeyType
operator|.
name|getAny
argument_list|()
argument_list|,
name|KeyInfoType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|keyInfoType
condition|)
block|{
for|for
control|(
name|Object
name|keyInfoContent
range|:
name|keyInfoType
operator|.
name|getContent
argument_list|()
control|)
block|{
name|X509DataType
name|x509DataType
init|=
name|extractType
argument_list|(
name|keyInfoContent
argument_list|,
name|X509DataType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|x509DataType
condition|)
block|{
for|for
control|(
name|Object
name|x509Object
range|:
name|x509DataType
operator|.
name|getX509IssuerSerialOrX509SKIOrX509SubjectName
argument_list|()
control|)
block|{
name|x509
operator|=
name|extractType
argument_list|(
name|x509Object
argument_list|,
name|byte
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|x509
condition|)
block|{
break|break;
block|}
block|}
block|}
block|}
block|}
else|else
block|{
name|Element
name|elementNSImpl
init|=
operator|(
name|Element
operator|)
name|useKeyType
operator|.
name|getAny
argument_list|()
decl_stmt|;
name|NodeList
name|x509CertData
init|=
name|elementNSImpl
operator|.
name|getElementsByTagNameNS
argument_list|(
name|Constants
operator|.
name|SignatureSpecNS
argument_list|,
name|Constants
operator|.
name|_TAG_X509CERTIFICATE
argument_list|)
decl_stmt|;
if|if
condition|(
name|x509CertData
operator|!=
literal|null
operator|&&
name|x509CertData
operator|.
name|getLength
argument_list|()
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|x509
operator|=
name|Base64Utility
operator|.
name|decode
argument_list|(
name|x509CertData
operator|.
name|item
argument_list|(
literal|0
argument_list|)
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|STSException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
name|x509
operator|!=
literal|null
condition|)
block|{
name|CertificateFactory
name|cf
init|=
name|CertificateFactory
operator|.
name|getInstance
argument_list|(
name|X_509
argument_list|)
decl_stmt|;
name|Certificate
name|certificate
init|=
name|cf
operator|.
name|generateCertificate
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|x509
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|(
name|X509Certificate
operator|)
name|certificate
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|extractType
parameter_list|(
name|Object
name|param
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|param
operator|instanceof
name|JAXBElement
condition|)
block|{
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|jaxbElement
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|param
decl_stmt|;
if|if
condition|(
name|clazz
operator|==
name|jaxbElement
operator|.
name|getDeclaredType
argument_list|()
condition|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|jaxbElement
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|signSAML
parameter_list|(
name|Element
name|assertionDocument
parameter_list|,
name|String
name|tokenId
parameter_list|)
block|{
name|InputStream
name|isKeyStore
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|certificateVerifierConfig
operator|.
name|getStorePath
argument_list|()
argument_list|)
decl_stmt|;
name|KeyStoreInfo
name|keyStoreInfo
init|=
operator|new
name|KeyStoreInfo
argument_list|(
name|isKeyStore
argument_list|,
name|certificateVerifierConfig
operator|.
name|getStorePwd
argument_list|()
argument_list|,
name|certificateVerifierConfig
operator|.
name|getKeySignAlias
argument_list|()
argument_list|,
name|certificateVerifierConfig
operator|.
name|getKeySignPwd
argument_list|()
argument_list|)
decl_stmt|;
name|signXML
argument_list|(
name|assertionDocument
argument_list|,
name|tokenId
argument_list|,
name|keyStoreInfo
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|signXML
parameter_list|(
name|Element
name|target
parameter_list|,
name|String
name|refId
parameter_list|,
name|KeyStoreInfo
name|keyStoreInfo
parameter_list|)
block|{
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|Init
operator|.
name|init
argument_list|()
expr_stmt|;
name|XMLSignatureFactory
name|signFactory
init|=
name|XMLSignatureFactory
operator|.
name|getInstance
argument_list|(
name|SIGN_FACTORY_TYPE
argument_list|)
decl_stmt|;
try|try
block|{
name|DigestMethod
name|method
init|=
name|signFactory
operator|.
name|newDigestMethod
argument_list|(
name|DigestMethod
operator|.
name|SHA1
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Transform
name|transform1
init|=
name|signFactory
operator|.
name|newTransform
argument_list|(
name|Transform
operator|.
name|ENVELOPED
argument_list|,
operator|(
name|TransformParameterSpec
operator|)
literal|null
argument_list|)
decl_stmt|;
name|Transform
name|transform2
init|=
name|signFactory
operator|.
name|newTransform
argument_list|(
name|CanonicalizationMethod
operator|.
name|EXCLUSIVE
argument_list|,
operator|(
name|TransformParameterSpec
operator|)
literal|null
argument_list|)
decl_stmt|;
name|Reference
name|ref
init|=
name|signFactory
operator|.
name|newReference
argument_list|(
literal|'#'
operator|+
name|refId
argument_list|,
name|method
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|transform1
argument_list|,
name|transform2
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|CanonicalizationMethod
name|canonMethod
init|=
name|signFactory
operator|.
name|newCanonicalizationMethod
argument_list|(
name|CanonicalizationMethod
operator|.
name|EXCLUSIVE
argument_list|,
operator|(
name|C14NMethodParameterSpec
operator|)
literal|null
argument_list|)
decl_stmt|;
name|SignatureMethod
name|signMethod
init|=
name|signFactory
operator|.
name|newSignatureMethod
argument_list|(
name|SignatureMethod
operator|.
name|RSA_SHA1
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SignedInfo
name|si
init|=
name|signFactory
operator|.
name|newSignedInfo
argument_list|(
name|canonMethod
argument_list|,
name|signMethod
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|ref
argument_list|)
argument_list|)
decl_stmt|;
name|KeyStore
operator|.
name|PrivateKeyEntry
name|keyEntry
init|=
name|getKeyEntry
argument_list|(
name|keyStoreInfo
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyEntry
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Key is not found in keystore. Alias: "
operator|+
name|keyStoreInfo
operator|.
name|getAlias
argument_list|()
argument_list|)
throw|;
block|}
name|KeyInfo
name|ki
init|=
name|getKeyInfo
argument_list|(
name|signFactory
argument_list|,
name|keyEntry
argument_list|)
decl_stmt|;
name|DOMSignContext
name|dsc
init|=
operator|new
name|DOMSignContext
argument_list|(
name|keyEntry
operator|.
name|getPrivateKey
argument_list|()
argument_list|,
name|target
argument_list|)
decl_stmt|;
name|XMLSignature
name|signature
init|=
name|signFactory
operator|.
name|newXMLSignature
argument_list|(
name|si
argument_list|,
name|ki
argument_list|)
decl_stmt|;
name|signature
operator|.
name|sign
argument_list|(
name|dsc
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Cannot sign xml document: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|PrivateKeyEntry
name|getKeyEntry
parameter_list|(
name|KeyStoreInfo
name|keyStoreInfo
parameter_list|)
throws|throws
name|Exception
block|{
name|KeyStore
name|ks
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|JKS_INSTANCE
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|keyStoreInfo
operator|.
name|getContent
argument_list|()
argument_list|)
decl_stmt|;
name|ks
operator|.
name|load
argument_list|(
name|is
argument_list|,
name|keyStoreInfo
operator|.
name|getStorePassword
argument_list|()
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|KeyStore
operator|.
name|PasswordProtection
name|passwordProtection
init|=
operator|new
name|KeyStore
operator|.
name|PasswordProtection
argument_list|(
name|keyStoreInfo
operator|.
name|getKeyPassword
argument_list|()
operator|.
name|toCharArray
argument_list|()
argument_list|)
decl_stmt|;
name|KeyStore
operator|.
name|PrivateKeyEntry
name|keyEntry
init|=
operator|(
name|KeyStore
operator|.
name|PrivateKeyEntry
operator|)
name|ks
operator|.
name|getEntry
argument_list|(
name|keyStoreInfo
operator|.
name|getAlias
argument_list|()
argument_list|,
name|passwordProtection
argument_list|)
decl_stmt|;
return|return
name|keyEntry
return|;
block|}
specifier|private
name|KeyInfo
name|getKeyInfo
parameter_list|(
name|XMLSignatureFactory
name|signFactory
parameter_list|,
name|PrivateKeyEntry
name|keyEntry
parameter_list|)
block|{
name|X509Certificate
name|cert
init|=
operator|(
name|X509Certificate
operator|)
name|keyEntry
operator|.
name|getCertificate
argument_list|()
decl_stmt|;
name|KeyInfoFactory
name|kif
init|=
name|signFactory
operator|.
name|getKeyInfoFactory
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|x509Content
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|x509Content
operator|.
name|add
argument_list|(
name|cert
operator|.
name|getSubjectX500Principal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|x509Content
operator|.
name|add
argument_list|(
name|cert
argument_list|)
expr_stmt|;
name|X509Data
name|xd
init|=
name|kif
operator|.
name|newX509Data
argument_list|(
name|x509Content
argument_list|)
decl_stmt|;
return|return
name|kif
operator|.
name|newKeyInfo
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|xd
argument_list|)
argument_list|)
return|;
block|}
specifier|public
class|class
name|KeyStoreInfo
block|{
specifier|private
name|byte
index|[]
name|content
decl_stmt|;
specifier|private
name|String
name|storePassword
decl_stmt|;
specifier|private
name|String
name|alias
decl_stmt|;
specifier|private
name|String
name|keyPassword
decl_stmt|;
specifier|public
name|KeyStoreInfo
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|String
name|storePassword
parameter_list|,
name|String
name|alias
parameter_list|,
name|String
name|keyPassword
parameter_list|)
block|{
name|this
operator|.
name|content
operator|=
name|getBytes
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|this
operator|.
name|alias
operator|=
name|alias
expr_stmt|;
name|this
operator|.
name|storePassword
operator|=
name|storePassword
expr_stmt|;
name|this
operator|.
name|keyPassword
operator|=
name|keyPassword
expr_stmt|;
block|}
specifier|public
name|byte
index|[]
name|getContent
parameter_list|()
block|{
return|return
name|content
return|;
block|}
specifier|public
name|String
name|getAlias
parameter_list|()
block|{
return|return
name|alias
return|;
block|}
specifier|public
name|String
name|getStorePassword
parameter_list|()
block|{
return|return
name|storePassword
return|;
block|}
specifier|public
name|String
name|getKeyPassword
parameter_list|()
block|{
return|return
name|keyPassword
return|;
block|}
specifier|private
name|byte
index|[]
name|getBytes
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
try|try
block|{
name|int
name|len
decl_stmt|;
name|int
name|size
init|=
literal|1024
decl_stmt|;
name|byte
index|[]
name|buf
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|buf
operator|=
operator|new
name|byte
index|[
name|size
index|]
expr_stmt|;
while|while
condition|(
operator|(
name|len
operator|=
name|is
operator|.
name|read
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|size
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|bos
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
name|buf
operator|=
name|bos
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
return|return
name|buf
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot read keystore content: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

