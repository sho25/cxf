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
name|management
operator|.
name|interceptor
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|Bus
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
name|BusFactory
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
name|endpoint
operator|.
name|Endpoint
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
name|management
operator|.
name|counters
operator|.
name|CounterRepository
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
name|management
operator|.
name|counters
operator|.
name|MessageHandlingTimeRecorder
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
name|Exchange
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
name|service
operator|.
name|Service
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
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|OperationInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
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
name|Before
import|;
end_import

begin_class
specifier|public
class|class
name|AbstractMessageResponseTestBase
extends|extends
name|Assert
block|{
specifier|protected
specifier|static
specifier|final
name|QName
name|SERVICE_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://org.apache.cxf"
argument_list|,
literal|"hello"
argument_list|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|QName
name|OPERATION_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://org.apache.cxf"
argument_list|,
literal|"world"
argument_list|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|QName
name|PORT_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://org.apache.cxf"
argument_list|,
literal|"port"
argument_list|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|CLIENT_SERVICE_ONAME
init|=
literal|"org.apache.cxf:type=Performance.Counter.Client,bus.id=cxf,service=\""
operator|+
name|SERVICE_NAME
operator|.
name|toString
argument_list|()
operator|+
literal|"\",port=\""
operator|+
name|PORT_NAME
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"\""
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|SERVER_SERVICE_ONAME
init|=
literal|"org.apache.cxf:type=Performance.Counter.Server,bus.id=cxf,service=\""
operator|+
name|SERVICE_NAME
operator|.
name|toString
argument_list|()
operator|+
literal|"\",port=\""
operator|+
name|PORT_NAME
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"\""
decl_stmt|;
specifier|protected
name|ObjectName
name|clientServiceCounterOName
decl_stmt|;
specifier|protected
name|ObjectName
name|serverServiceCounterOName
decl_stmt|;
specifier|protected
name|ObjectName
name|clientOperationCounterOName
decl_stmt|;
specifier|protected
name|ObjectName
name|serverOperationCounterOName
decl_stmt|;
specifier|protected
name|Bus
name|bus
decl_stmt|;
specifier|protected
name|Message
name|message
decl_stmt|;
specifier|protected
name|Exchange
name|exchange
decl_stmt|;
specifier|protected
name|CounterRepository
name|cRepository
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|message
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
expr_stmt|;
name|exchange
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
expr_stmt|;
name|bus
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
expr_stmt|;
name|cRepository
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|CounterRepository
operator|.
name|class
argument_list|)
expr_stmt|;
name|clientServiceCounterOName
operator|=
operator|new
name|ObjectName
argument_list|(
name|CLIENT_SERVICE_ONAME
argument_list|)
expr_stmt|;
name|serverServiceCounterOName
operator|=
operator|new
name|ObjectName
argument_list|(
name|SERVER_SERVICE_ONAME
argument_list|)
expr_stmt|;
name|clientOperationCounterOName
operator|=
operator|new
name|ObjectName
argument_list|(
name|CLIENT_SERVICE_ONAME
operator|+
literal|",operation=\""
operator|+
name|OPERATION_NAME
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|serverOperationCounterOName
operator|=
operator|new
name|ObjectName
argument_list|(
name|SERVER_SERVICE_ONAME
operator|+
literal|",operation=\""
operator|+
name|OPERATION_NAME
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setupCounterRepository
parameter_list|(
name|boolean
name|increase
parameter_list|,
name|boolean
name|isClient
parameter_list|)
block|{
name|ObjectName
name|serviceCounterOName
decl_stmt|;
name|ObjectName
name|operationCounterOName
decl_stmt|;
if|if
condition|(
name|isClient
condition|)
block|{
name|serviceCounterOName
operator|=
name|clientServiceCounterOName
expr_stmt|;
name|operationCounterOName
operator|=
name|clientOperationCounterOName
expr_stmt|;
block|}
else|else
block|{
name|serviceCounterOName
operator|=
name|serverServiceCounterOName
expr_stmt|;
name|operationCounterOName
operator|=
name|serverOperationCounterOName
expr_stmt|;
block|}
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getExtension
argument_list|(
name|CounterRepository
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|cRepository
argument_list|)
expr_stmt|;
if|if
condition|(
name|increase
condition|)
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Bus
operator|.
name|DEFAULT_BUS_ID
argument_list|)
expr_stmt|;
name|cRepository
operator|.
name|increaseCounter
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
name|serviceCounterOName
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|isA
argument_list|(
name|MessageHandlingTimeRecorder
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|cRepository
operator|.
name|increaseCounter
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
name|operationCounterOName
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|isA
argument_list|(
name|MessageHandlingTimeRecorder
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|cRepository
argument_list|)
expr_stmt|;
block|}
name|EasyMock
operator|.
name|replay
argument_list|(
name|bus
argument_list|)
expr_stmt|;
comment|// increase the number
block|}
specifier|protected
name|void
name|setupExchangeForMessage
parameter_list|()
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|exchange
operator|.
name|get
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|Service
name|service
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Service
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|service
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|SERVICE_NAME
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|exchange
operator|.
name|get
argument_list|(
name|Service
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|EndpointInfo
name|endpointInfo
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|EndpointInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpointInfo
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|PORT_NAME
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|endpointInfo
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|exchange
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|endpointInfo
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|setupOperationForMessage
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|setupOperationForMessage
parameter_list|()
block|{
name|OperationInfo
name|op
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|op
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|OPERATION_NAME
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|exchange
operator|.
name|get
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|op
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|op
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

