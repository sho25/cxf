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
name|policy
operator|.
name|blueprint
package|;
end_package

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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|PolicyBPHandlerTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetSchemaLocation
parameter_list|()
block|{
name|PolicyBPHandler
name|handler
init|=
operator|new
name|PolicyBPHandler
argument_list|()
decl_stmt|;
comment|//REVIST is this first one correct? the referenced schema has namespace http://cxf.apache.org/blueprint/policy
name|assertNotNull
argument_list|(
name|handler
operator|.
name|getSchemaLocation
argument_list|(
literal|"http://cxf.apache.org/policy"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|handler
operator|.
name|getSchemaLocation
argument_list|(
literal|"http://www.w3.org/ns/ws-policy"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|handler
operator|.
name|getSchemaLocation
argument_list|(
literal|"http://www.w3.org/2006/07/ws-policy"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|handler
operator|.
name|getSchemaLocation
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2004/09/policy"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|handler
operator|.
name|getSchemaLocation
argument_list|(
literal|"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|handler
operator|.
name|getSchemaLocation
argument_list|(
literal|"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|handler
operator|.
name|getSchemaLocation
argument_list|(
literal|"http://www.w3.org/2000/09/xmldsig#"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|handler
operator|.
name|getSchemaLocation
argument_list|(
literal|"http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200702"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

