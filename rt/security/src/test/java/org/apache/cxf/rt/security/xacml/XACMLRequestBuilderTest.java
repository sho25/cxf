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
name|rt
operator|.
name|security
operator|.
name|xacml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|opensaml
operator|.
name|xacml
operator|.
name|ctx
operator|.
name|RequestType
import|;
end_import

begin_comment
comment|/**  * Some unit tests to create a XACML Request via the XACMLRequestBuilder interface.  */
end_comment

begin_class
specifier|public
class|class
name|XACMLRequestBuilderTest
extends|extends
name|org
operator|.
name|junit
operator|.
name|Assert
block|{
static|static
block|{
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|OpenSAMLUtil
operator|.
name|initSamlEngine
argument_list|()
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testXACMLRequestBuilder
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Mock up a request
name|Principal
name|principal
init|=
operator|new
name|Principal
argument_list|()
block|{
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"alice"
return|;
block|}
block|}
decl_stmt|;
name|String
name|operation
init|=
literal|"{http://www.example.org/contract/DoubleIt}DoubleIt"
decl_stmt|;
name|MessageImpl
name|msg
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_OPERATION
argument_list|,
name|operation
argument_list|)
expr_stmt|;
name|XACMLRequestBuilder
name|builder
init|=
operator|new
name|DefaultXACMLRequestBuilder
argument_list|()
decl_stmt|;
name|RequestType
name|request
init|=
name|builder
operator|.
name|createRequest
argument_list|(
name|principal
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"manager"
argument_list|)
argument_list|,
name|msg
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testResource
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Mock up a request
name|Principal
name|principal
init|=
operator|new
name|Principal
argument_list|()
block|{
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"alice"
return|;
block|}
block|}
decl_stmt|;
name|String
name|operation
init|=
literal|"{http://www.example.org/contract/DoubleIt}DoubleIt"
decl_stmt|;
name|MessageImpl
name|msg
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_OPERATION
argument_list|,
name|operation
argument_list|)
expr_stmt|;
name|XACMLRequestBuilder
name|builder
init|=
operator|new
name|DefaultXACMLRequestBuilder
argument_list|()
decl_stmt|;
name|RequestType
name|request
init|=
name|builder
operator|.
name|createRequest
argument_list|(
name|principal
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"manager"
argument_list|)
argument_list|,
name|msg
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|operation
argument_list|,
name|builder
operator|.
name|getResource
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|operation
operator|=
literal|"user/list.json"
expr_stmt|;
name|msg
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|Message
operator|.
name|REQUEST_URI
argument_list|,
name|operation
argument_list|)
expr_stmt|;
name|request
operator|=
name|builder
operator|.
name|createRequest
argument_list|(
name|principal
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"manager"
argument_list|)
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|operation
argument_list|,
name|builder
operator|.
name|getResource
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|operation
operator|=
literal|"https://localhost:8080/user/list.json"
expr_stmt|;
name|msg
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|Message
operator|.
name|REQUEST_URL
argument_list|,
name|operation
argument_list|)
expr_stmt|;
operator|(
operator|(
name|DefaultXACMLRequestBuilder
operator|)
name|builder
operator|)
operator|.
name|setSendFullRequestURL
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|request
operator|=
name|builder
operator|.
name|createRequest
argument_list|(
name|principal
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"manager"
argument_list|)
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|operation
argument_list|,
name|builder
operator|.
name|getResource
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testAction
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Mock up a request
name|Principal
name|principal
init|=
operator|new
name|Principal
argument_list|()
block|{
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"alice"
return|;
block|}
block|}
decl_stmt|;
name|String
name|operation
init|=
literal|"{http://www.example.org/contract/DoubleIt}DoubleIt"
decl_stmt|;
name|MessageImpl
name|msg
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_OPERATION
argument_list|,
name|operation
argument_list|)
expr_stmt|;
name|XACMLRequestBuilder
name|builder
init|=
operator|new
name|DefaultXACMLRequestBuilder
argument_list|()
decl_stmt|;
name|RequestType
name|request
init|=
name|builder
operator|.
name|createRequest
argument_list|(
name|principal
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"manager"
argument_list|)
argument_list|,
name|msg
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|String
name|action
init|=
name|request
operator|.
name|getAction
argument_list|()
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAttributeValues
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|action
argument_list|,
literal|"execute"
argument_list|)
expr_stmt|;
operator|(
operator|(
name|DefaultXACMLRequestBuilder
operator|)
name|builder
operator|)
operator|.
name|setAction
argument_list|(
literal|"write"
argument_list|)
expr_stmt|;
name|request
operator|=
name|builder
operator|.
name|createRequest
argument_list|(
name|principal
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"manager"
argument_list|)
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|action
operator|=
name|request
operator|.
name|getAction
argument_list|()
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAttributeValues
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getValue
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|action
argument_list|,
literal|"write"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testEnvironment
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Mock up a request
name|Principal
name|principal
init|=
operator|new
name|Principal
argument_list|()
block|{
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"alice"
return|;
block|}
block|}
decl_stmt|;
name|String
name|operation
init|=
literal|"{http://www.example.org/contract/DoubleIt}DoubleIt"
decl_stmt|;
name|MessageImpl
name|msg
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_OPERATION
argument_list|,
name|operation
argument_list|)
expr_stmt|;
name|XACMLRequestBuilder
name|builder
init|=
operator|new
name|DefaultXACMLRequestBuilder
argument_list|()
decl_stmt|;
name|RequestType
name|request
init|=
name|builder
operator|.
name|createRequest
argument_list|(
name|principal
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"manager"
argument_list|)
argument_list|,
name|msg
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|request
operator|.
name|getEnvironment
argument_list|()
operator|.
name|getAttributes
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|DefaultXACMLRequestBuilder
operator|)
name|builder
operator|)
operator|.
name|setSendDateTime
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|request
operator|=
name|builder
operator|.
name|createRequest
argument_list|(
name|principal
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"manager"
argument_list|)
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|request
operator|.
name|getEnvironment
argument_list|()
operator|.
name|getAttributes
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

