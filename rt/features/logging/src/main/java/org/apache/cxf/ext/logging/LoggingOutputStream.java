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
name|ext
operator|.
name|logging
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
name|OutputStream
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
name|CacheAndWriteOutputStream
import|;
end_import

begin_class
specifier|public
class|class
name|LoggingOutputStream
extends|extends
name|CacheAndWriteOutputStream
block|{
specifier|private
name|boolean
name|skipFlushingFlowThroughStream
decl_stmt|;
name|LoggingOutputStream
parameter_list|(
name|OutputStream
name|stream
parameter_list|)
block|{
name|super
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
comment|/**      * Override, because there is no need to flush the flow-through stream.      * Flushing will be done by the underlying OutputStream.      *      * @see org.apache.cxf.io.AbstractThresholdOutputStream#close()      */
annotation|@
name|Override
specifier|public
name|void
name|closeFlowthroughStream
parameter_list|()
throws|throws
name|IOException
block|{
name|getFlowThroughStream
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Override, because there is no need to flush the flow-through stream.      * Flushing will be done by the underlying OutputStream.      *      * @see org.apache.cxf.io.AbstractThresholdOutputStream#close()      */
annotation|@
name|Override
specifier|protected
name|void
name|postClose
parameter_list|()
throws|throws
name|IOException
block|{
name|getFlowThroughStream
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Flush the flow-through stream if the current stream is also flushed.      */
annotation|@
name|Override
specifier|protected
name|void
name|doFlush
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|skipFlushingFlowThroughStream
condition|)
block|{
return|return;
block|}
name|getFlowThroughStream
argument_list|()
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeCacheTo
parameter_list|(
name|StringBuilder
name|out
parameter_list|,
name|String
name|charsetName
parameter_list|,
name|long
name|limit
parameter_list|)
throws|throws
name|IOException
block|{
name|skipFlushingFlowThroughStream
operator|=
literal|true
expr_stmt|;
name|super
operator|.
name|writeCacheTo
argument_list|(
name|out
argument_list|,
name|charsetName
argument_list|,
name|limit
argument_list|)
expr_stmt|;
name|skipFlushingFlowThroughStream
operator|=
literal|false
expr_stmt|;
block|}
block|}
end_class

end_unit

