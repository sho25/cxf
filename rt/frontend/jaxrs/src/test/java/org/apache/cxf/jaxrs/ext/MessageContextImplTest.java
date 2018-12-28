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
name|ext
package|;
end_package

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
name|ServletContext
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
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Application
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
name|SecurityContext
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
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|ContextResolver
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
name|ext
operator|.
name|Providers
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
name|impl
operator|.
name|HttpHeadersImpl
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
name|impl
operator|.
name|HttpServletRequestFilter
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
name|impl
operator|.
name|HttpServletResponseFilter
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
name|impl
operator|.
name|ProvidersImpl
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
name|impl
operator|.
name|RequestImpl
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
name|impl
operator|.
name|SecurityContextImpl
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
name|impl
operator|.
name|UriInfoImpl
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
name|provider
operator|.
name|ServerProviderFactory
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
name|easymock
operator|.
name|EasyMock
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
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertSame
import|;
end_import

begin_class
specifier|public
class|class
name|MessageContextImplTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetProperty
parameter_list|()
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
literal|"a"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|MessageContext
name|mc
init|=
operator|new
name|MessageContextImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|mc
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|mc
operator|.
name|get
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetPropertyFromExchange
parameter_list|()
block|{
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|Exchange
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|ex
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setInMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|MessageContext
name|mc
init|=
operator|new
name|MessageContextImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|mc
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|mc
operator|.
name|get
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetPropertyFromOtherMessage
parameter_list|()
block|{
name|Message
name|m1
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|Message
name|m2
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|m2
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|Exchange
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|ex
operator|.
name|setInMessage
argument_list|(
name|m1
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setOutMessage
argument_list|(
name|m2
argument_list|)
expr_stmt|;
name|MessageContext
name|mc
init|=
operator|new
name|MessageContextImpl
argument_list|(
name|m1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|mc
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|mc
operator|.
name|get
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetUriInfo
parameter_list|()
block|{
name|MessageContext
name|mc
init|=
operator|new
name|MessageContextImpl
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|UriInfoImpl
operator|.
name|class
argument_list|,
name|mc
operator|.
name|getUriInfo
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|UriInfoImpl
operator|.
name|class
argument_list|,
name|mc
operator|.
name|getContext
argument_list|(
name|UriInfo
operator|.
name|class
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetRequest
parameter_list|()
block|{
name|MessageContext
name|mc
init|=
operator|new
name|MessageContextImpl
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|RequestImpl
operator|.
name|class
argument_list|,
name|mc
operator|.
name|getRequest
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|RequestImpl
operator|.
name|class
argument_list|,
name|mc
operator|.
name|getContext
argument_list|(
name|Request
operator|.
name|class
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetHttpHeaders
parameter_list|()
block|{
name|MessageContext
name|mc
init|=
operator|new
name|MessageContextImpl
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|HttpHeadersImpl
operator|.
name|class
argument_list|,
name|mc
operator|.
name|getHttpHeaders
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|HttpHeadersImpl
operator|.
name|class
argument_list|,
name|mc
operator|.
name|getContext
argument_list|(
name|HttpHeaders
operator|.
name|class
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetSecurityContext
parameter_list|()
block|{
name|MessageContext
name|mc
init|=
operator|new
name|MessageContextImpl
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|SecurityContextImpl
operator|.
name|class
argument_list|,
name|mc
operator|.
name|getSecurityContext
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|SecurityContextImpl
operator|.
name|class
argument_list|,
name|mc
operator|.
name|getContext
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProviders
parameter_list|()
block|{
name|MessageContext
name|mc
init|=
operator|new
name|MessageContextImpl
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|ProvidersImpl
operator|.
name|class
argument_list|,
name|mc
operator|.
name|getProviders
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|ProvidersImpl
operator|.
name|class
argument_list|,
name|mc
operator|.
name|getContext
argument_list|(
name|Providers
operator|.
name|class
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHttpRequest
parameter_list|()
block|{
name|Message
name|m
init|=
name|createMessage
argument_list|()
decl_stmt|;
name|MessageContext
name|mc
init|=
operator|new
name|MessageContextImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|HttpServletRequest
name|request
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|AbstractHTTPDestination
operator|.
name|HTTP_REQUEST
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|request
operator|.
name|getClass
argument_list|()
argument_list|,
operator|(
operator|(
name|HttpServletRequestFilter
operator|)
name|mc
operator|.
name|getHttpServletRequest
argument_list|()
operator|)
operator|.
name|getRequest
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|request
operator|.
name|getClass
argument_list|()
argument_list|,
operator|(
operator|(
name|HttpServletRequestFilter
operator|)
name|mc
operator|.
name|getContext
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
operator|)
operator|.
name|getRequest
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHttpResponse
parameter_list|()
block|{
name|Message
name|m
init|=
name|createMessage
argument_list|()
decl_stmt|;
name|MessageContext
name|mc
init|=
operator|new
name|MessageContextImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|HttpServletResponse
name|request
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|HttpServletResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|AbstractHTTPDestination
operator|.
name|HTTP_RESPONSE
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|HttpServletResponseFilter
name|filter
init|=
operator|(
name|HttpServletResponseFilter
operator|)
name|mc
operator|.
name|getHttpServletResponse
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|request
operator|.
name|getClass
argument_list|()
argument_list|,
name|filter
operator|.
name|getResponse
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|filter
operator|=
operator|(
name|HttpServletResponseFilter
operator|)
name|mc
operator|.
name|getContext
argument_list|(
name|HttpServletResponse
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|request
operator|.
name|getClass
argument_list|()
argument_list|,
name|filter
operator|.
name|getResponse
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testServletContext
parameter_list|()
block|{
name|Message
name|m
init|=
name|createMessage
argument_list|()
decl_stmt|;
name|MessageContext
name|mc
init|=
operator|new
name|MessageContextImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|ServletContext
name|request
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ServletContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|AbstractHTTPDestination
operator|.
name|HTTP_CONTEXT
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|request
operator|.
name|getClass
argument_list|()
argument_list|,
name|mc
operator|.
name|getServletContext
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|request
operator|.
name|getClass
argument_list|()
argument_list|,
name|mc
operator|.
name|getContext
argument_list|(
name|ServletContext
operator|.
name|class
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testServletConfig
parameter_list|()
block|{
name|Message
name|m
init|=
name|createMessage
argument_list|()
decl_stmt|;
name|MessageContext
name|mc
init|=
operator|new
name|MessageContextImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|ServletConfig
name|request
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ServletConfig
operator|.
name|class
argument_list|)
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|AbstractHTTPDestination
operator|.
name|HTTP_CONFIG
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|request
operator|.
name|getClass
argument_list|()
argument_list|,
name|mc
operator|.
name|getServletConfig
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|request
operator|.
name|getClass
argument_list|()
argument_list|,
name|mc
operator|.
name|getContext
argument_list|(
name|ServletConfig
operator|.
name|class
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testContextResolver
parameter_list|()
block|{
name|ContextResolver
argument_list|<
name|JAXBContext
argument_list|>
name|resolver
init|=
operator|new
name|CustomContextResolver
argument_list|()
decl_stmt|;
name|ProviderFactory
name|factory
init|=
name|ServerProviderFactory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|factory
operator|.
name|registerUserProvider
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|Exchange
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setInMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|Endpoint
name|e
init|=
name|EasyMock
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
name|get
argument_list|(
name|ServerProviderFactory
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
name|factory
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|MessageContext
name|mc
init|=
operator|new
name|MessageContextImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|ContextResolver
argument_list|<
name|JAXBContext
argument_list|>
name|resolver2
init|=
name|mc
operator|.
name|getResolver
argument_list|(
name|ContextResolver
operator|.
name|class
argument_list|,
name|JAXBContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|resolver2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|resolver2
argument_list|,
name|resolver
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoContext
parameter_list|()
block|{
name|MessageContext
name|mc
init|=
operator|new
name|MessageContextImpl
argument_list|(
name|createMessage
argument_list|()
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|mc
operator|.
name|getContext
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Message
name|createMessage
parameter_list|()
block|{
name|ProviderFactory
name|factory
init|=
name|ServerProviderFactory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
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
name|e
operator|.
name|setInMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|endpoint
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
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|get
argument_list|(
name|Application
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
literal|null
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|size
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|0
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|get
argument_list|(
name|ServerProviderFactory
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
name|factory
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|e
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
specifier|public
specifier|static
class|class
name|CustomContextResolver
implements|implements
name|ContextResolver
argument_list|<
name|JAXBContext
argument_list|>
block|{
specifier|public
name|JAXBContext
name|getContext
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

