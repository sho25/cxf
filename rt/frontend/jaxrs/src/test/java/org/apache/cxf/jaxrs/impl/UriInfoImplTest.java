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
name|jaxrs
operator|.
name|impl
package|;
end_package

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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|UriInfo
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
name|model
operator|.
name|URITemplate
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
name|message
operator|.
name|Exchange
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
name|message
operator|.
name|Message
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
name|message
operator|.
name|MessageImpl
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|transport
operator|.
name|servlet
operator|.
name|ServletDestination
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|classextension
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|classextension
operator|.
name|IMocksControl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|UriInfoImplTest
extends|extends
name|Assert
block|{
specifier|private
name|IMocksControl
name|control
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|control
operator|.
name|makeThreadSafe
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetAbsolutePath
parameter_list|()
block|{
name|UriInfoImpl
name|u
init|=
operator|new
name|UriInfoImpl
argument_list|(
name|mockMessage
argument_list|(
literal|"http://localhost:8080/baz"
argument_list|,
literal|"/bar"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong absolute path"
argument_list|,
literal|"http://localhost:8080/baz/bar"
argument_list|,
name|u
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|u
operator|=
operator|new
name|UriInfoImpl
argument_list|(
name|mockMessage
argument_list|(
literal|"http://localhost:8080/baz/"
argument_list|,
literal|"/bar"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong absolute path"
argument_list|,
literal|"http://localhost:8080/baz/bar"
argument_list|,
name|u
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetAbsolutePathWithEncodedChars
parameter_list|()
block|{
name|UriInfoImpl
name|u
init|=
operator|new
name|UriInfoImpl
argument_list|(
name|mockMessage
argument_list|(
literal|"http://localhost:8080/baz%20foo"
argument_list|,
literal|"/bar"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong absolute path"
argument_list|,
literal|"http://localhost:8080/baz%20foo/bar"
argument_list|,
name|u
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|u
operator|=
operator|new
name|UriInfoImpl
argument_list|(
name|mockMessage
argument_list|(
literal|"http://localhost:8080/baz/%20foo"
argument_list|,
literal|"/bar%20foo"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong absolute path"
argument_list|,
literal|"http://localhost:8080/baz/%20foo/bar%20foo"
argument_list|,
name|u
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetQueryParameters
parameter_list|()
block|{
name|UriInfoImpl
name|u
init|=
operator|new
name|UriInfoImpl
argument_list|(
name|mockMessage
argument_list|(
literal|"http://localhost:8080/baz"
argument_list|,
literal|"/bar"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected queries"
argument_list|,
literal|0
argument_list|,
name|u
operator|.
name|getQueryParameters
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|u
operator|=
operator|new
name|UriInfoImpl
argument_list|(
name|mockMessage
argument_list|(
literal|"http://localhost:8080/baz"
argument_list|,
literal|"/bar"
argument_list|,
literal|"n=1%202"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|qps
init|=
name|u
operator|.
name|getQueryParameters
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number of queries is wrong"
argument_list|,
literal|1
argument_list|,
name|qps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong query value"
argument_list|,
name|qps
operator|.
name|getFirst
argument_list|(
literal|"n"
argument_list|)
argument_list|,
literal|"1%202"
argument_list|)
expr_stmt|;
name|u
operator|=
operator|new
name|UriInfoImpl
argument_list|(
name|mockMessage
argument_list|(
literal|"http://localhost:8080/baz"
argument_list|,
literal|"/bar"
argument_list|,
literal|"n=1%202&n=3&b=2&a%2Eb=ab"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|qps
operator|=
name|u
operator|.
name|getQueryParameters
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number of queiries is wrong"
argument_list|,
literal|3
argument_list|,
name|qps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong query value"
argument_list|,
name|qps
operator|.
name|get
argument_list|(
literal|"n"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|"1 2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong query value"
argument_list|,
name|qps
operator|.
name|get
argument_list|(
literal|"n"
argument_list|)
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong query value"
argument_list|,
name|qps
operator|.
name|get
argument_list|(
literal|"b"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong query value"
argument_list|,
name|qps
operator|.
name|get
argument_list|(
literal|"a.b"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|"ab"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetRequestURI
parameter_list|()
block|{
name|UriInfo
name|u
init|=
operator|new
name|UriInfoImpl
argument_list|(
name|mockMessage
argument_list|(
literal|"http://localhost:8080/baz/bar"
argument_list|,
literal|"/foo"
argument_list|,
literal|"n=1%202"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong request uri"
argument_list|,
literal|"http://localhost:8080/baz/bar/foo?n=1%202"
argument_list|,
name|u
operator|.
name|getRequestUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetRequestURIWithEncodedChars
parameter_list|()
block|{
name|UriInfo
name|u
init|=
operator|new
name|UriInfoImpl
argument_list|(
name|mockMessage
argument_list|(
literal|"http://localhost:8080/baz/bar"
argument_list|,
literal|"/foo/%20bar"
argument_list|,
literal|"n=1%202"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong request uri"
argument_list|,
literal|"http://localhost:8080/baz/bar/foo/%20bar?n=1%202"
argument_list|,
name|u
operator|.
name|getRequestUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetTemplateParameters
parameter_list|()
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
operator|new
name|URITemplate
argument_list|(
literal|"/bar"
argument_list|)
operator|.
name|match
argument_list|(
literal|"/baz"
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|UriInfoImpl
name|u
init|=
operator|new
name|UriInfoImpl
argument_list|(
name|mockMessage
argument_list|(
literal|"http://localhost:8080/baz"
argument_list|,
literal|"/bar"
argument_list|)
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected templates"
argument_list|,
literal|0
argument_list|,
name|u
operator|.
name|getPathParameters
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|values
operator|.
name|clear
argument_list|()
expr_stmt|;
operator|new
name|URITemplate
argument_list|(
literal|"/{id}"
argument_list|)
operator|.
name|match
argument_list|(
literal|"/bar%201"
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|u
operator|=
operator|new
name|UriInfoImpl
argument_list|(
name|mockMessage
argument_list|(
literal|"http://localhost:8080/baz"
argument_list|,
literal|"/bar%201"
argument_list|)
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|tps
init|=
name|u
operator|.
name|getPathParameters
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number of templates is wrong"
argument_list|,
literal|1
argument_list|,
name|tps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong template value"
argument_list|,
name|tps
operator|.
name|getFirst
argument_list|(
literal|"id"
argument_list|)
argument_list|,
literal|"bar%201"
argument_list|)
expr_stmt|;
name|values
operator|.
name|clear
argument_list|()
expr_stmt|;
operator|new
name|URITemplate
argument_list|(
literal|"/{id}/{baz}"
argument_list|)
operator|.
name|match
argument_list|(
literal|"/1%202/bar"
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|u
operator|=
operator|new
name|UriInfoImpl
argument_list|(
name|mockMessage
argument_list|(
literal|"http://localhost:8080/baz"
argument_list|,
literal|"/1%202/bar"
argument_list|)
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|tps
operator|=
name|u
operator|.
name|getPathParameters
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number of templates is wrong"
argument_list|,
literal|2
argument_list|,
name|tps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong template value"
argument_list|,
name|tps
operator|.
name|getFirst
argument_list|(
literal|"id"
argument_list|)
argument_list|,
literal|"1 2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong template value"
argument_list|,
name|tps
operator|.
name|getFirst
argument_list|(
literal|"baz"
argument_list|)
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
comment|// with suffix
name|values
operator|.
name|clear
argument_list|()
expr_stmt|;
operator|new
name|URITemplate
argument_list|(
literal|"/bar"
argument_list|)
operator|.
name|match
argument_list|(
literal|"/bar"
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|u
operator|=
operator|new
name|UriInfoImpl
argument_list|(
name|mockMessage
argument_list|(
literal|"http://localhost:8080/baz"
argument_list|,
literal|"/bar"
argument_list|)
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected templates"
argument_list|,
literal|0
argument_list|,
name|u
operator|.
name|getPathParameters
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBaseUri
parameter_list|()
block|{
name|UriInfoImpl
name|u
init|=
operator|new
name|UriInfoImpl
argument_list|(
name|mockMessage
argument_list|(
literal|"http://localhost:8080/baz"
argument_list|,
literal|null
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong base path"
argument_list|,
literal|"http://localhost:8080/baz"
argument_list|,
name|u
operator|.
name|getBaseUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|u
operator|=
operator|new
name|UriInfoImpl
argument_list|(
name|mockMessage
argument_list|(
literal|"http://localhost:8080/baz/"
argument_list|,
literal|null
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong base path"
argument_list|,
literal|"http://localhost:8080/baz/"
argument_list|,
name|u
operator|.
name|getBaseUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetPath
parameter_list|()
block|{
name|UriInfoImpl
name|u
init|=
operator|new
name|UriInfoImpl
argument_list|(
name|mockMessage
argument_list|(
literal|"http://localhost:8080/bar/baz"
argument_list|,
literal|"/baz"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong path"
argument_list|,
literal|"baz"
argument_list|,
name|u
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|u
operator|=
operator|new
name|UriInfoImpl
argument_list|(
name|mockMessage
argument_list|(
literal|"http://localhost:8080/bar/baz"
argument_list|,
literal|"/bar/baz"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong path"
argument_list|,
literal|"/"
argument_list|,
name|u
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|u
operator|=
operator|new
name|UriInfoImpl
argument_list|(
name|mockMessage
argument_list|(
literal|"http://localhost:8080/bar/baz/"
argument_list|,
literal|"/bar/baz/"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong path"
argument_list|,
literal|"/"
argument_list|,
name|u
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|u
operator|=
operator|new
name|UriInfoImpl
argument_list|(
name|mockMessage
argument_list|(
literal|"http://localhost:8080/baz"
argument_list|,
literal|"/baz/bar%201"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong path"
argument_list|,
literal|"bar 1"
argument_list|,
name|u
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|u
operator|=
operator|new
name|UriInfoImpl
argument_list|(
name|mockMessage
argument_list|(
literal|"http://localhost:8080/baz"
argument_list|,
literal|"/baz/bar%201"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong path"
argument_list|,
literal|"bar%201"
argument_list|,
name|u
operator|.
name|getPath
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Message
name|mockMessage
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|String
name|pathInfo
parameter_list|)
block|{
return|return
name|mockMessage
argument_list|(
name|baseAddress
argument_list|,
name|pathInfo
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|private
name|Message
name|mockMessage
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|String
name|pathInfo
parameter_list|,
name|String
name|query
parameter_list|)
block|{
return|return
name|mockMessage
argument_list|(
name|baseAddress
argument_list|,
name|pathInfo
argument_list|,
name|query
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|private
name|Message
name|mockMessage
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|String
name|pathInfo
parameter_list|,
name|String
name|query
parameter_list|,
name|String
name|fragment
parameter_list|)
block|{
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|Exchange
name|e
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
decl_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|ServletDestination
name|d
init|=
name|control
operator|.
name|createMock
argument_list|(
name|ServletDestination
operator|.
name|class
argument_list|)
decl_stmt|;
name|e
operator|.
name|getDestination
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|d
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EndpointInfo
name|epr
init|=
operator|new
name|EndpointInfo
argument_list|()
decl_stmt|;
name|epr
operator|.
name|setAddress
argument_list|(
name|baseAddress
argument_list|)
expr_stmt|;
name|d
operator|.
name|getEndpointInfo
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|epr
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|REQUEST_URI
argument_list|,
name|pathInfo
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|QUERY_STRING
argument_list|,
name|query
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
return|return
name|m
return|;
block|}
block|}
end_class

end_unit

