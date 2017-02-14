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
name|service
operator|.
name|invoker
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ArrayBlockingQueue
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
name|BlockingQueue
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
name|message
operator|.
name|Exchange
import|;
end_import

begin_comment
comment|/**  * Factory the maintains a pool of instances that are used.  *  * Can optionally create more instances than the size of the queue  */
end_comment

begin_class
specifier|public
class|class
name|PooledFactory
implements|implements
name|Factory
block|{
name|BlockingQueue
argument_list|<
name|Object
argument_list|>
name|pool
decl_stmt|;
name|Factory
name|factory
decl_stmt|;
name|int
name|count
decl_stmt|;
name|int
name|max
decl_stmt|;
name|boolean
name|createMore
decl_stmt|;
comment|/**      * Pool of instances of the svcClass      * @param svcClass the class to create      * @param max the absolute maximum number to create and pool      */
specifier|public
name|PooledFactory
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|svcClass
parameter_list|,
name|int
name|max
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|PerRequestFactory
argument_list|(
name|svcClass
argument_list|)
argument_list|,
name|max
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Pool of instances constructed from the given factory      * @param factory      * @param max the absolute maximum number to create and pool      */
specifier|public
name|PooledFactory
parameter_list|(
specifier|final
name|Factory
name|factory
parameter_list|,
name|int
name|max
parameter_list|)
block|{
name|this
argument_list|(
name|factory
argument_list|,
name|max
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Pool of instances constructed from the given factory      * @param factory      * @param max the absolute maximum number to create and pool      * @param createMore If the pool is empty, but max objects have already      * been constructed, should more be constructed on a per-request basis (and      * then discarded when done) or should requests block until instances are      * released back into the pool.      */
specifier|public
name|PooledFactory
parameter_list|(
specifier|final
name|Factory
name|factory
parameter_list|,
name|int
name|max
parameter_list|,
name|boolean
name|createMore
parameter_list|)
block|{
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
if|if
condition|(
name|max
operator|<
literal|1
condition|)
block|{
name|max
operator|=
literal|16
expr_stmt|;
block|}
name|pool
operator|=
operator|new
name|ArrayBlockingQueue
argument_list|<
name|Object
argument_list|>
argument_list|(
name|max
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|max
operator|=
name|max
expr_stmt|;
name|this
operator|.
name|count
operator|=
literal|0
expr_stmt|;
name|this
operator|.
name|createMore
operator|=
name|createMore
expr_stmt|;
block|}
comment|/**      * Pool constructed from the give Collection of objects.      * @param objs The collection of objects to pre-populate the pool      */
specifier|public
name|PooledFactory
parameter_list|(
name|Collection
argument_list|<
name|Object
argument_list|>
name|objs
parameter_list|)
block|{
name|pool
operator|=
operator|new
name|ArrayBlockingQueue
argument_list|<
name|Object
argument_list|>
argument_list|(
name|objs
operator|.
name|size
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|pool
operator|.
name|addAll
argument_list|(
name|objs
argument_list|)
expr_stmt|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|Object
name|create
parameter_list|(
name|Exchange
name|ex
parameter_list|)
throws|throws
name|Throwable
block|{
if|if
condition|(
name|factory
operator|==
literal|null
operator|||
operator|(
operator|(
name|count
operator|>=
name|max
operator|)
operator|&&
operator|!
name|createMore
operator|)
condition|)
block|{
return|return
name|pool
operator|.
name|take
argument_list|()
return|;
block|}
name|Object
name|o
init|=
name|pool
operator|.
name|poll
argument_list|()
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
return|return
name|createObject
argument_list|(
name|ex
argument_list|)
return|;
block|}
return|return
name|o
return|;
block|}
specifier|protected
specifier|synchronized
name|Object
name|createObject
parameter_list|(
name|Exchange
name|e
parameter_list|)
throws|throws
name|Throwable
block|{
comment|//recheck the count/max stuff now that we're in a sync block
if|if
condition|(
name|factory
operator|==
literal|null
operator|||
operator|(
operator|(
name|count
operator|>=
name|max
operator|)
operator|&&
operator|!
name|createMore
operator|)
condition|)
block|{
return|return
name|pool
operator|.
name|take
argument_list|()
return|;
block|}
name|count
operator|++
expr_stmt|;
return|return
name|factory
operator|.
name|create
argument_list|(
name|e
argument_list|)
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|release
parameter_list|(
name|Exchange
name|ex
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
name|pool
operator|.
name|offer
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

