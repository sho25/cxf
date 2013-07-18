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
name|sts
operator|.
name|event
package|;
end_package

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractSTSEvent
block|{
specifier|private
name|long
name|duration
decl_stmt|;
specifier|private
specifier|final
name|long
name|timestamp
decl_stmt|;
specifier|private
specifier|final
name|Object
name|source
decl_stmt|;
specifier|public
name|AbstractSTSEvent
parameter_list|(
name|Object
name|source
parameter_list|,
name|long
name|duration
parameter_list|)
block|{
name|this
operator|.
name|duration
operator|=
name|duration
expr_stmt|;
name|this
operator|.
name|timestamp
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
block|}
specifier|public
specifier|abstract
name|String
name|getOperation
parameter_list|()
function_decl|;
specifier|public
name|long
name|getDuration
parameter_list|()
block|{
return|return
name|duration
return|;
block|}
specifier|public
specifier|final
name|long
name|getTimestamp
parameter_list|()
block|{
return|return
name|this
operator|.
name|timestamp
return|;
block|}
specifier|public
specifier|final
name|Object
name|getSource
parameter_list|()
block|{
return|return
name|source
return|;
block|}
block|}
end_class

end_unit

