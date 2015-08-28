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
extends|extends
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
block|{
comment|//
comment|// User properties
comment|//
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
comment|/**      * Whether to enable streaming WS-Security. If set to false (the default), the old DOM      * implementation is used. If set to true, the new streaming (StAX) implementation is used.      */
specifier|public
specifier|static
specifier|final
name|String
name|ENABLE_STREAMING_SECURITY
init|=
literal|"ws-security.enable.streaming"
decl_stmt|;
comment|/**      * Whether to return the security error message to the client, and not the default error message.      * The "real" security errors should not be returned to the client in a deployment scenario,      * as they may leak information about the deployment, or otherwise provide a "oracle" for attacks.      * The default is false.      */
specifier|public
specifier|static
specifier|final
name|String
name|RETURN_SECURITY_ERROR
init|=
literal|"ws-security.return.security.error"
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
comment|/**      * Whether to cache SAML2 Token Identifiers, if the token contains a "OneTimeUse" Condition.      * The default value is "true" for message recipients, and "false" for message initiators.      * Set it to true to cache for both cases. Set this to "false" to not cache SAML2 Token Identifiers.      * Note that caching only applies when either a "SamlToken" policy is in effect, or      * else that a SAML action has been configured for the non-security-policy case.      */
specifier|public
specifier|static
specifier|final
name|String
name|ENABLE_SAML_ONE_TIME_USE_CACHE
init|=
literal|"ws-security.enable.saml.cache"
decl_stmt|;
comment|/**      * Whether to store bytes (CipherData or BinarySecurityToken) in an attachment. The default is       * true if MTOM is enabled. Set it to false to BASE-64 encode the bytes and "inlined" them in       * the message instead. Setting this to true is more efficient, as it means that the BASE-64       * encoding step can be skipped. This only applies to the DOM WS-Security stack.      */
specifier|public
specifier|static
specifier|final
name|String
name|STORE_BYTES_IN_ATTACHMENT
init|=
literal|"ws-security.store.bytes.in.attachment"
decl_stmt|;
comment|/**      * This configuration flag allows the user to decide whether the default Attachment-Complete       * transform or the Attachment-Content-Only transform should be used when an Attachment is encrypted       * via a WS-SecurityPolicy expression. The default is "false", meaning that the "complete"       * transformation is used.      */
specifier|public
specifier|static
specifier|final
name|String
name|USE_ATTACHMENT_ENCRYPTION_CONTENT_ONLY_TRANSFORM
init|=
literal|"ws-security.swa.encryption.attachment.transform.content"
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
comment|/**      * The SpnegoClientAction implementation to use for SPNEGO. This allows the user to plug in      * a different implementation to obtain a service ticket.      */
specifier|public
specifier|static
specifier|final
name|String
name|SPNEGO_CLIENT_ACTION
init|=
literal|"ws-security.spnego.client.action"
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
comment|/**      * This refers to a Map of QName, SecurityPolicyValidator, which retrieves a SecurityPolicyValidator      * implementation to validate a particular security policy, based on the QName of the policy. Any      * SecurityPolicyValidator implementation defined in this map will override the default value      * used internally for the corresponding QName.      */
specifier|public
specifier|static
specifier|final
name|String
name|POLICY_VALIDATOR_MAP
init|=
literal|"ws-security.policy.validator.map"
decl_stmt|;
comment|//
comment|// Kerberos Configuration tags
comment|//
comment|/**      * Whether to request credential delegation or not in the KerberosClient. If this is set to "true",      * then it tries to get a kerberos service ticket that can be used for delegation. The default      * is "false".      */
specifier|public
specifier|static
specifier|final
name|String
name|KERBEROS_REQUEST_CREDENTIAL_DELEGATION
init|=
literal|"ws-security.kerberos.request.credential.delegation"
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
comment|/**      * Whether the Kerberos username is in servicename form or not. The default is "false".      */
specifier|public
specifier|static
specifier|final
name|String
name|KERBEROS_IS_USERNAME_IN_SERVICENAME_FORM
init|=
literal|"ws-security.kerberos.is.username.in.servicename.form"
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
comment|/**      * A reference to the KerberosClient class used to obtain a service ticket.       */
specifier|public
specifier|static
specifier|final
name|String
name|KERBEROS_CLIENT
init|=
literal|"ws-security.kerberos.client"
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
name|String
name|TOKEN_ELEMENT
init|=
literal|"ws-security.token.element"
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
name|ACTOR
block|,
name|VALIDATE_TOKEN
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
name|DELEGATED_CREDENTIAL
block|,
name|KERBEROS_USE_CREDENTIAL_DELEGATION
block|,
name|KERBEROS_IS_USERNAME_IN_SERVICENAME_FORM
block|,
name|KERBEROS_REQUEST_CREDENTIAL_DELEGATION
block|,
name|POLICY_VALIDATOR_MAP
block|,
name|STORE_BYTES_IN_ATTACHMENT
block|,
name|USE_ATTACHMENT_ENCRYPTION_CONTENT_ONLY_TRANSFORM
block|}
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|commonProperty
range|:
name|COMMON_PROPERTIES
control|)
block|{
name|s
operator|.
name|add
argument_list|(
name|commonProperty
argument_list|)
expr_stmt|;
name|s
operator|.
name|add
argument_list|(
literal|"ws-"
operator|+
name|commonProperty
argument_list|)
expr_stmt|;
block|}
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

