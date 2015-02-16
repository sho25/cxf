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
name|interfaces
operator|.
name|ECPrivateKey
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
name|JoseUtils
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
name|jwa
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
name|jwk
operator|.
name|JwkUtils
import|;
end_import

begin_class
specifier|public
class|class
name|EcdhDirectKeyJweDecryption
extends|extends
name|JweDecryption
block|{
specifier|public
name|EcdhDirectKeyJweDecryption
parameter_list|(
name|ECPrivateKey
name|privateKey
parameter_list|,
name|String
name|supportedCtAlgo
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|EcdhDirectKeyDecryptionAlgorithm
argument_list|(
name|privateKey
argument_list|)
argument_list|,
operator|new
name|AesGcmContentDecryptionAlgorithm
argument_list|(
name|supportedCtAlgo
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
name|byte
index|[]
name|getDecryptedContentEncryptionKeyFromHeaders
parameter_list|(
name|JweHeaders
name|headers
parameter_list|,
name|ECPrivateKey
name|privateKey
parameter_list|)
block|{
name|Algorithm
name|jwtAlgo
init|=
name|Algorithm
operator|.
name|valueOf
argument_list|(
name|headers
operator|.
name|getContentEncryptionAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
name|JsonWebKey
name|publicJwk
init|=
name|headers
operator|.
name|getJsonWebKey
argument_list|(
literal|"epv"
argument_list|)
decl_stmt|;
name|String
name|apuHeader
init|=
operator|(
name|String
operator|)
name|headers
operator|.
name|getHeader
argument_list|(
literal|"apu"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|apuBytes
init|=
name|apuHeader
operator|==
literal|null
condition|?
literal|null
else|:
name|JoseUtils
operator|.
name|decode
argument_list|(
name|apuHeader
argument_list|)
decl_stmt|;
name|String
name|apvHeader
init|=
operator|(
name|String
operator|)
name|headers
operator|.
name|getHeader
argument_list|(
literal|"apv"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|apvBytes
init|=
name|apvHeader
operator|==
literal|null
condition|?
literal|null
else|:
name|JoseUtils
operator|.
name|decode
argument_list|(
name|apvHeader
argument_list|)
decl_stmt|;
return|return
name|JweUtils
operator|.
name|getECDHKey
argument_list|(
name|privateKey
argument_list|,
name|JwkUtils
operator|.
name|toECPublicKey
argument_list|(
name|publicJwk
argument_list|)
argument_list|,
name|apuBytes
argument_list|,
name|apvBytes
argument_list|,
name|jwtAlgo
operator|.
name|getJwtName
argument_list|()
argument_list|,
name|jwtAlgo
operator|.
name|getKeySizeBits
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
specifier|static
class|class
name|EcdhDirectKeyDecryptionAlgorithm
extends|extends
name|DirectKeyDecryptionAlgorithm
block|{
specifier|private
name|ECPrivateKey
name|privateKey
decl_stmt|;
specifier|public
name|EcdhDirectKeyDecryptionAlgorithm
parameter_list|(
name|ECPrivateKey
name|privateKey
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|byte
index|[]
operator|)
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|privateKey
operator|=
name|privateKey
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|getDecryptedContentEncryptionKey
parameter_list|(
name|JweDecryptionInput
name|jweDecryptionInput
parameter_list|)
block|{
name|super
operator|.
name|validateKeyEncryptionKey
argument_list|(
name|jweDecryptionInput
argument_list|)
expr_stmt|;
return|return
name|getDecryptedContentEncryptionKeyFromHeaders
argument_list|(
name|jweDecryptionInput
operator|.
name|getJweHeaders
argument_list|()
argument_list|,
name|privateKey
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

