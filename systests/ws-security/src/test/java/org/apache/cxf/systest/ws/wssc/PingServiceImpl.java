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
name|wssc
package|;
end_package

begin_import
import|import
name|org
operator|.
name|xmlsoap
operator|.
name|ping
operator|.
name|PingResponseBody
import|;
end_import

begin_import
import|import
name|wssec
operator|.
name|wssc
operator|.
name|IPingService
import|;
end_import

begin_import
import|import
name|wssec
operator|.
name|wssc
operator|.
name|PingRequest
import|;
end_import

begin_import
import|import
name|wssec
operator|.
name|wssc
operator|.
name|PingResponse
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|PingServiceImpl
implements|implements
name|IPingService
block|{
specifier|public
name|String
name|echo
parameter_list|(
name|String
name|request
parameter_list|)
block|{
return|return
name|request
return|;
block|}
specifier|public
name|PingResponse
name|ping
parameter_list|(
name|PingRequest
name|parameters
parameter_list|)
block|{
name|PingResponse
name|resp
init|=
operator|new
name|PingResponse
argument_list|()
decl_stmt|;
name|PingResponseBody
name|body
init|=
operator|new
name|PingResponseBody
argument_list|()
decl_stmt|;
name|body
operator|.
name|setOrigin
argument_list|(
literal|"CXF"
argument_list|)
expr_stmt|;
name|body
operator|.
name|setScenario
argument_list|(
name|parameters
operator|.
name|getPing
argument_list|()
operator|.
name|getScenario
argument_list|()
argument_list|)
expr_stmt|;
name|body
operator|.
name|setText
argument_list|(
name|parameters
operator|.
name|getPing
argument_list|()
operator|.
name|getOrigin
argument_list|()
operator|+
literal|" : "
operator|+
name|parameters
operator|.
name|getPing
argument_list|()
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|resp
operator|.
name|setPingResponse
argument_list|(
name|body
argument_list|)
expr_stmt|;
return|return
name|resp
return|;
block|}
block|}
end_class

end_unit

