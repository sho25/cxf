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
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|Callable
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
name|frontend
operator|.
name|ClientProxy
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
name|service
operator|.
name|model
operator|.
name|BindingOperationInfo
import|;
end_import

begin_class
specifier|public
class|class
name|JAXWSAsyncCallable
implements|implements
name|Callable
argument_list|<
name|Object
argument_list|>
block|{
specifier|private
name|ClientProxy
name|endPointInvocationHandler
decl_stmt|;
specifier|private
name|Method
name|method
decl_stmt|;
specifier|private
name|BindingOperationInfo
name|oi
decl_stmt|;
specifier|private
name|Object
index|[]
name|params
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
decl_stmt|;
specifier|public
name|JAXWSAsyncCallable
parameter_list|(
name|ClientProxy
name|endPointInvocationHandler
parameter_list|,
name|Method
name|method
parameter_list|,
name|BindingOperationInfo
name|oi
parameter_list|,
name|Object
index|[]
name|params
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cxt
parameter_list|)
block|{
name|this
operator|.
name|endPointInvocationHandler
operator|=
name|endPointInvocationHandler
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
name|this
operator|.
name|oi
operator|=
name|oi
expr_stmt|;
name|this
operator|.
name|params
operator|=
name|params
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|cxt
expr_stmt|;
block|}
specifier|public
name|Object
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|endPointInvocationHandler
operator|.
name|getRequestContext
argument_list|()
operator|.
name|putAll
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return
name|endPointInvocationHandler
operator|.
name|invokeSync
argument_list|(
name|method
argument_list|,
name|oi
argument_list|,
name|params
argument_list|)
return|;
block|}
block|}
end_class

end_unit

