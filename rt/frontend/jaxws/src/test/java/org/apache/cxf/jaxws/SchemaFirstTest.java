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
name|jaxws
package|;
end_package

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
name|List
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
name|Document
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
name|endpoint
operator|.
name|Server
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
name|service
operator|.
name|EchoFoo
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
name|SchemaFirstTest
extends|extends
name|AbstractJaxWsTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsServerFactoryBean
name|svr
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|svr
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|svr
operator|.
name|setServiceBean
argument_list|(
operator|new
name|EchoFoo
argument_list|()
argument_list|)
expr_stmt|;
name|svr
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:9000/hello"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|schemas
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|schemas
operator|.
name|add
argument_list|(
literal|"/org/apache/cxf/jaxws/service/echoFoo.xsd"
argument_list|)
expr_stmt|;
name|svr
operator|.
name|setSchemaLocations
argument_list|(
name|schemas
argument_list|)
expr_stmt|;
name|Server
name|server
init|=
name|svr
operator|.
name|create
argument_list|()
decl_stmt|;
name|Document
name|d
init|=
name|getWSDLDocument
argument_list|(
name|server
argument_list|)
decl_stmt|;
comment|// XmlSchema still isn't preserving all the extra info...
name|assertValid
argument_list|(
literal|"//xsd:complexType[@name='foo']/xsd:sequence"
argument_list|,
name|d
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

