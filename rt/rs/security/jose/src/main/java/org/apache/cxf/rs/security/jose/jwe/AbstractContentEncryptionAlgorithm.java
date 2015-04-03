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
name|common
operator|.
name|util
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
name|jose
operator|.
name|jwa
operator|.
name|ContentAlgorithm
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractContentEncryptionAlgorithm
extends|extends
name|AbstractContentEncryptionCipherProperties
implements|implements
name|ContentEncryptionProvider
block|{
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_IV_SIZE
init|=
literal|128
decl_stmt|;
specifier|private
name|byte
index|[]
name|cek
decl_stmt|;
specifier|private
name|byte
index|[]
name|iv
decl_stmt|;
specifier|private
name|AtomicInteger
name|providedIvUsageCount
decl_stmt|;
specifier|protected
name|AbstractContentEncryptionAlgorithm
parameter_list|(
name|byte
index|[]
name|cek
parameter_list|,
name|byte
index|[]
name|iv
parameter_list|,
name|ContentAlgorithm
name|algo
parameter_list|)
block|{
name|super
argument_list|(
name|algo
argument_list|)
expr_stmt|;
name|this
operator|.
name|cek
operator|=
name|cek
expr_stmt|;
name|this
operator|.
name|iv
operator|=
name|iv
expr_stmt|;
if|if
condition|(
name|iv
operator|!=
literal|null
operator|&&
name|iv
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|providedIvUsageCount
operator|=
operator|new
name|AtomicInteger
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|byte
index|[]
name|getContentEncryptionKey
parameter_list|(
name|JweHeaders
name|headers
parameter_list|)
block|{
return|return
name|cek
return|;
block|}
specifier|public
name|byte
index|[]
name|getInitVector
parameter_list|()
block|{
if|if
condition|(
name|iv
operator|==
literal|null
condition|)
block|{
return|return
name|CryptoUtils
operator|.
name|generateSecureRandomBytes
argument_list|(
name|getIvSize
argument_list|()
operator|/
literal|8
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|iv
operator|.
name|length
operator|>
literal|0
operator|&&
name|providedIvUsageCount
operator|.
name|addAndGet
argument_list|(
literal|1
argument_list|)
operator|>
literal|1
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Custom IV is recommeded to be used once"
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
name|CUSTOM_IV_REUSED
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|iv
return|;
block|}
block|}
specifier|protected
name|int
name|getIvSize
parameter_list|()
block|{
return|return
name|DEFAULT_IV_SIZE
return|;
block|}
block|}
end_class

end_unit

