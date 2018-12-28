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
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|soap
operator|.
name|SOAPFault
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPPart
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
name|Document
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
name|binding
operator|.
name|soap
operator|.
name|SoapVersion
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
name|interceptor
operator|.
name|Soap11FaultOutInterceptor
operator|.
name|Soap11FaultOutInterceptorInternal
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
name|interceptor
operator|.
name|Soap12FaultOutInterceptor
operator|.
name|Soap12FaultOutInterceptorInternal
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
name|saaj
operator|.
name|SAAJInInterceptor
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
name|saaj
operator|.
name|SAAJInInterceptor
operator|.
name|SAAJPreInInterceptor
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
name|ExchangeImpl
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
name|MessageImpl
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
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|SoapFaultSerializerTest
extends|extends
name|Assert
block|{
specifier|private
name|void
name|assertValid
parameter_list|(
name|String
name|xpathExpression
parameter_list|,
name|Document
name|doc
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|namespaces
operator|.
name|put
argument_list|(
literal|"s"
argument_list|,
literal|"http://schemas.xmlsoap.org/soap/envelope/"
argument_list|)
expr_stmt|;
name|namespaces
operator|.
name|put
argument_list|(
literal|"xsd"
argument_list|,
literal|"http://www.w3.org/2001/XMLSchema"
argument_list|)
expr_stmt|;
name|namespaces
operator|.
name|put
argument_list|(
literal|"wsdl"
argument_list|,
literal|"http://schemas.xmlsoap.org/wsdl/"
argument_list|)
expr_stmt|;
name|namespaces
operator|.
name|put
argument_list|(
literal|"wsdlsoap"
argument_list|,
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
argument_list|)
expr_stmt|;
name|namespaces
operator|.
name|put
argument_list|(
literal|"soap"
argument_list|,
literal|"http://schemas.xmlsoap.org/soap/"
argument_list|)
expr_stmt|;
name|namespaces
operator|.
name|put
argument_list|(
literal|"soap12env"
argument_list|,
literal|"http://www.w3.org/2003/05/soap-envelope"
argument_list|)
expr_stmt|;
name|namespaces
operator|.
name|put
argument_list|(
literal|"xml"
argument_list|,
literal|"http://www.w3.org/XML/1998/namespace"
argument_list|)
expr_stmt|;
name|XPathUtils
name|xpu
init|=
operator|new
name|XPathUtils
argument_list|(
name|namespaces
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|xpu
operator|.
name|isExist
argument_list|(
name|xpathExpression
argument_list|,
name|doc
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
condition|)
block|{
name|fail
argument_list|(
literal|"Failed to select any nodes for expression:\n"
operator|+
name|xpathExpression
operator|+
literal|" from document:\n"
operator|+
name|StaxUtils
operator|.
name|toString
argument_list|(
name|doc
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSoap11Out
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|faultString
init|=
literal|"Hadrian caused this Fault!"
decl_stmt|;
name|SoapFault
name|fault
init|=
operator|new
name|SoapFault
argument_list|(
name|faultString
argument_list|,
name|Soap11
operator|.
name|getInstance
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|)
decl_stmt|;
name|SoapMessage
name|m
init|=
operator|new
name|SoapMessage
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
operator|new
name|ExchangeImpl
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
name|fault
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|writer
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|writer
operator|.
name|writeStartDocument
argument_list|()
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
literal|"Body"
argument_list|)
expr_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|Soap11FaultOutInterceptorInternal
operator|.
name|INSTANCE
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|writer
operator|.
name|writeEndDocument
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|Document
name|faultDoc
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertValid
argument_list|(
literal|"//s:Fault/faultcode[text()='ns1:Client']"
argument_list|,
name|faultDoc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//s:Fault/faultstring[text()='"
operator|+
name|faultString
operator|+
literal|"']"
argument_list|,
name|faultDoc
argument_list|)
expr_stmt|;
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|reader
argument_list|)
expr_stmt|;
name|reader
operator|.
name|nextTag
argument_list|()
expr_stmt|;
name|Soap11FaultInInterceptor
name|inInterceptor
init|=
operator|new
name|Soap11FaultInInterceptor
argument_list|()
decl_stmt|;
name|inInterceptor
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|SoapFault
name|fault2
init|=
operator|(
name|SoapFault
operator|)
name|m
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|fault2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|fault
operator|.
name|getMessage
argument_list|()
argument_list|,
name|fault2
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Soap11
operator|.
name|getInstance
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|,
name|fault2
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSoap12Out
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|faultString
init|=
literal|"Hadrian caused this Fault!"
decl_stmt|;
name|SoapFault
name|fault
init|=
operator|new
name|SoapFault
argument_list|(
name|faultString
argument_list|,
name|Soap12
operator|.
name|getInstance
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|)
decl_stmt|;
name|QName
name|qname
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/soap/fault"
argument_list|,
literal|"invalidsoap"
argument_list|,
literal|"cxffaultcode"
argument_list|)
decl_stmt|;
name|fault
operator|.
name|setSubCode
argument_list|(
name|qname
argument_list|)
expr_stmt|;
name|SoapMessage
name|m
init|=
operator|new
name|SoapMessage
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
name|m
operator|.
name|setVersion
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
name|fault
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|writer
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|writer
operator|.
name|writeStartDocument
argument_list|()
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
literal|"Body"
argument_list|)
expr_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|Soap12FaultOutInterceptorInternal
operator|.
name|INSTANCE
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|writer
operator|.
name|writeEndDocument
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|Document
name|faultDoc
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertValid
argument_list|(
literal|"//soap12env:Fault/soap12env:Code/soap12env:Value[text()='ns1:Sender']"
argument_list|,
name|faultDoc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//soap12env:Fault/soap12env:Code/soap12env:Subcode/"
operator|+
literal|"soap12env:Value[text()='cxffaultcode:invalidsoap']"
argument_list|,
name|faultDoc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//soap12env:Fault/soap12env:Reason/soap12env:Text[@xml:lang='en']"
argument_list|,
name|faultDoc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//soap12env:Fault/soap12env:Reason/soap12env:Text[text()='"
operator|+
name|faultString
operator|+
literal|"']"
argument_list|,
name|faultDoc
argument_list|)
expr_stmt|;
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|reader
argument_list|)
expr_stmt|;
name|reader
operator|.
name|nextTag
argument_list|()
expr_stmt|;
name|Soap12FaultInInterceptor
name|inInterceptor
init|=
operator|new
name|Soap12FaultInInterceptor
argument_list|()
decl_stmt|;
name|inInterceptor
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|SoapFault
name|fault2
init|=
operator|(
name|SoapFault
operator|)
name|m
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|fault2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|,
name|fault2
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|fault
operator|.
name|getMessage
argument_list|()
argument_list|,
name|fault2
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|fault
operator|.
name|getSubCode
argument_list|()
argument_list|,
name|fault2
operator|.
name|getSubCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSoap12WithMultipleSubCodesOut
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|faultString
init|=
literal|"Hadrian caused this Fault!"
decl_stmt|;
name|SoapFault
name|fault
init|=
operator|new
name|SoapFault
argument_list|(
name|faultString
argument_list|,
name|Soap12
operator|.
name|getInstance
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|)
decl_stmt|;
name|fault
operator|.
name|addSubCode
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/soap/fault"
argument_list|,
literal|"invalidsoap"
argument_list|)
argument_list|)
expr_stmt|;
name|fault
operator|.
name|addSubCode
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/soap/fault2"
argument_list|,
literal|"invalidsoap2"
argument_list|,
literal|"cxffaultcode2"
argument_list|)
argument_list|)
expr_stmt|;
name|SoapMessage
name|m
init|=
operator|new
name|SoapMessage
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
name|m
operator|.
name|setVersion
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
name|fault
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|writer
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|writer
operator|.
name|writeStartDocument
argument_list|()
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
literal|"Body"
argument_list|)
expr_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|Soap12FaultOutInterceptorInternal
operator|.
name|INSTANCE
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|writer
operator|.
name|writeEndDocument
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|Document
name|faultDoc
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertValid
argument_list|(
literal|"//soap12env:Fault/soap12env:Code/soap12env:Value[text()='ns1:Sender']"
argument_list|,
name|faultDoc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//soap12env:Fault/soap12env:Code/soap12env:Subcode/"
operator|+
literal|"soap12env:Value[text()='ns2:invalidsoap']"
argument_list|,
name|faultDoc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//soap12env:Fault/soap12env:Code/soap12env:Subcode/soap12env:Subcode/"
operator|+
literal|"soap12env:Value[text()='cxffaultcode2:invalidsoap2']"
argument_list|,
name|faultDoc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//soap12env:Fault/soap12env:Reason/soap12env:Text[@xml:lang='en']"
argument_list|,
name|faultDoc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//soap12env:Fault/soap12env:Reason/soap12env:Text[text()='"
operator|+
name|faultString
operator|+
literal|"']"
argument_list|,
name|faultDoc
argument_list|)
expr_stmt|;
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|reader
argument_list|)
expr_stmt|;
name|reader
operator|.
name|nextTag
argument_list|()
expr_stmt|;
name|Soap12FaultInInterceptor
name|inInterceptor
init|=
operator|new
name|Soap12FaultInInterceptor
argument_list|()
decl_stmt|;
name|inInterceptor
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|SoapFault
name|fault2
init|=
operator|(
name|SoapFault
operator|)
name|m
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|fault2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|,
name|fault2
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|fault
operator|.
name|getMessage
argument_list|()
argument_list|,
name|fault2
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|fault
operator|.
name|getSubCodes
argument_list|()
argument_list|,
name|fault2
operator|.
name|getSubCodes
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFaultToSoapFault
parameter_list|()
throws|throws
name|Exception
block|{
name|Exception
name|ex
init|=
operator|new
name|Exception
argument_list|()
decl_stmt|;
name|Fault
name|fault
init|=
operator|new
name|Fault
argument_list|(
name|ex
argument_list|,
name|Fault
operator|.
name|FAULT_CODE_CLIENT
argument_list|)
decl_stmt|;
name|verifyFaultToSoapFault
argument_list|(
name|fault
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
name|Soap11
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|verifyFaultToSoapFault
argument_list|(
name|fault
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
name|Soap12
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|fault
operator|=
operator|new
name|Fault
argument_list|(
name|ex
argument_list|,
name|Fault
operator|.
name|FAULT_CODE_SERVER
argument_list|)
expr_stmt|;
name|verifyFaultToSoapFault
argument_list|(
name|fault
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
name|Soap11
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|verifyFaultToSoapFault
argument_list|(
name|fault
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
name|Soap12
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|fault
operator|.
name|setMessage
argument_list|(
literal|"fault-one"
argument_list|)
expr_stmt|;
name|verifyFaultToSoapFault
argument_list|(
name|fault
argument_list|,
literal|"fault-one"
argument_list|,
literal|false
argument_list|,
name|Soap11
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|ex
operator|=
operator|new
name|Exception
argument_list|(
literal|"fault-two"
argument_list|)
expr_stmt|;
name|fault
operator|=
operator|new
name|Fault
argument_list|(
name|ex
argument_list|,
name|Fault
operator|.
name|FAULT_CODE_CLIENT
argument_list|)
expr_stmt|;
name|verifyFaultToSoapFault
argument_list|(
name|fault
argument_list|,
literal|"fault-two"
argument_list|,
literal|true
argument_list|,
name|Soap11
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|fault
operator|=
operator|new
name|Fault
argument_list|(
name|ex
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org"
argument_list|,
literal|"myFaultCode"
argument_list|)
argument_list|)
expr_stmt|;
name|SoapFault
name|f
init|=
name|verifyFaultToSoapFault
argument_list|(
name|fault
argument_list|,
literal|"fault-two"
argument_list|,
literal|false
argument_list|,
name|Soap12
operator|.
name|getInstance
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"myFaultCode"
argument_list|,
name|f
operator|.
name|getSubCodes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|SoapFault
name|verifyFaultToSoapFault
parameter_list|(
name|Fault
name|fault
parameter_list|,
name|String
name|msg
parameter_list|,
name|boolean
name|sender
parameter_list|,
name|SoapVersion
name|v
parameter_list|)
block|{
name|SoapFault
name|sf
init|=
name|SoapFault
operator|.
name|createFault
argument_list|(
name|fault
argument_list|,
name|v
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|sender
condition|?
name|v
operator|.
name|getSender
argument_list|()
else|:
name|v
operator|.
name|getReceiver
argument_list|()
argument_list|,
name|sf
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|msg
argument_list|,
name|sf
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sf
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCXF1864
parameter_list|()
throws|throws
name|Exception
block|{
name|SoapMessage
name|m
init|=
operator|new
name|SoapMessage
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
name|m
operator|.
name|setVersion
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"cxf1864.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|reader
argument_list|)
expr_stmt|;
name|reader
operator|.
name|nextTag
argument_list|()
expr_stmt|;
name|Soap12FaultInInterceptor
name|inInterceptor
init|=
operator|new
name|Soap12FaultInInterceptor
argument_list|()
decl_stmt|;
name|inInterceptor
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|SoapFault
name|fault2
init|=
operator|(
name|SoapFault
operator|)
name|m
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|fault2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
operator|.
name|getReceiver
argument_list|()
argument_list|,
name|fault2
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCXF4181
parameter_list|()
throws|throws
name|Exception
block|{
comment|//Try WITH SAAJ
name|SoapMessage
name|m
init|=
operator|new
name|SoapMessage
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
name|m
operator|.
name|setVersion
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"cxf4181.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|reader
argument_list|)
expr_stmt|;
operator|new
name|SAAJPreInInterceptor
argument_list|()
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
operator|new
name|ReadHeadersInterceptor
argument_list|(
literal|null
argument_list|)
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
operator|new
name|StartBodyInterceptor
argument_list|()
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
operator|new
name|SAAJInInterceptor
argument_list|()
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
operator|new
name|Soap12FaultInInterceptor
argument_list|()
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|Node
name|nd
init|=
name|m
operator|.
name|getContent
argument_list|(
name|Node
operator|.
name|class
argument_list|)
decl_stmt|;
name|SOAPPart
name|part
init|=
operator|(
name|SOAPPart
operator|)
name|nd
decl_stmt|;
name|assertEquals
argument_list|(
literal|"S"
argument_list|,
name|part
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"S2"
argument_list|,
name|part
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getHeader
argument_list|()
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"S3"
argument_list|,
name|part
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|SOAPFault
name|fault
init|=
name|part
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|getFault
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"S"
argument_list|,
name|fault
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Authentication Failure"
argument_list|,
name|fault
operator|.
name|getFaultString
argument_list|()
argument_list|)
expr_stmt|;
name|SoapFault
name|fault2
init|=
operator|(
name|SoapFault
operator|)
name|m
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|fault2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|,
name|fault2
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/02/trust"
argument_list|,
literal|"FailedAuthentication"
argument_list|)
argument_list|,
name|fault2
operator|.
name|getSubCode
argument_list|()
argument_list|)
expr_stmt|;
name|Element
name|el
init|=
name|part
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|nd
operator|=
name|el
operator|.
name|getFirstChild
argument_list|()
expr_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|nd
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|nd
operator|instanceof
name|Element
condition|)
block|{
name|count
operator|++
expr_stmt|;
block|}
name|nd
operator|=
name|nd
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|count
argument_list|)
expr_stmt|;
comment|//Try WITHOUT SAAJ
name|m
operator|=
operator|new
name|SoapMessage
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|setVersion
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|reader
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"cxf4181.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|reader
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
operator|new
name|ReadHeadersInterceptor
argument_list|(
literal|null
argument_list|)
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
operator|new
name|StartBodyInterceptor
argument_list|()
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
operator|new
name|Soap12FaultInInterceptor
argument_list|()
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|nd
operator|=
name|m
operator|.
name|getContent
argument_list|(
name|Node
operator|.
name|class
argument_list|)
expr_stmt|;
name|fault2
operator|=
operator|(
name|SoapFault
operator|)
name|m
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|fault2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|,
name|fault2
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/02/trust"
argument_list|,
literal|"FailedAuthentication"
argument_list|)
argument_list|,
name|fault2
operator|.
name|getSubCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCXF5493
parameter_list|()
throws|throws
name|Exception
block|{
name|SoapMessage
name|m
init|=
operator|new
name|SoapMessage
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
name|m
operator|.
name|setVersion
argument_list|(
name|Soap11
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"cxf5493.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|reader
argument_list|)
expr_stmt|;
name|reader
operator|.
name|nextTag
argument_list|()
expr_stmt|;
comment|//env
name|reader
operator|.
name|nextTag
argument_list|()
expr_stmt|;
comment|//body
name|reader
operator|.
name|nextTag
argument_list|()
expr_stmt|;
comment|//fault
name|Soap11FaultInInterceptor
name|inInterceptor
init|=
operator|new
name|Soap11FaultInInterceptor
argument_list|()
decl_stmt|;
name|inInterceptor
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|SoapFault
name|fault2
init|=
operator|(
name|SoapFault
operator|)
name|m
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|fault2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Soap11
operator|.
name|getInstance
argument_list|()
operator|.
name|getReceiver
argument_list|()
argument_list|,
name|fault2
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"some text containing a xml tag<xml-tag>"
argument_list|,
name|fault2
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|=
operator|new
name|SoapMessage
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
name|m
operator|.
name|setVersion
argument_list|(
name|Soap11
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|reader
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"cxf5493.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|reader
argument_list|)
expr_stmt|;
operator|new
name|SAAJPreInInterceptor
argument_list|()
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
operator|new
name|ReadHeadersInterceptor
argument_list|(
literal|null
argument_list|)
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
operator|new
name|StartBodyInterceptor
argument_list|()
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
operator|new
name|SAAJInInterceptor
argument_list|()
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
operator|new
name|Soap11FaultInInterceptor
argument_list|()
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|fault2
operator|=
operator|(
name|SoapFault
operator|)
name|m
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|fault2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Soap11
operator|.
name|getInstance
argument_list|()
operator|.
name|getReceiver
argument_list|()
argument_list|,
name|fault2
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"some text containing a xml tag<xml-tag>"
argument_list|,
name|fault2
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

