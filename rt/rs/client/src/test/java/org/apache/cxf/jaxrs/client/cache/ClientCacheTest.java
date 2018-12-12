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
name|client
operator|.
name|cache
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
name|io
operator|.
name|Serializable
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
name|client
operator|.
name|ClientBuilder
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
name|client
operator|.
name|Invocation
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
name|client
operator|.
name|WebTarget
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
name|CacheControl
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
name|endpoint
operator|.
name|Server
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
name|lifecycle
operator|.
name|SingletonResourceProvider
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
name|local
operator|.
name|LocalConduit
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
name|local
operator|.
name|LocalTransportFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
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
name|BeforeClass
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
name|ClientCacheTest
extends|extends
name|Assert
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ADDRESS
init|=
literal|"local://transport"
decl_stmt|;
specifier|private
specifier|static
name|Server
name|server
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|bind
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|JAXRSServerFactoryBean
name|sf
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setResourceClasses
argument_list|(
name|TheServer
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setResourceProvider
argument_list|(
name|TheServer
operator|.
name|class
argument_list|,
operator|new
name|SingletonResourceProvider
argument_list|(
operator|new
name|TheServer
argument_list|()
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setTransportId
argument_list|(
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
name|ADDRESS
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
name|AfterClass
specifier|public
specifier|static
name|void
name|unbind
parameter_list|()
throws|throws
name|Exception
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetTimeString
parameter_list|()
block|{
name|CacheControlFeature
name|feature
init|=
operator|new
name|CacheControlFeature
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|WebTarget
name|base
init|=
name|ClientBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|register
argument_list|(
name|feature
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|target
argument_list|(
name|ADDRESS
argument_list|)
decl_stmt|;
specifier|final
name|Invocation
operator|.
name|Builder
name|cached
init|=
name|base
operator|.
name|request
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|CACHE_CONTROL
argument_list|,
literal|"public"
argument_list|)
decl_stmt|;
specifier|final
name|Response
name|r
init|=
name|cached
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|r1
init|=
name|r
operator|.
name|readEntity
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|waitABit
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|r1
argument_list|,
name|cached
operator|.
name|get
argument_list|()
operator|.
name|readEntity
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|feature
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetTimeStringAsInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|CacheControlFeature
name|feature
init|=
operator|new
name|CacheControlFeature
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|WebTarget
name|base
init|=
name|ClientBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|register
argument_list|(
name|feature
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|target
argument_list|(
name|ADDRESS
argument_list|)
decl_stmt|;
specifier|final
name|Invocation
operator|.
name|Builder
name|cached
init|=
name|base
operator|.
name|request
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|CACHE_CONTROL
argument_list|,
literal|"public"
argument_list|)
decl_stmt|;
specifier|final
name|Response
name|r
init|=
name|cached
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|r
operator|.
name|readEntity
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|String
name|r1
init|=
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|waitABit
argument_list|()
expr_stmt|;
name|is
operator|=
name|cached
operator|.
name|get
argument_list|()
operator|.
name|readEntity
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
expr_stmt|;
specifier|final
name|String
name|r2
init|=
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|r1
argument_list|,
name|r2
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|feature
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetTimeStringAsInputStreamAndString
parameter_list|()
throws|throws
name|Exception
block|{
name|CacheControlFeature
name|feature
init|=
operator|new
name|CacheControlFeature
argument_list|()
decl_stmt|;
try|try
block|{
name|feature
operator|.
name|setCacheResponseInputStream
argument_list|(
literal|true
argument_list|)
expr_stmt|;
specifier|final
name|WebTarget
name|base
init|=
name|ClientBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|register
argument_list|(
name|feature
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|target
argument_list|(
name|ADDRESS
argument_list|)
decl_stmt|;
specifier|final
name|Invocation
operator|.
name|Builder
name|cached
init|=
name|base
operator|.
name|request
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|CACHE_CONTROL
argument_list|,
literal|"public"
argument_list|)
decl_stmt|;
specifier|final
name|Response
name|r
init|=
name|cached
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|r
operator|.
name|readEntity
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|String
name|r1
init|=
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|waitABit
argument_list|()
expr_stmt|;
comment|// CassCastException would occur without a cached stream support
specifier|final
name|String
name|r2
init|=
name|cached
operator|.
name|get
argument_list|()
operator|.
name|readEntity
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|r1
argument_list|,
name|r2
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|feature
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetTimeStringAsStringAndInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|CacheControlFeature
name|feature
init|=
operator|new
name|CacheControlFeature
argument_list|()
decl_stmt|;
try|try
block|{
name|feature
operator|.
name|setCacheResponseInputStream
argument_list|(
literal|true
argument_list|)
expr_stmt|;
specifier|final
name|WebTarget
name|base
init|=
name|ClientBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|register
argument_list|(
name|feature
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|target
argument_list|(
name|ADDRESS
argument_list|)
decl_stmt|;
specifier|final
name|Invocation
operator|.
name|Builder
name|cached
init|=
name|base
operator|.
name|request
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|CACHE_CONTROL
argument_list|,
literal|"public"
argument_list|)
decl_stmt|;
specifier|final
name|Response
name|r
init|=
name|cached
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|r1
init|=
name|r
operator|.
name|readEntity
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|waitABit
argument_list|()
expr_stmt|;
comment|// CassCastException would occur without a cached stream support
name|InputStream
name|is
init|=
name|cached
operator|.
name|get
argument_list|()
operator|.
name|readEntity
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|String
name|r2
init|=
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|r1
argument_list|,
name|r2
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|feature
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetJaxbBookCache
parameter_list|()
block|{
name|CacheControlFeature
name|feature
init|=
operator|new
name|CacheControlFeature
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|WebTarget
name|base
init|=
name|ClientBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|register
argument_list|(
name|feature
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|target
argument_list|(
name|ADDRESS
argument_list|)
decl_stmt|;
specifier|final
name|Invocation
operator|.
name|Builder
name|cached
init|=
name|setAsLocal
argument_list|(
name|base
operator|.
name|request
argument_list|(
literal|"application/xml"
argument_list|)
argument_list|)
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|CACHE_CONTROL
argument_list|,
literal|"public"
argument_list|)
decl_stmt|;
specifier|final
name|Response
name|r
init|=
name|cached
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Book
name|b1
init|=
name|r
operator|.
name|readEntity
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"JCache"
argument_list|,
name|b1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|b1
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|waitABit
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|b1
argument_list|,
name|cached
operator|.
name|get
argument_list|()
operator|.
name|readEntity
argument_list|(
name|Book
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|feature
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetJaxbBookCacheByValue
parameter_list|()
block|{
comment|// org.apache.cxf.jaxrs.client.cache.CacheControlFeature.storeByValue
name|CacheControlFeature
name|feature
init|=
operator|new
name|CacheControlFeature
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|WebTarget
name|base
init|=
name|ClientBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|property
argument_list|(
literal|"org.apache.cxf.jaxrs.client.cache.CacheControlFeature.storeByValue"
argument_list|,
literal|"true"
argument_list|)
operator|.
name|register
argument_list|(
name|feature
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|target
argument_list|(
name|ADDRESS
argument_list|)
decl_stmt|;
specifier|final
name|Invocation
operator|.
name|Builder
name|cached
init|=
name|setAsLocal
argument_list|(
name|base
operator|.
name|request
argument_list|(
literal|"application/xml"
argument_list|)
argument_list|)
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|CACHE_CONTROL
argument_list|,
literal|"public"
argument_list|)
decl_stmt|;
specifier|final
name|Response
name|r
init|=
name|cached
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Book
name|b1
init|=
name|r
operator|.
name|readEntity
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"JCache"
argument_list|,
name|b1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|b1
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|waitABit
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|b1
argument_list|,
name|cached
operator|.
name|get
argument_list|()
operator|.
name|readEntity
argument_list|(
name|Book
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|feature
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|Invocation
operator|.
name|Builder
name|setAsLocal
parameter_list|(
specifier|final
name|Invocation
operator|.
name|Builder
name|client
parameter_list|)
block|{
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|LocalConduit
operator|.
name|DIRECT_DISPATCH
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
return|return
name|client
return|;
block|}
specifier|private
specifier|static
name|void
name|waitABit
parameter_list|()
block|{
try|try
block|{
comment|// just to be sure
name|Thread
operator|.
name|sleep
argument_list|(
literal|150
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|InterruptedException
name|e
parameter_list|)
block|{
name|Thread
operator|.
name|interrupted
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Path
argument_list|(
literal|"/"
argument_list|)
specifier|public
specifier|static
class|class
name|TheServer
block|{
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|Response
name|getString
parameter_list|()
block|{
return|return
name|Response
operator|.
name|ok
argument_list|(
name|Long
operator|.
name|toString
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
operator|.
name|tag
argument_list|(
literal|"123"
argument_list|)
operator|.
name|cacheControl
argument_list|(
name|CacheControl
operator|.
name|valueOf
argument_list|(
literal|"max-age=50000"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
specifier|public
name|Response
name|getJaxbBook
parameter_list|()
block|{
name|Book
name|b
init|=
operator|new
name|Book
argument_list|()
decl_stmt|;
name|b
operator|.
name|setId
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|setName
argument_list|(
literal|"JCache"
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|b
argument_list|)
operator|.
name|tag
argument_list|(
literal|"123"
argument_list|)
operator|.
name|cacheControl
argument_list|(
name|CacheControl
operator|.
name|valueOf
argument_list|(
literal|"max-age=50000"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
annotation|@
name|XmlRootElement
specifier|public
specifier|static
class|class
name|Book
implements|implements
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|4924824780883333782L
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|Long
name|id
decl_stmt|;
specifier|public
name|Book
parameter_list|()
block|{          }
specifier|public
name|Book
parameter_list|(
name|String
name|name
parameter_list|,
name|long
name|id
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
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
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|Long
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|Long
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|id
operator|.
name|hashCode
argument_list|()
operator|+
literal|37
operator|*
name|name
operator|.
name|hashCode
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|Book
condition|)
block|{
name|Book
name|other
init|=
operator|(
name|Book
operator|)
name|o
decl_stmt|;
return|return
name|other
operator|.
name|id
operator|.
name|equals
argument_list|(
name|id
argument_list|)
operator|&&
name|other
operator|.
name|name
operator|.
name|equals
argument_list|(
name|name
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

