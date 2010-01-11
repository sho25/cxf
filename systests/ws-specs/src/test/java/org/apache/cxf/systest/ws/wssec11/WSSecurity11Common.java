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
name|ws
operator|.
name|wssec11
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|crypto
operator|.
name|Cipher
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|SecretKey
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|spec
operator|.
name|SecretKeySpec
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusClientServerTestBase
import|;
end_import

begin_import
import|import
name|wssec
operator|.
name|wssec11
operator|.
name|IPingService
import|;
end_import

begin_import
import|import
name|wssec
operator|.
name|wssec11
operator|.
name|PingService11
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|WSSecurity11Common
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|INPUT
init|=
literal|"foo"
decl_stmt|;
specifier|public
name|void
name|runClientServer
parameter_list|(
name|String
index|[]
name|argv
parameter_list|,
name|boolean
name|unrestrictedPoliciesInstalled
parameter_list|)
block|{
name|Bus
name|bus
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|unrestrictedPoliciesInstalled
condition|)
block|{
name|bus
operator|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/systest/ws/wssec11/client/client.xml"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bus
operator|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/systest/ws/wssec11/client/client_restricted.xml"
argument_list|)
expr_stmt|;
block|}
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|URL
name|wsdlLocation
init|=
literal|null
decl_stmt|;
for|for
control|(
name|String
name|portPrefix
range|:
name|argv
control|)
block|{
name|PingService11
name|svc
init|=
literal|null
decl_stmt|;
name|wsdlLocation
operator|=
name|getWsdlLocation
argument_list|(
name|portPrefix
argument_list|)
expr_stmt|;
name|svc
operator|=
operator|new
name|PingService11
argument_list|(
name|wsdlLocation
argument_list|)
expr_stmt|;
specifier|final
name|IPingService
name|port
init|=
name|svc
operator|.
name|getPort
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://WSSec/wssec11"
argument_list|,
name|portPrefix
operator|+
literal|"_IPingService"
argument_list|)
argument_list|,
name|IPingService
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|String
name|output
init|=
name|port
operator|.
name|echo
argument_list|(
name|INPUT
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|INPUT
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|URL
name|getWsdlLocation
parameter_list|(
name|String
name|portPrefix
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
literal|"http://localhost:9001/"
operator|+
name|portPrefix
operator|+
literal|"PingService?wsdl"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|mue
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
specifier|static
name|boolean
name|checkUnrestrictedPoliciesInstalled
parameter_list|()
block|{
name|boolean
name|unrestrictedPoliciesInstalled
init|=
literal|false
decl_stmt|;
try|try
block|{
name|byte
index|[]
name|data
init|=
block|{
literal|0x00
block|,
literal|0x01
block|,
literal|0x02
block|,
literal|0x03
block|,
literal|0x04
block|,
literal|0x05
block|,
literal|0x06
block|,
literal|0x07
block|}
decl_stmt|;
name|SecretKey
name|key192
init|=
operator|new
name|SecretKeySpec
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|0x00
block|,
literal|0x01
block|,
literal|0x02
block|,
literal|0x03
block|,
literal|0x04
block|,
literal|0x05
block|,
literal|0x06
block|,
literal|0x07
block|,
literal|0x08
block|,
literal|0x09
block|,
literal|0x0a
block|,
literal|0x0b
block|,
literal|0x0c
block|,
literal|0x0d
block|,
literal|0x0e
block|,
literal|0x0f
block|,
literal|0x10
block|,
literal|0x11
block|,
literal|0x12
block|,
literal|0x13
block|,
literal|0x14
block|,
literal|0x15
block|,
literal|0x16
block|,
literal|0x17
block|}
argument_list|,
literal|"AES"
argument_list|)
decl_stmt|;
name|Cipher
name|c
init|=
name|Cipher
operator|.
name|getInstance
argument_list|(
literal|"AES"
argument_list|)
decl_stmt|;
name|c
operator|.
name|init
argument_list|(
name|Cipher
operator|.
name|ENCRYPT_MODE
argument_list|,
name|key192
argument_list|)
expr_stmt|;
name|c
operator|.
name|doFinal
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|unrestrictedPoliciesInstalled
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
name|unrestrictedPoliciesInstalled
return|;
block|}
return|return
name|unrestrictedPoliciesInstalled
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isIBMJDK16
parameter_list|()
block|{
name|String
name|fullVersion
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.fullversion"
argument_list|)
decl_stmt|;
if|if
condition|(
name|fullVersion
operator|==
literal|null
condition|)
block|{
comment|//Maybe one of the non IBM JDKs dont set this property, but
comment|//the IBM one definitely does
return|return
literal|false
return|;
block|}
if|if
condition|(
name|fullVersion
operator|.
name|indexOf
argument_list|(
literal|"IBM"
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|javaVersion
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.version"
argument_list|)
decl_stmt|;
name|double
name|javaVersionNum
init|=
literal|0.0
decl_stmt|;
if|if
condition|(
name|javaVersion
operator|.
name|length
argument_list|()
operator|>
literal|3
condition|)
block|{
name|javaVersionNum
operator|=
operator|new
name|Double
argument_list|(
name|javaVersion
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|3
argument_list|)
argument_list|)
operator|.
name|doubleValue
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|javaVersionNum
operator|=
operator|new
name|Double
argument_list|(
name|javaVersion
argument_list|)
operator|.
name|doubleValue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|javaVersionNum
operator|<
literal|1.6
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

