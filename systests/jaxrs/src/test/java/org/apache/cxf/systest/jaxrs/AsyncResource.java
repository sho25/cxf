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
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|POST
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|QueryParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|WebApplicationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|container
operator|.
name|AsyncResponse
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|container
operator|.
name|Suspended
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
operator|.
name|ResponseBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
operator|.
name|Status
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"resource"
argument_list|)
specifier|public
class|class
name|AsyncResource
block|{
specifier|public
specifier|static
specifier|final
name|String
name|RESUMED
init|=
literal|"Response resumed"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FALSE
init|=
literal|"A method returned false"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TRUE
init|=
literal|"A method return true"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|AsyncResponseQueue
index|[]
name|ASYNC_RESPONSES
init|=
block|{
operator|new
name|AsyncResponseQueue
argument_list|()
block|,
operator|new
name|AsyncResponseQueue
argument_list|()
block|,
operator|new
name|AsyncResponseQueue
argument_list|()
block|}
decl_stmt|;
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"suspend"
argument_list|)
specifier|public
name|void
name|suspend
parameter_list|(
annotation|@
name|Suspended
name|AsyncResponse
name|asyncResponse
parameter_list|)
block|{
name|ASYNC_RESPONSES
index|[
literal|0
index|]
operator|.
name|add
argument_list|(
name|asyncResponse
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"suspendthrow"
argument_list|)
specifier|public
name|void
name|suspendthrow
parameter_list|(
annotation|@
name|Suspended
name|AsyncResponse
name|asyncResponse
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|"Oh Dear"
argument_list|,
literal|502
argument_list|)
throw|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"cancelvoid"
argument_list|)
specifier|public
name|String
name|cancel
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"stage"
argument_list|)
name|String
name|stage
parameter_list|)
block|{
name|AsyncResponse
name|response
init|=
name|takeAsyncResponse
argument_list|(
name|stage
argument_list|)
decl_stmt|;
name|boolean
name|ret
init|=
name|response
operator|.
name|cancel
argument_list|()
decl_stmt|;
name|ret
operator|&=
name|response
operator|.
name|cancel
argument_list|()
expr_stmt|;
name|addResponse
argument_list|(
name|response
argument_list|,
name|stage
argument_list|)
expr_stmt|;
return|return
name|ret
condition|?
name|TRUE
else|:
name|FALSE
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"resume"
argument_list|)
specifier|public
name|String
name|resume
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"stage"
argument_list|)
name|String
name|stage
parameter_list|,
name|String
name|response
parameter_list|)
block|{
name|AsyncResponse
name|async
init|=
name|takeAsyncResponse
argument_list|(
name|stage
argument_list|)
decl_stmt|;
name|boolean
name|b
init|=
name|resume
argument_list|(
name|async
argument_list|,
name|response
argument_list|)
decl_stmt|;
name|addResponse
argument_list|(
name|async
argument_list|,
name|stage
argument_list|)
expr_stmt|;
return|return
name|b
condition|?
name|TRUE
else|:
name|FALSE
return|;
block|}
specifier|protected
specifier|static
name|AsyncResponse
name|takeAsyncResponse
parameter_list|(
name|String
name|stageId
parameter_list|)
block|{
return|return
name|takeAsyncResponse
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|stageId
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
specifier|static
name|AsyncResponse
name|takeAsyncResponse
parameter_list|(
name|int
name|stageId
parameter_list|)
block|{
name|AsyncResponse
name|asyncResponse
init|=
literal|null
decl_stmt|;
name|asyncResponse
operator|=
name|ASYNC_RESPONSES
index|[
name|stageId
index|]
operator|.
name|take
argument_list|()
expr_stmt|;
return|return
name|asyncResponse
return|;
block|}
specifier|protected
specifier|static
specifier|final
name|void
name|addResponse
parameter_list|(
name|AsyncResponse
name|response
parameter_list|,
name|String
name|stageId
parameter_list|)
block|{
name|int
name|id
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|stageId
argument_list|)
operator|+
literal|1
decl_stmt|;
name|ASYNC_RESPONSES
index|[
name|id
index|]
operator|.
name|add
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
name|boolean
name|resume
parameter_list|(
name|AsyncResponse
name|takenResponse
parameter_list|,
name|Object
name|response
parameter_list|)
block|{
return|return
name|takenResponse
operator|.
name|resume
argument_list|(
name|response
argument_list|)
return|;
block|}
specifier|protected
specifier|static
name|ResponseBuilder
name|createErrorResponseBuilder
parameter_list|()
block|{
return|return
name|Response
operator|.
name|status
argument_list|(
name|Status
operator|.
name|EXPECTATION_FAILED
argument_list|)
return|;
block|}
specifier|private
specifier|static
class|class
name|AsyncResponseQueue
block|{
name|Queue
argument_list|<
name|AsyncResponse
argument_list|>
name|queue
init|=
operator|new
name|ArrayBlockingQueue
argument_list|<>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|public
name|void
name|add
parameter_list|(
name|AsyncResponse
name|asyncResponse
parameter_list|)
block|{
name|queue
operator|.
name|add
argument_list|(
name|asyncResponse
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AsyncResponse
name|take
parameter_list|()
block|{
return|return
name|queue
operator|.
name|remove
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

