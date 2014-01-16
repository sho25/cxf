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
name|Properties
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
name|ws
operator|.
name|security
operator|.
name|wss4j
operator|.
name|StaxCryptoCoverageChecker
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
name|WSS4JStaxInInterceptor
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
name|WSS4JStaxOutInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|crypto
operator|.
name|CryptoFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|stax
operator|.
name|ext
operator|.
name|WSSConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|stax
operator|.
name|ext
operator|.
name|WSSSecurityProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|stax
operator|.
name|securityToken
operator|.
name|WSSecurityTokenConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|stax
operator|.
name|ext
operator|.
name|SecurePart
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|stax
operator|.
name|ext
operator|.
name|XMLSecurityConstants
import|;
end_import

begin_comment
comment|/**  * A StAX-based server  */
end_comment

begin_class
specifier|public
class|class
name|StaxServer
block|{
specifier|protected
name|StaxServer
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
literal|"Starting StaxServer"
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
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
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
name|Properties
name|decCryptoProperties
init|=
name|CryptoFactory
operator|.
name|getProperties
argument_list|(
literal|"etc/Server_Decrypt.properties"
argument_list|,
name|StaxServer
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|Properties
name|sigVerCryptoProperties
init|=
name|CryptoFactory
operator|.
name|getProperties
argument_list|(
literal|"etc/Server_SignVerf.properties"
argument_list|,
name|StaxServer
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|WSSSecurityProperties
name|properties
init|=
operator|new
name|WSSSecurityProperties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|addAction
argument_list|(
name|WSSConstants
operator|.
name|USERNAMETOKEN
argument_list|)
expr_stmt|;
name|properties
operator|.
name|addAction
argument_list|(
name|WSSConstants
operator|.
name|TIMESTAMP
argument_list|)
expr_stmt|;
name|properties
operator|.
name|addAction
argument_list|(
name|WSSConstants
operator|.
name|SIGNATURE
argument_list|)
expr_stmt|;
name|properties
operator|.
name|addAction
argument_list|(
name|WSSConstants
operator|.
name|ENCRYPT
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setUsernameTokenPasswordType
argument_list|(
name|WSSConstants
operator|.
name|UsernameTokenPasswordType
operator|.
name|PASSWORD_TEXT
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setTokenUser
argument_list|(
literal|"Alice"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setSignatureUser
argument_list|(
literal|"serverx509v1"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setEncryptionUser
argument_list|(
literal|"clientx509v1"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setEncryptionCryptoProperties
argument_list|(
name|sigVerCryptoProperties
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setEncryptionKeyIdentifier
argument_list|(
name|WSSecurityTokenConstants
operator|.
name|KeyIdentifier_IssuerSerial
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setEncryptionKeyTransportAlgorithm
argument_list|(
literal|"http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|addEncryptionPart
argument_list|(
operator|new
name|SecurePart
argument_list|(
operator|new
name|QName
argument_list|(
name|WSSConstants
operator|.
name|NS_WSSE10
argument_list|,
literal|"UsernameToken"
argument_list|)
argument_list|,
name|SecurePart
operator|.
name|Modifier
operator|.
name|Element
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|addEncryptionPart
argument_list|(
operator|new
name|SecurePart
argument_list|(
operator|new
name|QName
argument_list|(
name|WSSConstants
operator|.
name|NS_SOAP11
argument_list|,
literal|"Body"
argument_list|)
argument_list|,
name|SecurePart
operator|.
name|Modifier
operator|.
name|Content
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setSignatureCryptoProperties
argument_list|(
name|decCryptoProperties
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setSignatureKeyIdentifier
argument_list|(
name|WSSecurityTokenConstants
operator|.
name|KeyIdentifier_SecurityTokenDirectReference
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setSignatureAlgorithm
argument_list|(
literal|"http://www.w3.org/2000/09/xmldsig#rsa-sha1"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|addSignaturePart
argument_list|(
operator|new
name|SecurePart
argument_list|(
operator|new
name|QName
argument_list|(
name|WSSConstants
operator|.
name|NS_WSU10
argument_list|,
literal|"Timestamp"
argument_list|)
argument_list|,
name|SecurePart
operator|.
name|Modifier
operator|.
name|Element
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|addSignaturePart
argument_list|(
operator|new
name|SecurePart
argument_list|(
operator|new
name|QName
argument_list|(
name|WSSConstants
operator|.
name|NS_SOAP11
argument_list|,
literal|"Body"
argument_list|)
argument_list|,
name|SecurePart
operator|.
name|Modifier
operator|.
name|Content
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setCallbackHandler
argument_list|(
operator|new
name|UTPasswordCallback
argument_list|()
argument_list|)
expr_stmt|;
name|WSS4JStaxOutInterceptor
name|ohandler
init|=
operator|new
name|WSS4JStaxOutInterceptor
argument_list|(
name|properties
argument_list|)
decl_stmt|;
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|ohandler
argument_list|)
expr_stmt|;
name|WSSSecurityProperties
name|inProperties
init|=
operator|new
name|WSSSecurityProperties
argument_list|()
decl_stmt|;
name|inProperties
operator|.
name|addAction
argument_list|(
name|WSSConstants
operator|.
name|USERNAMETOKEN
argument_list|)
expr_stmt|;
name|inProperties
operator|.
name|addAction
argument_list|(
name|WSSConstants
operator|.
name|TIMESTAMP
argument_list|)
expr_stmt|;
name|inProperties
operator|.
name|addAction
argument_list|(
name|WSSConstants
operator|.
name|SIGNATURE
argument_list|)
expr_stmt|;
name|inProperties
operator|.
name|addAction
argument_list|(
name|WSSConstants
operator|.
name|ENCRYPT
argument_list|)
expr_stmt|;
name|inProperties
operator|.
name|setCallbackHandler
argument_list|(
operator|new
name|UTPasswordCallback
argument_list|()
argument_list|)
expr_stmt|;
name|inProperties
operator|.
name|setDecryptionCryptoProperties
argument_list|(
name|decCryptoProperties
argument_list|)
expr_stmt|;
name|inProperties
operator|.
name|setSignatureVerificationCryptoProperties
argument_list|(
name|sigVerCryptoProperties
argument_list|)
expr_stmt|;
name|WSS4JStaxInInterceptor
name|inhandler
init|=
operator|new
name|WSS4JStaxInInterceptor
argument_list|(
name|inProperties
argument_list|)
decl_stmt|;
name|bus
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|inhandler
argument_list|)
expr_stmt|;
comment|// Check to make sure that the SOAP Body and Timestamp were signed,
comment|// and that the SOAP Body was encrypted
name|StaxCryptoCoverageChecker
name|coverageChecker
init|=
operator|new
name|StaxCryptoCoverageChecker
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
name|bus
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|coverageChecker
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
operator|new
name|StaxServer
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"StaxServer ready..."
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
literal|"StaxServer exiting"
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

