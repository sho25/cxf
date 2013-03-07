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
name|net
operator|.
name|URLEncoder
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
name|Signature
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
name|InternalServerErrorException
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|UriBuilder
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
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
operator|.
name|components
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
name|ws
operator|.
name|security
operator|.
name|components
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
name|ws
operator|.
name|security
operator|.
name|util
operator|.
name|Base64
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
name|saml2
operator|.
name|core
operator|.
name|AuthnRequest
import|;
end_import

begin_class
specifier|public
class|class
name|SamlRedirectBindingFilter
extends|extends
name|AbstractServiceProviderFilter
block|{
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
else|else
block|{
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
name|String
name|urlEncodedRequest
init|=
name|URLEncoder
operator|.
name|encode
argument_list|(
name|info
operator|.
name|getSamlRequest
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|UriBuilder
name|ub
init|=
name|UriBuilder
operator|.
name|fromUri
argument_list|(
name|getIdpServiceAddress
argument_list|()
argument_list|)
decl_stmt|;
name|ub
operator|.
name|queryParam
argument_list|(
name|SSOConstants
operator|.
name|SAML_REQUEST
argument_list|,
name|urlEncodedRequest
argument_list|)
expr_stmt|;
name|ub
operator|.
name|queryParam
argument_list|(
name|SSOConstants
operator|.
name|RELAY_STATE
argument_list|,
name|info
operator|.
name|getRelayState
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isSignRequest
argument_list|()
condition|)
block|{
name|signRequest
argument_list|(
name|urlEncodedRequest
argument_list|,
name|info
operator|.
name|getRelayState
argument_list|()
argument_list|,
name|ub
argument_list|)
expr_stmt|;
block|}
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
name|context
operator|.
name|abortWith
argument_list|(
name|Response
operator|.
name|seeOther
argument_list|(
name|ub
operator|.
name|build
argument_list|()
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
name|header
argument_list|(
name|HttpHeaders
operator|.
name|SET_COOKIE
argument_list|,
name|contextCookie
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
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|InternalServerErrorException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
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
comment|// Do nothing as we sign the request in a different way for the redirect binding
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
name|DeflateEncoderDecoder
name|encoder
init|=
operator|new
name|DeflateEncoderDecoder
argument_list|()
decl_stmt|;
name|byte
index|[]
name|deflatedBytes
init|=
name|encoder
operator|.
name|deflateToken
argument_list|(
name|requestMessage
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|Base64Utility
operator|.
name|encode
argument_list|(
name|deflatedBytes
argument_list|)
return|;
block|}
comment|/**      * Sign a request according to the redirect binding spec for Web SSO      */
specifier|private
name|void
name|signRequest
parameter_list|(
name|String
name|authnRequest
parameter_list|,
name|String
name|relayState
parameter_list|,
name|UriBuilder
name|ub
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
operator|new
name|InternalServerErrorException
argument_list|()
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
operator|new
name|InternalServerErrorException
argument_list|()
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
operator|new
name|InternalServerErrorException
argument_list|()
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
name|WSSecurityException
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
name|SSOConstants
operator|.
name|RSA_SHA1
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
name|String
name|jceSigAlgo
init|=
literal|"SHA1withRSA"
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
name|SSOConstants
operator|.
name|DSA_SHA1
expr_stmt|;
name|jceSigAlgo
operator|=
literal|"SHA1withDSA"
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
name|ub
operator|.
name|queryParam
argument_list|(
name|SSOConstants
operator|.
name|SIG_ALG
argument_list|,
name|URLEncoder
operator|.
name|encode
argument_list|(
name|sigAlgo
argument_list|,
literal|"UTF-8"
argument_list|)
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
literal|null
decl_stmt|;
try|try
block|{
name|privateKey
operator|=
name|crypto
operator|.
name|getPrivateKey
argument_list|(
name|signatureUser
argument_list|,
name|password
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
operator|new
name|WSSecurityException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
comment|// Sign the request
name|Signature
name|signature
init|=
name|Signature
operator|.
name|getInstance
argument_list|(
name|jceSigAlgo
argument_list|)
decl_stmt|;
name|signature
operator|.
name|initSign
argument_list|(
name|privateKey
argument_list|)
expr_stmt|;
name|String
name|requestToSign
init|=
name|SSOConstants
operator|.
name|SAML_REQUEST
operator|+
literal|"="
operator|+
name|authnRequest
operator|+
literal|"&"
operator|+
name|SSOConstants
operator|.
name|RELAY_STATE
operator|+
literal|"="
operator|+
name|relayState
operator|+
literal|"&"
operator|+
name|SSOConstants
operator|.
name|SIG_ALG
operator|+
literal|"="
operator|+
name|URLEncoder
operator|.
name|encode
argument_list|(
name|sigAlgo
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|signature
operator|.
name|update
argument_list|(
name|requestToSign
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|byte
index|[]
name|signBytes
init|=
name|signature
operator|.
name|sign
argument_list|()
decl_stmt|;
name|String
name|encodedSignature
init|=
name|Base64
operator|.
name|encode
argument_list|(
name|signBytes
argument_list|)
decl_stmt|;
name|ub
operator|.
name|queryParam
argument_list|(
name|SSOConstants
operator|.
name|SIGNATURE
argument_list|,
name|URLEncoder
operator|.
name|encode
argument_list|(
name|encodedSignature
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

