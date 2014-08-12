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
name|jwe
package|;
end_package

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
name|utils
operator|.
name|crypto
operator|.
name|CryptoUtils
import|;
end_import

begin_class
specifier|public
class|class
name|AesWrapKeyDecryptionAlgorithm
extends|extends
name|WrappedKeyDecryptionAlgorithm
block|{
specifier|public
name|AesWrapKeyDecryptionAlgorithm
parameter_list|(
name|byte
index|[]
name|secretKey
parameter_list|)
block|{
name|this
argument_list|(
name|CryptoUtils
operator|.
name|createSecretKeySpec
argument_list|(
name|secretKey
argument_list|,
name|Algorithm
operator|.
name|AES_WRAP_ALGO_JAVA
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AesWrapKeyDecryptionAlgorithm
parameter_list|(
name|SecretKey
name|secretKey
parameter_list|)
block|{
name|super
argument_list|(
name|secretKey
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

