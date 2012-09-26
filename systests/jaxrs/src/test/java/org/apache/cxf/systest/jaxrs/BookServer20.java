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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|BindingPriority
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
name|NameBinding
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
name|WebApplicationException
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
name|container
operator|.
name|ContainerRequestContext
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
name|container
operator|.
name|ContainerRequestFilter
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
name|container
operator|.
name|ContainerResponseContext
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
name|container
operator|.
name|ContainerResponseFilter
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
name|container
operator|.
name|DynamicFeature
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
name|container
operator|.
name|PreMatching
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
name|container
operator|.
name|ResourceInfo
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
name|Configurable
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
name|ReaderInterceptor
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
name|ReaderInterceptorContext
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
name|WriterInterceptor
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
name|WriterInterceptorContext
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
name|BusFactory
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusTestServerBase
import|;
end_import

begin_class
specifier|public
class|class
name|BookServer20
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|BookServer20
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|JAXRSServerFactoryBean
name|sf
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setResourceClasses
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|PreMatchContainerRequestFilter2
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|PreMatchContainerRequestFilter
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|PostMatchContainerResponseFilter
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|PostMatchContainerResponseFilter3
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|PostMatchContainerResponseFilter2
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|CustomReaderInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|CustomWriterInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|CustomDynamicFeature
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setProviders
argument_list|(
name|providers
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setResourceProvider
argument_list|(
name|BookStore
operator|.
name|class
argument_list|,
operator|new
name|SingletonResourceProvider
argument_list|(
operator|new
name|BookStore
argument_list|()
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|server
operator|=
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|tearDown
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
name|BookServer20
name|s
init|=
operator|new
name|BookServer20
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
annotation|@
name|PreMatching
annotation|@
name|BindingPriority
argument_list|(
literal|1
argument_list|)
specifier|private
specifier|static
class|class
name|PreMatchContainerRequestFilter
implements|implements
name|ContainerRequestFilter
block|{
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|context
parameter_list|)
throws|throws
name|IOException
block|{
name|context
operator|.
name|setProperty
argument_list|(
literal|"FirstPrematchingFilter"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|UriInfo
name|ui
init|=
name|context
operator|.
name|getUriInfo
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|ui
operator|.
name|getPath
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"wrongpath"
operator|.
name|equals
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|context
operator|.
name|setRequestUri
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"/bookstore/bookheaders/simple"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|PreMatching
annotation|@
name|BindingPriority
argument_list|(
literal|3
argument_list|)
specifier|private
specifier|static
class|class
name|PreMatchContainerRequestFilter2
implements|implements
name|ContainerRequestFilter
block|{
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|context
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
literal|"true"
operator|.
name|equals
argument_list|(
name|context
operator|.
name|getProperty
argument_list|(
literal|"FirstPrematchingFilter"
argument_list|)
argument_list|)
operator|||
operator|!
literal|"true"
operator|.
name|equals
argument_list|(
name|context
operator|.
name|getProperty
argument_list|(
literal|"DynamicPrematchingFilter"
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
name|context
operator|.
name|getHeaders
argument_list|()
operator|.
name|add
argument_list|(
literal|"BOOK"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|PreMatching
annotation|@
name|BindingPriority
argument_list|(
literal|2
argument_list|)
specifier|private
specifier|static
class|class
name|PreMatchDynamicContainerRequestFilter
implements|implements
name|ContainerRequestFilter
block|{
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|context
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
literal|"true"
operator|.
name|equals
argument_list|(
name|context
operator|.
name|getProperty
argument_list|(
literal|"FirstPrematchingFilter"
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
name|context
operator|.
name|setProperty
argument_list|(
literal|"DynamicPrematchingFilter"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|BindingPriority
argument_list|(
literal|3
argument_list|)
specifier|public
specifier|static
class|class
name|PostMatchContainerResponseFilter
implements|implements
name|ContainerResponseFilter
block|{
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|requestContext
parameter_list|,
name|ContainerResponseContext
name|responseContext
parameter_list|)
throws|throws
name|IOException
block|{
name|responseContext
operator|.
name|getHeaders
argument_list|()
operator|.
name|add
argument_list|(
literal|"Response"
argument_list|,
literal|"OK"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|BindingPriority
argument_list|(
literal|1
argument_list|)
specifier|public
specifier|static
class|class
name|PostMatchContainerResponseFilter2
implements|implements
name|ContainerResponseFilter
block|{
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|requestContext
parameter_list|,
name|ContainerResponseContext
name|responseContext
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|responseContext
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"Response"
argument_list|)
operator|||
operator|!
name|responseContext
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"DynamicResponse"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
name|responseContext
operator|.
name|getHeaders
argument_list|()
operator|.
name|add
argument_list|(
literal|"Response2"
argument_list|,
literal|"OK2"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|BindingPriority
argument_list|(
literal|4
argument_list|)
annotation|@
name|CustomHeaderAdded
specifier|public
specifier|static
class|class
name|PostMatchContainerResponseFilter3
implements|implements
name|ContainerResponseFilter
block|{
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|requestContext
parameter_list|,
name|ContainerResponseContext
name|responseContext
parameter_list|)
throws|throws
name|IOException
block|{
name|responseContext
operator|.
name|getHeaders
argument_list|()
operator|.
name|add
argument_list|(
literal|"Custom"
argument_list|,
literal|"custom"
argument_list|)
expr_stmt|;
name|Book
name|book
init|=
operator|(
name|Book
operator|)
name|responseContext
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|responseContext
operator|.
name|setEntity
argument_list|(
operator|new
name|Book
argument_list|(
name|book
operator|.
name|getName
argument_list|()
argument_list|,
literal|1
operator|+
name|book
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|BindingPriority
argument_list|(
literal|2
argument_list|)
specifier|public
specifier|static
class|class
name|PostMatchDynamicContainerResponseFilter
implements|implements
name|ContainerResponseFilter
block|{
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|requestContext
parameter_list|,
name|ContainerResponseContext
name|responseContext
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|responseContext
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"Response"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
name|responseContext
operator|.
name|getHeaders
argument_list|()
operator|.
name|add
argument_list|(
literal|"DynamicResponse"
argument_list|,
literal|"Dynamic"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Target
argument_list|(
block|{
name|ElementType
operator|.
name|TYPE
block|,
name|ElementType
operator|.
name|METHOD
block|}
argument_list|)
annotation|@
name|Retention
argument_list|(
name|value
operator|=
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|NameBinding
specifier|public
annotation_defn|@interface
name|CustomHeaderAdded
block|{               }
specifier|public
specifier|static
class|class
name|CustomReaderInterceptor
implements|implements
name|ReaderInterceptor
block|{
annotation|@
name|Context
specifier|private
name|ResourceInfo
name|ri
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|aroundReadFrom
parameter_list|(
name|ReaderInterceptorContext
name|context
parameter_list|)
throws|throws
name|IOException
throws|,
name|WebApplicationException
block|{
if|if
condition|(
name|ri
operator|.
name|getResourceClass
argument_list|()
operator|==
name|BookStore
operator|.
name|class
condition|)
block|{
name|context
operator|.
name|getHeaders
argument_list|()
operator|.
name|add
argument_list|(
literal|"ServerReaderInterceptor"
argument_list|,
literal|"serverRead"
argument_list|)
expr_stmt|;
block|}
return|return
name|context
operator|.
name|proceed
argument_list|()
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|CustomWriterInterceptor
implements|implements
name|WriterInterceptor
block|{
annotation|@
name|Override
specifier|public
name|void
name|aroundWriteTo
parameter_list|(
name|WriterInterceptorContext
name|context
parameter_list|)
throws|throws
name|IOException
throws|,
name|WebApplicationException
block|{
name|context
operator|.
name|getHeaders
argument_list|()
operator|.
name|add
argument_list|(
literal|"ServerWriterInterceptor"
argument_list|,
literal|"serverWrite"
argument_list|)
expr_stmt|;
name|context
operator|.
name|proceed
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|CustomDynamicFeature
implements|implements
name|DynamicFeature
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|(
name|ResourceInfo
name|resourceInfo
parameter_list|,
name|Configurable
name|configurable
parameter_list|)
block|{
name|configurable
operator|.
name|register
argument_list|(
operator|new
name|PreMatchDynamicContainerRequestFilter
argument_list|()
argument_list|)
expr_stmt|;
name|configurable
operator|.
name|register
argument_list|(
operator|new
name|PostMatchDynamicContainerResponseFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

