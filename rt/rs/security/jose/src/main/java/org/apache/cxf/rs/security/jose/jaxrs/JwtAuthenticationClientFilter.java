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
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Priority
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Priorities
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|ClientRequestContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|ClientRequestFilter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|HttpHeaders
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
name|Base64UrlUtility
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
name|CryptoUtils
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
name|configuration
operator|.
name|security
operator|.
name|AuthorizationPolicy
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
name|endpoint
operator|.
name|Endpoint
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
name|jaxrs
operator|.
name|utils
operator|.
name|JAXRSUtils
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
name|JoseException
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
name|jwt
operator|.
name|AbstractJoseJwtProducer
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

begin_class
annotation|@
name|Priority
argument_list|(
name|Priorities
operator|.
name|AUTHENTICATION
argument_list|)
specifier|public
class|class
name|JwtAuthenticationClientFilter
extends|extends
name|AbstractJoseJwtProducer
implements|implements
name|ClientRequestFilter
block|{
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ClientRequestContext
name|requestContext
parameter_list|)
throws|throws
name|IOException
block|{
name|JwtToken
name|jwt
init|=
name|getJwtToken
argument_list|(
name|requestContext
argument_list|)
decl_stmt|;
name|boolean
name|jweRequired
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|jwt
operator|==
literal|null
condition|)
block|{
name|AuthorizationPolicy
name|ap
init|=
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getExtensor
argument_list|(
name|AuthorizationPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ap
operator|!=
literal|null
operator|&&
name|ap
operator|.
name|getUserName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|JwtClaims
name|claims
init|=
operator|new
name|JwtClaims
argument_list|()
decl_stmt|;
name|claims
operator|.
name|setSubject
argument_list|(
name|ap
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setClaim
argument_list|(
literal|"password"
argument_list|,
name|ap
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setIssuedAt
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|/
literal|1000
argument_list|)
expr_stmt|;
name|jwt
operator|=
operator|new
name|JwtToken
argument_list|(
operator|new
name|JoseHeaders
argument_list|()
argument_list|,
name|claims
argument_list|)
expr_stmt|;
name|jweRequired
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|jwt
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|JoseException
argument_list|(
literal|"JWT token is not available"
argument_list|)
throw|;
block|}
name|JoseUtils
operator|.
name|setJoseMessageContextProperty
argument_list|(
name|jwt
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|getContextPropertyValue
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|data
init|=
name|super
operator|.
name|processJwt
argument_list|(
name|jwt
argument_list|,
literal|true
argument_list|,
name|jweRequired
argument_list|)
decl_stmt|;
name|requestContext
operator|.
name|getHeaders
argument_list|()
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|AUTHORIZATION
argument_list|,
literal|"JWT "
operator|+
name|data
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|JwtToken
name|getJwtToken
parameter_list|(
name|ClientRequestContext
name|requestContext
parameter_list|)
block|{
return|return
operator|(
name|JwtToken
operator|)
name|requestContext
operator|.
name|getProperty
argument_list|(
literal|"jwt.token"
argument_list|)
return|;
block|}
specifier|protected
name|String
name|getContextPropertyValue
parameter_list|()
block|{
return|return
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|CryptoUtils
operator|.
name|generateSecureRandomBytes
argument_list|(
literal|16
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

