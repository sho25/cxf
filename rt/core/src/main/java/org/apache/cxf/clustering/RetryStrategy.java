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
comment|/**  * This strategy simply retries the invocation using the same Endpoint (CXF-2036).  *   * @author Dennis Kieselhorst  *  */
end_comment

begin_class
specifier|public
class|class
name|RetryStrategy
extends|extends
name|SequentialStrategy
block|{
comment|/* (non-Javadoc)      * @see org.apache.cxf.clustering.AbstractStaticFailoverStrategy#getAlternateEndpoints(      * org.apache.cxf.message.Exchange)      */
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Endpoint
argument_list|>
name|getAlternateEndpoints
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getEndpoints
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|)
return|;
block|}
block|}
end_class

end_unit

