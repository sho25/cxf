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
name|jaxws
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
name|common
operator|.
name|i18n
operator|.
name|Message
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
name|common
operator|.
name|i18n
operator|.
name|UncheckedException
import|;
end_import

begin_comment
comment|/**  * This exception is thrown when CXF discovers inconsistent or unsupported JAX-WS annotations.   */
end_comment

begin_class
specifier|public
class|class
name|JaxWsConfigurationException
extends|extends
name|UncheckedException
block|{
comment|/**      * @param msg      */
specifier|public
name|JaxWsConfigurationException
parameter_list|(
name|Message
name|msg
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param msg      * @param t      */
specifier|public
name|JaxWsConfigurationException
parameter_list|(
name|Message
name|msg
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param cause      */
specifier|public
name|JaxWsConfigurationException
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

