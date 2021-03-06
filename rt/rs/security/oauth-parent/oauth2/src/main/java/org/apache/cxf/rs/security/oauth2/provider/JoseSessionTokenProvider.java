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
name|provider
package|;
end_package

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
name|oauth2
operator|.
name|common
operator|.
name|OAuthRedirectionState
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
name|common
operator|.
name|UserSubject
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
name|OAuthUtils
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
name|crypto
operator|.
name|ModelEncryptionSupport
import|;
end_import

begin_class
specifier|public
class|class
name|JoseSessionTokenProvider
implements|implements
name|SessionAuthenticityTokenProvider
block|{
specifier|private
name|JwsSignatureProvider
name|jwsProvider
decl_stmt|;
specifier|private
name|JwsSignatureVerifier
name|jwsVerifier
decl_stmt|;
specifier|private
name|JweEncryptionProvider
name|jweEncryptor
decl_stmt|;
specifier|private
name|JweDecryptionProvider
name|jweDecryptor
decl_stmt|;
specifier|private
name|boolean
name|jwsRequired
decl_stmt|;
specifier|private
name|boolean
name|jweRequired
decl_stmt|;
specifier|private
name|int
name|maxDefaultSessionInterval
decl_stmt|;
annotation|@
name|Override
specifier|public
name|String
name|createSessionToken
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
name|params
parameter_list|,
name|UserSubject
name|subject
parameter_list|,
name|OAuthRedirectionState
name|secData
parameter_list|)
block|{
name|String
name|stateString
init|=
name|convertStateToString
argument_list|(
name|secData
argument_list|)
decl_stmt|;
name|String
name|sessionToken
init|=
name|protectStateString
argument_list|(
name|stateString
argument_list|)
decl_stmt|;
return|return
name|OAuthUtils
operator|.
name|setSessionToken
argument_list|(
name|mc
argument_list|,
name|sessionToken
argument_list|,
name|maxDefaultSessionInterval
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getSessionToken
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
name|params
parameter_list|,
name|UserSubject
name|subject
parameter_list|)
block|{
return|return
name|OAuthUtils
operator|.
name|getSessionToken
argument_list|(
name|mc
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|removeSessionToken
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
name|params
parameter_list|,
name|UserSubject
name|subject
parameter_list|)
block|{
return|return
name|getSessionToken
argument_list|(
name|mc
argument_list|,
name|params
argument_list|,
name|subject
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|OAuthRedirectionState
name|getSessionState
parameter_list|(
name|MessageContext
name|messageContext
parameter_list|,
name|String
name|sessionToken
parameter_list|,
name|UserSubject
name|subject
parameter_list|)
block|{
name|String
name|stateString
init|=
name|decryptStateString
argument_list|(
name|sessionToken
argument_list|)
decl_stmt|;
return|return
name|convertStateStringToState
argument_list|(
name|stateString
argument_list|)
return|;
block|}
specifier|public
name|void
name|setJwsProvider
parameter_list|(
name|JwsSignatureProvider
name|jwsProvider
parameter_list|)
block|{
name|this
operator|.
name|jwsProvider
operator|=
name|jwsProvider
expr_stmt|;
block|}
specifier|public
name|void
name|setJwsVerifier
parameter_list|(
name|JwsSignatureVerifier
name|jwsVerifier
parameter_list|)
block|{
name|this
operator|.
name|jwsVerifier
operator|=
name|jwsVerifier
expr_stmt|;
block|}
specifier|public
name|void
name|setJweEncryptor
parameter_list|(
name|JweEncryptionProvider
name|jweEncryptor
parameter_list|)
block|{
name|this
operator|.
name|jweEncryptor
operator|=
name|jweEncryptor
expr_stmt|;
block|}
specifier|public
name|void
name|setJweDecryptor
parameter_list|(
name|JweDecryptionProvider
name|jweDecryptor
parameter_list|)
block|{
name|this
operator|.
name|jweDecryptor
operator|=
name|jweDecryptor
expr_stmt|;
block|}
specifier|protected
name|JwsSignatureProvider
name|getInitializedSigProvider
parameter_list|()
block|{
if|if
condition|(
name|jwsProvider
operator|!=
literal|null
condition|)
block|{
return|return
name|jwsProvider
return|;
block|}
return|return
name|JwsUtils
operator|.
name|loadSignatureProvider
argument_list|(
name|jwsRequired
argument_list|)
return|;
block|}
specifier|protected
name|JweEncryptionProvider
name|getInitializedEncryptionProvider
parameter_list|()
block|{
if|if
condition|(
name|jweEncryptor
operator|!=
literal|null
condition|)
block|{
return|return
name|jweEncryptor
return|;
block|}
return|return
name|JweUtils
operator|.
name|loadEncryptionProvider
argument_list|(
name|jweRequired
argument_list|)
return|;
block|}
specifier|public
name|void
name|setJwsRequired
parameter_list|(
name|boolean
name|jwsRequired
parameter_list|)
block|{
name|this
operator|.
name|jwsRequired
operator|=
name|jwsRequired
expr_stmt|;
block|}
specifier|public
name|void
name|setJweRequired
parameter_list|(
name|boolean
name|jweRequired
parameter_list|)
block|{
name|this
operator|.
name|jweRequired
operator|=
name|jweRequired
expr_stmt|;
block|}
specifier|protected
name|JweDecryptionProvider
name|getInitializedDecryptionProvider
parameter_list|()
block|{
if|if
condition|(
name|jweDecryptor
operator|!=
literal|null
condition|)
block|{
return|return
name|jweDecryptor
return|;
block|}
return|return
name|JweUtils
operator|.
name|loadDecryptionProvider
argument_list|(
name|jweRequired
argument_list|)
return|;
block|}
specifier|protected
name|JwsSignatureVerifier
name|getInitializedSigVerifier
parameter_list|()
block|{
if|if
condition|(
name|jwsVerifier
operator|!=
literal|null
condition|)
block|{
return|return
name|jwsVerifier
return|;
block|}
return|return
name|JwsUtils
operator|.
name|loadSignatureVerifier
argument_list|(
name|jwsRequired
argument_list|)
return|;
block|}
specifier|private
name|String
name|decryptStateString
parameter_list|(
name|String
name|sessionToken
parameter_list|)
block|{
name|JweDecryptionProvider
name|jwe
init|=
name|getInitializedDecryptionProvider
argument_list|()
decl_stmt|;
name|String
name|stateString
init|=
name|jwe
operator|.
name|decrypt
argument_list|(
name|sessionToken
argument_list|)
operator|.
name|getContentText
argument_list|()
decl_stmt|;
name|JwsSignatureVerifier
name|jws
init|=
name|getInitializedSigVerifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|jws
operator|!=
literal|null
condition|)
block|{
name|stateString
operator|=
name|JwsUtils
operator|.
name|verify
argument_list|(
name|jws
argument_list|,
name|stateString
argument_list|)
operator|.
name|getDecodedJwsPayload
argument_list|()
expr_stmt|;
block|}
return|return
name|stateString
return|;
block|}
specifier|private
name|String
name|protectStateString
parameter_list|(
name|String
name|stateString
parameter_list|)
block|{
name|JwsSignatureProvider
name|jws
init|=
name|getInitializedSigProvider
argument_list|()
decl_stmt|;
name|JweEncryptionProvider
name|jwe
init|=
name|getInitializedEncryptionProvider
argument_list|()
decl_stmt|;
if|if
condition|(
name|jws
operator|==
literal|null
operator|&&
name|jwe
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"Session token can not be created"
argument_list|)
throw|;
block|}
if|if
condition|(
name|jws
operator|!=
literal|null
condition|)
block|{
name|stateString
operator|=
name|JwsUtils
operator|.
name|sign
argument_list|(
name|jws
argument_list|,
name|stateString
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jwe
operator|!=
literal|null
condition|)
block|{
name|stateString
operator|=
name|jwe
operator|.
name|encrypt
argument_list|(
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|stateString
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|stateString
return|;
block|}
specifier|private
name|OAuthRedirectionState
name|convertStateStringToState
parameter_list|(
name|String
name|stateString
parameter_list|)
block|{
name|String
index|[]
name|parts
init|=
name|ModelEncryptionSupport
operator|.
name|getParts
argument_list|(
name|stateString
argument_list|)
decl_stmt|;
name|OAuthRedirectionState
name|state
init|=
operator|new
name|OAuthRedirectionState
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|state
operator|.
name|setClientId
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|)
condition|)
block|{
name|state
operator|.
name|setAudience
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|parts
index|[
literal|2
index|]
argument_list|)
condition|)
block|{
name|state
operator|.
name|setClientCodeChallenge
argument_list|(
name|parts
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|parts
index|[
literal|3
index|]
argument_list|)
condition|)
block|{
name|state
operator|.
name|setState
argument_list|(
name|parts
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|parts
index|[
literal|4
index|]
argument_list|)
condition|)
block|{
name|state
operator|.
name|setProposedScope
argument_list|(
name|parts
index|[
literal|4
index|]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|parts
index|[
literal|5
index|]
argument_list|)
condition|)
block|{
name|state
operator|.
name|setRedirectUri
argument_list|(
name|parts
index|[
literal|5
index|]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|parts
index|[
literal|6
index|]
argument_list|)
condition|)
block|{
name|state
operator|.
name|setNonce
argument_list|(
name|parts
index|[
literal|6
index|]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|parts
index|[
literal|7
index|]
argument_list|)
condition|)
block|{
name|state
operator|.
name|setResponseType
argument_list|(
name|parts
index|[
literal|7
index|]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|parts
index|[
literal|8
index|]
argument_list|)
condition|)
block|{
name|state
operator|.
name|setExtraProperties
argument_list|(
name|ModelEncryptionSupport
operator|.
name|parseSimpleMap
argument_list|(
name|parts
index|[
literal|8
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|state
return|;
block|}
specifier|protected
name|String
name|convertStateToString
parameter_list|(
name|OAuthRedirectionState
name|secData
parameter_list|)
block|{
comment|//TODO: make it simpler, convert it to JwtClaims -> JSON
name|StringBuilder
name|state
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// 0: client id
name|state
operator|.
name|append
argument_list|(
name|ModelEncryptionSupport
operator|.
name|tokenizeString
argument_list|(
name|secData
operator|.
name|getClientId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|append
argument_list|(
name|ModelEncryptionSupport
operator|.
name|SEP
argument_list|)
expr_stmt|;
comment|// 1: client audience
name|state
operator|.
name|append
argument_list|(
name|ModelEncryptionSupport
operator|.
name|tokenizeString
argument_list|(
name|secData
operator|.
name|getAudience
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|append
argument_list|(
name|ModelEncryptionSupport
operator|.
name|SEP
argument_list|)
expr_stmt|;
comment|// 2: client code verifier
name|state
operator|.
name|append
argument_list|(
name|ModelEncryptionSupport
operator|.
name|tokenizeString
argument_list|(
name|secData
operator|.
name|getClientCodeChallenge
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|append
argument_list|(
name|ModelEncryptionSupport
operator|.
name|SEP
argument_list|)
expr_stmt|;
comment|// 3: state
name|state
operator|.
name|append
argument_list|(
name|ModelEncryptionSupport
operator|.
name|tokenizeString
argument_list|(
name|secData
operator|.
name|getState
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|append
argument_list|(
name|ModelEncryptionSupport
operator|.
name|SEP
argument_list|)
expr_stmt|;
comment|// 4: scope
name|state
operator|.
name|append
argument_list|(
name|ModelEncryptionSupport
operator|.
name|tokenizeString
argument_list|(
name|secData
operator|.
name|getProposedScope
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|append
argument_list|(
name|ModelEncryptionSupport
operator|.
name|SEP
argument_list|)
expr_stmt|;
comment|// 5: redirect uri
name|state
operator|.
name|append
argument_list|(
name|ModelEncryptionSupport
operator|.
name|tokenizeString
argument_list|(
name|secData
operator|.
name|getRedirectUri
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|append
argument_list|(
name|ModelEncryptionSupport
operator|.
name|SEP
argument_list|)
expr_stmt|;
comment|// 6: nonce
name|state
operator|.
name|append
argument_list|(
name|ModelEncryptionSupport
operator|.
name|tokenizeString
argument_list|(
name|secData
operator|.
name|getNonce
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|append
argument_list|(
name|ModelEncryptionSupport
operator|.
name|SEP
argument_list|)
expr_stmt|;
comment|// 7: response_type
name|state
operator|.
name|append
argument_list|(
name|ModelEncryptionSupport
operator|.
name|tokenizeString
argument_list|(
name|secData
operator|.
name|getResponseType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|append
argument_list|(
name|ModelEncryptionSupport
operator|.
name|SEP
argument_list|)
expr_stmt|;
comment|// 8: extra props
name|state
operator|.
name|append
argument_list|(
name|secData
operator|.
name|getExtraProperties
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|state
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|void
name|setMaxDefaultSessionInterval
parameter_list|(
name|int
name|maxDefaultSessionInterval
parameter_list|)
block|{
name|this
operator|.
name|maxDefaultSessionInterval
operator|=
name|maxDefaultSessionInterval
expr_stmt|;
block|}
block|}
end_class

end_unit

