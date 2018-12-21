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
name|javax
operator|.
name|crypto
operator|.
name|Cipher
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
name|KeyProperties
import|;
end_import

begin_class
specifier|public
class|class
name|JweEncryptionOutput
block|{
specifier|private
name|Cipher
name|cipher
decl_stmt|;
specifier|private
name|JweHeaders
name|headers
decl_stmt|;
specifier|private
name|byte
index|[]
name|encryptedContentEncryptionKey
decl_stmt|;
specifier|private
name|byte
index|[]
name|iv
decl_stmt|;
specifier|private
name|AuthenticationTagProducer
name|authTagProducer
decl_stmt|;
specifier|private
name|byte
index|[]
name|encryptedContent
decl_stmt|;
specifier|private
name|byte
index|[]
name|authTag
decl_stmt|;
specifier|private
name|KeyProperties
name|keyProps
decl_stmt|;
comment|//CHECKSTYLE:OFF
specifier|public
name|JweEncryptionOutput
parameter_list|(
name|Cipher
name|cipher
parameter_list|,
name|JweHeaders
name|headers
parameter_list|,
name|byte
index|[]
name|encryptedContentEncryptionKey
parameter_list|,
name|byte
index|[]
name|iv
parameter_list|,
name|AuthenticationTagProducer
name|authTagProducer
parameter_list|,
name|KeyProperties
name|keyProps
parameter_list|,
name|byte
index|[]
name|encryptedContent
parameter_list|,
name|byte
index|[]
name|authTag
parameter_list|)
block|{
comment|//CHECKSTYLE:ON
name|this
operator|.
name|cipher
operator|=
name|cipher
expr_stmt|;
name|this
operator|.
name|headers
operator|=
name|headers
expr_stmt|;
name|this
operator|.
name|encryptedContentEncryptionKey
operator|=
name|encryptedContentEncryptionKey
expr_stmt|;
name|this
operator|.
name|iv
operator|=
name|iv
expr_stmt|;
name|this
operator|.
name|authTagProducer
operator|=
name|authTagProducer
expr_stmt|;
name|this
operator|.
name|keyProps
operator|=
name|keyProps
expr_stmt|;
name|this
operator|.
name|encryptedContent
operator|=
name|encryptedContent
expr_stmt|;
name|this
operator|.
name|authTag
operator|=
name|authTag
expr_stmt|;
block|}
specifier|public
name|Cipher
name|getCipher
parameter_list|()
block|{
return|return
name|cipher
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
specifier|public
name|byte
index|[]
name|getEncryptedContentEncryptionKey
parameter_list|()
block|{
return|return
name|encryptedContentEncryptionKey
return|;
block|}
specifier|public
name|byte
index|[]
name|getIv
parameter_list|()
block|{
return|return
name|iv
return|;
block|}
specifier|public
name|boolean
name|isCompressionSupported
parameter_list|()
block|{
return|return
name|keyProps
operator|.
name|isCompressionSupported
argument_list|()
return|;
block|}
specifier|public
name|AuthenticationTagProducer
name|getAuthTagProducer
parameter_list|()
block|{
return|return
name|authTagProducer
return|;
block|}
specifier|public
name|byte
index|[]
name|getEncryptedContent
parameter_list|()
block|{
return|return
name|encryptedContent
return|;
block|}
specifier|public
name|byte
index|[]
name|getAuthTag
parameter_list|()
block|{
return|return
name|authTag
return|;
block|}
block|}
end_class

end_unit

