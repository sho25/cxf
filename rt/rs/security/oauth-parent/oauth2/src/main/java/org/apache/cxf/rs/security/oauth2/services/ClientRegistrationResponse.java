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
name|ClientRegistrationResponse
extends|extends
name|JsonMapObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_ID
init|=
name|OAuthConstants
operator|.
name|CLIENT_ID
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_SECRET
init|=
name|OAuthConstants
operator|.
name|CLIENT_SECRET
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REG_ACCESS_TOKEN
init|=
literal|"registration_access_token"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REG_CLIENT_URI
init|=
literal|"registration_client_uri"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_ID_ISSUED_AT
init|=
literal|"client_id_issued_at"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_SECRET_EXPIRES_AT
init|=
literal|"client_secret_expires_at"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|7114757825909879652L
decl_stmt|;
specifier|public
name|ClientRegistrationResponse
parameter_list|()
block|{     }
specifier|public
name|ClientRegistrationResponse
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
name|setClientId
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|super
operator|.
name|setProperty
argument_list|(
name|CLIENT_ID
argument_list|,
name|clientId
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getClientId
parameter_list|()
block|{
return|return
name|getStringProperty
argument_list|(
name|CLIENT_ID
argument_list|)
return|;
block|}
specifier|public
name|void
name|setClientSecret
parameter_list|(
name|String
name|clientSecret
parameter_list|)
block|{
name|super
operator|.
name|setProperty
argument_list|(
name|CLIENT_SECRET
argument_list|,
name|clientSecret
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getClientSecret
parameter_list|()
block|{
return|return
name|getStringProperty
argument_list|(
name|CLIENT_SECRET
argument_list|)
return|;
block|}
specifier|public
name|void
name|setRegistrationAccessToken
parameter_list|(
name|String
name|at
parameter_list|)
block|{
name|super
operator|.
name|setProperty
argument_list|(
name|REG_ACCESS_TOKEN
argument_list|,
name|at
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getRegistrationAccessToken
parameter_list|()
block|{
return|return
name|getStringProperty
argument_list|(
name|REG_ACCESS_TOKEN
argument_list|)
return|;
block|}
specifier|public
name|void
name|setRegistrationClientUri
parameter_list|(
name|String
name|at
parameter_list|)
block|{
name|super
operator|.
name|setProperty
argument_list|(
name|REG_CLIENT_URI
argument_list|,
name|at
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getRegistrationClientUri
parameter_list|()
block|{
return|return
name|getStringProperty
argument_list|(
name|REG_CLIENT_URI
argument_list|)
return|;
block|}
specifier|public
name|void
name|setClientIdIssuedAt
parameter_list|(
name|Long
name|issuedAt
parameter_list|)
block|{
name|super
operator|.
name|setProperty
argument_list|(
name|CLIENT_ID_ISSUED_AT
argument_list|,
name|issuedAt
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Long
name|getClientIdIssuedAt
parameter_list|()
block|{
return|return
name|getLongProperty
argument_list|(
name|CLIENT_ID_ISSUED_AT
argument_list|)
return|;
block|}
specifier|public
name|void
name|setClientSecretExpiresAt
parameter_list|(
name|Long
name|expiresAt
parameter_list|)
block|{
name|super
operator|.
name|setProperty
argument_list|(
name|CLIENT_ID_ISSUED_AT
argument_list|,
name|expiresAt
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Long
name|getClientSecretExpiresAt
parameter_list|()
block|{
return|return
name|getLongProperty
argument_list|(
name|CLIENT_SECRET_EXPIRES_AT
argument_list|)
return|;
block|}
block|}
end_class

end_unit

