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
name|BusException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_class
specifier|public
class|class
name|SchemaFirstXmlConfigTest
extends|extends
name|AbstractJaxWsTest
block|{
specifier|private
name|ClassPathXmlApplicationContext
name|ctx
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|Bus
name|createBus
parameter_list|()
throws|throws
name|BusException
block|{
name|ctx
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"classpath:org/apache/cxf/jaxws/schemaFirst.xml"
block|}
argument_list|)
expr_stmt|;
return|return
operator|(
name|Bus
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"cxf"
argument_list|)
return|;
block|}
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
name|serverFB
init|=
operator|(
name|JaxWsServerFactoryBean
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"helloServer"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|serverFB
operator|.
name|getServer
argument_list|()
argument_list|)
expr_stmt|;
name|Document
name|d
init|=
name|getWSDLDocument
argument_list|(
name|serverFB
operator|.
name|getServer
argument_list|()
argument_list|)
decl_stmt|;
comment|//XMLUtils.printDOM(d);
comment|// XmlSchema still isn't preserving all the extra info...
name|assertValid
argument_list|(
literal|"//xsd:complexType[@name='foo']/xsd:sequence"
argument_list|,
name|d
argument_list|)
expr_stmt|;
name|EndpointImpl
name|ep
init|=
operator|(
name|EndpointImpl
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"helloEndpoint"
argument_list|)
decl_stmt|;
name|d
operator|=
name|getWSDLDocument
argument_list|(
name|ep
operator|.
name|getServer
argument_list|()
argument_list|)
expr_stmt|;
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

