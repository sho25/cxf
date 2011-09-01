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
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|UndeclaredThrowableException
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
name|hello_world_soap_http
operator|.
name|Greeter
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
name|hello_world_soap_http
operator|.
name|GreeterService
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

begin_class
specifier|public
specifier|final
class|class
name|Client
block|{
specifier|private
specifier|static
specifier|final
name|String
name|USER_NAME
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.name"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WSU_NS
init|=
literal|"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
decl_stmt|;
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
try|try
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
name|Client
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
literal|"PasswordDigest"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"user"
argument_list|,
literal|"clientx509v1"
argument_list|)
expr_stmt|;
comment|//If you are using the patch WSS-194, then uncomment below two lines and
comment|//comment the above "user" prop line.
comment|//outProps.put("user", "abcd");
comment|//outProps.put("signatureUser", "clientx509v1");
name|outProps
operator|.
name|put
argument_list|(
literal|"passwordCallbackClass"
argument_list|,
literal|"demo.wssec.client.UTPasswordCallback"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"encryptionUser"
argument_list|,
literal|"serverx509v1"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"encryptionPropFile"
argument_list|,
literal|"etc/Client_Encrypt.properties"
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
name|WSU_NS
operator|+
literal|"}Timestamp;"
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
literal|"etc/Client_Sign.properties"
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
name|bus
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
literal|"PasswordText"
argument_list|)
expr_stmt|;
name|inProps
operator|.
name|put
argument_list|(
literal|"passwordCallbackClass"
argument_list|,
literal|"demo.wssec.client.UTPasswordCallback"
argument_list|)
expr_stmt|;
name|inProps
operator|.
name|put
argument_list|(
literal|"decryptionPropFile"
argument_list|,
literal|"etc/Client_Sign.properties"
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
literal|"etc/Client_Encrypt.properties"
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
name|bus
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
name|GreeterService
name|service
init|=
operator|new
name|GreeterService
argument_list|()
decl_stmt|;
name|Greeter
name|port
init|=
name|service
operator|.
name|getGreeterPort
argument_list|()
decl_stmt|;
name|String
index|[]
name|names
init|=
operator|new
name|String
index|[]
block|{
literal|"Anne"
block|,
literal|"Bill"
block|,
literal|"Chris"
block|,
literal|"Sachin Tendulkar"
block|}
decl_stmt|;
comment|// make a sequence of 4 invocations
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|4
condition|;
name|i
operator|++
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking greetMe..."
argument_list|)
expr_stmt|;
name|String
name|response
init|=
name|port
operator|.
name|greetMe
argument_list|(
name|names
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"response: "
operator|+
name|response
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
block|}
comment|// allow aynchronous resends to occur
name|Thread
operator|.
name|sleep
argument_list|(
literal|30
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
block|}
catch|catch
parameter_list|(
name|UndeclaredThrowableException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|getUndeclaredThrowable
argument_list|()
operator|.
name|printStackTrace
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
finally|finally
block|{
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

