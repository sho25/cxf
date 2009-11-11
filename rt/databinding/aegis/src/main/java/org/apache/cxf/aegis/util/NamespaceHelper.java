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
name|aegis
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|StringTokenizer
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
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|DatabindingException
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
name|DOMUtils
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
name|JavaUtils
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
comment|/**  * Namespace utilities.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|NamespaceHelper
block|{
specifier|private
name|NamespaceHelper
parameter_list|()
block|{
comment|//utility class
block|}
comment|/**      * Create a unique namespace uri/prefix combination.      *       * @param nsUri      * @return The namespace with the specified URI. If one doesn't exist, one      *         is created.      */
specifier|public
specifier|static
name|String
name|getUniquePrefix
parameter_list|(
name|Element
name|element
parameter_list|,
name|String
name|namespaceURI
parameter_list|)
block|{
name|String
name|prefix
init|=
name|getPrefix
argument_list|(
name|element
argument_list|,
name|namespaceURI
argument_list|)
decl_stmt|;
comment|// it is OK to have both namespace URI and prefix be empty.
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|namespaceURI
argument_list|)
condition|)
block|{
return|return
literal|""
return|;
block|}
name|prefix
operator|=
name|DOMUtils
operator|.
name|createNamespace
argument_list|(
name|element
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
block|}
return|return
name|prefix
return|;
block|}
specifier|public
specifier|static
name|String
name|getPrefix
parameter_list|(
name|Element
name|element
parameter_list|,
name|String
name|namespaceURI
parameter_list|)
block|{
return|return
name|DOMUtils
operator|.
name|getPrefixRecursive
argument_list|(
name|element
argument_list|,
name|namespaceURI
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|void
name|getPrefixes
parameter_list|(
name|Element
name|element
parameter_list|,
name|String
name|namespaceURI
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|prefixes
parameter_list|)
block|{
name|DOMUtils
operator|.
name|getPrefixesRecursive
argument_list|(
name|element
argument_list|,
name|namespaceURI
argument_list|,
name|prefixes
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a unique namespace uri/prefix combination.      *       * @param nsUri      * @return The namespace with the specified URI. If one doesn't exist, one      *         is created.      * @throws XMLStreamException      */
specifier|public
specifier|static
name|String
name|getUniquePrefix
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|String
name|namespaceURI
parameter_list|,
name|boolean
name|declare
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|getUniquePrefix
argument_list|(
name|writer
argument_list|,
name|namespaceURI
argument_list|,
literal|null
argument_list|,
name|declare
argument_list|)
return|;
block|}
comment|/**      * Make a unique prefix.      * @param writer target writer.      * @param namespaceURI namespace      * @param preferred if there's a proposed prefix (e.g. xsi), here it is.      * @param declare whether to declare to the stream.      * @return the prefix.      * @throws XMLStreamException      */
specifier|public
specifier|static
name|String
name|getUniquePrefix
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|String
name|namespaceURI
parameter_list|,
name|String
name|preferred
parameter_list|,
name|boolean
name|declare
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|preferred
operator|!=
literal|null
condition|)
block|{
name|String
name|existing
init|=
name|writer
operator|.
name|getNamespaceContext
argument_list|()
operator|.
name|getNamespaceURI
argument_list|(
name|preferred
argument_list|)
decl_stmt|;
if|if
condition|(
name|namespaceURI
operator|.
name|equals
argument_list|(
name|existing
argument_list|)
condition|)
block|{
return|return
name|preferred
return|;
block|}
block|}
name|String
name|prefix
init|=
name|preferred
decl_stmt|;
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
name|prefix
operator|=
name|writer
operator|.
name|getNamespaceContext
argument_list|()
operator|.
name|getPrefix
argument_list|(
name|namespaceURI
argument_list|)
expr_stmt|;
if|if
condition|(
name|prefix
operator|!=
literal|null
condition|)
block|{
name|declare
operator|=
literal|false
expr_stmt|;
block|}
block|}
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
name|prefix
operator|=
name|StaxUtils
operator|.
name|getUniquePrefix
argument_list|(
name|writer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|declare
condition|)
block|{
name|writer
operator|.
name|setPrefix
argument_list|(
name|prefix
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeNamespace
argument_list|(
name|prefix
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
block|}
return|return
name|prefix
return|;
block|}
comment|/**      * Generates the name of a XML namespace from a given class name and      * protocol. The returned namespace will take the form      *<code>protocol://domain</code>, where<code>protocol</code> is the      * given protocol, and<code>domain</code> the inversed package name of      * the given class name.<p/> For instance, if the given class name is      *<code>org.codehaus.xfire.services.Echo</code>, and the protocol is      *<code>http</code>, the resulting namespace would be      *<code>http://services.xfire.codehaus.org</code>.      *       * @param className the class name      * @param protocol the protocol (eg.<code>http</code>)      * @return the namespace      */
specifier|public
specifier|static
name|String
name|makeNamespaceFromClassName
parameter_list|(
name|String
name|className
parameter_list|,
name|String
name|protocol
parameter_list|)
block|{
name|int
name|index
init|=
name|className
operator|.
name|lastIndexOf
argument_list|(
literal|"."
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
name|protocol
operator|+
literal|"://"
operator|+
literal|"DefaultNamespace"
return|;
block|}
name|String
name|packageName
init|=
name|className
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
decl_stmt|;
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|packageName
argument_list|,
literal|"."
argument_list|)
decl_stmt|;
name|String
index|[]
name|words
init|=
operator|new
name|String
index|[
name|st
operator|.
name|countTokens
argument_list|()
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
name|words
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
name|words
index|[
name|i
index|]
operator|=
name|st
operator|.
name|nextToken
argument_list|()
expr_stmt|;
block|}
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|(
literal|80
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|words
operator|.
name|length
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
operator|--
name|i
control|)
block|{
name|String
name|word
init|=
name|words
index|[
name|i
index|]
decl_stmt|;
comment|// seperate with dot
if|if
condition|(
name|i
operator|!=
name|words
operator|.
name|length
operator|-
literal|1
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|word
argument_list|)
expr_stmt|;
block|}
return|return
name|protocol
operator|+
literal|"://"
operator|+
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Method makePackageName      *       * @param namespace      * @return      */
specifier|public
specifier|static
name|String
name|makePackageName
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|String
name|hostname
init|=
literal|null
decl_stmt|;
name|String
name|path
init|=
literal|""
decl_stmt|;
comment|// get the target namespace of the document
try|try
block|{
name|URL
name|u
init|=
operator|new
name|URL
argument_list|(
name|namespace
argument_list|)
decl_stmt|;
name|hostname
operator|=
name|u
operator|.
name|getHost
argument_list|()
expr_stmt|;
name|path
operator|=
name|u
operator|.
name|getPath
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
if|if
condition|(
name|namespace
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
operator|>
operator|-
literal|1
condition|)
block|{
name|hostname
operator|=
name|namespace
operator|.
name|substring
argument_list|(
name|namespace
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|hostname
operator|.
name|indexOf
argument_list|(
literal|"/"
argument_list|)
operator|>
operator|-
literal|1
condition|)
block|{
name|hostname
operator|=
name|hostname
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|hostname
operator|.
name|indexOf
argument_list|(
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|hostname
operator|=
name|namespace
expr_stmt|;
block|}
block|}
comment|// if we didn't file a hostname, bail
if|if
condition|(
name|hostname
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// convert illegal java identifier
name|hostname
operator|=
name|hostname
operator|.
name|replace
argument_list|(
literal|'-'
argument_list|,
literal|'_'
argument_list|)
expr_stmt|;
name|path
operator|=
name|path
operator|.
name|replace
argument_list|(
literal|'-'
argument_list|,
literal|'_'
argument_list|)
expr_stmt|;
comment|// chomp off last forward slash in path, if necessary
if|if
condition|(
operator|(
name|path
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|)
operator|&&
operator|(
name|path
operator|.
name|charAt
argument_list|(
name|path
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|==
literal|'/'
operator|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|path
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// tokenize the hostname and reverse it
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|hostname
argument_list|,
literal|".:"
argument_list|)
decl_stmt|;
name|String
index|[]
name|words
init|=
operator|new
name|String
index|[
name|st
operator|.
name|countTokens
argument_list|()
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
name|words
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
name|words
index|[
name|i
index|]
operator|=
name|st
operator|.
name|nextToken
argument_list|()
expr_stmt|;
block|}
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|(
name|namespace
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|words
operator|.
name|length
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
operator|--
name|i
control|)
block|{
name|addWordToPackageBuffer
argument_list|(
name|sb
argument_list|,
name|words
index|[
name|i
index|]
argument_list|,
name|i
operator|==
name|words
operator|.
name|length
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// tokenize the path
name|StringTokenizer
name|st2
init|=
operator|new
name|StringTokenizer
argument_list|(
name|path
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
while|while
condition|(
name|st2
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|addWordToPackageBuffer
argument_list|(
name|sb
argument_list|,
name|st2
operator|.
name|nextToken
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Massage<tt>word</tt> into a form suitable for use in a Java package      * name. Append it to the target string buffer with a<tt>.</tt> delimiter      * iff<tt>word</tt> is not the first word in the package name.      *       * @param sb the buffer to append to      * @param word the word to append      * @param firstWord a flag indicating whether this is the first word      */
specifier|private
specifier|static
name|void
name|addWordToPackageBuffer
parameter_list|(
name|StringBuffer
name|sb
parameter_list|,
name|String
name|word
parameter_list|,
name|boolean
name|firstWord
parameter_list|)
block|{
if|if
condition|(
name|JavaUtils
operator|.
name|isJavaKeyword
argument_list|(
name|word
argument_list|)
condition|)
block|{
name|word
operator|=
name|JavaUtils
operator|.
name|makeNonJavaKeyword
argument_list|(
name|word
argument_list|)
expr_stmt|;
block|}
comment|// separate with dot after the first word
if|if
condition|(
operator|!
name|firstWord
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
comment|// prefix digits with underscores
if|if
condition|(
name|Character
operator|.
name|isDigit
argument_list|(
name|word
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'_'
argument_list|)
expr_stmt|;
block|}
comment|// replace periods with underscores
if|if
condition|(
name|word
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|char
index|[]
name|buf
init|=
name|word
operator|.
name|toCharArray
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
name|word
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|buf
index|[
name|i
index|]
operator|==
literal|'.'
condition|)
block|{
name|buf
index|[
name|i
index|]
operator|=
literal|'_'
expr_stmt|;
block|}
block|}
name|word
operator|=
operator|new
name|String
argument_list|(
name|buf
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|word
argument_list|)
expr_stmt|;
block|}
comment|/**      * Reads a QName from the element text. Reader must be positioned at the      * start tag.      *       * @param reader      * @return      * @throws XMLStreamException      */
specifier|public
specifier|static
name|QName
name|readQName
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|String
name|value
init|=
name|reader
operator|.
name|getElementText
argument_list|()
decl_stmt|;
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
name|reader
operator|.
name|getNamespaceURI
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
name|DatabindingException
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
name|QName
name|createQName
parameter_list|(
name|NamespaceContext
name|nc
parameter_list|,
name|String
name|value
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
literal|':'
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
name|nc
operator|.
name|getNamespaceURI
argument_list|(
literal|""
argument_list|)
argument_list|,
name|value
argument_list|,
literal|""
argument_list|)
return|;
block|}
else|else
block|{
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
name|nc
operator|.
name|getNamespaceURI
argument_list|(
name|prefix
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
block|}
specifier|public
specifier|static
name|QName
name|createQName
parameter_list|(
name|Element
name|e
parameter_list|,
name|String
name|value
parameter_list|,
name|String
name|defaultNamespace
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
name|defaultNamespace
argument_list|,
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
name|jNS
init|=
name|DOMUtils
operator|.
name|getNamespace
argument_list|(
name|e
argument_list|,
name|prefix
argument_list|)
decl_stmt|;
if|if
condition|(
name|jNS
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"No namespace was found for prefix: "
operator|+
name|prefix
argument_list|)
throw|;
block|}
if|if
condition|(
name|jNS
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
name|DatabindingException
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
name|jNS
argument_list|,
name|localName
argument_list|,
name|prefix
argument_list|)
return|;
block|}
block|}
end_class

end_unit

