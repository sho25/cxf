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
name|description
operator|.
name|group1
package|;
end_package

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

begin_comment
comment|//FIXME swagger-jaxrs 1.5.3 can't handle a self-recursive sub resource like Book. so hide Book for now
end_comment

begin_comment
comment|//import org.apache.cxf.systest.jaxrs.Book;
end_comment

begin_class
annotation|@
name|Path
argument_list|(
literal|"/bookstore"
argument_list|)
specifier|public
class|class
name|BookStore
block|{
comment|//    @Produces({ MediaType.APPLICATION_JSON })
comment|//    @GET
comment|//    public Response getBooks(
comment|//        @QueryParam("page") @DefaultValue("1") int page) {
comment|//        return Response.ok(
comment|//            Arrays.asList(
comment|//                new Book("Book 1", 1),
comment|//                new Book("Book 2", 2)
comment|//            )
comment|//        ).build();
comment|//    }
comment|//
comment|//    @Produces({ MediaType.APPLICATION_JSON })
comment|//    @Path("/{id}")
comment|//    @GET
comment|//    public Book getBook(@PathParam("id") Long id) {
comment|//        return new Book("Book", id);
comment|//    }
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|}
argument_list|)
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/names"
argument_list|)
specifier|public
name|Response
name|getBookNames
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"page"
argument_list|)
annotation|@
name|DefaultValue
argument_list|(
literal|"1"
argument_list|)
name|int
name|page
parameter_list|)
block|{
return|return
name|Response
operator|.
name|ok
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"Book 1"
argument_list|,
literal|"Book 2"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/name/{id}"
argument_list|)
annotation|@
name|GET
specifier|public
name|String
name|getBookName
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|id
parameter_list|)
block|{
return|return
literal|"Book of "
operator|+
name|id
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/{id}"
argument_list|)
annotation|@
name|DELETE
specifier|public
name|Response
name|delete
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|id
parameter_list|)
block|{
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

