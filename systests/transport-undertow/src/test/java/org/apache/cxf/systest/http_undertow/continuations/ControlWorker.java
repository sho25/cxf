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
name|systest
operator|.
name|http_undertow
operator|.
name|continuations
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
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

begin_class
specifier|public
class|class
name|ControlWorker
implements|implements
name|Runnable
block|{
specifier|private
name|HelloContinuation
name|helloPort
decl_stmt|;
specifier|private
name|String
name|firstName
decl_stmt|;
specifier|private
name|CountDownLatch
name|startSignal
decl_stmt|;
specifier|private
name|CountDownLatch
name|resumeSignal
decl_stmt|;
specifier|public
name|ControlWorker
parameter_list|(
name|HelloContinuation
name|helloPort
parameter_list|,
name|String
name|firstName
parameter_list|,
name|CountDownLatch
name|startSignal
parameter_list|,
name|CountDownLatch
name|resumeSignal
parameter_list|)
block|{
name|this
operator|.
name|helloPort
operator|=
name|helloPort
expr_stmt|;
name|this
operator|.
name|firstName
operator|=
name|firstName
expr_stmt|;
name|this
operator|.
name|startSignal
operator|=
name|startSignal
expr_stmt|;
name|this
operator|.
name|resumeSignal
operator|=
name|resumeSignal
expr_stmt|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|startSignal
operator|.
name|await
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|helloPort
operator|.
name|isRequestSuspended
argument_list|(
name|firstName
argument_list|)
condition|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"No suspended invocation for "
operator|+
name|firstName
argument_list|)
expr_stmt|;
block|}
name|helloPort
operator|.
name|resumeRequest
argument_list|(
name|firstName
argument_list|)
expr_stmt|;
name|resumeSignal
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|(
literal|"Control thread for "
operator|+
name|firstName
operator|+
literal|" failed"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

