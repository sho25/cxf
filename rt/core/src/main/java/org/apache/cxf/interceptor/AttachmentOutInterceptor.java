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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|ResourceBundle
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
name|attachment
operator|.
name|AttachmentSerializer
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
name|i18n
operator|.
name|BundleUtils
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

begin_class
specifier|public
class|class
name|AttachmentOutInterceptor
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
name|WRITE_ATTACHMENTS
init|=
literal|"write.attachments"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|AttachmentOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|AttachmentOutEndingInterceptor
name|ending
init|=
operator|new
name|AttachmentOutEndingInterceptor
argument_list|()
decl_stmt|;
specifier|public
name|AttachmentOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
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
comment|// Make it possible to step into this process in spite of Eclipse
comment|// by declaring the Object.
name|Object
name|prop
init|=
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
decl_stmt|;
name|boolean
name|mtomEnabled
init|=
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|prop
argument_list|)
decl_stmt|;
name|boolean
name|writeAtts
init|=
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|WRITE_ATTACHMENTS
argument_list|)
argument_list|)
operator|||
operator|(
name|message
operator|.
name|getAttachments
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|message
operator|.
name|getAttachments
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|)
decl_stmt|;
if|if
condition|(
operator|!
name|mtomEnabled
operator|&&
operator|!
name|writeAtts
condition|)
block|{
return|return;
block|}
name|AttachmentSerializer
name|serializer
init|=
operator|new
name|AttachmentSerializer
argument_list|(
name|message
argument_list|,
name|getMultipartType
argument_list|()
argument_list|,
name|getRootHeaders
argument_list|()
argument_list|)
decl_stmt|;
name|serializer
operator|.
name|setXop
argument_list|(
name|mtomEnabled
argument_list|)
expr_stmt|;
try|try
block|{
name|serializer
operator|.
name|writeProlog
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
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
literal|"WRITE_ATTACHMENTS"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|message
operator|.
name|setContent
argument_list|(
name|AttachmentSerializer
operator|.
name|class
argument_list|,
name|serializer
argument_list|)
expr_stmt|;
comment|// Add a final interceptor to write attachements
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|ending
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|getMultipartType
parameter_list|()
block|{
return|return
literal|"multipart/related"
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|getRootHeaders
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
specifier|public
class|class
name|AttachmentOutEndingInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
name|AttachmentOutEndingInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_STREAM_ENDING
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
name|AttachmentSerializer
name|ser
init|=
name|message
operator|.
name|getContent
argument_list|(
name|AttachmentSerializer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ser
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|ser
operator|.
name|writeAttachments
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
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
literal|"WRITE_ATTACHMENTS"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

