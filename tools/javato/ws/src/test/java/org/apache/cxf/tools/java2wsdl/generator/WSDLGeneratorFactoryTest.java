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
name|tools
operator|.
name|java2wsdl
operator|.
name|generator
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|java2wsdl
operator|.
name|generator
operator|.
name|wsdl11
operator|.
name|WSDL11Generator
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
name|wsdl
operator|.
name|WSDLConstants
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|WSDLGeneratorFactoryTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testNewWSDL11Generator
parameter_list|()
block|{
name|WSDLGeneratorFactory
name|factory
init|=
operator|new
name|WSDLGeneratorFactory
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setWSDLVersion
argument_list|(
name|WSDLConstants
operator|.
name|WSDLVersion
operator|.
name|WSDL11
argument_list|)
expr_stmt|;
name|AbstractGenerator
argument_list|<
name|?
argument_list|>
name|generator
init|=
name|factory
operator|.
name|newGenerator
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|generator
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|generator
operator|instanceof
name|WSDL11Generator
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

