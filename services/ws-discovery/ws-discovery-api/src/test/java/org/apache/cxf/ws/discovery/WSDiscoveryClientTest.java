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
name|ws
operator|.
name|discovery
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
name|java
operator|.
name|net
operator|.
name|DatagramPacket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MulticastSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|NetworkInterface
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|wsaddressing
operator|.
name|W3CEndpointReference
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
name|feature
operator|.
name|LoggingFeature
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
name|IOUtils
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
name|TestUtil
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
name|discovery
operator|.
name|internal
operator|.
name|WSDiscoveryServiceImpl
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
name|discovery
operator|.
name|wsdl
operator|.
name|HelloType
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
name|discovery
operator|.
name|wsdl
operator|.
name|ProbeMatchType
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
name|discovery
operator|.
name|wsdl
operator|.
name|ProbeMatchesType
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
name|discovery
operator|.
name|wsdl
operator|.
name|ProbeType
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
name|discovery
operator|.
name|wsdl
operator|.
name|ResolveMatchType
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
name|discovery
operator|.
name|wsdl
operator|.
name|ScopesType
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|WSDiscoveryClientTest
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|WSDiscoveryClientTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testMultiResponses
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Disable the test on Redhat Enterprise Linux which doesn't enable the UDP broadcast by default
if|if
condition|(
name|System
operator|.
name|getProperties
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"Linux"
argument_list|)
operator|&&
name|System
operator|.
name|getProperties
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"os.version"
argument_list|)
operator|.
name|indexOf
argument_list|(
literal|"el"
argument_list|)
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Skipping MultiResponse test for REL"
argument_list|)
expr_stmt|;
return|return;
block|}
name|Enumeration
argument_list|<
name|NetworkInterface
argument_list|>
name|interfaces
init|=
name|NetworkInterface
operator|.
name|getNetworkInterfaces
argument_list|()
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|interfaces
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|NetworkInterface
name|networkInterface
init|=
name|interfaces
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|networkInterface
operator|.
name|isUp
argument_list|()
operator|||
name|networkInterface
operator|.
name|isLoopback
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|count
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|count
operator|==
literal|0
condition|)
block|{
comment|//no non-loopbacks, cannot do broadcasts
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Skipping MultiResponse test"
argument_list|)
expr_stmt|;
return|return;
block|}
operator|new
name|Thread
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
comment|//fake a discovery server to send back some canned messages.
name|InetAddress
name|address
init|=
name|InetAddress
operator|.
name|getByName
argument_list|(
literal|"239.255.255.250"
argument_list|)
decl_stmt|;
name|MulticastSocket
name|s
init|=
operator|new
name|MulticastSocket
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|PORT
argument_list|)
argument_list|)
decl_stmt|;
name|s
operator|.
name|setBroadcast
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|s
operator|.
name|joinGroup
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|s
operator|.
name|setReceiveBufferSize
argument_list|(
literal|64
operator|*
literal|1024
argument_list|)
expr_stmt|;
name|s
operator|.
name|setSoTimeout
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
literal|64
operator|*
literal|1024
index|]
decl_stmt|;
name|DatagramPacket
name|p
init|=
operator|new
name|DatagramPacket
argument_list|(
name|bytes
argument_list|,
name|bytes
operator|.
name|length
argument_list|,
name|address
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|PORT
argument_list|)
argument_list|)
decl_stmt|;
name|s
operator|.
name|receive
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|SocketAddress
name|sa
init|=
name|p
operator|.
name|getSocketAddress
argument_list|()
decl_stmt|;
name|String
name|incoming
init|=
operator|new
name|String
argument_list|(
name|p
operator|.
name|getData
argument_list|()
argument_list|,
literal|0
argument_list|,
name|p
operator|.
name|getLength
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|int
name|idx
init|=
name|incoming
operator|.
name|indexOf
argument_list|(
literal|"MessageID>"
argument_list|)
decl_stmt|;
name|incoming
operator|=
name|incoming
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|10
argument_list|)
expr_stmt|;
name|idx
operator|=
name|incoming
operator|.
name|indexOf
argument_list|(
literal|"</"
argument_list|)
expr_stmt|;
name|incoming
operator|=
name|incoming
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|1
init|;
name|x
operator|<
literal|4
condition|;
name|x
operator|++
control|)
block|{
name|InputStream
name|ins
init|=
name|WSDiscoveryClientTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"msg"
operator|+
name|x
operator|+
literal|".xml"
argument_list|)
decl_stmt|;
name|String
name|msg
init|=
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|ins
argument_list|)
decl_stmt|;
name|msg
operator|=
name|msg
operator|.
name|replace
argument_list|(
literal|"urn:uuid:883d0d53-92aa-4066-9b6f-9eadb1832366"
argument_list|,
name|incoming
argument_list|)
expr_stmt|;
name|byte
name|out
index|[]
init|=
name|msg
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|DatagramPacket
name|outp
init|=
operator|new
name|DatagramPacket
argument_list|(
name|out
argument_list|,
literal|0
argument_list|,
name|out
operator|.
name|length
argument_list|,
name|sa
argument_list|)
decl_stmt|;
name|s
operator|.
name|send
argument_list|(
name|outp
argument_list|)
expr_stmt|;
block|}
name|s
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createBus
argument_list|()
decl_stmt|;
operator|new
name|LoggingFeature
argument_list|()
operator|.
name|initialize
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|WSDiscoveryClient
name|c
init|=
operator|new
name|WSDiscoveryClient
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|c
operator|.
name|setVersion10
argument_list|()
expr_stmt|;
name|c
operator|.
name|setAddress
argument_list|(
literal|"soap.udp://239.255.255.250:"
operator|+
name|PORT
argument_list|)
expr_stmt|;
name|ProbeType
name|pt
init|=
operator|new
name|ProbeType
argument_list|()
decl_stmt|;
name|ScopesType
name|scopes
init|=
operator|new
name|ScopesType
argument_list|()
decl_stmt|;
name|pt
operator|.
name|setScopes
argument_list|(
name|scopes
argument_list|)
expr_stmt|;
name|ProbeMatchesType
name|pmts
init|=
name|c
operator|.
name|probe
argument_list|(
name|pt
argument_list|,
literal|1000
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|pmts
operator|.
name|getProbeMatch
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|c
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
comment|//this is a standalone test
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|arg
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
name|Endpoint
name|ep
init|=
name|Endpoint
operator|.
name|publish
argument_list|(
literal|"http://localhost:51919/Foo/Snarf"
argument_list|,
operator|new
name|FooImpl
argument_list|()
argument_list|)
decl_stmt|;
name|WSDiscoveryServiceImpl
name|service
init|=
operator|new
name|WSDiscoveryServiceImpl
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|service
operator|.
name|startup
argument_list|()
expr_stmt|;
comment|//this service will just generate an error.  However, the probes should still
comment|//work to probe the above stuff.
name|WSDiscoveryServiceImpl
name|s2
init|=
operator|new
name|WSDiscoveryServiceImpl
argument_list|()
block|{
specifier|public
name|ProbeMatchesType
name|handleProbe
parameter_list|(
name|ProbeType
name|pt
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error!!!"
argument_list|)
throw|;
block|}
block|}
decl_stmt|;
name|s2
operator|.
name|startup
argument_list|()
expr_stmt|;
name|HelloType
name|h
init|=
name|service
operator|.
name|register
argument_list|(
name|ep
operator|.
name|getEndpointReference
argument_list|()
argument_list|)
decl_stmt|;
name|bus
operator|=
name|BusFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createBus
argument_list|()
expr_stmt|;
operator|new
name|LoggingFeature
argument_list|()
operator|.
name|initialize
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|WSDiscoveryClient
name|c
init|=
operator|new
name|WSDiscoveryClient
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|c
operator|.
name|setVersion10
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|ProbeType
name|pt
init|=
operator|new
name|ProbeType
argument_list|()
decl_stmt|;
name|ScopesType
name|scopes
init|=
operator|new
name|ScopesType
argument_list|()
decl_stmt|;
name|pt
operator|.
name|setScopes
argument_list|(
name|scopes
argument_list|)
expr_stmt|;
name|ProbeMatchesType
name|pmts
init|=
name|c
operator|.
name|probe
argument_list|(
name|pt
argument_list|,
literal|1000
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"2"
argument_list|)
expr_stmt|;
if|if
condition|(
name|pmts
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ProbeMatchType
name|pmt
range|:
name|pmts
operator|.
name|getProbeMatch
argument_list|()
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Found "
operator|+
name|pmt
operator|.
name|getEndpointReference
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|pmt
operator|.
name|getTypes
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|pmt
operator|.
name|getXAddrs
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|pmts
operator|.
name|getProbeMatch
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
name|pmts
operator|=
name|c
operator|.
name|probe
argument_list|(
name|pt
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Size:"
operator|+
name|pmts
operator|.
name|getProbeMatch
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"3"
argument_list|)
expr_stmt|;
name|W3CEndpointReference
name|ref
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|pmts
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ProbeMatchType
name|pmt
range|:
name|pmts
operator|.
name|getProbeMatch
argument_list|()
control|)
block|{
name|ref
operator|=
name|pmt
operator|.
name|getEndpointReference
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Found "
operator|+
name|pmt
operator|.
name|getEndpointReference
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|pmt
operator|.
name|getTypes
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|pmt
operator|.
name|getXAddrs
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|ResolveMatchType
name|rmt
init|=
name|c
operator|.
name|resolve
argument_list|(
name|ref
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Resolved "
operator|+
name|rmt
operator|.
name|getEndpointReference
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|rmt
operator|.
name|getTypes
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|rmt
operator|.
name|getXAddrs
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|unregister
argument_list|(
name|h
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"4"
argument_list|)
expr_stmt|;
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|WebService
specifier|public
specifier|static
class|class
name|FooImpl
block|{
annotation|@
name|WebMethod
specifier|public
name|int
name|echo
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|i
return|;
block|}
block|}
block|}
end_class

end_unit

