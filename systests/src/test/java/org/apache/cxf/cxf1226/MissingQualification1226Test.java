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
name|cxf1226
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
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|NodeList
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
name|EndpointImpl
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
name|test
operator|.
name|AbstractCXFSpringTest
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
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|GenericApplicationContext
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|MissingQualification1226Test
extends|extends
name|AbstractCXFSpringTest
block|{
comment|/**      * @throws Exception      */
specifier|public
name|MissingQualification1226Test
parameter_list|()
throws|throws
name|Exception
block|{     }
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|protected
name|void
name|additionalSpringConfiguration
parameter_list|(
name|GenericApplicationContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{     }
annotation|@
name|Test
specifier|public
name|void
name|lookForMissingNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|EndpointImpl
name|endpoint
init|=
operator|(
name|EndpointImpl
operator|)
name|getBean
argument_list|(
name|EndpointImpl
operator|.
name|class
argument_list|,
literal|"helloWorld"
argument_list|)
decl_stmt|;
name|Document
name|d
init|=
name|getWSDLDocument
argument_list|(
name|endpoint
operator|.
name|getServer
argument_list|()
argument_list|)
decl_stmt|;
name|NodeList
name|schemas
init|=
name|assertValid
argument_list|(
literal|"//xsd:schema[@targetNamespace='http://nstest.helloworld']"
argument_list|,
name|d
argument_list|)
decl_stmt|;
name|Element
name|schemaElement
init|=
operator|(
name|Element
operator|)
name|schemas
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|ef
init|=
name|schemaElement
operator|.
name|getAttribute
argument_list|(
literal|"elementFormDefault"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"qualified"
argument_list|,
name|ef
argument_list|)
expr_stmt|;
block|}
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|protected
name|String
index|[]
name|getConfigLocations
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"classpath:/org/apache/cxf/cxf1226/beans.xml"
block|}
return|;
block|}
block|}
end_class

end_unit

