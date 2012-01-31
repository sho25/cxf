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
name|jaxrs
operator|.
name|ext
operator|.
name|provider
operator|.
name|atom
package|;
end_package

begin_comment
comment|/**  * A callback-style provider which can be used to map an object to Atom Entry  * without having to deal directly with types representing Atom entries  *   * @param<T> Type of objects which will be mapped to entries  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractEntryBuilder
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AbstractAtomElementBuilder
argument_list|<
name|T
argument_list|>
block|{
comment|/**      *       * @param pojo Object which is being mapped      * @return element publication date      */
specifier|public
name|String
name|getPublished
parameter_list|(
name|T
name|pojo
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
comment|/**      *       * @param pojo Object which is being mapped      * @return element summary      */
specifier|public
name|String
name|getSummary
parameter_list|(
name|T
name|pojo
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

