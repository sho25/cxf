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
name|jws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PublicKey
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
name|AlgorithmParameterSpec
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|JoseHeaders
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
name|SignatureAlgorithm
import|;
end_import

begin_class
specifier|public
class|class
name|EcDsaJwsSignatureVerifier
extends|extends
name|PublicKeyJwsSignatureVerifier
block|{
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|SIGNATURE_LENGTH_MAP
decl_stmt|;
static|static
block|{
name|SIGNATURE_LENGTH_MAP
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
expr_stmt|;
name|SIGNATURE_LENGTH_MAP
operator|.
name|put
argument_list|(
name|SignatureAlgorithm
operator|.
name|ES256
operator|.
name|getJwaName
argument_list|()
argument_list|,
literal|64
argument_list|)
expr_stmt|;
name|SIGNATURE_LENGTH_MAP
operator|.
name|put
argument_list|(
name|SignatureAlgorithm
operator|.
name|ES384
operator|.
name|getJwaName
argument_list|()
argument_list|,
literal|96
argument_list|)
expr_stmt|;
name|SIGNATURE_LENGTH_MAP
operator|.
name|put
argument_list|(
name|SignatureAlgorithm
operator|.
name|ES512
operator|.
name|getJwaName
argument_list|()
argument_list|,
literal|132
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EcDsaJwsSignatureVerifier
parameter_list|(
name|PublicKey
name|key
parameter_list|,
name|SignatureAlgorithm
name|supportedAlgo
parameter_list|)
block|{
name|this
argument_list|(
name|key
argument_list|,
literal|null
argument_list|,
name|supportedAlgo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EcDsaJwsSignatureVerifier
parameter_list|(
name|PublicKey
name|key
parameter_list|,
name|AlgorithmParameterSpec
name|spec
parameter_list|,
name|SignatureAlgorithm
name|supportedAlgo
parameter_list|)
block|{
name|super
argument_list|(
name|key
argument_list|,
name|spec
argument_list|,
name|supportedAlgo
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|verify
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|,
name|String
name|unsignedText
parameter_list|,
name|byte
index|[]
name|signature
parameter_list|)
block|{
if|if
condition|(
name|SIGNATURE_LENGTH_MAP
operator|.
name|get
argument_list|(
name|super
operator|.
name|getAlgorithm
argument_list|()
operator|.
name|getJwaName
argument_list|()
argument_list|)
operator|!=
name|signature
operator|.
name|length
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
name|byte
index|[]
name|der
init|=
name|signatureToDer
argument_list|(
name|signature
argument_list|)
decl_stmt|;
return|return
name|super
operator|.
name|verify
argument_list|(
name|headers
argument_list|,
name|unsignedText
argument_list|,
name|der
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isValidAlgorithmFamily
parameter_list|(
name|String
name|algo
parameter_list|)
block|{
return|return
name|AlgorithmUtils
operator|.
name|isEcDsaSign
argument_list|(
name|algo
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|byte
index|[]
name|signatureToDer
parameter_list|(
name|byte
name|joseSig
index|[]
parameter_list|)
block|{
name|int
name|partLen
init|=
name|joseSig
operator|.
name|length
operator|/
literal|2
decl_stmt|;
name|int
name|rOffset
init|=
name|joseSig
index|[
literal|0
index|]
operator|<
literal|0
condition|?
literal|1
else|:
literal|0
decl_stmt|;
name|int
name|sOffset
init|=
name|joseSig
index|[
name|partLen
index|]
operator|<
literal|0
condition|?
literal|1
else|:
literal|0
decl_stmt|;
name|int
name|rPartLen
init|=
name|partLen
operator|+
name|rOffset
decl_stmt|;
name|int
name|sPartLen
init|=
name|partLen
operator|+
name|sOffset
decl_stmt|;
name|int
name|totalLenBytesCount
init|=
name|joseSig
operator|.
name|length
operator|>
literal|127
condition|?
literal|2
else|:
literal|1
decl_stmt|;
name|int
name|rPartStart
init|=
literal|1
operator|+
name|totalLenBytesCount
operator|+
literal|2
decl_stmt|;
name|byte
index|[]
name|der
init|=
operator|new
name|byte
index|[
name|rPartStart
operator|+
literal|2
operator|+
name|rPartLen
operator|+
name|sPartLen
index|]
decl_stmt|;
name|der
index|[
literal|0
index|]
operator|=
literal|48
expr_stmt|;
if|if
condition|(
name|totalLenBytesCount
operator|==
literal|2
condition|)
block|{
name|der
index|[
literal|1
index|]
operator|=
operator|-
literal|127
expr_stmt|;
block|}
name|der
index|[
name|totalLenBytesCount
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|der
operator|.
name|length
operator|-
operator|(
literal|1
operator|+
name|totalLenBytesCount
operator|)
argument_list|)
expr_stmt|;
name|der
index|[
name|totalLenBytesCount
operator|+
literal|1
index|]
operator|=
literal|2
expr_stmt|;
name|der
index|[
name|totalLenBytesCount
operator|+
literal|2
index|]
operator|=
operator|(
name|byte
operator|)
name|rPartLen
expr_stmt|;
name|int
name|sPartStart
init|=
name|rPartStart
operator|+
name|rPartLen
decl_stmt|;
name|der
index|[
name|sPartStart
index|]
operator|=
literal|2
expr_stmt|;
name|der
index|[
name|sPartStart
operator|+
literal|1
index|]
operator|=
operator|(
name|byte
operator|)
name|sPartLen
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|joseSig
argument_list|,
literal|0
argument_list|,
name|der
argument_list|,
name|rPartStart
operator|+
name|rOffset
argument_list|,
name|partLen
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|joseSig
argument_list|,
name|partLen
argument_list|,
name|der
argument_list|,
name|sPartStart
operator|+
literal|2
operator|+
name|sOffset
argument_list|,
name|partLen
argument_list|)
expr_stmt|;
return|return
name|der
return|;
block|}
block|}
end_class

end_unit

