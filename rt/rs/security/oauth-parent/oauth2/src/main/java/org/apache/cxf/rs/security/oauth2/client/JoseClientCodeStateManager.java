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
name|client
package|;
end_package

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
name|ext
operator|.
name|MessageContext
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
name|impl
operator|.
name|MetadataMap
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
name|jwe
operator|.
name|JweDecryptionProvider
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
name|jwe
operator|.
name|JweEncryptionProvider
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
name|jwe
operator|.
name|JweUtils
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
name|jws
operator|.
name|JwsCompactConsumer
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
name|jws
operator|.
name|JwsCompactProducer
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
name|jws
operator|.
name|JwsSignatureProvider
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
name|jws
operator|.
name|JwsSignatureVerifier
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
name|jws
operator|.
name|JwsUtils
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
name|jws
operator|.
name|NoneJwsSignatureProvider
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
name|OAuthConstants
import|;
end_import

begin_class
specifier|public
class|class
name|JoseClientCodeStateManager
implements|implements
name|ClientCodeStateManager
block|{
specifier|private
name|JwsSignatureProvider
name|sigProvider
decl_stmt|;
specifier|private
name|JweEncryptionProvider
name|encryptionProvider
decl_stmt|;
specifier|private
name|JweDecryptionProvider
name|decryptionProvider
decl_stmt|;
specifier|private
name|JwsSignatureVerifier
name|signatureVerifier
decl_stmt|;
specifier|private
name|JsonMapObjectReaderWriter
name|jsonp
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|toRedirectState
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|requestState
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|stateMap
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
name|requestState
argument_list|)
decl_stmt|;
name|String
name|json
init|=
name|jsonp
operator|.
name|toJson
argument_list|(
name|stateMap
argument_list|)
decl_stmt|;
name|JwsCompactProducer
name|producer
init|=
operator|new
name|JwsCompactProducer
argument_list|(
name|json
argument_list|)
decl_stmt|;
name|JwsSignatureProvider
name|theSigProvider
init|=
name|getInitializedSigProvider
argument_list|()
decl_stmt|;
name|String
name|stateParam
init|=
name|producer
operator|.
name|signWith
argument_list|(
name|theSigProvider
argument_list|)
decl_stmt|;
name|JweEncryptionProvider
name|theEncryptionProvider
init|=
name|getInitializedEncryptionProvider
argument_list|()
decl_stmt|;
if|if
condition|(
name|theEncryptionProvider
operator|!=
literal|null
condition|)
block|{
name|stateParam
operator|=
name|theEncryptionProvider
operator|.
name|encrypt
argument_list|(
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|stateParam
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|putSingle
argument_list|(
name|OAuthConstants
operator|.
name|STATE
argument_list|,
name|stateParam
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
annotation|@
name|Override
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fromRedirectState
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|redirectState
parameter_list|)
block|{
name|String
name|stateParam
init|=
name|redirectState
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|STATE
argument_list|)
decl_stmt|;
name|JweDecryptionProvider
name|jwe
init|=
name|getInitializedDecryptionProvider
argument_list|()
decl_stmt|;
if|if
condition|(
name|jwe
operator|!=
literal|null
condition|)
block|{
name|stateParam
operator|=
name|jwe
operator|.
name|decrypt
argument_list|(
name|stateParam
argument_list|)
operator|.
name|getContentText
argument_list|()
expr_stmt|;
block|}
name|JwsCompactConsumer
name|jws
init|=
operator|new
name|JwsCompactConsumer
argument_list|(
name|stateParam
argument_list|)
decl_stmt|;
name|JwsSignatureVerifier
name|theSigVerifier
init|=
name|getInitializedSigVerifier
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|jws
operator|.
name|verifySignatureWith
argument_list|(
name|theSigVerifier
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
name|String
name|json
init|=
name|jws
operator|.
name|getUnsignedEncodedSequence
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|map
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
name|jsonp
operator|.
name|fromJson
argument_list|(
name|json
argument_list|)
argument_list|)
decl_stmt|;
comment|//CHECKSTYLE:OFF
return|return
operator|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|map
return|;
comment|//CHECKSTYLE:ON
block|}
specifier|public
name|void
name|setSignatureProvider
parameter_list|(
name|JwsSignatureProvider
name|signatureProvider
parameter_list|)
block|{
name|this
operator|.
name|sigProvider
operator|=
name|signatureProvider
expr_stmt|;
block|}
specifier|protected
name|JwsSignatureProvider
name|getInitializedSigProvider
parameter_list|()
block|{
if|if
condition|(
name|sigProvider
operator|!=
literal|null
condition|)
block|{
return|return
name|sigProvider
return|;
block|}
name|JwsSignatureProvider
name|theSigProvider
init|=
name|JwsUtils
operator|.
name|loadSignatureProvider
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|theSigProvider
operator|==
literal|null
condition|)
block|{
name|theSigProvider
operator|=
operator|new
name|NoneJwsSignatureProvider
argument_list|()
expr_stmt|;
block|}
return|return
name|theSigProvider
return|;
block|}
specifier|public
name|void
name|setDecryptionProvider
parameter_list|(
name|JweDecryptionProvider
name|decProvider
parameter_list|)
block|{
name|this
operator|.
name|decryptionProvider
operator|=
name|decProvider
expr_stmt|;
block|}
specifier|protected
name|JweDecryptionProvider
name|getInitializedDecryptionProvider
parameter_list|()
block|{
if|if
condition|(
name|decryptionProvider
operator|!=
literal|null
condition|)
block|{
return|return
name|decryptionProvider
return|;
block|}
return|return
name|JweUtils
operator|.
name|loadDecryptionProvider
argument_list|(
literal|false
argument_list|)
return|;
block|}
specifier|public
name|void
name|setSignatureVerifier
parameter_list|(
name|JwsSignatureVerifier
name|signatureVerifier
parameter_list|)
block|{
name|this
operator|.
name|signatureVerifier
operator|=
name|signatureVerifier
expr_stmt|;
block|}
specifier|protected
name|JwsSignatureVerifier
name|getInitializedSigVerifier
parameter_list|()
block|{
if|if
condition|(
name|signatureVerifier
operator|!=
literal|null
condition|)
block|{
return|return
name|signatureVerifier
return|;
block|}
return|return
name|JwsUtils
operator|.
name|loadSignatureVerifier
argument_list|(
literal|false
argument_list|)
return|;
block|}
specifier|public
name|void
name|setEncryptionProvider
parameter_list|(
name|JweEncryptionProvider
name|encProvider
parameter_list|)
block|{
name|this
operator|.
name|encryptionProvider
operator|=
name|encProvider
expr_stmt|;
block|}
specifier|protected
name|JweEncryptionProvider
name|getInitializedEncryptionProvider
parameter_list|()
block|{
if|if
condition|(
name|encryptionProvider
operator|!=
literal|null
condition|)
block|{
return|return
name|encryptionProvider
return|;
block|}
return|return
name|JweUtils
operator|.
name|loadEncryptionProvider
argument_list|(
literal|false
argument_list|)
return|;
block|}
block|}
end_class

end_unit

