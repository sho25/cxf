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
name|wsdl11
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
name|IOException
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
name|javax
operator|.
name|wsdl
operator|.
name|xml
operator|.
name|WSDLLocator
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
name|resource
operator|.
name|URIResolver
import|;
end_import

begin_class
specifier|public
class|class
name|WSDLResolver
implements|implements
name|WSDLLocator
block|{
specifier|private
name|String
name|baseUri
decl_stmt|;
specifier|private
name|String
name|importedUri
decl_stmt|;
specifier|private
name|InputSource
name|inputSource
decl_stmt|;
specifier|public
name|WSDLResolver
parameter_list|(
name|String
name|baseURI
parameter_list|,
name|InputSource
name|is
parameter_list|)
block|{
name|this
operator|.
name|baseUri
operator|=
name|baseURI
expr_stmt|;
name|inputSource
operator|=
name|is
expr_stmt|;
block|}
specifier|public
name|InputSource
name|getBaseInputSource
parameter_list|()
block|{
return|return
name|inputSource
return|;
block|}
specifier|public
name|String
name|getBaseURI
parameter_list|()
block|{
return|return
name|baseUri
return|;
block|}
specifier|public
name|String
name|getLatestImportURI
parameter_list|()
block|{
return|return
name|importedUri
return|;
block|}
specifier|public
name|InputSource
name|getImportInputSource
parameter_list|(
name|String
name|parent
parameter_list|,
name|String
name|importLocation
parameter_list|)
block|{
name|this
operator|.
name|baseUri
operator|=
name|parent
expr_stmt|;
name|URL
name|parentUrl
decl_stmt|;
try|try
block|{
name|parentUrl
operator|=
operator|new
name|URL
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|URL
name|importUrl
init|=
operator|new
name|URL
argument_list|(
name|parentUrl
argument_list|,
name|importLocation
argument_list|)
decl_stmt|;
if|if
condition|(
name|importUrl
operator|!=
literal|null
operator|&&
operator|!
name|importUrl
operator|.
name|getProtocol
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"file"
argument_list|)
condition|)
block|{
name|URLConnection
name|con
init|=
name|importUrl
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|con
operator|.
name|setUseCaches
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|inputSource
operator|=
operator|new
name|InputSource
argument_list|(
name|con
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|importUrl
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|URIResolver
name|resolver
init|=
operator|new
name|URIResolver
argument_list|(
name|parent
operator|.
name|toString
argument_list|()
argument_list|,
name|importLocation
argument_list|)
decl_stmt|;
name|inputSource
operator|=
operator|new
name|InputSource
argument_list|(
name|resolver
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|URIResolver
name|resolver
init|=
operator|new
name|URIResolver
argument_list|(
name|importLocation
argument_list|)
decl_stmt|;
if|if
condition|(
name|resolver
operator|.
name|isResolved
argument_list|()
condition|)
block|{
name|inputSource
operator|=
operator|new
name|InputSource
argument_list|(
name|resolver
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|importedUri
operator|=
name|importUrl
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
comment|//
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|//
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//
block|}
return|return
name|inputSource
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
name|inputSource
operator|.
name|getByteStream
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|inputSource
operator|.
name|getByteStream
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//
block|}
block|}
block|}
block|}
end_class

end_unit

