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
name|services
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
name|List
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
name|MultivaluedMap
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
name|Response
operator|.
name|ResponseBuilder
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
name|OAuthError
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
name|ClientIdProvider
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
name|ClientSecretVerifier
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
name|utils
operator|.
name|AuthorizationUtils
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
name|security
operator|.
name|transport
operator|.
name|TLSSessionInfo
import|;
end_import

begin_class
specifier|public
class|class
name|AbstractTokenService
extends|extends
name|AbstractOAuthService
block|{
specifier|private
name|boolean
name|canSupportPublicClients
decl_stmt|;
specifier|private
name|boolean
name|writeCustomErrors
decl_stmt|;
specifier|private
name|ClientIdProvider
name|clientIdProvider
decl_stmt|;
specifier|private
name|ClientSecretVerifier
name|clientSecretVerifier
decl_stmt|;
comment|/**      * Make sure the client is authenticated      */
specifier|protected
name|Client
name|authenticateClientIfNeeded
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
name|Client
name|client
init|=
literal|null
decl_stmt|;
name|SecurityContext
name|sc
init|=
name|getMessageContext
argument_list|()
operator|.
name|getSecurityContext
argument_list|()
decl_stmt|;
name|Principal
name|principal
init|=
name|sc
operator|.
name|getUserPrincipal
argument_list|()
decl_stmt|;
name|String
name|clientId
init|=
name|retrieveClientId
argument_list|(
name|params
argument_list|)
decl_stmt|;
if|if
condition|(
name|principal
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|clientId
operator|!=
literal|null
condition|)
block|{
name|String
name|clientSecret
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_SECRET
argument_list|)
decl_stmt|;
if|if
condition|(
name|clientSecret
operator|!=
literal|null
condition|)
block|{
name|client
operator|=
name|getAndValidateClientFromIdAndSecret
argument_list|(
name|clientId
argument_list|,
name|clientSecret
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|validateClientAuthenticationMethod
argument_list|(
name|client
argument_list|,
name|OAuthConstants
operator|.
name|TOKEN_ENDPOINT_AUTH_POST
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|OAuthUtils
operator|.
name|isMutualTls
argument_list|(
name|sc
argument_list|,
name|getTlsSessionInfo
argument_list|()
argument_list|)
condition|)
block|{
name|client
operator|=
name|getClient
argument_list|(
name|clientId
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|checkCertificateBinding
argument_list|(
name|client
argument_list|,
name|getTlsSessionInfo
argument_list|()
argument_list|)
expr_stmt|;
name|validateClientAuthenticationMethod
argument_list|(
name|client
argument_list|,
name|OAuthConstants
operator|.
name|TOKEN_ENDPOINT_AUTH_TLS
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|canSupportPublicClients
condition|)
block|{
name|client
operator|=
name|getValidClient
argument_list|(
name|clientId
argument_list|,
name|params
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isValidPublicClient
argument_list|(
name|client
argument_list|,
name|clientId
argument_list|)
condition|)
block|{
name|client
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|validateClientAuthenticationMethod
argument_list|(
name|client
argument_list|,
name|OAuthConstants
operator|.
name|TOKEN_ENDPOINT_AUTH_NONE
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
if|if
condition|(
name|clientId
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|clientId
operator|.
name|equals
argument_list|(
name|principal
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|reportInvalidClient
argument_list|()
expr_stmt|;
block|}
name|client
operator|=
operator|(
name|Client
operator|)
name|getMessageContext
argument_list|()
operator|.
name|get
argument_list|(
name|Client
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|client
operator|=
name|getClient
argument_list|(
name|clientId
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|principal
operator|.
name|getName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|client
operator|=
name|getClient
argument_list|(
name|principal
operator|.
name|getName
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|client
operator|=
name|getClientFromTLSCertificates
argument_list|(
name|sc
argument_list|,
name|getTlsSessionInfo
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
comment|// Basic Authentication is expected by default
name|client
operator|=
name|getClientFromBasicAuthScheme
argument_list|(
name|params
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|reportInvalidClient
argument_list|()
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
specifier|protected
name|void
name|validateClientAuthenticationMethod
parameter_list|(
name|Client
name|c
parameter_list|,
name|String
name|authMethod
parameter_list|)
block|{
if|if
condition|(
name|c
operator|!=
literal|null
operator|&&
name|c
operator|.
name|getTokenEndpointAuthMethod
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|c
operator|.
name|getTokenEndpointAuthMethod
argument_list|()
operator|.
name|equals
argument_list|(
name|authMethod
argument_list|)
condition|)
block|{
name|reportInvalidClient
argument_list|(
operator|new
name|OAuthError
argument_list|(
name|OAuthConstants
operator|.
name|UNAUTHORIZED_CLIENT
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|String
name|retrieveClientId
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
name|String
name|clientId
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|clientId
operator|==
literal|null
condition|)
block|{
name|clientId
operator|=
operator|(
name|String
operator|)
name|getMessageContext
argument_list|()
operator|.
name|get
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_ID
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|clientId
operator|==
literal|null
operator|&&
name|clientIdProvider
operator|!=
literal|null
condition|)
block|{
name|clientId
operator|=
name|clientIdProvider
operator|.
name|getClientId
argument_list|(
name|getMessageContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|clientId
return|;
block|}
comment|// Get the Client and check the id and secret
specifier|protected
name|Client
name|getAndValidateClientFromIdAndSecret
parameter_list|(
name|String
name|clientId
parameter_list|,
name|String
name|providedClientSecret
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
name|Client
name|client
init|=
name|getClient
argument_list|(
name|clientId
argument_list|,
name|providedClientSecret
argument_list|,
name|params
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|client
operator|.
name|getClientId
argument_list|()
operator|.
name|equals
argument_list|(
name|clientId
argument_list|)
condition|)
block|{
name|reportInvalidClient
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|client
operator|.
name|isConfidential
argument_list|()
operator|||
operator|!
name|isConfidenatialClientSecretValid
argument_list|(
name|client
argument_list|,
name|providedClientSecret
argument_list|)
condition|)
block|{
name|reportInvalidClient
argument_list|()
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
specifier|protected
name|boolean
name|isConfidenatialClientSecretValid
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|providedClientSecret
parameter_list|)
block|{
if|if
condition|(
name|clientSecretVerifier
operator|!=
literal|null
condition|)
block|{
return|return
name|clientSecretVerifier
operator|.
name|validateClientSecret
argument_list|(
name|client
argument_list|,
name|providedClientSecret
argument_list|)
return|;
block|}
return|return
name|client
operator|.
name|getClientSecret
argument_list|()
operator|!=
literal|null
operator|&&
name|providedClientSecret
operator|!=
literal|null
operator|&&
name|client
operator|.
name|getClientSecret
argument_list|()
operator|.
name|equals
argument_list|(
name|providedClientSecret
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|isValidPublicClient
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|clientId
parameter_list|)
block|{
return|return
name|canSupportPublicClients
operator|&&
operator|!
name|client
operator|.
name|isConfidential
argument_list|()
operator|&&
name|client
operator|.
name|getClientSecret
argument_list|()
operator|==
literal|null
return|;
block|}
specifier|protected
name|Client
name|getClientFromBasicAuthScheme
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
name|Client
name|client
init|=
literal|null
decl_stmt|;
name|String
index|[]
name|userInfo
init|=
name|AuthorizationUtils
operator|.
name|getBasicAuthUserInfo
argument_list|(
name|getMessageContext
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|userInfo
operator|!=
literal|null
operator|&&
name|userInfo
operator|.
name|length
operator|==
literal|2
condition|)
block|{
name|client
operator|=
name|getAndValidateClientFromIdAndSecret
argument_list|(
name|userInfo
index|[
literal|0
index|]
argument_list|,
name|userInfo
index|[
literal|1
index|]
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
name|validateClientAuthenticationMethod
argument_list|(
name|client
argument_list|,
name|OAuthConstants
operator|.
name|TOKEN_ENDPOINT_AUTH_BASIC
argument_list|)
expr_stmt|;
return|return
name|client
return|;
block|}
specifier|protected
name|void
name|checkCertificateBinding
parameter_list|(
name|Client
name|client
parameter_list|,
name|TLSSessionInfo
name|tlsSessionInfo
parameter_list|)
block|{
name|String
name|subjectDn
init|=
name|client
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|OAuthConstants
operator|.
name|TLS_CLIENT_AUTH_SUBJECT_DN
argument_list|)
decl_stmt|;
if|if
condition|(
name|subjectDn
operator|==
literal|null
operator|&&
name|client
operator|.
name|getApplicationCertificates
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Client \""
operator|+
name|client
operator|.
name|getClientId
argument_list|()
operator|+
literal|"\" can not be bound to the TLS certificate"
argument_list|)
expr_stmt|;
name|reportInvalidClient
argument_list|()
expr_stmt|;
block|}
name|X509Certificate
name|cert
init|=
name|OAuthUtils
operator|.
name|getRootTLSCertificate
argument_list|(
name|tlsSessionInfo
argument_list|)
decl_stmt|;
if|if
condition|(
name|subjectDn
operator|!=
literal|null
operator|&&
operator|!
name|subjectDn
operator|.
name|equals
argument_list|(
name|OAuthUtils
operator|.
name|getSubjectDnFromTLSCertificates
argument_list|(
name|cert
argument_list|)
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Client \""
operator|+
name|client
operator|.
name|getClientId
argument_list|()
operator|+
literal|"\" can not be bound to the TLS certificate"
argument_list|)
expr_stmt|;
name|reportInvalidClient
argument_list|()
expr_stmt|;
block|}
name|String
name|issuerDn
init|=
name|client
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|OAuthConstants
operator|.
name|TLS_CLIENT_AUTH_ISSUER_DN
argument_list|)
decl_stmt|;
if|if
condition|(
name|issuerDn
operator|!=
literal|null
operator|&&
operator|!
name|issuerDn
operator|.
name|equals
argument_list|(
name|OAuthUtils
operator|.
name|getIssuerDnFromTLSCertificates
argument_list|(
name|cert
argument_list|)
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Client \""
operator|+
name|client
operator|.
name|getClientId
argument_list|()
operator|+
literal|"\" can not be bound to the TLS certificate"
argument_list|)
expr_stmt|;
name|reportInvalidClient
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|client
operator|.
name|getApplicationCertificates
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|compareTlsCertificates
argument_list|(
name|tlsSessionInfo
argument_list|,
name|client
operator|.
name|getApplicationCertificates
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|OAuthUtils
operator|.
name|setCertificateThumbprintConfirmation
argument_list|(
name|getMessageContext
argument_list|()
argument_list|,
name|cert
argument_list|)
expr_stmt|;
block|}
specifier|private
name|TLSSessionInfo
name|getTlsSessionInfo
parameter_list|()
block|{
return|return
operator|(
name|TLSSessionInfo
operator|)
name|getMessageContext
argument_list|()
operator|.
name|get
argument_list|(
name|TLSSessionInfo
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|Client
name|getClientFromTLSCertificates
parameter_list|(
name|SecurityContext
name|sc
parameter_list|,
name|TLSSessionInfo
name|tlsSessionInfo
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
name|Client
name|client
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|OAuthUtils
operator|.
name|isMutualTls
argument_list|(
name|sc
argument_list|,
name|tlsSessionInfo
argument_list|)
condition|)
block|{
name|X509Certificate
name|cert
init|=
name|OAuthUtils
operator|.
name|getRootTLSCertificate
argument_list|(
name|tlsSessionInfo
argument_list|)
decl_stmt|;
name|String
name|subjectDn
init|=
name|OAuthUtils
operator|.
name|getSubjectDnFromTLSCertificates
argument_list|(
name|cert
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|subjectDn
argument_list|)
condition|)
block|{
name|client
operator|=
name|getClient
argument_list|(
name|subjectDn
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|validateClientAuthenticationMethod
argument_list|(
name|client
argument_list|,
name|OAuthConstants
operator|.
name|TOKEN_ENDPOINT_AUTH_TLS
argument_list|)
expr_stmt|;
comment|// The certificates must be registered with the client and match TLS certificates
comment|// in case of the binding where Client's clientId is a subject distinguished name
name|compareTlsCertificates
argument_list|(
name|tlsSessionInfo
argument_list|,
name|client
operator|.
name|getApplicationCertificates
argument_list|()
argument_list|)
expr_stmt|;
name|OAuthUtils
operator|.
name|setCertificateThumbprintConfirmation
argument_list|(
name|getMessageContext
argument_list|()
argument_list|,
name|cert
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|client
return|;
block|}
specifier|protected
name|void
name|compareTlsCertificates
parameter_list|(
name|TLSSessionInfo
name|tlsInfo
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|base64EncodedCerts
parameter_list|)
block|{
if|if
condition|(
operator|!
name|OAuthUtils
operator|.
name|compareTlsCertificates
argument_list|(
name|tlsInfo
argument_list|,
name|base64EncodedCerts
argument_list|)
condition|)
block|{
name|reportInvalidClient
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|Response
name|handleException
parameter_list|(
name|OAuthServiceException
name|ex
parameter_list|,
name|String
name|error
parameter_list|)
block|{
name|OAuthError
name|customError
init|=
name|ex
operator|.
name|getError
argument_list|()
decl_stmt|;
if|if
condition|(
name|writeCustomErrors
operator|&&
name|customError
operator|!=
literal|null
condition|)
block|{
return|return
name|createErrorResponseFromBean
argument_list|(
name|customError
argument_list|)
return|;
block|}
return|return
name|createErrorResponseFromBean
argument_list|(
operator|new
name|OAuthError
argument_list|(
name|error
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|Response
name|createErrorResponse
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|,
name|String
name|error
parameter_list|)
block|{
return|return
name|createErrorResponseFromBean
argument_list|(
operator|new
name|OAuthError
argument_list|(
name|error
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|Response
name|createErrorResponseFromErrorCode
parameter_list|(
name|String
name|error
parameter_list|)
block|{
return|return
name|createErrorResponseFromBean
argument_list|(
operator|new
name|OAuthError
argument_list|(
name|error
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|Response
name|createErrorResponseFromBean
parameter_list|(
name|OAuthError
name|errorBean
parameter_list|)
block|{
return|return
name|JAXRSUtils
operator|.
name|toResponseBuilder
argument_list|(
literal|400
argument_list|)
operator|.
name|entity
argument_list|(
name|errorBean
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**      * Get the {@link Client} reference      * @param clientId the provided client id      * @return Client the client reference      * @throws {@link javax.ws.rs.WebApplicationException} if no matching Client is found      */
specifier|protected
name|Client
name|getClient
parameter_list|(
name|String
name|clientId
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
return|return
name|getClient
argument_list|(
name|clientId
argument_list|,
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_SECRET
argument_list|)
argument_list|,
name|params
argument_list|)
return|;
block|}
specifier|protected
name|Client
name|getClient
parameter_list|(
name|String
name|clientId
parameter_list|,
name|String
name|clientSecret
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
if|if
condition|(
name|clientId
operator|==
literal|null
condition|)
block|{
name|reportInvalidRequestError
argument_list|(
literal|"Client ID is null"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|Client
name|client
init|=
literal|null
decl_stmt|;
try|try
block|{
name|client
operator|=
name|getValidClient
argument_list|(
name|clientId
argument_list|,
name|clientSecret
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OAuthServiceException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"No valid client found for clientId: "
operator|+
name|clientId
argument_list|)
expr_stmt|;
if|if
condition|(
name|ex
operator|.
name|getError
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|reportInvalidClient
argument_list|(
name|ex
operator|.
name|getError
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"No valid client found for clientId: "
operator|+
name|clientId
argument_list|)
expr_stmt|;
name|reportInvalidClient
argument_list|()
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
specifier|protected
name|void
name|reportInvalidClient
parameter_list|()
block|{
name|reportInvalidClient
argument_list|(
operator|new
name|OAuthError
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_CLIENT
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|reportInvalidClient
parameter_list|(
name|OAuthError
name|error
parameter_list|)
block|{
name|ResponseBuilder
name|rb
init|=
name|JAXRSUtils
operator|.
name|toResponseBuilder
argument_list|(
literal|401
argument_list|)
decl_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
name|rb
operator|.
name|type
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
operator|.
name|entity
argument_list|(
name|error
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
throw|;
block|}
specifier|public
name|void
name|setCanSupportPublicClients
parameter_list|(
name|boolean
name|support
parameter_list|)
block|{
name|this
operator|.
name|canSupportPublicClients
operator|=
name|support
expr_stmt|;
block|}
specifier|public
name|boolean
name|isCanSupportPublicClients
parameter_list|()
block|{
return|return
name|canSupportPublicClients
return|;
block|}
specifier|public
name|void
name|setWriteCustomErrors
parameter_list|(
name|boolean
name|writeCustomErrors
parameter_list|)
block|{
name|this
operator|.
name|writeCustomErrors
operator|=
name|writeCustomErrors
expr_stmt|;
block|}
specifier|public
name|void
name|setClientIdProvider
parameter_list|(
name|ClientIdProvider
name|clientIdProvider
parameter_list|)
block|{
name|this
operator|.
name|clientIdProvider
operator|=
name|clientIdProvider
expr_stmt|;
block|}
specifier|public
name|void
name|setClientSecretVerifier
parameter_list|(
name|ClientSecretVerifier
name|clientSecretVerifier
parameter_list|)
block|{
name|this
operator|.
name|clientSecretVerifier
operator|=
name|clientSecretVerifier
expr_stmt|;
block|}
block|}
end_class

end_unit

