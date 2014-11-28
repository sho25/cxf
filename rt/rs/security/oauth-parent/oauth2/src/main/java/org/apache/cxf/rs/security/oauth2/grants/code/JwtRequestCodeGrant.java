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
name|grants
operator|.
name|code
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|rs
operator|.
name|security
operator|.
name|jose
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
name|JwsJwtCompactProducer
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
name|JwtClaims
import|;
end_import

begin_comment
comment|/**  * Base Authorization Code Grant representation, captures the code   * and the redirect URI this code has been returned to, visible to the client  */
end_comment

begin_class
specifier|public
class|class
name|JwtRequestCodeGrant
extends|extends
name|AuthorizationCodeGrant
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|3738825769770411453L
decl_stmt|;
specifier|private
name|JwsSignatureProvider
name|sigProvider
decl_stmt|;
specifier|private
name|JweEncryptionProvider
name|encryptionProvider
decl_stmt|;
comment|// can be a client id
specifier|private
name|String
name|issuer
decl_stmt|;
specifier|public
name|JwtRequestCodeGrant
parameter_list|()
block|{     }
specifier|public
name|JwtRequestCodeGrant
parameter_list|(
name|String
name|issuer
parameter_list|)
block|{
name|this
operator|.
name|issuer
operator|=
name|issuer
expr_stmt|;
block|}
specifier|public
name|JwtRequestCodeGrant
parameter_list|(
name|String
name|code
parameter_list|,
name|String
name|issuer
parameter_list|)
block|{
name|super
argument_list|(
name|code
argument_list|)
expr_stmt|;
name|this
operator|.
name|issuer
operator|=
name|issuer
expr_stmt|;
block|}
specifier|public
name|JwtRequestCodeGrant
parameter_list|(
name|String
name|code
parameter_list|,
name|URI
name|uri
parameter_list|,
name|String
name|issuer
parameter_list|)
block|{
name|super
argument_list|(
name|code
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|this
operator|.
name|issuer
operator|=
name|issuer
expr_stmt|;
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
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|)
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
literal|true
argument_list|)
decl_stmt|;
name|headers
operator|.
name|setAlgorithm
argument_list|(
name|theSigProvider
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|theSigProvider
return|;
block|}
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|toMap
parameter_list|()
block|{
name|String
name|request
init|=
name|getRequest
argument_list|()
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|newMap
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
name|newMap
operator|.
name|putSingle
argument_list|(
literal|"request"
argument_list|,
name|request
argument_list|)
expr_stmt|;
return|return
name|newMap
return|;
block|}
specifier|public
name|String
name|getRequest
parameter_list|()
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|super
operator|.
name|toMap
argument_list|()
decl_stmt|;
name|JwtClaims
name|claims
init|=
operator|new
name|JwtClaims
argument_list|()
decl_stmt|;
name|claims
operator|.
name|setIssuer
argument_list|(
name|issuer
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|key
range|:
name|map
operator|.
name|keySet
argument_list|()
control|)
block|{
name|claims
operator|.
name|setClaim
argument_list|(
name|key
argument_list|,
name|map
operator|.
name|getFirst
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|JwsJwtCompactProducer
name|producer
init|=
operator|new
name|JwsJwtCompactProducer
argument_list|(
name|claims
argument_list|)
decl_stmt|;
name|JoseHeaders
name|headers
init|=
operator|new
name|JoseHeaders
argument_list|()
decl_stmt|;
name|JwsSignatureProvider
name|theSigProvider
init|=
name|getInitializedSigProvider
argument_list|(
name|headers
argument_list|)
decl_stmt|;
name|String
name|request
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
name|request
operator|=
name|theEncryptionProvider
operator|.
name|encrypt
argument_list|(
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|request
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|request
return|;
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
specifier|public
name|void
name|setIssuer
parameter_list|(
name|String
name|issuer
parameter_list|)
block|{
name|this
operator|.
name|issuer
operator|=
name|issuer
expr_stmt|;
block|}
block|}
end_class

end_unit

