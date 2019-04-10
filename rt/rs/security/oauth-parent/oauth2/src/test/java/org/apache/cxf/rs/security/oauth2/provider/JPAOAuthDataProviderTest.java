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
name|oauth2
operator|.
name|provider
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityManagerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Persistence
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
name|JPAOAuthDataProviderTest
extends|extends
name|AbstractOAuthDataProviderTest
block|{
specifier|protected
name|EntityManagerFactory
name|emFactory
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
try|try
block|{
name|emFactory
operator|=
name|Persistence
operator|.
name|createEntityManagerFactory
argument_list|(
name|getPersistenceUnitName
argument_list|()
argument_list|)
expr_stmt|;
name|JPAOAuthDataProvider
name|provider
init|=
operator|new
name|JPAOAuthDataProvider
argument_list|()
decl_stmt|;
name|provider
operator|.
name|setEntityManagerFactory
argument_list|(
name|emFactory
argument_list|)
expr_stmt|;
name|initializeProvider
argument_list|(
name|provider
argument_list|)
expr_stmt|;
name|setProvider
argument_list|(
name|provider
argument_list|)
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
name|fail
argument_list|(
literal|"Exception during JPA EntityManager creation."
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|String
name|getPersistenceUnitName
parameter_list|()
block|{
return|return
literal|"test-hibernate-cxf-rt-rs-security-oauth2"
return|;
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
try|try
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|emFactory
operator|!=
literal|null
condition|)
block|{
name|emFactory
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

