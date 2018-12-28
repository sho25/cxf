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
name|jaxb
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|DataBindingMarshallerPropertiesTest
extends|extends
name|TestBase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testInitializeUnmarshallerProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBDataBinding
name|db
init|=
operator|new
name|JAXBDataBinding
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|unmarshallerProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|unmarshallerProperties
operator|.
name|put
argument_list|(
literal|"someproperty"
argument_list|,
literal|"somevalue"
argument_list|)
expr_stmt|;
name|db
operator|.
name|setUnmarshallerProperties
argument_list|(
name|unmarshallerProperties
argument_list|)
expr_stmt|;
name|db
operator|.
name|initialize
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"somevalue"
operator|.
name|equals
argument_list|(
name|db
operator|.
name|getUnmarshallerProperties
argument_list|()
operator|.
name|get
argument_list|(
literal|"someproperty"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

