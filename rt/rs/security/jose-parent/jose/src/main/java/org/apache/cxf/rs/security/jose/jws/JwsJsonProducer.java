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
name|PrivateKey
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
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Set
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
name|JoseConstants
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
name|JoseHeaders
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
name|JwsJsonProducer
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
name|JwsJsonProducer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|supportFlattened
decl_stmt|;
specifier|private
name|boolean
name|supportDetached
decl_stmt|;
specifier|private
name|String
name|plainPayload
decl_stmt|;
specifier|private
name|String
name|encodedPayload
decl_stmt|;
specifier|private
name|List
argument_list|<
name|JwsJsonSignatureEntry
argument_list|>
name|signatures
init|=
operator|new
name|LinkedList
argument_list|<
name|JwsJsonSignatureEntry
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|JsonMapObjectReaderWriter
name|writer
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
decl_stmt|;
specifier|public
name|JwsJsonProducer
parameter_list|(
name|String
name|tbsDocument
parameter_list|)
block|{
name|this
argument_list|(
name|tbsDocument
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JwsJsonProducer
parameter_list|(
name|String
name|tbsDocument
parameter_list|,
name|boolean
name|supportFlattened
parameter_list|)
block|{
name|this
argument_list|(
name|tbsDocument
argument_list|,
name|supportFlattened
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JwsJsonProducer
parameter_list|(
name|String
name|tbsDocument
parameter_list|,
name|boolean
name|supportFlattened
parameter_list|,
name|boolean
name|supportDetached
parameter_list|)
block|{
name|this
operator|.
name|plainPayload
operator|=
name|tbsDocument
expr_stmt|;
name|this
operator|.
name|supportFlattened
operator|=
name|supportFlattened
expr_stmt|;
name|this
operator|.
name|supportDetached
operator|=
name|supportDetached
expr_stmt|;
block|}
specifier|public
name|String
name|getPlainPayload
parameter_list|()
block|{
return|return
name|plainPayload
return|;
block|}
specifier|public
name|String
name|getUnsignedEncodedPayload
parameter_list|()
block|{
if|if
condition|(
name|encodedPayload
operator|==
literal|null
condition|)
block|{
name|encodedPayload
operator|=
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|getPlainPayload
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|encodedPayload
return|;
block|}
specifier|public
name|String
name|getJwsJsonSignedDocument
parameter_list|()
block|{
return|return
name|doGetJwsJsonSignedDocument
argument_list|(
name|supportDetached
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
specifier|public
name|String
name|getJwsJsonSignedDocument
parameter_list|(
name|boolean
name|detached
parameter_list|)
block|{
return|return
name|doGetJwsJsonSignedDocument
argument_list|(
name|detached
argument_list|)
return|;
block|}
specifier|private
name|String
name|doGetJwsJsonSignedDocument
parameter_list|(
name|boolean
name|detached
parameter_list|)
block|{
if|if
condition|(
name|signatures
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Boolean
name|b64Status
init|=
name|validateB64Status
argument_list|(
name|signatures
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|detached
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\"payload\":\""
operator|+
name|getActualPayload
argument_list|(
name|b64Status
argument_list|)
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|supportFlattened
operator|||
name|signatures
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\"signatures\":["
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|signatures
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|JwsJsonSignatureEntry
name|signature
init|=
name|signatures
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|signature
operator|.
name|toJson
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|signatures
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toJson
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|JwsJsonSignatureEntry
argument_list|>
name|getSignatureEntries
parameter_list|()
block|{
return|return
name|signatures
return|;
block|}
specifier|public
name|String
name|signWith
parameter_list|(
name|List
argument_list|<
name|JwsSignatureProvider
argument_list|>
name|signers
parameter_list|)
block|{
for|for
control|(
name|JwsSignatureProvider
name|signer
range|:
name|signers
control|)
block|{
name|signWith
argument_list|(
name|signer
argument_list|)
expr_stmt|;
block|}
return|return
name|getJwsJsonSignedDocument
argument_list|()
return|;
block|}
specifier|public
name|String
name|signWith
parameter_list|(
name|JwsSignatureProvider
name|signer
parameter_list|)
block|{
name|JwsHeaders
name|headers
init|=
operator|new
name|JwsHeaders
argument_list|()
decl_stmt|;
name|headers
operator|.
name|setSignatureAlgorithm
argument_list|(
name|signer
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|signWith
argument_list|(
name|signer
argument_list|,
name|headers
argument_list|)
return|;
block|}
specifier|public
name|String
name|signWith
parameter_list|(
name|JwsSignatureProvider
name|signer
parameter_list|,
name|JwsHeaders
name|protectedHeader
parameter_list|)
block|{
return|return
name|signWith
argument_list|(
name|signer
argument_list|,
name|protectedHeader
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|String
name|signWith
parameter_list|(
name|JsonWebKey
name|jwk
parameter_list|)
block|{
return|return
name|signWith
argument_list|(
name|JwsUtils
operator|.
name|getSignatureProvider
argument_list|(
name|jwk
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|String
name|signWith
parameter_list|(
name|PrivateKey
name|key
parameter_list|,
name|SignatureAlgorithm
name|algo
parameter_list|)
block|{
return|return
name|signWith
argument_list|(
name|JwsUtils
operator|.
name|getPrivateKeySignatureProvider
argument_list|(
name|key
argument_list|,
name|algo
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|String
name|signWith
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
name|signWith
argument_list|(
name|JwsUtils
operator|.
name|getHmacSignatureProvider
argument_list|(
name|key
argument_list|,
name|algo
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|String
name|signWith
parameter_list|(
name|JwsSignatureProvider
name|signer
parameter_list|,
name|JwsHeaders
name|protectedHeader
parameter_list|,
name|JwsHeaders
name|unprotectedHeader
parameter_list|)
block|{
name|JwsHeaders
name|unionHeaders
init|=
operator|new
name|JwsHeaders
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
name|checkUnprotectedHeaders
argument_list|(
name|unprotectedHeader
argument_list|,
name|JoseConstants
operator|.
name|HEADER_CRITICAL
argument_list|,
name|JoseConstants
operator|.
name|JWS_HEADER_B64_STATUS_HEADER
argument_list|)
expr_stmt|;
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
name|LOG
operator|.
name|warning
argument_list|(
literal|"Protected and unprotected headers have duplicate values"
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
name|INVALID_JSON_JWS
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
if|if
condition|(
name|unionHeaders
operator|.
name|getSignatureAlgorithm
argument_list|()
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Algorithm header is not set"
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
name|INVALID_JSON_JWS
argument_list|)
throw|;
block|}
name|String
name|sequenceToBeSigned
decl_stmt|;
name|String
name|actualPayload
init|=
name|protectedHeader
operator|!=
literal|null
condition|?
name|getActualPayload
argument_list|(
name|protectedHeader
operator|.
name|getPayloadEncodingStatus
argument_list|()
argument_list|)
else|:
name|getUnsignedEncodedPayload
argument_list|()
decl_stmt|;
if|if
condition|(
name|protectedHeader
operator|!=
literal|null
condition|)
block|{
name|sequenceToBeSigned
operator|=
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
operator|+
literal|"."
operator|+
name|actualPayload
expr_stmt|;
block|}
else|else
block|{
name|sequenceToBeSigned
operator|=
literal|"."
operator|+
name|getUnsignedEncodedPayload
argument_list|()
expr_stmt|;
block|}
name|byte
index|[]
name|bytesToBeSigned
init|=
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|sequenceToBeSigned
argument_list|)
decl_stmt|;
name|byte
index|[]
name|signatureBytes
init|=
name|signer
operator|.
name|sign
argument_list|(
name|unionHeaders
argument_list|,
name|bytesToBeSigned
argument_list|)
decl_stmt|;
name|String
name|encodedSignatureBytes
init|=
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|signatureBytes
argument_list|)
decl_stmt|;
name|JwsJsonSignatureEntry
name|signature
decl_stmt|;
if|if
condition|(
name|protectedHeader
operator|!=
literal|null
condition|)
block|{
name|signature
operator|=
operator|new
name|JwsJsonSignatureEntry
argument_list|(
name|actualPayload
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
argument_list|,
name|encodedSignatureBytes
argument_list|,
name|unprotectedHeader
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|signature
operator|=
operator|new
name|JwsJsonSignatureEntry
argument_list|(
name|getUnsignedEncodedPayload
argument_list|()
argument_list|,
literal|null
argument_list|,
name|encodedSignatureBytes
argument_list|,
name|unprotectedHeader
argument_list|)
expr_stmt|;
block|}
return|return
name|updateJwsJsonSignedDocument
argument_list|(
name|signature
argument_list|)
return|;
block|}
specifier|private
name|String
name|getActualPayload
parameter_list|(
name|Boolean
name|payloadEncodingStatus
parameter_list|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
operator|==
name|payloadEncodingStatus
condition|?
name|getPlainPayload
argument_list|()
else|:
name|this
operator|.
name|getUnsignedEncodedPayload
argument_list|()
return|;
block|}
specifier|private
name|String
name|updateJwsJsonSignedDocument
parameter_list|(
name|JwsJsonSignatureEntry
name|signature
parameter_list|)
block|{
name|signatures
operator|.
name|add
argument_list|(
name|signature
argument_list|)
expr_stmt|;
return|return
name|getJwsJsonSignedDocument
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|void
name|checkUnprotectedHeaders
parameter_list|(
name|JoseHeaders
name|unprotected
parameter_list|,
name|String
modifier|...
name|headerNames
parameter_list|)
block|{
for|for
control|(
name|String
name|headerName
range|:
name|headerNames
control|)
block|{
if|if
condition|(
name|unprotected
operator|.
name|containsHeader
argument_list|(
name|headerName
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Unprotected headers contain a header \""
operator|+
name|headerName
operator|+
literal|"\" which must be protected"
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
name|INVALID_JSON_JWS
argument_list|)
throw|;
block|}
block|}
block|}
specifier|static
name|Boolean
name|validateB64Status
parameter_list|(
name|List
argument_list|<
name|JwsJsonSignatureEntry
argument_list|>
name|signatures
parameter_list|)
block|{
name|Set
argument_list|<
name|Boolean
argument_list|>
name|b64Set
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|Boolean
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|JwsJsonSignatureEntry
name|entry
range|:
name|signatures
control|)
block|{
name|JwsHeaders
name|headers
init|=
name|entry
operator|.
name|getProtectedHeader
argument_list|()
decl_stmt|;
name|Boolean
name|status
init|=
name|headers
operator|!=
literal|null
condition|?
name|headers
operator|.
name|getPayloadEncodingStatus
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|status
operator|==
literal|null
condition|)
block|{
name|status
operator|=
name|Boolean
operator|.
name|TRUE
expr_stmt|;
block|}
name|b64Set
operator|.
name|add
argument_list|(
name|status
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|b64Set
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Each signature entry can sign only encoded or only unencoded payload"
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
name|INVALID_JSON_JWS
argument_list|)
throw|;
block|}
return|return
name|b64Set
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

