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
name|Arrays
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
name|HmacUtils
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
name|JoseUtils
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
name|HmacJwsSignatureVerifier
implements|implements
name|JwsSignatureVerifier
block|{
specifier|private
name|byte
index|[]
name|key
decl_stmt|;
specifier|private
name|AlgorithmParameterSpec
name|hmacSpec
decl_stmt|;
specifier|private
name|String
name|supportedAlgo
decl_stmt|;
specifier|public
name|HmacJwsSignatureVerifier
parameter_list|(
name|String
name|encodedKey
parameter_list|,
name|String
name|supportedAlgo
parameter_list|)
block|{
name|this
argument_list|(
name|JoseUtils
operator|.
name|decode
argument_list|(
name|encodedKey
argument_list|)
argument_list|,
name|supportedAlgo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|HmacJwsSignatureVerifier
parameter_list|(
name|byte
index|[]
name|key
parameter_list|,
name|String
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
name|HmacJwsSignatureVerifier
parameter_list|(
name|byte
index|[]
name|key
parameter_list|,
name|AlgorithmParameterSpec
name|spec
parameter_list|,
name|String
name|supportedAlgo
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|hmacSpec
operator|=
name|spec
expr_stmt|;
name|this
operator|.
name|supportedAlgo
operator|=
name|supportedAlgo
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
name|byte
index|[]
name|expected
init|=
name|computeMac
argument_list|(
name|headers
argument_list|,
name|unsignedText
argument_list|)
decl_stmt|;
return|return
name|Arrays
operator|.
name|equals
argument_list|(
name|expected
argument_list|,
name|signature
argument_list|)
return|;
block|}
specifier|private
name|byte
index|[]
name|computeMac
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|,
name|String
name|text
parameter_list|)
block|{
return|return
name|HmacUtils
operator|.
name|computeHmac
argument_list|(
name|key
argument_list|,
name|Algorithm
operator|.
name|toJavaName
argument_list|(
name|checkAlgorithm
argument_list|(
name|headers
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|hmacSpec
argument_list|,
name|text
argument_list|)
return|;
block|}
specifier|protected
name|String
name|checkAlgorithm
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
operator|||
operator|!
name|Algorithm
operator|.
name|isHmacSign
argument_list|(
name|algo
argument_list|)
operator|||
operator|!
name|algo
operator|.
name|equals
argument_list|(
name|supportedAlgo
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
return|return
name|algo
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getAlgorithm
parameter_list|()
block|{
return|return
name|supportedAlgo
return|;
block|}
block|}
end_class

end_unit

