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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|utils
operator|.
name|ClassLoaderUtils
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
name|JCacheCodeDataProvider
block|{
specifier|public
name|OAuthDataProviderImpl
parameter_list|()
throws|throws
name|Exception
block|{
name|Client
name|client1
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
name|client1
operator|.
name|getAllowedGrantTypes
argument_list|()
operator|.
name|add
argument_list|(
literal|"custom_grant"
argument_list|)
expr_stmt|;
name|registerCert
argument_list|(
name|client1
argument_list|)
expr_stmt|;
name|this
operator|.
name|setClient
argument_list|(
name|client1
argument_list|)
expr_stmt|;
name|Client
name|client2
init|=
operator|new
name|Client
argument_list|(
literal|"bound"
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
name|this
operator|.
name|setClient
argument_list|(
name|client2
argument_list|)
expr_stmt|;
name|Client
name|client3
init|=
operator|new
name|Client
argument_list|(
literal|"unbound"
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
name|this
operator|.
name|setClient
argument_list|(
name|client3
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|registerCert
parameter_list|(
name|Client
name|client
parameter_list|)
throws|throws
name|Exception
block|{
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
name|client
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
block|}
specifier|private
name|Certificate
name|loadCert
parameter_list|()
throws|throws
name|Exception
block|{
try|try
init|(
name|InputStream
name|is
init|=
name|ClassLoaderUtils
operator|.
name|getResourceAsStream
argument_list|(
literal|"keys/Truststore.jks"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
init|)
block|{
return|return
name|CryptoUtils
operator|.
name|loadCertificate
argument_list|(
name|is
argument_list|,
literal|"password"
operator|.
name|toCharArray
argument_list|()
argument_list|,
literal|"morpit"
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

