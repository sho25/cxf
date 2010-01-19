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
name|io
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FilterInputStream
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

begin_class
specifier|public
class|class
name|DelegatingInputStream
extends|extends
name|FilterInputStream
block|{
specifier|private
name|InputStream
name|origIn
decl_stmt|;
specifier|private
name|boolean
name|cached
decl_stmt|;
specifier|public
name|DelegatingInputStream
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
name|super
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|origIn
operator|=
name|is
expr_stmt|;
block|}
specifier|public
name|void
name|setInputStream
parameter_list|(
name|InputStream
name|inputStream
parameter_list|)
block|{
name|in
operator|=
name|inputStream
expr_stmt|;
block|}
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
block|{
return|return
name|in
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|in
operator|!=
name|origIn
condition|)
block|{
name|origIn
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Read the entire original input stream and cache it.  Useful      * if switching threads or doing something where the original      * stream may not be valid by the time the next read() occurs      */
specifier|public
name|void
name|cacheInput
parameter_list|()
block|{
if|if
condition|(
operator|!
name|cached
condition|)
block|{
name|CachedOutputStream
name|cache
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|IOUtils
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|cache
argument_list|)
expr_stmt|;
if|if
condition|(
name|cache
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|in
operator|=
name|cache
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
block|}
name|cache
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
comment|//ignore
block|}
name|cached
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

