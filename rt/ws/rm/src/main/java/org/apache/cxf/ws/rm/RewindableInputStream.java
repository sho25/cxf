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
name|io
operator|.
name|CachedOutputStream
import|;
end_import

begin_comment
comment|/**  * Input stream wrapper to support rewinding to start of input.  */
end_comment

begin_class
specifier|public
class|class
name|RewindableInputStream
extends|extends
name|FilterInputStream
block|{
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
name|RewindableInputStream
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|MEMORY_SIZE_LIMIT
init|=
literal|0x10000
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|COPY_BLOCK_SIZE
init|=
literal|0x4000
decl_stmt|;
comment|/** Cached output stream -<code>null</code> if none used. */
specifier|private
specifier|final
name|CachedOutputStream
name|cachedStream
decl_stmt|;
comment|/**      * Constructs rewindable input stream      *      * @param is stream supporting mark      */
specifier|public
name|RewindableInputStream
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
name|mark
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|cachedStream
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Internal constructor from cached output stream.      *      * @param os      * @throws IOException      */
specifier|private
name|RewindableInputStream
parameter_list|(
name|CachedOutputStream
name|os
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|os
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|cachedStream
operator|=
name|os
expr_stmt|;
block|}
comment|/**      * @param is      * @return      * @throws IOException      */
specifier|public
specifier|static
name|RewindableInputStream
name|makeRewindable
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|is
operator|.
name|markSupported
argument_list|()
condition|)
block|{
return|return
operator|new
name|RewindableInputStream
argument_list|(
name|is
argument_list|)
return|;
block|}
name|CachedOutputStream
name|os
init|=
operator|new
name|CachedOutputStream
argument_list|(
name|MEMORY_SIZE_LIMIT
argument_list|)
decl_stmt|;
name|CachedOutputStream
operator|.
name|copyStream
argument_list|(
name|is
argument_list|,
name|os
argument_list|,
name|COPY_BLOCK_SIZE
argument_list|)
expr_stmt|;
return|return
operator|new
name|RewindableInputStream
argument_list|(
name|os
argument_list|)
return|;
block|}
comment|/**      * Rewind to start of input.      */
specifier|public
name|void
name|rewind
parameter_list|()
block|{
try|try
block|{
name|reset
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Error resetting stream"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|mark
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * Release resources.      */
specifier|public
name|void
name|release
parameter_list|()
block|{
if|if
condition|(
name|cachedStream
operator|!=
literal|null
condition|)
block|{
name|cachedStream
operator|.
name|releaseTempFileHold
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

