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
operator|.
name|common
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
name|ArrayList
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
name|grants
operator|.
name|code
operator|.
name|DefaultEHCacheCodeDataProvider
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
name|rt
operator|.
name|security
operator|.
name|crypto
operator|.
name|CryptoUtils
import|;
end_import

begin_comment
comment|/**  * Extend the DefaultEHCacheCodeDataProvider to allow refreshing of tokens  */
end_comment

begin_class
specifier|public
class|class
name|OAuthDataProviderImpl
extends|extends
name|DefaultEHCacheCodeDataProvider
block|{
specifier|public
name|OAuthDataProviderImpl
parameter_list|(
name|String
name|servicePort
parameter_list|)
throws|throws
name|Exception
block|{
comment|// filters/grants test client
name|Client
name|client
init|=
operator|new
name|Client
argument_list|(
literal|"consumer-id"
argument_list|,
literal|"this-is-a-secret"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|client
operator|.
name|setRedirectUris
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"http://www.blah.apache.org"
argument_list|)
argument_list|)
expr_stmt|;
name|client
operator|.
name|getAllowedGrantTypes
argument_list|()
operator|.
name|add
argument_list|(
literal|"authorization_code"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getAllowedGrantTypes
argument_list|()
operator|.
name|add
argument_list|(
literal|"refresh_token"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getAllowedGrantTypes
argument_list|()
operator|.
name|add
argument_list|(
literal|"implicit"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getAllowedGrantTypes
argument_list|()
operator|.
name|add
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getAllowedGrantTypes
argument_list|()
operator|.
name|add
argument_list|(
literal|"client_credentials"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getAllowedGrantTypes
argument_list|()
operator|.
name|add
argument_list|(
literal|"urn:ietf:params:oauth:grant-type:saml2-bearer"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getAllowedGrantTypes
argument_list|()
operator|.
name|add
argument_list|(
literal|"urn:ietf:params:oauth:grant-type:jwt-bearer"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getRegisteredScopes
argument_list|()
operator|.
name|add
argument_list|(
literal|"read_balance"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getRegisteredScopes
argument_list|()
operator|.
name|add
argument_list|(
literal|"create_balance"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getRegisteredScopes
argument_list|()
operator|.
name|add
argument_list|(
literal|"read_data"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getRegisteredScopes
argument_list|()
operator|.
name|add
argument_list|(
literal|"read_book"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getRegisteredScopes
argument_list|()
operator|.
name|add
argument_list|(
literal|"create_book"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getRegisteredScopes
argument_list|()
operator|.
name|add
argument_list|(
literal|"create_image"
argument_list|)
expr_stmt|;
name|this
operator|.
name|setClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
comment|// Audience test client
name|client
operator|=
operator|new
name|Client
argument_list|(
literal|"consumer-id-aud"
argument_list|,
literal|"this-is-a-secret"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|client
operator|.
name|setRedirectUris
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"http://www.blah.apache.org"
argument_list|)
argument_list|)
expr_stmt|;
name|client
operator|.
name|getAllowedGrantTypes
argument_list|()
operator|.
name|add
argument_list|(
literal|"authorization_code"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getAllowedGrantTypes
argument_list|()
operator|.
name|add
argument_list|(
literal|"refresh_token"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getRegisteredAudiences
argument_list|()
operator|.
name|add
argument_list|(
literal|"https://localhost:"
operator|+
name|servicePort
operator|+
literal|"/secured/bookstore/books"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getRegisteredAudiences
argument_list|()
operator|.
name|add
argument_list|(
literal|"https://127.0.0.1/test"
argument_list|)
expr_stmt|;
name|this
operator|.
name|setClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
comment|// Audience test client 2
name|client
operator|=
operator|new
name|Client
argument_list|(
literal|"consumer-id-aud2"
argument_list|,
literal|"this-is-a-secret"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|client
operator|.
name|setRedirectUris
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"http://www.blah.apache.org"
argument_list|)
argument_list|)
expr_stmt|;
name|client
operator|.
name|getAllowedGrantTypes
argument_list|()
operator|.
name|add
argument_list|(
literal|"authorization_code"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getAllowedGrantTypes
argument_list|()
operator|.
name|add
argument_list|(
literal|"refresh_token"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getRegisteredAudiences
argument_list|()
operator|.
name|add
argument_list|(
literal|"https://localhost:"
operator|+
name|servicePort
operator|+
literal|"/securedxyz/bookstore/books"
argument_list|)
expr_stmt|;
name|this
operator|.
name|setClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
comment|// JAXRSOAuth2Test clients
name|client
operator|=
operator|new
name|Client
argument_list|(
literal|"alice"
argument_list|,
literal|"alice"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
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
literal|"urn:ietf:params:oauth:grant-type:jwt-bearer"
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
name|this
operator|.
name|setClient
argument_list|(
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
name|this
operator|.
name|setClient
argument_list|(
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
specifier|protected
name|boolean
name|isRefreshTokenSupported
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|theScopes
parameter_list|)
block|{
return|return
literal|true
return|;
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
name|requestedScopes
parameter_list|)
block|{
if|if
condition|(
name|requestedScopes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|permissions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|requestedScope
range|:
name|requestedScopes
control|)
block|{
if|if
condition|(
literal|"read_book"
operator|.
name|equals
argument_list|(
name|requestedScope
argument_list|)
condition|)
block|{
name|OAuthPermission
name|permission
init|=
operator|new
name|OAuthPermission
argument_list|(
literal|"read_book"
argument_list|)
decl_stmt|;
name|permission
operator|.
name|setHttpVerbs
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"GET"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|uris
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|partnerAddress
init|=
literal|"/secured/bookstore/books/*"
decl_stmt|;
name|uris
operator|.
name|add
argument_list|(
name|partnerAddress
argument_list|)
expr_stmt|;
name|permission
operator|.
name|setUris
argument_list|(
name|uris
argument_list|)
expr_stmt|;
name|permissions
operator|.
name|add
argument_list|(
name|permission
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"create_book"
operator|.
name|equals
argument_list|(
name|requestedScope
argument_list|)
condition|)
block|{
name|OAuthPermission
name|permission
init|=
operator|new
name|OAuthPermission
argument_list|(
literal|"create_book"
argument_list|)
decl_stmt|;
name|permission
operator|.
name|setHttpVerbs
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"POST"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|uris
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|partnerAddress
init|=
literal|"/secured/bookstore/books/*"
decl_stmt|;
name|uris
operator|.
name|add
argument_list|(
name|partnerAddress
argument_list|)
expr_stmt|;
name|permission
operator|.
name|setUris
argument_list|(
name|uris
argument_list|)
expr_stmt|;
name|permissions
operator|.
name|add
argument_list|(
name|permission
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"create_image"
operator|.
name|equals
argument_list|(
name|requestedScope
argument_list|)
condition|)
block|{
name|OAuthPermission
name|permission
init|=
operator|new
name|OAuthPermission
argument_list|(
literal|"create_image"
argument_list|)
decl_stmt|;
name|permission
operator|.
name|setHttpVerbs
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"POST"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|uris
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|partnerAddress
init|=
literal|"/secured/bookstore/image/*"
decl_stmt|;
name|uris
operator|.
name|add
argument_list|(
name|partnerAddress
argument_list|)
expr_stmt|;
name|permission
operator|.
name|setUris
argument_list|(
name|uris
argument_list|)
expr_stmt|;
name|permissions
operator|.
name|add
argument_list|(
name|permission
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"read_balance"
operator|.
name|equals
argument_list|(
name|requestedScope
argument_list|)
condition|)
block|{
name|OAuthPermission
name|permission
init|=
operator|new
name|OAuthPermission
argument_list|(
literal|"read_balance"
argument_list|)
decl_stmt|;
name|permission
operator|.
name|setHttpVerbs
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"GET"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|uris
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|partnerAddress
init|=
literal|"/partners/balance/*"
decl_stmt|;
name|uris
operator|.
name|add
argument_list|(
name|partnerAddress
argument_list|)
expr_stmt|;
name|permission
operator|.
name|setUris
argument_list|(
name|uris
argument_list|)
expr_stmt|;
name|permissions
operator|.
name|add
argument_list|(
name|permission
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"create_balance"
operator|.
name|equals
argument_list|(
name|requestedScope
argument_list|)
condition|)
block|{
name|OAuthPermission
name|permission
init|=
operator|new
name|OAuthPermission
argument_list|(
literal|"create_balance"
argument_list|)
decl_stmt|;
name|permission
operator|.
name|setHttpVerbs
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"POST"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|uris
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|partnerAddress
init|=
literal|"/partners/balance/*"
decl_stmt|;
name|uris
operator|.
name|add
argument_list|(
name|partnerAddress
argument_list|)
expr_stmt|;
name|permission
operator|.
name|setUris
argument_list|(
name|uris
argument_list|)
expr_stmt|;
name|permissions
operator|.
name|add
argument_list|(
name|permission
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"read_data"
operator|.
name|equals
argument_list|(
name|requestedScope
argument_list|)
condition|)
block|{
name|OAuthPermission
name|permission
init|=
operator|new
name|OAuthPermission
argument_list|(
literal|"read_data"
argument_list|)
decl_stmt|;
name|permission
operator|.
name|setHttpVerbs
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"GET"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|uris
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|partnerAddress
init|=
literal|"/partners/data/*"
decl_stmt|;
name|uris
operator|.
name|add
argument_list|(
name|partnerAddress
argument_list|)
expr_stmt|;
name|permission
operator|.
name|setUris
argument_list|(
name|uris
argument_list|)
expr_stmt|;
name|permissions
operator|.
name|add
argument_list|(
name|permission
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"invalid_scope"
argument_list|)
throw|;
block|}
block|}
return|return
name|permissions
return|;
block|}
block|}
end_class

end_unit
