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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|Base64UrlUtility
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
name|JoseHeadersReaderWriter
import|;
end_import

begin_class
specifier|public
class|class
name|JweJsonProducer
block|{
specifier|private
name|JoseHeadersReaderWriter
name|writer
init|=
operator|new
name|JoseHeadersReaderWriter
argument_list|()
decl_stmt|;
specifier|private
name|JweHeaders
name|protectedHeader
decl_stmt|;
specifier|private
name|JweHeaders
name|unprotectedHeader
decl_stmt|;
specifier|private
name|byte
index|[]
name|content
decl_stmt|;
specifier|private
name|byte
index|[]
name|aad
decl_stmt|;
specifier|private
name|boolean
name|canBeFlat
decl_stmt|;
specifier|public
name|JweJsonProducer
parameter_list|(
name|JweHeaders
name|protectedHeader
parameter_list|,
name|byte
index|[]
name|content
parameter_list|)
block|{
name|this
argument_list|(
name|protectedHeader
argument_list|,
name|content
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JweJsonProducer
parameter_list|(
name|JweHeaders
name|protectedHeader
parameter_list|,
name|byte
index|[]
name|content
parameter_list|,
name|boolean
name|canBeFlat
parameter_list|)
block|{
name|this
argument_list|(
name|protectedHeader
argument_list|,
name|content
argument_list|,
literal|null
argument_list|,
name|canBeFlat
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JweJsonProducer
parameter_list|(
name|JweHeaders
name|protectedHeader
parameter_list|,
name|byte
index|[]
name|content
parameter_list|,
name|byte
index|[]
name|aad
parameter_list|,
name|boolean
name|canBeFlat
parameter_list|)
block|{
name|this
operator|.
name|protectedHeader
operator|=
name|protectedHeader
expr_stmt|;
name|this
operator|.
name|content
operator|=
name|content
expr_stmt|;
name|this
operator|.
name|aad
operator|=
name|aad
expr_stmt|;
name|this
operator|.
name|canBeFlat
operator|=
name|canBeFlat
expr_stmt|;
block|}
specifier|public
name|JweJsonProducer
parameter_list|(
name|JweHeaders
name|protectedHeader
parameter_list|,
name|JweHeaders
name|unprotectedHeader
parameter_list|,
name|byte
index|[]
name|content
parameter_list|,
name|byte
index|[]
name|aad
parameter_list|,
name|boolean
name|canBeFlat
parameter_list|)
block|{
name|this
argument_list|(
name|protectedHeader
argument_list|,
name|content
argument_list|,
name|aad
argument_list|,
name|canBeFlat
argument_list|)
expr_stmt|;
name|this
operator|.
name|unprotectedHeader
operator|=
name|unprotectedHeader
expr_stmt|;
block|}
specifier|public
name|String
name|encryptWith
parameter_list|(
name|JweEncryptionProvider
name|encryptor
parameter_list|)
block|{
return|return
name|encryptWith
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|encryptor
argument_list|)
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|String
name|encryptWith
parameter_list|(
name|JweEncryptionProvider
name|encryptor
parameter_list|,
name|JweHeaders
name|recipientUnprotected
parameter_list|)
block|{
return|return
name|encryptWith
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|encryptor
argument_list|)
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|recipientUnprotected
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|String
name|encryptWith
parameter_list|(
name|List
argument_list|<
name|JweEncryptionProvider
argument_list|>
name|encryptors
parameter_list|)
block|{
return|return
name|encryptWith
argument_list|(
name|encryptors
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|String
name|encryptWith
parameter_list|(
name|List
argument_list|<
name|JweEncryptionProvider
argument_list|>
name|encryptors
parameter_list|,
name|List
argument_list|<
name|JweHeaders
argument_list|>
name|recipientUnprotected
parameter_list|)
block|{
name|checkAndGetContentAlgorithm
argument_list|(
name|encryptors
argument_list|)
expr_stmt|;
if|if
condition|(
name|recipientUnprotected
operator|!=
literal|null
operator|&&
name|recipientUnprotected
operator|.
name|size
argument_list|()
operator|!=
name|encryptors
operator|.
name|size
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
name|JweHeaders
name|unionHeaders
init|=
operator|new
name|JweHeaders
argument_list|()
decl_stmt|;
if|if
condition|(
name|protectedHeader
operator|!=
literal|null
condition|)
block|{
name|unionHeaders
operator|.
name|asMap
argument_list|()
operator|.
name|putAll
argument_list|(
name|protectedHeader
operator|.
name|asMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|unprotectedHeader
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|Collections
operator|.
name|disjoint
argument_list|(
name|unionHeaders
operator|.
name|asMap
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|,
name|unprotectedHeader
operator|.
name|asMap
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"Protected and unprotected headers have duplicate values"
argument_list|)
throw|;
block|}
name|unionHeaders
operator|.
name|asMap
argument_list|()
operator|.
name|putAll
argument_list|(
name|unprotectedHeader
operator|.
name|asMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|JweJsonEncryptionEntry
argument_list|>
name|entries
init|=
operator|new
name|ArrayList
argument_list|<
name|JweJsonEncryptionEntry
argument_list|>
argument_list|(
name|encryptors
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|jweJsonMap
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|byte
index|[]
name|cipherText
init|=
literal|null
decl_stmt|;
name|byte
index|[]
name|authTag
init|=
literal|null
decl_stmt|;
name|byte
index|[]
name|iv
init|=
literal|null
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|encryptors
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|JweEncryptionProvider
name|encryptor
init|=
name|encryptors
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|JweHeaders
name|perRecipientUnprotected
init|=
name|recipientUnprotected
operator|==
literal|null
condition|?
literal|null
else|:
name|recipientUnprotected
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|JweHeaders
name|jsonHeaders
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|recipientUnprotected
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|Collections
operator|.
name|disjoint
argument_list|(
name|unionHeaders
operator|.
name|asMap
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|,
name|perRecipientUnprotected
operator|.
name|asMap
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"Protected and unprotected headers have duplicate values"
argument_list|)
throw|;
block|}
name|jsonHeaders
operator|=
operator|new
name|JweHeaders
argument_list|(
name|unionHeaders
operator|.
name|asMap
argument_list|()
argument_list|)
expr_stmt|;
name|jsonHeaders
operator|.
name|asMap
argument_list|()
operator|.
name|putAll
argument_list|(
name|unprotectedHeader
operator|.
name|asMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|jsonHeaders
operator|=
name|unionHeaders
expr_stmt|;
block|}
name|jsonHeaders
operator|.
name|setProtectedHeaders
argument_list|(
name|protectedHeader
argument_list|)
expr_stmt|;
name|JweEncryptionInput
name|input
init|=
name|createEncryptionInput
argument_list|(
name|jsonHeaders
argument_list|)
decl_stmt|;
name|JweEncryptionOutput
name|state
init|=
name|encryptor
operator|.
name|getEncryptionOutput
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|byte
index|[]
name|currentCipherText
init|=
name|state
operator|.
name|getEncryptedContent
argument_list|()
decl_stmt|;
name|byte
index|[]
name|currentAuthTag
init|=
name|state
operator|.
name|getAuthTag
argument_list|()
decl_stmt|;
name|byte
index|[]
name|currentIv
init|=
name|state
operator|.
name|getIv
argument_list|()
decl_stmt|;
if|if
condition|(
name|cipherText
operator|==
literal|null
condition|)
block|{
name|cipherText
operator|=
name|currentCipherText
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|Arrays
operator|.
name|equals
argument_list|(
name|cipherText
argument_list|,
name|currentCipherText
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
if|if
condition|(
name|authTag
operator|==
literal|null
condition|)
block|{
name|authTag
operator|=
name|currentAuthTag
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|Arrays
operator|.
name|equals
argument_list|(
name|authTag
argument_list|,
name|currentAuthTag
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
if|if
condition|(
name|iv
operator|==
literal|null
condition|)
block|{
name|iv
operator|=
name|currentIv
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|Arrays
operator|.
name|equals
argument_list|(
name|iv
argument_list|,
name|currentIv
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
name|byte
index|[]
name|encryptedCek
init|=
name|state
operator|.
name|getContentEncryptionKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|encryptedCek
operator|==
literal|null
operator|&&
name|encryptor
operator|.
name|getKeyAlgorithm
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// can be null only if it is the direct key encryption
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
name|String
name|encodedCek
init|=
name|encryptedCek
operator|==
literal|null
condition|?
literal|null
else|:
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|encryptedCek
argument_list|)
decl_stmt|;
name|entries
operator|.
name|add
argument_list|(
operator|new
name|JweJsonEncryptionEntry
argument_list|(
name|perRecipientUnprotected
argument_list|,
name|encodedCek
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|protectedHeader
operator|!=
literal|null
condition|)
block|{
name|jweJsonMap
operator|.
name|put
argument_list|(
literal|"protected"
argument_list|,
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|writer
operator|.
name|toJson
argument_list|(
name|protectedHeader
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|unprotectedHeader
operator|!=
literal|null
condition|)
block|{
name|jweJsonMap
operator|.
name|put
argument_list|(
literal|"unprotected"
argument_list|,
name|unprotectedHeader
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|entries
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|canBeFlat
condition|)
block|{
name|JweHeaders
name|unprotectedEntryHeader
init|=
name|entries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getUnprotectedHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|unprotectedEntryHeader
operator|!=
literal|null
condition|)
block|{
name|jweJsonMap
operator|.
name|put
argument_list|(
literal|"header"
argument_list|,
name|unprotectedEntryHeader
argument_list|)
expr_stmt|;
block|}
name|jweJsonMap
operator|.
name|put
argument_list|(
literal|"encrypted_key"
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getEncodedEncryptedKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|jweJsonMap
operator|.
name|put
argument_list|(
literal|"recipients"
argument_list|,
name|entries
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|aad
operator|!=
literal|null
condition|)
block|{
name|jweJsonMap
operator|.
name|put
argument_list|(
literal|"aad"
argument_list|,
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|aad
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|jweJsonMap
operator|.
name|put
argument_list|(
literal|"iv"
argument_list|,
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|iv
argument_list|)
argument_list|)
expr_stmt|;
name|jweJsonMap
operator|.
name|put
argument_list|(
literal|"ciphertext"
argument_list|,
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|cipherText
argument_list|)
argument_list|)
expr_stmt|;
name|jweJsonMap
operator|.
name|put
argument_list|(
literal|"tag"
argument_list|,
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|authTag
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|writer
operator|.
name|toJson
argument_list|(
name|jweJsonMap
argument_list|)
return|;
block|}
specifier|protected
name|JweEncryptionInput
name|createEncryptionInput
parameter_list|(
name|JweHeaders
name|jsonHeaders
parameter_list|)
block|{
return|return
operator|new
name|JweEncryptionInput
argument_list|(
name|jsonHeaders
argument_list|,
name|content
argument_list|,
name|aad
argument_list|)
return|;
block|}
specifier|private
name|String
name|checkAndGetContentAlgorithm
parameter_list|(
name|List
argument_list|<
name|JweEncryptionProvider
argument_list|>
name|encryptors
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|JweEncryptionProvider
name|encryptor
range|:
name|encryptors
control|)
block|{
name|set
operator|.
name|add
argument_list|(
name|encryptor
operator|.
name|getContentAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|set
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"Invalid content encryption algorithm"
argument_list|)
throw|;
block|}
return|return
name|set
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
block|}
end_class

end_unit

