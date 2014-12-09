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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Consumes
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
name|Encoded
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
name|POST
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
name|MediaType
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
name|MultivaluedMap
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
name|oauth2
operator|.
name|common
operator|.
name|AccessTokenValidation
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
name|oauth2
operator|.
name|utils
operator|.
name|AuthorizationUtils
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
name|oauth2
operator|.
name|utils
operator|.
name|OAuthConstants
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"validate"
argument_list|)
specifier|public
class|class
name|AccessTokenValidatorService
extends|extends
name|AbstractAccessTokenValidator
block|{
annotation|@
name|POST
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_XML
block|,
name|MediaType
operator|.
name|APPLICATION_JSON
block|}
argument_list|)
annotation|@
name|Consumes
argument_list|(
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED
argument_list|)
specifier|public
name|AccessTokenValidation
name|getTokenValidationInfo
parameter_list|(
annotation|@
name|Encoded
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
if|if
condition|(
name|getMessageContext
argument_list|()
operator|.
name|getSecurityContext
argument_list|()
operator|.
name|getUserPrincipal
argument_list|()
operator|==
literal|null
condition|)
block|{
name|AuthorizationUtils
operator|.
name|throwAuthorizationFailure
argument_list|(
name|supportedSchemes
argument_list|,
name|realm
argument_list|)
expr_stmt|;
block|}
name|String
name|authScheme
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_SCHEME_TYPE
argument_list|)
decl_stmt|;
name|String
name|authSchemeData
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_SCHEME_DATA
argument_list|)
decl_stmt|;
return|return
name|super
operator|.
name|getAccessTokenValidation
argument_list|(
name|authScheme
argument_list|,
name|authSchemeData
argument_list|)
return|;
block|}
block|}
end_class

end_unit

