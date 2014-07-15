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
name|JwtHeadersReader
import|;
end_import

begin_class
specifier|public
class|class
name|DirectKeyJweDecryption
extends|extends
name|AbstractJweDecryption
block|{
specifier|private
name|byte
index|[]
name|contentDecryptionKey
decl_stmt|;
specifier|public
name|DirectKeyJweDecryption
parameter_list|(
name|Key
name|contentDecryptionKey
parameter_list|)
block|{
name|this
argument_list|(
name|contentDecryptionKey
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DirectKeyJweDecryption
parameter_list|(
name|Key
name|contentDecryptionKey
parameter_list|,
name|JweCryptoProperties
name|props
parameter_list|)
block|{
name|this
argument_list|(
name|contentDecryptionKey
argument_list|,
name|props
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DirectKeyJweDecryption
parameter_list|(
name|Key
name|contentDecryptionKey
parameter_list|,
name|JweCryptoProperties
name|props
parameter_list|,
name|JwtHeadersReader
name|reader
parameter_list|)
block|{
name|super
argument_list|(
name|props
argument_list|,
name|reader
argument_list|)
expr_stmt|;
name|this
operator|.
name|contentDecryptionKey
operator|=
name|contentDecryptionKey
operator|.
name|getEncoded
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|byte
index|[]
name|getContentEncryptionKey
parameter_list|(
name|JweCompactConsumer
name|consumer
parameter_list|)
block|{
name|byte
index|[]
name|encryptedCEK
init|=
name|getEncryptedContentEncryptionKey
argument_list|(
name|consumer
argument_list|)
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
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
return|return
name|contentDecryptionKey
return|;
block|}
block|}
end_class

end_unit

