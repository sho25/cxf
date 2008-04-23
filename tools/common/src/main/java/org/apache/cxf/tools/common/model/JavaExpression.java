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
name|tools
operator|.
name|common
operator|.
name|model
package|;
end_package

begin_class
specifier|public
class|class
name|JavaExpression
block|{
comment|/**      * Describe value here.      */
specifier|private
name|String
name|value
decl_stmt|;
comment|/**      * Get the<code>Value</code> value.      *      * @return a<code>String</code> value      */
specifier|public
specifier|final
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
comment|/**      * Set the<code>Value</code> value.      *      * @param newValue The new Value value.      */
specifier|public
specifier|final
name|void
name|setValue
parameter_list|(
specifier|final
name|String
name|newValue
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|newValue
expr_stmt|;
block|}
specifier|public
specifier|final
name|String
name|toString
parameter_list|()
block|{
return|return
name|getValue
argument_list|()
operator|+
literal|";"
return|;
block|}
block|}
end_class

end_unit

