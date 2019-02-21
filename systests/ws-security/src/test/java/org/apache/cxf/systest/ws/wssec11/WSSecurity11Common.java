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
name|io
operator|.
name|IOException
import|;
end_import

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
name|BindingProvider
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
name|SecurityConstants
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
name|portPrefix
parameter_list|,
name|String
name|portNumber
parameter_list|,
name|boolean
name|unrestrictedPoliciesInstalled
parameter_list|,
name|boolean
name|streaming
parameter_list|)
throws|throws
name|IOException
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
literal|"org/apache/cxf/systest/ws/wssec11/client.xml"
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
literal|"org/apache/cxf/systest/ws/wssec11/client_restricted.xml"
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
argument_list|,
name|portNumber
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
if|if
condition|(
name|streaming
condition|)
block|{
operator|(
operator|(
name|BindingProvider
operator|)
name|port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENABLE_STREAMING_SECURITY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|port
operator|)
operator|.
name|getResponseContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENABLE_STREAMING_SECURITY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
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
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|port
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|URL
name|getWsdlLocation
parameter_list|(
name|String
name|portPrefix
parameter_list|,
name|String
name|portNumber
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|portNumber
operator|+
literal|"/"
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
block|}
end_class

end_unit

