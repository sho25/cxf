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
name|common
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
name|JwtUtils
import|;
end_import

begin_class
specifier|public
class|class
name|JwsJwtCompactProducer
extends|extends
name|JwsCompactProducer
block|{
specifier|public
name|JwsJwtCompactProducer
parameter_list|(
name|JwtToken
name|token
parameter_list|)
block|{
name|this
argument_list|(
name|token
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JwsJwtCompactProducer
parameter_list|(
name|JwtClaims
name|claims
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|JwtToken
argument_list|(
literal|null
argument_list|,
name|claims
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JwsJwtCompactProducer
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|,
name|JwtClaims
name|claims
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|JwtToken
argument_list|(
name|headers
argument_list|,
name|claims
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|JwsJwtCompactProducer
parameter_list|(
name|JwtToken
name|token
parameter_list|,
name|JwtTokenReaderWriter
name|w
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|JwsHeaders
argument_list|(
name|token
operator|.
name|getHeaders
argument_list|()
argument_list|)
argument_list|,
name|w
argument_list|,
name|JwtUtils
operator|.
name|claimsToJson
argument_list|(
name|token
operator|.
name|getClaims
argument_list|()
argument_list|,
name|w
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
