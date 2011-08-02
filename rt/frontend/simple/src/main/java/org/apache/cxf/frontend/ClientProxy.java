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
name|frontend
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
name|InvocationHandler
import|;
end_import

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
name|lang
operator|.
name|reflect
operator|.
name|Proxy
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
name|logging
operator|.
name|Logger
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
name|common
operator|.
name|i18n
operator|.
name|Message
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|endpoint
operator|.
name|Client
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
name|endpoint
operator|.
name|Endpoint
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
name|interceptor
operator|.
name|Fault
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
name|ClientProxy
implements|implements
name|InvocationHandler
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|ClientProxy
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|Client
name|client
decl_stmt|;
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
specifier|public
name|ClientProxy
parameter_list|(
name|Client
name|c
parameter_list|)
block|{
name|endpoint
operator|=
name|c
operator|.
name|getEndpoint
argument_list|()
expr_stmt|;
name|client
operator|=
name|c
expr_stmt|;
block|}
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
name|MethodDispatcher
name|dispatcher
init|=
operator|(
name|MethodDispatcher
operator|)
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|get
argument_list|(
name|MethodDispatcher
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|BindingOperationInfo
name|oi
init|=
name|dispatcher
operator|.
name|getBindingOperation
argument_list|(
name|method
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|oi
operator|==
literal|null
condition|)
block|{
comment|// check for method on BindingProvider and Object
if|if
condition|(
name|method
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|equals
argument_list|(
name|Object
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
name|method
operator|.
name|invoke
argument_list|(
name|this
argument_list|)
return|;
block|}
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"NO_OPERATION_INFO"
argument_list|,
name|LOG
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|Object
index|[]
name|params
init|=
name|args
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|params
condition|)
block|{
name|params
operator|=
operator|new
name|Object
index|[
literal|0
index|]
expr_stmt|;
block|}
return|return
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
specifier|public
name|Object
name|invokeSync
parameter_list|(
name|Method
name|method
parameter_list|,
name|BindingOperationInfo
name|oi
parameter_list|,
name|Object
index|[]
name|params
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|rawRet
index|[]
init|=
name|client
operator|.
name|invoke
argument_list|(
name|oi
argument_list|,
name|params
argument_list|)
decl_stmt|;
if|if
condition|(
name|rawRet
operator|!=
literal|null
operator|&&
name|rawRet
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
name|rawRet
index|[
literal|0
index|]
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getRequestContext
parameter_list|()
block|{
return|return
name|client
operator|.
name|getRequestContext
argument_list|()
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getResponseContext
parameter_list|()
block|{
return|return
name|client
operator|.
name|getResponseContext
argument_list|()
return|;
block|}
specifier|public
name|Client
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|finalize
parameter_list|()
throws|throws
name|Throwable
block|{
name|client
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|super
operator|.
name|finalize
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
name|Client
name|getClient
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
operator|(
operator|(
name|ClientProxy
operator|)
name|Proxy
operator|.
name|getInvocationHandler
argument_list|(
name|o
argument_list|)
operator|)
operator|.
name|getClient
argument_list|()
return|;
block|}
block|}
end_class

end_unit

