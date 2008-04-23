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
name|ByteArrayOutputStream
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|javax
operator|.
name|wsdl
operator|.
name|Definition
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
name|transform
operator|.
name|OutputKeys
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
name|Transformer
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
name|TransformerConfigurationException
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
name|TransformerFactory
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
name|DOMResult
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
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
name|DOMImplementation
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
name|NodeList
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
name|w3c
operator|.
name|dom
operator|.
name|bootstrap
operator|.
name|DOMImplementationRegistry
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
name|ls
operator|.
name|DOMImplementationLS
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
name|ls
operator|.
name|LSOutput
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
name|ls
operator|.
name|LSSerializer
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
name|xml
operator|.
name|sax
operator|.
name|SAXException
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
name|DocumentBuilderFactory
argument_list|>
name|DOCUMENT_BUILDER_FACTORIES
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
name|DocumentBuilderFactory
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|ClassLoader
argument_list|,
name|TransformerFactory
argument_list|>
name|TRANSFORMER_FACTORIES
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
name|TransformerFactory
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
name|XMLUtils
parameter_list|()
block|{     }
specifier|private
specifier|static
name|TransformerFactory
name|getTransformerFactory
parameter_list|()
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
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
return|;
block|}
name|TransformerFactory
name|factory
init|=
name|TRANSFORMER_FACTORIES
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
name|factory
operator|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|TRANSFORMER_FACTORIES
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
specifier|private
specifier|static
name|DocumentBuilderFactory
name|getDocumentBuilderFactory
parameter_list|()
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
return|;
block|}
name|DocumentBuilderFactory
name|factory
init|=
name|DOCUMENT_BUILDER_FACTORIES
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
name|factory
operator|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|factory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DOCUMENT_BUILDER_FACTORIES
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
name|Transformer
name|newTransformer
parameter_list|()
throws|throws
name|TransformerConfigurationException
block|{
return|return
name|getTransformerFactory
argument_list|()
operator|.
name|newTransformer
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|DocumentBuilder
name|getParser
parameter_list|()
throws|throws
name|ParserConfigurationException
block|{
return|return
name|getDocumentBuilderFactory
argument_list|()
operator|.
name|newDocumentBuilder
argument_list|()
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
name|ParserConfigurationException
throws|,
name|SAXException
throws|,
name|IOException
block|{
return|return
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
name|is
operator|.
name|getSystemId
argument_list|()
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
name|ParserConfigurationException
throws|,
name|SAXException
throws|,
name|IOException
block|{
return|return
name|getParser
argument_list|()
operator|.
name|parse
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
name|InputStream
name|in
parameter_list|)
throws|throws
name|ParserConfigurationException
throws|,
name|SAXException
throws|,
name|IOException
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
name|getParser
argument_list|()
operator|.
name|parse
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
name|ParserConfigurationException
throws|,
name|SAXException
throws|,
name|IOException
block|{
return|return
name|parse
argument_list|(
name|in
operator|.
name|getBytes
argument_list|()
argument_list|)
return|;
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
name|ParserConfigurationException
throws|,
name|SAXException
throws|,
name|IOException
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
literal|"XMLUtils trying to parse a null bytes"
argument_list|)
expr_stmt|;
block|}
return|return
name|getParser
argument_list|()
operator|.
name|parse
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
name|getParser
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
literal|"no"
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
name|String
name|omitXmlDecl
parameter_list|)
block|{
name|Transformer
name|it
decl_stmt|;
try|try
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
name|it
operator|=
name|newTransformer
argument_list|()
expr_stmt|;
name|it
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|METHOD
argument_list|,
literal|"xml"
argument_list|)
expr_stmt|;
if|if
condition|(
name|indent
operator|>
operator|-
literal|1
condition|)
block|{
name|it
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|it
operator|.
name|setOutputProperty
argument_list|(
literal|"{http://xml.apache.org/xslt}indent-amount"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|indent
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|it
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|OMIT_XML_DECLARATION
argument_list|,
name|omitXmlDecl
argument_list|)
expr_stmt|;
name|it
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|ENCODING
argument_list|,
name|charset
argument_list|)
expr_stmt|;
name|it
operator|.
name|transform
argument_list|(
name|src
argument_list|,
operator|new
name|StreamResult
argument_list|(
name|os
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TransformerException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Failed to configure TRaX"
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
return|return
name|toString
argument_list|(
name|source
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|Source
name|source
parameter_list|,
name|Properties
name|props
parameter_list|)
throws|throws
name|TransformerException
throws|,
name|IOException
block|{
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|StreamResult
name|sr
init|=
operator|new
name|StreamResult
argument_list|(
name|bos
argument_list|)
decl_stmt|;
name|Transformer
name|trans
init|=
name|newTransformer
argument_list|()
decl_stmt|;
if|if
condition|(
name|props
operator|==
literal|null
condition|)
block|{
name|props
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|OutputKeys
operator|.
name|OMIT_XML_DECLARATION
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
block|}
name|trans
operator|.
name|setOutputProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|trans
operator|.
name|transform
argument_list|(
name|source
argument_list|,
name|sr
argument_list|)
expr_stmt|;
name|bos
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|bos
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
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|writeTo
argument_list|(
name|node
argument_list|,
name|out
argument_list|,
name|indent
argument_list|)
expr_stmt|;
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
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|writeTo
argument_list|(
name|node
argument_list|,
name|out
argument_list|)
expr_stmt|;
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
operator|(
name|String
operator|)
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
block|{
try|try
block|{
name|Transformer
name|it
init|=
name|newTransformer
argument_list|()
decl_stmt|;
name|it
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|METHOD
argument_list|,
literal|"xml"
argument_list|)
expr_stmt|;
name|it
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|it
operator|.
name|setOutputProperty
argument_list|(
literal|"{http://xml.apache.org/xslt}indent-amount"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|it
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|ENCODING
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|it
operator|.
name|transform
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|element
argument_list|)
argument_list|,
operator|new
name|StreamResult
argument_list|(
name|writer
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
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
name|node
parameter_list|)
block|{
name|NodeList
name|list
init|=
name|node
operator|.
name|getChildNodes
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
name|list
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|entry
init|=
name|list
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|node
operator|.
name|removeChild
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|String
name|writeQName
parameter_list|(
name|Definition
name|def
parameter_list|,
name|QName
name|qname
parameter_list|)
block|{
return|return
name|def
operator|.
name|getPrefix
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|+
literal|":"
operator|+
name|qname
operator|.
name|getLocalPart
argument_list|()
return|;
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
name|DOMImplementationLS
name|impl
init|=
literal|null
decl_stmt|;
name|DOMImplementation
name|docImpl
init|=
name|doc
operator|.
name|getImplementation
argument_list|()
decl_stmt|;
comment|// Try to get the DOMImplementation from doc first before
comment|// defaulting to the sun implementation.
if|if
condition|(
name|docImpl
operator|!=
literal|null
operator|&&
name|docImpl
operator|.
name|hasFeature
argument_list|(
literal|"LS"
argument_list|,
literal|"3.0"
argument_list|)
condition|)
block|{
name|impl
operator|=
operator|(
name|DOMImplementationLS
operator|)
name|docImpl
operator|.
name|getFeature
argument_list|(
literal|"LS"
argument_list|,
literal|"3.0"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|DOMImplementationRegistry
name|registry
init|=
name|DOMImplementationRegistry
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|impl
operator|=
operator|(
name|DOMImplementationLS
operator|)
name|registry
operator|.
name|getDOMImplementation
argument_list|(
literal|"LS"
argument_list|)
expr_stmt|;
if|if
condition|(
name|impl
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|DOMImplementationRegistry
operator|.
name|PROPERTY
argument_list|,
literal|"com.sun.org.apache.xerces.internal.dom.DOMImplementationSourceImpl"
argument_list|)
expr_stmt|;
name|registry
operator|=
name|DOMImplementationRegistry
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|impl
operator|=
operator|(
name|DOMImplementationLS
operator|)
name|registry
operator|.
name|getDOMImplementation
argument_list|(
literal|"LS"
argument_list|)
expr_stmt|;
block|}
block|}
name|LSOutput
name|output
init|=
name|impl
operator|.
name|createLSOutput
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|byteArrayOutputStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|output
operator|.
name|setByteStream
argument_list|(
name|byteArrayOutputStream
argument_list|)
expr_stmt|;
name|LSSerializer
name|writer
init|=
name|impl
operator|.
name|createLSSerializer
argument_list|()
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|doc
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|byte
index|[]
name|buf
init|=
name|byteArrayOutputStream
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|buf
argument_list|)
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
name|Element
name|ret
init|=
literal|null
decl_stmt|;
name|NodeList
name|nodeList
init|=
name|parent
operator|.
name|getElementsByTagName
argument_list|(
name|targetName
argument_list|)
decl_stmt|;
name|Node
name|node
init|=
literal|null
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
name|nodeList
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|node
operator|=
name|nodeList
operator|.
name|item
argument_list|(
name|i
argument_list|)
expr_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|Element
operator|&&
operator|(
operator|(
name|Element
operator|)
name|node
operator|)
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
name|ret
operator|=
operator|(
name|Element
operator|)
name|node
expr_stmt|;
break|break;
block|}
block|}
return|return
name|ret
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
name|Transformer
name|trans
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|DOMResult
name|res
init|=
operator|new
name|DOMResult
argument_list|()
decl_stmt|;
name|trans
operator|.
name|transform
argument_list|(
name|src
argument_list|,
name|res
argument_list|)
expr_stmt|;
return|return
name|res
operator|.
name|getNode
argument_list|()
return|;
block|}
block|}
end_class

end_unit

