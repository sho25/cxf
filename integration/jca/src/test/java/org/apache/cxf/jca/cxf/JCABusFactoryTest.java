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
name|jca
operator|.
name|cxf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|ResourceException
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
name|jca
operator|.
name|core
operator|.
name|resourceadapter
operator|.
name|ResourceAdapterInternalException
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
name|test
operator|.
name|AbstractCXFTest
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
name|Ignore
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
name|assertSame
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|JCABusFactoryTest
extends|extends
name|AbstractCXFTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSetAppserverClassLoader
parameter_list|()
block|{
name|ClassLoader
name|loader
init|=
operator|new
name|DummyClassLoader
argument_list|()
decl_stmt|;
name|JCABusFactory
name|bf
init|=
operator|new
name|JCABusFactory
argument_list|(
operator|new
name|ManagedConnectionFactoryImpl
argument_list|()
argument_list|)
decl_stmt|;
name|bf
operator|.
name|setAppserverClassLoader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"Checking appserverClassLoader."
argument_list|,
name|loader
argument_list|,
name|bf
operator|.
name|getAppserverClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadNonexistentProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedConnectionFactoryImpl
name|mcf
init|=
operator|new
name|ManagedConnectionFactoryImpl
argument_list|()
decl_stmt|;
name|JCABusFactory
name|jcaBusFactory
init|=
operator|new
name|JCABusFactory
argument_list|(
name|mcf
argument_list|)
decl_stmt|;
try|try
block|{
name|jcaBusFactory
operator|.
name|loadProperties
argument_list|(
operator|new
name|File
argument_list|(
literal|"/rubbish_name.properties"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expect an exception ."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceException
name|re
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Cause is FileNotFoundException, cause: "
operator|+
name|re
operator|.
name|getCause
argument_list|()
argument_list|,
name|re
operator|.
name|getCause
argument_list|()
operator|instanceof
name|FileNotFoundException
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvalidMonitorConfigNoPropsURL
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedConnectionFactoryImpl
name|mcf
init|=
operator|new
name|ManagedConnectionFactoryImpl
argument_list|()
decl_stmt|;
name|mcf
operator|.
name|setMonitorEJBServiceProperties
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|JCABusFactory
name|jcaBusFactory
init|=
operator|new
name|JCABusFactory
argument_list|(
name|mcf
argument_list|)
decl_stmt|;
try|try
block|{
name|Bus
name|mockBus
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
decl_stmt|;
name|jcaBusFactory
operator|.
name|setBus
argument_list|(
name|mockBus
argument_list|)
expr_stmt|;
name|jcaBusFactory
operator|.
name|initializeServants
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"exception expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceAdapterInternalException
name|re
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"EJBServiceProperties is not set."
argument_list|,
name|re
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"EJBServicePropertiesURL is not set"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Ignore
annotation|@
name|Test
specifier|public
name|void
name|testInitServants
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedConnectionFactoryImpl
name|mcf
init|=
operator|new
name|ManagedConnectionFactoryImpl
argument_list|()
decl_stmt|;
comment|//get resource
name|URL
name|propFile
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/ejb_servants.properties"
argument_list|)
decl_stmt|;
name|mcf
operator|.
name|setEJBServicePropertiesURL
argument_list|(
name|propFile
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|JCABusFactory
name|jcaBusFactory
init|=
operator|new
name|JCABusFactory
argument_list|(
name|mcf
argument_list|)
decl_stmt|;
name|Bus
name|mockBus
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
decl_stmt|;
name|jcaBusFactory
operator|.
name|setBus
argument_list|(
name|mockBus
argument_list|)
expr_stmt|;
name|jcaBusFactory
operator|.
name|initializeServants
argument_list|()
expr_stmt|;
block|}
block|}
end_class

begin_class
class|class
name|DummyClassLoader
extends|extends
name|ClassLoader
block|{
name|DummyClassLoader
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

