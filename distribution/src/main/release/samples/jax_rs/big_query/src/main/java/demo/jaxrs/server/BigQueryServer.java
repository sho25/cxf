begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|jaxrs
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

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
name|KeyStore
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ext
operator|.
name|logging
operator|.
name|LoggingInInterceptor
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
name|client
operator|.
name|WebClient
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
name|provider
operator|.
name|json
operator|.
name|JsonMapObjectProvider
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
name|jose
operator|.
name|common
operator|.
name|JoseType
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
name|jose
operator|.
name|jwa
operator|.
name|SignatureAlgorithm
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
name|jose
operator|.
name|jws
operator|.
name|JwsHeaders
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
name|jose
operator|.
name|jws
operator|.
name|JwsJwtCompactProducer
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
name|jose
operator|.
name|jwt
operator|.
name|JwtClaims
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
name|jose
operator|.
name|jwt
operator|.
name|JwtToken
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
name|client
operator|.
name|AccessTokenGrantWriter
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
name|ClientAccessToken
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
name|jwt
operator|.
name|JwtBearerGrant
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
name|OAuthJSONProvider
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
name|OAuthUtils
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|BigQueryServer
block|{
specifier|private
name|BigQueryServer
parameter_list|()
block|{     }
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|String
name|pc12File
init|=
name|args
index|[
literal|0
index|]
decl_stmt|;
specifier|final
name|String
name|keySecret
init|=
name|args
index|[
literal|1
index|]
decl_stmt|;
specifier|final
name|String
name|issuer
init|=
name|args
index|[
literal|2
index|]
decl_stmt|;
specifier|final
name|String
name|projectId
init|=
name|args
index|[
literal|3
index|]
decl_stmt|;
name|PrivateKey
name|privateKey
init|=
name|loadPrivateKey
argument_list|(
name|pc12File
argument_list|,
name|keySecret
argument_list|)
decl_stmt|;
name|ClientAccessToken
name|accessToken
init|=
name|getAccessToken
argument_list|(
name|privateKey
argument_list|,
name|issuer
argument_list|)
decl_stmt|;
name|WebClient
name|bigQueryClient
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"https://www.googleapis.com/bigquery/v2/projects/"
operator|+
name|projectId
operator|+
literal|"/queries"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|JsonMapObjectProvider
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|bigQueryClient
operator|.
name|type
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ShakespeareText
argument_list|>
name|texts
init|=
name|BigQueryService
operator|.
name|getMatchingTexts
argument_list|(
name|bigQueryClient
argument_list|,
name|accessToken
argument_list|,
literal|"brave"
argument_list|,
literal|"10"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Matching texts:"
argument_list|)
expr_stmt|;
for|for
control|(
name|ShakespeareText
name|text
range|:
name|texts
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|text
operator|.
name|getText
argument_list|()
operator|+
literal|":"
operator|+
name|text
operator|.
name|getDate
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|ClientAccessToken
name|getAccessToken
parameter_list|(
name|PrivateKey
name|privateKey
parameter_list|,
name|String
name|issuer
parameter_list|)
block|{
name|JwsHeaders
name|headers
init|=
operator|new
name|JwsHeaders
argument_list|(
name|JoseType
operator|.
name|JWT
argument_list|,
name|SignatureAlgorithm
operator|.
name|RS256
argument_list|)
decl_stmt|;
name|JwtClaims
name|claims
init|=
operator|new
name|JwtClaims
argument_list|()
decl_stmt|;
name|claims
operator|.
name|setIssuer
argument_list|(
name|issuer
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setAudience
argument_list|(
literal|"https://www.googleapis.com/oauth2/v3/token"
argument_list|)
expr_stmt|;
name|long
name|issuedAt
init|=
name|OAuthUtils
operator|.
name|getIssuedAt
argument_list|()
decl_stmt|;
name|claims
operator|.
name|setIssuedAt
argument_list|(
name|issuedAt
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setExpiryTime
argument_list|(
name|issuedAt
operator|+
literal|60
operator|*
literal|60
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setProperty
argument_list|(
literal|"scope"
argument_list|,
literal|"https://www.googleapis.com/auth/bigquery.readonly"
argument_list|)
expr_stmt|;
name|JwtToken
name|token
init|=
operator|new
name|JwtToken
argument_list|(
name|headers
argument_list|,
name|claims
argument_list|)
decl_stmt|;
name|JwsJwtCompactProducer
name|p
init|=
operator|new
name|JwsJwtCompactProducer
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|String
name|base64UrlAssertion
init|=
name|p
operator|.
name|signWith
argument_list|(
name|privateKey
argument_list|)
decl_stmt|;
name|JwtBearerGrant
name|grant
init|=
operator|new
name|JwtBearerGrant
argument_list|(
name|base64UrlAssertion
argument_list|)
decl_stmt|;
name|WebClient
name|accessTokenService
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"https://www.googleapis.com/oauth2/v3/token"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|OAuthJSONProvider
argument_list|()
argument_list|,
operator|new
name|AccessTokenGrantWriter
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|accessTokenService
argument_list|)
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|accessTokenService
operator|.
name|type
argument_list|(
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
expr_stmt|;
return|return
name|accessTokenService
operator|.
name|post
argument_list|(
name|grant
argument_list|,
name|ClientAccessToken
operator|.
name|class
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|PrivateKey
name|loadPrivateKey
parameter_list|(
name|String
name|p12File
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|InputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
name|p12File
argument_list|)
init|)
block|{
name|KeyStore
name|store
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"PKCS12"
argument_list|)
decl_stmt|;
name|store
operator|.
name|load
argument_list|(
name|is
argument_list|,
name|password
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|(
name|PrivateKey
operator|)
name|store
operator|.
name|getKey
argument_list|(
literal|"privateKey"
argument_list|,
name|password
operator|.
name|toCharArray
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

