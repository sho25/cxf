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
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|LinkedHashMap
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
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataSource
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
name|MultivaluedMap
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
name|Unmarshaller
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|jaxrs
operator|.
name|ext
operator|.
name|multipart
operator|.
name|Attachment
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
name|Multipart
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
name|utils
operator|.
name|multipart
operator|.
name|AttachmentUtils
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/bookstore"
argument_list|)
specifier|public
class|class
name|MultipartStore
block|{
annotation|@
name|Context
specifier|private
name|MessageContext
name|context
decl_stmt|;
specifier|public
name|MultipartStore
parameter_list|()
block|{     }
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books/image"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"multipart/mixed"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"multipart/mixed"
argument_list|)
specifier|public
name|byte
index|[]
name|addBookImage
parameter_list|(
name|byte
index|[]
name|image
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|image
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books/formimage"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"multipart/form-data"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"multipart/form-data"
argument_list|)
specifier|public
name|MultipartBody
name|addBookFormImage
parameter_list|(
name|MultipartBody
name|image
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|image
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books/jaxbjsonimage"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"multipart/mixed"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"multipart/mixed"
argument_list|)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|addBookJaxbJsonImage
parameter_list|(
annotation|@
name|Multipart
argument_list|(
literal|"root.message@cxf.apache.org"
argument_list|)
name|Book
name|jaxb
parameter_list|,
annotation|@
name|Multipart
argument_list|(
literal|"1"
argument_list|)
name|Book
name|json
parameter_list|,
annotation|@
name|Multipart
argument_list|(
literal|"2"
argument_list|)
name|byte
index|[]
name|image
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|objects
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|objects
operator|.
name|put
argument_list|(
literal|"application/xml"
argument_list|,
name|jaxb
argument_list|)
expr_stmt|;
name|objects
operator|.
name|put
argument_list|(
literal|"application/json"
argument_list|,
name|json
argument_list|)
expr_stmt|;
name|objects
operator|.
name|put
argument_list|(
literal|"application/octet-stream"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|image
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|objects
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books/jaxbimagejson"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"multipart/mixed"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"multipart/mixed"
argument_list|)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|addBookJaxbJsonImage2
parameter_list|(
annotation|@
name|Multipart
argument_list|(
literal|"theroot"
argument_list|)
name|Book
name|jaxb
parameter_list|,
annotation|@
name|Multipart
argument_list|(
literal|"thejson"
argument_list|)
name|Book
name|json
parameter_list|,
annotation|@
name|Multipart
argument_list|(
literal|"theimage"
argument_list|)
name|byte
index|[]
name|image
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|objects
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|objects
operator|.
name|put
argument_list|(
literal|"application/xml"
argument_list|,
name|jaxb
argument_list|)
expr_stmt|;
name|objects
operator|.
name|put
argument_list|(
literal|"application/json"
argument_list|,
name|json
argument_list|)
expr_stmt|;
name|objects
operator|.
name|put
argument_list|(
literal|"application/octet-stream"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|image
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|objects
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books/stream"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBookFromStream
parameter_list|(
name|StreamSource
name|source
parameter_list|)
throws|throws
name|Exception
block|{
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
name|Unmarshaller
name|u
init|=
name|c
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|Book
name|b
init|=
operator|(
name|Book
operator|)
name|u
operator|.
name|unmarshal
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|b
operator|.
name|setId
argument_list|(
literal|124
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|b
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
literal|"/books/form"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"multipart/form-data"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBookFromForm
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|data
parameter_list|)
throws|throws
name|Exception
block|{
name|Book
name|b
init|=
operator|new
name|Book
argument_list|()
decl_stmt|;
name|b
operator|.
name|setId
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
name|data
operator|.
name|getFirst
argument_list|(
literal|"id"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|b
operator|.
name|setName
argument_list|(
name|data
operator|.
name|getFirst
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|b
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
literal|"/books/formbody"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"multipart/form-data"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBookFromFormBody
parameter_list|(
name|MultipartBody
name|body
parameter_list|)
throws|throws
name|Exception
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|data
init|=
name|AttachmentUtils
operator|.
name|populateFormMap
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Book
name|b
init|=
operator|new
name|Book
argument_list|()
decl_stmt|;
name|b
operator|.
name|setId
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
name|data
operator|.
name|getFirst
argument_list|(
literal|"id"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|b
operator|.
name|setName
argument_list|(
name|data
operator|.
name|getFirst
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|b
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
literal|"/books/formbody2"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"multipart/form-data"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBookFromFormBody2
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|addBookFromFormBody
argument_list|(
name|AttachmentUtils
operator|.
name|getMultipartBody
argument_list|(
name|context
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books/formparam"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBookFromFormParam
parameter_list|(
annotation|@
name|FormParam
argument_list|(
literal|"name"
argument_list|)
name|String
name|title
parameter_list|,
annotation|@
name|FormParam
argument_list|(
literal|"id"
argument_list|)
name|Long
name|id
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
literal|"CXF in Action - 2"
operator|.
name|equals
argument_list|(
name|title
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
name|Book
name|b
init|=
operator|new
name|Book
argument_list|()
decl_stmt|;
name|b
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|b
operator|.
name|setName
argument_list|(
name|title
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|b
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
literal|"/books/formparambean"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBookFromFormBean
parameter_list|(
annotation|@
name|FormParam
argument_list|(
literal|""
argument_list|)
name|Book
name|b
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|Response
operator|.
name|ok
argument_list|(
name|b
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
literal|"/books/istream"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBookFromInputStream
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|readBookFromInputStream
argument_list|(
name|is
argument_list|)
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books/dsource"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBookFromDataSource
parameter_list|(
name|DataSource
name|ds
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|readBookFromInputStream
argument_list|(
name|ds
operator|.
name|getInputStream
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books/jaxb2"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"multipart/related;type=\"text/xml\""
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBookParts
parameter_list|(
annotation|@
name|Multipart
argument_list|(
literal|"rootPart"
argument_list|)
name|Book
name|b1
parameter_list|,
annotation|@
name|Multipart
argument_list|(
literal|"book2"
argument_list|)
name|Book
name|b2
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|b1
operator|.
name|equals
argument_list|(
name|b2
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
if|if
condition|(
operator|!
name|b1
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|b2
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
name|b1
operator|.
name|setId
argument_list|(
literal|124
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|b1
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
literal|"/books/jaxbonly"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"multipart/mixed"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"multipart/mixed;type=text/xml"
argument_list|)
specifier|public
name|List
argument_list|<
name|Book
argument_list|>
name|addBooks
parameter_list|(
name|List
argument_list|<
name|Book
argument_list|>
name|books
parameter_list|)
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books2
init|=
operator|new
name|ArrayList
argument_list|<
name|Book
argument_list|>
argument_list|()
decl_stmt|;
name|books2
operator|.
name|addAll
argument_list|(
name|books
argument_list|)
expr_stmt|;
return|return
name|books2
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books/jaxbjson"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBookJaxbJson
parameter_list|(
annotation|@
name|Multipart
argument_list|(
name|value
operator|=
literal|"rootPart"
argument_list|,
name|type
operator|=
literal|"text/xml"
argument_list|)
name|Book2
name|b1
parameter_list|,
annotation|@
name|Multipart
argument_list|(
name|value
operator|=
literal|"book2"
argument_list|,
name|type
operator|=
literal|"application/json"
argument_list|)
name|Book
name|b2
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
literal|"CXF in Action"
operator|.
name|equals
argument_list|(
name|b1
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
operator|!
literal|"CXF in Action - 2"
operator|.
name|equals
argument_list|(
name|b2
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
name|b2
operator|.
name|setId
argument_list|(
literal|124
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|b2
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
literal|"/books/jsonform"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"multipart/form-data"
argument_list|)
specifier|public
name|Response
name|addBookJsonFromForm
parameter_list|(
name|Book
name|b1
parameter_list|)
throws|throws
name|Exception
block|{
name|b1
operator|.
name|setId
argument_list|(
literal|124
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|b1
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
literal|"/books/filesform"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"multipart/form-data"
argument_list|)
specifier|public
name|Response
name|addBookFilesForm
parameter_list|(
annotation|@
name|Multipart
argument_list|(
literal|"owner"
argument_list|)
name|String
name|name
parameter_list|,
annotation|@
name|Multipart
argument_list|(
literal|"files"
argument_list|)
name|List
argument_list|<
name|Book
argument_list|>
name|books
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|books
operator|.
name|size
argument_list|()
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
name|Book
name|b1
init|=
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Book
name|b2
init|=
name|books
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|"CXF in Action - 1"
operator|.
name|equals
argument_list|(
name|b1
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
operator|!
literal|"CXF in Action - 2"
operator|.
name|equals
argument_list|(
name|b2
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
operator|!
literal|"Larry"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
name|b1
operator|.
name|setId
argument_list|(
literal|124
argument_list|)
expr_stmt|;
name|b1
operator|.
name|setName
argument_list|(
literal|"CXF in Action - 2"
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|b1
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
literal|"/books/jaxbform"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"multipart/form-data"
argument_list|)
specifier|public
name|Response
name|addBookJaxbFromForm
parameter_list|(
name|Book
name|b1
parameter_list|)
throws|throws
name|Exception
block|{
name|b1
operator|.
name|setId
argument_list|(
literal|124
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|b1
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
literal|"/books/jsonjaxbform"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"multipart/form-data"
argument_list|)
specifier|public
name|Response
name|addBookJaxbJsonForm
parameter_list|(
annotation|@
name|Multipart
argument_list|(
literal|"jsonPart"
argument_list|)
name|Book
name|b1
parameter_list|,
annotation|@
name|Multipart
argument_list|(
literal|"bookXML"
argument_list|)
name|Book
name|b2
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
literal|"CXF in Action - 1"
operator|.
name|equals
argument_list|(
name|b1
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
operator|!
literal|"CXF in Action - 2"
operator|.
name|equals
argument_list|(
name|b2
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
name|b2
operator|.
name|setId
argument_list|(
literal|124
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|b2
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
literal|"/books/dsource2"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBookFromDataSource2
parameter_list|(
annotation|@
name|Multipart
argument_list|(
literal|"rootPart"
argument_list|)
name|DataSource
name|ds1
parameter_list|,
annotation|@
name|Multipart
argument_list|(
literal|"book2"
argument_list|)
name|DataSource
name|ds2
parameter_list|)
throws|throws
name|Exception
block|{
name|Response
name|r1
init|=
name|readBookFromInputStream
argument_list|(
name|ds1
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|Response
name|r2
init|=
name|readBookFromInputStream
argument_list|(
name|ds2
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|Book
name|b1
init|=
operator|(
name|Book
operator|)
name|r1
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|Book
name|b2
init|=
operator|(
name|Book
operator|)
name|r2
operator|.
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|b1
operator|.
name|equals
argument_list|(
name|b2
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
if|if
condition|(
operator|!
name|b1
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|b2
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
return|return
name|r1
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books/listattachments"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBookFromListOfAttachments
parameter_list|(
name|List
argument_list|<
name|Attachment
argument_list|>
name|atts
parameter_list|)
throws|throws
name|Exception
block|{
name|Response
name|r1
init|=
name|readBookFromInputStream
argument_list|(
name|atts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|Response
name|r2
init|=
name|readBookFromInputStream
argument_list|(
name|atts
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|Book
name|b1
init|=
operator|(
name|Book
operator|)
name|r1
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|Book
name|b2
init|=
operator|(
name|Book
operator|)
name|r2
operator|.
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|b1
operator|.
name|equals
argument_list|(
name|b2
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
if|if
condition|(
operator|!
name|b1
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|b2
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
return|return
name|r1
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books/body"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBookFromListOfAttachments
parameter_list|(
name|MultipartBody
name|body
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|addBookFromListOfAttachments
argument_list|(
name|body
operator|.
name|getAllAttachments
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books/lististreams"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBookFromListOfStreams
parameter_list|(
name|List
argument_list|<
name|InputStream
argument_list|>
name|atts
parameter_list|)
throws|throws
name|Exception
block|{
name|Response
name|r1
init|=
name|readBookFromInputStream
argument_list|(
name|atts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|Response
name|r2
init|=
name|readBookFromInputStream
argument_list|(
name|atts
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
decl_stmt|;
name|Book
name|b1
init|=
operator|(
name|Book
operator|)
name|r1
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|Book
name|b2
init|=
operator|(
name|Book
operator|)
name|r2
operator|.
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|b1
operator|.
name|equals
argument_list|(
name|b2
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
if|if
condition|(
operator|!
name|b1
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|b2
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
return|return
name|r1
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books/dhandler"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBookFromDataHandler
parameter_list|(
name|DataHandler
name|dh
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|readBookFromInputStream
argument_list|(
name|dh
operator|.
name|getInputStream
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books/attachment"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBookFromAttachment
parameter_list|(
name|Attachment
name|a
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|readBookFromInputStream
argument_list|(
name|a
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getInputStream
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books/mchandlers"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBookFromMessageContext
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Attachment
argument_list|>
name|handlers
init|=
name|AttachmentUtils
operator|.
name|getAttachmentsMap
argument_list|(
name|context
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Attachment
argument_list|>
name|entry
range|:
name|handlers
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|equals
argument_list|(
literal|"book2"
argument_list|)
condition|)
block|{
return|return
name|readBookFromInputStream
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getInputStream
argument_list|()
argument_list|)
return|;
block|}
block|}
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|500
argument_list|)
throw|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books/attachments"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBookFromAttachments
parameter_list|()
throws|throws
name|Exception
block|{
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|handlers
init|=
name|AttachmentUtils
operator|.
name|getChildAttachments
argument_list|(
name|context
argument_list|)
decl_stmt|;
for|for
control|(
name|Attachment
name|a
range|:
name|handlers
control|)
block|{
if|if
condition|(
name|a
operator|.
name|getContentId
argument_list|()
operator|.
name|equals
argument_list|(
literal|"book2"
argument_list|)
condition|)
block|{
return|return
name|readBookFromInputStream
argument_list|(
name|a
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getInputStream
argument_list|()
argument_list|)
return|;
block|}
block|}
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|500
argument_list|)
throw|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books/jaxb"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBook
parameter_list|(
name|Book
name|b
parameter_list|)
throws|throws
name|Exception
block|{
name|b
operator|.
name|setId
argument_list|(
literal|124
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|b
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
literal|"/books/mismatch1"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"multipart/related;type=\"bar/foo\""
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBookMismatched
parameter_list|(
name|Book
name|b
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books/mismatch2"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|addBookMismatched2
parameter_list|(
annotation|@
name|Multipart
argument_list|(
name|value
operator|=
literal|"rootPart"
argument_list|,
name|type
operator|=
literal|"f/b"
argument_list|)
name|Book
name|b
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
specifier|private
name|Response
name|readBookFromInputStream
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|Exception
block|{
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
name|Unmarshaller
name|u
init|=
name|c
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|Book
name|b
init|=
operator|(
name|Book
operator|)
name|u
operator|.
name|unmarshal
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|b
operator|.
name|setId
argument_list|(
literal|124
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|b
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

