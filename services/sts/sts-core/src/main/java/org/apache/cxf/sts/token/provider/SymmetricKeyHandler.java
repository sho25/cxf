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
name|sts
operator|.
name|token
operator|.
name|provider
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
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
name|sts
operator|.
name|STSConstants
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
name|sts
operator|.
name|STSPropertiesMBean
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
name|sts
operator|.
name|SignatureProperties
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
name|sts
operator|.
name|request
operator|.
name|BinarySecret
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
name|sts
operator|.
name|request
operator|.
name|Entropy
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
name|sts
operator|.
name|request
operator|.
name|KeyRequirements
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
name|ws
operator|.
name|security
operator|.
name|sts
operator|.
name|provider
operator|.
name|STSException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|WSS4JConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|derivedKey
operator|.
name|P_SHA1
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|ext
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
name|wss4j
operator|.
name|dom
operator|.
name|util
operator|.
name|WSSecurityUtil
import|;
end_import

begin_comment
comment|/**  * Some common functionality relating to parsing and generating Symmetric Keys.  */
end_comment

begin_class
specifier|public
class|class
name|SymmetricKeyHandler
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|SymmetricKeyHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|int
name|keySize
init|=
literal|256
decl_stmt|;
specifier|private
name|Entropy
name|clientEntropy
decl_stmt|;
specifier|private
name|byte
index|[]
name|entropyBytes
decl_stmt|;
specifier|private
name|byte
index|[]
name|secret
decl_stmt|;
specifier|private
name|boolean
name|computedKey
decl_stmt|;
specifier|public
name|SymmetricKeyHandler
parameter_list|(
name|TokenProviderParameters
name|tokenParameters
parameter_list|)
block|{
name|KeyRequirements
name|keyRequirements
init|=
name|tokenParameters
operator|.
name|getKeyRequirements
argument_list|()
decl_stmt|;
name|keySize
operator|=
name|Long
operator|.
name|valueOf
argument_list|(
name|keyRequirements
operator|.
name|getKeySize
argument_list|()
argument_list|)
operator|.
name|intValue
argument_list|()
expr_stmt|;
name|STSPropertiesMBean
name|stsProperties
init|=
name|tokenParameters
operator|.
name|getStsProperties
argument_list|()
decl_stmt|;
name|SignatureProperties
name|signatureProperties
init|=
name|stsProperties
operator|.
name|getSignatureProperties
argument_list|()
decl_stmt|;
comment|// Test EncryptWith
name|String
name|encryptWith
init|=
name|keyRequirements
operator|.
name|getEncryptWith
argument_list|()
decl_stmt|;
if|if
condition|(
name|encryptWith
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|(
name|WSS4JConstants
operator|.
name|AES_128
operator|.
name|equals
argument_list|(
name|encryptWith
argument_list|)
operator|||
name|WSS4JConstants
operator|.
name|AES_128_GCM
operator|.
name|equals
argument_list|(
name|encryptWith
argument_list|)
operator|)
operator|&&
name|keySize
operator|<
literal|128
condition|)
block|{
name|keySize
operator|=
literal|128
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|(
name|WSS4JConstants
operator|.
name|AES_192
operator|.
name|equals
argument_list|(
name|encryptWith
argument_list|)
operator|||
name|WSS4JConstants
operator|.
name|AES_192_GCM
operator|.
name|equals
argument_list|(
name|encryptWith
argument_list|)
operator|)
operator|&&
name|keySize
operator|<
literal|192
condition|)
block|{
name|keySize
operator|=
literal|192
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|(
name|WSS4JConstants
operator|.
name|AES_256
operator|.
name|equals
argument_list|(
name|encryptWith
argument_list|)
operator|||
name|WSS4JConstants
operator|.
name|AES_256_GCM
operator|.
name|equals
argument_list|(
name|encryptWith
argument_list|)
operator|)
operator|&&
name|keySize
operator|<
literal|256
condition|)
block|{
name|keySize
operator|=
literal|256
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|WSS4JConstants
operator|.
name|TRIPLE_DES
operator|.
name|equals
argument_list|(
name|encryptWith
argument_list|)
operator|&&
name|keySize
operator|<
literal|192
condition|)
block|{
name|keySize
operator|=
literal|192
expr_stmt|;
block|}
block|}
comment|// Test KeySize
if|if
condition|(
name|keySize
argument_list|<
name|signatureProperties
operator|.
name|getMinimumKeySize
operator|(
operator|)
operator|||
name|keySize
argument_list|>
name|signatureProperties
operator|.
name|getMaximumKeySize
argument_list|()
condition|)
block|{
name|keySize
operator|=
name|Long
operator|.
name|valueOf
argument_list|(
name|signatureProperties
operator|.
name|getKeySize
argument_list|()
argument_list|)
operator|.
name|intValue
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Received KeySize of "
operator|+
name|keyRequirements
operator|.
name|getKeySize
argument_list|()
operator|+
literal|" not accepted so defaulting to "
operator|+
name|signatureProperties
operator|.
name|getKeySize
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Test Entropy
name|clientEntropy
operator|=
name|keyRequirements
operator|.
name|getEntropy
argument_list|()
expr_stmt|;
if|if
condition|(
name|clientEntropy
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"A SymmetricKey KeyType is requested, but no client entropy is provided"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|clientEntropy
operator|.
name|getBinarySecret
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|BinarySecret
name|binarySecret
init|=
name|clientEntropy
operator|.
name|getBinarySecret
argument_list|()
decl_stmt|;
if|if
condition|(
name|STSConstants
operator|.
name|NONCE_TYPE
operator|.
name|equals
argument_list|(
name|binarySecret
operator|.
name|getBinarySecretType
argument_list|()
argument_list|)
condition|)
block|{
name|byte
index|[]
name|nonce
init|=
name|binarySecret
operator|.
name|getBinarySecretValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|nonce
operator|==
literal|null
operator|||
operator|(
name|nonce
operator|.
name|length
operator|<
operator|(
name|keySize
operator|/
literal|8
operator|)
operator|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"User Entropy rejected"
argument_list|)
expr_stmt|;
name|clientEntropy
operator|=
literal|null
expr_stmt|;
block|}
name|String
name|computedKeyAlgorithm
init|=
name|keyRequirements
operator|.
name|getComputedKeyAlgorithm
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|STSConstants
operator|.
name|COMPUTED_KEY_PSHA1
operator|.
name|equals
argument_list|(
name|computedKeyAlgorithm
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"The computed key algorithm of "
operator|+
name|computedKeyAlgorithm
operator|+
literal|" is not supported"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Computed Key Algorithm not supported"
argument_list|,
name|STSException
operator|.
name|INVALID_REQUEST
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|STSConstants
operator|.
name|SYMMETRIC_KEY_TYPE
operator|.
name|equals
argument_list|(
name|binarySecret
operator|.
name|getBinarySecretType
argument_list|()
argument_list|)
operator|||
name|binarySecret
operator|.
name|getBinarySecretType
argument_list|()
operator|==
literal|null
condition|)
block|{
name|byte
index|[]
name|secretValue
init|=
name|binarySecret
operator|.
name|getBinarySecretValue
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|secretValue
operator|.
name|length
operator|*
literal|8L
operator|)
operator|<
name|signatureProperties
operator|.
name|getMinimumKeySize
argument_list|()
operator|||
operator|(
name|secretValue
operator|.
name|length
operator|*
literal|8L
operator|)
operator|>
name|signatureProperties
operator|.
name|getMaximumKeySize
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Received secret of length "
operator|+
name|secretValue
operator|.
name|length
operator|+
literal|" bits is not accepted"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"User Entropy rejected"
argument_list|)
expr_stmt|;
name|clientEntropy
operator|=
literal|null
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"The type "
operator|+
name|binarySecret
operator|.
name|getBinarySecretType
argument_list|()
operator|+
literal|" is not supported"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"No user supplied entropy for SymmetricKey case"
argument_list|,
name|STSException
operator|.
name|INVALID_REQUEST
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|clientEntropy
operator|.
name|getDecryptedKey
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|byte
index|[]
name|secretValue
init|=
name|clientEntropy
operator|.
name|getDecryptedKey
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|secretValue
operator|.
name|length
operator|*
literal|8L
operator|)
operator|<
name|signatureProperties
operator|.
name|getMinimumKeySize
argument_list|()
operator|||
operator|(
name|secretValue
operator|.
name|length
operator|*
literal|8L
operator|)
operator|>
name|signatureProperties
operator|.
name|getMaximumKeySize
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Received secret of length "
operator|+
name|secretValue
operator|.
name|length
operator|+
literal|" bits is not accepted"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"User Entropy rejected"
argument_list|)
expr_stmt|;
name|clientEntropy
operator|=
literal|null
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"The user supplied Entropy structure is invalid"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"The user supplied Entropy structure is invalid"
argument_list|,
name|STSException
operator|.
name|INVALID_REQUEST
argument_list|)
throw|;
block|}
block|}
comment|/**      * Create the Symmetric Key      */
specifier|public
name|void
name|createSymmetricKey
parameter_list|()
block|{
name|computedKey
operator|=
literal|false
expr_stmt|;
name|boolean
name|generateEntropy
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|clientEntropy
operator|!=
literal|null
condition|)
block|{
name|BinarySecret
name|binarySecret
init|=
name|clientEntropy
operator|.
name|getBinarySecret
argument_list|()
decl_stmt|;
if|if
condition|(
name|binarySecret
operator|!=
literal|null
operator|&&
operator|(
name|STSConstants
operator|.
name|SYMMETRIC_KEY_TYPE
operator|.
name|equals
argument_list|(
name|binarySecret
operator|.
name|getBinarySecretType
argument_list|()
argument_list|)
operator|||
name|binarySecret
operator|.
name|getBinarySecretType
argument_list|()
operator|==
literal|null
operator|)
condition|)
block|{
name|secret
operator|=
name|binarySecret
operator|.
name|getBinarySecretValue
argument_list|()
expr_stmt|;
name|generateEntropy
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|clientEntropy
operator|.
name|getDecryptedKey
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|secret
operator|=
name|clientEntropy
operator|.
name|getDecryptedKey
argument_list|()
expr_stmt|;
name|generateEntropy
operator|=
literal|false
expr_stmt|;
block|}
block|}
if|if
condition|(
name|generateEntropy
condition|)
block|{
try|try
block|{
name|entropyBytes
operator|=
name|WSSecurityUtil
operator|.
name|generateNonce
argument_list|(
name|keySize
operator|/
literal|8
argument_list|)
expr_stmt|;
name|secret
operator|=
name|entropyBytes
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|""
argument_list|,
name|ex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Error in creating symmetric key"
argument_list|,
name|ex
argument_list|,
name|STSException
operator|.
name|INVALID_REQUEST
argument_list|)
throw|;
block|}
if|if
condition|(
name|clientEntropy
operator|!=
literal|null
operator|&&
name|clientEntropy
operator|.
name|getBinarySecret
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|byte
index|[]
name|nonce
init|=
name|clientEntropy
operator|.
name|getBinarySecret
argument_list|()
operator|.
name|getBinarySecretValue
argument_list|()
decl_stmt|;
try|try
block|{
name|P_SHA1
name|psha1
init|=
operator|new
name|P_SHA1
argument_list|()
decl_stmt|;
name|secret
operator|=
name|psha1
operator|.
name|createKey
argument_list|(
name|nonce
argument_list|,
name|entropyBytes
argument_list|,
literal|0
argument_list|,
name|keySize
operator|/
literal|8
argument_list|)
expr_stmt|;
name|computedKey
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|""
argument_list|,
name|ex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Error in creating symmetric key"
argument_list|,
name|STSException
operator|.
name|INVALID_REQUEST
argument_list|)
throw|;
block|}
block|}
block|}
block|}
comment|/**      * Get the KeySize.      */
specifier|public
name|long
name|getKeySize
parameter_list|()
block|{
return|return
name|keySize
return|;
block|}
comment|/**      * Get the Entropy bytes      */
specifier|public
name|byte
index|[]
name|getEntropyBytes
parameter_list|()
block|{
return|return
name|entropyBytes
return|;
block|}
comment|/**      * Get the secret      */
specifier|public
name|byte
index|[]
name|getSecret
parameter_list|()
block|{
return|return
name|secret
return|;
block|}
comment|/**      * Get whether this is a computed key or not      */
specifier|public
name|boolean
name|isComputedKey
parameter_list|()
block|{
return|return
name|computedKey
return|;
block|}
block|}
end_class

end_unit

