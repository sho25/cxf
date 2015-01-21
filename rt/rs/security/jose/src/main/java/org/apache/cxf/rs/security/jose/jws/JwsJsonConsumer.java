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
name|interfaces
operator|.
name|RSAPublicKey
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
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
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
name|helpers
operator|.
name|CastUtils
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
name|provider
operator|.
name|json
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
name|provider
operator|.
name|json
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
name|jwk
operator|.
name|JsonWebKey
import|;
end_import

begin_class
specifier|public
class|class
name|JwsJsonConsumer
block|{
specifier|private
name|String
name|jwsSignedDocument
decl_stmt|;
specifier|private
name|String
name|encodedJwsPayload
decl_stmt|;
specifier|private
name|List
argument_list|<
name|JwsJsonSignatureEntry
argument_list|>
name|signatureEntries
init|=
operator|new
name|LinkedList
argument_list|<
name|JwsJsonSignatureEntry
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * @param jwsSignedDocument      *            signed JWS Document      */
specifier|public
name|JwsJsonConsumer
parameter_list|(
name|String
name|jwsSignedDocument
parameter_list|)
block|{
name|this
argument_list|(
name|jwsSignedDocument
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JwsJsonConsumer
parameter_list|(
name|String
name|jwsSignedDocument
parameter_list|,
name|String
name|encodedDetachedPayload
parameter_list|)
block|{
name|this
operator|.
name|jwsSignedDocument
operator|=
name|jwsSignedDocument
expr_stmt|;
name|prepare
argument_list|(
name|encodedDetachedPayload
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|prepare
parameter_list|(
name|String
name|encodedDetachedPayload
parameter_list|)
block|{
name|JsonMapObject
name|jsonObject
init|=
operator|new
name|JsonMapObject
argument_list|()
decl_stmt|;
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
operator|.
name|fromJson
argument_list|(
name|jsonObject
argument_list|,
name|jwsSignedDocument
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|jsonObjectMap
init|=
name|jsonObject
operator|.
name|asMap
argument_list|()
decl_stmt|;
name|encodedJwsPayload
operator|=
operator|(
name|String
operator|)
name|jsonObjectMap
operator|.
name|get
argument_list|(
literal|"payload"
argument_list|)
expr_stmt|;
if|if
condition|(
name|encodedJwsPayload
operator|==
literal|null
condition|)
block|{
name|encodedJwsPayload
operator|=
name|encodedDetachedPayload
expr_stmt|;
block|}
if|if
condition|(
name|encodedJwsPayload
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"Invalid JWS JSON sequence: no payload is available"
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|signatureArray
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|jsonObjectMap
operator|.
name|get
argument_list|(
literal|"signatures"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|signatureArray
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|jsonObjectMap
operator|.
name|containsKey
argument_list|(
literal|"signature"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"Invalid JWS JSON sequence"
argument_list|)
throw|;
block|}
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|signatureEntry
range|:
name|signatureArray
control|)
block|{
name|this
operator|.
name|signatureEntries
operator|.
name|add
argument_list|(
name|getSignatureObject
argument_list|(
name|signatureEntry
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|this
operator|.
name|signatureEntries
operator|.
name|add
argument_list|(
name|getSignatureObject
argument_list|(
name|jsonObjectMap
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|signatureEntries
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"Invalid JWS JSON sequence: no signatures are available"
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|JwsJsonSignatureEntry
name|getSignatureObject
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|signatureEntry
parameter_list|)
block|{
name|String
name|protectedHeader
init|=
operator|(
name|String
operator|)
name|signatureEntry
operator|.
name|get
argument_list|(
literal|"protected"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|header
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|signatureEntry
operator|.
name|get
argument_list|(
literal|"header"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|signature
init|=
operator|(
name|String
operator|)
name|signatureEntry
operator|.
name|get
argument_list|(
literal|"signature"
argument_list|)
decl_stmt|;
return|return
operator|new
name|JwsJsonSignatureEntry
argument_list|(
name|encodedJwsPayload
argument_list|,
name|protectedHeader
argument_list|,
name|signature
argument_list|,
name|header
operator|!=
literal|null
condition|?
operator|new
name|JwsJsonUnprotectedHeader
argument_list|(
name|header
argument_list|)
else|:
literal|null
argument_list|)
return|;
block|}
specifier|public
name|String
name|getSignedDocument
parameter_list|()
block|{
return|return
name|this
operator|.
name|jwsSignedDocument
return|;
block|}
specifier|public
name|String
name|getEncodedJwsPayload
parameter_list|()
block|{
return|return
name|this
operator|.
name|encodedJwsPayload
return|;
block|}
specifier|public
name|String
name|getDecodedJwsPayload
parameter_list|()
block|{
return|return
name|JoseUtils
operator|.
name|decodeToString
argument_list|(
name|encodedJwsPayload
argument_list|)
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
name|List
argument_list|<
name|JwsJsonSignatureEntry
argument_list|>
name|getSignatureEntries
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|signatureEntries
argument_list|)
return|;
block|}
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|JwsJsonSignatureEntry
argument_list|>
name|getSignatureEntryMap
parameter_list|()
block|{
return|return
name|JwsUtils
operator|.
name|getJwsJsonSignatureMap
argument_list|(
name|signatureEntries
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
name|List
argument_list|<
name|JwsJsonSignatureEntry
argument_list|>
name|theSignatureEntries
init|=
name|getSignatureEntryMap
argument_list|()
operator|.
name|get
argument_list|(
name|validator
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|theSignatureEntries
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|JwsJsonSignatureEntry
name|signatureEntry
range|:
name|theSignatureEntries
control|)
block|{
if|if
condition|(
name|signatureEntry
operator|.
name|verifySignatureWith
argument_list|(
name|validator
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|verifySignatureWith
parameter_list|(
name|RSAPublicKey
name|key
parameter_list|,
name|String
name|algo
parameter_list|)
block|{
return|return
name|verifySignatureWith
argument_list|(
name|JwsUtils
operator|.
name|getRSAKeySignatureVerifier
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
name|String
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
name|verifySignatureWith
parameter_list|(
name|List
argument_list|<
name|JwsSignatureVerifier
argument_list|>
name|validators
parameter_list|)
block|{
try|try
block|{
name|verifyAndGetNonValidated
argument_list|(
name|validators
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|ex
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|JwsJsonSignatureEntry
argument_list|>
name|verifyAndGetNonValidated
parameter_list|(
name|List
argument_list|<
name|JwsSignatureVerifier
argument_list|>
name|validators
parameter_list|)
block|{
if|if
condition|(
name|validators
operator|.
name|size
argument_list|()
operator|>
name|signatureEntries
operator|.
name|size
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"Too many signature validators"
argument_list|)
throw|;
block|}
comment|// TODO: more effective approach is needed
name|List
argument_list|<
name|JwsJsonSignatureEntry
argument_list|>
name|validatedSignatures
init|=
operator|new
name|LinkedList
argument_list|<
name|JwsJsonSignatureEntry
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|JwsSignatureVerifier
name|validator
range|:
name|validators
control|)
block|{
name|boolean
name|validated
init|=
literal|false
decl_stmt|;
name|List
argument_list|<
name|JwsJsonSignatureEntry
argument_list|>
name|theSignatureEntries
init|=
name|getSignatureEntryMap
argument_list|()
operator|.
name|get
argument_list|(
name|validator
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|theSignatureEntries
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|JwsJsonSignatureEntry
name|sigEntry
range|:
name|theSignatureEntries
control|)
block|{
if|if
condition|(
name|sigEntry
operator|.
name|verifySignatureWith
argument_list|(
name|validator
argument_list|)
condition|)
block|{
name|validatedSignatures
operator|.
name|add
argument_list|(
name|sigEntry
argument_list|)
expr_stmt|;
name|validated
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|validated
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
block|}
if|if
condition|(
name|validatedSignatures
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
name|List
argument_list|<
name|JwsJsonSignatureEntry
argument_list|>
name|nonValidatedSignatures
init|=
operator|new
name|LinkedList
argument_list|<
name|JwsJsonSignatureEntry
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|JwsJsonSignatureEntry
name|sigEntry
range|:
name|signatureEntries
control|)
block|{
if|if
condition|(
operator|!
name|validatedSignatures
operator|.
name|contains
argument_list|(
name|sigEntry
argument_list|)
condition|)
block|{
name|nonValidatedSignatures
operator|.
name|add
argument_list|(
name|sigEntry
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|nonValidatedSignatures
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
name|String
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
name|JwsJsonProducer
name|toProducer
parameter_list|()
block|{
name|JwsJsonProducer
name|p
init|=
operator|new
name|JwsJsonProducer
argument_list|(
name|getDecodedJwsPayload
argument_list|()
argument_list|)
decl_stmt|;
name|p
operator|.
name|getSignatureEntries
argument_list|()
operator|.
name|addAll
argument_list|(
name|signatureEntries
argument_list|)
expr_stmt|;
return|return
name|p
return|;
block|}
block|}
end_class

end_unit

