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
name|http_undertow
operator|.
name|websocket
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_comment
comment|/**  * JAXRSClientServerWebSocket test with jaxrs:server using the embedded undertow server.  */
end_comment

begin_class
specifier|public
class|class
name|JAXRSClientServerWebSocketSpringTest
extends|extends
name|JAXRSClientServerWebSocketTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerWebSocket
operator|.
name|PORT_SPRING
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unused"
block|,
literal|"resource"
block|}
argument_list|)
name|ApplicationContext
name|appctxt
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
name|JAXRSClientServerWebSocketSpringTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/jaxrs_websocket/beans-embedded.xml"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
block|}
specifier|protected
name|String
name|getPort
parameter_list|()
block|{
return|return
name|PORT
return|;
block|}
block|}
end_class

end_unit

