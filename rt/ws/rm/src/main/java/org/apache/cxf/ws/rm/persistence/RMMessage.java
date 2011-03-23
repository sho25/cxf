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
name|ws
operator|.
name|rm
operator|.
name|persistence
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
name|io
operator|.
name|CachedOutputStream
import|;
end_import

begin_class
specifier|public
class|class
name|RMMessage
block|{
specifier|private
name|CachedOutputStream
name|content
decl_stmt|;
specifier|private
name|long
name|messageNumber
decl_stmt|;
specifier|private
name|String
name|to
decl_stmt|;
comment|/**      * Returns the message number of the message within its sequence.      * @return the message number      */
specifier|public
name|long
name|getMessageNumber
parameter_list|()
block|{
return|return
name|messageNumber
return|;
block|}
comment|/**      * Sets the message number of the message within its sequence.      * @param messageNumber the message number      */
specifier|public
name|void
name|setMessageNumber
parameter_list|(
name|long
name|mn
parameter_list|)
block|{
name|messageNumber
operator|=
name|mn
expr_stmt|;
block|}
comment|/**      * Returns the content of the message as an input stream.      * @return the content      * @deprecated      */
specifier|public
name|byte
index|[]
name|getContent
parameter_list|()
block|{
name|byte
index|[]
name|bytes
init|=
literal|null
decl_stmt|;
try|try
block|{
name|bytes
operator|=
name|content
operator|.
name|getBytes
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore and treat it as null
block|}
return|return
name|bytes
return|;
block|}
comment|/**      * Sets the message content as an input stream.      * @param content the message content      * @deprecated      */
specifier|public
name|void
name|setContent
parameter_list|(
name|byte
index|[]
name|c
parameter_list|)
block|{
name|content
operator|=
operator|new
name|CachedOutputStream
argument_list|()
expr_stmt|;
name|content
operator|.
name|holdTempFile
argument_list|()
expr_stmt|;
try|try
block|{
name|content
operator|.
name|write
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
comment|/**      * Sets the message content using the input stream.      * @param in      * @throws IOException      */
specifier|public
name|void
name|setContent
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|content
operator|=
operator|new
name|CachedOutputStream
argument_list|()
expr_stmt|;
name|content
operator|.
name|holdTempFile
argument_list|()
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the message content using the cached output stream.      * @param c      */
specifier|public
name|void
name|setContent
parameter_list|(
name|CachedOutputStream
name|c
parameter_list|)
block|{
name|content
operator|=
name|c
expr_stmt|;
block|}
comment|/**      * Returns the to address of this message.      * @return the to address      */
specifier|public
name|String
name|getTo
parameter_list|()
block|{
return|return
name|to
return|;
block|}
comment|/**      * Sets the to address of this message.      * @param t the to address      */
specifier|public
name|void
name|setTo
parameter_list|(
name|String
name|t
parameter_list|)
block|{
name|to
operator|=
name|t
expr_stmt|;
block|}
comment|/**      * Returns the input stream of this message content.      * @return      * @throws IOException      */
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|content
operator|.
name|getInputStream
argument_list|()
return|;
block|}
comment|/**      * Returns the associated cached output stream.      * @return      */
specifier|public
name|CachedOutputStream
name|getCachedOutputStream
parameter_list|()
block|{
return|return
name|content
return|;
block|}
comment|/**      * Returns the length of the message content in bytes.      *       * @return      */
specifier|public
name|int
name|getSize
parameter_list|()
block|{
return|return
name|content
operator|.
name|size
argument_list|()
return|;
block|}
block|}
end_class

end_unit

