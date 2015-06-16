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
name|provider
operator|.
name|json
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
literal|"/search"
argument_list|)
specifier|public
class|class
name|BigQueryService
block|{
specifier|private
name|WebClient
name|bigQueryClient
decl_stmt|;
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/complete"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/html"
argument_list|)
specifier|public
name|BigQueryResponse
name|completeBigQuery
parameter_list|(
annotation|@
name|Context
name|OidcClientTokenContext
name|context
parameter_list|)
block|{
name|ClientAccessToken
name|accessToken
init|=
name|context
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|bigQueryClient
operator|.
name|authorization
argument_list|(
name|accessToken
argument_list|)
expr_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|state
init|=
name|context
operator|.
name|getState
argument_list|()
decl_stmt|;
name|String
name|searchWord
init|=
name|state
operator|.
name|getFirst
argument_list|(
literal|"word"
argument_list|)
decl_stmt|;
name|String
name|maxResults
init|=
name|state
operator|.
name|getFirst
argument_list|(
literal|"maxResults"
argument_list|)
decl_stmt|;
name|String
name|bigQuerySelect
init|=
literal|"SELECT corpus,corpus_date FROM publicdata:samples.shakespeare WHERE word=\\\""
operator|+
name|searchWord
operator|+
literal|"\\\""
decl_stmt|;
name|String
name|bigQueryRequest
init|=
literal|"{"
operator|+
literal|"\"kind\": \"bigquery#queryRequest\","
operator|+
literal|"\"query\": \""
operator|+
name|bigQuerySelect
operator|+
literal|"\","
operator|+
literal|"\"maxResults\": "
operator|+
name|Integer
operator|.
name|parseInt
argument_list|(
name|maxResults
argument_list|)
operator|+
literal|"}"
decl_stmt|;
name|JsonMapObject
name|jsonMap
init|=
name|bigQueryClient
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
name|BigQueryResponse
name|bigQueryResponse
init|=
operator|new
name|BigQueryResponse
argument_list|(
name|context
operator|.
name|getUserInfo
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|searchWord
argument_list|)
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
name|bigQueryResponse
operator|.
name|getTexts
argument_list|()
operator|.
name|add
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|bigQueryResponse
return|;
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
block|}
end_class

end_unit

