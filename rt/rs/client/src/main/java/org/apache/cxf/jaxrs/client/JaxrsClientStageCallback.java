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
name|jaxrs
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

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
name|CompletableFuture
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
name|CompletionStage
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
name|Executor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
import|;
end_import

begin_class
class|class
name|JaxrsClientStageCallback
parameter_list|<
name|T
parameter_list|>
extends|extends
name|JaxrsClientCallback
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|CompletableFuture
argument_list|<
name|T
argument_list|>
name|cf
decl_stmt|;
name|JaxrsClientStageCallback
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|responseClass
parameter_list|,
name|Type
name|outGenericType
parameter_list|,
name|Executor
name|ex
parameter_list|)
block|{
name|super
argument_list|(
literal|null
argument_list|,
name|responseClass
argument_list|,
name|outGenericType
argument_list|)
expr_stmt|;
name|Supplier
argument_list|<
name|T
argument_list|>
name|supplier
init|=
operator|new
name|SupplierImpl
argument_list|()
decl_stmt|;
name|cf
operator|=
name|ex
operator|==
literal|null
condition|?
name|CompletableFuture
operator|.
name|supplyAsync
argument_list|(
name|supplier
argument_list|)
else|:
name|CompletableFuture
operator|.
name|supplyAsync
argument_list|(
name|supplier
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CompletionStage
argument_list|<
name|T
argument_list|>
name|getCompletionStage
parameter_list|()
block|{
return|return
name|cf
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleResponse
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ctx
parameter_list|,
name|Object
index|[]
name|res
parameter_list|)
block|{
name|context
operator|=
name|ctx
expr_stmt|;
name|result
operator|=
name|res
expr_stmt|;
comment|//consumer.accept((T)res[0]);
name|done
operator|=
literal|true
expr_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleException
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ctx
parameter_list|,
specifier|final
name|Throwable
name|ex
parameter_list|)
block|{
name|context
operator|=
name|ctx
expr_stmt|;
name|exception
operator|=
name|ex
expr_stmt|;
comment|//handler.failed(exception);
name|done
operator|=
literal|true
expr_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|cancel
parameter_list|(
name|boolean
name|mayInterruptIfRunning
parameter_list|)
block|{
name|boolean
name|result
init|=
name|super
operator|.
name|cancel
argument_list|(
name|mayInterruptIfRunning
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
condition|)
block|{
comment|//handler.failed(new CancellationException());
block|}
return|return
name|result
return|;
block|}
specifier|private
class|class
name|SupplierImpl
implements|implements
name|Supplier
argument_list|<
name|T
argument_list|>
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|T
name|get
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|T
operator|)
name|JaxrsClientStageCallback
operator|.
name|this
operator|.
name|get
argument_list|()
index|[
literal|0
index|]
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//handler.failed((InterruptedException)ex);
comment|//throw ex;
return|return
literal|null
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

