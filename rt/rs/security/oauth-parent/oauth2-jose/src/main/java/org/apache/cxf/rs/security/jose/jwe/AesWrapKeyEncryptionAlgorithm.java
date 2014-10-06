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
name|SecretKey
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
name|crypto
operator|.
name|CryptoUtils
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
name|AesWrapKeyEncryptionAlgorithm
extends|extends
name|AbstractWrapKeyEncryptionAlgorithm
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
name|A128KW
operator|.
name|getJwtName
argument_list|()
argument_list|,
name|Algorithm
operator|.
name|A192KW
operator|.
name|getJwtName
argument_list|()
argument_list|,
name|Algorithm
operator|.
name|A256KW
operator|.
name|getJwtName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
specifier|public
name|AesWrapKeyEncryptionAlgorithm
parameter_list|(
name|String
name|encodedKey
parameter_list|,
name|String
name|keyAlgoJwt
parameter_list|)
block|{
name|this
argument_list|(
name|CryptoUtils
operator|.
name|decodeSequence
argument_list|(
name|encodedKey
argument_list|)
argument_list|,
name|keyAlgoJwt
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AesWrapKeyEncryptionAlgorithm
parameter_list|(
name|byte
index|[]
name|keyBytes
parameter_list|,
name|String
name|keyAlgoJwt
parameter_list|)
block|{
name|this
argument_list|(
name|CryptoUtils
operator|.
name|createSecretKeySpec
argument_list|(
name|keyBytes
argument_list|,
name|Algorithm
operator|.
name|toJavaName
argument_list|(
name|keyAlgoJwt
argument_list|)
argument_list|)
argument_list|,
name|keyAlgoJwt
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AesWrapKeyEncryptionAlgorithm
parameter_list|(
name|SecretKey
name|key
parameter_list|,
name|String
name|keyAlgoJwt
parameter_list|)
block|{
name|super
argument_list|(
name|key
argument_list|,
name|keyAlgoJwt
argument_list|,
name|SUPPORTED_ALGORITHMS
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

