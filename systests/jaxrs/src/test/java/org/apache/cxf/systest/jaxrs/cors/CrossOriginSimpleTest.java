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
name|cors
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|IOUtils
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
name|cors
operator|.
name|CorsHeaderConstants
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
name|systest
operator|.
name|jaxrs
operator|.
name|AbstractSpringServer
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusClientServerTestBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|Header
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HeaderElement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|ClientProtocolException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|HttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpDelete
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpGet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|DefaultHttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Unit tests for CORS. This isn't precisely simple as it's turned out.   *   * Note that it's not the server's job to detect invalid CORS requests. If a client  * fails to preflight, it's just not our job. However, also note that all 'actual'   * requests are treated as simple requests. In other words, a DELETE gets the same  * treatment as a simple request. The 'hey, this is complex' test happens on the client,  * which thus decides to do a preflight.  *   */
end_comment

begin_class
specifier|public
class|class
name|CrossOriginSimpleTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|SpringServer
operator|.
name|PORT
decl_stmt|;
specifier|private
name|WebClient
name|configClient
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|SpringServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|jaxrs
operator|.
name|JacksonJsonProvider
argument_list|()
argument_list|)
expr_stmt|;
name|configClient
operator|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/config"
argument_list|,
name|providers
argument_list|)
expr_stmt|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|headerValues
parameter_list|(
name|Header
index|[]
name|headers
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Header
name|h
range|:
name|headers
control|)
block|{
for|for
control|(
name|HeaderElement
name|e
range|:
name|h
operator|.
name|getElements
argument_list|()
control|)
block|{
name|values
operator|.
name|add
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|values
return|;
block|}
specifier|private
name|void
name|assertAllOrigin
parameter_list|(
name|boolean
name|allOrigins
parameter_list|,
name|String
index|[]
name|originList
parameter_list|,
name|String
index|[]
name|requestOrigins
parameter_list|,
name|boolean
name|permitted
parameter_list|)
throws|throws
name|ClientProtocolException
throws|,
name|IOException
block|{
name|configureAllowOrigins
argument_list|(
name|allOrigins
argument_list|,
name|originList
argument_list|)
expr_stmt|;
name|HttpClient
name|httpclient
init|=
operator|new
name|DefaultHttpClient
argument_list|()
decl_stmt|;
name|HttpGet
name|httpget
init|=
operator|new
name|HttpGet
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/untest/simpleGet/HelloThere"
argument_list|)
decl_stmt|;
if|if
condition|(
name|requestOrigins
operator|!=
literal|null
condition|)
block|{
name|StringBuffer
name|ob
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|requestOrigin
range|:
name|requestOrigins
control|)
block|{
name|ob
operator|.
name|append
argument_list|(
name|requestOrigin
argument_list|)
expr_stmt|;
name|ob
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
comment|// extra trailing space won't hurt.
block|}
name|httpget
operator|.
name|addHeader
argument_list|(
literal|"Origin"
argument_list|,
name|ob
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|HttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|httpget
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|HttpEntity
name|entity
init|=
name|response
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|String
name|e
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|entity
operator|.
name|getContent
argument_list|()
argument_list|,
literal|"utf-8"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"HelloThere"
argument_list|,
name|e
argument_list|)
expr_stmt|;
comment|// ensure that we didn't bust the operation itself.
name|assertOriginResponse
argument_list|(
name|allOrigins
argument_list|,
name|requestOrigins
argument_list|,
name|permitted
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertOriginResponse
parameter_list|(
name|boolean
name|allOrigins
parameter_list|,
name|String
index|[]
name|requestOrigins
parameter_list|,
name|boolean
name|permitted
parameter_list|,
name|HttpResponse
name|response
parameter_list|)
block|{
name|Header
index|[]
name|aaoHeaders
init|=
name|response
operator|.
name|getHeaders
argument_list|(
name|CorsHeaderConstants
operator|.
name|HEADER_AC_ALLOW_ORIGIN
argument_list|)
decl_stmt|;
if|if
condition|(
name|permitted
condition|)
block|{
name|assertNotNull
argument_list|(
name|aaoHeaders
argument_list|)
expr_stmt|;
if|if
condition|(
name|allOrigins
condition|)
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|aaoHeaders
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"*"
argument_list|,
name|aaoHeaders
index|[
literal|0
index|]
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ovalues
init|=
name|headerValues
argument_list|(
name|aaoHeaders
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ovalues
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// get back one ac-allow-origin header.
name|String
index|[]
name|origins
init|=
name|ovalues
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|split
argument_list|(
literal|" +"
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|requestOrigins
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|requestOrigins
index|[
name|x
index|]
argument_list|,
name|origins
index|[
name|x
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// Origin: null? We don't use it and it's not in the CORS spec.
name|assertTrue
argument_list|(
name|aaoHeaders
operator|==
literal|null
operator|||
name|aaoHeaders
operator|.
name|length
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|configureAllowOrigins
parameter_list|(
name|boolean
name|allOrigins
parameter_list|,
name|String
index|[]
name|originList
parameter_list|)
block|{
if|if
condition|(
name|allOrigins
condition|)
block|{
name|originList
operator|=
operator|new
name|String
index|[
literal|0
index|]
expr_stmt|;
block|}
comment|// tell filter what to do.
name|String
name|confResult
init|=
name|configClient
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|replacePath
argument_list|(
literal|"/setOriginList"
argument_list|)
operator|.
name|type
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|post
argument_list|(
name|originList
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ok"
argument_list|,
name|confResult
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|failNoOrigin
parameter_list|()
throws|throws
name|Exception
block|{
name|assertAllOrigin
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|allowStarPassOne
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Allow *, pass origin
name|assertAllOrigin
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"http://localhost:"
operator|+
name|PORT
block|}
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|preflightPostClassAnnotation
parameter_list|()
throws|throws
name|ClientProtocolException
throws|,
name|IOException
block|{
name|HttpClient
name|httpclient
init|=
operator|new
name|DefaultHttpClient
argument_list|()
decl_stmt|;
name|HttpOptions
name|httpoptions
init|=
operator|new
name|HttpOptions
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/antest/unannotatedPost"
argument_list|)
decl_stmt|;
name|httpoptions
operator|.
name|addHeader
argument_list|(
literal|"Origin"
argument_list|,
literal|"http://in.org"
argument_list|)
expr_stmt|;
comment|// nonsimple header
name|httpoptions
operator|.
name|addHeader
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|httpoptions
operator|.
name|addHeader
argument_list|(
name|CorsHeaderConstants
operator|.
name|HEADER_AC_REQUEST_METHOD
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
name|httpoptions
operator|.
name|addHeader
argument_list|(
name|CorsHeaderConstants
operator|.
name|HEADER_AC_REQUEST_HEADERS
argument_list|,
literal|"X-custom-1"
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|httpoptions
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|simplePostClassAnnotation
parameter_list|()
throws|throws
name|ClientProtocolException
throws|,
name|IOException
block|{
name|HttpClient
name|httpclient
init|=
operator|new
name|DefaultHttpClient
argument_list|()
decl_stmt|;
name|HttpOptions
name|httpoptions
init|=
operator|new
name|HttpOptions
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/antest/unannotatedPost"
argument_list|)
decl_stmt|;
name|httpoptions
operator|.
name|addHeader
argument_list|(
literal|"Origin"
argument_list|,
literal|"http://in.org"
argument_list|)
expr_stmt|;
comment|// nonsimple header
name|httpoptions
operator|.
name|addHeader
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
name|httpoptions
operator|.
name|addHeader
argument_list|(
name|CorsHeaderConstants
operator|.
name|HEADER_AC_REQUEST_METHOD
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|httpoptions
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|allowStarPassNone
parameter_list|()
throws|throws
name|Exception
block|{
comment|// allow *, no origin
name|assertAllOrigin
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|allowOnePassOne
parameter_list|()
throws|throws
name|Exception
block|{
comment|// allow one, pass that one
name|assertAllOrigin
argument_list|(
literal|false
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"http://localhost:"
operator|+
name|PORT
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"http://localhost:"
operator|+
name|PORT
block|}
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|allowOnePassWrong
parameter_list|()
throws|throws
name|Exception
block|{
comment|// allow one, pass something else
name|assertAllOrigin
argument_list|(
literal|false
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"http://localhost:"
operator|+
name|PORT
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"http://area51.mil:31315"
block|,         }
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|allowTwoPassOne
parameter_list|()
throws|throws
name|Exception
block|{
comment|// allow two, pass one
name|assertAllOrigin
argument_list|(
literal|false
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"http://localhost:"
operator|+
name|PORT
block|,
literal|"http://area51.mil:3141"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"http://localhost:"
operator|+
name|PORT
block|}
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|allowTwoPassTwo
parameter_list|()
throws|throws
name|Exception
block|{
comment|// allow two, pass two
name|assertAllOrigin
argument_list|(
literal|false
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"http://localhost:"
operator|+
name|PORT
block|,
literal|"http://area51.mil:3141"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"http://localhost:"
operator|+
name|PORT
block|,
literal|"http://area51.mil:3141"
block|}
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|allowTwoPassThree
parameter_list|()
throws|throws
name|Exception
block|{
comment|// allow two, pass three
name|assertAllOrigin
argument_list|(
literal|false
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"http://localhost:"
operator|+
name|PORT
block|,
literal|"http://area51.mil:3141"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"http://localhost:"
operator|+
name|PORT
block|,
literal|"http://area51.mil:3141"
block|,
literal|"http://hogwarts.edu:9"
block|}
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAllowCredentials
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|r
init|=
name|configClient
operator|.
name|replacePath
argument_list|(
literal|"/setAllowCredentials/true"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|post
argument_list|(
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ok"
argument_list|,
name|r
argument_list|)
expr_stmt|;
name|HttpClient
name|httpclient
init|=
operator|new
name|DefaultHttpClient
argument_list|()
decl_stmt|;
name|HttpGet
name|httpget
init|=
operator|new
name|HttpGet
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/untest/simpleGet/HelloThere"
argument_list|)
decl_stmt|;
name|httpget
operator|.
name|addHeader
argument_list|(
literal|"Origin"
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|httpget
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertAllowCredentials
argument_list|(
name|response
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testForbidCredentials
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|r
init|=
name|configClient
operator|.
name|replacePath
argument_list|(
literal|"/setAllowCredentials/false"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|post
argument_list|(
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ok"
argument_list|,
name|r
argument_list|)
expr_stmt|;
name|HttpClient
name|httpclient
init|=
operator|new
name|DefaultHttpClient
argument_list|()
decl_stmt|;
name|HttpGet
name|httpget
init|=
operator|new
name|HttpGet
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/untest/simpleGet/HelloThere"
argument_list|)
decl_stmt|;
name|httpget
operator|.
name|addHeader
argument_list|(
literal|"Origin"
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|httpget
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertAllowCredentials
argument_list|(
name|response
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNonSimpleActualRequest
parameter_list|()
throws|throws
name|Exception
block|{
name|configureAllowOrigins
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|r
init|=
name|configClient
operator|.
name|replacePath
argument_list|(
literal|"/setAllowCredentials/false"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|post
argument_list|(
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ok"
argument_list|,
name|r
argument_list|)
expr_stmt|;
name|HttpClient
name|httpclient
init|=
operator|new
name|DefaultHttpClient
argument_list|()
decl_stmt|;
name|HttpDelete
name|httpdelete
init|=
operator|new
name|HttpDelete
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/untest/delete"
argument_list|)
decl_stmt|;
name|httpdelete
operator|.
name|addHeader
argument_list|(
literal|"Origin"
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|httpdelete
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertAllowCredentials
argument_list|(
name|response
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertOriginResponse
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertAllowCredentials
parameter_list|(
name|HttpResponse
name|response
parameter_list|,
name|boolean
name|correct
parameter_list|)
block|{
name|Header
index|[]
name|aaoHeaders
init|=
name|response
operator|.
name|getHeaders
argument_list|(
name|CorsHeaderConstants
operator|.
name|HEADER_AC_ALLOW_CREDENTIALS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|aaoHeaders
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|toString
argument_list|(
name|correct
argument_list|)
argument_list|,
name|aaoHeaders
index|[
literal|0
index|]
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAnnotatedSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|configureAllowOrigins
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|r
init|=
name|configClient
operator|.
name|replacePath
argument_list|(
literal|"/setAllowCredentials/false"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|post
argument_list|(
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ok"
argument_list|,
name|r
argument_list|)
expr_stmt|;
name|HttpClient
name|httpclient
init|=
operator|new
name|DefaultHttpClient
argument_list|()
decl_stmt|;
name|HttpGet
name|httpget
init|=
operator|new
name|HttpGet
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/untest/annotatedGet/HelloThere"
argument_list|)
decl_stmt|;
comment|// this is the origin we expect to get.
name|httpget
operator|.
name|addHeader
argument_list|(
literal|"Origin"
argument_list|,
literal|"http://area51.mil:31415"
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|httpget
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertOriginResponse
argument_list|(
literal|false
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"http://area51.mil:31415"
block|}
argument_list|,
literal|true
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertAllowCredentials
argument_list|(
name|response
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|exposeHeadersValues
init|=
name|headerValues
argument_list|(
name|response
operator|.
name|getHeaders
argument_list|(
name|CorsHeaderConstants
operator|.
name|HEADER_AC_EXPOSE_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"X-custom-3"
block|,
literal|"X-custom-4"
block|}
argument_list|)
argument_list|,
name|exposeHeadersValues
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAnnotatedMethodPreflight
parameter_list|()
throws|throws
name|Exception
block|{
name|configureAllowOrigins
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|r
init|=
name|configClient
operator|.
name|replacePath
argument_list|(
literal|"/setAllowCredentials/false"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|post
argument_list|(
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ok"
argument_list|,
name|r
argument_list|)
expr_stmt|;
name|HttpClient
name|httpclient
init|=
operator|new
name|DefaultHttpClient
argument_list|()
decl_stmt|;
name|HttpOptions
name|http
init|=
operator|new
name|HttpOptions
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/untest/annotatedPut"
argument_list|)
decl_stmt|;
comment|// this is the origin we expect to get.
name|http
operator|.
name|addHeader
argument_list|(
literal|"Origin"
argument_list|,
literal|"http://area51.mil:31415"
argument_list|)
expr_stmt|;
name|http
operator|.
name|addHeader
argument_list|(
name|CorsHeaderConstants
operator|.
name|HEADER_AC_REQUEST_METHOD
argument_list|,
literal|"PUT"
argument_list|)
expr_stmt|;
name|http
operator|.
name|addHeader
argument_list|(
name|CorsHeaderConstants
operator|.
name|HEADER_AC_REQUEST_HEADERS
argument_list|,
literal|"X-custom-1, X-custom-2"
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|http
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertOriginResponse
argument_list|(
literal|false
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"http://area51.mil:31415"
block|}
argument_list|,
literal|true
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertAllowCredentials
argument_list|(
name|response
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|exposeHeadersValues
init|=
name|headerValues
argument_list|(
name|response
operator|.
name|getHeaders
argument_list|(
name|CorsHeaderConstants
operator|.
name|HEADER_AC_EXPOSE_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
comment|// preflight never returns Expose-Headers
name|assertEquals
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|,
name|exposeHeadersValues
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|allowHeadersValues
init|=
name|headerValues
argument_list|(
name|response
operator|.
name|getHeaders
argument_list|(
name|CorsHeaderConstants
operator|.
name|HEADER_AC_ALLOW_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"X-custom-1"
block|,
literal|"X-custom-2"
block|}
argument_list|)
argument_list|,
name|allowHeadersValues
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAnnotatedClassCorrectOrigin
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpClient
name|httpclient
init|=
operator|new
name|DefaultHttpClient
argument_list|()
decl_stmt|;
name|HttpGet
name|httpget
init|=
operator|new
name|HttpGet
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/antest/simpleGet/HelloThere"
argument_list|)
decl_stmt|;
name|httpget
operator|.
name|addHeader
argument_list|(
literal|"Origin"
argument_list|,
literal|"http://area51.mil:31415"
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|httpget
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|HttpEntity
name|entity
init|=
name|response
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|String
name|e
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|entity
operator|.
name|getContent
argument_list|()
argument_list|,
literal|"utf-8"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"HelloThere"
argument_list|,
name|e
argument_list|)
expr_stmt|;
comment|// ensure that we didn't bust the operation itself.
name|assertOriginResponse
argument_list|(
literal|false
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"http://area51.mil:31415"
block|}
argument_list|,
literal|true
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAnnotatedClassWrongOrigin
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpClient
name|httpclient
init|=
operator|new
name|DefaultHttpClient
argument_list|()
decl_stmt|;
name|HttpGet
name|httpget
init|=
operator|new
name|HttpGet
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/antest/simpleGet/HelloThere"
argument_list|)
decl_stmt|;
name|httpget
operator|.
name|addHeader
argument_list|(
literal|"Origin"
argument_list|,
literal|"http://su.us:1001"
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|httpget
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|HttpEntity
name|entity
init|=
name|response
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|String
name|e
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|entity
operator|.
name|getContent
argument_list|()
argument_list|,
literal|"utf-8"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"HelloThere"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|assertOriginResponse
argument_list|(
literal|false
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAnnotatedLocalPreflight
parameter_list|()
throws|throws
name|Exception
block|{
name|configureAllowOrigins
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|r
init|=
name|configClient
operator|.
name|replacePath
argument_list|(
literal|"/setAllowCredentials/false"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|post
argument_list|(
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ok"
argument_list|,
name|r
argument_list|)
expr_stmt|;
name|HttpClient
name|httpclient
init|=
operator|new
name|DefaultHttpClient
argument_list|()
decl_stmt|;
name|HttpOptions
name|http
init|=
operator|new
name|HttpOptions
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/antest/delete"
argument_list|)
decl_stmt|;
comment|// this is the origin we expect to get.
name|http
operator|.
name|addHeader
argument_list|(
literal|"Origin"
argument_list|,
literal|"http://area51.mil:3333"
argument_list|)
expr_stmt|;
name|http
operator|.
name|addHeader
argument_list|(
name|CorsHeaderConstants
operator|.
name|HEADER_AC_REQUEST_METHOD
argument_list|,
literal|"DELETE"
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|http
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertOriginResponse
argument_list|(
literal|false
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"http://area51.mil:3333"
block|}
argument_list|,
literal|true
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertAllowCredentials
argument_list|(
name|response
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|exposeHeadersValues
init|=
name|headerValues
argument_list|(
name|response
operator|.
name|getHeaders
argument_list|(
name|CorsHeaderConstants
operator|.
name|HEADER_AC_EXPOSE_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
comment|// preflight never returns Expose-Headers
name|assertEquals
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|,
name|exposeHeadersValues
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|allowedMethods
init|=
name|headerValues
argument_list|(
name|response
operator|.
name|getHeaders
argument_list|(
name|CorsHeaderConstants
operator|.
name|HEADER_AC_ALLOW_METHODS
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"DELETE PUT"
argument_list|)
argument_list|,
name|allowedMethods
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAnnotatedLocalPreflightNoGo
parameter_list|()
throws|throws
name|Exception
block|{
name|configureAllowOrigins
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|r
init|=
name|configClient
operator|.
name|replacePath
argument_list|(
literal|"/setAllowCredentials/false"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|post
argument_list|(
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ok"
argument_list|,
name|r
argument_list|)
expr_stmt|;
name|HttpClient
name|httpclient
init|=
operator|new
name|DefaultHttpClient
argument_list|()
decl_stmt|;
name|HttpOptions
name|http
init|=
operator|new
name|HttpOptions
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/antest/delete"
argument_list|)
decl_stmt|;
comment|// this is the origin we expect to get.
name|http
operator|.
name|addHeader
argument_list|(
literal|"Origin"
argument_list|,
literal|"http://area51.mil:4444"
argument_list|)
expr_stmt|;
name|http
operator|.
name|addHeader
argument_list|(
name|CorsHeaderConstants
operator|.
name|HEADER_AC_REQUEST_METHOD
argument_list|,
literal|"DELETE"
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|http
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertOriginResponse
argument_list|(
literal|false
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"http://area51.mil:4444"
block|}
argument_list|,
literal|false
argument_list|,
name|response
argument_list|)
expr_stmt|;
comment|// we could check that the others are also missing.
block|}
annotation|@
name|Ignore
specifier|public
specifier|static
class|class
name|SpringServer
extends|extends
name|AbstractSpringServer
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|AbstractSpringServer
operator|.
name|PORT
decl_stmt|;
specifier|public
name|SpringServer
parameter_list|()
block|{
name|super
argument_list|(
literal|"/jaxrs_cors"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

