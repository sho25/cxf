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
name|jms
operator|.
name|uri
package|;
end_package

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|JMSJNDIEndpoint
extends|extends
name|JMSEndpoint
block|{
comment|/**      * @param uri      * @param subject      */
specifier|public
name|JMSJNDIEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|subject
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|JMSURIConstants
operator|.
name|JNDI
argument_list|,
name|subject
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

