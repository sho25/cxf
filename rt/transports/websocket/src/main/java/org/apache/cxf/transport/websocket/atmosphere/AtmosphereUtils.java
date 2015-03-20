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
name|websocket
operator|.
name|atmosphere
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
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereInterceptor
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|AtmosphereUtils
block|{
specifier|private
name|AtmosphereUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
name|AtmosphereInterceptor
name|getInterceptor
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|AtmosphereInterceptor
name|ai
init|=
operator|(
name|AtmosphereInterceptor
operator|)
name|bus
operator|.
name|getProperty
argument_list|(
literal|"atmosphere.interceptor"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ai
operator|==
literal|null
condition|)
block|{
name|ai
operator|=
operator|new
name|DefaultProtocolInterceptor
argument_list|()
expr_stmt|;
block|}
return|return
name|ai
return|;
block|}
block|}
end_class

end_unit

