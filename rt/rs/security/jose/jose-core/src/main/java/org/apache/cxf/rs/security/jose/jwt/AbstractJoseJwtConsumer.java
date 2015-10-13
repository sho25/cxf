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
name|jwt
package|;
end_package

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
name|AbstractJoseConsumer
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
name|JweJwtCompactConsumer
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
name|JwsHeaders
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
name|JwsJwtCompactConsumer
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractJoseJwtConsumer
extends|extends
name|AbstractJoseConsumer
block|{
specifier|private
name|boolean
name|jwsRequired
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|jweRequired
decl_stmt|;
specifier|protected
name|JwtToken
name|getJwtToken
parameter_list|(
name|String
name|wrappedJwtToken
parameter_list|)
block|{
return|return
name|getJwtToken
argument_list|(
name|wrappedJwtToken
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|protected
name|JwtToken
name|getJwtToken
parameter_list|(
name|String
name|wrappedJwtToken
parameter_list|,
name|JweDecryptionProvider
name|jweDecryptor
parameter_list|,
name|JwsSignatureVerifier
name|theSigVerifier
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isJwsRequired
argument_list|()
operator|&&
operator|!
name|isJweRequired
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|JwtException
argument_list|(
literal|"Unable to process JWT"
argument_list|)
throw|;
block|}
if|if
condition|(
name|isJweRequired
argument_list|()
condition|)
block|{
if|if
condition|(
name|jweDecryptor
operator|==
literal|null
condition|)
block|{
name|jweDecryptor
operator|=
name|getInitializedDecryptionProvider
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|jweDecryptor
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|JwtException
argument_list|(
literal|"Unable to decrypt JWT"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|isJwsRequired
argument_list|()
condition|)
block|{
return|return
operator|new
name|JweJwtCompactConsumer
argument_list|(
name|wrappedJwtToken
argument_list|)
operator|.
name|decryptWith
argument_list|(
name|jweDecryptor
argument_list|)
return|;
block|}
name|wrappedJwtToken
operator|=
name|jweDecryptor
operator|.
name|decrypt
argument_list|(
name|wrappedJwtToken
argument_list|)
operator|.
name|getContentText
argument_list|()
expr_stmt|;
block|}
name|JwsJwtCompactConsumer
name|jwtConsumer
init|=
operator|new
name|JwsJwtCompactConsumer
argument_list|(
name|wrappedJwtToken
argument_list|)
decl_stmt|;
name|JwtToken
name|jwt
init|=
name|jwtConsumer
operator|.
name|getJwtToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|isJwsRequired
argument_list|()
condition|)
block|{
if|if
condition|(
name|theSigVerifier
operator|==
literal|null
condition|)
block|{
name|theSigVerifier
operator|=
name|getInitializedSignatureVerifier
argument_list|(
name|jwt
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|theSigVerifier
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|JwtException
argument_list|(
literal|"Unable to validate JWT"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|jwtConsumer
operator|.
name|verifySignatureWith
argument_list|(
name|theSigVerifier
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|JwtException
argument_list|(
literal|"Invalid Signature"
argument_list|)
throw|;
block|}
block|}
name|validateToken
argument_list|(
name|jwt
argument_list|)
expr_stmt|;
return|return
name|jwt
return|;
block|}
specifier|protected
name|JwsSignatureVerifier
name|getInitializedSignatureVerifier
parameter_list|(
name|JwtToken
name|jwt
parameter_list|)
block|{
if|if
condition|(
name|super
operator|.
name|getJwsVerifier
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|super
operator|.
name|getJwsVerifier
argument_list|()
return|;
block|}
if|if
condition|(
name|jwt
operator|.
name|getHeaders
argument_list|()
operator|instanceof
name|JwsHeaders
condition|)
block|{
return|return
name|JwsUtils
operator|.
name|loadSignatureVerifier
argument_list|(
operator|(
name|JwsHeaders
operator|)
name|jwt
operator|.
name|getHeaders
argument_list|()
argument_list|,
literal|false
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|getInitializedSignatureVerifier
argument_list|()
return|;
block|}
specifier|protected
name|void
name|validateToken
parameter_list|(
name|JwtToken
name|jwt
parameter_list|)
block|{     }
specifier|public
name|boolean
name|isJwsRequired
parameter_list|()
block|{
return|return
name|jwsRequired
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
name|boolean
name|isJweRequired
parameter_list|()
block|{
return|return
name|jweRequired
return|;
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
block|}
end_class

end_unit

