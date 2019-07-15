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
name|rt
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
comment|/**  * This class contains some configuration tags that can be used to configure various security properties. These  * tags are shared between the SOAP stack (WS-SecurityPolicy configuration), as well as the REST stack (JAX-RS  * XML Security).  *  * The configuration tags largely relate to properties for signing, encryption as well as SAML tokens. Most of  * the signing/encryption tags refer to Apache WSS4J "Crypto" objects, which are used by both stacks to control  * how certificates/keys are retrieved, etc.  *  * More specific configuration tags for WS-SecurityPolicy are configured in the SecurityConstants  * class in the cxf-rt-ws-security module, which extends this class.  */
end_comment

begin_class
specifier|public
class|class
name|SecurityConstants
block|{
comment|//
comment|// User properties
comment|//
comment|/**      * The user's name. It is used as follows:      * a) As the name in the UsernameToken for WS-Security.      * b) As the alias name in the keystore to get the user's cert and private key for signature      *    if {@link SIGNATURE_USERNAME} is not set.      * c) As the alias name in the keystore to get the user's public key for encryption if      *    {@link ENCRYPT_USERNAME} is not set.      */
specifier|public
specifier|static
specifier|final
name|String
name|USERNAME
init|=
literal|"security.username"
decl_stmt|;
comment|/**      * The user's password when a {@link CALLBACK_HANDLER} is not defined. This is only used for the password      * in a WS-Security UsernameToken.      */
specifier|public
specifier|static
specifier|final
name|String
name|PASSWORD
init|=
literal|"security.password"
decl_stmt|;
comment|/**      * The user's name for signature. It is used as the alias name in the keystore to get the user's cert      * and private key for signature. If this is not defined, then {@link USERNAME} is used instead. If      * that is also not specified, it uses the the default alias set in the properties file referenced by      * {@link SIGNATURE_PROPERTIES}. If that's also not set, and the keystore only contains a single key,      * that key will be used.      */
specifier|public
specifier|static
specifier|final
name|String
name|SIGNATURE_USERNAME
init|=
literal|"security.signature.username"
decl_stmt|;
comment|/**      * The user's password for signature when a {@link CALLBACK_HANDLER} is not defined.      */
specifier|public
specifier|static
specifier|final
name|String
name|SIGNATURE_PASSWORD
init|=
literal|"security.signature.password"
decl_stmt|;
comment|/**      * The user's name for encryption. It is used as the alias name in the keystore to get the user's public      * key for encryption. If this is not defined, then {@link USERNAME} is used instead. If      * that is also not specified, it uses the the default alias set in the properties file referenced by      * {@link ENCRYPT_PROPERTIES}. If that's also not set, and the keystore only contains a single key,      * that key will be used.      *      * For the WS-Security web service provider, the "useReqSigCert" keyword can be used to accept (encrypt to)      * any client whose public key is in the service's truststore (defined in {@link ENCRYPT_PROPERTIES}).      */
specifier|public
specifier|static
specifier|final
name|String
name|ENCRYPT_USERNAME
init|=
literal|"security.encryption.username"
decl_stmt|;
comment|//
comment|// Callback class and Crypto properties
comment|//
comment|/**      * The CallbackHandler implementation class used to obtain passwords, for both outbound and inbound      * requests. The value of this tag must be either:      * a) The class name of a {@link javax.security.auth.callback.CallbackHandler} instance, which must      * be accessible via the classpath.      * b) A {@link javax.security.auth.callback.CallbackHandler} instance.      */
specifier|public
specifier|static
specifier|final
name|String
name|CALLBACK_HANDLER
init|=
literal|"security.callback-handler"
decl_stmt|;
comment|/**      * The SAML CallbackHandler implementation class used to construct SAML Assertions. The value of this      * tag must be either:      * a) The class name of a {@link javax.security.auth.callback.CallbackHandler} instance, which must      * be accessible via the classpath.      * b) A {@link javax.security.auth.callback.CallbackHandler} instance.      */
specifier|public
specifier|static
specifier|final
name|String
name|SAML_CALLBACK_HANDLER
init|=
literal|"security.saml-callback-handler"
decl_stmt|;
comment|/**      * The Crypto property configuration to use for signature, if {@link SIGNATURE_CRYPTO} is not set instead.      * The value of this tag must be either:      * a) A Java Properties object that contains the Crypto configuration.      * b) The path of the Crypto property file that contains the Crypto configuration.      * c) A URL that points to the Crypto property file that contains the Crypto configuration.      */
specifier|public
specifier|static
specifier|final
name|String
name|SIGNATURE_PROPERTIES
init|=
literal|"security.signature.properties"
decl_stmt|;
comment|/**      * The Crypto property configuration to use for encryption, if {@link ENCRYPT_CRYPTO} is not set instead.      * The value of this tag must be either:      * a) A Java Properties object that contains the Crypto configuration.      * b) The path of the Crypto property file that contains the Crypto configuration.      * c) A URL that points to the Crypto property file that contains the Crypto configuration.      */
specifier|public
specifier|static
specifier|final
name|String
name|ENCRYPT_PROPERTIES
init|=
literal|"security.encryption.properties"
decl_stmt|;
comment|/**      * A Crypto object to be used for signature. If this is not defined then the      * {@link SIGNATURE_PROPERTIES} is used instead.      */
specifier|public
specifier|static
specifier|final
name|String
name|SIGNATURE_CRYPTO
init|=
literal|"security.signature.crypto"
decl_stmt|;
comment|/**      * A Crypto object to be used for encryption. If this is not defined then the      * {@link ENCRYPT_PROPERTIES} is used instead.      */
specifier|public
specifier|static
specifier|final
name|String
name|ENCRYPT_CRYPTO
init|=
literal|"security.encryption.crypto"
decl_stmt|;
comment|/**      * A message property for prepared X509 certificate to be used for encryption.      * If this is not defined, then the certificate will be either loaded from the      * keystore {@link ENCRYPT_PROPERTIES} or extracted from request (when WS-Security is used and      * if {@link ENCRYPT_USERNAME} has value "useReqSigCert").      */
specifier|public
specifier|static
specifier|final
name|String
name|ENCRYPT_CERT
init|=
literal|"security.encryption.certificate"
decl_stmt|;
comment|//
comment|// Boolean Security configuration tags, e.g. the value should be "true" or "false".
comment|//
comment|/**      * Whether to enable Certificate Revocation List (CRL) checking or not when verifying trust      * in a certificate. The default value is "false".      */
specifier|public
specifier|static
specifier|final
name|String
name|ENABLE_REVOCATION
init|=
literal|"security.enableRevocation"
decl_stmt|;
comment|/**      * Whether to allow unsigned saml assertions as SecurityContext Principals. The default is false.      * Note that "unsigned" refers to an internal signature. Even if the token is signed by an      * external signature (as per the "sender-vouches" requirement), this boolean must still be      * configured if you want to use the token to set up the security context.      */
specifier|public
specifier|static
specifier|final
name|String
name|ENABLE_UNSIGNED_SAML_ASSERTION_PRINCIPAL
init|=
literal|"security.enable.unsigned-saml-assertion.principal"
decl_stmt|;
comment|/**      * Whether to allow UsernameTokens with no password to be used as SecurityContext Principals.      * The default is false.      */
specifier|public
specifier|static
specifier|final
name|String
name|ENABLE_UT_NOPASSWORD_PRINCIPAL
init|=
literal|"security.enable.ut-no-password.principal"
decl_stmt|;
comment|/**      * Whether to validate the SubjectConfirmation requirements of a received SAML Token      * (sender-vouches or holder-of-key). The default is true.      */
specifier|public
specifier|static
specifier|final
name|String
name|VALIDATE_SAML_SUBJECT_CONFIRMATION
init|=
literal|"security.validate.saml.subject.conf"
decl_stmt|;
comment|/**      * Set this to "false" if security context must not be created from JAAS Subject.      *      * The default value is "true".      */
specifier|public
specifier|static
specifier|final
name|String
name|SC_FROM_JAAS_SUBJECT
init|=
literal|"security.sc.jaas-subject"
decl_stmt|;
comment|/**      * Enable SAML AudienceRestriction validation. If this is set to "true", then IF the      * SAML Token contains Audience Restriction URIs, one of them must match one of the values of the      * AUDIENCE_RESTRICTIONS property. The default is "true" for SOAP services, "false" for REST services.      */
specifier|public
specifier|static
specifier|final
name|String
name|AUDIENCE_RESTRICTION_VALIDATION
init|=
literal|"security.validate.audience-restriction"
decl_stmt|;
comment|//
comment|// Non-boolean WS-Security Configuration parameters
comment|//
comment|/**      * The attribute URI of the SAML AttributeStatement where the role information is stored.      * The default is "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role".      */
specifier|public
specifier|static
specifier|final
name|String
name|SAML_ROLE_ATTRIBUTENAME
init|=
literal|"security.saml-role-attributename"
decl_stmt|;
comment|/**      * A String of regular expressions (separated by the value specified for CERT_CONSTRAINTS_SEPARATOR)      * which will be applied to the subject DN of the certificate used for signature validation, after trust      * verification of the certificate chain associated with the certificate.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT_CERT_CONSTRAINTS
init|=
literal|"security.subject.cert.constraints"
decl_stmt|;
comment|/**      * The separator that is used to parse certificate constraints configured in the SUBJECT_CERT_CONSTRAINTS      * tag. By default it is a comma - ",".      */
specifier|public
specifier|static
specifier|final
name|String
name|CERT_CONSTRAINTS_SEPARATOR
init|=
literal|"security.cert.constraints.separator"
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
literal|"security.sts.client"
decl_stmt|;
comment|/**      * The "AppliesTo" address to send to the STS. The default is the endpoint address of the      * service provider.      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_APPLIES_TO
init|=
literal|"security.sts.applies-to"
decl_stmt|;
comment|/**      * Whether to write out an X509Certificate structure in UseKey/KeyInfo, or whether to write      * out a KeyValue structure. The default value is "false".      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_USE_CERT_FOR_KEYINFO
init|=
literal|"security.sts.token.usecert"
decl_stmt|;
comment|/**      * Whether to cancel a token when using SecureConversation after successful invocation. The      * default is "false".      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_DO_CANCEL
init|=
literal|"security.sts.token.do.cancel"
decl_stmt|;
comment|/**      * Whether to fall back to calling "issue" after failing to renew an expired token. Some      * STSs do not support the renew binding, and so we should just issue a new token after expiry.      * The default is true.      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_ISSUE_AFTER_FAILED_RENEW
init|=
literal|"security.issue.after.failed.renew"
decl_stmt|;
comment|/**      * Set this to "false" to not cache a SecurityToken per proxy object in the      * IssuedTokenInterceptorProvider. This should be done if a token is being retrieved      * from an STS in an intermediary. The default value is "true".      */
specifier|public
specifier|static
specifier|final
name|String
name|CACHE_ISSUED_TOKEN_IN_ENDPOINT
init|=
literal|"security.cache.issued.token.in.endpoint"
decl_stmt|;
comment|/**      * Whether to avoid STS client trying send WS-MetadataExchange call using      * STS EPR WSA address when the endpoint contract contains no WS-MetadataExchange info.      * The default value is "false".      */
specifier|public
specifier|static
specifier|final
name|String
name|DISABLE_STS_CLIENT_WSMEX_CALL_USING_EPR_ADDRESS
init|=
literal|"security.sts.disable-wsmex-call-using-epr-address"
decl_stmt|;
comment|/**      * Whether to prefer to use WS-MEX over a STSClient's location/wsdlLocation properties      * when making an STS RequestSecurityToken call. This can be set to true for the scenario      * of making a WS-MEX call to an initial STS, and using the returned token to make another      * call to an STS (which is configured using the STSClient configuration). Default is      * "false".      */
specifier|public
specifier|static
specifier|final
name|String
name|PREFER_WSMEX_OVER_STS_CLIENT_CONFIG
init|=
literal|"security.sts.prefer-wsmex"
decl_stmt|;
comment|/**      * Switch STS client to send Soap 1.2 messages      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_CLIENT_SOAP12_BINDING
init|=
literal|"security.sts.client-soap12-binding"
decl_stmt|;
comment|/**      *      * A Crypto object to be used for the STS. If this is not defined then the      * {@link STS_TOKEN_PROPERTIES} is used instead.      *      * WCF's trust server sometimes will encrypt the token in the response IN ADDITION TO      * the full security on the message. These properties control the way the STS client      * will decrypt the EncryptedData elements in the response.      *      * These are also used by the STSClient to send/process any RSA/DSAKeyValue tokens      * used if the KeyType is "PublicKey"      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_CRYPTO
init|=
literal|"security.sts.token.crypto"
decl_stmt|;
comment|/**      * The Crypto property configuration to use for the STS, if {@link STS_TOKEN_CRYPTO} is not      * set instead.      * The value of this tag must be either:      * a) A Java Properties object that contains the Crypto configuration.      * b) The path of the Crypto property file that contains the Crypto configuration.      * c) A URL that points to the Crypto property file that contains the Crypto configuration.      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_PROPERTIES
init|=
literal|"security.sts.token.properties"
decl_stmt|;
comment|/**      * The alias name in the keystore to get the user's public key to send to the STS for the      * PublicKey KeyType case.      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_USERNAME
init|=
literal|"security.sts.token.username"
decl_stmt|;
comment|/**      * The token to be sent to the STS in an "ActAs" field. It can be either:      * a) A String (which must be an XML statement like "<wst:OnBehalfOf xmlns:wst=...>...</wst:OnBehalfOf>")      * b) A DOM Element      * c) A CallbackHandler object to use to obtain the token      *      * In the case of a CallbackHandler, it must be able to handle a      * org.apache.cxf.ws.security.trust.delegation.DelegationCallback Object, which contains a      * reference to the current Message. The CallbackHandler implementation is required to set      * the token Element to be sent in the request on the Callback.      *      * Some examples that can be reused are:      * org.apache.cxf.ws.security.trust.delegation.ReceivedTokenCallbackHandler      * org.apache.cxf.ws.security.trust.delegation.WSSUsernameCallbackHandler      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_ACT_AS
init|=
literal|"security.sts.token.act-as"
decl_stmt|;
comment|/**      * The token to be sent to the STS in an "OnBehalfOf" field. It can be either:      * a) A String (which must be an XML statement like "<wst:OnBehalfOf xmlns:wst=...>...</wst:OnBehalfOf>")      * b) A DOM Element      * c) A CallbackHandler object to use to obtain the token      *      * In the case of a CallbackHandler, it must be able to handle a      * org.apache.cxf.ws.security.trust.delegation.DelegationCallback Object, which contains a      * reference to the current Message. The CallbackHandler implementation is required to set      * the token Element to be sent in the request on the Callback.      *      * Some examples that can be reused are:      * org.apache.cxf.ws.security.trust.delegation.ReceivedTokenCallbackHandler      * org.apache.cxf.ws.security.trust.delegation.WSSUsernameCallbackHandler      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_ON_BEHALF_OF
init|=
literal|"security.sts.token.on-behalf-of"
decl_stmt|;
comment|/**      * This is the value in seconds within which a token is considered to be expired by the      * client. When a cached token (from a STS) is retrieved by the client, it is considered      * to be expired if it will expire in a time less than the value specified by this tag.      * This prevents token expiry when the message is en route / being processed by the      * service. When the token is found to be expired then it will be renewed via the STS.      *      * The default value is 10 (seconds). Specify 0 to avoid this check.      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_IMMINENT_EXPIRY_VALUE
init|=
literal|"security.sts.token.imminent-expiry-value"
decl_stmt|;
comment|/**      * An implementation of the STSTokenCacher interface, if you want to plug in custom caching behaviour for      * STS clients. The default value is the DefaultSTSTokenCacher.      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_CACHER_IMPL
init|=
literal|"security.sts.token.cacher.impl"
decl_stmt|;
comment|/**      * Check that we are not invoking on the STS using its own IssuedToken policy - in which case we      * will end up with a recursive loop. This check might be a problem in the unlikely scenario that the      * remote endpoint has the same service / port QName as the STS, so this configuration flag allows to      * disable this check for that scenario. The default is "true".      */
specifier|public
specifier|static
specifier|final
name|String
name|STS_CHECK_FOR_RECURSIVE_CALL
init|=
literal|"security.sts.check.for.recursive.call"
decl_stmt|;
comment|/**      * This property contains a comma separated String corresponding to a list of audience restriction URIs.      * The default value for this property contains the request URL and the Service QName. If the      * AUDIENCE_RESTRICTION_VALIDATION property is "true", and if a received SAML Token contains audience      * restriction URIs, then one of them must match one of the values specified in this property.      */
specifier|public
specifier|static
specifier|final
name|String
name|AUDIENCE_RESTRICTIONS
init|=
literal|"security.audience-restrictions"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|COMMON_PROPERTIES
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
argument_list|<>
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
name|ENCRYPT_CERT
block|,
name|ENABLE_REVOCATION
block|,
name|SUBJECT_CERT_CONSTRAINTS
block|,
name|ENABLE_UNSIGNED_SAML_ASSERTION_PRINCIPAL
block|,
name|ENABLE_UT_NOPASSWORD_PRINCIPAL
block|,
name|AUDIENCE_RESTRICTION_VALIDATION
block|,
name|SAML_ROLE_ATTRIBUTENAME
block|,
name|ENABLE_UNSIGNED_SAML_ASSERTION_PRINCIPAL
block|,
name|SC_FROM_JAAS_SUBJECT
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
name|STS_CLIENT
block|,
name|STS_APPLIES_TO
block|,
name|CACHE_ISSUED_TOKEN_IN_ENDPOINT
block|,
name|PREFER_WSMEX_OVER_STS_CLIENT_CONFIG
block|,
name|STS_TOKEN_IMMINENT_EXPIRY_VALUE
block|,
name|STS_TOKEN_CACHER_IMPL
block|,
name|AUDIENCE_RESTRICTIONS
block|,
name|STS_CHECK_FOR_RECURSIVE_CALL
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|COMMON_PROPERTIES
operator|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|SecurityConstants
parameter_list|()
block|{
comment|// complete
block|}
block|}
end_class

end_unit

