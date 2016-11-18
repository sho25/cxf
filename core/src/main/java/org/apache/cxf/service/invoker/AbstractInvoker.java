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
name|service
operator|.
name|invoker
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Level
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
name|continuations
operator|.
name|SuspendedInvocationException
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
name|helpers
operator|.
name|CastUtils
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
name|message
operator|.
name|Exchange
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
name|message
operator|.
name|FaultMode
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
name|message
operator|.
name|MessageContentsList
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

begin_comment
comment|/**  * Abstract implementation of Invoker.  *<p>  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractInvoker
implements|implements
name|Invoker
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
name|AbstractInvoker
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|Object
name|invoke
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
specifier|final
name|Object
name|serviceObject
init|=
name|getServiceObject
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
try|try
block|{
name|BindingOperationInfo
name|bop
init|=
name|exchange
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
name|MethodDispatcher
name|md
init|=
operator|(
name|MethodDispatcher
operator|)
name|exchange
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
name|Method
name|m
init|=
name|bop
operator|==
literal|null
condition|?
literal|null
else|:
name|md
operator|.
name|getMethod
argument_list|(
name|bop
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|==
literal|null
operator|&&
name|bop
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
operator|new
name|Message
argument_list|(
literal|"MISSING_BINDING_OPERATION"
argument_list|,
name|LOG
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"EXCEPTION_INVOKING_OBJECT"
argument_list|,
name|LOG
argument_list|,
literal|"No binding operation info"
argument_list|,
literal|"unknown method"
argument_list|,
literal|"unknown"
argument_list|)
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|Object
argument_list|>
name|params
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|List
condition|)
block|{
name|params
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|o
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
name|params
operator|=
operator|new
name|MessageContentsList
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
name|m
operator|=
name|adjustMethodAndParams
argument_list|(
name|m
argument_list|,
name|exchange
argument_list|,
name|params
argument_list|,
name|serviceObject
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
comment|//Method m = (Method)bop.getOperationInfo().getProperty(Method.class.getName());
name|m
operator|=
name|matchMethod
argument_list|(
name|m
argument_list|,
name|serviceObject
argument_list|)
expr_stmt|;
return|return
name|invoke
argument_list|(
name|exchange
argument_list|,
name|serviceObject
argument_list|,
name|m
argument_list|,
name|params
argument_list|)
return|;
block|}
finally|finally
block|{
name|releaseServiceObject
argument_list|(
name|exchange
argument_list|,
name|serviceObject
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Method
name|adjustMethodAndParams
parameter_list|(
name|Method
name|m
parameter_list|,
name|Exchange
name|ex
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|params
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|serviceObjectClass
parameter_list|)
block|{
comment|//nothing to do
return|return
name|m
return|;
block|}
specifier|protected
name|Object
name|invoke
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Object
name|serviceObject
parameter_list|,
name|Method
name|m
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|params
parameter_list|)
block|{
name|Object
name|res
decl_stmt|;
try|try
block|{
name|Object
index|[]
name|paramArray
init|=
operator|new
name|Object
index|[]
block|{}
decl_stmt|;
if|if
condition|(
name|params
operator|!=
literal|null
condition|)
block|{
name|paramArray
operator|=
name|params
operator|.
name|toArray
argument_list|()
expr_stmt|;
block|}
name|res
operator|=
name|performInvocation
argument_list|(
name|exchange
argument_list|,
name|serviceObject
argument_list|,
name|m
argument_list|,
name|paramArray
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|MessageContentsList
argument_list|(
name|res
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
name|Throwable
name|t
init|=
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
name|t
operator|=
name|e
expr_stmt|;
block|}
name|checkSuspendedInvocation
argument_list|(
name|exchange
argument_list|,
name|serviceObject
argument_list|,
name|m
argument_list|,
name|params
argument_list|,
name|t
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|put
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|,
name|FaultMode
operator|.
name|UNCHECKED_APPLICATION_FAULT
argument_list|)
expr_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|cl
range|:
name|m
operator|.
name|getExceptionTypes
argument_list|()
control|)
block|{
if|if
condition|(
name|cl
operator|.
name|isInstance
argument_list|(
name|t
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|put
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|,
name|FaultMode
operator|.
name|CHECKED_APPLICATION_FAULT
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|t
operator|instanceof
name|Fault
condition|)
block|{
name|exchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|put
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|,
name|FaultMode
operator|.
name|CHECKED_APPLICATION_FAULT
argument_list|)
expr_stmt|;
throw|throw
operator|(
name|Fault
operator|)
name|t
throw|;
block|}
throw|throw
name|createFault
argument_list|(
name|t
argument_list|,
name|m
argument_list|,
name|params
argument_list|,
literal|true
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|SuspendedInvocationException
name|suspendedEx
parameter_list|)
block|{
comment|// to avoid duplicating the same log statement
name|checkSuspendedInvocation
argument_list|(
name|exchange
argument_list|,
name|serviceObject
argument_list|,
name|m
argument_list|,
name|params
argument_list|,
name|suspendedEx
argument_list|)
expr_stmt|;
comment|// unreachable
throw|throw
name|suspendedEx
throw|;
block|}
catch|catch
parameter_list|(
name|Fault
name|f
parameter_list|)
block|{
name|exchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|put
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|,
name|FaultMode
operator|.
name|UNCHECKED_APPLICATION_FAULT
argument_list|)
expr_stmt|;
throw|throw
name|f
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|checkSuspendedInvocation
argument_list|(
name|exchange
argument_list|,
name|serviceObject
argument_list|,
name|m
argument_list|,
name|params
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|put
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|,
name|FaultMode
operator|.
name|UNCHECKED_APPLICATION_FAULT
argument_list|)
expr_stmt|;
throw|throw
name|createFault
argument_list|(
name|e
argument_list|,
name|m
argument_list|,
name|params
argument_list|,
literal|false
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|checkSuspendedInvocation
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|serviceObject
parameter_list|,
name|Method
name|m
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|params
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|t
operator|instanceof
name|SuspendedInvocationException
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"SUSPENDED_INVOCATION_EXCEPTION"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|serviceObject
block|,
name|m
operator|.
name|toString
argument_list|()
block|,
name|params
block|}
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|(
name|SuspendedInvocationException
operator|)
name|t
throw|;
block|}
block|}
specifier|protected
name|Fault
name|createFault
parameter_list|(
name|Throwable
name|ex
parameter_list|,
name|Method
name|m
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|params
parameter_list|,
name|boolean
name|checked
parameter_list|)
block|{
if|if
condition|(
name|checked
condition|)
block|{
return|return
operator|new
name|Fault
argument_list|(
name|ex
argument_list|)
return|;
block|}
else|else
block|{
name|String
name|message
init|=
operator|(
name|ex
operator|==
literal|null
operator|)
condition|?
literal|""
else|:
name|ex
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|String
name|method
init|=
operator|(
name|m
operator|==
literal|null
operator|)
condition|?
literal|"<null>"
else|:
name|m
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
operator|new
name|Fault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"EXCEPTION_INVOKING_OBJECT"
argument_list|,
name|LOG
argument_list|,
name|message
argument_list|,
name|method
argument_list|,
name|params
argument_list|)
argument_list|,
name|ex
argument_list|)
return|;
block|}
block|}
specifier|protected
name|Object
name|performInvocation
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Object
name|serviceObject
parameter_list|,
name|Method
name|m
parameter_list|,
name|Object
index|[]
name|paramArray
parameter_list|)
throws|throws
name|Exception
block|{
name|paramArray
operator|=
name|insertExchange
argument_list|(
name|m
argument_list|,
name|paramArray
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINER
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINER
argument_list|,
literal|"INVOKING_METHOD"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|serviceObject
block|,
name|m
block|,
name|Arrays
operator|.
name|asList
argument_list|(
name|paramArray
argument_list|)
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|m
operator|.
name|invoke
argument_list|(
name|serviceObject
argument_list|,
name|paramArray
argument_list|)
return|;
block|}
specifier|public
name|Object
index|[]
name|insertExchange
parameter_list|(
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|params
parameter_list|,
name|Exchange
name|context
parameter_list|)
block|{
name|Object
index|[]
name|newParams
init|=
name|params
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
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|method
operator|.
name|getParameterTypes
argument_list|()
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
condition|)
block|{
name|newParams
operator|=
operator|new
name|Object
index|[
name|params
operator|.
name|length
operator|+
literal|1
index|]
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|newParams
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
name|j
operator|==
name|i
condition|)
block|{
name|newParams
index|[
name|j
index|]
operator|=
name|context
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|j
operator|>
name|i
condition|)
block|{
name|newParams
index|[
name|j
index|]
operator|=
name|params
index|[
name|j
operator|-
literal|1
index|]
expr_stmt|;
block|}
else|else
block|{
name|newParams
index|[
name|j
index|]
operator|=
name|params
index|[
name|j
index|]
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|newParams
return|;
block|}
comment|/**      * Creates and returns a service object depending on the scope.      */
specifier|public
specifier|abstract
name|Object
name|getServiceObject
parameter_list|(
name|Exchange
name|context
parameter_list|)
function_decl|;
comment|/**      * Called when the invoker is done with the object.   Default implementation      * does nothing.      * @param context      * @param obj      */
specifier|public
name|void
name|releaseServiceObject
parameter_list|(
specifier|final
name|Exchange
name|context
parameter_list|,
name|Object
name|obj
parameter_list|)
block|{     }
comment|/**      * Returns a Method that has the same declaring class as the class of      * targetObject to avoid the IllegalArgumentException when invoking the      * method on the target object. The methodToMatch will be returned if the      * targetObject doesn't have a similar method.      *       * @param methodToMatch The method to be used when finding a matching method      *            in targetObject      * @param targetObject The object to search in for the method.      * @return The methodToMatch if no such method exist in the class of      *         targetObject; otherwise, a method from the class of targetObject      *         matching the matchToMethod method.      */
specifier|private
specifier|static
name|Method
name|matchMethod
parameter_list|(
name|Method
name|methodToMatch
parameter_list|,
name|Object
name|targetObject
parameter_list|)
block|{
if|if
condition|(
name|isJdkDynamicProxy
argument_list|(
name|targetObject
argument_list|)
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|interfaces
init|=
name|targetObject
operator|.
name|getClass
argument_list|()
operator|.
name|getInterfaces
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
name|interfaces
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Method
name|m
init|=
name|getMostSpecificMethod
argument_list|(
name|methodToMatch
argument_list|,
name|interfaces
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|methodToMatch
operator|.
name|equals
argument_list|(
name|m
argument_list|)
condition|)
block|{
return|return
name|m
return|;
block|}
block|}
block|}
return|return
name|methodToMatch
return|;
block|}
comment|/**      * Return whether the given object is a J2SE dynamic proxy.      *       * @param object the object to check      * @see java.lang.reflect.Proxy#isProxyClass      */
specifier|public
specifier|static
name|boolean
name|isJdkDynamicProxy
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|object
operator|!=
literal|null
operator|&&
name|Proxy
operator|.
name|isProxyClass
argument_list|(
name|object
operator|.
name|getClass
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Given a method, which may come from an interface, and a targetClass used      * in the current AOP invocation, find the most specific method if there is      * one. E.g. the method may be IFoo.bar() and the target class may be      * DefaultFoo. In this case, the method may be DefaultFoo.bar(). This      * enables attributes on that method to be found.      *       * @param method method to be invoked, which may come from an interface      * @param targetClass target class for the current invocation. May be      *<code>null</code> or may not even implement the method.      * @return the more specific method, or the original method if the      *         targetClass doesn't specialize it or implement it or is null      */
specifier|public
specifier|static
name|Method
name|getMostSpecificMethod
parameter_list|(
name|Method
name|method
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|targetClass
parameter_list|)
block|{
if|if
condition|(
name|method
operator|!=
literal|null
operator|&&
name|targetClass
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|method
operator|=
name|targetClass
operator|.
name|getMethod
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|,
name|method
operator|.
name|getParameterTypes
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|ex
parameter_list|)
block|{
comment|// Perhaps the target class doesn't implement this method:
comment|// that's fine, just use the original method
block|}
block|}
return|return
name|method
return|;
block|}
block|}
end_class

end_unit

