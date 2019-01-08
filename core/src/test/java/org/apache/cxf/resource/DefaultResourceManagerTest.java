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
name|resource
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|assertArrayEquals
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

begin_class
specifier|public
class|class
name|DefaultResourceManagerTest
block|{
specifier|private
name|DefaultResourceManager
name|manager
decl_stmt|;
specifier|private
name|DummyResolver
index|[]
name|resolvers
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|AtomicInteger
name|ordering
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|resolvers
operator|=
operator|new
name|DummyResolver
index|[
literal|4
index|]
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
name|resolvers
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|resolvers
index|[
name|i
index|]
operator|=
operator|new
name|DummyResolver
argument_list|(
name|ordering
argument_list|)
expr_stmt|;
block|}
name|manager
operator|=
operator|new
name|DefaultResourceManager
argument_list|(
name|resolvers
index|[
name|resolvers
operator|.
name|length
operator|-
literal|1
index|]
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|resolvers
operator|.
name|length
operator|-
literal|2
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|manager
operator|.
name|addResourceResolver
argument_list|(
name|resolvers
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|manager
operator|=
literal|null
expr_stmt|;
name|resolvers
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolverOrder
parameter_list|()
block|{
name|assertArrayEquals
argument_list|(
name|resolvers
argument_list|,
name|getResolvers
argument_list|(
name|manager
argument_list|)
argument_list|)
expr_stmt|;
name|checkCallOrder
argument_list|(
name|resolvers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCopiedResolverOrder
parameter_list|()
block|{
name|ResourceManager
name|newManager
init|=
operator|new
name|DefaultResourceManager
argument_list|(
name|manager
operator|.
name|getResourceResolvers
argument_list|()
argument_list|)
decl_stmt|;
name|assertArrayEquals
argument_list|(
name|resolvers
argument_list|,
name|getResolvers
argument_list|(
name|newManager
argument_list|)
argument_list|)
expr_stmt|;
name|checkCallOrder
argument_list|(
name|resolvers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddResolverList
parameter_list|()
block|{
name|List
argument_list|<
name|ResourceResolver
argument_list|>
name|addedResolvers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
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
literal|4
condition|;
name|i
operator|++
control|)
block|{
name|addedResolvers
operator|.
name|add
argument_list|(
operator|new
name|DummyResolver
argument_list|(
name|resolvers
index|[
literal|0
index|]
operator|.
name|source
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|manager
operator|.
name|addResourceResolvers
argument_list|(
name|addedResolvers
argument_list|)
expr_stmt|;
name|DummyResolver
index|[]
name|expected
init|=
operator|new
name|DummyResolver
index|[
name|addedResolvers
operator|.
name|size
argument_list|()
operator|+
name|resolvers
operator|.
name|length
index|]
decl_stmt|;
name|addedResolvers
operator|.
name|toArray
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|resolvers
argument_list|,
literal|0
argument_list|,
name|expected
argument_list|,
name|addedResolvers
operator|.
name|size
argument_list|()
argument_list|,
name|resolvers
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|expected
argument_list|,
name|getResolvers
argument_list|(
name|manager
argument_list|)
argument_list|)
expr_stmt|;
name|checkCallOrder
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddDuplicateResolver
parameter_list|()
block|{
name|manager
operator|.
name|addResourceResolver
argument_list|(
name|resolvers
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|resolvers
argument_list|,
name|getResolvers
argument_list|(
name|manager
argument_list|)
argument_list|)
expr_stmt|;
name|checkCallOrder
argument_list|(
name|resolvers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddDuplicateResolverList
parameter_list|()
block|{
name|manager
operator|.
name|addResourceResolvers
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|manager
operator|.
name|getResourceResolvers
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|resolvers
argument_list|,
name|getResolvers
argument_list|(
name|manager
argument_list|)
argument_list|)
expr_stmt|;
name|checkCallOrder
argument_list|(
name|resolvers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRemoveResolver
parameter_list|()
block|{
name|manager
operator|.
name|removeResourceResolver
argument_list|(
name|resolvers
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|DummyResolver
index|[]
name|expected
init|=
operator|new
name|DummyResolver
index|[
name|resolvers
operator|.
name|length
operator|-
literal|1
index|]
decl_stmt|;
name|expected
index|[
literal|0
index|]
operator|=
name|resolvers
index|[
literal|0
index|]
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|resolvers
argument_list|,
literal|2
argument_list|,
name|expected
argument_list|,
literal|1
argument_list|,
name|expected
operator|.
name|length
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|expected
argument_list|,
name|getResolvers
argument_list|(
name|manager
argument_list|)
argument_list|)
expr_stmt|;
name|checkCallOrder
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|resolvers
index|[
literal|1
index|]
operator|.
name|order
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLiveResolverList
parameter_list|()
block|{
name|List
argument_list|<
name|ResourceResolver
argument_list|>
name|currentResolvers
init|=
name|manager
operator|.
name|getResourceResolvers
argument_list|()
decl_stmt|;
name|DummyResolver
name|newResolver
init|=
operator|new
name|DummyResolver
argument_list|(
name|resolvers
index|[
literal|0
index|]
operator|.
name|source
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|currentResolvers
operator|.
name|contains
argument_list|(
name|newResolver
argument_list|)
argument_list|)
expr_stmt|;
name|manager
operator|.
name|addResourceResolver
argument_list|(
name|newResolver
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|currentResolvers
operator|.
name|contains
argument_list|(
name|newResolver
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|currentResolvers
operator|.
name|contains
argument_list|(
name|resolvers
index|[
literal|1
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|manager
operator|.
name|removeResourceResolver
argument_list|(
name|resolvers
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|currentResolvers
operator|.
name|contains
argument_list|(
name|resolvers
index|[
literal|1
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ResourceResolver
index|[]
name|getResolvers
parameter_list|(
name|ResourceManager
name|resourceManager
parameter_list|)
block|{
name|List
argument_list|<
name|ResourceResolver
argument_list|>
name|list
init|=
name|resourceManager
operator|.
name|getResourceResolvers
argument_list|()
decl_stmt|;
name|ResourceResolver
index|[]
name|actual
init|=
operator|new
name|ResourceResolver
index|[
name|list
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
return|return
name|list
operator|.
name|toArray
argument_list|(
name|actual
argument_list|)
return|;
block|}
specifier|private
name|void
name|checkCallOrder
parameter_list|(
name|DummyResolver
index|[]
name|usedResolvers
parameter_list|)
block|{
name|manager
operator|.
name|resolveResource
argument_list|(
literal|null
argument_list|,
name|Void
operator|.
name|class
argument_list|)
expr_stmt|;
name|int
index|[]
name|expected
init|=
operator|new
name|int
index|[
name|usedResolvers
operator|.
name|length
index|]
decl_stmt|;
name|int
index|[]
name|actual
init|=
operator|new
name|int
index|[
name|usedResolvers
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
name|usedResolvers
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|expected
index|[
name|i
index|]
operator|=
name|i
expr_stmt|;
name|actual
index|[
name|i
index|]
operator|=
name|usedResolvers
index|[
name|i
index|]
operator|.
name|order
expr_stmt|;
block|}
name|assertArrayEquals
argument_list|(
name|expected
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|DummyResolver
implements|implements
name|ResourceResolver
block|{
name|AtomicInteger
name|source
decl_stmt|;
name|int
name|order
init|=
operator|-
literal|1
decl_stmt|;
name|DummyResolver
parameter_list|(
name|AtomicInteger
name|source
parameter_list|)
block|{
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|resolve
parameter_list|(
name|String
name|resourceName
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|resourceType
parameter_list|)
block|{
name|order
operator|=
name|source
operator|.
name|getAndIncrement
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|getAsStream
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

