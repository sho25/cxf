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
name|oidc
operator|.
name|idp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

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
name|Client
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
name|services
operator|.
name|ClientRegistration
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
name|services
operator|.
name|ClientRegistrationResponse
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
name|services
operator|.
name|DynamicRegistrationService
import|;
end_import

begin_class
specifier|public
class|class
name|OidcDynamicRegistrationService
extends|extends
name|DynamicRegistrationService
block|{
specifier|private
specifier|static
specifier|final
name|String
name|RP_INITIATED_LOGOUT_URIS
init|=
literal|"post_logout_redirect_uris"
decl_stmt|;
specifier|private
name|boolean
name|protectIdTokenWithClientSecret
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|Client
name|createNewClient
parameter_list|(
name|ClientRegistration
name|request
parameter_list|)
block|{
name|Client
name|client
init|=
name|super
operator|.
name|createNewClient
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|logoutUris
init|=
name|request
operator|.
name|getListStringProperty
argument_list|(
name|RP_INITIATED_LOGOUT_URIS
argument_list|)
decl_stmt|;
if|if
condition|(
name|logoutUris
operator|!=
literal|null
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|uri
range|:
name|logoutUris
control|)
block|{
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
name|client
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|RP_INITIATED_LOGOUT_URIS
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
annotation|@
name|Override
specifier|protected
name|ClientRegistrationResponse
name|fromClientToRegistrationResponse
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
name|ClientRegistrationResponse
name|resp
init|=
name|super
operator|.
name|fromClientToRegistrationResponse
argument_list|(
name|client
argument_list|)
decl_stmt|;
name|String
name|logoutUris
init|=
name|client
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|RP_INITIATED_LOGOUT_URIS
argument_list|)
decl_stmt|;
if|if
condition|(
name|logoutUris
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|logoutUris
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
name|resp
operator|.
name|setProperty
argument_list|(
name|RP_INITIATED_LOGOUT_URIS
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
return|return
name|resp
return|;
block|}
annotation|@
name|Override
specifier|protected
name|ClientRegistration
name|fromClientToClientRegistration
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
comment|//TODO: check OIDC specific properties in Client extra properties
return|return
name|super
operator|.
name|fromClientToClientRegistration
argument_list|(
name|client
argument_list|)
return|;
block|}
specifier|protected
name|int
name|getClientSecretSizeInBytes
parameter_list|(
name|ClientRegistration
name|request
parameter_list|)
block|{
comment|// TODO: may need to be 384/8 or 512/8 if not a default HS256 but HS384 or HS512
name|int
name|keySizeOctets
init|=
name|protectIdTokenWithClientSecret
condition|?
literal|32
else|:
name|super
operator|.
name|getClientSecretSizeInBytes
argument_list|(
name|request
argument_list|)
decl_stmt|;
return|return
name|keySizeOctets
return|;
block|}
specifier|public
name|void
name|setProtectIdTokenWithClientSecret
parameter_list|(
name|boolean
name|protectIdTokenWithClientSecret
parameter_list|)
block|{
name|this
operator|.
name|protectIdTokenWithClientSecret
operator|=
name|protectIdTokenWithClientSecret
expr_stmt|;
block|}
block|}
end_class

end_unit

