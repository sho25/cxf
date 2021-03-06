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
name|handlers
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|HandlerChain
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
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPFault
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
name|WebServiceException
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPFaultException
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
name|binding
operator|.
name|soap
operator|.
name|saaj
operator|.
name|SAAJUtils
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
name|handler_test
operator|.
name|HandlerTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|handler_test
operator|.
name|PingException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|handler_test
operator|.
name|types
operator|.
name|PingFaultDetails
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"HandlerTestService"
argument_list|,
name|portName
operator|=
literal|"SoapPort"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.handler_test.HandlerTest"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/handler_test"
argument_list|,
name|wsdlLocation
operator|=
literal|"testutils/handler_test.wsdl"
argument_list|)
annotation|@
name|HandlerChain
argument_list|(
name|file
operator|=
literal|"./handlers_invocation.xml"
argument_list|,
name|name
operator|=
literal|"TestHandlerChain"
argument_list|)
specifier|public
class|class
name|HandlerTestImpl
implements|implements
name|HandlerTest
block|{
specifier|private
name|WebServiceContext
name|context
decl_stmt|;
specifier|public
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|ping
parameter_list|()
block|{
try|try
block|{
name|List
argument_list|<
name|String
argument_list|>
name|handlerInfoList
init|=
name|getHandlersInfo
argument_list|(
name|context
operator|.
name|getMessageContext
argument_list|()
argument_list|)
decl_stmt|;
name|handlerInfoList
operator|.
name|add
argument_list|(
literal|"servant"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getMessageContext
argument_list|()
operator|.
name|remove
argument_list|(
literal|"handler.info"
argument_list|)
expr_stmt|;
comment|//System.out.println(">> servant returning list: " + handlerInfoList);
return|return
name|handlerInfoList
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|final
name|void
name|pingOneWay
parameter_list|()
block|{     }
specifier|public
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|pingWithArgs
parameter_list|(
name|String
name|handlerCommand
parameter_list|)
throws|throws
name|PingException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|handlerCommand
argument_list|)
expr_stmt|;
name|ret
operator|.
name|addAll
argument_list|(
name|getHandlersInfo
argument_list|(
name|context
operator|.
name|getMessageContext
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|handlerCommand
operator|.
name|contains
argument_list|(
literal|"servant throw exception"
argument_list|)
condition|)
block|{
name|PingFaultDetails
name|details
init|=
operator|new
name|PingFaultDetails
argument_list|()
decl_stmt|;
name|details
operator|.
name|setDetail
argument_list|(
name|ret
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|PingException
argument_list|(
literal|"from servant"
argument_list|,
name|details
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|handlerCommand
operator|.
name|contains
argument_list|(
literal|"servant throw RuntimeException"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"servant throw RuntimeException"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|handlerCommand
operator|.
name|contains
argument_list|(
literal|"servant throw SOAPFaultException"
argument_list|)
condition|)
block|{
throw|throw
name|createSOAPFaultException
argument_list|(
literal|"servant throws SOAPFaultException"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|handlerCommand
operator|.
name|contains
argument_list|(
literal|"servant throw WebServiceException"
argument_list|)
condition|)
block|{
name|RuntimeException
name|re
init|=
operator|new
name|RuntimeException
argument_list|(
literal|"servant throws RuntimeException"
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|WebServiceException
argument_list|(
literal|"RemoteException with nested RuntimeException"
argument_list|,
name|re
argument_list|)
throw|;
block|}
return|return
name|ret
return|;
block|}
specifier|private
name|SOAPFaultException
name|createSOAPFaultException
parameter_list|(
name|String
name|faultString
parameter_list|)
block|{
try|try
block|{
name|SOAPFault
name|fault
init|=
name|SOAPFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createFault
argument_list|()
decl_stmt|;
name|fault
operator|.
name|setFaultString
argument_list|(
name|faultString
argument_list|)
expr_stmt|;
name|SAAJUtils
operator|.
name|setFaultCode
argument_list|(
name|fault
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/faultcode"
argument_list|,
literal|"Server"
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|SOAPFaultException
argument_list|(
name|fault
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
comment|// do nothing
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Resource
specifier|public
name|void
name|setWebServiceContext
parameter_list|(
name|WebServiceContext
name|ctx
parameter_list|)
block|{
name|context
operator|=
name|ctx
expr_stmt|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getHandlersInfo
parameter_list|(
name|MessageContext
name|ctx
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ret
init|=
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
name|ctx
operator|.
name|get
argument_list|(
literal|"handler.info"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|ret
operator|==
literal|null
condition|)
block|{
name|ret
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
block|}
end_class

end_unit

