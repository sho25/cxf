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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Priority
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
name|ws
operator|.
name|rs
operator|.
name|ApplicationPath
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

begin_class
annotation|@
name|ApplicationPath
argument_list|(
literal|"/thebooks"
argument_list|)
annotation|@
name|GlobalNameBinding
specifier|public
class|class
name|BookApplication
extends|extends
name|Application
block|{
specifier|private
name|String
name|defaultName
decl_stmt|;
specifier|private
name|long
name|defaultId
decl_stmt|;
annotation|@
name|Context
specifier|private
name|UriInfo
name|uriInfo
decl_stmt|;
specifier|public
name|BookApplication
parameter_list|(
annotation|@
name|Context
name|ServletContext
name|sc
parameter_list|)
block|{
if|if
condition|(
name|sc
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ServletContext is null"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
literal|"contextParamValue"
operator|.
name|equals
argument_list|(
name|sc
operator|.
name|getInitParameter
argument_list|(
literal|"contextParam"
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"ServletContext is not initialized"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getClasses
parameter_list|()
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|classes
operator|.
name|add
argument_list|(
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
name|BookStorePerRequest
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
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
name|jaxws
operator|.
name|BookStoreJaxrsJaxws
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
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
name|RuntimeExceptionMapper
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|BookRequestFilter
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|BookRequestFilter2
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|BookWriter
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|classes
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Object
argument_list|>
name|getSingletons
parameter_list|()
block|{
name|Set
argument_list|<
name|Object
argument_list|>
name|classes
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
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
name|BookStore
name|store
init|=
operator|new
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
name|BookStore
argument_list|(
name|uriInfo
argument_list|)
decl_stmt|;
name|store
operator|.
name|setDefaultNameAndId
argument_list|(
name|defaultName
argument_list|,
name|defaultId
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|store
argument_list|)
expr_stmt|;
name|BookExceptionMapper
name|mapper
init|=
operator|new
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
name|BookExceptionMapper
argument_list|()
decl_stmt|;
name|mapper
operator|.
name|setToHandle
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|mapper
argument_list|)
expr_stmt|;
return|return
name|classes
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|Collections
operator|.
expr|<
name|String
operator|,
name|Object
operator|>
name|singletonMap
argument_list|(
literal|"book"
argument_list|,
literal|"cxf"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setDefaultName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|defaultName
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|void
name|setDefaultId
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|ids
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|id
range|:
name|ids
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|defaultId
operator|=
name|Long
operator|.
name|valueOf
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GlobalNameBinding
specifier|public
specifier|static
class|class
name|BookWriter
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
name|putSingle
argument_list|(
literal|"BookWriter"
argument_list|,
literal|"TheBook"
argument_list|)
expr_stmt|;
name|context
operator|.
name|proceed
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Priority
argument_list|(
literal|1
argument_list|)
specifier|public
specifier|static
class|class
name|BookRequestFilter
implements|implements
name|ContainerRequestFilter
block|{
specifier|private
name|UriInfo
name|ui
decl_stmt|;
specifier|private
name|Application
name|ap
decl_stmt|;
specifier|public
name|BookRequestFilter
parameter_list|(
annotation|@
name|Context
name|UriInfo
name|ui
parameter_list|,
annotation|@
name|Context
name|Application
name|ap
parameter_list|)
block|{
name|this
operator|.
name|ui
operator|=
name|ui
expr_stmt|;
name|this
operator|.
name|ap
operator|=
name|ap
expr_stmt|;
block|}
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
name|ap
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
name|String
name|uri
init|=
name|ui
operator|.
name|getRequestUri
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|endsWith
argument_list|(
literal|"/application11/thebooks/bookstore2/bookheaders"
argument_list|)
operator|||
name|uri
operator|.
name|contains
argument_list|(
literal|"/application6"
argument_list|)
condition|)
block|{
name|context
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
literal|"BOOK"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"1"
argument_list|,
literal|"2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Priority
argument_list|(
literal|2
argument_list|)
specifier|public
specifier|static
class|class
name|BookRequestFilter2
implements|implements
name|ContainerRequestFilter
block|{
specifier|private
name|UriInfo
name|ui
decl_stmt|;
annotation|@
name|Context
specifier|private
name|Application
name|ap
decl_stmt|;
annotation|@
name|Context
specifier|public
name|void
name|setUriInfo
parameter_list|(
name|UriInfo
name|context
parameter_list|)
block|{
name|this
operator|.
name|ui
operator|=
name|context
expr_stmt|;
block|}
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
name|ap
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
name|String
name|uri
init|=
name|ui
operator|.
name|getRequestUri
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|endsWith
argument_list|(
literal|"/application11/thebooks/bookstore2/bookheaders"
argument_list|)
operator|||
name|uri
operator|.
name|contains
argument_list|(
literal|"/application6"
argument_list|)
condition|)
block|{
name|context
operator|.
name|getHeaders
argument_list|()
operator|.
name|add
argument_list|(
literal|"BOOK"
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

