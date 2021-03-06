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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|jaxrs
operator|.
name|json
operator|.
name|basic
operator|.
name|JsonMapObject
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
name|jaxrs
operator|.
name|json
operator|.
name|basic
operator|.
name|JsonMapObjectReaderWriter
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
name|common
operator|.
name|JoseUtils
import|;
end_import

begin_class
specifier|public
class|class
name|JweCompactConsumer
block|{
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JweCompactConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|JweDecryptionInput
name|jweDecryptionInput
decl_stmt|;
specifier|public
name|JweCompactConsumer
parameter_list|(
name|String
name|jweContent
parameter_list|)
block|{
name|String
index|[]
name|parts
init|=
name|JoseUtils
operator|.
name|getCompactParts
argument_list|(
name|jweContent
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
name|LOG
operator|.
name|warning
argument_list|(
literal|"5 JWE parts are expected"
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
name|INVALID_COMPACT_JWE
argument_list|)
throw|;
block|}
try|try
block|{
name|String
name|headersJson
init|=
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
decl_stmt|;
name|byte
index|[]
name|encryptedCEK
init|=
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|byte
index|[]
name|initVector
init|=
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|parts
index|[
literal|2
index|]
argument_list|)
decl_stmt|;
name|byte
index|[]
name|encryptedContent
init|=
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|parts
index|[
literal|3
index|]
argument_list|)
decl_stmt|;
name|byte
index|[]
name|authTag
init|=
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|parts
index|[
literal|4
index|]
argument_list|)
decl_stmt|;
name|JsonMapObjectReaderWriter
name|reader
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
decl_stmt|;
name|JsonMapObject
name|joseHeaders
init|=
name|reader
operator|.
name|fromJsonToJsonObject
argument_list|(
name|headersJson
argument_list|)
decl_stmt|;
if|if
condition|(
name|joseHeaders
operator|.
name|getUpdateCount
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Duplicate headers have been detected"
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
name|INVALID_COMPACT_JWE
argument_list|)
throw|;
block|}
name|JweHeaders
name|jweHeaders
init|=
operator|new
name|JweHeaders
argument_list|(
name|joseHeaders
operator|.
name|asMap
argument_list|()
argument_list|)
decl_stmt|;
name|jweDecryptionInput
operator|=
operator|new
name|JweDecryptionInput
argument_list|(
name|encryptedCEK
argument_list|,
name|initVector
argument_list|,
name|encryptedContent
argument_list|,
name|authTag
argument_list|,
literal|null
argument_list|,
name|headersJson
argument_list|,
name|jweHeaders
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Base64Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Incorrect Base64 URL encoding"
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
name|INVALID_COMPACT_JWE
argument_list|)
throw|;
block|}
block|}
specifier|public
name|String
name|getDecodedJsonHeaders
parameter_list|()
block|{
return|return
name|jweDecryptionInput
operator|.
name|getDecodedJsonHeaders
argument_list|()
return|;
block|}
specifier|public
name|JweHeaders
name|getJweHeaders
parameter_list|()
block|{
return|return
name|jweDecryptionInput
operator|.
name|getJweHeaders
argument_list|()
return|;
block|}
specifier|public
name|byte
index|[]
name|getEncryptedContentEncryptionKey
parameter_list|()
block|{
return|return
name|jweDecryptionInput
operator|.
name|getEncryptedCEK
argument_list|()
return|;
block|}
specifier|public
name|byte
index|[]
name|getContentDecryptionCipherInitVector
parameter_list|()
block|{
return|return
name|jweDecryptionInput
operator|.
name|getInitVector
argument_list|()
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
name|jweDecryptionInput
operator|.
name|getDecodedJsonHeaders
argument_list|()
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
name|jweDecryptionInput
operator|.
name|getAuthTag
argument_list|()
return|;
block|}
specifier|public
name|byte
index|[]
name|getEncryptedContent
parameter_list|()
block|{
return|return
name|jweDecryptionInput
operator|.
name|getEncryptedContent
argument_list|()
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
name|getEncryptedContent
argument_list|()
argument_list|,
name|getEncryptionAuthenticationTag
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|JweDecryptionInput
name|getJweDecryptionInput
parameter_list|()
block|{
return|return
name|jweDecryptionInput
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
comment|// temp workaround
return|return
name|decryption
operator|.
name|decrypt
argument_list|(
name|jweDecryptionInput
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
return|return
operator|new
name|String
argument_list|(
name|getDecryptedContent
argument_list|(
name|decryption
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|validateCriticalHeaders
parameter_list|()
block|{
return|return
name|JweUtils
operator|.
name|validateCriticalHeaders
argument_list|(
name|getJweHeaders
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

