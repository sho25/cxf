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
name|io
operator|.
name|UnsupportedEncodingException
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
name|Base64Exception
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
name|JoseHeadersReader
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
name|Base64UrlUtility
import|;
end_import

begin_class
specifier|public
class|class
name|JweCompactConsumer
block|{
specifier|private
name|String
name|headersJson
decl_stmt|;
specifier|private
name|byte
index|[]
name|encryptedCEK
decl_stmt|;
specifier|private
name|byte
index|[]
name|initVector
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
name|JweHeaders
name|jweHeaders
decl_stmt|;
specifier|public
name|JweCompactConsumer
parameter_list|(
name|String
name|jweContent
parameter_list|)
block|{
name|this
argument_list|(
name|jweContent
argument_list|,
operator|new
name|JoseHeadersReaderWriter
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JweCompactConsumer
parameter_list|(
name|String
name|jweContent
parameter_list|,
name|JoseHeadersReader
name|reader
parameter_list|)
block|{
name|String
index|[]
name|parts
init|=
name|jweContent
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|!=
literal|5
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"5 JWE parts are expected"
argument_list|)
throw|;
block|}
try|try
block|{
name|headersJson
operator|=
operator|new
name|String
argument_list|(
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|encryptedCEK
operator|=
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|initVector
operator|=
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|parts
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|encryptedContent
operator|=
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|parts
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|authTag
operator|=
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|parts
index|[
literal|4
index|]
argument_list|)
expr_stmt|;
name|jweHeaders
operator|=
operator|new
name|JweHeaders
argument_list|(
name|reader
operator|.
name|fromJsonHeaders
argument_list|(
name|headersJson
argument_list|)
operator|.
name|asMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Base64Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|enforceJweCryptoProperties
parameter_list|(
name|JweCryptoProperties
name|props
parameter_list|)
block|{
if|if
condition|(
name|props
operator|!=
literal|null
condition|)
block|{
comment|//TODO
block|}
block|}
specifier|public
name|String
name|getDecodedJsonHeaders
parameter_list|()
block|{
return|return
name|headersJson
return|;
block|}
specifier|public
name|JweHeaders
name|getJweHeaders
parameter_list|()
block|{
return|return
name|jweHeaders
return|;
block|}
specifier|public
name|byte
index|[]
name|getEncryptedContentEncryptionKey
parameter_list|()
block|{
return|return
name|encryptedCEK
return|;
block|}
specifier|public
name|byte
index|[]
name|getContentDecryptionCipherInitVector
parameter_list|()
block|{
return|return
name|initVector
return|;
block|}
specifier|public
name|byte
index|[]
name|getContentEncryptionCipherAAD
parameter_list|()
block|{
return|return
name|JweHeaders
operator|.
name|toCipherAdditionalAuthData
argument_list|(
name|headersJson
argument_list|)
return|;
block|}
specifier|public
name|byte
index|[]
name|getEncryptionAuthenticationTag
parameter_list|()
block|{
return|return
name|authTag
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
name|getEncryptedContentWithAuthTag
parameter_list|()
block|{
return|return
name|getCipherWithAuthTag
argument_list|(
name|encryptedContent
argument_list|,
name|authTag
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|byte
index|[]
name|getCipherWithAuthTag
parameter_list|(
name|byte
index|[]
name|cipher
parameter_list|,
name|byte
index|[]
name|authTag
parameter_list|)
block|{
name|byte
index|[]
name|encryptedContentWithTag
init|=
operator|new
name|byte
index|[
name|cipher
operator|.
name|length
operator|+
name|authTag
operator|.
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|cipher
argument_list|,
literal|0
argument_list|,
name|encryptedContentWithTag
argument_list|,
literal|0
argument_list|,
name|cipher
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|authTag
argument_list|,
literal|0
argument_list|,
name|encryptedContentWithTag
argument_list|,
name|cipher
operator|.
name|length
argument_list|,
name|authTag
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|encryptedContentWithTag
return|;
block|}
specifier|public
name|byte
index|[]
name|getDecryptedContent
parameter_list|(
name|JweDecryptionProvider
name|decryption
parameter_list|)
block|{
return|return
name|decryption
operator|.
name|decrypt
argument_list|(
name|this
argument_list|)
return|;
block|}
specifier|public
name|String
name|getDecryptedContentText
parameter_list|(
name|JweDecryptionProvider
name|decryption
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|String
argument_list|(
name|getDecryptedContent
argument_list|(
name|decryption
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

