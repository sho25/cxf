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
name|InputStream
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
name|XMLStreamReader
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
name|StaxInInterceptor
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

begin_comment
comment|/**  * Creates an XMLStreamReader from the InputStream on the Message.  */
end_comment

begin_class
specifier|public
class|class
name|TransformInInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|inDropElements
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inElementsMap
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inAppendMap
decl_stmt|;
specifier|private
name|boolean
name|blockOriginalReader
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|contextPropertyName
decl_stmt|;
specifier|public
name|TransformInInterceptor
parameter_list|()
block|{
name|this
argument_list|(
name|Phase
operator|.
name|POST_STREAM
argument_list|)
expr_stmt|;
name|addBefore
argument_list|(
name|StaxInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|TransformInInterceptor
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
name|TransformInInterceptor
parameter_list|(
name|String
name|phase
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|after
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
if|if
condition|(
name|after
operator|!=
literal|null
condition|)
block|{
name|addAfter
argument_list|(
name|after
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|TransformInInterceptor
parameter_list|(
name|String
name|phase
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|before
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|after
parameter_list|)
block|{
name|this
argument_list|(
name|phase
argument_list|,
name|after
argument_list|)
expr_stmt|;
if|if
condition|(
name|before
operator|!=
literal|null
condition|)
block|{
name|addBefore
argument_list|(
name|before
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
argument_list|,
name|contextPropertyName
argument_list|,
literal|false
argument_list|)
condition|)
block|{
return|return;
block|}
name|XMLStreamReader
name|reader
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|transformReader
init|=
name|createTransformReaderIfNeeded
argument_list|(
name|reader
argument_list|,
name|is
argument_list|)
decl_stmt|;
if|if
condition|(
name|transformReader
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|transformReader
argument_list|)
expr_stmt|;
name|message
operator|.
name|removeContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|XMLStreamReader
name|createTransformReaderIfNeeded
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|,
name|InputStream
name|is
parameter_list|)
block|{
return|return
name|TransformUtils
operator|.
name|createTransformReaderIfNeeded
argument_list|(
name|reader
argument_list|,
name|is
argument_list|,
name|inDropElements
argument_list|,
name|inElementsMap
argument_list|,
name|inAppendMap
argument_list|,
name|blockOriginalReader
argument_list|)
return|;
block|}
specifier|public
name|void
name|setInAppendElements
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inElements
parameter_list|)
block|{
name|this
operator|.
name|inAppendMap
operator|=
name|inElements
expr_stmt|;
block|}
specifier|public
name|void
name|setInDropElements
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
name|inDropElements
operator|=
name|dropElementsSet
expr_stmt|;
block|}
specifier|public
name|void
name|setInTransformElements
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inElements
parameter_list|)
block|{
name|this
operator|.
name|inElementsMap
operator|=
name|inElements
expr_stmt|;
block|}
specifier|public
name|void
name|setBlockOriginalReader
parameter_list|(
name|boolean
name|blockOriginalReader
parameter_list|)
block|{
name|this
operator|.
name|blockOriginalReader
operator|=
name|blockOriginalReader
expr_stmt|;
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
block|}
end_class

end_unit

