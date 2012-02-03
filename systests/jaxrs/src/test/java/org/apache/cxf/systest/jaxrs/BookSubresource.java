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
name|UriInfo
import|;
end_import

begin_interface
specifier|public
interface|interface
name|BookSubresource
block|{
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/subresource"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
name|Book
name|getTheBook
parameter_list|()
throws|throws
name|BookNotFoundFault
function_decl|;
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/subresource"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
name|Book
name|getTheBookWithContext
parameter_list|(
annotation|@
name|Context
name|UriInfo
name|ui
parameter_list|)
throws|throws
name|BookNotFoundFault
function_decl|;
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/subresource/noproduces"
argument_list|)
name|Book
name|getTheBookNoProduces
parameter_list|()
throws|throws
name|BookNotFoundFault
function_decl|;
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/subresource2/{n1:.*}"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"text/plain"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
name|Book
name|getTheBook2
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"n1"
argument_list|)
name|String
name|name1
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"n2"
argument_list|)
name|String
name|name2
parameter_list|,
annotation|@
name|MatrixParam
argument_list|(
literal|"n3"
argument_list|)
name|String
name|name3
parameter_list|,
annotation|@
name|MatrixParam
argument_list|(
literal|"n33"
argument_list|)
name|String
name|name33
parameter_list|,
annotation|@
name|HeaderParam
argument_list|(
literal|"N4"
argument_list|)
name|String
name|name4
parameter_list|,
annotation|@
name|CookieParam
argument_list|(
literal|"n5"
argument_list|)
name|String
name|name5
parameter_list|,
name|String
name|name6
parameter_list|)
throws|throws
name|BookNotFoundFault
function_decl|;
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/subresource3"
argument_list|)
name|Book
name|getTheBook3
parameter_list|(
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
name|List
argument_list|<
name|String
argument_list|>
name|nameParts
parameter_list|)
throws|throws
name|BookNotFoundFault
function_decl|;
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/subresource4/{id}/{name}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
annotation|@
name|Consumes
argument_list|(
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED
argument_list|)
name|Book
name|getTheBook4
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|""
argument_list|)
name|Book
name|bookPath
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|""
argument_list|)
name|Book
name|bookQuery
parameter_list|,
annotation|@
name|MatrixParam
argument_list|(
literal|""
argument_list|)
name|Book
name|matrixBook
parameter_list|,
annotation|@
name|FormParam
argument_list|(
literal|""
argument_list|)
name|Book
name|formBook
parameter_list|)
throws|throws
name|BookNotFoundFault
function_decl|;
annotation|@
name|POST
annotation|@
name|Consumes
argument_list|(
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
name|OrderBean
name|addOrder
parameter_list|(
annotation|@
name|FormParam
argument_list|(
literal|""
argument_list|)
name|OrderBean
name|order
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

