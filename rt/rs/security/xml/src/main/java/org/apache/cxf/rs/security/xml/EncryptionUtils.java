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
name|xml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|InvalidAlgorithmParameterException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|InvalidKeyException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Key
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
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
name|MGF1ParameterSpec
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|Cipher
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|spec
operator|.
name|OAEPParameterSpec
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|spec
operator|.
name|PSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|WSSecurityException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|util
operator|.
name|WSSecurityUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|encryption
operator|.
name|XMLCipher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|encryption
operator|.
name|XMLEncryptionException
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|EncryptionUtils
block|{
specifier|private
name|EncryptionUtils
parameter_list|()
block|{              }
specifier|public
specifier|static
name|Cipher
name|initCipherWithCert
parameter_list|(
name|String
name|keyEncAlgo
parameter_list|,
name|int
name|mode
parameter_list|,
name|X509Certificate
name|cert
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|Cipher
name|cipher
init|=
name|WSSecurityUtil
operator|.
name|getCipherInstance
argument_list|(
name|keyEncAlgo
argument_list|)
decl_stmt|;
try|try
block|{
name|OAEPParameterSpec
name|oaepParameterSpec
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|XMLCipher
operator|.
name|RSA_OAEP
operator|.
name|equals
argument_list|(
name|keyEncAlgo
argument_list|)
condition|)
block|{
name|oaepParameterSpec
operator|=
operator|new
name|OAEPParameterSpec
argument_list|(
literal|"SHA-1"
argument_list|,
literal|"MGF1"
argument_list|,
operator|new
name|MGF1ParameterSpec
argument_list|(
literal|"SHA-1"
argument_list|)
argument_list|,
name|PSource
operator|.
name|PSpecified
operator|.
name|DEFAULT
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|oaepParameterSpec
operator|==
literal|null
condition|)
block|{
name|cipher
operator|.
name|init
argument_list|(
name|mode
argument_list|,
name|cert
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cipher
operator|.
name|init
argument_list|(
name|mode
argument_list|,
name|cert
operator|.
name|getPublicKey
argument_list|()
argument_list|,
name|oaepParameterSpec
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InvalidKeyException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|FAILED_ENCRYPTION
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvalidAlgorithmParameterException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|FAILED_ENCRYPTION
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|cipher
return|;
block|}
specifier|public
specifier|static
name|Cipher
name|initCipherWithKey
parameter_list|(
name|String
name|keyEncAlgo
parameter_list|,
name|int
name|mode
parameter_list|,
name|Key
name|key
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|Cipher
name|cipher
init|=
name|WSSecurityUtil
operator|.
name|getCipherInstance
argument_list|(
name|keyEncAlgo
argument_list|)
decl_stmt|;
try|try
block|{
name|OAEPParameterSpec
name|oaepParameterSpec
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|XMLCipher
operator|.
name|RSA_OAEP
operator|.
name|equals
argument_list|(
name|keyEncAlgo
argument_list|)
condition|)
block|{
name|oaepParameterSpec
operator|=
operator|new
name|OAEPParameterSpec
argument_list|(
literal|"SHA-1"
argument_list|,
literal|"MGF1"
argument_list|,
operator|new
name|MGF1ParameterSpec
argument_list|(
literal|"SHA-1"
argument_list|)
argument_list|,
name|PSource
operator|.
name|PSpecified
operator|.
name|DEFAULT
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|oaepParameterSpec
operator|==
literal|null
condition|)
block|{
name|cipher
operator|.
name|init
argument_list|(
name|mode
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cipher
operator|.
name|init
argument_list|(
name|mode
argument_list|,
name|key
argument_list|,
name|oaepParameterSpec
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InvalidKeyException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|FAILED_ENCRYPTION
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvalidAlgorithmParameterException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|FAILED_ENCRYPTION
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|cipher
return|;
block|}
specifier|public
specifier|static
name|XMLCipher
name|initXMLCipher
parameter_list|(
name|String
name|symEncAlgo
parameter_list|,
name|int
name|mode
parameter_list|,
name|Key
name|key
parameter_list|)
throws|throws
name|WSSecurityException
block|{
try|try
block|{
name|XMLCipher
name|cipher
init|=
name|XMLCipher
operator|.
name|getInstance
argument_list|(
name|symEncAlgo
argument_list|)
decl_stmt|;
name|cipher
operator|.
name|setSecureValidation
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|cipher
operator|.
name|init
argument_list|(
name|mode
argument_list|,
name|key
argument_list|)
expr_stmt|;
return|return
name|cipher
return|;
block|}
catch|catch
parameter_list|(
name|XMLEncryptionException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|UNSUPPORTED_ALGORITHM
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

