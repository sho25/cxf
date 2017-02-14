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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|RMMessageTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|DATA
init|=
operator|(
literal|"<greetMe xmlns=\"http://cxf.apache.org/greeter_control/types\">"
operator|+
literal|"<requestType>one</requestType></greetMe>"
operator|)
operator|.
name|getBytes
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TO
init|=
literal|"http://localhost:9999/decoupled_endpoint"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testAttributes
parameter_list|()
throws|throws
name|Exception
block|{
name|RMMessage
name|msg
init|=
operator|new
name|RMMessage
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setTo
argument_list|(
name|TO
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setMessageNumber
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|msg
operator|.
name|getTo
argument_list|()
argument_list|,
name|TO
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|msg
operator|.
name|getMessageNumber
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContentCachedOutputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|RMMessage
name|msg
init|=
operator|new
name|RMMessage
argument_list|()
decl_stmt|;
name|CachedOutputStream
name|co
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|co
operator|.
name|write
argument_list|(
name|DATA
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setContent
argument_list|(
name|co
argument_list|)
expr_stmt|;
name|byte
index|[]
name|msgbytes
init|=
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|msg
operator|.
name|getContent
argument_list|()
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|assertArrayEquals
argument_list|(
name|DATA
argument_list|,
name|msgbytes
argument_list|)
expr_stmt|;
name|co
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

