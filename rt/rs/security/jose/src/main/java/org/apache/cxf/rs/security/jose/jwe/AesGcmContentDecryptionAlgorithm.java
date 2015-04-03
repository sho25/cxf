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
name|ContentAlgorithm
import|;
end_import

begin_class
specifier|public
class|class
name|AesGcmContentDecryptionAlgorithm
extends|extends
name|AbstractContentEncryptionCipherProperties
implements|implements
name|ContentDecryptionAlgorithm
block|{
specifier|public
name|AesGcmContentDecryptionAlgorithm
parameter_list|(
name|ContentAlgorithm
name|supportedAlgo
parameter_list|)
block|{
name|super
argument_list|(
name|supportedAlgo
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|getEncryptedSequence
parameter_list|(
name|JweHeaders
name|headers
parameter_list|,
name|byte
index|[]
name|cipher
parameter_list|,
name|byte
index|[]
name|authTag
parameter_list|)
block|{
name|String
name|algo
init|=
name|headers
operator|.
name|getContentEncryptionAlgorithm
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|AlgorithmUtils
operator|.
name|isAesGcm
argument_list|(
name|algo
argument_list|)
operator|||
operator|!
name|getAlgorithm
argument_list|()
operator|.
name|getJwaName
argument_list|()
operator|.
name|equals
argument_list|(
name|algo
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Invalid content encryption algorithm"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JweException
argument_list|(
name|JweException
operator|.
name|Error
operator|.
name|INVALID_CONTENT_ALGORITHM
argument_list|)
throw|;
block|}
return|return
name|JweCompactConsumer
operator|.
name|getCipherWithAuthTag
argument_list|(
name|cipher
argument_list|,
name|authTag
argument_list|)
return|;
block|}
block|}
end_class

end_unit

