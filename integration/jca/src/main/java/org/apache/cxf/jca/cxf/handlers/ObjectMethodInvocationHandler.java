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
name|CXFInvocationHandler
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
comment|/**  * Handles invocations for methods defined on java.lang.Object, like hashCode,  * toString and equals  */
end_comment

begin_class
specifier|public
class|class
name|ObjectMethodInvocationHandler
extends|extends
name|CXFInvocationHandlerBase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|EQUALS_METHOD_NAME
init|=
literal|"equals"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TO_STRING_METHOD_NAME
init|=
literal|"toString"
decl_stmt|;
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
name|ObjectMethodInvocationHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|ObjectMethodInvocationHandler
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
name|LOG
operator|.
name|fine
argument_list|(
literal|"ObjectMethodInvocationHandler instance created"
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
name|LOG
operator|.
name|fine
argument_list|(
name|this
operator|+
literal|" on "
operator|+
name|method
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|EQUALS_METHOD_NAME
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|ret
operator|=
name|doEquals
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|TO_STRING_METHOD_NAME
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|ret
operator|=
name|doToString
argument_list|()
expr_stmt|;
block|}
else|else
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
block|}
else|else
block|{
name|ret
operator|=
name|invokeNext
argument_list|(
name|proxy
argument_list|,
name|method
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
comment|/**      * checks for equality based on the underlying target object      */
specifier|private
name|Boolean
name|doEquals
parameter_list|(
name|Object
name|rhs
parameter_list|)
block|{
name|Boolean
name|ret
init|=
name|Boolean
operator|.
name|FALSE
decl_stmt|;
comment|// find the target object and do comparison
if|if
condition|(
name|rhs
operator|instanceof
name|Proxy
condition|)
block|{
name|InvocationHandler
name|rhsHandler
init|=
name|Proxy
operator|.
name|getInvocationHandler
argument_list|(
name|rhs
argument_list|)
decl_stmt|;
if|if
condition|(
name|rhsHandler
operator|instanceof
name|CXFInvocationHandler
condition|)
block|{
name|ret
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|getData
argument_list|()
operator|.
name|getTarget
argument_list|()
operator|==
operator|(
operator|(
name|CXFInvocationHandler
operator|)
name|rhsHandler
operator|)
operator|.
name|getData
argument_list|()
operator|.
name|getTarget
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
block|}
specifier|private
name|String
name|doToString
parameter_list|()
block|{
return|return
literal|"ConnectionHandle. Associated ManagedConnection: "
operator|+
name|getData
argument_list|()
operator|.
name|getManagedConnection
argument_list|()
return|;
block|}
block|}
end_class

end_unit

