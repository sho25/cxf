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
name|http_jetty
operator|.
name|continuations
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|javax
operator|.
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
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
name|WebServiceContext
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
name|Continuation
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
name|ContinuationProvider
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|name
operator|=
literal|"HelloContinuation"
argument_list|,
name|serviceName
operator|=
literal|"HelloContinuationService"
argument_list|,
name|portName
operator|=
literal|"HelloContinuationPort"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/systest/jaxws"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.cxf.systest.http_jetty.continuations.HelloContinuation"
argument_list|)
specifier|public
class|class
name|HelloImplWithWrapppedContinuation
implements|implements
name|HelloContinuation
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Continuation
argument_list|>
name|suspended
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Resource
specifier|private
name|WebServiceContext
name|context
decl_stmt|;
specifier|public
name|String
name|sayHi
parameter_list|(
name|String
name|firstName
parameter_list|,
name|String
name|secondName
parameter_list|)
block|{
name|Continuation
name|continuation
init|=
name|getContinuation
argument_list|(
name|firstName
argument_list|)
decl_stmt|;
if|if
condition|(
name|continuation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Failed to get continuation"
argument_list|)
throw|;
block|}
synchronized|synchronized
init|(
name|continuation
init|)
block|{
if|if
condition|(
name|continuation
operator|.
name|isNew
argument_list|()
condition|)
block|{
name|Object
name|userObject
init|=
name|secondName
operator|!=
literal|null
operator|&&
name|secondName
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|secondName
else|:
literal|null
decl_stmt|;
name|continuation
operator|.
name|setObject
argument_list|(
name|userObject
argument_list|)
expr_stmt|;
name|long
name|timeout
init|=
literal|20000
decl_stmt|;
if|if
condition|(
name|secondName
operator|.
name|startsWith
argument_list|(
literal|"to:"
argument_list|)
condition|)
block|{
name|timeout
operator|=
name|Long
operator|.
name|parseLong
argument_list|(
name|secondName
operator|.
name|substring
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|suspendInvocation
argument_list|(
name|firstName
argument_list|,
name|continuation
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|firstName
argument_list|)
expr_stmt|;
comment|// if the actual parameter is not null
if|if
condition|(
name|secondName
operator|!=
literal|null
operator|&&
name|secondName
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|String
name|surname
init|=
name|continuation
operator|.
name|getObject
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|surname
argument_list|)
expr_stmt|;
block|}
comment|// System.out.println("Saying hi to " + sb.toString());
return|return
literal|"Hi "
operator|+
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
comment|// unreachable
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isRequestSuspended
parameter_list|(
name|String
name|name
parameter_list|)
block|{
synchronized|synchronized
init|(
name|suspended
init|)
block|{
while|while
condition|(
operator|!
name|suspended
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
try|try
block|{
name|suspended
operator|.
name|wait
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|void
name|resumeRequest
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
name|Continuation
name|suspendedCont
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|suspended
init|)
block|{
name|suspendedCont
operator|=
name|suspended
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|suspendedCont
operator|!=
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|suspendedCont
init|)
block|{
name|suspendedCont
operator|.
name|resume
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|suspendInvocation
parameter_list|(
name|String
name|name
parameter_list|,
name|Continuation
name|cont
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
try|try
block|{
name|cont
operator|.
name|suspend
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
synchronized|synchronized
init|(
name|suspended
init|)
block|{
name|suspended
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|cont
argument_list|)
expr_stmt|;
name|suspended
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|Continuation
name|getContinuation
parameter_list|(
name|String
name|name
parameter_list|)
block|{
synchronized|synchronized
init|(
name|suspended
init|)
block|{
name|Continuation
name|suspendedCont
init|=
name|suspended
operator|.
name|remove
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|suspendedCont
operator|!=
literal|null
condition|)
block|{
return|return
name|suspendedCont
return|;
block|}
block|}
name|ContinuationProvider
name|provider
init|=
operator|(
name|ContinuationProvider
operator|)
name|context
operator|.
name|getMessageContext
argument_list|()
operator|.
name|get
argument_list|(
name|ContinuationProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|provider
operator|.
name|getContinuation
argument_list|()
return|;
block|}
block|}
end_class

end_unit

