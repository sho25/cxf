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
name|Set
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
name|Name
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
name|SOAPEnvelope
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
name|SOAPHeader
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
name|SOAPHeaderElement
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
name|SOAPMessage
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
name|SOAPPart
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
name|handler
operator|.
name|soap
operator|.
name|SOAPHandler
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
name|soap
operator|.
name|SOAPMessageContext
import|;
end_import

begin_class
specifier|public
class|class
name|TestMustUnderstandHandler
parameter_list|<
name|T
extends|extends
name|SOAPMessageContext
parameter_list|>
extends|extends
name|TestHandlerBase
implements|implements
name|SOAPHandler
argument_list|<
name|T
argument_list|>
block|{
specifier|public
name|TestMustUnderstandHandler
parameter_list|()
block|{
name|super
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|handleMessage
parameter_list|(
name|SOAPMessageContext
name|ctx
parameter_list|)
block|{
name|boolean
name|continueProcessing
init|=
literal|true
decl_stmt|;
try|try
block|{
name|Object
name|b
init|=
name|ctx
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|MESSAGE_OUTBOUND_PROPERTY
argument_list|)
decl_stmt|;
name|boolean
name|outbound
init|=
operator|(
name|Boolean
operator|)
name|b
decl_stmt|;
name|SOAPMessage
name|msg
init|=
name|ctx
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|isServerSideHandler
argument_list|()
condition|)
block|{
if|if
condition|(
name|outbound
condition|)
block|{
name|QName
name|qname
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/mu"
argument_list|,
literal|"MU"
argument_list|)
decl_stmt|;
name|SOAPPart
name|soapPart
init|=
name|msg
operator|.
name|getSOAPPart
argument_list|()
decl_stmt|;
name|SOAPEnvelope
name|envelope
init|=
name|soapPart
operator|.
name|getEnvelope
argument_list|()
decl_stmt|;
name|SOAPHeader
name|header
init|=
name|envelope
operator|.
name|getHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|header
operator|==
literal|null
condition|)
block|{
name|header
operator|=
name|envelope
operator|.
name|addHeader
argument_list|()
expr_stmt|;
block|}
name|SOAPHeaderElement
name|headerElement
init|=
name|header
operator|.
name|addHeaderElement
argument_list|(
name|envelope
operator|.
name|createName
argument_list|(
literal|"MU"
argument_list|,
literal|"ns1"
argument_list|,
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
comment|// QName soapMustUnderstand = new QName("http://schemas.xmlsoap.org/soap/envelope/",
comment|// "mustUnderstand");
name|Name
name|name
init|=
name|SOAPFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createName
argument_list|(
literal|"mustUnderstand"
argument_list|,
literal|"soap"
argument_list|,
literal|"http://schemas.xmlsoap.org/soap/envelope/"
argument_list|)
decl_stmt|;
name|headerElement
operator|.
name|addAttribute
argument_list|(
name|name
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getHandlerInfoList
argument_list|(
name|ctx
argument_list|)
operator|.
name|add
argument_list|(
name|getHandlerId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|SOAPException
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
name|continueProcessing
return|;
block|}
specifier|public
name|String
name|getHandlerId
parameter_list|()
block|{
return|return
literal|"TestMustUnderstandHandler"
return|;
block|}
specifier|public
name|Set
argument_list|<
name|QName
argument_list|>
name|getHeaders
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|close
parameter_list|(
name|MessageContext
name|messagecontext
parameter_list|)
block|{     }
specifier|public
name|boolean
name|handleFault
parameter_list|(
name|T
name|messagecontext
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

