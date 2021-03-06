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
name|resource
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
name|FileNotFoundException
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
name|HttpURLConnection
import|;
end_import

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
name|URLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
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
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
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
name|Map
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|CXFPermissions
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|Base64Utility
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
name|common
operator|.
name|util
operator|.
name|SystemPropertyAction
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
name|helpers
operator|.
name|LoadingByteArrayOutputStream
import|;
end_import

begin_comment
comment|/**  * Resolves a File, classpath resource, or URL according to the follow rules:  *<ul>  *<li>Check to see if a file exists, relative to the base URI.</li>  *<li>If the file doesn't exist, check the classpath</li>  *<li>If the classpath doesn't exist, try to create URL from the URI.</li>  *</ul>  */
end_comment

begin_class
specifier|public
class|class
name|URIResolver
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|URIResolver
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|LoadingByteArrayOutputStream
argument_list|>
name|cache
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|File
name|file
decl_stmt|;
specifier|private
name|URI
name|uri
decl_stmt|;
specifier|private
name|URL
name|url
decl_stmt|;
specifier|private
name|InputStream
name|is
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|calling
decl_stmt|;
specifier|public
name|URIResolver
parameter_list|()
block|{     }
specifier|public
name|URIResolver
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
literal|""
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
specifier|public
name|URIResolver
parameter_list|(
name|String
name|baseUriStr
parameter_list|,
name|String
name|uriStr
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|baseUriStr
argument_list|,
name|uriStr
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|URIResolver
parameter_list|(
name|String
name|baseUriStr
parameter_list|,
name|String
name|uriStr
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|calling
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|calling
operator|=
operator|(
name|calling
operator|!=
literal|null
operator|)
condition|?
name|calling
else|:
name|getClass
argument_list|()
expr_stmt|;
if|if
condition|(
name|uriStr
operator|.
name|startsWith
argument_list|(
literal|"classpath:"
argument_list|)
condition|)
block|{
name|tryClasspath
argument_list|(
name|uriStr
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|baseUriStr
operator|!=
literal|null
operator|&&
operator|(
name|baseUriStr
operator|.
name|startsWith
argument_list|(
literal|"jar:"
argument_list|)
operator|||
name|baseUriStr
operator|.
name|startsWith
argument_list|(
literal|"zip:"
argument_list|)
operator|||
name|baseUriStr
operator|.
name|startsWith
argument_list|(
literal|"wsjar:"
argument_list|)
operator|)
operator|&&
operator|!
name|isAbsolute
argument_list|(
name|uriStr
argument_list|)
condition|)
block|{
name|tryArchive
argument_list|(
name|baseUriStr
argument_list|,
name|uriStr
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|uriStr
operator|.
name|startsWith
argument_list|(
literal|"jar:"
argument_list|)
operator|||
name|uriStr
operator|.
name|startsWith
argument_list|(
literal|"zip:"
argument_list|)
operator|||
name|uriStr
operator|.
name|startsWith
argument_list|(
literal|"wsjar:"
argument_list|)
condition|)
block|{
name|tryArchive
argument_list|(
name|uriStr
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tryFileSystem
argument_list|(
name|baseUriStr
argument_list|,
name|uriStr
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|unresolve
parameter_list|()
block|{
name|this
operator|.
name|file
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|uri
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|is
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|void
name|resolve
parameter_list|(
name|String
name|baseUriStr
parameter_list|,
name|String
name|uriStr
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|callingCls
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|calling
operator|=
operator|(
name|callingCls
operator|!=
literal|null
operator|)
condition|?
name|callingCls
else|:
name|getClass
argument_list|()
expr_stmt|;
name|this
operator|.
name|file
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|uri
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|is
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|uriStr
operator|.
name|startsWith
argument_list|(
literal|"classpath:"
argument_list|)
condition|)
block|{
name|tryClasspath
argument_list|(
name|uriStr
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|baseUriStr
operator|!=
literal|null
operator|&&
operator|(
name|baseUriStr
operator|.
name|startsWith
argument_list|(
literal|"jar:"
argument_list|)
operator|||
name|baseUriStr
operator|.
name|startsWith
argument_list|(
literal|"zip:"
argument_list|)
operator|||
name|baseUriStr
operator|.
name|startsWith
argument_list|(
literal|"wsjar:"
argument_list|)
operator|)
operator|&&
operator|!
name|isAbsolute
argument_list|(
name|uriStr
argument_list|)
condition|)
block|{
name|tryArchive
argument_list|(
name|baseUriStr
argument_list|,
name|uriStr
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|uriStr
operator|.
name|startsWith
argument_list|(
literal|"jar:"
argument_list|)
operator|||
name|uriStr
operator|.
name|startsWith
argument_list|(
literal|"zip:"
argument_list|)
operator|||
name|uriStr
operator|.
name|startsWith
argument_list|(
literal|"wsjar:"
argument_list|)
condition|)
block|{
name|tryArchive
argument_list|(
name|uriStr
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tryFileSystem
argument_list|(
name|baseUriStr
argument_list|,
name|uriStr
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|isAbsolute
parameter_list|(
name|String
name|uriStr
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|URI
argument_list|(
name|uriStr
argument_list|)
operator|.
name|isAbsolute
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|private
name|void
name|tryFileSystem
parameter_list|(
name|String
name|baseUriStr
parameter_list|,
name|String
name|uriStr
parameter_list|)
throws|throws
name|IOException
throws|,
name|MalformedURLException
block|{
comment|// It is possible that spaces have been encoded.  We should decode them first.
name|String
name|fileStr
init|=
name|uriStr
operator|.
name|replace
argument_list|(
literal|"%20"
argument_list|,
literal|" "
argument_list|)
decl_stmt|;
try|try
block|{
specifier|final
name|File
name|uriFileTemp
init|=
operator|new
name|File
argument_list|(
name|fileStr
argument_list|)
decl_stmt|;
name|File
name|uriFile
init|=
operator|new
name|File
argument_list|(
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|run
parameter_list|()
block|{
return|return
name|uriFileTemp
operator|.
name|getAbsolutePath
argument_list|()
return|;
block|}
block|}
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|SecurityActions
operator|.
name|fileExists
argument_list|(
name|uriFile
argument_list|,
name|CXFPermissions
operator|.
name|RESOLVE_URI
argument_list|)
condition|)
block|{
try|try
block|{
name|URI
name|urif
init|=
operator|new
name|URI
argument_list|(
name|URLDecoder
operator|.
name|decode
argument_list|(
name|uriStr
argument_list|,
literal|"ASCII"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"file"
operator|.
name|equals
argument_list|(
name|urif
operator|.
name|getScheme
argument_list|()
argument_list|)
operator|&&
name|urif
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|File
name|f2
init|=
operator|new
name|File
argument_list|(
name|urif
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
name|uriFile
operator|=
name|f2
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
block|}
specifier|final
name|URI
name|relative
decl_stmt|;
if|if
condition|(
operator|!
name|SecurityActions
operator|.
name|fileExists
argument_list|(
name|uriFile
argument_list|,
name|CXFPermissions
operator|.
name|RESOLVE_URI
argument_list|)
condition|)
block|{
name|relative
operator|=
operator|new
name|URI
argument_list|(
name|uriStr
operator|.
name|replace
argument_list|(
literal|" "
argument_list|,
literal|"%20"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|relative
operator|=
name|uriFile
operator|.
name|getAbsoluteFile
argument_list|()
operator|.
name|toURI
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|relative
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|uri
operator|=
name|relative
expr_stmt|;
name|url
operator|=
name|relative
operator|.
name|toURL
argument_list|()
expr_stmt|;
try|try
block|{
name|HttpURLConnection
name|huc
init|=
name|createInputStream
argument_list|()
decl_stmt|;
name|int
name|status
init|=
name|huc
operator|.
name|getResponseCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|status
operator|!=
name|HttpURLConnection
operator|.
name|HTTP_OK
operator|&&
name|followRedirect
argument_list|(
name|status
argument_list|)
condition|)
block|{
comment|// only redirect once.
name|uri
operator|=
operator|new
name|URI
argument_list|(
name|huc
operator|.
name|getHeaderField
argument_list|(
literal|"Location"
argument_list|)
argument_list|)
expr_stmt|;
name|url
operator|=
name|uri
operator|.
name|toURL
argument_list|()
expr_stmt|;
name|createInputStream
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|ex
parameter_list|)
block|{
name|is
operator|=
name|url
operator|.
name|openStream
argument_list|()
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|baseUriStr
argument_list|)
condition|)
block|{
name|URI
name|base
decl_stmt|;
name|File
name|baseFile
init|=
operator|new
name|File
argument_list|(
name|baseUriStr
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|baseFile
operator|.
name|exists
argument_list|()
operator|&&
name|baseUriStr
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
condition|)
block|{
name|baseFile
operator|=
operator|new
name|File
argument_list|(
name|getFilePathFromUri
argument_list|(
name|baseUriStr
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|baseFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|base
operator|=
name|baseFile
operator|.
name|toURI
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|base
operator|=
operator|new
name|URI
argument_list|(
name|baseUriStr
argument_list|)
expr_stmt|;
block|}
name|base
operator|=
name|base
operator|.
name|resolve
argument_list|(
name|relative
argument_list|)
expr_stmt|;
if|if
condition|(
name|base
operator|.
name|isAbsolute
argument_list|()
operator|&&
literal|"file"
operator|.
name|equalsIgnoreCase
argument_list|(
name|base
operator|.
name|getScheme
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
comment|// decode space before create a file
name|baseFile
operator|=
operator|new
name|File
argument_list|(
name|base
operator|.
name|getPath
argument_list|()
operator|.
name|replace
argument_list|(
literal|"%20"
argument_list|,
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|baseFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|is
operator|=
name|base
operator|.
name|toURL
argument_list|()
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|uri
operator|=
name|base
expr_stmt|;
block|}
else|else
block|{
name|tryClasspath
argument_list|(
name|base
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
condition|?
name|base
operator|.
name|toString
argument_list|()
operator|.
name|substring
argument_list|(
literal|5
argument_list|)
else|:
name|base
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|tryClasspath
argument_list|(
name|base
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
condition|?
name|base
operator|.
name|toString
argument_list|()
operator|.
name|substring
argument_list|(
literal|5
argument_list|)
else|:
name|base
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|tryClasspath
argument_list|(
name|base
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
condition|?
name|base
operator|.
name|toString
argument_list|()
operator|.
name|substring
argument_list|(
literal|5
argument_list|)
else|:
name|base
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|tryClasspath
argument_list|(
name|fileStr
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
condition|?
name|fileStr
operator|.
name|substring
argument_list|(
literal|5
argument_list|)
else|:
name|fileStr
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|// do nothing
block|}
if|if
condition|(
name|is
operator|==
literal|null
operator|&&
name|baseUriStr
operator|!=
literal|null
operator|&&
name|baseUriStr
operator|.
name|startsWith
argument_list|(
literal|"classpath:"
argument_list|)
condition|)
block|{
name|tryClasspath
argument_list|(
name|baseUriStr
operator|+
name|fileStr
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|is
operator|==
literal|null
operator|&&
name|uri
operator|!=
literal|null
operator|&&
literal|"file"
operator|.
name|equals
argument_list|(
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
name|file
operator|=
operator|new
name|File
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|iae
parameter_list|)
block|{
name|file
operator|=
operator|new
name|File
argument_list|(
name|uri
operator|.
name|toURL
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|file
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|is
operator|==
literal|null
operator|&&
name|file
operator|!=
literal|null
operator|&&
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|uri
operator|=
name|file
operator|.
name|toURI
argument_list|()
expr_stmt|;
try|try
block|{
name|is
operator|=
name|Files
operator|.
name|newInputStream
argument_list|(
name|file
operator|.
name|toPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"File was deleted! "
operator|+
name|fileStr
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|url
operator|=
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
name|tryClasspath
argument_list|(
name|fileStr
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|boolean
name|followRedirect
parameter_list|(
name|int
name|status
parameter_list|)
block|{
return|return
operator|(
name|status
operator|==
name|HttpURLConnection
operator|.
name|HTTP_MOVED_TEMP
operator|||
name|status
operator|==
name|HttpURLConnection
operator|.
name|HTTP_MOVED_PERM
operator|||
name|status
operator|==
name|HttpURLConnection
operator|.
name|HTTP_SEE_OTHER
operator|)
operator|&&
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
literal|"http.autoredirect"
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|HttpURLConnection
name|createInputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|HttpURLConnection
name|huc
init|=
operator|(
name|HttpURLConnection
operator|)
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|String
name|host
init|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
literal|"http.proxyHost"
argument_list|)
decl_stmt|;
if|if
condition|(
name|host
operator|!=
literal|null
condition|)
block|{
comment|//comment out unused port to pass pmd check
comment|/*String ports = SystemPropertyAction.getProperty("http.proxyPort");             int port = 80;             if (ports != null) {                 port = Integer.parseInt(ports);             }*/
name|String
name|username
init|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
literal|"http.proxy.user"
argument_list|)
decl_stmt|;
name|String
name|password
init|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
literal|"http.proxy.password"
argument_list|)
decl_stmt|;
if|if
condition|(
name|username
operator|!=
literal|null
operator|&&
name|password
operator|!=
literal|null
condition|)
block|{
name|String
name|encoded
init|=
name|Base64Utility
operator|.
name|encode
argument_list|(
operator|(
name|username
operator|+
literal|":"
operator|+
name|password
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|huc
operator|.
name|setRequestProperty
argument_list|(
literal|"Proxy-Authorization"
argument_list|,
literal|"Basic "
operator|+
name|encoded
argument_list|)
expr_stmt|;
block|}
block|}
name|huc
operator|.
name|setConnectTimeout
argument_list|(
literal|30000
argument_list|)
expr_stmt|;
name|huc
operator|.
name|setReadTimeout
argument_list|(
literal|60000
argument_list|)
expr_stmt|;
name|is
operator|=
name|huc
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
return|return
name|huc
return|;
block|}
comment|/**      * Assumption: URI scheme is "file"      */
specifier|private
name|String
name|getFilePathFromUri
parameter_list|(
name|String
name|uriString
parameter_list|)
block|{
name|String
name|path
init|=
literal|null
decl_stmt|;
try|try
block|{
name|path
operator|=
operator|new
name|URL
argument_list|(
name|uriString
argument_list|)
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
comment|// ignore
block|}
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|uriString
operator|.
name|startsWith
argument_list|(
literal|"file:/"
argument_list|)
condition|)
block|{
name|path
operator|=
name|uriString
operator|.
name|substring
argument_list|(
literal|6
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|uriString
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
condition|)
block|{
comment|// handle Windows file URI such as "file:C:/foo/bar"
name|path
operator|=
name|uriString
operator|.
name|substring
argument_list|(
literal|5
argument_list|)
expr_stmt|;
block|}
block|}
comment|// decode spaces before returning otherwise File.exists returns false
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
return|return
name|path
operator|.
name|replace
argument_list|(
literal|"%20"
argument_list|,
literal|" "
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|tryArchive
parameter_list|(
name|String
name|baseStr
parameter_list|,
name|String
name|uriStr
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|i
init|=
name|baseStr
operator|.
name|indexOf
argument_list|(
literal|'!'
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|==
operator|-
literal|1
condition|)
block|{
name|tryFileSystem
argument_list|(
name|baseStr
argument_list|,
name|uriStr
argument_list|)
expr_stmt|;
block|}
name|String
name|archiveBase
init|=
name|baseStr
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i
operator|+
literal|1
argument_list|)
decl_stmt|;
name|String
name|archiveEntry
init|=
name|baseStr
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|)
decl_stmt|;
try|try
block|{
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|archiveEntry
argument_list|)
operator|.
name|resolve
argument_list|(
name|uriStr
argument_list|)
decl_stmt|;
name|tryArchive
argument_list|(
name|archiveBase
operator|+
name|u
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|u
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|url
operator|=
name|u
operator|.
name|toURL
argument_list|()
expr_stmt|;
block|}
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|// do nothing
block|}
name|tryFileSystem
argument_list|(
literal|""
argument_list|,
name|uriStr
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|tryArchive
parameter_list|(
name|String
name|uriStr
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|i
init|=
name|uriStr
operator|.
name|indexOf
argument_list|(
literal|'!'
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|==
operator|-
literal|1
condition|)
block|{
return|return;
block|}
name|url
operator|=
operator|new
name|URL
argument_list|(
name|uriStr
argument_list|)
expr_stmt|;
try|try
block|{
name|is
operator|=
name|url
operator|.
name|openStream
argument_list|()
expr_stmt|;
try|try
block|{
name|uri
operator|=
name|url
operator|.
name|toURI
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|uriStr
operator|=
name|uriStr
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
name|tryClasspath
argument_list|(
name|uriStr
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|tryClasspath
parameter_list|(
name|String
name|uriStr
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|isClasspathURL
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|uriStr
operator|.
name|startsWith
argument_list|(
literal|"classpath:"
argument_list|)
condition|)
block|{
name|uriStr
operator|=
name|uriStr
operator|.
name|substring
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|isClasspathURL
operator|=
literal|true
expr_stmt|;
block|}
name|url
operator|=
name|ClassLoaderUtils
operator|.
name|getResource
argument_list|(
name|uriStr
argument_list|,
name|calling
argument_list|)
expr_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|tryRemote
argument_list|(
name|uriStr
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|uri
operator|=
name|url
operator|.
name|toURI
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|// yep, some versions of the JDK can't handle spaces when URL.toURI() is called,
comment|// and lots of people on windows have their maven repositories at
comment|// C:/Documents and Settings/<userid>/.m2/repository
comment|// re: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6506304
if|if
condition|(
name|url
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|" "
argument_list|)
condition|)
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
name|url
operator|.
name|toString
argument_list|()
operator|.
name|replace
argument_list|(
literal|" "
argument_list|,
literal|"%20"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|//let's try this again
try|try
block|{
name|uri
operator|=
name|url
operator|.
name|toURI
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e1
parameter_list|)
block|{
comment|// processing the jar:file:/ type value
name|String
name|urlStr
init|=
name|url
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|urlStr
operator|.
name|startsWith
argument_list|(
literal|"jar:"
argument_list|)
operator|||
name|urlStr
operator|.
name|startsWith
argument_list|(
literal|"zip:"
argument_list|)
operator|||
name|urlStr
operator|.
name|startsWith
argument_list|(
literal|"wsjar:"
argument_list|)
condition|)
block|{
name|int
name|pos
init|=
name|urlStr
operator|.
name|indexOf
argument_list|(
literal|'!'
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|!=
operator|-
literal|1
condition|)
block|{
try|try
block|{
name|uri
operator|=
operator|new
name|URI
argument_list|(
literal|"classpath:"
operator|+
name|urlStr
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|ue
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
block|}
block|}
name|is
operator|=
name|url
operator|.
name|openStream
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|is
operator|==
literal|null
operator|&&
name|isClasspathURL
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"NOT_ON_CLASSPATH"
argument_list|,
name|uriStr
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|tryRemote
parameter_list|(
name|String
name|uriStr
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|LoadingByteArrayOutputStream
name|bout
init|=
name|cache
operator|.
name|get
argument_list|(
name|uriStr
argument_list|)
decl_stmt|;
name|url
operator|=
operator|new
name|URL
argument_list|(
name|uriStr
argument_list|)
expr_stmt|;
name|uri
operator|=
operator|new
name|URI
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|bout
operator|==
literal|null
condition|)
block|{
name|URLConnection
name|connection
init|=
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|is
operator|=
name|connection
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
name|bout
operator|=
operator|new
name|LoadingByteArrayOutputStream
argument_list|(
literal|1024
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|bout
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|uriStr
argument_list|,
name|bout
argument_list|)
expr_stmt|;
block|}
name|is
operator|=
name|bout
operator|.
name|createInputStream
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
decl||
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|// do nothing
block|}
block|}
specifier|public
name|URI
name|getURI
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
specifier|public
name|URL
name|getURL
parameter_list|()
block|{
return|return
name|url
return|;
block|}
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
block|{
return|return
name|is
return|;
block|}
specifier|public
name|boolean
name|isFile
parameter_list|()
block|{
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
return|return
name|file
operator|.
name|exists
argument_list|()
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|File
name|getFile
parameter_list|()
block|{
return|return
name|file
return|;
block|}
specifier|public
name|boolean
name|isResolved
parameter_list|()
block|{
return|return
name|is
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

