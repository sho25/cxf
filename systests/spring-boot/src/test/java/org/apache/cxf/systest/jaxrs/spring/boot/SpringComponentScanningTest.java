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
name|jaxrs
operator|.
name|spring
operator|.
name|boot
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
name|jaxrs
operator|.
name|openapi
operator|.
name|OpenApiFeature
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
name|jaxrs
operator|.
name|spring
operator|.
name|AbstractSpringComponentScanServer
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
name|jaxrs
operator|.
name|validation
operator|.
name|JAXRSBeanValidationFeature
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
name|jaxrs
operator|.
name|resources
operator|.
name|Library
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|EnableAutoConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|context
operator|.
name|SpringBootTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|context
operator|.
name|SpringBootTest
operator|.
name|WebEnvironment
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
name|annotation
operator|.
name|ComponentScan
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
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ActiveProfiles
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringRunner
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertThat
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringRunner
operator|.
name|class
argument_list|)
annotation|@
name|SpringBootTest
argument_list|(
name|webEnvironment
operator|=
name|WebEnvironment
operator|.
name|RANDOM_PORT
argument_list|,
name|classes
operator|=
name|SpringComponentScanningTest
operator|.
name|TestConfig
operator|.
name|class
argument_list|)
annotation|@
name|ActiveProfiles
argument_list|(
literal|"component-scan"
argument_list|)
specifier|public
class|class
name|SpringComponentScanningTest
block|{
annotation|@
name|Autowired
specifier|private
name|AbstractSpringComponentScanServer
name|scanner
decl_stmt|;
annotation|@
name|Configuration
annotation|@
name|EnableAutoConfiguration
annotation|@
name|ComponentScan
argument_list|(
name|basePackageClasses
operator|=
name|Library
operator|.
name|class
argument_list|)
specifier|static
class|class
name|TestConfig
block|{     }
annotation|@
name|Test
specifier|public
name|void
name|testCxfComponentScan
parameter_list|()
block|{
comment|// The component scanner only looks for CXF's @Provider annotations,
comment|// not JAX-RS Features/@Provider.
name|assertThat
argument_list|(
name|scanner
operator|.
name|getFeatures
argument_list|()
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
operator|.
name|hasOnlyElementsOfTypes
argument_list|(
name|OpenApiFeature
operator|.
name|class
argument_list|,
name|JAXRSBeanValidationFeature
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|scanner
operator|.
name|getOutInterceptors
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|scanner
operator|.
name|getInInterceptors
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|scanner
operator|.
name|getOutFaultInterceptors
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|scanner
operator|.
name|getInFaultInterceptors
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

