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
name|helpers
operator|.
name|JavaUtils
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
name|osgi
operator|.
name|itests
operator|.
name|CXFOSGiTestSupport
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
name|Constants
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|OptionUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|PaxExam
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|spi
operator|.
name|reactors
operator|.
name|ExamReactorStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|spi
operator|.
name|reactors
operator|.
name|PerClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundles
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|maven
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|provision
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|features
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|PaxExam
operator|.
name|class
argument_list|)
annotation|@
name|ExamReactorStrategy
argument_list|(
name|PerClass
operator|.
name|class
argument_list|)
specifier|public
class|class
name|JmsServiceTest
extends|extends
name|CXFOSGiTestSupport
block|{
annotation|@
name|Test
specifier|public
name|void
name|testJmsEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|Greeter
name|greeter
init|=
name|greeterJms
argument_list|()
decl_stmt|;
name|String
name|res
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"Chris"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hi Chris"
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|Greeter
name|greeterJms
parameter_list|()
block|{
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
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
name|ConnectionFactory
name|connectionFactory
init|=
name|createConnectionFactory
argument_list|()
decl_stmt|;
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
return|return
name|factory
operator|.
name|create
argument_list|(
name|Greeter
operator|.
name|class
argument_list|)
return|;
block|}
specifier|private
specifier|static
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
name|Configuration
specifier|public
name|Option
index|[]
name|config
parameter_list|()
block|{
return|return
name|OptionUtils
operator|.
name|combine
argument_list|(
name|cxfBaseConfig
argument_list|()
argument_list|,
name|features
argument_list|(
name|cxfUrl
argument_list|,
literal|"cxf-jaxws"
argument_list|,
literal|"cxf-transports-jms"
argument_list|)
argument_list|,
name|features
argument_list|(
name|maven
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.activemq"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"activemq-karaf"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
operator|.
name|type
argument_list|(
literal|"xml"
argument_list|)
operator|.
name|classifier
argument_list|(
literal|"features-core"
argument_list|)
argument_list|,
literal|"cxf-jackson"
argument_list|,
literal|"activemq-client"
argument_list|)
argument_list|,
name|provision
argument_list|(
name|serviceBundle
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|InputStream
name|serviceBundle
parameter_list|()
block|{
if|if
condition|(
name|JavaUtils
operator|.
name|isJava11Compatible
argument_list|()
condition|)
block|{
return|return
name|TinyBundles
operator|.
name|bundle
argument_list|()
operator|.
name|add
argument_list|(
name|JmsTestActivator
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|Greeter
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|GreeterImpl
operator|.
name|class
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_ACTIVATOR
argument_list|,
name|JmsTestActivator
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|set
argument_list|(
literal|"Require-Capability"
argument_list|,
literal|"osgi.ee;filter:=\"(&(osgi.ee=JavaSE)(version=11))\""
argument_list|)
operator|.
name|build
argument_list|(
name|TinyBundles
operator|.
name|withBnd
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|TinyBundles
operator|.
name|bundle
argument_list|()
operator|.
name|add
argument_list|(
name|JmsTestActivator
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|Greeter
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|GreeterImpl
operator|.
name|class
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_ACTIVATOR
argument_list|,
name|JmsTestActivator
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|build
argument_list|(
name|TinyBundles
operator|.
name|withBnd
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

