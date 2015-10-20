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
name|PublicKey
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|logging
operator|.
name|LogUtils
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
name|PublicKeyJwsSignatureVerifier
implements|implements
name|JwsSignatureVerifier
block|{
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|PublicKeyJwsSignatureVerifier
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|PublicKey
name|key
decl_stmt|;
specifier|private
name|AlgorithmParameterSpec
name|signatureSpec
decl_stmt|;
specifier|private
name|SignatureAlgorithm
name|supportedAlgo
decl_stmt|;
specifier|public
name|PublicKeyJwsSignatureVerifier
parameter_list|(
name|PublicKey
name|key
parameter_list|,
name|SignatureAlgorithm
name|supportedAlgorithm
parameter_list|)
block|{
name|this
argument_list|(
name|key
argument_list|,
literal|null
argument_list|,
name|supportedAlgorithm
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PublicKeyJwsSignatureVerifier
parameter_list|(
name|PublicKey
name|key
parameter_list|,
name|AlgorithmParameterSpec
name|spec
parameter_list|,
name|SignatureAlgorithm
name|supportedAlgo
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|signatureSpec
operator|=
name|spec
expr_stmt|;
name|this
operator|.
name|supportedAlgo
operator|=
name|supportedAlgo
expr_stmt|;
name|JwsUtils
operator|.
name|checkSignatureKeySize
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|verify
parameter_list|(
name|JwsHeaders
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
try|try
block|{
return|return
name|CryptoUtils
operator|.
name|verifySignature
argument_list|(
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|unsignedText
argument_list|)
argument_list|,
name|signature
argument_list|,
name|key
argument_list|,
name|AlgorithmUtils
operator|.
name|toJavaName
argument_list|(
name|checkAlgorithm
argument_list|(
name|headers
operator|.
name|getSignatureAlgorithm
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|signatureSpec
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Invalid signature: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
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
name|INVALID_SIGNATURE
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|String
name|checkAlgorithm
parameter_list|(
name|SignatureAlgorithm
name|sigAlgo
parameter_list|)
block|{
name|String
name|algo
init|=
name|sigAlgo
operator|.
name|getJwaName
argument_list|()
decl_stmt|;
if|if
condition|(
name|algo
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Signature algorithm is not set"
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
name|ALGORITHM_NOT_SET
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|isValidAlgorithmFamily
argument_list|(
name|algo
argument_list|)
operator|||
operator|!
name|algo
operator|.
name|equals
argument_list|(
name|supportedAlgo
operator|.
name|getJwaName
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Invalid signature algorithm: "
operator|+
name|algo
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
name|INVALID_ALGORITHM
argument_list|)
throw|;
block|}
return|return
name|algo
return|;
block|}
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
annotation|@
name|Override
specifier|public
name|SignatureAlgorithm
name|getAlgorithm
parameter_list|()
block|{
return|return
name|supportedAlgo
return|;
block|}
block|}
end_class

end_unit

