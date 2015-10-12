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
name|jwa
package|;
end_package

begin_enum
specifier|public
enum|enum
name|ContentAlgorithm
block|{
name|A128GCM
argument_list|(
name|AlgorithmUtils
operator|.
name|A128GCM_ALGO
argument_list|,
literal|"AES/GCM/NoPadding"
argument_list|,
literal|128
argument_list|)
block|,
name|A192GCM
argument_list|(
name|AlgorithmUtils
operator|.
name|A192GCM_ALGO
argument_list|,
literal|"AES/GCM/NoPadding"
argument_list|,
literal|192
argument_list|)
block|,
name|A256GCM
argument_list|(
name|AlgorithmUtils
operator|.
name|A256GCM_ALGO
argument_list|,
literal|"AES/GCM/NoPadding"
argument_list|,
literal|256
argument_list|)
block|,
comment|//TODO: default to "AES/CBC/PKCS5Padding" if Cipher "AES/CBC/PKCS7Padding"
comment|// can not be initialized, apparently Java 8 has decided to settle on PKCS5Padding only
name|A128CBC_HS256
argument_list|(
name|AlgorithmUtils
operator|.
name|A128CBC_HS256_ALGO
argument_list|,
literal|"AES/CBC/PKCS7Padding"
argument_list|,
literal|128
argument_list|)
block|,
name|A192CBC_HS384
argument_list|(
name|AlgorithmUtils
operator|.
name|A192CBC_HS384_ALGO
argument_list|,
literal|"AES/CBC/PKCS7Padding"
argument_list|,
literal|192
argument_list|)
block|,
name|A256CBC_HS512
argument_list|(
name|AlgorithmUtils
operator|.
name|A256CBC_HS512_ALGO
argument_list|,
literal|"AES/CBC/PKCS7Padding"
argument_list|,
literal|256
argument_list|)
block|;
specifier|private
specifier|final
name|String
name|jwaName
decl_stmt|;
specifier|private
specifier|final
name|String
name|javaName
decl_stmt|;
specifier|private
specifier|final
name|int
name|keySizeBits
decl_stmt|;
name|ContentAlgorithm
parameter_list|(
name|String
name|jwaName
parameter_list|,
name|String
name|javaName
parameter_list|,
name|int
name|keySizeBits
parameter_list|)
block|{
name|this
operator|.
name|jwaName
operator|=
name|jwaName
expr_stmt|;
name|this
operator|.
name|javaName
operator|=
name|javaName
expr_stmt|;
name|this
operator|.
name|keySizeBits
operator|=
name|keySizeBits
expr_stmt|;
block|}
specifier|public
name|String
name|getJwaName
parameter_list|()
block|{
return|return
name|jwaName
return|;
block|}
specifier|public
name|String
name|getJavaName
parameter_list|()
block|{
return|return
name|javaName
operator|==
literal|null
condition|?
name|name
argument_list|()
else|:
name|javaName
return|;
block|}
specifier|public
name|String
name|getJavaAlgoName
parameter_list|()
block|{
return|return
name|AlgorithmUtils
operator|.
name|stripAlgoProperties
argument_list|(
name|getJavaName
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|int
name|getKeySizeBits
parameter_list|()
block|{
return|return
name|keySizeBits
return|;
block|}
specifier|public
specifier|static
name|ContentAlgorithm
name|getAlgorithm
parameter_list|(
name|String
name|algo
parameter_list|)
block|{
if|if
condition|(
name|algo
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|ContentAlgorithm
operator|.
name|valueOf
argument_list|(
name|algo
operator|.
name|replace
argument_list|(
literal|'-'
argument_list|,
literal|'_'
argument_list|)
operator|.
name|replace
argument_list|(
literal|'+'
argument_list|,
literal|'_'
argument_list|)
argument_list|)
return|;
block|}
block|}
end_enum

end_unit

