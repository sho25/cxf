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
name|jwt
operator|.
name|JwtClaims
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
name|jwt
operator|.
name|JwtToken
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
name|jwt
operator|.
name|JwtTokenReaderWriter
import|;
end_import

begin_class
specifier|public
class|class
name|JwsJwtCompactConsumer
extends|extends
name|JwsCompactConsumer
block|{
specifier|private
name|JwtToken
name|token
decl_stmt|;
specifier|public
name|JwsJwtCompactConsumer
parameter_list|(
name|String
name|encodedJws
parameter_list|)
block|{
name|super
argument_list|(
name|encodedJws
argument_list|,
literal|null
argument_list|,
operator|new
name|JwtTokenReaderWriter
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JwtClaims
name|getJwtClaims
parameter_list|()
block|{
return|return
name|getJwtToken
argument_list|()
operator|.
name|getClaims
argument_list|()
return|;
block|}
specifier|public
name|JwtToken
name|getJwtToken
parameter_list|()
block|{
if|if
condition|(
name|token
operator|==
literal|null
condition|)
block|{
name|JwsHeaders
name|theHeaders
init|=
name|super
operator|.
name|getJwsHeaders
argument_list|()
decl_stmt|;
name|JwtClaims
name|theClaims
init|=
operator|(
operator|(
name|JwtTokenReaderWriter
operator|)
name|getReader
argument_list|()
operator|)
operator|.
name|fromJsonClaims
argument_list|(
name|getDecodedJwsPayload
argument_list|()
argument_list|)
decl_stmt|;
name|token
operator|=
operator|new
name|JwtToken
argument_list|(
name|theHeaders
argument_list|,
name|theClaims
argument_list|)
expr_stmt|;
block|}
return|return
name|token
return|;
block|}
block|}
end_class

end_unit

