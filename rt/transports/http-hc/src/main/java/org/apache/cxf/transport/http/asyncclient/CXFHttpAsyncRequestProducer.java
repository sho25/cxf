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
name|transport
operator|.
name|http
operator|.
name|asyncclient
package|;
end_package

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
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|Channels
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|ReadableByteChannel
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
name|io
operator|.
name|CachedOutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpHost
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|ContentEncoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|IOControl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|protocol
operator|.
name|HttpAsyncRequestProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|HttpContext
import|;
end_import

begin_class
specifier|public
class|class
name|CXFHttpAsyncRequestProducer
implements|implements
name|HttpAsyncRequestProducer
block|{
specifier|private
specifier|final
name|CXFHttpRequest
name|request
decl_stmt|;
specifier|private
specifier|final
name|SharedOutputBuffer
name|buf
decl_stmt|;
specifier|private
specifier|volatile
name|CachedOutputStream
name|content
decl_stmt|;
specifier|private
specifier|volatile
name|ByteBuffer
name|buffer
decl_stmt|;
specifier|private
specifier|volatile
name|InputStream
name|fis
decl_stmt|;
specifier|private
specifier|volatile
name|ReadableByteChannel
name|chan
decl_stmt|;
specifier|public
name|CXFHttpAsyncRequestProducer
parameter_list|(
specifier|final
name|CXFHttpRequest
name|request
parameter_list|,
specifier|final
name|SharedOutputBuffer
name|buf
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|buf
operator|=
name|buf
expr_stmt|;
name|this
operator|.
name|request
operator|=
name|request
expr_stmt|;
block|}
specifier|public
name|HttpHost
name|getTarget
parameter_list|()
block|{
name|URI
name|uri
init|=
name|request
operator|.
name|getURI
argument_list|()
decl_stmt|;
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Request URI is null"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|uri
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Request URI is not absolute"
argument_list|)
throw|;
block|}
return|return
operator|new
name|HttpHost
argument_list|(
name|uri
operator|.
name|getHost
argument_list|()
argument_list|,
name|uri
operator|.
name|getPort
argument_list|()
argument_list|,
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|HttpRequest
name|generateRequest
parameter_list|()
throws|throws
name|IOException
throws|,
name|HttpException
block|{
return|return
name|request
return|;
block|}
specifier|public
name|void
name|produceContent
parameter_list|(
specifier|final
name|ContentEncoder
name|enc
parameter_list|,
specifier|final
name|IOControl
name|ioc
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|content
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|buffer
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|content
operator|.
name|getTempFile
argument_list|()
operator|==
literal|null
condition|)
block|{
name|buffer
operator|=
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|content
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fis
operator|=
name|content
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
name|chan
operator|=
operator|(
name|fis
operator|instanceof
name|FileInputStream
operator|)
condition|?
operator|(
operator|(
name|FileInputStream
operator|)
name|fis
operator|)
operator|.
name|getChannel
argument_list|()
else|:
name|Channels
operator|.
name|newChannel
argument_list|(
name|fis
argument_list|)
expr_stmt|;
name|buffer
operator|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
literal|8
operator|*
literal|1024
argument_list|)
expr_stmt|;
block|}
block|}
name|int
name|i
init|=
operator|-
literal|1
decl_stmt|;
name|buffer
operator|.
name|rewind
argument_list|()
expr_stmt|;
if|if
condition|(
name|buffer
operator|.
name|hasRemaining
argument_list|()
operator|&&
name|chan
operator|!=
literal|null
condition|)
block|{
name|i
operator|=
name|chan
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|flip
argument_list|()
expr_stmt|;
block|}
name|enc
operator|.
name|write
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|buffer
operator|.
name|hasRemaining
argument_list|()
operator|&&
name|i
operator|==
operator|-
literal|1
condition|)
block|{
name|enc
operator|.
name|complete
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|buf
operator|.
name|produceContent
argument_list|(
name|enc
argument_list|,
name|ioc
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|requestCompleted
parameter_list|(
specifier|final
name|HttpContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|fis
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|fis
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|io
parameter_list|)
block|{
comment|//ignore
block|}
name|chan
operator|=
literal|null
expr_stmt|;
name|fis
operator|=
literal|null
expr_stmt|;
block|}
name|buffer
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|void
name|failed
parameter_list|(
specifier|final
name|Exception
name|ex
parameter_list|)
block|{
name|buf
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|isRepeatable
parameter_list|()
block|{
return|return
name|request
operator|.
name|getOutputStream
argument_list|()
operator|.
name|retransmitable
argument_list|()
return|;
block|}
specifier|public
name|void
name|resetRequest
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|request
operator|.
name|getOutputStream
argument_list|()
operator|.
name|retransmitable
argument_list|()
condition|)
block|{
name|content
operator|=
name|request
operator|.
name|getOutputStream
argument_list|()
operator|.
name|getCachedStream
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|buf
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|fis
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|fis
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|io
parameter_list|)
block|{
comment|//ignore
block|}
name|chan
operator|=
literal|null
expr_stmt|;
name|fis
operator|=
literal|null
expr_stmt|;
block|}
name|buffer
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

