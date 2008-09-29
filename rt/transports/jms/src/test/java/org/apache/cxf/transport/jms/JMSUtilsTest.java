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
name|transport
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|JMSUtilsTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testpopulateIncomingContextNonNull
parameter_list|()
throws|throws
name|Exception
block|{
name|AddressType
name|addrType
init|=
operator|new
name|AddressType
argument_list|()
decl_stmt|;
name|JMSNamingPropertyType
name|prop
init|=
operator|new
name|JMSNamingPropertyType
argument_list|()
decl_stmt|;
name|prop
operator|.
name|setName
argument_list|(
name|Context
operator|.
name|APPLET
argument_list|)
expr_stmt|;
name|prop
operator|.
name|setValue
argument_list|(
literal|"testValue"
argument_list|)
expr_stmt|;
name|addrType
operator|.
name|getJMSNamingProperty
argument_list|()
operator|.
name|add
argument_list|(
name|prop
argument_list|)
expr_stmt|;
name|JMSNamingPropertyType
name|prop2
init|=
operator|new
name|JMSNamingPropertyType
argument_list|()
decl_stmt|;
name|prop2
operator|.
name|setName
argument_list|(
name|Context
operator|.
name|BATCHSIZE
argument_list|)
expr_stmt|;
name|prop2
operator|.
name|setValue
argument_list|(
literal|"12"
argument_list|)
expr_stmt|;
name|addrType
operator|.
name|getJMSNamingProperty
argument_list|()
operator|.
name|add
argument_list|(
name|prop2
argument_list|)
expr_stmt|;
name|Properties
name|env
init|=
name|JMSUtils
operator|.
name|getInitialContextEnv
argument_list|(
name|addrType
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Environment should not be empty"
argument_list|,
name|env
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Environemnt should contain NamingBatchSize property"
argument_list|,
name|env
operator|.
name|get
argument_list|(
name|Context
operator|.
name|BATCHSIZE
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

