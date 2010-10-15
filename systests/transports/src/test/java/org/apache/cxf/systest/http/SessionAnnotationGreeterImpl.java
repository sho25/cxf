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
name|http
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|FactoryType
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
name|greeter_control
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
name|cxf
operator|.
name|greeter_control
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
name|cxf
operator|.
name|greeter_control
operator|.
name|types
operator|.
name|PingMeResponse
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
name|greeter_control
operator|.
name|types
operator|.
name|SayHiResponse
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"GreeterService"
argument_list|,
name|portName
operator|=
literal|"GreeterPort"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.cxf.greeter_control.Greeter"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/greeter_control"
argument_list|)
annotation|@
name|FactoryType
argument_list|(
name|FactoryType
operator|.
name|Type
operator|.
name|Session
argument_list|)
specifier|public
class|class
name|SessionAnnotationGreeterImpl
implements|implements
name|Greeter
block|{
name|String
name|name
decl_stmt|;
specifier|public
name|SessionAnnotationGreeterImpl
parameter_list|()
block|{     }
comment|// greetMe will use session to return last called name
specifier|public
name|String
name|greetMe
parameter_list|(
name|String
name|me
parameter_list|)
block|{
name|name
operator|=
name|me
expr_stmt|;
return|return
literal|"Hello "
operator|+
name|me
return|;
block|}
specifier|public
name|String
name|sayHi
parameter_list|()
block|{
return|return
literal|"Bonjour "
operator|+
name|name
return|;
block|}
specifier|public
name|void
name|pingMe
parameter_list|()
block|{     }
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
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
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
comment|// TODO Auto-generated method stub
return|return
literal|null
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
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|Future
argument_list|<
name|?
argument_list|>
name|pingMeAsync
parameter_list|(
name|AsyncHandler
argument_list|<
name|PingMeResponse
argument_list|>
name|asyncHandler
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
name|PingMeResponse
argument_list|>
name|pingMeAsync
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
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
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
specifier|public
name|Response
argument_list|<
name|SayHiResponse
argument_list|>
name|sayHiAsync
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

