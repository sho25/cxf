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
name|Key
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
name|utils
operator|.
name|crypto
operator|.
name|CryptoUtils
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
name|utils
operator|.
name|crypto
operator|.
name|KeyProperties
import|;
end_import

begin_class
specifier|public
class|class
name|WrappedKeyJweDecryptor
extends|extends
name|AbstractJweDecryptor
block|{
specifier|private
name|Key
name|cekDecryptionKey
decl_stmt|;
specifier|private
name|boolean
name|unwrap
decl_stmt|;
specifier|public
name|WrappedKeyJweDecryptor
parameter_list|(
name|Key
name|cekDecryptionKey
parameter_list|)
block|{
name|this
argument_list|(
name|cekDecryptionKey
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WrappedKeyJweDecryptor
parameter_list|(
name|Key
name|cekDecryptionKey
parameter_list|,
name|boolean
name|unwrap
parameter_list|)
block|{
name|this
argument_list|(
name|cekDecryptionKey
argument_list|,
name|unwrap
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WrappedKeyJweDecryptor
parameter_list|(
name|Key
name|cekDecryptionKey
parameter_list|,
name|JweCryptoProperties
name|props
parameter_list|)
block|{
name|this
argument_list|(
name|cekDecryptionKey
argument_list|,
literal|true
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WrappedKeyJweDecryptor
parameter_list|(
name|Key
name|cekDecryptionKey
parameter_list|,
name|boolean
name|unwrap
parameter_list|,
name|JweCryptoProperties
name|props
parameter_list|)
block|{
name|super
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|this
operator|.
name|cekDecryptionKey
operator|=
name|cekDecryptionKey
expr_stmt|;
name|this
operator|.
name|unwrap
operator|=
name|unwrap
expr_stmt|;
block|}
specifier|protected
name|byte
index|[]
name|getContentEncryptionKey
parameter_list|(
name|JweCompactConsumer
name|consumer
parameter_list|)
block|{
name|KeyProperties
name|keyProps
init|=
operator|new
name|KeyProperties
argument_list|(
name|getKeyEncryptionAlgorithm
argument_list|(
name|consumer
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|unwrap
condition|)
block|{
name|keyProps
operator|.
name|setBlockSize
argument_list|(
name|getKeyCipherBlockSize
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|CryptoUtils
operator|.
name|decryptBytes
argument_list|(
name|getEncryptedContentEncryptionKey
argument_list|(
name|consumer
argument_list|)
argument_list|,
name|getCekDecryptionKey
argument_list|()
argument_list|,
name|keyProps
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|CryptoUtils
operator|.
name|unwrapSecretKey
argument_list|(
name|getEncryptedContentEncryptionKey
argument_list|(
name|consumer
argument_list|)
argument_list|,
name|getContentEncryptionAlgorithm
argument_list|(
name|consumer
argument_list|)
argument_list|,
name|getCekDecryptionKey
argument_list|()
argument_list|,
name|keyProps
argument_list|)
operator|.
name|getEncoded
argument_list|()
return|;
block|}
block|}
specifier|protected
name|Key
name|getCekDecryptionKey
parameter_list|()
block|{
return|return
name|cekDecryptionKey
return|;
block|}
specifier|protected
name|int
name|getKeyCipherBlockSize
parameter_list|()
block|{
return|return
operator|-
literal|1
return|;
block|}
specifier|protected
name|String
name|getKeyEncryptionAlgorithm
parameter_list|(
name|JweCompactConsumer
name|consumer
parameter_list|)
block|{
return|return
name|Algorithm
operator|.
name|toJavaName
argument_list|(
name|consumer
operator|.
name|getJweHeaders
argument_list|()
operator|.
name|getKeyEncryptionAlgorithm
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

