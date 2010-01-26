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
name|common
package|;
end_package

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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|net
operator|.
name|URLClassLoader
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
name|Collection
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
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarFile
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
name|helpers
operator|.
name|FileUtils
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
name|util
operator|.
name|ToolsStaxUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|ComparisonFailure
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|TemporaryFolder
import|;
end_import

begin_class
specifier|public
class|class
name|ProcessorTestBase
extends|extends
name|Assert
block|{
specifier|public
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|DEFAULT_IGNORE_ATTR
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"attributeFormDefault"
block|,
literal|"elementFormDefault"
block|,
literal|"form"
block|,
literal|"version"
block|}
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|DEFAULT_IGNORE_TAG
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
comment|//CHECKSTYLE:OFF
annotation|@
name|Rule
specifier|public
name|TemporaryFolder
name|tmpDir
init|=
operator|new
name|TemporaryFolder
argument_list|()
block|{
specifier|protected
name|void
name|before
parameter_list|()
throws|throws
name|Throwable
block|{
name|super
operator|.
name|before
argument_list|()
expr_stmt|;
name|output
operator|=
name|tmpDir
operator|.
name|getRoot
argument_list|()
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|,
name|output
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|//CHECKSTYLE:ON
specifier|protected
name|File
name|output
decl_stmt|;
specifier|protected
name|ToolContext
name|env
init|=
operator|new
name|ToolContext
argument_list|()
decl_stmt|;
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|env
operator|=
literal|null
expr_stmt|;
block|}
specifier|protected
name|String
name|getClassPath
parameter_list|()
throws|throws
name|URISyntaxException
throws|,
name|IOException
block|{
name|ClassLoader
name|loader
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
name|StringBuilder
name|classPath
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|loader
operator|instanceof
name|URLClassLoader
condition|)
block|{
name|URLClassLoader
name|urlLoader
init|=
operator|(
name|URLClassLoader
operator|)
name|loader
decl_stmt|;
for|for
control|(
name|URL
name|url
range|:
name|urlLoader
operator|.
name|getURLs
argument_list|()
control|)
block|{
name|File
name|file
decl_stmt|;
name|file
operator|=
operator|new
name|File
argument_list|(
name|url
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|filename
init|=
name|file
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
if|if
condition|(
name|filename
operator|.
name|indexOf
argument_list|(
literal|"junit"
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
name|classPath
operator|.
name|append
argument_list|(
name|filename
argument_list|)
expr_stmt|;
name|classPath
operator|.
name|append
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"path.separator"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|filename
operator|.
name|indexOf
argument_list|(
literal|"surefirebooter"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
comment|//surefire 2.4 uses a MANIFEST classpath that javac doesn't like
name|JarFile
name|jar
init|=
operator|new
name|JarFile
argument_list|(
name|filename
argument_list|)
decl_stmt|;
name|Attributes
name|attr
init|=
name|jar
operator|.
name|getManifest
argument_list|()
operator|.
name|getMainAttributes
argument_list|()
decl_stmt|;
if|if
condition|(
name|attr
operator|!=
literal|null
condition|)
block|{
name|String
name|cp
init|=
name|attr
operator|.
name|getValue
argument_list|(
literal|"Class-Path"
argument_list|)
decl_stmt|;
while|while
condition|(
name|cp
operator|!=
literal|null
condition|)
block|{
name|String
name|fileName
init|=
name|cp
decl_stmt|;
name|int
name|idx
init|=
name|fileName
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|fileName
operator|=
name|fileName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
name|cp
operator|=
name|cp
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|cp
operator|=
literal|null
expr_stmt|;
block|}
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
name|File
name|f2
init|=
operator|new
name|File
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|f2
operator|.
name|exists
argument_list|()
condition|)
block|{
name|classPath
operator|.
name|append
argument_list|(
name|f2
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|classPath
operator|.
name|append
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"path.separator"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
return|return
name|classPath
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|protected
name|String
name|getLocation
parameter_list|(
name|String
name|wsdlFile
parameter_list|)
throws|throws
name|URISyntaxException
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|wsdlFile
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|protected
name|File
name|getResource
parameter_list|(
name|String
name|wsdlFile
parameter_list|)
throws|throws
name|URISyntaxException
block|{
return|return
operator|new
name|File
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|wsdlFile
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|void
name|assertFileEquals
parameter_list|(
name|String
name|f1
parameter_list|,
name|String
name|f2
parameter_list|)
block|{
name|assertFileEquals
argument_list|(
operator|new
name|File
argument_list|(
name|f1
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|f2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertFileEquals
parameter_list|(
name|File
name|location1
parameter_list|,
name|File
name|location2
parameter_list|)
block|{
name|String
name|str1
init|=
name|FileUtils
operator|.
name|getStringFromFile
argument_list|(
name|location1
argument_list|)
decl_stmt|;
name|String
name|str2
init|=
name|FileUtils
operator|.
name|getStringFromFile
argument_list|(
name|location2
argument_list|)
decl_stmt|;
name|StringTokenizer
name|st1
init|=
operator|new
name|StringTokenizer
argument_list|(
name|str1
argument_list|,
literal|" \t\n\r\f(),"
argument_list|)
decl_stmt|;
name|StringTokenizer
name|st2
init|=
operator|new
name|StringTokenizer
argument_list|(
name|str2
argument_list|,
literal|" \t\n\r\f(),"
argument_list|)
decl_stmt|;
comment|// namespace declarations and wsdl message parts can be ordered
comment|// differently in the generated wsdl between the ibm and sun jdks.
comment|// So, when we encounter a mismatch, put the unmatched token in a
comment|// list and check this list when matching subsequent tokens.
comment|// It would be much better to do a proper xml comparison.
name|List
argument_list|<
name|String
argument_list|>
name|unmatched
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|st1
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|tok1
init|=
name|st1
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|String
name|tok2
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|unmatched
operator|.
name|contains
argument_list|(
name|tok1
argument_list|)
condition|)
block|{
name|unmatched
operator|.
name|remove
argument_list|(
name|tok1
argument_list|)
expr_stmt|;
continue|continue;
block|}
while|while
condition|(
name|st2
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|tok2
operator|=
name|st2
operator|.
name|nextToken
argument_list|()
expr_stmt|;
if|if
condition|(
name|tok1
operator|.
name|equals
argument_list|(
name|tok2
argument_list|)
condition|)
block|{
break|break;
block|}
else|else
block|{
name|unmatched
operator|.
name|add
argument_list|(
name|tok2
argument_list|)
expr_stmt|;
block|}
block|}
name|assertEquals
argument_list|(
literal|"Compare failed "
operator|+
name|location1
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" != "
operator|+
name|location2
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|tok1
argument_list|,
name|tok2
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
operator|!
name|st1
operator|.
name|hasMoreTokens
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|st2
operator|.
name|hasMoreTokens
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Files did not match: "
operator|+
name|unmatched
argument_list|,
name|unmatched
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getStringFromFile
parameter_list|(
name|File
name|location
parameter_list|)
block|{
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
name|String
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
name|is
operator|=
operator|new
name|FileInputStream
argument_list|(
name|location
argument_list|)
expr_stmt|;
name|result
operator|=
name|FileUtils
operator|.
name|normalizeCRLF
argument_list|(
name|is
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
finally|finally
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//do nothing
block|}
block|}
block|}
return|return
name|result
return|;
block|}
specifier|public
name|boolean
name|assertXmlEquals
parameter_list|(
specifier|final
name|File
name|expected
parameter_list|,
specifier|final
name|File
name|source
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|attr
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"attributeFormDefault"
block|,
literal|"elementFormDefault"
block|,
literal|"form"
block|}
argument_list|)
decl_stmt|;
return|return
name|assertXmlEquals
argument_list|(
name|expected
argument_list|,
name|source
argument_list|,
name|attr
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|assertXmlEquals
parameter_list|(
specifier|final
name|File
name|expected
parameter_list|,
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
name|List
argument_list|<
name|Tag
argument_list|>
name|expectedTags
init|=
name|ToolsStaxUtils
operator|.
name|getTags
argument_list|(
name|expected
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Tag
argument_list|>
name|sourceTags
init|=
name|ToolsStaxUtils
operator|.
name|getTags
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|Tag
argument_list|>
name|iterator
init|=
name|sourceTags
operator|.
name|iterator
argument_list|()
decl_stmt|;
for|for
control|(
name|Tag
name|expectedTag
range|:
name|expectedTags
control|)
block|{
name|Tag
name|sourceTag
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|expectedTag
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|sourceTag
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ComparisonFailure
argument_list|(
literal|"Tags not equal: "
argument_list|,
name|expectedTag
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|sourceTag
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|QName
argument_list|,
name|String
argument_list|>
name|attr
range|:
name|expectedTag
operator|.
name|getAttributes
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|ignoreAttr
operator|.
name|contains
argument_list|(
name|attr
operator|.
name|getKey
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|sourceTag
operator|.
name|getAttributes
argument_list|()
operator|.
name|containsKey
argument_list|(
name|attr
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|sourceTag
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|attr
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|equals
argument_list|(
name|attr
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ComparisonFailure
argument_list|(
literal|"Attributes not equal: "
argument_list|,
name|attr
operator|.
name|getKey
argument_list|()
operator|+
literal|":"
operator|+
name|attr
operator|.
name|getValue
argument_list|()
argument_list|,
name|attr
operator|.
name|getKey
argument_list|()
operator|+
literal|":"
operator|+
name|sourceTag
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|attr
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"Attribute: "
operator|+
name|attr
operator|+
literal|" is missing in the source file."
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|expectedTag
operator|.
name|getText
argument_list|()
argument_list|)
operator|&&
operator|!
name|expectedTag
operator|.
name|getText
argument_list|()
operator|.
name|equals
argument_list|(
name|sourceTag
operator|.
name|getText
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ComparisonFailure
argument_list|(
literal|"Text not equal: "
argument_list|,
name|expectedTag
operator|.
name|getText
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|sourceTag
operator|.
name|getText
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|protected
name|void
name|assertTagEquals
parameter_list|(
name|Tag
name|expected
parameter_list|,
name|Tag
name|source
parameter_list|)
block|{
name|assertTagEquals
argument_list|(
name|expected
argument_list|,
name|source
argument_list|,
name|DEFAULT_IGNORE_ATTR
argument_list|,
name|DEFAULT_IGNORE_TAG
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertAttributesEquals
parameter_list|(
name|QName
name|element
parameter_list|,
name|Map
argument_list|<
name|QName
argument_list|,
name|String
argument_list|>
name|q1
parameter_list|,
name|Map
argument_list|<
name|QName
argument_list|,
name|String
argument_list|>
name|q2
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|ignoreAttr
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|QName
argument_list|,
name|String
argument_list|>
name|attr
range|:
name|q1
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|ignoreAttr
operator|.
name|contains
argument_list|(
name|attr
operator|.
name|getKey
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|String
name|found
init|=
name|q2
operator|.
name|get
argument_list|(
name|attr
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|found
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"Attribute: "
operator|+
name|attr
operator|.
name|getKey
argument_list|()
operator|+
literal|" is missing in "
operator|+
name|element
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|found
operator|.
name|equals
argument_list|(
name|attr
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ComparisonFailure
argument_list|(
literal|"Attribute not equal: "
argument_list|,
name|attr
operator|.
name|getKey
argument_list|()
operator|+
literal|":"
operator|+
name|attr
operator|.
name|getValue
argument_list|()
argument_list|,
name|attr
operator|.
name|getKey
argument_list|()
operator|+
literal|":"
operator|+
name|found
argument_list|)
throw|;
block|}
block|}
block|}
specifier|protected
name|void
name|assertTagEquals
parameter_list|(
name|Tag
name|expected
parameter_list|,
name|Tag
name|source
parameter_list|,
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|ignoreAttr
parameter_list|,
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|ignoreTag
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expected
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|source
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ComparisonFailure
argument_list|(
literal|"Tags not equal: "
argument_list|,
name|expected
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|source
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|assertAttributesEquals
argument_list|(
name|expected
operator|.
name|getName
argument_list|()
argument_list|,
name|expected
operator|.
name|getAttributes
argument_list|()
argument_list|,
name|source
operator|.
name|getAttributes
argument_list|()
argument_list|,
name|ignoreAttr
argument_list|)
expr_stmt|;
name|assertAttributesEquals
argument_list|(
name|expected
operator|.
name|getName
argument_list|()
argument_list|,
name|source
operator|.
name|getAttributes
argument_list|()
argument_list|,
name|expected
operator|.
name|getAttributes
argument_list|()
argument_list|,
name|ignoreAttr
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|expected
operator|.
name|getText
argument_list|()
argument_list|)
operator|&&
operator|!
name|expected
operator|.
name|getText
argument_list|()
operator|.
name|equals
argument_list|(
name|source
operator|.
name|getText
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ComparisonFailure
argument_list|(
literal|"Text not equal: "
argument_list|,
name|expected
operator|.
name|getText
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|source
operator|.
name|getText
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|expected
operator|.
name|getTags
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Tag
name|expectedTag
range|:
name|expected
operator|.
name|getTags
argument_list|()
control|)
block|{
if|if
condition|(
name|ignoreTag
operator|.
name|contains
argument_list|(
name|expectedTag
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
name|expectedTag
operator|.
name|getTags
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|Tag
name|sourceTag
init|=
name|getFromSource
argument_list|(
name|source
argument_list|,
name|expectedTag
argument_list|)
decl_stmt|;
if|if
condition|(
name|sourceTag
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"\n"
operator|+
name|expected
operator|.
name|toString
argument_list|()
operator|+
literal|" is missing in the source file"
argument_list|)
throw|;
block|}
name|assertTagEquals
argument_list|(
name|expectedTag
argument_list|,
name|sourceTag
argument_list|,
name|ignoreAttr
argument_list|,
name|ignoreTag
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|Tag
name|getFromSource
parameter_list|(
name|Tag
name|sourceTag
parameter_list|,
name|Tag
name|expectedTag
parameter_list|)
block|{
for|for
control|(
name|Tag
name|tag
range|:
name|sourceTag
operator|.
name|getTags
argument_list|()
control|)
block|{
if|if
condition|(
name|tag
operator|.
name|equals
argument_list|(
name|expectedTag
argument_list|)
condition|)
block|{
return|return
name|tag
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|assertWsdlEquals
parameter_list|(
specifier|final
name|File
name|expected
parameter_list|,
specifier|final
name|File
name|source
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|attr
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|tag
parameter_list|)
throws|throws
name|Exception
block|{
name|Tag
name|expectedTag
init|=
name|ToolsStaxUtils
operator|.
name|getTagTree
argument_list|(
name|expected
argument_list|,
name|attr
argument_list|)
decl_stmt|;
name|Tag
name|sourceTag
init|=
name|ToolsStaxUtils
operator|.
name|getTagTree
argument_list|(
name|source
argument_list|,
name|attr
argument_list|)
decl_stmt|;
name|assertTagEquals
argument_list|(
name|expectedTag
argument_list|,
name|sourceTag
argument_list|,
name|attr
argument_list|,
name|tag
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertWsdlEquals
parameter_list|(
specifier|final
name|File
name|expected
parameter_list|,
specifier|final
name|File
name|source
parameter_list|)
throws|throws
name|Exception
block|{
name|assertWsdlEquals
argument_list|(
name|expected
argument_list|,
name|source
argument_list|,
name|DEFAULT_IGNORE_ATTR
argument_list|,
name|DEFAULT_IGNORE_TAG
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertWsdlEquals
parameter_list|(
specifier|final
name|InputStream
name|expected
parameter_list|,
specifier|final
name|InputStream
name|source
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|attr
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|tag
parameter_list|)
throws|throws
name|Exception
block|{
name|Tag
name|expectedTag
init|=
name|ToolsStaxUtils
operator|.
name|getTagTree
argument_list|(
name|expected
argument_list|,
name|attr
argument_list|)
decl_stmt|;
name|Tag
name|sourceTag
init|=
name|ToolsStaxUtils
operator|.
name|getTagTree
argument_list|(
name|source
argument_list|,
name|attr
argument_list|)
decl_stmt|;
name|assertTagEquals
argument_list|(
name|expectedTag
argument_list|,
name|sourceTag
argument_list|,
name|attr
argument_list|,
name|tag
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertWsdlEquals
parameter_list|(
specifier|final
name|InputStream
name|expected
parameter_list|,
specifier|final
name|InputStream
name|source
parameter_list|)
throws|throws
name|Exception
block|{
name|assertWsdlEquals
argument_list|(
name|expected
argument_list|,
name|source
argument_list|,
name|DEFAULT_IGNORE_ATTR
argument_list|,
name|DEFAULT_IGNORE_TAG
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

