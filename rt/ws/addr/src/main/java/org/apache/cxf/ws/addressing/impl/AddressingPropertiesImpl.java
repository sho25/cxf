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
name|ws
operator|.
name|addressing
operator|.
name|impl
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
name|ws
operator|.
name|addressing
operator|.
name|AddressingProperties
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
name|ws
operator|.
name|addressing
operator|.
name|Names
import|;
end_import

begin_comment
comment|/**  * Abstraction of Message Addressing Properties.   */
end_comment

begin_class
specifier|public
class|class
name|AddressingPropertiesImpl
extends|extends
name|AddressingProperties
block|{
comment|/**      * Constructor, defaults to 2005/08 namespace.      */
specifier|public
name|AddressingPropertiesImpl
parameter_list|()
block|{
name|this
argument_list|(
name|Names
operator|.
name|WSA_NAMESPACE_NAME
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param uri the namespace URI      */
specifier|public
name|AddressingPropertiesImpl
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

