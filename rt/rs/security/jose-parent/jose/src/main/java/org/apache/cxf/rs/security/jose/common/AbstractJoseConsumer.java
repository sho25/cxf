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
name|common
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
name|JweHeaders
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
name|AbstractJoseConsumer
block|{
specifier|private
name|JweDecryptionProvider
name|jweDecryptor
decl_stmt|;
specifier|private
name|JwsSignatureVerifier
name|jwsVerifier
decl_stmt|;
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
specifier|public
name|JweDecryptionProvider
name|getJweDecryptor
parameter_list|()
block|{
return|return
name|jweDecryptor
return|;
block|}
specifier|public
name|void
name|setJwsVerifier
parameter_list|(
name|JwsSignatureVerifier
name|theJwsVerifier
parameter_list|)
block|{
name|this
operator|.
name|jwsVerifier
operator|=
name|theJwsVerifier
expr_stmt|;
block|}
specifier|public
name|JwsSignatureVerifier
name|getJwsVerifier
parameter_list|()
block|{
return|return
name|jwsVerifier
return|;
block|}
specifier|protected
name|JweDecryptionProvider
name|getInitializedDecryptionProvider
parameter_list|(
name|JweHeaders
name|jweHeaders
parameter_list|)
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
name|jweHeaders
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|protected
name|JwsSignatureVerifier
name|getInitializedSignatureVerifier
parameter_list|(
name|JwsHeaders
name|jwsHeaders
parameter_list|)
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
name|jwsHeaders
argument_list|,
literal|false
argument_list|)
return|;
block|}
block|}
end_class

end_unit

