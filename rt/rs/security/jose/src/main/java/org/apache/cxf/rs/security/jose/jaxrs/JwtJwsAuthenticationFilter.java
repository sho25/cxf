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
name|message
operator|.
name|Message
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
name|jws
operator|.
name|JwsCompactConsumer
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
name|jws
operator|.
name|JwsJwtCompactConsumer
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
name|jws
operator|.
name|JwsSignatureVerifier
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
name|JWS_SERVER_READ_PRIORITY
argument_list|)
specifier|public
class|class
name|JwtJwsAuthenticationFilter
extends|extends
name|AbstractJwsReaderProvider
implements|implements
name|ContainerRequestFilter
block|{
specifier|private
specifier|static
specifier|final
name|String
name|JWS_CONTEXT_PROPERTY
init|=
literal|"org.apache.cxf.jws.context"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JWT_SCHEME_PROPERTY
init|=
literal|"JWT"
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|context
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|authHeader
init|=
name|context
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
name|schemeData
init|=
name|authHeader
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
decl_stmt|;
if|if
condition|(
name|schemeData
operator|.
name|length
operator|!=
literal|2
operator|||
operator|!
name|JWT_SCHEME_PROPERTY
operator|.
name|equals
argument_list|(
name|schemeData
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
name|JwsJwtCompactConsumer
name|p
init|=
operator|new
name|JwsJwtCompactConsumer
argument_list|(
name|schemeData
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|JwsSignatureVerifier
name|theSigVerifier
init|=
name|getInitializedSigVerifier
argument_list|(
name|p
operator|.
name|getJoseHeaders
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|p
operator|.
name|verifySignatureWith
argument_list|(
name|theSigVerifier
argument_list|)
condition|)
block|{
name|context
operator|.
name|abortWith
argument_list|(
name|JAXRSUtils
operator|.
name|toResponse
argument_list|(
literal|400
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|Message
name|m
init|=
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|setRequestContextProperty
argument_list|(
name|m
argument_list|,
name|p
argument_list|)
expr_stmt|;
name|JwtToken
name|token
init|=
name|p
operator|.
name|getJwtToken
argument_list|()
decl_stmt|;
name|m
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
specifier|protected
name|void
name|setRequestContextProperty
parameter_list|(
name|Message
name|m
parameter_list|,
name|JwsCompactConsumer
name|c
parameter_list|)
block|{
name|Object
name|headerContext
init|=
name|c
operator|.
name|getJoseHeaders
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JWS_CONTEXT_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|headerContext
operator|!=
literal|null
condition|)
block|{
name|m
operator|.
name|put
argument_list|(
name|JWS_CONTEXT_PROPERTY
argument_list|,
name|headerContext
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

