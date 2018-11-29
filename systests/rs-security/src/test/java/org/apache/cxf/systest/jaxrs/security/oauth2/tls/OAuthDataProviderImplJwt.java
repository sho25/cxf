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
name|tls
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
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
name|BusFactory
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
name|grants
operator|.
name|code
operator|.
name|JCacheCodeDataProvider
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

begin_comment
comment|/**  * Extend the DefaultEHCacheCodeDataProvider to allow refreshing of tokens  */
end_comment

begin_class
specifier|public
class|class
name|OAuthDataProviderImplJwt
extends|extends
name|JCacheCodeDataProvider
block|{
specifier|public
name|OAuthDataProviderImplJwt
parameter_list|()
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|DEFAULT_CONFIG_URL
argument_list|,
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|(
literal|true
argument_list|)
argument_list|,
name|CLIENT_CACHE_KEY
operator|+
literal|"_"
operator|+
name|Math
operator|.
name|abs
argument_list|(
operator|new
name|Random
argument_list|()
operator|.
name|nextInt
argument_list|()
argument_list|)
argument_list|,
name|CODE_GRANT_CACHE_KEY
operator|+
literal|"_"
operator|+
name|Math
operator|.
name|abs
argument_list|(
operator|new
name|Random
argument_list|()
operator|.
name|nextInt
argument_list|()
argument_list|)
argument_list|,
name|ACCESS_TOKEN_CACHE_KEY
operator|+
literal|"_"
operator|+
name|Math
operator|.
name|abs
argument_list|(
operator|new
name|Random
argument_list|()
operator|.
name|nextInt
argument_list|()
argument_list|)
argument_list|,
name|REFRESH_TOKEN_CACHE_KEY
operator|+
literal|"_"
operator|+
name|Math
operator|.
name|abs
argument_list|(
operator|new
name|Random
argument_list|()
operator|.
name|nextInt
argument_list|()
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Client
name|client
init|=
operator|new
name|Client
argument_list|(
literal|"boundJwt"
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
name|client
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|OAuthConstants
operator|.
name|TLS_CLIENT_AUTH_SUBJECT_DN
argument_list|,
literal|"CN=whateverhost.com,OU=Morpit,O=ApacheTest,L=Syracuse,C=US"
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
name|this
operator|.
name|setUseJwtFormatForAccessTokens
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

