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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|security
operator|.
name|auth
operator|.
name|DestroyFailedException
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
name|ws
operator|.
name|rs
operator|.
name|container
operator|.
name|ContainerRequestContext
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
name|HttpHeaders
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
name|jaxrs
operator|.
name|ext
operator|.
name|MessageContextImpl
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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|DeflateEncoderDecoder
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
name|ext
operator|.
name|WSPasswordCallback
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
name|util
operator|.
name|DOM2Writer
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

begin_class
specifier|public
class|class
name|SamlPostBindingFilter
extends|extends
name|AbstractServiceProviderFilter
block|{
specifier|private
name|boolean
name|useDeflateEncoding
decl_stmt|;
specifier|public
name|void
name|setUseDeflateEncoding
parameter_list|(
name|boolean
name|useDeflateEncoding
parameter_list|)
block|{
name|this
operator|.
name|useDeflateEncoding
operator|=
name|useDeflateEncoding
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|context
parameter_list|)
block|{
name|Message
name|m
init|=
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|checkSecurityContext
argument_list|(
name|m
argument_list|)
condition|)
block|{
return|return;
block|}
try|try
block|{
name|SamlRequestInfo
name|info
init|=
name|createSamlRequestInfo
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|info
operator|.
name|setIdpServiceAddress
argument_list|(
name|getIdpServiceAddress
argument_list|()
argument_list|)
expr_stmt|;
comment|// This depends on RequestDispatcherProvider linking
comment|// SamlRequestInfo with the jsp page which will fill
comment|// in the XHTML form using SamlRequestInfo
comment|// in principle we could've built the XHTML form right here
comment|// but it will be cleaner to get that done in JSP
name|String
name|contextCookie
init|=
name|createCookie
argument_list|(
name|SSOConstants
operator|.
name|RELAY_STATE
argument_list|,
name|info
operator|.
name|getRelayState
argument_list|()
argument_list|,
name|info
operator|.
name|getWebAppContext
argument_list|()
argument_list|,
name|info
operator|.
name|getWebAppDomain
argument_list|()
argument_list|)
decl_stmt|;
operator|new
name|MessageContextImpl
argument_list|(
name|m
argument_list|)
operator|.
name|getHttpServletResponse
argument_list|()
operator|.
name|addHeader
argument_list|(
name|HttpHeaders
operator|.
name|SET_COOKIE
argument_list|,
name|contextCookie
argument_list|)
expr_stmt|;
name|context
operator|.
name|abortWith
argument_list|(
name|Response
operator|.
name|ok
argument_list|(
name|info
argument_list|)
operator|.
name|type
argument_list|(
literal|"text/html"
argument_list|)
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|CACHE_CONTROL
argument_list|,
literal|"no-cache, no-store"
argument_list|)
operator|.
name|header
argument_list|(
literal|"Pragma"
argument_list|,
literal|"no-cache"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
name|ex
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|String
name|encodeAuthnRequest
parameter_list|(
name|Element
name|authnRequest
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|requestMessage
init|=
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|authnRequest
argument_list|)
decl_stmt|;
name|byte
index|[]
name|deflatedBytes
init|=
literal|null
decl_stmt|;
comment|// Not correct according to the spec but required by some IDPs.
if|if
condition|(
name|useDeflateEncoding
condition|)
block|{
name|DeflateEncoderDecoder
name|encoder
init|=
operator|new
name|DeflateEncoderDecoder
argument_list|()
decl_stmt|;
name|deflatedBytes
operator|=
name|encoder
operator|.
name|deflateToken
argument_list|(
name|requestMessage
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|deflatedBytes
operator|=
name|requestMessage
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
expr_stmt|;
block|}
return|return
name|Base64Utility
operator|.
name|encode
argument_list|(
name|deflatedBytes
argument_list|)
return|;
block|}
specifier|protected
name|void
name|signAuthnRequest
parameter_list|(
name|AuthnRequest
name|authnRequest
parameter_list|)
throws|throws
name|Exception
block|{
name|Crypto
name|crypto
init|=
name|getSignatureCrypto
argument_list|()
decl_stmt|;
if|if
condition|(
name|crypto
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"No crypto instance of properties file configured for signature"
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
name|String
name|signatureUser
init|=
name|getSignatureUsername
argument_list|()
decl_stmt|;
if|if
condition|(
name|signatureUser
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"No user configured for signature"
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
name|CallbackHandler
name|callbackHandler
init|=
name|getCallbackHandler
argument_list|()
decl_stmt|;
if|if
condition|(
name|callbackHandler
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"No CallbackHandler configured to supply a password for signature"
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
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
name|signatureUser
argument_list|)
expr_stmt|;
name|X509Certificate
index|[]
name|issuerCerts
init|=
name|crypto
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
literal|"No issuer certs were found to sign the request using name: "
operator|+
name|signatureUser
argument_list|)
throw|;
block|}
name|String
name|sigAlgo
init|=
name|getSignatureAlgorithm
argument_list|()
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
name|LOG
operator|.
name|fine
argument_list|(
literal|"automatic sig algo detection: "
operator|+
name|pubKeyAlgo
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"DSA"
operator|.
name|equalsIgnoreCase
argument_list|(
name|pubKeyAlgo
argument_list|)
condition|)
block|{
name|sigAlgo
operator|=
name|SSOConstants
operator|.
name|DSA_SHA1
expr_stmt|;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"Using Signature algorithm "
operator|+
name|sigAlgo
argument_list|)
expr_stmt|;
comment|// Get the password
name|WSPasswordCallback
index|[]
name|cb
init|=
block|{
operator|new
name|WSPasswordCallback
argument_list|(
name|signatureUser
argument_list|,
name|WSPasswordCallback
operator|.
name|SIGNATURE
argument_list|)
block|}
decl_stmt|;
name|callbackHandler
operator|.
name|handle
argument_list|(
name|cb
argument_list|)
expr_stmt|;
name|String
name|password
init|=
name|cb
index|[
literal|0
index|]
operator|.
name|getPassword
argument_list|()
decl_stmt|;
comment|// Get the private key
name|PrivateKey
name|privateKey
init|=
name|crypto
operator|.
name|getPrivateKey
argument_list|(
name|signatureUser
argument_list|,
name|password
argument_list|)
decl_stmt|;
comment|// Create the signature
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
name|SignableSAMLObject
name|signableObject
init|=
name|authnRequest
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
comment|// Clean the private key from memory when we're done
try|try
block|{
name|privateKey
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DestroyFailedException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
end_class

end_unit

