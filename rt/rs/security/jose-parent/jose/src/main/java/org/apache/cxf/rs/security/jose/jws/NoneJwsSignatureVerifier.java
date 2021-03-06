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
name|NoneJwsSignatureVerifier
implements|implements
name|JwsSignatureVerifier
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|verify
parameter_list|(
name|JwsHeaders
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
return|return
name|headers
operator|.
name|getSignatureAlgorithm
argument_list|()
operator|==
name|getAlgorithm
argument_list|()
operator|&&
name|signature
operator|.
name|length
operator|==
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|SignatureAlgorithm
name|getAlgorithm
parameter_list|()
block|{
return|return
name|SignatureAlgorithm
operator|.
name|NONE
return|;
block|}
annotation|@
name|Override
specifier|public
name|JwsVerificationSignature
name|createJwsVerificationSignature
parameter_list|(
name|JwsHeaders
name|headers
parameter_list|)
block|{
return|return
operator|new
name|NoneJwsVerificationSignature
argument_list|()
return|;
block|}
specifier|private
specifier|static
class|class
name|NoneJwsVerificationSignature
implements|implements
name|JwsVerificationSignature
block|{
annotation|@
name|Override
specifier|public
name|void
name|update
parameter_list|(
name|byte
index|[]
name|src
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
block|{
comment|// complete
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|verify
parameter_list|(
name|byte
index|[]
name|signature
parameter_list|)
block|{
return|return
name|signature
operator|.
name|length
operator|==
literal|0
return|;
block|}
block|}
block|}
end_class

end_unit

