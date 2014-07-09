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
name|systest
operator|.
name|jaxrs
operator|.
name|security
operator|.
name|oauth2
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|common
operator|.
name|util
operator|.
name|Base64Utility
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
name|AccessTokenRegistration
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
name|common
operator|.
name|OAuthPermission
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
name|ServerAccessToken
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
name|UserSubject
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
name|OAuthDataProvider
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
name|saml
operator|.
name|Constants
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
name|tokens
operator|.
name|bearer
operator|.
name|BearerAccessToken
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
name|crypto
operator|.
name|CryptoUtils
import|;
end_import

begin_class
specifier|public
class|class
name|OAuthDataProviderImpl
implements|implements
name|OAuthDataProvider
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Client
argument_list|>
name|clients
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Client
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|OAuthDataProviderImpl
parameter_list|()
throws|throws
name|Exception
block|{
name|Client
name|client
init|=
operator|new
name|Client
argument_list|(
literal|"alice"
argument_list|,
literal|"alice"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|client
operator|.
name|getAllowedGrantTypes
argument_list|()
operator|.
name|add
argument_list|(
name|Constants
operator|.
name|SAML2_BEARER_GRANT
argument_list|)
expr_stmt|;
name|client
operator|.
name|getAllowedGrantTypes
argument_list|()
operator|.
name|add
argument_list|(
literal|"custom_grant"
argument_list|)
expr_stmt|;
name|clients
operator|.
name|put
argument_list|(
name|client
operator|.
name|getClientId
argument_list|()
argument_list|,
name|client
argument_list|)
expr_stmt|;
name|Certificate
name|cert
init|=
name|loadCert
argument_list|()
decl_stmt|;
name|String
name|encodedCert
init|=
name|Base64Utility
operator|.
name|encode
argument_list|(
name|cert
operator|.
name|getEncoded
argument_list|()
argument_list|)
decl_stmt|;
name|Client
name|client2
init|=
operator|new
name|Client
argument_list|(
literal|"CN=whateverhost.com,OU=Morpit,O=ApacheTest,L=Syracuse,C=US"
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|client2
operator|.
name|getAllowedGrantTypes
argument_list|()
operator|.
name|add
argument_list|(
literal|"custom_grant"
argument_list|)
expr_stmt|;
name|client2
operator|.
name|setApplicationCertificates
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|encodedCert
argument_list|)
argument_list|)
expr_stmt|;
name|clients
operator|.
name|put
argument_list|(
name|client2
operator|.
name|getClientId
argument_list|()
argument_list|,
name|client2
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Certificate
name|loadCert
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/org/apache/cxf/systest/http/resources/Truststore.jks"
argument_list|)
decl_stmt|;
return|return
name|CryptoUtils
operator|.
name|loadCertificate
argument_list|(
name|is
argument_list|,
operator|new
name|char
index|[]
block|{
literal|'p'
block|,
literal|'a'
block|,
literal|'s'
block|,
literal|'s'
block|,
literal|'w'
block|,
literal|'o'
block|,
literal|'r'
block|,
literal|'d'
block|}
argument_list|,
literal|"morpit"
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Client
name|getClient
parameter_list|(
name|String
name|clientId
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
return|return
name|clients
operator|.
name|get
argument_list|(
name|clientId
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAccessToken
name|createAccessToken
parameter_list|(
name|AccessTokenRegistration
name|accessToken
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
return|return
operator|new
name|BearerAccessToken
argument_list|(
name|accessToken
operator|.
name|getClient
argument_list|()
argument_list|,
literal|3600
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAccessToken
name|getAccessToken
parameter_list|(
name|String
name|accessToken
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAccessToken
name|getPreauthorizedToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScopes
parameter_list|,
name|UserSubject
name|subject
parameter_list|,
name|String
name|grantType
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAccessToken
name|refreshAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|refreshToken
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScopes
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeAccessToken
parameter_list|(
name|ServerAccessToken
name|accessToken
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
comment|// TODO Auto-generated method stub
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|convertScopeToPermissions
parameter_list|(
name|Client
name|client
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|revokeToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|token
parameter_list|,
name|String
name|tokenTypeHint
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
comment|// TODO Auto-generated method stub
block|}
block|}
end_class

end_unit

