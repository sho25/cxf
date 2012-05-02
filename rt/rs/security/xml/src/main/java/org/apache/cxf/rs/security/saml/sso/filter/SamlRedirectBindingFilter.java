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
name|saml
operator|.
name|sso
operator|.
name|filter
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
name|WebApplicationException
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|model
operator|.
name|ClassResourceInfo
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
name|saml
operator|.
name|sso
operator|.
name|SSOConstants
import|;
end_import

begin_class
specifier|public
class|class
name|SamlRedirectBindingFilter
extends|extends
name|AbstractServiceProviderFilter
block|{
specifier|public
name|Response
name|handleRequest
parameter_list|(
name|Message
name|m
parameter_list|,
name|ClassResourceInfo
name|resourceClass
parameter_list|)
block|{
if|if
condition|(
name|checkSecurityContext
argument_list|(
name|m
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
try|try
block|{
name|SamlRequestInfo
name|info
init|=
name|createSamlRequestInfo
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|UriBuilder
name|ub
init|=
name|UriBuilder
operator|.
name|fromUri
argument_list|(
name|getIdpServiceAddress
argument_list|()
argument_list|)
decl_stmt|;
name|ub
operator|.
name|queryParam
argument_list|(
name|SSOConstants
operator|.
name|SAML_REQUEST
argument_list|,
name|info
operator|.
name|getEncodedSamlRequest
argument_list|()
argument_list|)
expr_stmt|;
name|ub
operator|.
name|queryParam
argument_list|(
name|SSOConstants
operator|.
name|RELAY_STATE
argument_list|,
name|info
operator|.
name|getRelayState
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|seeOther
argument_list|(
name|ub
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|CACHE_CONTROL
argument_list|,
literal|"no-store"
argument_list|)
operator|.
name|header
argument_list|(
literal|"Pragma"
argument_list|,
literal|"no-cache"
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

