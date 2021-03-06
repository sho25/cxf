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
name|jws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
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
name|StringUtils
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
name|SignatureAlgorithm
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
name|jwk
operator|.
name|JsonWebKey
import|;
end_import

begin_class
specifier|public
class|class
name|JwsCompactConsumer
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
name|JwsCompactConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|JsonMapObjectReaderWriter
name|reader
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|String
name|encodedSequence
decl_stmt|;
specifier|private
specifier|final
name|String
name|encodedSignature
decl_stmt|;
specifier|private
specifier|final
name|String
name|headersJson
decl_stmt|;
specifier|private
name|String
name|jwsPayload
decl_stmt|;
specifier|private
name|String
name|decodedJwsPayload
decl_stmt|;
specifier|private
name|JwsHeaders
name|jwsHeaders
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
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JwsCompactConsumer
parameter_list|(
name|String
name|encodedJws
parameter_list|,
name|String
name|detachedPayload
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
name|encodedJws
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
name|LOG
operator|.
name|warning
argument_list|(
literal|"Compact JWS does not have 3 parts"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JwsException
argument_list|(
name|JwsException
operator|.
name|Error
operator|.
name|INVALID_COMPACT_JWS
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
name|jwsPayload
operator|=
name|parts
index|[
literal|1
index|]
expr_stmt|;
if|if
condition|(
name|detachedPayload
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|jwsPayload
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Compact JWS includes a payload expected to be detached"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JwsException
argument_list|(
name|JwsException
operator|.
name|Error
operator|.
name|INVALID_COMPACT_JWS
argument_list|)
throw|;
block|}
name|jwsPayload
operator|=
name|detachedPayload
expr_stmt|;
block|}
name|encodedSequence
operator|=
name|parts
index|[
literal|0
index|]
operator|+
literal|"."
operator|+
name|jwsPayload
expr_stmt|;
name|headersJson
operator|=
name|JoseUtils
operator|.
name|decodeToString
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getUnsignedEncodedSequence
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
if|if
condition|(
name|decodedJwsPayload
operator|==
literal|null
condition|)
block|{
name|getJwsHeaders
argument_list|()
expr_stmt|;
if|if
condition|(
name|JwsUtils
operator|.
name|isPayloadUnencoded
argument_list|(
name|jwsHeaders
argument_list|)
condition|)
block|{
name|decodedJwsPayload
operator|=
name|jwsPayload
expr_stmt|;
block|}
else|else
block|{
name|decodedJwsPayload
operator|=
name|JoseUtils
operator|.
name|decodeToString
argument_list|(
name|jwsPayload
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|decodedJwsPayload
return|;
block|}
specifier|public
name|byte
index|[]
name|getDecodedJwsPayloadBytes
parameter_list|()
block|{
return|return
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|getDecodedJwsPayload
argument_list|()
argument_list|)
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
name|JoseUtils
operator|.
name|decode
argument_list|(
name|encodedSignature
argument_list|)
return|;
block|}
specifier|public
name|JwsHeaders
name|getJwsHeaders
parameter_list|()
block|{
if|if
condition|(
name|jwsHeaders
operator|==
literal|null
condition|)
block|{
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
name|JwsException
argument_list|(
name|JwsException
operator|.
name|Error
operator|.
name|INVALID_COMPACT_JWS
argument_list|)
throw|;
block|}
name|jwsHeaders
operator|=
operator|new
name|JwsHeaders
argument_list|(
name|joseHeaders
operator|.
name|asMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|jwsHeaders
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
try|try
block|{
if|if
condition|(
name|validator
operator|.
name|verify
argument_list|(
name|getJwsHeaders
argument_list|()
argument_list|,
name|getUnsignedEncodedSequence
argument_list|()
argument_list|,
name|getDecodedSignature
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|JwsException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
name|LOG
operator|.
name|warning
argument_list|(
literal|"Invalid Signature"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|verifySignatureWith
parameter_list|(
name|JsonWebKey
name|key
parameter_list|)
block|{
return|return
name|verifySignatureWith
argument_list|(
name|JwsUtils
operator|.
name|getSignatureVerifier
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|verifySignatureWith
parameter_list|(
name|JsonWebKey
name|key
parameter_list|,
name|SignatureAlgorithm
name|algo
parameter_list|)
block|{
return|return
name|verifySignatureWith
argument_list|(
name|JwsUtils
operator|.
name|getSignatureVerifier
argument_list|(
name|key
argument_list|,
name|algo
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|verifySignatureWith
parameter_list|(
name|X509Certificate
name|cert
parameter_list|,
name|SignatureAlgorithm
name|algo
parameter_list|)
block|{
return|return
name|verifySignatureWith
argument_list|(
name|JwsUtils
operator|.
name|getPublicKeySignatureVerifier
argument_list|(
name|cert
argument_list|,
name|algo
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|verifySignatureWith
parameter_list|(
name|PublicKey
name|key
parameter_list|,
name|SignatureAlgorithm
name|algo
parameter_list|)
block|{
return|return
name|verifySignatureWith
argument_list|(
name|JwsUtils
operator|.
name|getPublicKeySignatureVerifier
argument_list|(
name|key
argument_list|,
name|algo
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|verifySignatureWith
parameter_list|(
name|byte
index|[]
name|key
parameter_list|,
name|SignatureAlgorithm
name|algo
parameter_list|)
block|{
return|return
name|verifySignatureWith
argument_list|(
name|JwsUtils
operator|.
name|getHmacSignatureVerifier
argument_list|(
name|key
argument_list|,
name|algo
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|validateCriticalHeaders
parameter_list|()
block|{
return|return
name|JwsUtils
operator|.
name|validateCriticalHeaders
argument_list|(
name|getJwsHeaders
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|JsonMapObjectReaderWriter
name|getReader
parameter_list|()
block|{
return|return
name|reader
return|;
block|}
block|}
end_class

end_unit

