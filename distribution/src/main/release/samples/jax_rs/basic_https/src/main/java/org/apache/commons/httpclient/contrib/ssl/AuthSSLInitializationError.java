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
name|commons
operator|.
name|httpclient
operator|.
name|contrib
operator|.
name|ssl
package|;
end_package

begin_comment
comment|/**  *<p>  * Signals fatal error in initialization of {@link AuthSSLProtocolSocketFactory}.  *</p>  *   * @author<a href="mailto:oleg@ural.ru">Oleg Kalnichevski</a>  *<p>  *         DISCLAIMER: HttpClient developers DO NOT actively support this  *         component. The component is provided as a reference material, which  *         may be inappropriate for use without additional customization.  *</p>  */
end_comment

begin_class
specifier|public
class|class
name|AuthSSLInitializationError
extends|extends
name|Error
block|{
comment|/**      * Creates a new AuthSSLInitializationError.      */
specifier|public
name|AuthSSLInitializationError
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * Creates a new AuthSSLInitializationError with the specified message.      *       * @param message error message      */
specifier|public
name|AuthSSLInitializationError
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

