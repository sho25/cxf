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
name|osgi
operator|.
name|itests
operator|.
name|soap
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

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
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Server
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
name|testutil
operator|.
name|common
operator|.
name|TestUtil
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
name|ConnectionFactoryFeature
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleActivator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_class
specifier|public
class|class
name|JmsTestActivator
implements|implements
name|BundleActivator
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|JmsTestActivator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Server
name|server
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|(
name|BundleContext
name|bc
parameter_list|)
throws|throws
name|Exception
block|{
name|ConnectionFactory
name|connectionFactory
init|=
name|createConnectionFactory
argument_list|()
decl_stmt|;
name|server
operator|=
name|publishService
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Server
name|publishService
parameter_list|(
name|ConnectionFactory
name|connectionFactory
parameter_list|)
block|{
name|JaxWsServerFactoryBean
name|factory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|Greeter
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
literal|"jms:queue:greeter"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setFeatures
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|ConnectionFactoryFeature
argument_list|(
name|connectionFactory
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceBean
argument_list|(
operator|new
name|GreeterImpl
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|factory
operator|.
name|create
argument_list|()
return|;
block|}
specifier|private
name|ActiveMQConnectionFactory
name|createConnectionFactory
parameter_list|()
block|{
name|ActiveMQConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://JmsServiceTest"
argument_list|)
decl_stmt|;
name|connectionFactory
operator|.
name|setUserName
argument_list|(
literal|"karaf"
argument_list|)
expr_stmt|;
name|connectionFactory
operator|.
name|setPassword
argument_list|(
literal|"karaf"
argument_list|)
expr_stmt|;
return|return
name|connectionFactory
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|(
name|BundleContext
name|arg0
parameter_list|)
throws|throws
name|Exception
block|{
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

