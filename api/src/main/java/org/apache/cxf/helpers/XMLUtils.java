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
name|helpers
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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|WeakHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
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
name|XMLStreamException
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|Source
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
name|TransformerException
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
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Attr
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NamedNodeMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Text
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
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
name|logging
operator|.
name|LogUtils
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
name|StringUtils
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
name|PrettyPrintXMLStreamWriter
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

begin_class
specifier|public
specifier|final
class|class
name|XMLUtils
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|XMLUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|ClassLoader
argument_list|,
name|DocumentBuilder
argument_list|>
name|DOCUMENT_BUILDERS
init|=
name|Collections
operator|.
name|synchronizedMap
argument_list|(
operator|new
name|WeakHashMap
argument_list|<
name|ClassLoader
argument_list|,
name|DocumentBuilder
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|XML_ESCAPE_CHARS
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"[\"'&<>]"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|XML_ENCODING_TABLE
decl_stmt|;
static|static
block|{
name|XML_ENCODING_TABLE
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|XML_ENCODING_TABLE
operator|.
name|put
argument_list|(
literal|"\""
argument_list|,
literal|"&quot;"
argument_list|)
expr_stmt|;
name|XML_ENCODING_TABLE
operator|.
name|put
argument_list|(
literal|"'"
argument_list|,
literal|"&apos;"
argument_list|)
expr_stmt|;
name|XML_ENCODING_TABLE
operator|.
name|put
argument_list|(
literal|"<"
argument_list|,
literal|"&lt;"
argument_list|)
expr_stmt|;
name|XML_ENCODING_TABLE
operator|.
name|put
argument_list|(
literal|">"
argument_list|,
literal|"&gt;"
argument_list|)
expr_stmt|;
name|XML_ENCODING_TABLE
operator|.
name|put
argument_list|(
literal|"&"
argument_list|,
literal|"&amp;"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|XMLUtils
parameter_list|()
block|{     }
specifier|private
specifier|static
name|DocumentBuilder
name|getDocumentBuilder
parameter_list|()
throws|throws
name|ParserConfigurationException
block|{
name|ClassLoader
name|loader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|loader
operator|==
literal|null
condition|)
block|{
name|loader
operator|=
name|XMLUtils
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|loader
operator|==
literal|null
condition|)
block|{
return|return
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newDocumentBuilder
argument_list|()
return|;
block|}
name|DocumentBuilder
name|factory
init|=
name|DOCUMENT_BUILDERS
operator|.
name|get
argument_list|(
name|loader
argument_list|)
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
name|DocumentBuilderFactory
name|f2
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|f2
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|factory
operator|=
name|f2
operator|.
name|newDocumentBuilder
argument_list|()
expr_stmt|;
name|DOCUMENT_BUILDERS
operator|.
name|put
argument_list|(
name|loader
argument_list|,
name|factory
argument_list|)
expr_stmt|;
block|}
return|return
name|factory
return|;
block|}
specifier|public
specifier|static
name|Document
name|parse
parameter_list|(
name|InputSource
name|is
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|StaxUtils
operator|.
name|read
argument_list|(
name|is
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Document
name|parse
parameter_list|(
name|File
name|is
parameter_list|)
throws|throws
name|XMLStreamException
throws|,
name|IOException
block|{
name|InputStream
name|fin
init|=
operator|new
name|FileInputStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|StaxUtils
operator|.
name|read
argument_list|(
name|fin
argument_list|)
return|;
block|}
finally|finally
block|{
name|fin
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|Document
name|parse
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|in
operator|==
literal|null
operator|&&
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"XMLUtils trying to parse a null inputstream"
argument_list|)
expr_stmt|;
block|}
return|return
name|StaxUtils
operator|.
name|read
argument_list|(
name|in
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Document
name|parse
parameter_list|(
name|String
name|in
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
operator|new
name|StringReader
argument_list|(
name|in
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|StaxUtils
operator|.
name|read
argument_list|(
name|reader
argument_list|)
return|;
block|}
finally|finally
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|Document
name|parse
parameter_list|(
name|byte
index|[]
name|in
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"XMLUtils trying to parse a null bytes"
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
return|return
name|StaxUtils
operator|.
name|read
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|in
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Document
name|newDocument
parameter_list|()
throws|throws
name|ParserConfigurationException
block|{
return|return
name|getDocumentBuilder
argument_list|()
operator|.
name|newDocument
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|void
name|writeTo
parameter_list|(
name|Node
name|node
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeTo
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|node
argument_list|)
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|writeTo
parameter_list|(
name|Node
name|node
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|int
name|indent
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeTo
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|node
argument_list|)
argument_list|,
name|os
argument_list|,
name|indent
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|writeTo
parameter_list|(
name|Source
name|src
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeTo
argument_list|(
name|src
argument_list|,
name|os
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|writeTo
parameter_list|(
name|Node
name|node
parameter_list|,
name|Writer
name|os
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeTo
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|node
argument_list|)
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|writeTo
parameter_list|(
name|Node
name|node
parameter_list|,
name|Writer
name|os
parameter_list|,
name|int
name|indent
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeTo
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|node
argument_list|)
argument_list|,
name|os
argument_list|,
name|indent
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|writeTo
parameter_list|(
name|Source
name|src
parameter_list|,
name|Writer
name|os
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeTo
argument_list|(
name|src
argument_list|,
name|os
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|writeTo
parameter_list|(
name|Source
name|src
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|int
name|indent
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|String
name|enc
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|src
operator|instanceof
name|DOMSource
operator|&&
operator|(
operator|(
name|DOMSource
operator|)
name|src
operator|)
operator|.
name|getNode
argument_list|()
operator|instanceof
name|Document
condition|)
block|{
try|try
block|{
name|enc
operator|=
operator|(
call|(
name|Document
call|)
argument_list|(
operator|(
name|DOMSource
operator|)
name|src
argument_list|)
operator|.
name|getNode
argument_list|()
operator|)
operator|.
name|getXmlEncoding
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore - not DOM level 3
block|}
block|}
name|writeTo
argument_list|(
name|src
argument_list|,
name|os
argument_list|,
name|indent
argument_list|,
name|enc
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|writeTo
parameter_list|(
name|Source
name|src
parameter_list|,
name|Writer
name|os
parameter_list|,
name|int
name|indent
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|String
name|enc
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|src
operator|instanceof
name|DOMSource
operator|&&
operator|(
operator|(
name|DOMSource
operator|)
name|src
operator|)
operator|.
name|getNode
argument_list|()
operator|instanceof
name|Document
condition|)
block|{
try|try
block|{
name|enc
operator|=
operator|(
call|(
name|Document
call|)
argument_list|(
operator|(
name|DOMSource
operator|)
name|src
argument_list|)
operator|.
name|getNode
argument_list|()
operator|)
operator|.
name|getXmlEncoding
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore - not DOM level 3
block|}
block|}
name|writeTo
argument_list|(
name|src
argument_list|,
name|os
argument_list|,
name|indent
argument_list|,
name|enc
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|writeTo
parameter_list|(
name|Source
name|src
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|int
name|indent
parameter_list|,
name|String
name|charset
parameter_list|,
name|boolean
name|omitXmlDecl
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|charset
argument_list|)
condition|)
block|{
name|charset
operator|=
literal|"utf-8"
expr_stmt|;
block|}
name|XMLStreamWriter
name|writer
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|os
argument_list|,
name|charset
argument_list|)
decl_stmt|;
if|if
condition|(
name|indent
operator|>
literal|0
condition|)
block|{
name|writer
operator|=
operator|new
name|PrettyPrintXMLStreamWriter
argument_list|(
name|writer
argument_list|,
literal|0
argument_list|,
name|indent
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|omitXmlDecl
condition|)
block|{
name|writer
operator|.
name|writeStartDocument
argument_list|(
name|charset
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
block|}
name|StaxUtils
operator|.
name|copy
argument_list|(
name|src
argument_list|,
name|writer
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|omitXmlDecl
condition|)
block|{
name|writer
operator|.
name|writeEndDocument
argument_list|()
expr_stmt|;
block|}
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|writeTo
parameter_list|(
name|Source
name|src
parameter_list|,
name|Writer
name|os
parameter_list|,
name|int
name|indent
parameter_list|,
name|String
name|charset
parameter_list|,
name|boolean
name|omitXmlDecl
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|charset
argument_list|)
condition|)
block|{
name|charset
operator|=
literal|"utf-8"
expr_stmt|;
block|}
name|XMLStreamWriter
name|writer
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|os
argument_list|)
decl_stmt|;
if|if
condition|(
name|indent
operator|>
literal|0
condition|)
block|{
name|writer
operator|=
operator|new
name|PrettyPrintXMLStreamWriter
argument_list|(
name|writer
argument_list|,
literal|0
argument_list|,
name|indent
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|omitXmlDecl
condition|)
block|{
name|writer
operator|.
name|writeStartDocument
argument_list|(
name|charset
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
block|}
name|StaxUtils
operator|.
name|copy
argument_list|(
name|src
argument_list|,
name|writer
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|omitXmlDecl
condition|)
block|{
name|writer
operator|.
name|writeEndDocument
argument_list|()
expr_stmt|;
block|}
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|Source
name|source
parameter_list|)
throws|throws
name|TransformerException
throws|,
name|IOException
block|{
name|StringWriter
name|out
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
try|try
block|{
name|writeTo
argument_list|(
name|source
argument_list|,
name|out
argument_list|,
literal|0
argument_list|,
literal|"utf-8"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
return|return
name|out
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|Node
name|node
parameter_list|,
name|int
name|indent
parameter_list|)
block|{
name|StringWriter
name|out
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
try|try
block|{
name|writeTo
argument_list|(
name|node
argument_list|,
name|out
argument_list|,
name|indent
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
return|return
name|out
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
name|StringWriter
name|out
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
try|try
block|{
name|writeTo
argument_list|(
name|node
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
return|return
name|out
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|void
name|printDOM
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
name|printDOM
argument_list|(
literal|""
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|printDOM
parameter_list|(
name|String
name|words
parameter_list|,
name|Node
name|node
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|words
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|toString
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|Attr
name|getAttribute
parameter_list|(
name|Element
name|el
parameter_list|,
name|String
name|attrName
parameter_list|)
block|{
return|return
name|el
operator|.
name|getAttributeNode
argument_list|(
name|attrName
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|void
name|replaceAttribute
parameter_list|(
name|Element
name|element
parameter_list|,
name|String
name|attr
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|element
operator|.
name|hasAttribute
argument_list|(
name|attr
argument_list|)
condition|)
block|{
name|element
operator|.
name|removeAttribute
argument_list|(
name|attr
argument_list|)
expr_stmt|;
block|}
name|element
operator|.
name|setAttribute
argument_list|(
name|attr
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|boolean
name|hasAttribute
parameter_list|(
name|Element
name|element
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|NamedNodeMap
name|attributes
init|=
name|element
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|attributes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|attributes
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getNodeValue
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
specifier|static
name|void
name|printAttributes
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
name|NamedNodeMap
name|attributes
init|=
name|element
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|attributes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|attributes
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"## prefix="
operator|+
name|node
operator|.
name|getPrefix
argument_list|()
operator|+
literal|" localname:"
operator|+
name|node
operator|.
name|getLocalName
argument_list|()
operator|+
literal|" value="
operator|+
name|node
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|QName
name|getNamespace
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|,
name|String
name|str
parameter_list|,
name|String
name|defaultNamespace
parameter_list|)
block|{
name|String
name|prefix
init|=
literal|null
decl_stmt|;
name|String
name|localName
init|=
literal|null
decl_stmt|;
name|StringTokenizer
name|tokenizer
init|=
operator|new
name|StringTokenizer
argument_list|(
name|str
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokenizer
operator|.
name|countTokens
argument_list|()
operator|==
literal|2
condition|)
block|{
name|prefix
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|localName
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tokenizer
operator|.
name|countTokens
argument_list|()
operator|==
literal|1
condition|)
block|{
name|localName
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
block|}
name|String
name|namespceURI
init|=
name|defaultNamespace
decl_stmt|;
if|if
condition|(
name|prefix
operator|!=
literal|null
condition|)
block|{
name|namespceURI
operator|=
name|namespaces
operator|.
name|get
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|QName
argument_list|(
name|namespceURI
argument_list|,
name|localName
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|void
name|generateXMLFile
parameter_list|(
name|Element
name|element
parameter_list|,
name|Writer
name|writer
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeTo
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|element
argument_list|)
argument_list|,
name|writer
argument_list|,
literal|2
argument_list|,
literal|"UTF-8"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|Element
name|createElementNS
parameter_list|(
name|Node
name|node
parameter_list|,
name|QName
name|name
parameter_list|)
block|{
return|return
name|createElementNS
argument_list|(
name|node
operator|.
name|getOwnerDocument
argument_list|()
argument_list|,
name|name
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|name
operator|.
name|getLocalPart
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Element
name|createElementNS
parameter_list|(
name|Document
name|root
parameter_list|,
name|QName
name|name
parameter_list|)
block|{
return|return
name|createElementNS
argument_list|(
name|root
argument_list|,
name|name
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|name
operator|.
name|getLocalPart
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Element
name|createElementNS
parameter_list|(
name|Document
name|root
parameter_list|,
name|String
name|namespaceURI
parameter_list|,
name|String
name|qualifiedName
parameter_list|)
block|{
return|return
name|root
operator|.
name|createElementNS
argument_list|(
name|namespaceURI
argument_list|,
name|qualifiedName
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Text
name|createTextNode
parameter_list|(
name|Document
name|root
parameter_list|,
name|String
name|data
parameter_list|)
block|{
return|return
name|root
operator|.
name|createTextNode
argument_list|(
name|data
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Text
name|createTextNode
parameter_list|(
name|Node
name|node
parameter_list|,
name|String
name|data
parameter_list|)
block|{
return|return
name|createTextNode
argument_list|(
name|node
operator|.
name|getOwnerDocument
argument_list|()
argument_list|,
name|data
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|void
name|removeContents
parameter_list|(
name|Node
name|parent
parameter_list|)
block|{
name|Node
name|node
init|=
name|parent
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|parent
operator|.
name|removeChild
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|node
operator|=
name|node
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|InputStream
name|getInputStream
parameter_list|(
name|Document
name|doc
parameter_list|)
throws|throws
name|Exception
block|{
name|LoadingByteArrayOutputStream
name|out
init|=
operator|new
name|LoadingByteArrayOutputStream
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|writer
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|out
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|writeDocument
argument_list|(
name|doc
argument_list|,
name|writer
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|out
operator|.
name|createInputStream
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|Element
name|fetchElementByNameAttribute
parameter_list|(
name|Element
name|parent
parameter_list|,
name|String
name|targetName
parameter_list|,
name|String
name|nameValue
parameter_list|)
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|elemList
init|=
name|DOMUtils
operator|.
name|findAllElementsByTagName
argument_list|(
name|parent
argument_list|,
name|targetName
argument_list|)
decl_stmt|;
for|for
control|(
name|Element
name|elem
range|:
name|elemList
control|)
block|{
if|if
condition|(
name|elem
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
operator|.
name|equals
argument_list|(
name|nameValue
argument_list|)
condition|)
block|{
return|return
name|elem
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|QName
name|getQName
parameter_list|(
name|String
name|value
parameter_list|,
name|Node
name|node
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|index
init|=
name|value
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|==
operator|-
literal|1
condition|)
block|{
return|return
operator|new
name|QName
argument_list|(
name|value
argument_list|)
return|;
block|}
name|String
name|prefix
init|=
name|value
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
decl_stmt|;
name|String
name|localName
init|=
name|value
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
decl_stmt|;
name|String
name|ns
init|=
name|node
operator|.
name|lookupNamespaceURI
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
if|if
condition|(
name|ns
operator|==
literal|null
operator|||
name|localName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Invalid QName in mapping: "
operator|+
name|value
argument_list|)
throw|;
block|}
return|return
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|localName
argument_list|,
name|prefix
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Node
name|fromSource
parameter_list|(
name|Source
name|src
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|StaxUtils
operator|.
name|read
argument_list|(
name|src
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|QName
name|convertStringToQName
parameter_list|(
name|String
name|expandedQName
parameter_list|)
block|{
return|return
name|convertStringToQName
argument_list|(
name|expandedQName
argument_list|,
literal|""
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|QName
name|convertStringToQName
parameter_list|(
name|String
name|expandedQName
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
name|int
name|ind1
init|=
name|expandedQName
operator|.
name|indexOf
argument_list|(
literal|'{'
argument_list|)
decl_stmt|;
if|if
condition|(
name|ind1
operator|!=
literal|0
condition|)
block|{
return|return
operator|new
name|QName
argument_list|(
name|expandedQName
argument_list|)
return|;
block|}
name|int
name|ind2
init|=
name|expandedQName
operator|.
name|indexOf
argument_list|(
literal|'}'
argument_list|)
decl_stmt|;
if|if
condition|(
name|ind2
operator|<=
name|ind1
operator|+
literal|1
operator|||
name|ind2
operator|>=
name|expandedQName
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|ns
init|=
name|expandedQName
operator|.
name|substring
argument_list|(
name|ind1
operator|+
literal|1
argument_list|,
name|ind2
argument_list|)
decl_stmt|;
name|String
name|localName
init|=
name|expandedQName
operator|.
name|substring
argument_list|(
name|ind2
operator|+
literal|1
argument_list|)
decl_stmt|;
return|return
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|localName
argument_list|,
name|prefix
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Set
argument_list|<
name|QName
argument_list|>
name|convertStringsToQNames
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|expandedQNames
parameter_list|)
block|{
name|Set
argument_list|<
name|QName
argument_list|>
name|dropElements
init|=
name|Collections
operator|.
name|emptySet
argument_list|()
decl_stmt|;
if|if
condition|(
name|expandedQNames
operator|!=
literal|null
condition|)
block|{
name|dropElements
operator|=
operator|new
name|LinkedHashSet
argument_list|<
name|QName
argument_list|>
argument_list|(
name|expandedQNames
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|val
range|:
name|expandedQNames
control|)
block|{
name|dropElements
operator|.
name|add
argument_list|(
name|XMLUtils
operator|.
name|convertStringToQName
argument_list|(
name|val
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|dropElements
return|;
block|}
specifier|public
specifier|static
name|String
name|xmlEncode
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|XML_ESCAPE_CHARS
operator|.
name|matcher
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|boolean
name|match
init|=
name|m
operator|.
name|find
argument_list|()
decl_stmt|;
if|if
condition|(
name|match
condition|)
block|{
name|int
name|i
init|=
literal|0
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
do|do
block|{
name|String
name|replacement
init|=
name|XML_ENCODING_TABLE
operator|.
name|get
argument_list|(
name|m
operator|.
name|group
argument_list|()
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|value
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|m
operator|.
name|start
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|replacement
argument_list|)
expr_stmt|;
name|i
operator|=
name|m
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
do|while
condition|(
name|m
operator|.
name|find
argument_list|()
condition|)
do|;
name|sb
operator|.
name|append
argument_list|(
name|value
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|value
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|value
return|;
block|}
block|}
block|}
end_class

end_unit

