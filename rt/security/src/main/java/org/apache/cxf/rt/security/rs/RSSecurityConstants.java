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
operator|.
name|rs
package|;
end_package

begin_comment
comment|/**  * Some common security constants that can be used RS-Security (for now they are used in the  * JOSE + HTTP Signature modules).  */
end_comment

begin_class
specifier|public
class|class
name|RSSecurityConstants
block|{
comment|/**      * The keystore type. It defaults to "JKS" for HTTP Signature, and "JWK" for JOSE.      */
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
comment|/**      * The keystore alias corresponding to the key to use.  You can append one of the following to this tag to      * get the alias for more specific operations for JOSE:      *  - jwe.out      *  - jwe.in      *  - jws.out      *  - jws.in      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_STORE_ALIAS
init|=
literal|"rs.security.keystore.alias"
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
comment|/**      * A reference to a PrivateKeyPasswordProvider instance used to retrieve passwords to access keys.      * If this is not specified for JOSE, it falls back to use the RSSEC_KEY_PSWD_PROVIDER in JoseConstants.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_PSWD_PROVIDER
init|=
literal|"rs.security.key.password.provider"
decl_stmt|;
comment|/**      * The signature algorithm to use. The default algorithm if not specified is "rsa-sha256" for HTTP      * Signature, and "RS256" for JOSE.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_ALGORITHM
init|=
literal|"rs.security.signature.algorithm"
decl_stmt|;
comment|/**      * The signature properties file for signature creation. If not specified then it falls back to      * RSSEC_SIGNATURE_PROPS.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_OUT_PROPS
init|=
literal|"rs.security.signature.out.properties"
decl_stmt|;
comment|/**      * The signature properties file for signature verification. If not specified then it falls back to      * RSSEC_SIGNATURE_PROPS.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_IN_PROPS
init|=
literal|"rs.security.signature.in.properties"
decl_stmt|;
comment|/**      * The signature properties file for signature creation/verification.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_PROPS
init|=
literal|"rs.security.signature.properties"
decl_stmt|;
specifier|protected
name|RSSecurityConstants
parameter_list|()
block|{
comment|// complete
block|}
block|}
end_class

end_unit

