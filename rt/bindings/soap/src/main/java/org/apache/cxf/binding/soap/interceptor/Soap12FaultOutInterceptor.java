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
name|binding
operator|.
name|soap
operator|.
name|SoapFault
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
name|SoapMessage
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
name|StaxUtils
import|;
end_import

begin_class
specifier|public
class|class
name|Soap12FaultOutInterceptor
extends|extends
name|AbstractSoapInterceptor
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
name|Soap12FaultOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|Soap12FaultOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PREPARE_SEND
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|message
operator|.
name|put
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
name|RESPONSE_CODE
argument_list|,
operator|new
name|Integer
argument_list|(
literal|500
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|Soap12FaultOutInterceptorInternal
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
specifier|static
class|class
name|Soap12FaultOutInterceptorInternal
extends|extends
name|AbstractSoapInterceptor
block|{
specifier|static
specifier|final
name|Soap12FaultOutInterceptorInternal
name|INSTANCE
init|=
operator|new
name|Soap12FaultOutInterceptorInternal
argument_list|()
decl_stmt|;
specifier|public
name|Soap12FaultOutInterceptorInternal
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
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|LOG
operator|.
name|info
argument_list|(
name|getClass
argument_list|()
operator|+
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|SoapMessage
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
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
name|RESPONSE_CODE
argument_list|,
operator|new
name|Integer
argument_list|(
literal|500
argument_list|)
argument_list|)
expr_stmt|;
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
name|Fault
name|f
init|=
operator|(
name|Fault
operator|)
name|message
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
name|SoapFault
name|fault
init|=
name|SoapFault
operator|.
name|createFault
argument_list|(
name|f
argument_list|,
name|message
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
init|=
name|fault
operator|.
name|getNamespaces
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|e
range|:
name|namespaces
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|writer
operator|.
name|writeNamespace
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|ns
init|=
name|message
operator|.
name|getVersion
argument_list|()
operator|.
name|getNamespace
argument_list|()
decl_stmt|;
name|String
name|defaultPrefix
init|=
name|StaxUtils
operator|.
name|getUniquePrefix
argument_list|(
name|writer
argument_list|,
name|ns
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|defaultPrefix
argument_list|,
literal|"Fault"
argument_list|,
name|ns
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|defaultPrefix
argument_list|,
literal|"Code"
argument_list|,
name|ns
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|defaultPrefix
argument_list|,
literal|"Value"
argument_list|,
name|ns
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeCharacters
argument_list|(
name|fault
operator|.
name|getCodeString
argument_list|(
name|getFaultCodePrefix
argument_list|(
name|writer
argument_list|,
name|fault
operator|.
name|getFaultCode
argument_list|()
argument_list|)
argument_list|,
name|defaultPrefix
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
if|if
condition|(
name|fault
operator|.
name|getSubCode
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
name|defaultPrefix
argument_list|,
literal|"Subcode"
argument_list|,
name|ns
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|defaultPrefix
argument_list|,
literal|"Value"
argument_list|,
name|ns
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeCharacters
argument_list|(
name|fault
operator|.
name|getSubCodeString
argument_list|(
name|getFaultCodePrefix
argument_list|(
name|writer
argument_list|,
name|fault
operator|.
name|getSubCode
argument_list|()
argument_list|)
argument_list|,
name|defaultPrefix
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|defaultPrefix
argument_list|,
literal|"Reason"
argument_list|,
name|ns
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|defaultPrefix
argument_list|,
literal|"Text"
argument_list|,
name|ns
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"xml"
argument_list|,
literal|"http://www.w3.org/XML/1998/namespace"
argument_list|,
literal|"lang "
argument_list|,
name|getLangCode
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|fault
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|writeCharacters
argument_list|(
name|fault
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|writer
operator|.
name|writeCharacters
argument_list|(
literal|"Fault occurred while processing."
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
if|if
condition|(
name|fault
operator|.
name|hasDetails
argument_list|()
condition|)
block|{
name|Element
name|detail
init|=
name|fault
operator|.
name|getDetail
argument_list|()
decl_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|defaultPrefix
argument_list|,
literal|"Detail"
argument_list|,
name|ns
argument_list|)
expr_stmt|;
name|Node
name|node
init|=
name|detail
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|StaxUtils
operator|.
name|writeNode
argument_list|(
name|node
argument_list|,
name|writer
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|node
operator|=
name|node
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
comment|// Details
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|fault
operator|.
name|getRole
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
name|defaultPrefix
argument_list|,
literal|"Role"
argument_list|,
name|ns
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeCharacters
argument_list|(
name|fault
operator|.
name|getRole
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
comment|// Fault
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|xe
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"XML_WRITE_EXC"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|xe
argument_list|)
throw|;
block|}
block|}
specifier|private
name|String
name|getLangCode
parameter_list|()
block|{
name|String
name|code
init|=
name|LOG
operator|.
name|getResourceBundle
argument_list|()
operator|.
name|getLocale
argument_list|()
operator|.
name|getDisplayLanguage
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|code
argument_list|)
condition|)
block|{
return|return
literal|"en"
return|;
block|}
return|return
name|code
return|;
block|}
block|}
block|}
end_class

end_unit

