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
name|ws
operator|.
name|eventing
operator|.
name|shared
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
name|SOAPElement
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

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|ReferenceParametersType
import|;
end_import

begin_class
specifier|public
class|class
name|ReferenceParametersAddingHandler
implements|implements
name|SOAPHandler
argument_list|<
name|SOAPMessageContext
argument_list|>
block|{
specifier|private
specifier|final
name|ReferenceParametersType
name|params
decl_stmt|;
specifier|public
name|ReferenceParametersAddingHandler
parameter_list|(
name|ReferenceParametersType
name|parametersType
parameter_list|)
block|{
name|this
operator|.
name|params
operator|=
name|parametersType
expr_stmt|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
name|boolean
name|handleMessage
parameter_list|(
name|SOAPMessageContext
name|context
parameter_list|)
block|{
comment|// we are interested only in outbound messages here
if|if
condition|(
operator|!
operator|(
name|Boolean
operator|)
name|context
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|MESSAGE_OUTBOUND_PROPERTY
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|params
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
try|try
block|{
name|SOAPFactory
name|factory
init|=
name|SOAPFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|params
operator|.
name|getAny
argument_list|()
control|)
block|{
name|SOAPElement
name|elm
init|=
name|factory
operator|.
name|createElement
argument_list|(
operator|(
name|Element
operator|)
name|o
argument_list|)
decl_stmt|;
name|context
operator|.
name|getMessage
argument_list|()
operator|.
name|getSOAPHeader
argument_list|()
operator|.
name|addChildElement
argument_list|(
name|SOAPFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createElement
argument_list|(
name|elm
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|handleFault
parameter_list|(
name|SOAPMessageContext
name|context
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|(
name|MessageContext
name|context
parameter_list|)
block|{     }
block|}
end_class

end_unit

