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
name|codegen
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
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|Comparator
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
name|HashSet
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
name|zip
operator|.
name|ZipEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipOutputStream
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
name|UriBuilder
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
name|ext
operator|.
name|RequestHandler
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
name|model
operator|.
name|ClassResourceInfo
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
name|model
operator|.
name|ProviderInfo
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
name|model
operator|.
name|wadl
operator|.
name|WadlGenerator
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
name|ProviderFactory
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
name|JAXRSUtils
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
name|message
operator|.
name|Message
import|;
end_import

begin_class
specifier|public
class|class
name|CodeGeneratorProvider
implements|implements
name|RequestHandler
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CODE_QUERY
init|=
literal|"_code"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LANGUAGE_QUERY
init|=
literal|"_lang"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OS_QUERY
init|=
literal|"_os"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SOURCE_QUERY
init|=
literal|"_source"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CODE_TYPE_QUERY
init|=
literal|"_codeType"
decl_stmt|;
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
name|CodeGeneratorProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|SUPPORTED_LANGUAGES
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"java"
block|}
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TMPDIR
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
decl_stmt|;
specifier|private
name|Comparator
argument_list|<
name|String
argument_list|>
name|importsComparator
decl_stmt|;
specifier|private
name|UriInfo
name|ui
decl_stmt|;
specifier|private
name|boolean
name|generateInterfaces
init|=
literal|true
decl_stmt|;
annotation|@
name|Context
specifier|public
name|void
name|setUriInfo
parameter_list|(
name|UriInfo
name|uriInfo
parameter_list|)
block|{
name|this
operator|.
name|ui
operator|=
name|uriInfo
expr_stmt|;
block|}
specifier|public
name|Response
name|handleRequest
parameter_list|(
name|Message
name|m
parameter_list|,
name|ClassResourceInfo
name|resourceClass
parameter_list|)
block|{
if|if
condition|(
operator|!
literal|"GET"
operator|.
name|equals
argument_list|(
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|ui
operator|.
name|getQueryParameters
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SOURCE_QUERY
argument_list|)
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
return|return
name|getSource
argument_list|(
operator|new
name|File
argument_list|(
name|TMPDIR
argument_list|,
name|getStem
argument_list|(
name|resourceClass
argument_list|,
literal|"zip"
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
name|String
name|codeQuery
init|=
name|ui
operator|.
name|getQueryParameters
argument_list|()
operator|.
name|getFirst
argument_list|(
name|CODE_QUERY
argument_list|)
decl_stmt|;
if|if
condition|(
name|codeQuery
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|language
init|=
name|ui
operator|.
name|getQueryParameters
argument_list|()
operator|.
name|getFirst
argument_list|(
name|LANGUAGE_QUERY
argument_list|)
decl_stmt|;
if|if
condition|(
name|language
operator|!=
literal|null
operator|&&
operator|!
name|SUPPORTED_LANGUAGES
operator|.
name|contains
argument_list|(
name|language
argument_list|)
condition|)
block|{
return|return
name|Response
operator|.
name|noContent
argument_list|()
operator|.
name|entity
argument_list|(
literal|"Unsupported language"
operator|+
name|language
argument_list|)
operator|.
name|type
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
return|return
name|doHandleRequest
argument_list|(
name|m
argument_list|,
name|resourceClass
argument_list|)
return|;
block|}
specifier|protected
name|Response
name|doHandleRequest
parameter_list|(
name|Message
name|m
parameter_list|,
name|ClassResourceInfo
name|resourceClass
parameter_list|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|File
name|zipDir
init|=
operator|new
name|File
argument_list|(
name|TMPDIR
argument_list|,
name|getStem
argument_list|(
name|resourceClass
argument_list|,
literal|"zip"
argument_list|)
argument_list|)
decl_stmt|;
name|Response
name|r
init|=
name|getLink
argument_list|(
name|zipDir
argument_list|,
name|m
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
return|return
name|r
return|;
block|}
name|File
name|srcDir
init|=
operator|new
name|File
argument_list|(
name|TMPDIR
argument_list|,
name|getStem
argument_list|(
name|resourceClass
argument_list|,
literal|"src"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|srcDir
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|srcDir
operator|.
name|mkdir
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to create working directory "
operator|+
name|srcDir
operator|.
name|getPath
argument_list|()
argument_list|)
throw|;
block|}
name|String
name|codeType
init|=
name|ui
operator|.
name|getQueryParameters
argument_list|()
operator|.
name|getFirst
argument_list|(
name|CODE_TYPE_QUERY
argument_list|)
decl_stmt|;
try|try
block|{
name|String
name|wadl
init|=
name|getWadl
argument_list|(
name|m
argument_list|,
name|resourceClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|wadl
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"WADL for "
operator|+
operator|(
name|resourceClass
operator|!=
literal|null
condition|?
name|resourceClass
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getName
argument_list|()
else|:
literal|"this service"
operator|)
operator|+
literal|" can not be loaded"
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|noContent
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
name|getProperties
argument_list|()
decl_stmt|;
name|SourceGenerator
name|sg
init|=
operator|new
name|SourceGenerator
argument_list|(
name|properties
argument_list|)
decl_stmt|;
name|sg
operator|.
name|setGenerateInterfaces
argument_list|(
name|generateInterfaces
argument_list|)
expr_stmt|;
name|sg
operator|.
name|setImportsComparator
argument_list|(
name|importsComparator
argument_list|)
expr_stmt|;
name|sg
operator|.
name|generateSource
argument_list|(
name|wadl
argument_list|,
name|srcDir
argument_list|,
name|codeType
argument_list|)
expr_stmt|;
name|zipSource
argument_list|(
name|srcDir
argument_list|,
name|zipDir
argument_list|)
expr_stmt|;
return|return
name|getLink
argument_list|(
name|zipDir
argument_list|,
name|m
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Code can not be generated for "
operator|+
operator|(
name|resourceClass
operator|!=
literal|null
condition|?
name|resourceClass
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getName
argument_list|()
else|:
literal|"this service"
operator|)
argument_list|,
name|ex
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|removeDir
argument_list|(
name|zipDir
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|noContent
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
finally|finally
block|{
name|FileUtils
operator|.
name|removeDir
argument_list|(
name|srcDir
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getProperties
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
name|HashMap
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
name|SourceGenerator
operator|.
name|LINE_SEP_PROPERTY
argument_list|,
name|getLineSep
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|SourceGenerator
operator|.
name|FILE_SEP_PROPERTY
argument_list|,
name|getFileSep
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
specifier|private
name|void
name|zipSource
parameter_list|(
name|File
name|srcDir
parameter_list|,
name|File
name|zipDir
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|zipDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|zipDir
operator|.
name|mkdir
argument_list|()
expr_stmt|;
block|}
name|File
name|zipFile
init|=
operator|new
name|File
argument_list|(
name|zipDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|"src.zip"
argument_list|)
decl_stmt|;
name|zipFile
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
name|ZipOutputStream
name|zos
init|=
operator|new
name|ZipOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|zipFile
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|File
argument_list|>
name|srcFiles
init|=
name|FileUtils
operator|.
name|getFilesRecurse
argument_list|(
name|srcDir
argument_list|,
literal|".+\\.java$"
argument_list|)
decl_stmt|;
for|for
control|(
name|File
name|f
range|:
name|srcFiles
control|)
block|{
name|String
name|entryName
init|=
name|f
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|substring
argument_list|(
name|srcDir
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
decl_stmt|;
name|zos
operator|.
name|putNextEntry
argument_list|(
operator|new
name|ZipEntry
argument_list|(
name|entryName
argument_list|)
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|f
argument_list|)
argument_list|,
name|zos
argument_list|)
expr_stmt|;
block|}
name|zos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|String
name|getLineSep
parameter_list|()
block|{
name|String
name|os
init|=
name|ui
operator|.
name|getQueryParameters
argument_list|()
operator|.
name|getFirst
argument_list|(
name|OS_QUERY
argument_list|)
decl_stmt|;
if|if
condition|(
name|os
operator|==
literal|null
condition|)
block|{
return|return
name|System
operator|.
name|getProperty
argument_list|(
name|SourceGenerator
operator|.
name|LINE_SEP_PROPERTY
argument_list|)
return|;
block|}
return|return
literal|"unix"
operator|.
name|equals
argument_list|(
name|os
argument_list|)
condition|?
literal|"\r"
else|:
literal|"\r\n"
return|;
block|}
specifier|private
name|String
name|getFileSep
parameter_list|()
block|{
name|String
name|os
init|=
name|ui
operator|.
name|getQueryParameters
argument_list|()
operator|.
name|getFirst
argument_list|(
name|OS_QUERY
argument_list|)
decl_stmt|;
if|if
condition|(
name|os
operator|==
literal|null
condition|)
block|{
return|return
name|System
operator|.
name|getProperty
argument_list|(
name|SourceGenerator
operator|.
name|FILE_SEP_PROPERTY
argument_list|)
return|;
block|}
return|return
literal|"unix"
operator|.
name|equals
argument_list|(
name|os
argument_list|)
condition|?
literal|"/"
else|:
literal|"\\"
return|;
block|}
specifier|private
name|Response
name|getSource
parameter_list|(
name|File
name|zipDir
parameter_list|)
block|{
if|if
condition|(
name|zipDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|File
name|zipFile
init|=
operator|new
name|File
argument_list|(
name|zipDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|"src.zip"
argument_list|)
decl_stmt|;
if|if
condition|(
name|zipFile
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
return|return
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|type
argument_list|(
literal|"application/zip"
argument_list|)
operator|.
name|entity
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|zipFile
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|ex
parameter_list|)
block|{
comment|// should not happen given we've checked it exists
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
block|}
block|}
return|return
name|Response
operator|.
name|noContent
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
specifier|private
name|Response
name|getLink
parameter_list|(
name|File
name|zipDir
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
if|if
condition|(
name|zipDir
operator|.
name|exists
argument_list|()
operator|&&
operator|new
name|File
argument_list|(
name|zipDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|"src.zip"
argument_list|)
operator|.
name|exists
argument_list|()
condition|)
block|{
name|UriBuilder
name|builder
init|=
name|ui
operator|.
name|getAbsolutePathBuilder
argument_list|()
decl_stmt|;
name|String
name|link
init|=
name|builder
operator|.
name|queryParam
argument_list|(
name|SOURCE_QUERY
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// TODO : move it into a resource template
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<html xmlns=\"http://www.w3.org/1999/xhtml\">"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<head><title>Download the source</title></head>"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<body>"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<h1>Link:</h1><br/>"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<ul>"
operator|+
literal|"<a href=\""
operator|+
name|link
operator|+
literal|"\">"
operator|+
name|link
operator|+
literal|"</a>"
operator|+
literal|"</ul>"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"</body>"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"</html>"
argument_list|)
expr_stmt|;
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|JAXRSUtils
operator|.
name|IGNORE_MESSAGE_WRITERS
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|type
argument_list|(
literal|"application/xhtml+xml"
argument_list|)
operator|.
name|entity
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|removeCode
parameter_list|(
name|ClassResourceInfo
name|cri
parameter_list|)
block|{
name|removeCode
argument_list|(
operator|new
name|File
argument_list|(
name|TMPDIR
argument_list|,
name|getStem
argument_list|(
name|cri
argument_list|,
literal|"src"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|removeCode
argument_list|(
operator|new
name|File
argument_list|(
name|TMPDIR
argument_list|,
name|getStem
argument_list|(
name|cri
argument_list|,
literal|"zip"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|getStem
parameter_list|(
name|ClassResourceInfo
name|cri
parameter_list|,
name|String
name|suffix
parameter_list|)
block|{
if|if
condition|(
name|cri
operator|==
literal|null
condition|)
block|{
return|return
literal|"cxf-jaxrs-"
operator|+
name|suffix
return|;
block|}
else|else
block|{
return|return
literal|"cxf-jaxrs-"
operator|+
name|cri
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"-"
operator|+
name|suffix
return|;
block|}
block|}
specifier|private
specifier|static
name|void
name|removeCode
parameter_list|(
name|File
name|src
parameter_list|)
block|{
if|if
condition|(
name|src
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtils
operator|.
name|removeDir
argument_list|(
name|src
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|String
name|getWadl
parameter_list|(
name|Message
name|m
parameter_list|,
name|ClassResourceInfo
name|resourceClass
parameter_list|)
block|{
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|QUERY_STRING
argument_list|,
name|WadlGenerator
operator|.
name|WADL_QUERY
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ProviderInfo
argument_list|<
name|RequestHandler
argument_list|>
argument_list|>
name|shs
init|=
name|ProviderFactory
operator|.
name|getInstance
argument_list|(
name|m
argument_list|)
operator|.
name|getRequestHandlers
argument_list|()
decl_stmt|;
comment|// this is actually being tested by ProviderFactory unit tests but just in case
comment|// WadlGenerator, the custom or default one, must be the first one
if|if
condition|(
name|shs
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|&&
name|shs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProvider
argument_list|()
operator|instanceof
name|WadlGenerator
condition|)
block|{
name|WadlGenerator
name|wg
init|=
operator|(
name|WadlGenerator
operator|)
name|shs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProvider
argument_list|()
decl_stmt|;
name|wg
operator|=
operator|new
name|WadlGenerator
argument_list|(
name|wg
argument_list|)
expr_stmt|;
name|wg
operator|.
name|setAddResourceAndMethodIds
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Response
name|r
init|=
name|wg
operator|.
name|handleRequest
argument_list|(
name|m
argument_list|,
name|resourceClass
argument_list|)
decl_stmt|;
return|return
name|r
operator|==
literal|null
condition|?
literal|null
else|:
operator|(
name|String
operator|)
name|r
operator|.
name|getEntity
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setImportsComparator
parameter_list|(
name|Comparator
argument_list|<
name|String
argument_list|>
name|importsComparator
parameter_list|)
block|{
name|this
operator|.
name|importsComparator
operator|=
name|importsComparator
expr_stmt|;
block|}
specifier|public
name|void
name|setGenerateInterfaces
parameter_list|(
name|boolean
name|generateInterfaces
parameter_list|)
block|{
name|this
operator|.
name|generateInterfaces
operator|=
name|generateInterfaces
expr_stmt|;
block|}
block|}
end_class

end_unit

