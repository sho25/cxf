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
package|;
end_package

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
name|UUID
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PreDestroy
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
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
name|CookieParam
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
name|DefaultValue
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
name|HeaderParam
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
name|MatrixParam
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
name|PathParam
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
name|QueryParam
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
name|PathSegment
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|PathSegmentImpl
import|;
end_import

begin_class
specifier|public
class|class
name|Customer
extends|extends
name|AbstractCustomer
implements|implements
name|CustomerInfo
block|{
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"CustomerBean"
argument_list|)
specifier|public
specifier|static
class|class
name|CustomerBean
block|{
specifier|private
name|String
name|a
decl_stmt|;
specifier|private
name|Long
name|b
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|c
decl_stmt|;
specifier|private
name|CustomerBean
name|d
decl_stmt|;
comment|//CHECKSTYLE:OFF
specifier|public
name|List
argument_list|<
name|CustomerBean
argument_list|>
name|e
decl_stmt|;
comment|//CHECKSTYLE:ON
specifier|public
name|void
name|setA
parameter_list|(
name|String
name|aString
parameter_list|)
block|{
name|this
operator|.
name|a
operator|=
name|aString
expr_stmt|;
block|}
specifier|public
name|void
name|setB
parameter_list|(
name|Long
name|bLong
parameter_list|)
block|{
name|this
operator|.
name|b
operator|=
name|bLong
expr_stmt|;
block|}
specifier|public
name|void
name|setC
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|cStringList
parameter_list|)
block|{
name|this
operator|.
name|c
operator|=
name|cStringList
expr_stmt|;
block|}
specifier|public
name|void
name|setD
parameter_list|(
name|CustomerBean
name|dCustomerBean
parameter_list|)
block|{
name|this
operator|.
name|d
operator|=
name|dCustomerBean
expr_stmt|;
block|}
specifier|public
name|String
name|getA
parameter_list|()
block|{
return|return
name|a
return|;
block|}
specifier|public
name|Long
name|getB
parameter_list|()
block|{
return|return
name|b
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getC
parameter_list|()
block|{
return|return
name|c
return|;
block|}
specifier|public
name|CustomerBean
name|getD
parameter_list|()
block|{
return|return
name|d
return|;
block|}
block|}
annotation|@
name|Context
specifier|private
name|ContextResolver
argument_list|<
name|JAXBContext
argument_list|>
name|cr
decl_stmt|;
specifier|private
name|UriInfo
name|uriInfo
decl_stmt|;
annotation|@
name|Context
specifier|private
name|HttpHeaders
name|headers
decl_stmt|;
annotation|@
name|Context
specifier|private
name|Request
name|request
decl_stmt|;
annotation|@
name|Context
specifier|private
name|SecurityContext
name|sContext
decl_stmt|;
annotation|@
name|Context
specifier|private
name|Providers
name|bodyWorkers
decl_stmt|;
annotation|@
name|Resource
specifier|private
name|HttpServletRequest
name|servletRequest
decl_stmt|;
annotation|@
name|Resource
specifier|private
name|HttpServletResponse
name|servletResponse
decl_stmt|;
annotation|@
name|Resource
specifier|private
name|ServletContext
name|servletContext
decl_stmt|;
annotation|@
name|Context
specifier|private
name|HttpServletRequest
name|servletRequest2
decl_stmt|;
annotation|@
name|Context
specifier|private
name|HttpServletResponse
name|servletResponse2
decl_stmt|;
annotation|@
name|Context
specifier|private
name|ServletContext
name|servletContext2
decl_stmt|;
specifier|private
name|ServletContext
name|servletContext3
decl_stmt|;
annotation|@
name|Context
specifier|private
name|UriInfo
name|uriInfo2
decl_stmt|;
specifier|private
name|String
name|queryParam
decl_stmt|;
annotation|@
name|QueryParam
argument_list|(
literal|"b"
argument_list|)
specifier|private
name|String
name|b
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|boolean
name|postConstuctCalled
decl_stmt|;
specifier|private
name|boolean
name|preDestroyCalled
decl_stmt|;
specifier|public
name|Customer
parameter_list|()
block|{              }
specifier|public
name|Customer
parameter_list|(
annotation|@
name|Context
name|UriInfo
name|info
parameter_list|)
block|{
name|uriInfo
operator|=
name|info
expr_stmt|;
block|}
specifier|public
name|Customer
parameter_list|(
annotation|@
name|Context
name|UriInfo
name|info
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"a"
argument_list|)
name|String
name|queryParam
parameter_list|)
block|{
name|uriInfo
operator|=
name|info
expr_stmt|;
name|this
operator|.
name|queryParam
operator|=
name|queryParam
expr_stmt|;
block|}
annotation|@
name|PostConstruct
specifier|public
name|void
name|postConstruct
parameter_list|()
block|{
name|postConstuctCalled
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|boolean
name|isPostConstuctCalled
parameter_list|()
block|{
return|return
name|postConstuctCalled
return|;
block|}
annotation|@
name|PreDestroy
specifier|public
name|void
name|preDestroy
parameter_list|()
block|{
name|preDestroyCalled
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|boolean
name|isPreDestroyCalled
parameter_list|()
block|{
return|return
name|preDestroyCalled
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|name
operator|=
name|n
expr_stmt|;
block|}
specifier|public
name|String
name|getB
parameter_list|()
block|{
return|return
name|b
return|;
block|}
specifier|public
name|void
name|testQueryBean
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|""
argument_list|)
name|CustomerBean
name|cb
parameter_list|)
block|{              }
specifier|public
name|void
name|testPathBean
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|""
argument_list|)
name|CustomerBean
name|cb
parameter_list|)
block|{              }
specifier|public
name|void
name|testFormBean
parameter_list|(
annotation|@
name|FormParam
argument_list|(
literal|""
argument_list|)
name|CustomerBean
name|cb
parameter_list|)
block|{              }
specifier|public
name|void
name|testMatrixBean
parameter_list|(
annotation|@
name|MatrixParam
argument_list|(
literal|""
argument_list|)
name|CustomerBean
name|cb
parameter_list|)
block|{              }
specifier|public
name|UriInfo
name|getUriInfo
parameter_list|()
block|{
return|return
name|uriInfo
return|;
block|}
specifier|public
name|UriInfo
name|getUriInfo2
parameter_list|()
block|{
return|return
name|uriInfo2
return|;
block|}
annotation|@
name|Context
specifier|public
name|void
name|setUriInfo
parameter_list|(
name|UriInfo
name|ui
parameter_list|)
block|{
name|uriInfo
operator|=
name|ui
expr_stmt|;
block|}
specifier|public
name|void
name|setUriInfoContext
parameter_list|(
name|UriInfo
name|ui
parameter_list|)
block|{     }
annotation|@
name|Context
specifier|public
name|void
name|setServletContext
parameter_list|(
name|ServletContext
name|sc
parameter_list|)
block|{
name|servletContext3
operator|=
name|sc
expr_stmt|;
block|}
specifier|public
name|ServletContext
name|getThreadLocalServletContext
parameter_list|()
block|{
return|return
name|servletContext3
return|;
block|}
annotation|@
name|QueryParam
argument_list|(
literal|"a"
argument_list|)
specifier|public
name|void
name|setA
parameter_list|(
name|String
name|a
parameter_list|)
block|{
name|queryParam
operator|=
name|a
expr_stmt|;
block|}
specifier|public
name|String
name|getQueryParam
parameter_list|()
block|{
return|return
name|queryParam
return|;
block|}
specifier|public
name|HttpHeaders
name|getHeaders
parameter_list|()
block|{
return|return
name|headers
return|;
block|}
specifier|public
name|Request
name|getRequest
parameter_list|()
block|{
return|return
name|request
return|;
block|}
specifier|public
name|Providers
name|getBodyWorkers
parameter_list|()
block|{
return|return
name|bodyWorkers
return|;
block|}
specifier|public
name|SecurityContext
name|getSecurityContext
parameter_list|()
block|{
return|return
name|sContext
return|;
block|}
specifier|public
name|HttpServletRequest
name|getServletRequest
parameter_list|()
block|{
return|return
name|servletRequest2
return|;
block|}
specifier|public
name|HttpServletResponse
name|getServletResponse
parameter_list|()
block|{
return|return
name|servletResponse2
return|;
block|}
specifier|public
name|ServletContext
name|getServletContext
parameter_list|()
block|{
return|return
name|servletContext2
return|;
block|}
specifier|public
name|HttpServletRequest
name|getServletRequestResource
parameter_list|()
block|{
return|return
name|servletRequest
return|;
block|}
specifier|public
name|HttpServletResponse
name|getServletResponseResource
parameter_list|()
block|{
return|return
name|servletResponse
return|;
block|}
specifier|public
name|ServletContext
name|getServletContextResource
parameter_list|()
block|{
return|return
name|servletContext
return|;
block|}
specifier|public
name|ContextResolver
name|getContextResolver
parameter_list|()
block|{
return|return
name|cr
return|;
block|}
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|void
name|test
parameter_list|()
block|{
comment|// complete
block|}
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|void
name|getItAsXML
parameter_list|()
block|{
comment|// complete
block|}
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|void
name|getItPlain
parameter_list|()
block|{
comment|// complete
block|}
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|void
name|testQuery
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"query"
argument_list|)
name|String
name|queryString
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"query"
argument_list|)
name|int
name|queryInt
parameter_list|)
block|{
comment|// complete
block|}
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|void
name|testPathSegment
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"ps"
argument_list|)
name|PathSegment
name|ps
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"ps"
argument_list|)
name|String
name|path
parameter_list|)
block|{
comment|// complete
block|}
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|void
name|testMultipleQuery
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"query"
argument_list|)
name|String
name|queryString
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"query2"
argument_list|)
name|String
name|queryString2
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"query3"
argument_list|)
name|Long
name|queryString3
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"query4"
argument_list|)
name|boolean
name|queryBoolean4
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"query5"
argument_list|)
name|String
name|queryString4
parameter_list|)
block|{
comment|// complete
block|}
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|void
name|testMatrixParam
parameter_list|(
annotation|@
name|MatrixParam
argument_list|(
literal|"p1"
argument_list|)
name|String
name|mp1
parameter_list|,
annotation|@
name|MatrixParam
argument_list|(
literal|"p2"
argument_list|)
name|String
name|mp2
parameter_list|,
annotation|@
name|MatrixParam
argument_list|(
literal|"p3"
argument_list|)
name|String
name|mp3
parameter_list|,
annotation|@
name|MatrixParam
argument_list|(
literal|"p4"
argument_list|)
name|String
name|mp4
parameter_list|,
annotation|@
name|MatrixParam
argument_list|(
literal|"p4"
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|mp4List
parameter_list|)
block|{
comment|// complete
block|}
specifier|public
name|void
name|testCustomerParam
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"p1"
argument_list|)
name|Customer
name|c
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"p2"
argument_list|)
name|Customer
index|[]
name|c2
parameter_list|)
block|{
comment|// complete
block|}
specifier|public
name|void
name|testCustomerParam2
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"p1"
argument_list|)
name|String
index|[]
name|p
parameter_list|)
block|{
comment|// complete
block|}
specifier|public
name|void
name|testFromStringParam
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"p1"
argument_list|)
name|UUID
name|uuid
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"p2"
argument_list|)
name|CustomerGender
name|gender
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"p3"
argument_list|)
name|CustomerGender
name|gender2
parameter_list|)
block|{
comment|// complete
block|}
specifier|public
name|void
name|testFromValueParam
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"p1"
argument_list|)
name|Timezone
name|tzone
parameter_list|)
block|{
comment|// complete
block|}
comment|//  CHECKSTYLE:OFF
specifier|public
name|void
name|testWrongType
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"p1"
argument_list|)
name|HashMap
name|map
parameter_list|)
block|{
comment|// complete
block|}
comment|//  CHECKSTYLE:ON
specifier|public
name|void
name|testWrongType2
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"p1"
argument_list|)
name|CustomerGender
name|g
parameter_list|)
block|{
comment|// complete
block|}
specifier|public
name|void
name|testFormParam
parameter_list|(
annotation|@
name|FormParam
argument_list|(
literal|"p1"
argument_list|)
name|String
name|fp1
parameter_list|,
annotation|@
name|FormParam
argument_list|(
literal|"p2"
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|fp2
parameter_list|)
block|{
comment|// complete
block|}
specifier|public
name|void
name|testCookieParam
parameter_list|(
annotation|@
name|CookieParam
argument_list|(
literal|"c1"
argument_list|)
name|String
name|c1
parameter_list|,
annotation|@
name|CookieParam
argument_list|(
literal|"c2"
argument_list|)
annotation|@
name|DefaultValue
argument_list|(
literal|"c2Value"
argument_list|)
name|String
name|c2
parameter_list|)
block|{
comment|// complete
block|}
specifier|public
name|void
name|testParams
parameter_list|(
annotation|@
name|Context
name|UriInfo
name|info
parameter_list|,
annotation|@
name|Context
name|HttpHeaders
name|hs
parameter_list|,
annotation|@
name|Context
name|Request
name|r
parameter_list|,
annotation|@
name|Context
name|SecurityContext
name|s
parameter_list|,
annotation|@
name|Context
name|Providers
name|workers
parameter_list|,
annotation|@
name|HeaderParam
argument_list|(
literal|"Foo"
argument_list|)
name|String
name|h
parameter_list|,
annotation|@
name|HeaderParam
argument_list|(
literal|"Foo"
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|l
parameter_list|)
block|{
comment|// complete
block|}
specifier|public
name|void
name|testServletParams
parameter_list|(
annotation|@
name|Context
name|HttpServletRequest
name|req
parameter_list|,
annotation|@
name|Context
name|HttpServletResponse
name|res
parameter_list|,
annotation|@
name|Context
name|ServletContext
name|context
parameter_list|,
annotation|@
name|Context
name|ServletConfig
name|config
parameter_list|)
block|{
comment|// complete
block|}
annotation|@
name|Path
argument_list|(
literal|"{id1}/{id2}"
argument_list|)
specifier|public
name|void
name|testConversion
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id1"
argument_list|)
name|PathSegmentImpl
name|id1
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"id2"
argument_list|)
name|SimpleFactory
name|f
parameter_list|)
block|{
comment|// complete
block|}
specifier|public
name|void
name|testContextResolvers
parameter_list|(
annotation|@
name|Context
name|ContextResolver
argument_list|<
name|JAXBContext
argument_list|>
name|resolver
parameter_list|)
block|{
comment|// complete
block|}
block|}
end_class

begin_empty_stmt
empty_stmt|;
end_empty_stmt

end_unit

