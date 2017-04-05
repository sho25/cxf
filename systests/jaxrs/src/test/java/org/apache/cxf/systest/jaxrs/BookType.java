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
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlSeeAlso
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
name|XmlType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonTypeInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonTypeInfo
operator|.
name|As
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonTypeInfo
operator|.
name|Id
import|;
end_import

begin_class
annotation|@
name|JsonTypeInfo
argument_list|(
name|use
operator|=
name|Id
operator|.
name|CLASS
argument_list|,
name|include
operator|=
name|As
operator|.
name|PROPERTY
argument_list|,
name|property
operator|=
literal|"class"
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"Book"
argument_list|)
annotation|@
name|XmlSeeAlso
argument_list|(
name|SuperBook2
operator|.
name|class
argument_list|)
specifier|public
class|class
name|BookType
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|long
name|id
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Long
argument_list|,
name|Chapter
argument_list|>
name|chapters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|BookType
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
comment|//System.out.println("----chapters: " + chapters.size());
block|}
specifier|public
name|BookType
parameter_list|(
name|String
name|name
parameter_list|,
name|long
name|id
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|name
operator|=
name|n
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|long
name|i
parameter_list|)
block|{
name|id
operator|=
name|i
expr_stmt|;
block|}
specifier|public
name|long
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
annotation|@
name|PUT
specifier|public
name|void
name|cloneState
parameter_list|(
name|BookType
name|book
parameter_list|)
block|{
name|id
operator|=
name|book
operator|.
name|getId
argument_list|()
expr_stmt|;
name|name
operator|=
name|book
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GET
specifier|public
name|BookType
name|retrieveState
parameter_list|()
block|{
return|return
name|this
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"chapters/{chapterid}/"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml;charset=ISO-8859-1"
argument_list|)
specifier|public
name|Chapter
name|getChapter
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"chapterid"
argument_list|)
name|int
name|chapterid
parameter_list|)
block|{
return|return
name|chapters
operator|.
name|get
argument_list|(
operator|new
name|Long
argument_list|(
name|chapterid
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"chapters/acceptencoding/{chapterid}/"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
specifier|public
name|Chapter
name|getChapterAcceptEncoding
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"chapterid"
argument_list|)
name|int
name|chapterid
parameter_list|)
block|{
return|return
name|chapters
operator|.
name|get
argument_list|(
operator|new
name|Long
argument_list|(
name|chapterid
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"chapters/badencoding/{chapterid}/"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml;charset=UTF-48"
argument_list|)
specifier|public
name|Chapter
name|getChapterBadEncoding
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"chapterid"
argument_list|)
name|int
name|chapterid
parameter_list|)
block|{
return|return
name|chapters
operator|.
name|get
argument_list|(
operator|new
name|Long
argument_list|(
name|chapterid
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"chapters/sub/{chapterid}/"
argument_list|)
specifier|public
name|Chapter
name|getSubChapter
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"chapterid"
argument_list|)
name|int
name|chapterid
parameter_list|)
block|{
return|return
name|chapters
operator|.
name|get
argument_list|(
operator|new
name|Long
argument_list|(
name|chapterid
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"chaptersobject/sub/{chapterid}/"
argument_list|)
specifier|public
name|Object
name|getSubChapterObject
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"chapterid"
argument_list|)
name|int
name|chapterid
parameter_list|)
block|{
return|return
name|getSubChapter
argument_list|(
name|chapterid
argument_list|)
return|;
block|}
specifier|final
name|void
name|init
parameter_list|()
block|{
name|Chapter
name|c1
init|=
operator|new
name|Chapter
argument_list|()
decl_stmt|;
name|c1
operator|.
name|setId
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|c1
operator|.
name|setTitle
argument_list|(
literal|"chapter 1"
argument_list|)
expr_stmt|;
name|chapters
operator|.
name|put
argument_list|(
name|c1
operator|.
name|getId
argument_list|()
argument_list|,
name|c1
argument_list|)
expr_stmt|;
name|Chapter
name|c2
init|=
operator|new
name|Chapter
argument_list|()
decl_stmt|;
name|c2
operator|.
name|setId
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|c2
operator|.
name|setTitle
argument_list|(
literal|"chapter 2"
argument_list|)
expr_stmt|;
name|chapters
operator|.
name|put
argument_list|(
name|c2
operator|.
name|getId
argument_list|()
argument_list|,
name|c2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

