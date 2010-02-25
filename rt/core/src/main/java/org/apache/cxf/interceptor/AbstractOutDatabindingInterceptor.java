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
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

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
name|Collection
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
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Schema
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
name|databinding
operator|.
name|DataWriter
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
name|Attachment
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
name|Exchange
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
name|message
operator|.
name|MessageUtils
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
name|AbstractPhaseInterceptor
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
name|Service
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
name|BindingInfo
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|wsdl
operator|.
name|EndpointReferenceUtils
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractOutDatabindingInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DISABLE_OUTPUTSTREAM_OPTIMIZATION
init|=
literal|"disable.outputstream.optimization"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OUT_BUFFERING
init|=
literal|"org.apache.cxf.output.buffering"
decl_stmt|;
specifier|public
name|AbstractOutDatabindingInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AbstractOutDatabindingInterceptor
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|,
name|phase
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|boolean
name|isRequestor
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|message
operator|.
name|containsKey
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|void
name|writeParts
parameter_list|(
name|Message
name|message
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|BindingOperationInfo
name|operation
parameter_list|,
name|MessageContentsList
name|objs
parameter_list|,
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|parts
parameter_list|)
block|{
name|OutputStream
name|out
init|=
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|XMLStreamWriter
name|origXmlWriter
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
name|exchange
operator|.
name|getService
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|xmlWriter
init|=
name|origXmlWriter
decl_stmt|;
name|CachingXmlEventWriter
name|cache
init|=
literal|null
decl_stmt|;
name|Object
name|en
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|OUT_BUFFERING
argument_list|)
decl_stmt|;
name|boolean
name|allowBuffer
init|=
literal|true
decl_stmt|;
name|boolean
name|buffer
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|en
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|=
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|en
argument_list|)
operator|||
literal|"true"
operator|.
name|equals
argument_list|(
name|en
argument_list|)
expr_stmt|;
name|allowBuffer
operator|=
operator|!
operator|(
name|Boolean
operator|.
name|FALSE
operator|.
name|equals
argument_list|(
name|en
argument_list|)
operator|||
literal|"false"
operator|.
name|equals
argument_list|(
name|en
argument_list|)
operator|)
expr_stmt|;
block|}
comment|// need to cache the events in case validation fails or buffering is enabled
if|if
condition|(
name|buffer
operator|||
operator|(
name|allowBuffer
operator|&&
name|shouldValidate
argument_list|(
name|message
argument_list|)
operator|&&
operator|!
name|isRequestor
argument_list|(
name|message
argument_list|)
operator|)
condition|)
block|{
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
name|origXmlWriter
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
name|xmlWriter
operator|=
name|cache
expr_stmt|;
name|out
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|out
operator|!=
literal|null
operator|&&
name|writeToOutputStream
argument_list|(
name|message
argument_list|,
name|operation
operator|.
name|getBinding
argument_list|()
argument_list|,
name|service
argument_list|)
operator|&&
operator|!
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|DISABLE_OUTPUTSTREAM_OPTIMIZATION
argument_list|)
argument_list|)
condition|)
block|{
if|if
condition|(
name|xmlWriter
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|xmlWriter
operator|.
name|writeCharacters
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|xmlWriter
operator|.
name|flush
argument_list|()
expr_stmt|;
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
name|DataWriter
argument_list|<
name|OutputStream
argument_list|>
name|osWriter
init|=
name|getDataWriter
argument_list|(
name|message
argument_list|,
name|service
argument_list|,
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|osWriter
operator|.
name|write
argument_list|(
name|o
argument_list|,
name|part
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|DataWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
name|dataWriter
init|=
name|getDataWriter
argument_list|(
name|message
argument_list|,
name|service
argument_list|,
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|dataWriter
operator|.
name|write
argument_list|(
name|o
argument_list|,
name|part
argument_list|,
name|xmlWriter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
specifier|protected
name|boolean
name|shouldValidate
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|Object
name|en
init|=
name|m
operator|.
name|getContextualProperty
argument_list|(
name|Message
operator|.
name|SCHEMA_VALIDATION_ENABLED
argument_list|)
decl_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|en
argument_list|)
operator|||
literal|"true"
operator|.
name|equals
argument_list|(
name|en
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|writeToOutputStream
parameter_list|(
name|Message
name|m
parameter_list|,
name|BindingInfo
name|info
parameter_list|,
name|Service
name|s
parameter_list|)
block|{
comment|/**          * Yes, all this code is EXTREMELY ugly. But it gives about a 60-70% performance          * boost with the JAXB RI, so its worth it.           */
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|enc
init|=
operator|(
name|String
operator|)
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|)
decl_stmt|;
return|return
name|info
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"org.apache.cxf.binding.soap.model.SoapBindingInfo"
argument_list|)
operator|&&
name|s
operator|.
name|getDataBinding
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"org.apache.cxf.jaxb.JAXBDataBinding"
argument_list|)
operator|&&
operator|!
name|MessageUtils
operator|.
name|isDOMPresent
argument_list|(
name|m
argument_list|)
operator|&&
operator|(
name|enc
operator|==
literal|null
operator|||
literal|"UTF-8"
operator|.
name|equals
argument_list|(
name|enc
argument_list|)
operator|)
return|;
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|DataWriter
argument_list|<
name|T
argument_list|>
name|getDataWriter
parameter_list|(
name|Message
name|message
parameter_list|,
name|Service
name|service
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|output
parameter_list|)
block|{
name|DataWriter
argument_list|<
name|T
argument_list|>
name|writer
init|=
name|service
operator|.
name|getDataBinding
argument_list|()
operator|.
name|createWriter
argument_list|(
name|output
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|atts
init|=
name|message
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
if|if
condition|(
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|MTOM_ENABLED
argument_list|)
argument_list|)
operator|&&
name|atts
operator|==
literal|null
condition|)
block|{
name|atts
operator|=
operator|new
name|ArrayList
argument_list|<
name|Attachment
argument_list|>
argument_list|()
expr_stmt|;
name|message
operator|.
name|setAttachments
argument_list|(
name|atts
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|setAttachments
argument_list|(
name|atts
argument_list|)
expr_stmt|;
name|writer
operator|.
name|setProperty
argument_list|(
name|DataWriter
operator|.
name|ENDPOINT
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|.
name|setProperty
argument_list|(
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|setSchemaOutMessage
argument_list|(
name|service
argument_list|,
name|message
argument_list|,
name|writer
argument_list|)
expr_stmt|;
return|return
name|writer
return|;
block|}
specifier|private
name|void
name|setSchemaOutMessage
parameter_list|(
name|Service
name|service
parameter_list|,
name|Message
name|message
parameter_list|,
name|DataWriter
argument_list|<
name|?
argument_list|>
name|writer
parameter_list|)
block|{
if|if
condition|(
name|shouldValidate
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|Schema
name|schema
init|=
name|EndpointReferenceUtils
operator|.
name|getSchema
argument_list|(
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|writer
operator|.
name|setSchema
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
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

