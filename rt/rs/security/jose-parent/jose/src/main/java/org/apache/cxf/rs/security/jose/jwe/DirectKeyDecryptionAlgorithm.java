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
name|Key
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|logging
operator|.
name|LogUtils
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
name|KeyAlgorithm
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
name|rt
operator|.
name|security
operator|.
name|crypto
operator|.
name|CryptoUtils
import|;
end_import

begin_class
specifier|public
class|class
name|DirectKeyDecryptionAlgorithm
implements|implements
name|KeyDecryptionProvider
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|DirectKeyDecryptionAlgorithm
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|byte
index|[]
name|contentDecryptionKey
decl_stmt|;
specifier|public
name|DirectKeyDecryptionAlgorithm
parameter_list|(
name|Key
name|contentDecryptionKey
parameter_list|)
block|{
name|this
argument_list|(
name|contentDecryptionKey
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DirectKeyDecryptionAlgorithm
parameter_list|(
name|String
name|encodedContentDecryptionKey
parameter_list|)
block|{
name|this
argument_list|(
name|CryptoUtils
operator|.
name|decodeSequence
argument_list|(
name|encodedContentDecryptionKey
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DirectKeyDecryptionAlgorithm
parameter_list|(
name|byte
index|[]
name|contentDecryptionKey
parameter_list|)
block|{
name|this
operator|.
name|contentDecryptionKey
operator|=
name|contentDecryptionKey
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
name|validateKeyEncryptionKey
argument_list|(
name|jweDecryptionInput
argument_list|)
expr_stmt|;
return|return
name|contentDecryptionKey
return|;
block|}
annotation|@
name|Override
specifier|public
name|KeyAlgorithm
name|getAlgorithm
parameter_list|()
block|{
return|return
name|KeyAlgorithm
operator|.
name|DIRECT
return|;
block|}
specifier|protected
name|void
name|validateKeyEncryptionKey
parameter_list|(
name|JweDecryptionInput
name|jweDecryptionInput
parameter_list|)
block|{
name|byte
index|[]
name|encryptedCEK
init|=
name|jweDecryptionInput
operator|.
name|getEncryptedCEK
argument_list|()
decl_stmt|;
if|if
condition|(
name|encryptedCEK
operator|!=
literal|null
operator|&&
name|encryptedCEK
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Unexpected content encryption key"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JweException
argument_list|(
name|JweException
operator|.
name|Error
operator|.
name|INVALID_KEY_ALGORITHM
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

