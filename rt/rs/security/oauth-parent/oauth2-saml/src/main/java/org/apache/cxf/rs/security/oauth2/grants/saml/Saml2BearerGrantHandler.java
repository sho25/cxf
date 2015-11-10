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
name|oauth2
operator|.
name|grants
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
name|Principal
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
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
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
name|Base64UrlUtility
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
name|HttpUtils
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
name|phase
operator|.
name|PhaseInterceptorChain
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
name|RSSecurityUtils
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
name|oauth2
operator|.
name|common
operator|.
name|Client
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
name|oauth2
operator|.
name|common
operator|.
name|ServerAccessToken
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
name|oauth2
operator|.
name|common
operator|.
name|UserSubject
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
name|oauth2
operator|.
name|grants
operator|.
name|AbstractGrantHandler
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
name|oauth2
operator|.
name|provider
operator|.
name|OAuthServiceException
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
name|oauth2
operator|.
name|saml
operator|.
name|Constants
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
name|oauth2
operator|.
name|saml
operator|.
name|SamlOAuthValidator
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
name|oauth2
operator|.
name|utils
operator|.
name|OAuthConstants
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
name|oauth2
operator|.
name|utils
operator|.
name|OAuthUtils
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
name|rt
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
name|cxf
operator|.
name|rt
operator|.
name|security
operator|.
name|saml
operator|.
name|claims
operator|.
name|SAMLSecurityContext
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
name|engine
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

begin_comment
comment|/**  * The "SAML2 Bearer" grant handler  */
end_comment

begin_class
specifier|public
class|class
name|Saml2BearerGrantHandler
extends|extends
name|AbstractGrantHandler
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ENCODED_SAML2_BEARER_GRANT
decl_stmt|;
static|static
block|{
name|WSSConfig
operator|.
name|init
argument_list|()
expr_stmt|;
comment|//  AccessTokenService may be configured with the form provider
comment|// which will not decode by default - so listing both the actual
comment|// and encoded grant type value will help
name|ENCODED_SAML2_BEARER_GRANT
operator|=
name|HttpUtils
operator|.
name|urlEncode
argument_list|(
name|Constants
operator|.
name|SAML2_BEARER_GRANT
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
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
name|SamlOAuthValidator
name|samlOAuthValidator
init|=
operator|new
name|SamlOAuthValidator
argument_list|()
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
name|Saml2BearerGrantHandler
parameter_list|()
block|{
name|super
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Constants
operator|.
name|SAML2_BEARER_GRANT
argument_list|,
name|ENCODED_SAML2_BEARER_GRANT
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSamlValidator
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
name|setSamlOAuthValidator
parameter_list|(
name|SamlOAuthValidator
name|validator
parameter_list|)
block|{
name|samlOAuthValidator
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
specifier|public
name|ServerAccessToken
name|createAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|String
name|assertion
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|Constants
operator|.
name|CLIENT_GRANT_ASSERTION_PARAM
argument_list|)
decl_stmt|;
if|if
condition|(
name|assertion
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_GRANT
argument_list|)
throw|;
block|}
try|try
block|{
name|InputStream
name|tokenStream
init|=
name|decodeAssertion
argument_list|(
name|assertion
argument_list|)
decl_stmt|;
name|Element
name|token
init|=
name|readToken
argument_list|(
name|tokenStream
argument_list|)
decl_stmt|;
name|SamlAssertionWrapper
name|assertionWrapper
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|validateToken
argument_list|(
name|message
argument_list|,
name|assertionWrapper
argument_list|)
expr_stmt|;
name|UserSubject
name|grantSubject
init|=
name|getGrantSubject
argument_list|(
name|message
argument_list|,
name|assertionWrapper
argument_list|)
decl_stmt|;
return|return
name|doCreateAccessToken
argument_list|(
name|client
argument_list|,
name|grantSubject
argument_list|,
name|Constants
operator|.
name|SAML2_BEARER_GRANT
argument_list|,
name|OAuthUtils
operator|.
name|parseScope
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|SCOPE
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OAuthServiceException
name|ex
parameter_list|)
block|{
throw|throw
name|ex
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_GRANT
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|UserSubject
name|getGrantSubject
parameter_list|(
name|Message
name|message
parameter_list|,
name|SamlAssertionWrapper
name|wrapper
parameter_list|)
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
if|if
condition|(
name|sc
operator|instanceof
name|SAMLSecurityContext
condition|)
block|{
name|SAMLSecurityContext
name|jaxrsSc
init|=
operator|(
name|SAMLSecurityContext
operator|)
name|sc
decl_stmt|;
name|Set
argument_list|<
name|Principal
argument_list|>
name|rolesP
init|=
name|jaxrsSc
operator|.
name|getUserRoles
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|roles
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
name|roles
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Principal
name|p
range|:
name|rolesP
control|)
block|{
name|roles
operator|.
name|add
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|SamlUserSubject
argument_list|(
name|jaxrsSc
operator|.
name|getUserPrincipal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|roles
argument_list|,
name|jaxrsSc
operator|.
name|getClaims
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|UserSubject
argument_list|(
name|sc
operator|.
name|getUserPrincipal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
specifier|private
name|InputStream
name|decodeAssertion
parameter_list|(
name|String
name|assertion
parameter_list|)
block|{
try|try
block|{
name|byte
index|[]
name|deflatedToken
init|=
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|assertion
argument_list|)
decl_stmt|;
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|deflatedToken
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Base64Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_GRANT
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|Element
name|readToken
parameter_list|(
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
name|StandardCharsets
operator|.
name|UTF_8
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
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_GRANT
argument_list|)
throw|;
block|}
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
name|RSSecurityUtils
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
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_GRANT
argument_list|)
throw|;
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
name|KeyInfo
name|keyInfo
init|=
name|sig
operator|.
name|getKeyInfo
argument_list|()
decl_stmt|;
name|SAMLKeyInfo
name|samlKeyInfo
init|=
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
decl_stmt|;
name|assertion
operator|.
name|verifySignature
argument_list|(
name|samlKeyInfo
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
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_GRANT
argument_list|)
throw|;
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
name|samlOAuthValidator
operator|.
name|validate
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
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_GRANT
argument_list|,
name|ex
argument_list|)
throw|;
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
block|}
end_class

end_unit

