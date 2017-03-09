begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|wssec
operator|.
name|server
package|;
end_package

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
name|ws
operator|.
name|Endpoint
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
name|Bus
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
name|BusFactory
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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|jaxws
operator|.
name|EndpointImpl
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
name|ws
operator|.
name|security
operator|.
name|wss4j
operator|.
name|DefaultCryptoCoverageChecker
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
name|ws
operator|.
name|security
operator|.
name|wss4j
operator|.
name|WSS4JInInterceptor
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
name|ws
operator|.
name|security
operator|.
name|wss4j
operator|.
name|WSS4JOutInterceptor
import|;
end_import

begin_comment
comment|/**  * A DOM-based server  */
end_comment

begin_class
specifier|public
class|class
name|Server
block|{
specifier|private
specifier|static
specifier|final
name|String
name|WSSE_NS
init|=
literal|"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WSU_NS
init|=
literal|"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
decl_stmt|;
specifier|protected
name|Server
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Starting Server"
argument_list|)
expr_stmt|;
name|Object
name|implementor
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:9000/SoapContext/GreeterPort"
decl_stmt|;
name|EndpointImpl
name|endpoint
init|=
operator|(
name|EndpointImpl
operator|)
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outProps
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"action"
argument_list|,
literal|"UsernameToken Timestamp Signature Encrypt"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"passwordType"
argument_list|,
literal|"PasswordText"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"passwordCallbackClass"
argument_list|,
literal|"demo.wssec.server.UTPasswordCallback"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"user"
argument_list|,
literal|"Alice"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"signatureUser"
argument_list|,
literal|"serverx509v1"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"encryptionUser"
argument_list|,
literal|"clientx509v1"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"encryptionPropFile"
argument_list|,
literal|"etc/Server_SignVerf.properties"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"encryptionKeyIdentifier"
argument_list|,
literal|"IssuerSerial"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"encryptionParts"
argument_list|,
literal|"{Element}{"
operator|+
name|WSSE_NS
operator|+
literal|"}UsernameToken;"
operator|+
literal|"{Content}{http://schemas.xmlsoap.org/soap/envelope/}Body"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"signaturePropFile"
argument_list|,
literal|"etc/Server_Decrypt.properties"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"signatureKeyIdentifier"
argument_list|,
literal|"DirectReference"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"signatureParts"
argument_list|,
literal|"{Element}{"
operator|+
name|WSU_NS
operator|+
literal|"}Timestamp;"
operator|+
literal|"{Element}{http://schemas.xmlsoap.org/soap/envelope/}Body"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"encryptionKeyTransportAlgorithm"
argument_list|,
literal|"http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"signatureAlgorithm"
argument_list|,
literal|"http://www.w3.org/2000/09/xmldsig#rsa-sha1"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|WSS4JOutInterceptor
argument_list|(
name|outProps
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|inProps
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|inProps
operator|.
name|put
argument_list|(
literal|"action"
argument_list|,
literal|"UsernameToken Timestamp Signature Encrypt"
argument_list|)
expr_stmt|;
name|inProps
operator|.
name|put
argument_list|(
literal|"passwordType"
argument_list|,
literal|"PasswordDigest"
argument_list|)
expr_stmt|;
name|inProps
operator|.
name|put
argument_list|(
literal|"passwordCallbackClass"
argument_list|,
literal|"demo.wssec.server.UTPasswordCallback"
argument_list|)
expr_stmt|;
name|inProps
operator|.
name|put
argument_list|(
literal|"decryptionPropFile"
argument_list|,
literal|"etc/Server_Decrypt.properties"
argument_list|)
expr_stmt|;
name|inProps
operator|.
name|put
argument_list|(
literal|"encryptionKeyIdentifier"
argument_list|,
literal|"IssuerSerial"
argument_list|)
expr_stmt|;
name|inProps
operator|.
name|put
argument_list|(
literal|"signaturePropFile"
argument_list|,
literal|"etc/Server_SignVerf.properties"
argument_list|)
expr_stmt|;
name|inProps
operator|.
name|put
argument_list|(
literal|"signatureKeyIdentifier"
argument_list|,
literal|"DirectReference"
argument_list|)
expr_stmt|;
name|inProps
operator|.
name|put
argument_list|(
literal|"encryptionKeyTransportAlgorithm"
argument_list|,
literal|"http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p"
argument_list|)
expr_stmt|;
name|inProps
operator|.
name|put
argument_list|(
literal|"signatureAlgorithm"
argument_list|,
literal|"http://www.w3.org/2000/09/xmldsig#rsa-sha1"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|WSS4JInInterceptor
argument_list|(
name|inProps
argument_list|)
argument_list|)
expr_stmt|;
comment|// Check to make sure that the SOAP Body and Timestamp were signed,
comment|// and that the SOAP Body was encrypted
name|DefaultCryptoCoverageChecker
name|coverageChecker
init|=
operator|new
name|DefaultCryptoCoverageChecker
argument_list|()
decl_stmt|;
name|coverageChecker
operator|.
name|setSignBody
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|coverageChecker
operator|.
name|setSignTimestamp
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|coverageChecker
operator|.
name|setEncryptBody
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|coverageChecker
argument_list|)
expr_stmt|;
block|}
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
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|URL
name|busFile
init|=
name|Server
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"wssec.xml"
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
operator|new
name|Server
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Server ready..."
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5
operator|*
literal|60
operator|*
literal|1000
argument_list|)
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Server exiting"
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
block|}
end_class

end_unit

