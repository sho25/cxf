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
name|jmx
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|JMException
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|management
operator|.
name|jmx
operator|.
name|export
operator|.
name|AnnotationTestInstrumentation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|JMXManagedComponentManagerTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|String
name|NAME_ATTRIBUTE
init|=
literal|"Name"
decl_stmt|;
specifier|private
name|InstrumentationManagerImpl
name|manager
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
name|manager
operator|=
operator|new
name|InstrumentationManagerImpl
argument_list|()
expr_stmt|;
name|manager
operator|.
name|setDaemon
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|manager
operator|.
name|setThreaded
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|manager
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|manager
operator|.
name|setJMXServiceURL
argument_list|(
literal|"service:jmx:rmi:///jndi/rmi://localhost:9913/jmxrmi"
argument_list|)
expr_stmt|;
name|manager
operator|.
name|init
argument_list|()
expr_stmt|;
comment|//Wait for MBeanServer connector to be initialized on separate thread.
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|manager
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRegisterInstrumentation
parameter_list|()
throws|throws
name|Exception
block|{
comment|//manager.setDaemon(false);
comment|//manager.setThreaded(false);
comment|//manager.setJMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9913/jmxrmi");
comment|//manager.init();
name|AnnotationTestInstrumentation
name|im
init|=
operator|new
name|AnnotationTestInstrumentation
argument_list|()
decl_stmt|;
name|ObjectName
name|name
init|=
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.cxf:type=foo,name=bar"
argument_list|)
decl_stmt|;
name|im
operator|.
name|setName
argument_list|(
literal|"John Smith"
argument_list|)
expr_stmt|;
name|manager
operator|.
name|register
argument_list|(
name|im
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|Object
name|val
init|=
name|manager
operator|.
name|getMBeanServer
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
name|NAME_ATTRIBUTE
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Incorrect result"
argument_list|,
literal|"John Smith"
argument_list|,
name|val
argument_list|)
expr_stmt|;
try|try
block|{
name|manager
operator|.
name|register
argument_list|(
name|im
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Registering with existing name should fail."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMException
name|jmex
parameter_list|)
block|{
comment|//Expected
block|}
name|manager
operator|.
name|register
argument_list|(
name|im
argument_list|,
name|name
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|val
operator|=
name|manager
operator|.
name|getMBeanServer
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
name|NAME_ATTRIBUTE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Incorrect result"
argument_list|,
literal|"John Smith"
argument_list|,
name|val
argument_list|)
expr_stmt|;
name|manager
operator|.
name|unregister
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|im
operator|.
name|setName
argument_list|(
literal|"Foo Bar"
argument_list|)
expr_stmt|;
name|name
operator|=
name|manager
operator|.
name|register
argument_list|(
name|im
argument_list|)
expr_stmt|;
name|val
operator|=
name|manager
operator|.
name|getMBeanServer
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
name|NAME_ATTRIBUTE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Incorrect result"
argument_list|,
literal|"Foo Bar"
argument_list|,
name|val
argument_list|)
expr_stmt|;
try|try
block|{
name|manager
operator|.
name|register
argument_list|(
name|im
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Registering with existing name should fail."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMException
name|jmex
parameter_list|)
block|{
comment|//Expected
block|}
name|name
operator|=
name|manager
operator|.
name|register
argument_list|(
name|im
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|val
operator|=
name|manager
operator|.
name|getMBeanServer
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
name|NAME_ATTRIBUTE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Incorrect result"
argument_list|,
literal|"Foo Bar"
argument_list|,
name|val
argument_list|)
expr_stmt|;
name|manager
operator|.
name|unregister
argument_list|(
name|im
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRegisterStandardMBean
parameter_list|()
throws|throws
name|Exception
block|{
name|HelloWorld
name|hw
init|=
operator|new
name|HelloWorld
argument_list|()
decl_stmt|;
name|ObjectName
name|name
init|=
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.cxf:type=foo,name=bar"
argument_list|)
decl_stmt|;
name|manager
operator|.
name|register
argument_list|(
name|hw
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|String
name|result
init|=
operator|(
name|String
operator|)
name|manager
operator|.
name|getMBeanServer
argument_list|()
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"sayHi"
argument_list|,
operator|new
name|Object
index|[
literal|0
index|]
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wazzzuuup!"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

