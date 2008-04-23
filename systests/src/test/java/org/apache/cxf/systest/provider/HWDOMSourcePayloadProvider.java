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
name|systest
operator|.
name|provider
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
name|Detail
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
name|DetailEntry
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
name|SOAPElement
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
name|SOAPException
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
name|SOAPFactory
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
name|Provider
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
name|WebServiceProvider
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
name|soap
operator|.
name|SOAPFaultException
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
name|helpers
operator|.
name|DOMUtils
import|;
end_import

begin_comment
comment|//The following wsdl file is used.
end_comment

begin_comment
comment|//wsdlLocation = "/trunk/testutils/src/main/resources/wsdl/hello_world_rpc_lit.wsdl"
end_comment

begin_class
annotation|@
name|WebServiceProvider
argument_list|(
name|portName
operator|=
literal|"SoapPortProviderRPCLit3"
argument_list|,
name|serviceName
operator|=
literal|"SOAPServiceProviderRPCLit"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
name|wsdlLocation
operator|=
literal|"/wsdl/hello_world_rpc_lit.wsdl"
argument_list|)
specifier|public
class|class
name|HWDOMSourcePayloadProvider
implements|implements
name|Provider
argument_list|<
name|DOMSource
argument_list|>
block|{
specifier|private
specifier|static
name|QName
name|sayHi
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"sayHi"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|QName
name|greetMe
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"greetMe"
argument_list|)
decl_stmt|;
specifier|private
name|Document
name|sayHiResponse
decl_stmt|;
specifier|private
name|Document
name|greetMeResponse
decl_stmt|;
specifier|private
name|MessageFactory
name|factory
decl_stmt|;
specifier|public
name|HWDOMSourcePayloadProvider
parameter_list|()
block|{
try|try
block|{
name|factory
operator|=
name|MessageFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"resources/sayHiRpcLiteralResp.xml"
argument_list|)
decl_stmt|;
name|sayHiResponse
operator|=
name|factory
operator|.
name|createMessage
argument_list|(
literal|null
argument_list|,
name|is
argument_list|)
operator|.
name|getSOAPBody
argument_list|()
operator|.
name|extractContentAsDocument
argument_list|()
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"resources/GreetMeRpcLiteralResp.xml"
argument_list|)
expr_stmt|;
name|greetMeResponse
operator|=
name|factory
operator|.
name|createMessage
argument_list|(
literal|null
argument_list|,
name|is
argument_list|)
operator|.
name|getSOAPBody
argument_list|()
operator|.
name|extractContentAsDocument
argument_list|()
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|DOMSource
name|invoke
parameter_list|(
name|DOMSource
name|request
parameter_list|)
block|{
name|DOMSource
name|response
init|=
operator|new
name|DOMSource
argument_list|()
decl_stmt|;
name|Node
name|n
init|=
name|request
operator|.
name|getNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|n
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
name|sayHi
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|response
operator|.
name|setNode
argument_list|(
name|sayHiResponse
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|n
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
name|greetMe
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|Element
name|el
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|n
argument_list|)
decl_stmt|;
name|String
name|s
init|=
name|DOMUtils
operator|.
name|getContent
argument_list|(
name|el
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|trim
argument_list|()
operator|.
name|equals
argument_list|(
literal|"throwFault"
argument_list|)
condition|)
block|{
try|try
block|{
name|SOAPFactory
name|f
init|=
name|SOAPFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|SOAPFault
name|soapFault
init|=
name|f
operator|.
name|createFault
argument_list|()
decl_stmt|;
name|soapFault
operator|.
name|setFaultString
argument_list|(
literal|"Test Fault String ****"
argument_list|)
expr_stmt|;
name|Detail
name|detail
init|=
name|soapFault
operator|.
name|addDetail
argument_list|()
decl_stmt|;
name|detail
operator|=
name|soapFault
operator|.
name|getDetail
argument_list|()
expr_stmt|;
name|QName
name|qName
init|=
operator|new
name|QName
argument_list|(
literal|"http://www.Hello.org/greeter"
argument_list|,
literal|"TestFault"
argument_list|,
literal|"ns"
argument_list|)
decl_stmt|;
name|DetailEntry
name|de
init|=
name|detail
operator|.
name|addDetailEntry
argument_list|(
name|qName
argument_list|)
decl_stmt|;
name|qName
operator|=
operator|new
name|QName
argument_list|(
literal|"http://www.Hello.org/greeter"
argument_list|,
literal|"ErrorCode"
argument_list|,
literal|"ns"
argument_list|)
expr_stmt|;
name|SOAPElement
name|errorElement
init|=
name|de
operator|.
name|addChildElement
argument_list|(
name|qName
argument_list|)
decl_stmt|;
name|errorElement
operator|.
name|setTextContent
argument_list|(
literal|"errorcode"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|SOAPFaultException
argument_list|(
name|soapFault
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
name|response
operator|.
name|setNode
argument_list|(
name|greetMeResponse
argument_list|)
expr_stmt|;
block|}
return|return
name|response
return|;
block|}
comment|/*         private Node getElementChildNode(SOAPBody body) {         Node n = body.getFirstChild();          while (n.getNodeType() != Node.ELEMENT_NODE) {             n = n.getNextSibling();         }                  return n;             }*/
block|}
end_class

end_unit

