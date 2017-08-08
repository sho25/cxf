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
name|http_undertow
operator|.
name|websocket
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ClientErrorException
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

begin_class
annotation|@
name|Path
argument_list|(
literal|"/bookstore2"
argument_list|)
specifier|public
class|class
name|BookStorePerRequest
block|{
specifier|private
name|HttpHeaders
name|httpHeaders
decl_stmt|;
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
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|bookIds
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|setterBookIds
decl_stmt|;
specifier|public
name|BookStorePerRequest
parameter_list|()
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
specifier|public
name|BookStorePerRequest
parameter_list|(
annotation|@
name|Context
name|HttpHeaders
name|headers
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
specifier|public
name|BookStorePerRequest
parameter_list|(
annotation|@
name|Context
name|HttpHeaders
name|headers
parameter_list|,
name|Long
name|bar
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
specifier|public
name|BookStorePerRequest
parameter_list|(
annotation|@
name|Context
name|HttpHeaders
name|headers
parameter_list|,
annotation|@
name|HeaderParam
argument_list|(
literal|"BOOK"
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|bookIds
parameter_list|)
block|{
if|if
condition|(
operator|!
name|bookIds
operator|.
name|contains
argument_list|(
literal|"3"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ClientErrorException
argument_list|(
name|Response
operator|.
name|status
argument_list|(
literal|400
argument_list|)
operator|.
name|type
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|entity
argument_list|(
literal|"Constructor: Header value 3 is required"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
throw|;
block|}
name|httpHeaders
operator|=
name|headers
expr_stmt|;
name|this
operator|.
name|bookIds
operator|=
name|bookIds
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
annotation|@
name|HeaderParam
argument_list|(
literal|"Book"
argument_list|)
specifier|public
name|void
name|setBook
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|ids
parameter_list|)
block|{
if|if
condition|(
operator|!
name|ids
operator|.
name|equals
argument_list|(
name|bookIds
argument_list|)
operator|||
name|ids
operator|.
name|size
argument_list|()
operator|!=
literal|3
condition|)
block|{
throw|throw
operator|new
name|ClientErrorException
argument_list|(
name|Response
operator|.
name|status
argument_list|(
literal|400
argument_list|)
operator|.
name|type
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|entity
argument_list|(
literal|"Param setter: 3 header values are required"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
throw|;
block|}
name|setterBookIds
operator|=
name|ids
expr_stmt|;
block|}
annotation|@
name|Context
specifier|public
name|void
name|setHttpHeaders
parameter_list|(
name|HttpHeaders
name|headers
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ids
init|=
name|httpHeaders
operator|.
name|getRequestHeader
argument_list|(
literal|"BOOK"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ids
operator|.
name|contains
argument_list|(
literal|"4"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ClientErrorException
argument_list|(
name|Response
operator|.
name|status
argument_list|(
literal|400
argument_list|)
operator|.
name|type
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|entity
argument_list|(
literal|"Context setter: unexpected header value"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/book%20headers/"
argument_list|)
specifier|public
name|Book
name|getBookByHeader2
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|getBookByHeader
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/bookheaders/"
argument_list|)
specifier|public
name|Book
name|getBookByHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ids
init|=
name|httpHeaders
operator|.
name|getRequestHeader
argument_list|(
literal|"BOOK"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ids
operator|.
name|equals
argument_list|(
name|bookIds
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
return|return
name|doGetBook
argument_list|(
name|ids
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|+
name|ids
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|+
name|ids
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/bookheaders/injected"
argument_list|)
specifier|public
name|Book
name|getBookByHeaderInjected
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|doGetBook
argument_list|(
name|setterBookIds
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|+
name|setterBookIds
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|+
name|setterBookIds
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|Book
name|doGetBook
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|BookNotFoundFault
block|{
name|Book
name|book
init|=
name|books
operator|.
name|get
argument_list|(
name|Long
operator|.
name|parseLong
argument_list|(
name|id
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|book
operator|!=
literal|null
condition|)
block|{
return|return
name|book
return|;
block|}
name|BookNotFoundDetails
name|details
init|=
operator|new
name|BookNotFoundDetails
argument_list|()
decl_stmt|;
name|details
operator|.
name|setId
argument_list|(
name|Long
operator|.
name|parseLong
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|BookNotFoundFault
argument_list|(
name|details
argument_list|)
throw|;
block|}
specifier|final
name|void
name|init
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
literal|123
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
block|}
end_class

end_unit

