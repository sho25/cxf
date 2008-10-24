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
name|resources
package|;
end_package

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
name|PUT
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
name|Response
import|;
end_import

begin_class
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"/bookstore/{id}"
argument_list|)
specifier|public
class|class
name|BookStoreNoSubResource
block|{
specifier|public
name|BookStoreNoSubResource
parameter_list|()
block|{     }
annotation|@
name|GET
specifier|public
name|String
name|getBookStoreInfo
parameter_list|()
block|{
return|return
literal|"This is a great store"
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
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
specifier|public
name|Book
name|getBooks
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/books/{bookId}/"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
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
literal|null
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/books/{bookId}/"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
specifier|public
name|Book
name|getBookJSON
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
literal|null
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
literal|"application/xml"
argument_list|)
specifier|public
name|Response
name|addBook
parameter_list|(
name|Book
name|book
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|PUT
annotation|@
name|Path
argument_list|(
literal|"/books/"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/*"
argument_list|)
specifier|public
name|Response
name|updateBook
parameter_list|(
name|Book
name|book
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/books/{bookId}/"
argument_list|)
annotation|@
name|DELETE
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
specifier|public
name|Response
name|deleteBook
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
literal|null
return|;
block|}
block|}
end_class

end_unit

