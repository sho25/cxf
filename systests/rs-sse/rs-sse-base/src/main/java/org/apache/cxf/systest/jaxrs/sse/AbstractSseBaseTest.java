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
name|systest
operator|.
name|jaxrs
operator|.
name|sse
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|ClientBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|WebTarget
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|JsonProcessingException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|jaxrs
operator|.
name|json
operator|.
name|JacksonJsonProvider
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
name|jaxrs
operator|.
name|client
operator|.
name|WebClient
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusClientServerTestBase
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractSseBaseTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|private
specifier|final
name|ObjectMapper
name|mapper
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
specifier|protected
name|String
name|toJson
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|Integer
name|id
parameter_list|)
throws|throws
name|JsonProcessingException
block|{
return|return
name|mapper
operator|.
name|writeValueAsString
argument_list|(
operator|new
name|Book
argument_list|(
name|name
argument_list|,
name|id
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|WebClient
name|createWebClient
parameter_list|(
specifier|final
name|String
name|url
parameter_list|,
specifier|final
name|String
name|media
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|?
argument_list|>
name|providers
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
name|url
argument_list|,
name|providers
argument_list|)
operator|.
name|accept
argument_list|(
name|media
argument_list|)
decl_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|wc
argument_list|)
operator|.
name|getHttpConduit
argument_list|()
operator|.
name|getClient
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
literal|8000L
argument_list|)
expr_stmt|;
return|return
name|wc
return|;
block|}
specifier|protected
name|WebClient
name|createWebClient
parameter_list|(
specifier|final
name|String
name|url
parameter_list|)
block|{
return|return
name|createWebClient
argument_list|(
name|url
argument_list|,
name|MediaType
operator|.
name|SERVER_SENT_EVENTS
argument_list|)
return|;
block|}
specifier|protected
name|WebTarget
name|createWebTarget
parameter_list|(
specifier|final
name|String
name|url
parameter_list|)
block|{
return|return
name|ClientBuilder
operator|.
name|newClient
argument_list|()
operator|.
name|property
argument_list|(
literal|"http.receive.timeout"
argument_list|,
literal|8000
argument_list|)
operator|.
name|register
argument_list|(
name|JacksonJsonProvider
operator|.
name|class
argument_list|)
operator|.
name|target
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
name|url
argument_list|)
return|;
block|}
specifier|protected
name|void
name|awaitEvents
parameter_list|(
name|long
name|timeout
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|?
argument_list|>
name|events
parameter_list|,
name|int
name|size
parameter_list|)
throws|throws
name|InterruptedException
block|{
specifier|final
name|long
name|sleep
init|=
name|timeout
operator|/
literal|10
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
name|timeout
condition|;
name|i
operator|+=
name|sleep
control|)
block|{
if|if
condition|(
name|events
operator|.
name|size
argument_list|()
operator|==
name|size
condition|)
block|{
break|break;
block|}
else|else
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|sleep
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
specifier|abstract
name|int
name|getPort
parameter_list|()
function_decl|;
block|}
end_class

end_unit

