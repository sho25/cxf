begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|interop
operator|.
name|server
package|;
end_package

begin_import
import|import
name|interopbaseaddress
operator|.
name|interop
operator|.
name|EchoDataSet
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|interopbaseaddress
operator|.
name|interop
operator|.
name|EchoDataSetResponse
operator|.
name|EchoDataSetResult
import|;
end_import

begin_import
import|import
name|interopbaseaddress
operator|.
name|interop
operator|.
name|EchoXmlResponse
operator|.
name|EchoXmlResult
import|;
end_import

begin_import
import|import
name|interopbaseaddress
operator|.
name|interop
operator|.
name|IPingService
import|;
end_import

begin_import
import|import
name|interopbaseaddress
operator|.
name|interop
operator|.
name|PingRequest
import|;
end_import

begin_import
import|import
name|interopbaseaddress
operator|.
name|interop
operator|.
name|PingResponse
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|PingService
implements|implements
name|IPingService
block|{
comment|/** {@inheritDoc}*/
specifier|public
name|String
name|echo
parameter_list|(
name|String
name|request
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"echo: "
operator|+
name|request
argument_list|)
expr_stmt|;
return|return
name|request
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|EchoDataSetResult
name|echoDataSet
parameter_list|(
name|Request
name|request
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|EchoXmlResult
name|echoXml
parameter_list|(
name|interopbaseaddress
operator|.
name|interop
operator|.
name|EchoXml
operator|.
name|Request
name|request
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|String
name|fault
parameter_list|(
name|String
name|request
parameter_list|)
block|{
return|return
name|request
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|String
name|header
parameter_list|(
name|String
name|request
parameter_list|)
block|{
return|return
name|request
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|PingResponse
name|ping
parameter_list|(
name|PingRequest
name|parameters
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

