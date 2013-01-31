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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|CachedWriterTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testResetOut
parameter_list|()
throws|throws
name|IOException
block|{
name|CachedWriter
name|cos
init|=
operator|new
name|CachedWriter
argument_list|()
decl_stmt|;
name|String
name|result
init|=
name|initTestData
argument_list|(
literal|16
argument_list|)
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
name|String
name|test
init|=
name|out
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The test stream content isn't same "
argument_list|,
name|test
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|cos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeleteTmpFile
parameter_list|()
throws|throws
name|IOException
block|{
name|CachedWriter
name|cos
init|=
operator|new
name|CachedWriter
argument_list|()
decl_stmt|;
comment|//ensure output data size larger then 64k which will generate tmp file
name|String
name|result
init|=
name|initTestData
argument_list|(
literal|65
argument_list|)
decl_stmt|;
name|cos
operator|.
name|write
argument_list|(
name|result
argument_list|)
expr_stmt|;
comment|//assert tmp file is generated
name|File
name|tempFile
init|=
name|cos
operator|.
name|getTempFile
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|tempFile
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tempFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|cos
operator|.
name|close
argument_list|()
expr_stmt|;
comment|//assert tmp file is deleted after close the CachedOutputStream
name|assertFalse
argument_list|(
name|tempFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeleteTmpFile2
parameter_list|()
throws|throws
name|IOException
block|{
name|CachedWriter
name|cos
init|=
operator|new
name|CachedWriter
argument_list|()
decl_stmt|;
comment|//ensure output data size larger then 64k which will generate tmp file
name|String
name|result
init|=
name|initTestData
argument_list|(
literal|65
argument_list|)
decl_stmt|;
name|cos
operator|.
name|write
argument_list|(
name|result
argument_list|)
expr_stmt|;
comment|//assert tmp file is generated
name|File
name|tempFile
init|=
name|cos
operator|.
name|getTempFile
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|tempFile
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tempFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|Reader
name|in
init|=
name|cos
operator|.
name|getReader
argument_list|()
decl_stmt|;
name|cos
operator|.
name|close
argument_list|()
expr_stmt|;
comment|//assert tmp file is not deleted when the reader is open
name|assertTrue
argument_list|(
name|tempFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
comment|//assert tmp file is deleted after the reader is closed
name|assertFalse
argument_list|(
name|tempFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|initTestData
parameter_list|(
name|int
name|packetSize
parameter_list|)
block|{
name|String
name|temp
init|=
literal|"abcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+?><[]/0123456789"
decl_stmt|;
name|String
name|result
init|=
operator|new
name|String
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|1024
operator|*
name|packetSize
operator|/
name|temp
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|result
operator|=
name|result
operator|+
name|temp
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

