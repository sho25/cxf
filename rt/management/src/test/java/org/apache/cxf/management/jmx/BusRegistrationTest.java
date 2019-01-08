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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

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
name|MBeanServer
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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|InstrumentationManager
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
name|ManagementConstants
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
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
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
name|assertTrue
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|BusRegistrationTest
block|{
specifier|private
name|Bus
name|serverBus
decl_stmt|;
specifier|private
name|Bus
name|clientBus
decl_stmt|;
specifier|private
name|InstrumentationManager
name|serverIM
decl_stmt|;
specifier|private
name|InstrumentationManager
name|clientIM
decl_stmt|;
specifier|private
name|boolean
name|ready
decl_stmt|;
specifier|private
name|boolean
name|running
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{     }
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|clientBus
operator|!=
literal|null
condition|)
block|{
name|clientBus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|serverBus
operator|!=
literal|null
condition|)
block|{
name|serverBus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRegisterMultipleBuses
parameter_list|()
throws|throws
name|Exception
block|{
comment|// classic external IM-bean
name|testRegisterMultipleBuses
argument_list|(
literal|"managed-spring.xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRegisterMultipleBuses2
parameter_list|()
throws|throws
name|Exception
block|{
comment|// integrated IM configuration in bus
name|testRegisterMultipleBuses
argument_list|(
literal|"managed-spring2.xml"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testRegisterMultipleBuses
parameter_list|(
name|String
name|conf
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|SpringBusFactory
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|serverBus
operator|=
name|factory
operator|.
name|createBus
argument_list|(
name|conf
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF-Test-Bus"
argument_list|,
name|serverBus
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|serverIM
operator|=
name|serverBus
operator|.
name|getExtension
argument_list|(
name|InstrumentationManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Instrumentation Manager should not be null"
argument_list|,
name|serverIM
argument_list|)
expr_stmt|;
name|Thread
name|t
init|=
operator|new
name|Thread
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|clientBus
operator|=
name|factory
operator|.
name|createBus
argument_list|(
literal|"no-connector-spring.xml"
argument_list|)
expr_stmt|;
name|clientIM
operator|=
name|clientBus
operator|.
name|getExtension
argument_list|(
name|InstrumentationManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Instrumentation Manager should not be null"
argument_list|,
name|clientIM
argument_list|)
expr_stmt|;
name|ready
operator|=
literal|true
expr_stmt|;
name|running
operator|=
literal|true
expr_stmt|;
while|while
condition|(
name|running
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore and leave
name|running
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|t
operator|.
name|start
argument_list|()
expr_stmt|;
while|while
condition|(
operator|!
name|ready
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|MBeanServer
name|mbs
init|=
name|serverIM
operator|.
name|getMBeanServer
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"MBeanServer should be available."
argument_list|,
name|mbs
argument_list|)
expr_stmt|;
name|MBeanServer
name|mbs2
init|=
name|clientIM
operator|.
name|getMBeanServer
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"MBeanServer should be available."
argument_list|,
name|mbs2
argument_list|)
expr_stmt|;
comment|// check if these servers refer to the same server
name|assertEquals
argument_list|(
literal|"There should be one MBeanServer"
argument_list|,
name|mbs
argument_list|,
name|mbs2
argument_list|)
expr_stmt|;
comment|// check both server and client bus can be found from this server
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|s
decl_stmt|;
name|ObjectName
name|serverName
init|=
name|getObjectName
argument_list|(
name|serverBus
argument_list|)
decl_stmt|;
name|s
operator|=
name|mbs
operator|.
name|queryNames
argument_list|(
name|serverName
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"sever-side bus should be found"
argument_list|,
name|s
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|ObjectName
name|clientName
init|=
name|getObjectName
argument_list|(
name|clientBus
argument_list|)
decl_stmt|;
name|s
operator|=
name|mbs
operator|.
name|queryNames
argument_list|(
name|clientName
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"client-side bus should be found"
argument_list|,
name|s
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|running
operator|=
literal|false
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|ObjectName
name|getObjectName
parameter_list|(
name|Bus
name|bus
parameter_list|)
throws|throws
name|JMException
block|{
name|String
name|busId
init|=
name|bus
operator|.
name|getId
argument_list|()
decl_stmt|;
return|return
name|getObjectName
argument_list|(
name|busId
argument_list|,
name|bus
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|ObjectName
name|getObjectName
parameter_list|(
name|String
name|id
parameter_list|,
name|Bus
name|bus
parameter_list|)
throws|throws
name|JMException
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
name|ManagementConstants
operator|.
name|DEFAULT_DOMAIN_NAME
operator|+
literal|":"
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|BUS_ID_PROP
operator|+
literal|"="
operator|+
name|id
operator|+
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|TYPE_PROP
operator|+
literal|"=Bus,"
argument_list|)
expr_stmt|;
comment|// Added the instance id to make the ObjectName unique
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|INSTANCE_ID_PROP
operator|+
literal|"="
operator|+
name|bus
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|ObjectName
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

