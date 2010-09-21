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
name|security
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
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|security
operator|.
name|RolesAllowed
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
name|Book
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
name|systest
operator|.
name|jaxrs
operator|.
name|BookNotFoundFault
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/bookstorestorage/"
argument_list|)
specifier|public
class|class
name|SecureBookStoreNoInterface
block|{
specifier|private
name|Map
argument_list|<
name|Long
argument_list|,
name|Book
argument_list|>
name|books
init|=
operator|new
name|HashMap
argument_list|<
name|Long
argument_list|,
name|Book
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|SecureBookStoreNoInterface
parameter_list|()
block|{
name|Book
name|book
init|=
operator|new
name|Book
argument_list|()
decl_stmt|;
name|book
operator|.
name|setId
argument_list|(
literal|123L
argument_list|)
expr_stmt|;
name|book
operator|.
name|setName
argument_list|(
literal|"CXF in Action"
argument_list|)
expr_stmt|;
name|books
operator|.
name|put
argument_list|(
name|book
operator|.
name|getId
argument_list|()
argument_list|,
name|book
argument_list|)
expr_stmt|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/bookforms"
argument_list|)
annotation|@
name|RolesAllowed
argument_list|(
block|{
literal|"ROLE_USER"
block|,
literal|"ROLE_ADMIN"
block|}
argument_list|)
specifier|public
name|Book
name|getBookFromFormParams
parameter_list|(
annotation|@
name|FormParam
argument_list|(
literal|"name"
argument_list|)
name|String
name|name
parameter_list|,
annotation|@
name|FormParam
argument_list|(
literal|"id"
argument_list|)
name|long
name|id
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|id
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"FormParams are not set"
argument_list|)
throw|;
block|}
return|return
operator|new
name|Book
argument_list|(
name|name
argument_list|,
name|id
argument_list|)
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/bookforms2"
argument_list|)
annotation|@
name|RolesAllowed
argument_list|(
block|{
literal|"ROLE_USER"
block|,
literal|"ROLE_ADMIN"
block|}
argument_list|)
annotation|@
name|Consumes
argument_list|(
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED
argument_list|)
specifier|public
name|Book
name|getBookFromHttpRequestParams
parameter_list|(
annotation|@
name|Context
name|HttpServletRequest
name|request
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|params
init|=
name|request
operator|.
name|getParameterMap
argument_list|()
decl_stmt|;
return|return
name|getBookFromFormParams
argument_list|(
name|params
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
index|[
literal|0
index|]
argument_list|,
name|Long
operator|.
name|valueOf
argument_list|(
name|params
operator|.
name|get
argument_list|(
literal|"id"
argument_list|)
index|[
literal|0
index|]
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/thosebooks/{bookId}/{id}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
annotation|@
name|RolesAllowed
argument_list|(
block|{
literal|"ROLE_USER"
block|,
literal|"ROLE_ADMIN"
block|}
argument_list|)
specifier|public
name|Book
name|getThatBook
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"bookId"
argument_list|)
name|Long
name|id
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|s
parameter_list|)
block|{
if|if
condition|(
name|s
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
literal|"/thosebooks/{bookId}/"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
annotation|@
name|RolesAllowed
argument_list|(
literal|"ROLE_USER"
argument_list|)
specifier|public
name|Book
name|getThatBook
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"bookId"
argument_list|)
name|Long
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
literal|"/thosebooks"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
annotation|@
name|RolesAllowed
argument_list|(
literal|"ROLE_ADMIN"
argument_list|)
specifier|public
name|Book
name|getThatBook
parameter_list|()
throws|throws
name|BookNotFoundFault
block|{
return|return
name|books
operator|.
name|get
argument_list|(
literal|123L
argument_list|)
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/securebook"
argument_list|)
specifier|public
name|SecureBook
name|getSecureBook
parameter_list|()
throws|throws
name|BookNotFoundFault
block|{
return|return
operator|new
name|SecureBook
argument_list|(
literal|"CXF in Action"
argument_list|,
literal|123L
argument_list|)
return|;
block|}
block|}
end_class

end_unit

