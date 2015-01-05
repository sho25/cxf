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

begin_class
specifier|public
class|class
name|DirectKeyJweEncryption
extends|extends
name|AbstractJweEncryption
block|{
specifier|public
name|DirectKeyJweEncryption
parameter_list|(
name|ContentEncryptionAlgorithm
name|ceAlgo
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|JweHeaders
argument_list|(
name|ceAlgo
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
argument_list|,
name|ceAlgo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DirectKeyJweEncryption
parameter_list|(
name|JweHeaders
name|headers
parameter_list|,
name|ContentEncryptionAlgorithm
name|ceAlgo
parameter_list|)
block|{
name|this
argument_list|(
name|headers
argument_list|,
name|ceAlgo
argument_list|,
operator|new
name|DirectKeyEncryptionAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|DirectKeyJweEncryption
parameter_list|(
name|JweHeaders
name|headers
parameter_list|,
name|ContentEncryptionAlgorithm
name|ceAlgo
parameter_list|,
name|DirectKeyEncryptionAlgorithm
name|direct
parameter_list|)
block|{
name|super
argument_list|(
name|headers
argument_list|,
name|ceAlgo
argument_list|,
name|direct
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|byte
index|[]
name|getProvidedContentEncryptionKey
parameter_list|()
block|{
return|return
name|validateCek
argument_list|(
name|super
operator|.
name|getProvidedContentEncryptionKey
argument_list|()
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|byte
index|[]
name|validateCek
parameter_list|(
name|byte
index|[]
name|cek
parameter_list|)
block|{
if|if
condition|(
name|cek
operator|==
literal|null
condition|)
block|{
comment|// to prevent the cek from being auto-generated which
comment|// does not make sense for the direct key case
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"CEK must not be null"
argument_list|)
throw|;
block|}
return|return
name|cek
return|;
block|}
specifier|protected
specifier|static
class|class
name|DirectKeyEncryptionAlgorithm
implements|implements
name|KeyEncryptionAlgorithm
block|{
specifier|public
name|byte
index|[]
name|getEncryptedContentEncryptionKey
parameter_list|(
name|JweHeaders
name|headers
parameter_list|,
name|byte
index|[]
name|theCek
parameter_list|)
block|{
if|if
condition|(
name|headers
operator|.
name|getKeyEncryptionAlgorithm
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
return|return
operator|new
name|byte
index|[
literal|0
index|]
return|;
block|}
specifier|protected
name|void
name|checkKeyEncryptionAlgorithm
parameter_list|(
name|JweHeaders
name|headers
parameter_list|)
block|{
if|if
condition|(
name|headers
operator|.
name|getKeyEncryptionAlgorithm
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getAlgorithm
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

