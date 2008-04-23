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
name|systest
operator|.
name|ws
operator|.
name|addressing
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

begin_comment
comment|/**  * Tests the addition of WS-Addressing Message Addressing Properties  * in the non-decoupled case.  */
end_comment

begin_class
specifier|public
class|class
name|NonDecoupledTest
extends|extends
name|MAPTestBase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|CONFIG
init|=
literal|"org/apache/cxf/systest/ws/addressing/wsa_interceptors.xml"
decl_stmt|;
specifier|public
name|String
name|getConfigFileName
parameter_list|()
block|{
return|return
name|CONFIG
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|foo
parameter_list|()
block|{              }
block|}
end_class

end_unit

