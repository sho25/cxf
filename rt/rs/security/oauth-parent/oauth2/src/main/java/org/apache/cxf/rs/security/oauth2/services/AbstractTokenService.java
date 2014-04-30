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
name|Arrays
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
name|x500
operator|.
name|X500Principal
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
name|ClientKey
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
if|if
condition|(
name|params
operator|.
name|containsKey
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_ID
argument_list|)
condition|)
block|{
comment|// Both client_id and client_secret are expected in the form payload
name|client
operator|=
name|getAndValidateClientFromIdAndSecret
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_ID
argument_list|)
argument_list|,
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_SECRET
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sc
operator|.
name|getUserPrincipal
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// Client has already been authenticated
name|Principal
name|p
init|=
name|sc
operator|.
name|getUserPrincipal
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
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
name|p
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Most likely a container-level authentication, possibly 2-way TLS,
comment|// Check if the mapping between Principal and Client Id has been done in a filter
name|String
name|clientId
init|=
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
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|clientId
argument_list|)
operator|&&
name|clientIdProvider
operator|!=
literal|null
condition|)
block|{
comment|// Check Custom ClientIdProvider
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
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|clientId
argument_list|)
condition|)
block|{
name|client
operator|=
name|getClient
argument_list|(
name|clientId
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|TLSSessionInfo
name|tlsSessionInfo
init|=
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
decl_stmt|;
if|if
condition|(
name|tlsSessionInfo
operator|!=
literal|null
condition|)
block|{
name|client
operator|=
name|getClientFromTLSCertificates
argument_list|(
name|sc
argument_list|,
name|tlsSessionInfo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Basic Authentication is expected by default
name|client
operator|=
name|getClientFromBasicAuthScheme
argument_list|()
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
comment|// Get the Client and check the id and secret
specifier|protected
name|Client
name|getAndValidateClientFromIdAndSecret
parameter_list|(
name|String
name|clientId
parameter_list|,
name|String
name|clientSecret
parameter_list|)
block|{
name|Client
name|client
init|=
name|getClient
argument_list|(
name|clientId
argument_list|)
decl_stmt|;
if|if
condition|(
name|clientSecret
operator|!=
literal|null
operator|&&
operator|(
name|client
operator|.
name|getClientKey
argument_list|()
operator|.
name|getType
argument_list|()
operator|==
literal|null
operator|||
name|ClientKey
operator|.
name|Type
operator|.
name|PASSWORD
operator|!=
name|client
operator|.
name|getClientKey
argument_list|()
operator|.
name|getType
argument_list|()
operator|)
condition|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
if|if
condition|(
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
name|getClientKey
argument_list|()
operator|==
literal|null
operator|&&
name|clientSecret
operator|==
literal|null
condition|)
block|{
return|return
name|client
return|;
block|}
if|if
condition|(
name|clientSecret
operator|==
literal|null
operator|||
name|client
operator|.
name|getClientKey
argument_list|()
operator|==
literal|null
operator|||
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
operator|||
operator|!
name|client
operator|.
name|getClientKey
argument_list|()
operator|.
name|getKey
argument_list|()
operator|.
name|equals
argument_list|(
name|clientSecret
argument_list|)
condition|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
return|return
name|client
return|;
block|}
specifier|protected
name|Client
name|getClientFromBasicAuthScheme
parameter_list|()
block|{
name|String
index|[]
name|parts
init|=
name|AuthorizationUtils
operator|.
name|getAuthorizationParts
argument_list|(
name|getMessageContext
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|OAuthConstants
operator|.
name|BASIC_SCHEME
operator|.
name|equalsIgnoreCase
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|String
index|[]
name|authInfo
init|=
name|AuthorizationUtils
operator|.
name|getBasicAuthParts
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
return|return
name|getAndValidateClientFromIdAndSecret
argument_list|(
name|authInfo
index|[
literal|0
index|]
argument_list|,
name|authInfo
index|[
literal|1
index|]
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
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
parameter_list|)
block|{
name|Client
name|client
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|tlsSessionInfo
operator|!=
literal|null
condition|)
block|{
name|String
name|authScheme
init|=
name|sc
operator|.
name|getAuthenticationScheme
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|authScheme
argument_list|)
condition|)
block|{
comment|// Pure 2-way TLS authentication
name|String
name|clientId
init|=
name|getClientIdFromTLSCertificates
argument_list|(
name|sc
argument_list|,
name|tlsSessionInfo
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|clientId
argument_list|)
condition|)
block|{
name|client
operator|=
name|getClient
argument_list|(
name|clientId
argument_list|)
expr_stmt|;
comment|// Validate the client identified from certificates
name|validateTwoWayTlsClient
argument_list|(
name|sc
argument_list|,
name|tlsSessionInfo
argument_list|,
name|client
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|OAuthConstants
operator|.
name|BASIC_SCHEME
operator|.
name|equalsIgnoreCase
argument_list|(
name|authScheme
argument_list|)
condition|)
block|{
comment|// Basic Authentication on top of 2-way TLS
name|client
operator|=
name|getClientFromBasicAuthScheme
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|client
return|;
block|}
specifier|protected
name|String
name|getClientIdFromTLSCertificates
parameter_list|(
name|SecurityContext
name|sc
parameter_list|,
name|TLSSessionInfo
name|tlsInfo
parameter_list|)
block|{
name|Certificate
index|[]
name|clientCerts
init|=
name|tlsInfo
operator|.
name|getPeerCertificates
argument_list|()
decl_stmt|;
if|if
condition|(
name|clientCerts
operator|!=
literal|null
operator|&&
name|clientCerts
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|X500Principal
name|x509Principal
init|=
operator|(
operator|(
name|X509Certificate
operator|)
name|clientCerts
index|[
literal|0
index|]
operator|)
operator|.
name|getSubjectX500Principal
argument_list|()
decl_stmt|;
return|return
name|x509Principal
operator|.
name|getName
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|void
name|validateTwoWayTlsClient
parameter_list|(
name|SecurityContext
name|sc
parameter_list|,
name|TLSSessionInfo
name|tlsSessionInfo
parameter_list|,
name|Client
name|client
parameter_list|)
block|{
name|ClientKey
operator|.
name|Type
name|credType
init|=
name|client
operator|.
name|getClientKey
argument_list|()
operator|.
name|getType
argument_list|()
decl_stmt|;
if|if
condition|(
name|credType
operator|!=
name|ClientKey
operator|.
name|Type
operator|.
name|X509CERTIFICATE
condition|)
block|{
name|reportInvalidClient
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|client
operator|.
name|getClientKey
argument_list|()
operator|.
name|getKey
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// Client has a Base64 encoded representation of the certificate loaded
comment|// so lets validate the TLS certificates
name|compareCertificates
argument_list|(
name|tlsSessionInfo
argument_list|,
name|client
operator|.
name|getClientKey
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|,
name|credType
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|compareCertificates
parameter_list|(
name|TLSSessionInfo
name|tlsInfo
parameter_list|,
name|String
name|base64EncodedCert
parameter_list|,
name|ClientKey
operator|.
name|Type
name|type
parameter_list|)
block|{
name|Certificate
index|[]
name|clientCerts
init|=
name|tlsInfo
operator|.
name|getPeerCertificates
argument_list|()
decl_stmt|;
try|try
block|{
name|X509Certificate
name|cert
init|=
operator|(
name|X509Certificate
operator|)
name|clientCerts
index|[
literal|0
index|]
decl_stmt|;
name|byte
index|[]
name|encodedKey
init|=
name|cert
operator|.
name|getEncoded
argument_list|()
decl_stmt|;
name|byte
index|[]
name|clientKey
init|=
name|Base64Utility
operator|.
name|decode
argument_list|(
name|base64EncodedCert
argument_list|)
decl_stmt|;
if|if
condition|(
name|Arrays
operator|.
name|equals
argument_list|(
name|encodedKey
argument_list|,
name|clientKey
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
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
else|else
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
comment|/**      * Get the {@link Client} reference      * @param clientId the provided client id      * @return Client the client reference       * @throws {@link javax.ws.rs.WebApplicationException} if no matching Client is found      */
specifier|protected
name|Client
name|getClient
parameter_list|(
name|String
name|clientId
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
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OAuthServiceException
name|ex
parameter_list|)
block|{
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
block|}
end_class

end_unit

