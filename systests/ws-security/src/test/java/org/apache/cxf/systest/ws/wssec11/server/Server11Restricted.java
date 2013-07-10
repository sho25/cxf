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
name|wssec11
operator|.
name|server
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
name|BusFactory
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
name|spring
operator|.
name|SpringBusFactory
import|;
end_import

begin_class
specifier|public
class|class
name|Server11Restricted
extends|extends
name|AbstractServerRestricted
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|Server11
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|Server11Restricted
parameter_list|()
throws|throws
name|Exception
block|{
name|super
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Server11Restricted
parameter_list|(
name|String
name|baseUrl
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|baseUrl
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|Bus
name|busLocal
init|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/systest/ws/wssec11/server.xml"
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|busLocal
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
name|busLocal
argument_list|)
expr_stmt|;
try|try
block|{
operator|new
name|Server11Restricted
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

