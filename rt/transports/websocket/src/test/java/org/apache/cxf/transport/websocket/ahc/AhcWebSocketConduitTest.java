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
operator|.
name|ahc
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
name|transport
operator|.
name|websocket
operator|.
name|WebSocketConstants
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
name|AhcWebSocketConduitTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TEST_RESPONSE1
init|=
literal|"200\r\nresponseId: 59610eed-d9de-4692-96d4-bb95a36c41ea\r\nContent-Type: text/plain\r\n\r\nHola!"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_RESPONSE2
init|=
literal|"responseId: 59610eed-d9de-4692-96d4-bb95a36c41ea\r\n\r\nNada!"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testResponseParsing
parameter_list|()
throws|throws
name|Exception
block|{
comment|// with all the headers using type string
name|AhcWebSocketConduit
operator|.
name|Response
name|resp
init|=
operator|new
name|AhcWebSocketConduit
operator|.
name|Response
argument_list|(
name|WebSocketConstants
operator|.
name|DEFAULT_RESPONSE_ID_KEY
argument_list|,
name|TEST_RESPONSE1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|resp
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"59610eed-d9de-4692-96d4-bb95a36c41ea"
argument_list|,
name|resp
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/plain"
argument_list|,
name|resp
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resp
operator|.
name|getEntity
argument_list|()
operator|instanceof
name|String
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hola!"
argument_list|,
name|resp
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
comment|// with all the heaers using type byte[]
name|resp
operator|=
operator|new
name|AhcWebSocketConduit
operator|.
name|Response
argument_list|(
name|WebSocketConstants
operator|.
name|DEFAULT_RESPONSE_ID_KEY
argument_list|,
name|TEST_RESPONSE1
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|resp
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"59610eed-d9de-4692-96d4-bb95a36c41ea"
argument_list|,
name|resp
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/plain"
argument_list|,
name|resp
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resp
operator|.
name|getEntity
argument_list|()
operator|instanceof
name|byte
index|[]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hola!"
argument_list|,
name|resp
operator|.
name|getTextEntity
argument_list|()
argument_list|)
expr_stmt|;
comment|// with only the id header using type String
name|resp
operator|=
operator|new
name|AhcWebSocketConduit
operator|.
name|Response
argument_list|(
name|WebSocketConstants
operator|.
name|DEFAULT_RESPONSE_ID_KEY
argument_list|,
name|TEST_RESPONSE2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|resp
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"59610eed-d9de-4692-96d4-bb95a36c41ea"
argument_list|,
name|resp
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resp
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resp
operator|.
name|getEntity
argument_list|()
operator|instanceof
name|String
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Nada!"
argument_list|,
name|resp
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
comment|// with only the id header using type byte[]
name|resp
operator|=
operator|new
name|AhcWebSocketConduit
operator|.
name|Response
argument_list|(
name|WebSocketConstants
operator|.
name|DEFAULT_RESPONSE_ID_KEY
argument_list|,
name|TEST_RESPONSE2
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|resp
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"59610eed-d9de-4692-96d4-bb95a36c41ea"
argument_list|,
name|resp
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resp
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resp
operator|.
name|getEntity
argument_list|()
operator|instanceof
name|byte
index|[]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Nada!"
argument_list|,
name|resp
operator|.
name|getTextEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

