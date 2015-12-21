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
name|FormParam
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
name|GET
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
name|Context
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
name|helpers
operator|.
name|CastUtils
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
name|oidc
operator|.
name|rp
operator|.
name|OidcClientTokenContext
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/"
argument_list|)
specifier|public
class|class
name|BigQueryService
block|{
specifier|private
specifier|static
specifier|final
name|String
name|BQ_SELECT
init|=
literal|"SELECT corpus,corpus_date FROM publicdata:samples.shakespeare WHERE word=\\\"%s\\\""
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BQ_REQUEST
init|=
literal|"{"
operator|+
literal|"\"kind\": \"bigquery#queryRequest\","
operator|+
literal|"\"query\": \"%s\","
operator|+
literal|"\"maxResults\": %d"
operator|+
literal|"}"
decl_stmt|;
annotation|@
name|Context
specifier|private
name|OidcClientTokenContext
name|oidcContext
decl_stmt|;
specifier|private
name|WebClient
name|bigQueryClient
decl_stmt|;
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/start"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/html"
argument_list|)
specifier|public
name|BigQueryStart
name|startBigQuerySearch
parameter_list|()
block|{
return|return
operator|new
name|BigQueryStart
argument_list|(
name|getUserInfo
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/complete"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"application/x-www-form-urlencoded"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/html"
argument_list|)
specifier|public
name|BigQueryResponse
name|completeBigQuerySearch
parameter_list|(
annotation|@
name|FormParam
argument_list|(
literal|"word"
argument_list|)
name|String
name|searchWord
parameter_list|,
annotation|@
name|FormParam
argument_list|(
literal|"maxResults"
argument_list|)
name|String
name|maxResults
parameter_list|)
block|{
name|ClientAccessToken
name|accessToken
init|=
name|oidcContext
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|BigQueryResponse
name|bigQueryResponse
init|=
operator|new
name|BigQueryResponse
argument_list|(
name|getUserInfo
argument_list|()
argument_list|,
name|searchWord
argument_list|)
decl_stmt|;
name|bigQueryResponse
operator|.
name|setTexts
argument_list|(
name|getMatchingTexts
argument_list|(
name|bigQueryClient
argument_list|,
name|accessToken
argument_list|,
name|searchWord
argument_list|,
name|maxResults
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|bigQueryResponse
return|;
block|}
specifier|private
name|String
name|getUserInfo
parameter_list|()
block|{
if|if
condition|(
name|oidcContext
operator|.
name|getUserInfo
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|oidcContext
operator|.
name|getUserInfo
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|oidcContext
operator|.
name|getIdToken
argument_list|()
operator|.
name|getSubject
argument_list|()
return|;
block|}
block|}
specifier|public
name|void
name|setBigQueryClient
parameter_list|(
name|WebClient
name|bigQueryClient
parameter_list|)
block|{
name|this
operator|.
name|bigQueryClient
operator|=
name|bigQueryClient
expr_stmt|;
block|}
specifier|static
name|List
argument_list|<
name|ShakespeareText
argument_list|>
name|getMatchingTexts
parameter_list|(
name|WebClient
name|bqClient
parameter_list|,
name|ClientAccessToken
name|accessToken
parameter_list|,
name|String
name|searchWord
parameter_list|,
name|String
name|maxResults
parameter_list|)
block|{
name|bqClient
operator|.
name|authorization
argument_list|(
name|accessToken
argument_list|)
expr_stmt|;
name|String
name|bigQuerySelect
init|=
name|String
operator|.
name|format
argument_list|(
name|BQ_SELECT
argument_list|,
name|searchWord
argument_list|)
decl_stmt|;
name|String
name|bigQueryRequest
init|=
name|String
operator|.
name|format
argument_list|(
name|BQ_REQUEST
argument_list|,
name|bigQuerySelect
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|maxResults
argument_list|)
argument_list|)
decl_stmt|;
name|JsonMapObject
name|jsonMap
init|=
name|bqClient
operator|.
name|post
argument_list|(
name|bigQueryRequest
argument_list|,
name|JsonMapObject
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ShakespeareText
argument_list|>
name|texts
init|=
operator|new
name|LinkedList
argument_list|<
name|ShakespeareText
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|rows
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|jsonMap
operator|.
name|getProperty
argument_list|(
literal|"rows"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|rows
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
range|:
name|rows
control|)
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|fields
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"f"
argument_list|)
argument_list|)
decl_stmt|;
name|ShakespeareText
name|text
init|=
operator|new
name|ShakespeareText
argument_list|(
operator|(
name|String
operator|)
name|fields
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|get
argument_list|(
literal|"v"
argument_list|)
argument_list|,
operator|(
name|String
operator|)
name|fields
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|get
argument_list|(
literal|"v"
argument_list|)
argument_list|)
decl_stmt|;
name|texts
operator|.
name|add
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|texts
return|;
block|}
block|}
end_class

end_unit

