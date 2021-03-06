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
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|work
operator|.
name|Work
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
name|work
operator|.
name|WorkEvent
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
name|work
operator|.
name|WorkException
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
name|work
operator|.
name|WorkManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|util
operator|.
name|thread
operator|.
name|ThreadPool
import|;
end_import

begin_comment
comment|/**  * The adapter for using Application Server's thread pool.  * Just simply override the dispatch method.  */
end_comment

begin_class
specifier|public
class|class
name|WorkManagerThreadPool
extends|extends
name|CXFWorkAdapter
implements|implements
name|ThreadPool
block|{
specifier|private
name|WorkManager
name|workManager
decl_stmt|;
specifier|private
name|boolean
name|isLowOnThreads
decl_stmt|;
specifier|private
name|Runnable
name|theJob
decl_stmt|;
specifier|public
name|WorkManagerThreadPool
parameter_list|(
name|WorkManager
name|wm
parameter_list|)
block|{
name|this
operator|.
name|workManager
operator|=
name|wm
expr_stmt|;
block|}
specifier|public
name|boolean
name|dispatch
parameter_list|(
name|Runnable
name|job
parameter_list|)
block|{
try|try
block|{
name|theJob
operator|=
name|job
expr_stmt|;
name|workManager
operator|.
name|startWork
argument_list|(
operator|new
name|WorkImpl
argument_list|(
name|job
argument_list|)
argument_list|,
name|DEFAULT_START_TIME_OUT
argument_list|,
literal|null
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|WorkException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
specifier|public
name|int
name|getIdleThreads
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
specifier|public
name|int
name|getThreads
parameter_list|()
block|{
return|return
literal|1
return|;
block|}
specifier|public
name|boolean
name|isLowOnThreads
parameter_list|()
block|{
return|return
name|isLowOnThreads
return|;
block|}
name|void
name|setIsLowOnThreads
parameter_list|(
name|boolean
name|isLow
parameter_list|)
block|{
name|this
operator|.
name|isLowOnThreads
operator|=
name|isLow
expr_stmt|;
block|}
specifier|public
name|void
name|join
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|//Do nothing
block|}
specifier|public
class|class
name|WorkImpl
implements|implements
name|Work
block|{
specifier|private
name|Runnable
name|job
decl_stmt|;
specifier|public
name|WorkImpl
parameter_list|(
name|Runnable
name|job
parameter_list|)
block|{
name|this
operator|.
name|job
operator|=
name|job
expr_stmt|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
name|job
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|release
parameter_list|()
block|{
comment|//empty
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|workRejected
parameter_list|(
name|WorkEvent
name|e
parameter_list|)
block|{
name|super
operator|.
name|workRejected
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|WorkException
name|we
init|=
name|e
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|WorkException
operator|.
name|START_TIMED_OUT
operator|.
name|equals
argument_list|(
name|we
operator|.
name|getErrorCode
argument_list|()
argument_list|)
operator|&&
operator|!
name|isLowOnThreads
condition|)
block|{
name|setIsLowOnThreads
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dispatch
argument_list|(
name|theJob
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|execute
parameter_list|(
name|Runnable
name|command
parameter_list|)
block|{
name|dispatch
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

