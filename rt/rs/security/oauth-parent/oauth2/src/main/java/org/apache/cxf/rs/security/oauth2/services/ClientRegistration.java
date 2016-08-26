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
name|JsonMapObject
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
name|ClientRegistration
extends|extends
name|JsonMapObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|REDIRECT_URIS
init|=
literal|"redirect_uris"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RESPONSE_TYPES
init|=
literal|"response_types"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|GRANT_TYPES
init|=
literal|"grant_types"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|APPLICATION_TYPE
init|=
literal|"application_type"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONTACTS
init|=
literal|"contacts"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_NAME
init|=
literal|"client_name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LOGO_URI
init|=
literal|"logo_uri"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_URI
init|=
literal|"client_uri"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|POLICY_URI
init|=
literal|"policy_uri"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOS_URI
init|=
literal|"tos_uri"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOKEN_ENDPOINT_AUTH_METHOD
init|=
literal|"token_endpoint_auth_method"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SCOPE
init|=
name|OAuthConstants
operator|.
name|SCOPE
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|7903976943604132150L
decl_stmt|;
specifier|public
name|ClientRegistration
parameter_list|()
block|{     }
specifier|public
name|ClientRegistration
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|props
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setRedirectUris
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|redirectUris
parameter_list|)
block|{
name|super
operator|.
name|setProperty
argument_list|(
name|REDIRECT_URIS
argument_list|,
name|redirectUris
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRedirectUris
parameter_list|()
block|{
return|return
name|getListStringProperty
argument_list|(
name|REDIRECT_URIS
argument_list|)
return|;
block|}
specifier|public
name|void
name|setResponseTypes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|responseTypes
parameter_list|)
block|{
name|super
operator|.
name|setProperty
argument_list|(
name|RESPONSE_TYPES
argument_list|,
name|responseTypes
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getResponseTypes
parameter_list|()
block|{
return|return
name|getListStringProperty
argument_list|(
name|RESPONSE_TYPES
argument_list|)
return|;
block|}
specifier|public
name|void
name|setGrantTypes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|grantTypes
parameter_list|)
block|{
name|super
operator|.
name|setProperty
argument_list|(
name|GRANT_TYPES
argument_list|,
name|grantTypes
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getGrantTypes
parameter_list|()
block|{
return|return
name|getListStringProperty
argument_list|(
name|GRANT_TYPES
argument_list|)
return|;
block|}
specifier|public
name|void
name|setApplicationType
parameter_list|(
name|String
name|applicationType
parameter_list|)
block|{
name|super
operator|.
name|setProperty
argument_list|(
name|APPLICATION_TYPE
argument_list|,
name|applicationType
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getApplicationType
parameter_list|()
block|{
return|return
name|getStringProperty
argument_list|(
name|APPLICATION_TYPE
argument_list|)
return|;
block|}
specifier|public
name|void
name|setContacts
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|contacts
parameter_list|)
block|{
name|super
operator|.
name|setProperty
argument_list|(
name|CONTACTS
argument_list|,
name|contacts
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getContacts
parameter_list|()
block|{
return|return
name|getListStringProperty
argument_list|(
name|CONTACTS
argument_list|)
return|;
block|}
specifier|public
name|void
name|setClientName
parameter_list|(
name|String
name|clientName
parameter_list|)
block|{
name|super
operator|.
name|setProperty
argument_list|(
name|CLIENT_NAME
argument_list|,
name|clientName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getClientName
parameter_list|()
block|{
return|return
name|getStringProperty
argument_list|(
name|CLIENT_NAME
argument_list|)
return|;
block|}
specifier|public
name|void
name|setLogoUri
parameter_list|(
name|String
name|logoUri
parameter_list|)
block|{
name|super
operator|.
name|setProperty
argument_list|(
name|LOGO_URI
argument_list|,
name|logoUri
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getLogoUri
parameter_list|()
block|{
return|return
name|getStringProperty
argument_list|(
name|LOGO_URI
argument_list|)
return|;
block|}
specifier|public
name|void
name|setClientUri
parameter_list|(
name|String
name|clientUri
parameter_list|)
block|{
name|super
operator|.
name|setProperty
argument_list|(
name|CLIENT_URI
argument_list|,
name|clientUri
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getClientUri
parameter_list|()
block|{
return|return
name|getStringProperty
argument_list|(
name|CLIENT_URI
argument_list|)
return|;
block|}
specifier|public
name|void
name|setPolicyUri
parameter_list|(
name|String
name|policyUri
parameter_list|)
block|{
name|super
operator|.
name|setProperty
argument_list|(
name|POLICY_URI
argument_list|,
name|policyUri
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPolicyUri
parameter_list|()
block|{
return|return
name|getStringProperty
argument_list|(
name|POLICY_URI
argument_list|)
return|;
block|}
specifier|public
name|void
name|setTosUri
parameter_list|(
name|String
name|tosUri
parameter_list|)
block|{
name|super
operator|.
name|setProperty
argument_list|(
name|TOS_URI
argument_list|,
name|tosUri
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getTosUri
parameter_list|()
block|{
return|return
name|getStringProperty
argument_list|(
name|TOS_URI
argument_list|)
return|;
block|}
specifier|public
name|void
name|setTokenEndpointAuthMethod
parameter_list|(
name|String
name|method
parameter_list|)
block|{
name|super
operator|.
name|setProperty
argument_list|(
name|TOKEN_ENDPOINT_AUTH_METHOD
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getTokenEndpointAuthMethod
parameter_list|()
block|{
return|return
name|getStringProperty
argument_list|(
name|TOKEN_ENDPOINT_AUTH_METHOD
argument_list|)
return|;
block|}
specifier|public
name|void
name|setScope
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
name|super
operator|.
name|setProperty
argument_list|(
name|SCOPE
argument_list|,
name|scope
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getScope
parameter_list|()
block|{
return|return
name|getStringProperty
argument_list|(
name|SCOPE
argument_list|)
return|;
block|}
block|}
end_class

end_unit
