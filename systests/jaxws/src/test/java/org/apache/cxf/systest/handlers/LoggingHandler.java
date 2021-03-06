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
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
import|;
end_import

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
name|SOAPMessage
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

begin_comment
comment|/*  * This simple SOAPHandler will output the contents of incoming  * and outgoing messages.  */
end_comment

begin_class
specifier|public
class|class
name|LoggingHandler
implements|implements
name|SOAPHandler
argument_list|<
name|SOAPMessageContext
argument_list|>
block|{
specifier|private
name|PrintStream
name|out
decl_stmt|;
specifier|public
name|LoggingHandler
parameter_list|()
block|{
comment|//setLogStream(System.out);
name|setLogStream
argument_list|(
operator|new
name|PrintStream
argument_list|(
operator|new
name|ByteArrayOutputStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|final
name|void
name|setLogStream
parameter_list|(
name|PrintStream
name|ps
parameter_list|)
block|{
name|out
operator|=
name|ps
expr_stmt|;
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
name|boolean
name|handleMessage
parameter_list|(
name|SOAPMessageContext
name|smc
parameter_list|)
block|{
comment|//System.out.println("LoggingHandler : handleMessage Called....");
name|logToSystemOut
argument_list|(
name|smc
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|handleFault
parameter_list|(
name|SOAPMessageContext
name|smc
parameter_list|)
block|{
comment|//System.out.println("LoggingHandler : handleFault Called....");
name|logToSystemOut
argument_list|(
name|smc
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// nothing to clean up
specifier|public
name|void
name|close
parameter_list|(
name|MessageContext
name|messageContext
parameter_list|)
block|{
comment|//System.out.println("LoggingHandler : close() Called....");
block|}
comment|// nothing to clean up
specifier|public
name|void
name|destroy
parameter_list|()
block|{
comment|//System.out.println("LoggingHandler : destroy() Called....");
block|}
comment|/*      * Check the MESSAGE_OUTBOUND_PROPERTY in the context      * to see if this is an outgoing or incoming message.      * Write a brief message to the print stream and      * output the message. The writeTo() method can throw      * SOAPException or IOException      */
specifier|protected
name|void
name|logToSystemOut
parameter_list|(
name|SOAPMessageContext
name|smc
parameter_list|)
block|{
name|Boolean
name|outboundProperty
init|=
operator|(
name|Boolean
operator|)
name|smc
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|MESSAGE_OUTBOUND_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|outboundProperty
operator|.
name|booleanValue
argument_list|()
condition|)
block|{
comment|//out.println("\nOutbound message:");
block|}
else|else
block|{
comment|//out.println("\nInbound message:");
block|}
name|SOAPMessage
name|message
init|=
name|smc
operator|.
name|getMessage
argument_list|()
decl_stmt|;
try|try
block|{
name|message
operator|.
name|writeTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
comment|//out.println();
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//out.println("Exception in handler: " + e);
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

