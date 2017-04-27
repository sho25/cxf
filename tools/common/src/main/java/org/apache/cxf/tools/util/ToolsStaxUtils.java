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
name|tools
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|nio
operator|.
name|file
operator|.
name|Files
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
name|Arrays
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Stack
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
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
name|staxutils
operator|.
name|StaxUtils
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
name|tools
operator|.
name|common
operator|.
name|Tag
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|ToolsStaxUtils
block|{
specifier|private
name|ToolsStaxUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
name|List
argument_list|<
name|Tag
argument_list|>
name|getTags
parameter_list|(
specifier|final
name|File
name|source
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Tag
argument_list|>
name|tags
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|ignoreEmptyTags
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"sequence"
block|}
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
operator|new
name|BufferedInputStream
argument_list|(
name|Files
operator|.
name|newInputStream
argument_list|(
name|source
operator|.
name|toPath
argument_list|()
argument_list|)
argument_list|)
init|)
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|Tag
name|newTag
init|=
literal|null
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
name|QName
name|checkingPoint
init|=
literal|null
decl_stmt|;
name|Stack
argument_list|<
name|Tag
argument_list|>
name|stack
init|=
operator|new
name|Stack
argument_list|<
name|Tag
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|reader
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|int
name|event
init|=
name|reader
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|checkingPoint
operator|!=
literal|null
condition|)
block|{
name|count
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|event
operator|==
name|XMLStreamReader
operator|.
name|START_ELEMENT
condition|)
block|{
name|newTag
operator|=
operator|new
name|Tag
argument_list|()
expr_stmt|;
name|newTag
operator|.
name|setName
argument_list|(
name|reader
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|ignoreEmptyTags
operator|.
name|contains
argument_list|(
name|reader
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|checkingPoint
operator|=
name|reader
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|reader
operator|.
name|getAttributeCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|newTag
operator|.
name|getAttributes
argument_list|()
operator|.
name|put
argument_list|(
name|reader
operator|.
name|getAttributeName
argument_list|(
name|i
argument_list|)
argument_list|,
name|reader
operator|.
name|getAttributeValue
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|stack
operator|.
name|push
argument_list|(
name|newTag
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|event
operator|==
name|XMLStreamReader
operator|.
name|CHARACTERS
condition|)
block|{
name|newTag
operator|.
name|setText
argument_list|(
name|reader
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|event
operator|==
name|XMLStreamReader
operator|.
name|END_ELEMENT
condition|)
block|{
name|Tag
name|startTag
init|=
name|stack
operator|.
name|pop
argument_list|()
decl_stmt|;
if|if
condition|(
name|checkingPoint
operator|!=
literal|null
operator|&&
name|checkingPoint
operator|.
name|equals
argument_list|(
name|reader
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|count
operator|==
literal|1
condition|)
block|{
comment|//Tag is empty, and it's in the ignore collection, so we just skip this tag
block|}
else|else
block|{
name|tags
operator|.
name|add
argument_list|(
name|startTag
argument_list|)
expr_stmt|;
block|}
name|count
operator|=
literal|0
expr_stmt|;
name|checkingPoint
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|tags
operator|.
name|add
argument_list|(
name|startTag
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|tags
return|;
block|}
specifier|public
specifier|static
name|Tag
name|getTagTree
parameter_list|(
specifier|final
name|File
name|source
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|getTagTree
argument_list|(
name|source
argument_list|,
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Tag
name|getTagTree
parameter_list|(
specifier|final
name|File
name|source
parameter_list|,
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|ignoreAttr
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|InputStream
name|is
init|=
operator|new
name|BufferedInputStream
argument_list|(
name|Files
operator|.
name|newInputStream
argument_list|(
name|source
operator|.
name|toPath
argument_list|()
argument_list|)
argument_list|)
init|)
block|{
return|return
name|getTagTree
argument_list|(
name|is
argument_list|,
name|ignoreAttr
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|Tag
name|getTagTree
parameter_list|(
specifier|final
name|File
name|source
parameter_list|,
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|ignoreAttr
parameter_list|,
name|Map
argument_list|<
name|QName
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|types
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|InputStream
name|is
init|=
operator|new
name|BufferedInputStream
argument_list|(
name|Files
operator|.
name|newInputStream
argument_list|(
name|source
operator|.
name|toPath
argument_list|()
argument_list|)
argument_list|)
init|)
block|{
return|return
name|getTagTree
argument_list|(
name|is
argument_list|,
name|ignoreAttr
argument_list|,
name|types
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|Tag
name|getTagTree
parameter_list|(
specifier|final
name|InputStream
name|is
parameter_list|,
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|ignoreAttr
parameter_list|,
name|Map
argument_list|<
name|QName
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|types
parameter_list|)
throws|throws
name|Exception
block|{
name|Tag
name|root
init|=
operator|new
name|Tag
argument_list|()
decl_stmt|;
name|root
operator|.
name|setName
argument_list|(
operator|new
name|QName
argument_list|(
literal|"root"
argument_list|,
literal|"root"
argument_list|)
argument_list|)
expr_stmt|;
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|Tag
name|newTag
init|=
literal|null
decl_stmt|;
name|Tag
name|currentTag
init|=
name|root
decl_stmt|;
while|while
condition|(
name|reader
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|int
name|event
init|=
name|reader
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|event
operator|==
name|XMLStreamReader
operator|.
name|START_ELEMENT
condition|)
block|{
name|newTag
operator|=
operator|new
name|Tag
argument_list|()
expr_stmt|;
name|newTag
operator|.
name|setName
argument_list|(
name|reader
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|ignoreAttr
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|newTag
operator|.
name|getIgnoreAttr
argument_list|()
operator|.
name|addAll
argument_list|(
name|ignoreAttr
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|reader
operator|.
name|getAttributeCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
comment|//probably a qname to a type, pull namespace in differently
name|String
name|tp
init|=
name|reader
operator|.
name|getAttributeValue
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|isType
argument_list|(
name|types
argument_list|,
name|reader
operator|.
name|getName
argument_list|()
argument_list|,
name|reader
operator|.
name|getAttributeName
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
name|int
name|idx
init|=
name|tp
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
operator|&&
name|tp
operator|.
name|length
argument_list|()
operator|>
name|idx
operator|&&
name|tp
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
name|String
name|pfx
init|=
name|tp
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
decl_stmt|;
name|String
name|ns
init|=
name|reader
operator|.
name|getNamespaceURI
argument_list|(
name|pfx
argument_list|)
decl_stmt|;
if|if
condition|(
name|ns
operator|!=
literal|null
condition|)
block|{
name|tp
operator|=
literal|"{"
operator|+
name|ns
operator|+
literal|"}"
operator|+
name|tp
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|String
name|ns
init|=
name|reader
operator|.
name|getNamespaceURI
argument_list|(
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
name|ns
operator|!=
literal|null
condition|)
block|{
name|tp
operator|=
literal|"{"
operator|+
name|ns
operator|+
literal|"}"
operator|+
name|tp
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|newTag
operator|.
name|getAttributes
argument_list|()
operator|.
name|put
argument_list|(
name|reader
operator|.
name|getAttributeName
argument_list|(
name|i
argument_list|)
argument_list|,
name|tp
argument_list|)
expr_stmt|;
block|}
name|newTag
operator|.
name|setParent
argument_list|(
name|currentTag
argument_list|)
expr_stmt|;
name|currentTag
operator|.
name|getTags
argument_list|()
operator|.
name|add
argument_list|(
name|newTag
argument_list|)
expr_stmt|;
name|currentTag
operator|=
name|newTag
expr_stmt|;
block|}
if|if
condition|(
name|event
operator|==
name|XMLStreamReader
operator|.
name|CHARACTERS
condition|)
block|{
name|newTag
operator|.
name|setText
argument_list|(
name|reader
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|event
operator|==
name|XMLStreamReader
operator|.
name|END_ELEMENT
condition|)
block|{
name|currentTag
operator|=
name|currentTag
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
block|}
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|root
return|;
block|}
specifier|private
specifier|static
name|boolean
name|isType
parameter_list|(
name|Map
argument_list|<
name|QName
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|types
parameter_list|,
name|QName
name|name
parameter_list|,
name|QName
name|attributeName
parameter_list|)
block|{
if|if
condition|(
name|types
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|a
init|=
name|types
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|a
operator|!=
literal|null
operator|&&
name|a
operator|.
name|contains
argument_list|(
name|attributeName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

