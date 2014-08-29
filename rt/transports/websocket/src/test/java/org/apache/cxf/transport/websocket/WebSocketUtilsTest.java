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
name|websocket
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|WebSocketUtilsTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|TEST_BODY_BYTES
init|=
literal|"buenos dias"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|TEST_HEADERS_BYTES
init|=
literal|"200\r\nContent-Type: text/xml;charset=utf-8\r\n"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|TEST_ID_BYTES
init|=
operator|(
name|WebSocketConstants
operator|.
name|DEFAULT_RESPONSE_ID_KEY
operator|+
literal|": 31415926-5358-9793-2384-626433832795\r\n"
operator|)
operator|.
name|getBytes
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|TEST_HEADERS_MAP
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|CRLF
init|=
literal|"\r\n"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
static|static
block|{
name|TEST_HEADERS_MAP
operator|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
expr_stmt|;
name|TEST_HEADERS_MAP
operator|.
name|put
argument_list|(
name|WebSocketUtils
operator|.
name|SC_KEY
argument_list|,
literal|"200"
argument_list|)
expr_stmt|;
name|TEST_HEADERS_MAP
operator|.
name|put
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"text/xml;charset=utf-8"
argument_list|)
expr_stmt|;
name|TEST_HEADERS_MAP
operator|.
name|put
argument_list|(
name|WebSocketConstants
operator|.
name|DEFAULT_RESPONSE_ID_KEY
argument_list|,
literal|"31415926-5358-9793-2384-626433832795"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuildResponse
parameter_list|()
block|{
name|byte
index|[]
name|r
init|=
name|WebSocketUtils
operator|.
name|buildResponse
argument_list|(
name|TEST_BODY_BYTES
argument_list|,
literal|0
argument_list|,
name|TEST_BODY_BYTES
operator|.
name|length
argument_list|)
decl_stmt|;
name|verifyBytes
argument_list|(
name|CRLF
argument_list|,
literal|0
argument_list|,
name|r
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|verifyBytes
argument_list|(
name|TEST_BODY_BYTES
argument_list|,
literal|0
argument_list|,
name|r
argument_list|,
literal|2
argument_list|,
name|TEST_BODY_BYTES
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
operator|+
name|TEST_BODY_BYTES
operator|.
name|length
argument_list|,
name|r
operator|.
name|length
argument_list|)
expr_stmt|;
name|r
operator|=
name|WebSocketUtils
operator|.
name|buildResponse
argument_list|(
name|TEST_HEADERS_BYTES
argument_list|,
name|TEST_BODY_BYTES
argument_list|,
literal|0
argument_list|,
name|TEST_BODY_BYTES
operator|.
name|length
argument_list|)
expr_stmt|;
name|verifyBytes
argument_list|(
name|TEST_HEADERS_BYTES
argument_list|,
literal|0
argument_list|,
name|r
argument_list|,
literal|0
argument_list|,
name|TEST_HEADERS_BYTES
operator|.
name|length
argument_list|)
expr_stmt|;
name|verifyBytes
argument_list|(
name|CRLF
argument_list|,
literal|0
argument_list|,
name|r
argument_list|,
name|TEST_HEADERS_BYTES
operator|.
name|length
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|verifyBytes
argument_list|(
name|TEST_BODY_BYTES
argument_list|,
literal|0
argument_list|,
name|r
argument_list|,
name|TEST_HEADERS_BYTES
operator|.
name|length
operator|+
literal|2
argument_list|,
name|TEST_BODY_BYTES
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_HEADERS_BYTES
operator|.
name|length
operator|+
literal|2
operator|+
name|TEST_BODY_BYTES
operator|.
name|length
argument_list|,
name|r
operator|.
name|length
argument_list|)
expr_stmt|;
name|r
operator|=
name|WebSocketUtils
operator|.
name|buildResponse
argument_list|(
name|TEST_HEADERS_MAP
argument_list|,
name|TEST_BODY_BYTES
argument_list|,
literal|0
argument_list|,
name|TEST_BODY_BYTES
operator|.
name|length
argument_list|)
expr_stmt|;
name|verifyBytes
argument_list|(
name|TEST_HEADERS_BYTES
argument_list|,
literal|0
argument_list|,
name|r
argument_list|,
literal|0
argument_list|,
name|TEST_HEADERS_BYTES
operator|.
name|length
argument_list|)
expr_stmt|;
name|verifyBytes
argument_list|(
name|TEST_ID_BYTES
argument_list|,
literal|0
argument_list|,
name|r
argument_list|,
name|TEST_HEADERS_BYTES
operator|.
name|length
argument_list|,
name|TEST_ID_BYTES
operator|.
name|length
argument_list|)
expr_stmt|;
name|verifyBytes
argument_list|(
name|CRLF
argument_list|,
literal|0
argument_list|,
name|r
argument_list|,
name|TEST_HEADERS_BYTES
operator|.
name|length
operator|+
name|TEST_ID_BYTES
operator|.
name|length
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|verifyBytes
argument_list|(
name|TEST_BODY_BYTES
argument_list|,
literal|0
argument_list|,
name|r
argument_list|,
name|TEST_HEADERS_BYTES
operator|.
name|length
operator|+
name|TEST_ID_BYTES
operator|.
name|length
operator|+
literal|2
argument_list|,
name|TEST_BODY_BYTES
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_HEADERS_BYTES
operator|.
name|length
operator|+
name|TEST_ID_BYTES
operator|.
name|length
operator|+
literal|2
operator|+
name|TEST_BODY_BYTES
operator|.
name|length
argument_list|,
name|r
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// with some offset
name|r
operator|=
name|WebSocketUtils
operator|.
name|buildResponse
argument_list|(
name|TEST_BODY_BYTES
argument_list|,
literal|3
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|verifyBytes
argument_list|(
name|CRLF
argument_list|,
literal|0
argument_list|,
name|r
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|verifyBytes
argument_list|(
name|TEST_BODY_BYTES
argument_list|,
literal|3
argument_list|,
name|r
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
operator|+
literal|3
argument_list|,
name|r
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifyBytes
parameter_list|(
name|byte
index|[]
name|expected
parameter_list|,
name|int
name|epos
parameter_list|,
name|byte
index|[]
name|result
parameter_list|,
name|int
name|rpos
parameter_list|,
name|int
name|length
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|result
index|[
name|rpos
operator|+
name|i
index|]
operator|!=
name|expected
index|[
name|epos
operator|+
name|i
index|]
condition|)
block|{
name|fail
argument_list|(
literal|"Wrong byte at position result["
operator|+
operator|(
name|rpos
operator|+
name|i
operator|)
operator|+
literal|"]. Expected "
operator|+
name|expected
index|[
name|epos
operator|+
name|i
index|]
operator|+
literal|" but was "
operator|+
name|result
index|[
name|rpos
operator|+
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

