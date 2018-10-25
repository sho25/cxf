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
name|tokens
operator|.
name|hawk
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|jaxrs
operator|.
name|ext
operator|.
name|MessageContext
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
name|impl
operator|.
name|MetadataMap
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
name|provider
operator|.
name|AccessTokenValidator
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
name|provider
operator|.
name|OAuthServiceException
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
specifier|public
class|class
name|HawkAccessTokenValidatorClient
extends|extends
name|AbstractHawkAccessTokenValidator
block|{
specifier|private
name|AccessTokenValidator
name|validator
decl_stmt|;
specifier|public
name|AccessTokenValidation
name|validateAccessToken
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|String
name|authScheme
parameter_list|,
name|String
name|authSchemeData
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraProps
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
if|if
condition|(
name|isRemoteSignatureValidation
argument_list|()
condition|)
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|extraProps
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|putAll
argument_list|(
name|extraProps
argument_list|)
expr_stmt|;
block|}
name|map
operator|.
name|putSingle
argument_list|(
name|HTTP_VERB
argument_list|,
name|mc
operator|.
name|getRequest
argument_list|()
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|putSingle
argument_list|(
name|HTTP_URI
argument_list|,
name|mc
operator|.
name|getUriInfo
argument_list|()
operator|.
name|getRequestUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|validator
operator|.
name|validateAccessToken
argument_list|(
name|mc
argument_list|,
name|authScheme
argument_list|,
name|authSchemeData
argument_list|,
name|map
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|validateAccessToken
argument_list|(
name|mc
argument_list|,
name|authScheme
argument_list|,
name|authSchemeData
argument_list|,
name|extraProps
argument_list|)
return|;
block|}
specifier|protected
name|AccessTokenValidation
name|getAccessTokenValidation
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|String
name|authScheme
parameter_list|,
name|String
name|authSchemeData
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraProps
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|schemeParams
parameter_list|)
block|{
return|return
name|validator
operator|.
name|validateAccessToken
argument_list|(
name|mc
argument_list|,
name|authScheme
argument_list|,
name|authSchemeData
argument_list|,
name|extraProps
argument_list|)
return|;
block|}
specifier|public
name|void
name|setValidator
parameter_list|(
name|AccessTokenValidator
name|validator
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|schemes
init|=
name|validator
operator|.
name|getSupportedAuthorizationSchemes
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|schemes
operator|.
name|contains
argument_list|(
literal|"*"
argument_list|)
operator|&&
operator|!
name|schemes
operator|.
name|contains
argument_list|(
name|OAuthConstants
operator|.
name|HAWK_AUTHORIZATION_SCHEME
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
name|this
operator|.
name|validator
operator|=
name|validator
expr_stmt|;
block|}
block|}
end_class

end_unit

