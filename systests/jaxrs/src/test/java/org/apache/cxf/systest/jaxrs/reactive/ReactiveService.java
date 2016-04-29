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
operator|.
name|reactive
package|;
end_package

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
name|Produces
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
name|rx
operator|.
name|Observable
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|Subscriber
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/reactive"
argument_list|)
specifier|public
class|class
name|ReactiveService
block|{
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"text"
argument_list|)
specifier|public
name|Observable
argument_list|<
name|String
argument_list|>
name|getText
parameter_list|()
block|{
return|return
name|Observable
operator|.
name|just
argument_list|(
literal|"Hello, world!"
argument_list|)
return|;
block|}
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"textAsync"
argument_list|)
specifier|public
name|void
name|getTextAsync
parameter_list|(
annotation|@
name|Suspended
specifier|final
name|AsyncResponse
name|ar
parameter_list|)
block|{
name|Observable
operator|.
name|just
argument_list|(
literal|"Hello, "
argument_list|)
operator|.
name|map
argument_list|(
name|s
lambda|->
name|s
operator|+
literal|"world!"
argument_list|)
operator|.
name|subscribe
argument_list|(
operator|new
name|AsyncResponseSubscriber
argument_list|(
name|ar
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"textJson"
argument_list|)
specifier|public
name|Observable
argument_list|<
name|HelloWorldBean
argument_list|>
name|getJsonText
parameter_list|()
block|{
return|return
name|Observable
operator|.
name|just
argument_list|(
operator|new
name|HelloWorldBean
argument_list|()
argument_list|)
return|;
block|}
specifier|private
class|class
name|AsyncResponseSubscriber
extends|extends
name|Subscriber
argument_list|<
name|String
argument_list|>
block|{
specifier|private
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
specifier|private
name|AsyncResponse
name|ar
decl_stmt|;
name|AsyncResponseSubscriber
parameter_list|(
name|AsyncResponse
name|ar
parameter_list|)
block|{
name|this
operator|.
name|ar
operator|=
name|ar
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onCompleted
parameter_list|()
block|{
name|ar
operator|.
name|resume
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onError
parameter_list|(
name|Throwable
name|arg0
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
annotation|@
name|Override
specifier|public
name|void
name|onNext
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

