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
name|interceptor
package|;
end_package

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
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|events
operator|.
name|XMLEvent
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
name|wsdl
operator|.
name|extensions
operator|.
name|SoapBody
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
name|logging
operator|.
name|LogUtils
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
name|helpers
operator|.
name|NSStack
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
name|interceptor
operator|.
name|AbstractOutDatabindingInterceptor
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
name|interceptor
operator|.
name|Fault
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
name|MessageContentsList
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
name|phase
operator|.
name|Phase
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
name|service
operator|.
name|model
operator|.
name|BindingOperationInfo
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
name|service
operator|.
name|model
operator|.
name|MessagePartInfo
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
name|CachingXmlEventWriter
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
name|StaxUtils
import|;
end_import

begin_class
specifier|public
class|class
name|RPCOutInterceptor
extends|extends
name|AbstractOutDatabindingInterceptor
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|RPCOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|RPCOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|MARSHAL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|XMLStreamWriter
name|origXmlWriter
init|=
literal|null
decl_stmt|;
try|try
block|{
name|NSStack
name|nsStack
init|=
operator|new
name|NSStack
argument_list|()
decl_stmt|;
name|nsStack
operator|.
name|push
argument_list|()
expr_stmt|;
name|BindingOperationInfo
name|operation
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
assert|assert
name|operation
operator|.
name|getName
argument_list|()
operator|!=
literal|null
assert|;
name|XMLStreamWriter
name|xmlWriter
init|=
name|getXMLStreamWriter
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|CachingXmlEventWriter
name|cache
init|=
literal|null
decl_stmt|;
comment|// need to cache the events in case validation fails or buffering is enabled
if|if
condition|(
name|shouldBuffer
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|origXmlWriter
operator|=
name|xmlWriter
expr_stmt|;
name|cache
operator|=
operator|new
name|CachingXmlEventWriter
argument_list|()
expr_stmt|;
try|try
block|{
name|cache
operator|.
name|setNamespaceContext
argument_list|(
name|xmlWriter
operator|.
name|getNamespaceContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
comment|//ignorable, will just get extra namespace decls
block|}
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|cache
argument_list|)
expr_stmt|;
name|xmlWriter
operator|=
name|cache
expr_stmt|;
block|}
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|parts
init|=
literal|null
decl_stmt|;
name|boolean
name|output
init|=
literal|false
decl_stmt|;
if|if
condition|(
operator|!
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
if|if
condition|(
name|operation
operator|.
name|getOutput
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|parts
operator|=
name|operation
operator|.
name|getOutput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
expr_stmt|;
name|output
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|parts
operator|=
name|operation
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
expr_stmt|;
name|output
operator|=
literal|false
expr_stmt|;
block|}
name|MessageContentsList
name|objs
init|=
name|MessageContentsList
operator|.
name|getContentsList
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|objs
operator|==
literal|null
condition|)
block|{
name|addOperationNode
argument_list|(
name|nsStack
argument_list|,
name|message
argument_list|,
name|xmlWriter
argument_list|,
name|output
argument_list|,
name|operation
argument_list|)
expr_stmt|;
name|xmlWriter
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
return|return;
block|}
for|for
control|(
name|MessagePartInfo
name|part
range|:
name|parts
control|)
block|{
if|if
condition|(
name|objs
operator|.
name|hasValue
argument_list|(
name|part
argument_list|)
condition|)
block|{
name|Object
name|o
init|=
name|objs
operator|.
name|get
argument_list|(
name|part
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
comment|//WSI-BP R2211 - RPC/Lit parts are not allowed to be xsi:nil
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"BP_2211_RPCLIT_CANNOT_BE_NULL"
argument_list|,
name|LOG
argument_list|,
name|part
operator|.
name|getConcreteName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
comment|//WSI-BP R2737  -RPC/LIG part name space is empty
comment|// part.setConcreteName(new QName("", part.getConcreteName().getLocalPart()));
block|}
block|}
name|addOperationNode
argument_list|(
name|nsStack
argument_list|,
name|message
argument_list|,
name|xmlWriter
argument_list|,
name|output
argument_list|,
name|operation
argument_list|)
expr_stmt|;
name|writeParts
argument_list|(
name|message
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
argument_list|,
name|operation
argument_list|,
name|objs
argument_list|,
name|parts
argument_list|)
expr_stmt|;
comment|// Finishing the writing.
name|xmlWriter
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
if|if
condition|(
name|cache
operator|!=
literal|null
condition|)
block|{
try|try
block|{
for|for
control|(
name|XMLEvent
name|event
range|:
name|cache
operator|.
name|getEvents
argument_list|()
control|)
block|{
name|StaxUtils
operator|.
name|writeEvent
argument_list|(
name|event
argument_list|,
name|origXmlWriter
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|origXmlWriter
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|origXmlWriter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|String
name|addOperationNode
parameter_list|(
name|NSStack
name|nsStack
parameter_list|,
name|Message
name|message
parameter_list|,
name|XMLStreamWriter
name|xmlWriter
parameter_list|,
name|boolean
name|output
parameter_list|,
name|BindingOperationInfo
name|boi
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|String
name|ns
init|=
name|boi
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|SoapBody
name|body
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|output
condition|)
block|{
name|body
operator|=
name|boi
operator|.
name|getOutput
argument_list|()
operator|.
name|getExtensor
argument_list|(
name|SoapBody
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|body
operator|=
name|boi
operator|.
name|getInput
argument_list|()
operator|.
name|getExtensor
argument_list|(
name|SoapBody
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|nsUri
init|=
name|body
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
comment|//do it once, as it might internally use reflection...
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|nsUri
argument_list|)
condition|)
block|{
name|ns
operator|=
name|nsUri
expr_stmt|;
block|}
block|}
name|nsStack
operator|.
name|add
argument_list|(
name|ns
argument_list|)
expr_stmt|;
name|String
name|prefix
init|=
name|nsStack
operator|.
name|getPrefix
argument_list|(
name|ns
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|output
condition|?
name|boi
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"Response"
else|:
name|boi
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|StaxUtils
operator|.
name|writeStartElement
argument_list|(
name|xmlWriter
argument_list|,
name|prefix
argument_list|,
name|name
argument_list|,
name|ns
argument_list|)
expr_stmt|;
return|return
name|ns
return|;
block|}
specifier|protected
name|XMLStreamWriter
name|getXMLStreamWriter
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

