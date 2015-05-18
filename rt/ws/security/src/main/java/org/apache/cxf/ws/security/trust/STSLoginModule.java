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
name|ws
operator|.
name|security
operator|.
name|trust
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
name|MalformedURLException
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Map
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
name|security
operator|.
name|auth
operator|.
name|Subject
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
name|Callback
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
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|NameCallback
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
name|PasswordCallback
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
name|UnsupportedCallbackException
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
name|login
operator|.
name|LoginException
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
name|spi
operator|.
name|LoginModule
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
name|Bus
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
name|BusException
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
name|BusFactory
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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|endpoint
operator|.
name|EndpointException
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
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|ClaimCollection
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
name|utils
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
name|rt
operator|.
name|security
operator|.
name|utils
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
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|tokenstore
operator|.
name|EHCacheTokenStore
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
name|tokenstore
operator|.
name|TokenStore
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
name|tokenstore
operator|.
name|TokenStoreFactory
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
name|trust
operator|.
name|claims
operator|.
name|RoleClaimsCallbackHandler
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
name|wss4j
operator|.
name|WSS4JInInterceptor
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
name|WSConstants
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
name|message
operator|.
name|token
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
name|wss4j
operator|.
name|dom
operator|.
name|validate
operator|.
name|Credential
import|;
end_import

begin_comment
comment|/**  * A JAAS LoginModule for authenticating a Username/Password to the STS. It can be configured  * either by specifying the various options (documented below) in the JAAS configuration, or  * else by picking up a CXF STSClient from the CXF bus (either the default one, or else one  * that has the same QName as the service name).  */
end_comment

begin_class
specifier|public
class|class
name|STSLoginModule
implements|implements
name|LoginModule
block|{
comment|/**      * Whether we require roles or not from the STS. If this is not set then the       * WS-Trust validate binding is used. If it is set then the issue binding is       * used, where the Username + Password credentials are passed via "OnBehalfOf"      * (unless the DISABLE_ON_BEHALF_OF property is set to "true", see below). In addition,       * claims are added to the request for the standard "role" ClaimType.      */
specifier|public
specifier|static
specifier|final
name|String
name|REQUIRE_ROLES
init|=
literal|"require.roles"
decl_stmt|;
comment|/**      * Whether to disable passing Username + Password credentials via "OnBehalfOf". If the      * REQUIRE_ROLES property (see above) is set to "true", then the Issue Binding is used      * and the credentials are passed via OnBehalfOf. If this (DISABLE_ON_BEHALF_OF) property      * is set to "true", then the credentials instead are passed through to the       * WS-SecurityPolicy layer and used depending on the security policy of the STS endpoint.      * For example, if the STS endpoint requires a WS-Security UsernameToken, then the       * credentials are inserted here.      */
specifier|public
specifier|static
specifier|final
name|String
name|DISABLE_ON_BEHALF_OF
init|=
literal|"disable.on.behalf.of"
decl_stmt|;
comment|/**      * Whether to disable caching of validated credentials or not. The default is "false", meaning that      * caching is enabled. However, caching only applies when token transformation takes place, i.e. when      * the "require.roles" property is set to "true".      */
specifier|public
specifier|static
specifier|final
name|String
name|DISABLE_CACHING
init|=
literal|"disable.caching"
decl_stmt|;
comment|/**      * The WSDL Location of the STS      */
specifier|public
specifier|static
specifier|final
name|String
name|WSDL_LOCATION
init|=
literal|"wsdl.location"
decl_stmt|;
comment|/**      * The Service QName of the STS      */
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_NAME
init|=
literal|"service.name"
decl_stmt|;
comment|/**      * The Endpoint QName of the STS      */
specifier|public
specifier|static
specifier|final
name|String
name|ENDPOINT_NAME
init|=
literal|"endpoint.name"
decl_stmt|;
comment|/**      * The default key size to use if using the SymmetricKey KeyType. Defaults to 256.      */
specifier|public
specifier|static
specifier|final
name|String
name|KEY_SIZE
init|=
literal|"key.size"
decl_stmt|;
comment|/**      * The key type to use. The default is the standard "Bearer" URI.      */
specifier|public
specifier|static
specifier|final
name|String
name|KEY_TYPE
init|=
literal|"key.type"
decl_stmt|;
comment|/**      * The token type to use. The default is the standard SAML 2.0 URI.      */
specifier|public
specifier|static
specifier|final
name|String
name|TOKEN_TYPE
init|=
literal|"token.type"
decl_stmt|;
comment|/**      * The WS-Trust namespace to use. The default is the WS-Trust 1.3 namespace.      */
specifier|public
specifier|static
specifier|final
name|String
name|WS_TRUST_NAMESPACE
init|=
literal|"ws.trust.namespace"
decl_stmt|;
comment|/**      * The location of a Spring configuration file that can be used to configure the      * STS client (for example, to configure the TrustStore if TLS is used). This is      * designed to be used if the service that is being secured is not CXF-based.      */
specifier|public
specifier|static
specifier|final
name|String
name|CXF_SPRING_CFG
init|=
literal|"cxf.spring.config"
decl_stmt|;
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
name|STSLoginModule
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TOKEN_STORE_KEY
init|=
literal|"sts.login.module.tokenstore"
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|Principal
argument_list|>
name|roles
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Principal
name|userPrincipal
decl_stmt|;
specifier|private
name|Subject
name|subject
decl_stmt|;
specifier|private
name|CallbackHandler
name|callbackHandler
decl_stmt|;
specifier|private
name|boolean
name|requireRoles
decl_stmt|;
specifier|private
name|boolean
name|disableOnBehalfOf
decl_stmt|;
specifier|private
name|boolean
name|disableCaching
decl_stmt|;
specifier|private
name|String
name|wsdlLocation
decl_stmt|;
specifier|private
name|String
name|serviceName
decl_stmt|;
specifier|private
name|String
name|endpointName
decl_stmt|;
specifier|private
name|String
name|cxfSpringCfg
decl_stmt|;
specifier|private
name|int
name|keySize
decl_stmt|;
specifier|private
name|String
name|keyType
init|=
literal|"http://docs.oasis-open.org/ws-sx/ws-trust/200512/Bearer"
decl_stmt|;
specifier|private
name|String
name|tokenType
init|=
literal|"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0"
decl_stmt|;
specifier|private
name|String
name|namespace
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|stsClientProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|(
name|Subject
name|subj
parameter_list|,
name|CallbackHandler
name|cbHandler
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|sharedState
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|options
parameter_list|)
block|{
name|subject
operator|=
name|subj
expr_stmt|;
name|callbackHandler
operator|=
name|cbHandler
expr_stmt|;
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|REQUIRE_ROLES
argument_list|)
condition|)
block|{
name|requireRoles
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|REQUIRE_ROLES
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|DISABLE_ON_BEHALF_OF
argument_list|)
condition|)
block|{
name|disableOnBehalfOf
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|DISABLE_ON_BEHALF_OF
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|DISABLE_CACHING
argument_list|)
condition|)
block|{
name|disableCaching
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|DISABLE_CACHING
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|WSDL_LOCATION
argument_list|)
condition|)
block|{
name|wsdlLocation
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|WSDL_LOCATION
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|SERVICE_NAME
argument_list|)
condition|)
block|{
name|serviceName
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|SERVICE_NAME
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|ENDPOINT_NAME
argument_list|)
condition|)
block|{
name|endpointName
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|ENDPOINT_NAME
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|KEY_SIZE
argument_list|)
condition|)
block|{
name|keySize
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|KEY_SIZE
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|KEY_TYPE
argument_list|)
condition|)
block|{
name|keyType
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|KEY_TYPE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|TOKEN_TYPE
argument_list|)
condition|)
block|{
name|tokenType
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|TOKEN_TYPE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|WS_TRUST_NAMESPACE
argument_list|)
condition|)
block|{
name|namespace
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|WS_TRUST_NAMESPACE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|CXF_SPRING_CFG
argument_list|)
condition|)
block|{
name|cxfSpringCfg
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|CXF_SPRING_CFG
argument_list|)
expr_stmt|;
block|}
name|stsClientProperties
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|s
range|:
name|SecurityConstants
operator|.
name|ALL_PROPERTIES
control|)
block|{
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|stsClientProperties
operator|.
name|put
argument_list|(
name|s
argument_list|,
name|options
operator|.
name|get
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|login
parameter_list|()
throws|throws
name|LoginException
block|{
comment|// Get username and password
name|Callback
index|[]
name|callbacks
init|=
operator|new
name|Callback
index|[
literal|2
index|]
decl_stmt|;
name|callbacks
index|[
literal|0
index|]
operator|=
operator|new
name|NameCallback
argument_list|(
literal|"Username: "
argument_list|)
expr_stmt|;
name|callbacks
index|[
literal|1
index|]
operator|=
operator|new
name|PasswordCallback
argument_list|(
literal|"Password: "
argument_list|,
literal|false
argument_list|)
expr_stmt|;
try|try
block|{
name|callbackHandler
operator|.
name|handle
argument_list|(
name|callbacks
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioException
parameter_list|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
name|ioException
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|UnsupportedCallbackException
name|unsupportedCallbackException
parameter_list|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
name|unsupportedCallbackException
operator|.
name|getMessage
argument_list|()
operator|+
literal|" not available to obtain information from user."
argument_list|)
throw|;
block|}
name|String
name|user
init|=
operator|(
operator|(
name|NameCallback
operator|)
name|callbacks
index|[
literal|0
index|]
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
name|char
index|[]
name|tmpPassword
init|=
operator|(
operator|(
name|PasswordCallback
operator|)
name|callbacks
index|[
literal|1
index|]
operator|)
operator|.
name|getPassword
argument_list|()
decl_stmt|;
if|if
condition|(
name|tmpPassword
operator|==
literal|null
condition|)
block|{
name|tmpPassword
operator|=
operator|new
name|char
index|[
literal|0
index|]
expr_stmt|;
block|}
name|String
name|password
init|=
operator|new
name|String
argument_list|(
name|tmpPassword
argument_list|)
decl_stmt|;
name|roles
operator|=
operator|new
name|HashSet
argument_list|<
name|Principal
argument_list|>
argument_list|()
expr_stmt|;
name|userPrincipal
operator|=
literal|null
expr_stmt|;
name|STSTokenValidator
name|validator
init|=
operator|new
name|STSTokenValidator
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|validator
operator|.
name|setUseIssueBinding
argument_list|(
name|requireRoles
argument_list|)
expr_stmt|;
name|validator
operator|.
name|setUseOnBehalfOf
argument_list|(
operator|!
name|disableOnBehalfOf
argument_list|)
expr_stmt|;
name|validator
operator|.
name|setDisableCaching
argument_list|(
operator|!
name|requireRoles
operator|||
name|disableCaching
argument_list|)
expr_stmt|;
comment|// Authenticate token
try|try
block|{
name|UsernameToken
name|token
init|=
name|convertToToken
argument_list|(
name|user
argument_list|,
name|password
argument_list|)
decl_stmt|;
name|Credential
name|credential
init|=
operator|new
name|Credential
argument_list|()
decl_stmt|;
name|credential
operator|.
name|setUsernametoken
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|RequestData
name|data
init|=
operator|new
name|RequestData
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|STSClient
name|stsClient
init|=
name|configureSTSClient
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_CLIENT
argument_list|,
name|stsClient
argument_list|)
expr_stmt|;
name|data
operator|.
name|setMsgContext
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|TokenStore
name|tokenStore
init|=
name|configureTokenStore
argument_list|()
decl_stmt|;
name|validator
operator|.
name|setStsClient
argument_list|(
name|stsClient
argument_list|)
expr_stmt|;
name|validator
operator|.
name|setTokenStore
argument_list|(
name|tokenStore
argument_list|)
expr_stmt|;
block|}
name|credential
operator|=
name|validator
operator|.
name|validate
argument_list|(
name|credential
argument_list|,
name|data
argument_list|)
expr_stmt|;
comment|// Add user principal
name|userPrincipal
operator|=
operator|new
name|SimplePrincipal
argument_list|(
name|user
argument_list|)
expr_stmt|;
comment|// Add roles if a SAML Assertion was returned from the STS
name|roles
operator|.
name|addAll
argument_list|(
name|getRoles
argument_list|(
name|message
argument_list|,
name|credential
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"User "
operator|+
name|user
operator|+
literal|" authentication failed"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|LoginException
argument_list|(
literal|"User "
operator|+
name|user
operator|+
literal|" authentication failed: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|STSClient
name|configureSTSClient
parameter_list|(
name|Message
name|msg
parameter_list|)
throws|throws
name|BusException
throws|,
name|EndpointException
block|{
name|STSClient
name|c
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|cxfSpringCfg
operator|!=
literal|null
condition|)
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|URL
name|busFile
init|=
name|Loader
operator|.
name|getResource
argument_list|(
name|cxfSpringCfg
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|SpringBusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|SpringBusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|c
operator|=
operator|new
name|STSClient
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|msg
operator|==
literal|null
condition|)
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|c
operator|=
operator|new
name|STSClient
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|c
operator|=
name|STSUtils
operator|.
name|getClient
argument_list|(
name|msg
argument_list|,
literal|"sts"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wsdlLocation
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|setWsdlLocation
argument_list|(
name|wsdlLocation
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|serviceName
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|setServiceName
argument_list|(
name|serviceName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpointName
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|setEndpointName
argument_list|(
name|endpointName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|keySize
operator|>
literal|0
condition|)
block|{
name|c
operator|.
name|setKeySize
argument_list|(
name|keySize
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|keyType
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|setKeyType
argument_list|(
name|keyType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|tokenType
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|setTokenType
argument_list|(
name|tokenType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|namespace
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|setNamespace
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
block|}
name|c
operator|.
name|setProperties
argument_list|(
name|stsClientProperties
argument_list|)
expr_stmt|;
if|if
condition|(
name|requireRoles
operator|&&
name|c
operator|.
name|getClaimsCallbackHandler
argument_list|()
operator|==
literal|null
condition|)
block|{
name|c
operator|.
name|setClaimsCallbackHandler
argument_list|(
operator|new
name|RoleClaimsCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
specifier|private
name|TokenStore
name|configureTokenStore
parameter_list|()
throws|throws
name|MalformedURLException
block|{
if|if
condition|(
name|TokenStoreFactory
operator|.
name|isEhCacheInstalled
argument_list|()
condition|)
block|{
name|String
name|cfg
init|=
literal|"cxf-ehcache.xml"
decl_stmt|;
name|URL
name|url
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|url
operator|=
name|ClassLoaderUtils
operator|.
name|getResource
argument_list|(
name|cfg
argument_list|,
name|STSLoginModule
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
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
name|cfg
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
return|return
operator|new
name|EHCacheTokenStore
argument_list|(
name|TOKEN_STORE_KEY
argument_list|,
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|,
name|url
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|UsernameToken
name|convertToToken
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
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
name|UsernameToken
name|token
init|=
operator|new
name|UsernameToken
argument_list|(
literal|false
argument_list|,
name|doc
argument_list|,
name|WSConstants
operator|.
name|PASSWORD_TEXT
argument_list|)
decl_stmt|;
name|token
operator|.
name|setName
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|token
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
return|return
name|token
return|;
block|}
specifier|private
name|Set
argument_list|<
name|Principal
argument_list|>
name|getRoles
parameter_list|(
name|Message
name|msg
parameter_list|,
name|Credential
name|credential
parameter_list|)
block|{
name|SamlAssertionWrapper
name|samlAssertion
init|=
name|credential
operator|.
name|getTransformedToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|samlAssertion
operator|==
literal|null
condition|)
block|{
name|samlAssertion
operator|=
name|credential
operator|.
name|getSamlAssertion
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|samlAssertion
operator|!=
literal|null
condition|)
block|{
name|String
name|roleAttributeName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|msg
operator|!=
literal|null
condition|)
block|{
name|roleAttributeName
operator|=
operator|(
name|String
operator|)
name|SecurityUtils
operator|.
name|getSecurityPropertyValue
argument_list|(
name|SecurityConstants
operator|.
name|SAML_ROLE_ATTRIBUTENAME
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|roleAttributeName
operator|==
literal|null
operator|||
name|roleAttributeName
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|roleAttributeName
operator|=
name|WSS4JInInterceptor
operator|.
name|SAML_ROLE_ATTRIBUTENAME_DEFAULT
expr_stmt|;
block|}
name|ClaimCollection
name|claims
init|=
name|SAMLUtils
operator|.
name|getClaims
argument_list|(
operator|(
name|SamlAssertionWrapper
operator|)
name|samlAssertion
argument_list|)
decl_stmt|;
return|return
name|SAMLUtils
operator|.
name|parseRolesFromClaims
argument_list|(
name|claims
argument_list|,
name|roleAttributeName
argument_list|,
literal|null
argument_list|)
return|;
block|}
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|commit
parameter_list|()
throws|throws
name|LoginException
block|{
if|if
condition|(
name|userPrincipal
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|add
argument_list|(
name|userPrincipal
argument_list|)
expr_stmt|;
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|addAll
argument_list|(
name|roles
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|abort
parameter_list|()
throws|throws
name|LoginException
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|logout
parameter_list|()
throws|throws
name|LoginException
block|{
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|remove
argument_list|(
name|userPrincipal
argument_list|)
expr_stmt|;
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|removeAll
argument_list|(
name|roles
argument_list|)
expr_stmt|;
name|roles
operator|.
name|clear
argument_list|()
expr_stmt|;
name|userPrincipal
operator|=
literal|null
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

