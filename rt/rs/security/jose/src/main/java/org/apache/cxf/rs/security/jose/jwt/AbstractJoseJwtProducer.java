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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|AbstractJoseProducer
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
name|JoseException
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
name|JweJwtCompactProducer
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractJoseJwtProducer
extends|extends
name|AbstractJoseProducer
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
init|=
literal|true
decl_stmt|;
specifier|protected
name|String
name|processJwt
parameter_list|(
name|JwtToken
name|jwt
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
name|JoseException
argument_list|(
literal|"Unable to secure JWT"
argument_list|)
throw|;
block|}
name|String
name|data
init|=
literal|null
decl_stmt|;
name|JweEncryptionProvider
name|theEncProvider
init|=
name|getInitializedEncryptionProvider
argument_list|(
name|isJweRequired
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|isJwsRequired
argument_list|()
condition|)
block|{
name|JwsJwtCompactProducer
name|jws
init|=
operator|new
name|JwsJwtCompactProducer
argument_list|(
name|jwt
argument_list|)
decl_stmt|;
name|JwsSignatureProvider
name|theSigProvider
init|=
name|getInitializedSignatureProvider
argument_list|(
name|isJwsRequired
argument_list|()
argument_list|)
decl_stmt|;
name|data
operator|=
name|jws
operator|.
name|signWith
argument_list|(
name|theSigProvider
argument_list|)
expr_stmt|;
if|if
condition|(
name|theEncProvider
operator|!=
literal|null
condition|)
block|{
name|data
operator|=
name|theEncProvider
operator|.
name|encrypt
argument_list|(
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|data
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|JweJwtCompactProducer
name|jwe
init|=
operator|new
name|JweJwtCompactProducer
argument_list|(
name|jwt
argument_list|)
decl_stmt|;
name|data
operator|=
name|jwe
operator|.
name|encryptWith
argument_list|(
name|theEncProvider
argument_list|)
expr_stmt|;
block|}
return|return
name|data
return|;
block|}
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

