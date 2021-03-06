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
operator|.
name|handlers
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
name|InvocationTargetException
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
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|ResourceException
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
name|jca
operator|.
name|cxf
operator|.
name|CXFInvocationHandlerData
import|;
end_import

begin_comment
comment|/**  * delegates invocations to the target object  */
end_comment

begin_class
specifier|public
class|class
name|InvokingInvocationHandler
extends|extends
name|CXFInvocationHandlerBase
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
name|InvokingInvocationHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|InvokingInvocationHandler
parameter_list|(
name|CXFInvocationHandlerData
name|data
parameter_list|)
block|{
name|super
argument_list|(
name|data
argument_list|)
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
name|Object
name|ret
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|isConnectionCloseMethod
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|ret
operator|=
name|invokeTargetMethod
argument_list|(
name|proxy
argument_list|,
name|method
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|closeConnection
argument_list|(
name|proxy
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|private
name|boolean
name|isConnectionCloseMethod
parameter_list|(
name|Method
name|m
parameter_list|)
block|{
return|return
literal|"close"
operator|.
name|equals
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|void
name|closeConnection
parameter_list|(
name|Object
name|handle
parameter_list|)
throws|throws
name|ResourceException
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"calling close on managed connection with handle"
argument_list|)
expr_stmt|;
name|getData
argument_list|()
operator|.
name|getManagedConnection
argument_list|()
operator|.
name|close
argument_list|(
name|handle
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Object
name|invokeTargetMethod
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
name|Object
name|ret
init|=
literal|null
decl_stmt|;
try|try
block|{
name|ret
operator|=
name|method
operator|.
name|invoke
argument_list|(
name|getData
argument_list|()
operator|.
name|getTarget
argument_list|()
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|ite
parameter_list|)
block|{
throw|throw
name|ite
operator|.
name|getTargetException
argument_list|()
throw|;
block|}
return|return
name|ret
return|;
block|}
block|}
end_class

end_unit

