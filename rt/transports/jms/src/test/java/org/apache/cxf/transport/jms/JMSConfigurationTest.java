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
name|jms
operator|.
name|ConnectionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|ActiveMQConnectionFactory
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|connection
operator|.
name|SingleConnectionFactory
import|;
end_import

begin_class
specifier|public
class|class
name|JMSConfigurationTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testDestroyAutoWrappedConnectionFactory
parameter_list|()
block|{
comment|// create an empty configuration
name|JMSConfiguration
name|jmsConfig
init|=
operator|new
name|JMSConfiguration
argument_list|()
decl_stmt|;
comment|// create the connectionFactory to wrap
name|ActiveMQConnectionFactory
name|amqCf
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|()
decl_stmt|;
name|jmsConfig
operator|.
name|setConnectionFactory
argument_list|(
name|amqCf
argument_list|)
expr_stmt|;
comment|// get the connectionFactory
name|assertTrue
argument_list|(
literal|"Should get the instance of ActiveMQConnectionFactory"
argument_list|,
name|jmsConfig
operator|.
name|getConnectionFactory
argument_list|()
operator|instanceof
name|ActiveMQConnectionFactory
argument_list|)
expr_stmt|;
comment|// get the wrapped connectionFactory
name|ConnectionFactory
name|wrappingCf
init|=
name|jmsConfig
operator|.
name|getOrCreateWrappedConnectionFactory
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should get the instance of SingleConnectionFactory"
argument_list|,
name|wrappingCf
operator|instanceof
name|SingleConnectionFactory
argument_list|)
expr_stmt|;
name|SingleConnectionFactory
name|scf
init|=
operator|(
name|SingleConnectionFactory
operator|)
name|wrappingCf
decl_stmt|;
name|assertSame
argument_list|(
literal|"Should get the wrapped ActiveMQConnectionFactory"
argument_list|,
name|amqCf
argument_list|,
name|scf
operator|.
name|getTargetConnectionFactory
argument_list|()
argument_list|)
expr_stmt|;
comment|// destroy the wrapping
name|jmsConfig
operator|.
name|destroyWrappedConnectionFactory
argument_list|()
expr_stmt|;
comment|// get the wrapping cf
name|assertNull
argument_list|(
literal|"Should be null after destroy"
argument_list|,
name|jmsConfig
operator|.
name|getWrappedConnectionFactory
argument_list|()
argument_list|)
expr_stmt|;
comment|// original connectionFactory should be unchanged
name|assertSame
argument_list|(
literal|"Should be the same with original connectionFactory"
argument_list|,
name|amqCf
argument_list|,
name|jmsConfig
operator|.
name|getConnectionFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDestroySuppliedConnectionFactory
parameter_list|()
block|{
comment|// create an empty configuration
name|JMSConfiguration
name|jmsConfig
init|=
operator|new
name|JMSConfiguration
argument_list|()
decl_stmt|;
comment|// create the connectionFactory to wrap
name|ActiveMQConnectionFactory
name|amqCf
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|()
decl_stmt|;
comment|// wrap into SingleConnectionFactory
name|SingleConnectionFactory
name|scf
init|=
operator|new
name|SingleConnectionFactory
argument_list|(
name|amqCf
argument_list|)
decl_stmt|;
comment|// set the connectionFactory to reuse
name|jmsConfig
operator|.
name|setConnectionFactory
argument_list|(
name|scf
argument_list|)
expr_stmt|;
comment|// get the connectionFactory
name|assertTrue
argument_list|(
literal|"Should get the instance of SingleConnectionFactory"
argument_list|,
name|jmsConfig
operator|.
name|getConnectionFactory
argument_list|()
operator|instanceof
name|SingleConnectionFactory
argument_list|)
expr_stmt|;
comment|// get the wrapped connectionFactory
name|ConnectionFactory
name|wrappingCf
init|=
name|jmsConfig
operator|.
name|getOrCreateWrappedConnectionFactory
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should get the instance of SingleConnectionFactory"
argument_list|,
name|wrappingCf
operator|instanceof
name|SingleConnectionFactory
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"Should not be wrapped again"
argument_list|,
name|scf
argument_list|,
name|wrappingCf
argument_list|)
expr_stmt|;
comment|// destroy the wrapping
name|jmsConfig
operator|.
name|destroyWrappedConnectionFactory
argument_list|()
expr_stmt|;
comment|// get the wrapping cf
name|assertNotNull
argument_list|(
literal|"Should be not null after destroy"
argument_list|,
name|jmsConfig
operator|.
name|getWrappedConnectionFactory
argument_list|()
argument_list|)
expr_stmt|;
comment|// original connectionFactory should be unchanged
name|assertSame
argument_list|(
literal|"Should be the same with supplied connectionFactory"
argument_list|,
name|scf
argument_list|,
name|jmsConfig
operator|.
name|getConnectionFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

