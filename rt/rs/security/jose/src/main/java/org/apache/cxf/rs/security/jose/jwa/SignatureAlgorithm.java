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
name|SignatureAlgorithm
block|{
name|HS256
argument_list|(
name|AlgorithmUtils
operator|.
name|HMAC_SHA_256_ALGO
argument_list|,
name|AlgorithmUtils
operator|.
name|HMAC_SHA_256_JAVA
argument_list|,
literal|256
argument_list|)
block|,
name|HS384
argument_list|(
name|AlgorithmUtils
operator|.
name|HMAC_SHA_384_ALGO
argument_list|,
name|AlgorithmUtils
operator|.
name|HMAC_SHA_384_JAVA
argument_list|,
literal|384
argument_list|)
block|,
name|HS512
argument_list|(
name|AlgorithmUtils
operator|.
name|HMAC_SHA_512_ALGO
argument_list|,
name|AlgorithmUtils
operator|.
name|HMAC_SHA_512_JAVA
argument_list|,
literal|512
argument_list|)
block|,
name|RS256
argument_list|(
name|AlgorithmUtils
operator|.
name|RS_SHA_256_ALGO
argument_list|,
name|AlgorithmUtils
operator|.
name|RS_SHA_256_JAVA
argument_list|,
literal|256
argument_list|)
block|,
name|RS384
argument_list|(
name|AlgorithmUtils
operator|.
name|RS_SHA_384_ALGO
argument_list|,
name|AlgorithmUtils
operator|.
name|RS_SHA_384_JAVA
argument_list|,
literal|384
argument_list|)
block|,
name|RS512
argument_list|(
name|AlgorithmUtils
operator|.
name|RS_SHA_512_ALGO
argument_list|,
name|AlgorithmUtils
operator|.
name|RS_SHA_512_JAVA
argument_list|,
literal|512
argument_list|)
block|,
name|PS256
argument_list|(
name|AlgorithmUtils
operator|.
name|PS_SHA_256_ALGO
argument_list|,
name|AlgorithmUtils
operator|.
name|PS_SHA_256_JAVA
argument_list|,
literal|256
argument_list|)
block|,
name|PS384
argument_list|(
name|AlgorithmUtils
operator|.
name|PS_SHA_384_ALGO
argument_list|,
name|AlgorithmUtils
operator|.
name|PS_SHA_384_JAVA
argument_list|,
literal|384
argument_list|)
block|,
name|PS512
argument_list|(
name|AlgorithmUtils
operator|.
name|PS_SHA_512_ALGO
argument_list|,
name|AlgorithmUtils
operator|.
name|PS_SHA_512_JAVA
argument_list|,
literal|512
argument_list|)
block|,
name|ES256
argument_list|(
name|AlgorithmUtils
operator|.
name|ES_SHA_256_ALGO
argument_list|,
name|AlgorithmUtils
operator|.
name|ES_SHA_256_JAVA
argument_list|,
literal|256
argument_list|)
block|,
name|ES384
argument_list|(
name|AlgorithmUtils
operator|.
name|ES_SHA_384_ALGO
argument_list|,
name|AlgorithmUtils
operator|.
name|ES_SHA_384_JAVA
argument_list|,
literal|384
argument_list|)
block|,
name|ES512
argument_list|(
name|AlgorithmUtils
operator|.
name|ES_SHA_512_ALGO
argument_list|,
name|AlgorithmUtils
operator|.
name|ES_SHA_512_JAVA
argument_list|,
literal|512
argument_list|)
block|,
name|NONE
argument_list|(
name|AlgorithmUtils
operator|.
name|NONE_TEXT_ALGO
argument_list|,
literal|null
argument_list|,
operator|-
literal|1
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
specifier|private
name|SignatureAlgorithm
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
name|SignatureAlgorithm
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
if|if
condition|(
name|AlgorithmUtils
operator|.
name|NONE_TEXT_ALGO
operator|.
name|equals
argument_list|(
name|algo
argument_list|)
condition|)
block|{
return|return
name|NONE
return|;
block|}
return|return
name|SignatureAlgorithm
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

