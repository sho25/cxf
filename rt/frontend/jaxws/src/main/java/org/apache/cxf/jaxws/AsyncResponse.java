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
name|jaxws
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
name|ExecutionException
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
name|Future
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
name|TimeUnit
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
name|TimeoutException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Response
import|;
end_import

begin_class
specifier|public
class|class
name|AsyncResponse
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Response
argument_list|<
name|T
argument_list|>
block|{
specifier|private
specifier|final
name|Future
argument_list|<
name|T
argument_list|>
name|obj
decl_stmt|;
specifier|private
name|T
name|result
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|T
argument_list|>
name|cls
decl_stmt|;
specifier|public
name|AsyncResponse
parameter_list|(
name|Future
argument_list|<
name|T
argument_list|>
name|object
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|c
parameter_list|)
block|{
name|obj
operator|=
name|object
expr_stmt|;
name|cls
operator|=
name|c
expr_stmt|;
block|}
specifier|public
name|boolean
name|cancel
parameter_list|(
name|boolean
name|interrupt
parameter_list|)
block|{
return|return
name|obj
operator|.
name|cancel
argument_list|(
name|interrupt
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isCancelled
parameter_list|()
block|{
return|return
name|obj
operator|.
name|isCancelled
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isDone
parameter_list|()
block|{
return|return
name|obj
operator|.
name|isDone
argument_list|()
return|;
block|}
specifier|public
specifier|synchronized
name|T
name|get
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
block|{
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
name|cls
operator|.
name|cast
argument_list|(
name|obj
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
name|T
name|get
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
throws|,
name|TimeoutException
block|{
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
name|cls
operator|.
name|cast
argument_list|(
name|obj
operator|.
name|get
argument_list|(
name|timeout
argument_list|,
name|unit
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getContext
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

