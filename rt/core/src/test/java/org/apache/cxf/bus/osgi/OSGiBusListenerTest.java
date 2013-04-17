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
name|bus
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|buslifecycle
operator|.
name|BusLifeCycleManager
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|ClientLifeCycleListener
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
name|ClientLifeCycleManager
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
name|ServerLifeCycleListener
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
name|ServerLifeCycleManager
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
name|feature
operator|.
name|Feature
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
name|easymock
operator|.
name|IMocksControl
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

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
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

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceReference
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|OSGiBusListenerTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|SERVICE_BUNDLE_NAMES
init|=
operator|new
name|String
index|[]
block|{
literal|"me.temp.foo.test"
block|,
literal|"me.temp.bar.sample"
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EXCLUDES
init|=
literal|"me\\.temp\\.bar\\..*"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RESTRICTED
init|=
literal|"me\\.my\\.app\\..*"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BUNDLE_NAME
init|=
literal|"me.my.app"
decl_stmt|;
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|Bundle
name|bundle
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|bus
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
expr_stmt|;
name|BusLifeCycleManager
name|blcManager
init|=
name|control
operator|.
name|createMock
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|blcManager
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|blcManager
operator|.
name|registerLifeCycleListener
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|OSGIBusListener
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|bundleContext
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|BundleContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|BundleContext
name|app
init|=
name|control
operator|.
name|createMock
argument_list|(
name|BundleContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|BundleContext
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|app
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|bundle
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Bundle
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|app
operator|.
name|getBundle
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bundle
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|BUNDLE_NAME
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRegistratioWithNoServices
parameter_list|()
throws|throws
name|Exception
block|{
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
operator|new
name|OSGIBusListener
argument_list|(
name|bus
argument_list|,
operator|new
name|Object
index|[]
block|{
name|bundleContext
block|}
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRegistratioWithServices
parameter_list|()
throws|throws
name|Exception
block|{
name|setUpClientLifeCycleListeners
argument_list|(
name|SERVICE_BUNDLE_NAMES
argument_list|,
operator|new
name|String
index|[]
block|{
literal|null
block|,
literal|null
block|}
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|setUpServerLifeCycleListeners
argument_list|(
name|SERVICE_BUNDLE_NAMES
argument_list|,
operator|new
name|String
index|[]
block|{
literal|null
block|,
literal|null
block|}
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Feature
argument_list|>
name|lst
init|=
operator|new
name|ArrayList
argument_list|<
name|Feature
argument_list|>
argument_list|()
decl_stmt|;
name|setFeatures
argument_list|(
name|SERVICE_BUNDLE_NAMES
argument_list|,
operator|new
name|String
index|[]
block|{
literal|null
block|,
literal|null
block|}
argument_list|,
literal|null
argument_list|,
name|lst
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
operator|new
name|OSGIBusListener
argument_list|(
name|bus
argument_list|,
operator|new
name|Object
index|[]
block|{
name|bundleContext
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|countServices
argument_list|(
name|SERVICE_BUNDLE_NAMES
argument_list|,
operator|new
name|String
index|[]
block|{
literal|null
block|,
literal|null
block|}
argument_list|,
literal|null
argument_list|)
argument_list|,
name|lst
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRegistratioWithServicesExcludes
parameter_list|()
throws|throws
name|Exception
block|{
name|setUpClientLifeCycleListeners
argument_list|(
name|SERVICE_BUNDLE_NAMES
argument_list|,
operator|new
name|String
index|[]
block|{
literal|null
block|,
literal|null
block|}
argument_list|,
name|EXCLUDES
argument_list|)
expr_stmt|;
name|setUpServerLifeCycleListeners
argument_list|(
name|SERVICE_BUNDLE_NAMES
argument_list|,
operator|new
name|String
index|[]
block|{
literal|null
block|,
literal|null
block|}
argument_list|,
name|EXCLUDES
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Feature
argument_list|>
name|lst
init|=
operator|new
name|ArrayList
argument_list|<
name|Feature
argument_list|>
argument_list|()
decl_stmt|;
name|setFeatures
argument_list|(
name|SERVICE_BUNDLE_NAMES
argument_list|,
operator|new
name|String
index|[]
block|{
literal|null
block|,
literal|null
block|}
argument_list|,
name|EXCLUDES
argument_list|,
name|lst
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
literal|"bus.extension.bundles.excludes"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|EXCLUDES
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
operator|new
name|OSGIBusListener
argument_list|(
name|bus
argument_list|,
operator|new
name|Object
index|[]
block|{
name|bundleContext
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|countServices
argument_list|(
name|SERVICE_BUNDLE_NAMES
argument_list|,
operator|new
name|String
index|[]
block|{
literal|null
block|,
literal|null
block|}
argument_list|,
name|EXCLUDES
argument_list|)
argument_list|,
name|lst
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRegistratioWithServicesExcludesAndRestricted
parameter_list|()
throws|throws
name|Exception
block|{
name|setUpClientLifeCycleListeners
argument_list|(
name|SERVICE_BUNDLE_NAMES
argument_list|,
operator|new
name|String
index|[]
block|{
name|RESTRICTED
block|,
literal|null
block|}
argument_list|,
name|EXCLUDES
argument_list|)
expr_stmt|;
name|setUpServerLifeCycleListeners
argument_list|(
name|SERVICE_BUNDLE_NAMES
argument_list|,
operator|new
name|String
index|[]
block|{
name|RESTRICTED
block|,
literal|null
block|}
argument_list|,
name|EXCLUDES
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Feature
argument_list|>
name|lst
init|=
operator|new
name|ArrayList
argument_list|<
name|Feature
argument_list|>
argument_list|()
decl_stmt|;
name|setFeatures
argument_list|(
name|SERVICE_BUNDLE_NAMES
argument_list|,
operator|new
name|String
index|[]
block|{
name|RESTRICTED
block|,
literal|null
block|}
argument_list|,
name|EXCLUDES
argument_list|,
name|lst
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
literal|"bus.extension.bundles.excludes"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|EXCLUDES
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
operator|new
name|OSGIBusListener
argument_list|(
name|bus
argument_list|,
operator|new
name|Object
index|[]
block|{
name|bundleContext
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|countServices
argument_list|(
name|SERVICE_BUNDLE_NAMES
argument_list|,
operator|new
name|String
index|[]
block|{
name|RESTRICTED
block|,
literal|null
block|}
argument_list|,
name|EXCLUDES
argument_list|)
argument_list|,
name|lst
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|setUpClientLifeCycleListeners
parameter_list|(
name|String
index|[]
name|names
parameter_list|,
name|String
index|[]
name|restricted
parameter_list|,
name|String
name|excludes
parameter_list|)
throws|throws
name|Exception
block|{
name|ServiceReference
index|[]
name|svcrefs
init|=
name|createTestServiceReferences
argument_list|(
name|names
argument_list|,
name|restricted
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|ClientLifeCycleListener
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|svcrefs
argument_list|)
expr_stmt|;
name|ClientLifeCycleManager
name|lcmanager
init|=
name|control
operator|.
name|createMock
argument_list|(
name|ClientLifeCycleManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|ClientLifeCycleManager
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|lcmanager
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|names
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|ClientLifeCycleListener
name|cl
init|=
name|control
operator|.
name|createMock
argument_list|(
name|ClientLifeCycleListener
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bundleContext
operator|.
name|getService
argument_list|(
name|svcrefs
index|[
name|i
index|]
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|cl
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|isExcluded
argument_list|(
name|BUNDLE_NAME
argument_list|,
name|names
index|[
name|i
index|]
argument_list|,
name|restricted
index|[
name|i
index|]
argument_list|,
name|excludes
argument_list|)
condition|)
block|{
name|lcmanager
operator|.
name|registerListener
argument_list|(
name|cl
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|setUpServerLifeCycleListeners
parameter_list|(
name|String
index|[]
name|names
parameter_list|,
name|String
index|[]
name|restricted
parameter_list|,
name|String
name|excludes
parameter_list|)
throws|throws
name|Exception
block|{
name|ServiceReference
index|[]
name|svcrefs
init|=
name|createTestServiceReferences
argument_list|(
name|names
argument_list|,
name|restricted
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|ServerLifeCycleListener
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|svcrefs
argument_list|)
expr_stmt|;
name|ServerLifeCycleManager
name|lcmanager
init|=
name|control
operator|.
name|createMock
argument_list|(
name|ServerLifeCycleManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|ServerLifeCycleManager
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|lcmanager
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|names
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|ServerLifeCycleListener
name|cl
init|=
name|control
operator|.
name|createMock
argument_list|(
name|ServerLifeCycleListener
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bundleContext
operator|.
name|getService
argument_list|(
name|svcrefs
index|[
name|i
index|]
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|cl
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|isExcluded
argument_list|(
name|BUNDLE_NAME
argument_list|,
name|names
index|[
name|i
index|]
argument_list|,
name|restricted
index|[
name|i
index|]
argument_list|,
name|excludes
argument_list|)
condition|)
block|{
name|lcmanager
operator|.
name|registerListener
argument_list|(
name|cl
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|setFeatures
parameter_list|(
name|String
index|[]
name|names
parameter_list|,
name|String
index|[]
name|restricted
parameter_list|,
name|String
name|excludes
parameter_list|,
name|Collection
argument_list|<
name|Feature
argument_list|>
name|lst
parameter_list|)
throws|throws
name|Exception
block|{
name|ServiceReference
index|[]
name|svcrefs
init|=
name|createTestServiceReferences
argument_list|(
name|names
argument_list|,
name|restricted
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|Feature
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|svcrefs
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|names
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Feature
name|f
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Feature
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bundleContext
operator|.
name|getService
argument_list|(
name|svcrefs
index|[
name|i
index|]
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|f
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
block|}
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getFeatures
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|lst
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
block|}
comment|// Creates test service references with the specified symbolic names and the restricted extension properties.
specifier|private
name|ServiceReference
index|[]
name|createTestServiceReferences
parameter_list|(
name|String
index|[]
name|names
parameter_list|,
name|String
index|[]
name|restricted
parameter_list|)
block|{
name|ServiceReference
index|[]
name|refs
init|=
operator|new
name|ServiceReference
index|[
name|names
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|names
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|refs
index|[
name|i
index|]
operator|=
name|createTestServiceReference
argument_list|(
name|names
index|[
name|i
index|]
argument_list|,
name|restricted
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|refs
return|;
block|}
comment|// Creates a test service reference with the specified symbolic name and the restricted extension property.
specifier|private
name|ServiceReference
name|createTestServiceReference
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|rst
parameter_list|)
block|{
name|ServiceReference
name|ref
init|=
name|control
operator|.
name|createMock
argument_list|(
name|ServiceReference
operator|.
name|class
argument_list|)
decl_stmt|;
name|Bundle
name|b
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Bundle
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|b
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|name
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ref
operator|.
name|getBundle
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|b
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ref
operator|.
name|getProperty
argument_list|(
literal|"org.apache.cxf.bus.restricted.extension"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|rst
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
return|return
name|ref
return|;
block|}
specifier|private
specifier|static
name|boolean
name|isExcluded
parameter_list|(
name|String
name|aname
parameter_list|,
name|String
name|sname
parameter_list|,
name|String
name|rst
parameter_list|,
name|String
name|exc
parameter_list|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|rst
argument_list|)
operator|&&
operator|!
name|aname
operator|.
name|matches
argument_list|(
name|rst
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|exc
operator|!=
literal|null
operator|&&
name|sname
operator|.
name|matches
argument_list|(
name|exc
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|int
name|countServices
parameter_list|(
name|String
index|[]
name|names
parameter_list|,
name|String
index|[]
name|restricted
parameter_list|,
name|String
name|excluded
parameter_list|)
block|{
name|int
name|c
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|names
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|isExcluded
argument_list|(
name|BUNDLE_NAME
argument_list|,
name|names
index|[
name|i
index|]
argument_list|,
name|restricted
index|[
name|i
index|]
argument_list|,
name|excluded
argument_list|)
condition|)
block|{
name|c
operator|++
expr_stmt|;
block|}
block|}
return|return
name|c
return|;
block|}
block|}
end_class

end_unit

