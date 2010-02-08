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
name|core
operator|.
name|HttpHeaders
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
name|Response
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
name|endpoint
operator|.
name|Endpoint
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
name|JAXRSServiceImpl
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
name|ClassResourceInfo
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
name|wadl
operator|.
name|WadlGenerator
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
name|ProviderFactory
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
name|utils
operator|.
name|ResourceUtils
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
name|ExchangeImpl
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
name|Service
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
name|RequestPreprocessorTest
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
name|testMethodQuery
parameter_list|()
block|{
name|Message
name|m
init|=
name|mockMessage
argument_list|(
literal|"http://localhost:8080"
argument_list|,
literal|"/bar"
argument_list|,
literal|"_method=GET"
argument_list|,
literal|"POST"
argument_list|)
decl_stmt|;
name|RequestPreprocessor
name|sqh
init|=
operator|new
name|RequestPreprocessor
argument_list|()
decl_stmt|;
name|sqh
operator|.
name|preprocess
argument_list|(
name|m
argument_list|,
operator|new
name|UriInfoImpl
argument_list|(
name|m
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"GET"
argument_list|,
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMethodOverride
parameter_list|()
block|{
name|Message
name|m
init|=
name|mockMessage
argument_list|(
literal|"http://localhost:8080"
argument_list|,
literal|"/bar"
argument_list|,
literal|"bar"
argument_list|,
literal|"POST"
argument_list|,
literal|"GET"
argument_list|)
decl_stmt|;
name|RequestPreprocessor
name|sqh
init|=
operator|new
name|RequestPreprocessor
argument_list|()
decl_stmt|;
name|sqh
operator|.
name|preprocess
argument_list|(
name|m
argument_list|,
operator|new
name|UriInfoImpl
argument_list|(
name|m
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"GET"
argument_list|,
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWadlQuery
parameter_list|()
block|{
name|Message
name|m
init|=
name|mockMessage
argument_list|(
literal|"http://localhost:8080/bar"
argument_list|,
literal|"/bar"
argument_list|,
literal|"_wadl"
argument_list|,
literal|"GET"
argument_list|)
decl_stmt|;
name|ClassResourceInfo
name|cri
init|=
name|ResourceUtils
operator|.
name|createClassResourceInfo
argument_list|(
name|TestResource
operator|.
name|class
argument_list|,
name|TestResource
operator|.
name|class
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|Service
operator|.
name|class
argument_list|,
operator|new
name|JAXRSServiceImpl
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|cri
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|RequestPreprocessor
name|sqh
init|=
operator|new
name|RequestPreprocessor
argument_list|()
decl_stmt|;
name|sqh
operator|.
name|preprocess
argument_list|(
name|m
argument_list|,
operator|new
name|UriInfoImpl
argument_list|(
name|m
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|Response
name|r
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Response
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|WadlGenerator
operator|.
name|WADL_TYPE
operator|.
name|toString
argument_list|()
argument_list|,
name|r
operator|.
name|getMetadata
argument_list|()
operator|.
name|getFirst
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTypeQuery
parameter_list|()
block|{
name|Message
name|m
init|=
name|mockMessage
argument_list|(
literal|"http://localhost:8080"
argument_list|,
literal|"/bar"
argument_list|,
literal|"_type=xml"
argument_list|,
literal|"POST"
argument_list|)
decl_stmt|;
name|RequestPreprocessor
name|sqh
init|=
operator|new
name|RequestPreprocessor
argument_list|()
decl_stmt|;
name|sqh
operator|.
name|preprocess
argument_list|(
name|m
argument_list|,
operator|new
name|UriInfoImpl
argument_list|(
name|m
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"POST"
argument_list|,
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/xml"
argument_list|,
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ACCEPT_CONTENT_TYPE
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
parameter_list|,
name|String
name|query
parameter_list|,
name|String
name|method
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
name|method
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
name|method
parameter_list|,
name|String
name|methodHeader
parameter_list|)
block|{
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
literal|"org.apache.cxf.http.case_insensitive_queries"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|Exchange
name|e
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|Endpoint
name|endp
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|e
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|endp
argument_list|)
expr_stmt|;
name|endp
operator|.
name|get
argument_list|(
name|ProviderFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|ProviderFactory
operator|.
name|getInstance
argument_list|()
argument_list|)
operator|.
name|anyTimes
argument_list|()
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
name|setDestination
argument_list|(
name|d
argument_list|)
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
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|,
name|method
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|methodHeader
operator|!=
literal|null
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"X-HTTP-Method-Override"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|methodHeader
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|headers
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
annotation|@
name|Path
argument_list|(
literal|"/test"
argument_list|)
specifier|private
specifier|static
class|class
name|TestResource
block|{
comment|//suppress the unused get method warning in eclipse.   The class is private
comment|//so nothing really calls the "get" method, but this is needed for the
comment|//test case
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
annotation|@
name|GET
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
literal|"test"
return|;
block|}
block|}
block|}
end_class

end_unit

