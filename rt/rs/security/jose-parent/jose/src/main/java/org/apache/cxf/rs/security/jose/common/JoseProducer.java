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
name|JwsSignatureProvider
import|;
end_import

begin_class
specifier|public
class|class
name|JoseProducer
extends|extends
name|AbstractJoseProducer
block|{
specifier|public
name|String
name|processData
parameter_list|(
name|String
name|data
parameter_list|)
block|{
name|super
operator|.
name|checkProcessRequirements
argument_list|()
expr_stmt|;
name|JweEncryptionProvider
name|theEncProvider
init|=
literal|null
decl_stmt|;
name|JweHeaders
name|jweHeaders
init|=
operator|new
name|JweHeaders
argument_list|()
decl_stmt|;
if|if
condition|(
name|isJweRequired
argument_list|()
condition|)
block|{
name|theEncProvider
operator|=
name|getInitializedEncryptionProvider
argument_list|(
name|jweHeaders
argument_list|)
expr_stmt|;
if|if
condition|(
name|theEncProvider
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|JoseException
argument_list|(
literal|"Unable to encrypt the data"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|isJwsRequired
argument_list|()
condition|)
block|{
name|JwsHeaders
name|jwsHeaders
init|=
operator|new
name|JwsHeaders
argument_list|()
decl_stmt|;
name|JwsCompactProducer
name|jws
init|=
operator|new
name|JwsCompactProducer
argument_list|(
name|jwsHeaders
argument_list|,
name|data
argument_list|)
decl_stmt|;
name|JwsSignatureProvider
name|theSigProvider
init|=
name|getInitializedSignatureProvider
argument_list|(
name|jwsHeaders
argument_list|)
decl_stmt|;
if|if
condition|(
name|theSigProvider
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|JoseException
argument_list|(
literal|"Unable to sign the data"
argument_list|)
throw|;
block|}
name|data
operator|=
name|jws
operator|.
name|signWith
argument_list|(
name|theSigProvider
argument_list|)
expr_stmt|;
block|}
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
name|jweHeaders
argument_list|)
expr_stmt|;
block|}
return|return
name|data
return|;
block|}
block|}
end_class

end_unit

