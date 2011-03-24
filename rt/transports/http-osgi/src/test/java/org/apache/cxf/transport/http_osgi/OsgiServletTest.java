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
name|transport
operator|.
name|http_osgi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletOutputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServlet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|Bus
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
name|service
operator|.
name|model
operator|.
name|InterfaceInfo
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
name|ServiceInfo
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
name|AbstractDestination
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
name|MessageObserver
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
name|http
operator|.
name|AbstractHTTPDestination
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
name|http
operator|.
name|DestinationRegistry
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
name|ServletController
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
name|transports
operator|.
name|http
operator|.
name|QueryHandler
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
name|transports
operator|.
name|http
operator|.
name|QueryHandlerRegistry
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
name|wsdl
operator|.
name|http
operator|.
name|AddressType
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
name|After
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
name|OsgiServletTest
extends|extends
name|Assert
block|{
specifier|private
specifier|final
class|class
name|TestOsgiServletController
extends|extends
name|ServletController
block|{
specifier|private
name|boolean
name|invokeDestinationCalled
decl_stmt|;
specifier|private
name|TestOsgiServletController
parameter_list|(
name|ServletConfig
name|config
parameter_list|,
name|DestinationRegistry
name|destinationRegistry
parameter_list|,
name|HttpServlet
name|serviceListGenerator
parameter_list|)
block|{
name|super
argument_list|(
name|destinationRegistry
argument_list|,
name|config
argument_list|,
name|serviceListGenerator
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|invokeDestination
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|res
parameter_list|,
name|AbstractHTTPDestination
name|d
parameter_list|)
throws|throws
name|ServletException
block|{
name|invokeDestinationCalled
operator|=
literal|true
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
specifier|final
name|String
name|ADDRESS
init|=
literal|"http://bar/snafu"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ROOT
init|=
literal|"http://localhost:8080/"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|QNAME
init|=
operator|new
name|QName
argument_list|(
name|ADDRESS
argument_list|,
literal|"foobar"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PATH
init|=
literal|"/SoapContext/SoapPort"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|URI
init|=
literal|"/cxf"
operator|+
name|PATH
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SERVICES
init|=
literal|"/cxf/services"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|QUERY
init|=
literal|"wsdl"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEXT
init|=
literal|"text/html"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEXT_LIST
init|=
literal|"text/html; charset=UTF-8"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|XML
init|=
literal|"text/xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NO_SERVICE
init|=
literal|"<html><body>No service was found.</body></html>"
decl_stmt|;
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|DestinationRegistry
name|registry
decl_stmt|;
specifier|private
name|AbstractHTTPDestination
name|destination
decl_stmt|;
specifier|private
name|ServletConfig
name|config
decl_stmt|;
specifier|private
name|HttpServletRequest
name|request
decl_stmt|;
specifier|private
name|HttpServletResponse
name|response
decl_stmt|;
specifier|private
name|MessageObserver
name|observer
decl_stmt|;
specifier|private
name|AddressType
name|extensor
decl_stmt|;
specifier|private
name|EndpointInfo
name|endpoint
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|paths
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
name|bus
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
expr_stmt|;
name|registry
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|DestinationRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|destination
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|AbstractHTTPDestination
operator|.
name|class
argument_list|)
expr_stmt|;
name|config
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|ServletConfig
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
expr_stmt|;
name|response
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|HttpServletResponse
operator|.
name|class
argument_list|)
expr_stmt|;
name|observer
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|MessageObserver
operator|.
name|class
argument_list|)
expr_stmt|;
name|extensor
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|AddressType
operator|.
name|class
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|new
name|EndpointInfo
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|setAddress
argument_list|(
name|PATH
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setName
argument_list|(
name|QNAME
argument_list|)
expr_stmt|;
name|ServiceInfo
name|service
init|=
operator|new
name|ServiceInfo
argument_list|()
decl_stmt|;
name|service
operator|.
name|setInterface
argument_list|(
operator|new
name|InterfaceInfo
argument_list|(
name|service
argument_list|,
name|QNAME
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setService
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|paths
operator|=
operator|new
name|TreeSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|bus
operator|=
literal|null
expr_stmt|;
name|registry
operator|=
literal|null
expr_stmt|;
name|destination
operator|=
literal|null
expr_stmt|;
name|config
operator|=
literal|null
expr_stmt|;
name|request
operator|=
literal|null
expr_stmt|;
name|response
operator|=
literal|null
expr_stmt|;
name|observer
operator|=
literal|null
expr_stmt|;
name|extensor
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvokeNoDestination
parameter_list|()
throws|throws
name|Exception
block|{
name|setUpRequest
argument_list|(
name|URI
argument_list|,
literal|null
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|setUpResponse
argument_list|(
literal|404
argument_list|,
name|TEXT
argument_list|,
name|NO_SERVICE
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|OsgiServlet
name|servlet
init|=
operator|new
name|OsgiServlet
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|servlet
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|servlet
operator|.
name|invoke
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvokeGetServices
parameter_list|()
throws|throws
name|Exception
block|{
name|setUpRequest
argument_list|(
name|SERVICES
argument_list|,
literal|null
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|HttpServlet
name|serviceListGenerator
init|=
name|control
operator|.
name|createMock
argument_list|(
name|HttpServlet
operator|.
name|class
argument_list|)
decl_stmt|;
name|serviceListGenerator
operator|.
name|service
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|ServletRequest
operator|.
name|class
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|isA
argument_list|(
name|ServletResponse
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|OsgiServlet
name|servlet
init|=
operator|new
name|OsgiServlet
argument_list|(
name|registry
argument_list|,
name|serviceListGenerator
argument_list|)
decl_stmt|;
name|servlet
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|servlet
operator|.
name|invoke
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvokeGetServicesNoService
parameter_list|()
throws|throws
name|Exception
block|{
name|setUpRequest
argument_list|(
name|SERVICES
argument_list|,
literal|null
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|setUpResponse
argument_list|(
literal|0
argument_list|,
name|TEXT_LIST
argument_list|,
literal|"<span class=\"heading\">No services have been found.</span>"
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|OsgiServlet
name|servlet
init|=
operator|new
name|OsgiServlet
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|servlet
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|servlet
operator|.
name|invoke
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvokeWsdlQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|setUpRequest
argument_list|(
name|URI
argument_list|,
name|PATH
argument_list|,
operator|-
literal|2
argument_list|)
expr_stmt|;
name|setUpQuery
argument_list|()
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|OsgiServlet
name|servlet
init|=
operator|new
name|OsgiServlet
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|servlet
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|servlet
operator|.
name|invoke
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvokeDestination
parameter_list|()
throws|throws
name|Exception
block|{
name|setUpRequest
argument_list|(
name|URI
argument_list|,
name|PATH
argument_list|,
operator|-
literal|2
argument_list|)
expr_stmt|;
name|HttpServlet
name|serviceListGenerator
init|=
name|control
operator|.
name|createMock
argument_list|(
name|HttpServlet
operator|.
name|class
argument_list|)
decl_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|TestOsgiServletController
name|controller
init|=
operator|new
name|TestOsgiServletController
argument_list|(
name|config
argument_list|,
name|registry
argument_list|,
name|serviceListGenerator
argument_list|)
decl_stmt|;
name|controller
operator|.
name|invoke
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|controller
operator|.
name|invokeDestinationCalled
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvokeRestful
parameter_list|()
throws|throws
name|Exception
block|{
name|setUpRequest
argument_list|(
name|URI
argument_list|,
literal|null
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
comment|//EasyMock.expect(request.getContextPath()).andReturn("");
comment|//EasyMock.expect(request.getServletPath()).andReturn("/cxf");
name|paths
operator|.
name|add
argument_list|(
name|PATH
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|registry
operator|.
name|getDestinationForPath
argument_list|(
name|PATH
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|destination
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|registry
operator|.
name|checkRestfulRequest
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|destination
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|destination
operator|.
name|getMessageObserver
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|observer
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|addExtensor
argument_list|(
name|extensor
argument_list|)
expr_stmt|;
comment|//extensor.setLocation(EasyMock.eq(ROOT + URI));
comment|//EasyMock.expectLastCall();
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|OsgiServlet
name|servlet
init|=
operator|new
name|OsgiServlet
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|servlet
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|servlet
operator|.
name|invoke
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|setUpRequest
parameter_list|(
name|String
name|requestURI
parameter_list|,
name|String
name|path
parameter_list|,
name|int
name|destinationCount
parameter_list|)
throws|throws
name|Exception
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|request
operator|.
name|getRequestURI
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|requestURI
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|StringBuffer
name|url
init|=
operator|new
name|StringBuffer
argument_list|(
name|ROOT
operator|+
name|requestURI
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|request
operator|.
name|getRequestURL
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|url
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|request
operator|.
name|getQueryString
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|QUERY
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|destination
operator|.
name|getEndpointInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|endpoint
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|destination
operator|.
name|getBus
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bus
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|request
operator|.
name|getPathInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|path
operator|!=
literal|null
condition|?
name|path
else|:
name|PATH
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|registry
operator|.
name|getDestinationForPath
argument_list|(
name|path
argument_list|,
literal|true
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|destination
argument_list|)
expr_stmt|;
block|}
name|EasyMock
operator|.
name|expect
argument_list|(
name|registry
operator|.
name|getDestinationsPaths
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|paths
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
if|if
condition|(
name|destinationCount
operator|>=
literal|0
condition|)
block|{
name|List
argument_list|<
name|AbstractHTTPDestination
argument_list|>
name|destinations
init|=
operator|new
name|ArrayList
argument_list|<
name|AbstractHTTPDestination
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|destinationCount
condition|;
name|i
operator|++
control|)
block|{
name|destinations
operator|.
name|add
argument_list|(
name|destination
argument_list|)
expr_stmt|;
block|}
name|EasyMock
operator|.
name|expect
argument_list|(
name|registry
operator|.
name|getDestinations
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|destinations
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|registry
operator|.
name|getSortedDestinations
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|destinations
operator|.
name|toArray
argument_list|(
operator|new
name|AbstractDestination
index|[]
block|{}
argument_list|)
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|setUpResponse
parameter_list|(
name|int
name|status
parameter_list|,
name|String
name|responseType
parameter_list|,
name|String
modifier|...
name|responseMsgs
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|status
operator|!=
literal|0
condition|)
block|{
name|response
operator|.
name|setStatus
argument_list|(
name|status
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|responseType
operator|!=
literal|null
condition|)
block|{
name|response
operator|.
name|setContentType
argument_list|(
name|responseType
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|responseMsgs
operator|!=
literal|null
condition|)
block|{
name|PrintWriter
name|writer
init|=
name|control
operator|.
name|createMock
argument_list|(
name|PrintWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|response
operator|.
name|getWriter
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|writer
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|msg
range|:
name|responseMsgs
control|)
block|{
name|writer
operator|.
name|write
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|setUpQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|QueryHandlerRegistry
name|qrh
init|=
name|control
operator|.
name|createMock
argument_list|(
name|QueryHandlerRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|QueryHandlerRegistry
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|qrh
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|QueryHandler
name|qh
init|=
name|control
operator|.
name|createMock
argument_list|(
name|QueryHandler
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|QueryHandler
argument_list|>
name|handlers
init|=
operator|new
name|ArrayList
argument_list|<
name|QueryHandler
argument_list|>
argument_list|()
decl_stmt|;
name|handlers
operator|.
name|add
argument_list|(
name|qh
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|qrh
operator|.
name|getHandlers
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|handlers
argument_list|)
expr_stmt|;
name|String
name|base
init|=
name|ROOT
operator|+
name|URI
operator|+
literal|"?"
operator|+
name|QUERY
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|qh
operator|.
name|isRecognizedQuery
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
name|base
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|eq
argument_list|(
name|PATH
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|same
argument_list|(
name|endpoint
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|qh
operator|.
name|getResponseContentType
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
name|base
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|eq
argument_list|(
name|PATH
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|XML
argument_list|)
expr_stmt|;
name|ServletOutputStream
name|sos
init|=
name|control
operator|.
name|createMock
argument_list|(
name|ServletOutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|response
operator|.
name|getOutputStream
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|sos
argument_list|)
expr_stmt|;
name|qh
operator|.
name|writeResponse
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
name|base
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|eq
argument_list|(
name|PATH
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|same
argument_list|(
name|endpoint
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|same
argument_list|(
name|sos
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|sos
operator|.
name|flush
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

