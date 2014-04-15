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
name|javax
operator|.
name|naming
operator|.
name|NamingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|TransactionManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|xa
operator|.
name|XAException
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
name|configuration
operator|.
name|ConfiguredBeanLocator
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
name|transport
operator|.
name|jms
operator|.
name|uri
operator|.
name|JMSEndpoint
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
name|uri
operator|.
name|MyBeanLocator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|transaction
operator|.
name|manager
operator|.
name|GeronimoTransactionManager
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

begin_class
specifier|public
class|class
name|JMSConfigFactoryTest
extends|extends
name|AbstractJMSTester
block|{
annotation|@
name|Test
specifier|public
name|void
name|testUsernameAndPassword
parameter_list|()
throws|throws
name|Exception
block|{
name|EndpointInfo
name|ei
init|=
name|setupServiceInfo
argument_list|(
literal|"HelloWorldService"
argument_list|,
literal|"HelloWorldPort"
argument_list|)
decl_stmt|;
name|JMSConfiguration
name|config
init|=
name|JMSConfigFactory
operator|.
name|createFromEndpointInfo
argument_list|(
name|bus
argument_list|,
name|ei
argument_list|,
name|target
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"User name does not match."
argument_list|,
literal|"testUser"
argument_list|,
name|config
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Password does not match."
argument_list|,
literal|"testPassword"
argument_list|,
name|config
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTransactionManagerFromBus
parameter_list|()
throws|throws
name|XAException
throws|,
name|NamingException
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createBus
argument_list|()
decl_stmt|;
name|ConfiguredBeanLocator
name|cbl
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
decl_stmt|;
name|MyBeanLocator
name|mybl
init|=
operator|new
name|MyBeanLocator
argument_list|(
name|cbl
argument_list|)
decl_stmt|;
name|bus
operator|.
name|setExtension
argument_list|(
name|mybl
argument_list|,
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
expr_stmt|;
name|TransactionManager
name|tmExpected
init|=
operator|new
name|GeronimoTransactionManager
argument_list|()
decl_stmt|;
name|mybl
operator|.
name|register
argument_list|(
literal|"tm"
argument_list|,
name|tmExpected
argument_list|)
expr_stmt|;
name|tmByName
argument_list|(
name|bus
argument_list|,
name|tmExpected
argument_list|)
expr_stmt|;
name|tmByClass
argument_list|(
name|bus
argument_list|,
name|tmExpected
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|tmByName
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|TransactionManager
name|tmExpected
parameter_list|)
block|{
name|JMSEndpoint
name|endpoint
init|=
operator|new
name|JMSEndpoint
argument_list|(
literal|"jms:queue:Foo.Bar?jndiTransactionManagerName=tm"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"tm"
argument_list|,
name|endpoint
operator|.
name|getJndiTransactionManagerName
argument_list|()
argument_list|)
expr_stmt|;
name|JMSConfiguration
name|jmsConfig
init|=
name|JMSConfigFactory
operator|.
name|createFromEndpoint
argument_list|(
name|bus
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
name|TransactionManager
name|tm
init|=
name|jmsConfig
operator|.
name|getTransactionManager
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|tmExpected
argument_list|,
name|tm
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|tmByClass
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|TransactionManager
name|tmExpected
parameter_list|)
block|{
name|JMSEndpoint
name|endpoint
init|=
operator|new
name|JMSEndpoint
argument_list|(
literal|"jms:queue:Foo.Bar"
argument_list|)
decl_stmt|;
name|JMSConfiguration
name|jmsConfig
init|=
name|JMSConfigFactory
operator|.
name|createFromEndpoint
argument_list|(
name|bus
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
name|TransactionManager
name|tm
init|=
name|jmsConfig
operator|.
name|getTransactionManager
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|tmExpected
argument_list|,
name|tm
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTransactionManagerFromJndi
parameter_list|()
throws|throws
name|XAException
throws|,
name|NamingException
block|{
name|JMSEndpoint
name|endpoint
init|=
operator|new
name|JMSEndpoint
argument_list|(
literal|"jms:queue:Foo.Bar?jndiTransactionManagerName=java:/comp/TransactionManager"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"java:/comp/TransactionManager"
argument_list|,
name|endpoint
operator|.
name|getJndiTransactionManagerName
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO Check JNDI lookup
block|}
block|}
end_class

end_unit

