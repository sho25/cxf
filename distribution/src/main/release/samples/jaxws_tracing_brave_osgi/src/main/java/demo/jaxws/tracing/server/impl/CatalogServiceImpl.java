begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|jaxws
operator|.
name|tracing
operator|.
name|server
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|ConcurrentHashMap
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
name|ExecutorService
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
name|Executors
import|;
end_import

begin_import
import|import
name|brave
operator|.
name|ScopedSpan
import|;
end_import

begin_import
import|import
name|brave
operator|.
name|Tracing
import|;
end_import

begin_import
import|import
name|brave
operator|.
name|propagation
operator|.
name|TraceContext
import|;
end_import

begin_import
import|import
name|demo
operator|.
name|jaxws
operator|.
name|tracing
operator|.
name|server
operator|.
name|Book
import|;
end_import

begin_import
import|import
name|demo
operator|.
name|jaxws
operator|.
name|tracing
operator|.
name|server
operator|.
name|CatalogService
import|;
end_import

begin_class
specifier|public
class|class
name|CatalogServiceImpl
implements|implements
name|CatalogService
block|{
specifier|private
specifier|final
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|2
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Book
argument_list|>
name|books
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Tracing
name|brave
decl_stmt|;
specifier|public
name|CatalogServiceImpl
parameter_list|(
specifier|final
name|Tracing
name|brave
parameter_list|)
block|{
name|this
operator|.
name|brave
operator|=
name|brave
expr_stmt|;
block|}
specifier|public
name|void
name|addBook
parameter_list|(
name|Book
name|book
parameter_list|)
block|{
specifier|final
name|TraceContext
name|parent
init|=
name|brave
operator|.
name|tracer
argument_list|()
operator|.
name|currentSpan
argument_list|()
operator|.
name|context
argument_list|()
decl_stmt|;
name|executor
operator|.
name|submit
argument_list|(
parameter_list|()
lambda|->
block|{
specifier|final
name|ScopedSpan
name|span
init|=
name|brave
operator|.
name|tracer
argument_list|()
operator|.
name|startScopedSpanWithParent
argument_list|(
literal|"Inserting New Book"
argument_list|,
name|parent
argument_list|)
decl_stmt|;
try|try
block|{
name|books
operator|.
name|put
argument_list|(
name|book
operator|.
name|getId
argument_list|()
argument_list|,
name|book
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|span
operator|.
name|finish
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Book
name|getBook
parameter_list|(
specifier|final
name|String
name|id
parameter_list|)
block|{
specifier|final
name|Book
name|book
init|=
name|books
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|book
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Book with does not exists: "
operator|+
name|id
argument_list|)
throw|;
block|}
return|return
name|book
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|deleteBook
parameter_list|(
specifier|final
name|String
name|id
parameter_list|)
block|{
if|if
condition|(
name|books
operator|.
name|remove
argument_list|(
name|id
argument_list|)
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Book with does not exists: "
operator|+
name|id
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

