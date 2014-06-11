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
name|jwe
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|interfaces
operator|.
name|RSAPublicKey
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|jwt
operator|.
name|Algorithm
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
name|jwt
operator|.
name|JwtHeadersWriter
import|;
end_import

begin_class
specifier|public
class|class
name|RSAJweEncryptor
extends|extends
name|WrappedKeyJweEncryptor
block|{
specifier|public
name|RSAJweEncryptor
parameter_list|(
name|RSAPublicKey
name|publicKey
parameter_list|,
name|String
name|contentEncryptionAlgo
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|JweHeaders
argument_list|(
name|Algorithm
operator|.
name|RSA_OAEP
operator|.
name|getJwtName
argument_list|()
argument_list|,
name|contentEncryptionAlgo
argument_list|)
argument_list|,
name|publicKey
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RSAJweEncryptor
parameter_list|(
name|RSAPublicKey
name|publicKey
parameter_list|,
name|JweHeaders
name|headers
parameter_list|,
name|byte
index|[]
name|cek
parameter_list|,
name|byte
index|[]
name|iv
parameter_list|)
block|{
name|this
argument_list|(
name|publicKey
argument_list|,
name|headers
argument_list|,
name|cek
argument_list|,
name|iv
argument_list|,
name|DEFAULT_AUTH_TAG_LENGTH
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RSAJweEncryptor
parameter_list|(
name|RSAPublicKey
name|publicKey
parameter_list|,
name|SecretKey
name|secretKey
parameter_list|,
name|String
name|secretKeyJwtAlgorithm
parameter_list|,
name|byte
index|[]
name|iv
parameter_list|)
block|{
name|this
argument_list|(
name|publicKey
argument_list|,
operator|new
name|JweHeaders
argument_list|(
name|Algorithm
operator|.
name|RSA_OAEP
operator|.
name|getJwtName
argument_list|()
argument_list|,
name|secretKeyJwtAlgorithm
argument_list|)
argument_list|,
name|secretKey
operator|!=
literal|null
condition|?
name|secretKey
operator|.
name|getEncoded
argument_list|()
else|:
literal|null
argument_list|,
name|iv
argument_list|,
name|DEFAULT_AUTH_TAG_LENGTH
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RSAJweEncryptor
parameter_list|(
name|RSAPublicKey
name|publicKey
parameter_list|,
name|SecretKey
name|secretKey
parameter_list|,
name|byte
index|[]
name|iv
parameter_list|)
block|{
name|this
argument_list|(
name|publicKey
argument_list|,
name|secretKey
argument_list|,
name|Algorithm
operator|.
name|toJwtName
argument_list|(
name|secretKey
operator|.
name|getAlgorithm
argument_list|()
argument_list|,
name|secretKey
operator|.
name|getEncoded
argument_list|()
operator|.
name|length
operator|*
literal|8
argument_list|)
argument_list|,
name|iv
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RSAJweEncryptor
parameter_list|(
name|RSAPublicKey
name|publicKey
parameter_list|,
name|JweHeaders
name|headers
parameter_list|,
name|byte
index|[]
name|cek
parameter_list|,
name|byte
index|[]
name|iv
parameter_list|,
name|int
name|authTagLen
parameter_list|,
name|boolean
name|wrap
parameter_list|)
block|{
name|this
argument_list|(
name|publicKey
argument_list|,
name|headers
argument_list|,
name|cek
argument_list|,
name|iv
argument_list|,
name|authTagLen
argument_list|,
name|wrap
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RSAJweEncryptor
parameter_list|(
name|RSAPublicKey
name|publicKey
parameter_list|,
name|JweHeaders
name|headers
parameter_list|,
name|byte
index|[]
name|cek
parameter_list|,
name|byte
index|[]
name|iv
parameter_list|,
name|JwtHeadersWriter
name|writer
parameter_list|)
block|{
name|this
argument_list|(
name|publicKey
argument_list|,
name|headers
argument_list|,
name|cek
argument_list|,
name|iv
argument_list|,
name|DEFAULT_AUTH_TAG_LENGTH
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RSAJweEncryptor
parameter_list|(
name|RSAPublicKey
name|publicKey
parameter_list|,
name|JweHeaders
name|headers
parameter_list|,
name|byte
index|[]
name|cek
parameter_list|,
name|byte
index|[]
name|iv
parameter_list|,
name|int
name|authTagLen
parameter_list|,
name|boolean
name|wrap
parameter_list|,
name|JwtHeadersWriter
name|writer
parameter_list|)
block|{
name|super
argument_list|(
name|headers
argument_list|,
name|publicKey
argument_list|,
name|cek
argument_list|,
name|iv
argument_list|,
name|authTagLen
argument_list|,
name|wrap
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

