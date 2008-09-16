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
name|basicDOCBare
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|Method
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
name|WebParam
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
name|Holder
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
name|DOCBareClientServerTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
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
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
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
name|String
name|response
init|=
name|putLastTradedPrice
operator|.
name|bareNoParam
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"testResponse"
argument_list|,
name|response
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
name|Holder
argument_list|<
name|TradePriceData
argument_list|>
name|holder
init|=
operator|new
name|Holder
argument_list|<
name|TradePriceData
argument_list|>
argument_list|(
name|priceData
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|putLastTradedPrice
operator|.
name|sayHi
argument_list|(
name|holder
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4.5f
argument_list|,
name|holder
operator|.
name|value
operator|.
name|getTickerPrice
argument_list|()
argument_list|,
literal|0.01
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"APACHE"
argument_list|,
name|holder
operator|.
name|value
operator|.
name|getTickerSymbol
argument_list|()
argument_list|)
expr_stmt|;
name|putLastTradedPrice
operator|.
name|putLastTradedPrice
argument_list|(
name|priceData
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAnnotation
parameter_list|()
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|PutLastTradedPricePortType
argument_list|>
name|claz
init|=
name|PutLastTradedPricePortType
operator|.
name|class
decl_stmt|;
name|TradePriceData
name|priceData
init|=
operator|new
name|TradePriceData
argument_list|()
decl_stmt|;
name|Holder
argument_list|<
name|TradePriceData
argument_list|>
name|holder
init|=
operator|new
name|Holder
argument_list|<
name|TradePriceData
argument_list|>
argument_list|(
name|priceData
argument_list|)
decl_stmt|;
name|Method
name|method
init|=
name|claz
operator|.
name|getMethod
argument_list|(
literal|"sayHi"
argument_list|,
name|holder
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Can not find SayHi method in generated class "
argument_list|,
name|method
argument_list|)
expr_stmt|;
name|Annotation
name|ann
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|WebMethod
operator|.
name|class
argument_list|)
decl_stmt|;
name|WebMethod
name|webMethod
init|=
operator|(
name|WebMethod
operator|)
name|ann
decl_stmt|;
name|assertEquals
argument_list|(
name|webMethod
operator|.
name|operationName
argument_list|()
argument_list|,
literal|"SayHi"
argument_list|)
expr_stmt|;
name|Annotation
index|[]
index|[]
name|paraAnns
init|=
name|method
operator|.
name|getParameterAnnotations
argument_list|()
decl_stmt|;
for|for
control|(
name|Annotation
index|[]
name|paraType
range|:
name|paraAnns
control|)
block|{
for|for
control|(
name|Annotation
name|an
range|:
name|paraType
control|)
block|{
if|if
condition|(
name|an
operator|.
name|annotationType
argument_list|()
operator|==
name|WebParam
operator|.
name|class
condition|)
block|{
name|WebParam
name|webParam
init|=
operator|(
name|WebParam
operator|)
name|an
decl_stmt|;
name|assertNotSame
argument_list|(
literal|""
argument_list|,
name|webParam
operator|.
name|targetNamespace
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNillableParameter
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
name|port
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
name|String
name|result
init|=
name|port
operator|.
name|nillableParameter
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

