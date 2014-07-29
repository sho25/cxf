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
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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

begin_class
specifier|public
class|class
name|WrappedKeyJweEncryption
extends|extends
name|AbstractJweEncryption
block|{
specifier|private
name|AtomicInteger
name|providedCekUsageCount
decl_stmt|;
specifier|public
name|WrappedKeyJweEncryption
parameter_list|(
name|JweHeaders
name|headers
parameter_list|,
name|KeyEncryptionAlgorithm
name|keyEncryptionAlgorithm
parameter_list|)
block|{
name|this
argument_list|(
name|headers
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|keyEncryptionAlgorithm
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WrappedKeyJweEncryption
parameter_list|(
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
name|KeyEncryptionAlgorithm
name|keyEncryptionAlgorithm
parameter_list|)
block|{
name|this
argument_list|(
name|headers
argument_list|,
name|cek
argument_list|,
name|iv
argument_list|,
name|DEFAULT_AUTH_TAG_LENGTH
argument_list|,
name|keyEncryptionAlgorithm
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WrappedKeyJweEncryption
parameter_list|(
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
name|KeyEncryptionAlgorithm
name|keyEncryptionAlgorithm
parameter_list|,
name|JwtHeadersWriter
name|writer
parameter_list|)
block|{
name|super
argument_list|(
name|headers
argument_list|,
name|cek
argument_list|,
name|iv
argument_list|,
name|authTagLen
argument_list|,
name|keyEncryptionAlgorithm
argument_list|,
name|writer
argument_list|)
expr_stmt|;
if|if
condition|(
name|cek
operator|!=
literal|null
condition|)
block|{
name|providedCekUsageCount
operator|=
operator|new
name|AtomicInteger
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|byte
index|[]
name|getContentEncryptionKey
parameter_list|()
block|{
name|byte
index|[]
name|theCek
init|=
name|super
operator|.
name|getContentEncryptionKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|theCek
operator|==
literal|null
condition|)
block|{
name|String
name|algoJava
init|=
name|getContentEncryptionAlgoJava
argument_list|()
decl_stmt|;
name|String
name|algoJwt
init|=
name|getContentEncryptionAlgoJwt
argument_list|()
decl_stmt|;
name|theCek
operator|=
name|CryptoUtils
operator|.
name|getSecretKey
argument_list|(
name|Algorithm
operator|.
name|stripAlgoProperties
argument_list|(
name|algoJava
argument_list|)
argument_list|,
name|Algorithm
operator|.
name|valueOf
argument_list|(
name|algoJwt
argument_list|)
operator|.
name|getKeySizeBits
argument_list|()
argument_list|)
operator|.
name|getEncoded
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|providedCekUsageCount
operator|.
name|addAndGet
argument_list|(
literal|1
argument_list|)
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
return|return
name|theCek
return|;
block|}
block|}
end_class

end_unit

