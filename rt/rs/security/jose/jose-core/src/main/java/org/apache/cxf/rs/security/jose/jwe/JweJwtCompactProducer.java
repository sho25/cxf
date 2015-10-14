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
name|jwe
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PublicKey
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|SecretKey
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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|jwk
operator|.
name|JsonWebKey
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
name|jose
operator|.
name|jwt
operator|.
name|JwtClaims
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
name|jose
operator|.
name|jwt
operator|.
name|JwtToken
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
name|jose
operator|.
name|jwt
operator|.
name|JwtUtils
import|;
end_import

begin_class
specifier|public
class|class
name|JweJwtCompactProducer
block|{
specifier|private
name|JweHeaders
name|headers
decl_stmt|;
specifier|private
name|String
name|claimsJson
decl_stmt|;
specifier|public
name|JweJwtCompactProducer
parameter_list|(
name|JwtToken
name|token
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|JweHeaders
argument_list|(
name|token
operator|.
name|getHeaders
argument_list|()
argument_list|)
argument_list|,
name|token
operator|.
name|getClaims
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JweJwtCompactProducer
parameter_list|(
name|JwtClaims
name|claims
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|JweHeaders
argument_list|()
argument_list|,
name|claims
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JweJwtCompactProducer
parameter_list|(
name|JweHeaders
name|joseHeaders
parameter_list|,
name|JwtClaims
name|claims
parameter_list|)
block|{
name|headers
operator|=
operator|new
name|JweHeaders
argument_list|(
name|joseHeaders
argument_list|)
expr_stmt|;
name|claimsJson
operator|=
name|JwtUtils
operator|.
name|claimsToJson
argument_list|(
name|claims
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|encryptWith
parameter_list|(
name|JsonWebKey
name|key
parameter_list|)
block|{
name|JweEncryptionProvider
name|jwe
init|=
name|JweUtils
operator|.
name|createJweEncryptionProvider
argument_list|(
name|key
argument_list|,
name|headers
argument_list|)
decl_stmt|;
return|return
name|encryptWith
argument_list|(
name|jwe
argument_list|)
return|;
block|}
specifier|public
name|String
name|encryptWith
parameter_list|(
name|PublicKey
name|key
parameter_list|)
block|{
name|JweEncryptionProvider
name|jwe
init|=
name|JweUtils
operator|.
name|createJweEncryptionProvider
argument_list|(
name|key
argument_list|,
name|headers
argument_list|)
decl_stmt|;
return|return
name|encryptWith
argument_list|(
name|jwe
argument_list|)
return|;
block|}
specifier|public
name|String
name|encryptWith
parameter_list|(
name|SecretKey
name|key
parameter_list|)
block|{
name|JweEncryptionProvider
name|jwe
init|=
name|JweUtils
operator|.
name|createJweEncryptionProvider
argument_list|(
name|key
argument_list|,
name|headers
argument_list|)
decl_stmt|;
return|return
name|encryptWith
argument_list|(
name|jwe
argument_list|)
return|;
block|}
specifier|public
name|String
name|encryptWith
parameter_list|(
name|JweEncryptionProvider
name|jwe
parameter_list|)
block|{
return|return
name|jwe
operator|.
name|encrypt
argument_list|(
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|claimsJson
argument_list|)
argument_list|,
name|headers
argument_list|)
return|;
block|}
block|}
end_class

end_unit

