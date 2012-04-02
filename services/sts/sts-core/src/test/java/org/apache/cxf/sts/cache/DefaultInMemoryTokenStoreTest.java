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
name|sts
operator|.
name|cache
package|;
end_package

begin_import
import|import
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
name|tokenstore
operator|.
name|SecurityToken
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
name|ws
operator|.
name|security
operator|.
name|tokenstore
operator|.
name|TokenStore
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

begin_class
specifier|public
class|class
name|DefaultInMemoryTokenStoreTest
extends|extends
name|org
operator|.
name|junit
operator|.
name|Assert
block|{
specifier|private
specifier|static
name|TokenStore
name|store
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|init
parameter_list|()
block|{
name|store
operator|=
operator|new
name|DefaultInMemoryTokenStore
argument_list|()
expr_stmt|;
block|}
comment|// tests STSCache apis for storing in the cache.
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testTokenAdd
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|key
init|=
literal|"key"
decl_stmt|;
name|SecurityToken
name|token
init|=
operator|new
name|SecurityToken
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|store
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|token
argument_list|,
name|store
operator|.
name|getToken
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|store
operator|.
name|remove
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|store
operator|.
name|getToken
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// tests STSCache apis for removing from the cache.
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testTokenRemove
parameter_list|()
block|{
name|SecurityToken
name|token1
init|=
operator|new
name|SecurityToken
argument_list|(
literal|"token1"
argument_list|)
decl_stmt|;
name|SecurityToken
name|token2
init|=
operator|new
name|SecurityToken
argument_list|(
literal|"token2"
argument_list|)
decl_stmt|;
name|SecurityToken
name|token3
init|=
operator|new
name|SecurityToken
argument_list|(
literal|"token3"
argument_list|)
decl_stmt|;
name|store
operator|.
name|add
argument_list|(
name|token1
argument_list|)
expr_stmt|;
name|store
operator|.
name|add
argument_list|(
name|token2
argument_list|)
expr_stmt|;
name|store
operator|.
name|add
argument_list|(
name|token3
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|store
operator|.
name|getTokenIdentifiers
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|3
argument_list|)
expr_stmt|;
name|store
operator|.
name|remove
argument_list|(
name|token3
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|store
operator|.
name|getToken
argument_list|(
literal|"test3"
argument_list|)
argument_list|)
expr_stmt|;
name|store
operator|.
name|remove
argument_list|(
name|token1
argument_list|)
expr_stmt|;
name|store
operator|.
name|remove
argument_list|(
name|token2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|store
operator|.
name|getTokenIdentifiers
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

