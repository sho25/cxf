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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|sso
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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|EHCacheUtilTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCreateCacheManager
parameter_list|()
block|{
name|Configuration
name|conf
init|=
name|ConfigurationFactory
operator|.
name|parseConfiguration
argument_list|(
name|EHCacheUtil
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/cxf-test-ehcache.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|conf
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setName
argument_list|(
literal|"testCache"
argument_list|)
expr_stmt|;
name|CacheManager
name|manager1
init|=
name|EHCacheUtil
operator|.
name|createCacheManager
argument_list|(
name|conf
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|manager1
argument_list|)
expr_stmt|;
name|CacheManager
name|manager2
init|=
name|EHCacheUtil
operator|.
name|createCacheManager
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|manager2
argument_list|)
expr_stmt|;
name|manager1
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
name|manager1
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|STATUS_ALIVE
argument_list|,
name|manager2
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|manager2
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
name|manager2
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

