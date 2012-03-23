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
name|HashMap
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
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
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
name|Soap11
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
name|Soap12
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
name|helpers
operator|.
name|XMLUtils
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
name|XPathUtils
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
name|ClientFaultConverter
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
name|FragmentStreamReader
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
name|Soap12FaultInInterceptor
extends|extends
name|AbstractSoapInterceptor
block|{
specifier|public
name|Soap12FaultInInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|UNMARSHAL
argument_list|)
expr_stmt|;
name|addBefore
argument_list|(
name|ClientFaultConverter
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
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
if|if
condition|(
name|message
operator|.
name|getVersion
argument_list|()
operator|==
name|Soap11
operator|.
name|getInstance
argument_list|()
condition|)
block|{
operator|new
name|Soap11FaultInInterceptor
argument_list|()
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
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
name|message
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
name|unmarshalFault
argument_list|(
name|message
argument_list|,
name|reader
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|SoapFault
name|unmarshalFault
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|XMLStreamReader
name|reader
parameter_list|)
block|{
name|String
name|exMessage
init|=
literal|null
decl_stmt|;
name|QName
name|faultCode
init|=
literal|null
decl_stmt|;
name|QName
name|subCode
init|=
literal|null
decl_stmt|;
name|String
name|role
init|=
literal|null
decl_stmt|;
name|String
name|node
init|=
literal|null
decl_stmt|;
name|Element
name|detail
init|=
literal|null
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ns
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ns
operator|.
name|put
argument_list|(
literal|"s"
argument_list|,
name|Soap12
operator|.
name|SOAP_NAMESPACE
argument_list|)
expr_stmt|;
name|XPathUtils
name|xu
init|=
operator|new
name|XPathUtils
argument_list|(
name|ns
argument_list|)
decl_stmt|;
try|try
block|{
name|Node
name|mainNode
init|=
name|message
operator|.
name|getContent
argument_list|(
name|Node
operator|.
name|class
argument_list|)
decl_stmt|;
name|Node
name|fault
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|mainNode
operator|!=
literal|null
condition|)
block|{
name|Node
name|bodyNode
init|=
operator|(
name|Node
operator|)
name|xu
operator|.
name|getValue
argument_list|(
literal|"//s:Body"
argument_list|,
name|mainNode
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|readDocElements
argument_list|(
name|bodyNode
operator|.
name|getOwnerDocument
argument_list|()
argument_list|,
name|bodyNode
argument_list|,
operator|new
name|FragmentStreamReader
argument_list|(
name|reader
argument_list|)
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|fault
operator|=
operator|(
name|Element
operator|)
name|xu
operator|.
name|getValue
argument_list|(
literal|"//s:Fault"
argument_list|,
name|bodyNode
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fault
operator|=
name|StaxUtils
operator|.
name|read
argument_list|(
operator|new
name|FragmentStreamReader
argument_list|(
name|reader
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Element
name|el
init|=
operator|(
name|Element
operator|)
name|xu
operator|.
name|getValue
argument_list|(
literal|"//s:Fault/s:Code/s:Value"
argument_list|,
name|fault
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|el
operator|!=
literal|null
condition|)
block|{
name|faultCode
operator|=
name|XMLUtils
operator|.
name|getQName
argument_list|(
name|el
operator|.
name|getTextContent
argument_list|()
argument_list|,
name|el
argument_list|)
expr_stmt|;
block|}
name|el
operator|=
operator|(
name|Element
operator|)
name|xu
operator|.
name|getValue
argument_list|(
literal|"//s:Fault/s:Code/s:Subcode/s:Value"
argument_list|,
name|fault
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
expr_stmt|;
if|if
condition|(
name|el
operator|!=
literal|null
condition|)
block|{
name|subCode
operator|=
name|XMLUtils
operator|.
name|getQName
argument_list|(
name|el
operator|.
name|getTextContent
argument_list|()
argument_list|,
name|el
argument_list|)
expr_stmt|;
block|}
name|exMessage
operator|=
operator|(
name|String
operator|)
name|xu
operator|.
name|getValue
argument_list|(
literal|"//s:Fault/s:Reason/s:Text/text()"
argument_list|,
name|fault
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
expr_stmt|;
name|Node
name|detailNode
init|=
operator|(
name|Node
operator|)
name|xu
operator|.
name|getValue
argument_list|(
literal|"//s:Fault/s:Detail"
argument_list|,
name|fault
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|detailNode
operator|!=
literal|null
condition|)
block|{
name|detail
operator|=
operator|(
name|Element
operator|)
name|detailNode
expr_stmt|;
block|}
name|role
operator|=
operator|(
name|String
operator|)
name|xu
operator|.
name|getValue
argument_list|(
literal|"//s:Fault/s:Role/text()"
argument_list|,
name|fault
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
expr_stmt|;
name|node
operator|=
operator|(
name|String
operator|)
name|xu
operator|.
name|getValue
argument_list|(
literal|"//s:Fault/s:Node/text()"
argument_list|,
name|fault
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
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
name|SoapFault
argument_list|(
literal|"Could not parse message."
argument_list|,
name|message
operator|.
name|getVersion
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|)
throw|;
block|}
name|SoapFault
name|fault
init|=
operator|new
name|SoapFault
argument_list|(
name|exMessage
argument_list|,
name|faultCode
argument_list|)
decl_stmt|;
name|fault
operator|.
name|setSubCode
argument_list|(
name|subCode
argument_list|)
expr_stmt|;
name|fault
operator|.
name|setDetail
argument_list|(
name|detail
argument_list|)
expr_stmt|;
name|fault
operator|.
name|setRole
argument_list|(
name|role
argument_list|)
expr_stmt|;
name|fault
operator|.
name|setNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
return|return
name|fault
return|;
block|}
block|}
end_class

end_unit

