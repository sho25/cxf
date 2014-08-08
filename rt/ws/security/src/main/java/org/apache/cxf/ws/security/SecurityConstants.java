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
package|;
end_package

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
name|Set
import|;
end_import

begin_comment
comment|/**  * Configuration tags used to configure the WS-SecurityPolicy layer. Some of them are also   * used by the non WS-SecurityPolicy approach in the WSS4J(Out|In)Interceptors.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|SecurityConstants
block|{
comment|//
comment|// User properties
comment|//
comment|/**      * The user's name. It is used differently by each of the WS-Security functions:      * a) It is used as the name in the UsernameToken      * b) It is used as the alias name in the keystore to get the user's cert and private key for signature      *    if {@link SIGNATURE_USERNAME} is not set.      * c) It is used as the alias name in the keystore to get the user's public key for encryption if       *    {@link ENCRYPT_USERNAME} is not set.      */
specifier|public
specifier|static
specifier|final
name|String
name|USERNAME
init|=
literal|"ws-security.username"
decl_stmt|;
comment|/**      * The user's password when a {@link CALLBACK_HANDLER} is not defined. It is currently only used for       * the case of adding a password to a UsernameToken.      */
specifier|public
specifier|static
specifier|final
name|String
name|PASSWORD
init|=
literal|"ws-security.password"
decl_stmt|;
comment|/**      * The user's name for signature. It is used as the alias name in the keystore to get the user's cert       * and private key for signature. If this is not defined, then {@link USERNAME} is used instead. If       * that is also not specified, it uses the the default alias set in the properties file referenced by      * {@link SIGNATURE_PROPERTIES}. If that's also not set, and the keystore only contains a single key,       * that key will be used.       */
specifier|public
specifier|static
specifier|final
name|String
name|SIGNATURE_USERNAME
init|=
literal|"ws-security.signature.username"
decl_stmt|;
comment|/**      * The user's name for encryption. It is used as the alias name in the keystore to get the user's public       * key for encryption. If this is not defined, then {@link USERNAME} is used instead. If       * that is also not specified, it uses the the default alias set in the properties file referenced by      * {@link ENCRYPT_PROPERTIES}. If that's also not set, and the keystore only contains a single key,       * that key will be used.      *       * For the web service provider, the "useReqSigCert" keyword can be used to accept (encrypt to) any       * client whose public key is in the service's truststore (defined in {@link ENCRYPT_PROPERTIES}).      */
specifier|public
specifier|static
specifier|final
name|String
name|ENCRYPT_USERNAME
init|=
literal|"ws-security.encryption.username"
decl_stmt|;
comment|/**      * The actor or role name of the wsse:Security header. If this parameter       * is omitted, the actor name is not set.      */
specifier|public
specifier|static
specifier|final
name|String
name|ACTOR
init|=
literal|"ws-security.actor"
decl_stmt|;
comment|//
comment|// Callback class and Crypto properties
comment|//
comment|/**      * The CallbackHandler implementation class used to obtain passwords, for both outbound and inbound       * requests. The value of this tag must be either:      * a) The class name of a {@link javax.security.auth.callback.CallbackHandler} instance, which must      * be accessible via the classpath.      * b) A {@link javax.security.auth.callback.CallbackHandler} instance.      */
specifier|public
specifier|static
specifier|final
name|String
name|CALLBACK_HANDLER
init|=
literal|"ws-security.callback-handler"
decl_stmt|;
comment|/**      * The SAML CallbackHandler implementation class used to construct SAML Assertions. The value of this       * tag must be either:      * a) The class name of a {@link javax.security.auth.callback.CallbackHandler} instance, which must      * be accessible via the classpath.      * b) A {@link javax.security.auth.callback.CallbackHandler} instance.      */
specifier|public
specifier|static
specifier|final
name|String
name|SAML_CALLBACK_HANDLER
init|=
literal|"ws-security.saml-callback-handler"
decl_stmt|;
comment|/**      * The Crypto property configuration to use for signature, if {@link SIGNATURE_CRYPTO} is not set instead.      * The value of this tag must be either:      * a) A Java Properties object that contains the Crypto configuration.      * b) The path of the Crypto property file that contains the Crypto configuration.      * c) A URL that points to the Crypto property file that contains the Crypto configuration.      */
specifier|public
specifier|static
specifier|final
name|String
name|SIGNATURE_PROPERTIES
init|=
literal|"ws-security.signature.properties"
decl_stmt|;
comment|/**      * The Crypto property configuration to use for encryption, if {@link ENCRYPT_CRYPTO} is not set instead.      * The value of this tag must be either:      * a) A Java Properties object that contains the Crypto configuration.      * b) The path of the Crypto property file that contains the Crypto configuration.      * c) A URL that points to the Crypto property file that contains the Crypto configuration.      */
specifier|public
specifier|static
specifier|final
name|String
name|ENCRYPT_PROPERTIES
init|=
literal|"ws-security.encryption.properties"
decl_stmt|;
comment|/**      * A Crypto object to be used for signature. If this is not defined then the       * {@link SIGNATURE_PROPERTIES} is used instead.      */
specifier|public
specifier|static
specifier|final
name|String
name|SIGNATURE_CRYPTO
init|=
literal|"ws-security.signature.crypto"
decl_stmt|;
comment|/**      * A Crypto object to be used for encryption. If this is not defined then the       * {@link ENCRYPT_PROPERTIES} is used instead.      */
specifier|public
specifier|static
specifier|final
name|String
name|ENCRYPT_CRYPTO
init|=
literal|"ws-security.encryption.crypto"
decl_stmt|;
comment|//
comment|// Boolean WS-Security configuration tags, e.g. the value should be "true" or "false".
comment|//
comment|/**      * Whether to validate the password of a received UsernameToken or not. The default is true.      */
specifier|public
specifier|static
specifier|final
name|String
name|VALIDATE_TOKEN
init|=
literal|"ws-security.validate.token"
decl_stmt|;
comment|/**      * Whether to enable Certificate Revocation List (CRL) checking or not when verifying trust       * in a certificate. The default value is "false".      */
specifier|public
specifier|static
specifier|final
name|String
name|ENABLE_REVOCATION
init|=
literal|"ws-security.enableRevocation"
decl_stmt|;
comment|// WebLogic and WCF always encrypt UsernameTokens whenever possible
comment|//See:  http://e-docs.bea.com/wls/docs103/webserv_intro/interop.html
comment|//Be default, we will encrypt as well for interop reasons.  However, this
comment|//setting can be set to false to turn that off.
comment|/**      * Whether to always encrypt UsernameTokens that are defined as a SupportingToken. The default       * is true. This should not be set to false in a production environment, as it exposes the       * password (or the digest of the password) on the wire.      */
specifier|public
specifier|static
specifier|final
name|String
name|ALWAYS_ENCRYPT_UT
init|=
literal|"ws-security.username-token.always.encrypted"
decl_stmt|;
comment|/**      * Whether to ensure compliance with the Basic Security Profile (BSP) 1.1 or not. The      * default value is "true".      */
specifier|public
specifier|static
specifier|final
name|String
name|IS_BSP_COMPLIANT
init|=
literal|"ws-security.is-bsp-compliant"
decl_stmt|;
comment|/**      * Whether to cache UsernameToken nonces. The default value is "true" for message recipients, and       * "false" for message initiators. Set it to true to cache for both cases. Set this to "false" to      * not cache UsernameToken nonces. Note that caching only applies when either a UsernameToken      * WS-SecurityPolicy is in effect, or else that a UsernameToken action has been configured      * for the non-security-policy case.      */
specifier|public
specifier|static
specifier|final
name|String
name|ENABLE_NONCE_CACHE
init|=
literal|"ws-security.enable.nonce.cache"
decl_stmt|;
comment|/**      * Whether to cache Timestamp Created Strings (these are only cached in conjunction with a message       * Signature).The default value is "true" for message recipients, and "false" for message initiators.      * Set it to true to cache for both cases. Set this to "false" to not cache Timestamp Created Strings.      * Note that caching only applies when either a "IncludeTimestamp" policy is in effect, or      * else that a Timestamp action has been configured for the non-security-policy case.      */
specifier|public
specifier|static
specifier|final
name|String
name|ENABLE_TIMESTAMP_CACHE
init|=
literal|"ws-security.enable.timestamp.cache"
decl_stmt|;
comment|/**      * Whether to cache SAML2 Token Identifiers, if the token contains a "OneTimeUse" Condition.      * The default value is "true" for message recipients, and "false" for message initiators.      * Set it to true to cache for both cases. Set this to "false" to not cache SAML2 Token Identifiers.      * Note that caching only applies when either a "SamlToken" policy is in effect, or      * else that a SAML action has been configured for the non-security-policy case.      */
specifier|public
specifier|static
specifier|final
name|String
name|ENABLE_SAML_ONE_TIME_USE_CACHE
init|=
literal|"ws-security.enable.saml.cache"
decl_stmt|;
comment|/**      * Whether to validate the SubjectConfirmation requirements of a received SAML Token      * (sender-vouches or holder-of-key). The default is true.      */
specifier|public
specifier|static
specifier|final
name|String
name|VALIDATE_SAML_SUBJECT_CONFIRMATION
init|=
literal|"ws-security.validate.saml.subject.conf"
decl_stmt|;
comment|/**      * Whether to enable streaming WS-Security. If set to false (the default), the old DOM      * implementation is used. If set to true, the new streaming (StAX) implementation is used.      */
specifier|public
specifier|static
specifier|final
name|String
name|ENABLE_STREAMING_SECURITY
init|=
literal|"ws-security.enable.streaming"
decl_stmt|;
comment|/**      * Whether to return the security error message to the client, and not one of the default error      * QNames. The default is false.      */
specifier|public
specifier|static
specifier|final
name|String
name|RETURN_SECURITY_ERROR
init|=
literal|"ws-security.return.security.error"
decl_stmt|;
comment|/**      * Whether to use credential delegation or not in the KerberosClient. If this is set to "true",      * then it tries to get a GSSCredential Object from the Message Context using the       * DELEGATED_CREDENTIAL configuration tag below, and then use this to obtain a service ticket.      * The default is "false".      */
specifier|public
specifier|static
specifier|final
name|String
name|KERBEROS_USE_CREDENTIAL_DELEGATION
init|=
literal|"ws-security.kerberos.use.credential.delegation"
decl_stmt|;
comment|//
comment|// Non-boolean WS-Security Configuration parameters
comment|//
comment|/**      * The time in seconds to append to the Creation value of an incoming Timestamp to determine      * whether to accept the Timestamp as valid or not. The default value is 300 seconds (5 minutes).      */
specifier|public
specifier|static
specifier|final
name|String
name|TIMESTAMP_TTL
init|=
literal|"ws-security.timestamp.timeToLive"
decl_stmt|;
comment|/**      * The time in seconds in the future within which the Created time of an incoming       * Timestamp is valid. The default value is "60", to avoid problems where clocks are       * slightly askew. To reject all future-created Timestamps, set this value to "0".       */
specifier|public
specifier|static
specifier|final
name|String
name|TIMESTAMP_FUTURE_TTL
init|=
literal|"ws-security.timestamp.futureTimeToLive"
decl_stmt|;
comment|/**      * The time in seconds to append to the Creation value of an incoming UsernameToken to determine      * whether to accept the UsernameToken as valid or not. The default value is 300 seconds (5 minutes).      */
specifier|public
specifier|static
specifier|final
name|String
name|USERNAMETOKEN_TTL
init|=
literal|"ws-security.usernametoken.timeToLive"
decl_stmt|;
comment|/**      * The time in seconds in the future within which the Created time of an incoming       * UsernameToken is valid. The default value is "60", to avoid problems where clocks are       * slightly askew. To reject all future-created UsernameTokens, set this value to "0".       */
specifier|public
specifier|static
specifier|final
name|String
name|USERNAMETOKEN_FUTURE_TTL
init|=
literal|"ws-security.usernametoken.futureTimeToLive"
decl_stmt|;
comment|/**      * The attribute URI of the SAML AttributeStatement where the role information is stored.      * The default is "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role".      */
specifier|public
specifier|static
specifier|final
name|String
name|SAML_ROLE_ATTRIBUTENAME
init|=
literal|"ws-security.saml-role-attributename"
decl_stmt|;
comment|/**      * A reference to the KerberosClient class used to obtain a service ticket.       */
specifier|public
specifier|static
specifier|final
name|String
name|KERBEROS_CLIENT
init|=
literal|"ws-security.kerberos.client"
decl_stmt|;
comment|/**      * The SpnegoClientAction implementation to use for SPNEGO. This allows the user to plug in      * a different implementation to obtain a service ticket.      */
specifier|public
specifier|static
specifier|final
name|String
name|SPNEGO_CLIENT_ACTION
init|=
literal|"ws-security.spnego.client.action"
decl_stmt|;
comment|/**      * The JAAS Context name to use for Kerberos.      */
specifier|public
specifier|static
specifier|final
name|String
name|KERBEROS_JAAS_CONTEXT_NAME
init|=
literal|"ws-security.kerberos.jaas.context"
decl_stmt|;
comment|/**      * The Kerberos Service Provider Name (spn) to use.      */
specifier|public
specifier|static
specifier|final
name|String
name|KERBEROS_SPN
init|=
literal|"ws-security.kerberos.spn"
decl_stmt|;
comment|/**      * This holds a reference to a ReplayCache instance used to cache UsernameToken nonces. The      * default instance that is used is the EHCacheReplayCache.      */
specifier|public
specifier|static
specifier|final
name|String
name|NONCE_CACHE_INSTANCE
init|=
literal|"ws-security.nonce.cache.instance"
decl_stmt|;
comment|/**      * This holds a reference to a ReplayCache instance used to cache Timestamp Created Strings. The      * default instance that is used is the EHCacheReplayCache.      */
specifier|public
specifier|static
specifier|final
name|String
name|TIMESTAMP_CACHE_INSTANCE
init|=
literal|"ws-security.timestamp.cache.instance"
decl_stmt|;
comment|/**      * This holds a reference to a ReplayCache instance used to cache SAML2 Token Identifiers, when      * the token has a "OneTimeUse" Condition. The default instance that is used is the EHCacheReplayCache.      */
specifier|public
specifier|static
specifier|final
name|String
name|SAML_ONE_TIME_USE_CACHE_INSTANCE
init|=
literal|"ws-security.saml.cache.instance"
decl_stmt|;
comment|/**      * Set this property to point to a configuration file for the underlying caching implementation.      * The default configuration file that is used is cxf-ehcache.xml in this module.      */
specifier|public
specifier|static
specifier|final
name|String
name|CACHE_CONFIG_FILE
init|=
literal|"ws-security.cache.config.file"
decl_stmt|;
comment|/**      * The TokenStore instance to use to cache security tokens. By default this uses the      * EHCacheTokenStore if EhCache is available. Otherwise it uses the MemoryTokenStore.      */
specifier|public
specifier|static
specifier|final
name|String
name|TOKEN_STORE_CACHE_INSTANCE
init|=
literal|"org.apache.cxf.ws.security.tokenstore.TokenStore"
decl_stmt|;
comment|/**      * The Cache Identifier to use with the TokenStore. CXF uses the following key to retrieve a      * token store: "org.apache.cxf.ws.security.tokenstore.TokenStore-<identifier>". This key can be       * used to configure service-specific cache configuration. If the identifier does not match, then it       * falls back to a cache configuration with key "org.apache.cxf.ws.security.tokenstore.TokenStore".      *       * The default "<identifier>" is the QName of the service in question. However to pick up a       * custom cache configuration (for example, if you want to specify a TokenStore per-client proxy),      * it can be configured with this identifier instead.      */
specifier|public
specifier|static
specifier|final
name|String
name|CACHE_IDENTIFIER
init|=
literal|"ws-security.cache.identifier"
decl_stmt|;
comment|/**      * A comma separated String of regular expressions which will be applied to the subject DN of       * the certificate used for signature validation, after trust verification of the certificate       * chain associated with the  certificate.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT_CERT_CONSTRAINTS
init|=
literal|"ws-security.subject.cert.constraints"
decl_stmt|;
comment|/**      * The Subject Role Classifier to use. If one of the WSS4J Validators returns a JAAS Subject      * from Validation, then the WSS4JInInterceptor will attempt to create a SecurityContext      * based on this Subject. If this value is not specified, then it tries to get roles using      * the DefaultSecurityContext in cxf-rt-core. Otherwise it uses this value in combination      * with the SUBJECT_ROLE_CLASSIFIER_TYPE to get the roles from the Subject.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT_ROLE_CLASSIFIER
init|=
literal|"ws-security.role.classifier"
decl_stmt|;
comment|/**      * The Subject Role Classifier Type to use. If one of the WSS4J Validators returns a JAAS Subject      * from Validation, then the WSS4JInInterceptor will attempt to create a SecurityContext      * based on this Subject. Currently accepted values are "prefix" or "classname". Must be      * used in conjunction with the SUBJECT_ROLE_CLASSIFIER. The default value is "prefix".      */
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT_ROLE_CLASSIFIER_TYPE
init|=
literal|"ws-security.role.classifier.type"
decl_stmt|;
comment|/**      * This configuration tag allows the user to override the default Asymmetric Signature       * algorithm (RSA-SHA1) for use in WS-SecurityPolicy, as the WS-SecurityPolicy specification      * does not allow the use of other algorithms at present.      */
specifier|public
specifier|static
specifier|final
name|String
name|ASYMMETRIC_SIGNATURE_ALGORITHM
init|=
literal|"ws-security.asymmetric.signature.algorithm"
decl_stmt|;
comment|/**      * This holds a reference to a PasswordEncryptor instance, which is used to encrypt or       * decrypt passwords in the Merlin Crypto implementation (or any custom Crypto implementations).      *       * By default, WSS4J uses the JasyptPasswordEncryptor, which must be instantiated with a       * master password to use to decrypt keystore passwords in the Merlin Crypto properties file.      * This master password is obtained via the CallbackHandler defined via PW_CALLBACK_CLASS      * or PW_CALLBACK_REF.      *       * The encrypted passwords must be stored in the format "ENC(encoded encrypted password)".      */
specifier|public
specifier|static
specifier|final
name|String
name|PASSWORD_ENCRYPTOR_INSTANCE
init|=
literal|"ws-security.password.encryptor.instance"
decl_stmt|;
comment|//
comment|// Validator implementations for validating received security tokens
comment|//
comment|/**      * The WSS4J Validator instance to use to validate UsernameTokens. The default value is the      * UsernameTokenValidator.      */
specifier|public
specifier|static
specifier|final
name|String
name|USERNAME_TOKEN_VALIDATOR
init|=
literal|"ws-security.ut.validator"
decl_stmt|;
comment|/**      * The WSS4J Validator instance to use to validate SAML 1.1 Tokens. The default value is the      * SamlAssertionValidator.      */
specifier|public
specifier|static
specifier|final
name|String
name|SAML1_TOKEN_VALIDATOR
init|=
literal|"ws-security.saml1.validator"
decl_stmt|;
comment|/**      * The WSS4J Validator instance to use to validate SAML 2.0 Tokens. The default value is the      * SamlAssertionValidator.      */
specifier|public
specifier|static
specifier|final
name|String
name|SAML2_TOKEN_VALIDATOR
init|=
literal|"ws-security.saml2.validator"
decl_stmt|;
comment|/**      * The WSS4J Validator instance to use to validate Timestamps. The default value is the      * TimestampValidator.      */
specifier|public
specifier|static
specifier|final
name|String
name|TIMESTAMP_TOKEN_VALIDATOR
init|=
literal|"ws-security.timestamp.validator"
decl_stmt|;
comment|/**      * The WSS4J Validator instance to use to validate trust in credentials used in      * Signature verification. The default value is the SignatureTrustValidator.      */
specifier|public
specifier|static
specifier|final
name|String
name|SIGNATURE_TOKEN_VALIDATOR
init|=
literal|"ws-security.signature.validator"
decl_stmt|;
comment|/**      * The WSS4J Validator instance to use to validate BinarySecurityTokens. The default value       * is the NoOpValidator.      */
specifier|public
specifier|static
specifier|final
name|String
name|BST_TOKEN_VALIDATOR
init|=
literal|"ws-security.bst.validator"
decl_stmt|;
comment|/**      * The WSS4J Validator instance to use to validate SecurityContextTokens. The default value is       * the NoOpValidator.      */
specifier|public
specifier|static
specifier|final
name|String
name|SCT_TOKEN_VALIDATOR
init|=
literal|"ws-security.sct.validator"
decl_stmt|;
comment|//
comment|// STS Client Configuration tags
comment|//
comment|/**      * A reference to the STSClient class used to communicate with the STS.      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_CLIENT
init|=
literal|"ws-security.sts.client"
decl_stmt|;
comment|/**      * The "AppliesTo" address to send to the STS. The default is the endpoint address of the       * service provider.      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_APPLIES_TO
init|=
literal|"ws-security.sts.applies-to"
decl_stmt|;
comment|/**      * Whether to write out an X509Certificate structure in UseKey/KeyInfo, or whether to write      * out a KeyValue structure. The default value is "false".      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_USE_CERT_FOR_KEYINFO
init|=
literal|"ws-security.sts.token.usecert"
decl_stmt|;
comment|/**      * Whether to cancel a token when using SecureConversation after successful invocation. The      * default is "false".      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_DO_CANCEL
init|=
literal|"ws-security.sts.token.do.cancel"
decl_stmt|;
comment|/**      * Whether to fall back to calling "issue" after failing to renew an expired token. Some      * STSs do not support the renew binding, and so we should just issue a new token after expiry.      * The default is true.      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_ISSUE_AFTER_FAILED_RENEW
init|=
literal|"ws-security.issue.after.failed.renew"
decl_stmt|;
comment|/**      * Set this to "false" to not cache a SecurityToken per proxy object in the       * IssuedTokenInterceptorProvider. This should be done if a token is being retrieved      * from an STS in an intermediary. The default value is "true".      */
specifier|public
specifier|static
specifier|final
name|String
name|CACHE_ISSUED_TOKEN_IN_ENDPOINT
init|=
literal|"ws-security.cache.issued.token.in.endpoint"
decl_stmt|;
comment|/**      * Whether to avoid STS client trying send WS-MetadataExchange call using      * STS EPR WSA address when the endpoint contract contains no WS-MetadataExchange info.      * The default value is "false".      */
specifier|public
specifier|static
specifier|final
name|String
name|DISABLE_STS_CLIENT_WSMEX_CALL_USING_EPR_ADDRESS
init|=
literal|"ws-security.sts.disable-wsmex-call-using-epr-address"
decl_stmt|;
comment|/**      * Whether to prefer to use WS-MEX over a STSClient's location/wsdlLocation properties      * when making an STS RequestSecurityToken call. This can be set to true for the scenario      * of making a WS-MEX call to an initial STS, and using the returned token to make another      * call to an STS (which is configured using the STSClient configuration). Default is       * "false".      */
specifier|public
specifier|static
specifier|final
name|String
name|PREFER_WSMEX_OVER_STS_CLIENT_CONFIG
init|=
literal|"ws-security.sts.prefer-wsmex"
decl_stmt|;
comment|/**      * Switch STS client to send Soap 1.2 messages      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_CLIENT_SOAP12_BINDING
init|=
literal|"ws-security.sts.client-soap12-binding"
decl_stmt|;
comment|/**      *       * A Crypto object to be used for the STS. If this is not defined then the       * {@link STS_TOKEN_PROPERTIES} is used instead.      *       * WCF's trust server sometimes will encrypt the token in the response IN ADDITION TO      * the full security on the message. These properties control the way the STS client      * will decrypt the EncryptedData elements in the response.      *       * These are also used by the STSClient to send/process any RSA/DSAKeyValue tokens       * used if the KeyType is "PublicKey"       */
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_CRYPTO
init|=
literal|"ws-security.sts.token.crypto"
decl_stmt|;
comment|/**      * The Crypto property configuration to use for the STS, if {@link STS_TOKEN_CRYPTO} is not      * set instead.      * The value of this tag must be either:      * a) A Java Properties object that contains the Crypto configuration.      * b) The path of the Crypto property file that contains the Crypto configuration.      * c) A URL that points to the Crypto property file that contains the Crypto configuration.      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_PROPERTIES
init|=
literal|"ws-security.sts.token.properties"
decl_stmt|;
comment|/**      * The alias name in the keystore to get the user's public key to send to the STS for the      * PublicKey KeyType case.      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_USERNAME
init|=
literal|"ws-security.sts.token.username"
decl_stmt|;
comment|/**      * The token to be sent to the STS in an "ActAs" field. It can be either:      * a) A String (which must be an XML statement like "<wst:OnBehalfOf xmlns:wst=...>...</wst:OnBehalfOf>")      * b) A DOM Element      * c) A CallbackHandler object to use to obtain the token      *       * In the case of a CallbackHandler, it must be able to handle a       * org.apache.cxf.ws.security.trust.delegation.DelegationCallback Object, which contains a       * reference to the current Message. The CallbackHandler implementation is required to set       * the token Element to be sent in the request on the Callback.      *       * Some examples that can be reused are:      * org.apache.cxf.ws.security.trust.delegation.ReceivedTokenCallbackHandler      * org.apache.cxf.ws.security.trust.delegation.WSSUsernameCallbackHandler      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_ACT_AS
init|=
literal|"ws-security.sts.token.act-as"
decl_stmt|;
comment|/**      * The token to be sent to the STS in an "OnBehalfOf" field. It can be either:      * a) A String (which must be an XML statement like "<wst:OnBehalfOf xmlns:wst=...>...</wst:OnBehalfOf>")      * b) A DOM Element      * c) A CallbackHandler object to use to obtain the token      *       * In the case of a CallbackHandler, it must be able to handle a       * org.apache.cxf.ws.security.trust.delegation.DelegationCallback Object, which contains a       * reference to the current Message. The CallbackHandler implementation is required to set       * the token Element to be sent in the request on the Callback.      *       * Some examples that can be reused are:      * org.apache.cxf.ws.security.trust.delegation.ReceivedTokenCallbackHandler      * org.apache.cxf.ws.security.trust.delegation.WSSUsernameCallbackHandler      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_ON_BEHALF_OF
init|=
literal|"ws-security.sts.token.on-behalf-of"
decl_stmt|;
comment|/**      * Set this to "false" in order to remove the SOAP mustUnderstand header from security headers generated based on      * a WS-SecurityPolicy.      *      * The default value is "true" which included the SOAP mustUnderstand header.      */
specifier|public
specifier|static
specifier|final
name|String
name|MUST_UNDERSTAND
init|=
literal|"ws-security.must-understand"
decl_stmt|;
comment|/**      * Set this to "false" if security context must not be created from JAAS Subject.      *      * The default value is "true".      */
specifier|public
specifier|static
specifier|final
name|String
name|SC_FROM_JAAS_SUBJECT
init|=
literal|"ws-security.sc.jaas-subject"
decl_stmt|;
comment|/**      * A delegated credential to use for WS-Security. Currently only a Kerberos GSSCredential      * Object is supported. This is used to retrieve a service ticket instead of using the      * client credentials.      */
specifier|public
specifier|static
specifier|final
name|String
name|DELEGATED_CREDENTIAL
init|=
literal|"ws-security.delegated.credential"
decl_stmt|;
comment|//
comment|// Internal tags
comment|//
specifier|public
specifier|static
specifier|final
name|String
name|TOKEN
init|=
literal|"ws-security.token"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOKEN_ID
init|=
literal|"ws-security.token.id"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|ALL_PROPERTIES
decl_stmt|;
static|static
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|s
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
name|USERNAME
block|,
name|PASSWORD
block|,
name|SIGNATURE_USERNAME
block|,
name|ENCRYPT_USERNAME
block|,
name|ACTOR
block|,
name|CALLBACK_HANDLER
block|,
name|SAML_CALLBACK_HANDLER
block|,
name|SIGNATURE_PROPERTIES
block|,
name|SIGNATURE_CRYPTO
block|,
name|ENCRYPT_PROPERTIES
block|,
name|ENCRYPT_CRYPTO
block|,
name|VALIDATE_TOKEN
block|,
name|ENABLE_REVOCATION
block|,
name|ALWAYS_ENCRYPT_UT
block|,
name|IS_BSP_COMPLIANT
block|,
name|ENABLE_NONCE_CACHE
block|,
name|ENABLE_TIMESTAMP_CACHE
block|,
name|TIMESTAMP_TTL
block|,
name|TIMESTAMP_FUTURE_TTL
block|,
name|SAML_ROLE_ATTRIBUTENAME
block|,
name|KERBEROS_CLIENT
block|,
name|SPNEGO_CLIENT_ACTION
block|,
name|KERBEROS_JAAS_CONTEXT_NAME
block|,
name|KERBEROS_SPN
block|,
name|NONCE_CACHE_INSTANCE
block|,
name|TIMESTAMP_CACHE_INSTANCE
block|,
name|CACHE_CONFIG_FILE
block|,
name|TOKEN_STORE_CACHE_INSTANCE
block|,
name|SUBJECT_CERT_CONSTRAINTS
block|,
name|USERNAME_TOKEN_VALIDATOR
block|,
name|SAML1_TOKEN_VALIDATOR
block|,
name|SAML2_TOKEN_VALIDATOR
block|,
name|TIMESTAMP_TOKEN_VALIDATOR
block|,
name|SIGNATURE_TOKEN_VALIDATOR
block|,
name|BST_TOKEN_VALIDATOR
block|,
name|SCT_TOKEN_VALIDATOR
block|,
name|STS_CLIENT
block|,
name|STS_APPLIES_TO
block|,
name|STS_TOKEN_USE_CERT_FOR_KEYINFO
block|,
name|STS_TOKEN_DO_CANCEL
block|,
name|CACHE_ISSUED_TOKEN_IN_ENDPOINT
block|,
name|DISABLE_STS_CLIENT_WSMEX_CALL_USING_EPR_ADDRESS
block|,
name|STS_TOKEN_CRYPTO
block|,
name|STS_TOKEN_PROPERTIES
block|,
name|STS_TOKEN_USERNAME
block|,
name|STS_TOKEN_ACT_AS
block|,
name|STS_TOKEN_ON_BEHALF_OF
block|,
name|TOKEN
block|,
name|TOKEN_ID
block|,
name|SUBJECT_ROLE_CLASSIFIER
block|,
name|SUBJECT_ROLE_CLASSIFIER_TYPE
block|,
name|MUST_UNDERSTAND
block|,
name|ASYMMETRIC_SIGNATURE_ALGORITHM
block|,
name|PASSWORD_ENCRYPTOR_INSTANCE
block|,
name|ENABLE_SAML_ONE_TIME_USE_CACHE
block|,
name|SAML_ONE_TIME_USE_CACHE_INSTANCE
block|,
name|ENABLE_STREAMING_SECURITY
block|,
name|RETURN_SECURITY_ERROR
block|,
name|CACHE_IDENTIFIER
block|,
name|CACHE_ISSUED_TOKEN_IN_ENDPOINT
block|,
name|PREFER_WSMEX_OVER_STS_CLIENT_CONFIG
block|,
name|DELEGATED_CREDENTIAL
block|,
name|KERBEROS_USE_CREDENTIAL_DELEGATION
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|ALL_PROPERTIES
operator|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
specifier|private
name|SecurityConstants
parameter_list|()
block|{
comment|//utility class
block|}
block|}
end_class

end_unit

