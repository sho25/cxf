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
name|ws
operator|.
name|addressing
package|;
end_package

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
name|ws
operator|.
name|addressing
operator|.
name|AddressingProperties
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
name|BadRecordLitFault
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
name|Greeter
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
name|NoSuchCodeLitFault
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
name|ErrorCode
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
name|NoSuchCodeLit
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|JAXWSAConstants
operator|.
name|SERVER_ADDRESSING_PROPERTIES_INBOUND
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"SOAPServiceAddressing"
argument_list|,
name|portName
operator|=
literal|"SoapPort"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.hello_world_soap_http.Greeter"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
name|wsdlLocation
operator|=
literal|"testutils/hello_world.wsdl"
argument_list|)
specifier|public
class|class
name|GreeterImpl
implements|implements
name|Greeter
block|{
name|VerificationCache
name|verificationCache
decl_stmt|;
comment|/**      * Injectable context.      */
annotation|@
name|Resource
specifier|private
name|WebServiceContext
name|context
decl_stmt|;
specifier|public
name|String
name|greetMe
parameter_list|(
name|String
name|me
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\n\n*** GreetMe called with: "
operator|+
name|me
operator|+
literal|"***\n\n"
argument_list|)
expr_stmt|;
name|verifyMAPs
argument_list|()
expr_stmt|;
return|return
literal|"Hello "
operator|+
name|me
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\n\n*** GreetMeLater called with: "
operator|+
name|delay
operator|+
literal|"***\n\n"
argument_list|)
expr_stmt|;
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
comment|// ignore
block|}
block|}
name|verifyMAPs
argument_list|()
expr_stmt|;
return|return
literal|"Hello, finally"
return|;
block|}
specifier|public
name|void
name|greetMeOneWay
parameter_list|(
name|String
name|requestType
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\n\n*** GreetMeOneWay called with: "
operator|+
name|requestType
operator|+
literal|"***\n\n"
argument_list|)
expr_stmt|;
name|verifyMAPs
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|sayHi
parameter_list|()
block|{
name|verifyMAPs
argument_list|()
expr_stmt|;
return|return
literal|"Bonjour"
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
block|{
name|verifyMAPs
argument_list|()
expr_stmt|;
if|if
condition|(
name|faultType
operator|.
name|equals
argument_list|(
name|BadRecordLitFault
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRecordLitFault
argument_list|(
literal|"TestBadRecordLit"
argument_list|,
literal|"BadRecordLitFault"
argument_list|)
throw|;
block|}
if|if
condition|(
name|faultType
operator|.
name|equals
argument_list|(
name|NoSuchCodeLitFault
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
condition|)
block|{
name|ErrorCode
name|ec
init|=
operator|new
name|ErrorCode
argument_list|()
decl_stmt|;
name|ec
operator|.
name|setMajor
argument_list|(
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
name|ec
operator|.
name|setMinor
argument_list|(
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
name|NoSuchCodeLit
name|nscl
init|=
operator|new
name|NoSuchCodeLit
argument_list|()
decl_stmt|;
name|nscl
operator|.
name|setCode
argument_list|(
name|ec
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|NoSuchCodeLitFault
argument_list|(
literal|"TestNoSuchCodeLit"
argument_list|,
name|nscl
argument_list|)
throw|;
block|}
block|}
specifier|public
name|BareDocumentResponse
name|testDocLitBare
parameter_list|(
name|String
name|in
parameter_list|)
block|{
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
literal|"Celtix"
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
name|verifyMAPs
parameter_list|()
block|{
if|if
condition|(
name|context
operator|.
name|getMessageContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|property
init|=
name|SERVER_ADDRESSING_PROPERTIES_INBOUND
decl_stmt|;
name|AddressingProperties
name|maps
init|=
operator|(
name|AddressingProperties
operator|)
name|context
operator|.
name|getMessageContext
argument_list|()
operator|.
name|get
argument_list|(
name|property
argument_list|)
decl_stmt|;
name|verificationCache
operator|.
name|put
argument_list|(
name|MAPTest
operator|.
name|verifyMAPs
argument_list|(
name|maps
argument_list|,
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|greetMeSometime
parameter_list|(
name|String
name|me
parameter_list|)
block|{
return|return
literal|"How are you "
operator|+
name|me
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
comment|/*not called */
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
name|testDocLitFaultAsync
parameter_list|(
name|String
name|faultType
parameter_list|,
name|AsyncHandler
name|ah
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
name|testDocLitBareAsync
parameter_list|(
name|String
name|bare
parameter_list|,
name|AsyncHandler
name|ah
parameter_list|)
block|{
return|return
literal|null
return|;
comment|/* not called */
block|}
specifier|public
name|Response
argument_list|<
name|BareDocumentResponse
argument_list|>
name|testDocLitBareAsync
parameter_list|(
name|String
name|bare
parameter_list|)
block|{
return|return
literal|null
return|;
comment|/* not called */
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

