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
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|SecretKey
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
name|AlgorithmUtils
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
name|ContentAlgorithm
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
name|jwt
operator|.
name|AbstractJoseJwtConsumer
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
name|jwt
operator|.
name|JwtToken
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|rt
operator|.
name|security
operator|.
name|crypto
operator|.
name|CryptoUtils
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractOAuthJoseJwtConsumer
extends|extends
name|AbstractJoseJwtConsumer
block|{
specifier|private
name|boolean
name|decryptWithClientSecret
decl_stmt|;
specifier|private
name|boolean
name|verifyWithClientSecret
decl_stmt|;
specifier|protected
name|JwtToken
name|getJwtToken
parameter_list|(
name|String
name|wrappedJwtToken
parameter_list|,
name|String
name|clientSecret
parameter_list|)
block|{
return|return
name|getJwtToken
argument_list|(
name|wrappedJwtToken
argument_list|,
name|getInitializedDecryptionProvider
argument_list|(
name|clientSecret
argument_list|)
argument_list|,
name|getInitializedSignatureVerifier
argument_list|(
name|clientSecret
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|JwsSignatureVerifier
name|getInitializedSignatureVerifier
parameter_list|(
name|String
name|clientSecret
parameter_list|)
block|{
if|if
condition|(
name|verifyWithClientSecret
condition|)
block|{
name|Properties
name|props
init|=
name|JwsUtils
operator|.
name|loadSignatureInProperties
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|SignatureAlgorithm
name|sigAlgo
init|=
name|SignatureAlgorithm
operator|.
name|getAlgorithm
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_SECRET_SIGNATURE_ALGORITHM
argument_list|)
argument_list|)
decl_stmt|;
name|sigAlgo
operator|=
name|sigAlgo
operator|!=
literal|null
condition|?
name|sigAlgo
else|:
name|SignatureAlgorithm
operator|.
name|HS256
expr_stmt|;
if|if
condition|(
name|AlgorithmUtils
operator|.
name|isHmacSign
argument_list|(
name|sigAlgo
argument_list|)
condition|)
block|{
return|return
name|JwsUtils
operator|.
name|getHmacSignatureVerifier
argument_list|(
name|clientSecret
argument_list|,
name|sigAlgo
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|JweDecryptionProvider
name|getInitializedDecryptionProvider
parameter_list|(
name|String
name|clientSecret
parameter_list|)
block|{
name|JweDecryptionProvider
name|theDecryptionProvider
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|decryptWithClientSecret
condition|)
block|{
name|SecretKey
name|key
init|=
name|CryptoUtils
operator|.
name|decodeSecretKey
argument_list|(
name|clientSecret
argument_list|)
decl_stmt|;
name|Properties
name|props
init|=
name|JweUtils
operator|.
name|loadEncryptionInProperties
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|ContentAlgorithm
name|ctAlgo
init|=
name|ContentAlgorithm
operator|.
name|getAlgorithm
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_SECRET_ENCRYPTION_ALGORITHM
argument_list|)
argument_list|)
decl_stmt|;
name|ctAlgo
operator|=
name|ctAlgo
operator|!=
literal|null
condition|?
name|ctAlgo
else|:
name|ContentAlgorithm
operator|.
name|A128GCM
expr_stmt|;
name|theDecryptionProvider
operator|=
name|JweUtils
operator|.
name|getDirectKeyJweDecryption
argument_list|(
name|key
argument_list|,
name|ctAlgo
argument_list|)
expr_stmt|;
block|}
return|return
name|theDecryptionProvider
return|;
block|}
specifier|public
name|void
name|setDecryptWithClientSecret
parameter_list|(
name|boolean
name|decryptWithClientSecret
parameter_list|)
block|{
name|this
operator|.
name|decryptWithClientSecret
operator|=
name|verifyWithClientSecret
expr_stmt|;
block|}
specifier|public
name|void
name|setVerifyWithClientSecret
parameter_list|(
name|boolean
name|verifyWithClientSecret
parameter_list|)
block|{
name|this
operator|.
name|verifyWithClientSecret
operator|=
name|verifyWithClientSecret
expr_stmt|;
block|}
block|}
end_class

end_unit

