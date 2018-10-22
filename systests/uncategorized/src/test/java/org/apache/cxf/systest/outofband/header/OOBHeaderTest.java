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
name|outofband
operator|.
name|header
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
name|InvocationHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Proxy
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
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
name|SoapHeader
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
name|headers
operator|.
name|Header
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
name|jaxb
operator|.
name|JAXBDataBinding
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
name|outofband
operator|.
name|header
operator|.
name|ObjectFactory
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
name|outofband
operator|.
name|header
operator|.
name|OutofBandHeader
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
name|hello_world_doc_lit_bare
operator|.
name|PutLastTradedPricePortType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_doc_lit_bare
operator|.
name|SOAPService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_doc_lit_bare
operator|.
name|types
operator|.
name|TradePriceData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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

begin_class
specifier|public
class|class
name|OOBHeaderTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|Server
operator|.
name|PORT
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONFIG_FILE
init|=
literal|"org/apache/cxf/systest/outofband/header/cxf.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TEST_HDR_NS
init|=
literal|"http://cxf.apache.org/outofband/Header"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TEST_HDR_REQUEST_ELEM
init|=
literal|"outofbandHeader"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TEST_HDR_RESPONSE_ELEM
init|=
literal|"outofbandHeader"
decl_stmt|;
specifier|private
specifier|final
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_doc_lit_bare"
argument_list|,
literal|"SOAPService"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_doc_lit_bare"
argument_list|,
literal|"SoapPort"
argument_list|)
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.apache.cxf.bus.factory"
argument_list|,
literal|"org.apache.cxf.bus.CXFBusFactory"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"cxf.config.file"
argument_list|,
literal|"org/apache/cxf/systest/outofband/header/cxf.xml"
argument_list|)
expr_stmt|;
name|createStaticBus
argument_list|(
name|CONFIG_FILE
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addOutOfBoundHeader
parameter_list|(
name|PutLastTradedPricePortType
name|portType
parameter_list|,
name|boolean
name|invalid
parameter_list|,
name|boolean
name|mu
parameter_list|)
block|{
name|InvocationHandler
name|handler
init|=
name|Proxy
operator|.
name|getInvocationHandler
argument_list|(
name|portType
argument_list|)
decl_stmt|;
name|BindingProvider
name|bp
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|handler
operator|instanceof
name|BindingProvider
condition|)
block|{
name|bp
operator|=
operator|(
name|BindingProvider
operator|)
name|handler
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
name|bp
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|OutofBandHeader
name|ob
init|=
operator|new
name|OutofBandHeader
argument_list|()
decl_stmt|;
name|ob
operator|.
name|setName
argument_list|(
literal|"testOobHeader"
argument_list|)
expr_stmt|;
name|ob
operator|.
name|setValue
argument_list|(
literal|"testOobHeaderValue"
argument_list|)
expr_stmt|;
name|ob
operator|.
name|setHdrAttribute
argument_list|(
name|invalid
condition|?
literal|"dontProcess"
else|:
literal|"testHdrAttribute"
argument_list|)
expr_stmt|;
name|SoapHeader
name|hdr
init|=
operator|new
name|SoapHeader
argument_list|(
operator|new
name|QName
argument_list|(
name|TEST_HDR_NS
argument_list|,
name|TEST_HDR_REQUEST_ELEM
argument_list|)
argument_list|,
name|ob
argument_list|,
operator|new
name|JAXBDataBinding
argument_list|(
name|ob
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|hdr
operator|.
name|setMustUnderstand
argument_list|(
name|mu
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Header
argument_list|>
name|holder
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|holder
operator|.
name|add
argument_list|(
name|hdr
argument_list|)
expr_stmt|;
comment|//Add List of headerHolders to requestContext.
name|requestContext
operator|.
name|put
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|,
name|holder
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|JAXBException
name|ex
parameter_list|)
block|{
comment|//System.out.println("failed to insert header into request context :" + ex);
block|}
block|}
specifier|private
name|void
name|checkReturnedOOBHeader
parameter_list|(
name|PutLastTradedPricePortType
name|portType
parameter_list|)
block|{
name|InvocationHandler
name|handler
init|=
name|Proxy
operator|.
name|getInvocationHandler
argument_list|(
name|portType
argument_list|)
decl_stmt|;
name|BindingProvider
name|bp
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|handler
operator|instanceof
name|BindingProvider
condition|)
block|{
name|bp
operator|=
operator|(
name|BindingProvider
operator|)
name|handler
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseContext
init|=
name|bp
operator|.
name|getResponseContext
argument_list|()
decl_stmt|;
name|OutofBandHeader
name|hdrToTest
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|oobHdr
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|responseContext
operator|.
name|get
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|)
decl_stmt|;
if|if
condition|(
name|oobHdr
operator|==
literal|null
condition|)
block|{
name|fail
argument_list|(
literal|"Should have got List of out-of-band headers .."
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"HeaderHolder list expected to conain 1 object received "
operator|+
name|oobHdr
operator|.
name|size
argument_list|()
argument_list|,
name|oobHdr
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|oobHdr
operator|!=
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|?
argument_list|>
name|iter
init|=
name|oobHdr
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|hdr
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|hdr
operator|instanceof
name|Header
condition|)
block|{
name|Header
name|hdr1
init|=
operator|(
name|Header
operator|)
name|hdr
decl_stmt|;
if|if
condition|(
name|hdr1
operator|.
name|getObject
argument_list|()
operator|instanceof
name|Node
condition|)
block|{
comment|//System.out.println("Node conains : " + hdr1.getObject().toString());
try|try
block|{
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|job
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|ObjectFactory
operator|.
name|class
argument_list|)
operator|.
name|createUnmarshaller
argument_list|()
operator|.
name|unmarshal
argument_list|(
operator|(
name|Node
operator|)
name|hdr1
operator|.
name|getObject
argument_list|()
argument_list|)
decl_stmt|;
name|hdrToTest
operator|=
operator|(
name|OutofBandHeader
operator|)
name|job
operator|.
name|getValue
argument_list|()
expr_stmt|;
comment|//                                 System.out.println("oob-hdr contains : \nname = "
comment|//                                       + hdrToTest.getName()
comment|//                                       + "  \nvalue = " + hdrToTest.getValue()
comment|//                                       + " \natribute = " + hdrToTest.getHdrAttribute());
block|}
catch|catch
parameter_list|(
name|JAXBException
name|ex
parameter_list|)
block|{
comment|//
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
name|assertNotNull
argument_list|(
literal|"out-of-band header should not be null"
argument_list|,
name|hdrToTest
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Expected out-of-band Header name testOobReturnHeaderName recevied :"
operator|+
name|hdrToTest
operator|.
name|getName
argument_list|()
argument_list|,
literal|"testOobReturnHeaderName"
operator|.
name|equals
argument_list|(
name|hdrToTest
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Expected out-of-band Header value testOobReturnHeaderValue recevied :"
operator|+
name|hdrToTest
operator|.
name|getValue
argument_list|()
argument_list|,
literal|"testOobReturnHeaderValue"
operator|.
name|equals
argument_list|(
name|hdrToTest
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Expected out-of-band Header attribute testReturnHdrAttribute recevied :"
operator|+
name|hdrToTest
operator|.
name|getHdrAttribute
argument_list|()
argument_list|,
literal|"testReturnHdrAttribute"
operator|.
name|equals
argument_list|(
name|hdrToTest
operator|.
name|getHdrAttribute
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBasicConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/doc_lit_bare.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"WSDL is null"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Service is null"
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|PutLastTradedPricePortType
name|putLastTradedPrice
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|PutLastTradedPricePortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|putLastTradedPrice
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|TradePriceData
name|priceData
init|=
operator|new
name|TradePriceData
argument_list|()
decl_stmt|;
name|priceData
operator|.
name|setTickerPrice
argument_list|(
literal|1.0f
argument_list|)
expr_stmt|;
name|priceData
operator|.
name|setTickerSymbol
argument_list|(
literal|"CELTIX"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|check
argument_list|(
literal|0
argument_list|,
name|putLastTradedPrice
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|priceData
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|check
argument_list|(
literal|1
argument_list|,
name|putLastTradedPrice
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|priceData
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|check
argument_list|(
literal|2
argument_list|,
name|putLastTradedPrice
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|priceData
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|check
argument_list|(
literal|3
argument_list|,
name|putLastTradedPrice
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|priceData
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|check
argument_list|(
literal|0
argument_list|,
name|putLastTradedPrice
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
name|priceData
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|check
argument_list|(
literal|1
argument_list|,
name|putLastTradedPrice
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
name|priceData
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|check
argument_list|(
literal|2
argument_list|,
name|putLastTradedPrice
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
name|priceData
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|check
argument_list|(
literal|3
argument_list|,
name|putLastTradedPrice
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
name|priceData
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|check
argument_list|(
literal|0
argument_list|,
name|putLastTradedPrice
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
name|priceData
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|check
argument_list|(
literal|1
argument_list|,
name|putLastTradedPrice
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
name|priceData
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|check
argument_list|(
literal|2
argument_list|,
name|putLastTradedPrice
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
name|priceData
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|check
argument_list|(
literal|4
argument_list|,
name|putLastTradedPrice
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
name|priceData
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|check
argument_list|(
literal|0
argument_list|,
name|putLastTradedPrice
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
name|priceData
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|check
argument_list|(
literal|1
argument_list|,
name|putLastTradedPrice
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
name|priceData
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|check
argument_list|(
literal|2
argument_list|,
name|putLastTradedPrice
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
name|priceData
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|check
argument_list|(
literal|4
argument_list|,
name|putLastTradedPrice
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
name|priceData
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|check
parameter_list|(
name|int
name|i
parameter_list|,
name|PutLastTradedPricePortType
name|putLastTradedPrice
parameter_list|,
name|boolean
name|invalid
parameter_list|,
name|boolean
name|mu
parameter_list|,
name|TradePriceData
name|priceData
parameter_list|)
block|{
name|String
name|address
init|=
literal|""
decl_stmt|;
switch|switch
condition|(
name|i
condition|)
block|{
case|case
literal|0
case|:
name|address
operator|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPDocLitBareService/SoapPort"
expr_stmt|;
break|break;
case|case
literal|1
case|:
name|address
operator|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPDocLitBareService/SoapPortNoHeader"
expr_stmt|;
break|break;
case|case
literal|2
case|:
name|address
operator|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPDocLitBareService/SoapPortHeader"
expr_stmt|;
break|break;
default|default:
name|address
operator|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPDocLitBareService/SoapPortHeaderProperty"
expr_stmt|;
block|}
operator|(
operator|(
name|BindingProvider
operator|)
name|putLastTradedPrice
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
name|address
argument_list|)
expr_stmt|;
name|Holder
argument_list|<
name|TradePriceData
argument_list|>
name|holder
init|=
operator|new
name|Holder
argument_list|<>
argument_list|(
name|priceData
argument_list|)
decl_stmt|;
try|try
block|{
name|addOutOfBoundHeader
argument_list|(
name|putLastTradedPrice
argument_list|,
name|invalid
argument_list|,
name|mu
argument_list|)
expr_stmt|;
name|putLastTradedPrice
operator|.
name|sayHi
argument_list|(
name|holder
argument_list|)
expr_stmt|;
name|checkReturnedOOBHeader
argument_list|(
name|putLastTradedPrice
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"MustUnderstand"
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
throw|throw
name|ex
throw|;
block|}
block|}
block|}
end_class

end_unit

