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
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|jwt
operator|.
name|Algorithm
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
name|jwt
operator|.
name|JwtHeaders
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
name|oauth2
operator|.
name|utils
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
implements|implements
name|JwsSignatureVerifier
block|{
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|SUPPORTED_ALGORITHMS
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Algorithm
operator|.
name|HmacSHA256
operator|.
name|getJwtName
argument_list|()
argument_list|,
name|Algorithm
operator|.
name|HmacSHA384
operator|.
name|getJwtName
argument_list|()
argument_list|,
name|Algorithm
operator|.
name|HmacSHA512
operator|.
name|getJwtName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
name|byte
index|[]
name|key
decl_stmt|;
specifier|private
name|AlgorithmParameterSpec
name|hmacSpec
decl_stmt|;
specifier|public
name|HmacJwsSignatureProvider
parameter_list|(
name|byte
index|[]
name|key
parameter_list|)
block|{
name|this
argument_list|(
name|key
argument_list|,
literal|null
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
parameter_list|)
block|{
name|super
argument_list|(
name|SUPPORTED_ALGORITHMS
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
parameter_list|)
block|{
name|super
argument_list|(
name|SUPPORTED_ALGORITHMS
argument_list|)
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
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|verify
parameter_list|(
name|JwtHeaders
name|headers
parameter_list|,
name|String
name|unsignedText
parameter_list|,
name|byte
index|[]
name|signature
parameter_list|)
block|{
name|byte
index|[]
name|expected
init|=
name|computeMac
argument_list|(
name|headers
argument_list|,
name|unsignedText
argument_list|)
decl_stmt|;
return|return
name|Arrays
operator|.
name|equals
argument_list|(
name|expected
argument_list|,
name|signature
argument_list|)
return|;
block|}
specifier|private
name|byte
index|[]
name|computeMac
parameter_list|(
name|JwtHeaders
name|headers
parameter_list|,
name|String
name|text
parameter_list|)
block|{
return|return
name|HmacUtils
operator|.
name|computeHmac
argument_list|(
name|key
argument_list|,
name|Algorithm
operator|.
name|toJavaName
argument_list|(
name|headers
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
argument_list|,
name|hmacSpec
argument_list|,
name|text
argument_list|)
return|;
block|}
specifier|protected
name|JwsSignature
name|doCreateJwsSignature
parameter_list|(
name|JwtHeaders
name|headers
parameter_list|)
block|{
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
name|Algorithm
operator|.
name|toJavaName
argument_list|(
name|headers
operator|.
name|getAlgorithm
argument_list|()
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
block|}
end_class

end_unit

