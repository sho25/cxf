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
name|binding
operator|.
name|soap
operator|.
name|saaj
package|;
end_package

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
name|SOAPFault
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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|staxutils
operator|.
name|OverlayW3CDOMStreamWriter
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|SAAJStreamWriter
extends|extends
name|OverlayW3CDOMStreamWriter
block|{
specifier|private
specifier|final
name|SOAPPart
name|part
decl_stmt|;
specifier|public
name|SAAJStreamWriter
parameter_list|(
name|SOAPPart
name|part
parameter_list|)
block|{
name|super
argument_list|(
name|part
argument_list|)
expr_stmt|;
name|this
operator|.
name|part
operator|=
name|part
expr_stmt|;
block|}
specifier|public
name|SAAJStreamWriter
parameter_list|(
name|SOAPPart
name|part
parameter_list|,
name|Element
name|current
parameter_list|)
block|{
name|super
argument_list|(
name|part
argument_list|,
name|current
argument_list|)
expr_stmt|;
name|this
operator|.
name|part
operator|=
name|part
expr_stmt|;
block|}
specifier|private
name|SOAPElement
name|adjustPrefix
parameter_list|(
name|SOAPElement
name|e
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
name|prefix
operator|=
literal|""
expr_stmt|;
block|}
try|try
block|{
name|String
name|s
init|=
name|e
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|prefix
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|e
operator|.
name|setPrefix
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
name|e
operator|.
name|removeNamespaceDeclaration
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//likely old old version of SAAJ, we'll just try our best
block|}
return|return
name|e
return|;
block|}
specifier|protected
name|void
name|adjustOverlaidNode
parameter_list|(
name|Node
name|nd2
parameter_list|,
name|String
name|pfx
parameter_list|)
block|{
name|String
name|namespace
init|=
name|nd2
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|namespace
operator|!=
literal|null
operator|&&
name|namespace
operator|.
name|equals
argument_list|(
name|part
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getElementName
argument_list|()
operator|.
name|getURI
argument_list|()
argument_list|)
condition|)
block|{
name|adjustPrefix
argument_list|(
operator|(
name|SOAPElement
operator|)
name|nd2
argument_list|,
name|pfx
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
comment|//ignore, fallback
block|}
name|super
operator|.
name|adjustOverlaidNode
argument_list|(
name|nd2
argument_list|,
name|pfx
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createAndAddElement
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|local
parameter_list|,
name|String
name|namespace
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|namespace
operator|!=
literal|null
operator|&&
name|namespace
operator|.
name|equals
argument_list|(
name|part
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getElementName
argument_list|()
operator|.
name|getURI
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"Envelope"
operator|.
name|equals
argument_list|(
name|local
argument_list|)
condition|)
block|{
name|setChild
argument_list|(
name|adjustPrefix
argument_list|(
name|part
operator|.
name|getEnvelope
argument_list|()
argument_list|,
name|prefix
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|adjustPrefix
argument_list|(
name|part
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getHeader
argument_list|()
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
literal|"Body"
operator|.
name|equals
argument_list|(
name|local
argument_list|)
condition|)
block|{
name|setChild
argument_list|(
name|adjustPrefix
argument_list|(
name|part
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|prefix
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
literal|"Header"
operator|.
name|equals
argument_list|(
name|local
argument_list|)
condition|)
block|{
if|if
condition|(
name|part
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getHeader
argument_list|()
operator|==
literal|null
condition|)
block|{
name|part
operator|.
name|getEnvelope
argument_list|()
operator|.
name|addHeader
argument_list|()
expr_stmt|;
block|}
name|setChild
argument_list|(
name|adjustPrefix
argument_list|(
name|part
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getHeader
argument_list|()
argument_list|,
name|prefix
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
literal|"Fault"
operator|.
name|equals
argument_list|(
name|local
argument_list|)
condition|)
block|{
name|SOAPFault
name|f
init|=
name|part
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|getFault
argument_list|()
decl_stmt|;
if|if
condition|(
name|f
operator|==
literal|null
condition|)
block|{
name|Element
name|el
init|=
name|part
operator|.
name|createElementNS
argument_list|(
name|namespace
argument_list|,
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|prefix
argument_list|)
condition|?
name|local
else|:
name|prefix
operator|+
literal|":"
operator|+
name|local
argument_list|)
decl_stmt|;
name|part
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|appendChild
argument_list|(
name|el
argument_list|)
expr_stmt|;
name|f
operator|=
name|part
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|getFault
argument_list|()
expr_stmt|;
if|if
condition|(
name|f
operator|==
literal|null
condition|)
block|{
name|f
operator|=
name|part
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|addFault
argument_list|()
expr_stmt|;
block|}
block|}
name|setChild
argument_list|(
name|adjustPrefix
argument_list|(
name|f
argument_list|,
name|prefix
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
elseif|else
if|if
condition|(
name|getCurrentNode
argument_list|()
operator|instanceof
name|SOAPFault
condition|)
block|{
name|SOAPFault
name|f
init|=
operator|(
name|SOAPFault
operator|)
name|getCurrentNode
argument_list|()
decl_stmt|;
name|Node
name|nd
init|=
name|f
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|nd
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|nd
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|el
init|=
operator|(
name|Element
operator|)
name|nd
decl_stmt|;
if|if
condition|(
name|local
operator|.
name|equals
argument_list|(
name|nd
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|setChild
argument_list|(
name|el
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|nd
operator|=
name|el
operator|.
name|getFirstChild
argument_list|()
expr_stmt|;
while|while
condition|(
name|nd
operator|!=
literal|null
condition|)
block|{
name|el
operator|.
name|removeChild
argument_list|(
name|nd
argument_list|)
expr_stmt|;
name|nd
operator|=
name|el
operator|.
name|getFirstChild
argument_list|()
expr_stmt|;
block|}
return|return;
block|}
block|}
name|nd
operator|=
name|nd
operator|.
name|getNextSibling
argument_list|()
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
comment|//ignore, fallback
block|}
name|super
operator|.
name|createAndAddElement
argument_list|(
name|prefix
argument_list|,
name|local
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

