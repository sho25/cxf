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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|jwt
operator|.
name|JwtTokenReaderWriter
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
name|JweCompactProducer
block|{
specifier|private
name|StringBuilder
name|jweContentBuilder
decl_stmt|;
specifier|private
name|String
name|encodedEncryptedContent
decl_stmt|;
specifier|private
name|String
name|encodedAuthTag
decl_stmt|;
specifier|public
name|JweCompactProducer
parameter_list|(
name|JweHeaders
name|headers
parameter_list|,
name|byte
index|[]
name|encryptedContentEncryptionKey
parameter_list|,
name|byte
index|[]
name|cipherInitVector
parameter_list|,
name|byte
index|[]
name|encryptedContentNoTag
parameter_list|,
name|byte
index|[]
name|authenticationTag
parameter_list|)
block|{
name|this
argument_list|(
name|headers
argument_list|,
literal|null
argument_list|,
name|encryptedContentEncryptionKey
argument_list|,
name|cipherInitVector
argument_list|,
name|encryptedContentNoTag
argument_list|,
name|authenticationTag
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JweCompactProducer
parameter_list|(
name|JweHeaders
name|headers
parameter_list|,
name|JwtHeadersWriter
name|writer
parameter_list|,
name|byte
index|[]
name|encryptedContentEncryptionKey
parameter_list|,
name|byte
index|[]
name|cipherInitVector
parameter_list|,
name|byte
index|[]
name|encryptedContentNoTag
parameter_list|,
name|byte
index|[]
name|authenticationTag
parameter_list|)
block|{
name|jweContentBuilder
operator|=
name|startJweContent
argument_list|(
operator|new
name|StringBuilder
argument_list|()
argument_list|,
name|headers
argument_list|,
name|writer
argument_list|,
name|encryptedContentEncryptionKey
argument_list|,
name|cipherInitVector
argument_list|)
expr_stmt|;
name|this
operator|.
name|encodedEncryptedContent
operator|=
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|encryptedContentNoTag
argument_list|)
expr_stmt|;
name|this
operator|.
name|encodedAuthTag
operator|=
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|authenticationTag
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JweCompactProducer
parameter_list|(
name|JweHeaders
name|headers
parameter_list|,
name|byte
index|[]
name|encryptedContentEncryptionKey
parameter_list|,
name|byte
index|[]
name|cipherInitVector
parameter_list|,
name|byte
index|[]
name|encryptedContentWithTag
parameter_list|,
name|int
name|authTagLengthBits
parameter_list|)
block|{
name|this
argument_list|(
name|headers
argument_list|,
literal|null
argument_list|,
name|encryptedContentEncryptionKey
argument_list|,
name|cipherInitVector
argument_list|,
name|encryptedContentWithTag
argument_list|,
name|authTagLengthBits
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JweCompactProducer
parameter_list|(
name|JweHeaders
name|headers
parameter_list|,
name|JwtHeadersWriter
name|writer
parameter_list|,
name|byte
index|[]
name|encryptedContentEncryptionKey
parameter_list|,
name|byte
index|[]
name|cipherInitVector
parameter_list|,
name|byte
index|[]
name|encryptedContentWithTag
parameter_list|,
name|int
name|authTagLengthBits
parameter_list|)
block|{
name|jweContentBuilder
operator|=
name|startJweContent
argument_list|(
operator|new
name|StringBuilder
argument_list|()
argument_list|,
name|headers
argument_list|,
name|writer
argument_list|,
name|encryptedContentEncryptionKey
argument_list|,
name|cipherInitVector
argument_list|)
expr_stmt|;
name|this
operator|.
name|encodedEncryptedContent
operator|=
name|Base64UrlUtility
operator|.
name|encodeChunk
argument_list|(
name|encryptedContentWithTag
argument_list|,
literal|0
argument_list|,
name|encryptedContentWithTag
operator|.
name|length
operator|-
name|authTagLengthBits
operator|/
literal|8
argument_list|)
expr_stmt|;
name|this
operator|.
name|encodedAuthTag
operator|=
name|Base64UrlUtility
operator|.
name|encodeChunk
argument_list|(
name|encryptedContentWithTag
argument_list|,
name|encryptedContentWithTag
operator|.
name|length
operator|-
name|authTagLengthBits
operator|/
literal|8
argument_list|,
name|authTagLengthBits
operator|/
literal|8
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|String
name|startJweContent
parameter_list|(
name|JweHeaders
name|headers
parameter_list|,
name|JwtHeadersWriter
name|writer
parameter_list|,
name|byte
index|[]
name|encryptedContentEncryptionKey
parameter_list|,
name|byte
index|[]
name|cipherInitVector
parameter_list|)
block|{
return|return
name|startJweContent
argument_list|(
operator|new
name|StringBuilder
argument_list|()
argument_list|,
name|headers
argument_list|,
name|writer
argument_list|,
name|encryptedContentEncryptionKey
argument_list|,
name|cipherInitVector
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|StringBuilder
name|startJweContent
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|JweHeaders
name|headers
parameter_list|,
name|JwtHeadersWriter
name|writer
parameter_list|,
name|byte
index|[]
name|encryptedContentEncryptionKey
parameter_list|,
name|byte
index|[]
name|cipherInitVector
parameter_list|)
block|{
name|writer
operator|=
name|writer
operator|==
literal|null
condition|?
operator|new
name|JwtTokenReaderWriter
argument_list|()
else|:
name|writer
expr_stmt|;
name|String
name|encodedHeaders
init|=
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|writer
operator|.
name|headersToJson
argument_list|(
name|headers
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|encodedContentEncryptionKey
init|=
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|encryptedContentEncryptionKey
argument_list|)
decl_stmt|;
name|String
name|encodedInitVector
init|=
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|cipherInitVector
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|encodedHeaders
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|encodedContentEncryptionKey
operator|==
literal|null
condition|?
literal|""
else|:
name|encodedContentEncryptionKey
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|encodedInitVector
operator|==
literal|null
condition|?
literal|""
else|:
name|encodedInitVector
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
return|return
name|sb
return|;
block|}
specifier|public
specifier|static
name|void
name|startJweContent
parameter_list|(
name|OutputStream
name|os
parameter_list|,
name|JweHeaders
name|headers
parameter_list|,
name|JwtHeadersWriter
name|writer
parameter_list|,
name|byte
index|[]
name|encryptedContentEncryptionKey
parameter_list|,
name|byte
index|[]
name|cipherInitVector
parameter_list|)
throws|throws
name|IOException
block|{
name|writer
operator|=
name|writer
operator|==
literal|null
condition|?
operator|new
name|JwtTokenReaderWriter
argument_list|()
else|:
name|writer
expr_stmt|;
name|byte
index|[]
name|jsonBytes
init|=
name|writer
operator|.
name|headersToJson
argument_list|(
name|headers
argument_list|)
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|Base64UrlUtility
operator|.
name|encodeAndStream
argument_list|(
name|jsonBytes
argument_list|,
literal|0
argument_list|,
name|jsonBytes
operator|.
name|length
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
name|Base64UrlUtility
operator|.
name|encodeAndStream
argument_list|(
name|encryptedContentEncryptionKey
argument_list|,
literal|0
argument_list|,
name|encryptedContentEncryptionKey
operator|.
name|length
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
name|Base64UrlUtility
operator|.
name|encodeAndStream
argument_list|(
name|cipherInitVector
argument_list|,
literal|0
argument_list|,
name|cipherInitVector
operator|.
name|length
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getJweContent
parameter_list|()
block|{
return|return
name|jweContentBuilder
operator|.
name|append
argument_list|(
name|encodedEncryptedContent
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|encodedAuthTag
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

