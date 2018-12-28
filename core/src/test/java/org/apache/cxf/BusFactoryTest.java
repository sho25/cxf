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
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|BusFactoryTest
extends|extends
name|Assert
block|{
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|System
operator|.
name|clearProperty
argument_list|(
name|BusFactory
operator|.
name|BUS_FACTORY_PROPERTY_NAME
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetInstance
parameter_list|()
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|BusFactory
operator|.
name|BUS_FACTORY_PROPERTY_NAME
argument_list|,
name|TestBusFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|BusFactory
name|factory
init|=
name|BusFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|factory
operator|instanceof
name|TestBusFactory
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|TestBusFactory
extends|extends
name|BusFactory
block|{
specifier|public
name|Bus
name|createBus
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

