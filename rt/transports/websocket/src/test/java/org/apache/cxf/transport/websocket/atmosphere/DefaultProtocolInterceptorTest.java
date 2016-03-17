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
name|atmosphere
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|Map
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
name|transport
operator|.
name|websocket
operator|.
name|WebSocketUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereResourceImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|FrameworkConfig
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
name|DefaultProtocolInterceptorTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCreateResponseWithHeadersFiltering
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultProtocolInterceptor
name|dpi
init|=
operator|new
name|DefaultProtocolInterceptor
argument_list|()
decl_stmt|;
name|AtmosphereRequest
name|request
init|=
name|AtmosphereRequest
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|AtmosphereResponse
name|response
init|=
name|AtmosphereResponse
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|AtmosphereResourceImpl
name|resource
init|=
operator|new
name|AtmosphereResourceImpl
argument_list|()
decl_stmt|;
name|resource
operator|.
name|transport
argument_list|(
name|AtmosphereResource
operator|.
name|TRANSPORT
operator|.
name|WEBSOCKET
argument_list|)
expr_stmt|;
name|request
operator|.
name|localAttributes
argument_list|()
operator|.
name|put
argument_list|(
name|FrameworkConfig
operator|.
name|ATMOSPHERE_RESOURCE
argument_list|,
name|resource
argument_list|)
expr_stmt|;
name|response
operator|.
name|request
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|String
name|payload
init|=
literal|"hello cxf"
decl_stmt|;
name|String
name|contentType
init|=
literal|"text/plain"
decl_stmt|;
name|response
operator|.
name|headers
argument_list|()
operator|.
name|put
argument_list|(
literal|"Content-Type"
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
name|byte
index|[]
name|transformed
init|=
name|dpi
operator|.
name|createResponse
argument_list|(
name|response
argument_list|,
name|payload
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|verifyTransformed
argument_list|(
literal|"200"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Content-Type"
block|,
name|contentType
block|}
argument_list|,
name|payload
argument_list|,
name|transformed
argument_list|)
expr_stmt|;
name|response
operator|.
name|headers
argument_list|()
operator|.
name|put
argument_list|(
literal|"X-fruit"
argument_list|,
literal|"peach"
argument_list|)
expr_stmt|;
name|response
operator|.
name|headers
argument_list|()
operator|.
name|put
argument_list|(
literal|"X-vegetable"
argument_list|,
literal|"tomato"
argument_list|)
expr_stmt|;
name|transformed
operator|=
name|dpi
operator|.
name|createResponse
argument_list|(
name|response
argument_list|,
name|payload
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|verifyTransformed
argument_list|(
literal|"200"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Content-Type"
block|,
name|contentType
block|}
argument_list|,
name|payload
argument_list|,
name|transformed
argument_list|)
expr_stmt|;
name|dpi
operator|.
name|includedheaders
argument_list|(
literal|"X-f.*"
argument_list|)
expr_stmt|;
name|transformed
operator|=
name|dpi
operator|.
name|createResponse
argument_list|(
name|response
argument_list|,
name|payload
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|verifyTransformed
argument_list|(
literal|"200"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Content-Type"
block|,
name|contentType
block|,
literal|"X-Fruit"
block|,
literal|"peach"
block|}
argument_list|,
name|payload
argument_list|,
name|transformed
argument_list|)
expr_stmt|;
name|dpi
operator|.
name|includedheaders
argument_list|(
literal|"X-.*"
argument_list|)
expr_stmt|;
name|transformed
operator|=
name|dpi
operator|.
name|createResponse
argument_list|(
name|response
argument_list|,
name|payload
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|verifyTransformed
argument_list|(
literal|"200"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Content-Type"
block|,
name|contentType
block|,
literal|"X-Fruit"
block|,
literal|"peach"
block|,
literal|"X-vegetable"
block|,
literal|"tomato"
block|}
argument_list|,
name|payload
argument_list|,
name|transformed
argument_list|)
expr_stmt|;
name|dpi
operator|.
name|excludedheaders
argument_list|(
literal|".*able"
argument_list|)
expr_stmt|;
name|transformed
operator|=
name|dpi
operator|.
name|createResponse
argument_list|(
name|response
argument_list|,
name|payload
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|verifyTransformed
argument_list|(
literal|"200"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Content-Type"
block|,
name|contentType
block|,
literal|"X-Fruit"
block|,
literal|"peach"
block|}
argument_list|,
name|payload
argument_list|,
name|transformed
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifyTransformed
parameter_list|(
name|String
name|code
parameter_list|,
name|String
index|[]
name|headers
parameter_list|,
name|String
name|body
parameter_list|,
name|byte
index|[]
name|transformed
parameter_list|)
throws|throws
name|Exception
block|{
name|InputStream
name|in
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|transformed
argument_list|)
decl_stmt|;
name|String
name|c
init|=
name|WebSocketUtils
operator|.
name|readLine
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|hs
init|=
name|WebSocketUtils
operator|.
name|readHeaders
argument_list|(
name|in
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|byte
index|[]
name|b
init|=
name|WebSocketUtils
operator|.
name|readBody
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|code
argument_list|,
name|c
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|headers
operator|.
name|length
operator|>>
literal|1
argument_list|,
name|hs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|headers
operator|.
name|length
condition|;
name|i
operator|+=
literal|2
control|)
block|{
name|assertEquals
argument_list|(
name|headers
index|[
name|i
operator|+
literal|1
index|]
argument_list|,
name|hs
operator|.
name|get
argument_list|(
name|headers
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|body
argument_list|,
operator|new
name|String
argument_list|(
name|b
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

