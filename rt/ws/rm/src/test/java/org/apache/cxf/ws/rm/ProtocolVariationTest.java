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
name|ws
operator|.
name|rm
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
name|ws
operator|.
name|addressing
operator|.
name|Names
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
name|ws
operator|.
name|addressing
operator|.
name|VersionTransformer
operator|.
name|Names200408
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
name|assertNull
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|ProtocolVariationTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testFindVariant
parameter_list|()
block|{
comment|// valid combinations
name|assertNotNull
argument_list|(
name|ProtocolVariation
operator|.
name|findVariant
argument_list|(
name|RM10Constants
operator|.
name|NAMESPACE_URI
argument_list|,
name|Names200408
operator|.
name|WSA_NAMESPACE_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ProtocolVariation
operator|.
name|findVariant
argument_list|(
name|RM10Constants
operator|.
name|NAMESPACE_URI
argument_list|,
name|Names
operator|.
name|WSA_NAMESPACE_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ProtocolVariation
operator|.
name|findVariant
argument_list|(
name|RM11Constants
operator|.
name|NAMESPACE_URI
argument_list|,
name|Names
operator|.
name|WSA_NAMESPACE_NAME
argument_list|)
argument_list|)
expr_stmt|;
comment|// invalid combinations
name|assertNull
argument_list|(
name|ProtocolVariation
operator|.
name|findVariant
argument_list|(
name|RM11Constants
operator|.
name|NAMESPACE_URI
argument_list|,
name|Names200408
operator|.
name|WSA_NAMESPACE_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

