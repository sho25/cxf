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
name|io
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
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
name|util
operator|.
name|Arrays
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
name|KeyGenerator
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
name|javax
operator|.
name|crypto
operator|.
name|spec
operator|.
name|IvParameterSpec
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|DestroyFailedException
import|;
end_import

begin_comment
comment|/**  * A class to hold a pair of encryption and decryption ciphers.  */
end_comment

begin_class
specifier|public
class|class
name|CipherPair
block|{
specifier|private
name|String
name|transformation
decl_stmt|;
specifier|private
name|Cipher
name|enccipher
decl_stmt|;
specifier|private
name|SecretKey
name|key
decl_stmt|;
specifier|private
name|byte
index|[]
name|ivp
decl_stmt|;
specifier|public
name|CipherPair
parameter_list|(
name|String
name|transformation
parameter_list|)
throws|throws
name|GeneralSecurityException
block|{
name|this
operator|.
name|transformation
operator|=
name|transformation
expr_stmt|;
name|int
name|d
init|=
name|transformation
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|String
name|a
decl_stmt|;
if|if
condition|(
name|d
operator|>
literal|0
condition|)
block|{
name|a
operator|=
name|transformation
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|d
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|a
operator|=
name|transformation
expr_stmt|;
block|}
try|try
block|{
name|KeyGenerator
name|keygen
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|keygen
operator|.
name|init
argument_list|(
operator|new
name|SecureRandom
argument_list|()
argument_list|)
expr_stmt|;
name|key
operator|=
name|keygen
operator|.
name|generateKey
argument_list|()
expr_stmt|;
name|enccipher
operator|=
name|Cipher
operator|.
name|getInstance
argument_list|(
name|transformation
argument_list|)
expr_stmt|;
name|enccipher
operator|.
name|init
argument_list|(
name|Cipher
operator|.
name|ENCRYPT_MODE
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|ivp
operator|=
name|enccipher
operator|.
name|getIV
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GeneralSecurityException
name|e
parameter_list|)
block|{
name|enccipher
operator|=
literal|null
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
specifier|public
name|String
name|getTransformation
parameter_list|()
block|{
return|return
name|transformation
return|;
block|}
specifier|public
name|Cipher
name|getEncryptor
parameter_list|()
block|{
return|return
name|enccipher
return|;
block|}
specifier|public
name|Cipher
name|getDecryptor
parameter_list|()
block|{
name|Cipher
name|deccipher
init|=
literal|null
decl_stmt|;
try|try
block|{
name|deccipher
operator|=
name|Cipher
operator|.
name|getInstance
argument_list|(
name|transformation
argument_list|)
expr_stmt|;
name|deccipher
operator|.
name|init
argument_list|(
name|Cipher
operator|.
name|DECRYPT_MODE
argument_list|,
name|key
argument_list|,
name|ivp
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|IvParameterSpec
argument_list|(
name|ivp
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GeneralSecurityException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
return|return
name|deccipher
return|;
block|}
specifier|public
name|void
name|clean
parameter_list|()
block|{
if|if
condition|(
name|ivp
operator|!=
literal|null
condition|)
block|{
name|Arrays
operator|.
name|fill
argument_list|(
name|ivp
argument_list|,
operator|(
name|byte
operator|)
literal|0
argument_list|)
expr_stmt|;
block|}
comment|// Clean the key after we're done with it
try|try
block|{
name|key
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DestroyFailedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
end_class

end_unit

