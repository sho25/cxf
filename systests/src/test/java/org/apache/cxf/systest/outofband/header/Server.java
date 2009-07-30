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

begin_comment
comment|//import org.apache.cxf.BusFactory;
end_comment

begin_comment
comment|//import org.apache.cxf.bus.spring.SpringBusFactory;
end_comment

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
name|AbstractBusTestServerBase
import|;
end_import

begin_class
specifier|public
class|class
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
name|Bus
name|bus
decl_stmt|;
specifier|protected
name|void
name|run
parameter_list|()
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
comment|//        // Create bus
comment|//        SpringBusFactory bf = new SpringBusFactory();
comment|//        bus = bf.createBus(OOBHeaderTest.CONFIG_FILE);
comment|//        BusFactory.setDefaultBus(bus);
comment|//        OOBHeaderTest.registerOutOfBandHeaders(bus);
comment|// Register expected Headers (namespace, element and class type mapping)
name|Object
name|implementor
init|=
operator|new
name|OOBHdrServiceImpl
argument_list|()
decl_stmt|;
name|Endpoint
name|ep
init|=
name|Endpoint
operator|.
name|create
argument_list|(
name|implementor
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|WSDL_SERVICE
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_doc_lit_bare"
argument_list|,
literal|"SOAPService"
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|WSDL_PORT
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_doc_lit_bare"
argument_list|,
literal|"SoapPort"
argument_list|)
argument_list|)
expr_stmt|;
name|ep
operator|.
name|setProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|ep
operator|.
name|publish
argument_list|(
literal|"http://localhost:9107/SOAPDocLitBareService/SoapPort"
argument_list|)
expr_stmt|;
name|ep
operator|=
name|Endpoint
operator|.
name|create
argument_list|(
name|implementor
argument_list|)
expr_stmt|;
name|props
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|WSDL_SERVICE
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_doc_lit_bare"
argument_list|,
literal|"SOAPService"
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|WSDL_PORT
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_doc_lit_bare"
argument_list|,
literal|"SoapPort"
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"endpoint-processes-headers"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|ep
operator|.
name|setProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|ep
operator|.
name|publish
argument_list|(
literal|"http://localhost:9107/SOAPDocLitBareService/SoapPortNoHeader"
argument_list|)
expr_stmt|;
name|ep
operator|=
name|Endpoint
operator|.
name|create
argument_list|(
name|implementor
argument_list|)
expr_stmt|;
name|props
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|WSDL_SERVICE
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_doc_lit_bare"
argument_list|,
literal|"SOAPService"
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|WSDL_PORT
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_doc_lit_bare"
argument_list|,
literal|"SoapPort"
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"endpoint-processes-headers"
argument_list|,
literal|"{http://cxf.apache.org/outofband/Header}outofbandHeader"
argument_list|)
expr_stmt|;
name|ep
operator|.
name|setProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|ep
operator|.
name|publish
argument_list|(
literal|"http://localhost:9107/SOAPDocLitBareService/SoapPortHeader"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
try|try
block|{
name|Server
name|s
init|=
operator|new
name|Server
argument_list|()
decl_stmt|;
name|s
operator|.
name|start
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
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"done!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

