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
name|clustering
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|endpoint
operator|.
name|Endpoint
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
name|message
operator|.
name|Exchange
import|;
end_import

begin_comment
comment|/**  * Supports pluggable strategies for alternate endpoint selection on  * failover.  */
end_comment

begin_interface
specifier|public
interface|interface
name|FailoverStrategy
block|{
comment|/**      * Get the alternate endpoints for this invocation.      *       * @param exchange the current Exchange           * @return a failover endpoint if one is available      */
name|List
argument_list|<
name|Endpoint
argument_list|>
name|getAlternateEndpoints
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Select one of the alternate endpoints for a retried invocation.      *       * @param alternates List of alternate endpoints if available      * @return the selected endpoint      */
name|Endpoint
name|selectAlternateEndpoint
parameter_list|(
name|List
argument_list|<
name|Endpoint
argument_list|>
name|alternates
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

