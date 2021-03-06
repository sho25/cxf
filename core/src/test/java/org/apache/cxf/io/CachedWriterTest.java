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
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_class
specifier|public
class|class
name|CachedWriterTest
extends|extends
name|CachedStreamTestBase
block|{
annotation|@
name|Override
specifier|protected
name|void
name|reloadDefaultProperties
parameter_list|()
block|{
name|CachedWriter
operator|.
name|setDefaultThreshold
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|CachedWriter
operator|.
name|setDefaultMaxSize
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|CachedWriter
operator|.
name|setDefaultCipherTransformation
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|createCache
parameter_list|()
block|{
return|return
operator|new
name|CachedWriter
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|createCache
parameter_list|(
name|long
name|threshold
parameter_list|)
block|{
return|return
name|createCache
argument_list|(
name|threshold
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|createCache
parameter_list|(
name|long
name|threshold
parameter_list|,
name|String
name|transformation
parameter_list|)
block|{
name|CachedWriter
name|cos
init|=
operator|new
name|CachedWriter
argument_list|()
decl_stmt|;
name|cos
operator|.
name|setThreshold
argument_list|(
name|threshold
argument_list|)
expr_stmt|;
name|cos
operator|.
name|setCipherTransformation
argument_list|(
name|transformation
argument_list|)
expr_stmt|;
return|return
name|cos
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getResetOutValue
parameter_list|(
name|String
name|result
parameter_list|,
name|Object
name|cache
parameter_list|)
throws|throws
name|IOException
block|{
name|CachedWriter
name|cos
init|=
operator|(
name|CachedWriter
operator|)
name|cache
decl_stmt|;
name|cos
operator|.
name|write
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|StringWriter
name|out
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|cos
operator|.
name|resetOut
argument_list|(
name|out
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|out
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|File
name|getTmpFile
parameter_list|(
name|String
name|result
parameter_list|,
name|Object
name|cache
parameter_list|)
throws|throws
name|IOException
block|{
name|CachedWriter
name|cos
init|=
operator|(
name|CachedWriter
operator|)
name|cache
decl_stmt|;
name|cos
operator|.
name|write
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|cos
operator|.
name|flush
argument_list|()
expr_stmt|;
name|cos
operator|.
name|getOut
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|cos
operator|.
name|getTempFile
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|getInputStreamObject
parameter_list|(
name|Object
name|cache
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|(
operator|(
name|CachedWriter
operator|)
name|cache
operator|)
operator|.
name|getReader
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|readFromStreamObject
parameter_list|(
name|Object
name|obj
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|readFromReader
argument_list|(
operator|(
name|Reader
operator|)
name|obj
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|readPartiallyFromStreamObject
parameter_list|(
name|Object
name|cache
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|readPartiallyFromReader
argument_list|(
operator|(
name|Reader
operator|)
name|cache
argument_list|,
name|len
argument_list|)
return|;
block|}
block|}
end_class

end_unit

