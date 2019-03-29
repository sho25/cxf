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
name|java
operator|.
name|util
operator|.
name|Collections
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|BeanParam
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
name|container
operator|.
name|AsyncResponse
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
name|container
operator|.
name|Suspended
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
name|Status
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
name|ResponseStatus
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
name|multipart
operator|.
name|MultipartBody
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
name|ElementClass
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
name|packageinfo
operator|.
name|Book2
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
specifier|public
class|class
name|BookStore
extends|extends
name|AbstractStore
argument_list|<
name|Book
argument_list|>
implements|implements
name|BookDescription
block|{
annotation|@
name|Descriptions
argument_list|(
block|{
annotation|@
name|Description
argument_list|(
name|value
operator|=
literal|"Attachments, max< 10"
argument_list|,
name|target
operator|=
name|DocTarget
operator|.
name|PARAM
argument_list|)
block|}
argument_list|)
annotation|@
name|POST
annotation|@
name|Consumes
argument_list|(
literal|"multipart/form-data"
argument_list|)
specifier|public
name|void
name|formdata
parameter_list|(
name|MultipartBody
name|body
parameter_list|)
block|{      }
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
annotation|@
name|XMLName
argument_list|(
literal|"{http://superbooks}books"
argument_list|)
annotation|@
name|Descriptions
argument_list|(
block|{
annotation|@
name|Description
argument_list|(
name|value
operator|=
literal|"Get Books"
argument_list|,
name|target
operator|=
name|DocTarget
operator|.
name|METHOD
argument_list|)
block|}
argument_list|)
specifier|public
name|List
argument_list|<
name|Book
argument_list|>
name|getBooks
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
name|BeanParam
name|TheBeanParam
name|beanParam
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"thebooks2"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
annotation|@
name|Descriptions
argument_list|(
block|{
annotation|@
name|Description
argument_list|(
name|value
operator|=
literal|"Get Books2"
argument_list|,
name|target
operator|=
name|DocTarget
operator|.
name|METHOD
argument_list|)
block|}
argument_list|)
specifier|public
name|List
argument_list|<
name|Book2
argument_list|>
name|getBooks2
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|Long
name|id
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
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
name|BeanParam
name|TheBeanParam
name|beanParam
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
literal|"books/\"{bookid}\""
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
name|Descriptions
argument_list|(
block|{
annotation|@
name|Description
argument_list|(
name|value
operator|=
literal|"Update the books collection"
argument_list|,
name|target
operator|=
name|DocTarget
operator|.
name|METHOD
argument_list|)
block|,
annotation|@
name|Description
argument_list|(
name|value
operator|=
literal|"Requested Book"
argument_list|,
name|target
operator|=
name|DocTarget
operator|.
name|RETURN
argument_list|)
block|,
annotation|@
name|Description
argument_list|(
name|value
operator|=
literal|"Request"
argument_list|,
name|target
operator|=
name|DocTarget
operator|.
name|REQUEST
argument_list|)
block|,
annotation|@
name|Description
argument_list|(
name|value
operator|=
literal|"Response"
argument_list|,
name|target
operator|=
name|DocTarget
operator|.
name|RESPONSE
argument_list|)
block|,
annotation|@
name|Description
argument_list|(
name|value
operator|=
literal|"Resource books/{bookid}"
argument_list|,
name|target
operator|=
name|DocTarget
operator|.
name|RESOURCE
argument_list|)
block|}
argument_list|)
annotation|@
name|ResponseStatus
argument_list|(
block|{
name|Status
operator|.
name|CREATED
block|,
name|Status
operator|.
name|OK
block|}
argument_list|)
comment|//CHECKSTYLE:OFF
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"books/{bookid}"
argument_list|)
specifier|public
name|Book
name|addBook
parameter_list|(
annotation|@
name|Description
argument_list|(
literal|"book id"
argument_list|)
comment|//NOPMD
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
annotation|@
name|DefaultValue
argument_list|(
literal|"mid> 5"
argument_list|)
name|String
name|matrixId
parameter_list|,
annotation|@
name|Description
argument_list|(
literal|"header param"
argument_list|)
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
name|QueryParam
argument_list|(
literal|"bookstate"
argument_list|)
name|BookEnum
name|state
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"a"
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|queryList
parameter_list|,
annotation|@
name|Context
name|HttpHeaders
name|headers
parameter_list|,
annotation|@
name|Description
argument_list|(
literal|"InputBook"
argument_list|)
annotation|@
name|XMLName
argument_list|(
name|value
operator|=
literal|"{http://books}thesuperbook2"
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
name|getChapter
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
name|GET
annotation|@
name|Path
argument_list|(
literal|"chapter2"
argument_list|)
annotation|@
name|ElementClass
argument_list|(
name|response
operator|=
name|Chapter
operator|.
name|class
argument_list|)
specifier|public
name|void
name|getChapterAsync
parameter_list|(
annotation|@
name|Suspended
name|AsyncResponse
name|async
parameter_list|)
block|{
name|async
operator|.
name|resume
argument_list|(
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|entity
argument_list|(
operator|new
name|Chapter
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
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
name|TheBeanParam
block|{
specifier|private
name|int
name|a
decl_stmt|;
annotation|@
name|QueryParam
argument_list|(
literal|"b"
argument_list|)
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
name|PathParam
argument_list|(
literal|"a"
argument_list|)
specifier|public
name|void
name|setA
parameter_list|(
name|int
name|aa
parameter_list|)
block|{
name|this
operator|.
name|a
operator|=
name|aa
expr_stmt|;
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
name|void
name|setB
parameter_list|(
name|int
name|bb
parameter_list|)
block|{
name|this
operator|.
name|b
operator|=
name|bb
expr_stmt|;
block|}
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
name|getAProp
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
specifier|public
name|TestEnum
name|getE
parameter_list|()
block|{
return|return
name|TestEnum
operator|.
name|A
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
specifier|public
name|QueryBean3
name|getD2
parameter_list|()
block|{
return|return
name|bean
return|;
block|}
specifier|public
name|QueryBean2
name|getIt
parameter_list|()
block|{
return|return
name|this
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|QueryBean3
block|{
specifier|private
name|boolean
name|a
decl_stmt|;
specifier|private
name|int
name|b
decl_stmt|;
specifier|public
name|boolean
name|isA
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
specifier|public
enum|enum
name|TestEnum
block|{
name|A
block|;     }
block|}
end_class

end_unit

