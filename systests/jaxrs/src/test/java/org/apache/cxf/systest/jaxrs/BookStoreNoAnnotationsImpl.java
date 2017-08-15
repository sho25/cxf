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
name|UriInfo
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
name|common
operator|.
name|util
operator|.
name|PropertyUtils
import|;
end_import

begin_class
specifier|public
class|class
name|BookStoreNoAnnotationsImpl
implements|implements
name|BookStoreNoAnnotationsInterface
implements|,
name|HttpHeadersContext
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
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Context
specifier|private
name|UriInfo
name|ui
decl_stmt|;
specifier|private
name|HttpHeaders
name|hs
decl_stmt|;
specifier|public
name|BookStoreNoAnnotationsImpl
parameter_list|()
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
literal|123L
argument_list|)
expr_stmt|;
name|b
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
name|b
operator|.
name|getId
argument_list|()
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setHttpHeaders
parameter_list|(
name|HttpHeaders
name|headers
parameter_list|)
block|{
name|this
operator|.
name|hs
operator|=
name|headers
expr_stmt|;
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
name|hs
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|Response
operator|.
name|serverError
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
throw|;
block|}
name|boolean
name|springProxy
init|=
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|hs
operator|.
name|getHeaderString
argument_list|(
literal|"SpringProxy"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|springProxy
operator|&&
name|ui
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|Response
operator|.
name|serverError
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
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
specifier|public
name|ChapterNoAnnotations
name|getBookChapter
parameter_list|(
name|Long
name|id
parameter_list|)
throws|throws
name|BookNotFoundFault
block|{
name|Book
name|b
init|=
name|books
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|Chapter
name|ch
init|=
name|b
operator|.
name|getChapter
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|ChapterNoAnnotations
name|ch2
init|=
operator|new
name|ChapterNoAnnotations
argument_list|()
decl_stmt|;
name|ch2
operator|.
name|setId
argument_list|(
name|ch
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|ch2
operator|.
name|setTitle
argument_list|(
name|ch
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ch2
return|;
block|}
specifier|public
name|List
argument_list|<
name|Book
argument_list|>
name|getBooks
parameter_list|(
name|List
argument_list|<
name|Book
argument_list|>
name|thebooks
parameter_list|)
block|{
return|return
name|thebooks
return|;
block|}
specifier|public
name|void
name|pingBookStore
parameter_list|()
block|{
comment|// complete
block|}
block|}
end_class

end_unit

