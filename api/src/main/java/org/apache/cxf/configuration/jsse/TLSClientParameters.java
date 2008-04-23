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

begin_comment
comment|/**  * This class extends {@link TLSParameterBase} with client-specific  * SSL/TLS parameters.  *   */
end_comment

begin_class
specifier|public
class|class
name|TLSClientParameters
extends|extends
name|TLSParameterBase
block|{
specifier|private
name|boolean
name|disableCNCheck
decl_stmt|;
comment|/**      * Set whether or not JSEE should omit checking if the host name      * specified in the URL matches that of the Common Name      * (CN) on the server's certificate. Default is false;        * this attribute should not be set to true during production use.      */
specifier|public
name|void
name|setDisableCNCheck
parameter_list|(
name|boolean
name|disableCNCheck
parameter_list|)
block|{
name|this
operator|.
name|disableCNCheck
operator|=
name|disableCNCheck
expr_stmt|;
block|}
comment|/**      * Returns whether or not JSSE omits checking if the      * host name specified in the URL matches that of the Common Name      * (CN) on the server's certificate.      */
specifier|public
name|boolean
name|isDisableCNCheck
parameter_list|()
block|{
return|return
name|disableCNCheck
return|;
block|}
block|}
end_class

end_unit

