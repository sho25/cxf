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
name|common
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|assertEquals
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
name|PropertiesLoaderUtilsTest
block|{
name|Properties
name|properties
decl_stmt|;
name|String
name|soapBindingFactory
init|=
literal|"org.apache.cxf.bindings.soap.SOAPBindingFactory"
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|properties
operator|=
name|PropertiesLoaderUtils
operator|.
name|loadAllProperties
argument_list|(
literal|"org/apache/cxf/common/util/resources/bindings.properties.xml"
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadBindings
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|soapBindingFactory
argument_list|,
name|properties
operator|.
name|getProperty
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|soapBindingFactory
argument_list|,
name|properties
operator|.
name|getProperty
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/http"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

