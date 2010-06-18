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
name|util
operator|.
name|ResourceBundle
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
name|MessageInfo
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
name|StaxUtils
import|;
end_import

begin_class
specifier|public
class|class
name|WrappedOutInterceptor
extends|extends
name|AbstractOutDatabindingInterceptor
block|{
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
name|WrappedOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|WrappedOutEndingInterceptor
name|ending
decl_stmt|;
specifier|public
name|WrappedOutInterceptor
parameter_list|()
block|{
name|this
argument_list|(
name|Phase
operator|.
name|MARSHAL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WrappedOutInterceptor
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
name|ending
operator|=
operator|new
name|WrappedOutEndingInterceptor
argument_list|(
name|phase
operator|+
literal|"-ending"
argument_list|)
expr_stmt|;
name|addBefore
argument_list|(
name|BareOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
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
name|BindingOperationInfo
name|bop
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|bop
operator|!=
literal|null
operator|&&
name|bop
operator|.
name|isUnwrapped
argument_list|()
condition|)
block|{
name|XMLStreamWriter
name|xmlWriter
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
name|MessageInfo
name|messageInfo
decl_stmt|;
if|if
condition|(
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|messageInfo
operator|=
name|bop
operator|.
name|getWrappedOperation
argument_list|()
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getInput
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|messageInfo
operator|=
name|bop
operator|.
name|getWrappedOperation
argument_list|()
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getOutput
argument_list|()
expr_stmt|;
block|}
name|MessagePartInfo
name|part
init|=
name|messageInfo
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|QName
name|name
init|=
name|part
operator|.
name|getConcreteName
argument_list|()
decl_stmt|;
try|try
block|{
name|String
name|pfx
init|=
literal|null
decl_stmt|;
name|Service
name|service
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Service
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|service
operator|.
name|getDataBinding
argument_list|()
operator|.
name|getDeclaredNamespaceMappings
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|pfx
operator|=
name|service
operator|.
name|getDataBinding
argument_list|()
operator|.
name|getDeclaredNamespaceMappings
argument_list|()
operator|.
name|get
argument_list|(
name|name
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|pfx
operator|==
literal|null
condition|)
block|{
name|pfx
operator|=
name|StaxUtils
operator|.
name|getUniquePrefix
argument_list|(
name|xmlWriter
argument_list|,
name|name
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|xmlWriter
operator|.
name|setPrefix
argument_list|(
name|pfx
argument_list|,
name|name
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|xmlWriter
operator|.
name|writeStartElement
argument_list|(
name|pfx
argument_list|,
name|name
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|name
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|pfx
argument_list|)
condition|)
block|{
name|xmlWriter
operator|.
name|writeDefaultNamespace
argument_list|(
name|name
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|xmlWriter
operator|.
name|writeNamespace
argument_list|(
name|pfx
argument_list|,
name|name
operator|.
name|getNamespaceURI
argument_list|()
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
literal|"STAX_WRITE_EXC"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// Add a final interceptor to write end element
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
block|}
specifier|public
class|class
name|WrappedOutEndingInterceptor
extends|extends
name|AbstractOutDatabindingInterceptor
block|{
specifier|public
name|WrappedOutEndingInterceptor
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
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
try|try
block|{
name|XMLStreamWriter
name|xtw
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
name|xtw
operator|!=
literal|null
condition|)
block|{
name|xtw
operator|.
name|writeEndElement
argument_list|()
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
literal|"STAX_WRITE_EXC"
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
end_class

end_unit

