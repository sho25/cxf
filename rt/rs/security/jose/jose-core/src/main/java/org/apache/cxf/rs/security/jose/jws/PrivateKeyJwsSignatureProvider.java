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
name|PrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|SecureRandom
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Signature
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|SignatureException
import|;
end_import

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
name|CryptoUtils
import|;
end_import

begin_class
specifier|public
class|class
name|PrivateKeyJwsSignatureProvider
extends|extends
name|AbstractJwsSignatureProvider
block|{
specifier|private
name|PrivateKey
name|key
decl_stmt|;
specifier|private
name|SecureRandom
name|random
decl_stmt|;
specifier|private
name|AlgorithmParameterSpec
name|signatureSpec
decl_stmt|;
specifier|public
name|PrivateKeyJwsSignatureProvider
parameter_list|(
name|PrivateKey
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
name|PrivateKeyJwsSignatureProvider
parameter_list|(
name|PrivateKey
name|key
parameter_list|,
name|AlgorithmParameterSpec
name|spec
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
name|spec
argument_list|,
name|algo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PrivateKeyJwsSignatureProvider
parameter_list|(
name|PrivateKey
name|key
parameter_list|,
name|SecureRandom
name|random
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
name|random
operator|=
name|random
expr_stmt|;
name|this
operator|.
name|signatureSpec
operator|=
name|spec
expr_stmt|;
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
name|Signature
name|s
init|=
name|CryptoUtils
operator|.
name|getSignature
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
name|random
argument_list|,
name|signatureSpec
argument_list|)
decl_stmt|;
return|return
name|doCreateJwsSignature
argument_list|(
name|s
argument_list|)
return|;
block|}
specifier|protected
name|JwsSignature
name|doCreateJwsSignature
parameter_list|(
name|Signature
name|s
parameter_list|)
block|{
return|return
operator|new
name|PrivateKeyJwsSignature
argument_list|(
name|s
argument_list|)
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
name|isRsaSign
argument_list|(
name|algo
argument_list|)
return|;
block|}
specifier|protected
specifier|static
class|class
name|PrivateKeyJwsSignature
implements|implements
name|JwsSignature
block|{
specifier|private
name|Signature
name|s
decl_stmt|;
specifier|public
name|PrivateKeyJwsSignature
parameter_list|(
name|Signature
name|s
parameter_list|)
block|{
name|this
operator|.
name|s
operator|=
name|s
expr_stmt|;
block|}
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
try|try
block|{
name|s
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
catch|catch
parameter_list|(
name|SignatureException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|JwsException
argument_list|(
name|JwsException
operator|.
name|Error
operator|.
name|SIGNATURE_FAILURE
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|sign
parameter_list|()
block|{
try|try
block|{
return|return
name|s
operator|.
name|sign
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|SignatureException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|JwsException
argument_list|(
name|JwsException
operator|.
name|Error
operator|.
name|SIGNATURE_FAILURE
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit
