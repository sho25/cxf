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
operator|.
name|validation
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
name|Map
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
name|DELETE
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
name|BookStoreWithValidation
extends|extends
name|AbstractBookStoreWithValidation
implements|implements
name|BookStoreValidatable
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|BookWithValidation
argument_list|>
name|books
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|BookWithValidation
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|BookStoreWithValidation
parameter_list|()
block|{     }
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/books/{bookId}"
argument_list|)
annotation|@
name|Override
specifier|public
name|BookWithValidation
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
name|books
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
literal|"/booksResponse/{bookId}"
argument_list|)
annotation|@
name|Valid
specifier|public
name|Response
name|getBookResponse
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
name|Response
operator|.
name|ok
argument_list|(
name|books
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|build
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
specifier|public
name|Response
name|addBook
parameter_list|(
annotation|@
name|Context
specifier|final
name|UriInfo
name|uriInfo
parameter_list|,
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
name|FormParam
argument_list|(
literal|"name"
argument_list|)
name|String
name|name
parameter_list|)
block|{
name|books
operator|.
name|put
argument_list|(
name|id
argument_list|,
operator|new
name|BookWithValidation
argument_list|(
name|name
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
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
name|build
argument_list|()
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
name|Override
specifier|public
name|Collection
argument_list|<
name|BookWithValidation
argument_list|>
name|list
parameter_list|(
annotation|@
name|DefaultValue
argument_list|(
literal|"1"
argument_list|)
annotation|@
name|QueryParam
argument_list|(
literal|"page"
argument_list|)
name|int
name|page
parameter_list|)
block|{
return|return
name|books
operator|.
name|values
argument_list|()
return|;
block|}
annotation|@
name|DELETE
annotation|@
name|Path
argument_list|(
literal|"/books"
argument_list|)
specifier|public
name|Response
name|clear
parameter_list|()
block|{
name|books
operator|.
name|clear
argument_list|()
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

