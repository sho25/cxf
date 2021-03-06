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
name|jms
operator|.
name|testsuite
operator|.
name|testcases
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
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
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
name|jaxws
operator|.
name|JaxWsServerFactoryBean
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
name|systest
operator|.
name|jms
operator|.
name|AbstractVmJMSTest
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
name|systest
operator|.
name|jms
operator|.
name|Hello
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
name|systest
operator|.
name|jms
operator|.
name|HelloImpl
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
name|jms
operator|.
name|spec
operator|.
name|JMSSpecConstants
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
name|BeforeClass
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
name|JavaFirstNoWsdlTest
extends|extends
name|AbstractVmJMSTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SERVICE_ADDRESS
init|=
literal|"jms:queue:test.cxf.jmstransport.queue3?receivetTimeOut=5000"
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServer
parameter_list|()
block|{
name|startBusAndJMS
argument_list|(
name|JavaFirstNoWsdlTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|JaxWsServerFactoryBean
name|svrFactory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|svrFactory
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
name|cff
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setServiceClass
argument_list|(
name|Hello
operator|.
name|class
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setAddress
argument_list|(
name|SERVICE_ADDRESS
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setTransportId
argument_list|(
name|JMSSpecConstants
operator|.
name|SOAP_JMS_SPECIFICATION_TRANSPORTID
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setServiceBean
argument_list|(
operator|new
name|HelloImpl
argument_list|()
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSpecNoWsdlService
parameter_list|()
throws|throws
name|Exception
block|{
name|specNoWsdlService
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSpecNoWsdlServiceWithDifferentMessageType
parameter_list|()
throws|throws
name|Exception
block|{
name|specNoWsdlService
argument_list|(
literal|"text"
argument_list|)
expr_stmt|;
name|specNoWsdlService
argument_list|(
literal|"byte"
argument_list|)
expr_stmt|;
name|specNoWsdlService
argument_list|(
literal|"binary"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|specNoWsdlService
parameter_list|(
name|String
name|messageType
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|address
init|=
name|SERVICE_ADDRESS
operator|+
operator|(
operator|(
name|messageType
operator|!=
literal|null
operator|)
condition|?
literal|"&messageType="
operator|+
name|messageType
else|:
literal|""
operator|)
decl_stmt|;
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|factory
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
name|cff
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setTransportId
argument_list|(
name|JMSSpecConstants
operator|.
name|SOAP_JMS_SPECIFICATION_TRANSPORTID
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|Hello
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|Hello
name|client
init|=
operator|(
name|Hello
operator|)
name|markForClose
argument_list|(
name|factory
operator|.
name|create
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|reply
init|=
name|client
operator|.
name|sayHi
argument_list|(
literal|" HI"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"get HI"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

