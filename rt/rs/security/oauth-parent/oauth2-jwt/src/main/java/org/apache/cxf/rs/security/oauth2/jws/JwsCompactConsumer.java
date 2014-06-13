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
name|jws
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
name|oauth2
operator|.
name|jwt
operator|.
name|JwtHeaders
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
name|JwtTokenReader
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
name|provider
operator|.
name|OAuthServiceException
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
name|JwsCompactConsumer
block|{
specifier|private
name|JwtHeadersReader
name|reader
init|=
operator|new
name|JwtTokenReaderWriter
argument_list|()
decl_stmt|;
specifier|private
name|String
name|encodedSequence
decl_stmt|;
specifier|private
name|String
name|encodedSignature
decl_stmt|;
specifier|private
name|String
name|headersJson
decl_stmt|;
specifier|private
name|String
name|jwsPayload
decl_stmt|;
specifier|private
name|JwsSignatureProperties
name|props
decl_stmt|;
specifier|public
name|JwsCompactConsumer
parameter_list|(
name|String
name|encodedJws
parameter_list|)
block|{
name|this
argument_list|(
name|encodedJws
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JwsCompactConsumer
parameter_list|(
name|String
name|encodedJws
parameter_list|,
name|JwsSignatureProperties
name|props
parameter_list|)
block|{
name|this
argument_list|(
name|encodedJws
argument_list|,
name|props
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JwsCompactConsumer
parameter_list|(
name|String
name|encodedJws
parameter_list|,
name|JwtTokenReader
name|r
parameter_list|)
block|{
name|this
argument_list|(
name|encodedJws
argument_list|,
literal|null
argument_list|,
name|r
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JwsCompactConsumer
parameter_list|(
name|String
name|encodedJws
parameter_list|,
name|JwsSignatureProperties
name|props
parameter_list|,
name|JwtHeadersReader
name|r
parameter_list|)
block|{
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|reader
operator|=
name|r
expr_stmt|;
block|}
name|this
operator|.
name|props
operator|=
name|props
expr_stmt|;
name|String
index|[]
name|parts
init|=
name|encodedJws
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
literal|3
condition|)
block|{
if|if
condition|(
name|parts
operator|.
name|length
operator|==
literal|2
operator|&&
name|encodedJws
operator|.
name|endsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|encodedSignature
operator|=
literal|""
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"Invalid JWS Compact sequence"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|encodedSignature
operator|=
name|parts
index|[
literal|2
index|]
expr_stmt|;
block|}
name|headersJson
operator|=
name|decodeToString
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|jwsPayload
operator|=
name|decodeToString
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|encodedSequence
operator|=
name|parts
index|[
literal|0
index|]
operator|+
literal|"."
operator|+
name|parts
index|[
literal|1
index|]
expr_stmt|;
block|}
specifier|public
name|String
name|getUnsignedEncodedPayload
parameter_list|()
block|{
return|return
name|encodedSequence
return|;
block|}
specifier|public
name|String
name|getEncodedSignature
parameter_list|()
block|{
return|return
name|encodedSignature
return|;
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
name|String
name|getDecodedJwsPayload
parameter_list|()
block|{
return|return
name|jwsPayload
return|;
block|}
specifier|public
name|byte
index|[]
name|getDecodedSignature
parameter_list|()
block|{
return|return
name|encodedSignature
operator|.
name|isEmpty
argument_list|()
condition|?
operator|new
name|byte
index|[]
block|{}
else|:
name|decode
argument_list|(
name|encodedSignature
argument_list|)
return|;
block|}
specifier|public
name|JwtHeaders
name|getJwtHeaders
parameter_list|()
block|{
return|return
name|getReader
argument_list|()
operator|.
name|fromJsonHeaders
argument_list|(
name|headersJson
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|verifySignatureWith
parameter_list|(
name|JwsSignatureVerifier
name|validator
parameter_list|)
block|{
name|enforceJweSignatureProperties
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|validator
operator|.
name|verify
argument_list|(
name|getJwtHeaders
argument_list|()
argument_list|,
name|getUnsignedEncodedPayload
argument_list|()
argument_list|,
name|getDecodedSignature
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|enforceJweSignatureProperties
parameter_list|()
block|{
if|if
condition|(
name|props
operator|!=
literal|null
condition|)
block|{
comment|//TODO:
block|}
block|}
specifier|private
specifier|static
name|String
name|decodeToString
parameter_list|(
name|String
name|encoded
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|String
argument_list|(
name|decode
argument_list|(
name|encoded
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
specifier|protected
name|JwtHeadersReader
name|getReader
parameter_list|()
block|{
return|return
name|reader
return|;
block|}
specifier|private
specifier|static
name|byte
index|[]
name|decode
parameter_list|(
name|String
name|encoded
parameter_list|)
block|{
try|try
block|{
return|return
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|encoded
argument_list|)
return|;
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
block|}
end_class

end_unit

