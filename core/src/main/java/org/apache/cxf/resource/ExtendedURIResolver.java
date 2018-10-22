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
name|util
operator|.
name|Stack
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

begin_class
specifier|public
class|class
name|ExtendedURIResolver
block|{
specifier|protected
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|resource
operator|.
name|URIResolver
name|currentResolver
decl_stmt|;
specifier|protected
name|String
name|lastestImportUri
decl_stmt|;
specifier|protected
name|Stack
argument_list|<
name|InputStream
argument_list|>
name|resourceOpened
init|=
operator|new
name|Stack
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|ExtendedURIResolver
parameter_list|()
block|{
name|currentResolver
operator|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|resource
operator|.
name|URIResolver
argument_list|()
expr_stmt|;
block|}
specifier|public
name|InputSource
name|resolve
parameter_list|(
name|String
name|curUri
parameter_list|,
name|String
name|baseUri
parameter_list|)
block|{
try|try
block|{
name|currentResolver
operator|.
name|resolve
argument_list|(
name|baseUri
argument_list|,
name|curUri
argument_list|,
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|currentResolver
operator|.
name|isResolved
argument_list|()
condition|)
block|{
if|if
condition|(
name|currentResolver
operator|.
name|getURI
argument_list|()
operator|!=
literal|null
operator|&&
name|currentResolver
operator|.
name|getURI
argument_list|()
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
comment|// When importing a relative file,
comment|// setSystemId with an absolute path so the
comment|// resolver finds any files which that file
comment|// imports with locations relative to it.
name|curUri
operator|=
name|currentResolver
operator|.
name|getURI
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|currentResolver
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|curUri
operator|=
name|currentResolver
operator|.
name|getFile
argument_list|()
operator|.
name|getAbsoluteFile
argument_list|()
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|InputStream
name|in
init|=
name|currentResolver
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|resourceOpened
operator|.
name|addElement
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|InputSource
name|source
init|=
operator|new
name|InputSource
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|source
operator|.
name|setSystemId
argument_list|(
name|curUri
argument_list|)
expr_stmt|;
name|source
operator|.
name|setPublicId
argument_list|(
name|curUri
argument_list|)
expr_stmt|;
return|return
name|source
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// move on...
block|}
finally|finally
block|{
name|lastestImportUri
operator|=
name|curUri
expr_stmt|;
comment|// the uri may have been updated since we were called
comment|// so only store it away when everything else is done
block|}
return|return
literal|null
return|;
comment|// return new InputSource(schemaLocation);
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
while|while
condition|(
operator|!
name|resourceOpened
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
try|try
block|{
name|InputStream
name|in
init|=
name|resourceOpened
operator|.
name|pop
argument_list|()
decl_stmt|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
comment|// move on...
block|}
block|}
block|}
specifier|public
name|String
name|getLatestImportURI
parameter_list|()
block|{
return|return
name|this
operator|.
name|getURI
argument_list|()
return|;
block|}
specifier|public
name|String
name|getURI
parameter_list|()
block|{
if|if
condition|(
name|currentResolver
operator|.
name|getURI
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|currentResolver
operator|.
name|getURI
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
name|lastestImportUri
return|;
block|}
block|}
end_class

end_unit

