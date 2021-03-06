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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|common
operator|.
name|JoseException
import|;
end_import

begin_class
specifier|public
class|class
name|JwtAuthenticationFilter
extends|extends
name|AbstractJwtAuthenticationFilter
block|{
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
specifier|protected
name|String
name|getEncodedJwtToken
parameter_list|(
name|ContainerRequestContext
name|requestContext
parameter_list|)
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
return|return
name|parts
index|[
literal|1
index|]
return|;
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
block|}
end_class

end_unit

