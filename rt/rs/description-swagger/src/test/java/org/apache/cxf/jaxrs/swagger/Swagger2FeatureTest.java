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
name|jaxrs
operator|.
name|swagger
package|;
end_package

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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|Swagger2FeatureTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSetBasePathByAddress
parameter_list|()
block|{
name|Swagger2Feature
name|f
init|=
operator|new
name|Swagger2Feature
argument_list|()
decl_stmt|;
name|f
operator|.
name|setBasePathByAddress
argument_list|(
literal|"http://localhost:8080/foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/foo"
argument_list|,
name|f
operator|.
name|getBasePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"localhost:8080"
argument_list|,
name|f
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|unsetBasePath
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|f
operator|.
name|setBasePathByAddress
argument_list|(
literal|"http://localhost/foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/foo"
argument_list|,
name|f
operator|.
name|getBasePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"localhost"
argument_list|,
name|f
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|unsetBasePath
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|f
operator|.
name|setBasePathByAddress
argument_list|(
literal|"/foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/foo"
argument_list|,
name|f
operator|.
name|getBasePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|f
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|unsetBasePath
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|unsetBasePath
parameter_list|(
name|Swagger2Feature
name|f
parameter_list|)
block|{
name|f
operator|.
name|setBasePath
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|f
operator|.
name|setHost
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

