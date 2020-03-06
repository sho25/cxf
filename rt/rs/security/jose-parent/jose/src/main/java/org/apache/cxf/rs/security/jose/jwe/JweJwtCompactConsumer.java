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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivateKey
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
name|JweJwtCompactConsumer
block|{
specifier|private
specifier|final
name|JweCompactConsumer
name|jweConsumer
decl_stmt|;
specifier|private
specifier|final
name|JweHeaders
name|headers
decl_stmt|;
specifier|public
name|JweJwtCompactConsumer
parameter_list|(
name|String
name|content
parameter_list|)
block|{
name|jweConsumer
operator|=
operator|new
name|JweCompactConsumer
argument_list|(
name|content
argument_list|)
expr_stmt|;
name|headers
operator|=
name|jweConsumer
operator|.
name|getJweHeaders
argument_list|()
expr_stmt|;
block|}
specifier|public
name|JwtToken
name|decryptWith
parameter_list|(
name|JsonWebKey
name|key
parameter_list|)
block|{
return|return
name|decryptWith
argument_list|(
name|JweUtils
operator|.
name|createJweDecryptionProvider
argument_list|(
name|key
argument_list|,
name|headers
operator|.
name|getContentEncryptionAlgorithm
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|JwtToken
name|decryptWith
parameter_list|(
name|PrivateKey
name|key
parameter_list|)
block|{
return|return
name|decryptWith
argument_list|(
name|JweUtils
operator|.
name|createJweDecryptionProvider
argument_list|(
name|key
argument_list|,
name|headers
operator|.
name|getKeyEncryptionAlgorithm
argument_list|()
argument_list|,
name|headers
operator|.
name|getContentEncryptionAlgorithm
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|JwtToken
name|decryptWith
parameter_list|(
name|SecretKey
name|key
parameter_list|)
block|{
return|return
name|decryptWith
argument_list|(
name|JweUtils
operator|.
name|createJweDecryptionProvider
argument_list|(
name|key
argument_list|,
name|headers
operator|.
name|getKeyEncryptionAlgorithm
argument_list|()
argument_list|,
name|headers
operator|.
name|getContentEncryptionAlgorithm
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|JwtToken
name|decryptWith
parameter_list|(
name|JweDecryptionProvider
name|jwe
parameter_list|)
block|{
name|byte
index|[]
name|bytes
init|=
name|jwe
operator|.
name|decrypt
argument_list|(
name|jweConsumer
operator|.
name|getJweDecryptionInput
argument_list|()
argument_list|)
decl_stmt|;
name|JwtClaims
name|claims
init|=
name|JwtUtils
operator|.
name|jsonToClaims
argument_list|(
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|new
name|JwtToken
argument_list|(
name|headers
argument_list|,
name|claims
argument_list|)
return|;
block|}
specifier|public
name|JweHeaders
name|getHeaders
parameter_list|()
block|{
return|return
name|headers
return|;
block|}
block|}
end_class

end_unit

