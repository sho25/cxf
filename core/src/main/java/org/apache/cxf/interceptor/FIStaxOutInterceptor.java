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
name|logging
operator|.
name|Level
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
name|com
operator|.
name|sun
operator|.
name|xml
operator|.
name|fastinfoset
operator|.
name|stax
operator|.
name|StAXDocumentSerializer
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
name|phase
operator|.
name|Phase
import|;
end_import

begin_comment
comment|/**  * Creates an XMLStreamReader from the InputStream on the Message.  */
end_comment

begin_class
specifier|public
class|class
name|FIStaxOutInterceptor
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
name|FI_ENABLED
init|=
literal|"org.apache.cxf.fastinfoset.enabled"
decl_stmt|;
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
name|FIStaxOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OUTPUT_STREAM_HOLDER
init|=
name|FIStaxOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".outputstream"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|StaxOutEndingInterceptor
name|ENDING
init|=
operator|new
name|StaxOutEndingInterceptor
argument_list|(
name|OUTPUT_STREAM_HOLDER
argument_list|)
decl_stmt|;
name|boolean
name|force
decl_stmt|;
specifier|private
name|Integer
name|serializerAttributeValueMapMemoryLimit
decl_stmt|;
specifier|private
name|Integer
name|serializerMinAttributeValueSize
decl_stmt|;
specifier|private
name|Integer
name|serializerMaxAttributeValueSize
decl_stmt|;
specifier|private
name|Integer
name|serializerCharacterContentChunkMapMemoryLimit
decl_stmt|;
specifier|private
name|Integer
name|serializerMinCharacterContentChunkSize
decl_stmt|;
specifier|private
name|Integer
name|serializerMaxCharacterContentChunkSize
decl_stmt|;
specifier|public
name|FIStaxOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|AttachmentOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|addBefore
argument_list|(
name|StaxOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|FIStaxOutInterceptor
parameter_list|(
name|boolean
name|f
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|force
operator|=
name|f
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleFault
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|super
operator|.
name|handleFault
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|OutputStream
name|os
init|=
operator|(
name|OutputStream
operator|)
name|message
operator|.
name|get
argument_list|(
name|OUTPUT_STREAM_HOLDER
argument_list|)
decl_stmt|;
if|if
condition|(
name|os
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
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
name|writer
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
if|if
condition|(
name|out
operator|==
literal|null
operator|||
name|writer
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
name|boolean
name|req
init|=
name|isRequestor
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|Object
name|o
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|FI_ENABLED
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|req
condition|)
block|{
if|if
condition|(
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|//check incoming accept header
name|String
name|s
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
operator|&&
name|s
operator|.
name|contains
argument_list|(
literal|"fastinfoset"
argument_list|)
condition|)
block|{
name|o
operator|=
name|Boolean
operator|.
name|TRUE
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|accepts
init|=
name|headers
operator|.
name|get
argument_list|(
literal|"Accept"
argument_list|)
decl_stmt|;
if|if
condition|(
name|accepts
operator|==
literal|null
condition|)
block|{
name|accepts
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Accept"
argument_list|,
name|accepts
argument_list|)
expr_stmt|;
block|}
name|String
name|a
init|=
literal|"application/fastinfoset"
decl_stmt|;
if|if
condition|(
operator|!
name|accepts
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|a
operator|+=
literal|", "
operator|+
name|accepts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|accepts
operator|.
name|set
argument_list|(
literal|0
argument_list|,
name|a
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|accepts
operator|.
name|add
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|force
operator|||
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|o
argument_list|)
condition|)
block|{
name|StAXDocumentSerializer
name|serializer
init|=
name|getOutput
argument_list|(
name|message
argument_list|,
name|out
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|serializer
argument_list|)
expr_stmt|;
name|message
operator|.
name|removeContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|OUTPUT_STREAM_HOLDER
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|AbstractOutDatabindingInterceptor
operator|.
name|DISABLE_OUTPUTSTREAM_OPTIMIZATION
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|String
name|s
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
operator|&&
name|s
operator|.
name|contains
argument_list|(
literal|"application/soap+xml"
argument_list|)
condition|)
block|{
name|s
operator|=
name|s
operator|.
name|replace
argument_list|(
literal|"application/soap+xml"
argument_list|,
literal|"application/soap+fastinfoset"
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/fastinfoset"
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|serializer
operator|.
name|writeStartDocument
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
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|ENDING
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|StAXDocumentSerializer
name|getOutput
parameter_list|(
name|Message
name|m
parameter_list|,
name|OutputStream
name|out
parameter_list|)
block|{
comment|/*         StAXDocumentSerializer serializer = (StAXDocumentSerializer)m.getExchange().getEndpoint()             .remove(StAXDocumentSerializer.class.getName());         if (serializer != null) {             serializer.setOutputStream(out);         } else {             serializer = new StAXDocumentSerializer(out);         }         return serializer;         */
specifier|final
name|StAXDocumentSerializer
name|stAXDocumentSerializer
init|=
operator|new
name|StAXDocumentSerializer
argument_list|(
name|out
argument_list|)
decl_stmt|;
if|if
condition|(
name|serializerAttributeValueMapMemoryLimit
operator|!=
literal|null
operator|&&
name|serializerAttributeValueMapMemoryLimit
operator|.
name|intValue
argument_list|()
operator|>
literal|0
condition|)
block|{
name|stAXDocumentSerializer
operator|.
name|setAttributeValueMapMemoryLimit
argument_list|(
name|serializerAttributeValueMapMemoryLimit
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|serializerMinAttributeValueSize
operator|!=
literal|null
operator|&&
name|serializerMinAttributeValueSize
operator|.
name|intValue
argument_list|()
operator|>
literal|0
condition|)
block|{
name|stAXDocumentSerializer
operator|.
name|setMinAttributeValueSize
argument_list|(
name|serializerMinAttributeValueSize
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|serializerMaxAttributeValueSize
operator|!=
literal|null
operator|&&
name|serializerMaxAttributeValueSize
operator|.
name|intValue
argument_list|()
operator|>
literal|0
condition|)
block|{
name|stAXDocumentSerializer
operator|.
name|setMaxAttributeValueSize
argument_list|(
name|serializerMaxAttributeValueSize
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|serializerCharacterContentChunkMapMemoryLimit
operator|!=
literal|null
operator|&&
name|serializerCharacterContentChunkMapMemoryLimit
operator|.
name|intValue
argument_list|()
operator|>
literal|0
condition|)
block|{
name|stAXDocumentSerializer
operator|.
name|setCharacterContentChunkMapMemoryLimit
argument_list|(
name|serializerCharacterContentChunkMapMemoryLimit
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|serializerMinCharacterContentChunkSize
operator|!=
literal|null
operator|&&
name|serializerMinCharacterContentChunkSize
operator|.
name|intValue
argument_list|()
operator|>
literal|0
condition|)
block|{
name|stAXDocumentSerializer
operator|.
name|setMinCharacterContentChunkSize
argument_list|(
name|serializerMinCharacterContentChunkSize
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|serializerMaxCharacterContentChunkSize
operator|!=
literal|null
operator|&&
name|serializerMaxCharacterContentChunkSize
operator|.
name|intValue
argument_list|()
operator|>
literal|0
condition|)
block|{
name|stAXDocumentSerializer
operator|.
name|setMaxCharacterContentChunkSize
argument_list|(
name|serializerMaxCharacterContentChunkSize
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|stAXDocumentSerializer
return|;
block|}
specifier|public
name|Integer
name|getSerializerMinAttributeValueSize
parameter_list|()
block|{
return|return
name|serializerMinAttributeValueSize
return|;
block|}
comment|/**      * Sets the property<code>minAttributeValueSize</code> on FastInfoset StAX Serializers created and used      * by this interceptor. The property controls the<b>minimum</b> size of attribute values to be indexed.      *      * @param serializerMinAttributeValueSize      *         The<b>minimum</b> size for attribute values to be indexed,      *         measured as a number of characters. The default is typically 0.      */
specifier|public
name|void
name|setSerializerMinAttributeValueSize
parameter_list|(
name|Integer
name|serializerMinAttributeValueSize
parameter_list|)
block|{
name|logSetter
argument_list|(
literal|"serializerMinAttributeValueSize"
argument_list|,
name|serializerMinAttributeValueSize
argument_list|)
expr_stmt|;
name|this
operator|.
name|serializerMinAttributeValueSize
operator|=
name|serializerMinAttributeValueSize
expr_stmt|;
block|}
specifier|public
name|Integer
name|getSerializerMaxAttributeValueSize
parameter_list|()
block|{
return|return
name|serializerMaxAttributeValueSize
return|;
block|}
comment|/**      * Sets the property<code>maxAttributeValueSize</code> on FastInfoset StAX Serializers created and used      * by this interceptor. The property controls the<b>maximum</b> size of attribute values to be indexed.      * Tests have shown that setting this property to lower values reduces CPU burden of processing, at the expense      * of larger sizes of resultant encoded Fast Infoset data.      *      * @param serializerMaxAttributeValueSize      *         The<b>maximum</b> size for attribute values to be indexed,      *         measured as a number of characters. The default is typically 32.      */
specifier|public
name|void
name|setSerializerMaxAttributeValueSize
parameter_list|(
name|Integer
name|serializerMaxAttributeValueSize
parameter_list|)
block|{
name|logSetter
argument_list|(
literal|"serializerMaxAttributeValueSize"
argument_list|,
name|serializerMaxAttributeValueSize
argument_list|)
expr_stmt|;
name|this
operator|.
name|serializerMaxAttributeValueSize
operator|=
name|serializerMaxAttributeValueSize
expr_stmt|;
block|}
specifier|public
name|Integer
name|getSerializerCharacterContentChunkMapMemoryLimit
parameter_list|()
block|{
return|return
name|serializerCharacterContentChunkMapMemoryLimit
return|;
block|}
comment|/**      * Sets the property<code>characterContentChunkMapMemoryLimit</code> on FastInfoset StAX Serializers created and      * used by this interceptor. The property controls character content chunk map size and can be used to control the      * memory and (indirectly) CPU footprint of processing.      *      * @param serializerCharacterContentChunkMapMemoryLimit      *         The value for the limit, measured as a number of Unicode characters.      */
specifier|public
name|void
name|setSerializerCharacterContentChunkMapMemoryLimit
parameter_list|(
name|Integer
name|serializerCharacterContentChunkMapMemoryLimit
parameter_list|)
block|{
name|logSetter
argument_list|(
literal|"serializerCharacterContentChunkMapMemoryLimit"
argument_list|,
name|serializerCharacterContentChunkMapMemoryLimit
argument_list|)
expr_stmt|;
name|this
operator|.
name|serializerCharacterContentChunkMapMemoryLimit
operator|=
name|serializerCharacterContentChunkMapMemoryLimit
expr_stmt|;
block|}
specifier|public
name|Integer
name|getSerializerMinCharacterContentChunkSize
parameter_list|()
block|{
return|return
name|serializerMinCharacterContentChunkSize
return|;
block|}
comment|/**      * Sets the property<code>minCharacterContentChunkSize</code> on FastInfoset StAX Serializers created and used      * by this interceptor. The property controls the<b>minimum</b> size of character content chunks to be indexed.      *      * @param serializerMinCharacterContentChunkSize      *         The<b>minimum</b> size for character content chunks to be indexed,      *         measured as a number of characters. The default is typically 0.      */
specifier|public
name|void
name|setSerializerMinCharacterContentChunkSize
parameter_list|(
name|Integer
name|serializerMinCharacterContentChunkSize
parameter_list|)
block|{
name|logSetter
argument_list|(
literal|"serializerMinCharacterContentChunkSize"
argument_list|,
name|serializerMinCharacterContentChunkSize
argument_list|)
expr_stmt|;
name|this
operator|.
name|serializerMinCharacterContentChunkSize
operator|=
name|serializerMinCharacterContentChunkSize
expr_stmt|;
block|}
specifier|public
name|Integer
name|getSerializerMaxCharacterContentChunkSize
parameter_list|()
block|{
return|return
name|serializerMaxCharacterContentChunkSize
return|;
block|}
comment|/**      * Sets the property<code>maxCharacterContentChunkSize</code> on FastInfoset StAX Serializers created and used      * by this interceptor. The property controls the<b>maximum</b> size of character content chunks to be indexed.      * Tests have shown that setting this property to lower values reduces CPU burden of processing, at the expense      * of larger sizes of resultant encoded Fast Infoset data.      *      * @param serializerMaxCharacterContentChunkSize      *         The<b>maximum</b> size for character content chunks to be indexed,      *         measured as a number of characters. The default is typically 32.      */
specifier|public
name|void
name|setSerializerMaxCharacterContentChunkSize
parameter_list|(
name|Integer
name|serializerMaxCharacterContentChunkSize
parameter_list|)
block|{
name|logSetter
argument_list|(
literal|"serializerMaxCharacterContentChunkSize"
argument_list|,
name|serializerMaxCharacterContentChunkSize
argument_list|)
expr_stmt|;
name|this
operator|.
name|serializerMaxCharacterContentChunkSize
operator|=
name|serializerMaxCharacterContentChunkSize
expr_stmt|;
block|}
specifier|public
name|Integer
name|getSerializerAttributeValueMapMemoryLimit
parameter_list|()
block|{
return|return
name|serializerAttributeValueMapMemoryLimit
return|;
block|}
comment|/**      * Sets the property<code>attributeValueMapMemoryLimit</code> on FastInfoset StAX Serializers created and used      * by this interceptor. The property controls attribute value map size and can be used to control      * the memory and (indirectly) CPU footprint of processing.      *      * @param serializerAttributeValueMapMemoryLimit      *         The value for the limit, measured as a number of Unicode characters.      */
specifier|public
name|void
name|setSerializerAttributeValueMapMemoryLimit
parameter_list|(
name|Integer
name|serializerAttributeValueMapMemoryLimit
parameter_list|)
block|{
name|logSetter
argument_list|(
literal|"serializerAttributeValueMapMemoryLimit"
argument_list|,
name|serializerAttributeValueMapMemoryLimit
argument_list|)
expr_stmt|;
name|this
operator|.
name|serializerAttributeValueMapMemoryLimit
operator|=
name|serializerAttributeValueMapMemoryLimit
expr_stmt|;
block|}
specifier|private
name|void
name|logSetter
parameter_list|(
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|CONFIG
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|config
argument_list|(
literal|"Setting "
operator|+
name|propertyName
operator|+
literal|" to "
operator|+
name|propertyValue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

