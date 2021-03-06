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
name|endpoint
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
name|bus
operator|.
name|extension
operator|.
name|ExtensionManagerBus
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
name|service
operator|.
name|Service
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
name|service
operator|.
name|ServiceImpl
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|assertNotEquals
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|EndpointImplTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testEqualsAndHashCode
parameter_list|()
throws|throws
name|Exception
block|{
name|Bus
name|bus
init|=
operator|new
name|ExtensionManagerBus
argument_list|()
decl_stmt|;
name|Service
name|svc
init|=
operator|new
name|ServiceImpl
argument_list|()
decl_stmt|;
name|EndpointInfo
name|ei
init|=
operator|new
name|EndpointInfo
argument_list|()
decl_stmt|;
name|ei
operator|.
name|setAddress
argument_list|(
literal|"http://nowhere.com/bar/foo"
argument_list|)
expr_stmt|;
name|EndpointInfo
name|ei2
init|=
operator|new
name|EndpointInfo
argument_list|()
decl_stmt|;
name|ei2
operator|.
name|setAddress
argument_list|(
literal|"http://nowhere.com/foo/bar"
argument_list|)
expr_stmt|;
name|Endpoint
name|ep
init|=
operator|new
name|EndpointImpl
argument_list|(
name|bus
argument_list|,
name|svc
argument_list|,
name|ei
argument_list|)
decl_stmt|;
name|Endpoint
name|ep1
init|=
operator|new
name|EndpointImpl
argument_list|(
name|bus
argument_list|,
name|svc
argument_list|,
name|ei
argument_list|)
decl_stmt|;
name|Endpoint
name|ep2
init|=
operator|new
name|EndpointImpl
argument_list|(
name|bus
argument_list|,
name|svc
argument_list|,
name|ei2
argument_list|)
decl_stmt|;
name|int
name|hashcode
init|=
name|ep
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|int
name|hashcode1
init|=
name|ep1
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|int
name|hashcode2
init|=
name|ep2
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"hashcodes must be equal"
argument_list|,
name|hashcode
argument_list|,
name|hashcode1
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
literal|"hashcodes must not be equal"
argument_list|,
name|hashcode
argument_list|,
name|hashcode2
argument_list|)
expr_stmt|;
comment|// assertEquals("reflexivity violated", ep, ep);
name|assertNotEquals
argument_list|(
literal|"two objects must not be equal"
argument_list|,
name|ep
argument_list|,
name|ep1
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
literal|"two objects must not be equal"
argument_list|,
name|ep
argument_list|,
name|ep2
argument_list|)
expr_stmt|;
name|ep
operator|.
name|put
argument_list|(
literal|"custom"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hashcode must remain equal"
argument_list|,
name|hashcode
argument_list|,
name|ep
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

