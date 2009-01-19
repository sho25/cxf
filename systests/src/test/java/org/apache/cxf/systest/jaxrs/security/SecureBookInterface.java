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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|security
operator|.
name|annotation
operator|.
name|Secured
import|;
end_import

begin_interface
specifier|public
interface|interface
name|SecureBookInterface
block|{
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
name|Secured
argument_list|(
block|{
literal|"ROLE_USER"
block|,
literal|"ROLE_ADMIN"
block|}
argument_list|)
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
throws|throws
name|BookNotFoundFault
function_decl|;
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
name|Secured
argument_list|(
literal|"ROLE_USER"
argument_list|)
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
throws|throws
name|BookNotFoundFault
function_decl|;
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
name|Secured
argument_list|(
literal|"ROLE_ADMIN"
argument_list|)
name|Book
name|getThatBook
parameter_list|()
throws|throws
name|BookNotFoundFault
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"/subresource"
argument_list|)
name|SecureBookInterface
name|getBookSubResource
parameter_list|()
throws|throws
name|BookNotFoundFault
function_decl|;
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
annotation|@
name|Secured
argument_list|(
literal|"ROLE_ADMIN"
argument_list|)
name|Book
name|getDefaultBook
parameter_list|()
throws|throws
name|BookNotFoundFault
function_decl|;
block|}
end_interface

end_unit

