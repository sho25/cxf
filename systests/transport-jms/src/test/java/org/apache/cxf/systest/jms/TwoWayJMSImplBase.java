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
name|jms
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|MessageContext
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
name|hello_world_jms
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
name|cxf
operator|.
name|hello_world_jms
operator|.
name|HelloWorldPortType
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
name|hello_world_jms
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
name|cxf
operator|.
name|hello_world_jms
operator|.
name|types
operator|.
name|BadRecordLit
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
name|hello_world_jms
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
name|cxf
operator|.
name|hello_world_jms
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
name|cxf
operator|.
name|hello_world_jms
operator|.
name|types
operator|.
name|TestRpcLitFaultResponse
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
name|transport
operator|.
name|jms
operator|.
name|JMSConstants
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
name|transport
operator|.
name|jms
operator|.
name|JMSMessageHeadersType
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
name|transport
operator|.
name|jms
operator|.
name|JMSPropertyType
import|;
end_import

begin_class
specifier|public
class|class
name|TwoWayJMSImplBase
implements|implements
name|HelloWorldPortType
block|{
annotation|@
name|Resource
specifier|protected
name|WebServiceContext
name|wsContext
decl_stmt|;
specifier|public
name|String
name|greetMe
parameter_list|(
name|String
name|me
parameter_list|)
block|{
if|if
condition|(
name|me
operator|.
name|startsWith
argument_list|(
literal|"PauseForTwoSecs"
argument_list|)
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
name|me
operator|=
name|me
operator|.
name|substring
argument_list|(
literal|"PauseForTwoSecs"
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
name|MessageContext
name|mc
init|=
name|wsContext
operator|.
name|getMessageContext
argument_list|()
decl_stmt|;
comment|//JMSMessageHeadersType headers =
comment|//    (JMSMessageHeadersType) mc.get(JMSConstants.JMS_SERVER_REQUEST_HEADERS);
comment|//System.out.println("get the message headers JMSCorrelationID: " + headers.getJMSCorrelationID());
comment|//System.out.println("Reached here :" + me);
comment|// set reply header custom property
name|JMSPropertyType
name|testProperty
init|=
operator|new
name|JMSPropertyType
argument_list|()
decl_stmt|;
name|testProperty
operator|.
name|setName
argument_list|(
literal|"Test_Prop"
argument_list|)
expr_stmt|;
name|testProperty
operator|.
name|setValue
argument_list|(
literal|"some return value "
operator|+
name|me
argument_list|)
expr_stmt|;
comment|//System.out.println("found property in request headers at index: "
comment|//                   + headers.getProperty().indexOf(testProperty));
name|JMSMessageHeadersType
name|responseHeaders
init|=
operator|(
name|JMSMessageHeadersType
operator|)
name|mc
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_SERVER_RESPONSE_HEADERS
argument_list|)
decl_stmt|;
name|responseHeaders
operator|.
name|getProperty
argument_list|()
operator|.
name|add
argument_list|(
name|testProperty
argument_list|)
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
literal|"Bonjour"
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
comment|//System.out.println("*********  greetMeOneWay: " + requestType);
block|}
specifier|public
name|TestRpcLitFaultResponse
name|testRpcLitFault
parameter_list|(
name|String
name|faultType
parameter_list|)
throws|throws
name|BadRecordLitFault
throws|,
name|NoSuchCodeLitFault
block|{
name|BadRecordLit
name|badRecord
init|=
operator|new
name|BadRecordLit
argument_list|()
decl_stmt|;
name|badRecord
operator|.
name|setReason
argument_list|(
literal|"BadRecordLitFault"
argument_list|)
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
name|badRecord
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
return|return
operator|new
name|TestRpcLitFaultResponse
argument_list|()
return|;
block|}
specifier|public
name|Response
argument_list|<
name|String
argument_list|>
name|greetMeAsync
parameter_list|(
name|String
name|stringParam0
parameter_list|)
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
name|greetMeAsync
parameter_list|(
name|String
name|stringParam0
parameter_list|,
name|AsyncHandler
argument_list|<
name|String
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
name|String
argument_list|>
name|sayHiAsync
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
name|String
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
name|TestRpcLitFaultResponse
argument_list|>
name|testRpcLitFaultAsync
parameter_list|(
name|String
name|in
parameter_list|)
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
name|testRpcLitFaultAsync
parameter_list|(
name|String
name|in
parameter_list|,
name|AsyncHandler
argument_list|<
name|TestRpcLitFaultResponse
argument_list|>
name|asyncHandler
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

