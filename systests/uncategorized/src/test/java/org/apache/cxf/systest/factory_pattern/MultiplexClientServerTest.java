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
name|factory_pattern
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|wsaddressing
operator|.
name|W3CEndpointReference
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
name|factory_pattern
operator|.
name|Number
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
name|factory_pattern
operator|.
name|NumberFactory
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
name|factory_pattern
operator|.
name|NumberFactoryService
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
name|factory_pattern
operator|.
name|NumberService
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
name|frontend
operator|.
name|ClientProxy
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
name|ServiceImpl
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
name|support
operator|.
name|ServiceDelegateAccessor
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
name|AbstractBusTestServerBase
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
name|EmbeddedJMSBrokerLauncher
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
name|MultiplexClientServerTest
extends|extends
name|AbstractBusClientServerTestBase
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
name|MultiplexClientServerTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FACTORY_ADDRESS
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/NumberFactoryService/NumberFactoryPort"
decl_stmt|;
specifier|static
specifier|final
name|String
name|JMS_PORT
init|=
name|EmbeddedJMSBrokerLauncher
operator|.
name|PORT
decl_stmt|;
specifier|public
specifier|static
class|class
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
name|Endpoint
name|ep
decl_stmt|;
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|Object
name|implementor
init|=
operator|new
name|NumberFactoryImpl
argument_list|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|,
name|PORT
argument_list|)
decl_stmt|;
name|ep
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|FACTORY_ADDRESS
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|ep
operator|.
name|stop
argument_list|()
expr_stmt|;
name|ep
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
try|try
block|{
name|Server
name|s
init|=
operator|new
name|Server
argument_list|()
decl_stmt|;
name|s
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"done!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|createStaticBus
argument_list|(
literal|"org/apache/cxf/systest/factory_pattern/cxf_multiplex.xml"
argument_list|)
expr_stmt|;
comment|// requires ws-a support to propagate reference parameters
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"org.apache.activemq.default.directory.prefix"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
literal|"org.apache.activemq.default.directory.prefix"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"org.apache.activemq.default.directory.prefix"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.util.logging.config.file"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
literal|"java.util.logging.config.file"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.util.logging.config.file"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|EmbeddedJMSBrokerLauncher
operator|.
name|class
argument_list|,
name|props
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|close
parameter_list|(
name|Object
name|o
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|o
operator|instanceof
name|Closeable
condition|)
block|{
operator|(
operator|(
name|Closeable
operator|)
name|o
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithGetPortExtensionHttp
parameter_list|()
throws|throws
name|Exception
block|{
name|NumberFactoryService
name|service
init|=
operator|new
name|NumberFactoryService
argument_list|()
decl_stmt|;
name|NumberFactory
name|factory
init|=
name|service
operator|.
name|getNumberFactoryPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|factory
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|NumberService
name|numService
init|=
operator|new
name|NumberService
argument_list|()
decl_stmt|;
name|ServiceImpl
name|serviceImpl
init|=
name|ServiceDelegateAccessor
operator|.
name|get
argument_list|(
name|numService
argument_list|)
decl_stmt|;
name|W3CEndpointReference
name|numberTwoRef
init|=
name|factory
operator|.
name|create
argument_list|(
literal|"20"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"reference"
argument_list|,
name|numberTwoRef
argument_list|)
expr_stmt|;
name|Number
name|num
init|=
name|serviceImpl
operator|.
name|getPort
argument_list|(
name|numberTwoRef
argument_list|,
name|Number
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"20 is even"
argument_list|,
name|num
operator|.
name|isEven
argument_list|()
operator|.
name|isEven
argument_list|()
argument_list|)
expr_stmt|;
name|close
argument_list|(
name|num
argument_list|)
expr_stmt|;
name|W3CEndpointReference
name|numberTwentyThreeRef
init|=
name|factory
operator|.
name|create
argument_list|(
literal|"23"
argument_list|)
decl_stmt|;
name|num
operator|=
name|serviceImpl
operator|.
name|getPort
argument_list|(
name|numberTwentyThreeRef
argument_list|,
name|Number
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"23 is not even"
argument_list|,
operator|!
name|num
operator|.
name|isEven
argument_list|()
operator|.
name|isEven
argument_list|()
argument_list|)
expr_stmt|;
name|close
argument_list|(
name|num
argument_list|)
expr_stmt|;
name|close
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithGetPortExtensionOverJMS
parameter_list|()
throws|throws
name|Exception
block|{
name|NumberFactoryService
name|service
init|=
operator|new
name|NumberFactoryService
argument_list|()
decl_stmt|;
name|NumberFactory
name|factory
init|=
name|service
operator|.
name|getNumberFactoryPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|factory
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
comment|// use values>= 30 to create JMS eprs - see NumberFactoryImpl.create
comment|// verify it is JMS, 999 for JMS will throw a fault
name|W3CEndpointReference
name|ref
init|=
name|factory
operator|.
name|create
argument_list|(
literal|"999"
argument_list|)
decl_stmt|;
name|String
name|s
init|=
name|NumberService
operator|.
name|WSDL_LOCATION
operator|.
name|toString
argument_list|()
decl_stmt|;
name|EmbeddedJMSBrokerLauncher
operator|.
name|updateWsdlExtensors
argument_list|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|,
name|s
argument_list|)
expr_stmt|;
name|NumberService
name|numService
init|=
operator|new
name|NumberService
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"reference"
argument_list|,
name|ref
argument_list|)
expr_stmt|;
name|ServiceImpl
name|serviceImpl
init|=
name|ServiceDelegateAccessor
operator|.
name|get
argument_list|(
name|numService
argument_list|)
decl_stmt|;
name|Number
name|num
init|=
name|serviceImpl
operator|.
name|getPort
argument_list|(
name|ref
argument_list|,
name|Number
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|num
operator|.
name|isEven
argument_list|()
operator|.
name|isEven
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"there should be a fault on val 999"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|expected
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"match on exception message "
operator|+
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"999"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|num
argument_list|)
operator|.
name|getConduit
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|ref
operator|=
name|factory
operator|.
name|create
argument_list|(
literal|"37"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"reference"
argument_list|,
name|ref
argument_list|)
expr_stmt|;
name|num
operator|=
name|serviceImpl
operator|.
name|getPort
argument_list|(
name|ref
argument_list|,
name|Number
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"37 is not even"
argument_list|,
operator|!
name|num
operator|.
name|isEven
argument_list|()
operator|.
name|isEven
argument_list|()
argument_list|)
expr_stmt|;
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|num
argument_list|)
operator|.
name|getConduit
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|factory
argument_list|)
operator|.
name|getConduit
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

