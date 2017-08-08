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
name|oauth2
operator|.
name|services
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
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
name|Path
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
name|Produces
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
name|Context
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
name|UriBuilder
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
name|UriInfo
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
name|json
operator|.
name|basic
operator|.
name|JsonMapObjectReaderWriter
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"oauth-authorization-server"
argument_list|)
specifier|public
class|class
name|AuthorizationMetadataService
block|{
specifier|private
name|String
name|issuer
decl_stmt|;
comment|// Required
specifier|private
name|String
name|authorizationEndpointAddress
decl_stmt|;
comment|// Optional if only an implicit flow is used
specifier|private
name|boolean
name|tokenEndpointNotAvailable
decl_stmt|;
specifier|private
name|String
name|tokenEndpointAddress
decl_stmt|;
comment|// Optional
specifier|private
name|boolean
name|tokenRevocationEndpointNotAvailable
decl_stmt|;
specifier|private
name|String
name|tokenRevocationEndpointAddress
decl_stmt|;
comment|// Required for OIDC, optional otherwise
specifier|private
name|boolean
name|jwkEndpointNotAvailable
decl_stmt|;
specifier|private
name|String
name|jwkEndpointAddress
decl_stmt|;
comment|// Optional
specifier|private
name|boolean
name|dynamicRegistrationEndpointNotAvailable
decl_stmt|;
specifier|private
name|String
name|dynamicRegistrationEndpointAddress
decl_stmt|;
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
specifier|public
name|String
name|getConfiguration
parameter_list|(
annotation|@
name|Context
name|UriInfo
name|ui
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cfg
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|String
name|baseUri
init|=
name|getBaseUri
argument_list|(
name|ui
argument_list|)
decl_stmt|;
name|prepareConfigurationData
argument_list|(
name|cfg
argument_list|,
name|baseUri
argument_list|)
expr_stmt|;
name|JsonMapObjectReaderWriter
name|writer
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
decl_stmt|;
name|writer
operator|.
name|setFormat
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|writer
operator|.
name|toJson
argument_list|(
name|cfg
argument_list|)
return|;
block|}
specifier|protected
name|void
name|prepareConfigurationData
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cfg
parameter_list|,
name|String
name|baseUri
parameter_list|)
block|{
comment|// Issuer
name|cfg
operator|.
name|put
argument_list|(
literal|"issuer"
argument_list|,
name|issuer
operator|==
literal|null
condition|?
name|baseUri
else|:
name|issuer
argument_list|)
expr_stmt|;
comment|// Authorization Endpoint
name|String
name|theAuthorizationEndpointAddress
init|=
name|calculateEndpointAddress
argument_list|(
name|authorizationEndpointAddress
argument_list|,
name|baseUri
argument_list|,
literal|"/idp/authorize"
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|put
argument_list|(
literal|"authorization_endpoint"
argument_list|,
name|theAuthorizationEndpointAddress
argument_list|)
expr_stmt|;
comment|// Token Endpoint
if|if
condition|(
operator|!
name|isTokenEndpointNotAvailable
argument_list|()
condition|)
block|{
name|String
name|theTokenEndpointAddress
init|=
name|calculateEndpointAddress
argument_list|(
name|tokenEndpointAddress
argument_list|,
name|baseUri
argument_list|,
literal|"/oauth2/token"
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|put
argument_list|(
literal|"token_endpoint"
argument_list|,
name|theTokenEndpointAddress
argument_list|)
expr_stmt|;
block|}
comment|// Token Revocation Endpoint
if|if
condition|(
operator|!
name|isTokenRevocationEndpointNotAvailable
argument_list|()
condition|)
block|{
name|String
name|theTokenRevocationEndpointAddress
init|=
name|calculateEndpointAddress
argument_list|(
name|tokenRevocationEndpointAddress
argument_list|,
name|baseUri
argument_list|,
literal|"/oauth2/revoke"
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|put
argument_list|(
literal|"revocation_endpoint"
argument_list|,
name|theTokenRevocationEndpointAddress
argument_list|)
expr_stmt|;
block|}
comment|// Jwks Uri Endpoint
if|if
condition|(
operator|!
name|isJwkEndpointNotAvailable
argument_list|()
condition|)
block|{
name|String
name|theJwkEndpointAddress
init|=
name|calculateEndpointAddress
argument_list|(
name|jwkEndpointAddress
argument_list|,
name|baseUri
argument_list|,
literal|"/jwk/keys"
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|put
argument_list|(
literal|"jwks_uri"
argument_list|,
name|theJwkEndpointAddress
argument_list|)
expr_stmt|;
block|}
comment|// Dynamic Registration Endpoint
if|if
condition|(
operator|!
name|isDynamicRegistrationEndpointNotAvailable
argument_list|()
condition|)
block|{
name|String
name|theDynamicRegistrationEndpointAddress
init|=
name|calculateEndpointAddress
argument_list|(
name|dynamicRegistrationEndpointAddress
argument_list|,
name|baseUri
argument_list|,
literal|"/dynamic/register"
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|put
argument_list|(
literal|"registration_endpoint"
argument_list|,
name|theDynamicRegistrationEndpointAddress
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|static
name|String
name|calculateEndpointAddress
parameter_list|(
name|String
name|endpointAddress
parameter_list|,
name|String
name|baseUri
parameter_list|,
name|String
name|defRelAddress
parameter_list|)
block|{
name|endpointAddress
operator|=
name|endpointAddress
operator|!=
literal|null
condition|?
name|endpointAddress
else|:
name|defRelAddress
expr_stmt|;
if|if
condition|(
name|endpointAddress
operator|.
name|startsWith
argument_list|(
literal|"https"
argument_list|)
condition|)
block|{
return|return
name|endpointAddress
return|;
block|}
return|return
name|UriBuilder
operator|.
name|fromUri
argument_list|(
name|baseUri
argument_list|)
operator|.
name|path
argument_list|(
name|endpointAddress
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|String
name|getBaseUri
parameter_list|(
name|UriInfo
name|ui
parameter_list|)
block|{
name|String
name|requestUri
init|=
name|ui
operator|.
name|getRequestUri
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|int
name|ind
init|=
name|requestUri
operator|.
name|lastIndexOf
argument_list|(
literal|".well-known"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ind
operator|!=
operator|-
literal|1
condition|)
block|{
name|requestUri
operator|=
name|requestUri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|ind
argument_list|)
expr_stmt|;
block|}
return|return
name|requestUri
return|;
block|}
specifier|public
name|void
name|setIssuer
parameter_list|(
name|String
name|issuer
parameter_list|)
block|{
name|this
operator|.
name|issuer
operator|=
name|issuer
expr_stmt|;
block|}
specifier|public
name|void
name|setAuthorizationEndpointAddress
parameter_list|(
name|String
name|authorizationEndpointAddress
parameter_list|)
block|{
name|this
operator|.
name|authorizationEndpointAddress
operator|=
name|authorizationEndpointAddress
expr_stmt|;
block|}
specifier|public
name|void
name|setTokenEndpointAddress
parameter_list|(
name|String
name|tokenEndpointAddress
parameter_list|)
block|{
name|this
operator|.
name|tokenEndpointAddress
operator|=
name|tokenEndpointAddress
expr_stmt|;
block|}
specifier|public
name|void
name|setJwkEndpointAddress
parameter_list|(
name|String
name|jwkEndpointAddress
parameter_list|)
block|{
name|this
operator|.
name|jwkEndpointAddress
operator|=
name|jwkEndpointAddress
expr_stmt|;
block|}
specifier|public
name|void
name|setTokenRevocationEndpointAddress
parameter_list|(
name|String
name|tokenRevocationEndpointAddress
parameter_list|)
block|{
name|this
operator|.
name|tokenRevocationEndpointAddress
operator|=
name|tokenRevocationEndpointAddress
expr_stmt|;
block|}
specifier|public
name|void
name|setTokenRevocationEndpointNotAvailable
parameter_list|(
name|boolean
name|tokenRevocationEndpointNotAvailable
parameter_list|)
block|{
name|this
operator|.
name|tokenRevocationEndpointNotAvailable
operator|=
name|tokenRevocationEndpointNotAvailable
expr_stmt|;
block|}
specifier|public
name|boolean
name|isTokenRevocationEndpointNotAvailable
parameter_list|()
block|{
return|return
name|tokenRevocationEndpointNotAvailable
return|;
block|}
specifier|public
name|void
name|setJwkEndpointNotAvailable
parameter_list|(
name|boolean
name|jwkEndpointNotAvailable
parameter_list|)
block|{
name|this
operator|.
name|jwkEndpointNotAvailable
operator|=
name|jwkEndpointNotAvailable
expr_stmt|;
block|}
specifier|public
name|boolean
name|isJwkEndpointNotAvailable
parameter_list|()
block|{
return|return
name|jwkEndpointNotAvailable
return|;
block|}
specifier|public
name|boolean
name|isTokenEndpointNotAvailable
parameter_list|()
block|{
return|return
name|tokenEndpointNotAvailable
return|;
block|}
specifier|public
name|void
name|setTokenEndpointNotAvailable
parameter_list|(
name|boolean
name|tokenEndpointNotAvailable
parameter_list|)
block|{
name|this
operator|.
name|tokenEndpointNotAvailable
operator|=
name|tokenEndpointNotAvailable
expr_stmt|;
block|}
specifier|public
name|boolean
name|isDynamicRegistrationEndpointNotAvailable
parameter_list|()
block|{
return|return
name|dynamicRegistrationEndpointNotAvailable
return|;
block|}
specifier|public
name|void
name|setDynamicRegistrationEndpointNotAvailable
parameter_list|(
name|boolean
name|dynamicRegistrationEndpointNotAvailable
parameter_list|)
block|{
name|this
operator|.
name|dynamicRegistrationEndpointNotAvailable
operator|=
name|dynamicRegistrationEndpointNotAvailable
expr_stmt|;
block|}
specifier|public
name|String
name|getDynamicRegistrationEndpointAddress
parameter_list|()
block|{
return|return
name|dynamicRegistrationEndpointAddress
return|;
block|}
specifier|public
name|void
name|setDynamicRegistrationEndpointAddress
parameter_list|(
name|String
name|dynamicRegistrationEndpointAddress
parameter_list|)
block|{
name|this
operator|.
name|dynamicRegistrationEndpointAddress
operator|=
name|dynamicRegistrationEndpointAddress
expr_stmt|;
block|}
block|}
end_class

end_unit

