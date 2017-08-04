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
name|hello_world_soap_http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|RemoteException
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
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
name|xml
operator|.
name|ws
operator|.
name|AsyncHandler
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
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|types
operator|.
name|BareDocumentResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|types
operator|.
name|GreetMeLaterResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|types
operator|.
name|GreetMeResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|types
operator|.
name|GreetMeSometimeResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|types
operator|.
name|SayHiResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|types
operator|.
name|TestDocLitFaultResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|types
operator|.
name|TestNillableResponse
import|;
end_import

begin_class
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|name
operator|=
literal|"DerivedGreeter"
argument_list|,
name|serviceName
operator|=
literal|"DerivedGreeterService"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/hello_world_soap_http"
argument_list|)
specifier|public
class|class
name|DerivedGreeterImpl
implements|implements
name|Greeter
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|DerivedGreeterImpl
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|invocationCount
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|DerivedGreeterImpl
parameter_list|()
block|{
name|invocationCount
operator|.
name|put
argument_list|(
literal|"sayHi"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|invocationCount
operator|.
name|put
argument_list|(
literal|"greetMe"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|invocationCount
operator|.
name|put
argument_list|(
literal|"greetMeLater"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|invocationCount
operator|.
name|put
argument_list|(
literal|"greetMeOneWay"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|invocationCount
operator|.
name|put
argument_list|(
literal|"overloadedSayHi"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getInvocationCount
parameter_list|(
name|String
name|method
parameter_list|)
block|{
if|if
condition|(
name|invocationCount
operator|.
name|containsKey
argument_list|(
name|method
argument_list|)
condition|)
block|{
return|return
name|invocationCount
operator|.
name|get
argument_list|(
name|method
argument_list|)
operator|.
name|intValue
argument_list|()
return|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"No invocation count for method: "
operator|+
name|method
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
comment|/**      * overloaded method - present for test purposes      */
specifier|public
name|String
name|sayHi
parameter_list|(
name|String
name|me
parameter_list|)
throws|throws
name|RemoteException
block|{
name|incrementInvocationCount
argument_list|(
literal|"overloadedSayHi"
argument_list|)
expr_stmt|;
return|return
literal|"Hi "
operator|+
name|me
operator|+
literal|"!"
return|;
block|}
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"sayHi"
argument_list|)
comment|/*      * @javax.jws.WebResult(name="responseType",      * targetNamespace="http://apache.org/hello_world_soap_http")      */
specifier|public
name|String
name|sayHi
parameter_list|()
block|{
name|incrementInvocationCount
argument_list|(
literal|"sayHi"
argument_list|)
expr_stmt|;
return|return
literal|"Hi"
return|;
block|}
specifier|public
name|void
name|testDocLitFault
parameter_list|(
name|String
name|faultType
parameter_list|)
throws|throws
name|BadRecordLitFault
throws|,
name|NoSuchCodeLitFault
block|{     }
specifier|public
name|Response
argument_list|<
name|TestDocLitFaultResponse
argument_list|>
name|testDocLitFaultAsync
parameter_list|(
name|String
name|faultType
parameter_list|)
block|{
return|return
literal|null
return|;
comment|/*not called */
block|}
specifier|public
name|Response
argument_list|<
name|TestDocLitFaultResponse
argument_list|>
name|testDocLitFaultAsync
parameter_list|(
name|String
name|faultType
parameter_list|,
name|AsyncHandler
argument_list|<
name|TestDocLitFaultResponse
argument_list|>
name|ah
parameter_list|)
block|{
return|return
literal|null
return|;
comment|/*not called */
block|}
specifier|public
name|String
name|greetMe
parameter_list|(
name|String
name|me
parameter_list|)
block|{
name|incrementInvocationCount
argument_list|(
literal|"greetMe"
argument_list|)
expr_stmt|;
return|return
literal|"Bonjour "
operator|+
name|me
operator|+
literal|"!"
return|;
block|}
specifier|public
name|String
name|greetMeLater
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
if|if
condition|(
name|delay
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
comment|/// ignore
block|}
block|}
name|incrementInvocationCount
argument_list|(
literal|"greetMeLater"
argument_list|)
expr_stmt|;
return|return
literal|"Hello, finally!"
return|;
block|}
specifier|public
name|String
name|greetMeSometime
parameter_list|(
name|String
name|me
parameter_list|)
block|{
name|incrementInvocationCount
argument_list|(
literal|"greetMeSometime"
argument_list|)
expr_stmt|;
return|return
literal|"Hello there "
operator|+
name|me
operator|+
literal|"!"
return|;
block|}
specifier|public
name|Future
argument_list|<
name|?
argument_list|>
name|greetMeSometimeAsync
parameter_list|(
name|String
name|requestType
parameter_list|,
name|AsyncHandler
argument_list|<
name|GreetMeSometimeResponse
argument_list|>
name|asyncHandler
parameter_list|)
block|{
return|return
literal|null
return|;
comment|/* to be implemented */
block|}
specifier|public
name|Response
argument_list|<
name|GreetMeSometimeResponse
argument_list|>
name|greetMeSometimeAsync
parameter_list|(
name|String
name|requestType
parameter_list|)
block|{
return|return
literal|null
return|;
comment|/* to be implemented */
block|}
specifier|public
name|Future
argument_list|<
name|?
argument_list|>
name|greetMeAsync
parameter_list|(
name|String
name|requestType
parameter_list|,
name|AsyncHandler
argument_list|<
name|GreetMeResponse
argument_list|>
name|asyncHandler
parameter_list|)
block|{
return|return
literal|null
return|;
comment|/*not called */
block|}
specifier|public
name|Response
argument_list|<
name|GreetMeResponse
argument_list|>
name|greetMeAsync
parameter_list|(
name|String
name|requestType
parameter_list|)
block|{
return|return
literal|null
return|;
comment|/*not called */
block|}
specifier|public
name|Future
argument_list|<
name|?
argument_list|>
name|greetMeLaterAsync
parameter_list|(
name|long
name|requestType
parameter_list|,
name|AsyncHandler
argument_list|<
name|GreetMeLaterResponse
argument_list|>
name|asyncHandler
parameter_list|)
block|{
return|return
literal|null
return|;
comment|/*not called */
block|}
specifier|public
name|Response
argument_list|<
name|GreetMeLaterResponse
argument_list|>
name|greetMeLaterAsync
parameter_list|(
name|long
name|requestType
parameter_list|)
block|{
return|return
literal|null
return|;
comment|/*not called */
block|}
specifier|public
name|Future
argument_list|<
name|?
argument_list|>
name|sayHiAsync
parameter_list|(
name|AsyncHandler
argument_list|<
name|SayHiResponse
argument_list|>
name|asyncHandler
parameter_list|)
block|{
return|return
literal|null
return|;
comment|/*not called */
block|}
specifier|public
name|Response
argument_list|<
name|SayHiResponse
argument_list|>
name|sayHiAsync
parameter_list|()
block|{
return|return
literal|null
return|;
comment|/*not called */
block|}
specifier|public
name|Future
argument_list|<
name|?
argument_list|>
name|testDocLitBareAsync
parameter_list|(
name|String
name|in
parameter_list|,
name|AsyncHandler
argument_list|<
name|BareDocumentResponse
argument_list|>
name|asyncHandler
parameter_list|)
block|{
return|return
literal|null
return|;
comment|/*not called */
block|}
specifier|public
name|Response
argument_list|<
name|BareDocumentResponse
argument_list|>
name|testDocLitBareAsync
parameter_list|(
name|String
name|in
parameter_list|)
block|{
return|return
literal|null
return|;
comment|/*not called */
block|}
specifier|public
name|void
name|greetMeOneWay
parameter_list|(
name|String
name|me
parameter_list|)
block|{
name|incrementInvocationCount
argument_list|(
literal|"greetMeOneWay"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BareDocumentResponse
name|testDocLitBare
parameter_list|(
name|String
name|in
parameter_list|)
block|{
name|incrementInvocationCount
argument_list|(
literal|"testDocLitBare"
argument_list|)
expr_stmt|;
name|BareDocumentResponse
name|res
init|=
operator|new
name|BareDocumentResponse
argument_list|()
decl_stmt|;
name|res
operator|.
name|setCompany
argument_list|(
literal|"CXF"
argument_list|)
expr_stmt|;
name|res
operator|.
name|setId
argument_list|(
literal|1
argument_list|)
expr_stmt|;
return|return
name|res
return|;
block|}
specifier|private
name|void
name|incrementInvocationCount
parameter_list|(
name|String
name|method
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Executing "
operator|+
name|method
argument_list|)
expr_stmt|;
name|int
name|n
init|=
name|invocationCount
operator|.
name|get
argument_list|(
name|method
argument_list|)
decl_stmt|;
name|invocationCount
operator|.
name|put
argument_list|(
name|method
argument_list|,
name|n
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|testNillable
parameter_list|(
name|String
name|nillElem
parameter_list|,
name|int
name|intElem
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
specifier|public
name|Response
argument_list|<
name|TestNillableResponse
argument_list|>
name|testNillableAsync
parameter_list|(
name|String
name|nillElem
parameter_list|,
name|int
name|intElem
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Future
argument_list|<
name|?
argument_list|>
name|testNillableAsync
parameter_list|(
name|String
name|nillElem
parameter_list|,
name|int
name|intElem
parameter_list|,
name|AsyncHandler
argument_list|<
name|TestNillableResponse
argument_list|>
name|asyncHandler
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

