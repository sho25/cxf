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
name|lifecycle
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
name|Customer
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
name|message
operator|.
name|Message
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
name|message
operator|.
name|MessageImpl
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

begin_class
specifier|public
class|class
name|PerRequestResourceProviderTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetInstance
parameter_list|()
block|{
name|PerRequestResourceProvider
name|rp
init|=
operator|new
name|PerRequestResourceProvider
argument_list|(
name|Customer
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|QUERY_STRING
argument_list|,
literal|"a=aValue"
argument_list|)
expr_stmt|;
name|Customer
name|c
init|=
operator|(
name|Customer
operator|)
name|rp
operator|.
name|getInstance
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|c
operator|.
name|getUriInfo
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"aValue"
argument_list|,
name|c
operator|.
name|getQueryParam
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|c
operator|.
name|isPostConstuctCalled
argument_list|()
argument_list|)
expr_stmt|;
name|rp
operator|.
name|releaseInstance
argument_list|(
name|message
argument_list|,
name|c
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|c
operator|.
name|isPreDestroyCalled
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

