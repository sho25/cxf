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
name|type_test
operator|.
name|xml
package|;
end_package

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
name|systest
operator|.
name|type_test
operator|.
name|TypeTestImpl
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
name|AbstractBusTestServerBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|type_test
operator|.
name|xml
operator|.
name|TypeTestPortType
import|;
end_import

begin_class
specifier|public
class|class
name|XMLServerImpl
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|SpringBusFactory
name|sf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|sf
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/systest/type_test/databinding-schema-validation.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|implementor
init|=
operator|new
name|XMLTypeTestImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:9008/XMLService/XMLPort/"
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
block|{
try|try
block|{
name|XMLServerImpl
name|s
init|=
operator|new
name|XMLServerImpl
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
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"XMLService"
argument_list|,
name|portName
operator|=
literal|"XMLPort"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.type_test.xml.TypeTestPortType"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/type_test/xml"
argument_list|,
name|wsdlLocation
operator|=
literal|"testutils/type_test/type_test_xml.wsdl"
argument_list|)
annotation|@
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingType
argument_list|(
name|value
operator|=
literal|"http://cxf.apache.org/bindings/xmlformat"
argument_list|)
class|class
name|XMLTypeTestImpl
extends|extends
name|TypeTestImpl
implements|implements
name|TypeTestPortType
block|{     }
block|}
end_class

end_unit

