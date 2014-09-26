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
name|jwe
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|interfaces
operator|.
name|RSAPrivateKey
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
name|Algorithm
import|;
end_import

begin_class
specifier|public
class|class
name|RSAOaepKeyDecryptionAlgorithm
extends|extends
name|WrappedKeyDecryptionAlgorithm
block|{
specifier|public
name|RSAOaepKeyDecryptionAlgorithm
parameter_list|(
name|RSAPrivateKey
name|privateKey
parameter_list|)
block|{
name|this
argument_list|(
name|privateKey
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RSAOaepKeyDecryptionAlgorithm
parameter_list|(
name|RSAPrivateKey
name|privateKey
parameter_list|,
name|String
name|supportedAlgo
parameter_list|)
block|{
name|this
argument_list|(
name|privateKey
argument_list|,
name|supportedAlgo
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RSAOaepKeyDecryptionAlgorithm
parameter_list|(
name|RSAPrivateKey
name|privateKey
parameter_list|,
name|String
name|supportedAlgo
parameter_list|,
name|boolean
name|unwrap
parameter_list|)
block|{
name|super
argument_list|(
name|privateKey
argument_list|,
name|supportedAlgo
argument_list|,
name|unwrap
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|int
name|getKeyCipherBlockSize
parameter_list|()
block|{
return|return
operator|(
operator|(
name|RSAPrivateKey
operator|)
name|getCekDecryptionKey
argument_list|()
operator|)
operator|.
name|getModulus
argument_list|()
operator|.
name|toByteArray
argument_list|()
operator|.
name|length
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|validateKeyEncryptionAlgorithm
parameter_list|(
name|String
name|keyAlgo
parameter_list|)
block|{
name|super
operator|.
name|validateKeyEncryptionAlgorithm
argument_list|(
name|keyAlgo
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|Algorithm
operator|.
name|isRsaOaep
argument_list|(
name|keyAlgo
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

