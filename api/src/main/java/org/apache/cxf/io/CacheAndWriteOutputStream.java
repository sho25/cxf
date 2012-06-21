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

begin_comment
comment|/**  * This outputstream implementation will both write to the outputstream  * that is specified and cache the data at the same time. This allows us  * to go back and retransmit the data at a later time if necessary.  *  */
end_comment

begin_class
specifier|public
class|class
name|CacheAndWriteOutputStream
extends|extends
name|CachedOutputStream
block|{
name|OutputStream
name|flowThroughStream
decl_stmt|;
specifier|public
name|CacheAndWriteOutputStream
parameter_list|(
name|OutputStream
name|stream
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
if|if
condition|(
name|stream
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Stream may not be null"
argument_list|)
throw|;
block|}
name|flowThroughStream
operator|=
name|stream
expr_stmt|;
block|}
specifier|public
name|void
name|closeFlowthroughStream
parameter_list|()
throws|throws
name|IOException
block|{
name|flowThroughStream
operator|.
name|flush
argument_list|()
expr_stmt|;
name|flowThroughStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|postClose
parameter_list|()
throws|throws
name|IOException
block|{
name|flowThroughStream
operator|.
name|flush
argument_list|()
expr_stmt|;
name|flowThroughStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|OutputStream
name|getFlowThroughStream
parameter_list|()
block|{
return|return
name|flowThroughStream
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|onWrite
parameter_list|()
throws|throws
name|IOException
block|{
comment|// does nothing
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|flowThroughStream
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|super
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
name|flowThroughStream
operator|.
name|write
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
name|super
operator|.
name|write
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|flowThroughStream
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|super
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

