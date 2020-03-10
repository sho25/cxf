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
operator|.
name|transform
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|StaxOutEndingInterceptor
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
name|StaxOutInterceptor
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
name|transform
operator|.
name|TransformUtils
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|AbstractOutDatabindingInterceptor
operator|.
name|DISABLE_OUTPUTSTREAM_OPTIMIZATION
import|;
end_import

begin_comment
comment|/**  * Creates an XMLStreamReader from the InputStream on the Message.  */
end_comment

begin_class
specifier|public
class|class
name|TransformOutInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|String
name|OUTPUT_STREAM_HOLDER
init|=
name|TransformOutInterceptor
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
name|String
name|TRANSFORM_SKIP
init|=
literal|"transform.skip"
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
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outElementsMap
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outAppendMap
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|outDropElements
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outAttributesMap
decl_stmt|;
specifier|private
name|boolean
name|attributesToElements
decl_stmt|;
specifier|private
name|boolean
name|skipOnFault
decl_stmt|;
specifier|private
name|String
name|contextPropertyName
decl_stmt|;
specifier|private
name|String
name|defaultNamespace
decl_stmt|;
specifier|public
name|TransformOutInterceptor
parameter_list|()
block|{
name|this
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
argument_list|)
expr_stmt|;
block|}
specifier|public
name|TransformOutInterceptor
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
name|addAfter
argument_list|(
literal|"org.apache.cxf.interceptor.LoggingOutInterceptor"
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
literal|"org.apache.cxf.ext.logging.LoggingOutInterceptor"
argument_list|)
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
if|if
condition|(
operator|!
name|isHttpVerbSupported
argument_list|(
name|message
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|contextPropertyName
operator|!=
literal|null
operator|&&
operator|!
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
argument_list|,
name|contextPropertyName
argument_list|,
literal|false
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|skipOnFault
operator|&&
literal|null
operator|!=
name|message
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|||
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|TRANSFORM_SKIP
argument_list|,
literal|false
argument_list|)
condition|)
block|{
return|return;
block|}
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
name|transformWriter
init|=
name|createTransformWriterIfNeeded
argument_list|(
name|writer
argument_list|,
name|out
argument_list|)
decl_stmt|;
if|if
condition|(
name|transformWriter
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
name|transformWriter
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|DISABLE_OUTPUTSTREAM_OPTIMIZATION
argument_list|)
operator|==
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|DISABLE_OUTPUTSTREAM_OPTIMIZATION
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
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
block|}
specifier|protected
name|XMLStreamWriter
name|createTransformWriterIfNeeded
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|OutputStream
name|os
parameter_list|)
block|{
return|return
name|TransformUtils
operator|.
name|createTransformWriterIfNeeded
argument_list|(
name|writer
argument_list|,
name|os
argument_list|,
name|outElementsMap
argument_list|,
name|outDropElements
argument_list|,
name|outAppendMap
argument_list|,
name|outAttributesMap
argument_list|,
name|attributesToElements
argument_list|,
name|defaultNamespace
argument_list|)
return|;
block|}
specifier|public
name|void
name|setOutTransformElements
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outElements
parameter_list|)
block|{
name|this
operator|.
name|outElementsMap
operator|=
name|outElements
expr_stmt|;
block|}
specifier|public
name|void
name|setOutAppendElements
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
block|{
name|this
operator|.
name|outAppendMap
operator|=
name|map
expr_stmt|;
block|}
specifier|public
name|void
name|setOutDropElements
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|dropElementsSet
parameter_list|)
block|{
name|this
operator|.
name|outDropElements
operator|=
name|dropElementsSet
expr_stmt|;
block|}
specifier|public
name|void
name|setOutTransformAttributes
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outAttributes
parameter_list|)
block|{
name|this
operator|.
name|outAttributesMap
operator|=
name|outAttributes
expr_stmt|;
block|}
specifier|public
name|void
name|setAttributesToElements
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|this
operator|.
name|attributesToElements
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|void
name|setSkipOnFault
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|this
operator|.
name|skipOnFault
operator|=
name|value
expr_stmt|;
block|}
specifier|protected
name|boolean
name|isHttpVerbSupported
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|isRequestor
argument_list|(
name|message
argument_list|)
operator|&&
name|isGET
argument_list|(
name|message
argument_list|)
condition|?
literal|false
else|:
literal|true
return|;
block|}
specifier|public
name|void
name|setContextPropertyName
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
name|contextPropertyName
operator|=
name|propertyName
expr_stmt|;
block|}
specifier|public
name|void
name|setDefaultNamespace
parameter_list|(
name|String
name|defaultNamespace
parameter_list|)
block|{
name|this
operator|.
name|defaultNamespace
operator|=
name|defaultNamespace
expr_stmt|;
block|}
block|}
end_class

end_unit

