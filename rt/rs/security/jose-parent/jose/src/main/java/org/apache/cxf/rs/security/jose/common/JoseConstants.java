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
name|jose
operator|.
name|common
package|;
end_package

begin_class
specifier|public
specifier|final
class|class
name|JoseConstants
block|{
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_TYPE
init|=
literal|"typ"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_ALGORITHM
init|=
literal|"alg"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_CONTENT_TYPE
init|=
literal|"cty"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_CRITICAL
init|=
literal|"crit"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_KEY_ID
init|=
literal|"kid"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_X509_URL
init|=
literal|"x5u"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_X509_CHAIN
init|=
literal|"x5c"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_X509_THUMBPRINT
init|=
literal|"x5t"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_X509_THUMBPRINT_SHA256
init|=
literal|"x5t#S256"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_JSON_WEB_KEY
init|=
literal|"jwk"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_JSON_WEB_KEY_SET
init|=
literal|"jku"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JWE_HEADER_KEY_ENC_ALGORITHM
init|=
name|HEADER_ALGORITHM
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JWE_HEADER_CONTENT_ENC_ALGORITHM
init|=
literal|"enc"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JWE_HEADER_ZIP_ALGORITHM
init|=
literal|"zip"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JWE_DEFLATE_ZIP_ALGORITHM
init|=
literal|"DEF"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JWS_HEADER_B64_STATUS_HEADER
init|=
literal|"b64"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_JWT
init|=
literal|"JWT"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_JOSE
init|=
literal|"JOSE"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_JOSE_JSON
init|=
literal|"JOSE+JSON"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MEDIA_TYPE_JOSE
init|=
literal|"application/jose"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MEDIA_TYPE_JOSE_JSON
init|=
literal|"application/jose+json"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JOSE_CONTEXT_PROPERTY
init|=
literal|"org.apache.cxf.jose.context"
decl_stmt|;
comment|//
comment|// JOSE Configuration constants
comment|//
comment|//
comment|// Shared configuration
comment|//
comment|/**      * The keystore type. Suitable values are "jks" or "jwk".      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_STORE_TYPE
init|=
literal|"rs.security.keystore.type"
decl_stmt|;
comment|/**      * The password required to access the keystore.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_STORE_PSWD
init|=
literal|"rs.security.keystore.password"
decl_stmt|;
comment|/**      * The password required to access the private key (in the keystore).      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_PSWD
init|=
literal|"rs.security.key.password"
decl_stmt|;
comment|/**      * The keystore alias corresponding to the key to use. You can append one of the following to this tag to      * get the alias for more specific operations:      *  - jwe.out      *  - jwe.in      *  - jws.out      *  - jws.in      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_STORE_ALIAS
init|=
literal|"rs.security.keystore.alias"
decl_stmt|;
comment|/**      * The keystore aliases corresponding to the keys to use, when using the JSON serialization form. You can       * append one of the following to this tag to get the alias for more specific operations:      *  - jws.out      *  - jws.in      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_STORE_ALIASES
init|=
literal|"rs.security.keystore.aliases"
decl_stmt|;
comment|/**      * The path to the keystore file.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_STORE_FILE
init|=
literal|"rs.security.keystore.file"
decl_stmt|;
comment|/**      * The KeyStore Object.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_STORE
init|=
literal|"rs.security.keystore"
decl_stmt|;
comment|/**      * A reference to a PrivateKeyPasswordProvider instance used to retrieve passwords to access keys.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_PSWD_PROVIDER
init|=
literal|"rs.security.key.password.provider"
decl_stmt|;
comment|/**      * Whether to allow using a JWK received in the header for signature validation. The default      * is "false".      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_ACCEPT_PUBLIC_KEY
init|=
literal|"rs.security.accept.public.key"
decl_stmt|;
comment|/**      * TODO documentation for these      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_STORE_JWKSET
init|=
literal|"rs.security.keystore.jwkset"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_STORE_JWKKEY
init|=
literal|"rs.security.keystore.jwkkey"
decl_stmt|;
comment|//
comment|// JWS specific Configuration
comment|//
comment|/**      * A reference to a PrivateKeyPasswordProvider instance used to retrieve passwords to access keys      * for signature. If this is not specified it falls back to use the RSSEC_KEY_PSWD_PROVIDER.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_KEY_PSWD_PROVIDER
init|=
literal|"rs.security.signature.key.password.provider"
decl_stmt|;
comment|/**      * The signature algorithm to use. The default algorithm if not specified is 'RS256'.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_ALGORITHM
init|=
literal|"rs.security.signature.algorithm"
decl_stmt|;
comment|/**      * The OLD signature algorithm identifier. Use RSSEC_SIGNATURE_ALGORITHM instead.      */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|DEPR_RSSEC_SIGNATURE_ALGORITHM
init|=
literal|"rs.security.jws.content.signature.algorithm"
decl_stmt|;
comment|/**      * The signature properties file for compact signature creation. If not specified then it falls back to       * RSSEC_SIGNATURE_PROPS.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_OUT_PROPS
init|=
literal|"rs.security.signature.out.properties"
decl_stmt|;
comment|/**      * The signature properties file for compact signature verification. If not specified then it falls back to       * RSSEC_SIGNATURE_PROPS.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_IN_PROPS
init|=
literal|"rs.security.signature.in.properties"
decl_stmt|;
comment|/**      * The signature properties file for compact signature creation/verification.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_PROPS
init|=
literal|"rs.security.signature.properties"
decl_stmt|;
comment|/**      * The signature properties file for JSON Serialization signature creation. If not specified then it       * falls back to RSSEC_SIGNATURE_LIST_PROPS.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_OUT_LIST_PROPS
init|=
literal|"rs.security.signature.out.list.properties"
decl_stmt|;
comment|/**      * The signature properties file for JSON Serialization signature verification. If not specified then it       * falls back to RSSEC_SIGNATURE_LIST_PROPS.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_IN_LIST_PROPS
init|=
literal|"rs.security.signature.in.list.properties"
decl_stmt|;
comment|/**      * The signature properties file for JSON Serialization signature creation/verification.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_LIST_PROPS
init|=
literal|"rs.security.signature.list.properties"
decl_stmt|;
comment|/**      * Include the JWK public key for signature in the "jwk" header.       */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_INCLUDE_PUBLIC_KEY
init|=
literal|"rs.security.signature.include.public.key"
decl_stmt|;
comment|/**      * Include the X.509 certificate for signature in the "x5c" header.       */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_INCLUDE_CERT
init|=
literal|"rs.security.signature.include.cert"
decl_stmt|;
comment|/**      * Include the JWK key id for signature in the "kid" header.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_INCLUDE_KEY_ID
init|=
literal|"rs.security.signature.include.key.id"
decl_stmt|;
comment|/**      * Include the X.509 certificate SHA-1 digest for signature in the "x5t" header.       */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_INCLUDE_CERT_SHA1
init|=
literal|"rs.security.signature.include.cert.sha1"
decl_stmt|;
comment|//
comment|// JWE specific Configuration
comment|//
comment|/**      * A reference to a PrivateKeyPasswordProvider instance used to retrieve passwords to access keys      * for decryption. If this is not specified it falls back to use the RSSEC_KEY_PSWD_PROVIDER.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_DECRYPTION_KEY_PSWD_PROVIDER
init|=
literal|"rs.security.decryption.key.password.provider"
decl_stmt|;
comment|/**      * The encryption content algorithm to use. The default algorithm if not specified is 'A128GCM'.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_ENCRYPTION_CONTENT_ALGORITHM
init|=
literal|"rs.security.encryption.content.algorithm"
decl_stmt|;
comment|/**      * The OLD encryption content algorithm to use. Use RSSEC_ENCRYPTION_CONTENT_ALGORITHM instead.      */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|DEPR_RSSEC_ENCRYPTION_CONTENT_ALGORITHM
init|=
literal|"rs.security.jwe.content.encryption.algorithm"
decl_stmt|;
comment|/**      * The encryption key algorithm to use. The default algorithm if not specified is 'RSA-OAEP' if the key is an      * RSA key, and 'A128GCMKW' if it is an octet sequence.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_ENCRYPTION_KEY_ALGORITHM
init|=
literal|"rs.security.encryption.key.algorithm"
decl_stmt|;
comment|/**      * The OLD encryption key algorithm to use. Use RSSEC_ENCRYPTION_KEY_ALGORITHM instead.      */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|DEPR_RSSEC_ENCRYPTION_KEY_ALGORITHM
init|=
literal|"rs.security.jwe.key.encryption.algorithm"
decl_stmt|;
comment|/**      * The encryption zip algorithm to use.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_ENCRYPTION_ZIP_ALGORITHM
init|=
literal|"rs.security.encryption.zip.algorithm"
decl_stmt|;
comment|/**      * The OLD encryption zip algorithm to use. Use RSSEC_ENCRYPTION_ZIP_ALGORITHM instead.      */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|DEPR_RSSEC_ENCRYPTION_ZIP_ALGORITHM
init|=
literal|"rs.security.jwe.zip.algorithm"
decl_stmt|;
comment|/**      * The encryption properties file for encryption creation. If not specified then it falls back to       * RSSEC_ENCRYPTION_PROPS.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_ENCRYPTION_OUT_PROPS
init|=
literal|"rs.security.encryption.out.properties"
decl_stmt|;
comment|/**      * The decryption properties file for decryption. If not specified then it falls back to       * RSSEC_ENCRYPTION_PROPS.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_ENCRYPTION_IN_PROPS
init|=
literal|"rs.security.encryption.in.properties"
decl_stmt|;
comment|/**      * The encryption/decryption properties file      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_ENCRYPTION_PROPS
init|=
literal|"rs.security.encryption.properties"
decl_stmt|;
comment|/**      * Include the JWK public key for encryption in the "jwk" header.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_ENCRYPTION_INCLUDE_PUBLIC_KEY
init|=
literal|"rs.security.encryption.include.public.key"
decl_stmt|;
comment|/**      * Include the X.509 certificate for encryption the "x5c" header.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_ENCRYPTION_INCLUDE_CERT
init|=
literal|"rs.security.encryption.include.cert"
decl_stmt|;
comment|/**      * Include the JWK key id for encryption in the "kid" header.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_ENCRYPTION_INCLUDE_KEY_ID
init|=
literal|"rs.security.encryption.include.key.id"
decl_stmt|;
comment|/**      * Include the X.509 certificate SHA-1 digest for encryption in the "x5t" header.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_ENCRYPTION_INCLUDE_CERT_SHA1
init|=
literal|"rs.security.encryption.include.cert.sha1"
decl_stmt|;
comment|//
comment|// JWT specific configuration
comment|//
comment|/**      * Whether to allow unsigned JWT tokens as SecurityContext Principals. The default is false.      */
specifier|public
specifier|static
specifier|final
name|String
name|ENABLE_UNSIGNED_JWT_PRINCIPAL
init|=
literal|"rs.security.enable.unsigned-jwt.principal"
decl_stmt|;
comment|/**      * Whether to trace JOSE headers.      */
specifier|public
specifier|static
specifier|final
name|String
name|JOSE_DEBUG
init|=
literal|"jose.debug"
decl_stmt|;
specifier|private
name|JoseConstants
parameter_list|()
block|{              }
block|}
end_class

end_unit

