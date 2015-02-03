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
name|xml
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
name|bindings
operator|.
name|xformat
operator|.
name|XMLBindingMessageFormat
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
name|BindingMessageInfo
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
name|interceptors
operator|.
name|BareOutInterceptor
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
name|interceptors
operator|.
name|WrappedOutInterceptor
import|;
end_import

begin_class
specifier|public
class|class
name|XMLMessageOutInterceptor
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
name|XMLMessageOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|XMLMessageOutInterceptor
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
name|XMLMessageOutInterceptor
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
name|addAfter
argument_list|(
name|WrappedOutInterceptor
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
throws|throws
name|Fault
block|{
name|BindingOperationInfo
name|boi
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|MessageInfo
name|mi
decl_stmt|;
name|BindingMessageInfo
name|bmi
decl_stmt|;
if|if
condition|(
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|mi
operator|=
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getInput
argument_list|()
expr_stmt|;
name|bmi
operator|=
name|boi
operator|.
name|getInput
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|mi
operator|=
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getOutput
argument_list|()
expr_stmt|;
name|bmi
operator|=
name|boi
operator|.
name|getOutput
argument_list|()
expr_stmt|;
block|}
name|XMLBindingMessageFormat
name|xmf
init|=
name|bmi
operator|.
name|getExtensor
argument_list|(
name|XMLBindingMessageFormat
operator|.
name|class
argument_list|)
decl_stmt|;
name|QName
name|rootInModel
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|xmf
operator|!=
literal|null
condition|)
block|{
name|rootInModel
operator|=
name|xmf
operator|.
name|getRootNode
argument_list|()
expr_stmt|;
block|}
specifier|final
name|int
name|mpn
init|=
name|mi
operator|.
name|getMessagePartsNumber
argument_list|()
decl_stmt|;
if|if
condition|(
name|boi
operator|.
name|isUnwrapped
argument_list|()
operator|||
name|mpn
operator|==
literal|1
condition|)
block|{
comment|// wrapper out interceptor created the wrapper
comment|// or if bare-one-param
operator|new
name|BareOutInterceptor
argument_list|()
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|rootInModel
operator|==
literal|null
condition|)
block|{
name|rootInModel
operator|=
name|boi
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|mpn
operator|==
literal|0
operator|&&
operator|!
name|boi
operator|.
name|isUnwrapped
argument_list|()
condition|)
block|{
comment|// write empty operation qname
name|writeMessage
argument_list|(
name|message
argument_list|,
name|rootInModel
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// multi param, bare mode, needs write root node
name|writeMessage
argument_list|(
name|message
argument_list|,
name|rootInModel
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
comment|// in the end we do flush ;)
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
try|try
block|{
name|writer
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
argument_list|,
name|e
argument_list|)
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|writeMessage
parameter_list|(
name|Message
name|message
parameter_list|,
name|QName
name|name
parameter_list|,
name|boolean
name|executeBare
parameter_list|)
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
try|try
block|{
name|String
name|pfx
init|=
name|name
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
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
name|pfx
operator|=
literal|"ns1"
expr_stmt|;
block|}
name|StaxUtils
operator|.
name|writeStartElement
argument_list|(
name|xmlWriter
argument_list|,
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
name|executeBare
condition|)
block|{
operator|new
name|BareOutInterceptor
argument_list|()
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
name|xmlWriter
operator|.
name|writeEndElement
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
argument_list|,
name|e
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

