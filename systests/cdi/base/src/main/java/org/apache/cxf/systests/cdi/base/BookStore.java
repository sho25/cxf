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
name|systests
operator|.
name|cdi
operator|.
name|base
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|Valid
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|constraints
operator|.
name|NotNull
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|constraints
operator|.
name|Size
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
name|POST
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
name|core
operator|.
name|MediaType
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

begin_class
annotation|@
name|Path
argument_list|(
literal|"/bookstore/"
argument_list|)
specifier|public
class|class
name|BookStore
block|{
specifier|private
name|BookStoreService
name|service
decl_stmt|;
specifier|private
name|BookStoreVersion
name|bookStoreVersion
decl_stmt|;
specifier|private
name|UriInfo
name|uriInfo
decl_stmt|;
specifier|private
name|Injections
name|injections
decl_stmt|;
specifier|public
name|BookStore
parameter_list|()
block|{     }
annotation|@
name|Inject
specifier|public
name|BookStore
parameter_list|(
name|BookStoreService
name|service
parameter_list|,
name|BookStoreVersion
name|bookStoreVersion
parameter_list|,
name|UriInfo
name|uriInfo
parameter_list|,
name|Injections
name|injections
parameter_list|)
block|{
name|this
operator|.
name|service
operator|=
name|service
expr_stmt|;
name|this
operator|.
name|bookStoreVersion
operator|=
name|bookStoreVersion
expr_stmt|;
name|this
operator|.
name|uriInfo
operator|=
name|uriInfo
expr_stmt|;
name|this
operator|.
name|injections
operator|=
name|injections
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"injections"
argument_list|)
specifier|public
name|String
name|injections
parameter_list|()
block|{
return|return
name|injections
operator|.
name|state
argument_list|()
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/version"
argument_list|)
specifier|public
name|BookStoreVersion
name|getVersion
parameter_list|()
block|{
return|return
name|bookStoreVersion
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/books/{bookId}"
argument_list|)
annotation|@
name|NotNull
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
specifier|public
name|Book
name|getBook
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"bookId"
argument_list|)
name|String
name|id
parameter_list|)
block|{
return|return
name|service
operator|.
name|get
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/books"
argument_list|)
annotation|@
name|NotNull
annotation|@
name|Valid
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
specifier|public
name|Collection
argument_list|<
name|Book
argument_list|>
name|getBooks
parameter_list|()
block|{
return|return
name|service
operator|.
name|all
argument_list|()
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
specifier|public
name|Response
name|addBook
parameter_list|(
annotation|@
name|NotNull
annotation|@
name|Size
argument_list|(
name|min
operator|=
literal|1
argument_list|,
name|max
operator|=
literal|50
argument_list|)
annotation|@
name|FormParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|id
parameter_list|,
annotation|@
name|NotNull
annotation|@
name|FormParam
argument_list|(
literal|"name"
argument_list|)
name|String
name|name
parameter_list|)
block|{
specifier|final
name|Book
name|book
init|=
name|service
operator|.
name|store
argument_list|(
name|id
argument_list|,
name|name
argument_list|)
decl_stmt|;
return|return
name|Response
operator|.
name|created
argument_list|(
name|uriInfo
operator|.
name|getRequestUriBuilder
argument_list|()
operator|.
name|path
argument_list|(
name|id
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|entity
argument_list|(
name|book
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

