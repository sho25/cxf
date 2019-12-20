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
name|spec
operator|.
name|AlgorithmParameterSpec
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|Mac
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
name|Base64Exception
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
name|rt
operator|.
name|security
operator|.
name|crypto
operator|.
name|HmacUtils
import|;
end_import

begin_class
specifier|public
class|class
name|HmacJwsSignatureProvider
extends|extends
name|AbstractJwsSignatureProvider
block|{
specifier|private
specifier|final
name|byte
index|[]
name|key
decl_stmt|;
specifier|private
specifier|final
name|AlgorithmParameterSpec
name|hmacSpec
decl_stmt|;
specifier|public
name|HmacJwsSignatureProvider
parameter_list|(
name|byte
index|[]
name|key
parameter_list|,
name|SignatureAlgorithm
name|algo
parameter_list|)
block|{
name|this
argument_list|(
name|key
argument_list|,
literal|null
argument_list|,
name|algo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|HmacJwsSignatureProvider
parameter_list|(
name|byte
index|[]
name|key
parameter_list|,
name|AlgorithmParameterSpec
name|spec
parameter_list|,
name|SignatureAlgorithm
name|algo
parameter_list|)
block|{
name|super
argument_list|(
name|algo
argument_list|)
expr_stmt|;
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|hmacSpec
operator|=
name|spec
expr_stmt|;
block|}
specifier|public
name|HmacJwsSignatureProvider
parameter_list|(
name|String
name|encodedKey
parameter_list|,
name|SignatureAlgorithm
name|algo
parameter_list|)
block|{
name|super
argument_list|(
name|algo
argument_list|)
expr_stmt|;
name|hmacSpec
operator|=
literal|null
expr_stmt|;
try|try
block|{
name|this
operator|.
name|key
operator|=
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|encodedKey
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Base64Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Hmac key can not be decoded"
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
name|INVALID_KEY
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|JwsSignature
name|doCreateJwsSignature
parameter_list|(
name|JwsHeaders
name|headers
parameter_list|)
block|{
specifier|final
name|String
name|sigAlgo
init|=
name|headers
operator|.
name|getSignatureAlgorithm
argument_list|()
operator|.
name|getJwaName
argument_list|()
decl_stmt|;
specifier|final
name|Mac
name|mac
init|=
name|HmacUtils
operator|.
name|getInitializedMac
argument_list|(
name|key
argument_list|,
name|AlgorithmUtils
operator|.
name|toJavaName
argument_list|(
name|sigAlgo
argument_list|)
argument_list|,
name|hmacSpec
argument_list|)
decl_stmt|;
return|return
operator|new
name|JwsSignature
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|update
parameter_list|(
name|byte
index|[]
name|src
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
block|{
name|mac
operator|.
name|update
argument_list|(
name|src
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|sign
parameter_list|()
block|{
return|return
name|mac
operator|.
name|doFinal
argument_list|()
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isValidAlgorithmFamily
parameter_list|(
name|String
name|algo
parameter_list|)
block|{
return|return
name|AlgorithmUtils
operator|.
name|isHmacSign
argument_list|(
name|algo
argument_list|)
return|;
block|}
block|}
end_class

end_unit

