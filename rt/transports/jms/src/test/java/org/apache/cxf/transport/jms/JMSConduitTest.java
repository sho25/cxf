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
name|jms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|message
operator|.
name|Message
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
name|message
operator|.
name|MessageImpl
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|JMSConduitTest
extends|extends
name|AbstractJMSTester
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|EndpointInfo
name|ei
init|=
name|setupServiceInfo
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
name|WSDL
argument_list|,
literal|"HelloWorldQueueBinMsgService"
argument_list|,
literal|"HelloWorldQueueBinMsgPort"
argument_list|)
decl_stmt|;
name|JMSConduit
name|conduit
init|=
name|setupJMSConduit
argument_list|(
name|ei
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Can't get the right ClientReceiveTimeout"
argument_list|,
literal|500L
argument_list|,
name|conduit
operator|.
name|getJmsConfig
argument_list|()
operator|.
name|getReceiveTimeout
argument_list|()
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrepareSend
parameter_list|()
throws|throws
name|Exception
block|{
name|EndpointInfo
name|ei
init|=
name|setupServiceInfo
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
name|WSDL
argument_list|,
literal|"HelloWorldService"
argument_list|,
literal|"HelloWorldPort"
argument_list|)
decl_stmt|;
name|JMSConduit
name|conduit
init|=
name|setupJMSConduit
argument_list|(
name|ei
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|OutputStream
name|os
init|=
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|Writer
name|writer
init|=
name|message
operator|.
name|getContent
argument_list|(
name|Writer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"The OutputStream and Writer should not both be null "
argument_list|,
name|os
operator|!=
literal|null
operator|||
name|writer
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Sends several messages and verifies the results. The service sends the message to itself. So it should      * always receive the result      *       * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testTimeoutOnReceive
parameter_list|()
throws|throws
name|Exception
block|{
name|EndpointInfo
name|ei
init|=
name|setupServiceInfo
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
name|WSDL
argument_list|,
literal|"HelloWorldServiceLoop"
argument_list|,
literal|"HelloWorldPortLoop"
argument_list|)
decl_stmt|;
name|JMSConduit
name|conduit
init|=
name|setupJMSConduitWithObserver
argument_list|(
name|ei
argument_list|)
decl_stmt|;
comment|// If the system is extremely fast. The message could still get through
name|conduit
operator|.
name|getJmsConfig
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
try|try
block|{
name|sendMessageSync
argument_list|(
name|conduit
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected a timeout here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Timeout receiving message with correlationId"
argument_list|)
condition|)
block|{
throw|throw
name|e
throw|;
block|}
block|}
finally|finally
block|{
name|conduit
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

