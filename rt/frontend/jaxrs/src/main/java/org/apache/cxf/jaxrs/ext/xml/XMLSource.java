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
name|ext
operator|.
name|xml
package|;
end_package

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
name|lang
operator|.
name|reflect
operator|.
name|Array
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|Iterator
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ResourceBundle
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
name|namespace
operator|.
name|NamespaceContext
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
name|xpath
operator|.
name|XPath
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathExpressionException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactory
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
name|i18n
operator|.
name|BundleUtils
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
name|helpers
operator|.
name|CastUtils
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
name|helpers
operator|.
name|IOUtils
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
name|provider
operator|.
name|JAXBElementProvider
import|;
end_import

begin_comment
comment|/**  * Utility class for manipulating XML response using XPath and XSLT  *  */
end_comment

begin_class
specifier|public
class|class
name|XMLSource
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
name|XMLSource
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|XMLSource
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|XML_NAMESPACE
init|=
literal|"http://www.w3.org/XML/1998/namespace"
decl_stmt|;
specifier|private
name|InputStream
name|stream
decl_stmt|;
specifier|private
name|boolean
name|buffering
decl_stmt|;
specifier|public
name|XMLSource
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
name|stream
operator|=
name|is
expr_stmt|;
block|}
specifier|public
name|void
name|setBuffering
parameter_list|(
name|boolean
name|enable
parameter_list|)
block|{
name|buffering
operator|=
name|enable
expr_stmt|;
if|if
condition|(
operator|!
name|stream
operator|.
name|markSupported
argument_list|()
condition|)
block|{
try|try
block|{
name|stream
operator|=
name|IOUtils
operator|.
name|loadIntoBAIS
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"NO_SOURCE_MARK"
argument_list|,
name|BUNDLE
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getNode
parameter_list|(
name|String
name|expression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|getNode
argument_list|(
name|expression
argument_list|,
name|CastUtils
operator|.
name|cast
argument_list|(
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|cls
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getNode
parameter_list|(
name|String
name|expression
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
name|XPath
name|xpath
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newXPath
argument_list|()
decl_stmt|;
name|xpath
operator|.
name|setNamespaceContext
argument_list|(
operator|new
name|NamespaceContextImpl
argument_list|(
name|namespaces
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|Node
name|node
init|=
operator|(
name|Node
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
name|expression
argument_list|,
name|getSource
argument_list|()
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|DOMSource
name|ds
init|=
operator|new
name|DOMSource
argument_list|(
name|node
argument_list|)
decl_stmt|;
return|return
name|readFromSource
argument_list|(
name|ds
argument_list|,
name|cls
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|XPathExpressionException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Illegal XPath expression '"
operator|+
name|expression
operator|+
literal|"'"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|getNodes
parameter_list|(
name|String
name|expression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|getNodes
argument_list|(
name|expression
argument_list|,
name|CastUtils
operator|.
name|cast
argument_list|(
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|cls
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|getNodes
parameter_list|(
name|String
name|expression
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
name|XPath
name|xpath
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newXPath
argument_list|()
decl_stmt|;
name|xpath
operator|.
name|setNamespaceContext
argument_list|(
operator|new
name|NamespaceContextImpl
argument_list|(
name|namespaces
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|NodeList
name|nodes
init|=
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
name|expression
argument_list|,
name|getSource
argument_list|()
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
decl_stmt|;
if|if
condition|(
name|nodes
operator|==
literal|null
operator|||
name|nodes
operator|.
name|getLength
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|T
index|[]
name|values
init|=
operator|(
name|T
index|[]
operator|)
name|Array
operator|.
name|newInstance
argument_list|(
name|cls
argument_list|,
name|nodes
operator|.
name|getLength
argument_list|()
argument_list|)
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
name|nodes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|DOMSource
name|ds
init|=
operator|new
name|DOMSource
argument_list|(
name|nodes
operator|.
name|item
argument_list|(
name|i
argument_list|)
argument_list|)
decl_stmt|;
name|values
index|[
name|i
index|]
operator|=
name|readFromSource
argument_list|(
name|ds
argument_list|,
name|cls
argument_list|)
expr_stmt|;
block|}
return|return
name|values
return|;
block|}
catch|catch
parameter_list|(
name|XPathExpressionException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Illegal XPath expression '"
operator|+
name|expression
operator|+
literal|"'"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|URI
name|getLink
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
name|getLink
argument_list|(
name|expression
argument_list|,
name|CastUtils
operator|.
name|cast
argument_list|(
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|URI
name|getLink
parameter_list|(
name|String
name|expression
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
block|{
name|String
name|value
init|=
name|getValue
argument_list|(
name|expression
argument_list|,
name|namespaces
argument_list|)
decl_stmt|;
return|return
name|value
operator|==
literal|null
condition|?
literal|null
else|:
name|URI
operator|.
name|create
argument_list|(
name|value
argument_list|)
return|;
block|}
specifier|public
name|URI
name|getBaseURI
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"xml"
argument_list|,
name|XML_NAMESPACE
argument_list|)
expr_stmt|;
return|return
name|getLink
argument_list|(
literal|"/*/@xml:base"
argument_list|,
name|map
argument_list|)
return|;
block|}
specifier|public
name|String
name|getValue
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
name|getValue
argument_list|(
name|expression
argument_list|,
name|CastUtils
operator|.
name|cast
argument_list|(
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|String
name|getValue
parameter_list|(
name|String
name|expression
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
block|{
name|XPath
name|xpath
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newXPath
argument_list|()
decl_stmt|;
name|xpath
operator|.
name|setNamespaceContext
argument_list|(
operator|new
name|NamespaceContextImpl
argument_list|(
name|namespaces
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
return|return
operator|(
name|String
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
name|expression
argument_list|,
name|getSource
argument_list|()
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|XPathExpressionException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Illegal XPath expression '"
operator|+
name|expression
operator|+
literal|"'"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
class|class
name|NamespaceContextImpl
implements|implements
name|NamespaceContext
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
decl_stmt|;
specifier|public
name|NamespaceContextImpl
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
block|{
name|this
operator|.
name|namespaces
operator|=
name|namespaces
expr_stmt|;
block|}
specifier|public
name|String
name|getNamespaceURI
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
return|return
name|namespaces
operator|.
name|get
argument_list|(
name|prefix
argument_list|)
return|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|namespaces
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
return|return
name|entry
operator|.
name|getKey
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Iterator
name|getPrefixes
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|String
name|prefix
init|=
name|namespaces
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
decl_stmt|;
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|prefix
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|readFromSource
parameter_list|(
name|Source
name|s
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
try|try
block|{
name|JAXBElementProvider
name|provider
init|=
operator|new
name|JAXBElementProvider
argument_list|()
decl_stmt|;
name|JAXBContext
name|c
init|=
name|provider
operator|.
name|getPackageContext
argument_list|(
name|cls
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
name|c
operator|=
name|provider
operator|.
name|getClassContext
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
name|Unmarshaller
name|u
init|=
name|c
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
return|return
name|cls
operator|.
name|cast
argument_list|(
name|u
operator|.
name|unmarshal
argument_list|(
name|s
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
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
block|}
specifier|private
name|InputSource
name|getSource
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|buffering
condition|)
block|{
name|stream
operator|.
name|reset
argument_list|()
expr_stmt|;
name|stream
operator|.
name|mark
argument_list|(
name|stream
operator|.
name|available
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"NO_SOURCE_MARK"
argument_list|,
name|BUNDLE
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|InputSource
argument_list|(
name|stream
argument_list|)
return|;
block|}
block|}
end_class

end_unit

