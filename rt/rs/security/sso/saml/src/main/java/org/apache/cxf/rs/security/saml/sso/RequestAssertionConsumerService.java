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
name|ByteArrayInputStream
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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ResourceBundle
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|DataFormatException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
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
name|FormParam
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
name|GET
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
name|POST
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
name|Path
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
name|Produces
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
name|QueryParam
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
name|WebApplicationException
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
name|Context
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
name|MediaType
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
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
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
name|i18n
operator|.
name|BundleUtils
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
name|common
operator|.
name|util
operator|.
name|Base64Exception
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|jaxrs
operator|.
name|ext
operator|.
name|MessageContext
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
name|cxf
operator|.
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|sso
operator|.
name|state
operator|.
name|RequestState
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
name|sso
operator|.
name|state
operator|.
name|ResponseState
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
name|transport
operator|.
name|http
operator|.
name|AbstractHTTPDestination
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
name|CryptoFactory
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
name|xml
operator|.
name|XMLObject
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"sso"
argument_list|)
specifier|public
class|class
name|RequestAssertionConsumerService
extends|extends
name|AbstractSSOSpHandler
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
name|RequestAssertionConsumerService
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|RequestAssertionConsumerService
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|supportDeflateEncoding
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|supportBase64Encoding
init|=
literal|true
decl_stmt|;
specifier|private
name|Crypto
name|signatureCrypto
decl_stmt|;
specifier|private
name|String
name|signaturePropertiesFile
decl_stmt|;
specifier|private
name|boolean
name|enforceAssertionsSigned
init|=
literal|true
decl_stmt|;
annotation|@
name|Context
specifier|private
name|MessageContext
name|jaxrsContext
decl_stmt|;
specifier|public
name|void
name|setSupportDeflateEncoding
parameter_list|(
name|boolean
name|deflate
parameter_list|)
block|{
name|supportDeflateEncoding
operator|=
name|deflate
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSupportDeflateEncoding
parameter_list|()
block|{
return|return
name|supportDeflateEncoding
return|;
block|}
comment|/**      * Enforce that Assertions must be signed if the POST binding was used. The default is true.      */
specifier|public
name|void
name|setEnforceAssertionsSigned
parameter_list|(
name|boolean
name|enforceAssertionsSigned
parameter_list|)
block|{
name|this
operator|.
name|enforceAssertionsSigned
operator|=
name|enforceAssertionsSigned
expr_stmt|;
block|}
specifier|public
name|void
name|setSupportBase64Encoding
parameter_list|(
name|boolean
name|supportBase64Encoding
parameter_list|)
block|{
name|this
operator|.
name|supportBase64Encoding
operator|=
name|supportBase64Encoding
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSupportBase64Encoding
parameter_list|()
block|{
return|return
name|supportBase64Encoding
return|;
block|}
specifier|public
name|void
name|setSignatureCrypto
parameter_list|(
name|Crypto
name|crypto
parameter_list|)
block|{
name|signatureCrypto
operator|=
name|crypto
expr_stmt|;
block|}
comment|/**      * Set the String corresponding to the signature Properties class      * @param signaturePropertiesFile the String corresponding to the signature properties file      */
specifier|public
name|void
name|setSignaturePropertiesFile
parameter_list|(
name|String
name|signaturePropertiesFile
parameter_list|)
block|{
name|this
operator|.
name|signaturePropertiesFile
operator|=
name|signaturePropertiesFile
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Setting signature properties: "
operator|+
name|signaturePropertiesFile
argument_list|)
expr_stmt|;
block|}
annotation|@
name|POST
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED
argument_list|)
specifier|public
name|Response
name|processSamlResponse
parameter_list|(
annotation|@
name|FormParam
argument_list|(
name|SSOConstants
operator|.
name|SAML_RESPONSE
argument_list|)
name|String
name|encodedSamlResponse
parameter_list|,
annotation|@
name|FormParam
argument_list|(
name|SSOConstants
operator|.
name|RELAY_STATE
argument_list|)
name|String
name|relayState
parameter_list|)
block|{
name|RequestState
name|requestState
init|=
name|processRelayState
argument_list|(
name|relayState
argument_list|)
decl_stmt|;
name|URI
name|targetURI
init|=
name|getTargetURI
argument_list|(
name|requestState
operator|.
name|getTargetAddress
argument_list|()
argument_list|)
decl_stmt|;
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Response
name|samlResponse
init|=
name|readSAMLResponse
argument_list|(
literal|true
argument_list|,
name|encodedSamlResponse
argument_list|)
decl_stmt|;
comment|// Validate the Response
name|validateSamlResponseProtocol
argument_list|(
name|samlResponse
argument_list|)
expr_stmt|;
name|SSOValidatorResponse
name|validatorResponse
init|=
name|validateSamlSSOResponse
argument_list|(
literal|true
argument_list|,
name|samlResponse
argument_list|,
name|requestState
argument_list|)
decl_stmt|;
comment|// Set the security context
name|String
name|securityContextKey
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|long
name|currentTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|Date
name|notOnOrAfter
init|=
name|validatorResponse
operator|.
name|getSessionNotOnOrAfter
argument_list|()
decl_stmt|;
name|long
name|expiresAt
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|notOnOrAfter
operator|!=
literal|null
condition|)
block|{
name|expiresAt
operator|=
name|notOnOrAfter
operator|.
name|getTime
argument_list|()
expr_stmt|;
block|}
name|ResponseState
name|responseState
init|=
operator|new
name|ResponseState
argument_list|(
name|relayState
argument_list|,
name|currentTime
argument_list|,
name|expiresAt
argument_list|)
decl_stmt|;
name|getStateProvider
argument_list|()
operator|.
name|setResponseState
argument_list|(
name|securityContextKey
argument_list|,
name|responseState
argument_list|)
expr_stmt|;
name|String
name|contextCookie
init|=
name|createCookie
argument_list|(
name|SSOConstants
operator|.
name|SECURITY_CONTEXT_TOKEN
argument_list|,
name|securityContextKey
argument_list|,
name|requestState
operator|.
name|getWebAppContext
argument_list|()
argument_list|,
name|requestState
operator|.
name|getWebAppDomain
argument_list|()
argument_list|)
decl_stmt|;
comment|// Finally, redirect to the service provider endpoint
return|return
name|Response
operator|.
name|seeOther
argument_list|(
name|targetURI
argument_list|)
operator|.
name|header
argument_list|(
literal|"Set-Cookie"
argument_list|,
name|contextCookie
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|GET
specifier|public
name|Response
name|getSamlResponse
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
name|SSOConstants
operator|.
name|SAML_RESPONSE
argument_list|)
name|String
name|encodedSamlResponse
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
name|SSOConstants
operator|.
name|RELAY_STATE
argument_list|)
name|String
name|relayState
parameter_list|)
block|{
name|RequestState
name|requestState
init|=
name|processRelayState
argument_list|(
name|relayState
argument_list|)
decl_stmt|;
name|URI
name|targetURI
init|=
name|getTargetURI
argument_list|(
name|requestState
operator|.
name|getTargetAddress
argument_list|()
argument_list|)
decl_stmt|;
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Response
name|samlResponse
init|=
name|readSAMLResponse
argument_list|(
literal|false
argument_list|,
name|encodedSamlResponse
argument_list|)
decl_stmt|;
comment|// Validate the Response
name|validateSamlResponseProtocol
argument_list|(
name|samlResponse
argument_list|)
expr_stmt|;
name|SSOValidatorResponse
name|validatorResponse
init|=
name|validateSamlSSOResponse
argument_list|(
literal|false
argument_list|,
name|samlResponse
argument_list|,
name|requestState
argument_list|)
decl_stmt|;
comment|// Set the security context
name|String
name|securityContextKey
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|long
name|currentTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|Date
name|notOnOrAfter
init|=
name|validatorResponse
operator|.
name|getSessionNotOnOrAfter
argument_list|()
decl_stmt|;
name|long
name|expiresAt
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|notOnOrAfter
operator|!=
literal|null
condition|)
block|{
name|expiresAt
operator|=
name|notOnOrAfter
operator|.
name|getTime
argument_list|()
expr_stmt|;
block|}
name|ResponseState
name|responseState
init|=
operator|new
name|ResponseState
argument_list|(
name|relayState
argument_list|,
name|currentTime
argument_list|,
name|expiresAt
argument_list|)
decl_stmt|;
name|getStateProvider
argument_list|()
operator|.
name|setResponseState
argument_list|(
name|securityContextKey
argument_list|,
name|responseState
argument_list|)
expr_stmt|;
name|String
name|contextCookie
init|=
name|createCookie
argument_list|(
name|SSOConstants
operator|.
name|SECURITY_CONTEXT_TOKEN
argument_list|,
name|securityContextKey
argument_list|,
name|requestState
operator|.
name|getWebAppContext
argument_list|()
argument_list|,
name|requestState
operator|.
name|getWebAppDomain
argument_list|()
argument_list|)
decl_stmt|;
comment|// Finally, redirect to the service provider endpoint
return|return
name|Response
operator|.
name|seeOther
argument_list|(
name|targetURI
argument_list|)
operator|.
name|header
argument_list|(
literal|"Set-Cookie"
argument_list|,
name|contextCookie
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
specifier|private
name|RequestState
name|processRelayState
parameter_list|(
name|String
name|relayState
parameter_list|)
block|{
if|if
condition|(
name|relayState
operator|==
literal|null
condition|)
block|{
name|reportError
argument_list|(
literal|"MISSING_RELAY_STATE"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
if|if
condition|(
name|relayState
operator|.
name|getBytes
argument_list|()
operator|.
name|length
operator|<
literal|0
operator|||
name|relayState
operator|.
name|getBytes
argument_list|()
operator|.
name|length
operator|>
literal|80
condition|)
block|{
name|reportError
argument_list|(
literal|"INVALID_RELAY_STATE"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
name|RequestState
name|requestState
init|=
name|getStateProvider
argument_list|()
operator|.
name|removeRequestState
argument_list|(
name|relayState
argument_list|)
decl_stmt|;
if|if
condition|(
name|requestState
operator|==
literal|null
condition|)
block|{
name|reportError
argument_list|(
literal|"MISSING_REQUEST_STATE"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
if|if
condition|(
name|isStateExpired
argument_list|(
name|requestState
operator|.
name|getCreatedAt
argument_list|()
argument_list|,
literal|0
argument_list|)
condition|)
block|{
name|reportError
argument_list|(
literal|"EXPIRED_REQUEST_STATE"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
return|return
name|requestState
return|;
block|}
specifier|private
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Response
name|readSAMLResponse
parameter_list|(
name|boolean
name|postBinding
parameter_list|,
name|String
name|samlResponse
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|samlResponse
argument_list|)
condition|)
block|{
name|reportError
argument_list|(
literal|"MISSING_SAML_RESPONSE"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
name|String
name|samlResponseDecoded
init|=
name|samlResponse
decl_stmt|;
comment|// URL Decoding only applies for the re-direct binding
if|if
condition|(
operator|!
name|postBinding
condition|)
block|{
try|try
block|{
name|samlResponseDecoded
operator|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|samlResponse
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
block|}
name|InputStream
name|tokenStream
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isSupportBase64Encoding
argument_list|()
condition|)
block|{
try|try
block|{
name|byte
index|[]
name|deflatedToken
init|=
name|Base64Utility
operator|.
name|decode
argument_list|(
name|samlResponseDecoded
argument_list|)
decl_stmt|;
name|tokenStream
operator|=
name|isSupportDeflateEncoding
argument_list|()
condition|?
operator|new
name|DeflateEncoderDecoder
argument_list|()
operator|.
name|inflateToken
argument_list|(
name|deflatedToken
argument_list|)
else|:
operator|new
name|ByteArrayInputStream
argument_list|(
name|deflatedToken
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Base64Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|DataFormatException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
block|}
else|else
block|{
try|try
block|{
name|tokenStream
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|samlResponseDecoded
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
block|}
name|Document
name|responseDoc
init|=
literal|null
decl_stmt|;
try|try
block|{
name|responseDoc
operator|=
name|DOMUtils
operator|.
name|readXml
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|tokenStream
argument_list|,
literal|"UTF-8"
argument_list|)
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
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
name|XMLObject
name|responseObject
init|=
literal|null
decl_stmt|;
try|try
block|{
name|responseObject
operator|=
name|OpenSAMLUtil
operator|.
name|fromDom
argument_list|(
name|responseDoc
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
operator|(
name|responseObject
operator|instanceof
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Response
operator|)
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
return|return
operator|(
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Response
operator|)
name|responseObject
return|;
block|}
comment|/**      * Validate the received SAML Response as per the protocol      */
specifier|protected
name|void
name|validateSamlResponseProtocol
parameter_list|(
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Response
name|samlResponse
parameter_list|)
block|{
try|try
block|{
name|SAMLProtocolResponseValidator
name|protocolValidator
init|=
operator|new
name|SAMLProtocolResponseValidator
argument_list|()
decl_stmt|;
name|protocolValidator
operator|.
name|validateSamlResponse
argument_list|(
name|samlResponse
argument_list|,
name|getSignatureCrypto
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
block|{
name|reportError
argument_list|(
literal|"INVALID_SAML_RESPONSE"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
block|}
comment|/**      * Validate the received SAML Response as per the Web SSO profile      */
specifier|protected
name|SSOValidatorResponse
name|validateSamlSSOResponse
parameter_list|(
name|boolean
name|postBinding
parameter_list|,
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Response
name|samlResponse
parameter_list|,
name|RequestState
name|requestState
parameter_list|)
block|{
try|try
block|{
name|SAMLSSOResponseValidator
name|ssoResponseValidator
init|=
operator|new
name|SAMLSSOResponseValidator
argument_list|()
decl_stmt|;
name|ssoResponseValidator
operator|.
name|setAssertionConsumerURL
argument_list|(
operator|(
name|String
operator|)
name|jaxrsContext
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUEST_URL
argument_list|)
argument_list|)
expr_stmt|;
name|HttpServletRequest
name|httpRequest
init|=
operator|(
name|HttpServletRequest
operator|)
name|jaxrsContext
operator|.
name|get
argument_list|(
name|AbstractHTTPDestination
operator|.
name|HTTP_REQUEST
argument_list|)
decl_stmt|;
name|ssoResponseValidator
operator|.
name|setClientAddress
argument_list|(
name|httpRequest
operator|.
name|getRemoteAddr
argument_list|()
argument_list|)
expr_stmt|;
name|ssoResponseValidator
operator|.
name|setIssuerIDP
argument_list|(
name|requestState
operator|.
name|getIdpServiceAddress
argument_list|()
argument_list|)
expr_stmt|;
name|ssoResponseValidator
operator|.
name|setRequestId
argument_list|(
name|requestState
operator|.
name|getSamlRequestId
argument_list|()
argument_list|)
expr_stmt|;
name|ssoResponseValidator
operator|.
name|setSpIdentifier
argument_list|(
name|requestState
operator|.
name|getIssuerId
argument_list|()
argument_list|)
expr_stmt|;
name|ssoResponseValidator
operator|.
name|setEnforceAssertionsSigned
argument_list|(
name|enforceAssertionsSigned
argument_list|)
expr_stmt|;
return|return
name|ssoResponseValidator
operator|.
name|validateSamlResponse
argument_list|(
name|samlResponse
argument_list|,
name|postBinding
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
block|{
name|reportError
argument_list|(
literal|"INVALID_SAML_RESPONSE"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
block|}
specifier|private
name|URI
name|getTargetURI
parameter_list|(
name|String
name|targetAddress
parameter_list|)
block|{
if|if
condition|(
name|targetAddress
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|URI
operator|.
name|create
argument_list|(
name|targetAddress
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
name|reportError
argument_list|(
literal|"INVALID_TARGET_URI"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|reportError
argument_list|(
literal|"MISSING_TARGET_URI"
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
specifier|private
name|void
name|reportError
parameter_list|(
name|String
name|code
parameter_list|)
block|{
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
name|errorMsg
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
name|code
argument_list|,
name|BUNDLE
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|errorMsg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Crypto
name|getSignatureCrypto
parameter_list|()
block|{
if|if
condition|(
name|signatureCrypto
operator|==
literal|null
operator|&&
name|signaturePropertiesFile
operator|!=
literal|null
condition|)
block|{
name|Properties
name|sigProperties
init|=
name|getProps
argument_list|(
name|signaturePropertiesFile
argument_list|)
decl_stmt|;
if|if
condition|(
name|sigProperties
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Cannot load signature properties using: "
operator|+
name|signaturePropertiesFile
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
try|try
block|{
name|signatureCrypto
operator|=
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
name|sigProperties
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Error in loading the signature Crypto object: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
return|return
name|signatureCrypto
return|;
block|}
specifier|private
specifier|static
name|Properties
name|getProps
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|Properties
name|properties
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Properties
condition|)
block|{
name|properties
operator|=
operator|(
name|Properties
operator|)
name|o
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
name|URL
name|url
init|=
literal|null
decl_stmt|;
try|try
block|{
name|url
operator|=
name|ClassLoaderUtils
operator|.
name|getResource
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|,
name|RequestAssertionConsumerService
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
name|InputStream
name|ins
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|ins
argument_list|)
expr_stmt|;
name|ins
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|=
literal|null
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|URL
condition|)
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
try|try
block|{
name|InputStream
name|ins
init|=
operator|(
operator|(
name|URL
operator|)
name|o
operator|)
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|ins
argument_list|)
expr_stmt|;
name|ins
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|return
name|properties
return|;
block|}
block|}
end_class

end_unit

