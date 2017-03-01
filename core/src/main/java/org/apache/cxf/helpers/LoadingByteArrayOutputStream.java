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
name|helpers
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|FileOutputStream
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
name|FileChannel
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
name|Transferable
import|;
end_import

begin_comment
comment|/**  * Subclass of ByteArrayOutputStream that allows creation of a  * ByteArrayInputStream directly without creating a copy of the byte[].  *  * Also, on "toByteArray()" it truncates it's buffer to the current size  * and returns the new buffer directly.  Multiple calls to toByteArray()  * will return the exact same byte[] unless a write is called in between.  *  * Note: once the InputStream is created, the output stream should  * no longer be used.  In particular, make sure not to call reset()  * and then write as that may overwrite the data that the InputStream  * is using.  */
end_comment

begin_class
specifier|public
class|class
name|LoadingByteArrayOutputStream
extends|extends
name|ByteArrayOutputStream
block|{
specifier|public
name|LoadingByteArrayOutputStream
parameter_list|()
block|{
name|super
argument_list|(
literal|1024
argument_list|)
expr_stmt|;
block|}
specifier|public
name|LoadingByteArrayOutputStream
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|super
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|LoadedByteArrayInputStream
extends|extends
name|ByteArrayInputStream
implements|implements
name|Transferable
block|{
name|LoadedByteArrayInputStream
parameter_list|(
name|byte
index|[]
name|buf
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|super
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|IOUtils
operator|.
name|newStringFromBytes
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|count
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|transferTo
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|FileOutputStream
name|fout
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|FileChannel
name|channel
init|=
name|fout
operator|.
name|getChannel
argument_list|()
decl_stmt|;
name|ByteBuffer
name|bb
init|=
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|count
argument_list|)
decl_stmt|;
while|while
condition|(
name|bb
operator|.
name|hasRemaining
argument_list|()
condition|)
block|{
name|channel
operator|.
name|write
argument_list|(
name|bb
argument_list|)
expr_stmt|;
block|}
name|channel
operator|.
name|close
argument_list|()
expr_stmt|;
name|fout
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|ByteArrayInputStream
name|createInputStream
parameter_list|()
block|{
return|return
operator|new
name|LoadedByteArrayInputStream
argument_list|(
name|buf
argument_list|,
name|count
argument_list|)
return|;
block|}
specifier|public
name|void
name|setSize
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|count
operator|=
name|i
expr_stmt|;
block|}
specifier|public
name|byte
index|[]
name|toByteArray
parameter_list|()
block|{
if|if
condition|(
name|count
operator|!=
name|buf
operator|.
name|length
condition|)
block|{
name|buf
operator|=
name|super
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
block|}
return|return
name|buf
return|;
block|}
specifier|public
name|byte
index|[]
name|getRawBytes
parameter_list|()
block|{
return|return
name|buf
return|;
block|}
block|}
end_class

end_unit

