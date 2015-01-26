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
name|HttpMethod
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
name|NotAllowedException
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
name|Request
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
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
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|GetMethod
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
name|JAXRSServerFactoryBean
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
name|AbstractResourceInfo
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
name|Parameter
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
name|ParameterType
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
name|UserOperation
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
name|UserResource
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
name|cxf
operator|.
name|testutil
operator|.
name|common
operator|.
name|AbstractBusTestServerBase
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

begin_class
specifier|public
class|class
name|JAXRSClientServerUserResourceDefaultTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Ignore
specifier|public
specifier|static
class|class
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Server
name|server
decl_stmt|;
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|JAXRSServerFactoryBean
name|sf
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/default"
argument_list|)
expr_stmt|;
name|sf
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setDefaultModelClass
argument_list|(
name|DefaultResource
operator|.
name|class
argument_list|)
expr_stmt|;
name|UserResource
name|ur
init|=
operator|new
name|UserResource
argument_list|()
decl_stmt|;
name|ur
operator|.
name|setPath
argument_list|(
literal|"/bookstoreNoAnnotations"
argument_list|)
expr_stmt|;
name|UserOperation
name|op
init|=
operator|new
name|UserOperation
argument_list|()
decl_stmt|;
name|op
operator|.
name|setPath
argument_list|(
literal|"/books/{id}"
argument_list|)
expr_stmt|;
name|op
operator|.
name|setName
argument_list|(
literal|"getBook"
argument_list|)
expr_stmt|;
name|op
operator|.
name|setVerb
argument_list|(
literal|"GET"
argument_list|)
expr_stmt|;
name|op
operator|.
name|setParameters
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|Parameter
argument_list|(
name|ParameterType
operator|.
name|PATH
argument_list|,
literal|"id"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|UserOperation
name|op2
init|=
operator|new
name|UserOperation
argument_list|()
decl_stmt|;
name|op2
operator|.
name|setPath
argument_list|(
literal|"/books/{id}/chapter"
argument_list|)
expr_stmt|;
name|op2
operator|.
name|setName
argument_list|(
literal|"getBookChapter"
argument_list|)
expr_stmt|;
name|op2
operator|.
name|setParameters
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|Parameter
argument_list|(
name|ParameterType
operator|.
name|PATH
argument_list|,
literal|"id"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|UserOperation
argument_list|>
name|ops
init|=
operator|new
name|ArrayList
argument_list|<
name|UserOperation
argument_list|>
argument_list|()
decl_stmt|;
name|ops
operator|.
name|add
argument_list|(
name|op
argument_list|)
expr_stmt|;
name|ops
operator|.
name|add
argument_list|(
name|op2
argument_list|)
expr_stmt|;
name|ur
operator|.
name|setOperations
argument_list|(
name|ops
argument_list|)
expr_stmt|;
name|UserResource
name|ur2
init|=
operator|new
name|UserResource
argument_list|()
decl_stmt|;
name|ur2
operator|.
name|setName
argument_list|(
name|ChapterNoAnnotations
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|UserOperation
name|op3
init|=
operator|new
name|UserOperation
argument_list|()
decl_stmt|;
name|op3
operator|.
name|setPath
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|op3
operator|.
name|setName
argument_list|(
literal|"getItself"
argument_list|)
expr_stmt|;
name|op3
operator|.
name|setVerb
argument_list|(
literal|"GET"
argument_list|)
expr_stmt|;
name|ur2
operator|.
name|setOperations
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|op3
argument_list|)
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setModelBeans
argument_list|(
name|ur
argument_list|,
name|ur2
argument_list|)
expr_stmt|;
name|server
operator|=
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|server
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
try|try
block|{
name|Server
name|s
init|=
operator|new
name|Server
argument_list|()
decl_stmt|;
name|s
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"done!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|AbstractResourceInfo
operator|.
name|clearAllMaps
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|createStaticBus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123
parameter_list|()
throws|throws
name|Exception
block|{
name|getAndCompare
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/default/bookstoreNoAnnotations/books/123"
argument_list|,
literal|"application/xml"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|getAndCompare
parameter_list|(
name|String
name|address
parameter_list|,
name|String
name|acceptType
parameter_list|,
name|int
name|expectedStatus
parameter_list|)
throws|throws
name|Exception
block|{
name|GetMethod
name|get
init|=
operator|new
name|GetMethod
argument_list|(
name|address
argument_list|)
decl_stmt|;
name|get
operator|.
name|setRequestHeader
argument_list|(
literal|"Accept"
argument_list|,
name|acceptType
argument_list|)
expr_stmt|;
name|HttpClient
name|httpClient
init|=
operator|new
name|HttpClient
argument_list|()
decl_stmt|;
try|try
block|{
name|int
name|result
init|=
name|httpClient
operator|.
name|executeMethod
argument_list|(
name|get
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedStatus
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|Book
name|book
init|=
name|readBook
argument_list|(
name|get
operator|.
name|getResponseBodyAsStream
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action"
argument_list|,
name|book
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|get
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|Book
name|readBook
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|Exception
block|{
name|JAXBContext
name|c
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
operator|new
name|Class
index|[]
block|{
name|Book
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|Unmarshaller
name|u
init|=
name|c
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
return|return
operator|(
name|Book
operator|)
name|u
operator|.
name|unmarshal
argument_list|(
name|is
argument_list|)
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/"
argument_list|)
specifier|public
specifier|static
class|class
name|DefaultResource
block|{
annotation|@
name|Context
specifier|private
name|Request
name|request
decl_stmt|;
annotation|@
name|Context
specifier|private
name|UriInfo
name|ui
decl_stmt|;
annotation|@
name|Context
specifier|private
name|HttpHeaders
name|headers
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Book
argument_list|>
name|books
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"123"
argument_list|,
operator|new
name|Book
argument_list|(
literal|"CXF in Action"
argument_list|,
literal|123L
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|Path
argument_list|(
literal|"{a:.*}"
argument_list|)
specifier|public
name|Response
name|handle
parameter_list|()
block|{
if|if
condition|(
name|HttpMethod
operator|.
name|GET
operator|.
name|equals
argument_list|(
name|request
operator|.
name|getMethod
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|id
init|=
name|ui
operator|.
name|getPathParameters
argument_list|()
operator|.
name|getFirst
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
name|Book
name|book
init|=
name|books
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|book
argument_list|,
name|headers
operator|.
name|getAcceptableMediaTypes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|NotAllowedException
argument_list|(
literal|"GET"
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

