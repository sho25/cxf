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
name|jaxws
operator|.
name|handler
operator|.
name|soap
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Map
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
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
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
name|handler
operator|.
name|soap
operator|.
name|SOAPMessageContext
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
name|SoapMessage
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
name|SAAJInInterceptor
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
name|jaxws
operator|.
name|context
operator|.
name|WrappedMessageContext
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
name|Message
import|;
end_import

begin_class
specifier|public
class|class
name|SOAPMessageContextImpl
extends|extends
name|WrappedMessageContext
implements|implements
name|SOAPMessageContext
block|{
specifier|private
specifier|static
specifier|final
name|SAAJInInterceptor
name|SAAJ_IN
init|=
operator|new
name|SAAJInInterceptor
argument_list|()
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|roles
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|SOAPMessageContextImpl
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|super
argument_list|(
name|m
argument_list|,
name|Scope
operator|.
name|HANDLER
argument_list|)
expr_stmt|;
name|roles
operator|.
name|add
argument_list|(
name|getWrappedSoapMessage
argument_list|()
operator|.
name|getVersion
argument_list|()
operator|.
name|getNextRole
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setMessage
parameter_list|(
name|SOAPMessage
name|message
parameter_list|)
block|{
if|if
condition|(
name|getWrappedMessage
argument_list|()
operator|.
name|getContent
argument_list|(
name|Object
operator|.
name|class
argument_list|)
operator|instanceof
name|SOAPMessage
condition|)
block|{
name|getWrappedMessage
argument_list|()
operator|.
name|setContent
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getWrappedMessage
argument_list|()
operator|.
name|setContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|SOAPMessage
name|getMessage
parameter_list|()
block|{
name|SOAPMessage
name|message
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getWrappedMessage
argument_list|()
operator|.
name|getContent
argument_list|(
name|Object
operator|.
name|class
argument_list|)
operator|instanceof
name|SOAPMessage
condition|)
block|{
name|message
operator|=
operator|(
name|SOAPMessage
operator|)
name|getWrappedMessage
argument_list|()
operator|.
name|getContent
argument_list|(
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|=
name|getWrappedMessage
argument_list|()
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|//Only happens to non-Dispatch/Provider case.
if|if
condition|(
literal|null
operator|==
name|message
condition|)
block|{
name|Boolean
name|outboundProperty
init|=
operator|(
name|Boolean
operator|)
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
operator|==
literal|null
operator|||
operator|!
name|outboundProperty
condition|)
block|{
comment|//No SOAPMessage exists yet, so lets create one
name|SAAJ_IN
operator|.
name|handleMessage
argument_list|(
name|getWrappedSoapMessage
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|=
name|getWrappedSoapMessage
argument_list|()
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|message
return|;
block|}
specifier|public
name|Object
index|[]
name|getHeaders
parameter_list|(
name|QName
name|name
parameter_list|,
name|JAXBContext
name|context
parameter_list|,
name|boolean
name|allRoles
parameter_list|)
block|{
name|SOAPMessage
name|msg
init|=
name|getMessage
argument_list|()
decl_stmt|;
name|SOAPHeader
name|header
decl_stmt|;
try|try
block|{
name|header
operator|=
name|msg
operator|.
name|getSOAPPart
argument_list|()
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getHeader
argument_list|()
expr_stmt|;
if|if
condition|(
name|header
operator|==
literal|null
operator|||
operator|!
name|header
operator|.
name|hasChildNodes
argument_list|()
condition|)
block|{
return|return
operator|new
name|Object
index|[
literal|0
index|]
return|;
block|}
name|List
argument_list|<
name|Object
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|SOAPHeaderElement
argument_list|>
name|it
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|header
operator|.
name|examineAllHeaderElements
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|SOAPHeaderElement
name|she
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|allRoles
operator|||
name|roles
operator|.
name|contains
argument_list|(
name|she
operator|.
name|getActor
argument_list|()
argument_list|)
operator|)
operator|&&
name|name
operator|.
name|equals
argument_list|(
name|she
operator|.
name|getElementQName
argument_list|()
argument_list|)
condition|)
block|{
name|ret
operator|.
name|add
argument_list|(
name|context
operator|.
name|createUnmarshaller
argument_list|()
operator|.
name|unmarshal
argument_list|(
name|she
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
operator|.
name|toArray
argument_list|(
operator|new
name|Object
index|[
name|ret
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getRoles
parameter_list|()
block|{
return|return
name|roles
return|;
block|}
specifier|private
name|SoapMessage
name|getWrappedSoapMessage
parameter_list|()
block|{
return|return
operator|(
name|SoapMessage
operator|)
name|getWrappedMessage
argument_list|()
return|;
block|}
specifier|public
name|Object
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|Object
name|o
init|=
name|super
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|MessageContext
operator|.
name|HTTP_RESPONSE_HEADERS
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|||
name|MessageContext
operator|.
name|HTTP_REQUEST_HEADERS
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|Map
name|mp
init|=
operator|(
name|Map
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|mp
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|mp
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
operator|!
name|isRequestor
argument_list|()
operator|&&
name|isOutbound
argument_list|()
operator|&&
name|MessageContext
operator|.
name|HTTP_RESPONSE_HEADERS
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|isRequestor
argument_list|()
operator|&&
name|isOutbound
argument_list|()
operator|&&
name|MessageContext
operator|.
name|HTTP_REQUEST_HEADERS
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
return|return
name|o
return|;
block|}
block|}
end_class

end_unit

