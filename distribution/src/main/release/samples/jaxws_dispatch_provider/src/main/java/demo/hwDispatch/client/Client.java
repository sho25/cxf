begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|hwDispatch
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

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
name|net
operator|.
name|URL
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
name|MessageFactory
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
name|SOAPMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Dispatch
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
operator|.
name|Mode
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
name|hello_world_soap_http
operator|.
name|SOAPService1
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|SOAPService2
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|SOAPService3
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|Client
block|{
specifier|private
name|Client
parameter_list|()
block|{     }
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
name|args
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"please specify wsdl"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|URL
name|wsdlURL
decl_stmt|;
name|File
name|wsdlFile
init|=
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|wsdlFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|wsdlURL
operator|=
name|wsdlFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|wsdlURL
operator|=
operator|new
name|URL
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
name|MessageFactory
name|factory
init|=
name|MessageFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|wsdlURL
operator|+
literal|"\n\n"
argument_list|)
expr_stmt|;
name|QName
name|serviceName1
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SOAPService1"
argument_list|)
decl_stmt|;
name|QName
name|portName1
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SoapPort1"
argument_list|)
decl_stmt|;
name|SOAPService1
name|service1
init|=
operator|new
name|SOAPService1
argument_list|(
name|wsdlURL
argument_list|,
name|serviceName1
argument_list|)
decl_stmt|;
name|InputStream
name|is1
init|=
name|Client
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/GreetMeDocLiteralReq1.xml"
argument_list|)
decl_stmt|;
if|if
condition|(
name|is1
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Failed to create input stream from file "
operator|+
literal|"GreetMeDocLiteralReq1.xml, please check"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|SOAPMessage
name|soapReq1
init|=
name|factory
operator|.
name|createMessage
argument_list|(
literal|null
argument_list|,
name|is1
argument_list|)
decl_stmt|;
name|Dispatch
argument_list|<
name|SOAPMessage
argument_list|>
name|dispSOAPMsg
init|=
name|service1
operator|.
name|createDispatch
argument_list|(
name|portName1
argument_list|,
name|SOAPMessage
operator|.
name|class
argument_list|,
name|Mode
operator|.
name|MESSAGE
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking server through Dispatch interface using SOAPMessage"
argument_list|)
expr_stmt|;
name|SOAPMessage
name|soapResp
init|=
name|dispSOAPMsg
operator|.
name|invoke
argument_list|(
name|soapReq1
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Response from server: "
operator|+
name|soapResp
operator|.
name|getSOAPBody
argument_list|()
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
name|QName
name|serviceName2
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SOAPService2"
argument_list|)
decl_stmt|;
name|QName
name|portName2
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SoapPort2"
argument_list|)
decl_stmt|;
name|SOAPService2
name|service2
init|=
operator|new
name|SOAPService2
argument_list|(
name|wsdlURL
argument_list|,
name|serviceName2
argument_list|)
decl_stmt|;
name|InputStream
name|is2
init|=
name|Client
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/GreetMeDocLiteralReq2.xml"
argument_list|)
decl_stmt|;
if|if
condition|(
name|is2
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Failed to create input stream from file "
operator|+
literal|"GreetMeDocLiteralReq2.xml, please check"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|SOAPMessage
name|soapReq2
init|=
name|factory
operator|.
name|createMessage
argument_list|(
literal|null
argument_list|,
name|is2
argument_list|)
decl_stmt|;
name|DOMSource
name|domReqMessage
init|=
operator|new
name|DOMSource
argument_list|(
name|soapReq2
operator|.
name|getSOAPPart
argument_list|()
argument_list|)
decl_stmt|;
name|Dispatch
argument_list|<
name|DOMSource
argument_list|>
name|dispDOMSrcMessage
init|=
name|service2
operator|.
name|createDispatch
argument_list|(
name|portName2
argument_list|,
name|DOMSource
operator|.
name|class
argument_list|,
name|Mode
operator|.
name|MESSAGE
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking server through Dispatch interface using DOMSource in MESSAGE Mode"
argument_list|)
expr_stmt|;
name|DOMSource
name|domRespMessage
init|=
name|dispDOMSrcMessage
operator|.
name|invoke
argument_list|(
name|domReqMessage
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Response from server: "
operator|+
name|domRespMessage
operator|.
name|getNode
argument_list|()
operator|.
name|getLastChild
argument_list|()
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
name|QName
name|serviceName3
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SOAPService3"
argument_list|)
decl_stmt|;
name|QName
name|portName3
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SoapPort3"
argument_list|)
decl_stmt|;
name|SOAPService3
name|service3
init|=
operator|new
name|SOAPService3
argument_list|(
name|wsdlURL
argument_list|,
name|serviceName3
argument_list|)
decl_stmt|;
name|InputStream
name|is3
init|=
name|Client
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/GreetMeDocLiteralReq3.xml"
argument_list|)
decl_stmt|;
if|if
condition|(
name|is3
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Failed to create input stream from file "
operator|+
literal|"GreetMeDocLiteralReq3.xml, please check"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|SOAPMessage
name|soapReq3
init|=
name|MessageFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createMessage
argument_list|(
literal|null
argument_list|,
name|is3
argument_list|)
decl_stmt|;
name|DOMSource
name|domReqPayload
init|=
operator|new
name|DOMSource
argument_list|(
name|soapReq3
operator|.
name|getSOAPBody
argument_list|()
operator|.
name|extractContentAsDocument
argument_list|()
argument_list|)
decl_stmt|;
name|Dispatch
argument_list|<
name|DOMSource
argument_list|>
name|dispDOMSrcPayload
init|=
name|service3
operator|.
name|createDispatch
argument_list|(
name|portName3
argument_list|,
name|DOMSource
operator|.
name|class
argument_list|,
name|Mode
operator|.
name|PAYLOAD
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking server through Dispatch interface using DOMSource in PAYLOAD Mode"
argument_list|)
expr_stmt|;
name|DOMSource
name|domRespPayload
init|=
name|dispDOMSrcPayload
operator|.
name|invoke
argument_list|(
name|domReqPayload
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Response from server: "
operator|+
name|fetchElementByName
argument_list|(
name|domRespPayload
operator|.
name|getNode
argument_list|()
argument_list|,
literal|"greetMeResponse"
argument_list|)
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|Element
name|fetchElementByName
parameter_list|(
name|Node
name|parent
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|Element
name|ret
init|=
literal|null
decl_stmt|;
name|Node
name|node
init|=
name|parent
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
if|if
condition|(
name|node
operator|instanceof
name|Element
operator|&&
operator|(
operator|(
name|Element
operator|)
name|node
operator|)
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|ret
operator|=
operator|(
name|Element
operator|)
name|node
expr_stmt|;
break|break;
block|}
name|node
operator|=
name|node
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
block|}
end_class

end_unit

