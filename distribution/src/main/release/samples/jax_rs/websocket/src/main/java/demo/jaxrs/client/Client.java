begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|jaxrs
operator|.
name|client
package|;
end_package

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
name|List
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

begin_class
specifier|public
specifier|final
class|class
name|Client
block|{
specifier|private
name|Client
parameter_list|()
block|{     }
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
name|args
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Sent HTTP GET request to query all customer info
comment|/*          * URL url = new URL("http://localhost:9000/customers");          * System.out.println("Invoking server through HTTP GET to query all          * customer info"); InputStream in = url.openStream(); StreamSource          * source = new StreamSource(in); printSource(source);          */
comment|// Create a websocket client and connect to the target service
name|WebSocketTestClient
name|client
init|=
operator|new
name|WebSocketTestClient
argument_list|(
literal|"ws://localhost:9000/"
argument_list|)
decl_stmt|;
name|client
operator|.
name|connect
argument_list|()
expr_stmt|;
comment|// Sent GET request to query customer info
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sent GET request to query customer info"
argument_list|)
expr_stmt|;
name|client
operator|.
name|sendTextMessage
argument_list|(
literal|"GET /customerservice/customers/123"
argument_list|)
expr_stmt|;
name|client
operator|.
name|await
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|WebSocketTestClient
operator|.
name|Response
argument_list|>
name|responses
init|=
name|client
operator|.
name|getReceivedResponses
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|responses
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// Sent GET request to query sub resource product info
name|client
operator|.
name|reset
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sent GET request to query sub resource product info"
argument_list|)
expr_stmt|;
name|client
operator|.
name|sendTextMessage
argument_list|(
literal|"GET /customerservice/orders/223/products/323"
argument_list|)
expr_stmt|;
name|client
operator|.
name|await
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|responses
operator|=
name|client
operator|.
name|getReceivedResponses
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|responses
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// Sent PUT request to update customer info
name|client
operator|.
name|reset
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sent PUT request to update customer info"
argument_list|)
expr_stmt|;
name|String
name|inputData
init|=
name|getStringFromInputStream
argument_list|(
name|Client
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/update_customer.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|client
operator|.
name|sendTextMessage
argument_list|(
literal|"PUT /customerservice/customers\r\nContent-Type: text/xml;"
operator|+
literal|" charset=ISO-8859-1\r\n\r\n"
operator|+
name|inputData
argument_list|)
expr_stmt|;
name|client
operator|.
name|await
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|responses
operator|=
name|client
operator|.
name|getReceivedResponses
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|responses
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// Sent POST request to add customer
name|client
operator|.
name|reset
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sent POST request to add customer"
argument_list|)
expr_stmt|;
name|inputData
operator|=
name|getStringFromInputStream
argument_list|(
name|Client
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/add_customer.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|client
operator|.
name|sendTextMessage
argument_list|(
literal|"POST /customerservice/customers\r\nContent-Type: text/xml; "
operator|+
literal|"charset=ISO-8859-1\r\nAccept: text/xml\r\n\r\n"
operator|+
name|inputData
argument_list|)
expr_stmt|;
name|client
operator|.
name|await
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|responses
operator|=
name|client
operator|.
name|getReceivedResponses
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|responses
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// Create another websocket client and connect to the target service
name|WebSocketTestClient
name|client2
init|=
operator|new
name|WebSocketTestClient
argument_list|(
literal|"ws://localhost:9000/"
argument_list|)
decl_stmt|;
name|client2
operator|.
name|connect
argument_list|()
expr_stmt|;
comment|// Sent GET request to monitor the customer activities
name|client2
operator|.
name|reset
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sent GET request to monitor activities"
argument_list|)
expr_stmt|;
name|client2
operator|.
name|sendTextMessage
argument_list|(
literal|"GET /customerservice/monitor"
argument_list|)
expr_stmt|;
name|client2
operator|.
name|await
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|responses
operator|=
name|client2
operator|.
name|getReceivedResponses
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|responses
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// one retrieval, one delete
name|client2
operator|.
name|reset
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|client
operator|.
name|reset
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|client
operator|.
name|sendTextMessage
argument_list|(
literal|"GET /customerservice/customers/123"
argument_list|)
expr_stmt|;
name|client
operator|.
name|sendTextMessage
argument_list|(
literal|"DELETE /customerservice/customers/235"
argument_list|)
expr_stmt|;
name|client2
operator|.
name|await
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|responses
operator|=
name|client2
operator|.
name|getReceivedResponses
argument_list|()
expr_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|responses
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
name|client
operator|.
name|close
argument_list|()
expr_stmt|;
name|client2
operator|.
name|close
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|String
name|getStringFromInputStream
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|Exception
block|{
name|CachedOutputStream
name|bos
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|bos
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|bos
operator|.
name|getOut
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

