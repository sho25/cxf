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
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
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
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|XMLStreamException
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
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactoryConfigurationException
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
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|jaxb
operator|.
name|JAXBUtils
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
name|interceptor
operator|.
name|Fault
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
name|InjectionUtils
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
name|Document
name|doc
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
comment|/**      * Allows for multiple queries against the same stream by buffering to DOM      */
specifier|public
name|void
name|setBuffering
parameter_list|()
block|{
try|try
block|{
name|doc
operator|=
name|StaxUtils
operator|.
name|read
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|stream
argument_list|)
argument_list|)
expr_stmt|;
name|stream
operator|=
literal|null
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Find the matching XML node and convert it into an instance of the provided class.      * The default JAXB MessageBodyReader is currently used in case of non-primitive types.      *      * @param expression XPath expression      * @param cls class of the node      * @return the instance representing the matching node      */
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
comment|/**      * Find the matching XML node and convert it into an instance of the provided class.      * The default JAXB MessageBodyReader is currently used in case of non-primitive types.      *      * @param expression XPath expression      * @param namespaces the namespaces map, prefixes which are used in the XPath expression      *        are the keys, namespace URIs are the values; note, the prefixes do not have to match      *        the actual ones used in the XML instance.      * @param cls class of the node      * @return the instance representing the matching node      */
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
name|Object
name|obj
init|=
name|evaluate
argument_list|(
name|expression
argument_list|,
name|namespaces
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|obj
operator|instanceof
name|Node
condition|)
block|{
name|Node
name|node
init|=
operator|(
name|Node
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|cls
operator|.
name|isPrimitive
argument_list|()
operator|||
name|cls
operator|==
name|String
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|readPrimitiveValue
argument_list|(
name|node
argument_list|,
name|cls
argument_list|)
return|;
block|}
return|return
name|readNode
argument_list|(
name|node
argument_list|,
name|cls
argument_list|)
return|;
block|}
return|return
name|cls
operator|.
name|cast
argument_list|(
name|evaluate
argument_list|(
name|expression
argument_list|,
name|namespaces
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Find the list of matching XML nodes and convert them into      * an array of instances of the provided class.      * The default JAXB MessageBodyReader is currently used  in case of non-primitive types.      *      * @param expression XPath expression      * @param cls class of the node      * @return the array of instances representing the matching nodes      */
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
comment|/**      * Find the list of matching XML nodes and convert them into      * an array of instances of the provided class.      * The default JAXB MessageBodyReader is currently used  in case of non-primitive types.      *      * @param expression XPath expression      * @param namespaces the namespaces map, prefixes which are used in the XPath expression      *        are the keys, namespace URIs are the values; note, the prefixes do not have to match      *        the actual ones used in the XML instance.      * @param cls class of the node      * @return the array of instances representing the matching nodes      */
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
name|NodeList
name|nodes
init|=
operator|(
name|NodeList
operator|)
name|evaluate
argument_list|(
name|expression
argument_list|,
name|namespaces
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
name|Node
name|node
init|=
name|nodes
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|InjectionUtils
operator|.
name|isPrimitive
argument_list|(
name|cls
argument_list|)
condition|)
block|{
name|values
index|[
name|i
index|]
operator|=
operator|(
name|T
operator|)
name|readPrimitiveValue
argument_list|(
name|node
argument_list|,
name|cls
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|values
index|[
name|i
index|]
operator|=
name|readNode
argument_list|(
name|node
argument_list|,
name|cls
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|values
return|;
block|}
comment|/**      * Find an attribute or text node representing      * an absolute or relative link and convert it to URI      * @param expression the XPath expression      * @return the link      */
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
comment|/**      * Find an attribute or text node representing      * an absolute or relative link and convert it to URI      * @param expression the XPath expression      * @param namespaces the namespaces map, prefixes which are used in the XPath expression      *        are the keys, namespace URIs are the values; note, the prefixes do not have to match      *        the actual ones used in the XML instance.      * @return the link      */
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
comment|/**      * Find attributes or text nodes representing      * absolute or relative links and convert them to URIs      * @param expression the XPath expression      * @param namespaces the namespaces map, prefixes which are used in the XPath expression      *        are the keys, namespace URIs are the values; note, the prefixes do not have to match      *        the actual ones used in the XML instance.      * @return the links      */
specifier|public
name|URI
index|[]
name|getLinks
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
index|[]
name|values
init|=
name|getValues
argument_list|(
name|expression
argument_list|,
name|namespaces
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|URI
index|[]
name|uris
init|=
operator|new
name|URI
index|[
name|values
operator|.
name|length
index|]
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
name|values
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|uris
index|[
name|i
index|]
operator|=
name|URI
operator|.
name|create
argument_list|(
name|values
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|uris
return|;
block|}
comment|/**      * Returns the value of the xml:base attribute, if any.      * This can be used to calculate an absolute URI provided      * the links in the actual XML instance are relative.      *      * @return the xml:base value      */
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
argument_list|<>
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
comment|/**      * Find the attribute or simple/text node      * @param expression the XPath expression      * @return the value of the matching node      */
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
comment|/**      * Find the attribute or simple/text node      * @param expression the XPath expression      * @param namespaces the namespaces map, prefixes which are used in the XPath expression      *        are the keys, namespace URIs are the values; note, the prefixes do not have to match      *        the actual ones used in the XML instance.      * @return the value of the matching node      */
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
return|return
name|getValue
argument_list|(
name|expression
argument_list|,
name|namespaces
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Find the attributes or simple/text nodes      * @param expression the XPath expression      * @return the values of the matching nodes      */
specifier|public
name|String
index|[]
name|getValues
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
name|getValues
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
comment|/**      * Find the attributes or simple/text nodes      * @param expression the XPath expression      * @param namespaces the namespaces map, prefixes which are used in the XPath expression      *        are the keys, namespace URIs are the values; note, the prefixes do not have to match      *        the actual ones used in the XML instance.      * @return the values of the matching nodes      */
specifier|public
name|String
index|[]
name|getValues
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
return|return
name|getNodes
argument_list|(
name|expression
argument_list|,
name|namespaces
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Find the attribute or simple/text node and convert the string value to the      * instance of the provided class, example, Integer.class.      * @param expression the XPath expression      * @param namespaces the namespaces map, prefixes which are used in the XPath expression      *        are the keys, namespace URIs are the values; note, the prefixes do not have to match      *        the actual ones used in the XML instance.      * @param cls the class of the response      * @return the value      */
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
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
name|Object
name|result
init|=
name|evaluate
argument_list|(
name|expression
argument_list|,
name|namespaces
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
decl_stmt|;
return|return
name|result
operator|==
literal|null
condition|?
literal|null
else|:
operator|(
name|T
operator|)
name|InjectionUtils
operator|.
name|convertStringToPrimitive
argument_list|(
name|result
operator|.
name|toString
argument_list|()
argument_list|,
name|cls
argument_list|)
return|;
block|}
specifier|private
name|Object
name|evaluate
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
name|QName
name|type
parameter_list|)
block|{
name|XPathFactory
name|factory
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
try|try
block|{
name|factory
operator|.
name|setFeature
argument_list|(
name|XMLConstants
operator|.
name|FEATURE_SECURE_PROCESSING
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XPathFactoryConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|XPath
name|xpath
init|=
name|factory
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
name|boolean
name|releaseDoc
init|=
literal|false
decl_stmt|;
try|try
block|{
if|if
condition|(
name|stream
operator|!=
literal|null
condition|)
block|{
comment|//xalan xpath evaluate parses to a DOM via a DocumentBuilderFactory, but doesn't
comment|//set the SecureProcessing on that. Since a DOM is always created, might as well
comment|//do it via stax and avoid the service factory performance hits that the
comment|//DocumentBuilderFactory will entail as well as get the extra security
comment|//that woodstox provides
name|setBuffering
argument_list|()
expr_stmt|;
name|releaseDoc
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|xpath
operator|.
name|compile
argument_list|(
name|expression
argument_list|)
operator|.
name|evaluate
argument_list|(
name|doc
argument_list|,
name|type
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
finally|finally
block|{
if|if
condition|(
name|releaseDoc
condition|)
block|{
comment|//don't need to maintain the doc
name|doc
operator|=
literal|null
expr_stmt|;
block|}
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
argument_list|<
name|String
argument_list|>
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
name|Object
name|readPrimitiveValue
parameter_list|(
name|Node
name|node
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|String
operator|.
name|class
operator|==
name|cls
condition|)
block|{
if|if
condition|(
name|node
operator|.
name|getNodeType
argument_list|()
operator|==
name|Node
operator|.
name|ELEMENT_NODE
condition|)
block|{
return|return
name|StaxUtils
operator|.
name|toString
argument_list|(
operator|(
name|Element
operator|)
name|node
argument_list|)
return|;
block|}
return|return
name|cls
operator|.
name|cast
argument_list|(
name|node
operator|.
name|getNodeValue
argument_list|()
argument_list|)
return|;
block|}
return|return
name|InjectionUtils
operator|.
name|convertStringToPrimitive
argument_list|(
name|node
operator|.
name|getNodeValue
argument_list|()
argument_list|,
name|cls
argument_list|)
return|;
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|readNode
parameter_list|(
name|Node
name|node
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|Node
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|cls
argument_list|)
condition|)
block|{
return|return
name|cls
operator|.
name|cast
argument_list|(
name|node
argument_list|)
return|;
block|}
name|DOMSource
name|s
init|=
operator|new
name|DOMSource
argument_list|(
name|node
argument_list|)
decl_stmt|;
if|if
condition|(
name|Source
operator|.
name|class
operator|==
name|cls
operator|||
name|DOMSource
operator|.
name|class
operator|==
name|cls
condition|)
block|{
return|return
name|cls
operator|.
name|cast
argument_list|(
name|s
argument_list|)
return|;
block|}
try|try
block|{
name|JAXBElementProvider
argument_list|<
name|?
argument_list|>
name|provider
init|=
operator|new
name|JAXBElementProvider
argument_list|<>
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
try|try
block|{
if|if
condition|(
name|cls
operator|.
name|getAnnotation
argument_list|(
name|XmlRootElement
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
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
return|return
name|u
operator|.
name|unmarshal
argument_list|(
name|s
argument_list|,
name|cls
argument_list|)
operator|.
name|getValue
argument_list|()
return|;
block|}
finally|finally
block|{
name|JAXBUtils
operator|.
name|closeUnmarshaller
argument_list|(
name|u
argument_list|)
expr_stmt|;
block|}
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
block|}
end_class

end_unit

