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
name|model
operator|.
name|wadl
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
name|HttpHeaders
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
name|annotation
operator|.
name|XmlTransient
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
name|aegis
operator|.
name|type
operator|.
name|java5
operator|.
name|IgnoreProperty
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
name|Description
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
name|xml
operator|.
name|XMLName
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
name|fortest
operator|.
name|jaxb
operator|.
name|packageinfo
operator|.
name|Book2
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
name|model
operator|.
name|wadl
operator|.
name|jaxb
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
name|jaxrs
operator|.
name|model
operator|.
name|wadl
operator|.
name|jaxb
operator|.
name|Chapter
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/bookstore/{id}"
argument_list|)
annotation|@
name|Consumes
argument_list|(
block|{
literal|"application/xml"
block|,
literal|"application/json"
block|}
argument_list|)
annotation|@
name|Produces
argument_list|(
block|{
literal|"application/xml"
block|,
literal|"application/json"
block|}
argument_list|)
annotation|@
name|Description
argument_list|(
name|lang
operator|=
literal|"en-us"
argument_list|,
name|title
operator|=
literal|"book store resource"
argument_list|,
name|value
operator|=
literal|"super resource"
argument_list|)
specifier|public
class|class
name|BookStore
block|{
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|String
name|getName
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|Long
name|id
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|""
argument_list|)
name|QueryBean
name|query
parameter_list|)
block|{
return|return
literal|"store"
return|;
block|}
annotation|@
name|PUT
annotation|@
name|Consumes
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|void
name|setName
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|Long
name|id
parameter_list|,
name|String
name|name
parameter_list|)
block|{     }
annotation|@
name|Path
argument_list|(
literal|"books/{bookid}"
argument_list|)
specifier|public
name|Object
name|addBook
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|int
name|id
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"bookid"
argument_list|)
name|int
name|bookId
parameter_list|,
annotation|@
name|MatrixParam
argument_list|(
literal|"mid"
argument_list|)
name|int
name|matrixId
parameter_list|)
block|{
return|return
operator|new
name|Book
argument_list|(
literal|1
argument_list|)
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"books/{bookid}"
argument_list|)
annotation|@
name|Description
argument_list|(
literal|"Update the books collection"
argument_list|)
comment|//CHECKSTYLE:OFF
specifier|public
name|Book
name|addBook
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|int
name|id
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"bookid"
argument_list|)
name|int
name|bookId
parameter_list|,
annotation|@
name|MatrixParam
argument_list|(
literal|"mid"
argument_list|)
name|int
name|matrixId
parameter_list|,
annotation|@
name|HeaderParam
argument_list|(
literal|"hid"
argument_list|)
name|int
name|headerId
parameter_list|,
annotation|@
name|CookieParam
argument_list|(
literal|"cid"
argument_list|)
name|int
name|cookieId
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"provider.bar"
argument_list|)
name|int
name|queryParam
parameter_list|,
annotation|@
name|Context
name|HttpHeaders
name|headers
parameter_list|,
annotation|@
name|XMLName
argument_list|(
name|value
operator|=
literal|"{http://books}thesuperbook2"
argument_list|,
name|prefix
operator|=
literal|"p1"
argument_list|)
name|Book2
name|b
parameter_list|)
block|{
return|return
operator|new
name|Book
argument_list|(
literal|1
argument_list|)
return|;
block|}
annotation|@
name|PUT
annotation|@
name|Path
argument_list|(
literal|"books/{bookid}"
argument_list|)
annotation|@
name|Description
argument_list|(
literal|"Update the book"
argument_list|)
specifier|public
name|void
name|addBook
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|int
name|id
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"bookid"
argument_list|)
name|int
name|bookId
parameter_list|,
annotation|@
name|MatrixParam
argument_list|(
literal|"mid"
argument_list|)
name|int
name|matrixId
parameter_list|,
name|Book
name|b
parameter_list|)
block|{     }
comment|//CHECKSTYLE:ON
annotation|@
name|Path
argument_list|(
literal|"booksubresource"
argument_list|)
specifier|public
name|Book
name|getBook
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|int
name|id
parameter_list|,
annotation|@
name|MatrixParam
argument_list|(
literal|"mid"
argument_list|)
name|int
name|matrixId
parameter_list|)
block|{
return|return
operator|new
name|Book
argument_list|(
literal|1
argument_list|)
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"chapter"
argument_list|)
specifier|public
name|Chapter
name|getChaper
parameter_list|()
block|{
return|return
operator|new
name|Chapter
argument_list|(
literal|1
argument_list|)
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"form"
argument_list|)
specifier|public
name|FormInterface
name|getForm
parameter_list|()
block|{
return|return
operator|new
name|Book
argument_list|(
literal|1
argument_list|)
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"itself"
argument_list|)
specifier|public
name|BookStore
name|getItself
parameter_list|()
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"book2"
argument_list|)
annotation|@
name|GET
annotation|@
name|XMLName
argument_list|(
name|value
operator|=
literal|"{http://books}thesuperbook2"
argument_list|,
name|prefix
operator|=
literal|"p1"
argument_list|)
specifier|public
name|Book2
name|getBook2
parameter_list|()
block|{
return|return
operator|new
name|Book2
argument_list|()
return|;
block|}
specifier|public
specifier|static
class|class
name|QueryBean
block|{
specifier|private
name|int
name|a
decl_stmt|;
specifier|private
name|int
name|b
decl_stmt|;
specifier|private
name|QueryBean2
name|bean
decl_stmt|;
specifier|public
name|int
name|getA
parameter_list|()
block|{
return|return
name|a
return|;
block|}
annotation|@
name|IgnoreProperty
specifier|public
name|int
name|getB
parameter_list|()
block|{
return|return
name|b
return|;
block|}
specifier|public
name|QueryBean2
name|getC
parameter_list|()
block|{
return|return
name|bean
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|QueryBean2
block|{
specifier|private
name|int
name|a
decl_stmt|;
specifier|private
name|int
name|b
decl_stmt|;
specifier|private
name|QueryBean3
name|bean
decl_stmt|;
specifier|public
name|int
name|getA
parameter_list|()
block|{
return|return
name|a
return|;
block|}
specifier|public
name|int
name|getB
parameter_list|()
block|{
return|return
name|b
return|;
block|}
specifier|public
name|QueryBean3
name|getD
parameter_list|()
block|{
return|return
name|bean
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|QueryBean3
block|{
specifier|private
name|int
name|a
decl_stmt|;
specifier|private
name|int
name|b
decl_stmt|;
specifier|public
name|int
name|getA
parameter_list|()
block|{
return|return
name|a
return|;
block|}
annotation|@
name|XmlTransient
specifier|public
name|int
name|getB
parameter_list|()
block|{
return|return
name|b
return|;
block|}
block|}
block|}
end_class

end_unit

