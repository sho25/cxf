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
name|ws
operator|.
name|security
operator|.
name|cache
package|;
end_package

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|CacheManager
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Status
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|config
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|config
operator|.
name|ConfigurationFactory
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
name|BusFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|cache
operator|.
name|EHCacheManagerHolder
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

begin_class
specifier|public
class|class
name|EHCacheUtilsTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testUseGlobalManager
parameter_list|()
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
decl_stmt|;
name|Configuration
name|conf
init|=
name|ConfigurationFactory
operator|.
name|parseConfiguration
argument_list|(
name|EHCacheManagerHolder
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/cxf-test-ehcache.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|conf
operator|.
name|setName
argument_list|(
literal|"myGlobalConfig"
argument_list|)
expr_stmt|;
name|CacheManager
operator|.
name|newInstance
argument_list|(
name|conf
argument_list|)
expr_stmt|;
name|CacheManager
name|manager
init|=
name|EHCacheUtils
operator|.
name|getCacheManager
argument_list|(
name|bus
argument_list|,
name|EHCacheManagerHolder
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/cxf-test-ehcache.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|manager
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"myGlobalConfig"
argument_list|)
argument_list|)
expr_stmt|;
name|EHCacheManagerHolder
operator|.
name|releaseCacheManger
argument_list|(
name|manager
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|STATUS_SHUTDOWN
argument_list|,
name|manager
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|bus
operator|.
name|setProperty
argument_list|(
name|EHCacheUtils
operator|.
name|GLOBAL_EHCACHE_MANAGER_NAME
argument_list|,
literal|"myGlobalConfig"
argument_list|)
expr_stmt|;
name|manager
operator|=
name|EHCacheUtils
operator|.
name|getCacheManager
argument_list|(
name|bus
argument_list|,
name|EHCacheManagerHolder
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/cxf-test-ehcache.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"myGlobalConfig"
argument_list|,
name|manager
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|EHCacheManagerHolder
operator|.
name|releaseCacheManger
argument_list|(
name|manager
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|STATUS_ALIVE
argument_list|,
name|manager
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|manager
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|STATUS_SHUTDOWN
argument_list|,
name|manager
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|bus
operator|.
name|setProperty
argument_list|(
name|EHCacheUtils
operator|.
name|GLOBAL_EHCACHE_MANAGER_NAME
argument_list|,
literal|"myGlobalConfigXXX"
argument_list|)
expr_stmt|;
name|manager
operator|=
name|EHCacheUtils
operator|.
name|getCacheManager
argument_list|(
name|bus
argument_list|,
name|EHCacheManagerHolder
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/cxf-test-ehcache.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|manager
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"myGlobalConfig"
argument_list|)
argument_list|)
expr_stmt|;
name|EHCacheManagerHolder
operator|.
name|releaseCacheManger
argument_list|(
name|manager
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|STATUS_SHUTDOWN
argument_list|,
name|manager
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

