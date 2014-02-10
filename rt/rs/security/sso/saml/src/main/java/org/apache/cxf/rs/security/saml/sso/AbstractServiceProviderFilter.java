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
name|io
operator|.
name|StringReader
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
name|URLEncoder
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
name|util
operator|.
name|Map
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
name|annotation
operator|.
name|PreDestroy
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
name|Cookie
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
name|security
operator|.
name|SimplePrincipal
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
name|impl
operator|.
name|HttpHeadersImpl
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
name|impl
operator|.
name|UriInfoImpl
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
name|SAMLUtils
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
name|assertion
operator|.
name|Subject
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
name|SamlAssertionWrapper
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
annotation|@
name|PreMatching
specifier|public
specifier|abstract
class|class
name|AbstractServiceProviderFilter
extends|extends
name|AbstractSSOSpHandler
implements|implements
name|ContainerRequestFilter
block|{
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AbstractServiceProviderFilter
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|AbstractServiceProviderFilter
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|idpServiceAddress
decl_stmt|;
specifier|private
name|String
name|issuerId
decl_stmt|;
specifier|private
name|String
name|assertionConsumerServiceAddress
decl_stmt|;
specifier|private
name|AuthnRequestBuilder
name|authnRequestBuilder
init|=
operator|new
name|DefaultAuthnRequestBuilder
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|signRequest
decl_stmt|;
specifier|private
name|String
name|signatureUsername
decl_stmt|;
specifier|private
name|String
name|webAppDomain
decl_stmt|;
specifier|private
name|boolean
name|addWebAppContext
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|addEndpointAddressToContext
decl_stmt|;
specifier|public
name|void
name|setAddEndpointAddressToContext
parameter_list|(
name|boolean
name|add
parameter_list|)
block|{
name|addEndpointAddressToContext
operator|=
name|add
expr_stmt|;
block|}
specifier|public
name|void
name|setSignRequest
parameter_list|(
name|boolean
name|signRequest
parameter_list|)
block|{
name|this
operator|.
name|signRequest
operator|=
name|signRequest
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSignRequest
parameter_list|()
block|{
return|return
name|signRequest
return|;
block|}
specifier|public
name|void
name|setAuthnRequestBuilder
parameter_list|(
name|AuthnRequestBuilder
name|authnRequestBuilder
parameter_list|)
block|{
name|this
operator|.
name|authnRequestBuilder
operator|=
name|authnRequestBuilder
expr_stmt|;
block|}
specifier|public
name|void
name|setAssertionConsumerServiceAddress
parameter_list|(
name|String
name|assertionConsumerServiceAddress
parameter_list|)
block|{
name|this
operator|.
name|assertionConsumerServiceAddress
operator|=
name|assertionConsumerServiceAddress
expr_stmt|;
block|}
specifier|public
name|void
name|setIssuerId
parameter_list|(
name|String
name|issuerId
parameter_list|)
block|{
name|this
operator|.
name|issuerId
operator|=
name|issuerId
expr_stmt|;
block|}
specifier|public
name|void
name|setIdpServiceAddress
parameter_list|(
name|String
name|idpServiceAddress
parameter_list|)
block|{
name|this
operator|.
name|idpServiceAddress
operator|=
name|idpServiceAddress
expr_stmt|;
block|}
specifier|public
name|String
name|getIdpServiceAddress
parameter_list|()
block|{
return|return
name|idpServiceAddress
return|;
block|}
comment|/**      * Set the username/alias to use to sign any request      * @param signatureUsername the username/alias to use to sign any request      */
specifier|public
name|void
name|setSignatureUsername
parameter_list|(
name|String
name|signatureUsername
parameter_list|)
block|{
name|this
operator|.
name|signatureUsername
operator|=
name|signatureUsername
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Setting signatureUsername: "
operator|+
name|signatureUsername
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the username/alias to use to sign any request      * @return the username/alias to use to sign any request      */
specifier|public
name|String
name|getSignatureUsername
parameter_list|()
block|{
return|return
name|signatureUsername
return|;
block|}
annotation|@
name|PreDestroy
specifier|public
name|void
name|close
parameter_list|()
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|String
name|getIssuerId
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
if|if
condition|(
name|issuerId
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|UriInfoImpl
argument_list|(
name|m
argument_list|)
operator|.
name|getBaseUri
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|issuerId
return|;
block|}
block|}
specifier|protected
name|boolean
name|checkSecurityContext
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|HttpHeaders
name|headers
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Cookie
argument_list|>
name|cookies
init|=
name|headers
operator|.
name|getCookies
argument_list|()
decl_stmt|;
name|Cookie
name|securityContextCookie
init|=
name|cookies
operator|.
name|get
argument_list|(
name|SSOConstants
operator|.
name|SECURITY_CONTEXT_TOKEN
argument_list|)
decl_stmt|;
name|ResponseState
name|responseState
init|=
name|getValidResponseState
argument_list|(
name|securityContextCookie
argument_list|,
name|m
argument_list|)
decl_stmt|;
if|if
condition|(
name|responseState
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Cookie
name|relayStateCookie
init|=
name|cookies
operator|.
name|get
argument_list|(
name|SSOConstants
operator|.
name|RELAY_STATE
argument_list|)
decl_stmt|;
if|if
condition|(
name|relayStateCookie
operator|==
literal|null
condition|)
block|{
name|reportError
argument_list|(
literal|"MISSING_RELAY_COOKIE"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|String
name|originalRelayState
init|=
name|responseState
operator|.
name|getRelayState
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|originalRelayState
operator|.
name|equals
argument_list|(
name|relayStateCookie
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
comment|// perhaps the response state should also be removed
name|reportError
argument_list|(
literal|"INVALID_RELAY_STATE"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
try|try
block|{
name|String
name|assertion
init|=
name|responseState
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
name|SamlAssertionWrapper
name|assertionWrapper
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|StaxUtils
operator|.
name|read
argument_list|(
operator|new
name|StringReader
argument_list|(
name|assertion
argument_list|)
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
decl_stmt|;
name|setSecurityContext
argument_list|(
name|m
argument_list|,
name|assertionWrapper
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|reportError
argument_list|(
literal|"INVALID_RESPONSE_STATE"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|protected
name|void
name|setSecurityContext
parameter_list|(
name|Message
name|m
parameter_list|,
name|SamlAssertionWrapper
name|assertionWrapper
parameter_list|)
block|{
comment|// don't worry about roles/claims for now, just set a basic SecurityContext
name|Subject
name|subject
init|=
name|SAMLUtils
operator|.
name|getSubject
argument_list|(
name|m
argument_list|,
name|assertionWrapper
argument_list|)
decl_stmt|;
specifier|final
name|String
name|name
init|=
name|subject
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
specifier|final
name|SecurityContext
name|sc
init|=
operator|new
name|SecurityContext
argument_list|()
block|{
specifier|public
name|Principal
name|getUserPrincipal
parameter_list|()
block|{
return|return
operator|new
name|SimplePrincipal
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isUserInRole
parameter_list|(
name|String
name|role
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
decl_stmt|;
name|m
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
specifier|protected
name|ResponseState
name|getValidResponseState
parameter_list|(
name|Cookie
name|securityContextCookie
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
if|if
condition|(
name|securityContextCookie
operator|==
literal|null
condition|)
block|{
comment|// most likely it means that the user has not been offered
comment|// a chance to get logged on yet, though it might be that the browser
comment|// has removed an expired cookie from its cache; warning is too noisy in the
comment|// former case
name|reportTrace
argument_list|(
literal|"MISSING_RESPONSE_STATE"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|String
name|contextKey
init|=
name|securityContextCookie
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|ResponseState
name|responseState
init|=
name|getStateProvider
argument_list|()
operator|.
name|getResponseState
argument_list|(
name|contextKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|responseState
operator|==
literal|null
condition|)
block|{
name|reportError
argument_list|(
literal|"MISSING_RESPONSE_STATE"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
if|if
condition|(
name|isStateExpired
argument_list|(
name|responseState
operator|.
name|getCreatedAt
argument_list|()
argument_list|,
name|responseState
operator|.
name|getExpiresAt
argument_list|()
argument_list|)
condition|)
block|{
name|reportError
argument_list|(
literal|"EXPIRED_RESPONSE_STATE"
argument_list|)
expr_stmt|;
name|getStateProvider
argument_list|()
operator|.
name|removeResponseState
argument_list|(
name|contextKey
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|String
name|webAppContext
init|=
name|getWebAppContext
argument_list|(
name|m
argument_list|)
decl_stmt|;
if|if
condition|(
name|webAppDomain
operator|!=
literal|null
operator|&&
operator|(
name|responseState
operator|.
name|getWebAppDomain
argument_list|()
operator|==
literal|null
operator|||
operator|!
name|webAppDomain
operator|.
name|equals
argument_list|(
name|responseState
operator|.
name|getWebAppDomain
argument_list|()
argument_list|)
operator|)
operator|||
name|responseState
operator|.
name|getWebAppContext
argument_list|()
operator|==
literal|null
operator|||
operator|!
name|webAppContext
operator|.
name|equals
argument_list|(
name|responseState
operator|.
name|getWebAppContext
argument_list|()
argument_list|)
condition|)
block|{
name|getStateProvider
argument_list|()
operator|.
name|removeResponseState
argument_list|(
name|contextKey
argument_list|)
expr_stmt|;
name|reportError
argument_list|(
literal|"INVALID_RESPONSE_STATE"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
if|if
condition|(
name|responseState
operator|.
name|getAssertion
argument_list|()
operator|==
literal|null
condition|)
block|{
name|reportError
argument_list|(
literal|"INVALID_RESPONSE_STATE"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
name|responseState
return|;
block|}
specifier|protected
name|SamlRequestInfo
name|createSamlRequestInfo
parameter_list|(
name|Message
name|m
parameter_list|)
throws|throws
name|Exception
block|{
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|doc
operator|.
name|appendChild
argument_list|(
name|doc
operator|.
name|createElement
argument_list|(
literal|"root"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Create the AuthnRequest
name|AuthnRequest
name|authnRequest
init|=
name|authnRequestBuilder
operator|.
name|createAuthnRequest
argument_list|(
name|m
argument_list|,
name|getIssuerId
argument_list|(
name|m
argument_list|)
argument_list|,
name|getAbsoluteAssertionServiceAddress
argument_list|(
name|m
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|isSignRequest
argument_list|()
condition|)
block|{
name|authnRequest
operator|.
name|setDestination
argument_list|(
name|idpServiceAddress
argument_list|)
expr_stmt|;
name|signAuthnRequest
argument_list|(
name|authnRequest
argument_list|)
expr_stmt|;
block|}
name|Element
name|authnRequestElement
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
name|String
name|authnRequestEncoded
init|=
name|encodeAuthnRequest
argument_list|(
name|authnRequestElement
argument_list|)
decl_stmt|;
name|SamlRequestInfo
name|info
init|=
operator|new
name|SamlRequestInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|setSamlRequest
argument_list|(
name|authnRequestEncoded
argument_list|)
expr_stmt|;
name|String
name|webAppContext
init|=
name|getWebAppContext
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|String
name|originalRequestURI
init|=
operator|new
name|UriInfoImpl
argument_list|(
name|m
argument_list|)
operator|.
name|getRequestUri
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|RequestState
name|requestState
init|=
operator|new
name|RequestState
argument_list|(
name|originalRequestURI
argument_list|,
name|getIdpServiceAddress
argument_list|()
argument_list|,
name|authnRequest
operator|.
name|getID
argument_list|()
argument_list|,
name|getIssuerId
argument_list|(
name|m
argument_list|)
argument_list|,
name|webAppContext
argument_list|,
name|getWebAppDomain
argument_list|()
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|relayState
init|=
name|URLEncoder
operator|.
name|encode
argument_list|(
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|getStateProvider
argument_list|()
operator|.
name|setRequestState
argument_list|(
name|relayState
argument_list|,
name|requestState
argument_list|)
expr_stmt|;
name|info
operator|.
name|setRelayState
argument_list|(
name|relayState
argument_list|)
expr_stmt|;
name|info
operator|.
name|setWebAppContext
argument_list|(
name|webAppContext
argument_list|)
expr_stmt|;
name|info
operator|.
name|setWebAppDomain
argument_list|(
name|getWebAppDomain
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
specifier|protected
specifier|abstract
name|String
name|encodeAuthnRequest
parameter_list|(
name|Element
name|authnRequest
parameter_list|)
throws|throws
name|IOException
function_decl|;
specifier|protected
specifier|abstract
name|void
name|signAuthnRequest
parameter_list|(
name|AuthnRequest
name|authnRequest
parameter_list|)
throws|throws
name|Exception
function_decl|;
specifier|private
name|String
name|getAbsoluteAssertionServiceAddress
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
if|if
condition|(
name|assertionConsumerServiceAddress
operator|==
literal|null
condition|)
block|{
comment|//TODO: Review the possibility of using this filter
comment|//for validating SAMLResponse too
name|reportError
argument_list|(
literal|"MISSING_ASSERTION_SERVICE_URL"
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
if|if
condition|(
operator|!
name|assertionConsumerServiceAddress
operator|.
name|startsWith
argument_list|(
literal|"http"
argument_list|)
condition|)
block|{
name|String
name|httpBasePath
init|=
operator|(
name|String
operator|)
name|m
operator|.
name|get
argument_list|(
literal|"http.base.path"
argument_list|)
decl_stmt|;
return|return
name|UriBuilder
operator|.
name|fromUri
argument_list|(
name|httpBasePath
argument_list|)
operator|.
name|path
argument_list|(
name|assertionConsumerServiceAddress
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|assertionConsumerServiceAddress
return|;
block|}
block|}
specifier|protected
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
specifier|protected
name|void
name|reportTrace
parameter_list|(
name|String
name|code
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
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
name|fine
argument_list|(
name|errorMsg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getWebAppContext
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
if|if
condition|(
name|addWebAppContext
condition|)
block|{
if|if
condition|(
name|addEndpointAddressToContext
condition|)
block|{
return|return
operator|new
name|UriInfoImpl
argument_list|(
name|m
argument_list|)
operator|.
name|getBaseUri
argument_list|()
operator|.
name|getRawPath
argument_list|()
return|;
block|}
else|else
block|{
name|String
name|httpBasePath
init|=
operator|(
name|String
operator|)
name|m
operator|.
name|get
argument_list|(
literal|"http.base.path"
argument_list|)
decl_stmt|;
return|return
name|URI
operator|.
name|create
argument_list|(
name|httpBasePath
argument_list|)
operator|.
name|getRawPath
argument_list|()
return|;
block|}
block|}
else|else
block|{
return|return
literal|"/"
return|;
block|}
block|}
specifier|public
name|String
name|getWebAppDomain
parameter_list|()
block|{
return|return
name|webAppDomain
return|;
block|}
specifier|public
name|void
name|setWebAppDomain
parameter_list|(
name|String
name|webAppDomain
parameter_list|)
block|{
name|this
operator|.
name|webAppDomain
operator|=
name|webAppDomain
expr_stmt|;
block|}
specifier|public
name|void
name|setAddWebAppContext
parameter_list|(
name|boolean
name|addWebAppContext
parameter_list|)
block|{
name|this
operator|.
name|addWebAppContext
operator|=
name|addWebAppContext
expr_stmt|;
block|}
block|}
end_class

end_unit

