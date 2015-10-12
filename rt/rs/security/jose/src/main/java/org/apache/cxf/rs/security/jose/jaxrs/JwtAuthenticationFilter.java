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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|container
operator|.
name|ContainerRequestContext
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
name|container
operator|.
name|ContainerRequestFilter
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
name|container
operator|.
name|PreMatching
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
name|logging
operator|.
name|LogUtils
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
name|AbstractJoseJwtConsumer
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
name|JwtUtils
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
name|security
operator|.
name|SecurityContext
import|;
end_import

begin_class
annotation|@
name|PreMatching
annotation|@
name|Priority
argument_list|(
name|Priorities
operator|.
name|AUTHENTICATION
argument_list|)
specifier|public
class|class
name|JwtAuthenticationFilter
extends|extends
name|AbstractJoseJwtConsumer
implements|implements
name|ContainerRequestFilter
block|{
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JwtAuthenticationFilter
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_AUTH_SCHEME
init|=
literal|"JWT"
decl_stmt|;
specifier|private
name|String
name|expectedAuthScheme
init|=
name|DEFAULT_AUTH_SCHEME
decl_stmt|;
specifier|private
name|int
name|clockOffset
decl_stmt|;
specifier|private
name|int
name|ttl
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|requestContext
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|auth
init|=
name|requestContext
operator|.
name|getHeaderString
argument_list|(
name|HttpHeaders
operator|.
name|AUTHORIZATION
argument_list|)
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|auth
operator|==
literal|null
condition|?
literal|null
else|:
name|auth
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|==
literal|null
operator|||
operator|!
name|expectedAuthScheme
operator|.
name|equals
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
operator|||
name|parts
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|JoseException
argument_list|(
name|expectedAuthScheme
operator|+
literal|" scheme is expected"
argument_list|)
throw|;
block|}
name|JwtToken
name|token
init|=
name|super
operator|.
name|getJwtToken
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|JoseUtils
operator|.
name|setMessageContextProperty
argument_list|(
name|token
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|,
operator|new
name|JwtTokenSecurityContext
argument_list|(
name|token
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setExpectedAuthScheme
parameter_list|(
name|String
name|expectedAuthScheme
parameter_list|)
block|{
name|this
operator|.
name|expectedAuthScheme
operator|=
name|expectedAuthScheme
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|validateToken
parameter_list|(
name|JwtToken
name|jwt
parameter_list|)
block|{
comment|// If we have no issued time then we need to have an expiry
name|boolean
name|expiredRequired
init|=
name|jwt
operator|.
name|getClaims
argument_list|()
operator|.
name|getIssuedAt
argument_list|()
operator|==
literal|null
decl_stmt|;
name|JwtUtils
operator|.
name|validateJwtExpiry
argument_list|(
name|jwt
operator|.
name|getClaims
argument_list|()
argument_list|,
name|clockOffset
argument_list|,
name|expiredRequired
argument_list|)
expr_stmt|;
name|JwtUtils
operator|.
name|validateJwtNotBefore
argument_list|(
name|jwt
operator|.
name|getClaims
argument_list|()
argument_list|,
name|clockOffset
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// If we have no expiry then we must have an issued at
name|boolean
name|issuedAtRequired
init|=
name|jwt
operator|.
name|getClaims
argument_list|()
operator|.
name|getExpiryTime
argument_list|()
operator|==
literal|null
decl_stmt|;
name|JwtUtils
operator|.
name|validateJwtIssuedAt
argument_list|(
name|jwt
operator|.
name|getClaims
argument_list|()
argument_list|,
name|ttl
argument_list|,
name|clockOffset
argument_list|,
name|issuedAtRequired
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getClockOffset
parameter_list|()
block|{
return|return
name|clockOffset
return|;
block|}
specifier|public
name|void
name|setClockOffset
parameter_list|(
name|int
name|clockOffset
parameter_list|)
block|{
name|this
operator|.
name|clockOffset
operator|=
name|clockOffset
expr_stmt|;
block|}
specifier|public
name|int
name|getTtl
parameter_list|()
block|{
return|return
name|ttl
return|;
block|}
specifier|public
name|void
name|setTtl
parameter_list|(
name|int
name|ttl
parameter_list|)
block|{
name|this
operator|.
name|ttl
operator|=
name|ttl
expr_stmt|;
block|}
block|}
end_class

end_unit

