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
name|security
operator|.
name|auth
operator|.
name|Subject
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
name|Bus
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
name|CXFManagedConnection
import|;
end_import

begin_class
specifier|abstract
class|class
name|CXFInvocationHandlerBase
implements|implements
name|CXFInvocationHandler
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
name|CXFInvocationHandlerBase
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|CXFInvocationHandler
name|next
decl_stmt|;
specifier|private
name|CXFInvocationHandlerData
name|data
decl_stmt|;
name|CXFInvocationHandlerBase
parameter_list|(
name|CXFInvocationHandlerData
name|cihd
parameter_list|)
block|{
name|this
operator|.
name|data
operator|=
name|cihd
expr_stmt|;
block|}
specifier|public
name|void
name|setNext
parameter_list|(
name|CXFInvocationHandler
name|cih
parameter_list|)
block|{
name|this
operator|.
name|next
operator|=
name|cih
expr_stmt|;
block|}
specifier|public
name|CXFInvocationHandler
name|getNext
parameter_list|()
block|{
return|return
name|next
return|;
block|}
specifier|public
name|CXFInvocationHandlerData
name|getData
parameter_list|()
block|{
return|return
name|data
return|;
block|}
specifier|protected
name|Object
name|invokeNext
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
name|getNext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ret
operator|=
name|getNext
argument_list|()
operator|.
name|invoke
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
comment|// if the next handler is null , there could be end of the handler chains
name|LOG
operator|.
name|fine
argument_list|(
literal|"no more invocation handler"
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|protected
name|Throwable
name|getExceptionToThrow
parameter_list|(
name|InvocationTargetException
name|ex
parameter_list|,
name|Method
name|targetMethod
parameter_list|)
throws|throws
name|Throwable
block|{
name|Throwable
name|targetException
init|=
name|ex
operator|.
name|getTargetException
argument_list|()
decl_stmt|;
name|Throwable
name|ret
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isOkToThrow
argument_list|(
name|targetMethod
argument_list|,
name|targetException
argument_list|)
condition|)
block|{
name|ret
operator|=
name|targetException
expr_stmt|;
block|}
else|else
block|{
comment|//get the exception when call the method
name|RuntimeException
name|re
init|=
operator|new
name|RuntimeException
argument_list|(
literal|"Unexpected exception from method "
operator|+
name|targetMethod
argument_list|,
name|targetException
argument_list|)
decl_stmt|;
name|ret
operator|=
name|re
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|private
name|boolean
name|isOkToThrow
parameter_list|(
name|Method
name|method
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
return|return
name|t
operator|instanceof
name|RuntimeException
operator|||
name|isCheckedException
argument_list|(
name|method
argument_list|,
name|t
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isCheckedException
parameter_list|(
name|Method
name|method
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|boolean
name|isCheckedException
init|=
literal|false
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|checkExceptionTypes
init|=
name|method
operator|.
name|getExceptionTypes
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|checkExceptionTypes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|checkExceptionTypes
index|[
name|i
index|]
operator|.
name|isAssignableFrom
argument_list|(
name|t
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|isCheckedException
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
return|return
name|isCheckedException
return|;
block|}
block|}
end_class

begin_class
class|class
name|CXFInvocationHandlerDataImpl
implements|implements
name|CXFInvocationHandlerData
block|{
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|CXFManagedConnection
name|managedConnection
decl_stmt|;
specifier|private
name|Subject
name|subject
decl_stmt|;
specifier|private
name|Object
name|target
decl_stmt|;
specifier|public
specifier|final
name|void
name|setSubject
parameter_list|(
name|Subject
name|sub
parameter_list|)
block|{
name|this
operator|.
name|subject
operator|=
name|sub
expr_stmt|;
block|}
specifier|public
specifier|final
name|Subject
name|getSubject
parameter_list|()
block|{
return|return
name|subject
return|;
block|}
specifier|public
specifier|final
name|void
name|setBus
parameter_list|(
specifier|final
name|Bus
name|b
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|b
expr_stmt|;
block|}
specifier|public
specifier|final
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|bus
return|;
block|}
specifier|public
specifier|final
name|void
name|setManagedConnection
parameter_list|(
specifier|final
name|CXFManagedConnection
name|cxfManagedConnection
parameter_list|)
block|{
name|this
operator|.
name|managedConnection
operator|=
name|cxfManagedConnection
expr_stmt|;
block|}
specifier|public
specifier|final
name|CXFManagedConnection
name|getManagedConnection
parameter_list|()
block|{
return|return
name|managedConnection
return|;
block|}
specifier|public
name|void
name|setTarget
parameter_list|(
name|Object
name|t
parameter_list|)
block|{
name|this
operator|.
name|target
operator|=
name|t
expr_stmt|;
block|}
specifier|public
name|Object
name|getTarget
parameter_list|()
block|{
return|return
name|target
return|;
block|}
block|}
end_class

end_unit

