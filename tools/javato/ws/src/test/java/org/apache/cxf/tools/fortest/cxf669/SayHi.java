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
name|fortest
operator|.
name|cxf669
package|;
end_package

begin_class
specifier|public
class|class
name|SayHi
block|{
comment|/**      * Describe msg here.      */
specifier|private
name|String
name|msg
decl_stmt|;
comment|/**      * Get the<code>Msg</code> value.      *      * @return a<code>String</code> value      */
specifier|public
specifier|final
name|String
name|getMsg
parameter_list|()
block|{
return|return
name|msg
return|;
block|}
comment|/**      * Set the<code>Msg</code> value.      *      * @param newMsg The new Msg value.      */
specifier|public
specifier|final
name|void
name|setMsg
parameter_list|(
specifier|final
name|String
name|newMsg
parameter_list|)
block|{
name|this
operator|.
name|msg
operator|=
name|newMsg
expr_stmt|;
block|}
block|}
end_class

end_unit

