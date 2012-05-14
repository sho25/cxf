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
name|jaxws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebMethod
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
name|Response
operator|.
name|ResponseBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Marshaller
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
name|annotations
operator|.
name|SchemaValidation
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
name|client
operator|.
name|WebClient
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
name|ext
operator|.
name|MessageContext
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
name|BookNotFoundDetails
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
name|apache
operator|.
name|cxf
operator|.
name|systest
operator|.
name|jaxrs
operator|.
name|BookSubresource
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
name|BookSubresourceImpl
import|;
end_import

begin_class
annotation|@
name|SchemaValidation
specifier|public
class|class
name|BookStoreSoapRestImpl
implements|implements
name|BookStoreJaxrsJaxws
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
specifier|private
name|boolean
name|ignoreJaxrsClient
decl_stmt|;
annotation|@
name|Resource
specifier|private
name|MessageContext
name|jaxrsContext
decl_stmt|;
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"restClient"
argument_list|)
specifier|private
name|BookStoreJaxrsJaxws
name|webClient
decl_stmt|;
specifier|private
name|boolean
name|invocationInProcess
decl_stmt|;
specifier|public
name|BookStoreSoapRestImpl
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setIgnoreJaxrsClient
parameter_list|(
name|boolean
name|ignore
parameter_list|)
block|{
name|this
operator|.
name|ignoreJaxrsClient
operator|=
name|ignore
expr_stmt|;
block|}
annotation|@
name|PostConstruct
specifier|public
name|void
name|verifyWebClient
parameter_list|()
block|{
if|if
condition|(
operator|!
name|ignoreJaxrsClient
condition|)
block|{
if|if
condition|(
name|webClient
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
name|WebClient
operator|.
name|client
argument_list|(
name|webClient
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/xml"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Book
name|getBook
parameter_list|(
name|Long
name|id
parameter_list|)
throws|throws
name|BookNotFoundFault
block|{
if|if
condition|(
name|books
operator|.
name|get
argument_list|(
name|id
argument_list|)
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|id
operator|==
literal|0
condition|)
block|{
try|try
block|{
name|OutputStream
name|os
init|=
name|jaxrsContext
operator|.
name|getHttpServletResponse
argument_list|()
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|JAXBContext
name|c
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
operator|new
name|Class
index|[]
block|{
name|Book
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|Marshaller
name|m
init|=
name|c
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
name|m
operator|.
name|marshal
argument_list|(
name|books
operator|.
name|get
argument_list|(
literal|123L
argument_list|)
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
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
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
block|}
name|int
name|returnCode
init|=
literal|404
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|321
condition|)
block|{
name|returnCode
operator|=
literal|525
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|id
operator|==
literal|322
condition|)
block|{
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
name|id
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
name|String
name|msg
init|=
literal|"No Book with id "
operator|+
name|id
operator|+
literal|" is available"
decl_stmt|;
name|ResponseBuilder
name|builder
init|=
name|Response
operator|.
name|status
argument_list|(
name|returnCode
argument_list|)
operator|.
name|header
argument_list|(
literal|"BOOK-HEADER"
argument_list|,
name|msg
argument_list|)
decl_stmt|;
if|if
condition|(
name|returnCode
operator|==
literal|404
condition|)
block|{
name|builder
operator|.
name|type
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|entity
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|ignoreJaxrsClient
condition|)
block|{
if|if
condition|(
operator|!
name|invocationInProcess
condition|)
block|{
name|invocationInProcess
operator|=
literal|true
expr_stmt|;
return|return
name|webClient
operator|.
name|getBook
argument_list|(
name|id
argument_list|)
return|;
block|}
name|invocationInProcess
operator|=
literal|false
expr_stmt|;
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
specifier|public
name|Book
name|addBook
parameter_list|(
name|Book
name|book
parameter_list|)
block|{
name|book
operator|.
name|setId
argument_list|(
literal|124
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
return|return
name|books
operator|.
name|get
argument_list|(
name|book
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
specifier|private
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
operator|new
name|Long
argument_list|(
literal|123
argument_list|)
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
name|WebMethod
argument_list|(
name|exclude
operator|=
literal|true
argument_list|)
specifier|public
name|BookSubresource
name|getBookSubresource
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
operator|new
name|BookSubresourceImpl
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|WebMethod
argument_list|(
name|exclude
operator|=
literal|true
argument_list|)
specifier|public
name|BookStoreJaxrsJaxws
name|getBookStore
parameter_list|(
name|String
name|id
parameter_list|)
block|{
if|if
condition|(
operator|!
literal|"number1"
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|404
argument_list|)
throw|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|Book
name|addFastinfoBook
parameter_list|(
name|Book
name|book
parameter_list|)
block|{
return|return
name|book
return|;
block|}
specifier|public
name|Book
name|getFastinfoBook
parameter_list|()
block|{
return|return
operator|new
name|Book
argument_list|(
literal|"CXF2"
argument_list|,
literal|2L
argument_list|)
return|;
block|}
block|}
end_class

end_unit

