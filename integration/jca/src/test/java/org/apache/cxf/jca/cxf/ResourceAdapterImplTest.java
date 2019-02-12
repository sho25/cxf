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
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|BootstrapContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|ResourceAdapter
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
name|ResourceBean
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
name|assertFalse
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
name|assertNull
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
name|ResourceAdapterImplTest
block|{
specifier|public
name|ResourceAdapterImplTest
parameter_list|()
block|{      }
annotation|@
name|Test
specifier|public
name|void
name|testConstructorWithoutProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|ResourceAdapterImpl
name|rai
init|=
operator|new
name|ResourceAdapterImpl
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"constructed without props"
argument_list|,
name|rai
operator|instanceof
name|ResourceAdapterImpl
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"constructed without props"
argument_list|,
name|rai
operator|instanceof
name|ResourceBean
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"constructed without props"
argument_list|,
name|rai
operator|instanceof
name|ResourceAdapter
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"constructed without props"
argument_list|,
name|rai
operator|instanceof
name|Serializable
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConstructorWithProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|ResourceAdapterImpl
name|rai
init|=
operator|new
name|ResourceAdapterImpl
argument_list|(
name|props
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"constructed with props"
argument_list|,
name|rai
operator|instanceof
name|ResourceAdapterImpl
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"constructed with props"
argument_list|,
name|rai
operator|instanceof
name|ResourceBean
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"constructed with props"
argument_list|,
name|rai
operator|instanceof
name|ResourceAdapter
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"constructed with props"
argument_list|,
name|rai
operator|instanceof
name|Serializable
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSerializability
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
init|=
literal|"key"
decl_stmt|;
specifier|final
name|String
name|value
init|=
literal|"value"
decl_stmt|;
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|ResourceAdapterImpl
name|rai
init|=
operator|new
name|ResourceAdapterImpl
argument_list|(
name|props
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"before serialized, key is set"
argument_list|,
name|rai
operator|.
name|getPluginProps
argument_list|()
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ObjectOutputStream
name|oos
init|=
operator|new
name|ObjectOutputStream
argument_list|(
name|baos
argument_list|)
decl_stmt|;
name|oos
operator|.
name|writeObject
argument_list|(
name|rai
argument_list|)
expr_stmt|;
name|byte
index|[]
name|buf
init|=
name|baos
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|oos
operator|.
name|close
argument_list|()
expr_stmt|;
name|baos
operator|.
name|close
argument_list|()
expr_stmt|;
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|buf
argument_list|)
decl_stmt|;
name|ObjectInputStream
name|ois
init|=
operator|new
name|ObjectInputStream
argument_list|(
name|bais
argument_list|)
decl_stmt|;
name|ResourceAdapterImpl
name|rai2
init|=
operator|(
name|ResourceAdapterImpl
operator|)
name|ois
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|ois
operator|.
name|close
argument_list|()
expr_stmt|;
name|bais
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"deserialized is not null"
argument_list|,
name|rai2
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"props not empty"
argument_list|,
name|rai2
operator|.
name|getPluginProps
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"props contains key"
argument_list|,
name|rai2
operator|.
name|getPluginProps
argument_list|()
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"no change after serialized and reconstitued "
argument_list|,
name|value
argument_list|,
name|rai2
operator|.
name|getPluginProps
argument_list|()
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRegisterBusOfNull
parameter_list|()
throws|throws
name|Exception
block|{
name|ResourceAdapterImpl
name|rai
init|=
operator|new
name|ResourceAdapterImpl
argument_list|()
decl_stmt|;
name|rai
operator|.
name|registerBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"bus cache is not null"
argument_list|,
name|rai
operator|.
name|getBusCache
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"bus null registered"
argument_list|,
name|rai
operator|.
name|getBusCache
argument_list|()
operator|.
name|contains
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRegisterBusNotNull
parameter_list|()
throws|throws
name|Exception
block|{
name|ResourceAdapterImpl
name|rai
init|=
operator|new
name|ResourceAdapterImpl
argument_list|()
decl_stmt|;
name|Bus
name|bus
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
name|rai
operator|.
name|registerBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"bus cache is not null"
argument_list|,
name|rai
operator|.
name|getBusCache
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"bus registered"
argument_list|,
name|rai
operator|.
name|getBusCache
argument_list|()
operator|.
name|contains
argument_list|(
name|bus
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStartWithNullBootstrapContextThrowException
parameter_list|()
throws|throws
name|Exception
block|{
name|ResourceAdapterImpl
name|rai
init|=
operator|new
name|ResourceAdapterImpl
argument_list|()
decl_stmt|;
try|try
block|{
name|rai
operator|.
name|start
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Exception expected"
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
literal|"error message contains BootstrapContext"
argument_list|,
name|re
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"BootstrapContext"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"BootstrapContext is null"
argument_list|,
name|rai
operator|.
name|getBootstrapContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetCorrectBootstrapContext
parameter_list|()
throws|throws
name|Exception
block|{
name|ResourceAdapterImpl
name|rai
init|=
operator|new
name|ResourceAdapterImpl
argument_list|()
decl_stmt|;
name|BootstrapContext
name|bc
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|BootstrapContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"BootstrapContext not null"
argument_list|,
name|bc
argument_list|)
expr_stmt|;
name|rai
operator|.
name|start
argument_list|(
name|bc
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BootstrapContext set"
argument_list|,
name|rai
operator|.
name|getBootstrapContext
argument_list|()
argument_list|,
name|bc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStopWithEmptyBusCache
parameter_list|()
throws|throws
name|Exception
block|{
name|ResourceAdapterImpl
name|rai
init|=
operator|new
name|ResourceAdapterImpl
argument_list|()
decl_stmt|;
name|rai
operator|.
name|setBusCache
argument_list|(
operator|new
name|HashSet
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|assertNotNull
argument_list|(
literal|"bus cache is not null"
argument_list|,
name|rai
operator|.
name|getBusCache
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"bus cache is empty"
argument_list|,
name|rai
operator|.
name|getBusCache
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|rai
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"no exception expected"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStopWithNonEmptyBusCache
parameter_list|()
throws|throws
name|Exception
block|{
name|ResourceAdapterImpl
name|rai
init|=
operator|new
name|ResourceAdapterImpl
argument_list|()
decl_stmt|;
name|rai
operator|.
name|setBusCache
argument_list|(
operator|new
name|HashSet
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
name|Bus
name|bus
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
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
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
name|bus
argument_list|)
expr_stmt|;
name|rai
operator|.
name|registerBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|rai
operator|.
name|stop
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

