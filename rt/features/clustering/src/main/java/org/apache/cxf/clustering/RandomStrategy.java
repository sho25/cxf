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
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_comment
comment|/**  * Failover strategy based on a randomized walk through the  * static cluster represented by multiple endpoints associated  * with the same service instance.  */
end_comment

begin_class
specifier|public
class|class
name|RandomStrategy
extends|extends
name|AbstractStaticFailoverStrategy
block|{
specifier|private
name|Random
name|random
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|RandomStrategy
parameter_list|()
block|{
name|random
operator|=
operator|new
name|Random
argument_list|()
expr_stmt|;
block|}
comment|/**      * Get next alternate endpoint.      *      * @param alternates non-empty List of alternate endpoints      * @return      */
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getNextAlternate
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|alternates
parameter_list|)
block|{
return|return
name|alternates
operator|.
name|remove
argument_list|(
name|random
operator|.
name|nextInt
argument_list|(
name|alternates
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

