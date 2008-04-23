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
name|configuration
operator|.
name|jsse
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
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
name|configuration
operator|.
name|security
operator|.
name|ClientAuthentication
import|;
end_import

begin_comment
comment|/**  * This class extends {@link TLSParameterBase} with service-specific  * SSL/TLS parameters.  *   */
end_comment

begin_class
specifier|public
class|class
name|TLSServerParameters
extends|extends
name|TLSParameterBase
block|{
name|ClientAuthentication
name|clientAuthentication
decl_stmt|;
comment|/**      * This parameter configures the server side to request and/or      * require client authentication.      */
annotation|@
name|Resource
specifier|public
specifier|final
name|void
name|setClientAuthentication
parameter_list|(
name|ClientAuthentication
name|clientAuth
parameter_list|)
block|{
name|clientAuthentication
operator|=
name|clientAuth
expr_stmt|;
block|}
comment|/**      * This parameter retrieves the client authentication settings.      */
specifier|public
name|ClientAuthentication
name|getClientAuthentication
parameter_list|()
block|{
return|return
name|clientAuthentication
return|;
block|}
block|}
end_class

end_unit

