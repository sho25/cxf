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
name|ECPublicKey
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
name|ContentAlgorithm
import|;
end_import

begin_class
specifier|public
class|class
name|EcdhDirectKeyJweEncryption
extends|extends
name|JweEncryption
block|{
specifier|public
name|EcdhDirectKeyJweEncryption
parameter_list|(
name|ECPublicKey
name|peerPublicKey
parameter_list|,
name|String
name|curve
parameter_list|,
name|ContentAlgorithm
name|ctAlgo
parameter_list|)
block|{
name|this
argument_list|(
name|peerPublicKey
argument_list|,
name|curve
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|ctAlgo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EcdhDirectKeyJweEncryption
parameter_list|(
name|ECPublicKey
name|peerPublicKey
parameter_list|,
name|String
name|curve
parameter_list|,
name|String
name|apuString
parameter_list|,
name|String
name|apvString
parameter_list|,
name|ContentAlgorithm
name|ctAlgo
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|EcdhDirectKeyEncryptionAlgorithm
argument_list|()
argument_list|,
operator|new
name|EcdhAesGcmContentEncryptionAlgorithm
argument_list|(
name|peerPublicKey
argument_list|,
name|curve
argument_list|,
name|apuString
argument_list|,
name|apvString
argument_list|,
name|ctAlgo
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

