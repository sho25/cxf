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
name|attachment
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

begin_class
specifier|public
class|class
name|DelegatingInputStream
extends|extends
name|InputStream
block|{
specifier|private
name|InputStream
name|is
decl_stmt|;
specifier|private
name|AttachmentDeserializer
name|deserializer
decl_stmt|;
specifier|private
name|boolean
name|isClosed
decl_stmt|;
comment|/**      * @param source      */
name|DelegatingInputStream
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|AttachmentDeserializer
name|ads
parameter_list|)
block|{
name|this
operator|.
name|is
operator|=
name|is
expr_stmt|;
name|deserializer
operator|=
name|ads
expr_stmt|;
block|}
name|DelegatingInputStream
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
name|this
operator|.
name|is
operator|=
name|is
expr_stmt|;
name|deserializer
operator|=
literal|null
expr_stmt|;
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
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|isClosed
operator|&&
name|deserializer
operator|!=
literal|null
condition|)
block|{
name|deserializer
operator|.
name|markClosed
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|isClosed
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|boolean
name|isClosed
parameter_list|()
block|{
return|return
name|isClosed
return|;
block|}
specifier|public
name|void
name|setClosed
parameter_list|(
name|boolean
name|closed
parameter_list|)
block|{
name|this
operator|.
name|isClosed
operator|=
name|closed
expr_stmt|;
block|}
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|this
operator|.
name|is
operator|.
name|read
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|available
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|this
operator|.
name|is
operator|.
name|available
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|mark
parameter_list|(
name|int
name|arg0
parameter_list|)
block|{
name|this
operator|.
name|is
operator|.
name|mark
argument_list|(
name|arg0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|markSupported
parameter_list|()
block|{
return|return
name|this
operator|.
name|is
operator|.
name|markSupported
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|int
name|arg1
parameter_list|,
name|int
name|arg2
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|this
operator|.
name|is
operator|.
name|read
argument_list|(
name|bytes
argument_list|,
name|arg1
argument_list|,
name|arg2
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|this
operator|.
name|is
operator|.
name|read
argument_list|(
name|bytes
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|reset
parameter_list|()
throws|throws
name|IOException
block|{
name|this
operator|.
name|is
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|skip
parameter_list|(
name|long
name|n
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|this
operator|.
name|is
operator|.
name|skip
argument_list|(
name|n
argument_list|)
return|;
block|}
specifier|public
name|void
name|setInputStream
parameter_list|(
name|InputStream
name|inputStream
parameter_list|)
block|{
name|this
operator|.
name|is
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
name|is
return|;
block|}
block|}
end_class

end_unit

