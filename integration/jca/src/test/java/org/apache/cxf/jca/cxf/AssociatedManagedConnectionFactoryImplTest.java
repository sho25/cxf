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
name|AssociatedManagedConnectionFactoryImplTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSetResourceAdapter
parameter_list|()
throws|throws
name|Exception
block|{
name|TestableAssociatedManagedConnectionFactoryImpl
name|mci
init|=
operator|new
name|TestableAssociatedManagedConnectionFactoryImpl
argument_list|()
decl_stmt|;
name|ResourceAdapterImpl
name|rai
init|=
operator|new
name|ResourceAdapterImpl
argument_list|()
decl_stmt|;
name|mci
operator|.
name|setResourceAdapter
argument_list|(
name|rai
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ResourceAdapter is set"
argument_list|,
name|mci
operator|.
name|getResourceAdapter
argument_list|()
argument_list|,
name|rai
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetWrongResourceAdapterThrowException
parameter_list|()
throws|throws
name|Exception
block|{
name|TestableAssociatedManagedConnectionFactoryImpl
name|mci
init|=
operator|new
name|TestableAssociatedManagedConnectionFactoryImpl
argument_list|()
decl_stmt|;
name|ResourceAdapter
name|rai
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ResourceAdapter
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|mci
operator|.
name|setResourceAdapter
argument_list|(
name|rai
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"exception expected"
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
literal|"wrong ResourceAdapter set"
argument_list|,
name|re
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"ResourceAdapterImpl"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRegisterBusThrowExceptionIfResourceAdapterNotSet
parameter_list|()
throws|throws
name|Exception
block|{
name|TestableAssociatedManagedConnectionFactoryImpl
name|mci
init|=
operator|new
name|TestableAssociatedManagedConnectionFactoryImpl
argument_list|()
decl_stmt|;
try|try
block|{
name|mci
operator|.
name|registerBus
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
name|ResourceException
name|re
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"ResourceAdapter not set"
argument_list|,
name|re
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"null"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*     public void testBusInitializedAndRegisteredToResourceAdapter() throws ResourceException, Exception {         DummyBus.reset();         System.setProperty("test.bus.class", DummyBus.class.getName());         TestableAssociatedManagedConnectionFactoryImpl mci =             new TestableAssociatedManagedConnectionFactoryImpl();         DummyResourceAdapterImpl rai = new DummyResourceAdapterImpl();         mci.setResourceAdapter(rai);         ClassLoader originalCl = Thread.currentThread().getContextClassLoader();         try {             // do this for MockObject creation             Thread.currentThread().setContextClassLoader(mci.getClass().getClassLoader());              Class dummyBusClass = Class.forName(DummyBus.class.getName(), true, mci.getClass()                 .getClassLoader());             Method initializeCount = dummyBusClass.getMethod("getInitializeCount", new Class[]{});             ConnectionManager cm =                 (ConnectionManager)EasyMock.createMock(                     Class.forName(ConnectionManager.class.getName(), true, mci.getClass().getClassLoader()));              mci.createConnectionFactory(cm);             assertEquals("bus should be initialized once", 1,                          initializeCount.invoke(null, new Object[]{}));             assertEquals("bus registered once after first call", 1, rai.registeredCount);         } finally {             Thread.currentThread().setContextClassLoader(originalCl);         }     }     */
annotation|@
name|Test
specifier|public
name|void
name|testMergeNonDuplicateResourceAdapterProps
parameter_list|()
throws|throws
name|ResourceException
block|{
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
literal|"key1"
argument_list|,
literal|"value1"
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
name|TestableAssociatedManagedConnectionFactoryImpl
name|mci
init|=
operator|new
name|TestableAssociatedManagedConnectionFactoryImpl
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"before associate, one props"
argument_list|,
literal|0
argument_list|,
name|mci
operator|.
name|getPluginProps
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"before associate, key1 not set"
argument_list|,
name|mci
operator|.
name|getPluginProps
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"key1"
argument_list|)
argument_list|)
expr_stmt|;
name|mci
operator|.
name|setResourceAdapter
argument_list|(
name|rai
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"after associate, two props"
argument_list|,
literal|1
argument_list|,
name|mci
operator|.
name|getPluginProps
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"after associate, key1 is set"
argument_list|,
name|mci
operator|.
name|getPluginProps
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"key1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|ManagedConnectionFactoryImpl
name|createManagedConnectionFactoryImpl
parameter_list|()
block|{
name|TestableAssociatedManagedConnectionFactoryImpl
name|mci
init|=
operator|new
name|TestableAssociatedManagedConnectionFactoryImpl
argument_list|()
decl_stmt|;
try|try
block|{
name|mci
operator|.
name|setResourceAdapter
argument_list|(
operator|new
name|DummyResourceAdapterImpl
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"failed to setResourceAdapter"
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|mci
return|;
block|}
block|}
end_class

begin_class
class|class
name|DummyResourceAdapterImpl
extends|extends
name|ResourceAdapterImpl
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|2416067861013682575L
decl_stmt|;
name|int
name|registeredCount
decl_stmt|;
name|DummyResourceAdapterImpl
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|registerBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|registeredCount
operator|++
expr_stmt|;
block|}
block|}
end_class

begin_class
class|class
name|TestableAssociatedManagedConnectionFactoryImpl
extends|extends
name|AssociatedManagedConnectionFactoryImpl
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|1077391534536084071L
decl_stmt|;
block|}
end_class

end_unit

